package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
// CraftBukkit end

public class TileEntityHopper extends TileEntityLockableLoot implements IHopper, ITickable {

    private NonNullList<ItemStack> inventory;
    private int transferCooldown;
    private long tickedGameTime;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.inventory;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityHopper() {
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        this.transferCooldown = -1;
    }

    public static void registerFixesHopper(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityHopper.class, new String[] { "Items"})));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbttagcompound)) {
            ItemStackHelper.loadAllItems(nbttagcompound, this.inventory);
        }

        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.customName = nbttagcompound.getString("CustomName");
        }

        this.transferCooldown = nbttagcompound.getInteger("TransferCooldown");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (!this.checkLootAndWrite(nbttagcompound)) {
            ItemStackHelper.saveAllItems(nbttagcompound, this.inventory);
        }

        nbttagcompound.setInteger("TransferCooldown", this.transferCooldown);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.customName);
        }

        return nbttagcompound;
    }

    public int getSizeInventory() {
        return this.inventory.size();
    }

    public ItemStack decrStackSize(int i, int j) {
        this.fillWithLoot((EntityPlayer) null);
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.getItems(), i, j);

        return itemstack;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.fillWithLoot((EntityPlayer) null);
        this.getItems().set(i, itemstack);
        if (itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }

    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.hopper";
    }

    public int getInventoryStackLimit() {
        return maxStack; // CraftBukkit
    }

    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.transferCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();
            if (!this.isOnTransferCooldown()) {
                this.setTransferCooldown(0);
                // Spigot start
                if (!this.updateHopper() && this.world.spigotConfig.hopperCheck > 1) {
                    this.setTransferCooldown(this.world.spigotConfig.hopperCheck);
                }
                // Spigot end
            }

        }
    }

    private boolean updateHopper() {
        mayAcceptItems = false; // Paper - at the beginning of a tick, assume we can't accept items
        if (this.world != null && !this.world.isRemote) {
            if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata())) {
                boolean flag = false;

                if (!this.isInventoryEmpty()) {
                    flag = this.transferItemsOut();
                }

                if (!this.isFull()) {
                    mayAcceptItems = true; // Paper - flag this hopper to be able to accept items
                    flag = pullItems((IHopper) this) || flag;
                }

                if (flag) {
                    this.setTransferCooldown(world.spigotConfig.hopperTransfer); // Spigot
                    this.markDirty();
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    // Paper start
    private boolean mayAcceptItems = false;

    public boolean canAcceptItems() {
        return mayAcceptItems;
    }
    // Paper end

    private boolean isInventoryEmpty() {
        Iterator iterator = this.inventory.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public boolean isEmpty() {
        return this.isInventoryEmpty();
    }

    private boolean isFull() {
        Iterator iterator = this.inventory.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (!itemstack.isEmpty() && itemstack.getCount() == itemstack.getMaxStackSize());

        return false;
    }

    // Paper start - Optimize Hoppers
    private static boolean skipPullModeEventFire = false;
    private static boolean skipPushModeEventFire = false;
    public static boolean skipHopperEvents = false;

    private boolean hopperPush(IInventory iinventory, EnumFacing enumdirection) {
        skipPushModeEventFire = skipHopperEvents;
        boolean foundItem = false;
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (!this.getStackInSlot(i).isEmpty()) {
                foundItem = true;
                ItemStack origItemStack = this.getStackInSlot(i);
                ItemStack itemstack = origItemStack;

                final int origCount = origItemStack.getCount();
                final int moved = Math.min(world.spigotConfig.hopperAmount, origCount);
                origItemStack.setCount(moved);

                // We only need to fire the event once to give protection plugins a chance to cancel this event
                // Because nothing uses getItem, every event call should end up the same result.
                if (!skipPushModeEventFire) {
                    itemstack = callPushMoveEvent(iinventory, itemstack);
                    if (itemstack == null) { // cancelled
                        origItemStack.setCount(origCount);
                        return false;
                    }
                }
                final ItemStack itemstack2 = putStackInInventoryAllSlots(this, iinventory, itemstack, enumdirection);
                final int remaining = itemstack2.getCount();
                if (remaining != moved) {
                    origItemStack = origItemStack.copy();
                    origItemStack.setCount(origCount - moved + remaining);
                    this.setInventorySlotContents(i, origItemStack);
                    iinventory.markDirty();
                    return true;
                }
                origItemStack.setCount(origCount);
            }
        }
        if (foundItem && world.paperConfig.cooldownHopperWhenFull) { // Inventory was full - cooldown
            this.setTransferCooldown(world.spigotConfig.hopperTransfer);
        }
        return false;
    }

    private static boolean hopperPull(IHopper ihopper, IInventory iinventory, int i) {
        ItemStack origItemStack = iinventory.getStackInSlot(i);
        ItemStack itemstack = origItemStack;
        final int origCount = origItemStack.getCount();
        final World world = ihopper.getWorld();
        final int moved = Math.min(world.spigotConfig.hopperAmount, origCount);
        itemstack.setCount(moved);

        if (!skipPullModeEventFire) {
            itemstack = callPullMoveEvent(ihopper, iinventory, itemstack);
            if (itemstack == null) { // cancelled
                origItemStack.setCount(origCount);
                // Drastically improve performance by returning true.
                // No plugin could of relied on the behavior of false as the other call
                // site for IMIE did not exhibit the same behavior
                return true;
            }
        }

        final ItemStack itemstack2 = putStackInInventoryAllSlots(iinventory, ihopper, itemstack, null);
        final int remaining = itemstack2.getCount();
        if (remaining != moved) {
            origItemStack = origItemStack.copy();
            origItemStack.setCount(origCount - moved + remaining);
            IGNORE_TILE_UPDATES = true;
            iinventory.setInventorySlotContents(i, origItemStack);
            IGNORE_TILE_UPDATES = false;
            iinventory.markDirty();
            return true;
        }
        origItemStack.setCount(origCount);

        if (world.paperConfig.cooldownHopperWhenFull) {
            cooldownHopper(ihopper);
        }

        return false;
    }

    private ItemStack callPushMoveEvent(IInventory iinventory, ItemStack itemstack) {
        Inventory destinationInventory = getInventory(iinventory);
        InventoryMoveItemEvent event = new InventoryMoveItemEvent(this.getOwner(false).getInventory(),
                CraftItemStack.asCraftMirror(itemstack), destinationInventory, true);
        boolean result = event.callEvent();
        if (!event.calledGetItem && !event.calledSetItem) {
            skipPushModeEventFire = true;
        }
        if (!result) {
            cooldownHopper(this);
            return null;
        }

        if (event.calledSetItem) {
            return CraftItemStack.asNMSCopy(event.getItem());
        } else {
            return itemstack;
        }
    }

    private static ItemStack callPullMoveEvent(IHopper hopper, IInventory iinventory, ItemStack itemstack) {
        Inventory sourceInventory = getInventory(iinventory);
        Inventory destination = getInventory(hopper);

        InventoryMoveItemEvent event = new InventoryMoveItemEvent(sourceInventory,
                // Mirror is safe as we no plugins ever use this item
                CraftItemStack.asCraftMirror(itemstack), destination, false);
        boolean result = event.callEvent();
        if (!event.calledGetItem && !event.calledSetItem) {
            skipPullModeEventFire = true;
        }
        if (!result) {
            cooldownHopper(hopper);
            return null;
        }

        if (event.calledSetItem) {
            return CraftItemStack.asNMSCopy(event.getItem());
        } else {
            return itemstack;
        }
    }

    private static Inventory getInventory(IInventory iinventory) {
        Inventory sourceInventory;// Have to special case large chests as they work oddly
        if (iinventory instanceof InventoryLargeChest) {
            sourceInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
        } else if (iinventory instanceof TileEntity) {
            sourceInventory = ((TileEntity) iinventory).getOwner(false).getInventory();
        } else {
            sourceInventory = iinventory.getOwner().getInventory();
        }
        return sourceInventory;
    }

    private static void cooldownHopper(IHopper hopper) {
        if (hopper instanceof TileEntityHopper) {
            ((TileEntityHopper) hopper).setTransferCooldown(hopper.getWorld().spigotConfig.hopperTransfer);
        } else if (hopper instanceof EntityMinecartHopper) {
            ((EntityMinecartHopper) hopper).setTransferTicker(hopper.getWorld().spigotConfig.hopperTransfer / 2);
        }
    }

    // Paper end
    private boolean transferItemsOut() {
        IInventory iinventory = this.getInventoryForHopperTransfer();

        if (iinventory == null) {
            return false;
        } else {
            EnumFacing enumdirection = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();

            if (this.isInventoryFull(iinventory, enumdirection)) {
                return false;
            } else {
                return hopperPush(iinventory, enumdirection); /* // Paper - disable rest
                for (int i = 0; i < this.getSize(); ++i) {
                    if (!this.getItem(i).isEmpty()) {
                        ItemStack itemstack = this.getItem(i).cloneItemStack();
                        // ItemStack itemstack1 = addItem(this, iinventory, this.splitStack(i, 1), enumdirection);

                        // CraftBukkit start - Call event when pushing items into other inventories
                        CraftItemStack oitemstack = CraftItemStack.asCraftMirror(this.splitStack(i, world.spigotConfig.hopperAmount)); // Spigot

                        Inventory destinationInventory;
                        // Have to special case large chests as they work oddly
                        if (iinventory instanceof InventoryLargeChest) {
                            destinationInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
                        } else {
                            destinationInventory = iinventory.getOwner().getInventory();
                        }

                        InventoryMoveItemEvent event = new InventoryMoveItemEvent(this.getOwner().getInventory(), oitemstack.clone(), destinationInventory, true);
                        this.getWorld().getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            this.setItem(i, itemstack);
                            this.setCooldown(world.spigotConfig.hopperTransfer); // Spigot
                            return false;
                        }
                        int origCount = event.getItem().getAmount(); // Spigot
                        ItemStack itemstack1 = addItem(this, iinventory, CraftItemStack.asNMSCopy(event.getItem()), enumdirection);

                        if (itemstack1.isEmpty()) {
                            if (event.getItem().equals(oitemstack)) {
                                iinventory.update();
                            } else {
                                this.setItem(i, itemstack);
                            }
                            // CraftBukkit end
                            return true;
                        }

                        itemstack.subtract(origCount - itemstack1.getCount()); // Spigot
                        this.setItem(i, itemstack);
                    }
                }

                return false;*/ // Paper - end commenting out replaced block for Hopper Optimizations
            }
        }
    }

    private boolean isInventoryFull(IInventory iinventory, EnumFacing enumdirection) {
        if (iinventory instanceof ISidedInventory) {
            ISidedInventory iworldinventory = (ISidedInventory) iinventory;
            int[] aint = iworldinventory.getSlotsForFace(enumdirection);
            int[] aint1 = aint;
            int i = aint.length;

            for (int j = 0; j < i; ++j) {
                int k = aint1[j];
                ItemStack itemstack = iworldinventory.getStackInSlot(k);

                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                    return false;
                }
            }
        } else {
            int l = iinventory.getSizeInventory();

            for (int i1 = 0; i1 < l; ++i1) {
                ItemStack itemstack1 = iinventory.getStackInSlot(i1);

                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isInventoryEmpty(IInventory iinventory, EnumFacing enumdirection) {
        if (iinventory instanceof ISidedInventory) {
            ISidedInventory iworldinventory = (ISidedInventory) iinventory;
            int[] aint = iworldinventory.getSlotsForFace(enumdirection);
            int[] aint1 = aint;
            int i = aint.length;

            for (int j = 0; j < i; ++j) {
                int k = aint1[j];

                if (!iworldinventory.getStackInSlot(k).isEmpty()) {
                    return false;
                }
            }
        } else {
            int l = iinventory.getSizeInventory();

            for (int i1 = 0; i1 < l; ++i1) {
                if (!iinventory.getStackInSlot(i1).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    // Paper start - split methods, and only do entity lookup if in pull mode
    public static boolean pullItems(IHopper ihopper) {
        IInventory iinventory = getInventory(ihopper, !(ihopper instanceof TileEntityHopper) || !ihopper.getWorld().paperConfig.isHopperPushBased);

        return acceptItem(ihopper, iinventory);
    }

    public static boolean acceptItem(IHopper ihopper, IInventory iinventory) {
        // Paper end

        if (iinventory != null) {
            EnumFacing enumdirection = EnumFacing.DOWN;

            if (isInventoryEmpty(iinventory, enumdirection)) {
                return false;
            }
            skipPullModeEventFire = skipHopperEvents; // Paper

            if (iinventory instanceof ISidedInventory) {
                ISidedInventory iworldinventory = (ISidedInventory) iinventory;
                int[] aint = iworldinventory.getSlotsForFace(enumdirection);
                int[] aint1 = aint;
                int i = aint.length;

                for (int j = 0; j < i; ++j) {
                    int k = aint1[j];

                    if (pullItemFromSlot(ihopper, iinventory, k, enumdirection)) {
                        return true;
                    }
                }
            } else {
                int l = iinventory.getSizeInventory();

                for (int i1 = 0; i1 < l; ++i1) {
                    if (pullItemFromSlot(ihopper, iinventory, i1, enumdirection)) {
                        return true;
                    }
                }
            }
        } else if (!ihopper.getWorld().paperConfig.isHopperPushBased || !(ihopper instanceof TileEntityHopper)) { // Paper - only search for entities in 'pull mode'
            Iterator iterator = getCaptureItems(ihopper.getWorld(), ihopper.getXPos(), ihopper.getYPos(), ihopper.getZPos()).iterator(); // Change getHopperLookupBoundingBox() if this ever changes

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                if (putDropInInventoryAllSlots((IInventory) null, ihopper, entityitem)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean pullItemFromSlot(IHopper ihopper, IInventory iinventory, int i, EnumFacing enumdirection) {
        ItemStack itemstack = iinventory.getStackInSlot(i);

        if (!itemstack.isEmpty() && canExtractItemFromSlot(iinventory, itemstack, i, enumdirection)) {
            return hopperPull(ihopper, iinventory, i); /* // Paper - disable rest
            ItemStack itemstack1 = itemstack.cloneItemStack();
            // ItemStack itemstack2 = addItem(iinventory, ihopper, iinventory.splitStack(i, 1), (EnumDirection) null);
            // CraftBukkit start - Call event on collection of items from inventories into the hopper
            CraftItemStack oitemstack = CraftItemStack.asCraftMirror(iinventory.splitStack(i, ihopper.getWorld().spigotConfig.hopperAmount)); // Spigot

            Inventory sourceInventory;
            // Have to special case large chests as they work oddly
            if (iinventory instanceof InventoryLargeChest) {
                sourceInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
            } else {
                sourceInventory = iinventory.getOwner().getInventory();
            }

            InventoryMoveItemEvent event = new InventoryMoveItemEvent(sourceInventory, oitemstack.clone(), ihopper.getOwner().getInventory(), false);

            ihopper.getWorld().getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                iinventory.setItem(i, itemstack1);

                if (ihopper instanceof TileEntityHopper) {
                    ((TileEntityHopper) ihopper).setCooldown(ihopper.getWorld().spigotConfig.hopperTransfer); // Spigot
                } else if (ihopper instanceof EntityMinecartHopper) {
                    ((EntityMinecartHopper) ihopper).setCooldown(ihopper.getWorld().spigotConfig.hopperTransfer / 2); // Spigot
                }
                return false;
            }
            int origCount = event.getItem().getAmount(); // Spigot
            ItemStack itemstack2 = addItem(iinventory, ihopper, CraftItemStack.asNMSCopy(event.getItem()), null);

            if (itemstack2.isEmpty()) {
                if (event.getItem().equals(oitemstack)) {
                    iinventory.update();
                } else {
                    iinventory.setItem(i, itemstack1);
                }
                // CraftBukkit end
                return true;
            }

            itemstack1.subtract(origCount - itemstack2.getCount()); // Spigot
            iinventory.setItem(i, itemstack1);*/ // Paper - end commenting out replaced block for Hopper Optimizations
        }

        return false;
    }

    public static boolean putDropInInventory(IInventory iinventory, IInventory iinventory1, EntityItem entityitem) { return putDropInInventoryAllSlots(iinventory, iinventory1, entityitem); } // Paper - OBFHELPER
    public static boolean putDropInInventoryAllSlots(IInventory iinventory, IInventory iinventory1, EntityItem entityitem) {
        boolean flag = false;

        if (entityitem == null) {
            return false;
        } else {
            // CraftBukkit start
            InventoryPickupItemEvent event = new InventoryPickupItemEvent(getInventory(iinventory1), (org.bukkit.entity.Item) entityitem.getBukkitEntity()); // Paper - avoid snapshot creation
            entityitem.world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end
            ItemStack itemstack = entityitem.getItem().copy();
            ItemStack itemstack1 = putStackInInventoryAllSlots(iinventory, iinventory1, itemstack, (EnumFacing) null);

            if (itemstack1.isEmpty()) {
                flag = true;
                entityitem.setDead();
            } else {
                entityitem.setItem(itemstack1);
            }

            return flag;
        }
    }

    public static ItemStack putStackInInventoryAllSlots(IInventory iinventory, IInventory iinventory1, ItemStack itemstack, @Nullable EnumFacing enumdirection) {
        if (iinventory1 instanceof ISidedInventory && enumdirection != null) {
            ISidedInventory iworldinventory = (ISidedInventory) iinventory1;
            int[] aint = iworldinventory.getSlotsForFace(enumdirection);

            for (int i = 0; i < aint.length && !itemstack.isEmpty(); ++i) {
                itemstack = insertStack(iinventory, iinventory1, itemstack, aint[i], enumdirection);
            }
        } else {
            int j = iinventory1.getSizeInventory();

            for (int k = 0; k < j && !itemstack.isEmpty(); ++k) {
                itemstack = insertStack(iinventory, iinventory1, itemstack, k, enumdirection);
            }
        }

        return itemstack;
    }

    private static boolean canInsertItemInSlot(IInventory iinventory, ItemStack itemstack, int i, EnumFacing enumdirection) {
        return !iinventory.isItemValidForSlot(i, itemstack) ? false : !(iinventory instanceof ISidedInventory) || ((ISidedInventory) iinventory).canInsertItem(i, itemstack, enumdirection);
    }

    private static boolean canExtractItemFromSlot(IInventory iinventory, ItemStack itemstack, int i, EnumFacing enumdirection) {
        return !(iinventory instanceof ISidedInventory) || ((ISidedInventory) iinventory).canExtractItem(i, itemstack, enumdirection);
    }

    private static ItemStack insertStack(IInventory iinventory, IInventory iinventory1, ItemStack itemstack, int i, EnumFacing enumdirection) {
        ItemStack itemstack1 = iinventory1.getStackInSlot(i);

        if (canInsertItemInSlot(iinventory1, itemstack, i, enumdirection)) {
            boolean flag = false;
            boolean flag1 = iinventory1.isEmpty();

            if (itemstack1.isEmpty()) {
                IGNORE_TILE_UPDATES = true; // Paper
                iinventory1.setInventorySlotContents(i, itemstack);
                IGNORE_TILE_UPDATES = false; // Paper
                itemstack = ItemStack.EMPTY;
                flag = true;
            } else if (canCombine(itemstack1, itemstack)) {
                int j = itemstack.getMaxStackSize() - itemstack1.getCount();
                int k = Math.min(itemstack.getCount(), j);

                itemstack.shrink(k);
                itemstack1.grow(k);
                flag = k > 0;
            }

            if (flag) {
                if (flag1 && iinventory1 instanceof TileEntityHopper) {
                    TileEntityHopper tileentityhopper = (TileEntityHopper) iinventory1;

                    if (!tileentityhopper.mayTransfer()) {
                        byte b0 = 0;

                        if (iinventory != null && iinventory instanceof TileEntityHopper) {
                            TileEntityHopper tileentityhopper1 = (TileEntityHopper) iinventory;

                            if (tileentityhopper.tickedGameTime >= tileentityhopper1.tickedGameTime) {
                                b0 = 1;
                            }
                        }

                        tileentityhopper.setTransferCooldown(tileentityhopper.world.spigotConfig.hopperTransfer - b0); // Spigot
                    }
                }

                iinventory1.markDirty();
            }
        }

        return itemstack;
    }

    private IInventory getInventoryForHopperTransfer() {
        EnumFacing enumdirection = BlockHopper.getFacing(this.getBlockMetadata());

        // Paper start - don't search for entities in push mode
        World world = getWorld();
        return getInventory(world, this.getXPos() + (double) enumdirection.getFrontOffsetX(), this.getYPos() + (double) enumdirection.getFrontOffsetY(), this.getZPos() + (double) enumdirection.getFrontOffsetZ(), !world.paperConfig.isHopperPushBased);
        // Paper end
    }

    // Paper start - add option to search for entities
    public static IInventory getSourceInventory(IHopper hopper) {
        return getInventory(hopper, true);
    }

    public static IInventory getInventory(IHopper ihopper, boolean searchForEntities) {
        return getInventory(ihopper.getWorld(), ihopper.getXPos(), ihopper.getYPos() + 1.0D, ihopper.getZPos(), searchForEntities);
        // Paper end
    }

    public static List<EntityItem> getCaptureItems(World world, double d0, double d1, double d2) {
        return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(d0 - 0.5D, d1, d2 - 0.5D, d0 + 0.5D, d1 + 1.5D, d2 + 0.5D), EntitySelectors.IS_ALIVE); // Change getHopperLookupBoundingBox(double, double, double) if the bounding box calculation is ever changed
    }

    // Paper start
    public AxisAlignedBB getHopperLookupBoundingBox() {
        return getHopperLookupBoundingBox(this.getX(), this.getY(), this.getZ());
    }

    private static AxisAlignedBB getHopperLookupBoundingBox(double d0, double d1, double d2) {
        // Change this if a(World, double, double, double) above ever changes
        return new AxisAlignedBB(d0 - 0.5D, d1, d2 - 0.5D, d0 + 0.5D, d1 + 1.5D, d2 + 0.5D);
    }
    // Paper end

    // Paper start - add option to searchForEntities
    public static IInventory getInventoryAtPosition(World world, double d0, double d1, double d2) {
        return getInventory(world, d0, d1, d2, true);
    }

    public static IInventory getInventory(World world, double d0, double d1, double d2, boolean searchForEntities) {
        // Paper end
        Object object = null;
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);
        BlockPos blockposition = new BlockPos(i, j, k);
        if ( !world.isBlockLoaded( blockposition ) ) return null; // Spigot
        Block block = world.getBlockState(blockposition).getBlock();

        if (block.hasTileEntity()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof IInventory) {
                object = (IInventory) tileentity;
                if (object instanceof TileEntityChest && block instanceof BlockChest) {
                    object = ((BlockChest) block).getContainer(world, blockposition, true);
                }
            }
        }

        if (object == null && searchForEntities) { // Paper - only if searchForEntities
            List list = world.getEntitiesInAABBexcluding((Entity) null, new AxisAlignedBB(d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, d0 + 0.5D, d1 + 0.5D, d2 + 0.5D), EntitySelectors.HAS_INVENTORY);

            if (!list.isEmpty()) {
                object = (IInventory) list.get(world.rand.nextInt(list.size()));
            }
        }

        return (IInventory) object;
    }

    private static boolean canCombine(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.getItem() != itemstack1.getItem() ? false : (itemstack.getMetadata() != itemstack1.getMetadata() ? false : (itemstack.getCount() > itemstack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(itemstack, itemstack1)));
    }

    public double getXPos() {
        return (double) this.pos.getX() + 0.5D;
    }

    public double getYPos() {
        return (double) this.pos.getY() + 0.5D;
    }

    public double getZPos() {
        return (double) this.pos.getZ() + 0.5D;
    }

    private void setTransferCooldown(int i) {
        this.transferCooldown = i;
    }

    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    private boolean mayTransfer() {
        return this.transferCooldown > 8;
    }

    public String getGuiID() {
        return "minecraft:hopper";
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.fillWithLoot(entityhuman);
        return new ContainerHopper(playerinventory, this, entityhuman);
    }

    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }
}

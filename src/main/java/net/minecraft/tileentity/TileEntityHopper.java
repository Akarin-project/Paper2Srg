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

    private NonNullList<ItemStack> field_145900_a;
    private int field_145901_j;
    private long field_190578_g;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.field_145900_a;
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
        this.field_145900_a = NonNullList.func_191197_a(5, ItemStack.field_190927_a);
        this.field_145901_j = -1;
    }

    public static void func_189683_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityHopper.class, new String[] { "Items"})));
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145900_a = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
        if (!this.func_184283_b(nbttagcompound)) {
            ItemStackHelper.func_191283_b(nbttagcompound, this.field_145900_a);
        }

        if (nbttagcompound.func_150297_b("CustomName", 8)) {
            this.field_190577_o = nbttagcompound.func_74779_i("CustomName");
        }

        this.field_145901_j = nbttagcompound.func_74762_e("TransferCooldown");
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        if (!this.func_184282_c(nbttagcompound)) {
            ItemStackHelper.func_191282_a(nbttagcompound, this.field_145900_a);
        }

        nbttagcompound.func_74768_a("TransferCooldown", this.field_145901_j);
        if (this.func_145818_k_()) {
            nbttagcompound.func_74778_a("CustomName", this.field_190577_o);
        }

        return nbttagcompound;
    }

    public int func_70302_i_() {
        return this.field_145900_a.size();
    }

    public ItemStack func_70298_a(int i, int j) {
        this.func_184281_d((EntityPlayer) null);
        ItemStack itemstack = ItemStackHelper.func_188382_a(this.func_190576_q(), i, j);

        return itemstack;
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        this.func_184281_d((EntityPlayer) null);
        this.func_190576_q().set(i, itemstack);
        if (itemstack.func_190916_E() > this.func_70297_j_()) {
            itemstack.func_190920_e(this.func_70297_j_());
        }

    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_190577_o : "container.hopper";
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    public void func_73660_a() {
        if (this.field_145850_b != null && !this.field_145850_b.field_72995_K) {
            --this.field_145901_j;
            this.field_190578_g = this.field_145850_b.func_82737_E();
            if (!this.func_145888_j()) {
                this.func_145896_c(0);
                // Spigot start
                if (!this.func_145887_i() && this.field_145850_b.spigotConfig.hopperCheck > 1) {
                    this.func_145896_c(this.field_145850_b.spigotConfig.hopperCheck);
                }
                // Spigot end
            }

        }
    }

    private boolean func_145887_i() {
        mayAcceptItems = false; // Paper - at the beginning of a tick, assume we can't accept items
        if (this.field_145850_b != null && !this.field_145850_b.field_72995_K) {
            if (!this.func_145888_j() && BlockHopper.func_149917_c(this.func_145832_p())) {
                boolean flag = false;

                if (!this.func_152104_k()) {
                    flag = this.func_145883_k();
                }

                if (!this.func_152105_l()) {
                    mayAcceptItems = true; // Paper - flag this hopper to be able to accept items
                    flag = func_145891_a((IHopper) this) || flag;
                }

                if (flag) {
                    this.func_145896_c(field_145850_b.spigotConfig.hopperTransfer); // Spigot
                    this.func_70296_d();
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

    private boolean func_152104_k() {
        Iterator iterator = this.field_145900_a.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public boolean func_191420_l() {
        return this.func_152104_k();
    }

    private boolean func_152105_l() {
        Iterator iterator = this.field_145900_a.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (!itemstack.func_190926_b() && itemstack.func_190916_E() == itemstack.func_77976_d());

        return false;
    }

    // Paper start - Optimize Hoppers
    private static boolean skipPullModeEventFire = false;
    private static boolean skipPushModeEventFire = false;
    public static boolean skipHopperEvents = false;

    private boolean hopperPush(IInventory iinventory, EnumFacing enumdirection) {
        skipPushModeEventFire = skipHopperEvents;
        boolean foundItem = false;
        for (int i = 0; i < this.func_70302_i_(); ++i) {
            if (!this.func_70301_a(i).func_190926_b()) {
                foundItem = true;
                ItemStack origItemStack = this.func_70301_a(i);
                ItemStack itemstack = origItemStack;

                final int origCount = origItemStack.func_190916_E();
                final int moved = Math.min(field_145850_b.spigotConfig.hopperAmount, origCount);
                origItemStack.func_190920_e(moved);

                // We only need to fire the event once to give protection plugins a chance to cancel this event
                // Because nothing uses getItem, every event call should end up the same result.
                if (!skipPushModeEventFire) {
                    itemstack = callPushMoveEvent(iinventory, itemstack);
                    if (itemstack == null) { // cancelled
                        origItemStack.func_190920_e(origCount);
                        return false;
                    }
                }
                final ItemStack itemstack2 = func_174918_a(this, iinventory, itemstack, enumdirection);
                final int remaining = itemstack2.func_190916_E();
                if (remaining != moved) {
                    origItemStack = origItemStack.func_77946_l();
                    origItemStack.func_190920_e(origCount - moved + remaining);
                    this.func_70299_a(i, origItemStack);
                    iinventory.func_70296_d();
                    return true;
                }
                origItemStack.func_190920_e(origCount);
            }
        }
        if (foundItem && field_145850_b.paperConfig.cooldownHopperWhenFull) { // Inventory was full - cooldown
            this.func_145896_c(field_145850_b.spigotConfig.hopperTransfer);
        }
        return false;
    }

    private static boolean hopperPull(IHopper ihopper, IInventory iinventory, int i) {
        ItemStack origItemStack = iinventory.func_70301_a(i);
        ItemStack itemstack = origItemStack;
        final int origCount = origItemStack.func_190916_E();
        final World world = ihopper.func_145831_w();
        final int moved = Math.min(world.spigotConfig.hopperAmount, origCount);
        itemstack.func_190920_e(moved);

        if (!skipPullModeEventFire) {
            itemstack = callPullMoveEvent(ihopper, iinventory, itemstack);
            if (itemstack == null) { // cancelled
                origItemStack.func_190920_e(origCount);
                // Drastically improve performance by returning true.
                // No plugin could of relied on the behavior of false as the other call
                // site for IMIE did not exhibit the same behavior
                return true;
            }
        }

        final ItemStack itemstack2 = func_174918_a(iinventory, ihopper, itemstack, null);
        final int remaining = itemstack2.func_190916_E();
        if (remaining != moved) {
            origItemStack = origItemStack.func_77946_l();
            origItemStack.func_190920_e(origCount - moved + remaining);
            IGNORE_TILE_UPDATES = true;
            iinventory.func_70299_a(i, origItemStack);
            IGNORE_TILE_UPDATES = false;
            iinventory.func_70296_d();
            return true;
        }
        origItemStack.func_190920_e(origCount);

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
            ((TileEntityHopper) hopper).func_145896_c(hopper.func_145831_w().spigotConfig.hopperTransfer);
        } else if (hopper instanceof EntityMinecartHopper) {
            ((EntityMinecartHopper) hopper).func_98042_n(hopper.func_145831_w().spigotConfig.hopperTransfer / 2);
        }
    }

    // Paper end
    private boolean func_145883_k() {
        IInventory iinventory = this.func_145895_l();

        if (iinventory == null) {
            return false;
        } else {
            EnumFacing enumdirection = BlockHopper.func_176428_b(this.func_145832_p()).func_176734_d();

            if (this.func_174919_a(iinventory, enumdirection)) {
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

    private boolean func_174919_a(IInventory iinventory, EnumFacing enumdirection) {
        if (iinventory instanceof ISidedInventory) {
            ISidedInventory iworldinventory = (ISidedInventory) iinventory;
            int[] aint = iworldinventory.func_180463_a(enumdirection);
            int[] aint1 = aint;
            int i = aint.length;

            for (int j = 0; j < i; ++j) {
                int k = aint1[j];
                ItemStack itemstack = iworldinventory.func_70301_a(k);

                if (itemstack.func_190926_b() || itemstack.func_190916_E() != itemstack.func_77976_d()) {
                    return false;
                }
            }
        } else {
            int l = iinventory.func_70302_i_();

            for (int i1 = 0; i1 < l; ++i1) {
                ItemStack itemstack1 = iinventory.func_70301_a(i1);

                if (itemstack1.func_190926_b() || itemstack1.func_190916_E() != itemstack1.func_77976_d()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean func_174917_b(IInventory iinventory, EnumFacing enumdirection) {
        if (iinventory instanceof ISidedInventory) {
            ISidedInventory iworldinventory = (ISidedInventory) iinventory;
            int[] aint = iworldinventory.func_180463_a(enumdirection);
            int[] aint1 = aint;
            int i = aint.length;

            for (int j = 0; j < i; ++j) {
                int k = aint1[j];

                if (!iworldinventory.func_70301_a(k).func_190926_b()) {
                    return false;
                }
            }
        } else {
            int l = iinventory.func_70302_i_();

            for (int i1 = 0; i1 < l; ++i1) {
                if (!iinventory.func_70301_a(i1).func_190926_b()) {
                    return false;
                }
            }
        }

        return true;
    }

    // Paper start - split methods, and only do entity lookup if in pull mode
    public static boolean func_145891_a(IHopper ihopper) {
        IInventory iinventory = getInventory(ihopper, !(ihopper instanceof TileEntityHopper) || !ihopper.func_145831_w().paperConfig.isHopperPushBased);

        return acceptItem(ihopper, iinventory);
    }

    public static boolean acceptItem(IHopper ihopper, IInventory iinventory) {
        // Paper end

        if (iinventory != null) {
            EnumFacing enumdirection = EnumFacing.DOWN;

            if (func_174917_b(iinventory, enumdirection)) {
                return false;
            }
            skipPullModeEventFire = skipHopperEvents; // Paper

            if (iinventory instanceof ISidedInventory) {
                ISidedInventory iworldinventory = (ISidedInventory) iinventory;
                int[] aint = iworldinventory.func_180463_a(enumdirection);
                int[] aint1 = aint;
                int i = aint.length;

                for (int j = 0; j < i; ++j) {
                    int k = aint1[j];

                    if (func_174915_a(ihopper, iinventory, k, enumdirection)) {
                        return true;
                    }
                }
            } else {
                int l = iinventory.func_70302_i_();

                for (int i1 = 0; i1 < l; ++i1) {
                    if (func_174915_a(ihopper, iinventory, i1, enumdirection)) {
                        return true;
                    }
                }
            }
        } else if (!ihopper.func_145831_w().paperConfig.isHopperPushBased || !(ihopper instanceof TileEntityHopper)) { // Paper - only search for entities in 'pull mode'
            Iterator iterator = func_184292_a(ihopper.func_145831_w(), ihopper.func_96107_aA(), ihopper.func_96109_aB(), ihopper.func_96108_aC()).iterator(); // Change getHopperLookupBoundingBox() if this ever changes

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                if (func_145898_a((IInventory) null, ihopper, entityitem)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean func_174915_a(IHopper ihopper, IInventory iinventory, int i, EnumFacing enumdirection) {
        ItemStack itemstack = iinventory.func_70301_a(i);

        if (!itemstack.func_190926_b() && func_174921_b(iinventory, itemstack, i, enumdirection)) {
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

    public static boolean putDropInInventory(IInventory iinventory, IInventory iinventory1, EntityItem entityitem) { return func_145898_a(iinventory, iinventory1, entityitem); } // Paper - OBFHELPER
    public static boolean func_145898_a(IInventory iinventory, IInventory iinventory1, EntityItem entityitem) {
        boolean flag = false;

        if (entityitem == null) {
            return false;
        } else {
            // CraftBukkit start
            InventoryPickupItemEvent event = new InventoryPickupItemEvent(getInventory(iinventory1), (org.bukkit.entity.Item) entityitem.getBukkitEntity()); // Paper - avoid snapshot creation
            entityitem.field_70170_p.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end
            ItemStack itemstack = entityitem.func_92059_d().func_77946_l();
            ItemStack itemstack1 = func_174918_a(iinventory, iinventory1, itemstack, (EnumFacing) null);

            if (itemstack1.func_190926_b()) {
                flag = true;
                entityitem.func_70106_y();
            } else {
                entityitem.func_92058_a(itemstack1);
            }

            return flag;
        }
    }

    public static ItemStack func_174918_a(IInventory iinventory, IInventory iinventory1, ItemStack itemstack, @Nullable EnumFacing enumdirection) {
        if (iinventory1 instanceof ISidedInventory && enumdirection != null) {
            ISidedInventory iworldinventory = (ISidedInventory) iinventory1;
            int[] aint = iworldinventory.func_180463_a(enumdirection);

            for (int i = 0; i < aint.length && !itemstack.func_190926_b(); ++i) {
                itemstack = func_174916_c(iinventory, iinventory1, itemstack, aint[i], enumdirection);
            }
        } else {
            int j = iinventory1.func_70302_i_();

            for (int k = 0; k < j && !itemstack.func_190926_b(); ++k) {
                itemstack = func_174916_c(iinventory, iinventory1, itemstack, k, enumdirection);
            }
        }

        return itemstack;
    }

    private static boolean func_174920_a(IInventory iinventory, ItemStack itemstack, int i, EnumFacing enumdirection) {
        return !iinventory.func_94041_b(i, itemstack) ? false : !(iinventory instanceof ISidedInventory) || ((ISidedInventory) iinventory).func_180462_a(i, itemstack, enumdirection);
    }

    private static boolean func_174921_b(IInventory iinventory, ItemStack itemstack, int i, EnumFacing enumdirection) {
        return !(iinventory instanceof ISidedInventory) || ((ISidedInventory) iinventory).func_180461_b(i, itemstack, enumdirection);
    }

    private static ItemStack func_174916_c(IInventory iinventory, IInventory iinventory1, ItemStack itemstack, int i, EnumFacing enumdirection) {
        ItemStack itemstack1 = iinventory1.func_70301_a(i);

        if (func_174920_a(iinventory1, itemstack, i, enumdirection)) {
            boolean flag = false;
            boolean flag1 = iinventory1.func_191420_l();

            if (itemstack1.func_190926_b()) {
                IGNORE_TILE_UPDATES = true; // Paper
                iinventory1.func_70299_a(i, itemstack);
                IGNORE_TILE_UPDATES = false; // Paper
                itemstack = ItemStack.field_190927_a;
                flag = true;
            } else if (func_145894_a(itemstack1, itemstack)) {
                int j = itemstack.func_77976_d() - itemstack1.func_190916_E();
                int k = Math.min(itemstack.func_190916_E(), j);

                itemstack.func_190918_g(k);
                itemstack1.func_190917_f(k);
                flag = k > 0;
            }

            if (flag) {
                if (flag1 && iinventory1 instanceof TileEntityHopper) {
                    TileEntityHopper tileentityhopper = (TileEntityHopper) iinventory1;

                    if (!tileentityhopper.func_174914_o()) {
                        byte b0 = 0;

                        if (iinventory != null && iinventory instanceof TileEntityHopper) {
                            TileEntityHopper tileentityhopper1 = (TileEntityHopper) iinventory;

                            if (tileentityhopper.field_190578_g >= tileentityhopper1.field_190578_g) {
                                b0 = 1;
                            }
                        }

                        tileentityhopper.func_145896_c(tileentityhopper.field_145850_b.spigotConfig.hopperTransfer - b0); // Spigot
                    }
                }

                iinventory1.func_70296_d();
            }
        }

        return itemstack;
    }

    private IInventory func_145895_l() {
        EnumFacing enumdirection = BlockHopper.func_176428_b(this.func_145832_p());

        // Paper start - don't search for entities in push mode
        World world = func_145831_w();
        return getInventory(world, this.func_96107_aA() + (double) enumdirection.func_82601_c(), this.func_96109_aB() + (double) enumdirection.func_96559_d(), this.func_96108_aC() + (double) enumdirection.func_82599_e(), !world.paperConfig.isHopperPushBased);
        // Paper end
    }

    // Paper start - add option to search for entities
    public static IInventory func_145884_b(IHopper hopper) {
        return getInventory(hopper, true);
    }

    public static IInventory getInventory(IHopper ihopper, boolean searchForEntities) {
        return getInventory(ihopper.func_145831_w(), ihopper.func_96107_aA(), ihopper.func_96109_aB() + 1.0D, ihopper.func_96108_aC(), searchForEntities);
        // Paper end
    }

    public static List<EntityItem> func_184292_a(World world, double d0, double d1, double d2) {
        return world.func_175647_a(EntityItem.class, new AxisAlignedBB(d0 - 0.5D, d1, d2 - 0.5D, d0 + 0.5D, d1 + 1.5D, d2 + 0.5D), EntitySelectors.field_94557_a); // Change getHopperLookupBoundingBox(double, double, double) if the bounding box calculation is ever changed
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
    public static IInventory func_145893_b(World world, double d0, double d1, double d2) {
        return getInventory(world, d0, d1, d2, true);
    }

    public static IInventory getInventory(World world, double d0, double d1, double d2, boolean searchForEntities) {
        // Paper end
        Object object = null;
        int i = MathHelper.func_76128_c(d0);
        int j = MathHelper.func_76128_c(d1);
        int k = MathHelper.func_76128_c(d2);
        BlockPos blockposition = new BlockPos(i, j, k);
        if ( !world.func_175667_e( blockposition ) ) return null; // Spigot
        Block block = world.func_180495_p(blockposition).func_177230_c();

        if (block.func_149716_u()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof IInventory) {
                object = (IInventory) tileentity;
                if (object instanceof TileEntityChest && block instanceof BlockChest) {
                    object = ((BlockChest) block).func_189418_a(world, blockposition, true);
                }
            }
        }

        if (object == null && searchForEntities) { // Paper - only if searchForEntities
            List list = world.func_175674_a((Entity) null, new AxisAlignedBB(d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, d0 + 0.5D, d1 + 0.5D, d2 + 0.5D), EntitySelectors.field_96566_b);

            if (!list.isEmpty()) {
                object = (IInventory) list.get(world.field_73012_v.nextInt(list.size()));
            }
        }

        return (IInventory) object;
    }

    private static boolean func_145894_a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.func_77973_b() != itemstack1.func_77973_b() ? false : (itemstack.func_77960_j() != itemstack1.func_77960_j() ? false : (itemstack.func_190916_E() > itemstack.func_77976_d() ? false : ItemStack.func_77970_a(itemstack, itemstack1)));
    }

    public double func_96107_aA() {
        return (double) this.field_174879_c.func_177958_n() + 0.5D;
    }

    public double func_96109_aB() {
        return (double) this.field_174879_c.func_177956_o() + 0.5D;
    }

    public double func_96108_aC() {
        return (double) this.field_174879_c.func_177952_p() + 0.5D;
    }

    private void func_145896_c(int i) {
        this.field_145901_j = i;
    }

    private boolean func_145888_j() {
        return this.field_145901_j > 0;
    }

    private boolean func_174914_o() {
        return this.field_145901_j > 8;
    }

    public String func_174875_k() {
        return "minecraft:hopper";
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.func_184281_d(entityhuman);
        return new ContainerHopper(playerinventory, this, entityhuman);
    }

    protected NonNullList<ItemStack> func_190576_q() {
        return this.field_145900_a;
    }
}

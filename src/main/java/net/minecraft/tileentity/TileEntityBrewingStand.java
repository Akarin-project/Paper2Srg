package net.minecraft.tileentity;

import java.util.Arrays;
import java.util.Iterator;

import java.util.List;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.InventoryHolder;

// CraftBukkit start
import java.util.List;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] SLOTS_FOR_UP = new int[] { 3};
    private static final int[] SLOTS_FOR_DOWN = new int[] { 0, 1, 2, 3};
    private static final int[] OUTPUT_SLOTS = new int[] { 0, 1, 2, 4};
    private NonNullList<ItemStack> brewingItemStacks;
    private int brewTime;
    private boolean[] filledSlots;
    private Item ingredientID;
    private String customName;
    private int fuel;
    // CraftBukkit start - add fields and methods
    private int lastTick = MinecraftServer.currentTick;
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = 64;

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public List<ItemStack> getContents() {
        return this.brewingItemStacks;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityBrewingStand() {
        this.brewingItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.brewing";
    }

    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setName(String s) {
        this.customName = s;
    }

    public int getSizeInventory() {
        return this.brewingItemStacks.size();
    }

    public boolean isEmpty() {
        Iterator iterator = this.brewingItemStacks.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public void update() {
        ItemStack itemstack = (ItemStack) this.brewingItemStacks.get(4);

        if (this.fuel <= 0 && itemstack.getItem() == Items.BLAZE_POWDER) {
            // CraftBukkit start
            BrewingStandFuelEvent event = new BrewingStandFuelEvent(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), CraftItemStack.asCraftMirror(itemstack), 20);
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            this.fuel = event.getFuelPower(); // PAIL fuelLevel
            if (this.fuel > 0 && event.isConsuming()) {
                itemstack.shrink(1);
            }
            // CraftBukkit end
            this.markDirty();
        }

        boolean flag = this.canBrew();
        boolean flag1 = this.brewTime > 0;
        ItemStack itemstack1 = (ItemStack) this.brewingItemStacks.get(3);

        // CraftBukkit start - Use wall time instead of ticks for brewing
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        this.lastTick = MinecraftServer.currentTick;

        if (flag1) {
            this.brewTime -= elapsedTicks;
            boolean flag2 = this.brewTime <= 0; // == -> <=
            // CraftBukkit end

            if (flag2 && flag) {
                this.brewPotions();
                this.markDirty();
            } else if (!flag) {
                this.brewTime = 0;
                this.markDirty();
            } else if (this.ingredientID != itemstack1.getItem()) {
                this.brewTime = 0;
                this.markDirty();
            }
        } else if (flag && this.fuel > 0) {
            --this.fuel;
            this.brewTime = 400;
            this.ingredientID = itemstack1.getItem();
            this.markDirty();
        }

        if (!this.world.isRemote) {
            boolean[] aboolean = this.createFilledSlotsArray();

            if (!Arrays.equals(aboolean, this.filledSlots)) {
                this.filledSlots = aboolean;
                IBlockState iblockdata = this.world.getBlockState(this.getPos());

                if (!(iblockdata.getBlock() instanceof BlockBrewingStand)) {
                    return;
                }

                for (int i = 0; i < BlockBrewingStand.HAS_BOTTLE.length; ++i) {
                    iblockdata = iblockdata.withProperty(BlockBrewingStand.HAS_BOTTLE[i], Boolean.valueOf(aboolean[i]));
                }

                this.world.setBlockState(this.pos, iblockdata, 2);
            }
        }

    }

    public boolean[] createFilledSlotsArray() {
        boolean[] aboolean = new boolean[3];

        for (int i = 0; i < 3; ++i) {
            if (!((ItemStack) this.brewingItemStacks.get(i)).isEmpty()) {
                aboolean[i] = true;
            }
        }

        return aboolean;
    }

    private boolean canBrew() {
        ItemStack itemstack = (ItemStack) this.brewingItemStacks.get(3);

        if (itemstack.isEmpty()) {
            return false;
        } else if (!PotionHelper.isReagent(itemstack)) {
            return false;
        } else {
            for (int i = 0; i < 3; ++i) {
                ItemStack itemstack1 = (ItemStack) this.brewingItemStacks.get(i);

                if (!itemstack1.isEmpty() && PotionHelper.hasConversions(itemstack1, itemstack)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void brewPotions() {
        ItemStack itemstack = (ItemStack) this.brewingItemStacks.get(3);
        // CraftBukkit start
        InventoryHolder owner = this.getOwner();
        if (owner != null) {
            BrewEvent event = new BrewEvent(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), (org.bukkit.inventory.BrewerInventory) owner.getInventory(), this.fuel);
            org.bukkit.Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end

        for (int i = 0; i < 3; ++i) {
            this.brewingItemStacks.set(i, PotionHelper.doReaction(itemstack, (ItemStack) this.brewingItemStacks.get(i)));
        }

        itemstack.shrink(1);
        BlockPos blockposition = this.getPos();

        if (itemstack.getItem().hasContainerItem()) {
            ItemStack itemstack1 = new ItemStack(itemstack.getItem().getContainerItem());

            if (itemstack.isEmpty()) {
                itemstack = itemstack1;
            } else {
                InventoryHelper.spawnItemStack(this.world, (double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), itemstack1);
            }
        }

        this.brewingItemStacks.set(3, itemstack);
        this.world.playEvent(1035, blockposition, 0);
    }

    public static void registerFixesBrewingStand(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityBrewingStand.class, new String[] { "Items"})));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.brewingItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbttagcompound, this.brewingItemStacks);
        this.brewTime = nbttagcompound.getShort("BrewTime");
        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.customName = nbttagcompound.getString("CustomName");
        }

        this.fuel = nbttagcompound.getByte("Fuel");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BrewTime", (short) this.brewTime);
        ItemStackHelper.saveAllItems(nbttagcompound, this.brewingItemStacks);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.customName);
        }

        nbttagcompound.setByte("Fuel", (byte) this.fuel);
        return nbttagcompound;
    }

    public ItemStack getStackInSlot(int i) {
        return i >= 0 && i < this.brewingItemStacks.size() ? (ItemStack) this.brewingItemStacks.get(i) : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int i, int j) {
        return ItemStackHelper.getAndSplit(this.brewingItemStacks, i, j);
    }

    public ItemStack removeStackFromSlot(int i) {
        return ItemStackHelper.getAndRemove(this.brewingItemStacks, i);
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.brewingItemStacks.size()) {
            this.brewingItemStacks.set(i, itemstack);
        }

    }

    public int getInventoryStackLimit() {
        return this.maxStack; // CraftBukkit
    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.world.getTileEntity(this.pos) != this ? false : entityhuman.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer entityhuman) {}

    public void closeInventory(EntityPlayer entityhuman) {}

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i == 3) {
            return PotionHelper.isReagent(itemstack);
        } else {
            Item item = itemstack.getItem();

            return i == 4 ? item == Items.BLAZE_POWDER : (item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE) && this.getStackInSlot(i).isEmpty();
        }
    }

    public int[] getSlotsForFace(EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? TileEntityBrewingStand.SLOTS_FOR_UP : (enumdirection == EnumFacing.DOWN ? TileEntityBrewingStand.SLOTS_FOR_DOWN : TileEntityBrewingStand.OUTPUT_SLOTS);
    }

    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return this.isItemValidForSlot(i, itemstack);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return i == 3 ? itemstack.getItem() == Items.GLASS_BOTTLE : true;
    }

    public String getGuiID() {
        return "minecraft:brewing_stand";
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerBrewingStand(playerinventory, this);
    }

    public int getField(int i) {
        switch (i) {
        case 0:
            return this.brewTime;

        case 1:
            return this.fuel;

        default:
            return 0;
        }
    }

    public void setField(int i, int j) {
        switch (i) {
        case 0:
            this.brewTime = j;
            break;

        case 1:
            this.fuel = j;
        }

    }

    public int getFieldCount() {
        return 2;
    }

    public void clear() {
        this.brewingItemStacks.clear();
    }
}

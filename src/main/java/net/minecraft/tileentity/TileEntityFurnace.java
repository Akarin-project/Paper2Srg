package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
// CraftBukkit end

public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] SLOTS_TOP = new int[] { 0};
    private static final int[] SLOTS_BOTTOM = new int[] { 2, 1};
    private static final int[] SLOTS_SIDES = new int[] { 1};
    private NonNullList<ItemStack> furnaceItemStacks;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;

    // CraftBukkit start - add fields and methods
    private int lastTick = MinecraftServer.currentTick;
    private int maxStack = MAX_STACK;
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();

    public List<ItemStack> getContents() {
        return this.furnaceItemStacks;
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

    public TileEntityFurnace() {
        this.furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    public int getSizeInventory() {
        return this.furnaceItemStacks.size();
    }

    public boolean isEmpty() {
        Iterator iterator = this.furnaceItemStacks.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public ItemStack getStackInSlot(int i) {
        return (ItemStack) this.furnaceItemStacks.get(i);
    }

    public ItemStack decrStackSize(int i, int j) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, i, j);
    }

    public ItemStack removeStackFromSlot(int i) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, i);
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        ItemStack itemstack1 = (ItemStack) this.furnaceItemStacks.get(i);
        boolean flag = !itemstack.isEmpty() && itemstack.isItemEqual(itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1);

        this.furnaceItemStacks.set(i, itemstack);
        if (itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }

        if (i == 0 && !flag) {
            this.totalCookTime = this.getCookTime(itemstack);
            this.cookTime = 0;
            this.markDirty();
        }

    }

    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }

    public boolean hasCustomName() {
        return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
    }

    public void setCustomInventoryName(String s) {
        this.furnaceCustomName = s;
    }

    public static void registerFixesFurnace(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityFurnace.class, new String[] { "Items"})));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.furnaceItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbttagcompound, this.furnaceItemStacks);
        this.furnaceBurnTime = nbttagcompound.getShort("BurnTime");
        this.cookTime = nbttagcompound.getShort("CookTime");
        this.totalCookTime = nbttagcompound.getShort("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime((ItemStack) this.furnaceItemStacks.get(1));
        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = nbttagcompound.getString("CustomName");
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short) this.furnaceBurnTime);
        nbttagcompound.setShort("CookTime", (short) this.cookTime);
        nbttagcompound.setShort("CookTimeTotal", (short) this.totalCookTime);
        ItemStackHelper.saveAllItems(nbttagcompound, this.furnaceItemStacks);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.furnaceCustomName);
        }

        return nbttagcompound;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public void update() {
        boolean flag = (this.getBlockType() == Blocks.LIT_FURNACE); // CraftBukkit - SPIGOT-844 - Check if furnace block is lit using the block instead of burn time
        boolean flag1 = false;

        // CraftBukkit start - Use wall time instead of ticks for cooking
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        this.lastTick = MinecraftServer.currentTick;

        // CraftBukkit - moved from below - edited for wall time
        if (this.isBurning() && this.canSmelt()) {
            this.cookTime += elapsedTicks;
            if (this.cookTime >= this.totalCookTime) {
                this.cookTime -= this.totalCookTime; // Paper
                this.totalCookTime = this.getCookTime((ItemStack) this.furnaceItemStacks.get(0));
                this.smeltItem();
                flag1 = true;
            }
        } else {
            this.cookTime = 0;
        }
        // CraftBukkit end

        if (this.isBurning()) {
            this.furnaceBurnTime -= elapsedTicks; // CraftBukkit - use elapsedTicks in place of constant
        }

        if (!this.world.isRemote) {
            ItemStack itemstack = (ItemStack) this.furnaceItemStacks.get(1);

            if (!this.isBurning() && (itemstack.isEmpty() || ((ItemStack) this.furnaceItemStacks.get(0)).isEmpty())) {
                if (!this.isBurning() && this.cookTime > 0) {
                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
                }
            } else {
                // CraftBukkit start - Handle multiple elapsed ticks
                if (this.furnaceBurnTime <= 0 && this.canSmelt()) { // CraftBukkit - == to <=
                    CraftItemStack fuel = CraftItemStack.asCraftMirror(itemstack);

                    FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(this.world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), fuel, getItemBurnTime(itemstack));
                    this.world.getServer().getPluginManager().callEvent(furnaceBurnEvent);

                    if (furnaceBurnEvent.isCancelled()) {
                        return;
                    }

                    this.currentItemBurnTime = furnaceBurnEvent.getBurnTime();
                    this.furnaceBurnTime += this.currentItemBurnTime;
                    if (this.furnaceBurnTime > 0 && furnaceBurnEvent.isBurning()) {
                        // CraftBukkit end
                        flag1 = true;
                        if (!itemstack.isEmpty()) {
                            Item item = itemstack.getItem();

                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                Item item1 = item.getContainerItem();

                                this.furnaceItemStacks.set(1, item1 == null ? ItemStack.EMPTY : new ItemStack(item1));
                            }
                        }
                    }
                }

                /* CraftBukkit start - Moved up
                if (this.isBurning() && this.canBurn()) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.a((ItemStack) this.items.get(0));
                        this.burn();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
                */
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                BlockFurnace.setState(this.isBurning(), this.world, this.pos);
                this.updateContainingBlockInfo(); // CraftBukkit - Invalidate tile entity's cached block type 
            }
        }

        if (flag1) {
            this.markDirty();
        }

    }

    public int getCookTime(ItemStack itemstack) {
        return 200;
    }

    private boolean canSmelt() {
        if (((ItemStack) this.furnaceItemStacks.get(0)).isEmpty()) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult((ItemStack) this.furnaceItemStacks.get(0));

            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = (ItemStack) this.furnaceItemStacks.get(2);

                // CraftBukkit - consider resultant count instead of current count
                return itemstack1.isEmpty() ? true : (!itemstack1.isItemEqual(itemstack) ? false : (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() < itemstack1.getMaxStackSize() ? true : itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize()));
            }
        }
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = (ItemStack) this.furnaceItemStacks.get(0);
            ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
            ItemStack itemstack2 = (ItemStack) this.furnaceItemStacks.get(2);

            // CraftBukkit start - fire FurnaceSmeltEvent
            CraftItemStack source = CraftItemStack.asCraftMirror(itemstack);
            org.bukkit.inventory.ItemStack result = CraftItemStack.asBukkitCopy(itemstack1);

            FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(this.world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), source, result);
            this.world.getServer().getPluginManager().callEvent(furnaceSmeltEvent);

            if (furnaceSmeltEvent.isCancelled()) {
                return;
            }

            result = furnaceSmeltEvent.getResult();
            itemstack1 = CraftItemStack.asNMSCopy(result);

            if (!itemstack1.isEmpty()) {
                if (itemstack2.isEmpty()) {
                    this.furnaceItemStacks.set(2, itemstack1.copy());
                } else if (CraftItemStack.asCraftMirror(itemstack2).isSimilar(result)) {
                    itemstack2.grow(itemstack1.getCount());
                } else {
                    return;
                }
            }

            /*
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.cloneItemStack());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.add(1);
            }
            */
            // CraftBukkit end

            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !((ItemStack) this.furnaceItemStacks.get(1)).isEmpty() && ((ItemStack) this.furnaceItemStacks.get(1)).getItem() == Items.BUCKET) {
                this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    public static int getItemBurnTime(ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return 0;
        } else {
            Item item = itemstack.getItem();

            return item == Item.getItemFromBlock(Blocks.WOODEN_SLAB) ? 150 : (item == Item.getItemFromBlock(Blocks.WOOL) ? 100 : (item == Item.getItemFromBlock(Blocks.CARPET) ? 67 : (item == Item.getItemFromBlock(Blocks.LADDER) ? 300 : (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON) ? 100 : (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD ? 300 : (item == Item.getItemFromBlock(Blocks.COAL_BLOCK) ? 16000 : (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName()) ? 200 : (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName()) ? 200 : (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName()) ? 200 : (item == Items.STICK ? 100 : (item != Items.BOW && item != Items.FISHING_ROD ? (item == Items.SIGN ? 200 : (item == Items.COAL ? 1600 : (item == Items.LAVA_BUCKET ? 20000 : (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL ? (item == Items.BLAZE_ROD ? 2400 : (item instanceof ItemDoor && item != Items.IRON_DOOR ? 200 : (item instanceof ItemBoat ? 400 : 0))) : 100)))) : 300)))))))))));
        }
    }

    public static boolean isItemFuel(ItemStack itemstack) {
        return getItemBurnTime(itemstack) > 0;
    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.world.getTileEntity(this.pos) != this ? false : entityhuman.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer entityhuman) {}

    public void closeInventory(EntityPlayer entityhuman) {}

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i == 2) {
            return false;
        } else if (i != 1) {
            return true;
        } else {
            ItemStack itemstack1 = (ItemStack) this.furnaceItemStacks.get(1);

            return isItemFuel(itemstack) || SlotFurnaceFuel.isBucket(itemstack) && itemstack1.getItem() != Items.BUCKET;
        }
    }

    public int[] getSlotsForFace(EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? TileEntityFurnace.SLOTS_BOTTOM : (enumdirection == EnumFacing.UP ? TileEntityFurnace.SLOTS_TOP : TileEntityFurnace.SLOTS_SIDES);
    }

    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return this.isItemValidForSlot(i, itemstack);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        if (enumdirection == EnumFacing.DOWN && i == 1) {
            Item item = itemstack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }

    public String getGuiID() {
        return "minecraft:furnace";
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerFurnace(playerinventory, this);
    }

    public int getField(int i) {
        switch (i) {
        case 0:
            return this.furnaceBurnTime;

        case 1:
            return this.currentItemBurnTime;

        case 2:
            return this.cookTime;

        case 3:
            return this.totalCookTime;

        default:
            return 0;
        }
    }

    public void setField(int i, int j) {
        switch (i) {
        case 0:
            this.furnaceBurnTime = j;
            break;

        case 1:
            this.currentItemBurnTime = j;
            break;

        case 2:
            this.cookTime = j;
            break;

        case 3:
            this.totalCookTime = j;
        }

    }

    public int getFieldCount() {
        return 4;
    }

    public void clear() {
        this.furnaceItemStacks.clear();
    }
}

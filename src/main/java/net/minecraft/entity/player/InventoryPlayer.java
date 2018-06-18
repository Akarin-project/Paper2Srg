package net.minecraft.entity.player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import java.util.ArrayList;
import net;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryPlayer implements IInventory {

    public final NonNullList<ItemStack> mainInventory;
    public final NonNullList<ItemStack> armorInventory;
    public final NonNullList<ItemStack> offHandInventory;
    private final List<NonNullList<ItemStack>> allInventories;
    public int currentItem;
    public EntityPlayer player;
    private ItemStack itemStack;
    private int timesChanged;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        List<ItemStack> combined = new ArrayList<ItemStack>(mainInventory.size() + armorInventory.size() + offHandInventory.size());
        for (List<net.minecraft.item.ItemStack> sub : this.allInventories) {
            combined.addAll(sub);
        }

        return combined;
    }

    public List<ItemStack> getArmorContents() {
        return this.armorInventory;
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

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return this.player.getBukkitEntity();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    @Override
    public Location getLocation() {
        return player.getBukkitEntity().getLocation();
    }
    // CraftBukkit end

    public InventoryPlayer(EntityPlayer entityhuman) {
        this.mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
        this.armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        this.offHandInventory = NonNullList.withSize(1, ItemStack.EMPTY);
        this.allInventories = Arrays.asList(new NonNullList[] { this.mainInventory, this.armorInventory, this.offHandInventory});
        this.itemStack = ItemStack.EMPTY;
        this.player = entityhuman;
    }

    public ItemStack getCurrentItem() {
        return isHotbar(this.currentItem) ? (ItemStack) this.mainInventory.get(this.currentItem) : ItemStack.EMPTY;
    }

    public static int getHotbarSize() {
        return 9;
    }

    private boolean canMergeStacks(ItemStack itemstack, ItemStack itemstack1) {
        return !itemstack.isEmpty() && this.stackEqualExact(itemstack, itemstack1) && itemstack.isStackable() && itemstack.getCount() < itemstack.getMaxStackSize() && itemstack.getCount() < this.getInventoryStackLimit();
    }

    private boolean stackEqualExact(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.getItem() == itemstack1.getItem() && (!itemstack.getHasSubtypes() || itemstack.getMetadata() == itemstack1.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1);
    }

    // CraftBukkit start - Watch method above! :D
    public int canHold(ItemStack itemstack) {
        int remains = itemstack.getCount();
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            ItemStack itemstack1 = this.getStackInSlot(i);
            if (itemstack1.isEmpty()) return itemstack.getCount();

            // Taken from firstPartial(ItemStack)
            if (!itemstack1.isEmpty() && itemstack1.getItem() == itemstack.getItem() && itemstack1.isStackable() && itemstack1.getCount() < itemstack1.getMaxStackSize() && itemstack1.getCount() < this.getInventoryStackLimit() && (!itemstack1.getHasSubtypes() || itemstack1.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack)) {
                remains -= (itemstack1.getMaxStackSize() < this.getInventoryStackLimit() ? itemstack1.getMaxStackSize() : this.getInventoryStackLimit()) - itemstack1.getCount();
            }
            if (remains <= 0) return itemstack.getCount();
        }
        return itemstack.getCount() - remains;
    }
    // CraftBukkit end

    public int getFirstEmptyStack() {
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            if (((ItemStack) this.mainInventory.get(i)).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public void pickItem(int i) {
        this.currentItem = this.getBestHotbarSlot();
        ItemStack itemstack = (ItemStack) this.mainInventory.get(this.currentItem);

        this.mainInventory.set(this.currentItem, this.mainInventory.get(i));
        this.mainInventory.set(i, itemstack);
    }

    public static boolean isHotbar(int i) {
        return i >= 0 && i < 9;
    }

    public int findSlotMatchingUnusedItem(ItemStack itemstack) {
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            ItemStack itemstack1 = (ItemStack) this.mainInventory.get(i);

            if (!((ItemStack) this.mainInventory.get(i)).isEmpty() && this.stackEqualExact(itemstack, (ItemStack) this.mainInventory.get(i)) && !((ItemStack) this.mainInventory.get(i)).isItemDamaged() && !itemstack1.isItemEnchanted() && !itemstack1.hasDisplayName()) {
                return i;
            }
        }

        return -1;
    }

    public int getBestHotbarSlot() {
        int i;
        int j;

        for (i = 0; i < 9; ++i) {
            j = (this.currentItem + i) % 9;
            if (((ItemStack) this.mainInventory.get(j)).isEmpty()) {
                return j;
            }
        }

        for (i = 0; i < 9; ++i) {
            j = (this.currentItem + i) % 9;
            if (!((ItemStack) this.mainInventory.get(j)).isItemEnchanted()) {
                return j;
            }
        }

        return this.currentItem;
    }

    public int clearMatchingItems(@Nullable Item item, int i, int j, @Nullable NBTTagCompound nbttagcompound) {
        int k = 0;

        int l;

        for (l = 0; l < this.getSizeInventory(); ++l) {
            ItemStack itemstack = this.getStackInSlot(l);

            if (!itemstack.isEmpty() && (item == null || itemstack.getItem() == item) && (i <= -1 || itemstack.getMetadata() == i) && (nbttagcompound == null || NBTUtil.areNBTEquals(nbttagcompound, itemstack.getTagCompound(), true))) {
                int i1 = j <= 0 ? itemstack.getCount() : Math.min(j - k, itemstack.getCount());

                k += i1;
                if (j != 0) {
                    itemstack.shrink(i1);
                    if (itemstack.isEmpty()) {
                        this.setInventorySlotContents(l, ItemStack.EMPTY);
                    }

                    if (j > 0 && k >= j) {
                        return k;
                    }
                }
            }
        }

        if (!this.itemStack.isEmpty()) {
            if (item != null && this.itemStack.getItem() != item) {
                return k;
            }

            if (i > -1 && this.itemStack.getMetadata() != i) {
                return k;
            }

            if (nbttagcompound != null && !NBTUtil.areNBTEquals(nbttagcompound, this.itemStack.getTagCompound(), true)) {
                return k;
            }

            l = j <= 0 ? this.itemStack.getCount() : Math.min(j - k, this.itemStack.getCount());
            k += l;
            if (j != 0) {
                this.itemStack.shrink(l);
                if (this.itemStack.isEmpty()) {
                    this.itemStack = ItemStack.EMPTY;
                }

                if (j > 0 && k >= j) {
                    return k;
                }
            }
        }

        return k;
    }

    private int storePartialItemStack(ItemStack itemstack) {
        int i = this.storeItemStack(itemstack);

        if (i == -1) {
            i = this.getFirstEmptyStack();
        }

        return i == -1 ? itemstack.getCount() : this.addResource(i, itemstack);
    }

    private int addResource(int i, ItemStack itemstack) {
        Item item = itemstack.getItem();
        int j = itemstack.getCount();
        ItemStack itemstack1 = this.getStackInSlot(i);

        if (itemstack1.isEmpty()) {
            itemstack1 = new ItemStack(item, 0, itemstack.getMetadata());
            if (itemstack.hasTagCompound()) {
                itemstack1.setTagCompound(itemstack.getTagCompound().copy());
            }

            this.setInventorySlotContents(i, itemstack1);
        }

        int k = j;

        if (j > itemstack1.getMaxStackSize() - itemstack1.getCount()) {
            k = itemstack1.getMaxStackSize() - itemstack1.getCount();
        }

        if (k > this.getInventoryStackLimit() - itemstack1.getCount()) {
            k = this.getInventoryStackLimit() - itemstack1.getCount();
        }

        if (k == 0) {
            return j;
        } else {
            j -= k;
            itemstack1.grow(k);
            itemstack1.setAnimationsToGo(5);
            return j;
        }
    }

    public int storeItemStack(ItemStack itemstack) {
        if (this.canMergeStacks(this.getStackInSlot(this.currentItem), itemstack)) {
            return this.currentItem;
        } else if (this.canMergeStacks(this.getStackInSlot(40), itemstack)) {
            return 40;
        } else {
            for (int i = 0; i < this.mainInventory.size(); ++i) {
                if (this.canMergeStacks((ItemStack) this.mainInventory.get(i), itemstack)) {
                    return i;
                }
            }

            return -1;
        }
    }

    public void decrementAnimations() {
        Iterator iterator = this.allInventories.iterator();

        while (iterator.hasNext()) {
            NonNullList nonnulllist = (NonNullList) iterator.next();

            for (int i = 0; i < nonnulllist.size(); ++i) {
                if (!((ItemStack) nonnulllist.get(i)).isEmpty()) {
                    ((ItemStack) nonnulllist.get(i)).updateAnimation(this.player.world, this.player, i, this.currentItem == i);
                }
            }
        }

    }

    public boolean addItemStackToInventory(ItemStack itemstack) {
        return this.add(-1, itemstack);
    }

    public boolean add(int i, final ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return false;
        } else {
            try {
                if (itemstack.isItemDamaged()) {
                    if (i == -1) {
                        i = this.getFirstEmptyStack();
                    }

                    if (i >= 0) {
                        this.mainInventory.set(i, itemstack.copy());
                        ((ItemStack) this.mainInventory.get(i)).setAnimationsToGo(5);
                        itemstack.setCount(0);
                        return true;
                    } else if (this.player.capabilities.isCreativeMode) {
                        itemstack.setCount(0);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    int j;

                    do {
                        j = itemstack.getCount();
                        if (i == -1) {
                            itemstack.setCount(this.storePartialItemStack(itemstack));
                        } else {
                            itemstack.setCount(this.addResource(i, itemstack));
                        }
                    } while (!itemstack.isEmpty() && itemstack.getCount() < j);

                    if (itemstack.getCount() == j && this.player.capabilities.isCreativeMode) {
                        itemstack.setCount(0);
                        return true;
                    } else {
                        return itemstack.getCount() < j;
                    }
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Item being added");

                crashreportsystemdetails.addCrashSection("Item ID", (Object) Integer.valueOf(Item.getIdFromItem(itemstack.getItem())));
                crashreportsystemdetails.addCrashSection("Item data", (Object) Integer.valueOf(itemstack.getMetadata()));
                crashreportsystemdetails.addDetail("Item name", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return itemstack.getDisplayName();
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    public void placeItemBackInInventory(World world, ItemStack itemstack) {
        if (!world.isRemote) {
            while (!itemstack.isEmpty()) {
                int i = this.storeItemStack(itemstack);

                if (i == -1) {
                    i = this.getFirstEmptyStack();
                }

                if (i == -1) {
                    this.player.dropItem(itemstack, false);
                    break;
                }

                int j = itemstack.getMaxStackSize() - this.getStackInSlot(i).getCount();

                if (this.add(i, itemstack.splitStack(j))) {
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketSetSlot(-2, i, this.getStackInSlot(i)));
                }
            }

        }
    }

    public ItemStack decrStackSize(int i, int j) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.allInventories.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        return nonnulllist != null && !((ItemStack) nonnulllist.get(i)).isEmpty() ? ItemStackHelper.getAndSplit(nonnulllist, i, j) : ItemStack.EMPTY;
    }

    public void deleteStack(ItemStack itemstack) {
        Iterator iterator = this.allInventories.iterator();

        while (iterator.hasNext()) {
            NonNullList nonnulllist = (NonNullList) iterator.next();

            for (int i = 0; i < nonnulllist.size(); ++i) {
                if (nonnulllist.get(i) == itemstack) {
                    nonnulllist.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }

    }

    public ItemStack removeStackFromSlot(int i) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.allInventories.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        if (nonnulllist != null && !((ItemStack) nonnulllist.get(i)).isEmpty()) {
            ItemStack itemstack = (ItemStack) nonnulllist.get(i);

            nonnulllist.set(i, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.allInventories.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        if (nonnulllist != null) {
            nonnulllist.set(i, itemstack);
        }

    }

    public float getDestroySpeed(IBlockState iblockdata) {
        float f = 1.0F;

        if (!((ItemStack) this.mainInventory.get(this.currentItem)).isEmpty()) {
            f *= ((ItemStack) this.mainInventory.get(this.currentItem)).getDestroySpeed(iblockdata);
        }

        return f;
    }

    public NBTTagList writeToNBT(NBTTagList nbttaglist) {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.mainInventory.size(); ++i) {
            if (!((ItemStack) this.mainInventory.get(i)).isEmpty()) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                ((ItemStack) this.mainInventory.get(i)).writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        for (i = 0; i < this.armorInventory.size(); ++i) {
            if (!((ItemStack) this.armorInventory.get(i)).isEmpty()) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) (i + 100));
                ((ItemStack) this.armorInventory.get(i)).writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        for (i = 0; i < this.offHandInventory.size(); ++i) {
            if (!((ItemStack) this.offHandInventory.get(i)).isEmpty()) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) (i + 150));
                ((ItemStack) this.offHandInventory.get(i)).writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void readFromNBT(NBTTagList nbttaglist) {
        this.mainInventory.clear();
        this.armorInventory.clear();
        this.offHandInventory.clear();

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.isEmpty()) {
                if (j >= 0 && j < this.mainInventory.size()) {
                    this.mainInventory.set(j, itemstack);
                } else if (j >= 100 && j < this.armorInventory.size() + 100) {
                    this.armorInventory.set(j - 100, itemstack);
                } else if (j >= 150 && j < this.offHandInventory.size() + 150) {
                    this.offHandInventory.set(j - 150, itemstack);
                }
            }
        }

    }

    public int getSizeInventory() {
        return this.mainInventory.size() + this.armorInventory.size() + this.offHandInventory.size();
    }

    public boolean isEmpty() {
        Iterator iterator = this.mainInventory.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                iterator = this.armorInventory.iterator();

                do {
                    if (!iterator.hasNext()) {
                        iterator = this.offHandInventory.iterator();

                        do {
                            if (!iterator.hasNext()) {
                                return true;
                            }

                            itemstack = (ItemStack) iterator.next();
                        } while (itemstack.isEmpty());

                        return false;
                    }

                    itemstack = (ItemStack) iterator.next();
                } while (itemstack.isEmpty());

                return false;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public ItemStack getStackInSlot(int i) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.allInventories.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        return nonnulllist == null ? ItemStack.EMPTY : (ItemStack) nonnulllist.get(i);
    }

    public String getName() {
        return "container.inventory";
    }

    public boolean hasCustomName() {
        return false;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public int getInventoryStackLimit() {
        return maxStack; // CraftBukkit
    }

    public boolean canHarvestBlock(IBlockState iblockdata) {
        if (iblockdata.getMaterial().isToolNotRequired()) {
            return true;
        } else {
            ItemStack itemstack = this.getStackInSlot(this.currentItem);

            return !itemstack.isEmpty() ? itemstack.canHarvestBlock(iblockdata) : false;
        }
    }

    public void damageArmor(float f) {
        f /= 4.0F;
        if (f < 1.0F) {
            f = 1.0F;
        }

        for (int i = 0; i < this.armorInventory.size(); ++i) {
            ItemStack itemstack = (ItemStack) this.armorInventory.get(i);

            if (itemstack.getItem() instanceof ItemArmor) {
                itemstack.damageItem((int) f, this.player);
            }
        }

    }

    public void dropAllItems() {
        Iterator iterator = this.allInventories.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();

            for (int i = 0; i < list.size(); ++i) {
                ItemStack itemstack = (ItemStack) list.get(i);

                if (!itemstack.isEmpty()) {
                    this.player.dropItem(itemstack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }

    }

    public void markDirty() {
        ++this.timesChanged;
    }

    public void setItemStack(ItemStack itemstack) {
        this.itemStack = itemstack;
    }

    public ItemStack getItemStack() {
        // CraftBukkit start
        if (this.itemStack.isEmpty()) {
            this.setItemStack(ItemStack.EMPTY);
        }
        // CraftBukkit end
        return this.itemStack;
    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.player.isDead ? false : entityhuman.getDistanceSq(this.player) <= 64.0D;
    }

    public boolean hasItemStack(ItemStack itemstack) {
        Iterator iterator = this.allInventories.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                ItemStack itemstack1 = (ItemStack) iterator1.next();

                if (!itemstack1.isEmpty() && itemstack1.isItemEqual(itemstack)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void openInventory(EntityPlayer entityhuman) {}

    public void closeInventory(EntityPlayer entityhuman) {}

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public void copyInventory(InventoryPlayer playerinventory) {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, playerinventory.getStackInSlot(i));
        }

        this.currentItem = playerinventory.currentItem;
    }

    public int getField(int i) {
        return 0;
    }

    public void setField(int i, int j) {}

    public int getFieldCount() {
        return 0;
    }

    public void clear() {
        Iterator iterator = this.allInventories.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();

            list.clear();
        }

    }

    public void fillStackedContents(RecipeItemHelper autorecipestackmanager, boolean flag) {
        Iterator iterator = this.mainInventory.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            autorecipestackmanager.accountStack(itemstack);
        }

        if (flag) {
            autorecipestackmanager.accountStack((ItemStack) this.offHandInventory.get(0));
        }

    }
}

package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import java.util.List;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryBasic implements IInventory {

    private String inventoryTitle;
    private final int slotsCount;
    public final NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> changeListeners;
    private boolean hasCustomName;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;
    protected org.bukkit.inventory.InventoryHolder bukkitOwner;

    public List<ItemStack> getContents() {
        return this.inventoryContents;
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

    public void setMaxStackSize(int i) {
        maxStack = i;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return bukkitOwner;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    public InventoryBasic(String s, boolean flag, int i) {
        this(s, flag, i, null);
    }

    public InventoryBasic(String s, boolean flag, int i, org.bukkit.inventory.InventoryHolder owner) {
        this.bukkitOwner = owner;
        // CraftBukkit end
        this.inventoryTitle = s;
        this.hasCustomName = flag;
        this.slotsCount = i;
        this.inventoryContents = NonNullList.withSize(i, ItemStack.EMPTY);
    }

    public void addInventoryChangeListener(IInventoryChangedListener iinventorylistener) {
        if (this.changeListeners == null) {
            this.changeListeners = Lists.newArrayList();
        }

        this.changeListeners.add(iinventorylistener);
    }

    public void removeInventoryChangeListener(IInventoryChangedListener iinventorylistener) {
        this.changeListeners.remove(iinventorylistener);
    }

    public ItemStack getStackInSlot(int i) {
        return i >= 0 && i < this.inventoryContents.size() ? (ItemStack) this.inventoryContents.get(i) : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int i, int j) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, i, j);

        if (!itemstack.isEmpty()) {
            this.markDirty();
        }

        return itemstack;
    }

    public ItemStack addItem(ItemStack itemstack) {
        ItemStack itemstack1 = itemstack.copy();

        for (int i = 0; i < this.slotsCount; ++i) {
            ItemStack itemstack2 = this.getStackInSlot(i);

            if (itemstack2.isEmpty()) {
                this.setInventorySlotContents(i, itemstack1);
                this.markDirty();
                return ItemStack.EMPTY;
            }

            if (ItemStack.areItemsEqual(itemstack2, itemstack1)) {
                int j = Math.min(this.getInventoryStackLimit(), itemstack2.getMaxStackSize());
                int k = Math.min(itemstack1.getCount(), j - itemstack2.getCount());

                if (k > 0) {
                    itemstack2.grow(k);
                    itemstack1.shrink(k);
                    if (itemstack1.isEmpty()) {
                        this.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack1.getCount() != itemstack.getCount()) {
            this.markDirty();
        }

        return itemstack1;
    }

    public ItemStack removeStackFromSlot(int i) {
        ItemStack itemstack = (ItemStack) this.inventoryContents.get(i);

        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.inventoryContents.set(i, ItemStack.EMPTY);
            return itemstack;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.inventoryContents.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    public int getSizeInventory() {
        return this.slotsCount;
    }

    public boolean isEmpty() {
        Iterator iterator = this.inventoryContents.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public String getName() {
        return this.inventoryTitle;
    }

    public boolean hasCustomName() {
        return this.hasCustomName;
    }

    public void setCustomName(String s) {
        this.hasCustomName = true;
        this.inventoryTitle = s;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void markDirty() {
        if (this.changeListeners != null) {
            for (int i = 0; i < this.changeListeners.size(); ++i) {
                ((IInventoryChangedListener) this.changeListeners.get(i)).onInventoryChanged(this);
            }
        }

    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return true;
    }

    public void openInventory(EntityPlayer entityhuman) {}

    public void closeInventory(EntityPlayer entityhuman) {}

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public int getField(int i) {
        return 0;
    }

    public void setField(int i, int j) {}

    public int getFieldCount() {
        return 0;
    }

    public void clear() {
        this.inventoryContents.clear();
    }
}

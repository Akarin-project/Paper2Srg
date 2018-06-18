package net.minecraft.inventory;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

// CraftBukkit end

public class InventoryLargeChest implements ILockableContainer {

    private final String name;
    public final ILockableContainer upperChest;
    public final ILockableContainer lowerChest;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();

    public List<ItemStack> getContents() {
        List<ItemStack> result = new ArrayList<ItemStack>(this.getSizeInventory());
        for (int i = 0; i < this.getSizeInventory(); i++) {
            result.add(this.getStackInSlot(i));
        }
        return result;
    }

    public void onOpen(CraftHumanEntity who) {
        this.upperChest.onOpen(who);
        this.lowerChest.onOpen(who);
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        this.upperChest.onClose(who);
        this.lowerChest.onClose(who);
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return null; // This method won't be called since CraftInventoryDoubleChest doesn't defer to here
    }

    public void setMaxStackSize(int size) {
        this.upperChest.setMaxStackSize(size);
        this.lowerChest.setMaxStackSize(size);
    }

    @Override
    public Location getLocation() {
        return upperChest.getLocation(); // TODO: right?
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, ILockableContainer itileinventory, ILockableContainer itileinventory1) {
        this.name = s;
        if (itileinventory == null) {
            itileinventory = itileinventory1;
        }

        if (itileinventory1 == null) {
            itileinventory1 = itileinventory;
        }

        this.upperChest = itileinventory;
        this.lowerChest = itileinventory1;
        if (itileinventory.isLocked()) {
            itileinventory1.setLockCode(itileinventory.getLockCode());
        } else if (itileinventory1.isLocked()) {
            itileinventory.setLockCode(itileinventory1.getLockCode());
        }

    }

    public int getSizeInventory() {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    public boolean isEmpty() {
        return this.upperChest.isEmpty() && this.lowerChest.isEmpty();
    }

    public boolean isPartOfLargeChest(IInventory iinventory) {
        return this.upperChest == iinventory || this.lowerChest == iinventory;
    }

    public String getName() {
        return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
    }

    public boolean hasCustomName() {
        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public ItemStack getStackInSlot(int i) {
        return i >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(i - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(i);
    }

    public ItemStack decrStackSize(int i, int j) {
        return i >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(i - this.upperChest.getSizeInventory(), j) : this.upperChest.decrStackSize(i, j);
    }

    public ItemStack removeStackFromSlot(int i) {
        return i >= this.upperChest.getSizeInventory() ? this.lowerChest.removeStackFromSlot(i - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(i);
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if (i >= this.upperChest.getSizeInventory()) {
            this.lowerChest.setInventorySlotContents(i - this.upperChest.getSizeInventory(), itemstack);
        } else {
            this.upperChest.setInventorySlotContents(i, itemstack);
        }

    }

    public int getInventoryStackLimit() {
        return Math.min(this.upperChest.getInventoryStackLimit(), this.lowerChest.getInventoryStackLimit()); // CraftBukkit - check both sides
    }

    public void markDirty() {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.upperChest.isUsableByPlayer(entityhuman) && this.lowerChest.isUsableByPlayer(entityhuman);
    }

    public void openInventory(EntityPlayer entityhuman) {
        this.upperChest.openInventory(entityhuman);
        this.lowerChest.openInventory(entityhuman);
    }

    public void closeInventory(EntityPlayer entityhuman) {
        this.upperChest.closeInventory(entityhuman);
        this.lowerChest.closeInventory(entityhuman);
    }

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

    public boolean isLocked() {
        return this.upperChest.isLocked() || this.lowerChest.isLocked();
    }

    public void setLockCode(LockCode chestlock) {
        this.upperChest.setLockCode(chestlock);
        this.lowerChest.setLockCode(chestlock);
    }

    public LockCode getLockCode() {
        return this.upperChest.getLockCode();
    }

    public String getGuiID() {
        return this.upperChest.getGuiID();
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerChest(playerinventory, this, entityhuman);
    }

    public void clear() {
        this.upperChest.clear();
        this.lowerChest.clear();
    }
}

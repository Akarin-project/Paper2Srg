package net.minecraft.inventory;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryCraftResult implements IInventory {

    private final NonNullList<ItemStack> stackResult;
    private IRecipe recipeUsed;

    // CraftBukkit start
    private int maxStack = MAX_STACK;

    public java.util.List<ItemStack> getContents() {
        return this.stackResult;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return null; // Result slots don't get an owner
    }

    // Don't need a transaction; the InventoryCrafting keeps track of it for us
    public void onOpen(CraftHumanEntity who) {}
    public void onClose(CraftHumanEntity who) {}
    public java.util.List<HumanEntity> getViewers() {
        return new java.util.ArrayList<HumanEntity>();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    @Override
    public Location getLocation() {
        return null;
    }
    // CraftBukkit end

    public InventoryCraftResult() {
        this.stackResult = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public int getSizeInventory() {
        return 1;
    }

    public boolean isEmpty() {
        Iterator iterator = this.stackResult.iterator();

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
        return (ItemStack) this.stackResult.get(0);
    }

    public String getName() {
        return "Result";
    }

    public boolean hasCustomName() {
        return false;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public ItemStack decrStackSize(int i, int j) {
        return ItemStackHelper.getAndRemove(this.stackResult, 0);
    }

    public ItemStack removeStackFromSlot(int i) {
        return ItemStackHelper.getAndRemove(this.stackResult, 0);
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.stackResult.set(0, itemstack);
    }

    public int getInventoryStackLimit() {
        return maxStack; // CraftBukkit
    }

    public void markDirty() {}

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
        this.stackResult.clear();
    }

    public void setRecipeUsed(@Nullable IRecipe irecipe) {
        this.recipeUsed = irecipe;
    }

    @Nullable
    public IRecipe getRecipeUsed() {
        return this.recipeUsed;
    }
}

package net.minecraft.inventory;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.util.RecipeItemHelper;
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
import org.bukkit.event.inventory.InventoryType;

// CraftBukkit start
import java.util.List;
import org.bukkit.Location;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
// CraftBukkit end

public class InventoryCrafting implements IInventory {

    private final NonNullList<ItemStack> stackList;
    private final int inventoryWidth;
    private final int inventoryHeight;
    public final Container eventHandler;

    // CraftBukkit start - add fields
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    public IRecipe currentRecipe;
    public IInventory resultInventory;
    private EntityPlayer owner;
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.stackList;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public InventoryType getInvType() {
        return stackList.size() == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return (owner == null) ? null : owner.getBukkitEntity();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
        resultInventory.setMaxStackSize(size);
    }

    @Override
    public Location getLocation() {
        return owner.getBukkitEntity().getLocation();
    }

    public InventoryCrafting(Container container, int i, int j, EntityPlayer player) {
        this(container, i, j);
        this.owner = player;
    }
    // CraftBukkit end

    public InventoryCrafting(Container container, int i, int j) {
        this.stackList = NonNullList.withSize(i * j, ItemStack.EMPTY);
        this.eventHandler = container;
        this.inventoryWidth = i;
        this.inventoryHeight = j;
    }

    public int getSizeInventory() {
        return this.stackList.size();
    }

    public boolean isEmpty() {
        Iterator iterator = this.stackList.iterator();

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
        return i >= this.getSizeInventory() ? ItemStack.EMPTY : (ItemStack) this.stackList.get(i);
    }

    public ItemStack getStackInRowAndColumn(int i, int j) {
        return i >= 0 && i < this.inventoryWidth && j >= 0 && j <= this.inventoryHeight ? this.getStackInSlot(i + j * this.inventoryWidth) : ItemStack.EMPTY;
    }

    public String getName() {
        return "container.crafting";
    }

    public boolean hasCustomName() {
        return false;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public ItemStack removeStackFromSlot(int i) {
        return ItemStackHelper.getAndRemove(this.stackList, i);
    }

    public ItemStack decrStackSize(int i, int j) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, i, j);

        if (!itemstack.isEmpty()) {
            this.eventHandler.onCraftMatrixChanged((IInventory) this);
        }

        return itemstack;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.stackList.set(i, itemstack);
        this.eventHandler.onCraftMatrixChanged((IInventory) this);
    }

    public int getInventoryStackLimit() {
        return 64;
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
        this.stackList.clear();
    }

    public int getHeight() {
        return this.inventoryHeight;
    }

    public int getWidth() {
        return this.inventoryWidth;
    }

    public void fillStackedContents(RecipeItemHelper autorecipestackmanager) {
        Iterator iterator = this.stackList.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            autorecipestackmanager.accountStack(itemstack);
        }

    }
}

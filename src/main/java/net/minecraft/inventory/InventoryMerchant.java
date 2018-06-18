package net.minecraft.inventory;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import java.util.List;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryMerchant implements IInventory {

    private final IMerchant merchant;
    private final NonNullList<ItemStack> slots;
    private final EntityPlayer player;
    private MerchantRecipe currentRecipe;
    public int currentRecipeIndex;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.slots;
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
        return (merchant instanceof EntityVillager) ? (CraftVillager) ((EntityVillager) this.merchant).getBukkitEntity() : null;
    }

    @Override
    public Location getLocation() {
        return (merchant instanceof EntityVillager) ? ((EntityVillager) this.merchant).getBukkitEntity().getLocation() : null;
    }
    // CraftBukkit end

    public InventoryMerchant(EntityPlayer entityhuman, IMerchant imerchant) {
        this.slots = NonNullList.withSize(3, ItemStack.EMPTY);
        this.player = entityhuman;
        this.merchant = imerchant;
    }

    public int getSizeInventory() {
        return this.slots.size();
    }

    public boolean isEmpty() {
        Iterator iterator = this.slots.iterator();

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
        return (ItemStack) this.slots.get(i);
    }

    public ItemStack decrStackSize(int i, int j) {
        ItemStack itemstack = (ItemStack) this.slots.get(i);

        if (i == 2 && !itemstack.isEmpty()) {
            return ItemStackHelper.getAndSplit(this.slots, i, itemstack.getCount());
        } else {
            ItemStack itemstack1 = ItemStackHelper.getAndSplit(this.slots, i, j);

            if (!itemstack1.isEmpty() && this.inventoryResetNeededOnSlotChange(i)) {
                this.resetRecipeAndSlots();
            }

            return itemstack1;
        }
    }

    private boolean inventoryResetNeededOnSlotChange(int i) {
        return i == 0 || i == 1;
    }

    public ItemStack removeStackFromSlot(int i) {
        return ItemStackHelper.getAndRemove(this.slots, i);
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.slots.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }

        if (this.inventoryResetNeededOnSlotChange(i)) {
            this.resetRecipeAndSlots();
        }

    }

    public String getName() {
        return "mob.villager";
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

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.merchant.getCustomer() == entityhuman;
    }

    public void openInventory(EntityPlayer entityhuman) {}

    public void closeInventory(EntityPlayer entityhuman) {}

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public void markDirty() {
        this.resetRecipeAndSlots();
    }

    public void resetRecipeAndSlots() {
        this.currentRecipe = null;
        ItemStack itemstack = (ItemStack) this.slots.get(0);
        ItemStack itemstack1 = (ItemStack) this.slots.get(1);

        if (itemstack.isEmpty()) {
            itemstack = itemstack1;
            itemstack1 = ItemStack.EMPTY;
        }

        if (itemstack.isEmpty()) {
            this.setInventorySlotContents(2, ItemStack.EMPTY);
        } else {
            MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.player);

            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);

                if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                    this.currentRecipe = merchantrecipe;
                    this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                } else if (!itemstack1.isEmpty()) {
                    merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantrecipe;
                        this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                    } else {
                        this.setInventorySlotContents(2, ItemStack.EMPTY);
                    }
                } else {
                    this.setInventorySlotContents(2, ItemStack.EMPTY);
                }
            }

            this.merchant.verifySellingItem(this.getStackInSlot(2));
        }

    }

    public MerchantRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    public void setCurrentRecipeIndex(int i) {
        this.currentRecipeIndex = i;
        this.resetRecipeAndSlots();
    }

    public int getField(int i) {
        return 0;
    }

    public void setField(int i, int j) {}

    public int getFieldCount() {
        return 0;
    }

    public void clear() {
        this.slots.clear();
    }
}

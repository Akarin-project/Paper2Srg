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

    private final NonNullList<ItemStack> field_70467_a;
    private IRecipe field_193057_b;

    // CraftBukkit start
    private int maxStack = MAX_STACK;

    public java.util.List<ItemStack> getContents() {
        return this.field_70467_a;
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
        this.field_70467_a = NonNullList.func_191197_a(1, ItemStack.field_190927_a);
    }

    public int func_70302_i_() {
        return 1;
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_70467_a.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public ItemStack func_70301_a(int i) {
        return (ItemStack) this.field_70467_a.get(0);
    }

    public String func_70005_c_() {
        return "Result";
    }

    public boolean func_145818_k_() {
        return false;
    }

    public ITextComponent func_145748_c_() {
        return (ITextComponent) (this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public ItemStack func_70298_a(int i, int j) {
        return ItemStackHelper.func_188383_a(this.field_70467_a, 0);
    }

    public ItemStack func_70304_b(int i) {
        return ItemStackHelper.func_188383_a(this.field_70467_a, 0);
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        this.field_70467_a.set(0, itemstack);
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    public void func_70296_d() {}

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return true;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        return true;
    }

    public int func_174887_a_(int i) {
        return 0;
    }

    public void func_174885_b(int i, int j) {}

    public int func_174890_g() {
        return 0;
    }

    public void func_174888_l() {
        this.field_70467_a.clear();
    }

    public void func_193056_a(@Nullable IRecipe irecipe) {
        this.field_193057_b = irecipe;
    }

    @Nullable
    public IRecipe func_193055_i() {
        return this.field_193057_b;
    }
}

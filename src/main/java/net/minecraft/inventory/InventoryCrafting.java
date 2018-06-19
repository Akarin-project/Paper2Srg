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

    private final NonNullList<ItemStack> field_70466_a;
    private final int field_70464_b;
    private final int field_174924_c;
    public final Container field_70465_c;

    // CraftBukkit start - add fields
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    public IRecipe currentRecipe;
    public IInventory resultInventory;
    private EntityPlayer owner;
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.field_70466_a;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public InventoryType getInvType() {
        return field_70466_a.size() == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
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
        this.field_70466_a = NonNullList.func_191197_a(i * j, ItemStack.field_190927_a);
        this.field_70465_c = container;
        this.field_70464_b = i;
        this.field_174924_c = j;
    }

    public int func_70302_i_() {
        return this.field_70466_a.size();
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_70466_a.iterator();

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
        return i >= this.func_70302_i_() ? ItemStack.field_190927_a : (ItemStack) this.field_70466_a.get(i);
    }

    public ItemStack func_70463_b(int i, int j) {
        return i >= 0 && i < this.field_70464_b && j >= 0 && j <= this.field_174924_c ? this.func_70301_a(i + j * this.field_70464_b) : ItemStack.field_190927_a;
    }

    public String func_70005_c_() {
        return "container.crafting";
    }

    public boolean func_145818_k_() {
        return false;
    }

    public ITextComponent func_145748_c_() {
        return (ITextComponent) (this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public ItemStack func_70304_b(int i) {
        return ItemStackHelper.func_188383_a(this.field_70466_a, i);
    }

    public ItemStack func_70298_a(int i, int j) {
        ItemStack itemstack = ItemStackHelper.func_188382_a(this.field_70466_a, i, j);

        if (!itemstack.func_190926_b()) {
            this.field_70465_c.func_75130_a((IInventory) this);
        }

        return itemstack;
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        this.field_70466_a.set(i, itemstack);
        this.field_70465_c.func_75130_a((IInventory) this);
    }

    public int func_70297_j_() {
        return 64;
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
        this.field_70466_a.clear();
    }

    public int func_174923_h() {
        return this.field_174924_c;
    }

    public int func_174922_i() {
        return this.field_70464_b;
    }

    public void func_194018_a(RecipeItemHelper autorecipestackmanager) {
        Iterator iterator = this.field_70466_a.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            autorecipestackmanager.func_194112_a(itemstack);
        }

    }
}

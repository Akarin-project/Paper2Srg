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

    private final String field_70479_a;
    public final ILockableContainer field_70477_b;
    public final ILockableContainer field_70478_c;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();

    public List<ItemStack> getContents() {
        List<ItemStack> result = new ArrayList<ItemStack>(this.func_70302_i_());
        for (int i = 0; i < this.func_70302_i_(); i++) {
            result.add(this.func_70301_a(i));
        }
        return result;
    }

    public void onOpen(CraftHumanEntity who) {
        this.field_70477_b.onOpen(who);
        this.field_70478_c.onOpen(who);
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        this.field_70477_b.onClose(who);
        this.field_70478_c.onClose(who);
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return null; // This method won't be called since CraftInventoryDoubleChest doesn't defer to here
    }

    public void setMaxStackSize(int size) {
        this.field_70477_b.setMaxStackSize(size);
        this.field_70478_c.setMaxStackSize(size);
    }

    @Override
    public Location getLocation() {
        return field_70477_b.getLocation(); // TODO: right?
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, ILockableContainer itileinventory, ILockableContainer itileinventory1) {
        this.field_70479_a = s;
        if (itileinventory == null) {
            itileinventory = itileinventory1;
        }

        if (itileinventory1 == null) {
            itileinventory1 = itileinventory;
        }

        this.field_70477_b = itileinventory;
        this.field_70478_c = itileinventory1;
        if (itileinventory.func_174893_q_()) {
            itileinventory1.func_174892_a(itileinventory.func_174891_i());
        } else if (itileinventory1.func_174893_q_()) {
            itileinventory.func_174892_a(itileinventory1.func_174891_i());
        }

    }

    public int func_70302_i_() {
        return this.field_70477_b.func_70302_i_() + this.field_70478_c.func_70302_i_();
    }

    public boolean func_191420_l() {
        return this.field_70477_b.func_191420_l() && this.field_70478_c.func_191420_l();
    }

    public boolean func_90010_a(IInventory iinventory) {
        return this.field_70477_b == iinventory || this.field_70478_c == iinventory;
    }

    public String func_70005_c_() {
        return this.field_70477_b.func_145818_k_() ? this.field_70477_b.func_70005_c_() : (this.field_70478_c.func_145818_k_() ? this.field_70478_c.func_70005_c_() : this.field_70479_a);
    }

    public boolean func_145818_k_() {
        return this.field_70477_b.func_145818_k_() || this.field_70478_c.func_145818_k_();
    }

    public ITextComponent func_145748_c_() {
        return (ITextComponent) (this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public ItemStack func_70301_a(int i) {
        return i >= this.field_70477_b.func_70302_i_() ? this.field_70478_c.func_70301_a(i - this.field_70477_b.func_70302_i_()) : this.field_70477_b.func_70301_a(i);
    }

    public ItemStack func_70298_a(int i, int j) {
        return i >= this.field_70477_b.func_70302_i_() ? this.field_70478_c.func_70298_a(i - this.field_70477_b.func_70302_i_(), j) : this.field_70477_b.func_70298_a(i, j);
    }

    public ItemStack func_70304_b(int i) {
        return i >= this.field_70477_b.func_70302_i_() ? this.field_70478_c.func_70304_b(i - this.field_70477_b.func_70302_i_()) : this.field_70477_b.func_70304_b(i);
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        if (i >= this.field_70477_b.func_70302_i_()) {
            this.field_70478_c.func_70299_a(i - this.field_70477_b.func_70302_i_(), itemstack);
        } else {
            this.field_70477_b.func_70299_a(i, itemstack);
        }

    }

    public int func_70297_j_() {
        return Math.min(this.field_70477_b.func_70297_j_(), this.field_70478_c.func_70297_j_()); // CraftBukkit - check both sides
    }

    public void func_70296_d() {
        this.field_70477_b.func_70296_d();
        this.field_70478_c.func_70296_d();
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_70477_b.func_70300_a(entityhuman) && this.field_70478_c.func_70300_a(entityhuman);
    }

    public void func_174889_b(EntityPlayer entityhuman) {
        this.field_70477_b.func_174889_b(entityhuman);
        this.field_70478_c.func_174889_b(entityhuman);
    }

    public void func_174886_c(EntityPlayer entityhuman) {
        this.field_70477_b.func_174886_c(entityhuman);
        this.field_70478_c.func_174886_c(entityhuman);
    }

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

    public boolean func_174893_q_() {
        return this.field_70477_b.func_174893_q_() || this.field_70478_c.func_174893_q_();
    }

    public void func_174892_a(LockCode chestlock) {
        this.field_70477_b.func_174892_a(chestlock);
        this.field_70478_c.func_174892_a(chestlock);
    }

    public LockCode func_174891_i() {
        return this.field_70477_b.func_174891_i();
    }

    public String func_174875_k() {
        return this.field_70477_b.func_174875_k();
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerChest(playerinventory, this, entityhuman);
    }

    public void func_174888_l() {
        this.field_70477_b.func_174888_l();
        this.field_70478_c.func_174888_l();
    }
}

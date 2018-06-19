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

    private String field_70483_a;
    private final int field_70481_b;
    public final NonNullList<ItemStack> field_70482_c;
    private List<IInventoryChangedListener> field_70480_d;
    private boolean field_94051_e;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;
    protected org.bukkit.inventory.InventoryHolder bukkitOwner;

    public List<ItemStack> getContents() {
        return this.field_70482_c;
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
        this.field_70483_a = s;
        this.field_94051_e = flag;
        this.field_70481_b = i;
        this.field_70482_c = NonNullList.func_191197_a(i, ItemStack.field_190927_a);
    }

    public void func_110134_a(IInventoryChangedListener iinventorylistener) {
        if (this.field_70480_d == null) {
            this.field_70480_d = Lists.newArrayList();
        }

        this.field_70480_d.add(iinventorylistener);
    }

    public void func_110132_b(IInventoryChangedListener iinventorylistener) {
        this.field_70480_d.remove(iinventorylistener);
    }

    public ItemStack func_70301_a(int i) {
        return i >= 0 && i < this.field_70482_c.size() ? (ItemStack) this.field_70482_c.get(i) : ItemStack.field_190927_a;
    }

    public ItemStack func_70298_a(int i, int j) {
        ItemStack itemstack = ItemStackHelper.func_188382_a(this.field_70482_c, i, j);

        if (!itemstack.func_190926_b()) {
            this.func_70296_d();
        }

        return itemstack;
    }

    public ItemStack func_174894_a(ItemStack itemstack) {
        ItemStack itemstack1 = itemstack.func_77946_l();

        for (int i = 0; i < this.field_70481_b; ++i) {
            ItemStack itemstack2 = this.func_70301_a(i);

            if (itemstack2.func_190926_b()) {
                this.func_70299_a(i, itemstack1);
                this.func_70296_d();
                return ItemStack.field_190927_a;
            }

            if (ItemStack.func_179545_c(itemstack2, itemstack1)) {
                int j = Math.min(this.func_70297_j_(), itemstack2.func_77976_d());
                int k = Math.min(itemstack1.func_190916_E(), j - itemstack2.func_190916_E());

                if (k > 0) {
                    itemstack2.func_190917_f(k);
                    itemstack1.func_190918_g(k);
                    if (itemstack1.func_190926_b()) {
                        this.func_70296_d();
                        return ItemStack.field_190927_a;
                    }
                }
            }
        }

        if (itemstack1.func_190916_E() != itemstack.func_190916_E()) {
            this.func_70296_d();
        }

        return itemstack1;
    }

    public ItemStack func_70304_b(int i) {
        ItemStack itemstack = (ItemStack) this.field_70482_c.get(i);

        if (itemstack.func_190926_b()) {
            return ItemStack.field_190927_a;
        } else {
            this.field_70482_c.set(i, ItemStack.field_190927_a);
            return itemstack;
        }
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        this.field_70482_c.set(i, itemstack);
        if (!itemstack.func_190926_b() && itemstack.func_190916_E() > this.func_70297_j_()) {
            itemstack.func_190920_e(this.func_70297_j_());
        }

        this.func_70296_d();
    }

    public int func_70302_i_() {
        return this.field_70481_b;
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_70482_c.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public String func_70005_c_() {
        return this.field_70483_a;
    }

    public boolean func_145818_k_() {
        return this.field_94051_e;
    }

    public void func_110133_a(String s) {
        this.field_94051_e = true;
        this.field_70483_a = s;
    }

    public ITextComponent func_145748_c_() {
        return (ITextComponent) (this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public int func_70297_j_() {
        return 64;
    }

    public void func_70296_d() {
        if (this.field_70480_d != null) {
            for (int i = 0; i < this.field_70480_d.size(); ++i) {
                ((IInventoryChangedListener) this.field_70480_d.get(i)).func_76316_a(this);
            }
        }

    }

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
        this.field_70482_c.clear();
    }
}

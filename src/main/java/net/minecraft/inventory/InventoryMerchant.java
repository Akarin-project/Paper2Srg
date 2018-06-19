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

    private final IMerchant field_70476_a;
    private final NonNullList<ItemStack> field_70474_b;
    private final EntityPlayer field_70475_c;
    private MerchantRecipe field_70472_d;
    public int field_70473_e;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.field_70474_b;
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
        return (field_70476_a instanceof EntityVillager) ? (CraftVillager) ((EntityVillager) this.field_70476_a).getBukkitEntity() : null;
    }

    @Override
    public Location getLocation() {
        return (field_70476_a instanceof EntityVillager) ? ((EntityVillager) this.field_70476_a).getBukkitEntity().getLocation() : null;
    }
    // CraftBukkit end

    public InventoryMerchant(EntityPlayer entityhuman, IMerchant imerchant) {
        this.field_70474_b = NonNullList.func_191197_a(3, ItemStack.field_190927_a);
        this.field_70475_c = entityhuman;
        this.field_70476_a = imerchant;
    }

    public int func_70302_i_() {
        return this.field_70474_b.size();
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_70474_b.iterator();

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
        return (ItemStack) this.field_70474_b.get(i);
    }

    public ItemStack func_70298_a(int i, int j) {
        ItemStack itemstack = (ItemStack) this.field_70474_b.get(i);

        if (i == 2 && !itemstack.func_190926_b()) {
            return ItemStackHelper.func_188382_a(this.field_70474_b, i, itemstack.func_190916_E());
        } else {
            ItemStack itemstack1 = ItemStackHelper.func_188382_a(this.field_70474_b, i, j);

            if (!itemstack1.func_190926_b() && this.func_70469_d(i)) {
                this.func_70470_g();
            }

            return itemstack1;
        }
    }

    private boolean func_70469_d(int i) {
        return i == 0 || i == 1;
    }

    public ItemStack func_70304_b(int i) {
        return ItemStackHelper.func_188383_a(this.field_70474_b, i);
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        this.field_70474_b.set(i, itemstack);
        if (!itemstack.func_190926_b() && itemstack.func_190916_E() > this.func_70297_j_()) {
            itemstack.func_190920_e(this.func_70297_j_());
        }

        if (this.func_70469_d(i)) {
            this.func_70470_g();
        }

    }

    public String func_70005_c_() {
        return "mob.villager";
    }

    public boolean func_145818_k_() {
        return false;
    }

    public ITextComponent func_145748_c_() {
        return (ITextComponent) (this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_70476_a.func_70931_l_() == entityhuman;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        return true;
    }

    public void func_70296_d() {
        this.func_70470_g();
    }

    public void func_70470_g() {
        this.field_70472_d = null;
        ItemStack itemstack = (ItemStack) this.field_70474_b.get(0);
        ItemStack itemstack1 = (ItemStack) this.field_70474_b.get(1);

        if (itemstack.func_190926_b()) {
            itemstack = itemstack1;
            itemstack1 = ItemStack.field_190927_a;
        }

        if (itemstack.func_190926_b()) {
            this.func_70299_a(2, ItemStack.field_190927_a);
        } else {
            MerchantRecipeList merchantrecipelist = this.field_70476_a.func_70934_b(this.field_70475_c);

            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.func_77203_a(itemstack, itemstack1, this.field_70473_e);

                if (merchantrecipe != null && !merchantrecipe.func_82784_g()) {
                    this.field_70472_d = merchantrecipe;
                    this.func_70299_a(2, merchantrecipe.func_77397_d().func_77946_l());
                } else if (!itemstack1.func_190926_b()) {
                    merchantrecipe = merchantrecipelist.func_77203_a(itemstack1, itemstack, this.field_70473_e);
                    if (merchantrecipe != null && !merchantrecipe.func_82784_g()) {
                        this.field_70472_d = merchantrecipe;
                        this.func_70299_a(2, merchantrecipe.func_77397_d().func_77946_l());
                    } else {
                        this.func_70299_a(2, ItemStack.field_190927_a);
                    }
                } else {
                    this.func_70299_a(2, ItemStack.field_190927_a);
                }
            }

            this.field_70476_a.func_110297_a_(this.func_70301_a(2));
        }

    }

    public MerchantRecipe func_70468_h() {
        return this.field_70472_d;
    }

    public void func_70471_c(int i) {
        this.field_70473_e = i;
        this.func_70470_g();
    }

    public int func_174887_a_(int i) {
        return 0;
    }

    public void func_174885_b(int i, int j) {}

    public int func_174890_g() {
        return 0;
    }

    public void func_174888_l() {
        this.field_70474_b.clear();
    }
}

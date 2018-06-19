package net.minecraft.inventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;


public class Slot {

    public final int field_75225_a;
    public final IInventory field_75224_c;
    public int field_75222_d;
    public int field_75223_e;
    public int field_75221_f;

    public Slot(IInventory iinventory, int i, int j, int k) {
        this.field_75224_c = iinventory;
        this.field_75225_a = i;
        this.field_75223_e = j;
        this.field_75221_f = k;
    }

    public void func_75220_a(ItemStack itemstack, ItemStack itemstack1) {
        int i = itemstack1.func_190916_E() - itemstack.func_190916_E();

        if (i > 0) {
            this.func_75210_a(itemstack1, i);
        }

    }

    protected void func_75210_a(ItemStack itemstack, int i) {}

    protected void func_190900_b(int i) {}

    protected void func_75208_c(ItemStack itemstack) {}

    public ItemStack func_190901_a(EntityPlayer entityhuman, ItemStack itemstack) {
        this.func_75218_e();
        return itemstack;
    }

    public boolean func_75214_a(ItemStack itemstack) {
        return true;
    }

    public ItemStack func_75211_c() {
        return this.field_75224_c.func_70301_a(this.field_75225_a);
    }

    public boolean func_75216_d() {
        return !this.func_75211_c().func_190926_b();
    }

    public void func_75215_d(ItemStack itemstack) {
        this.field_75224_c.func_70299_a(this.field_75225_a, itemstack);
        this.func_75218_e();
    }

    public void func_75218_e() {
        this.field_75224_c.func_70296_d();
    }

    public int func_75219_a() {
        return this.field_75224_c.func_70297_j_();
    }

    public int func_178170_b(ItemStack itemstack) {
        return this.func_75219_a();
    }

    public ItemStack func_75209_a(int i) {
        return this.field_75224_c.func_70298_a(this.field_75225_a, i);
    }

    public boolean func_75217_a(IInventory iinventory, int i) {
        return iinventory == this.field_75224_c && i == this.field_75225_a;
    }

    public boolean func_82869_a(EntityPlayer entityhuman) {
        return true;
    }
}

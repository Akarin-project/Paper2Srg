package net.minecraft.village;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.inventory.CraftMerchantRecipe;

public class MerchantRecipe {

    public ItemStack field_77403_a;
    public ItemStack field_77401_b;
    public ItemStack field_77402_c;
    public int field_77400_d;
    public int field_82786_e;
    public boolean field_180323_f;
    // CraftBukkit start
    private CraftMerchantRecipe bukkitHandle;

    public CraftMerchantRecipe asBukkit() {
        return (bukkitHandle == null) ? bukkitHandle = new CraftMerchantRecipe(this) : bukkitHandle;
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j, CraftMerchantRecipe bukkit) {
        this(itemstack, itemstack1, itemstack2, i, j);
        this.bukkitHandle = bukkit;
    }
    // CraftBukkit end

    public MerchantRecipe(NBTTagCompound nbttagcompound) {
        this.field_77403_a = ItemStack.field_190927_a;
        this.field_77401_b = ItemStack.field_190927_a;
        this.field_77402_c = ItemStack.field_190927_a;
        this.func_77390_a(nbttagcompound);
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2) {
        this(itemstack, itemstack1, itemstack2, 0, 7);
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j) {
        this.field_77403_a = ItemStack.field_190927_a;
        this.field_77401_b = ItemStack.field_190927_a;
        this.field_77402_c = ItemStack.field_190927_a;
        this.field_77403_a = itemstack;
        this.field_77401_b = itemstack1;
        this.field_77402_c = itemstack2;
        this.field_77400_d = i;
        this.field_82786_e = j;
        this.field_180323_f = true;
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1) {
        this(itemstack, ItemStack.field_190927_a, itemstack1);
    }

    public MerchantRecipe(ItemStack itemstack, Item item) {
        this(itemstack, new ItemStack(item));
    }

    public ItemStack func_77394_a() {
        return this.field_77403_a;
    }

    public ItemStack func_77396_b() {
        return this.field_77401_b;
    }

    public boolean func_77398_c() {
        return !this.field_77401_b.func_190926_b();
    }

    public ItemStack func_77397_d() {
        return this.field_77402_c;
    }

    public int func_180321_e() {
        return this.field_77400_d;
    }

    public int func_180320_f() {
        return this.field_82786_e;
    }

    public void func_77399_f() {
        ++this.field_77400_d;
    }

    public void func_82783_a(int i) {
        this.field_82786_e += i;
    }

    public boolean func_82784_g() {
        return this.field_77400_d >= this.field_82786_e;
    }

    public boolean func_180322_j() {
        return this.field_180323_f;
    }

    public void func_77390_a(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("buy");

        this.field_77403_a = new ItemStack(nbttagcompound1);
        NBTTagCompound nbttagcompound2 = nbttagcompound.func_74775_l("sell");

        this.field_77402_c = new ItemStack(nbttagcompound2);
        if (nbttagcompound.func_150297_b("buyB", 10)) {
            this.field_77401_b = new ItemStack(nbttagcompound.func_74775_l("buyB"));
        }

        if (nbttagcompound.func_150297_b("uses", 99)) {
            this.field_77400_d = nbttagcompound.func_74762_e("uses");
        }

        if (nbttagcompound.func_150297_b("maxUses", 99)) {
            this.field_82786_e = nbttagcompound.func_74762_e("maxUses");
        } else {
            this.field_82786_e = 7;
        }

        if (nbttagcompound.func_150297_b("rewardExp", 1)) {
            this.field_180323_f = nbttagcompound.func_74767_n("rewardExp");
        } else {
            this.field_180323_f = true;
        }

    }

    public NBTTagCompound func_77395_g() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74782_a("buy", this.field_77403_a.func_77955_b(new NBTTagCompound()));
        nbttagcompound.func_74782_a("sell", this.field_77402_c.func_77955_b(new NBTTagCompound()));
        if (!this.field_77401_b.func_190926_b()) {
            nbttagcompound.func_74782_a("buyB", this.field_77401_b.func_77955_b(new NBTTagCompound()));
        }

        nbttagcompound.func_74768_a("uses", this.field_77400_d);
        nbttagcompound.func_74768_a("maxUses", this.field_82786_e);
        nbttagcompound.func_74757_a("rewardExp", this.field_180323_f);
        return nbttagcompound;
    }
}

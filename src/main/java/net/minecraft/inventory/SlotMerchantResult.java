package net.minecraft.inventory;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;


public class SlotMerchantResult extends Slot {

    private final InventoryMerchant field_75233_a;
    private final EntityPlayer field_75232_b;
    private int field_75231_g;
    private final IMerchant field_75234_h;

    public SlotMerchantResult(EntityPlayer entityhuman, IMerchant imerchant, InventoryMerchant inventorymerchant, int i, int j, int k) {
        super(inventorymerchant, i, j, k);
        this.field_75232_b = entityhuman;
        this.field_75234_h = imerchant;
        this.field_75233_a = inventorymerchant;
    }

    public boolean func_75214_a(ItemStack itemstack) {
        return false;
    }

    public ItemStack func_75209_a(int i) {
        if (this.func_75216_d()) {
            this.field_75231_g += Math.min(i, this.func_75211_c().func_190916_E());
        }

        return super.func_75209_a(i);
    }

    protected void func_75210_a(ItemStack itemstack, int i) {
        this.field_75231_g += i;
        this.func_75208_c(itemstack);
    }

    protected void func_75208_c(ItemStack itemstack) {
        itemstack.func_77980_a(this.field_75232_b.field_70170_p, this.field_75232_b, this.field_75231_g);
        this.field_75231_g = 0;
    }

    public ItemStack func_190901_a(EntityPlayer entityhuman, ItemStack itemstack) {
        this.func_75208_c(itemstack);
        MerchantRecipe merchantrecipe = this.field_75233_a.func_70468_h();

        if (merchantrecipe != null) {
            ItemStack itemstack1 = this.field_75233_a.func_70301_a(0);
            ItemStack itemstack2 = this.field_75233_a.func_70301_a(1);

            if (this.func_75230_a(merchantrecipe, itemstack1, itemstack2) || this.func_75230_a(merchantrecipe, itemstack2, itemstack1)) {
                this.field_75234_h.func_70933_a(merchantrecipe);
                entityhuman.func_71029_a(StatList.field_188075_I);
                this.field_75233_a.func_70299_a(0, itemstack1);
                this.field_75233_a.func_70299_a(1, itemstack2);
            }
        }

        return itemstack;
    }

    private boolean func_75230_a(MerchantRecipe merchantrecipe, ItemStack itemstack, ItemStack itemstack1) {
        ItemStack itemstack2 = merchantrecipe.func_77394_a();
        ItemStack itemstack3 = merchantrecipe.func_77396_b();

        if (itemstack.func_77973_b() == itemstack2.func_77973_b() && itemstack.func_190916_E() >= itemstack2.func_190916_E()) {
            if (!itemstack3.func_190926_b() && !itemstack1.func_190926_b() && itemstack3.func_77973_b() == itemstack1.func_77973_b() && itemstack1.func_190916_E() >= itemstack3.func_190916_E()) {
                itemstack.func_190918_g(itemstack2.func_190916_E());
                itemstack1.func_190918_g(itemstack3.func_190916_E());
                return true;
            }

            if (itemstack3.func_190926_b() && itemstack1.func_190926_b()) {
                itemstack.func_190918_g(itemstack2.func_190916_E());
                return true;
            }
        }

        return false;
    }
}

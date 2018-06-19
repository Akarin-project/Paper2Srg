package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


public class ItemAppleGold extends ItemFood {

    public ItemAppleGold(int i, float f, boolean flag) {
        super(i, f, flag);
        this.func_77627_a(true);
    }

    public EnumRarity func_77613_e(ItemStack itemstack) {
        return itemstack.func_77960_j() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
    }

    protected void func_77849_c(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        if (!world.field_72995_K) {
            if (itemstack.func_77960_j() > 0) {
                entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76428_l, 400, 1));
                entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76429_m, 6000, 0));
                entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76426_n, 6000, 0));
                entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76444_x, 2400, 3));
            } else {
                entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76428_l, 100, 1));
                entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76444_x, 2400, 0));
            }
        }

    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            nonnulllist.add(new ItemStack(this));
            nonnulllist.add(new ItemStack(this, 1, 1));
        }

    }
}

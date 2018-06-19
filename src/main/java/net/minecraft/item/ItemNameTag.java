package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;


public class ItemNameTag extends Item {

    public ItemNameTag() {
        this.func_77637_a(CreativeTabs.field_78040_i);
    }

    public boolean func_111207_a(ItemStack itemstack, EntityPlayer entityhuman, EntityLivingBase entityliving, EnumHand enumhand) {
        if (itemstack.func_82837_s() && !(entityliving instanceof EntityPlayer)) {
            entityliving.func_96094_a(itemstack.func_82833_r());
            if (entityliving instanceof EntityLiving) {
                ((EntityLiving) entityliving).func_110163_bv();
            }

            itemstack.func_190918_g(1);
            return true;
        } else {
            return false;
        }
    }
}

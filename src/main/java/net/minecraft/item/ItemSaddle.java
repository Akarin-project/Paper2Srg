package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;


public class ItemSaddle extends Item {

    public ItemSaddle() {
        this.field_77777_bU = 1;
        this.func_77637_a(CreativeTabs.field_78029_e);
    }

    public boolean func_111207_a(ItemStack itemstack, EntityPlayer entityhuman, EntityLivingBase entityliving, EnumHand enumhand) {
        if (entityliving instanceof EntityPig) {
            EntityPig entitypig = (EntityPig) entityliving;

            if (!entitypig.func_70901_n() && !entitypig.func_70631_g_()) {
                entitypig.func_70900_e(true);
                entitypig.field_70170_p.func_184148_a(entityhuman, entitypig.field_70165_t, entitypig.field_70163_u, entitypig.field_70161_v, SoundEvents.field_187706_dO, SoundCategory.NEUTRAL, 0.5F, 1.0F);
                itemstack.func_190918_g(1);
            }

            return true;
        } else {
            return false;
        }
    }
}

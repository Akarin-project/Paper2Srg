package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;


public class ItemNameTag extends Item {

    public ItemNameTag() {
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer entityhuman, EntityLivingBase entityliving, EnumHand enumhand) {
        if (itemstack.hasDisplayName() && !(entityliving instanceof EntityPlayer)) {
            entityliving.setCustomNameTag(itemstack.getDisplayName());
            if (entityliving instanceof EntityLiving) {
                ((EntityLiving) entityliving).enablePersistence();
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }
}

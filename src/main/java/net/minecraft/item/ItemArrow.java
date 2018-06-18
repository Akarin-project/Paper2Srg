package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;


public class ItemArrow extends Item {

    public ItemArrow() {
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    public EntityArrow createArrow(World world, ItemStack itemstack, EntityLivingBase entityliving) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, entityliving);

        entitytippedarrow.setPotionEffect(itemstack);
        return entitytippedarrow;
    }
}

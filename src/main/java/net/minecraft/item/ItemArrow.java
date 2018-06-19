package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;


public class ItemArrow extends Item {

    public ItemArrow() {
        this.func_77637_a(CreativeTabs.field_78037_j);
    }

    public EntityArrow func_185052_a(World world, ItemStack itemstack, EntityLivingBase entityliving) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, entityliving);

        entitytippedarrow.func_184555_a(itemstack);
        return entitytippedarrow;
    }
}

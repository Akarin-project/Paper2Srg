package net.minecraft.item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.world.World;


public class ItemSpectralArrow extends ItemArrow {

    public ItemSpectralArrow() {}

    public EntityArrow func_185052_a(World world, ItemStack itemstack, EntityLivingBase entityliving) {
        return new EntitySpectralArrow(world, entityliving);
    }
}

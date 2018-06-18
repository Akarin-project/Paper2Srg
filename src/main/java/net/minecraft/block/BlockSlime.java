package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockSlime extends BlockBreakable {

    public BlockSlime() {
        super(Material.CLAY, false, MapColor.GRASS);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.slipperiness = 0.8F;
    }

    public void onFallenUpon(World world, BlockPos blockposition, Entity entity, float f) {
        if (entity.isSneaking()) {
            super.onFallenUpon(world, blockposition, entity, f);
        } else {
            entity.fall(f, 0.0F);
        }

    }

    public void onLanded(World world, Entity entity) {
        if (entity.isSneaking()) {
            super.onLanded(world, entity);
        } else if (entity.motionY < 0.0D) {
            entity.motionY = -entity.motionY;
            if (!(entity instanceof EntityLivingBase)) {
                entity.motionY *= 0.8D;
            }
        }

    }

    public void onEntityWalk(World world, BlockPos blockposition, Entity entity) {
        if (Math.abs(entity.motionY) < 0.1D && !entity.isSneaking()) {
            double d0 = 0.4D + Math.abs(entity.motionY) * 0.2D;

            entity.motionX *= d0;
            entity.motionZ *= d0;
        }

        super.onEntityWalk(world, blockposition, entity);
    }
}

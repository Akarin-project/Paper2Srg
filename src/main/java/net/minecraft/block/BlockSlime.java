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
        super(Material.field_151571_B, false, MapColor.field_151661_c);
        this.func_149647_a(CreativeTabs.field_78031_c);
        this.field_149765_K = 0.8F;
    }

    public void func_180658_a(World world, BlockPos blockposition, Entity entity, float f) {
        if (entity.func_70093_af()) {
            super.func_180658_a(world, blockposition, entity, f);
        } else {
            entity.func_180430_e(f, 0.0F);
        }

    }

    public void func_176216_a(World world, Entity entity) {
        if (entity.func_70093_af()) {
            super.func_176216_a(world, entity);
        } else if (entity.field_70181_x < 0.0D) {
            entity.field_70181_x = -entity.field_70181_x;
            if (!(entity instanceof EntityLivingBase)) {
                entity.field_70181_x *= 0.8D;
            }
        }

    }

    public void func_176199_a(World world, BlockPos blockposition, Entity entity) {
        if (Math.abs(entity.field_70181_x) < 0.1D && !entity.func_70093_af()) {
            double d0 = 0.4D + Math.abs(entity.field_70181_x) * 0.2D;

            entity.field_70159_w *= d0;
            entity.field_70179_y *= d0;
        }

        super.func_176199_a(world, blockposition, entity);
    }
}

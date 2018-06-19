package net.minecraft.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor {

    public SwimNodeProcessor() {}

    public PathPoint func_186318_b() {
        return this.func_176159_a(MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72340_a), MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72338_b + 0.5D), MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72339_c));
    }

    public PathPoint func_186325_a(double d0, double d1, double d2) {
        return this.func_176159_a(MathHelper.func_76128_c(d0 - (double) (this.field_186326_b.field_70130_N / 2.0F)), MathHelper.func_76128_c(d1 + 0.5D), MathHelper.func_76128_c(d2 - (double) (this.field_186326_b.field_70130_N / 2.0F)));
    }

    public int func_186320_a(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f) {
        int i = 0;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumFacing enumdirection = aenumdirection[k];
            PathPoint pathpoint2 = this.func_186328_b(pathpoint.field_75839_a + enumdirection.func_82601_c(), pathpoint.field_75837_b + enumdirection.func_96559_d(), pathpoint.field_75838_c + enumdirection.func_82599_e());

            if (pathpoint2 != null && !pathpoint2.field_75842_i && pathpoint2.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint2;
            }
        }

        return i;
    }

    public PathNodeType func_186319_a(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1) {
        return PathNodeType.WATER;
    }

    public PathNodeType func_186330_a(IBlockAccess iblockaccess, int i, int j, int k) {
        return PathNodeType.WATER;
    }

    @Nullable
    private PathPoint func_186328_b(int i, int j, int k) {
        PathNodeType pathtype = this.func_186327_c(i, j, k);

        return pathtype == PathNodeType.WATER ? this.func_176159_a(i, j, k) : null;
    }

    private PathNodeType func_186327_c(int i, int j, int k) {
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int l = i; l < i + this.field_176168_c; ++l) {
            for (int i1 = j; i1 < j + this.field_176165_d; ++i1) {
                for (int j1 = k; j1 < k + this.field_176166_e; ++j1) {
                    IBlockState iblockdata = this.field_176169_a.func_180495_p(blockposition_mutableblockposition.func_181079_c(l, i1, j1));

                    if (iblockdata.func_185904_a() != Material.field_151586_h) {
                        return PathNodeType.BLOCKED;
                    }
                }
            }
        }

        return PathNodeType.WATER;
    }
}

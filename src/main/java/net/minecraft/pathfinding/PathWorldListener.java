package net.minecraft.pathfinding;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;

public class PathWorldListener implements IWorldEventListener {

    private final List<PathNavigate> field_189519_a = Lists.newArrayList();

    public PathWorldListener() {}

    public void func_184376_a(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i) {
        if (this.func_184378_a(world, blockposition, iblockdata, iblockdata1)) {
            int j = 0;

            for (int k = this.field_189519_a.size(); j < k; ++j) {
                PathNavigate navigationabstract = (PathNavigate) this.field_189519_a.get(j);

                if (navigationabstract != null && !navigationabstract.func_188553_i()) {
                    Path pathentity = navigationabstract.func_75505_d();

                    if (pathentity != null && !pathentity.func_75879_b() && pathentity.func_75874_d() != 0) {
                        PathPoint pathpoint = navigationabstract.field_75514_c.func_75870_c();
                        double d0 = blockposition.func_177954_c(((double) pathpoint.field_75839_a + navigationabstract.field_75515_a.field_70165_t) / 2.0D, ((double) pathpoint.field_75837_b + navigationabstract.field_75515_a.field_70163_u) / 2.0D, ((double) pathpoint.field_75838_c + navigationabstract.field_75515_a.field_70161_v) / 2.0D);
                        int l = (pathentity.func_75874_d() - pathentity.func_75873_e()) * (pathentity.func_75874_d() - pathentity.func_75873_e());

                        if (d0 < (double) l) {
                            navigationabstract.func_188554_j();
                        }
                    }
                }
            }

        }
    }

    protected boolean func_184378_a(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {
        AxisAlignedBB axisalignedbb = iblockdata.func_185890_d(world, blockposition);
        AxisAlignedBB axisalignedbb1 = iblockdata1.func_185890_d(world, blockposition);

        return axisalignedbb != axisalignedbb1 && (axisalignedbb == null || !axisalignedbb.equals(axisalignedbb1));
    }

    public void func_174959_b(BlockPos blockposition) {}

    public void func_147585_a(int i, int j, int k, int l, int i1, int j1) {}

    public void func_184375_a(@Nullable EntityPlayer entityhuman, SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {}

    public void func_180442_a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void func_190570_a(int i, boolean flag, boolean flag1, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void func_72703_a(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.field_189519_a.add(((EntityLiving) entity).func_70661_as());
        }

    }

    public void func_72709_b(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.field_189519_a.remove(((EntityLiving) entity).func_70661_as());
        }

    }

    public void func_184377_a(SoundEvent soundeffect, BlockPos blockposition) {}

    public void func_180440_a(int i, BlockPos blockposition, int j) {}

    public void func_180439_a(EntityPlayer entityhuman, int i, BlockPos blockposition, int j) {}

    public void func_180441_b(int i, BlockPos blockposition, int j) {}
}

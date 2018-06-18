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

    private final List<PathNavigate> navigations = Lists.newArrayList();

    public PathWorldListener() {}

    public void notifyBlockUpdate(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i) {
        if (this.didBlockChange(world, blockposition, iblockdata, iblockdata1)) {
            int j = 0;

            for (int k = this.navigations.size(); j < k; ++j) {
                PathNavigate navigationabstract = (PathNavigate) this.navigations.get(j);

                if (navigationabstract != null && !navigationabstract.canUpdatePathOnTimeout()) {
                    Path pathentity = navigationabstract.getPath();

                    if (pathentity != null && !pathentity.isFinished() && pathentity.getCurrentPathLength() != 0) {
                        PathPoint pathpoint = navigationabstract.currentPath.getFinalPathPoint();
                        double d0 = blockposition.distanceSq(((double) pathpoint.x + navigationabstract.entity.posX) / 2.0D, ((double) pathpoint.y + navigationabstract.entity.posY) / 2.0D, ((double) pathpoint.z + navigationabstract.entity.posZ) / 2.0D);
                        int l = (pathentity.getCurrentPathLength() - pathentity.getCurrentPathIndex()) * (pathentity.getCurrentPathLength() - pathentity.getCurrentPathIndex());

                        if (d0 < (double) l) {
                            navigationabstract.updatePath();
                        }
                    }
                }
            }

        }
    }

    protected boolean didBlockChange(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {
        AxisAlignedBB axisalignedbb = iblockdata.getCollisionBoundingBox(world, blockposition);
        AxisAlignedBB axisalignedbb1 = iblockdata1.getCollisionBoundingBox(world, blockposition);

        return axisalignedbb != axisalignedbb1 && (axisalignedbb == null || !axisalignedbb.equals(axisalignedbb1));
    }

    public void notifyLightSet(BlockPos blockposition) {}

    public void markBlockRangeForRenderUpdate(int i, int j, int k, int l, int i1, int j1) {}

    public void playSoundToAllNearExcept(@Nullable EntityPlayer entityhuman, SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {}

    public void spawnParticle(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void spawnParticle(int i, boolean flag, boolean flag1, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void onEntityAdded(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.navigations.add(((EntityLiving) entity).getNavigator());
        }

    }

    public void onEntityRemoved(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.navigations.remove(((EntityLiving) entity).getNavigator());
        }

    }

    public void playRecord(SoundEvent soundeffect, BlockPos blockposition) {}

    public void broadcastSound(int i, BlockPos blockposition, int j) {}

    public void playEvent(EntityPlayer entityhuman, int i, BlockPos blockposition, int j) {}

    public void sendBlockBreakProgress(int i, BlockPos blockposition, int j) {}
}

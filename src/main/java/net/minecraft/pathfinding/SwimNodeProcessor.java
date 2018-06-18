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

    public PathPoint getStart() {
        return this.openPoint(MathHelper.floor(this.entity.getEntityBoundingBox().minX), MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.getEntityBoundingBox().minZ));
    }

    public PathPoint getPathPointToCoords(double d0, double d1, double d2) {
        return this.openPoint(MathHelper.floor(d0 - (double) (this.entity.width / 2.0F)), MathHelper.floor(d1 + 0.5D), MathHelper.floor(d2 - (double) (this.entity.width / 2.0F)));
    }

    public int findPathOptions(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f) {
        int i = 0;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumFacing enumdirection = aenumdirection[k];
            PathPoint pathpoint2 = this.getWaterNode(pathpoint.x + enumdirection.getFrontOffsetX(), pathpoint.y + enumdirection.getFrontOffsetY(), pathpoint.z + enumdirection.getFrontOffsetZ());

            if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint2;
            }
        }

        return i;
    }

    public PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1) {
        return PathNodeType.WATER;
    }

    public PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k) {
        return PathNodeType.WATER;
    }

    @Nullable
    private PathPoint getWaterNode(int i, int j, int k) {
        PathNodeType pathtype = this.isFree(i, j, k);

        return pathtype == PathNodeType.WATER ? this.openPoint(i, j, k) : null;
    }

    private PathNodeType isFree(int i, int j, int k) {
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int l = i; l < i + this.entitySizeX; ++l) {
            for (int i1 = j; i1 < j + this.entitySizeY; ++i1) {
                for (int j1 = k; j1 < k + this.entitySizeZ; ++j1) {
                    IBlockState iblockdata = this.blockaccess.getBlockState(blockposition_mutableblockposition.setPos(l, i1, j1));

                    if (iblockdata.getMaterial() != Material.WATER) {
                        return PathNodeType.BLOCKED;
                    }
                }
            }
        }

        return PathNodeType.WATER;
    }
}

package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class FlyingNodeProcessor extends WalkNodeProcessor {

    public FlyingNodeProcessor() {}

    public void init(IBlockAccess iblockaccess, EntityLiving entityinsentient) {
        super.init(iblockaccess, entityinsentient);
        this.avoidsWater = entityinsentient.getPathPriority(PathNodeType.WATER);
    }

    public void postProcess() {
        this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
        super.postProcess();
    }

    public PathPoint getStart() {
        int i;

        if (this.getCanSwim() && this.entity.isInWater()) {
            i = (int) this.entity.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));

            for (Block block = this.blockaccess.getBlockState(blockposition_mutableblockposition).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState(blockposition_mutableblockposition).getBlock()) {
                ++i;
                blockposition_mutableblockposition.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
            }
        } else {
            i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D);
        }

        BlockPos blockposition = new BlockPos(this.entity);
        PathNodeType pathtype = this.getPathNodeType(this.entity, blockposition.getX(), i, blockposition.getZ());

        if (this.entity.getPathPriority(pathtype) < 0.0F) {
            HashSet hashset = Sets.newHashSet();

            hashset.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double) i, this.entity.getEntityBoundingBox().minZ));
            hashset.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double) i, this.entity.getEntityBoundingBox().maxZ));
            hashset.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double) i, this.entity.getEntityBoundingBox().minZ));
            hashset.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double) i, this.entity.getEntityBoundingBox().maxZ));
            Iterator iterator = hashset.iterator();

            while (iterator.hasNext()) {
                BlockPos blockposition1 = (BlockPos) iterator.next();
                PathNodeType pathtype1 = this.getPathNodeType(this.entity, blockposition1);

                if (this.entity.getPathPriority(pathtype1) >= 0.0F) {
                    return super.openPoint(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
                }
            }
        }

        return super.openPoint(blockposition.getX(), i, blockposition.getZ());
    }

    public PathPoint getPathPointToCoords(double d0, double d1, double d2) {
        return super.openPoint(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
    }

    public int findPathOptions(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f) {
        int i = 0;
        PathPoint pathpoint2 = this.openPoint(pathpoint.x, pathpoint.y, pathpoint.z + 1);
        PathPoint pathpoint3 = this.openPoint(pathpoint.x - 1, pathpoint.y, pathpoint.z);
        PathPoint pathpoint4 = this.openPoint(pathpoint.x + 1, pathpoint.y, pathpoint.z);
        PathPoint pathpoint5 = this.openPoint(pathpoint.x, pathpoint.y, pathpoint.z - 1);
        PathPoint pathpoint6 = this.openPoint(pathpoint.x, pathpoint.y + 1, pathpoint.z);
        PathPoint pathpoint7 = this.openPoint(pathpoint.x, pathpoint.y - 1, pathpoint.z);

        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(pathpoint1) < f) {
            apathpoint[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(pathpoint1) < f) {
            apathpoint[i++] = pathpoint3;
        }

        if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(pathpoint1) < f) {
            apathpoint[i++] = pathpoint4;
        }

        if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(pathpoint1) < f) {
            apathpoint[i++] = pathpoint5;
        }

        if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(pathpoint1) < f) {
            apathpoint[i++] = pathpoint6;
        }

        if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(pathpoint1) < f) {
            apathpoint[i++] = pathpoint7;
        }

        boolean flag = pathpoint5 == null || pathpoint5.costMalus != 0.0F;
        boolean flag1 = pathpoint2 == null || pathpoint2.costMalus != 0.0F;
        boolean flag2 = pathpoint4 == null || pathpoint4.costMalus != 0.0F;
        boolean flag3 = pathpoint3 == null || pathpoint3.costMalus != 0.0F;
        boolean flag4 = pathpoint6 == null || pathpoint6.costMalus != 0.0F;
        boolean flag5 = pathpoint7 == null || pathpoint7.costMalus != 0.0F;
        PathPoint pathpoint8;

        if (flag && flag3) {
            pathpoint8 = this.openPoint(pathpoint.x - 1, pathpoint.y, pathpoint.z - 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag && flag2) {
            pathpoint8 = this.openPoint(pathpoint.x + 1, pathpoint.y, pathpoint.z - 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag3) {
            pathpoint8 = this.openPoint(pathpoint.x - 1, pathpoint.y, pathpoint.z + 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag2) {
            pathpoint8 = this.openPoint(pathpoint.x + 1, pathpoint.y, pathpoint.z + 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag && flag4) {
            pathpoint8 = this.openPoint(pathpoint.x, pathpoint.y + 1, pathpoint.z - 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag4) {
            pathpoint8 = this.openPoint(pathpoint.x, pathpoint.y + 1, pathpoint.z + 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag2 && flag4) {
            pathpoint8 = this.openPoint(pathpoint.x + 1, pathpoint.y + 1, pathpoint.z);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag3 && flag4) {
            pathpoint8 = this.openPoint(pathpoint.x - 1, pathpoint.y + 1, pathpoint.z);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag && flag5) {
            pathpoint8 = this.openPoint(pathpoint.x, pathpoint.y - 1, pathpoint.z - 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag5) {
            pathpoint8 = this.openPoint(pathpoint.x, pathpoint.y - 1, pathpoint.z + 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag2 && flag5) {
            pathpoint8 = this.openPoint(pathpoint.x + 1, pathpoint.y - 1, pathpoint.z);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag3 && flag5) {
            pathpoint8 = this.openPoint(pathpoint.x - 1, pathpoint.y - 1, pathpoint.z);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        return i;
    }

    @Nullable
    protected PathPoint openPoint(int i, int j, int k) {
        PathPoint pathpoint = null;
        PathNodeType pathtype = this.getPathNodeType(this.entity, i, j, k);
        float f = this.entity.getPathPriority(pathtype);

        if (f >= 0.0F) {
            pathpoint = super.openPoint(i, j, k);
            pathpoint.nodeType = pathtype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            if (pathtype == PathNodeType.WALKABLE) {
                ++pathpoint.costMalus;
            }
        }

        return pathtype != PathNodeType.OPEN && pathtype != PathNodeType.WALKABLE ? pathpoint : pathpoint;
    }

    public PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1) {
        EnumSet enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathtype = PathNodeType.BLOCKED;
        BlockPos blockposition = new BlockPos(entityinsentient);

        pathtype = this.getPathNodeType(iblockaccess, i, j, k, l, i1, j1, flag, flag1, enumset, pathtype, blockposition);
        if (enumset.contains(PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        } else {
            PathNodeType pathtype1 = PathNodeType.BLOCKED;
            Iterator iterator = enumset.iterator();

            while (iterator.hasNext()) {
                PathNodeType pathtype2 = (PathNodeType) iterator.next();

                if (entityinsentient.getPathPriority(pathtype2) < 0.0F) {
                    return pathtype2;
                }

                if (entityinsentient.getPathPriority(pathtype2) >= entityinsentient.getPathPriority(pathtype1)) {
                    pathtype1 = pathtype2;
                }
            }

            if (pathtype == PathNodeType.OPEN && entityinsentient.getPathPriority(pathtype1) == 0.0F) {
                return PathNodeType.OPEN;
            } else {
                return pathtype1;
            }
        }
    }

    public PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k) {
        PathNodeType pathtype = this.getPathNodeTypeRaw(iblockaccess, i, j, k);

        if (pathtype == PathNodeType.OPEN && j >= 1) {
            Block block = iblockaccess.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
            PathNodeType pathtype1 = this.getPathNodeTypeRaw(iblockaccess, i, j - 1, k);

            if (pathtype1 != PathNodeType.DAMAGE_FIRE && block != Blocks.MAGMA && pathtype1 != PathNodeType.LAVA) {
                if (pathtype1 == PathNodeType.DAMAGE_CACTUS) {
                    pathtype = PathNodeType.DAMAGE_CACTUS;
                } else {
                    pathtype = pathtype1 != PathNodeType.WALKABLE && pathtype1 != PathNodeType.OPEN && pathtype1 != PathNodeType.WATER ? PathNodeType.WALKABLE : PathNodeType.OPEN;
                }
            } else {
                pathtype = PathNodeType.DAMAGE_FIRE;
            }
        }

        pathtype = this.checkNeighborBlocks(iblockaccess, i, j, k, pathtype);
        return pathtype;
    }

    private PathNodeType getPathNodeType(EntityLiving entityinsentient, BlockPos blockposition) {
        return this.getPathNodeType(entityinsentient, blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    private PathNodeType getPathNodeType(EntityLiving entityinsentient, int i, int j, int k) {
        return this.getPathNodeType(this.blockaccess, i, j, k, entityinsentient, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }
}

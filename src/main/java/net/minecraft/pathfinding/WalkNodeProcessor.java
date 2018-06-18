package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor {

    protected float avoidsWater;

    public WalkNodeProcessor() {}

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
        BlockPos blockposition;

        if (this.getCanSwim() && this.entity.isInWater()) {
            i = (int) this.entity.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));

            for (Block block = this.blockaccess.getBlockState(blockposition_mutableblockposition).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState(blockposition_mutableblockposition).getBlock()) {
                ++i;
                blockposition_mutableblockposition.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
            }
        } else if (this.entity.onGround) {
            i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D);
        } else {
            for (blockposition = new BlockPos(this.entity); (this.blockaccess.getBlockState(blockposition).getMaterial() == Material.AIR || this.blockaccess.getBlockState(blockposition).getBlock().isPassable(this.blockaccess, blockposition)) && blockposition.getY() > 0; blockposition = blockposition.down()) {
                ;
            }

            i = blockposition.up().getY();
        }

        blockposition = new BlockPos(this.entity);
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
                    return this.openPoint(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
                }
            }
        }

        return this.openPoint(blockposition.getX(), i, blockposition.getZ());
    }

    public PathPoint getPathPointToCoords(double d0, double d1, double d2) {
        return this.openPoint(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
    }

    public int findPathOptions(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f) {
        int i = 0;
        int j = 0;
        PathNodeType pathtype = this.getPathNodeType(this.entity, pathpoint.x, pathpoint.y + 1, pathpoint.z);

        if (this.entity.getPathPriority(pathtype) >= 0.0F) {
            j = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
        }

        BlockPos blockposition = (new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z)).down();
        double d0 = (double) pathpoint.y - (1.0D - this.blockaccess.getBlockState(blockposition).getBoundingBox(this.blockaccess, blockposition).maxY);
        PathPoint pathpoint2 = this.getSafePoint(pathpoint.x, pathpoint.y, pathpoint.z + 1, j, d0, EnumFacing.SOUTH);
        PathPoint pathpoint3 = this.getSafePoint(pathpoint.x - 1, pathpoint.y, pathpoint.z, j, d0, EnumFacing.WEST);
        PathPoint pathpoint4 = this.getSafePoint(pathpoint.x + 1, pathpoint.y, pathpoint.z, j, d0, EnumFacing.EAST);
        PathPoint pathpoint5 = this.getSafePoint(pathpoint.x, pathpoint.y, pathpoint.z - 1, j, d0, EnumFacing.NORTH);

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

        boolean flag = pathpoint5 == null || pathpoint5.nodeType == PathNodeType.OPEN || pathpoint5.costMalus != 0.0F;
        boolean flag1 = pathpoint2 == null || pathpoint2.nodeType == PathNodeType.OPEN || pathpoint2.costMalus != 0.0F;
        boolean flag2 = pathpoint4 == null || pathpoint4.nodeType == PathNodeType.OPEN || pathpoint4.costMalus != 0.0F;
        boolean flag3 = pathpoint3 == null || pathpoint3.nodeType == PathNodeType.OPEN || pathpoint3.costMalus != 0.0F;
        PathPoint pathpoint6;

        if (flag && flag3) {
            pathpoint6 = this.getSafePoint(pathpoint.x - 1, pathpoint.y, pathpoint.z - 1, j, d0, EnumFacing.NORTH);
            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        if (flag && flag2) {
            pathpoint6 = this.getSafePoint(pathpoint.x + 1, pathpoint.y, pathpoint.z - 1, j, d0, EnumFacing.NORTH);
            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        if (flag1 && flag3) {
            pathpoint6 = this.getSafePoint(pathpoint.x - 1, pathpoint.y, pathpoint.z + 1, j, d0, EnumFacing.SOUTH);
            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        if (flag1 && flag2) {
            pathpoint6 = this.getSafePoint(pathpoint.x + 1, pathpoint.y, pathpoint.z + 1, j, d0, EnumFacing.SOUTH);
            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        return i;
    }

    @Nullable
    private PathPoint getSafePoint(int i, int j, int k, int l, double d0, EnumFacing enumdirection) {
        PathPoint pathpoint = null;
        BlockPos blockposition = new BlockPos(i, j, k);
        BlockPos blockposition1 = blockposition.down();
        double d1 = (double) j - (1.0D - this.blockaccess.getBlockState(blockposition1).getBoundingBox(this.blockaccess, blockposition1).maxY);

        if (d1 - d0 > 1.125D) {
            return null;
        } else {
            PathNodeType pathtype = this.getPathNodeType(this.entity, i, j, k);
            float f = this.entity.getPathPriority(pathtype);
            double d2 = (double) this.entity.width / 2.0D;

            if (f >= 0.0F) {
                pathpoint = this.openPoint(i, j, k);
                pathpoint.nodeType = pathtype;
                pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            }

            if (pathtype == PathNodeType.WALKABLE) {
                return pathpoint;
            } else {
                if (pathpoint == null && l > 0 && pathtype != PathNodeType.FENCE && pathtype != PathNodeType.TRAPDOOR) {
                    pathpoint = this.getSafePoint(i, j + 1, k, l - 1, d0, enumdirection);
                    if (pathpoint != null && (pathpoint.nodeType == PathNodeType.OPEN || pathpoint.nodeType == PathNodeType.WALKABLE) && this.entity.width < 1.0F) {
                        double d3 = (double) (i - enumdirection.getFrontOffsetX()) + 0.5D;
                        double d4 = (double) (k - enumdirection.getFrontOffsetZ()) + 0.5D;
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(d3 - d2, (double) j + 0.001D, d4 - d2, d3 + d2, (double) ((float) j + this.entity.height), d4 + d2);
                        AxisAlignedBB axisalignedbb1 = this.blockaccess.getBlockState(blockposition).getBoundingBox(this.blockaccess, blockposition);
                        AxisAlignedBB axisalignedbb2 = axisalignedbb.expand(0.0D, axisalignedbb1.maxY - 0.002D, 0.0D);

                        if (this.entity.world.collidesWithAnyBlock(axisalignedbb2)) {
                            pathpoint = null;
                        }
                    }
                }

                if (pathtype == PathNodeType.OPEN) {
                    AxisAlignedBB axisalignedbb3 = new AxisAlignedBB((double) i - d2 + 0.5D, (double) j + 0.001D, (double) k - d2 + 0.5D, (double) i + d2 + 0.5D, (double) ((float) j + this.entity.height), (double) k + d2 + 0.5D);

                    if (this.entity.world.collidesWithAnyBlock(axisalignedbb3)) {
                        return null;
                    }

                    if (this.entity.width >= 1.0F) {
                        PathNodeType pathtype1 = this.getPathNodeType(this.entity, i, j - 1, k);

                        if (pathtype1 == PathNodeType.BLOCKED) {
                            pathpoint = this.openPoint(i, j, k);
                            pathpoint.nodeType = PathNodeType.WALKABLE;
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            return pathpoint;
                        }
                    }

                    int i1 = 0;

                    while (j > 0 && pathtype == PathNodeType.OPEN) {
                        --j;
                        if (i1++ >= this.entity.getMaxFallHeight()) {
                            return null;
                        }

                        pathtype = this.getPathNodeType(this.entity, i, j, k);
                        f = this.entity.getPathPriority(pathtype);
                        if (pathtype != PathNodeType.OPEN && f >= 0.0F) {
                            pathpoint = this.openPoint(i, j, k);
                            pathpoint.nodeType = pathtype;
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            break;
                        }

                        if (f < 0.0F) {
                            return null;
                        }
                    }
                }

                return pathpoint;
            }
        }
    }

    public PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1) {
        EnumSet enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathtype = PathNodeType.BLOCKED;
        double d0 = (double) entityinsentient.width / 2.0D;
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

    public PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k, int l, int i1, int j1, boolean flag, boolean flag1, EnumSet<PathNodeType> enumset, PathNodeType pathtype, BlockPos blockposition) {
        for (int k1 = 0; k1 < l; ++k1) {
            for (int l1 = 0; l1 < i1; ++l1) {
                for (int i2 = 0; i2 < j1; ++i2) {
                    int j2 = k1 + i;
                    int k2 = l1 + j;
                    int l2 = i2 + k;
                    PathNodeType pathtype1 = this.getPathNodeType(iblockaccess, j2, k2, l2);

                    if (pathtype1 == PathNodeType.DOOR_WOOD_CLOSED && flag && flag1) {
                        pathtype1 = PathNodeType.WALKABLE;
                    }

                    if (pathtype1 == PathNodeType.DOOR_OPEN && !flag1) {
                        pathtype1 = PathNodeType.BLOCKED;
                    }

                    if (pathtype1 == PathNodeType.RAIL && !(iblockaccess.getBlockState(blockposition).getBlock() instanceof BlockRailBase) && !(iblockaccess.getBlockState(blockposition.down()).getBlock() instanceof BlockRailBase)) {
                        pathtype1 = PathNodeType.FENCE;
                    }

                    if (k1 == 0 && l1 == 0 && i2 == 0) {
                        pathtype = pathtype1;
                    }

                    enumset.add(pathtype1);
                }
            }
        }

        return pathtype;
    }

    private PathNodeType getPathNodeType(EntityLiving entityinsentient, BlockPos blockposition) {
        return this.getPathNodeType(entityinsentient, blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    private PathNodeType getPathNodeType(EntityLiving entityinsentient, int i, int j, int k) {
        return this.getPathNodeType(this.blockaccess, i, j, k, entityinsentient, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }

    public PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k) {
        PathNodeType pathtype = this.getPathNodeTypeRaw(iblockaccess, i, j, k);

        if (pathtype == PathNodeType.OPEN && j >= 1) {
            Block block = iblockaccess.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
            PathNodeType pathtype1 = this.getPathNodeTypeRaw(iblockaccess, i, j - 1, k);

            pathtype = pathtype1 != PathNodeType.WALKABLE && pathtype1 != PathNodeType.OPEN && pathtype1 != PathNodeType.WATER && pathtype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (pathtype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA) {
                pathtype = PathNodeType.DAMAGE_FIRE;
            }

            if (pathtype1 == PathNodeType.DAMAGE_CACTUS) {
                pathtype = PathNodeType.DAMAGE_CACTUS;
            }
        }

        pathtype = this.checkNeighborBlocks(iblockaccess, i, j, k, pathtype);
        return pathtype;
    }

    public PathNodeType checkNeighborBlocks(IBlockAccess iblockaccess, int i, int j, int k, PathNodeType pathtype) {
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        if (pathtype == PathNodeType.WALKABLE) {
            for (int l = -1; l <= 1; ++l) {
                for (int i1 = -1; i1 <= 1; ++i1) {
                    if (l != 0 || i1 != 0) {
                        Block block = iblockaccess.getBlockState(blockposition_pooledblockposition.setPos(l + i, j, i1 + k)).getBlock();

                        if (block == Blocks.CACTUS) {
                            pathtype = PathNodeType.DANGER_CACTUS;
                        } else if (block == Blocks.FIRE) {
                            pathtype = PathNodeType.DANGER_FIRE;
                        }
                    }
                }
            }
        }

        blockposition_pooledblockposition.release();
        return pathtype;
    }

    protected PathNodeType getPathNodeTypeRaw(IBlockAccess iblockaccess, int i, int j, int k) {
        BlockPos blockposition = new BlockPos(i, j, k);
        IBlockState iblockdata = iblockaccess.getBlockState(blockposition);
        Block block = iblockdata.getBlock();
        Material material = iblockdata.getMaterial();

        return material == Material.AIR ? PathNodeType.OPEN : (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY ? (block == Blocks.FIRE ? PathNodeType.DAMAGE_FIRE : (block == Blocks.CACTUS ? PathNodeType.DAMAGE_CACTUS : (block instanceof BlockDoor && material == Material.WOOD && !((Boolean) iblockdata.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_WOOD_CLOSED : (block instanceof BlockDoor && material == Material.IRON && !((Boolean) iblockdata.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_IRON_CLOSED : (block instanceof BlockDoor && ((Boolean) iblockdata.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_OPEN : (block instanceof BlockRailBase ? PathNodeType.RAIL : (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || ((Boolean) iblockdata.getValue(BlockFenceGate.OPEN)).booleanValue()) ? (material == Material.WATER ? PathNodeType.WATER : (material == Material.LAVA ? PathNodeType.LAVA : (block.isPassable(iblockaccess, blockposition) ? PathNodeType.OPEN : PathNodeType.BLOCKED))) : PathNodeType.FENCE))))))) : PathNodeType.TRAPDOOR);
    }
}

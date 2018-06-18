package net.minecraft.pathfinding;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateGround extends PathNavigate {

    private boolean shouldAvoidSun;

    public PathNavigateGround(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    protected boolean canNavigate() {
        return this.entity.onGround || this.getCanSwim() && this.isInLiquid() || this.entity.isRiding();
    }

    protected Vec3d getEntityPosition() {
        return new Vec3d(this.entity.posX, (double) this.getPathablePosY(), this.entity.posZ);
    }

    public Path getPathToPos(BlockPos blockposition) {
        BlockPos blockposition1;

        if (this.world.getBlockState(blockposition).getMaterial() == Material.AIR) {
            for (blockposition1 = blockposition.down(); blockposition1.getY() > 0 && this.world.getBlockState(blockposition1).getMaterial() == Material.AIR; blockposition1 = blockposition1.down()) {
                ;
            }

            if (blockposition1.getY() > 0) {
                return super.getPathToPos(blockposition1.up());
            }

            while (blockposition1.getY() < this.world.getHeight() && this.world.getBlockState(blockposition1).getMaterial() == Material.AIR) {
                blockposition1 = blockposition1.up();
            }

            blockposition = blockposition1;
        }

        if (!this.world.getBlockState(blockposition).getMaterial().isSolid()) {
            return super.getPathToPos(blockposition);
        } else {
            for (blockposition1 = blockposition.up(); blockposition1.getY() < this.world.getHeight() && this.world.getBlockState(blockposition1).getMaterial().isSolid(); blockposition1 = blockposition1.up()) {
                ;
            }

            return super.getPathToPos(blockposition1);
        }
    }

    public Path getPathToEntityLiving(Entity entity) {
        return this.getPathToPos(new BlockPos(entity));
    }

    private int getPathablePosY() {
        if (this.entity.isInWater() && this.getCanSwim()) {
            int i = (int) this.entity.getEntityBoundingBox().minY;
            Block block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
            int j = 0;

            do {
                if (block != Blocks.FLOWING_WATER && block != Blocks.WATER) {
                    return i;
                }

                ++i;
                block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
                ++j;
            } while (j <= 16);

            return (int) this.entity.getEntityBoundingBox().minY;
        } else {
            return (int) (this.entity.getEntityBoundingBox().minY + 0.5D);
        }
    }

    protected void removeSunnyPath() {
        super.removeSunnyPath();
        if (this.shouldAvoidSun) {
            if (this.world.canSeeSky(new BlockPos(MathHelper.floor(this.entity.posX), (int) (this.entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.posZ)))) {
                return;
            }

            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);

                if (this.world.canSeeSky(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z))) {
                    this.currentPath.setCurrentPathLength(i - 1);
                    return;
                }
            }
        }

    }

    protected boolean isDirectPathBetweenPoints(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k) {
        int l = MathHelper.floor(vec3d.x);
        int i1 = MathHelper.floor(vec3d.z);
        double d0 = vec3d1.x - vec3d.x;
        double d1 = vec3d1.z - vec3d.z;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 < 1.0E-8D) {
            return false;
        } else {
            double d3 = 1.0D / Math.sqrt(d2);

            d0 *= d3;
            d1 *= d3;
            i += 2;
            k += 2;
            if (!this.isSafeToStandAt(l, (int) vec3d.y, i1, i, j, k, vec3d, d0, d1)) {
                return false;
            } else {
                i -= 2;
                k -= 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double) l - vec3d.x;
                double d7 = (double) i1 - vec3d.z;

                if (d0 >= 0.0D) {
                    ++d6;
                }

                if (d1 >= 0.0D) {
                    ++d7;
                }

                d6 /= d0;
                d7 /= d1;
                int j1 = d0 < 0.0D ? -1 : 1;
                int k1 = d1 < 0.0D ? -1 : 1;
                int l1 = MathHelper.floor(vec3d1.x);
                int i2 = MathHelper.floor(vec3d1.z);
                int j2 = l1 - l;
                int k2 = i2 - i1;

                do {
                    if (j2 * j1 <= 0 && k2 * k1 <= 0) {
                        return true;
                    }

                    if (d6 < d7) {
                        d6 += d4;
                        l += j1;
                        j2 = l1 - l;
                    } else {
                        d7 += d5;
                        i1 += k1;
                        k2 = i2 - i1;
                    }
                } while (this.isSafeToStandAt(l, (int) vec3d.y, i1, i, j, k, vec3d, d0, d1));

                return false;
            }
        }
    }

    private boolean isSafeToStandAt(int i, int j, int k, int l, int i1, int j1, Vec3d vec3d, double d0, double d1) {
        int k1 = i - l / 2;
        int l1 = k - j1 / 2;

        if (!this.isPositionClear(k1, j, l1, l, i1, j1, vec3d, d0, d1)) {
            return false;
        } else {
            for (int i2 = k1; i2 < k1 + l; ++i2) {
                for (int j2 = l1; j2 < l1 + j1; ++j2) {
                    double d2 = (double) i2 + 0.5D - vec3d.x;
                    double d3 = (double) j2 + 0.5D - vec3d.z;

                    if (d2 * d0 + d3 * d1 >= 0.0D) {
                        PathNodeType pathtype = this.nodeProcessor.getPathNodeType(this.world, i2, j - 1, j2, this.entity, l, i1, j1, true, true);

                        if (pathtype == PathNodeType.WATER) {
                            return false;
                        }

                        if (pathtype == PathNodeType.LAVA) {
                            return false;
                        }

                        if (pathtype == PathNodeType.OPEN) {
                            return false;
                        }

                        pathtype = this.nodeProcessor.getPathNodeType(this.world, i2, j, j2, this.entity, l, i1, j1, true, true);
                        float f = this.entity.getPathPriority(pathtype);

                        if (f < 0.0F || f >= 8.0F) {
                            return false;
                        }

                        if (pathtype == PathNodeType.DAMAGE_FIRE || pathtype == PathNodeType.DANGER_FIRE || pathtype == PathNodeType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean isPositionClear(int i, int j, int k, int l, int i1, int j1, Vec3d vec3d, double d0, double d1) {
        Iterator iterator = BlockPos.getAllInBox(new BlockPos(i, j, k), new BlockPos(i + l - 1, j + i1 - 1, k + j1 - 1)).iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition = (BlockPos) iterator.next();
            double d2 = (double) blockposition.getX() + 0.5D - vec3d.x;
            double d3 = (double) blockposition.getZ() + 0.5D - vec3d.z;

            if (d2 * d0 + d3 * d1 >= 0.0D) {
                Block block = this.world.getBlockState(blockposition).getBlock();

                if (!block.isPassable(this.world, blockposition)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setBreakDoors(boolean flag) {
        this.nodeProcessor.setCanOpenDoors(flag);
    }

    public void setEnterDoors(boolean flag) {
        this.nodeProcessor.setCanEnterDoors(flag);
    }

    public boolean getEnterDoors() {
        return this.nodeProcessor.getCanEnterDoors();
    }

    public void setCanSwim(boolean flag) {
        this.nodeProcessor.setCanSwim(flag);
    }

    public boolean getCanSwim() {
        return this.nodeProcessor.getCanSwim();
    }

    public void setAvoidSun(boolean flag) {
        this.shouldAvoidSun = flag;
    }
}

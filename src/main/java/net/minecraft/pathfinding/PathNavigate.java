package net.minecraft.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public abstract class PathNavigate {

    protected EntityLiving entity; public Entity getEntity() { return entity; } // Paper - OBFHELPER
    protected World world;
    @Nullable
    protected Path currentPath;
    protected double speed;
    private final IAttributeInstance pathSearchRange;
    protected int totalTicks;
    private int ticksAtLastPos;
    private Vec3d lastPosCheck;
    private Vec3d timeoutCachedNode;
    private long timeoutTimer;
    private long lastTimeoutCheck;
    private double timeoutLimit;
    protected float maxDistanceToWaypoint;
    protected boolean tryUpdatePath;
    private long lastTimeUpdated;
    protected NodeProcessor nodeProcessor;
    private BlockPos targetPos;
    private final PathFinder pathFinder;

    public PathNavigate(EntityLiving entityinsentient, World world) {
        this.lastPosCheck = Vec3d.ZERO;
        this.timeoutCachedNode = Vec3d.ZERO;
        this.maxDistanceToWaypoint = 0.5F;
        this.entity = entityinsentient;
        this.world = world;
        this.pathSearchRange = entityinsentient.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        this.pathFinder = this.getPathFinder();
    }

    protected abstract PathFinder getPathFinder();

    public void setSpeed(double d0) {
        this.speed = d0;
    }

    public float getPathSearchRange() {
        return (float) this.pathSearchRange.getAttributeValue();
    }

    public boolean canUpdatePathOnTimeout() {
        return this.tryUpdatePath;
    }

    public void updatePath() {
        if (this.world.getTotalWorldTime() - this.lastTimeUpdated > 20L) {
            if (this.targetPos != null) {
                this.currentPath = null;
                this.currentPath = this.getPathToPos(this.targetPos);
                this.lastTimeUpdated = this.world.getTotalWorldTime();
                this.tryUpdatePath = false;
            }
        } else {
            this.tryUpdatePath = true;
        }

    }

    @Nullable
    public final Path getPathToXYZ(double d0, double d1, double d2) {
        return this.getPathToPos(new BlockPos(d0, d1, d2));
    }

    @Nullable
    public Path getPathToPos(BlockPos blockposition) {
        if (!getEntity().getEntityWorld().getWorldBorder().isInBounds(blockposition)) return null; // Paper - don't path out of world border
        if (!this.canNavigate()) {
            return null;
        } else if (this.currentPath != null && !this.currentPath.isFinished() && blockposition.equals(this.targetPos)) {
            return this.currentPath;
        } else {
            if (!new com.destroystokyo.paper.event.entity.EntityPathfindEvent(getEntity().getBukkitEntity(), MCUtil.toLocation(getEntity().world, blockposition), null).callEvent()) { return null; } // Paper
            this.targetPos = blockposition;
            float f = this.getPathSearchRange();

            this.world.profiler.startSection("pathfind");
            BlockPos blockposition1 = new BlockPos(this.entity);
            int i = (int) (f + 8.0F);
            ChunkCache chunkcache = new ChunkCache(this.world, blockposition1.add(-i, -i, -i), blockposition1.add(i, i, i), 0);
            Path pathentity = this.pathFinder.findPath(chunkcache, this.entity, this.targetPos, f);

            this.world.profiler.endSection();
            return pathentity;
        }
    }

    @Nullable
    public Path getPathToEntityLiving(Entity entity) {
        if (!this.canNavigate()) {
            return null;
        } else {
            BlockPos blockposition = new BlockPos(entity);
            if (!getEntity().getEntityWorld().getWorldBorder().isInBounds(blockposition)) return null; // Paper - don't path out of world border

            if (this.currentPath != null && !this.currentPath.isFinished() && blockposition.equals(this.targetPos)) {
                return this.currentPath;
            } else {
                if (!new com.destroystokyo.paper.event.entity.EntityPathfindEvent(getEntity().getBukkitEntity(), MCUtil.toLocation(entity.world, blockposition), entity.getBukkitEntity()).callEvent()) { return null; } // Paper
                this.targetPos = blockposition;
                float f = this.getPathSearchRange();

                this.world.profiler.startSection("pathfind");
                BlockPos blockposition1 = (new BlockPos(this.entity)).up();
                int i = (int) (f + 16.0F);
                ChunkCache chunkcache = new ChunkCache(this.world, blockposition1.add(-i, -i, -i), blockposition1.add(i, i, i), 0);
                Path pathentity = this.pathFinder.findPath(chunkcache, this.entity, entity, f);

                this.world.profiler.endSection();
                return pathentity;
            }
        }
    }

    public boolean tryMoveToXYZ(double d0, double d1, double d2, double d3) {
        return this.setPath(this.getPathToXYZ(d0, d1, d2), d3);
    }

    public boolean tryMoveToEntityLiving(Entity entity, double d0) {
        // Paper start - Pathfinding optimizations
        if (this.pathfindFailures > 10 && this.currentPath == null && MinecraftServer.currentTick < this.lastFailure + 40) {
            return false;
        }

        Path pathentity = this.getPathToEntityLiving(entity);

        if (pathentity != null && this.setPath(pathentity, d0)) {
            this.lastFailure = 0;
            this.pathfindFailures = 0;
            return true;
        } else {
            this.pathfindFailures++;
            this.lastFailure = MinecraftServer.currentTick;
            return false;
        }
    }
    private int lastFailure = 0;
    private int pathfindFailures = 0;
    // Paper end

    public boolean setPath(@Nullable Path pathentity, double d0) {
        if (pathentity == null) {
            this.currentPath = null;
            return false;
        } else {
            if (!pathentity.isSamePath(this.currentPath)) {
                this.currentPath = pathentity;
            }

            this.removeSunnyPath();
            if (this.currentPath.getCurrentPathLength() <= 0) {
                return false;
            } else {
                this.speed = d0;
                Vec3d vec3d = this.getEntityPosition();

                this.ticksAtLastPos = this.totalTicks;
                this.lastPosCheck = vec3d;
                return true;
            }
        }
    }

    @Nullable
    public Path getPath() {
        return this.currentPath;
    }

    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (this.tryUpdatePath) {
            this.updatePath();
        }

        if (!this.noPath()) {
            Vec3d vec3d;

            if (this.canNavigate()) {
                this.pathFollow();
            } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                vec3d = this.getEntityPosition();
                Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());

                if (vec3d.y > vec3d1.y && !this.entity.onGround && MathHelper.floor(vec3d.x) == MathHelper.floor(vec3d1.x) && MathHelper.floor(vec3d.z) == MathHelper.floor(vec3d1.z)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }

            this.debugPathFinding();
            if (!this.noPath()) {
                vec3d = this.currentPath.getPosition((Entity) this.entity);
                BlockPos blockposition = (new BlockPos(vec3d)).down();
                AxisAlignedBB axisalignedbb = this.world.getBlockState(blockposition).getBoundingBox(this.world, blockposition);

                vec3d = vec3d.subtract(0.0D, 1.0D - axisalignedbb.maxY, 0.0D);
                this.entity.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }
        }
    }

    protected void debugPathFinding() {}

    protected void pathFollow() {
        Vec3d vec3d = this.getEntityPosition();
        int i = this.currentPath.getCurrentPathLength();

        for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j) {
            if ((double) this.currentPath.getPathPointFromIndex(j).y != Math.floor(vec3d.y)) {
                i = j;
                break;
            }
        }

        this.maxDistanceToWaypoint = this.entity.width > 0.75F ? this.entity.width / 2.0F : 0.75F - this.entity.width / 2.0F;
        Vec3d vec3d1 = this.currentPath.getCurrentPos();

        if (MathHelper.abs((float) (this.entity.posX - (vec3d1.x + 0.5D))) < this.maxDistanceToWaypoint && MathHelper.abs((float) (this.entity.posZ - (vec3d1.z + 0.5D))) < this.maxDistanceToWaypoint && Math.abs(this.entity.posY - vec3d1.y) < 1.0D) {
            this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
        }

        int k = MathHelper.ceil(this.entity.width);
        int l = MathHelper.ceil(this.entity.height);
        int i1 = k;

        for (int j1 = i - 1; j1 >= this.currentPath.getCurrentPathIndex(); --j1) {
            if (this.isDirectPathBetweenPoints(vec3d, this.currentPath.getVectorFromIndex(this.entity, j1), k, l, i1)) {
                this.currentPath.setCurrentPathIndex(j1);
                break;
            }
        }

        this.checkForStuck(vec3d);
    }

    protected void checkForStuck(Vec3d vec3d) {
        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (vec3d.squareDistanceTo(this.lastPosCheck) < 2.25D) {
                this.clearPath();
            }

            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = vec3d;
        }

        if (this.currentPath != null && !this.currentPath.isFinished()) {
            Vec3d vec3d1 = this.currentPath.getCurrentPos();

            if (vec3d1.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = vec3d1;
                double d0 = vec3d.distanceTo(this.timeoutCachedNode);

                this.timeoutLimit = this.entity.getAIMoveSpeed() > 0.0F ? d0 / (double) this.entity.getAIMoveSpeed() * 1000.0D : 0.0D;
            }

            if (this.timeoutLimit > 0.0D && (double) this.timeoutTimer > this.timeoutLimit * 3.0D) {
                this.timeoutCachedNode = Vec3d.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0D;
                this.clearPath();
            }

            this.lastTimeoutCheck = System.currentTimeMillis();
        }

    }

    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }

    public void clearPath() {
        this.pathfindFailures = 0; this.lastFailure = 0; // Paper - Pathfinding optimizations
        this.currentPath = null;
    }

    protected abstract Vec3d getEntityPosition();

    protected abstract boolean canNavigate();

    protected boolean isInLiquid() {
        return this.entity.isInWater() || this.entity.isInLava();
    }

    protected void removeSunnyPath() {
        if (this.currentPath != null) {
            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
                PathPoint pathpoint1 = i + 1 < this.currentPath.getCurrentPathLength() ? this.currentPath.getPathPointFromIndex(i + 1) : null;
                IBlockState iblockdata = this.world.getBlockState(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z));
                Block block = iblockdata.getBlock();

                if (block == Blocks.CAULDRON) {
                    this.currentPath.setPoint(i, pathpoint.cloneMove(pathpoint.x, pathpoint.y + 1, pathpoint.z));
                    if (pathpoint1 != null && pathpoint.y >= pathpoint1.y) {
                        this.currentPath.setPoint(i + 1, pathpoint1.cloneMove(pathpoint1.x, pathpoint.y + 1, pathpoint1.z));
                    }
                }
            }

        }
    }

    protected abstract boolean isDirectPathBetweenPoints(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k);

    public boolean canEntityStandOnPos(BlockPos blockposition) {
        return this.world.getBlockState(blockposition.down()).isFullBlock();
    }

    public NodeProcessor getNodeProcessor() {
        return this.nodeProcessor;
    }
}

package net.minecraft.pathfinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class PathNavigateFlying extends PathNavigate {

    public PathNavigateFlying(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new FlyingNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    protected boolean canNavigate() {
        return this.canFloat() && this.isInLiquid() || !this.entity.isRiding();
    }

    protected Vec3d getEntityPosition() {
        return new Vec3d(this.entity.posX, this.entity.posY, this.entity.posZ);
    }

    public Path getPathToEntityLiving(Entity entity) {
        return this.getPathToPos(new BlockPos(entity));
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
                vec3d = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());
                if (MathHelper.floor(this.entity.posX) == MathHelper.floor(vec3d.x) && MathHelper.floor(this.entity.posY) == MathHelper.floor(vec3d.y) && MathHelper.floor(this.entity.posZ) == MathHelper.floor(vec3d.z)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }

            this.debugPathFinding();
            if (!this.noPath()) {
                vec3d = this.currentPath.getPosition((Entity) this.entity);
                this.entity.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }
        }
    }

    protected boolean isDirectPathBetweenPoints(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k) {
        int l = MathHelper.floor(vec3d.x);
        int i1 = MathHelper.floor(vec3d.y);
        int j1 = MathHelper.floor(vec3d.z);
        double d0 = vec3d1.x - vec3d.x;
        double d1 = vec3d1.y - vec3d.y;
        double d2 = vec3d1.z - vec3d.z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 < 1.0E-8D) {
            return false;
        } else {
            double d4 = 1.0D / Math.sqrt(d3);

            d0 *= d4;
            d1 *= d4;
            d2 *= d4;
            double d5 = 1.0D / Math.abs(d0);
            double d6 = 1.0D / Math.abs(d1);
            double d7 = 1.0D / Math.abs(d2);
            double d8 = (double) l - vec3d.x;
            double d9 = (double) i1 - vec3d.y;
            double d10 = (double) j1 - vec3d.z;

            if (d0 >= 0.0D) {
                ++d8;
            }

            if (d1 >= 0.0D) {
                ++d9;
            }

            if (d2 >= 0.0D) {
                ++d10;
            }

            d8 /= d0;
            d9 /= d1;
            d10 /= d2;
            int k1 = d0 < 0.0D ? -1 : 1;
            int l1 = d1 < 0.0D ? -1 : 1;
            int i2 = d2 < 0.0D ? -1 : 1;
            int j2 = MathHelper.floor(vec3d1.x);
            int k2 = MathHelper.floor(vec3d1.y);
            int l2 = MathHelper.floor(vec3d1.z);
            int i3 = j2 - l;
            int j3 = k2 - i1;
            int k3 = l2 - j1;

            while (i3 * k1 > 0 || j3 * l1 > 0 || k3 * i2 > 0) {
                if (d8 < d10 && d8 <= d9) {
                    d8 += d5;
                    l += k1;
                    i3 = j2 - l;
                } else if (d9 < d8 && d9 <= d10) {
                    d9 += d6;
                    i1 += l1;
                    j3 = k2 - i1;
                } else {
                    d10 += d7;
                    j1 += i2;
                    k3 = l2 - j1;
                }
            }

            return true;
        }
    }

    public void setCanOpenDoors(boolean flag) {
        this.nodeProcessor.setCanOpenDoors(flag);
    }

    public void setCanEnterDoors(boolean flag) {
        this.nodeProcessor.setCanEnterDoors(flag);
    }

    public void setCanFloat(boolean flag) {
        this.nodeProcessor.setCanSwim(flag);
    }

    public boolean canFloat() {
        return this.nodeProcessor.getCanSwim();
    }

    public boolean canEntityStandOnPos(BlockPos blockposition) {
        return this.world.getBlockState(blockposition).isTopSolid();
    }
}

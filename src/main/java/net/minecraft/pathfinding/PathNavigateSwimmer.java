package net.minecraft.pathfinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class PathNavigateSwimmer extends PathNavigate {

    public PathNavigateSwimmer(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    protected PathFinder getPathFinder() {
        return new PathFinder(new SwimNodeProcessor());
    }

    protected boolean canNavigate() {
        return this.isInLiquid();
    }

    protected Vec3d getEntityPosition() {
        return new Vec3d(this.entity.posX, this.entity.posY + (double) this.entity.height * 0.5D, this.entity.posZ);
    }

    protected void pathFollow() {
        Vec3d vec3d = this.getEntityPosition();
        float f = this.entity.width * this.entity.width;
        boolean flag = true;

        if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex())) < (double) f) {
            this.currentPath.incrementPathIndex();
        }

        for (int i = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); i > this.currentPath.getCurrentPathIndex(); --i) {
            Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.entity, i);

            if (vec3d1.squareDistanceTo(vec3d) <= 36.0D && this.isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0)) {
                this.currentPath.setCurrentPathIndex(i);
                break;
            }
        }

        this.checkForStuck(vec3d);
    }

    protected boolean isDirectPathBetweenPoints(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3d, new Vec3d(vec3d1.x, vec3d1.y + (double) this.entity.height * 0.5D, vec3d1.z), false, true, false);

        return movingobjectposition == null || movingobjectposition.typeOfHit == RayTraceResult.Type.MISS;
    }

    public boolean canEntityStandOnPos(BlockPos blockposition) {
        return !this.world.getBlockState(blockposition).isFullBlock();
    }
}

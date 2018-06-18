package net.minecraft.pathfinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class PathNavigateClimber extends PathNavigateGround {

    private BlockPos targetPosition;

    public PathNavigateClimber(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    public Path getPathToPos(BlockPos blockposition) {
        this.targetPosition = blockposition;
        return super.getPathToPos(blockposition);
    }

    public Path getPathToEntityLiving(Entity entity) {
        this.targetPosition = new BlockPos(entity);
        return super.getPathToEntityLiving(entity);
    }

    public boolean tryMoveToEntityLiving(Entity entity, double d0) {
        Path pathentity = this.getPathToEntityLiving(entity);

        if (pathentity != null) {
            return this.setPath(pathentity, d0);
        } else {
            this.targetPosition = new BlockPos(entity);
            this.speed = d0;
            return true;
        }
    }

    public void onUpdateNavigation() {
        if (!this.noPath()) {
            super.onUpdateNavigation();
        } else {
            if (this.targetPosition != null) {
                double d0 = (double) (this.entity.width * this.entity.width);

                if (this.entity.getDistanceSqToCenter(this.targetPosition) >= d0 && (this.entity.posY <= (double) this.targetPosition.getY() || this.entity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor(this.entity.posY), this.targetPosition.getZ())) >= d0)) {
                    this.entity.getMoveHelper().setMoveTo((double) this.targetPosition.getX(), (double) this.targetPosition.getY(), (double) this.targetPosition.getZ(), this.speed);
                } else {
                    this.targetPosition = null;
                }
            }

        }
    }
}

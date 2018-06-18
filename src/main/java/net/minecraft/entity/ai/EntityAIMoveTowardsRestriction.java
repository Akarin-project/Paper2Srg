package net.minecraft.entity.ai;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class EntityAIMoveTowardsRestriction extends EntityAIBase {

    private final EntityCreature creature;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;

    public EntityAIMoveTowardsRestriction(EntityCreature entitycreature, double d0) {
        this.creature = entitycreature;
        this.movementSpeed = d0;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.creature.isWithinHomeDistanceCurrentPosition()) {
            return false;
        } else {
            BlockPos blockposition = this.creature.getHomePosition();
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature, 16, 7, new Vec3d((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()));

            if (vec3d == null) {
                return false;
            } else {
                this.movePosX = vec3d.x;
                this.movePosY = vec3d.y;
                this.movePosZ = vec3d.z;
                return true;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}

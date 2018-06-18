package net.minecraft.entity.ai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;


public class EntityAIOcelotAttack extends EntityAIBase {

    World world;
    EntityLiving entity;
    EntityLivingBase target;
    int attackCountdown;

    public EntityAIOcelotAttack(EntityLiving entityinsentient) {
        this.entity = entityinsentient;
        this.world = entityinsentient.world;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entityliving = this.entity.getAttackTarget();

        if (entityliving == null) {
            return false;
        } else {
            this.target = entityliving;
            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.target.isEntityAlive() ? false : (this.entity.getDistanceSq(this.target) > 225.0D ? false : !this.entity.getNavigator().noPath() || this.shouldExecute());
    }

    public void resetTask() {
        this.target = null;
        this.entity.getNavigator().clearPath();
    }

    public void updateTask() {
        this.entity.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        double d0 = (double) (this.entity.width * 2.0F * this.entity.width * 2.0F);
        double d1 = this.entity.getDistanceSq(this.target.posX, this.target.getEntityBoundingBox().minY, this.target.posZ);
        double d2 = 0.8D;

        if (d1 > d0 && d1 < 16.0D) {
            d2 = 1.33D;
        } else if (d1 < 225.0D) {
            d2 = 0.6D;
        }

        this.entity.getNavigator().tryMoveToEntityLiving((Entity) this.target, d2);
        this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
        if (d1 <= d0) {
            if (this.attackCountdown <= 0) {
                this.attackCountdown = 20;
                this.entity.attackEntityAsMob(this.target);
            }
        }
    }
}

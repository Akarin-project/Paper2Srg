package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;


public class EntityAISwimming extends EntityAIBase {

    private final EntityLiving entity;

    public EntityAISwimming(EntityLiving entityinsentient) {
        this.entity = entityinsentient;
        if (entityinsentient.getEntityWorld().paperConfig.nerfedMobsShouldJump) entityinsentient.goalFloat = this; // Paper
        this.setMutexBits(4);
        if (entityinsentient.getNavigator() instanceof PathNavigateGround) {
            ((PathNavigateGround) entityinsentient.getNavigator()).setCanSwim(true);
        } else if (entityinsentient.getNavigator() instanceof PathNavigateFlying) {
            ((PathNavigateFlying) entityinsentient.getNavigator()).setCanFloat(true);
        }

    }

    public boolean validConditions() { return this.shouldExecute(); } // Paper - OBFHELPER
    public boolean shouldExecute() {
        return this.entity.isInWater() || this.entity.isInLava();
    }

    public void update() { this.updateTask(); } // Paper - OBFHELPER
    public void updateTask() {
        if (this.entity.getRNG().nextFloat() < 0.8F) {
            this.entity.getJumpHelper().setJumping();
        }

    }
}

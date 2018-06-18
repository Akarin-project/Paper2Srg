package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;


public class EntityAIOwnerHurtByTarget extends EntityAITarget {

    EntityTameable tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIOwnerHurtByTarget(EntityTameable entitytameableanimal) {
        super(entitytameableanimal, false);
        this.tameable = entitytameableanimal;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.tameable.isTamed()) {
            return false;
        } else {
            EntityLivingBase entityliving = this.tameable.getOwner();

            if (entityliving == null) {
                return false;
            } else {
                this.attacker = entityliving.getRevengeTarget();
                int i = entityliving.getRevengeTimer();

                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entityliving);
            }
        }
    }

    public void startExecuting() {
        this.taskOwner.setGoalTarget(this.attacker, org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true); // CraftBukkit - reason
        EntityLivingBase entityliving = this.tameable.getOwner();

        if (entityliving != null) {
            this.timestamp = entityliving.getRevengeTimer();
        }

        super.startExecuting();
    }
}

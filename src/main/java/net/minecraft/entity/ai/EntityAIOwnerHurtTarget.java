package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;


public class EntityAIOwnerHurtTarget extends EntityAITarget {

    EntityTameable tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIOwnerHurtTarget(EntityTameable entitytameableanimal) {
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
                this.attacker = entityliving.getLastAttackedEntity();
                int i = entityliving.getLastAttackedEntityTime();

                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entityliving);
            }
        }
    }

    public void startExecuting() {
        this.taskOwner.setGoalTarget(this.attacker, org.bukkit.event.entity.EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true); // CraftBukkit - reason
        EntityLivingBase entityliving = this.tameable.getOwner();

        if (entityliving != null) {
            this.timestamp = entityliving.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}

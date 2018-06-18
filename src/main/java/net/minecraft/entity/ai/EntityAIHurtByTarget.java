package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {

    private final boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;

    public EntityAIHurtByTarget(EntityCreature entitycreature, boolean flag, Class<?>... aclass) {
        super(entitycreature, true);
        this.entityCallsForHelp = flag;
        this.excludedReinforcementTypes = aclass;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        int i = this.taskOwner.getRevengeTimer();
        EntityLivingBase entityliving = this.taskOwner.getRevengeTarget();

        return i != this.revengeTimerOld && entityliving != null && this.isSuitableTarget(entityliving, false);
    }

    public void startExecuting() {
        this.taskOwner.setGoalTarget(this.taskOwner.getRevengeTarget(), org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY, true); // CraftBukkit - reason
        this.target = this.taskOwner.getAttackTarget();
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;
        if (this.entityCallsForHelp) {
            this.alertOthers();
        }

        super.startExecuting();
    }

    protected void alertOthers() {
        double d0 = this.getTargetDistance();
        List list = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).grow(d0, 10.0D, d0));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityCreature entitycreature = (EntityCreature) iterator.next();

            if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && (!(this.taskOwner instanceof EntityTameable) || ((EntityTameable) this.taskOwner).getOwner() == ((EntityTameable) entitycreature).getOwner()) && !entitycreature.isOnSameTeam(this.taskOwner.getRevengeTarget())) {
                boolean flag = false;
                Class[] aclass = this.excludedReinforcementTypes;
                int i = aclass.length;

                for (int j = 0; j < i; ++j) {
                    Class oclass = aclass[j];

                    if (entitycreature.getClass() == oclass) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    this.setEntityAttackTarget(entitycreature, this.taskOwner.getRevengeTarget());
                }
            }
        }

    }

    protected void setEntityAttackTarget(EntityCreature entitycreature, EntityLivingBase entityliving) {
        entitycreature.setGoalTarget(entityliving, org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY, true); // CraftBukkit - reason
    }
}

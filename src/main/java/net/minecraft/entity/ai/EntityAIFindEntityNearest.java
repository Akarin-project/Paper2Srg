package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;

public class EntityAIFindEntityNearest extends EntityAIBase {

    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityLiving mob;
    private final Predicate<EntityLivingBase> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase target;
    private final Class<? extends EntityLivingBase> classToCheck;

    public EntityAIFindEntityNearest(EntityLiving entityinsentient, Class<? extends EntityLivingBase> oclass) {
        this.mob = entityinsentient;
        this.classToCheck = oclass;
        if (entityinsentient instanceof EntityCreature) {
            EntityAIFindEntityNearest.LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.predicate = new Predicate() {
            public boolean a(@Nullable EntityLivingBase entityliving) {
                double d0 = EntityAIFindEntityNearest.this.getFollowRange();

                if (entityliving.isSneaking()) {
                    d0 *= 0.800000011920929D;
                }

                return entityliving.isInvisible() ? false : ((double) entityliving.getDistance(EntityAIFindEntityNearest.this.mob) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.mob, entityliving, false, true));
            }

            public boolean apply(@Nullable Object object) {
                return this.a((EntityLivingBase) object);
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(entityinsentient);
    }

    public boolean shouldExecute() {
        double d0 = this.getFollowRange();
        List list = this.mob.world.getEntitiesWithinAABB(this.classToCheck, this.mob.getEntityBoundingBox().grow(d0, 4.0D, d0), this.predicate);

        Collections.sort(list, this.sorter);
        if (list.isEmpty()) {
            return false;
        } else {
            this.target = (EntityLivingBase) list.get(0);
            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        EntityLivingBase entityliving = this.mob.getAttackTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isEntityAlive()) {
            return false;
        } else {
            double d0 = this.getFollowRange();

            return this.mob.getDistanceSq(entityliving) > d0 * d0 ? false : !(entityliving instanceof EntityPlayerMP) || !((EntityPlayerMP) entityliving).interactionManager.isCreative();
        }
    }

    public void startExecuting() {
        this.mob.setGoalTarget(this.target, org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true); // CraftBukkit - reason
        super.startExecuting();
    }

    public void resetTask() {
        this.mob.setAttackTarget((EntityLivingBase) null);
        super.startExecuting();
    }

    protected double getFollowRange() {
        IAttributeInstance attributeinstance = this.mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);

        return attributeinstance == null ? 16.0D : attributeinstance.getAttributeValue();
    }
}

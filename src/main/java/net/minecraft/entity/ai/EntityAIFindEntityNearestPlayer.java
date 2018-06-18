package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase {

    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityLiving entityLiving;
    private final Predicate<Entity> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase entityTarget;

    public EntityAIFindEntityNearestPlayer(EntityLiving entityinsentient) {
        this.entityLiving = entityinsentient;
        if (entityinsentient instanceof EntityCreature) {
            EntityAIFindEntityNearestPlayer.LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.predicate = new Predicate() {
            public boolean a(@Nullable Entity entity) {
                if (!(entity instanceof EntityPlayer)) {
                    return false;
                } else if (((EntityPlayer) entity).capabilities.disableDamage) {
                    return false;
                } else {
                    double d0 = EntityAIFindEntityNearestPlayer.this.maxTargetRange();

                    if (entity.isSneaking()) {
                        d0 *= 0.800000011920929D;
                    }

                    if (entity.isInvisible()) {
                        float f = ((EntityPlayer) entity).getArmorVisibility();

                        if (f < 0.1F) {
                            f = 0.1F;
                        }

                        d0 *= (double) (0.7F * f);
                    }

                    return (double) entity.getDistance(EntityAIFindEntityNearestPlayer.this.entityLiving) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.entityLiving, (EntityLivingBase) entity, false, true);
                }
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(entityinsentient);
    }

    public boolean shouldExecute() {
        double d0 = this.maxTargetRange();
        List list = this.entityLiving.world.getEntitiesWithinAABB(EntityPlayer.class, this.entityLiving.getEntityBoundingBox().grow(d0, 4.0D, d0), this.predicate);

        Collections.sort(list, this.sorter);
        if (list.isEmpty()) {
            return false;
        } else {
            this.entityTarget = (EntityLivingBase) list.get(0);
            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        EntityLivingBase entityliving = this.entityLiving.getAttackTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isEntityAlive()) {
            return false;
        } else if (entityliving instanceof EntityPlayer && ((EntityPlayer) entityliving).capabilities.disableDamage) {
            return false;
        } else {
            Team scoreboardteambase = this.entityLiving.getTeam();
            Team scoreboardteambase1 = entityliving.getTeam();

            if (scoreboardteambase != null && scoreboardteambase1 == scoreboardteambase) {
                return false;
            } else {
                double d0 = this.maxTargetRange();

                return this.entityLiving.getDistanceSq(entityliving) > d0 * d0 ? false : !(entityliving instanceof EntityPlayerMP) || !((EntityPlayerMP) entityliving).interactionManager.isCreative();
            }
        }
    }

    public void startExecuting() {
        this.entityLiving.setGoalTarget(this.entityTarget, org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true); // CraftBukkit - added reason
        super.startExecuting();
    }

    public void resetTask() {
        this.entityLiving.setAttackTarget((EntityLivingBase) null);
        super.startExecuting();
    }

    protected double maxTargetRange() {
        IAttributeInstance attributeinstance = this.entityLiving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);

        return attributeinstance == null ? 16.0D : attributeinstance.getAttributeValue();
    }
}

package net.minecraft.entity.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.bukkit.event.entity.EntityTargetEvent;

public abstract class EntityAITarget extends EntityAIBase {

    protected final EntityCreature taskOwner;
    protected boolean shouldCheckSight;
    private final boolean nearbyOnly;
    private int targetSearchStatus;
    private int targetSearchDelay;
    private int targetUnseenTicks;
    protected EntityLivingBase target;
    protected int unseenMemoryTicks;

    public EntityAITarget(EntityCreature entitycreature, boolean flag) {
        this(entitycreature, flag, false);
    }

    public EntityAITarget(EntityCreature entitycreature, boolean flag, boolean flag1) {
        this.unseenMemoryTicks = 60;
        this.taskOwner = entitycreature;
        this.shouldCheckSight = flag;
        this.nearbyOnly = flag1;
    }

    public boolean shouldContinueExecuting() {
        EntityLivingBase entityliving = this.taskOwner.getAttackTarget();

        if (entityliving == null) {
            entityliving = this.target;
        }

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isEntityAlive()) {
            return false;
        } else {
            Team scoreboardteambase = this.taskOwner.getTeam();
            Team scoreboardteambase1 = entityliving.getTeam();

            if (scoreboardteambase != null && scoreboardteambase1 == scoreboardteambase) {
                return false;
            } else {
                double d0 = this.getTargetDistance();

                if (this.taskOwner.getDistanceSq(entityliving) > d0 * d0) {
                    return false;
                } else {
                    if (this.shouldCheckSight) {
                        if (this.taskOwner.getEntitySenses().canSee(entityliving)) {
                            this.targetUnseenTicks = 0;
                        } else if (++this.targetUnseenTicks > this.unseenMemoryTicks) {
                            return false;
                        }
                    }

                    if (entityliving instanceof EntityPlayer && ((EntityPlayer) entityliving).capabilities.disableDamage) {
                        return false;
                    } else {
                        this.taskOwner.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true); // CraftBukkit
                        return true;
                    }
                }
            }
        }
    }

    protected double getTargetDistance() {
        IAttributeInstance attributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);

        return attributeinstance == null ? 16.0D : attributeinstance.getAttributeValue();
    }

    public void startExecuting() {
        this.targetSearchStatus = 0;
        this.targetSearchDelay = 0;
        this.targetUnseenTicks = 0;
    }

    public void resetTask() {
        this.taskOwner.setGoalTarget((EntityLivingBase) null, EntityTargetEvent.TargetReason.FORGOT_TARGET, true); // CraftBukkit
        this.target = null;
    }

    public static boolean isSuitableTarget(EntityLiving entityinsentient, @Nullable EntityLivingBase entityliving, boolean flag, boolean flag1) {
        if (entityliving == null) {
            return false;
        } else if (entityliving == entityinsentient) {
            return false;
        } else if (!entityliving.isEntityAlive()) {
            return false;
        } else if (!entityinsentient.canAttackClass(entityliving.getClass())) {
            return false;
        } else if (entityinsentient.isOnSameTeam(entityliving)) {
            return false;
        } else {
            if (entityinsentient instanceof IEntityOwnable && ((IEntityOwnable) entityinsentient).getOwnerId() != null) {
                if (entityliving instanceof IEntityOwnable && ((IEntityOwnable) entityinsentient).getOwnerId().equals(((IEntityOwnable) entityliving).getOwnerId())) {
                    return false;
                }

                if (entityliving == ((IEntityOwnable) entityinsentient).getOwner()) {
                    return false;
                }
            } else if (entityliving instanceof EntityPlayer && !flag && ((EntityPlayer) entityliving).capabilities.disableDamage) {
                return false;
            }

            return !flag1 || entityinsentient.getEntitySenses().canSee(entityliving);
        }
    }

    protected boolean isSuitableTarget(@Nullable EntityLivingBase entityliving, boolean flag) {
        if (!isSuitableTarget(this.taskOwner, entityliving, flag, this.shouldCheckSight)) {
            return false;
        } else if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(entityliving))) {
            return false;
        } else {
            if (this.nearbyOnly) {
                if (--this.targetSearchDelay <= 0) {
                    this.targetSearchStatus = 0;
                }

                if (this.targetSearchStatus == 0) {
                    this.targetSearchStatus = this.canEasilyReach(entityliving) ? 1 : 2;
                }

                if (this.targetSearchStatus == 2) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean canEasilyReach(EntityLivingBase entityliving) {
        this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
        Path pathentity = this.taskOwner.getNavigator().getPathToEntityLiving((Entity) entityliving);

        if (pathentity == null) {
            return false;
        } else {
            PathPoint pathpoint = pathentity.getFinalPathPoint();

            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.x - MathHelper.floor(entityliving.posX);
                int j = pathpoint.z - MathHelper.floor(entityliving.posZ);

                return (double) (i * i + j * j) <= 2.25D;
            }
        }
    }

    public EntityAITarget setUnseenMemoryTicks(int i) {
        this.unseenMemoryTicks = i;
        return this;
    }
}

package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;

public class EntityAIFollow extends EntityAIBase {

    private final EntityLiving entity;
    private final Predicate<EntityLiving> followPredicate;
    private EntityLiving followingEntity;
    private final double speedModifier;
    private final PathNavigate navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    private final float areaSize;

    public EntityAIFollow(final EntityLiving entityinsentient, double d0, float f, float f1) {
        this.entity = entityinsentient;
        this.followPredicate = new Predicate<EntityLiving>() {
            @Override
            public boolean apply(@Nullable EntityLiving entity) {
                return entity != null && entityinsentient.getClass() != entity.getClass();
            }
        };
        this.speedModifier = d0;
        this.navigation = entityinsentient.getNavigator();
        this.stopDistance = f;
        this.areaSize = f1;
        this.setMutexBits(3);
        if (!(entityinsentient.getNavigator() instanceof PathNavigateGround) && !(entityinsentient.getNavigator() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    @Override
    public boolean shouldExecute() {
        List list = this.entity.world.getEntitiesWithinAABB(EntityLiving.class, this.entity.getEntityBoundingBox().grow(this.areaSize), this.followPredicate);

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLiving entityinsentient = (EntityLiving) iterator.next();

                if (!entityinsentient.isInvisible()) {
                    this.followingEntity = entityinsentient;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.followingEntity != null && !this.navigation.noPath() && this.entity.getDistanceSq(this.followingEntity) > this.stopDistance * this.stopDistance;
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.followingEntity = null;
        this.navigation.clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void updateTask() {
        if (this.followingEntity != null && !this.entity.getLeashed()) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.followingEntity, 10.0F, this.entity.getVerticalFaceSpeed());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                double d0 = this.entity.posX - this.followingEntity.posX;
                double d1 = this.entity.posY - this.followingEntity.posY;
                double d2 = this.entity.posZ - this.followingEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > this.stopDistance * this.stopDistance) {
                    this.navigation.tryMoveToEntityLiving(this.followingEntity, this.speedModifier);
                } else {
                    this.navigation.clearPath();
                    EntityLookHelper controllerlook = this.followingEntity.getLookHelper();

                    if (d3 <= this.stopDistance || controllerlook.getLookPosX() == this.entity.posX && controllerlook.getLookPosY() == this.entity.posY && controllerlook.getLookPosZ() == this.entity.posZ) {
                        double d4 = this.followingEntity.posX - this.entity.posX;
                        double d5 = this.followingEntity.posZ - this.entity.posZ;

                        this.navigation.tryMoveToXYZ(this.entity.posX - d4, this.entity.posY, this.entity.posZ - d5, this.speedModifier);
                    }

                }
            }
        }
    }
}

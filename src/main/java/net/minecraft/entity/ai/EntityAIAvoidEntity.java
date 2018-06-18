package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase {

    private final Predicate<Entity> canBeSeenSelector;
    protected EntityCreature entity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;
    private Path path;
    private final PathNavigate navigation;
    private final Class<T> classToAvoid;
    private final Predicate<? super T> avoidTargetSelector;

    public EntityAIAvoidEntity(EntityCreature entitycreature, Class<T> oclass, float f, double d0, double d1) {
        this(entitycreature, oclass, Predicates.alwaysTrue(), f, d0, d1);
    }

    public EntityAIAvoidEntity(EntityCreature entitycreature, Class<T> oclass, Predicate<? super T> predicate, float f, double d0, double d1) {
        this.canBeSeenSelector = new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return entity.isEntityAlive() && EntityAIAvoidEntity.this.entity.getEntitySenses().canSee(entity) && !EntityAIAvoidEntity.this.entity.isOnSameTeam(entity);
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        };
        this.entity = entitycreature;
        this.classToAvoid = oclass;
        this.avoidTargetSelector = predicate;
        this.avoidDistance = f;
        this.farSpeed = d0;
        this.nearSpeed = d1;
        this.navigation = entitycreature.getNavigator();
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        List list = this.entity.world.getEntitiesWithinAABB(this.classToAvoid, this.entity.getEntityBoundingBox().grow((double) this.avoidDistance, 3.0D, (double) this.avoidDistance), Predicates.and(new Predicate[] { EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector}));

        if (list.isEmpty()) {
            return false;
        } else {
            this.closestLivingEntity = (Entity) list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null) {
                return false;
            } else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity)) {
                return false;
            } else {
                this.path = this.navigation.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.path != null;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.navigation.noPath();
    }

    public void startExecuting() {
        this.navigation.setPath(this.path, this.farSpeed);
    }

    public void resetTask() {
        this.closestLivingEntity = null;
    }

    public void updateTask() {
        if (this.entity.getDistanceSq(this.closestLivingEntity) < 49.0D) {
            this.entity.getNavigator().setSpeed(this.nearSpeed);
        } else {
            this.entity.getNavigator().setSpeed(this.farSpeed);
        }

    }
}

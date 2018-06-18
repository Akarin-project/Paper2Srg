package net.minecraft.entity.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWater extends EntityAIWander {

    protected final float probability;

    public EntityAIWanderAvoidWater(EntityCreature entitycreature, double d0) {
        this(entitycreature, d0, 0.001F);
    }

    public EntityAIWanderAvoidWater(EntityCreature entitycreature, double d0, float f) {
        super(entitycreature, d0);
        this.probability = f;
    }

    @Nullable
    protected Vec3d getPosition() {
        if (this.entity.isInWater()) {
            Vec3d vec3d = RandomPositionGenerator.getLandPos(this.entity, 15, 7);

            return vec3d == null ? super.getPosition() : vec3d;
        } else {
            return this.entity.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.entity, 10, 7) : super.getPosition();
        }
    }
}

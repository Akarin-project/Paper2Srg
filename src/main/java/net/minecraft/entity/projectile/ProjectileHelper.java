package net.minecraft.entity.projectile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ProjectileHelper {

    public static RayTraceResult forwardsRaycast(Entity entity, boolean flag, boolean flag1, Entity entity1) {
        double d0 = entity.posX;
        double d1 = entity.posY;
        double d2 = entity.posZ;
        double d3 = entity.motionX;
        double d4 = entity.motionY;
        double d5 = entity.motionZ;
        World world = entity.world;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        Vec3d vec3d1 = new Vec3d(d0 + d3, d1 + d4, d2 + d5);
        RayTraceResult movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1, false, true, false);

        if (flag) {
            if (movingobjectposition != null) {
                vec3d1 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
            }

            Entity entity2 = null;
            List list = world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand(d3, d4, d5).grow(1.0D));
            double d6 = 0.0D;

            for (int i = 0; i < list.size(); ++i) {
                Entity entity3 = (Entity) list.get(i);

                if (entity3.canBeCollidedWith() && (flag1 || !entity3.isEntityEqual(entity1)) && !entity3.noClip) {
                    AxisAlignedBB axisalignedbb = entity3.getEntityBoundingBox().grow(0.30000001192092896D);
                    RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d7 = vec3d.squareDistanceTo(movingobjectposition1.hitVec);

                        if (d7 < d6 || d6 == 0.0D) {
                            entity2 = entity3;
                            d6 = d7;
                        }
                    }
                }
            }

            if (entity2 != null) {
                movingobjectposition = new RayTraceResult(entity2);
            }
        }

        return movingobjectposition;
    }

    public static final void rotateTowardsMovement(Entity entity, float f) {
        double d0 = entity.motionX;
        double d1 = entity.motionY;
        double d2 = entity.motionZ;
        float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        entity.rotationYaw = (float) (MathHelper.atan2(d2, d0) * 57.2957763671875D) + 90.0F;

        for (entity.rotationPitch = (float) (MathHelper.atan2((double) f1, d1) * 57.2957763671875D) - 90.0F; entity.rotationPitch - entity.prevRotationPitch < -180.0F; entity.prevRotationPitch -= 360.0F) {
            ;
        }

        while (entity.rotationPitch - entity.prevRotationPitch >= 180.0F) {
            entity.prevRotationPitch += 360.0F;
        }

        while (entity.rotationYaw - entity.prevRotationYaw < -180.0F) {
            entity.prevRotationYaw -= 360.0F;
        }

        while (entity.rotationYaw - entity.prevRotationYaw >= 180.0F) {
            entity.prevRotationYaw += 360.0F;
        }

        entity.rotationPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f;
        entity.rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f;
    }
}

package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.math.Vec3d;

public class EntityAILlamaFollowCaravan extends EntityAIBase {

    public EntityLlama llama;
    private double speedModifier;
    private int distCheckCounter;

    public EntityAILlamaFollowCaravan(EntityLlama entityllama, double d0) {
        this.llama = entityllama;
        this.speedModifier = d0;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.llama.getLeashed() && !this.llama.inCaravan()) {
            List list = this.llama.world.getEntitiesWithinAABB(this.llama.getClass(), this.llama.getEntityBoundingBox().grow(9.0D, 4.0D, 9.0D));
            EntityLlama entityllama = null;
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            EntityLlama entityllama1;
            double d1;

            while (iterator.hasNext()) {
                entityllama1 = (EntityLlama) iterator.next();
                if (entityllama1.inCaravan() && !entityllama1.hasCaravanTrail()) {
                    d1 = this.llama.getDistanceSq((Entity) entityllama1);
                    if (d1 <= d0) {
                        d0 = d1;
                        entityllama = entityllama1;
                    }
                }
            }

            if (entityllama == null) {
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    entityllama1 = (EntityLlama) iterator.next();
                    if (entityllama1.getLeashed() && !entityllama1.hasCaravanTrail()) {
                        d1 = this.llama.getDistanceSq((Entity) entityllama1);
                        if (d1 <= d0) {
                            d0 = d1;
                            entityllama = entityllama1;
                        }
                    }
                }
            }

            if (entityllama == null) {
                return false;
            } else if (d0 < 4.0D) {
                return false;
            } else if (!entityllama.getLeashed() && !this.firstIsLeashed(entityllama, 1)) {
                return false;
            } else {
                this.llama.joinCaravan(entityllama);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        if (this.llama.inCaravan() && this.llama.getCaravanHead().isEntityAlive() && this.firstIsLeashed(this.llama, 0)) {
            double d0 = this.llama.getDistanceSq((Entity) this.llama.getCaravanHead());

            if (d0 > 676.0D) {
                if (this.speedModifier <= 3.0D) {
                    this.speedModifier *= 1.2D;
                    this.distCheckCounter = 40;
                    return true;
                }

                if (this.distCheckCounter == 0) {
                    return false;
                }
            }

            if (this.distCheckCounter > 0) {
                --this.distCheckCounter;
            }

            return true;
        } else {
            return false;
        }
    }

    public void resetTask() {
        this.llama.leaveCaravan();
        this.speedModifier = 2.1D;
    }

    public void updateTask() {
        if (this.llama.inCaravan()) {
            EntityLlama entityllama = this.llama.getCaravanHead();
            double d0 = (double) this.llama.getDistance((Entity) entityllama);
            float f = 2.0F;
            Vec3d vec3d = (new Vec3d(entityllama.posX - this.llama.posX, entityllama.posY - this.llama.posY, entityllama.posZ - this.llama.posZ)).normalize().scale(Math.max(d0 - 2.0D, 0.0D));

            this.llama.getNavigator().tryMoveToXYZ(this.llama.posX + vec3d.x, this.llama.posY + vec3d.y, this.llama.posZ + vec3d.z, this.speedModifier);
        }
    }

    private boolean firstIsLeashed(EntityLlama entityllama, int i) {
        if (i > 8) {
            return false;
        } else if (entityllama.inCaravan()) {
            if (entityllama.getCaravanHead().getLeashed()) {
                return true;
            } else {
                EntityLlama entityllama1 = entityllama.getCaravanHead();

                ++i;
                return this.firstIsLeashed(entityllama1, i);
            }
        } else {
            return false;
        }
    }
}

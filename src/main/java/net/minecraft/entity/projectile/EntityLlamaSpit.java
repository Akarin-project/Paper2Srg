package net.minecraft.entity.projectile;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityLlamaSpit extends Entity implements IProjectile {

    public EntityLivingBase owner; // CraftBukkit - type
    private NBTTagCompound ownerNbt;

    public EntityLlamaSpit(World world) {
        super(world);
    }

    public EntityLlamaSpit(World world, EntityLlama entityllama) {
        super(world);
        this.owner = entityllama;
        this.setPosition(entityllama.posX - (double) (entityllama.width + 1.0F) * 0.5D * (double) MathHelper.sin(entityllama.renderYawOffset * 0.017453292F), entityllama.posY + (double) entityllama.getEyeHeight() - 0.10000000149011612D, entityllama.posZ + (double) (entityllama.width + 1.0F) * 0.5D * (double) MathHelper.cos(entityllama.renderYawOffset * 0.017453292F));
        this.setSize(0.25F, 0.25F);
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.ownerNbt != null) {
            this.restoreOwnerFromSave();
        }

        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3d, vec3d1);

        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (movingobjectposition != null) {
            vec3d1 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
        }

        Entity entity = this.getHitEntity(vec3d, vec3d1);

        if (entity != null) {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null) {
            this.onHit(movingobjectposition);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.2957763671875D);

        for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * 57.2957763671875D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f1 = 0.99F;
        float f2 = 0.06F;

        if (!this.world.isMaterialInBB(this.getEntityBoundingBox(), Material.AIR)) {
            this.setDead();
        } else if (this.isInWater()) {
            this.setDead();
        } else {
            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.9900000095367432D;
            this.motionZ *= 0.9900000095367432D;
            if (!this.hasNoGravity()) {
                this.motionY -= 0.05999999865889549D;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    @Nullable
    private Entity getHitEntity(Vec3d vec3d, Vec3d vec3d1) {
        Entity entity = null;
        List list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double d0 = 0.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity1 = (Entity) iterator.next();

            if (entity1 != this.owner) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                if (movingobjectposition != null) {
                    double d1 = vec3d.squareDistanceTo(movingobjectposition.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    public void shoot(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
        d1 += this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
        d2 += this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.motionX = d0;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        this.rotationYaw = (float) (MathHelper.atan2(d0, d2) * 57.2957763671875D);
        this.rotationPitch = (float) (MathHelper.atan2(d1, (double) f3) * 57.2957763671875D);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public void onHit(RayTraceResult movingobjectposition) {
        org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // Craftbukkit - Call event
        if (movingobjectposition.entityHit != null && this.owner != null) {
            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 1.0F);
        }

        if (!this.world.isRemote) {
            this.setDead();
        }

    }

    protected void entityInit() {}

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("Owner", 10)) {
            this.ownerNbt = nbttagcompound.getCompoundTag("Owner");
        }

    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        if (this.owner != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            UUID uuid = this.owner.getUniqueID();

            nbttagcompound1.setUniqueId("OwnerUUID", uuid);
            nbttagcompound.setTag("Owner", nbttagcompound1);
        }

    }

    private void restoreOwnerFromSave() {
        if (this.ownerNbt != null && this.ownerNbt.hasUniqueId("OwnerUUID")) {
            UUID uuid = this.ownerNbt.getUniqueId("OwnerUUID");
            List list = this.world.getEntitiesWithinAABB(EntityLlama.class, this.getEntityBoundingBox().grow(15.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLlama entityllama = (EntityLlama) iterator.next();

                if (entityllama.getUniqueID().equals(uuid)) {
                    this.owner = entityllama;
                    break;
                }
            }
        }

        this.ownerNbt = null;
    }
}

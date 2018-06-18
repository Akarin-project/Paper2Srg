package net.minecraft.entity.projectile;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityEvokerFangs extends Entity {

    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks;
    private boolean clientSideAttackStarted;
    private EntityLivingBase caster;
    private UUID casterUuid;

    public EntityEvokerFangs(World world) {
        super(world);
        this.lifeTicks = 22;
        this.setSize(0.5F, 0.8F);
    }

    public EntityEvokerFangs(World world, double d0, double d1, double d2, float f, int i, EntityLivingBase entityliving) {
        this(world);
        this.warmupDelayTicks = i;
        this.setCaster(entityliving);
        this.rotationYaw = f * 57.295776F;
        this.setPosition(d0, d1, d2);
    }

    protected void entityInit() {}

    public void setCaster(@Nullable EntityLivingBase entityliving) {
        this.caster = entityliving;
        this.casterUuid = entityliving == null ? null : entityliving.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getCaster() {
        if (this.caster == null && this.casterUuid != null && this.world instanceof WorldServer) {
            Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.casterUuid);

            if (entity instanceof EntityLivingBase) {
                this.caster = (EntityLivingBase) entity;
            }
        }

        return this.caster;
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.warmupDelayTicks = nbttagcompound.getInteger("Warmup");
        this.casterUuid = nbttagcompound.getUniqueId("OwnerUUID");
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("Warmup", this.warmupDelayTicks);
        if (this.casterUuid != null) {
            nbttagcompound.setUniqueId("OwnerUUID", this.casterUuid);
        }

    }

    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 14) {
                    for (int i = 0; i < 12; ++i) {
                        double d0 = this.posX + (this.rand.nextDouble() * 2.0D - 1.0D) * (double) this.width * 0.5D;
                        double d1 = this.posY + 0.05D + this.rand.nextDouble() * 1.0D;
                        double d2 = this.posZ + (this.rand.nextDouble() * 2.0D - 1.0D) * (double) this.width * 0.5D;
                        double d3 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.rand.nextDouble() * 0.3D;
                        double d5 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;

                        this.world.spawnParticle(EnumParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5, new int[0]);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.2D, 0.0D, 0.2D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                    this.damage(entityliving);
                }
            }

            if (!this.sentSpikeEvent) {
                this.world.setEntityState(this, (byte) 4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.setDead();
            }
        }

    }

    private void damage(EntityLivingBase entityliving) {
        EntityLivingBase entityliving1 = this.getCaster();

        if (entityliving.isEntityAlive() && !entityliving.getIsInvulnerable() && entityliving != entityliving1) {
            if (entityliving1 == null) {
                org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = this; // CraftBukkit
                entityliving.attackEntityFrom(DamageSource.MAGIC, 6.0F);
                org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = null; // CraftBukkit
            } else {
                if (entityliving1.isOnSameTeam(entityliving)) {
                    return;
                }

                entityliving.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, entityliving1), 6.0F);
            }

        }
    }
}

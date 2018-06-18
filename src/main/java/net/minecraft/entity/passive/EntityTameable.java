package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {

    protected static final DataParameter<Byte> TAMED = EntityDataManager.createKey(EntityTameable.class, DataSerializers.BYTE);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityTameable.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    protected EntityAISit aiSit;

    public EntityTameable(World world) {
        super(world);
        this.setupTamedAI();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityTameable.TAMED, Byte.valueOf((byte) 0));
        this.dataManager.register(EntityTameable.OWNER_UNIQUE_ID, Optional.absent());
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        if (this.getOwnerId() == null) {
            nbttagcompound.setString("OwnerUUID", "");
        } else {
            nbttagcompound.setString("OwnerUUID", this.getOwnerId().toString());
        }

        nbttagcompound.setBoolean("Sitting", this.isSitting());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        String s;

        if (nbttagcompound.hasKey("OwnerUUID", 8)) {
            s = nbttagcompound.getString("OwnerUUID");
        } else {
            String s1 = nbttagcompound.getString("Owner");

            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            try {
                this.setOwnerId(UUID.fromString(s));
                this.setTamed(true);
            } catch (Throwable throwable) {
                this.setTamed(false);
            }
        }

        if (this.aiSit != null) {
            this.aiSit.setSitting(nbttagcompound.getBoolean("Sitting"));
        }

        this.setSitting(nbttagcompound.getBoolean("Sitting"));
    }

    public boolean canBeLeashedTo(EntityPlayer entityhuman) {
        return !this.getLeashed();
    }

    protected void playTameEffect(boolean flag) {
        EnumParticleTypes enumparticle = EnumParticleTypes.HEART;

        if (!flag) {
            enumparticle = EnumParticleTypes.SMOKE_NORMAL;
        }

        for (int i = 0; i < 7; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;

            this.world.spawnParticle(enumparticle, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2, new int[0]);
        }

    }

    public boolean isTamed() {
        return (((Byte) this.dataManager.get(EntityTameable.TAMED)).byteValue() & 4) != 0;
    }

    public void setTamed(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntityTameable.TAMED)).byteValue();

        if (flag) {
            this.dataManager.set(EntityTameable.TAMED, Byte.valueOf((byte) (b0 | 4)));
        } else {
            this.dataManager.set(EntityTameable.TAMED, Byte.valueOf((byte) (b0 & -5)));
        }

        this.setupTamedAI();
    }

    protected void setupTamedAI() {}

    public boolean isSitting() {
        return (((Byte) this.dataManager.get(EntityTameable.TAMED)).byteValue() & 1) != 0;
    }

    public void setSitting(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntityTameable.TAMED)).byteValue();

        if (flag) {
            this.dataManager.set(EntityTameable.TAMED, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.dataManager.set(EntityTameable.TAMED, Byte.valueOf((byte) (b0 & -2)));
        }

    }

    @Nullable
    public UUID getOwnerId() {
        return (UUID) ((Optional) this.dataManager.get(EntityTameable.OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerId(@Nullable UUID uuid) {
        this.dataManager.set(EntityTameable.OWNER_UNIQUE_ID, Optional.fromNullable(uuid));
    }

    public void setTamedBy(EntityPlayer entityhuman) {
        this.setTamed(true);
        this.setOwnerId(entityhuman.getUniqueID());
        if (entityhuman instanceof EntityPlayerMP) {
            CriteriaTriggers.TAME_ANIMAL.trigger((EntityPlayerMP) entityhuman, (EntityAnimal) this);
        }

    }

    @Nullable
    public EntityLivingBase getOwner() {
        try {
            UUID uuid = this.getOwnerId();

            return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public boolean isOwner(EntityLivingBase entityliving) {
        return entityliving == this.getOwner();
    }

    public EntityAISit getAISit() {
        return this.aiSit;
    }

    public boolean shouldAttackEntity(EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        return true;
    }

    public Team getTeam() {
        if (this.isTamed()) {
            EntityLivingBase entityliving = this.getOwner();

            if (entityliving != null) {
                return entityliving.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isOnSameTeam(Entity entity) {
        if (this.isTamed()) {
            EntityLivingBase entityliving = this.getOwner();

            if (entity == entityliving) {
                return true;
            }

            if (entityliving != null) {
                return entityliving.isOnSameTeam(entity);
            }
        }

        return super.isOnSameTeam(entity);
    }

    public void onDeath(DamageSource damagesource) {
        if (!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && this.getOwner() instanceof EntityPlayerMP) {
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
        }

        super.onDeath(damagesource);
    }

    @Nullable
    public Entity getOwner() {
        return this.getOwner();
    }
}

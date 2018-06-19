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

    protected static final DataParameter<Byte> field_184755_bv = EntityDataManager.func_187226_a(EntityTameable.class, DataSerializers.field_187191_a);
    protected static final DataParameter<Optional<UUID>> field_184756_bw = EntityDataManager.func_187226_a(EntityTameable.class, DataSerializers.field_187203_m);
    protected EntityAISit field_70911_d;

    public EntityTameable(World world) {
        super(world);
        this.func_175544_ck();
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityTameable.field_184755_bv, Byte.valueOf((byte) 0));
        this.field_70180_af.func_187214_a(EntityTameable.field_184756_bw, Optional.absent());
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        if (this.func_184753_b() == null) {
            nbttagcompound.func_74778_a("OwnerUUID", "");
        } else {
            nbttagcompound.func_74778_a("OwnerUUID", this.func_184753_b().toString());
        }

        nbttagcompound.func_74757_a("Sitting", this.func_70906_o());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        String s;

        if (nbttagcompound.func_150297_b("OwnerUUID", 8)) {
            s = nbttagcompound.func_74779_i("OwnerUUID");
        } else {
            String s1 = nbttagcompound.func_74779_i("Owner");

            s = PreYggdrasilConverter.func_187473_a(this.func_184102_h(), s1);
        }

        if (!s.isEmpty()) {
            try {
                this.func_184754_b(UUID.fromString(s));
                this.func_70903_f(true);
            } catch (Throwable throwable) {
                this.func_70903_f(false);
            }
        }

        if (this.field_70911_d != null) {
            this.field_70911_d.func_75270_a(nbttagcompound.func_74767_n("Sitting"));
        }

        this.func_70904_g(nbttagcompound.func_74767_n("Sitting"));
    }

    public boolean func_184652_a(EntityPlayer entityhuman) {
        return !this.func_110167_bD();
    }

    protected void func_70908_e(boolean flag) {
        EnumParticleTypes enumparticle = EnumParticleTypes.HEART;

        if (!flag) {
            enumparticle = EnumParticleTypes.SMOKE_NORMAL;
        }

        for (int i = 0; i < 7; ++i) {
            double d0 = this.field_70146_Z.nextGaussian() * 0.02D;
            double d1 = this.field_70146_Z.nextGaussian() * 0.02D;
            double d2 = this.field_70146_Z.nextGaussian() * 0.02D;

            this.field_70170_p.func_175688_a(enumparticle, this.field_70165_t + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, this.field_70163_u + 0.5D + (double) (this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, d0, d1, d2, new int[0]);
        }

    }

    public boolean func_70909_n() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityTameable.field_184755_bv)).byteValue() & 4) != 0;
    }

    public void func_70903_f(boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityTameable.field_184755_bv)).byteValue();

        if (flag) {
            this.field_70180_af.func_187227_b(EntityTameable.field_184755_bv, Byte.valueOf((byte) (b0 | 4)));
        } else {
            this.field_70180_af.func_187227_b(EntityTameable.field_184755_bv, Byte.valueOf((byte) (b0 & -5)));
        }

        this.func_175544_ck();
    }

    protected void func_175544_ck() {}

    public boolean func_70906_o() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityTameable.field_184755_bv)).byteValue() & 1) != 0;
    }

    public void func_70904_g(boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityTameable.field_184755_bv)).byteValue();

        if (flag) {
            this.field_70180_af.func_187227_b(EntityTameable.field_184755_bv, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.field_70180_af.func_187227_b(EntityTameable.field_184755_bv, Byte.valueOf((byte) (b0 & -2)));
        }

    }

    @Nullable
    public UUID func_184753_b() {
        return (UUID) ((Optional) this.field_70180_af.func_187225_a(EntityTameable.field_184756_bw)).orNull();
    }

    public void func_184754_b(@Nullable UUID uuid) {
        this.field_70180_af.func_187227_b(EntityTameable.field_184756_bw, Optional.fromNullable(uuid));
    }

    public void func_193101_c(EntityPlayer entityhuman) {
        this.func_70903_f(true);
        this.func_184754_b(entityhuman.func_110124_au());
        if (entityhuman instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193136_w.func_193178_a((EntityPlayerMP) entityhuman, (EntityAnimal) this);
        }

    }

    @Nullable
    public EntityLivingBase func_70902_q() {
        try {
            UUID uuid = this.func_184753_b();

            return uuid == null ? null : this.field_70170_p.func_152378_a(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public boolean func_152114_e(EntityLivingBase entityliving) {
        return entityliving == this.func_70902_q();
    }

    public EntityAISit func_70907_r() {
        return this.field_70911_d;
    }

    public boolean func_142018_a(EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        return true;
    }

    public Team func_96124_cp() {
        if (this.func_70909_n()) {
            EntityLivingBase entityliving = this.func_70902_q();

            if (entityliving != null) {
                return entityliving.func_96124_cp();
            }
        }

        return super.func_96124_cp();
    }

    public boolean func_184191_r(Entity entity) {
        if (this.func_70909_n()) {
            EntityLivingBase entityliving = this.func_70902_q();

            if (entity == entityliving) {
                return true;
            }

            if (entityliving != null) {
                return entityliving.func_184191_r(entity);
            }
        }

        return super.func_184191_r(entity);
    }

    public void func_70645_a(DamageSource damagesource) {
        if (!this.field_70170_p.field_72995_K && this.field_70170_p.func_82736_K().func_82766_b("showDeathMessages") && this.func_70902_q() instanceof EntityPlayerMP) {
            this.func_70902_q().func_145747_a(this.func_110142_aN().func_151521_b());
        }

        super.func_70645_a(damagesource);
    }

    @Nullable
    public Entity getOwner() {
        return this.getOwner();
    }
}

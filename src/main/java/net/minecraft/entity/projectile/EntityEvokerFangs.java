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

    private int field_190553_a;
    private boolean field_190554_b;
    private int field_190555_c;
    private boolean field_190556_d;
    private EntityLivingBase field_190557_e;
    private UUID field_190558_f;

    public EntityEvokerFangs(World world) {
        super(world);
        this.field_190555_c = 22;
        this.func_70105_a(0.5F, 0.8F);
    }

    public EntityEvokerFangs(World world, double d0, double d1, double d2, float f, int i, EntityLivingBase entityliving) {
        this(world);
        this.field_190553_a = i;
        this.func_190549_a(entityliving);
        this.field_70177_z = f * 57.295776F;
        this.func_70107_b(d0, d1, d2);
    }

    protected void func_70088_a() {}

    public void func_190549_a(@Nullable EntityLivingBase entityliving) {
        this.field_190557_e = entityliving;
        this.field_190558_f = entityliving == null ? null : entityliving.func_110124_au();
    }

    @Nullable
    public EntityLivingBase func_190552_j() {
        if (this.field_190557_e == null && this.field_190558_f != null && this.field_70170_p instanceof WorldServer) {
            Entity entity = ((WorldServer) this.field_70170_p).func_175733_a(this.field_190558_f);

            if (entity instanceof EntityLivingBase) {
                this.field_190557_e = (EntityLivingBase) entity;
            }
        }

        return this.field_190557_e;
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_190553_a = nbttagcompound.func_74762_e("Warmup");
        this.field_190558_f = nbttagcompound.func_186857_a("OwnerUUID");
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("Warmup", this.field_190553_a);
        if (this.field_190558_f != null) {
            nbttagcompound.func_186854_a("OwnerUUID", this.field_190558_f);
        }

    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70170_p.field_72995_K) {
            if (this.field_190556_d) {
                --this.field_190555_c;
                if (this.field_190555_c == 14) {
                    for (int i = 0; i < 12; ++i) {
                        double d0 = this.field_70165_t + (this.field_70146_Z.nextDouble() * 2.0D - 1.0D) * (double) this.field_70130_N * 0.5D;
                        double d1 = this.field_70163_u + 0.05D + this.field_70146_Z.nextDouble() * 1.0D;
                        double d2 = this.field_70161_v + (this.field_70146_Z.nextDouble() * 2.0D - 1.0D) * (double) this.field_70130_N * 0.5D;
                        double d3 = (this.field_70146_Z.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.field_70146_Z.nextDouble() * 0.3D;
                        double d5 = (this.field_70146_Z.nextDouble() * 2.0D - 1.0D) * 0.3D;

                        this.field_70170_p.func_175688_a(EnumParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5, new int[0]);
                    }
                }
            }
        } else if (--this.field_190553_a < 0) {
            if (this.field_190553_a == -8) {
                List list = this.field_70170_p.func_72872_a(EntityLivingBase.class, this.func_174813_aQ().func_72314_b(0.2D, 0.0D, 0.2D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                    this.func_190551_c(entityliving);
                }
            }

            if (!this.field_190554_b) {
                this.field_70170_p.func_72960_a(this, (byte) 4);
                this.field_190554_b = true;
            }

            if (--this.field_190555_c < 0) {
                this.func_70106_y();
            }
        }

    }

    private void func_190551_c(EntityLivingBase entityliving) {
        EntityLivingBase entityliving1 = this.func_190552_j();

        if (entityliving.func_70089_S() && !entityliving.func_190530_aW() && entityliving != entityliving1) {
            if (entityliving1 == null) {
                org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = this; // CraftBukkit
                entityliving.func_70097_a(DamageSource.field_76376_m, 6.0F);
                org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = null; // CraftBukkit
            } else {
                if (entityliving1.func_184191_r(entityliving)) {
                    return;
                }

                entityliving.func_70097_a(DamageSource.func_76354_b(this, entityliving1), 6.0F);
            }

        }
    }
}

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

    public EntityLivingBase field_190539_a; // CraftBukkit - type
    private NBTTagCompound field_190540_b;

    public EntityLlamaSpit(World world) {
        super(world);
    }

    public EntityLlamaSpit(World world, EntityLlama entityllama) {
        super(world);
        this.field_190539_a = entityllama;
        this.func_70107_b(entityllama.field_70165_t - (double) (entityllama.field_70130_N + 1.0F) * 0.5D * (double) MathHelper.func_76126_a(entityllama.field_70761_aq * 0.017453292F), entityllama.field_70163_u + (double) entityllama.func_70047_e() - 0.10000000149011612D, entityllama.field_70161_v + (double) (entityllama.field_70130_N + 1.0F) * 0.5D * (double) MathHelper.func_76134_b(entityllama.field_70761_aq * 0.017453292F));
        this.func_70105_a(0.25F, 0.25F);
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_190540_b != null) {
            this.func_190537_j();
        }

        Vec3d vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        Vec3d vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
        RayTraceResult movingobjectposition = this.field_70170_p.func_72933_a(vec3d, vec3d1);

        vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
        if (movingobjectposition != null) {
            vec3d1 = new Vec3d(movingobjectposition.field_72307_f.field_72450_a, movingobjectposition.field_72307_f.field_72448_b, movingobjectposition.field_72307_f.field_72449_c);
        }

        Entity entity = this.func_190538_a(vec3d, vec3d1);

        if (entity != null) {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null) {
            this.func_190536_a(movingobjectposition);
        }

        this.field_70165_t += this.field_70159_w;
        this.field_70163_u += this.field_70181_x;
        this.field_70161_v += this.field_70179_y;
        float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

        this.field_70177_z = (float) (MathHelper.func_181159_b(this.field_70159_w, this.field_70179_y) * 57.2957763671875D);

        for (this.field_70125_A = (float) (MathHelper.func_181159_b(this.field_70181_x, (double) f) * 57.2957763671875D); this.field_70125_A - this.field_70127_C < -180.0F; this.field_70127_C -= 360.0F) {
            ;
        }

        while (this.field_70125_A - this.field_70127_C >= 180.0F) {
            this.field_70127_C += 360.0F;
        }

        while (this.field_70177_z - this.field_70126_B < -180.0F) {
            this.field_70126_B -= 360.0F;
        }

        while (this.field_70177_z - this.field_70126_B >= 180.0F) {
            this.field_70126_B += 360.0F;
        }

        this.field_70125_A = this.field_70127_C + (this.field_70125_A - this.field_70127_C) * 0.2F;
        this.field_70177_z = this.field_70126_B + (this.field_70177_z - this.field_70126_B) * 0.2F;
        float f1 = 0.99F;
        float f2 = 0.06F;

        if (!this.field_70170_p.func_72875_a(this.func_174813_aQ(), Material.field_151579_a)) {
            this.func_70106_y();
        } else if (this.func_70090_H()) {
            this.func_70106_y();
        } else {
            this.field_70159_w *= 0.9900000095367432D;
            this.field_70181_x *= 0.9900000095367432D;
            this.field_70179_y *= 0.9900000095367432D;
            if (!this.func_189652_ae()) {
                this.field_70181_x -= 0.05999999865889549D;
            }

            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        }
    }

    @Nullable
    private Entity func_190538_a(Vec3d vec3d, Vec3d vec3d1) {
        Entity entity = null;
        List list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72321_a(this.field_70159_w, this.field_70181_x, this.field_70179_y).func_186662_g(1.0D));
        double d0 = 0.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity1 = (Entity) iterator.next();

            if (entity1 != this.field_190539_a) {
                AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_186662_g(0.30000001192092896D);
                RayTraceResult movingobjectposition = axisalignedbb.func_72327_a(vec3d, vec3d1);

                if (movingobjectposition != null) {
                    double d1 = vec3d.func_72436_e(movingobjectposition.field_72307_f);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    public void func_70186_c(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double) f1;
        d1 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double) f1;
        d2 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.field_70159_w = d0;
        this.field_70181_x = d1;
        this.field_70179_y = d2;
        float f3 = MathHelper.func_76133_a(d0 * d0 + d2 * d2);

        this.field_70177_z = (float) (MathHelper.func_181159_b(d0, d2) * 57.2957763671875D);
        this.field_70125_A = (float) (MathHelper.func_181159_b(d1, (double) f3) * 57.2957763671875D);
        this.field_70126_B = this.field_70177_z;
        this.field_70127_C = this.field_70125_A;
    }

    public void func_190536_a(RayTraceResult movingobjectposition) {
        org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // Craftbukkit - Call event
        if (movingobjectposition.field_72308_g != null && this.field_190539_a != null) {
            movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_188403_a(this, this.field_190539_a).func_76349_b(), 1.0F);
        }

        if (!this.field_70170_p.field_72995_K) {
            this.func_70106_y();
        }

    }

    protected void func_70088_a() {}

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("Owner", 10)) {
            this.field_190540_b = nbttagcompound.func_74775_l("Owner");
        }

    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        if (this.field_190539_a != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            UUID uuid = this.field_190539_a.func_110124_au();

            nbttagcompound1.func_186854_a("OwnerUUID", uuid);
            nbttagcompound.func_74782_a("Owner", nbttagcompound1);
        }

    }

    private void func_190537_j() {
        if (this.field_190540_b != null && this.field_190540_b.func_186855_b("OwnerUUID")) {
            UUID uuid = this.field_190540_b.func_186857_a("OwnerUUID");
            List list = this.field_70170_p.func_72872_a(EntityLlama.class, this.func_174813_aQ().func_186662_g(15.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLlama entityllama = (EntityLlama) iterator.next();

                if (entityllama.func_110124_au().equals(uuid)) {
                    this.field_190539_a = entityllama;
                    break;
                }
            }
        }

        this.field_190540_b = null;
    }
}

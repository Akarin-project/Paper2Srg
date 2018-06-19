package net.minecraft.entity.projectile;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public abstract class EntityFireball extends Entity {

    public EntityLivingBase field_70235_a;
    private int field_70236_j;
    private int field_70234_an;
    public double field_70232_b;
    public double field_70233_c;
    public double field_70230_d;
    public float bukkitYield = 1; // CraftBukkit
    public boolean isIncendiary = true; // CraftBukkit

    public EntityFireball(World world) {
        super(world);
        this.func_70105_a(1.0F, 1.0F);
    }

    protected void func_70088_a() {}

    public EntityFireball(World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(world);
        this.func_70105_a(1.0F, 1.0F);
        this.func_70012_b(d0, d1, d2, this.field_70177_z, this.field_70125_A);
        this.func_70107_b(d0, d1, d2);
        double d6 = (double) MathHelper.func_76133_a(d3 * d3 + d4 * d4 + d5 * d5);

        this.field_70232_b = d3 / d6 * 0.1D;
        this.field_70233_c = d4 / d6 * 0.1D;
        this.field_70230_d = d5 / d6 * 0.1D;
    }

    public EntityFireball(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world);
        this.field_70235_a = entityliving;
        this.projectileSource = (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
        this.func_70105_a(1.0F, 1.0F);
        this.func_70012_b(entityliving.field_70165_t, entityliving.field_70163_u, entityliving.field_70161_v, entityliving.field_70177_z, entityliving.field_70125_A);
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        // CraftBukkit start - Added setDirection method
        this.setDirection(d0, d1, d2);
    }

    public void setDirection(double d0, double d1, double d2) {
        // CraftBukkit end
        d0 += this.field_70146_Z.nextGaussian() * 0.4D;
        d1 += this.field_70146_Z.nextGaussian() * 0.4D;
        d2 += this.field_70146_Z.nextGaussian() * 0.4D;
        double d3 = (double) MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2);

        this.field_70232_b = d0 / d3 * 0.1D;
        this.field_70233_c = d1 / d3 * 0.1D;
        this.field_70230_d = d2 / d3 * 0.1D;
    }

    public void func_70071_h_() {
        if (!this.field_70170_p.field_72995_K && (this.field_70235_a != null && this.field_70235_a.field_70128_L || !this.field_70170_p.func_175667_e(new BlockPos(this)))) {
            this.func_70106_y();
        } else {
            super.func_70071_h_();
            if (this.func_184564_k()) {
                this.func_70015_d(1);
            }

            ++this.field_70234_an;
            RayTraceResult movingobjectposition = ProjectileHelper.func_188802_a(this, true, this.field_70234_an >= 25, this.field_70235_a);

            // Paper start - Call ProjectileCollideEvent
            if (movingobjectposition != null && movingobjectposition.field_72308_g != null) {
                com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = CraftEventFactory.callProjectileCollideEvent(this, movingobjectposition);
                if (event.isCancelled()) {
                    movingobjectposition = null;
                }
            }
            // Paper end

            if (movingobjectposition != null) {
                this.func_70227_a(movingobjectposition);

                // CraftBukkit start - Fire ProjectileHitEvent
                if (this.field_70128_L) {
                    CraftEventFactory.callProjectileHitEvent(this, movingobjectposition);
                }
                // CraftBukkit end
            }

            this.field_70165_t += this.field_70159_w;
            this.field_70163_u += this.field_70181_x;
            this.field_70161_v += this.field_70179_y;
            ProjectileHelper.func_188803_a(this, 0.2F);
            float f = this.func_82341_c();

            if (this.func_70090_H()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;

                    this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t - this.field_70159_w * 0.25D, this.field_70163_u - this.field_70181_x * 0.25D, this.field_70161_v - this.field_70179_y * 0.25D, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
                }

                f = 0.8F;
            }

            this.field_70159_w += this.field_70232_b;
            this.field_70181_x += this.field_70233_c;
            this.field_70179_y += this.field_70230_d;
            this.field_70159_w *= (double) f;
            this.field_70181_x *= (double) f;
            this.field_70179_y *= (double) f;
            this.field_70170_p.func_175688_a(this.func_184563_j(), this.field_70165_t, this.field_70163_u + 0.5D, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        }
    }

    protected boolean func_184564_k() {
        return true;
    }

    protected EnumParticleTypes func_184563_j() {
        return EnumParticleTypes.SMOKE_NORMAL;
    }

    protected float func_82341_c() {
        return 0.95F;
    }

    protected abstract void func_70227_a(RayTraceResult movingobjectposition);

    public static void func_189743_a(DataFixer dataconvertermanager, String s) {}

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74782_a("direction", this.func_70087_a(new double[] { this.field_70159_w, this.field_70181_x, this.field_70179_y}));
        nbttagcompound.func_74782_a("power", this.func_70087_a(new double[] { this.field_70232_b, this.field_70233_c, this.field_70230_d}));
        nbttagcompound.func_74768_a("life", this.field_70236_j);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist;

        if (nbttagcompound.func_150297_b("power", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("power", 6);
            if (nbttaglist.func_74745_c() == 3) {
                this.field_70232_b = nbttaglist.func_150309_d(0);
                this.field_70233_c = nbttaglist.func_150309_d(1);
                this.field_70230_d = nbttaglist.func_150309_d(2);
            }
        }

        this.field_70236_j = nbttagcompound.func_74762_e("life");
        if (nbttagcompound.func_150297_b("direction", 9) && nbttagcompound.func_150295_c("direction", 6).func_74745_c() == 3) {
            nbttaglist = nbttagcompound.func_150295_c("direction", 6);
            this.field_70159_w = nbttaglist.func_150309_d(0);
            this.field_70181_x = nbttaglist.func_150309_d(1);
            this.field_70179_y = nbttaglist.func_150309_d(2);
        } else {
            this.func_70106_y();
        }

    }

    public boolean func_70067_L() {
        return true;
    }

    public float func_70111_Y() {
        return 1.0F;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            this.func_70018_K();
            if (damagesource.func_76346_g() != null) {
                // CraftBukkit start
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end
                Vec3d vec3d = damagesource.func_76346_g().func_70040_Z();

                if (vec3d != null) {
                    this.field_70159_w = vec3d.field_72450_a;
                    this.field_70181_x = vec3d.field_72448_b;
                    this.field_70179_y = vec3d.field_72449_c;
                    this.field_70232_b = this.field_70159_w * 0.1D;
                    this.field_70233_c = this.field_70181_x * 0.1D;
                    this.field_70230_d = this.field_70179_y * 0.1D;
                }

                if (damagesource.func_76346_g() instanceof EntityLivingBase) {
                    this.field_70235_a = (EntityLivingBase) damagesource.func_76346_g();
                    this.projectileSource = (org.bukkit.projectiles.ProjectileSource) this.field_70235_a.getBukkitEntity();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public float func_70013_c() {
        return 1.0F;
    }
}

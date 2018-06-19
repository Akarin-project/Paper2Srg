package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityThrowable extends Entity implements IProjectile {

    private int field_145788_c;
    private int field_145786_d;
    private int field_145787_e;
    private Block field_174853_f;
    protected boolean field_174854_a;
    public int field_70191_b;
    public EntityLivingBase field_70192_c;
    public String field_85053_h;
    private int field_70194_h;
    private int field_70195_i;
    public Entity field_184539_c;
    private int field_184540_av;

    public EntityThrowable(World world) {
        super(world);
        this.field_145788_c = -1;
        this.field_145786_d = -1;
        this.field_145787_e = -1;
        this.func_70105_a(0.25F, 0.25F);
    }

    public EntityThrowable(World world, double d0, double d1, double d2) {
        this(world);
        this.func_70107_b(d0, d1, d2);
    }

    public EntityThrowable(World world, EntityLivingBase entityliving) {
        this(world, entityliving.field_70165_t, entityliving.field_70163_u + (double) entityliving.func_70047_e() - 0.10000000149011612D, entityliving.field_70161_v);
        this.field_70192_c = entityliving;
        this.projectileSource = (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
    }

    protected void func_70088_a() {}

    public void func_184538_a(Entity entity, float f, float f1, float f2, float f3, float f4) {
        float f5 = -MathHelper.func_76126_a(f1 * 0.017453292F) * MathHelper.func_76134_b(f * 0.017453292F);
        float f6 = -MathHelper.func_76126_a((f + f2) * 0.017453292F);
        float f7 = MathHelper.func_76134_b(f1 * 0.017453292F) * MathHelper.func_76134_b(f * 0.017453292F);

        this.func_70186_c((double) f5, (double) f6, (double) f7, f3, f4);
        this.field_70159_w += entity.field_70159_w;
        this.field_70179_y += entity.field_70179_y;
        if (!entity.field_70122_E) {
            this.field_70181_x += entity.field_70181_x;
        }

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
        this.field_70194_h = 0;
    }

    public void func_70071_h_() {
        this.field_70142_S = this.field_70165_t;
        this.field_70137_T = this.field_70163_u;
        this.field_70136_U = this.field_70161_v;
        super.func_70071_h_();
        if (this.field_70191_b > 0) {
            --this.field_70191_b;
        }

        if (this.field_174854_a) {
            if (this.field_70170_p.func_180495_p(new BlockPos(this.field_145788_c, this.field_145786_d, this.field_145787_e)).func_177230_c() == this.field_174853_f) {
                ++this.field_70194_h;
                if (this.field_70194_h == 1200) {
                    this.func_70106_y();
                }

                return;
            }

            this.field_174854_a = false;
            this.field_70159_w *= (double) (this.field_70146_Z.nextFloat() * 0.2F);
            this.field_70181_x *= (double) (this.field_70146_Z.nextFloat() * 0.2F);
            this.field_70179_y *= (double) (this.field_70146_Z.nextFloat() * 0.2F);
            this.field_70194_h = 0;
            this.field_70195_i = 0;
        } else {
            ++this.field_70195_i;
        }

        Vec3d vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        Vec3d vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
        RayTraceResult movingobjectposition = this.field_70170_p.func_72933_a(vec3d, vec3d1);

        vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
        if (movingobjectposition != null) {
            vec3d1 = new Vec3d(movingobjectposition.field_72307_f.field_72450_a, movingobjectposition.field_72307_f.field_72448_b, movingobjectposition.field_72307_f.field_72449_c);
        }

        Entity entity = null;
        List list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72321_a(this.field_70159_w, this.field_70181_x, this.field_70179_y).func_186662_g(1.0D));
        double d0 = 0.0D;
        boolean flag = false;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1.func_70067_L()) {
                if (entity1 == this.field_184539_c) {
                    flag = true;
                } else if (this.field_70192_c != null && this.field_70173_aa < 2 && this.field_184539_c == null && this.field_70192_c == entity1) { // CraftBukkit - MC-88491
                    this.field_184539_c = entity1;
                    flag = true;
                } else {
                    flag = false;
                    AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_186662_g(0.30000001192092896D);
                    RayTraceResult movingobjectposition1 = axisalignedbb.func_72327_a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.func_72436_e(movingobjectposition1.field_72307_f);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }
        }

        if (this.field_184539_c != null) {
            if (flag) {
                this.field_184540_av = 2;
            } else if (this.field_184540_av-- <= 0) {
                this.field_184539_c = null;
            }
        }

        if (entity != null) {
            movingobjectposition = new RayTraceResult(entity);
        }

        // Paper start - Call ProjectileCollideEvent
        if (movingobjectposition != null && movingobjectposition.field_72308_g != null) {
            com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileCollideEvent(this, movingobjectposition);
            if (event.isCancelled()) {
                movingobjectposition = null;
            }
        }
        // Paper end

        if (movingobjectposition != null) {
            if (movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK && this.field_70170_p.func_180495_p(movingobjectposition.func_178782_a()).func_177230_c() == Blocks.field_150427_aO) {
                this.func_181015_d(movingobjectposition.func_178782_a());
            } else {
                this.func_70184_a(movingobjectposition);
                // CraftBukkit start
                if (this.field_70128_L) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition);
                }
                // CraftBukkit end
            }
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
        float f2 = this.func_70185_h();

        if (this.func_70090_H()) {
            for (int j = 0; j < 4; ++j) {
                float f3 = 0.25F;

                this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t - this.field_70159_w * 0.25D, this.field_70163_u - this.field_70181_x * 0.25D, this.field_70161_v - this.field_70179_y * 0.25D, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
            }

            f1 = 0.8F;
        }

        this.field_70159_w *= (double) f1;
        this.field_70181_x *= (double) f1;
        this.field_70179_y *= (double) f1;
        if (!this.func_189652_ae()) {
            this.field_70181_x -= (double) f2;
        }

        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    protected float func_70185_h() {
        return 0.03F;
    }

    protected abstract void func_70184_a(RayTraceResult movingobjectposition);

    public static void func_189661_a(DataFixer dataconvertermanager, String s) {}

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("xTile", this.field_145788_c);
        nbttagcompound.func_74768_a("yTile", this.field_145786_d);
        nbttagcompound.func_74768_a("zTile", this.field_145787_e);
        ResourceLocation minecraftkey = (ResourceLocation) Block.field_149771_c.func_177774_c(this.field_174853_f);

        nbttagcompound.func_74778_a("inTile", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.func_74774_a("shake", (byte) this.field_70191_b);
        nbttagcompound.func_74774_a("inGround", (byte) (this.field_174854_a ? 1 : 0));
        if ((this.field_85053_h == null || this.field_85053_h.isEmpty()) && this.field_70192_c instanceof EntityPlayer) {
            this.field_85053_h = this.field_70192_c.func_70005_c_();
        }

        nbttagcompound.func_74778_a("ownerName", this.field_85053_h == null ? "" : this.field_85053_h);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_145788_c = nbttagcompound.func_74762_e("xTile");
        this.field_145786_d = nbttagcompound.func_74762_e("yTile");
        this.field_145787_e = nbttagcompound.func_74762_e("zTile");
        if (nbttagcompound.func_150297_b("inTile", 8)) {
            this.field_174853_f = Block.func_149684_b(nbttagcompound.func_74779_i("inTile"));
        } else {
            this.field_174853_f = Block.func_149729_e(nbttagcompound.func_74771_c("inTile") & 255);
        }

        this.field_70191_b = nbttagcompound.func_74771_c("shake") & 255;
        this.field_174854_a = nbttagcompound.func_74771_c("inGround") == 1;
        this.field_70192_c = null;
        this.field_85053_h = nbttagcompound.func_74779_i("ownerName");
        if (this.field_85053_h != null && this.field_85053_h.isEmpty()) {
            this.field_85053_h = null;
        }
        if (this instanceof EntityEnderPearl && this.field_70170_p != null && this.field_70170_p.paperConfig.disableEnderpearlExploit) { this.field_85053_h = null; } // Paper - Don't store shooter name for pearls to block enderpearl travel exploit

        this.field_70192_c = this.func_85052_h();
    }

    @Nullable
    public EntityLivingBase func_85052_h() {
        if (this.field_70192_c == null && this.field_85053_h != null && !this.field_85053_h.isEmpty()) {
            this.field_70192_c = this.field_70170_p.func_72924_a(this.field_85053_h);
            if (this.field_70192_c == null && this.field_70170_p instanceof WorldServer) {
                try {
                    Entity entity = ((WorldServer) this.field_70170_p).func_175733_a(UUID.fromString(this.field_85053_h));

                    if (entity instanceof EntityLivingBase) {
                        this.field_70192_c = (EntityLivingBase) entity;
                    }
                } catch (Throwable throwable) {
                    this.field_70192_c = null;
                }
            }
        }

        return this.field_70192_c;
    }
}

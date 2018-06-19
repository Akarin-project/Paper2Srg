package net.minecraft.entity.projectile;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityShulkerBullet extends Entity {

    private EntityLivingBase field_184570_a;
    private Entity field_184571_b;
    @Nullable
    private EnumFacing field_184573_c;
    private int field_184575_d;
    private double field_184577_e;
    private double field_184578_f;
    private double field_184579_g;
    @Nullable
    private UUID field_184580_h;
    private BlockPos field_184572_as;
    @Nullable
    private UUID field_184574_at;
    private BlockPos field_184576_au;

    public EntityShulkerBullet(World world) {
        super(world);
        this.func_70105_a(0.3125F, 0.3125F);
        this.field_70145_X = true;
    }

    public SoundCategory func_184176_by() {
        return SoundCategory.HOSTILE;
    }

    public EntityShulkerBullet(World world, EntityLivingBase entityliving, Entity entity, EnumFacing.Axis enumdirection_enumaxis) {
        this(world);
        this.field_184570_a = entityliving;
        BlockPos blockposition = new BlockPos(entityliving);
        double d0 = (double) blockposition.func_177958_n() + 0.5D;
        double d1 = (double) blockposition.func_177956_o() + 0.5D;
        double d2 = (double) blockposition.func_177952_p() + 0.5D;

        this.func_70012_b(d0, d1, d2, this.field_70177_z, this.field_70125_A);
        this.field_184571_b = entity;
        this.field_184573_c = EnumFacing.UP;
        this.func_184569_a(enumdirection_enumaxis);
        projectileSource = (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
    }

    // CraftBukkit start
    public EntityLivingBase getShooter() {
        return this.field_184570_a;
    }

    public void setShooter(EntityLivingBase e) {
        this.field_184570_a = e;
    }

    public Entity getTarget() {
        return this.field_184571_b;
    }

    public void setTarget(Entity e) {
        this.field_184571_b = e;
        this.field_184573_c = EnumFacing.UP;
        this.func_184569_a(EnumFacing.Axis.X);
    }
    // CraftBukkit end

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        BlockPos blockposition;
        NBTTagCompound nbttagcompound1;

        if (this.field_184570_a != null) {
            blockposition = new BlockPos(this.field_184570_a);
            nbttagcompound1 = NBTUtil.func_186862_a(this.field_184570_a.func_110124_au());
            nbttagcompound1.func_74768_a("X", blockposition.func_177958_n());
            nbttagcompound1.func_74768_a("Y", blockposition.func_177956_o());
            nbttagcompound1.func_74768_a("Z", blockposition.func_177952_p());
            nbttagcompound.func_74782_a("Owner", nbttagcompound1);
        }

        if (this.field_184571_b != null) {
            blockposition = new BlockPos(this.field_184571_b);
            nbttagcompound1 = NBTUtil.func_186862_a(this.field_184571_b.func_110124_au());
            nbttagcompound1.func_74768_a("X", blockposition.func_177958_n());
            nbttagcompound1.func_74768_a("Y", blockposition.func_177956_o());
            nbttagcompound1.func_74768_a("Z", blockposition.func_177952_p());
            nbttagcompound.func_74782_a("Target", nbttagcompound1);
        }

        if (this.field_184573_c != null) {
            nbttagcompound.func_74768_a("Dir", this.field_184573_c.func_176745_a());
        }

        nbttagcompound.func_74768_a("Steps", this.field_184575_d);
        nbttagcompound.func_74780_a("TXD", this.field_184577_e);
        nbttagcompound.func_74780_a("TYD", this.field_184578_f);
        nbttagcompound.func_74780_a("TZD", this.field_184579_g);
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_184575_d = nbttagcompound.func_74762_e("Steps");
        this.field_184577_e = nbttagcompound.func_74769_h("TXD");
        this.field_184578_f = nbttagcompound.func_74769_h("TYD");
        this.field_184579_g = nbttagcompound.func_74769_h("TZD");
        if (nbttagcompound.func_150297_b("Dir", 99)) {
            this.field_184573_c = EnumFacing.func_82600_a(nbttagcompound.func_74762_e("Dir"));
        }

        NBTTagCompound nbttagcompound1;

        if (nbttagcompound.func_150297_b("Owner", 10)) {
            nbttagcompound1 = nbttagcompound.func_74775_l("Owner");
            this.field_184580_h = NBTUtil.func_186860_b(nbttagcompound1);
            this.field_184572_as = new BlockPos(nbttagcompound1.func_74762_e("X"), nbttagcompound1.func_74762_e("Y"), nbttagcompound1.func_74762_e("Z"));
        }

        if (nbttagcompound.func_150297_b("Target", 10)) {
            nbttagcompound1 = nbttagcompound.func_74775_l("Target");
            this.field_184574_at = NBTUtil.func_186860_b(nbttagcompound1);
            this.field_184576_au = new BlockPos(nbttagcompound1.func_74762_e("X"), nbttagcompound1.func_74762_e("Y"), nbttagcompound1.func_74762_e("Z"));
        }

    }

    protected void func_70088_a() {}

    private void func_184568_a(@Nullable EnumFacing enumdirection) {
        this.field_184573_c = enumdirection;
    }

    private void func_184569_a(@Nullable EnumFacing.Axis enumdirection_enumaxis) {
        double d0 = 0.5D;
        BlockPos blockposition;

        if (this.field_184571_b == null) {
            blockposition = (new BlockPos(this)).func_177977_b();
        } else {
            d0 = (double) this.field_184571_b.field_70131_O * 0.5D;
            blockposition = new BlockPos(this.field_184571_b.field_70165_t, this.field_184571_b.field_70163_u + d0, this.field_184571_b.field_70161_v);
        }

        double d1 = (double) blockposition.func_177958_n() + 0.5D;
        double d2 = (double) blockposition.func_177956_o() + d0;
        double d3 = (double) blockposition.func_177952_p() + 0.5D;
        EnumFacing enumdirection = null;

        if (blockposition.func_177957_d(this.field_70165_t, this.field_70163_u, this.field_70161_v) >= 4.0D) {
            BlockPos blockposition1 = new BlockPos(this);
            ArrayList arraylist = Lists.newArrayList();

            if (enumdirection_enumaxis != EnumFacing.Axis.X) {
                if (blockposition1.func_177958_n() < blockposition.func_177958_n() && this.field_70170_p.func_175623_d(blockposition1.func_177974_f())) {
                    arraylist.add(EnumFacing.EAST);
                } else if (blockposition1.func_177958_n() > blockposition.func_177958_n() && this.field_70170_p.func_175623_d(blockposition1.func_177976_e())) {
                    arraylist.add(EnumFacing.WEST);
                }
            }

            if (enumdirection_enumaxis != EnumFacing.Axis.Y) {
                if (blockposition1.func_177956_o() < blockposition.func_177956_o() && this.field_70170_p.func_175623_d(blockposition1.func_177984_a())) {
                    arraylist.add(EnumFacing.UP);
                } else if (blockposition1.func_177956_o() > blockposition.func_177956_o() && this.field_70170_p.func_175623_d(blockposition1.func_177977_b())) {
                    arraylist.add(EnumFacing.DOWN);
                }
            }

            if (enumdirection_enumaxis != EnumFacing.Axis.Z) {
                if (blockposition1.func_177952_p() < blockposition.func_177952_p() && this.field_70170_p.func_175623_d(blockposition1.func_177968_d())) {
                    arraylist.add(EnumFacing.SOUTH);
                } else if (blockposition1.func_177952_p() > blockposition.func_177952_p() && this.field_70170_p.func_175623_d(blockposition1.func_177978_c())) {
                    arraylist.add(EnumFacing.NORTH);
                }
            }

            enumdirection = EnumFacing.func_176741_a(this.field_70146_Z);
            if (arraylist.isEmpty()) {
                for (int i = 5; !this.field_70170_p.func_175623_d(blockposition1.func_177972_a(enumdirection)) && i > 0; --i) {
                    enumdirection = EnumFacing.func_176741_a(this.field_70146_Z);
                }
            } else {
                enumdirection = (EnumFacing) arraylist.get(this.field_70146_Z.nextInt(arraylist.size()));
            }

            d1 = this.field_70165_t + (double) enumdirection.func_82601_c();
            d2 = this.field_70163_u + (double) enumdirection.func_96559_d();
            d3 = this.field_70161_v + (double) enumdirection.func_82599_e();
        }

        this.func_184568_a(enumdirection);
        double d4 = d1 - this.field_70165_t;
        double d5 = d2 - this.field_70163_u;
        double d6 = d3 - this.field_70161_v;
        double d7 = (double) MathHelper.func_76133_a(d4 * d4 + d5 * d5 + d6 * d6);

        if (d7 == 0.0D) {
            this.field_184577_e = 0.0D;
            this.field_184578_f = 0.0D;
            this.field_184579_g = 0.0D;
        } else {
            this.field_184577_e = d4 / d7 * 0.15D;
            this.field_184578_f = d5 / d7 * 0.15D;
            this.field_184579_g = d6 / d7 * 0.15D;
        }

        this.field_70160_al = true;
        this.field_184575_d = 10 + this.field_70146_Z.nextInt(5) * 10;
    }

    public void func_70071_h_() {
        if (!this.field_70170_p.field_72995_K && this.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            this.func_70106_y();
        } else {
            super.func_70071_h_();
            if (!this.field_70170_p.field_72995_K) {
                List list;
                Iterator iterator;
                EntityLivingBase entityliving;

                if (this.field_184571_b == null && this.field_184574_at != null) {
                    list = this.field_70170_p.func_72872_a(EntityLivingBase.class, new AxisAlignedBB(this.field_184576_au.func_177982_a(-2, -2, -2), this.field_184576_au.func_177982_a(2, 2, 2)));
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityliving = (EntityLivingBase) iterator.next();
                        if (entityliving.func_110124_au().equals(this.field_184574_at)) {
                            this.field_184571_b = entityliving;
                            break;
                        }
                    }

                    this.field_184574_at = null;
                }

                if (this.field_184570_a == null && this.field_184580_h != null) {
                    list = this.field_70170_p.func_72872_a(EntityLivingBase.class, new AxisAlignedBB(this.field_184572_as.func_177982_a(-2, -2, -2), this.field_184572_as.func_177982_a(2, 2, 2)));
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityliving = (EntityLivingBase) iterator.next();
                        if (entityliving.func_110124_au().equals(this.field_184580_h)) {
                            this.field_184570_a = entityliving;
                            break;
                        }
                    }

                    this.field_184580_h = null;
                }

                if (this.field_184571_b != null && this.field_184571_b.func_70089_S() && (!(this.field_184571_b instanceof EntityPlayer) || !((EntityPlayer) this.field_184571_b).func_175149_v())) {
                    this.field_184577_e = MathHelper.func_151237_a(this.field_184577_e * 1.025D, -1.0D, 1.0D);
                    this.field_184578_f = MathHelper.func_151237_a(this.field_184578_f * 1.025D, -1.0D, 1.0D);
                    this.field_184579_g = MathHelper.func_151237_a(this.field_184579_g * 1.025D, -1.0D, 1.0D);
                    this.field_70159_w += (this.field_184577_e - this.field_70159_w) * 0.2D;
                    this.field_70181_x += (this.field_184578_f - this.field_70181_x) * 0.2D;
                    this.field_70179_y += (this.field_184579_g - this.field_70179_y) * 0.2D;
                } else if (!this.func_189652_ae()) {
                    this.field_70181_x -= 0.04D;
                }

                RayTraceResult movingobjectposition = ProjectileHelper.func_188802_a(this, true, false, this.field_184570_a);

                if (movingobjectposition != null) {
                    this.func_184567_a(movingobjectposition);
                }
            }

            this.func_70107_b(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
            ProjectileHelper.func_188803_a(this, 0.5F);
            if (this.field_70170_p.field_72995_K) {
                this.field_70170_p.func_175688_a(EnumParticleTypes.END_ROD, this.field_70165_t - this.field_70159_w, this.field_70163_u - this.field_70181_x + 0.15D, this.field_70161_v - this.field_70179_y, 0.0D, 0.0D, 0.0D, new int[0]);
            } else if (this.field_184571_b != null && !this.field_184571_b.field_70128_L) {
                if (this.field_184575_d > 0) {
                    --this.field_184575_d;
                    if (this.field_184575_d == 0) {
                        this.func_184569_a(this.field_184573_c == null ? null : this.field_184573_c.func_176740_k());
                    }
                }

                if (this.field_184573_c != null) {
                    BlockPos blockposition = new BlockPos(this);
                    EnumFacing.Axis enumdirection_enumaxis = this.field_184573_c.func_176740_k();

                    if (this.field_70170_p.func_175677_d(blockposition.func_177972_a(this.field_184573_c), false)) {
                        this.func_184569_a(enumdirection_enumaxis);
                    } else {
                        BlockPos blockposition1 = new BlockPos(this.field_184571_b);

                        if (enumdirection_enumaxis == EnumFacing.Axis.X && blockposition.func_177958_n() == blockposition1.func_177958_n() || enumdirection_enumaxis == EnumFacing.Axis.Z && blockposition.func_177952_p() == blockposition1.func_177952_p() || enumdirection_enumaxis == EnumFacing.Axis.Y && blockposition.func_177956_o() == blockposition1.func_177956_o()) {
                            this.func_184569_a(enumdirection_enumaxis);
                        }
                    }
                }
            }

        }
    }

    public boolean func_70027_ad() {
        return false;
    }

    public float func_70013_c() {
        return 1.0F;
    }

    protected void func_184567_a(RayTraceResult movingobjectposition) {
        org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // Craftbukkit - Call event
        if (movingobjectposition.field_72308_g == null) {
            ((WorldServer) this.field_70170_p).func_175739_a(EnumParticleTypes.EXPLOSION_LARGE, this.field_70165_t, this.field_70163_u, this.field_70161_v, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
            this.func_184185_a(SoundEvents.field_187775_eP, 1.0F, 1.0F);
        } else {
            boolean flag = movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_188403_a(this, this.field_184570_a).func_76349_b(), 4.0F);

            if (flag) {
                this.func_174815_a(this.field_184570_a, movingobjectposition.field_72308_g);
                if (movingobjectposition.field_72308_g instanceof EntityLivingBase) {
                    ((EntityLivingBase) movingobjectposition.field_72308_g).func_70690_d(new PotionEffect(MobEffects.field_188424_y, 200));
                }
            }
        }

        this.func_70106_y();
    }

    public boolean func_70067_L() {
        return true;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (!this.field_70170_p.field_72995_K) {
            this.func_184185_a(SoundEvents.field_187777_eQ, 1.0F, 1.0F);
            ((WorldServer) this.field_70170_p).func_175739_a(EnumParticleTypes.CRIT, this.field_70165_t, this.field_70163_u, this.field_70161_v, 15, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
            this.func_70106_y();
        }

        return true;
    }
}

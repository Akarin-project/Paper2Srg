package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGuardian extends EntityMob {

    private static final DataParameter<Boolean> field_190766_bz = EntityDataManager.func_187226_a(EntityGuardian.class, DataSerializers.field_187198_h);
    private static final DataParameter<Integer> field_184723_b = EntityDataManager.func_187226_a(EntityGuardian.class, DataSerializers.field_187192_b);
    protected float field_175482_b;
    protected float field_175484_c;
    protected float field_175483_bk;
    protected float field_175485_bl;
    protected float field_175486_bm;
    private EntityLivingBase field_175478_bn;
    private int field_175479_bo;
    private boolean field_175480_bp;
    public EntityAIWander field_175481_bq;

    public EntityGuardian(World world) {
        super(world);
        this.field_70728_aV = 10;
        this.func_70105_a(0.85F, 0.85F);
        this.field_70765_h = new EntityGuardian.GuardianMoveHelper(this);
        this.field_175482_b = this.field_70146_Z.nextFloat();
        this.field_175484_c = this.field_175482_b;
    }

    protected void func_184651_r() {
        EntityAIMoveTowardsRestriction pathfindergoalmovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D);

        this.field_175481_bq = new EntityAIWander(this, 1.0D, 80);
        this.field_70714_bg.func_75776_a(4, new EntityGuardian.AIGuardianAttack(this));
        this.field_70714_bg.func_75776_a(5, pathfindergoalmovetowardsrestriction);
        this.field_70714_bg.func_75776_a(7, this.field_175481_bq);
        this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
        this.field_70714_bg.func_75776_a(9, new EntityAILookIdle(this));
        this.field_175481_bq.func_75248_a(3);
        pathfindergoalmovetowardsrestriction.func_75248_a(3);
        this.field_70715_bh.func_75776_a(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new EntityGuardian.GuardianTargetSelector(this)));
    }

    public void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(6.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.5D);
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(16.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(30.0D);
    }

    public static void func_189766_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityGuardian.class);
    }

    protected PathNavigate func_175447_b(World world) {
        return new PathNavigateSwimmer(this, world);
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityGuardian.field_190766_bz, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(EntityGuardian.field_184723_b, Integer.valueOf(0));
    }

    public boolean do() {
        return ((Boolean) this.field_70180_af.func_187225_a(EntityGuardian.field_190766_bz)).booleanValue();
    }

    private void func_175476_l(boolean flag) {
        this.field_70180_af.func_187227_b(EntityGuardian.field_190766_bz, Boolean.valueOf(flag));
    }

    public int func_175464_ck() {
        return 80;
    }

    private void func_175463_b(int i) {
        this.field_70180_af.func_187227_b(EntityGuardian.field_184723_b, Integer.valueOf(i));
    }

    public boolean func_175474_cn() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityGuardian.field_184723_b)).intValue() != 0;
    }

    @Nullable
    public EntityLivingBase func_175466_co() {
        if (!this.func_175474_cn()) {
            return null;
        } else if (this.field_70170_p.field_72995_K) {
            if (this.field_175478_bn != null) {
                return this.field_175478_bn;
            } else {
                Entity entity = this.field_70170_p.func_73045_a(((Integer) this.field_70180_af.func_187225_a(EntityGuardian.field_184723_b)).intValue());

                if (entity instanceof EntityLivingBase) {
                    this.field_175478_bn = (EntityLivingBase) entity;
                    return this.field_175478_bn;
                } else {
                    return null;
                }
            }
        } else {
            return this.func_70638_az();
        }
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        super.func_184206_a(datawatcherobject);
        if (EntityGuardian.field_184723_b.equals(datawatcherobject)) {
            this.field_175479_bo = 0;
            this.field_175478_bn = null;
        }

    }

    public int func_70627_aG() {
        return 160;
    }

    protected SoundEvent func_184639_G() {
        return this.func_70090_H() ? SoundEvents.field_187670_cb : SoundEvents.field_187672_cc;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return this.func_70090_H() ? SoundEvents.field_187687_ch : SoundEvents.field_187690_ci;
    }

    protected SoundEvent func_184615_bR() {
        return this.func_70090_H() ? SoundEvents.field_187678_ce : SoundEvents.field_187681_cf;
    }

    protected boolean func_70041_e_() {
        return false;
    }

    public float func_70047_e() {
        return this.field_70131_O * 0.5F;
    }

    public float func_180484_a(BlockPos blockposition) {
        return this.field_70170_p.func_180495_p(blockposition).func_185904_a() == Material.field_151586_h ? 10.0F + this.field_70170_p.func_175724_o(blockposition) - 0.5F : super.func_180484_a(blockposition);
    }

    public void func_70636_d() {
        if (this.world.isClientSide) {
            this.b = this.a;
            if (!this.isInWater()) {
                this.c = 2.0F;
                if (this.motY > 0.0D && this.bE && !this.isSilent()) {
                    this.world.a(this.locX, this.locY, this.locZ, this.dn(), this.bK(), 1.0F, 1.0F, false);
                }

                this.bE = this.motY < 0.0D && this.world.d((new BlockPosition(this)).down(), false);
            } else if (this.do()) {
                if (this.c < 0.5F) {
                    this.c = 4.0F;
                } else {
                    this.c += (0.5F - this.c) * 0.1F;
                }
            } else {
                this.c += (0.125F - this.c) * 0.2F;
            }

            this.a += this.c;
            this.by = this.bx;
            if (!this.isInWater()) {
                this.bx = this.random.nextFloat();
            } else if (this.do()) {
                this.bx += (0.0F - this.bx) * 0.25F;
            } else {
                this.bx += (1.0F - this.bx) * 0.06F;
            }

            if (this.do() && this.isInWater()) {
                Vec3D vec3d = this.e(0.0F);

                for (int i = 0; i < 2; ++i) {
                    this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX + (this.random.nextDouble() - 0.5D) * (double) this.width - vec3d.x * 1.5D, this.locY + this.random.nextDouble() * (double) this.length - vec3d.y * 1.5D, this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.width - vec3d.z * 1.5D, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (this.dp()) {
                if (this.bD < this.p()) {
                    ++this.bD;
                }

                EntityLiving entityliving = this.dq();

                if (entityliving != null) {
                    this.getControllerLook().a(entityliving, 90.0F, 90.0F);
                    this.getControllerLook().a();
                    double d0 = (double) this.s(0.0F);
                    double d1 = entityliving.locX - this.locX;
                    double d2 = entityliving.locY + (double) (entityliving.length * 0.5F) - (this.locY + (double) this.getHeadHeight());
                    double d3 = entityliving.locZ - this.locZ;
                    double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);

                    d1 /= d4;
                    d2 /= d4;
                    d3 /= d4;
                    double d5 = this.random.nextDouble();

                    while (d5 < d4) {
                        d5 += 1.8D - d0 + this.random.nextDouble() * (1.7D - d0);
                        this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX + d1 * d5, this.locY + d2 * d5 + (double) this.getHeadHeight(), this.locZ + d3 * d5, 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
            }
        }

        if (this.inWater) {
            this.setAirTicks(300);
        } else if (this.onGround) {
            this.motY += 0.5D;
            this.motX += (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
            this.motZ += (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
            this.yaw = this.random.nextFloat() * 360.0F;
            this.onGround = false;
            this.impulse = true;
        }

        if (this.dp()) {
            this.yaw = this.aP;
        }

        super.n();
    }

    protected SoundEvent func_190765_dj() {
        return SoundEvents.field_187684_cg;
    }

    public float func_175477_p(float f) {
        return ((float) this.field_175479_bo + f) / (float) this.func_175464_ck();
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186440_v;
    }

    protected boolean func_70814_o() {
        return true;
    }

    public boolean func_70058_J() {
        return this.field_70170_p.func_72917_a(this.func_174813_aQ(), (Entity) this) && this.field_70170_p.func_184144_a(this, this.func_174813_aQ()).isEmpty();
    }

    public boolean func_70601_bi() {
        return (this.field_70146_Z.nextInt(20) == 0 || !this.field_70170_p.func_175710_j(new BlockPos(this))) && super.func_70601_bi();
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (!this.do() && !damagesource.isMagic() && damagesource.i() instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) damagesource.i();

            if (!damagesource.isExplosion()) {
                entityliving.damageEntity(DamageSource.a(this), 2.0F);
            }
        }

        if (this.goalRandomStroll != null) {
            this.goalRandomStroll.i();
        }

        return super.damageEntity(damagesource, f);
    }

    public int func_70646_bf() {
        return 180;
    }

    public void func_191986_a(float f, float f1, float f2) {
        if (this.cC() && this.isInWater()) {
            this.b(f, f1, f2, 0.1F);
            this.move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
            this.motX *= 0.8999999761581421D;
            this.motY *= 0.8999999761581421D;
            this.motZ *= 0.8999999761581421D;
            if (!this.do() && this.getGoalTarget() == null) {
                this.motY -= 0.005D;
            }
        } else {
            super.a(f, f1, f2);
        }

    }

    static class GuardianMoveHelper extends EntityMoveHelper {

        private final EntityGuardian field_179930_g;

        public GuardianMoveHelper(EntityGuardian entityguardian) {
            super(entityguardian);
            this.field_179930_g = entityguardian;
        }

        public void func_75641_c() {
            if (this.field_188491_h == EntityMoveHelper.Action.MOVE_TO && !this.field_179930_g.func_70661_as().func_75500_f()) {
                double d0 = this.field_75646_b - this.field_179930_g.field_70165_t;
                double d1 = this.field_75647_c - this.field_179930_g.field_70163_u;
                double d2 = this.field_75644_d - this.field_179930_g.field_70161_v;
                double d3 = (double) MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2);

                d1 /= d3;
                float f = (float) (MathHelper.func_181159_b(d2, d0) * 57.2957763671875D) - 90.0F;

                this.field_179930_g.field_70177_z = this.func_75639_a(this.field_179930_g.field_70177_z, f, 90.0F);
                this.field_179930_g.field_70761_aq = this.field_179930_g.field_70177_z;
                float f1 = (float) (this.field_75645_e * this.field_179930_g.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e());

                this.field_179930_g.func_70659_e(this.field_179930_g.func_70689_ay() + (f1 - this.field_179930_g.func_70689_ay()) * 0.125F);
                double d4 = Math.sin((double) (this.field_179930_g.field_70173_aa + this.field_179930_g.func_145782_y()) * 0.5D) * 0.05D;
                double d5 = Math.cos((double) (this.field_179930_g.field_70177_z * 0.017453292F));
                double d6 = Math.sin((double) (this.field_179930_g.field_70177_z * 0.017453292F));

                this.field_179930_g.field_70159_w += d4 * d5;
                this.field_179930_g.field_70179_y += d4 * d6;
                d4 = Math.sin((double) (this.field_179930_g.field_70173_aa + this.field_179930_g.func_145782_y()) * 0.75D) * 0.05D;
                this.field_179930_g.field_70181_x += d4 * (d6 + d5) * 0.25D;
                this.field_179930_g.field_70181_x += (double) this.field_179930_g.func_70689_ay() * d1 * 0.1D;
                EntityLookHelper controllerlook = this.field_179930_g.func_70671_ap();
                double d7 = this.field_179930_g.field_70165_t + d0 / d3 * 2.0D;
                double d8 = (double) this.field_179930_g.func_70047_e() + this.field_179930_g.field_70163_u + d1 / d3;
                double d9 = this.field_179930_g.field_70161_v + d2 / d3 * 2.0D;
                double d10 = controllerlook.func_180423_e();
                double d11 = controllerlook.func_180422_f();
                double d12 = controllerlook.func_180421_g();

                if (!controllerlook.func_180424_b()) {
                    d10 = d7;
                    d11 = d8;
                    d12 = d9;
                }

                this.field_179930_g.func_70671_ap().func_75650_a(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
                this.field_179930_g.func_175476_l(true);
            } else {
                this.field_179930_g.func_70659_e(0.0F);
                this.field_179930_g.func_175476_l(false);
            }
        }
    }

    static class AIGuardianAttack extends EntityAIBase {

        private final EntityGuardian field_179456_a;
        private int field_179455_b;
        private final boolean field_190881_c;

        public AIGuardianAttack(EntityGuardian entityguardian) {
            this.field_179456_a = entityguardian;
            this.field_190881_c = entityguardian instanceof EntityElderGuardian;
            this.func_75248_a(3);
        }

        public boolean func_75250_a() {
            EntityLivingBase entityliving = this.field_179456_a.func_70638_az();

            return entityliving != null && entityliving.func_70089_S();
        }

        public boolean func_75253_b() {
            return super.func_75253_b() && (this.field_190881_c || this.field_179456_a.func_70068_e(this.field_179456_a.func_70638_az()) > 9.0D);
        }

        public void func_75249_e() {
            this.field_179455_b = -10;
            this.field_179456_a.func_70661_as().func_75499_g();
            this.field_179456_a.func_70671_ap().func_75651_a(this.field_179456_a.func_70638_az(), 90.0F, 90.0F);
            this.field_179456_a.field_70160_al = true;
        }

        public void func_75251_c() {
            this.field_179456_a.func_175463_b(0);
            this.field_179456_a.func_70624_b((EntityLivingBase) null);
            this.field_179456_a.field_175481_bq.func_179480_f();
        }

        public void func_75246_d() {
            EntityLivingBase entityliving = this.field_179456_a.func_70638_az();

            this.field_179456_a.func_70661_as().func_75499_g();
            this.field_179456_a.func_70671_ap().func_75651_a(entityliving, 90.0F, 90.0F);
            if (!this.field_179456_a.func_70685_l(entityliving)) {
                this.field_179456_a.func_70624_b((EntityLivingBase) null);
            } else {
                ++this.field_179455_b;
                if (this.field_179455_b == 0) {
                    this.field_179456_a.func_175463_b(this.field_179456_a.func_70638_az().func_145782_y());
                    this.field_179456_a.field_70170_p.func_72960_a(this.field_179456_a, (byte) 21);
                } else if (this.field_179455_b >= this.field_179456_a.func_175464_ck()) {
                    float f = 1.0F;

                    if (this.field_179456_a.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) {
                        f += 2.0F;
                    }

                    if (this.field_190881_c) {
                        f += 2.0F;
                    }

                    entityliving.func_70097_a(DamageSource.func_76354_b(this.field_179456_a, this.field_179456_a), f);
                    entityliving.func_70097_a(DamageSource.func_76358_a(this.field_179456_a), (float) this.field_179456_a.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e());
                    this.field_179456_a.func_70624_b((EntityLivingBase) null);
                }

                super.func_75246_d();
            }
        }
    }

    static class GuardianTargetSelector implements Predicate<EntityLivingBase> {

        private final EntityGuardian field_179916_a;

        public GuardianTargetSelector(EntityGuardian entityguardian) {
            this.field_179916_a = entityguardian;
        }

        public boolean apply(@Nullable EntityLivingBase entityliving) {
            return (entityliving instanceof EntityPlayer || entityliving instanceof EntitySquid) && entityliving.func_70068_e(this.field_179916_a) > 9.0D;
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((EntityLivingBase) object);
        }
    }
}

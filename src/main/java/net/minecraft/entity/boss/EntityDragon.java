package net.minecraft.entity.boss;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

// CraftBukkit start
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
// CraftBukkit end

// PAIL: Fixme
public class EntityDragon extends EntityLiving implements IEntityMultiPart, IMob {

    private static final Logger field_184675_bH = LogManager.getLogger();
    public static final DataParameter<Integer> field_184674_a = EntityDataManager.func_187226_a(EntityDragon.class, DataSerializers.field_187192_b);
    public double[][] field_70979_e = new double[64][3];
    public int field_70976_f = -1;
    public MultiPartEntityPart[] field_70977_g;
    public MultiPartEntityPart field_70986_h = new MultiPartEntityPart(this, "head", 6.0F, 6.0F);
    public MultiPartEntityPart field_184673_bv = new MultiPartEntityPart(this, "neck", 6.0F, 6.0F);
    public MultiPartEntityPart field_70987_i = new MultiPartEntityPart(this, "body", 8.0F, 8.0F);
    public MultiPartEntityPart field_70985_j = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart field_70984_by = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart field_70982_bz = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart field_70983_bA = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
    public MultiPartEntityPart field_70990_bB = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
    public float field_70991_bC;
    public float field_70988_bD;
    public boolean field_70994_bF;
    public int field_70995_bG;
    public EntityEnderCrystal field_70992_bH;
    private final DragonFightManager field_184676_bI;
    private final PhaseManager field_184677_bJ;
    private int field_184678_bK = 200;
    private int field_184679_bL;
    private final PathPoint[] field_184680_bM = new PathPoint[24];
    private final int[] field_184681_bN = new int[24];
    private final PathHeap field_184682_bO = new PathHeap();
    private Explosion explosionSource = new Explosion(null, this, Double.NaN, Double.NaN, Double.NaN, Float.NaN, true, true); // CraftBukkit - reusable source for CraftTNTPrimed.getSource()

    public EntityDragon(World world) {
        super(world);
        this.field_70977_g = new MultiPartEntityPart[] { this.field_70986_h, this.field_184673_bv, this.field_70987_i, this.field_70985_j, this.field_70984_by, this.field_70982_bz, this.field_70983_bA, this.field_70990_bB};
        this.func_70606_j(this.func_110138_aP());
        this.func_70105_a(16.0F, 8.0F);
        this.field_70145_X = true;
        this.field_70178_ae = true;
        this.field_184678_bK = 100;
        this.field_70158_ak = true;
        if (!world.field_72995_K && world.field_73011_w instanceof WorldProviderEnd) {
            this.field_184676_bI = ((WorldProviderEnd) world.field_73011_w).func_186063_s();
        } else {
            this.field_184676_bI = null;
        }

        this.field_184677_bJ = new PhaseManager(this);
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(200.0D);
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.func_184212_Q().func_187214_a(EntityDragon.field_184674_a, Integer.valueOf(PhaseList.field_188751_k.func_188740_b()));
    }

    public double[] func_70974_a(int i, float f) {
        if (this.func_110143_aJ() <= 0.0F) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.field_70976_f - i & 63;
        int k = this.field_70976_f - i - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.field_70979_e[j][0];
        double d1 = MathHelper.func_76138_g(this.field_70979_e[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.field_70979_e[j][1];
        d1 = this.field_70979_e[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.field_70979_e[j][2] + (this.field_70979_e[k][2] - this.field_70979_e[j][2]) * (double) f;
        return adouble;
    }

    public void func_70636_d() {
        float f;
        float f1;

        if (this.field_70170_p.field_72995_K) {
            this.func_70606_j(this.func_110143_aJ());
            if (!this.func_174814_R()) {
                f = MathHelper.func_76134_b(this.field_70988_bD * 6.2831855F);
                f1 = MathHelper.func_76134_b(this.field_70991_bC * 6.2831855F);
                if (f1 <= -0.3F && f >= -0.3F) {
                    this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187524_aN, this.func_184176_by(), 5.0F, 0.8F + this.field_70146_Z.nextFloat() * 0.3F, false);
                }

                if (!this.field_184677_bJ.func_188756_a().func_188654_a() && --this.field_184678_bK < 0) {
                    this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187525_aO, this.func_184176_by(), 2.5F, 0.8F + this.field_70146_Z.nextFloat() * 0.3F, false);
                    this.field_184678_bK = 200 + this.field_70146_Z.nextInt(200);
                }
            }
        }

        this.field_70991_bC = this.field_70988_bD;
        float f2;

        if (this.func_110143_aJ() <= 0.0F) {
            f = (this.field_70146_Z.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.field_70146_Z.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.field_70146_Z.nextFloat() - 0.5F) * 8.0F;
            this.field_70170_p.func_175688_a(EnumParticleTypes.EXPLOSION_LARGE, this.field_70165_t + (double) f, this.field_70163_u + 2.0D + (double) f1, this.field_70161_v + (double) f2, 0.0D, 0.0D, 0.0D, new int[0]);
        } else {
            this.func_70969_j();
            f = 0.2F / (MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.field_70181_x);
            if (this.field_184677_bJ.func_188756_a().func_188654_a()) {
                this.field_70988_bD += 0.1F;
            } else if (this.field_70994_bF) {
                this.field_70988_bD += f * 0.5F;
            } else {
                this.field_70988_bD += f;
            }

            this.field_70177_z = MathHelper.func_76142_g(this.field_70177_z);
            if (this.func_175446_cd()) {
                this.field_70988_bD = 0.5F;
            } else {
                if (this.field_70976_f < 0) {
                    for (int i = 0; i < this.field_70979_e.length; ++i) {
                        this.field_70979_e[i][0] = (double) this.field_70177_z;
                        this.field_70979_e[i][1] = this.field_70163_u;
                    }
                }

                if (++this.field_70976_f == this.field_70979_e.length) {
                    this.field_70976_f = 0;
                }

                this.field_70979_e[this.field_70976_f][0] = (double) this.field_70177_z;
                this.field_70979_e[this.field_70976_f][1] = this.field_70163_u;
                double d0;
                double d1;
                double d2;
                float f3;
                float f4;

                if (this.field_70170_p.field_72995_K) {
                    if (this.field_70716_bi > 0) {
                        double d3 = this.field_70165_t + (this.field_184623_bh - this.field_70165_t) / (double) this.field_70716_bi;

                        d0 = this.field_70163_u + (this.field_184624_bi - this.field_70163_u) / (double) this.field_70716_bi;
                        d1 = this.field_70161_v + (this.field_184625_bj - this.field_70161_v) / (double) this.field_70716_bi;
                        d2 = MathHelper.func_76138_g(this.field_184626_bk - (double) this.field_70177_z);
                        this.field_70177_z = (float) ((double) this.field_70177_z + d2 / (double) this.field_70716_bi);
                        this.field_70125_A = (float) ((double) this.field_70125_A + (this.field_70709_bj - (double) this.field_70125_A) / (double) this.field_70716_bi);
                        --this.field_70716_bi;
                        this.func_70107_b(d3, d0, d1);
                        this.func_70101_b(this.field_70177_z, this.field_70125_A);
                    }

                    this.field_184677_bJ.func_188756_a().func_188657_b();
                } else {
                    IPhase idragoncontroller = this.field_184677_bJ.func_188756_a();

                    idragoncontroller.func_188659_c();
                    if (this.field_184677_bJ.func_188756_a() != idragoncontroller) {
                        idragoncontroller = this.field_184677_bJ.func_188756_a();
                        idragoncontroller.func_188659_c();
                    }

                    Vec3d vec3d = idragoncontroller.func_188650_g();

                    if (vec3d != null && idragoncontroller.func_188652_i() != PhaseList.field_188751_k) { // CraftBukkit - Don't move when hovering // PAIL: rename
                        d0 = vec3d.field_72450_a - this.field_70165_t;
                        d1 = vec3d.field_72448_b - this.field_70163_u;
                        d2 = vec3d.field_72449_c - this.field_70161_v;
                        double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                        f3 = idragoncontroller.func_188651_f();
                        d1 = MathHelper.func_151237_a(d1 / (double) MathHelper.func_76133_a(d0 * d0 + d2 * d2), (double) (-f3), (double) f3);
                        this.field_70181_x += d1 * 0.10000000149011612D;
                        this.field_70177_z = MathHelper.func_76142_g(this.field_70177_z);
                        double d5 = MathHelper.func_151237_a(MathHelper.func_76138_g(180.0D - MathHelper.func_181159_b(d0, d2) * 57.2957763671875D - (double) this.field_70177_z), -50.0D, 50.0D);
                        Vec3d vec3d1 = (new Vec3d(vec3d.field_72450_a - this.field_70165_t, vec3d.field_72448_b - this.field_70163_u, vec3d.field_72449_c - this.field_70161_v)).func_72432_b();
                        Vec3d vec3d2 = (new Vec3d((double) MathHelper.func_76126_a(this.field_70177_z * 0.017453292F), this.field_70181_x, (double) (-MathHelper.func_76134_b(this.field_70177_z * 0.017453292F)))).func_72432_b();

                        f4 = Math.max(((float) vec3d2.func_72430_b(vec3d1) + 0.5F) / 1.5F, 0.0F);
                        this.field_70704_bt *= 0.8F;
                        this.field_70704_bt = (float) ((double) this.field_70704_bt + d5 * (double) idragoncontroller.func_188653_h());
                        this.field_70177_z += this.field_70704_bt * 0.1F;
                        float f5 = (float) (2.0D / (d4 + 1.0D));
                        float f6 = 0.06F;

                        this.func_191958_b(0.0F, 0.0F, -1.0F, 0.06F * (f4 * f5 + (1.0F - f5)));
                        if (this.field_70994_bF) {
                            this.func_70091_d(MoverType.SELF, this.field_70159_w * 0.800000011920929D, this.field_70181_x * 0.800000011920929D, this.field_70179_y * 0.800000011920929D);
                        } else {
                            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                        }

                        Vec3d vec3d3 = (new Vec3d(this.field_70159_w, this.field_70181_x, this.field_70179_y)).func_72432_b();
                        float f7 = ((float) vec3d3.func_72430_b(vec3d2) + 1.0F) / 2.0F;

                        f7 = 0.8F + 0.15F * f7;
                        this.field_70159_w *= (double) f7;
                        this.field_70179_y *= (double) f7;
                        this.field_70181_x *= 0.9100000262260437D;
                    }
                }

                this.field_70761_aq = this.field_70177_z;
                this.field_70986_h.field_70130_N = 1.0F;
                this.field_70986_h.field_70131_O = 1.0F;
                this.field_184673_bv.field_70130_N = 3.0F;
                this.field_184673_bv.field_70131_O = 3.0F;
                this.field_70985_j.field_70130_N = 2.0F;
                this.field_70985_j.field_70131_O = 2.0F;
                this.field_70984_by.field_70130_N = 2.0F;
                this.field_70984_by.field_70131_O = 2.0F;
                this.field_70982_bz.field_70130_N = 2.0F;
                this.field_70982_bz.field_70131_O = 2.0F;
                this.field_70987_i.field_70131_O = 3.0F;
                this.field_70987_i.field_70130_N = 5.0F;
                this.field_70983_bA.field_70131_O = 2.0F;
                this.field_70983_bA.field_70130_N = 4.0F;
                this.field_70990_bB.field_70131_O = 3.0F;
                this.field_70990_bB.field_70130_N = 4.0F;
                Vec3d[] avec3d = new Vec3d[this.field_70977_g.length];

                for (int j = 0; j < this.field_70977_g.length; ++j) {
                    avec3d[j] = new Vec3d(this.field_70977_g[j].field_70165_t, this.field_70977_g[j].field_70163_u, this.field_70977_g[j].field_70161_v);
                }

                f2 = (float) (this.func_70974_a(5, 1.0F)[1] - this.func_70974_a(10, 1.0F)[1]) * 10.0F * 0.017453292F;
                float f8 = MathHelper.func_76134_b(f2);
                float f9 = MathHelper.func_76126_a(f2);
                float f10 = this.field_70177_z * 0.017453292F;
                float f11 = MathHelper.func_76126_a(f10);
                float f12 = MathHelper.func_76134_b(f10);

                this.field_70987_i.func_70071_h_();
                this.field_70987_i.func_70012_b(this.field_70165_t + (double) (f11 * 0.5F), this.field_70163_u, this.field_70161_v - (double) (f12 * 0.5F), 0.0F, 0.0F);
                this.field_70983_bA.func_70071_h_();
                this.field_70983_bA.func_70012_b(this.field_70165_t + (double) (f12 * 4.5F), this.field_70163_u + 2.0D, this.field_70161_v + (double) (f11 * 4.5F), 0.0F, 0.0F);
                this.field_70990_bB.func_70071_h_();
                this.field_70990_bB.func_70012_b(this.field_70165_t - (double) (f12 * 4.5F), this.field_70163_u + 2.0D, this.field_70161_v - (double) (f11 * 4.5F), 0.0F, 0.0F);
                if (!this.field_70170_p.field_72995_K && this.field_70737_aN == 0) {
                    this.func_70970_a(this.field_70170_p.func_72839_b(this, this.field_70983_bA.func_174813_aQ().func_72314_b(4.0D, 2.0D, 4.0D).func_72317_d(0.0D, -2.0D, 0.0D)));
                    this.func_70970_a(this.field_70170_p.func_72839_b(this, this.field_70990_bB.func_174813_aQ().func_72314_b(4.0D, 2.0D, 4.0D).func_72317_d(0.0D, -2.0D, 0.0D)));
                    this.func_70971_b(this.field_70170_p.func_72839_b(this, this.field_70986_h.func_174813_aQ().func_186662_g(1.0D)));
                    this.func_70971_b(this.field_70170_p.func_72839_b(this, this.field_184673_bv.func_174813_aQ().func_186662_g(1.0D)));
                }

                double[] adouble = this.func_70974_a(5, 1.0F);
                float f13 = MathHelper.func_76126_a(this.field_70177_z * 0.017453292F - this.field_70704_bt * 0.01F);
                float f14 = MathHelper.func_76134_b(this.field_70177_z * 0.017453292F - this.field_70704_bt * 0.01F);

                this.field_70986_h.func_70071_h_();
                this.field_184673_bv.func_70071_h_();
                f3 = this.func_184662_q(1.0F);
                this.field_70986_h.func_70012_b(this.field_70165_t + (double) (f13 * 6.5F * f8), this.field_70163_u + (double) f3 + (double) (f9 * 6.5F), this.field_70161_v - (double) (f14 * 6.5F * f8), 0.0F, 0.0F);
                this.field_184673_bv.func_70012_b(this.field_70165_t + (double) (f13 * 5.5F * f8), this.field_70163_u + (double) f3 + (double) (f9 * 5.5F), this.field_70161_v - (double) (f14 * 5.5F * f8), 0.0F, 0.0F);

                int k;

                for (k = 0; k < 3; ++k) {
                    MultiPartEntityPart entitycomplexpart = null;

                    if (k == 0) {
                        entitycomplexpart = this.field_70985_j;
                    }

                    if (k == 1) {
                        entitycomplexpart = this.field_70984_by;
                    }

                    if (k == 2) {
                        entitycomplexpart = this.field_70982_bz;
                    }

                    double[] adouble1 = this.func_70974_a(12 + k * 2, 1.0F);
                    float f15 = this.field_70177_z * 0.017453292F + this.func_70973_b(adouble1[0] - adouble[0]) * 0.017453292F;
                    float f16 = MathHelper.func_76126_a(f15);
                    float f17 = MathHelper.func_76134_b(f15);
                    float f18 = 1.5F;

                    f4 = (float) (k + 1) * 2.0F;
                    entitycomplexpart.func_70071_h_();
                    entitycomplexpart.func_70012_b(this.field_70165_t - (double) ((f11 * 1.5F + f16 * f4) * f8), this.field_70163_u + (adouble1[1] - adouble[1]) - (double) ((f4 + 1.5F) * f9) + 1.5D, this.field_70161_v + (double) ((f12 * 1.5F + f17 * f4) * f8), 0.0F, 0.0F);
                }

                if (!this.field_70170_p.field_72995_K) {
                    this.field_70994_bF = this.func_70972_a(this.field_70986_h.func_174813_aQ()) | this.func_70972_a(this.field_184673_bv.func_174813_aQ()) | this.func_70972_a(this.field_70987_i.func_174813_aQ());
                    if (this.field_184676_bI != null) {
                        this.field_184676_bI.func_186099_b(this);
                    }
                }

                for (k = 0; k < this.field_70977_g.length; ++k) {
                    this.field_70977_g[k].field_70169_q = avec3d[k].field_72450_a;
                    this.field_70977_g[k].field_70167_r = avec3d[k].field_72448_b;
                    this.field_70977_g[k].field_70166_s = avec3d[k].field_72449_c;
                }

            }
        }
    }

    private float func_184662_q(float f) {
        double d0;

        if (this.field_184677_bJ.func_188756_a().func_188654_a()) {
            d0 = -1.0D;
        } else {
            double[] adouble = this.func_70974_a(5, 1.0F);
            double[] adouble1 = this.func_70974_a(0, 1.0F);

            d0 = adouble[1] - adouble1[1];
        }

        return (float) d0;
    }

    private void func_70969_j() {
        if (this.field_70992_bH != null) {
            if (this.field_70992_bH.field_70128_L) {
                this.field_70992_bH = null;
            } else if (this.field_70173_aa % 10 == 0 && this.func_110143_aJ() < this.func_110138_aP()) {
                // CraftBukkit start
                EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), 1.0F, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
                this.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.func_70606_j((float) (this.func_110143_aJ() + event.getAmount()));
                }
                // CraftBukkit end
            }
        }

        if (this.field_70146_Z.nextInt(10) == 0) {
            List list = this.field_70170_p.func_72872_a(EntityEnderCrystal.class, this.func_174813_aQ().func_186662_g(32.0D));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal) iterator.next();
                double d1 = entityendercrystal1.func_70068_e(this);

                if (d1 < d0) {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.field_70992_bH = entityendercrystal;
        }

    }

    private void func_70970_a(List<Entity> list) {
        double d0 = (this.field_70987_i.func_174813_aQ().field_72340_a + this.field_70987_i.func_174813_aQ().field_72336_d) / 2.0D;
        double d1 = (this.field_70987_i.func_174813_aQ().field_72339_c + this.field_70987_i.func_174813_aQ().field_72334_f) / 2.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLivingBase) {
                double d2 = entity.field_70165_t - d0;
                double d3 = entity.field_70161_v - d1;
                double d4 = d2 * d2 + d3 * d3;

                entity.func_70024_g(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
                if (!this.field_184677_bJ.func_188756_a().func_188654_a() && ((EntityLivingBase) entity).func_142015_aE() < entity.field_70173_aa - 2) {
                    entity.func_70097_a(DamageSource.func_76358_a(this), 5.0F);
                    this.func_174815_a((EntityLivingBase) this, entity);
                }
            }
        }

    }

    private void func_70971_b(List<Entity> list) {
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (entity instanceof EntityLivingBase) {
                entity.func_70097_a(DamageSource.func_76358_a(this), 10.0F);
                this.func_174815_a((EntityLivingBase) this, entity);
            }
        }

    }

    private float func_70973_b(double d0) {
        return (float) MathHelper.func_76138_g(d0);
    }

    private boolean func_70972_a(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.func_76128_c(axisalignedbb.field_72340_a);
        int j = MathHelper.func_76128_c(axisalignedbb.field_72338_b);
        int k = MathHelper.func_76128_c(axisalignedbb.field_72339_c);
        int l = MathHelper.func_76128_c(axisalignedbb.field_72336_d);
        int i1 = MathHelper.func_76128_c(axisalignedbb.field_72337_e);
        int j1 = MathHelper.func_76128_c(axisalignedbb.field_72334_f);
        boolean flag = false;
        boolean flag1 = false;
        // CraftBukkit start - Create a list to hold all the destroyed blocks
        List<org.bukkit.block.Block> destroyedBlocks = new java.util.ArrayList<org.bukkit.block.Block>();
        org.bukkit.craftbukkit.CraftWorld craftWorld = this.field_70170_p.getWorld();
        // CraftBukkit end

        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    BlockPos blockposition = new BlockPos(k1, l1, i2);
                    IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);
                    Block block = iblockdata.func_177230_c();

                    if (iblockdata.func_185904_a() != Material.field_151579_a && iblockdata.func_185904_a() != Material.field_151581_o) {
                        if (!this.field_70170_p.func_82736_K().func_82766_b("mobGriefing")) {
                            flag = true;
                        } else if (block != Blocks.field_180401_cv && block != Blocks.field_150343_Z && block != Blocks.field_150377_bs && block != Blocks.field_150357_h && block != Blocks.field_150384_bq && block != Blocks.field_150378_br) {
                            if (block != Blocks.field_150483_bI && block != Blocks.field_185776_dc && block != Blocks.field_185777_dd && block != Blocks.field_150411_aY && block != Blocks.field_185775_db) {
                                // CraftBukkit start - Add blocks to list rather than destroying them
                                // flag1 = this.world.setAir(blockposition) || flag1;
                                flag1 = true;
                                destroyedBlocks.add(craftWorld.getBlockAt(k1, l1, i2));
                                // CraftBukkit end
                            } else {
                                flag = true;
                            }
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }

        // CraftBukkit start - Set off an EntityExplodeEvent for the dragon exploding all these blocks
        org.bukkit.entity.Entity bukkitEntity = this.getBukkitEntity();
        EntityExplodeEvent event = new EntityExplodeEvent(bukkitEntity, bukkitEntity.getLocation(), destroyedBlocks, 0F);
        bukkitEntity.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            // This flag literally means 'Dragon hit something hard' (Obsidian, White Stone or Bedrock) and will cause the dragon to slow down.
            // We should consider adding an event extension for it, or perhaps returning true if the event is cancelled.
            return flag;
        } else if (event.getYield() == 0F) {
            // Yield zero ==> no drops
            for (org.bukkit.block.Block block : event.blockList()) {
                this.field_70170_p.func_175698_g(new BlockPos(block.getX(), block.getY(), block.getZ()));
            }
        } else {
            for (org.bukkit.block.Block block : event.blockList()) {
                org.bukkit.Material blockId = block.getType();
                if (blockId == org.bukkit.Material.AIR) {
                    continue;
                }

                int blockX = block.getX();
                int blockY = block.getY();
                int blockZ = block.getZ();

                Block nmsBlock = org.bukkit.craftbukkit.util.CraftMagicNumbers.getBlock(blockId);
                if (nmsBlock.func_149659_a(explosionSource)) {
                    nmsBlock.func_180653_a(this.field_70170_p, new BlockPos(blockX, blockY, blockZ), nmsBlock.func_176203_a(block.getData()), event.getYield(), 0);
                }
                nmsBlock.func_180652_a(field_70170_p, new BlockPos(blockX, blockY, blockZ), explosionSource);

                this.field_70170_p.func_175698_g(new BlockPos(blockX, blockY, blockZ));
            }
        }
        // CraftBukkit end

        if (flag1) {
            double d0 = axisalignedbb.field_72340_a + (axisalignedbb.field_72336_d - axisalignedbb.field_72340_a) * (double) this.field_70146_Z.nextFloat();
            double d1 = axisalignedbb.field_72338_b + (axisalignedbb.field_72337_e - axisalignedbb.field_72338_b) * (double) this.field_70146_Z.nextFloat();
            double d2 = axisalignedbb.field_72339_c + (axisalignedbb.field_72334_f - axisalignedbb.field_72339_c) * (double) this.field_70146_Z.nextFloat();

            this.field_70170_p.func_175688_a(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        return flag;
    }

    public boolean func_70965_a(MultiPartEntityPart entitycomplexpart, DamageSource damagesource, float f) {
        f = this.field_184677_bJ.func_188756_a().func_188656_a(entitycomplexpart, damagesource, f);
        if (entitycomplexpart != this.field_70986_h) {
            f = f / 4.0F + Math.min(f, 1.0F);
        }

        if (f < 0.01F) {
            return false;
        } else {
            if (damagesource.func_76346_g() instanceof EntityPlayer || damagesource.func_94541_c()) {
                float f1 = this.func_110143_aJ();

                this.func_82195_e(damagesource, f);
                if (this.func_110143_aJ() <= 0.0F && !this.field_184677_bJ.func_188756_a().func_188654_a()) {
                    this.func_70606_j(1.0F);
                    this.field_184677_bJ.func_188758_a(PhaseList.field_188750_j);
                }

                if (this.field_184677_bJ.func_188756_a().func_188654_a()) {
                    this.field_184679_bL = (int) ((float) this.field_184679_bL + (f1 - this.func_110143_aJ()));
                    if ((float) this.field_184679_bL > 0.25F * this.func_110138_aP()) {
                        this.field_184679_bL = 0;
                        this.field_184677_bJ.func_188758_a(PhaseList.field_188745_e);
                    }
                }
            }

            return true;
        }
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (damagesource instanceof EntityDamageSource && ((EntityDamageSource) damagesource).func_180139_w()) {
            this.func_70965_a(this.field_70987_i, damagesource, f);
        }

        return false;
    }

    protected boolean func_82195_e(DamageSource damagesource, float f) {
        return super.func_70097_a(damagesource, f);
    }

    public void func_174812_G() {
        this.func_70106_y();
        if (this.field_184676_bI != null) {
            this.field_184676_bI.func_186099_b(this);
            this.field_184676_bI.func_186096_a(this);
        }

    }

    protected void func_70609_aI() {
        if (this.field_184676_bI != null) {
            this.field_184676_bI.func_186099_b(this);
        }

        ++this.field_70995_bG;
        if (this.field_70995_bG >= 180 && this.field_70995_bG <= 200) {
            float f = (this.field_70146_Z.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.field_70146_Z.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.field_70146_Z.nextFloat() - 0.5F) * 8.0F;

            this.field_70170_p.func_175688_a(EnumParticleTypes.EXPLOSION_HUGE, this.field_70165_t + (double) f, this.field_70163_u + 2.0D + (double) f1, this.field_70161_v + (double) f2, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        boolean flag = this.field_70170_p.func_82736_K().func_82766_b("doMobLoot");
        short short0 = 500;

        if (this.field_184676_bI != null && !this.field_184676_bI.func_186102_d()) {
            short0 = 12000;
        }

        if (!this.field_70170_p.field_72995_K) {
            if (this.field_70995_bG > 150 && this.field_70995_bG % 5 == 0 && flag) {
                this.func_184668_a(MathHelper.func_76141_d((float) short0 * 0.08F));
            }

            if (this.field_70995_bG == 1) {
                // CraftBukkit start - Use relative location for far away sounds
                // this.world.a(1028, new BlockPosition(this), 0);
                // Paper start
                //int viewDistance = ((WorldServer) this.world).spigotConfig.viewDistance * 16; // Paper - updated to use worlds actual view distance incase we have to uncomment this due to removal of player view distance API
                for (EntityPlayer human : field_70170_p.field_73010_i) {
                    EntityPlayerMP player = (EntityPlayerMP) human;
                    int viewDistance = player.getViewDistance();
                    // Paper end
                    double deltaX = this.field_70165_t - player.field_70165_t;
                    double deltaZ = this.field_70161_v - player.field_70161_v;
                    double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    if ( field_70170_p.spigotConfig.dragonDeathSoundRadius > 0 && distanceSquared > field_70170_p.spigotConfig.dragonDeathSoundRadius * field_70170_p.spigotConfig.dragonDeathSoundRadius ) continue; // Spigot
                    if (distanceSquared > viewDistance * viewDistance) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.field_70165_t + (deltaX / deltaLength) * viewDistance;
                        double relativeZ = player.field_70161_v + (deltaZ / deltaLength) * viewDistance;
                        player.field_71135_a.func_147359_a(new SPacketEffect(1028, new BlockPos((int) relativeX, (int) this.field_70163_u, (int) relativeZ), 0, true));
                    } else {
                        player.field_71135_a.func_147359_a(new SPacketEffect(1028, new BlockPos((int) this.field_70165_t, (int) this.field_70163_u, (int) this.field_70161_v), 0, true));
                    }
                }
                // CraftBukkit end
            }
        }

        this.func_70091_d(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
        this.field_70177_z += 20.0F;
        this.field_70761_aq = this.field_70177_z;
        if (this.field_70995_bG == 200 && !this.field_70170_p.field_72995_K) {
            if (flag) {
                this.func_184668_a(MathHelper.func_76141_d((float) short0 * 0.2F));
            }

            if (this.field_184676_bI != null) {
                this.field_184676_bI.func_186096_a(this);
            }

            this.func_70106_y();
        }

    }

    private void func_184668_a(int i) {
        while (i > 0) {
            int j = EntityXPOrb.func_70527_a(i);

            i -= j;
            this.field_70170_p.func_72838_d(new EntityXPOrb(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, j, org.bukkit.entity.ExperienceOrb.SpawnReason.ENTITY_DEATH, this.field_70717_bb, this)); // Paper
        }

    }

    public int func_184671_o() {
        if (this.field_184680_bM[0] == null) {
            for (int i = 0; i < 24; ++i) {
                int j = 5;
                int k;
                int l;

                if (i < 12) {
                    k = (int) (60.0F * MathHelper.func_76134_b(2.0F * (-3.1415927F + 0.2617994F * (float) i)));
                    l = (int) (60.0F * MathHelper.func_76126_a(2.0F * (-3.1415927F + 0.2617994F * (float) i)));
                } else {
                    int i1;

                    if (i < 20) {
                        i1 = i - 12;
                        k = (int) (40.0F * MathHelper.func_76134_b(2.0F * (-3.1415927F + 0.3926991F * (float) i1)));
                        l = (int) (40.0F * MathHelper.func_76126_a(2.0F * (-3.1415927F + 0.3926991F * (float) i1)));
                        j += 10;
                    } else {
                        i1 = i - 20;
                        k = (int) (20.0F * MathHelper.func_76134_b(2.0F * (-3.1415927F + 0.7853982F * (float) i1)));
                        l = (int) (20.0F * MathHelper.func_76126_a(2.0F * (-3.1415927F + 0.7853982F * (float) i1)));
                    }
                }

                int j1 = Math.max(this.field_70170_p.func_181545_F() + 10, this.field_70170_p.func_175672_r(new BlockPos(k, 0, l)).func_177956_o() + j);

                this.field_184680_bM[i] = new PathPoint(k, j1, l);
            }

            this.field_184681_bN[0] = 6146;
            this.field_184681_bN[1] = 8197;
            this.field_184681_bN[2] = 8202;
            this.field_184681_bN[3] = 16404;
            this.field_184681_bN[4] = '\u8028';
            this.field_184681_bN[5] = '\u8050';
            this.field_184681_bN[6] = 65696;
            this.field_184681_bN[7] = 131392;
            this.field_184681_bN[8] = 131712;
            this.field_184681_bN[9] = 263424;
            this.field_184681_bN[10] = 526848;
            this.field_184681_bN[11] = 525313;
            this.field_184681_bN[12] = 1581057;
            this.field_184681_bN[13] = 3166214;
            this.field_184681_bN[14] = 2138120;
            this.field_184681_bN[15] = 6373424;
            this.field_184681_bN[16] = 4358208;
            this.field_184681_bN[17] = 12910976;
            this.field_184681_bN[18] = 9044480;
            this.field_184681_bN[19] = 9706496;
            this.field_184681_bN[20] = 15216640;
            this.field_184681_bN[21] = 13688832;
            this.field_184681_bN[22] = 11763712;
            this.field_184681_bN[23] = 8257536;
        }

        return this.func_184663_l(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public int func_184663_l(double d0, double d1, double d2) {
        float f = 10000.0F;
        int i = 0;
        PathPoint pathpoint = new PathPoint(MathHelper.func_76128_c(d0), MathHelper.func_76128_c(d1), MathHelper.func_76128_c(d2));
        byte b0 = 0;

        if (this.field_184676_bI == null || this.field_184676_bI.func_186092_c() == 0) {
            b0 = 12;
        }

        for (int j = b0; j < 24; ++j) {
            if (this.field_184680_bM[j] != null) {
                float f1 = this.field_184680_bM[j].func_75832_b(pathpoint);

                if (f1 < f) {
                    f = f1;
                    i = j;
                }
            }
        }

        return i;
    }

    @Nullable
    public Path func_184666_a(int i, int j, @Nullable PathPoint pathpoint) {
        PathPoint pathpoint1;

        for (int k = 0; k < 24; ++k) {
            pathpoint1 = this.field_184680_bM[k];
            pathpoint1.field_75842_i = false;
            pathpoint1.field_75834_g = 0.0F;
            pathpoint1.field_75836_e = 0.0F;
            pathpoint1.field_75833_f = 0.0F;
            pathpoint1.field_75841_h = null;
            pathpoint1.field_75835_d = -1;
        }

        PathPoint pathpoint2 = this.field_184680_bM[i];

        pathpoint1 = this.field_184680_bM[j];
        pathpoint2.field_75836_e = 0.0F;
        pathpoint2.field_75833_f = pathpoint2.func_75829_a(pathpoint1);
        pathpoint2.field_75834_g = pathpoint2.field_75833_f;
        this.field_184682_bO.func_75848_a();
        this.field_184682_bO.func_75849_a(pathpoint2);
        PathPoint pathpoint3 = pathpoint2;
        byte b0 = 0;

        if (this.field_184676_bI == null || this.field_184676_bI.func_186092_c() == 0) {
            b0 = 12;
        }

        label70:
        while (!this.field_184682_bO.func_75845_e()) {
            PathPoint pathpoint4 = this.field_184682_bO.func_75844_c();

            if (pathpoint4.equals(pathpoint1)) {
                if (pathpoint != null) {
                    pathpoint.field_75841_h = pathpoint1;
                    pathpoint1 = pathpoint;
                }

                return this.func_184669_a(pathpoint2, pathpoint1);
            }

            if (pathpoint4.func_75829_a(pathpoint1) < pathpoint3.func_75829_a(pathpoint1)) {
                pathpoint3 = pathpoint4;
            }

            pathpoint4.field_75842_i = true;
            int l = 0;
            int i1 = 0;

            while (true) {
                if (i1 < 24) {
                    if (this.field_184680_bM[i1] != pathpoint4) {
                        ++i1;
                        continue;
                    }

                    l = i1;
                }

                i1 = b0;

                while (true) {
                    if (i1 >= 24) {
                        continue label70;
                    }

                    if ((this.field_184681_bN[l] & 1 << i1) > 0) {
                        PathPoint pathpoint5 = this.field_184680_bM[i1];

                        if (!pathpoint5.field_75842_i) {
                            float f = pathpoint4.field_75836_e + pathpoint4.func_75829_a(pathpoint5);

                            if (!pathpoint5.func_75831_a() || f < pathpoint5.field_75836_e) {
                                pathpoint5.field_75841_h = pathpoint4;
                                pathpoint5.field_75836_e = f;
                                pathpoint5.field_75833_f = pathpoint5.func_75829_a(pathpoint1);
                                if (pathpoint5.func_75831_a()) {
                                    this.field_184682_bO.func_75850_a(pathpoint5, pathpoint5.field_75836_e + pathpoint5.field_75833_f);
                                } else {
                                    pathpoint5.field_75834_g = pathpoint5.field_75836_e + pathpoint5.field_75833_f;
                                    this.field_184682_bO.func_75849_a(pathpoint5);
                                }
                            }
                        }
                    }

                    ++i1;
                }
            }
        }

        if (pathpoint3 == pathpoint2) {
            return null;
        } else {
            EntityDragon.field_184675_bH.debug("Failed to find path from {} to {}", Integer.valueOf(i), Integer.valueOf(j));
            if (pathpoint != null) {
                pathpoint.field_75841_h = pathpoint3;
                pathpoint3 = pathpoint;
            }

            return this.func_184669_a(pathpoint2, pathpoint3);
        }
    }

    private Path func_184669_a(PathPoint pathpoint, PathPoint pathpoint1) {
        int i = 1;

        PathPoint pathpoint2;

        for (pathpoint2 = pathpoint1; pathpoint2.field_75841_h != null; pathpoint2 = pathpoint2.field_75841_h) {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];

        pathpoint2 = pathpoint1;
        --i;

        for (apathpoint[i] = pathpoint1; pathpoint2.field_75841_h != null; apathpoint[i] = pathpoint2) {
            pathpoint2 = pathpoint2.field_75841_h;
            --i;
        }

        return new Path(apathpoint);
    }

    public static void func_189755_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityDragon.class);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("DragonPhase", this.field_184677_bJ.func_188756_a().func_188652_i().func_188740_b());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (nbttagcompound.func_74764_b("DragonPhase")) {
            this.field_184677_bJ.func_188758_a(PhaseList.func_188738_a(nbttagcompound.func_74762_e("DragonPhase")));
        }

    }

    protected void func_70623_bb() {}

    public Entity[] func_70021_al() {
        return this.field_70977_g;
    }

    public boolean func_70067_L() {
        return false;
    }

    public World func_82194_d() {
        return this.field_70170_p;
    }

    public SoundCategory func_184176_by() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187521_aK;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187526_aP;
    }

    protected float func_70599_aP() {
        return 5.0F;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_191189_ay;
    }

    public Vec3d func_184665_a(float f) {
        IPhase idragoncontroller = this.field_184677_bJ.func_188756_a();
        PhaseList dragoncontrollerphase = idragoncontroller.func_188652_i();
        Vec3d vec3d;
        float f1;

        if (dragoncontrollerphase != PhaseList.field_188744_d && dragoncontrollerphase != PhaseList.field_188745_e) {
            if (idragoncontroller.func_188654_a()) {
                float f2 = this.field_70125_A;

                f1 = 1.5F;
                this.field_70125_A = -45.0F;
                vec3d = this.func_70676_i(f);
                this.field_70125_A = f2;
            } else {
                vec3d = this.func_70676_i(f);
            }
        } else {
            BlockPos blockposition = this.field_70170_p.func_175672_r(WorldGenEndPodium.field_186139_a);

            f1 = Math.max(MathHelper.func_76133_a(this.func_174831_c(blockposition)) / 4.0F, 1.0F);
            float f3 = 6.0F / f1;
            float f4 = this.field_70125_A;
            float f5 = 1.5F;

            this.field_70125_A = -f3 * 1.5F * 5.0F;
            vec3d = this.func_70676_i(f);
            this.field_70125_A = f4;
        }

        return vec3d;
    }

    public void func_184672_a(EntityEnderCrystal entityendercrystal, BlockPos blockposition, DamageSource damagesource) {
        EntityPlayer entityhuman;

        if (damagesource.func_76346_g() instanceof EntityPlayer) {
            entityhuman = (EntityPlayer) damagesource.func_76346_g();
        } else {
            entityhuman = this.field_70170_p.func_184139_a(blockposition, 64.0D, 64.0D);
        }

        if (entityendercrystal == this.field_70992_bH) {
            this.func_70965_a(this.field_70986_h, DamageSource.func_188405_b(entityhuman), 10.0F);
        }

        this.field_184677_bJ.func_188756_a().func_188655_a(entityendercrystal, blockposition, damagesource, entityhuman);
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityDragon.field_184674_a.equals(datawatcherobject) && this.field_70170_p.field_72995_K) {
            this.field_184677_bJ.func_188758_a(PhaseList.func_188738_a(((Integer) this.func_184212_Q().func_187225_a(EntityDragon.field_184674_a)).intValue()));
        }

        super.func_184206_a(datawatcherobject);
    }

    public PhaseManager func_184670_cT() {
        return this.field_184677_bJ;
    }

    @Nullable
    public DragonFightManager func_184664_cU() {
        return this.field_184676_bI;
    }

    public void func_70690_d(PotionEffect mobeffect) {}

    protected boolean func_184228_n(Entity entity) {
        return false;
    }

    public boolean func_184222_aU() {
        return false;
    }
}

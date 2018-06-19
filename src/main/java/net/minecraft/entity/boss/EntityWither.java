package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.EntityWither.a;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityWither extends EntityMob implements IRangedAttackMob {

    private static final DataParameter<Integer> field_184741_a = EntityDataManager.func_187226_a(EntityWither.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_184742_b = EntityDataManager.func_187226_a(EntityWither.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_184743_c = EntityDataManager.func_187226_a(EntityWither.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer>[] field_184745_bv = new DataParameter[] { EntityWither.field_184741_a, EntityWither.field_184742_b, EntityWither.field_184743_c};
    private static final DataParameter<Integer> field_184746_bw = EntityDataManager.func_187226_a(EntityWither.class, DataSerializers.field_187192_b);
    private final float[] field_82220_d = new float[2];
    private final float[] field_82221_e = new float[2];
    private final float[] field_82217_f = new float[2];
    private final float[] field_82218_g = new float[2];
    private final int[] field_82223_h = new int[2];
    private final int[] field_82224_i = new int[2];
    private int field_82222_j;
    private final BossInfoServer field_184744_bE;
    private static final Predicate<Entity> field_82219_bJ = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof EntityLivingBase && ((EntityLivingBase) entity).func_70668_bt() != EnumCreatureAttribute.UNDEAD && ((EntityLivingBase) entity).func_190631_cK();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };

    public EntityWither(World world) {
        super(world);
        this.field_184744_bE = (BossInfoServer) (new BossInfoServer(this.func_145748_c_(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).func_186741_a(true);
        this.func_70606_j(this.func_110138_aP());
        this.func_70105_a(0.9F, 3.5F);
        this.field_70178_ae = true;
        ((PathNavigateGround) this.func_70661_as()).func_179693_d(true);
        this.field_70728_aV = 50;
    }

    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(0, new EntityWither.a());
        this.field_70714_bg.func_75776_a(1, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(2, new EntityAIAttackRanged(this, 1.0D, 40, 20.0F));
        this.field_70714_bg.func_75776_a(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.field_70714_bg.func_75776_a(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(7, new EntityAILookIdle(this));
        this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, EntityWither.field_82219_bJ));
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityWither.field_184741_a, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityWither.field_184742_b, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityWither.field_184743_c, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityWither.field_184746_bw, Integer.valueOf(0));
    }

    public static void func_189782_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityWither.class);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("Invul", this.func_82212_n());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_82215_s(nbttagcompound.func_74762_e("Invul"));
        if (this.func_145818_k_()) {
            this.field_184744_bE.func_186739_a(this.func_145748_c_());
        }

    }

    public void func_96094_a(String s) {
        super.func_96094_a(s);
        this.field_184744_bE.func_186739_a(this.func_145748_c_());
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187925_gy;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187851_gB;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187849_gA;
    }

    public void func_70636_d() {
        this.field_70181_x *= 0.6000000238418579D;
        double d0;
        double d1;
        double d2;

        if (!this.field_70170_p.field_72995_K && this.func_82203_t(0) > 0) {
            Entity entity = this.field_70170_p.func_73045_a(this.func_82203_t(0));

            if (entity != null) {
                if (this.field_70163_u < entity.field_70163_u || !this.func_82205_o() && this.field_70163_u < entity.field_70163_u + 5.0D) {
                    if (this.field_70181_x < 0.0D) {
                        this.field_70181_x = 0.0D;
                    }

                    this.field_70181_x += (0.5D - this.field_70181_x) * 0.6000000238418579D;
                }

                double d3 = entity.field_70165_t - this.field_70165_t;

                d0 = entity.field_70161_v - this.field_70161_v;
                d1 = d3 * d3 + d0 * d0;
                if (d1 > 9.0D) {
                    d2 = (double) MathHelper.func_76133_a(d1);
                    this.field_70159_w += (d3 / d2 * 0.5D - this.field_70159_w) * 0.6000000238418579D;
                    this.field_70179_y += (d0 / d2 * 0.5D - this.field_70179_y) * 0.6000000238418579D;
                }
            }
        }

        if (this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 0.05000000074505806D) {
            this.field_70177_z = (float) MathHelper.func_181159_b(this.field_70179_y, this.field_70159_w) * 57.295776F - 90.0F;
        }

        super.func_70636_d();

        int i;

        for (i = 0; i < 2; ++i) {
            this.field_82218_g[i] = this.field_82221_e[i];
            this.field_82217_f[i] = this.field_82220_d[i];
        }

        int j;

        for (i = 0; i < 2; ++i) {
            j = this.func_82203_t(i + 1);
            Entity entity1 = null;

            if (j > 0) {
                entity1 = this.field_70170_p.func_73045_a(j);
            }

            if (entity1 != null) {
                d0 = this.func_82214_u(i + 1);
                d1 = this.func_82208_v(i + 1);
                d2 = this.func_82213_w(i + 1);
                double d4 = entity1.field_70165_t - d0;
                double d5 = entity1.field_70163_u + (double) entity1.func_70047_e() - d1;
                double d6 = entity1.field_70161_v - d2;
                double d7 = (double) MathHelper.func_76133_a(d4 * d4 + d6 * d6);
                float f = (float) (MathHelper.func_181159_b(d6, d4) * 57.2957763671875D) - 90.0F;
                float f1 = (float) (-(MathHelper.func_181159_b(d5, d7) * 57.2957763671875D));

                this.field_82220_d[i] = this.func_82204_b(this.field_82220_d[i], f1, 40.0F);
                this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], f, 10.0F);
            } else {
                this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], this.field_70761_aq, 10.0F);
            }
        }

        boolean flag = this.func_82205_o();

        for (j = 0; j < 3; ++j) {
            double d8 = this.func_82214_u(j);
            double d9 = this.func_82208_v(j);
            double d10 = this.func_82213_w(j);

            this.field_70170_p.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d8 + this.field_70146_Z.nextGaussian() * 0.30000001192092896D, d9 + this.field_70146_Z.nextGaussian() * 0.30000001192092896D, d10 + this.field_70146_Z.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
            if (flag && this.field_70170_p.field_73012_v.nextInt(4) == 0) {
                this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, d8 + this.field_70146_Z.nextGaussian() * 0.30000001192092896D, d9 + this.field_70146_Z.nextGaussian() * 0.30000001192092896D, d10 + this.field_70146_Z.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
            }
        }

        if (this.func_82212_n() > 0) {
            for (j = 0; j < 3; ++j) {
                this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t + this.field_70146_Z.nextGaussian(), this.field_70163_u + (double) (this.field_70146_Z.nextFloat() * 3.3F), this.field_70161_v + this.field_70146_Z.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
            }
        }

    }

    protected void func_70619_bc() {
        int i;

        if (this.func_82212_n() > 0) {
            i = this.func_82212_n() - 1;
            if (i <= 0) {
                // CraftBukkit start
                // this.world.createExplosion(this, this.locX, this.locY + (double) this.getHeadHeight(), this.locZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 7.0F, false);
                this.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.field_70170_p.func_72885_a(this, this.field_70165_t, this.field_70163_u + (double) this.func_70047_e(), this.field_70161_v, event.getRadius(), event.getFire(), this.field_70170_p.func_82736_K().func_82766_b("mobGriefing"));
                }
                // CraftBukkit end

                // CraftBukkit start - Use relative location for far away sounds
                // this.world.a(1023, new BlockPosition(this), 0);
                // Paper start
                //int viewDistance = ((WorldServer) this.world).spigotConfig.viewDistance * 16; // Paper - updated to use worlds actual view distance incase we have to uncomment this due to removal of player view distance API
                for (EntityPlayer human : field_70170_p.field_73010_i) {
                    EntityPlayerMP player = (EntityPlayerMP) human;
                    int viewDistance = player.getViewDistance();
                    // Paper end
                    double deltaX = this.field_70165_t - player.field_70165_t;
                    double deltaZ = this.field_70161_v - player.field_70161_v;
                    double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    if ( field_70170_p.spigotConfig.witherSpawnSoundRadius > 0 && distanceSquared > field_70170_p.spigotConfig.witherSpawnSoundRadius * field_70170_p.spigotConfig.witherSpawnSoundRadius ) continue; // Spigot
                    if (distanceSquared > viewDistance * viewDistance) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.field_70165_t + (deltaX / deltaLength) * viewDistance;
                        double relativeZ = player.field_70161_v + (deltaZ / deltaLength) * viewDistance;
                        player.field_71135_a.func_147359_a(new SPacketEffect(1023, new BlockPos((int) relativeX, (int) this.field_70163_u, (int) relativeZ), 0, true));
                    } else {
                        player.field_71135_a.func_147359_a(new SPacketEffect(1023, new BlockPos((int) this.field_70165_t, (int) this.field_70163_u, (int) this.field_70161_v), 0, true));
                    }
                }
                // CraftBukkit end
            }

            this.func_82215_s(i);
            if (this.field_70173_aa % 10 == 0) {
                this.heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN); // CraftBukkit
            }

        } else {
            super.func_70619_bc();

            int j;

            for (i = 1; i < 3; ++i) {
                if (this.field_70173_aa >= this.field_82223_h[i - 1]) {
                    this.field_82223_h[i - 1] = this.field_70173_aa + 10 + this.field_70146_Z.nextInt(10);
                    if (this.field_70170_p.func_175659_aa() == EnumDifficulty.NORMAL || this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) {
                        int k = i - 1;
                        int l = this.field_82224_i[i - 1];

                        this.field_82224_i[k] = this.field_82224_i[i - 1] + 1;
                        if (l > 15) {
                            float f = 10.0F;
                            float f1 = 5.0F;
                            double d0 = MathHelper.func_82716_a(this.field_70146_Z, this.field_70165_t - 10.0D, this.field_70165_t + 10.0D);
                            double d1 = MathHelper.func_82716_a(this.field_70146_Z, this.field_70163_u - 5.0D, this.field_70163_u + 5.0D);
                            double d2 = MathHelper.func_82716_a(this.field_70146_Z, this.field_70161_v - 10.0D, this.field_70161_v + 10.0D);

                            this.func_82209_a(i + 1, d0, d1, d2, true);
                            this.field_82224_i[i - 1] = 0;
                        }
                    }

                    j = this.func_82203_t(i);
                    if (j > 0) {
                        Entity entity = this.field_70170_p.func_73045_a(j);

                        if (entity != null && entity.func_70089_S() && this.func_70068_e(entity) <= 900.0D && this.func_70685_l(entity)) {
                            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).field_71075_bZ.field_75102_a) {
                                this.func_82211_c(i, 0);
                            } else {
                                this.func_82216_a(i + 1, (EntityLivingBase) entity);
                                this.field_82223_h[i - 1] = this.field_70173_aa + 40 + this.field_70146_Z.nextInt(20);
                                this.field_82224_i[i - 1] = 0;
                            }
                        } else {
                            this.func_82211_c(i, 0);
                        }
                    } else {
                        List list = this.field_70170_p.func_175647_a(EntityLivingBase.class, this.func_174813_aQ().func_72314_b(20.0D, 8.0D, 20.0D), Predicates.and(EntityWither.field_82219_bJ, EntitySelectors.field_180132_d));

                        for (int i1 = 0; i1 < 10 && !list.isEmpty(); ++i1) {
                            EntityLivingBase entityliving = (EntityLivingBase) list.get(this.field_70146_Z.nextInt(list.size()));

                            if (entityliving != this && entityliving.func_70089_S() && this.func_70685_l(entityliving)) {
                                if (entityliving instanceof EntityPlayer) {
                                    if (!((EntityPlayer) entityliving).field_71075_bZ.field_75102_a) {
                                        this.func_82211_c(i, entityliving.func_145782_y());
                                    }
                                } else {
                                    this.func_82211_c(i, entityliving.func_145782_y());
                                }
                                break;
                            }

                            list.remove(entityliving);
                        }
                    }
                }
            }

            if (this.func_70638_az() != null) {
                this.func_82211_c(0, this.func_70638_az().func_145782_y());
            } else {
                this.func_82211_c(0, 0);
            }

            if (this.field_82222_j > 0) {
                --this.field_82222_j;
                if (this.field_82222_j == 0 && this.field_70170_p.func_82736_K().func_82766_b("mobGriefing")) {
                    i = MathHelper.func_76128_c(this.field_70163_u);
                    j = MathHelper.func_76128_c(this.field_70165_t);
                    int j1 = MathHelper.func_76128_c(this.field_70161_v);
                    boolean flag = false;

                    for (int k1 = -1; k1 <= 1; ++k1) {
                        for (int l1 = -1; l1 <= 1; ++l1) {
                            for (int i2 = 0; i2 <= 3; ++i2) {
                                int j2 = j + k1;
                                int k2 = i + i2;
                                int l2 = j1 + l1;
                                BlockPos blockposition = new BlockPos(j2, k2, l2);
                                IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);
                                Block block = iblockdata.func_177230_c();

                                if (iblockdata.func_185904_a() != Material.field_151579_a && func_181033_a(block)) {
                                    // CraftBukkit start
                                    if (CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, Blocks.field_150350_a, 0).isCancelled()) {
                                        continue;
                                    }
                                    // CraftBukkit end
                                    flag = this.field_70170_p.func_175655_b(blockposition, true) || flag;
                                }
                            }
                        }
                    }

                    if (flag) {
                        this.field_70170_p.func_180498_a((EntityPlayer) null, 1022, new BlockPos(this), 0);
                    }
                }
            }

            if (this.field_70173_aa % 20 == 0) {
                this.heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN); // CraftBukkit
            }

            this.field_184744_bE.func_186735_a(this.func_110143_aJ() / this.func_110138_aP());
        }
    }

    public static boolean func_181033_a(Block block) {
        return block != Blocks.field_150357_h && block != Blocks.field_150384_bq && block != Blocks.field_150378_br && block != Blocks.field_150483_bI && block != Blocks.field_185776_dc && block != Blocks.field_185777_dd && block != Blocks.field_180401_cv && block != Blocks.field_185779_df && block != Blocks.field_189881_dj && block != Blocks.field_180384_M && block != Blocks.field_185775_db;
    }

    public void func_82206_m() {
        this.func_82215_s(220);
        this.func_70606_j(this.func_110138_aP() / 3.0F);
    }

    public void func_70110_aj() {}

    public void func_184178_b(EntityPlayerMP entityplayer) {
        super.func_184178_b(entityplayer);
        this.field_184744_bE.func_186760_a(entityplayer);
    }

    public void func_184203_c(EntityPlayerMP entityplayer) {
        super.func_184203_c(entityplayer);
        this.field_184744_bE.func_186761_b(entityplayer);
    }

    private double func_82214_u(int i) {
        if (i <= 0) {
            return this.field_70165_t;
        } else {
            float f = (this.field_70761_aq + (float) (180 * (i - 1))) * 0.017453292F;
            float f1 = MathHelper.func_76134_b(f);

            return this.field_70165_t + (double) f1 * 1.3D;
        }
    }

    private double func_82208_v(int i) {
        return i <= 0 ? this.field_70163_u + 3.0D : this.field_70163_u + 2.2D;
    }

    private double func_82213_w(int i) {
        if (i <= 0) {
            return this.field_70161_v;
        } else {
            float f = (this.field_70761_aq + (float) (180 * (i - 1))) * 0.017453292F;
            float f1 = MathHelper.func_76126_a(f);

            return this.field_70161_v + (double) f1 * 1.3D;
        }
    }

    private float func_82204_b(float f, float f1, float f2) {
        float f3 = MathHelper.func_76142_g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    private void func_82216_a(int i, EntityLivingBase entityliving) {
        this.func_82209_a(i, entityliving.field_70165_t, entityliving.field_70163_u + (double) entityliving.func_70047_e() * 0.5D, entityliving.field_70161_v, i == 0 && this.field_70146_Z.nextFloat() < 0.001F);
    }

    private void func_82209_a(int i, double d0, double d1, double d2, boolean flag) {
        this.field_70170_p.func_180498_a((EntityPlayer) null, 1024, new BlockPos(this), 0);
        double d3 = this.func_82214_u(i);
        double d4 = this.func_82208_v(i);
        double d5 = this.func_82213_w(i);
        double d6 = d0 - d3;
        double d7 = d1 - d4;
        double d8 = d2 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.field_70170_p, this, d6, d7, d8);

        if (flag) {
            entitywitherskull.func_82343_e(true);
        }

        entitywitherskull.field_70163_u = d4;
        entitywitherskull.field_70165_t = d3;
        entitywitherskull.field_70161_v = d5;
        this.field_70170_p.func_72838_d(entitywitherskull);
    }

    public void func_82196_d(EntityLivingBase entityliving, float f) {
        this.func_82216_a(0, entityliving);
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else if (damagesource != DamageSource.field_76369_e && !(damagesource.func_76346_g() instanceof EntityWither)) {
            if (this.func_82212_n() > 0 && damagesource != DamageSource.field_76380_i) {
                return false;
            } else {
                Entity entity;

                if (this.func_82205_o()) {
                    entity = damagesource.func_76364_f();
                    if (entity instanceof EntityArrow) {
                        return false;
                    }
                }

                entity = damagesource.func_76346_g();
                if (entity != null && !(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).func_70668_bt() == this.func_70668_bt()) {
                    return false;
                } else {
                    if (this.field_82222_j <= 0) {
                        this.field_82222_j = 20;
                    }

                    for (int i = 0; i < this.field_82224_i.length; ++i) {
                        this.field_82224_i[i] += 3;
                    }

                    return super.func_70097_a(damagesource, f);
                }
            }
        } else {
            return false;
        }
    }

    protected void func_70628_a(boolean flag, int i) {
        EntityItem entityitem = this.func_145779_a(Items.field_151156_bN, 1);

        if (entityitem != null) {
            entityitem.func_174873_u();
        }

    }

    protected void func_70623_bb() {
        this.field_70708_bq = 0;
    }

    public void func_180430_e(float f, float f1) {}

    public void func_70690_d(PotionEffect mobeffect) {}

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(300.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.6000000238418579D);
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(40.0D);
        this.func_110148_a(SharedMonsterAttributes.field_188791_g).func_111128_a(4.0D);
    }

    public int func_82212_n() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityWither.field_184746_bw)).intValue();
    }

    public void func_82215_s(int i) {
        this.field_70180_af.func_187227_b(EntityWither.field_184746_bw, Integer.valueOf(i));
    }

    public int func_82203_t(int i) {
        return ((Integer) this.field_70180_af.func_187225_a(EntityWither.field_184745_bv[i])).intValue();
    }

    public void func_82211_c(int i, int j) {
        this.field_70180_af.func_187227_b(EntityWither.field_184745_bv[i], Integer.valueOf(j));
    }

    public boolean func_82205_o() {
        return this.func_110143_aJ() <= this.func_110138_aP() / 2.0F;
    }

    public EnumCreatureAttribute func_70668_bt() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected boolean func_184228_n(Entity entity) {
        return false;
    }

    public boolean func_184222_aU() {
        return false;
    }

    public void func_184724_a(boolean flag) {}

    class a extends EntityAIBase {

        public a() {
            this.func_75248_a(7);
        }

        public boolean func_75250_a() {
            return EntityWither.this.func_82212_n() > 0;
        }
    }
}

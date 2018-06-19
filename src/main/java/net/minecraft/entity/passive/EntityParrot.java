package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityParrot extends EntityShoulderRiding implements EntityFlying {

    private static final DataParameter<Integer> field_192013_bG = EntityDataManager.func_187226_a(EntityParrot.class, DataSerializers.field_187192_b);
    private static final Predicate<EntityLiving> field_192014_bH = new Predicate() {
        public boolean a(@Nullable EntityLiving entityinsentient) {
            return entityinsentient != null && EntityParrot.field_192017_bK.containsKey(EntityList.field_191308_b.func_148757_b(entityinsentient.getClass())); // CraftBukkit - decompile error
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EntityLiving) object);
        }
    };
    private static final Item field_192015_bI = Items.field_151106_aX;
    private static final Set<Item> field_192016_bJ = Sets.newHashSet(new Item[] { Items.field_151014_N, Items.field_151081_bc, Items.field_151080_bb, Items.field_185163_cU});
    private static final Int2ObjectMap<SoundEvent> field_192017_bK = new Int2ObjectOpenHashMap(32);
    public float field_192008_bB;
    public float field_192009_bC;
    public float field_192010_bD;
    public float field_192011_bE;
    public float field_192012_bF = 1.0F;
    private boolean field_192018_bL;
    private BlockPos field_192019_bM;

    public EntityParrot(World world) {
        super(world);
        this.func_70105_a(0.5F, 0.9F);
        this.field_70765_h = new EntityFlyHelper(this);
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.func_191997_m(this.field_70146_Z.nextInt(5));
        return super.func_180482_a(difficultydamagescaler, groupdataentity);
    }

    protected void func_184651_r() {
        this.field_70911_d = new EntityAISit(this);
        this.field_70714_bg.func_75776_a(0, new EntityAIPanic(this, 1.25D));
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(2, this.field_70911_d);
        this.field_70714_bg.func_75776_a(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
        this.field_70714_bg.func_75776_a(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
        this.field_70714_bg.func_75776_a(3, new EntityAILandOnOwnersShoulder(this));
        this.field_70714_bg.func_75776_a(3, new EntityAIFollow(this, 1.0D, 3.0F, 7.0F));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_193334_e);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(6.0D);
        this.func_110148_a(SharedMonsterAttributes.field_193334_e).func_111128_a(0.4000000059604645D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.20000000298023224D);
    }

    protected PathNavigate func_175447_b(World world) {
        PathNavigateFlying navigationflying = new PathNavigateFlying(this, world);

        navigationflying.func_192879_a(false);
        navigationflying.func_192877_c(true);
        navigationflying.func_192878_b(true);
        return navigationflying;
    }

    public float func_70047_e() {
        return this.field_70131_O * 0.6F;
    }

    public void func_70636_d() {
        func_192006_b(this.field_70170_p, (Entity) this);
        if (this.field_192019_bM == null || this.field_192019_bM.func_177954_c(this.field_70165_t, this.field_70163_u, this.field_70161_v) > 12.0D || this.field_70170_p.func_180495_p(this.field_192019_bM).func_177230_c() != Blocks.field_150421_aI) {
            this.field_192018_bL = false;
            this.field_192019_bM = null;
        }

        super.func_70636_d();
        this.func_192001_dv();
    }

    private void func_192001_dv() {
        this.field_192011_bE = this.field_192008_bB;
        this.field_192010_bD = this.field_192009_bC;
        this.field_192009_bC = (float) ((double) this.field_192009_bC + (double) (this.field_70122_E ? -1 : 4) * 0.3D);
        this.field_192009_bC = MathHelper.func_76131_a(this.field_192009_bC, 0.0F, 1.0F);
        if (!this.field_70122_E && this.field_192012_bF < 1.0F) {
            this.field_192012_bF = 1.0F;
        }

        this.field_192012_bF = (float) ((double) this.field_192012_bF * 0.9D);
        if (!this.field_70122_E && this.field_70181_x < 0.0D) {
            this.field_70181_x *= 0.6D;
        }

        this.field_192008_bB += this.field_192012_bF * 2.0F;
    }

    private static boolean func_192006_b(World world, Entity entity) {
        if (!entity.func_174814_R() && world.field_73012_v.nextInt(50) == 0) {
            List list = world.func_175647_a(EntityLiving.class, entity.func_174813_aQ().func_186662_g(20.0D), EntityParrot.field_192014_bH);

            if (!list.isEmpty()) {
                EntityLiving entityinsentient = (EntityLiving) list.get(world.field_73012_v.nextInt(list.size()));

                if (!entityinsentient.func_174814_R()) {
                    SoundEvent soundeffect = func_191999_g(EntityList.field_191308_b.func_148757_b(entityinsentient.getClass())); // CraftBukkit - decompile error

                    world.func_184148_a((EntityPlayer) null, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, soundeffect, entity.func_184176_by(), 0.7F, func_192000_b(world.field_73012_v));
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!this.func_70909_n() && EntityParrot.field_192016_bJ.contains(itemstack.func_77973_b())) {
            if (!entityhuman.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }

            if (!this.func_174814_R()) {
                this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_192797_eu, this.func_184176_by(), 1.0F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
            }

            if (!this.field_70170_p.field_72995_K) {
                if (this.field_70146_Z.nextInt(10) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) { // CraftBukkit
                    this.func_193101_c(entityhuman);
                    this.func_70908_e(true);
                    this.field_70170_p.func_72960_a(this, (byte) 7);
                } else {
                    this.func_70908_e(false);
                    this.field_70170_p.func_72960_a(this, (byte) 6);
                }
            }

            return true;
        } else if (itemstack.func_77973_b() == EntityParrot.field_192015_bI) {
            if (!entityhuman.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }

            this.func_70690_d(new PotionEffect(MobEffects.field_76436_u, 900));
            if (entityhuman.func_184812_l_() || !this.func_190530_aW()) {
                this.func_70097_a(DamageSource.func_76365_a(entityhuman), Float.MAX_VALUE);
            }

            return true;
        } else {
            if (!this.field_70170_p.field_72995_K && !this.func_192002_a() && this.func_70909_n() && this.func_152114_e((EntityLivingBase) entityhuman)) {
                this.field_70911_d.func_75270_a(!this.func_70906_o());
            }

            return super.func_184645_a(entityhuman, enumhand);
        }
    }

    public boolean func_70877_b(ItemStack itemstack) {
        return false;
    }

    public boolean func_70601_bi() {
        int i = MathHelper.func_76128_c(this.field_70165_t);
        int j = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b);
        int k = MathHelper.func_76128_c(this.field_70161_v);
        BlockPos blockposition = new BlockPos(i, j, k);
        Block block = this.field_70170_p.func_180495_p(blockposition.func_177977_b()).func_177230_c();

        return block instanceof BlockLeaves || block == Blocks.field_150349_c || block instanceof BlockLog || block == Blocks.field_150350_a && this.field_70170_p.func_175699_k(blockposition) > 8 && super.func_70601_bi();
    }

    public void func_180430_e(float f, float f1) {}

    protected void func_184231_a(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {}

    public boolean func_70878_b(EntityAnimal entityanimal) {
        return false;
    }

    @Nullable
    public EntityAgeable func_90011_a(EntityAgeable entityageable) {
        return null;
    }

    public static void func_192005_a(World world, Entity entity) {
        if (!entity.func_174814_R() && !func_192006_b(world, entity) && world.field_73012_v.nextInt(200) == 0) {
            world.func_184148_a((EntityPlayer) null, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, func_192003_a(world.field_73012_v), entity.func_184176_by(), 1.0F, func_192000_b(world.field_73012_v));
        }

    }

    public boolean func_70652_k(Entity entity) {
        return entity.func_70097_a(DamageSource.func_76358_a(this), 3.0F);
    }

    @Nullable
    public SoundEvent func_184639_G() {
        return func_192003_a(this.field_70146_Z);
    }

    private static SoundEvent func_192003_a(Random random) {
        if (random.nextInt(1000) == 0) {
            ArrayList arraylist = new ArrayList(EntityParrot.field_192017_bK.keySet());

            return func_191999_g(((Integer) arraylist.get(random.nextInt(arraylist.size()))).intValue());
        } else {
            return SoundEvents.field_192792_ep;
        }
    }

    public static SoundEvent func_191999_g(int i) {
        return EntityParrot.field_192017_bK.containsKey(i) ? (SoundEvent) EntityParrot.field_192017_bK.get(i) : SoundEvents.field_192792_ep;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_192794_er;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_192793_eq;
    }

    protected void func_180429_a(BlockPos blockposition, Block block) {
        this.func_184185_a(SoundEvents.field_192795_es, 0.15F, 1.0F);
    }

    protected float func_191954_d(float f) {
        this.func_184185_a(SoundEvents.field_192796_et, 0.15F, 1.0F);
        return f + this.field_192009_bC / 2.0F;
    }

    protected boolean func_191957_ae() {
        return true;
    }

    protected float func_70647_i() {
        return func_192000_b(this.field_70146_Z);
    }

    private static float func_192000_b(Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundCategory func_184176_by() {
        return SoundCategory.NEUTRAL;
    }

    public boolean func_70104_M() {
        return true;
    }

    protected void func_82167_n(Entity entity) {
        if (!(entity instanceof EntityPlayer)) {
            super.func_82167_n(entity);
        }
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            if (this.field_70911_d != null) {
                // CraftBukkit - moved into EntityLiving.d(DamageSource, float)
                // this.goalSit.setSitting(false);
            }

            return super.func_70097_a(damagesource, f);
        }
    }

    public int func_191998_ds() {
        return MathHelper.func_76125_a(((Integer) this.field_70180_af.func_187225_a(EntityParrot.field_192013_bG)).intValue(), 0, 4);
    }

    public void func_191997_m(int i) {
        this.field_70180_af.func_187227_b(EntityParrot.field_192013_bG, Integer.valueOf(i));
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityParrot.field_192013_bG, Integer.valueOf(0));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("Variant", this.func_191998_ds());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_191997_m(nbttagcompound.func_74762_e("Variant"));
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_192561_ax;
    }

    public boolean func_192002_a() {
        return !this.field_70122_E;
    }

    static {
        // CraftBukkit start
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityBlaze.class), SoundEvents.field_193791_eM);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityCaveSpider.class), SoundEvents.field_193813_fc);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityCreeper.class), SoundEvents.field_193792_eN);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityElderGuardian.class), SoundEvents.field_193793_eO);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityDragon.class), SoundEvents.field_193794_eP);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityEnderman.class), SoundEvents.field_193795_eQ);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityEndermite.class), SoundEvents.field_193796_eR);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityEvoker.class), SoundEvents.field_193797_eS);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityGhast.class), SoundEvents.field_193798_eT);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityHusk.class), SoundEvents.field_193799_eU);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityIllusionIllager.class), SoundEvents.field_193800_eV);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityMagmaCube.class), SoundEvents.field_193801_eW);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityPigZombie.class), SoundEvents.field_193822_fl);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityPolarBear.class), SoundEvents.field_193802_eX);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityShulker.class), SoundEvents.field_193803_eY);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntitySilverfish.class), SoundEvents.field_193804_eZ);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntitySkeleton.class), SoundEvents.field_193811_fa);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntitySlime.class), SoundEvents.field_193812_fb);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntitySpider.class), SoundEvents.field_193813_fc);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityStray.class), SoundEvents.field_193814_fd);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityVex.class), SoundEvents.field_193815_fe);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityVindicator.class), SoundEvents.field_193816_ff);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityWitch.class), SoundEvents.field_193817_fg);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityWither.class), SoundEvents.field_193818_fh);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityWitherSkeleton.class), SoundEvents.field_193819_fi);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityWolf.class), SoundEvents.field_193820_fj);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityZombie.class), SoundEvents.field_193821_fk);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.func_148757_b(EntityZombieVillager.class), SoundEvents.field_193823_fm);
        // CraftBukkit end
    }
}

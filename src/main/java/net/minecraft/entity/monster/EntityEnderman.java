package net.minecraft.entity.monster;

import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEnderman extends EntityMob {

    private static final UUID field_110192_bp = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier field_110193_bq = (new AttributeModifier(EntityEnderman.field_110192_bp, "Attacking speed boost", 0.15000000596046448D, 0)).func_111168_a(false);
    private static final Set<Block> field_70827_d = Sets.newIdentityHashSet();
    private static final DataParameter<Optional<IBlockState>> field_184718_bv = EntityDataManager.func_187226_a(EntityEnderman.class, DataSerializers.field_187197_g);
    private static final DataParameter<Boolean> field_184719_bw = EntityDataManager.func_187226_a(EntityEnderman.class, DataSerializers.field_187198_h);
    private int field_184720_bx;
    private int field_184721_by;

    public EntityEnderman(World world) {
        super(world);
        this.func_70105_a(0.6F, 2.9F);
        this.field_70138_W = 1.0F;
        this.func_184644_a(PathNodeType.WATER, -1.0F);
    }

    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.field_70714_bg.func_75776_a(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
        this.field_70714_bg.func_75776_a(10, new EntityEnderman.AIPlaceBlock(this));
        this.field_70714_bg.func_75776_a(11, new EntityEnderman.AITakeBlock(this));
        this.field_70715_bh.func_75776_a(1, new EntityEnderman.AIFindPlayer(this));
        this.field_70715_bh.func_75776_a(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate() {
            public boolean a(@Nullable EntityEndermite entityendermite) {
                return entityendermite.func_175495_n();
            }

            public boolean apply(@Nullable Object object) {
                return this.a((EntityEndermite) object);
            }
        }));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(40.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.30000001192092896D);
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(7.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(64.0D);
    }

    public void func_70624_b(@Nullable EntityLivingBase entityliving) {
        // CraftBukkit start - fire event
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    // Paper start
    private boolean tryEscape(EndermanEscapeEvent.Reason reason) {
        return new EndermanEscapeEvent((org.bukkit.craftbukkit.entity.CraftEnderman) this.getBukkitEntity(), reason).callEvent();
    }
    // Paper end

    @Override
    public boolean setGoalTarget(EntityLivingBase entityliving, org.bukkit.event.entity.EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (!super.setGoalTarget(entityliving, reason, fireEvent)) {
            return false;
        }
        entityliving = func_70638_az();
        // CraftBukkit end
        IAttributeInstance attributeinstance = this.func_110148_a(SharedMonsterAttributes.field_111263_d);

        if (entityliving == null) {
            this.field_184721_by = 0;
            this.field_70180_af.func_187227_b(EntityEnderman.field_184719_bw, Boolean.valueOf(false));
            attributeinstance.func_111124_b(EntityEnderman.field_110193_bq);
        } else {
            this.field_184721_by = this.field_70173_aa;
            this.field_70180_af.func_187227_b(EntityEnderman.field_184719_bw, Boolean.valueOf(true));
            if (!attributeinstance.func_180374_a(EntityEnderman.field_110193_bq)) {
                attributeinstance.func_111121_a(EntityEnderman.field_110193_bq);
            }
        }
        return true;

    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityEnderman.field_184718_bv, Optional.absent());
        this.field_70180_af.func_187214_a(EntityEnderman.field_184719_bw, Boolean.valueOf(false));
    }

    public void func_184716_o() {
        if (this.field_70173_aa >= this.field_184720_bx + 400) {
            this.field_184720_bx = this.field_70173_aa;
            if (!this.func_174814_R()) {
                this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u + (double) this.func_70047_e(), this.field_70161_v, SoundEvents.field_187533_aW, this.func_184176_by(), 2.5F, 1.0F, false);
            }
        }

    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityEnderman.field_184719_bw.equals(datawatcherobject) && this.func_70823_r() && this.field_70170_p.field_72995_K) {
            this.func_184716_o();
        }

        super.func_184206_a(datawatcherobject);
    }

    public static void func_189763_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityEnderman.class);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        IBlockState iblockdata = this.func_175489_ck();

        if (iblockdata != null) {
            nbttagcompound.func_74777_a("carried", (short) Block.func_149682_b(iblockdata.func_177230_c()));
            nbttagcompound.func_74777_a("carriedData", (short) iblockdata.func_177230_c().func_176201_c(iblockdata));
        }

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        IBlockState iblockdata;

        if (nbttagcompound.func_150297_b("carried", 8)) {
            iblockdata = Block.func_149684_b(nbttagcompound.func_74779_i("carried")).func_176203_a(nbttagcompound.func_74765_d("carriedData") & '\uffff');
        } else {
            iblockdata = Block.func_149729_e(nbttagcompound.func_74765_d("carried")).func_176203_a(nbttagcompound.func_74765_d("carriedData") & '\uffff');
        }

        if (iblockdata == null || iblockdata.func_177230_c() == null || iblockdata.func_185904_a() == Material.field_151579_a) {
            iblockdata = null;
        }

        this.func_175490_a(iblockdata);
    }

    // Paper start - OBFHELPER - ok not really, but verify this on updates
    private boolean func_70821_d(EntityPlayer entityhuman) {
        boolean shouldAttack = f_real(entityhuman);
        com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent event = new com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent((org.bukkit.entity.Enderman) getBukkitEntity(), (org.bukkit.entity.Player) entityhuman.getBukkitEntity());
        event.setCancelled(!shouldAttack);
        return event.callEvent();
    }
    private boolean f_real(EntityPlayer entityhuman) {
        // Paper end
        ItemStack itemstack = (ItemStack) entityhuman.field_71071_by.field_70460_b.get(3);

        if (itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_150423_aK)) {
            return false;
        } else {
            Vec3d vec3d = entityhuman.func_70676_i(1.0F).func_72432_b();
            Vec3d vec3d1 = new Vec3d(this.field_70165_t - entityhuman.field_70165_t, this.func_174813_aQ().field_72338_b + (double) this.func_70047_e() - (entityhuman.field_70163_u + (double) entityhuman.func_70047_e()), this.field_70161_v - entityhuman.field_70161_v);
            double d0 = vec3d1.func_72433_c();

            vec3d1 = vec3d1.func_72432_b();
            double d1 = vec3d.func_72430_b(vec3d1);

            return d1 > 1.0D - 0.025D / d0 ? entityhuman.func_70685_l(this) : false;
        }
    }

    public float func_70047_e() {
        return 2.55F;
    }

    public void func_70636_d() {
        if (this.field_70170_p.field_72995_K) {
            for (int i = 0; i < 2; ++i) {
                this.field_70170_p.func_175688_a(EnumParticleTypes.PORTAL, this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, this.field_70163_u + this.field_70146_Z.nextDouble() * (double) this.field_70131_O - 0.25D, this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, (this.field_70146_Z.nextDouble() - 0.5D) * 2.0D, -this.field_70146_Z.nextDouble(), (this.field_70146_Z.nextDouble() - 0.5D) * 2.0D, new int[0]);
            }
        }

        this.field_70703_bu = false;
        super.func_70636_d();
    }

    protected void func_70619_bc() {
        if (this.func_70026_G()) {
            this.func_70097_a(DamageSource.field_76369_e, 1.0F);
        }

        if (this.field_70170_p.func_72935_r() && this.field_70173_aa >= this.field_184721_by + 600) {
            float f = this.func_70013_c();

            if (f > 0.5F && this.field_70170_p.func_175678_i(new BlockPos(this)) && this.field_70146_Z.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && tryEscape(EndermanEscapeEvent.Reason.RUNAWAY)) { // Paper
                this.func_70624_b((EntityLivingBase) null);
                this.func_70820_n();
            }
        }

        super.func_70619_bc();
    }

    public boolean teleportRandomly() { return func_70820_n(); } // Paper - OBFHELPER
    protected boolean func_70820_n() {
        double d0 = this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.field_70163_u + (double) (this.field_70146_Z.nextInt(64) - 32);
        double d2 = this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * 64.0D;

        return this.func_70825_j(d0, d1, d2);
    }

    protected boolean func_70816_c(Entity entity) {
        Vec3d vec3d = new Vec3d(this.field_70165_t - entity.field_70165_t, this.func_174813_aQ().field_72338_b + (double) (this.field_70131_O / 2.0F) - entity.field_70163_u + (double) entity.func_70047_e(), this.field_70161_v - entity.field_70161_v);

        vec3d = vec3d.func_72432_b();
        double d0 = 16.0D;
        double d1 = this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * 8.0D - vec3d.field_72450_a * 16.0D;
        double d2 = this.field_70163_u + (double) (this.field_70146_Z.nextInt(16) - 8) - vec3d.field_72448_b * 16.0D;
        double d3 = this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * 8.0D - vec3d.field_72449_c * 16.0D;

        return this.func_70825_j(d1, d2, d3);
    }

    private boolean func_70825_j(double d0, double d1, double d2) {
        boolean flag = this.func_184595_k(d0, d1, d2);

        if (flag) {
            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70169_q, this.field_70167_r, this.field_70166_s, SoundEvents.field_187534_aX, this.func_184176_by(), 1.0F, 1.0F);
            this.func_184185_a(SoundEvents.field_187534_aX, 1.0F, 1.0F);
        }

        return flag;
    }

    protected SoundEvent func_184639_G() {
        return this.func_70823_r() ? SoundEvents.field_187532_aV : SoundEvents.field_187529_aS;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187531_aU;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187530_aT;
    }

    protected void func_82160_b(boolean flag, int i) {
        super.func_82160_b(flag, i);
        IBlockState iblockdata = this.func_175489_ck();

        if (iblockdata != null) {
            Item item = Item.func_150898_a(iblockdata.func_177230_c());
            int j = item.func_77614_k() ? iblockdata.func_177230_c().func_176201_c(iblockdata) : 0;

            this.func_70099_a(new ItemStack(item, 1, j), 0.0F);
        }

    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186439_u;
    }

    public void func_175490_a(@Nullable IBlockState iblockdata) {
        this.field_70180_af.func_187227_b(EntityEnderman.field_184718_bv, Optional.fromNullable(iblockdata));
    }

    @Nullable
    public IBlockState func_175489_ck() {
        return (IBlockState) ((Optional) this.field_70180_af.func_187225_a(EntityEnderman.field_184718_bv)).orNull();
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else if (damagesource instanceof EntityDamageSourceIndirect && tryEscape(EndermanEscapeEvent.Reason.INDIRECT)) { // Paper
            for (int i = 0; i < 64; ++i) {
                if (this.func_70820_n()) {
                    return true;
                }
            }

            return false;
        } else {
            boolean flag = super.func_70097_a(damagesource, f);

            if (damagesource.func_76363_c() && this.field_70146_Z.nextInt(10) != 0 && tryEscape(damagesource == DamageSource.field_76369_e ? EndermanEscapeEvent.Reason.DROWN : EndermanEscapeEvent.Reason.CRITICAL_HIT)) { // Paper
                this.func_70820_n();
            }

            return flag;
        }
    }

    public boolean func_70823_r() {
        return ((Boolean) this.field_70180_af.func_187225_a(EntityEnderman.field_184719_bw)).booleanValue();
    }

    static {
        EntityEnderman.field_70827_d.add(Blocks.field_150349_c);
        EntityEnderman.field_70827_d.add(Blocks.field_150346_d);
        EntityEnderman.field_70827_d.add(Blocks.field_150354_m);
        EntityEnderman.field_70827_d.add(Blocks.field_150351_n);
        EntityEnderman.field_70827_d.add(Blocks.field_150327_N);
        EntityEnderman.field_70827_d.add(Blocks.field_150328_O);
        EntityEnderman.field_70827_d.add(Blocks.field_150338_P);
        EntityEnderman.field_70827_d.add(Blocks.field_150337_Q);
        EntityEnderman.field_70827_d.add(Blocks.field_150335_W);
        EntityEnderman.field_70827_d.add(Blocks.field_150434_aF);
        EntityEnderman.field_70827_d.add(Blocks.field_150435_aG);
        EntityEnderman.field_70827_d.add(Blocks.field_150423_aK);
        EntityEnderman.field_70827_d.add(Blocks.field_150440_ba);
        EntityEnderman.field_70827_d.add(Blocks.field_150391_bh);
        EntityEnderman.field_70827_d.add(Blocks.field_150424_aL);
    }

    static class AITakeBlock extends EntityAIBase {

        private final EntityEnderman field_179473_a;

        public AITakeBlock(EntityEnderman entityenderman) {
            this.field_179473_a = entityenderman;
        }

        public boolean func_75250_a() {
            return this.field_179473_a.func_175489_ck() != null ? false : (!this.field_179473_a.field_70170_p.func_82736_K().func_82766_b("mobGriefing") ? false : this.field_179473_a.func_70681_au().nextInt(20) == 0);
        }

        public void func_75246_d() {
            Random random = this.field_179473_a.func_70681_au();
            World world = this.field_179473_a.field_70170_p;
            int i = MathHelper.func_76128_c(this.field_179473_a.field_70165_t - 2.0D + random.nextDouble() * 4.0D);
            int j = MathHelper.func_76128_c(this.field_179473_a.field_70163_u + random.nextDouble() * 3.0D);
            int k = MathHelper.func_76128_c(this.field_179473_a.field_70161_v - 2.0D + random.nextDouble() * 4.0D);
            BlockPos blockposition = new BlockPos(i, j, k);
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();
            RayTraceResult movingobjectposition = world.func_147447_a(new Vec3d((double) ((float) MathHelper.func_76128_c(this.field_179473_a.field_70165_t) + 0.5F), (double) ((float) j + 0.5F), (double) ((float) MathHelper.func_76128_c(this.field_179473_a.field_70161_v) + 0.5F)), new Vec3d((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F)), false, true, false);
            boolean flag = movingobjectposition != null && movingobjectposition.func_178782_a().equals(blockposition);

            if (EntityEnderman.field_70827_d.contains(block) && flag) {
                // CraftBukkit start - Pickup event
                if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.field_179473_a, this.field_179473_a.field_70170_p.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), org.bukkit.Material.AIR).isCancelled()) {
                    this.field_179473_a.func_175490_a(iblockdata);
                    world.func_175698_g(blockposition);
                }
                // CraftBukkit end
            }

        }
    }

    static class AIPlaceBlock extends EntityAIBase {

        private final EntityEnderman field_179475_a;

        public AIPlaceBlock(EntityEnderman entityenderman) {
            this.field_179475_a = entityenderman;
        }

        public boolean func_75250_a() {
            return this.field_179475_a.func_175489_ck() == null ? false : (!this.field_179475_a.field_70170_p.func_82736_K().func_82766_b("mobGriefing") ? false : this.field_179475_a.func_70681_au().nextInt(2000) == 0);
        }

        public void func_75246_d() {
            Random random = this.field_179475_a.func_70681_au();
            World world = this.field_179475_a.field_70170_p;
            int i = MathHelper.func_76128_c(this.field_179475_a.field_70165_t - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.func_76128_c(this.field_179475_a.field_70163_u + random.nextDouble() * 2.0D);
            int k = MathHelper.func_76128_c(this.field_179475_a.field_70161_v - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockposition = new BlockPos(i, j, k);
            IBlockState iblockdata = world.func_180495_p(blockposition);
            IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177977_b());
            IBlockState iblockdata2 = this.field_179475_a.func_175489_ck();

            if (iblockdata2 != null && this.func_188518_a(world, blockposition, iblockdata2.func_177230_c(), iblockdata, iblockdata1)) {
                // CraftBukkit start - Place event
                if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.field_179475_a, blockposition, this.field_179475_a.func_175489_ck().func_177230_c(), this.field_179475_a.func_175489_ck().func_177230_c().func_176201_c(this.field_179475_a.func_175489_ck())).isCancelled()) {
                world.func_180501_a(blockposition, iblockdata2, 3);
                this.field_179475_a.func_175490_a((IBlockState) null);
                }
                // CraftBukkit end
            }

        }

        private boolean func_188518_a(World world, BlockPos blockposition, Block block, IBlockState iblockdata, IBlockState iblockdata1) {
            return !block.func_176196_c(world, blockposition) ? false : (iblockdata.func_185904_a() != Material.field_151579_a ? false : (iblockdata1.func_185904_a() == Material.field_151579_a ? false : iblockdata1.func_185917_h()));
        }
    }

    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {

        private final EntityEnderman field_179449_j; public EntityEnderman getEnderman() { return field_179449_j; } // Paper - OBFHELPER
        private EntityPlayer field_179448_g;
        private int field_179450_h;
        private int field_179451_i;

        public AIFindPlayer(EntityEnderman entityenderman) {
            super(entityenderman, EntityPlayer.class, false);
            this.field_179449_j = entityenderman;
        }

        public boolean func_75250_a() {
            double d0 = this.func_111175_f();

            this.field_179448_g = this.field_179449_j.field_70170_p.func_184150_a(this.field_179449_j.field_70165_t, this.field_179449_j.field_70163_u, this.field_179449_j.field_70161_v, d0, d0, (Function) null, new Predicate() {
                public boolean a(@Nullable EntityPlayer entityhuman) {
                    return entityhuman != null && AIFindPlayer.this.field_179449_j.func_70821_d(entityhuman);
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((EntityPlayer) object);
                }
            });
            return this.field_179448_g != null;
        }

        public void func_75249_e() {
            this.field_179450_h = 5;
            this.field_179451_i = 0;
        }

        public void func_75251_c() {
            this.field_179448_g = null;
            super.func_75251_c();
        }

        public boolean func_75253_b() {
            if (this.field_179448_g != null) {
                if (!this.field_179449_j.func_70821_d(this.field_179448_g)) {
                    return false;
                } else {
                    this.field_179449_j.func_70625_a((Entity) this.field_179448_g, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.field_75309_a != null && ((EntityPlayer) this.field_75309_a).func_70089_S() ? true : super.func_75253_b();
            }
        }

        public void func_75246_d() {
            if (this.field_179448_g != null) {
                if (--this.field_179450_h <= 0) {
                    this.field_75309_a = this.field_179448_g;
                    this.field_179448_g = null;
                    super.func_75249_e();
                }
            } else {
                if (this.field_75309_a != null) {
                    if (this.field_179449_j.func_70821_d((EntityPlayer) this.field_75309_a)) {
                        if (((EntityPlayer) this.field_75309_a).func_70068_e(this.field_179449_j) < 16.0D && this.getEnderman().tryEscape(EndermanEscapeEvent.Reason.STARE)) { // Paper
                            this.field_179449_j.func_70820_n();
                        }

                        this.field_179451_i = 0;
                    } else if (((EntityPlayer) this.field_75309_a).func_70068_e(this.field_179449_j) > 256.0D && this.field_179451_i++ >= 30 && this.field_179449_j.func_70816_c((Entity) this.field_75309_a)) {
                        this.field_179451_i = 0;
                    }
                }

                super.func_75246_d();
            }

        }
    }
}

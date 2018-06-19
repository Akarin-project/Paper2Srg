package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetEvent;

//CraftBukkit start
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetEvent;
//CraftBukkit end

public class EntityZombie extends EntityMob {

    protected static final IAttribute field_110186_bp = (new RangedAttribute((IAttribute) null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).func_111117_a("Spawn Reinforcements Chance");
    private static final UUID field_110187_bq = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private final AttributeModifier field_110188_br = new AttributeModifier(EntityZombie.field_110187_bq, "Baby speed boost", field_70170_p.paperConfig.babyZombieMovementSpeed, 1); // Paper - Remove static - Make baby speed configurable
    private static final DataParameter<Boolean> field_184737_bv = EntityDataManager.func_187226_a(EntityZombie.class, DataSerializers.field_187198_h);
    private static final DataParameter<Integer> field_184738_bw = EntityDataManager.func_187226_a(EntityZombie.class, DataSerializers.field_187192_b);
    private static final DataParameter<Boolean> field_184740_by = EntityDataManager.func_187226_a(EntityZombie.class, DataSerializers.field_187198_h);
    private final EntityAIBreakDoor field_146075_bs = new EntityAIBreakDoor(this);
    private boolean field_146076_bu;
    private float field_146074_bv = -1.0F;
    private float field_146073_bw;

    public EntityZombie(World world) {
        super(world);
        this.func_70105_a(0.6F, 1.95F);
    }

    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(2, new EntityAIZombieAttack(this, 1.0D, false));
        this.field_70714_bg.func_75776_a(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.field_70714_bg.func_75776_a(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
        this.func_175456_n();
    }

    protected void func_175456_n() {
        this.field_70714_bg.func_75776_a(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class}));
        this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        if ( field_70170_p.spigotConfig.zombieAggressiveTowardsVillager ) this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false)); // Spigot
        this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(35.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.23000000417232513D);
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(3.0D);
        this.func_110148_a(SharedMonsterAttributes.field_188791_g).func_111128_a(2.0D);
        this.func_110140_aT().func_111150_b(EntityZombie.field_110186_bp).func_111128_a(this.field_70146_Z.nextDouble() * 0.10000000149011612D);
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.func_184212_Q().func_187214_a(EntityZombie.field_184737_bv, Boolean.valueOf(false));
        this.func_184212_Q().func_187214_a(EntityZombie.field_184738_bw, Integer.valueOf(0));
        this.func_184212_Q().func_187214_a(EntityZombie.field_184740_by, Boolean.valueOf(false));
    }

    public void func_184733_a(boolean flag) {
        this.func_184212_Q().func_187227_b(EntityZombie.field_184740_by, Boolean.valueOf(flag));
    }

    public boolean func_146072_bX() {
        return this.field_146076_bu;
    }

    public void func_146070_a(boolean flag) {
        if (this.field_146076_bu != flag) {
            this.field_146076_bu = flag;
            ((PathNavigateGround) this.func_70661_as()).func_179688_b(flag);
            if (flag) {
                this.field_70714_bg.func_75776_a(1, this.field_146075_bs);
            } else {
                this.field_70714_bg.func_85156_a((EntityAIBase) this.field_146075_bs);
            }
        }

    }

    public boolean func_70631_g_() {
        return ((Boolean) this.func_184212_Q().func_187225_a(EntityZombie.field_184737_bv)).booleanValue();
    }

    protected int func_70693_a(EntityPlayer entityhuman) {
        if (this.func_70631_g_()) {
            this.field_70728_aV = (int) ((float) this.field_70728_aV * 2.5F);
        }

        return super.func_70693_a(entityhuman);
    }

    public void func_82227_f(boolean flag) {
        this.func_184212_Q().func_187227_b(EntityZombie.field_184737_bv, Boolean.valueOf(flag));
        if (this.field_70170_p != null && !this.field_70170_p.field_72995_K) {
            IAttributeInstance attributeinstance = this.func_110148_a(SharedMonsterAttributes.field_111263_d);

            attributeinstance.func_111124_b(this.field_110188_br);
            if (flag) {
                attributeinstance.func_111121_a(this.field_110188_br);
            }
        }

        this.func_146071_k(flag);
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityZombie.field_184737_bv.equals(datawatcherobject)) {
            this.func_146071_k(this.func_70631_g_());
        }

        super.func_184206_a(datawatcherobject);
    }

    public void func_70636_d() {
        if (this.field_70170_p.func_72935_r() && !this.field_70170_p.field_72995_K && !this.func_70631_g_() && this.func_190730_o()) {
            float f = this.func_70013_c();

            if (f > 0.5F && this.field_70146_Z.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.field_70170_p.func_175678_i(new BlockPos(this.field_70165_t, this.field_70163_u + (double) this.func_70047_e(), this.field_70161_v))) {
                boolean flag = true;
                ItemStack itemstack = this.func_184582_a(EntityEquipmentSlot.HEAD);

                if (!itemstack.func_190926_b()) {
                    if (itemstack.func_77984_f()) {
                        itemstack.func_77964_b(itemstack.func_77952_i() + this.field_70146_Z.nextInt(2));
                        if (itemstack.func_77952_i() >= itemstack.func_77958_k()) {
                            this.func_70669_a(itemstack);
                            this.func_184201_a(EntityEquipmentSlot.HEAD, ItemStack.field_190927_a);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    // CraftBukkit start
                    EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                    this.field_70170_p.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        this.func_70015_d(event.getDuration());
                    }
                    // CraftBukkit end
                }
            }
        }

        super.func_70636_d();
    }

    protected boolean func_190730_o() {
        return true;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (super.func_70097_a(damagesource, f)) {
            EntityLivingBase entityliving = this.func_70638_az();

            if (entityliving == null && damagesource.func_76346_g() instanceof EntityLivingBase) {
                entityliving = (EntityLivingBase) damagesource.func_76346_g();
            }

            if (entityliving != null && this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD && (double) this.field_70146_Z.nextFloat() < this.func_110148_a(EntityZombie.field_110186_bp).func_111126_e() && this.field_70170_p.func_82736_K().func_82766_b("doMobSpawning")) {
                int i = MathHelper.func_76128_c(this.field_70165_t);
                int j = MathHelper.func_76128_c(this.field_70163_u);
                int k = MathHelper.func_76128_c(this.field_70161_v);
                EntityZombie entityzombie = new EntityZombie(this.field_70170_p);

                for (int l = 0; l < 50; ++l) {
                    int i1 = i + MathHelper.func_76136_a(this.field_70146_Z, 7, 40) * MathHelper.func_76136_a(this.field_70146_Z, -1, 1);
                    int j1 = j + MathHelper.func_76136_a(this.field_70146_Z, 7, 40) * MathHelper.func_76136_a(this.field_70146_Z, -1, 1);
                    int k1 = k + MathHelper.func_76136_a(this.field_70146_Z, 7, 40) * MathHelper.func_76136_a(this.field_70146_Z, -1, 1);

                    if (this.field_70170_p.func_180495_p(new BlockPos(i1, j1 - 1, k1)).func_185896_q() && !this.field_70170_p.isLightLevel(new BlockPos(i1, j1, k1), 10)) { // Paper
                        entityzombie.func_70107_b((double) i1, (double) j1, (double) k1);
                        if (!this.field_70170_p.func_175636_b((double) i1, (double) j1, (double) k1, 7.0D) && this.field_70170_p.func_72917_a(entityzombie.func_174813_aQ(), (Entity) entityzombie) && this.field_70170_p.func_184144_a(entityzombie, entityzombie.func_174813_aQ()).isEmpty() && !this.field_70170_p.func_72953_d(entityzombie.func_174813_aQ())) {
                            this.field_70170_p.addEntity(entityzombie, CreatureSpawnEvent.SpawnReason.REINFORCEMENTS); // CraftBukkit
                            entityzombie.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.REINFORCEMENT_TARGET, true);
                            entityzombie.func_180482_a(this.field_70170_p.func_175649_E(new BlockPos(entityzombie)), (IEntityLivingData) null);
                            this.func_110148_a(EntityZombie.field_110186_bp).func_111121_a(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
                            entityzombie.func_110148_a(EntityZombie.field_110186_bp).func_111121_a(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
                            break;
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean func_70652_k(Entity entity) {
        boolean flag = super.func_70652_k(entity);

        if (flag) {
            float f = this.field_70170_p.func_175649_E(new BlockPos(this)).func_180168_b();

            if (this.func_184614_ca().func_190926_b() && this.func_70027_ad() && this.field_70146_Z.nextFloat() < f * 0.3F) {
                // CraftBukkit start
                EntityCombustByEntityEvent event = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 2 * (int) f); // PAIL: fixme
                this.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    entity.func_70015_d(event.getDuration());
                }
                // CraftBukkit end
            }
        }

        return flag;
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187899_gZ;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187934_hh;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187930_hd;
    }

    protected SoundEvent func_190731_di() {
        return SoundEvents.field_187939_hm;
    }

    protected void func_180429_a(BlockPos blockposition, Block block) {
        this.func_184185_a(this.func_190731_di(), 0.15F, 1.0F);
    }

    public EnumCreatureAttribute func_70668_bt() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186383_ah;
    }

    protected void func_180481_a(DifficultyInstance difficultydamagescaler) {
        super.func_180481_a(difficultydamagescaler);
        if (this.field_70146_Z.nextFloat() < (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
            int i = this.field_70146_Z.nextInt(3);

            if (i == 0) {
                this.func_184201_a(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.field_151040_l));
            } else {
                this.func_184201_a(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.field_151037_a));
            }
        }

    }

    public static void func_189779_d(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityZombie.class);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        if (this.func_70631_g_()) {
            nbttagcompound.func_74757_a("IsBaby", true);
        }

        nbttagcompound.func_74757_a("CanBreakDoors", this.func_146072_bX());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (nbttagcompound.func_74767_n("IsBaby")) {
            this.func_82227_f(true);
        }

        this.func_146070_a(nbttagcompound.func_74767_n("CanBreakDoors"));
    }

    public void func_70074_a(EntityLivingBase entityliving) {
        super.func_70074_a(entityliving);
        if ((this.field_70170_p.func_175659_aa() == EnumDifficulty.NORMAL || this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) && entityliving instanceof EntityVillager) {
            if (this.field_70170_p.func_175659_aa() != EnumDifficulty.HARD && this.field_70146_Z.nextBoolean()) {
                return;
            }

            EntityVillager entityvillager = (EntityVillager) entityliving;
            EntityZombieVillager entityzombievillager = new EntityZombieVillager(this.field_70170_p);

            entityzombievillager.func_82149_j(entityvillager);
            this.field_70170_p.func_72900_e(entityvillager);
            entityzombievillager.func_180482_a(this.field_70170_p.func_175649_E(new BlockPos(entityzombievillager)), new EntityZombie.GroupData(false, null));
            entityzombievillager.func_190733_a(entityvillager.func_70946_n());
            entityzombievillager.func_82227_f(entityvillager.func_70631_g_());
            entityzombievillager.func_94061_f(entityvillager.func_175446_cd());
            if (entityvillager.func_145818_k_()) {
                entityzombievillager.func_96094_a(entityvillager.func_95999_t());
                entityzombievillager.func_174805_g(entityvillager.func_174833_aM());
            }

            this.field_70170_p.addEntity(entityzombievillager, CreatureSpawnEvent.SpawnReason.INFECTION); // CraftBukkit - add SpawnReason
            this.field_70170_p.func_180498_a((EntityPlayer) null, 1026, new BlockPos(this), 0);
        }

    }

    public float func_70047_e() {
        float f = 1.74F;

        if (this.func_70631_g_()) {
            f = (float) ((double) f - 0.81D);
        }

        return f;
    }

    protected boolean func_175448_a(ItemStack itemstack) {
        return itemstack.func_77973_b() == Items.field_151110_aK && this.func_70631_g_() && this.func_184218_aH() ? false : super.func_175448_a(itemstack);
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        Object object = super.func_180482_a(difficultydamagescaler, groupdataentity);
        float f = difficultydamagescaler.func_180170_c();

        this.func_98053_h(this.field_70146_Z.nextFloat() < 0.55F * f);
        if (object == null) {
            object = new EntityZombie.GroupData(this.field_70170_p.field_73012_v.nextFloat() < 0.05F, null);
        }

        if (object instanceof EntityZombie.GroupData) {
            EntityZombie.GroupData entityzombie_groupdatazombie = (EntityZombie.GroupData) object;

            if (entityzombie_groupdatazombie.field_142048_a) {
                this.func_82227_f(true);
                if ((double) this.field_70170_p.field_73012_v.nextFloat() < 0.05D) {
                    List list = this.field_70170_p.func_175647_a(EntityChicken.class, this.func_174813_aQ().func_72314_b(5.0D, 3.0D, 5.0D), EntitySelectors.field_152785_b);

                    if (!list.isEmpty()) {
                        EntityChicken entitychicken = (EntityChicken) list.get(0);

                        entitychicken.func_152117_i(true);
                        this.func_184220_m(entitychicken);
                    }
                } else if ((double) this.field_70170_p.field_73012_v.nextFloat() < 0.05D) {
                    EntityChicken entitychicken1 = new EntityChicken(this.field_70170_p);

                    entitychicken1.func_70012_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, 0.0F);
                    entitychicken1.func_180482_a(difficultydamagescaler, (IEntityLivingData) null);
                    entitychicken1.func_152117_i(true);
                    this.field_70170_p.addEntity(entitychicken1, CreatureSpawnEvent.SpawnReason.MOUNT); // CraftBukkit
                    this.func_184220_m(entitychicken1);
                }
            }
        }

        this.func_146070_a(this.field_70146_Z.nextFloat() < f * 0.1F);
        this.func_180481_a(difficultydamagescaler);
        this.func_180483_b(difficultydamagescaler);
        if (this.func_184582_a(EntityEquipmentSlot.HEAD).func_190926_b()) {
            Calendar calendar = this.field_70170_p.func_83015_S();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.field_70146_Z.nextFloat() < 0.25F) {
                this.func_184201_a(EntityEquipmentSlot.HEAD, new ItemStack(this.field_70146_Z.nextFloat() < 0.1F ? Blocks.field_150428_aP : Blocks.field_150423_aK));
                this.field_184655_bs[EntityEquipmentSlot.HEAD.func_188454_b()] = 0.0F;
            }
        }

        this.func_110148_a(SharedMonsterAttributes.field_111266_c).func_111121_a(new AttributeModifier("Random spawn bonus", this.field_70146_Z.nextDouble() * 0.05000000074505806D, 0));
        double d0 = this.field_70146_Z.nextDouble() * 1.5D * (double) f;

        if (d0 > 1.0D) {
            this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111121_a(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (this.field_70146_Z.nextFloat() < f * 0.05F) {
            this.func_110148_a(EntityZombie.field_110186_bp).func_111121_a(new AttributeModifier("Leader zombie bonus", this.field_70146_Z.nextDouble() * 0.25D + 0.5D, 0));
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111121_a(new AttributeModifier("Leader zombie bonus", this.field_70146_Z.nextDouble() * 3.0D + 1.0D, 2));
            this.func_146070_a(true);
        }

        return (IEntityLivingData) object;
    }

    public void func_146071_k(boolean flag) {
        this.func_146069_a(flag ? 0.5F : 1.0F);
    }

    public final void func_70105_a(float f, float f1) {
        boolean flag = this.field_146074_bv > 0.0F && this.field_146073_bw > 0.0F;

        this.field_146074_bv = f;
        this.field_146073_bw = f1;
        if (!flag) {
            this.func_146069_a(1.0F);
        }

    }

    protected final void func_146069_a(float f) {
        super.func_70105_a(this.field_146074_bv * f, this.field_146073_bw * f);
    }

    public double func_70033_W() {
        return this.func_70631_g_() ? 0.0D : -0.45D;
    }

    public void func_70645_a(DamageSource damagesource) {
        // super.die(damagesource); // CraftBukkit
        if (damagesource.func_76346_g() instanceof EntityCreeper) {
            EntityCreeper entitycreeper = (EntityCreeper) damagesource.func_76346_g();

            if (entitycreeper.func_70830_n() && entitycreeper.func_70650_aV()) {
                entitycreeper.func_175493_co();
                ItemStack itemstack = this.func_190732_dj();

                if (!itemstack.func_190926_b()) {
                    this.func_70099_a(itemstack, 0.0F);
                }
            }
        }
        super.func_70645_a(damagesource); // CraftBukkit - moved from above

    }

    protected ItemStack func_190732_dj() {
        return new ItemStack(Items.field_151144_bL, 1, 2);
    }

    class GroupData implements IEntityLivingData {

        public boolean field_142048_a;

        private GroupData(boolean flag) {
            this.field_142048_a = flag;
        }

        GroupData(boolean flag, Object object) {
            this(flag);
        }
    }
}

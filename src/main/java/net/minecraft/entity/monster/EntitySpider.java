package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySpider extends EntityMob {

    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntitySpider.class, DataSerializers.BYTE);

    public EntitySpider(World world) {
        super(world);
        this.setSize(1.4F, 0.9F);
    }

    public static void registerFixesSpider(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntitySpider.class);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntitySpider.AISpiderAttack(this));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntitySpider.AISpiderTarget(this, EntityPlayer.class));
        this.targetTasks.addTask(3, new EntitySpider.AISpiderTarget(this, EntityIronGolem.class));
    }

    public double getMountedYOffset() {
        return (double) (this.height * 0.5F);
    }

    protected PathNavigate createNavigator(World world) {
        return new PathNavigateClimber(this, world);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySpider.CLIMBING, Byte.valueOf((byte) 0));
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }

    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SPIDER;
    }

    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    public void setInWeb() {}

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean isPotionApplicable(PotionEffect mobeffect) {
        return mobeffect.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(mobeffect);
    }

    public boolean isBesideClimbableBlock() {
        return (((Byte) this.dataManager.get(EntitySpider.CLIMBING)).byteValue() & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntitySpider.CLIMBING)).byteValue();

        if (flag) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 &= -2;
        }

        this.dataManager.set(EntitySpider.CLIMBING, Byte.valueOf(b0));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        Object object = super.onInitialSpawn(difficultydamagescaler, groupdataentity);

        if (this.world.rand.nextInt(100) == 0) {
            EntitySkeleton entityskeleton = new EntitySkeleton(this.world);

            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(difficultydamagescaler, (IEntityLivingData) null);
            this.world.addEntity(entityskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.JOCKEY); // CraftBukkit - add SpawnReason
            entityskeleton.startRiding(this);
        }

        if (object == null) {
            object = new EntitySpider.GroupData();
            if (this.world.getDifficulty() == EnumDifficulty.HARD && this.world.rand.nextFloat() < 0.1F * difficultydamagescaler.getClampedAdditionalDifficulty()) {
                ((EntitySpider.GroupData) object).setRandomEffect(this.world.rand);
            }
        }

        if (object instanceof EntitySpider.GroupData) {
            Potion mobeffectlist = ((EntitySpider.GroupData) object).effect;

            if (mobeffectlist != null) {
                this.addPotionEffect(new PotionEffect(mobeffectlist, Integer.MAX_VALUE));
            }
        }

        return (IEntityLivingData) object;
    }

    public float getEyeHeight() {
        return 0.65F;
    }

    static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

        public AISpiderTarget(EntitySpider entityspider, Class<T> oclass) {
            super(entityspider, oclass, true);
        }

        public boolean shouldExecute() {
            float f = this.taskOwner.getBrightness();

            return f >= 0.5F ? false : super.shouldExecute();
        }
    }

    static class AISpiderAttack extends EntityAIAttackMelee {

        public AISpiderAttack(EntitySpider entityspider) {
            super(entityspider, 1.0D, true);
        }

        public boolean shouldContinueExecuting() {
            float f = this.attacker.getBrightness();

            if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget((EntityLivingBase) null);
                return false;
            } else {
                return super.shouldContinueExecuting();
            }
        }

        protected double getAttackReachSqr(EntityLivingBase entityliving) {
            return (double) (4.0F + entityliving.width);
        }
    }

    public static class GroupData implements IEntityLivingData {

        public Potion effect;

        public GroupData() {}

        public void setRandomEffect(Random random) {
            int i = random.nextInt(5);

            if (i <= 1) {
                this.effect = MobEffects.SPEED;
            } else if (i <= 2) {
                this.effect = MobEffects.STRENGTH;
            } else if (i <= 3) {
                this.effect = MobEffects.REGENERATION;
            } else if (i <= 4) {
                this.effect = MobEffects.INVISIBILITY;
            }

        }
    }
}

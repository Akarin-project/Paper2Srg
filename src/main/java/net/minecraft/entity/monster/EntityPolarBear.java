package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityPolarBear.a;
import net.minecraft.server.EntityPolarBear.b;
import net.minecraft.server.EntityPolarBear.c;
import net.minecraft.server.EntityPolarBear.d;
import net.minecraft.server.EntityPolarBear.e;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPolarBear extends EntityAnimal {

    private static final DataParameter<Boolean> IS_STANDING = EntityDataManager.createKey(EntityPolarBear.class, DataSerializers.BOOLEAN);
    private float clientSideStandAnimation0;
    private float clientSideStandAnimation;
    private int warningSoundTicks;

    public EntityPolarBear(World world) {
        super(world);
        this.setSize(1.3F, 1.4F);
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return new EntityPolarBear(this.world);
    }

    public boolean isBreedingItem(ItemStack itemstack) {
        return false;
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityPolarBear.d());
        this.tasks.addTask(1, new EntityPolarBear.e());
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityPolarBear.c());
        this.targetTasks.addTask(2, new EntityPolarBear.a());
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    protected SoundEvent getAmbientSound() {
        return this.isChild() ? SoundEvents.ENTITY_POLAR_BEAR_BABY_AMBIENT : SoundEvents.ENTITY_POLAR_BEAR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_POLAR_BEAR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_POLAR_BEAR_DEATH;
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(SoundEvents.ENTITY_POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(SoundEvents.ENTITY_POLAR_BEAR_WARNING, 1.0F, 1.0F);
            this.warningSoundTicks = 40;
        }

    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_POLAR_BEAR;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityPolarBear.IS_STANDING, Boolean.valueOf(false));
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            this.clientSideStandAnimation0 = this.clientSideStandAnimation;
            if (this.isStanding()) {
                this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else {
                this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }

    }

    public boolean attackEntityAsMob(Entity entity) {
        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag) {
            this.applyEnchantments((EntityLivingBase) this, entity);
        }

        return flag;
    }

    public boolean isStanding() {
        return ((Boolean) this.dataManager.get(EntityPolarBear.IS_STANDING)).booleanValue();
    }

    public void setStanding(boolean flag) {
        this.dataManager.set(EntityPolarBear.IS_STANDING, Boolean.valueOf(flag));
    }

    protected float getWaterSlowDown() {
        return 0.98F;
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, IEntityLivingData groupdataentity) {
        if (groupdataentity instanceof EntityPolarBear.b) {
            if (((EntityPolarBear.b) groupdataentity).a) {
                this.setGrowingAge(-24000);
            }
        } else {
            EntityPolarBear.b entitypolarbear_b = new EntityPolarBear.b(null);

            entitypolarbear_b.a = true;
            groupdataentity = entitypolarbear_b;
        }

        return (IEntityLivingData) groupdataentity;
    }

    class e extends EntityAIPanic {

        public e() {
            super(EntityPolarBear.this, 2.0D);
        }

        public boolean shouldExecute() {
            return !EntityPolarBear.this.isChild() && !EntityPolarBear.this.isBurning() ? false : super.shouldExecute();
        }
    }

    class d extends EntityAIAttackMelee {

        public d() {
            super(EntityPolarBear.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(EntityLivingBase entityliving, double d0) {
            double d1 = this.getAttackReachSqr(entityliving);

            if (d0 <= d1 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.attacker.attackEntityAsMob(entityliving);
                EntityPolarBear.this.setStanding(false);
            } else if (d0 <= d1 * 2.0D) {
                if (this.attackTick <= 0) {
                    EntityPolarBear.this.setStanding(false);
                    this.attackTick = 20;
                }

                if (this.attackTick <= 10) {
                    EntityPolarBear.this.setStanding(true);
                    EntityPolarBear.this.playWarningSound();
                }
            } else {
                this.attackTick = 20;
                EntityPolarBear.this.setStanding(false);
            }

        }

        public void resetTask() {
            EntityPolarBear.this.setStanding(false);
            super.resetTask();
        }

        protected double getAttackReachSqr(EntityLivingBase entityliving) {
            return (double) (4.0F + entityliving.width);
        }
    }

    class a extends EntityAINearestAttackableTarget<EntityPlayer> {

        public a() {
            super(EntityPolarBear.this, EntityPlayer.class, 20, true, true, (Predicate) null);
        }

        public boolean shouldExecute() {
            if (EntityPolarBear.this.isChild()) {
                return false;
            } else {
                if (super.shouldExecute()) {
                    List list = EntityPolarBear.this.world.getEntitiesWithinAABB(EntityPolarBear.class, EntityPolarBear.this.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        EntityPolarBear entitypolarbear = (EntityPolarBear) iterator.next();

                        if (entitypolarbear.isChild()) {
                            return true;
                        }
                    }
                }

                EntityPolarBear.this.setAttackTarget((EntityLivingBase) null);
                return false;
            }
        }

        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.5D;
        }
    }

    class c extends EntityAIHurtByTarget {

        public c() {
            super(EntityPolarBear.this, false, new Class[0]);
        }

        public void startExecuting() {
            super.startExecuting();
            if (EntityPolarBear.this.isChild()) {
                this.alertOthers();
                this.resetTask();
            }

        }

        protected void setEntityAttackTarget(EntityCreature entitycreature, EntityLivingBase entityliving) {
            if (entitycreature instanceof EntityPolarBear && !entitycreature.isChild()) {
                super.setEntityAttackTarget(entitycreature, entityliving);
            }

        }
    }

    static class b implements IEntityLivingData {

        public boolean a;

        private b() {}

        b(Object object) {
            this();
        }
    }
}

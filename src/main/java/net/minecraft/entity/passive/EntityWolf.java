package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityWolf.a;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
// CraftBukkit end

public class EntityWolf extends EntityTameable {

    private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(EntityWolf.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(EntityWolf.class, DataSerializers.VARINT);
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    public EntityWolf(World world) {
        super(world);
        this.setSize(0.6F, 0.85F);
        this.setTamed(false);
    }

    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityWolf.a(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
        this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(9, new EntityAIBeg(this, 8.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return entity instanceof EntitySheep || entity instanceof EntityRabbit;
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        }));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, AbstractSkeleton.class, false));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    // CraftBukkit - add overriden version
    @Override
    public boolean setGoalTarget(EntityLivingBase entityliving, org.bukkit.event.entity.EntityTargetEvent.TargetReason reason, boolean fire) {
        if (!super.setGoalTarget(entityliving, reason, fire)) {
            return false;
        }
        entityliving = getAttackTarget();
        if (entityliving == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
        return true;
    }
    // CraftBukkit end

    public void setAttackTarget(@Nullable EntityLivingBase entityliving) {
        super.setAttackTarget(entityliving);
        if (entityliving == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }

    }

    protected void updateAITasks() {
        this.dataManager.set(EntityWolf.DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityWolf.DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
        this.dataManager.register(EntityWolf.BEGGING, Boolean.valueOf(false));
        this.dataManager.register(EntityWolf.COLLAR_COLOR, Integer.valueOf(EnumDyeColor.RED.getDyeDamage()));
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    public static void registerFixesWolf(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityWolf.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Angry", this.isAngry());
        nbttagcompound.setByte("CollarColor", (byte) this.getCollarColor().getDyeDamage());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setAngry(nbttagcompound.getBoolean("Angry"));
        if (nbttagcompound.hasKey("CollarColor", 99)) {
            this.setCollarColor(EnumDyeColor.byDyeDamage(nbttagcompound.getByte("CollarColor")));
        }

    }

    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? SoundEvents.ENTITY_WOLF_GROWL : (this.rand.nextInt(3) == 0 ? (this.isTamed() && ((Float) this.dataManager.get(EntityWolf.DATA_HEALTH_ID)).floatValue() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT) : SoundEvents.ENTITY_WOLF_AMBIENT);
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WOLF;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.world.setEntityState(this, (byte) 8);
        }

        if (!this.world.isRemote && this.getAttackTarget() == null && this.isAngry()) {
            this.setAngry(false);
        }

    }

    public void onUpdate() {
        super.onUpdate();
        this.headRotationCourseOld = this.headRotationCourse;
        if (this.isBegging()) {
            this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
        } else {
            this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
        }

        if (this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else if ((this.isWet || this.isShaking) && this.isShaking) {
            if (this.timeWolfIsShaking == 0.0F) {
                this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;
            if (this.prevTimeWolfIsShaking >= 2.0F) {
                this.isWet = false;
                this.isShaking = false;
                this.prevTimeWolfIsShaking = 0.0F;
                this.timeWolfIsShaking = 0.0F;
            }

            if (this.timeWolfIsShaking > 0.4F) {
                float f = (float) this.getEntityBoundingBox().minY;
                int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double) f1, (double) (f + 0.8F), this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }

    }

    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            Entity entity = damagesource.getTrueSource();

            if (this.aiSit != null) {
                // CraftBukkit - moved into EntityLiving.d(DamageSource, float)
                // this.goalSit.setSitting(false);
            }
            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
                f = (f + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(damagesource, f);
        }
    }

    public boolean attackEntityAsMob(Entity entity) {
        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag) {
            this.applyEnchantments((EntityLivingBase) this, entity);
        }

        return flag;
    }

    public void setTamed(boolean flag) {
        super.setTamed(flag);
        if (flag) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (this.isTamed()) {
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof ItemFood) {
                    ItemFood itemfood = (ItemFood) itemstack.getItem();

                    if (itemfood.isWolfsFavoriteMeat() && ((Float) this.dataManager.get(EntityWolf.DATA_HEALTH_ID)).floatValue() < 20.0F) {
                        if (!entityhuman.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }

                        this.heal((float) itemfood.getHealAmount(itemstack), org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.EATING); // CraftBukkit
                        return true;
                    }
                } else if (itemstack.getItem() == Items.DYE) {
                    EnumDyeColor enumcolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

                    if (enumcolor != this.getCollarColor()) {
                        this.setCollarColor(enumcolor);
                        if (!entityhuman.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }

                        return true;
                    }
                }
            }

            if (this.isOwner((EntityLivingBase) entityhuman) && !this.world.isRemote && !this.isBreedingItem(itemstack)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPath();
                this.setGoalTarget((EntityLivingBase) null, TargetReason.FORGOT_TARGET, true); // CraftBukkit - reason
            }
        } else if (itemstack.getItem() == Items.BONE && !this.isAngry()) {
            if (!entityhuman.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            if (!this.world.isRemote) {
                // CraftBukkit - added event call and isCancelled check.
                if (this.rand.nextInt(3) == 0 && !CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                    this.setTamedBy(entityhuman);
                    this.navigator.clearPath();
                    this.setAttackTarget((EntityLivingBase) null);
                    this.aiSit.setSitting(true);
                    this.setHealth(this.getMaxHealth()); // CraftBukkit - 20.0 -> getMaxHealth()
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.processInteract(entityhuman, enumhand);
    }

    public boolean isBreedingItem(ItemStack itemstack) {
        return itemstack.getItem() instanceof ItemFood && ((ItemFood) itemstack.getItem()).isWolfsFavoriteMeat();
    }

    public int getMaxSpawnedInChunk() {
        return 8;
    }

    public boolean isAngry() {
        return (((Byte) this.dataManager.get(EntityWolf.TAMED)).byteValue() & 2) != 0;
    }

    public void setAngry(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntityWolf.TAMED)).byteValue();

        if (flag) {
            this.dataManager.set(EntityWolf.TAMED, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.dataManager.set(EntityWolf.TAMED, Byte.valueOf((byte) (b0 & -3)));
        }

    }

    public EnumDyeColor getCollarColor() {
        return EnumDyeColor.byDyeDamage(((Integer) this.dataManager.get(EntityWolf.COLLAR_COLOR)).intValue() & 15);
    }

    public void setCollarColor(EnumDyeColor enumcolor) {
        this.dataManager.set(EntityWolf.COLLAR_COLOR, Integer.valueOf(enumcolor.getDyeDamage()));
    }

    public EntityWolf createChild(EntityAgeable entityageable) {
        EntityWolf entitywolf = new EntityWolf(this.world);
        UUID uuid = this.getOwnerId();

        if (uuid != null) {
            entitywolf.setOwnerId(uuid);
            entitywolf.setTamed(true);
        }

        return entitywolf;
    }

    public void setBegging(boolean flag) {
        this.dataManager.set(EntityWolf.BEGGING, Boolean.valueOf(flag));
    }

    public boolean canMateWith(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(entityanimal instanceof EntityWolf)) {
            return false;
        } else {
            EntityWolf entitywolf = (EntityWolf) entityanimal;

            return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : this.isInLove() && entitywolf.isInLove());
        }
    }

    public boolean isBegging() {
        return ((Boolean) this.dataManager.get(EntityWolf.BEGGING)).booleanValue();
    }

    public boolean shouldAttackEntity(EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        if (!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
            if (entityliving instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) entityliving;

                if (entitywolf.isTamed() && entitywolf.getOwner() == entityliving1) {
                    return false;
                }
            }

            return entityliving instanceof EntityPlayer && entityliving1 instanceof EntityPlayer && !((EntityPlayer) entityliving1).canAttackPlayer((EntityPlayer) entityliving) ? false : !(entityliving instanceof AbstractHorse) || !((AbstractHorse) entityliving).isTame();
        } else {
            return false;
        }
    }

    public boolean canBeLeashedTo(EntityPlayer entityhuman) {
        return !this.isAngry() && super.canBeLeashedTo(entityhuman);
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.createChild(entityageable);
    }

    class a<T extends Entity> extends EntityAIAvoidEntity<T> {

        private final EntityWolf d;

        public a(EntityWolf entitywolf, Class oclass, float f, double d0, double d1) {
            super(entitywolf, oclass, f, d0, d1);
            this.d = entitywolf;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && this.closestLivingEntity instanceof EntityLlama ? !this.d.isTamed() && this.a((EntityLlama) this.closestLivingEntity) : false;
        }

        private boolean a(EntityLlama entityllama) {
            return entityllama.getStrength() >= EntityWolf.this.rand.nextInt(5);
        }

        public void startExecuting() {
            EntityWolf.this.setAttackTarget((EntityLivingBase) null);
            super.startExecuting();
        }

        public void updateTask() {
            EntityWolf.this.setAttackTarget((EntityLivingBase) null);
            super.updateTask();
        }
    }
}

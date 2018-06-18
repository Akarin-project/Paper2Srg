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

    private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.VARINT);
    protected float clientSideTailAnimation;
    protected float clientSideTailAnimationO;
    protected float clientSideTailAnimationSpeed;
    protected float clientSideSpikesAnimation;
    protected float clientSideSpikesAnimationO;
    private EntityLivingBase targetedEntity;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    public EntityAIWander wander;

    public EntityGuardian(World world) {
        super(world);
        this.experienceValue = 10;
        this.setSize(0.85F, 0.85F);
        this.moveHelper = new EntityGuardian.GuardianMoveHelper(this);
        this.clientSideTailAnimation = this.rand.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }

    protected void initEntityAI() {
        EntityAIMoveTowardsRestriction pathfindergoalmovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D);

        this.wander = new EntityAIWander(this, 1.0D, 80);
        this.tasks.addTask(4, new EntityGuardian.AIGuardianAttack(this));
        this.tasks.addTask(5, pathfindergoalmovetowardsrestriction);
        this.tasks.addTask(7, this.wander);
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.wander.setMutexBits(3);
        pathfindergoalmovetowardsrestriction.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new EntityGuardian.GuardianTargetSelector(this)));
    }

    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    public static void registerFixesGuardian(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityGuardian.class);
    }

    protected PathNavigate createNavigator(World world) {
        return new PathNavigateSwimmer(this, world);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityGuardian.MOVING, Boolean.valueOf(false));
        this.dataManager.register(EntityGuardian.TARGET_ENTITY, Integer.valueOf(0));
    }

    public boolean do() {
        return ((Boolean) this.dataManager.get(EntityGuardian.MOVING)).booleanValue();
    }

    private void setMoving(boolean flag) {
        this.dataManager.set(EntityGuardian.MOVING, Boolean.valueOf(flag));
    }

    public int getAttackDuration() {
        return 80;
    }

    private void setTargetedEntity(int i) {
        this.dataManager.set(EntityGuardian.TARGET_ENTITY, Integer.valueOf(i));
    }

    public boolean hasTargetedEntity() {
        return ((Integer) this.dataManager.get(EntityGuardian.TARGET_ENTITY)).intValue() != 0;
    }

    @Nullable
    public EntityLivingBase getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.world.isRemote) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.world.getEntityByID(((Integer) this.dataManager.get(EntityGuardian.TARGET_ENTITY)).intValue());

                if (entity instanceof EntityLivingBase) {
                    this.targetedEntity = (EntityLivingBase) entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.getAttackTarget();
        }
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        super.notifyDataManagerChange(datawatcherobject);
        if (EntityGuardian.TARGET_ENTITY.equals(datawatcherobject)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }

    }

    public int getTalkInterval() {
        return 160;
    }

    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public float getEyeHeight() {
        return this.height * 0.5F;
    }

    public float getBlockPathWeight(BlockPos blockposition) {
        return this.world.getBlockState(blockposition).getMaterial() == Material.WATER ? 10.0F + this.world.getLightBrightness(blockposition) - 0.5F : super.getBlockPathWeight(blockposition);
    }

    public void onLivingUpdate() {
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

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    public float getAttackAnimationScale(float f) {
        return ((float) this.clientSideAttackTime + f) / (float) this.getAttackDuration();
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_GUARDIAN;
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), (Entity) this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0 || !this.world.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
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

    public int getVerticalFaceSpeed() {
        return 180;
    }

    public void travel(float f, float f1, float f2) {
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

        private final EntityGuardian entityGuardian;

        public GuardianMoveHelper(EntityGuardian entityguardian) {
            super(entityguardian);
            this.entityGuardian = entityguardian;
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.entityGuardian.getNavigator().noPath()) {
                double d0 = this.posX - this.entityGuardian.posX;
                double d1 = this.posY - this.entityGuardian.posY;
                double d2 = this.posZ - this.entityGuardian.posZ;
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                d1 /= d3;
                float f = (float) (MathHelper.atan2(d2, d0) * 57.2957763671875D) - 90.0F;

                this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 90.0F);
                this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
                float f1 = (float) (this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());

                this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125F);
                double d4 = Math.sin((double) (this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5D) * 0.05D;
                double d5 = Math.cos((double) (this.entityGuardian.rotationYaw * 0.017453292F));
                double d6 = Math.sin((double) (this.entityGuardian.rotationYaw * 0.017453292F));

                this.entityGuardian.motionX += d4 * d5;
                this.entityGuardian.motionZ += d4 * d6;
                d4 = Math.sin((double) (this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75D) * 0.05D;
                this.entityGuardian.motionY += d4 * (d6 + d5) * 0.25D;
                this.entityGuardian.motionY += (double) this.entityGuardian.getAIMoveSpeed() * d1 * 0.1D;
                EntityLookHelper controllerlook = this.entityGuardian.getLookHelper();
                double d7 = this.entityGuardian.posX + d0 / d3 * 2.0D;
                double d8 = (double) this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3;
                double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0D;
                double d10 = controllerlook.getLookPosX();
                double d11 = controllerlook.getLookPosY();
                double d12 = controllerlook.getLookPosZ();

                if (!controllerlook.getIsLooking()) {
                    d10 = d7;
                    d11 = d8;
                    d12 = d9;
                }

                this.entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
                this.entityGuardian.setMoving(true);
            } else {
                this.entityGuardian.setAIMoveSpeed(0.0F);
                this.entityGuardian.setMoving(false);
            }
        }
    }

    static class AIGuardianAttack extends EntityAIBase {

        private final EntityGuardian guardian;
        private int tickCounter;
        private final boolean isElder;

        public AIGuardianAttack(EntityGuardian entityguardian) {
            this.guardian = entityguardian;
            this.isElder = entityguardian instanceof EntityElderGuardian;
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            EntityLivingBase entityliving = this.guardian.getAttackTarget();

            return entityliving != null && entityliving.isEntityAlive();
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && (this.isElder || this.guardian.getDistanceSq(this.guardian.getAttackTarget()) > 9.0D);
        }

        public void startExecuting() {
            this.tickCounter = -10;
            this.guardian.getNavigator().clearPath();
            this.guardian.getLookHelper().setLookPositionWithEntity(this.guardian.getAttackTarget(), 90.0F, 90.0F);
            this.guardian.isAirBorne = true;
        }

        public void resetTask() {
            this.guardian.setTargetedEntity(0);
            this.guardian.setAttackTarget((EntityLivingBase) null);
            this.guardian.wander.makeUpdate();
        }

        public void updateTask() {
            EntityLivingBase entityliving = this.guardian.getAttackTarget();

            this.guardian.getNavigator().clearPath();
            this.guardian.getLookHelper().setLookPositionWithEntity(entityliving, 90.0F, 90.0F);
            if (!this.guardian.canEntityBeSeen(entityliving)) {
                this.guardian.setAttackTarget((EntityLivingBase) null);
            } else {
                ++this.tickCounter;
                if (this.tickCounter == 0) {
                    this.guardian.setTargetedEntity(this.guardian.getAttackTarget().getEntityId());
                    this.guardian.world.setEntityState(this.guardian, (byte) 21);
                } else if (this.tickCounter >= this.guardian.getAttackDuration()) {
                    float f = 1.0F;

                    if (this.guardian.world.getDifficulty() == EnumDifficulty.HARD) {
                        f += 2.0F;
                    }

                    if (this.isElder) {
                        f += 2.0F;
                    }

                    entityliving.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.guardian, this.guardian), f);
                    entityliving.attackEntityFrom(DamageSource.causeMobDamage(this.guardian), (float) this.guardian.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                    this.guardian.setAttackTarget((EntityLivingBase) null);
                }

                super.updateTask();
            }
        }
    }

    static class GuardianTargetSelector implements Predicate<EntityLivingBase> {

        private final EntityGuardian parentEntity;

        public GuardianTargetSelector(EntityGuardian entityguardian) {
            this.parentEntity = entityguardian;
        }

        public boolean apply(@Nullable EntityLivingBase entityliving) {
            return (entityliving instanceof EntityPlayer || entityliving instanceof EntitySquid) && entityliving.getDistanceSq(this.parentEntity) > 9.0D;
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((EntityLivingBase) object);
        }
    }
}

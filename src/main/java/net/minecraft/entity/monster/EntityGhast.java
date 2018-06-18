package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGhast extends EntityFlying implements IMob {

    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);
    private int explosionStrength = 1;

    public EntityGhast(World world) {
        super(world);
        this.setSize(4.0F, 4.0F);
        this.isImmuneToFire = true;
        this.experienceValue = 5;
        this.moveHelper = new EntityGhast.GhastMoveHelper(this);
    }

    protected void initEntityAI() {
        this.tasks.addTask(5, new EntityGhast.AIRandomFly(this));
        this.tasks.addTask(7, new EntityGhast.AILookAround(this));
        this.tasks.addTask(7, new EntityGhast.AIFireballAttack(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

    public void setAttacking(boolean flag) {
        this.dataManager.set(EntityGhast.ATTACKING, Boolean.valueOf(flag));
    }

    public int getFireballStrength() {
        return this.explosionStrength;
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }

    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (damagesource.getImmediateSource() instanceof EntityLargeFireball && damagesource.getTrueSource() instanceof EntityPlayer) {
            super.attackEntityFrom(damagesource, 1000.0F);
            return true;
        } else {
            return super.attackEntityFrom(damagesource, f);
        }
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityGhast.ATTACKING, Boolean.valueOf(false));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_GHAST;
    }

    protected float getSoundVolume() {
        return 10.0F;
    }

    public boolean getCanSpawnHere() {
        return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public static void registerFixesGhast(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityGhast.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("ExplosionPower", this.explosionStrength);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = nbttagcompound.getInteger("ExplosionPower");
        }

    }

    public float getEyeHeight() {
        return 2.6F;
    }

    static class AIFireballAttack extends EntityAIBase {

        private final EntityGhast parentEntity;
        public int attackTimer;

        public AIFireballAttack(EntityGhast entityghast) {
            this.parentEntity = entityghast;
        }

        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
            this.parentEntity.setAttacking(false);
        }

        public void updateTask() {
            EntityLivingBase entityliving = this.parentEntity.getAttackTarget();
            double d0 = 64.0D;

            if (entityliving.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(entityliving)) {
                World world = this.parentEntity.world;

                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    world.playEvent((EntityPlayer) null, 1015, new BlockPos(this.parentEntity), 0);
                }

                if (this.attackTimer == 20) {
                    double d1 = 4.0D;
                    Vec3d vec3d = this.parentEntity.getLook(1.0F);
                    double d2 = entityliving.posX - (this.parentEntity.posX + vec3d.x * 4.0D);
                    double d3 = entityliving.getEntityBoundingBox().minY + (double) (entityliving.height / 2.0F) - (0.5D + this.parentEntity.posY + (double) (this.parentEntity.height / 2.0F));
                    double d4 = entityliving.posZ - (this.parentEntity.posZ + vec3d.z * 4.0D);

                    world.playEvent((EntityPlayer) null, 1016, new BlockPos(this.parentEntity), 0);
                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this.parentEntity, d2, d3, d4);

                    // CraftBukkit - set bukkitYield when setting explosionpower
                    entitylargefireball.bukkitYield = entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
                    entitylargefireball.posX = this.parentEntity.posX + vec3d.x * 4.0D;
                    entitylargefireball.posY = this.parentEntity.posY + (double) (this.parentEntity.height / 2.0F) + 0.5D;
                    entitylargefireball.posZ = this.parentEntity.posZ + vec3d.z * 4.0D;
                    world.spawnEntity(entitylargefireball);
                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }

            this.parentEntity.setAttacking(this.attackTimer > 10);
        }
    }

    static class AILookAround extends EntityAIBase {

        private final EntityGhast parentEntity;

        public AILookAround(EntityGhast entityghast) {
            this.parentEntity = entityghast;
            this.setMutexBits(2);
        }

        public boolean shouldExecute() {
            return true;
        }

        public void updateTask() {
            if (this.parentEntity.getAttackTarget() == null) {
                this.parentEntity.rotationYaw = -((float) MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * 57.295776F;
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            } else {
                EntityLivingBase entityliving = this.parentEntity.getAttackTarget();
                double d0 = 64.0D;

                if (entityliving.getDistanceSq(this.parentEntity) < 4096.0D) {
                    double d1 = entityliving.posX - this.parentEntity.posX;
                    double d2 = entityliving.posZ - this.parentEntity.posZ;

                    this.parentEntity.rotationYaw = -((float) MathHelper.atan2(d1, d2)) * 57.295776F;
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }

        }
    }

    static class AIRandomFly extends EntityAIBase {

        private final EntityGhast parentEntity;

        public AIRandomFly(EntityGhast entityghast) {
            this.parentEntity = entityghast;
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            EntityMoveHelper controllermove = this.parentEntity.getMoveHelper();

            if (!controllermove.isUpdating()) {
                return true;
            } else {
                double d0 = controllermove.getX() - this.parentEntity.posX;
                double d1 = controllermove.getY() - this.parentEntity.posY;
                double d2 = controllermove.getZ() - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);

            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

    static class GhastMoveHelper extends EntityMoveHelper {

        private final EntityGhast parentEntity;
        private int courseChangeCooldown;

        public GhastMoveHelper(EntityGhast entityghast) {
            super(entityghast);
            this.parentEntity = entityghast;
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - this.parentEntity.posX;
                double d1 = this.posY - this.parentEntity.posY;
                double d2 = this.posZ - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    d3 = (double) MathHelper.sqrt(d3);
                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3)) {
                        this.parentEntity.motionX += d0 / d3 * 0.1D;
                        this.parentEntity.motionY += d1 / d3 * 0.1D;
                        this.parentEntity.motionZ += d2 / d3 * 0.1D;
                    } else {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }

            }
        }

        private boolean isNotColliding(double d0, double d1, double d2, double d3) {
            double d4 = (d0 - this.parentEntity.posX) / d3;
            double d5 = (d1 - this.parentEntity.posY) / d3;
            double d6 = (d2 - this.parentEntity.posZ) / d3;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double) i < d3; ++i) {
                axisalignedbb = axisalignedbb.offset(d4, d5, d6);
                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }
}

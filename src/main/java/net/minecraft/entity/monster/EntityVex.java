package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityVex.a;
import net.minecraft.server.EntityVex.b;
import net.minecraft.server.EntityVex.c;
import net.minecraft.server.EntityVex.d;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVex extends EntityMob {

    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.createKey(EntityVex.class, DataSerializers.BYTE);
    private EntityLiving owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public EntityVex(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.moveHelper = new EntityVex.c(this);
        this.setSize(0.4F, 0.8F);
        this.experienceValue = 3;
    }

    public void move(MoverType enummovetype, double d0, double d1, double d2) {
        super.move(enummovetype, d0, d1, d2);
        this.doBlockCollisions();
    }

    public void onUpdate() {
        this.noClip = true;
        super.onUpdate();
        this.noClip = false;
        this.setNoGravity(true);
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }

    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityVex.a());
        this.tasks.addTask(8, new EntityVex.d());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityVex.class}));
        this.targetTasks.addTask(2, new EntityVex.b(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityVex.VEX_FLAGS, Byte.valueOf((byte) 0));
    }

    public static void registerFixesVex(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityVex.class);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("BoundX")) {
            this.boundOrigin = new BlockPos(nbttagcompound.getInteger("BoundX"), nbttagcompound.getInteger("BoundY"), nbttagcompound.getInteger("BoundZ"));
        }

        if (nbttagcompound.hasKey("LifeTicks")) {
            this.setLimitedLife(nbttagcompound.getInteger("LifeTicks"));
        }

    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        if (this.boundOrigin != null) {
            nbttagcompound.setInteger("BoundX", this.boundOrigin.getX());
            nbttagcompound.setInteger("BoundY", this.boundOrigin.getY());
            nbttagcompound.setInteger("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan) {
            nbttagcompound.setInteger("LifeTicks", this.limitedLifeTicks);
        }

    }

    public EntityLiving getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos blockposition) {
        this.boundOrigin = blockposition;
    }

    private boolean getVexFlag(int i) {
        byte b0 = ((Byte) this.dataManager.get(EntityVex.VEX_FLAGS)).byteValue();

        return (b0 & i) != 0;
    }

    private void setVexFlag(int i, boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntityVex.VEX_FLAGS)).byteValue();
        int j;

        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.dataManager.set(EntityVex.VEX_FLAGS, Byte.valueOf((byte) (j & 255)));
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    public void setCharging(boolean flag) {
        this.setVexFlag(1, flag);
    }

    public void setOwner(EntityLiving entityinsentient) {
        this.owner = entityinsentient;
    }

    public void setLimitedLife(int i) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = i;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VEX;
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.setEquipmentBasedOnDifficulty(difficultydamagescaler);
        this.setEnchantmentBasedOnDifficulty(difficultydamagescaler);
        return super.onInitialSpawn(difficultydamagescaler, groupdataentity);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultydamagescaler) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
    }

    class b extends EntityAITarget {

        public b(EntityCreature entitycreature) {
            super(entitycreature, false);
        }

        public boolean shouldExecute() {
            return EntityVex.this.owner != null && EntityVex.this.owner.getAttackTarget() != null && this.isSuitableTarget(EntityVex.this.owner.getAttackTarget(), false);
        }

        public void startExecuting() {
            EntityVex.this.setGoalTarget(EntityVex.this.owner.getAttackTarget(), EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true); // CraftBukkit
            super.startExecuting();
        }
    }

    class d extends EntityAIBase {

        public d() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            return !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockposition = EntityVex.this.getBoundOrigin();

            if (blockposition == null) {
                blockposition = new BlockPos(EntityVex.this);
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockposition1 = blockposition.add(EntityVex.this.rand.nextInt(15) - 7, EntityVex.this.rand.nextInt(11) - 5, EntityVex.this.rand.nextInt(15) - 7);

                if (EntityVex.this.world.isAirBlock(blockposition1)) {
                    EntityVex.this.moveHelper.setMoveTo((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.5D, (double) blockposition1.getZ() + 0.5D, 0.25D);
                    if (EntityVex.this.getAttackTarget() == null) {
                        EntityVex.this.getLookHelper().setLookPosition((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.5D, (double) blockposition1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class a extends EntityAIBase {

        public a() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            return EntityVex.this.getAttackTarget() != null && !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0 ? EntityVex.this.getDistanceSq((Entity) EntityVex.this.getAttackTarget()) > 4.0D : false;
        }

        public boolean shouldContinueExecuting() {
            return EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.isCharging() && EntityVex.this.getAttackTarget() != null && EntityVex.this.getAttackTarget().isEntityAlive();
        }

        public void startExecuting() {
            EntityLivingBase entityliving = EntityVex.this.getAttackTarget();
            Vec3d vec3d = entityliving.getPositionEyes(1.0F);

            EntityVex.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityVex.this.setCharging(true);
            EntityVex.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        public void resetTask() {
            EntityVex.this.setCharging(false);
        }

        public void updateTask() {
            EntityLivingBase entityliving = EntityVex.this.getAttackTarget();

            if (EntityVex.this.getEntityBoundingBox().intersects(entityliving.getEntityBoundingBox())) {
                EntityVex.this.attackEntityAsMob(entityliving);
                EntityVex.this.setCharging(false);
            } else {
                double d0 = EntityVex.this.getDistanceSq((Entity) entityliving);

                if (d0 < 9.0D) {
                    Vec3d vec3d = entityliving.getPositionEyes(1.0F);

                    EntityVex.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }

        }
    }

    class c extends EntityMoveHelper {

        public c(EntityVex entityvex) {
            super(entityvex);
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityVex.this.posX;
                double d1 = this.posY - EntityVex.this.posY;
                double d2 = this.posZ - EntityVex.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                d3 = (double) MathHelper.sqrt(d3);
                if (d3 < EntityVex.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityVex.this.motionX *= 0.5D;
                    EntityVex.this.motionY *= 0.5D;
                    EntityVex.this.motionZ *= 0.5D;
                } else {
                    EntityVex.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityVex.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityVex.this.motionZ += d2 / d3 * 0.05D * this.speed;
                    if (EntityVex.this.getAttackTarget() == null) {
                        EntityVex.this.rotationYaw = -((float) MathHelper.atan2(EntityVex.this.motionX, EntityVex.this.motionZ)) * 57.295776F;
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    } else {
                        double d4 = EntityVex.this.getAttackTarget().posX - EntityVex.this.posX;
                        double d5 = EntityVex.this.getAttackTarget().posZ - EntityVex.this.posZ;

                        EntityVex.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * 57.295776F;
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    }
                }

            }
        }
    }
}

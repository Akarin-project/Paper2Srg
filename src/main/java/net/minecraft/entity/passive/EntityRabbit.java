package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityRabbit extends EntityAnimal {

    private static final DataParameter<Integer> RABBIT_TYPE = EntityDataManager.createKey(EntityRabbit.class, DataSerializers.VARINT);
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int currentMoveTypeDuration;
    private int carrotTicks;

    public EntityRabbit(World world) {
        super(world);
        this.setSize(0.4F, 0.5F);
        this.jumpHelper = new EntityRabbit.RabbitJumpHelper(this);
        this.moveHelper = new EntityRabbit.RabbitMoveHelper(this);
        this.initializePathFinderGoals(); // CraftBukkit - moved code
    }

    // CraftBukkit start - code from constructor
    public void initializePathFinderGoals(){
        this.setMovementSpeed(0.0D);
    }
    // CraftBukkit end

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityRabbit.AIPanic(this, 2.2D));
        this.tasks.addTask(2, new EntityAIMate(this, 0.8D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.CARROT, false));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.GOLDEN_CARROT, false));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), false));
        this.tasks.addTask(4, new EntityRabbit.AIAvoidEntity(this, EntityPlayer.class, 8.0F, 2.2D, 2.2D));
        this.tasks.addTask(4, new EntityRabbit.AIAvoidEntity(this, EntityWolf.class, 10.0F, 2.2D, 2.2D));
        this.tasks.addTask(4, new EntityRabbit.AIAvoidEntity(this, EntityMob.class, 4.0F, 2.2D, 2.2D));
        this.tasks.addTask(5, new EntityRabbit.AIRaidFarm(this));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
    }

    protected float getJumpUpwardsMotion() {
        if (!this.collidedHorizontally && (!this.moveHelper.isUpdating() || this.moveHelper.getY() <= this.posY + 0.5D)) {
            Path pathentity = this.navigator.getPath();

            if (pathentity != null && pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength()) {
                Vec3d vec3d = pathentity.getPosition((Entity) this);

                if (vec3d.y > this.posY + 0.5D) {
                    return 0.5F;
                }
            }

            return this.moveHelper.getSpeed() <= 0.6D ? 0.2F : 0.3F;
        } else {
            return 0.5F;
        }
    }

    protected void jump() {
        super.jump();
        double d0 = this.moveHelper.getSpeed();

        if (d0 > 0.0D) {
            double d1 = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (d1 < 0.010000000000000002D) {
                this.moveRelative(0.0F, 0.0F, 1.0F, 0.1F);
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte) 1);
        }

    }

    public void setMovementSpeed(double d0) {
        this.getNavigator().setSpeed(d0);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), d0);
    }

    public void setJumping(boolean flag) {
        super.setJumping(flag);
        if (flag) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }

    }

    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityRabbit.RABBIT_TYPE, Integer.valueOf(0));
    }

    public void updateAITasks() {
        if (this.currentMoveTypeDuration > 0) {
            --this.currentMoveTypeDuration;
        }

        if (this.carrotTicks > 0) {
            this.carrotTicks -= this.rand.nextInt(3);
            if (this.carrotTicks < 0) {
                this.carrotTicks = 0;
            }
        }

        if (this.onGround) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0) {
                EntityLivingBase entityliving = this.getAttackTarget();

                if (entityliving != null && this.getDistanceSq(entityliving) < 16.0D) {
                    this.calculateRotationYaw(entityliving.posX, entityliving.posZ);
                    this.moveHelper.setMoveTo(entityliving.posX, entityliving.posY, entityliving.posZ, this.moveHelper.getSpeed());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }

            EntityRabbit.RabbitJumpHelper entityrabbit_controllerjumprabbit = (EntityRabbit.RabbitJumpHelper) this.jumpHelper;

            if (!entityrabbit_controllerjumprabbit.getIsJumping()) {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
                    Path pathentity = this.navigator.getPath();
                    Vec3d vec3d = new Vec3d(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());

                    if (pathentity != null && pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength()) {
                        vec3d = pathentity.getPosition((Entity) this);
                    }

                    this.calculateRotationYaw(vec3d.x, vec3d.z);
                    this.startJumping();
                }
            } else if (!entityrabbit_controllerjumprabbit.canJump()) {
                this.enableJumpControl();
            }
        }

        this.wasOnGround = this.onGround;
    }

    public void spawnRunningParticles() {}

    private void calculateRotationYaw(double d0, double d1) {
        this.rotationYaw = (float) (MathHelper.atan2(d1 - this.posZ, d0 - this.posX) * 57.2957763671875D) - 90.0F;
    }

    private void enableJumpControl() {
        ((EntityRabbit.RabbitJumpHelper) this.jumpHelper).setCanJump(true);
    }

    private void disableJumpControl() {
        ((EntityRabbit.RabbitJumpHelper) this.jumpHelper).setCanJump(false);
    }

    private void updateMoveTypeDuration() {
        if (this.moveHelper.getSpeed() < 2.2D) {
            this.currentMoveTypeDuration = 10;
        } else {
            this.currentMoveTypeDuration = 1;
        }

    }

    private void checkLandingDelay() {
        this.updateMoveTypeDuration();
        this.disableJumpControl();
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }

    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    public static void registerFixesRabbit(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityRabbit.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("RabbitType", this.getRabbitType());
        nbttagcompound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setRabbitType(nbttagcompound.getInteger("RabbitType"));
        this.carrotTicks = nbttagcompound.getInteger("MoreCarrotTicks");
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.ENTITY_RABBIT_JUMP;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_RABBIT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_RABBIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_RABBIT_DEATH;
    }

    public boolean attackEntityAsMob(Entity entity) {
        if (this.getRabbitType() == 99) {
            this.playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
        } else {
            return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
        }
    }

    public SoundCategory getSoundCategory() {
        return this.getRabbitType() == 99 ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        return this.isEntityInvulnerable(damagesource) ? false : super.attackEntityFrom(damagesource, f);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_RABBIT;
    }

    private boolean isRabbitBreedingItem(Item item) {
        return item == Items.CARROT || item == Items.GOLDEN_CARROT || item == Item.getItemFromBlock(Blocks.YELLOW_FLOWER);
    }

    public EntityRabbit createChild(EntityAgeable entityageable) {
        EntityRabbit entityrabbit = new EntityRabbit(this.world);
        int i = this.getRandomRabbitType();

        if (this.rand.nextInt(20) != 0) {
            if (entityageable instanceof EntityRabbit && this.rand.nextBoolean()) {
                i = ((EntityRabbit) entityageable).getRabbitType();
            } else {
                i = this.getRabbitType();
            }
        }

        entityrabbit.setRabbitType(i);
        return entityrabbit;
    }

    public boolean isBreedingItem(ItemStack itemstack) {
        return this.isRabbitBreedingItem(itemstack.getItem());
    }

    public int getRabbitType() {
        return ((Integer) this.dataManager.get(EntityRabbit.RABBIT_TYPE)).intValue();
    }

    public void setRabbitType(int i) {
        if (i == 99) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
            this.tasks.addTask(4, new EntityRabbit.AIEvilAttack(this));
            this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomNameTag(I18n.translateToLocal("entity.KillerBunny.name"));
            }
        }

        this.dataManager.set(EntityRabbit.RABBIT_TYPE, Integer.valueOf(i));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        Object object = super.onInitialSpawn(difficultydamagescaler, groupdataentity);
        int i = this.getRandomRabbitType();
        boolean flag = false;

        if (object instanceof EntityRabbit.RabbitTypeData) {
            i = ((EntityRabbit.RabbitTypeData) object).typeData;
            flag = true;
        } else {
            object = new EntityRabbit.RabbitTypeData(i);
        }

        this.setRabbitType(i);
        if (flag) {
            this.setGrowingAge(-24000);
        }

        return (IEntityLivingData) object;
    }

    private int getRandomRabbitType() {
        Biome biomebase = this.world.getBiome(new BlockPos(this));
        int i = this.rand.nextInt(100);

        return biomebase.isSnowyBiome() ? (i < 80 ? 1 : 3) : (biomebase instanceof BiomeDesert ? 4 : (i < 50 ? 0 : (i < 90 ? 5 : 2)));
    }

    private boolean isCarrotEaten() {
        return this.carrotTicks == 0;
    }

    protected void createEatingParticles() {
        BlockCarrot blockcarrots = (BlockCarrot) Blocks.CARROTS;
        IBlockState iblockdata = blockcarrots.withAge(blockcarrots.getMaxAge());

        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D, new int[] { Block.getStateId(iblockdata)});
        this.carrotTicks = 40;
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.createChild(entityageable);
    }

    static class AIEvilAttack extends EntityAIAttackMelee {

        public AIEvilAttack(EntityRabbit entityrabbit) {
            super(entityrabbit, 1.4D, true);
        }

        protected double getAttackReachSqr(EntityLivingBase entityliving) {
            return (double) (4.0F + entityliving.width);
        }
    }

    static class AIPanic extends EntityAIPanic {

        private final EntityRabbit rabbit;

        public AIPanic(EntityRabbit entityrabbit, double d0) {
            super(entityrabbit, d0);
            this.rabbit = entityrabbit;
        }

        public void updateTask() {
            super.updateTask();
            this.rabbit.setMovementSpeed(this.speed);
        }
    }

    static class AIRaidFarm extends EntityAIMoveToBlock {

        private final EntityRabbit rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;

        public AIRaidFarm(EntityRabbit entityrabbit) {
            super(entityrabbit, 0.699999988079071D, 16);
            this.rabbit = entityrabbit;
        }

        public boolean shouldExecute() {
            if (this.runDelay <= 0) {
                if (!this.rabbit.world.getGameRules().getBoolean("mobGriefing")) {
                    return false;
                }

                this.canRaid = false;
                this.wantsToRaid = this.rabbit.isCarrotEaten();
                this.wantsToRaid = true;
            }

            return super.shouldExecute();
        }

        public boolean shouldContinueExecuting() {
            return this.canRaid && super.shouldContinueExecuting();
        }

        public void updateTask() {
            super.updateTask();
            this.rabbit.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5D, (double) (this.destinationBlock.getY() + 1), (double) this.destinationBlock.getZ() + 0.5D, 10.0F, (float) this.rabbit.getVerticalFaceSpeed());
            if (this.getIsAboveDestination()) {
                World world = this.rabbit.world;
                BlockPos blockposition = this.destinationBlock.up();
                IBlockState iblockdata = world.getBlockState(blockposition);
                Block block = iblockdata.getBlock();

                if (this.canRaid && block instanceof BlockCarrot) {
                    Integer integer = (Integer) iblockdata.getValue(BlockCarrot.AGE);

                    if (integer.intValue() == 0) {
                        // CraftBukkit start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.rabbit, blockposition, Blocks.AIR, 0).isCancelled()) {
                            return;
                        }
                        // CraftBukkit end
                        world.setBlockState(blockposition, Blocks.AIR.getDefaultState(), 2);
                        world.destroyBlock(blockposition, true);
                    } else {
                        // CraftBukkit start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(
                                this.rabbit,
                                blockposition,
                                block, block.getMetaFromState(iblockdata.withProperty(BlockCarrot.AGE, Integer.valueOf(integer.intValue() - 1)))
                        ).isCancelled()) {
                            return;
                        }
                        // CraftBukkit end
                        world.setBlockState(blockposition, iblockdata.withProperty(BlockCarrot.AGE, Integer.valueOf(integer.intValue() - 1)), 2);
                        world.playEvent(2001, blockposition, Block.getStateId(iblockdata));
                    }

                    this.rabbit.createEatingParticles();
                }

                this.canRaid = false;
                this.runDelay = 10;
            }

        }

        protected boolean shouldMoveTo(World world, BlockPos blockposition) {
            Block block = world.getBlockState(blockposition).getBlock();

            if (block == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid) {
                blockposition = blockposition.up();
                IBlockState iblockdata = world.getBlockState(blockposition);

                block = iblockdata.getBlock();
                if (block instanceof BlockCarrot && ((BlockCarrot) block).isMaxAge(iblockdata)) {
                    this.canRaid = true;
                    return true;
                }
            }

            return false;
        }
    }

    static class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T> {

        private final EntityRabbit rabbit;

        public AIAvoidEntity(EntityRabbit entityrabbit, Class<T> oclass, float f, double d0, double d1) {
            super(entityrabbit, oclass, f, d0, d1);
            this.rabbit = entityrabbit;
        }

        public boolean shouldExecute() {
            return this.rabbit.getRabbitType() != 99 && super.shouldExecute();
        }
    }

    static class RabbitMoveHelper extends EntityMoveHelper {

        private final EntityRabbit rabbit;
        private double nextJumpSpeed;

        public RabbitMoveHelper(EntityRabbit entityrabbit) {
            super(entityrabbit);
            this.rabbit = entityrabbit;
        }

        public void onUpdateMoveHelper() {
            if (this.rabbit.onGround && !this.rabbit.isJumping && !((EntityRabbit.RabbitJumpHelper) this.rabbit.jumpHelper).getIsJumping()) {
                this.rabbit.setMovementSpeed(0.0D);
            } else if (this.isUpdating()) {
                this.rabbit.setMovementSpeed(this.nextJumpSpeed);
            }

            super.onUpdateMoveHelper();
        }

        public void setMoveTo(double d0, double d1, double d2, double d3) {
            if (this.rabbit.isInWater()) {
                d3 = 1.5D;
            }

            super.setMoveTo(d0, d1, d2, d3);
            if (d3 > 0.0D) {
                this.nextJumpSpeed = d3;
            }

        }
    }

    public class RabbitJumpHelper extends EntityJumpHelper {

        private final EntityRabbit rabbit;
        private boolean canJump;

        public RabbitJumpHelper(EntityRabbit entityrabbit) {
            super(entityrabbit);
            this.rabbit = entityrabbit;
        }

        public boolean getIsJumping() {
            return this.isJumping;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean flag) {
            this.canJump = flag;
        }

        public void doJump() {
            if (this.isJumping) {
                this.rabbit.startJumping();
                this.isJumping = false;
            }

        }
    }

    public static class RabbitTypeData implements IEntityLivingData {

        public int typeData;

        public RabbitTypeData(int i) {
            this.typeData = i;
        }
    }
}

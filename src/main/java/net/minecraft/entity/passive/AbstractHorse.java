package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public abstract class AbstractHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount {

    private static final Predicate<Entity> IS_HORSE_BREEDING = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof AbstractHorse && ((AbstractHorse) entity).isBreeding();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final IAttribute JUMP_STRENGTH = (new RangedAttribute((IAttribute) null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    private static final DataParameter<Byte> STATUS = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private int eatingCounter;
    private int openMouthCounter;
    private int jumpRearingCounter;
    public int tailCounter;
    public int sprintCounter;
    protected boolean horseJumping;
    public ContainerHorseChest horseChest;
    protected int temper;
    protected float jumpPower;
    private boolean allowStandSliding;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;
    protected boolean canGallop = true;
    protected int gallopTime;
    public int maxDomestication = 100; // CraftBukkit - store max domestication value

    public AbstractHorse(World world) {
        super(world);
        this.setSize(1.3964844F, 1.6F);
        this.stepHeight = 1.0F;
        this.initHorseChest();
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D, AbstractHorse.class));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractHorse.STATUS, Byte.valueOf((byte) 0));
        this.dataManager.register(AbstractHorse.OWNER_UNIQUE_ID, Optional.absent());
    }

    protected boolean getHorseWatchableBoolean(int i) {
        return (((Byte) this.dataManager.get(AbstractHorse.STATUS)).byteValue() & i) != 0;
    }

    protected void setHorseWatchableBoolean(int i, boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(AbstractHorse.STATUS)).byteValue();

        if (flag) {
            this.dataManager.set(AbstractHorse.STATUS, Byte.valueOf((byte) (b0 | i)));
        } else {
            this.dataManager.set(AbstractHorse.STATUS, Byte.valueOf((byte) (b0 & ~i)));
        }

    }

    public boolean isTame() {
        return this.getHorseWatchableBoolean(2);
    }

    @Nullable
    public UUID getOwnerUniqueId() {
        return (UUID) ((Optional) this.dataManager.get(AbstractHorse.OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerUniqueId(@Nullable UUID uuid) {
        this.dataManager.set(AbstractHorse.OWNER_UNIQUE_ID, Optional.fromNullable(uuid));
    }

    public float getHorseSize() {
        return 0.5F;
    }

    public void setScaleForAge(boolean flag) {
        this.setScale(flag ? this.getHorseSize() : 1.0F);
    }

    public boolean isHorseJumping() {
        return this.horseJumping;
    }

    public void setHorseTamed(boolean flag) {
        this.setHorseWatchableBoolean(2, flag);
    }

    public void setHorseJumping(boolean flag) {
        this.horseJumping = flag;
    }

    public boolean canBeLeashedTo(EntityPlayer entityhuman) {
        return world.paperConfig.allowLeashingUndeadHorse ? super.canBeLeashedTo(entityhuman) : super.canBeLeashedTo(entityhuman) && this.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD; // Paper
    }

    protected void onLeashDistance(float f) {
        if (f > 6.0F && this.isEatingHaystack()) {
            this.setEatingHaystack(false);
        }

    }

    public boolean isEatingHaystack() {
        return this.getHorseWatchableBoolean(16);
    }

    public boolean isRearing() {
        return this.getHorseWatchableBoolean(32);
    }

    public boolean isBreeding() {
        return this.getHorseWatchableBoolean(8);
    }

    public void setBreeding(boolean flag) {
        this.setHorseWatchableBoolean(8, flag);
    }

    public void setHorseSaddled(boolean flag) {
        this.setHorseWatchableBoolean(4, flag);
    }

    public int getTemper() {
        return this.temper;
    }

    public void setTemper(int i) {
        this.temper = i;
    }

    public int increaseTemper(int i) {
        int j = MathHelper.clamp(this.getTemper() + i, 0, this.getMaxTemper());

        this.setTemper(j);
        return j;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        Entity entity = damagesource.getTrueSource();

        return this.isBeingRidden() && entity != null && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(damagesource, f);
    }

    public boolean canBePushed() {
        return !this.isBeingRidden();
    }

    private void eatingHorse() {
        this.openHorseMouth();
        if (!this.isSilent()) {
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_HORSE_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }

    }

    public void fall(float f, float f1) {
        if (f > 1.0F) {
            this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F);
        }

        int i = MathHelper.ceil((f * 0.5F - 3.0F) * f1);

        if (i > 0) {
            this.attackEntityFrom(DamageSource.FALL, (float) i);
            if (this.isBeingRidden()) {
                Iterator iterator = this.getRecursivePassengers().iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.attackEntityFrom(DamageSource.FALL, (float) i);
                }
            }

            IBlockState iblockdata = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - (double) this.prevRotationYaw, this.posZ));
            Block block = iblockdata.getBlock();

            if (iblockdata.getMaterial() != Material.AIR && !this.isSilent()) {
                SoundType soundeffecttype = block.getSoundType();

                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, soundeffecttype.getStepSound(), this.getSoundCategory(), soundeffecttype.getVolume() * 0.5F, soundeffecttype.getPitch() * 0.75F);
            }

        }
    }

    protected int getInventorySize() {
        return 2;
    }

    public void initHorseChest() {
        ContainerHorseChest inventoryhorsechest = this.horseChest;

        this.horseChest = new ContainerHorseChest("HorseChest", this.getInventorySize(), this); // CraftBukkit
        this.horseChest.setCustomName(this.getName());
        if (inventoryhorsechest != null) {
            inventoryhorsechest.removeInventoryChangeListener(this);
            int i = Math.min(inventoryhorsechest.getSizeInventory(), this.horseChest.getSizeInventory());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryhorsechest.getStackInSlot(j);

                if (!itemstack.isEmpty()) {
                    this.horseChest.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }

        this.horseChest.addInventoryChangeListener((IInventoryChangedListener) this);
        this.updateHorseSlots();
    }

    protected void updateHorseSlots() {
        if (!this.world.isRemote) {
            this.setHorseSaddled(!this.horseChest.getStackInSlot(0).isEmpty() && this.canBeSaddled());
        }
    }

    public void onInventoryChanged(IInventory iinventory) {
        boolean flag = this.isHorseSaddled();

        this.updateHorseSlots();
        if (this.ticksExisted > 20 && !flag && this.isHorseSaddled()) {
            this.playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
        }

    }

    @Nullable
    protected AbstractHorse getClosestHorse(Entity entity, double d0) {
        double d1 = Double.MAX_VALUE;
        Entity entity1 = null;
        List list = this.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(d0, d0, d0), AbstractHorse.IS_HORSE_BREEDING);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity2 = (Entity) iterator.next();
            double d2 = entity2.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if (d2 < d1) {
                entity1 = entity2;
                d1 = d2;
            }
        }

        return (AbstractHorse) entity1;
    }

    public double getHorseJumpStrength() {
        return this.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).getAttributeValue();
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        this.openHorseMouth();
        return null;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        this.openHorseMouth();
        if (this.rand.nextInt(3) == 0) {
            this.makeHorseRear();
        }

        return null;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        this.openHorseMouth();
        if (this.rand.nextInt(10) == 0 && !this.isMovementBlocked()) {
            this.makeHorseRear();
        }

        return null;
    }

    public boolean canBeSaddled() {
        return true;
    }

    public boolean isHorseSaddled() {
        return this.getHorseWatchableBoolean(4);
    }

    @Nullable
    protected SoundEvent getAngrySound() {
        this.openHorseMouth();
        this.makeHorseRear();
        return null;
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        if (!block.getDefaultState().getMaterial().isLiquid()) {
            SoundType soundeffecttype = block.getSoundType();

            if (this.world.getBlockState(blockposition.up()).getBlock() == Blocks.SNOW_LAYER) {
                soundeffecttype = Blocks.SNOW_LAYER.getSoundType();
            }

            if (this.isBeingRidden() && this.canGallop) {
                ++this.gallopTime;
                if (this.gallopTime > 5 && this.gallopTime % 3 == 0) {
                    this.playGallopSound(soundeffecttype);
                } else if (this.gallopTime <= 5) {
                    this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
                }
            } else if (soundeffecttype == SoundType.WOOD) {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
            } else {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
            }

        }
    }

    protected void playGallopSound(SoundType soundeffecttype) {
        this.playSound(SoundEvents.ENTITY_HORSE_GALLOP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(AbstractHorse.JUMP_STRENGTH);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22499999403953552D);
    }

    public int getMaxSpawnedInChunk() {
        return 6;
    }

    public int getMaxTemper() {
        return this.maxDomestication; // CraftBukkit - return stored max domestication instead of 100
    }

    protected float getSoundVolume() {
        return 0.8F;
    }

    public int getTalkInterval() {
        return 400;
    }

    public void openGUI(EntityPlayer entityhuman) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(entityhuman)) && this.isTame()) {
            this.horseChest.setCustomName(this.getName());
            entityhuman.openGuiHorseInventory(this, this.horseChest);
        }

    }

    protected boolean handleEating(EntityPlayer entityhuman, ItemStack itemstack) {
        boolean flag = false;
        float f = 0.0F;
        short short0 = 0;
        byte b0 = 0;
        Item item = itemstack.getItem();

        if (item == Items.WHEAT) {
            f = 2.0F;
            short0 = 20;
            b0 = 3;
        } else if (item == Items.SUGAR) {
            f = 1.0F;
            short0 = 30;
            b0 = 3;
        } else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            f = 20.0F;
            short0 = 180;
        } else if (item == Items.APPLE) {
            f = 3.0F;
            short0 = 60;
            b0 = 3;
        } else if (item == Items.GOLDEN_CARROT) {
            f = 4.0F;
            short0 = 60;
            b0 = 5;
            if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(entityhuman);
            }
        } else if (item == Items.GOLDEN_APPLE) {
            f = 10.0F;
            short0 = 240;
            b0 = 10;
            if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(entityhuman);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f, RegainReason.EATING); // CraftBukkit
            flag = true;
        }

        if (this.isChild() && short0 > 0) {
            this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            if (!this.world.isRemote) {
                this.addGrowth(short0);
            }

            flag = true;
        }

        if (b0 > 0 && (flag || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
            flag = true;
            if (!this.world.isRemote) {
                this.increaseTemper(b0);
            }
        }

        if (flag) {
            this.eatingHorse();
        }

        return flag;
    }

    protected void mountTo(EntityPlayer entityhuman) {
        entityhuman.rotationYaw = this.rotationYaw;
        entityhuman.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);
        if (!this.world.isRemote) {
            entityhuman.startRiding(this);
        }

    }

    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() && this.isBeingRidden() && this.isHorseSaddled() || this.isEatingHaystack() || this.isRearing();
    }

    public boolean isBreedingItem(ItemStack itemstack) {
        return false;
    }

    private void moveTail() {
        this.tailCounter = 1;
    }

    public void onDeath(DamageSource damagesource) {
        // super.die(damagesource); // Moved down
        if (!this.world.isRemote && this.horseChest != null) {
            for (int i = 0; i < this.horseChest.getSizeInventory(); ++i) {
                ItemStack itemstack = this.horseChest.getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }

        }
        super.onDeath(damagesource); // CraftBukkit
    }

    public void onLivingUpdate() {
        if (this.rand.nextInt(200) == 0) {
            this.moveTail();
        }

        super.onLivingUpdate();
        if (!this.world.isRemote) {
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F, RegainReason.REGEN); // CraftBukkit
            }

            if (this.canEatGrass()) {
                if (!this.isEatingHaystack() && !this.isBeingRidden() && this.rand.nextInt(300) == 0 && this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY) - 1, MathHelper.floor(this.posZ))).getBlock() == Blocks.GRASS) {
                    this.setEatingHaystack(true);
                }

                if (this.isEatingHaystack() && ++this.eatingCounter > 50) {
                    this.eatingCounter = 0;
                    this.setEatingHaystack(false);
                }
            }

            this.followMother();
        }
    }

    protected void followMother() {
        if (this.isBreeding() && this.isChild() && !this.isEatingHaystack()) {
            AbstractHorse entityhorseabstract = this.getClosestHorse(this, 16.0D);

            if (entityhorseabstract != null && this.getDistanceSq((Entity) entityhorseabstract) > 4.0D) {
                this.navigator.getPathToEntityLiving((Entity) entityhorseabstract);
            }
        }

    }

    public boolean canEatGrass() {
        return true;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30) {
            this.openMouthCounter = 0;
            this.setHorseWatchableBoolean(64, false);
        }

        if (this.canPassengerSteer() && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {
            this.jumpRearingCounter = 0;
            this.setRearing(false);
        }

        if (this.tailCounter > 0 && ++this.tailCounter > 8) {
            this.tailCounter = 0;
        }

        if (this.sprintCounter > 0) {
            ++this.sprintCounter;
            if (this.sprintCounter > 300) {
                this.sprintCounter = 0;
            }
        }

        this.prevHeadLean = this.headLean;
        if (this.isEatingHaystack()) {
            this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;
            if (this.headLean > 1.0F) {
                this.headLean = 1.0F;
            }
        } else {
            this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;
            if (this.headLean < 0.0F) {
                this.headLean = 0.0F;
            }
        }

        this.prevRearingAmount = this.rearingAmount;
        if (this.isRearing()) {
            this.headLean = 0.0F;
            this.prevHeadLean = this.headLean;
            this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;
            if (this.rearingAmount > 1.0F) {
                this.rearingAmount = 1.0F;
            }
        } else {
            this.allowStandSliding = false;
            this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;
            if (this.rearingAmount < 0.0F) {
                this.rearingAmount = 0.0F;
            }
        }

        this.prevMouthOpenness = this.mouthOpenness;
        if (this.getHorseWatchableBoolean(64)) {
            this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;
            if (this.mouthOpenness > 1.0F) {
                this.mouthOpenness = 1.0F;
            }
        } else {
            this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;
            if (this.mouthOpenness < 0.0F) {
                this.mouthOpenness = 0.0F;
            }
        }

    }

    private void openHorseMouth() {
        if (!this.world.isRemote) {
            this.openMouthCounter = 1;
            this.setHorseWatchableBoolean(64, true);
        }

    }

    public void setEatingHaystack(boolean flag) {
        this.setHorseWatchableBoolean(16, flag);
    }

    public void setRearing(boolean flag) {
        if (flag) {
            this.setEatingHaystack(false);
        }

        this.setHorseWatchableBoolean(32, flag);
    }

    private void makeHorseRear() {
        if (this.canPassengerSteer()) {
            this.jumpRearingCounter = 1;
            this.setRearing(true);
        }

    }

    public void makeMad() {
        this.makeHorseRear();
        SoundEvent soundeffect = this.getAngrySound();

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    public boolean setTamedBy(EntityPlayer entityhuman) {
        this.setOwnerUniqueId(entityhuman.getUniqueID());
        this.setHorseTamed(true);
        if (entityhuman instanceof EntityPlayerMP) {
            CriteriaTriggers.TAME_ANIMAL.trigger((EntityPlayerMP) entityhuman, (EntityAnimal) this);
        }

        this.world.setEntityState(this, (byte) 7);
        return true;
    }

    public void travel(float f, float f1, float f2) {
        if (this.isBeingRidden() && this.canBeSteered() && this.isHorseSaddled()) {
            EntityLivingBase entityliving = (EntityLivingBase) this.getControllingPassenger();

            this.rotationYaw = entityliving.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entityliving.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            f = entityliving.moveStrafing * 0.5F;
            f2 = entityliving.moveForward;
            if (f2 <= 0.0F) {
                f2 *= 0.25F;
                this.gallopTime = 0;
            }

            if (this.onGround && this.jumpPower == 0.0F && this.isRearing() && !this.allowStandSliding) {
                f = 0.0F;
                f2 = 0.0F;
            }

            if (this.jumpPower > 0.0F && !this.isHorseJumping() && this.onGround) {
                this.motionY = this.getHorseJumpStrength() * (double) this.jumpPower;
                if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                    this.motionY += (double) ((float) (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                }

                this.setHorseJumping(true);
                this.isAirBorne = true;
                if (f2 > 0.0F) {
                    float f3 = MathHelper.sin(this.rotationYaw * 0.017453292F);
                    float f4 = MathHelper.cos(this.rotationYaw * 0.017453292F);

                    this.motionX += (double) (-0.4F * f3 * this.jumpPower);
                    this.motionZ += (double) (0.4F * f4 * this.jumpPower);
                    this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
                }

                this.jumpPower = 0.0F;
            }

            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(f, f1, f2);
            } else if (entityliving instanceof EntityPlayer) {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            if (this.onGround) {
                this.jumpPower = 0.0F;
                this.setHorseJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f5 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            this.limbSwingAmount += (f5 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.jumpMovementFactor = 0.02F;
            super.travel(f, f1, f2);
        }
    }

    public static void registerFixesAbstractHorse(DataFixer dataconvertermanager, Class<?> oclass) {
        EntityLiving.registerFixesMob(dataconvertermanager, oclass);
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(oclass, new String[] { "SaddleItem"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("EatingHaystack", this.isEatingHaystack());
        nbttagcompound.setBoolean("Bred", this.isBreeding());
        nbttagcompound.setInteger("Temper", this.getTemper());
        nbttagcompound.setBoolean("Tame", this.isTame());
        if (this.getOwnerUniqueId() != null) {
            nbttagcompound.setString("OwnerUUID", this.getOwnerUniqueId().toString());
        }
        nbttagcompound.setInteger("Bukkit.MaxDomestication", this.maxDomestication); // CraftBukkit

        if (!this.horseChest.getStackInSlot(0).isEmpty()) {
            nbttagcompound.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setEatingHaystack(nbttagcompound.getBoolean("EatingHaystack"));
        this.setBreeding(nbttagcompound.getBoolean("Bred"));
        this.setTemper(nbttagcompound.getInteger("Temper"));
        this.setHorseTamed(nbttagcompound.getBoolean("Tame"));
        String s;

        if (nbttagcompound.hasKey("OwnerUUID", 8)) {
            s = nbttagcompound.getString("OwnerUUID");
        } else {
            String s1 = nbttagcompound.getString("Owner");

            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            this.setOwnerUniqueId(UUID.fromString(s));
        }
        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxDomestication")) {
            this.maxDomestication = nbttagcompound.getInteger("Bukkit.MaxDomestication");
        }
        // CraftBukkit end

        IAttributeInstance attributeinstance = this.getAttributeMap().getAttributeInstanceByName("Speed");

        if (attributeinstance != null) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(attributeinstance.getBaseValue() * 0.25D);
        }

        if (nbttagcompound.hasKey("SaddleItem", 10)) {
            ItemStack itemstack = new ItemStack(nbttagcompound.getCompoundTag("SaddleItem"));

            if (itemstack.getItem() == Items.SADDLE) {
                this.horseChest.setInventorySlotContents(0, itemstack);
            }
        }

        this.updateHorseSlots();
    }

    public boolean canMateWith(EntityAnimal entityanimal) {
        return false;
    }

    protected boolean canMate() {
        return !this.isBeingRidden() && !this.isRiding() && this.isTame() && !this.isChild() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    @Nullable
    public EntityAgeable createChild(EntityAgeable entityageable) {
        return null;
    }

    protected void setOffspringAttributes(EntityAgeable entityageable, AbstractHorse entityhorseabstract) {
        double d0 = this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + entityageable.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + (double) this.getModifiedMaxHealth();

        entityhorseabstract.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(d0 / 3.0D);
        double d1 = this.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).getBaseValue() + entityageable.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).getBaseValue() + this.getModifiedJumpStrength();

        entityhorseabstract.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).setBaseValue(d1 / 3.0D);
        double d2 = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + entityageable.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + this.getModifiedMovementSpeed();

        entityhorseabstract.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(d2 / 3.0D);
    }

    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof EntityLivingBase;
    }

    public boolean canJump() {
        return this.isHorseSaddled();
    }

    public void handleStartJump(int i) {
        // CraftBukkit start
        float power;
        if (i >= 90) {
            power = 1.0F;
        } else {
            power = 0.4F + 0.4F * (float) i / 90.0F;
        }
        org.bukkit.event.entity.HorseJumpEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callHorseJumpEvent(this, power);
        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end
        this.allowStandSliding = true;
        this.makeHorseRear();
    }

    public void handleStopJump() {}

    public void updatePassenger(Entity entity) {
        super.updatePassenger(entity);
        if (entity instanceof EntityLiving) {
            EntityLiving entityinsentient = (EntityLiving) entity;

            this.renderYawOffset = entityinsentient.renderYawOffset;
        }

        if (this.prevRearingAmount > 0.0F) {
            float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float f2 = 0.7F * this.prevRearingAmount;
            float f3 = 0.15F * this.prevRearingAmount;

            entity.setPosition(this.posX + (double) (f2 * f), this.posY + this.getMountedYOffset() + entity.getYOffset() + (double) f3, this.posZ - (double) (f2 * f1));
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).renderYawOffset = this.renderYawOffset;
            }
        }

    }

    protected float getModifiedMaxHealth() {
        return 15.0F + (float) this.rand.nextInt(8) + (float) this.rand.nextInt(9);
    }

    protected double getModifiedJumpStrength() {
        return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D;
    }

    protected double getModifiedMovementSpeed() {
        return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D) * 0.25D;
    }

    public boolean isOnLadder() {
        return false;
    }

    public float getEyeHeight() {
        return this.height;
    }

    public boolean wearsArmor() {
        return false;
    }

    public boolean isArmor(ItemStack itemstack) {
        return false;
    }

    public boolean replaceItemInInventory(int i, ItemStack itemstack) {
        int j = i - 400;

        if (j >= 0 && j < 2 && j < this.horseChest.getSizeInventory()) {
            if (j == 0 && itemstack.getItem() != Items.SADDLE) {
                return false;
            } else if (j == 1 && (!this.wearsArmor() || !this.isArmor(itemstack))) {
                return false;
            } else {
                this.horseChest.setInventorySlotContents(j, itemstack);
                this.updateHorseSlots();
                return true;
            }
        } else {
            int k = i - 500 + 2;

            if (k >= 2 && k < this.horseChest.getSizeInventory()) {
                this.horseChest.setInventorySlotContents(k, itemstack);
                return true;
            } else {
                return false;
            }
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        groupdataentity = super.onInitialSpawn(difficultydamagescaler, groupdataentity);
        if (this.rand.nextInt(5) == 0) {
            this.setGrowingAge(-24000);
        }

        return groupdataentity;
    }
}

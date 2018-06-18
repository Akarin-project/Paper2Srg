package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;


import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityPig extends EntityAnimal {

    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(EntityPig.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> BOOST_TIME = EntityDataManager.createKey(EntityPig.class, DataSerializers.VARINT);
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(new Item[] { Items.CARROT, Items.POTATO, Items.BEETROOT});
    private boolean boosting;
    private int boostTime;
    private int totalBoostTime;

    public EntityPig(World world) {
        super(world);
        this.setSize(0.9F, 0.9F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.CARROT_ON_A_STICK, false));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, false, EntityPig.TEMPTATION_ITEMS));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    public boolean canBeSteered() {
        Entity entity = this.getControllingPassenger();

        if (!(entity instanceof EntityPlayer)) {
            return false;
        } else {
            EntityPlayer entityhuman = (EntityPlayer) entity;

            return entityhuman.getHeldItemMainhand().getItem() == Items.CARROT_ON_A_STICK || entityhuman.getHeldItemOffhand().getItem() == Items.CARROT_ON_A_STICK;
        }
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityPig.BOOST_TIME.equals(datawatcherobject) && this.world.isRemote) {
            this.boosting = true;
            this.boostTime = 0;
            this.totalBoostTime = ((Integer) this.dataManager.get(EntityPig.BOOST_TIME)).intValue();
        }

        super.notifyDataManagerChange(datawatcherobject);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityPig.SADDLED, Boolean.valueOf(false));
        this.dataManager.register(EntityPig.BOOST_TIME, Integer.valueOf(0));
    }

    public static void registerFixesPig(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityPig.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Saddle", this.getSaddled());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setSaddled(nbttagcompound.getBoolean("Saddle"));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PIG_DEATH;
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        if (!super.processInteract(entityhuman, enumhand)) {
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (itemstack.getItem() == Items.NAME_TAG) {
                itemstack.interactWithEntity(entityhuman, (EntityLivingBase) this, enumhand);
                return true;
            } else if (this.getSaddled() && !this.isBeingRidden()) {
                if (!this.world.isRemote) {
                    entityhuman.startRiding(this);
                }

                return true;
            } else if (itemstack.getItem() == Items.SADDLE) {
                itemstack.interactWithEntity(entityhuman, (EntityLivingBase) this, enumhand);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void onDeath(DamageSource damagesource) {
        // super.die(damagesource); // CraftBukkit - Moved to end
        if (!this.world.isRemote) {
            if (this.getSaddled()) {
                this.dropItem(Items.SADDLE, 1);
            }

        }
        super.onDeath(damagesource); // CraftBukkit - Moved from above
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_PIG;
    }

    public boolean getSaddled() {
        return ((Boolean) this.dataManager.get(EntityPig.SADDLED)).booleanValue();
    }

    public void setSaddled(boolean flag) {
        if (flag) {
            this.dataManager.set(EntityPig.SADDLED, Boolean.valueOf(true));
        } else {
            this.dataManager.set(EntityPig.SADDLED, Boolean.valueOf(false));
        }

    }

    public void onStruckByLightning(EntityLightningBolt entitylightning) {
        if (!this.world.isRemote && !this.isDead) {
            EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);

            // Paper start
            if (CraftEventFactory.callEntityZapEvent(this, entitylightning, entitypigzombie).isCancelled()) {
                return;
            }
            // Paper end

            // CraftBukkit start
            if (CraftEventFactory.callPigZapEvent(this, entitylightning, entitypigzombie).isCancelled()) {
                return;
            }
            // CraftBukkit end

            entitypigzombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitypigzombie.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitypigzombie.setCustomNameTag(this.getCustomNameTag());
                entitypigzombie.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }

            // CraftBukkit - added a reason for spawning this creature
            this.world.addEntity(entitypigzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.LIGHTNING);
            this.setDead();
        }
    }

    public void travel(float f, float f1, float f2) {
        Entity entity = this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);

        if (this.isBeingRidden() && this.canBeSteered()) {
            this.rotationYaw = entity.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
            if (this.boosting && this.boostTime++ > this.totalBoostTime) {
                this.boosting = false;
            }

            if (this.canPassengerSteer()) {
                float f3 = (float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.225F;

                if (this.boosting) {
                    f3 += f3 * 1.15F * MathHelper.sin((float) this.boostTime / (float) this.totalBoostTime * 3.1415927F);
                }

                this.setAIMoveSpeed(f3);
                super.travel(0.0F, 0.0F, 1.0F);
            } else {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.travel(f, f1, f2);
        }
    }

    public boolean boost() {
        if (this.boosting) {
            return false;
        } else {
            this.boosting = true;
            this.boostTime = 0;
            this.totalBoostTime = this.getRNG().nextInt(841) + 140;
            this.getDataManager().set(EntityPig.BOOST_TIME, Integer.valueOf(this.totalBoostTime));
            return true;
        }
    }

    public EntityPig createChild(EntityAgeable entityageable) {
        return new EntityPig(this.world);
    }

    public boolean isBreedingItem(ItemStack itemstack) {
        return EntityPig.TEMPTATION_ITEMS.contains(itemstack.getItem());
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.createChild(entityageable);
    }
}

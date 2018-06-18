package net.minecraft.entity.monster;

import java.util.Calendar;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityCombustEvent;

public abstract class AbstractSkeleton extends EntityMob implements IRangedAttackMob {

    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(AbstractSkeleton.class, DataSerializers.BOOLEAN);
    private final EntityAIAttackRangedBow<AbstractSkeleton> aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false) { // CraftBukkit - decompile error
        public void resetTask() {
            super.resetTask();
            AbstractSkeleton.this.setSwingingArms(false);
        }

        public void startExecuting() {
            super.startExecuting();
            AbstractSkeleton.this.setSwingingArms(true);
        }
    };

    public AbstractSkeleton(World world) {
        super(world);
        this.setSize(0.6F, 1.99F);
        this.setCombatTask();
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractSkeleton.SWINGING_ARMS, Boolean.valueOf(false));
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    abstract SoundEvent getStepSound();

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public void onLivingUpdate() {
        if (this.world.isDaytime() && !this.world.isRemote) {
            float f = this.getBrightness();
            BlockPos blockposition = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(blockposition)) {
                boolean flag = true;
                ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (!itemstack.isEmpty()) {
                    if (itemstack.isItemStackDamageable()) {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemstack);
                            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    // CraftBukkit start
                    EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        this.setFire(event.getDuration());
                    }
                    // CraftBukkit end
                }
            }
        }

        super.onLivingUpdate();
    }

    public void updateRidden() {
        super.updateRidden();
        if (this.getRidingEntity() instanceof EntityCreature) {
            EntityCreature entitycreature = (EntityCreature) this.getRidingEntity();

            this.renderYawOffset = entitycreature.renderYawOffset;
        }

    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultydamagescaler) {
        super.setEquipmentBasedOnDifficulty(difficultydamagescaler);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        groupdataentity = super.onInitialSpawn(difficultydamagescaler, groupdataentity);
        this.setEquipmentBasedOnDifficulty(difficultydamagescaler);
        this.setEnchantmentBasedOnDifficulty(difficultydamagescaler);
        this.setCombatTask();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficultydamagescaler.getClampedAdditionalDifficulty());
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = this.world.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return groupdataentity;
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask((EntityAIBase) this.aiAttackOnCollide);
            this.tasks.removeTask((EntityAIBase) this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();

            if (itemstack.getItem() == Items.BOW) {
                byte b0 = 20;

                if (this.world.getDifficulty() != EnumDifficulty.HARD) {
                    b0 = 40;
                }

                this.aiArrowAttack.setAttackCooldown(b0);
                this.tasks.addTask(4, this.aiArrowAttack);
            } else {
                this.tasks.addTask(4, this.aiAttackOnCollide);
            }

        }
    }

    public void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f) {
        EntityArrow entityarrow = this.getArrow(f);
        double d0 = entityliving.posX - this.posX;
        double d1 = entityliving.getEntityBoundingBox().minY + (double) (entityliving.height / 3.0F) - entityarrow.posY;
        double d2 = entityliving.posZ - this.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);

        entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
        // CraftBukkit start
        org.bukkit.event.entity.EntityShootBowEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callEntityShootBowEvent(this, this.getHeldItemMainhand(), this.getHeldItemOffhand(), entityarrow, 0.8F); // Paper
        if (event.isCancelled()) {
            event.getProjectile().remove();
            return;
        }

        if (event.getProjectile() == entityarrow.getBukkitEntity()) {
            world.spawnEntity(entityarrow);
        }
        // CraftBukkit end
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        // this.world.addEntity(entityarrow); // CraftBukkit - moved up
    }

    protected EntityArrow getArrow(float f) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);

        entitytippedarrow.setEnchantmentEffectsFromEntity((EntityLivingBase) this, f);
        return entitytippedarrow;
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setCombatTask();
    }

    public void setItemStackToSlot(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        super.setItemStackToSlot(enumitemslot, itemstack);
        if (!this.world.isRemote && enumitemslot == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }

    }

    public float getEyeHeight() {
        return 1.74F;
    }

    public double getYOffset() {
        return -0.6D;
    }

    public void setSwingingArms(boolean flag) {
        this.dataManager.set(AbstractSkeleton.SWINGING_ARMS, Boolean.valueOf(flag));
    }
}

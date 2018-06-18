package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityLlama.a;
import net.minecraft.server.EntityLlama.b;
import net.minecraft.server.EntityLlama.c;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityLlama extends AbstractChestHorse implements IRangedAttackMob {

    private static final DataParameter<Integer> DATA_STRENGTH_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_VARIANT_ID = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
    private boolean didSpit;
    @Nullable
    private EntityLlama caravanHead;
    @Nullable
    private EntityLlama caravanTail;

    public EntityLlama(World world) {
        super(world);
        this.setSize(0.9F, 1.87F);
    }

    public void setStrength(int i) {
        this.dataManager.set(EntityLlama.DATA_STRENGTH_ID, Integer.valueOf(Math.max(1, Math.min(5, i))));
    }

    private void setRandomStrength() {
        int i = this.rand.nextFloat() < 0.04F ? 5 : 3;

        this.setStrength(1 + this.rand.nextInt(i));
    }

    public int getStrength() {
        return ((Integer) this.dataManager.get(EntityLlama.DATA_STRENGTH_ID)).intValue();
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Variant", this.getVariant());
        nbttagcompound.setInteger("Strength", this.getStrength());
        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            nbttagcompound.setTag("DecorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.setStrength(nbttagcompound.getInteger("Strength"));
        super.readEntityFromNBT(nbttagcompound);
        this.setVariant(nbttagcompound.getInteger("Variant"));
        if (nbttagcompound.hasKey("DecorItem", 10)) {
            this.horseChest.setInventorySlotContents(1, new ItemStack(nbttagcompound.getCompoundTag("DecorItem")));
        }

        this.updateHorseSlots();
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.tasks.addTask(2, new EntityAILlamaFollowCaravan(this, 2.0999999046325684D));
        this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
        this.tasks.addTask(3, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityLlama.c(this));
        this.targetTasks.addTask(2, new EntityLlama.a(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityLlama.DATA_STRENGTH_ID, Integer.valueOf(0));
        this.dataManager.register(EntityLlama.DATA_COLOR_ID, Integer.valueOf(-1));
        this.dataManager.register(EntityLlama.DATA_VARIANT_ID, Integer.valueOf(0));
    }

    public int getVariant() {
        return MathHelper.clamp(((Integer) this.dataManager.get(EntityLlama.DATA_VARIANT_ID)).intValue(), 0, 3);
    }

    public void setVariant(int i) {
        this.dataManager.set(EntityLlama.DATA_VARIANT_ID, Integer.valueOf(i));
    }

    protected int getInventorySize() {
        return this.hasChest() ? 2 + 3 * this.getInventoryColumns() : super.getInventorySize();
    }

    public void updatePassenger(Entity entity) {
        if (this.isPassenger(entity)) {
            float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float f1 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            float f2 = 0.3F;

            entity.setPosition(this.posX + (double) (0.3F * f1), this.posY + this.getMountedYOffset() + entity.getYOffset(), this.posZ - (double) (0.3F * f));
        }
    }

    public double getMountedYOffset() {
        return (double) this.height * 0.67D;
    }

    public boolean canBeSteered() {
        return false;
    }

    protected boolean handleEating(EntityPlayer entityhuman, ItemStack itemstack) {
        byte b0 = 0;
        byte b1 = 0;
        float f = 0.0F;
        boolean flag = false;
        Item item = itemstack.getItem();

        if (item == Items.WHEAT) {
            b0 = 10;
            b1 = 3;
            f = 2.0F;
        } else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            b0 = 90;
            b1 = 6;
            f = 10.0F;
            if (this.isTame() && this.getGrowingAge() == 0) {
                flag = true;
                this.setInLove(entityhuman);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f);
            flag = true;
        }

        if (this.isChild() && b0 > 0) {
            this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            if (!this.world.isRemote) {
                this.addGrowth(b0);
            }

            flag = true;
        }

        if (b1 > 0 && (flag || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
            flag = true;
            if (!this.world.isRemote) {
                this.increaseTemper(b1);
            }
        }

        if (flag && !this.isSilent()) {
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }

        return flag;
    }

    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0F || this.isEatingHaystack();
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        Object object = super.onInitialSpawn(difficultydamagescaler, groupdataentity);

        this.setRandomStrength();
        int i;

        if (object instanceof EntityLlama.b) {
            i = ((EntityLlama.b) object).a;
        } else {
            i = this.rand.nextInt(4);
            object = new EntityLlama.b(i, null);
        }

        this.setVariant(i);
        return (IEntityLivingData) object;
    }

    protected SoundEvent getAngrySound() {
        return SoundEvents.ENTITY_LLAMA_ANGRY;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_LLAMA_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LLAMA_DEATH;
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(SoundEvents.ENTITY_LLAMA_STEP, 0.15F, 1.0F);
    }

    protected void playChestEquipSound() {
        this.playSound(SoundEvents.ENTITY_LLAMA_CHEST, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    public void makeMad() {
        SoundEvent soundeffect = this.getAngrySound();

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_LLAMA;
    }

    public int getInventoryColumns() {
        return this.getStrength();
    }

    public boolean wearsArmor() {
        return true;
    }

    public boolean isArmor(ItemStack itemstack) {
        return itemstack.getItem() == Item.getItemFromBlock(Blocks.CARPET);
    }

    public boolean canBeSaddled() {
        return false;
    }

    public void onInventoryChanged(IInventory iinventory) {
        EnumDyeColor enumcolor = this.getColor();

        super.onInventoryChanged(iinventory);
        EnumDyeColor enumcolor1 = this.getColor();

        if (this.ticksExisted > 20 && enumcolor1 != null && enumcolor1 != enumcolor) {
            this.playSound(SoundEvents.ENTITY_LLAMA_SWAG, 0.5F, 1.0F);
        }

    }

    protected void updateHorseSlots() {
        if (!this.world.isRemote) {
            super.updateHorseSlots();
            this.setColorByItem(this.horseChest.getStackInSlot(1));
        }
    }

    private void setColor(@Nullable EnumDyeColor enumcolor) {
        this.dataManager.set(EntityLlama.DATA_COLOR_ID, Integer.valueOf(enumcolor == null ? -1 : enumcolor.getMetadata()));
    }

    private void setColorByItem(ItemStack itemstack) {
        if (this.isArmor(itemstack)) {
            this.setColor(EnumDyeColor.byMetadata(itemstack.getMetadata()));
        } else {
            this.setColor((EnumDyeColor) null);
        }

    }

    @Nullable
    public EnumDyeColor getColor() {
        int i = ((Integer) this.dataManager.get(EntityLlama.DATA_COLOR_ID)).intValue();

        return i == -1 ? null : EnumDyeColor.byMetadata(i);
    }

    public int getMaxTemper() {
        return 30;
    }

    public boolean canMateWith(EntityAnimal entityanimal) {
        return entityanimal != this && entityanimal instanceof EntityLlama && this.canMate() && ((EntityLlama) entityanimal).canMate();
    }

    public EntityLlama createChild(EntityAgeable entityageable) {
        EntityLlama entityllama = new EntityLlama(this.world);

        this.setOffspringAttributes(entityageable, (AbstractHorse) entityllama);
        EntityLlama entityllama1 = (EntityLlama) entityageable;
        int i = this.rand.nextInt(Math.max(this.getStrength(), entityllama1.getStrength())) + 1;

        if (this.rand.nextFloat() < 0.03F) {
            ++i;
        }

        entityllama.setStrength(i);
        entityllama.setVariant(this.rand.nextBoolean() ? this.getVariant() : entityllama1.getVariant());
        return entityllama;
    }

    private void spit(EntityLivingBase entityliving) {
        EntityLlamaSpit entityllamaspit = new EntityLlamaSpit(this.world, this);
        double d0 = entityliving.posX - this.posX;
        double d1 = entityliving.getEntityBoundingBox().minY + (double) (entityliving.height / 3.0F) - entityllamaspit.posY;
        double d2 = entityliving.posZ - this.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;

        entityllamaspit.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        this.world.spawnEntity(entityllamaspit);
        this.didSpit = true;
    }

    private void setDidSpit(boolean flag) {
        this.didSpit = flag;
    }

    public void fall(float f, float f1) {
        int i = MathHelper.ceil((f * 0.5F - 3.0F) * f1);

        if (i > 0) {
            if (f >= 6.0F) {
                this.attackEntityFrom(DamageSource.FALL, (float) i);
                if (this.isBeingRidden()) {
                    Iterator iterator = this.getRecursivePassengers().iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        entity.attackEntityFrom(DamageSource.FALL, (float) i);
                    }
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

    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }

        this.caravanHead = null;
    }

    public void joinCaravan(EntityLlama entityllama) {
        this.caravanHead = entityllama;
        this.caravanHead.caravanTail = this;
    }

    public boolean hasCaravanTrail() {
        return this.caravanTail != null;
    }

    public boolean inCaravan() { return this.inCaravan(); } // Paper - OBFHELPER
    public boolean inCaravan() {
        return this.caravanHead != null;
    }

    @Nullable
    public EntityLlama getCaravanHead() {
        return this.caravanHead;
    }

    protected double followLeashSpeed() {
        return 2.0D;
    }

    protected void followMother() {
        if (!this.inCaravan() && this.isChild()) {
            super.followMother();
        }

    }

    public boolean canEatGrass() {
        return false;
    }

    public void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f) {
        this.spit(entityliving);
    }

    public void setSwingingArms(boolean flag) {}

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.createChild(entityageable);
    }

    static class a extends EntityAINearestAttackableTarget<EntityWolf> {

        public a(EntityLlama entityllama) {
            super(entityllama, EntityWolf.class, 16, false, true, (Predicate) null);
        }

        public boolean shouldExecute() {
            if (super.shouldExecute() && this.targetEntity != null && !((EntityWolf) this.targetEntity).isTamed()) {
                return true;
            } else {
                this.taskOwner.setAttackTarget((EntityLivingBase) null);
                return false;
            }
        }

        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.25D;
        }
    }

    static class c extends EntityAIHurtByTarget {

        public c(EntityLlama entityllama) {
            super(entityllama, false, new Class[0]);
        }

        public boolean shouldContinueExecuting() {
            if (this.taskOwner instanceof EntityLlama) {
                EntityLlama entityllama = (EntityLlama) this.taskOwner;

                if (entityllama.didSpit) {
                    entityllama.setDidSpit(false);
                    return false;
                }
            }

            return super.shouldContinueExecuting();
        }
    }

    static class b implements IEntityLivingData {

        public int a;

        private b(int i) {
            this.a = i;
        }

        b(int i, Object object) {
            this(i);
        }
    }
}

package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPigZombie extends EntityZombie {

    private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = (new AttributeModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, 0)).setSaved(false);
    public int angerLevel;
    private int randomSoundDelay;
    private UUID angerTargetUUID;

    public EntityPigZombie(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    public void setRevengeTarget(@Nullable EntityLivingBase entityliving) {
        super.setRevengeTarget(entityliving);
        if (entityliving != null) {
            this.angerTargetUUID = entityliving.getUniqueID();
        }

    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityPigZombie.AIHurtByAggressor(this));
        this.targetTasks.addTask(2, new EntityPigZombie.AITargetAggressor(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(EntityPigZombie.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    protected void updateAITasks() {
        IAttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (this.isAngry()) {
            if (!this.isChild() && !attributeinstance.hasModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER)) {
                attributeinstance.applyModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER);
            }

            --this.angerLevel;
        } else if (attributeinstance.hasModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER)) {
            attributeinstance.removeModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER);
        }

        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
            this.playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getRevengeTarget() == null) {
            EntityPlayer entityhuman = this.world.getPlayerEntityByUUID(this.angerTargetUUID);

            this.setRevengeTarget((EntityLivingBase) entityhuman);
            this.attackingPlayer = entityhuman;
            this.recentlyHit = this.getRevengeTimer();
        }

        super.updateAITasks();
    }

    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), (Entity) this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
    }

    public static void registerFixesPigZombie(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityPigZombie.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short) this.angerLevel);
        if (this.angerTargetUUID != null) {
            nbttagcompound.setString("HurtBy", this.angerTargetUUID.toString());
        } else {
            nbttagcompound.setString("HurtBy", "");
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
        String s = nbttagcompound.getString("HurtBy");

        if (!s.isEmpty()) {
            this.angerTargetUUID = UUID.fromString(s);
            EntityPlayer entityhuman = this.world.getPlayerEntityByUUID(this.angerTargetUUID);

            this.setRevengeTarget((EntityLivingBase) entityhuman);
            if (entityhuman != null) {
                this.attackingPlayer = entityhuman;
                this.recentlyHit = this.getRevengeTimer();
            }
        }

    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            Entity entity = damagesource.getTrueSource();

            if (entity instanceof EntityPlayer) {
                this.becomeAngryAt(entity);
            }

            return super.attackEntityFrom(damagesource, f);
        }
    }

    private void becomeAngryAt(Entity entity) {
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);
        if (entity instanceof EntityLivingBase) {
            this.setRevengeTarget((EntityLivingBase) entity);
        }

    }

    public boolean isAngry() {
        return this.angerLevel > 0;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_PIG_DEATH;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE_PIGMAN;
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        return false;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultydamagescaler) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }

    protected ItemStack getSkullDrop() {
        return ItemStack.EMPTY;
    }

    public boolean isPreventingPlayerRest(EntityPlayer entityhuman) {
        return this.isAngry();
    }

    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer> {

        public AITargetAggressor(EntityPigZombie entitypigzombie) {
            super(entitypigzombie, EntityPlayer.class, true);
        }

        public boolean shouldExecute() {
            return ((EntityPigZombie) this.taskOwner).isAngry() && super.shouldExecute();
        }
    }

    static class AIHurtByAggressor extends EntityAIHurtByTarget {

        public AIHurtByAggressor(EntityPigZombie entitypigzombie) {
            super(entitypigzombie, true, new Class[0]);
        }

        protected void setEntityAttackTarget(EntityCreature entitycreature, EntityLivingBase entityliving) {
            super.setEntityAttackTarget(entitycreature, entityliving);
            if (entitycreature instanceof EntityPigZombie) {
                ((EntityPigZombie) entitycreature).becomeAngryAt((Entity) entityliving);
            }

        }
    }
}

package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySkeletonHorse extends AbstractHorse {

    private final EntityAISkeletonRiders skeletonTrapAI = new EntityAISkeletonRiders(this);
    private boolean skeletonTrap;
    private int skeletonTrapTime;

    public EntitySkeletonHorse(World world) {
        super(world);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
        this.getEntityAttribute(EntitySkeletonHorse.JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }

    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        super.getHurtSound(damagesource);
        return SoundEvents.ENTITY_SKELETON_HORSE_HURT;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public double getMountedYOffset() {
        return super.getMountedYOffset() - 0.1875D;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SKELETON_HORSE;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.isTrap() && this.skeletonTrapTime++ >= 18000) {
            this.setDead();
        }

    }

    public static void registerFixesSkeletonHorse(DataFixer dataconvertermanager) {
        AbstractHorse.registerFixesAbstractHorse(dataconvertermanager, EntitySkeletonHorse.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("SkeletonTrap", this.isTrap());
        nbttagcompound.setInteger("SkeletonTrapTime", this.skeletonTrapTime);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setTrap(nbttagcompound.getBoolean("SkeletonTrap"));
        this.skeletonTrapTime = nbttagcompound.getInteger("SkeletonTrapTime");
    }

    public boolean isTrap() {
        return this.skeletonTrap;
    }

    public void setTrap(boolean flag) {
        if (flag != this.skeletonTrap) {
            this.skeletonTrap = flag;
            if (flag) {
                this.tasks.addTask(1, this.skeletonTrapAI);
            } else {
                this.tasks.removeTask((EntityAIBase) this.skeletonTrapAI);
            }

        }
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        boolean flag = !itemstack.isEmpty();

        if (flag && itemstack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(entityhuman, enumhand);
        } else if (!this.isTame()) {
            return false;
        } else if (this.isChild()) {
            return super.processInteract(entityhuman, enumhand);
        } else if (entityhuman.isSneaking()) {
            this.openGUI(entityhuman);
            return true;
        } else if (this.isBeingRidden()) {
            return super.processInteract(entityhuman, enumhand);
        } else {
            if (flag) {
                if (itemstack.getItem() == Items.SADDLE && !this.isHorseSaddled()) {
                    this.openGUI(entityhuman);
                    return true;
                }

                if (itemstack.interactWithEntity(entityhuman, (EntityLivingBase) this, enumhand)) {
                    return true;
                }
            }

            this.mountTo(entityhuman);
            return true;
        }
    }
}

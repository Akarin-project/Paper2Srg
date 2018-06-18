package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieHorse extends AbstractHorse {

    public EntityZombieHorse(World world) {
        super(world);
    }

    public static void registerFixesZombieHorse(DataFixer dataconvertermanager) {
        AbstractHorse.registerFixesAbstractHorse(dataconvertermanager, EntityZombieHorse.class);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
        this.getEntityAttribute(EntityZombieHorse.JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_ZOMBIE_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        super.getHurtSound(damagesource);
        return SoundEvents.ENTITY_ZOMBIE_HORSE_HURT;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE_HORSE;
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
                if (!this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE) {
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

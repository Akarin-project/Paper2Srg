package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMagmaCube extends EntitySlime {

    public EntityMagmaCube(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    public static void registerFixesMagmaCube(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityMagmaCube.class);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }

    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), (Entity) this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
    }

    protected void setSize(int i, boolean flag) {
        super.setSlimeSize(i, flag);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue((double) (i * 3));
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.FLAME;
    }

    protected EntitySlime createInstance() {
        return new EntityMagmaCube(this.world);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return this.isSmallSlime() ? LootTableList.EMPTY : LootTableList.ENTITIES_MAGMA_CUBE;
    }

    public boolean isBurning() {
        return false;
    }

    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.9F;
    }

    protected void jump() {
        this.motionY = (double) (0.42F + (float) this.getSlimeSize() * 0.1F);
        this.isAirBorne = true;
    }

    protected void handleJumpLava() {
        this.motionY = (double) (0.22F + (float) this.getSlimeSize() * 0.05F);
        this.isAirBorne = true;
    }

    public void fall(float f, float f1) {}

    protected boolean canDamagePlayer() {
        return true;
    }

    protected int getAttackStrength() {
        return super.getAttackStrength() + 2;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_HURT : SoundEvents.ENTITY_MAGMACUBE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_DEATH : SoundEvents.ENTITY_MAGMACUBE_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_SQUISH : SoundEvents.ENTITY_MAGMACUBE_SQUISH;
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.ENTITY_MAGMACUBE_JUMP;
    }
}

package net.minecraft.util;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;

public class DamageSource {

    public static final DamageSource IN_FIRE = (new DamageSource("inFire")).setFireDamage();
    public static final DamageSource LIGHTNING_BOLT = new DamageSource("lightningBolt");
    public static final DamageSource ON_FIRE = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
    public static final DamageSource LAVA = (new DamageSource("lava")).setFireDamage();
    public static final DamageSource HOT_FLOOR = (new DamageSource("hotFloor")).setFireDamage();
    public static final DamageSource IN_WALL = (new DamageSource("inWall")).setDamageBypassesArmor();
    public static final DamageSource CRAMMING = (new DamageSource("cramming")).setDamageBypassesArmor();
    public static final DamageSource DROWN = (new DamageSource("drown")).setDamageBypassesArmor();
    public static final DamageSource STARVE = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource CACTUS = new DamageSource("cactus");
    public static final DamageSource FALL = (new DamageSource("fall")).setDamageBypassesArmor();
    public static final DamageSource FLY_INTO_WALL = (new DamageSource("flyIntoWall")).setDamageBypassesArmor();
    public static final DamageSource OUT_OF_WORLD = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static final DamageSource GENERIC = (new DamageSource("generic")).setDamageBypassesArmor();
    public static final DamageSource MAGIC = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
    public static final DamageSource WITHER = (new DamageSource("wither")).setDamageBypassesArmor();
    public static final DamageSource ANVIL = new DamageSource("anvil");
    public static final DamageSource FALLING_BLOCK = new DamageSource("fallingBlock");
    public static final DamageSource DRAGON_BREATH = (new DamageSource("dragonBreath")).setDamageBypassesArmor();
    public static final DamageSource FIREWORKS = (new DamageSource("fireworks")).setExplosion();
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;
    private boolean damageIsAbsolute;
    private float hungerDamage = 0.1F;
    private boolean fireDamage;
    private boolean projectile;
    private boolean difficultyScaled;
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;
    // CraftBukkit start
    private boolean sweep;

    public boolean isSweep() {
        return sweep;
    }

    public DamageSource sweep() {
        this.sweep = true;
        return this;
    }
    // CraftBukkit end

    public static DamageSource causeMobDamage(EntityLivingBase entityliving) {
        return new EntityDamageSource("mob", entityliving);
    }

    public static DamageSource causeIndirectDamage(Entity entity, EntityLivingBase entityliving) {
        return new EntityDamageSourceIndirect("mob", entity, entityliving);
    }

    public static DamageSource causePlayerDamage(EntityPlayer entityhuman) {
        return new EntityDamageSource("player", entityhuman);
    }

    public static DamageSource causeArrowDamage(EntityArrow entityarrow, @Nullable Entity entity) {
        return (new EntityDamageSourceIndirect("arrow", entityarrow, entity)).setProjectile();
    }

    public static DamageSource causeFireballDamage(EntityFireball entityfireball, @Nullable Entity entity) {
        return entity == null ? (new EntityDamageSourceIndirect("onFire", entityfireball, entityfireball)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", entityfireball, entity)).setFireDamage().setProjectile();
    }

    public static DamageSource causeThrownDamage(Entity entity, @Nullable Entity entity1) {
        return (new EntityDamageSourceIndirect("thrown", entity, entity1)).setProjectile();
    }

    public static DamageSource causeIndirectMagicDamage(Entity entity, @Nullable Entity entity1) {
        return (new EntityDamageSourceIndirect("indirectMagic", entity, entity1)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource causeThornsDamage(Entity entity) {
        return (new EntityDamageSource("thorns", entity)).setIsThornsDamage().setMagicDamage();
    }

    public static DamageSource causeExplosionDamage(@Nullable Explosion explosion) {
        return explosion != null && explosion.getExplosivePlacedBy() != null ? (new EntityDamageSource("explosion.player", explosion.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    public static DamageSource causeExplosionDamage(@Nullable EntityLivingBase entityliving) {
        return entityliving != null ? (new EntityDamageSource("explosion.player", entityliving)).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    public boolean isProjectile() {
        return this.projectile;
    }

    public DamageSource setProjectile() {
        this.projectile = true;
        return this;
    }

    public boolean isExplosion() {
        return this.explosion;
    }

    public DamageSource setExplosion() {
        this.explosion = true;
        return this;
    }

    public boolean isUnblockable() {
        return this.isUnblockable;
    }

    public float getHungerDamage() {
        return this.hungerDamage;
    }

    public boolean canHarmInCreative() {
        return this.isDamageAllowedInCreativeMode;
    }

    public boolean isDamageAbsolute() {
        return this.damageIsAbsolute;
    }

    protected DamageSource(String s) {
        this.damageType = s;
    }

    @Nullable
    public Entity getImmediateSource() {
        return this.getTrueSource();
    }

    @Nullable
    public Entity getTrueSource() {
        return null;
    }

    protected DamageSource setDamageBypassesArmor() {
        this.isUnblockable = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setDamageAllowedInCreativeMode() {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }

    protected DamageSource setDamageIsAbsolute() {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setFireDamage() {
        this.fireDamage = true;
        return this;
    }

    public ITextComponent getDeathMessage(EntityLivingBase entityliving) {
        EntityLivingBase entityliving1 = entityliving.getAttackingEntity();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";

        return entityliving1 != null && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] { entityliving.getDisplayName(), entityliving1.getDisplayName()}) : new TextComponentTranslation(s, new Object[] { entityliving.getDisplayName()});
    }

    public boolean isFireDamage() {
        return this.fireDamage;
    }

    public String getDamageType() {
        return this.damageType;
    }

    public DamageSource setDifficultyScaled() {
        this.difficultyScaled = true;
        return this;
    }

    public boolean isDifficultyScaled() {
        return this.difficultyScaled;
    }

    public boolean isMagicDamage() {
        return this.magicDamage;
    }

    public DamageSource setMagicDamage() {
        this.magicDamage = true;
        return this;
    }

    public boolean isCreativePlayer() {
        Entity entity = this.getTrueSource();

        return entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode;
    }

    @Nullable
    public Vec3d getDamageLocation() {
        return null;
    }
}

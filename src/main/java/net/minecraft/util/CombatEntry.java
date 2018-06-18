package net.minecraft.util;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;

public class CombatEntry {

    private final DamageSource damageSrc;
    private final int time;
    private final float damage;
    private final float health;
    private final String fallSuffix;
    private final float fallDistance;

    public CombatEntry(DamageSource damagesource, int i, float f, float f1, String s, float f2) {
        this.damageSrc = damagesource;
        this.time = i;
        this.damage = f1;
        this.health = f;
        this.fallSuffix = s;
        this.fallDistance = f2;
    }

    public DamageSource getDamageSrc() {
        return this.damageSrc;
    }

    public float getDamage() {
        return this.damage;
    }

    public boolean isLivingDamageSrc() {
        return this.damageSrc.getTrueSource() instanceof EntityLivingBase;
    }

    @Nullable
    public String getFallSuffix() {
        return this.fallSuffix;
    }

    @Nullable
    public ITextComponent getDamageSrcDisplayName() {
        return this.getDamageSrc().getTrueSource() == null ? null : this.getDamageSrc().getTrueSource().getDisplayName();
    }

    public float getDamageAmount() {
        return this.damageSrc == DamageSource.OUT_OF_WORLD ? Float.MAX_VALUE : this.fallDistance;
    }
}

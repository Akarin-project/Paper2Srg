package net.minecraft.entity.boss.dragon.phase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;


public abstract class PhaseSittingBase extends PhaseBase {

    public PhaseSittingBase(EntityDragon entityenderdragon) {
        super(entityenderdragon);
    }

    public boolean getIsStationary() {
        return true;
    }

    public float getAdjustedDamage(MultiPartEntityPart entitycomplexpart, DamageSource damagesource, float f) {
        if (damagesource.getImmediateSource() instanceof EntityArrow) {
            damagesource.getImmediateSource().setFire(1);
            return 0.0F;
        } else {
            return super.getAdjustedDamage(entitycomplexpart, damagesource, f);
        }
    }
}

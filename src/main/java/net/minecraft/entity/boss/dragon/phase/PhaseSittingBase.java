package net.minecraft.entity.boss.dragon.phase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;


public abstract class PhaseSittingBase extends PhaseBase {

    public PhaseSittingBase(EntityDragon entityenderdragon) {
        super(entityenderdragon);
    }

    public boolean func_188654_a() {
        return true;
    }

    public float func_188656_a(MultiPartEntityPart entitycomplexpart, DamageSource damagesource, float f) {
        if (damagesource.func_76364_f() instanceof EntityArrow) {
            damagesource.func_76364_f().func_70015_d(1);
            return 0.0F;
        } else {
            return super.func_188656_a(entitycomplexpart, damagesource, f);
        }
    }
}

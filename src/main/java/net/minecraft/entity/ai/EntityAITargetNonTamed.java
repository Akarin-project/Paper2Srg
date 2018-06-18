package net.minecraft.entity.ai;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

    private final EntityTameable tameable;

    public EntityAITargetNonTamed(EntityTameable entitytameableanimal, Class<T> oclass, boolean flag, Predicate<? super T> predicate) {
        super(entitytameableanimal, oclass, 10, flag, false, predicate);
        this.tameable = entitytameableanimal;
    }

    public boolean shouldExecute() {
        return !this.tameable.isTamed() && super.shouldExecute();
    }
}

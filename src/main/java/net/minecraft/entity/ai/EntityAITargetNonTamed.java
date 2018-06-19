package net.minecraft.entity.ai;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

    private final EntityTameable field_75310_g;

    public EntityAITargetNonTamed(EntityTameable entitytameableanimal, Class<T> oclass, boolean flag, Predicate<? super T> predicate) {
        super(entitytameableanimal, oclass, 10, flag, false, predicate);
        this.field_75310_g = entitytameableanimal;
    }

    public boolean func_75250_a() {
        return !this.field_75310_g.func_70909_n() && super.func_75250_a();
    }
}

package net.minecraft.entity.ai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;


public class EntityAIWatchClosest2 extends EntityAIWatchClosest {

    public EntityAIWatchClosest2(EntityLiving entityinsentient, Class<? extends Entity> oclass, float f, float f1) {
        super(entityinsentient, oclass, f, f1);
        this.func_75248_a(3);
    }
}

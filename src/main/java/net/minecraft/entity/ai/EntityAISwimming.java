package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;


public class EntityAISwimming extends EntityAIBase {

    private final EntityLiving field_75373_a;

    public EntityAISwimming(EntityLiving entityinsentient) {
        this.field_75373_a = entityinsentient;
        if (entityinsentient.func_130014_f_().paperConfig.nerfedMobsShouldJump) entityinsentient.goalFloat = this; // Paper
        this.func_75248_a(4);
        if (entityinsentient.func_70661_as() instanceof PathNavigateGround) {
            ((PathNavigateGround) entityinsentient.func_70661_as()).func_179693_d(true);
        } else if (entityinsentient.func_70661_as() instanceof PathNavigateFlying) {
            ((PathNavigateFlying) entityinsentient.func_70661_as()).func_192877_c(true);
        }

    }

    public boolean validConditions() { return this.func_75250_a(); } // Paper - OBFHELPER
    public boolean func_75250_a() {
        return this.field_75373_a.func_70090_H() || this.field_75373_a.func_180799_ab();
    }

    public void update() { this.func_75246_d(); } // Paper - OBFHELPER
    public void func_75246_d() {
        if (this.field_75373_a.func_70681_au().nextFloat() < 0.8F) {
            this.field_75373_a.func_70683_ar().func_75660_a();
        }

    }
}

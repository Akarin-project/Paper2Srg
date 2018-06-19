package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;


public class EntityAIOwnerHurtByTarget extends EntityAITarget {

    EntityTameable field_75316_a;
    EntityLivingBase field_75315_b;
    private int field_142051_e;

    public EntityAIOwnerHurtByTarget(EntityTameable entitytameableanimal) {
        super(entitytameableanimal, false);
        this.field_75316_a = entitytameableanimal;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        if (!this.field_75316_a.func_70909_n()) {
            return false;
        } else {
            EntityLivingBase entityliving = this.field_75316_a.func_70902_q();

            if (entityliving == null) {
                return false;
            } else {
                this.field_75315_b = entityliving.func_70643_av();
                int i = entityliving.func_142015_aE();

                return i != this.field_142051_e && this.func_75296_a(this.field_75315_b, false) && this.field_75316_a.func_142018_a(this.field_75315_b, entityliving);
            }
        }
    }

    public void func_75249_e() {
        this.field_75299_d.setGoalTarget(this.field_75315_b, org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true); // CraftBukkit - reason
        EntityLivingBase entityliving = this.field_75316_a.func_70902_q();

        if (entityliving != null) {
            this.field_142051_e = entityliving.func_142015_aE();
        }

        super.func_75249_e();
    }
}

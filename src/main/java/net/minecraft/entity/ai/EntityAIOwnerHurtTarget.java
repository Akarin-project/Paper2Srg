package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;


public class EntityAIOwnerHurtTarget extends EntityAITarget {

    EntityTameable field_75314_a;
    EntityLivingBase field_75313_b;
    private int field_142050_e;

    public EntityAIOwnerHurtTarget(EntityTameable entitytameableanimal) {
        super(entitytameableanimal, false);
        this.field_75314_a = entitytameableanimal;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        if (!this.field_75314_a.func_70909_n()) {
            return false;
        } else {
            EntityLivingBase entityliving = this.field_75314_a.func_70902_q();

            if (entityliving == null) {
                return false;
            } else {
                this.field_75313_b = entityliving.func_110144_aD();
                int i = entityliving.func_142013_aG();

                return i != this.field_142050_e && this.func_75296_a(this.field_75313_b, false) && this.field_75314_a.func_142018_a(this.field_75313_b, entityliving);
            }
        }
    }

    public void func_75249_e() {
        this.field_75299_d.setGoalTarget(this.field_75313_b, org.bukkit.event.entity.EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true); // CraftBukkit - reason
        EntityLivingBase entityliving = this.field_75314_a.func_70902_q();

        if (entityliving != null) {
            this.field_142050_e = entityliving.func_142013_aG();
        }

        super.func_75249_e();
    }
}

package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;


public class EntityAIDefendVillage extends EntityAITarget {

    EntityIronGolem field_75305_a;
    EntityLivingBase field_75304_b;

    public EntityAIDefendVillage(EntityIronGolem entityirongolem) {
        super(entityirongolem, false, true);
        this.field_75305_a = entityirongolem;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        Village village = this.field_75305_a.func_70852_n();

        if (village == null) {
            return false;
        } else {
            this.field_75304_b = village.func_75571_b((EntityLivingBase) this.field_75305_a);
            if (this.field_75304_b instanceof EntityCreeper) {
                return false;
            } else if (this.func_75296_a(this.field_75304_b, false)) {
                return true;
            } else if (this.field_75299_d.func_70681_au().nextInt(20) == 0) {
                this.field_75304_b = village.func_82685_c((EntityLivingBase) this.field_75305_a);
                return this.func_75296_a(this.field_75304_b, false);
            } else {
                return false;
            }
        }
    }

    public void func_75249_e() {
        this.field_75305_a.setGoalTarget(this.field_75304_b, org.bukkit.event.entity.EntityTargetEvent.TargetReason.DEFEND_VILLAGE, true); // CraftBukkit - reason
        super.func_75249_e();
    }
}

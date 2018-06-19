package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {

    private final boolean field_75312_a;
    private int field_142052_b;
    private final Class<?>[] field_179447_c;

    public EntityAIHurtByTarget(EntityCreature entitycreature, boolean flag, Class<?>... aclass) {
        super(entitycreature, true);
        this.field_75312_a = flag;
        this.field_179447_c = aclass;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        int i = this.field_75299_d.func_142015_aE();
        EntityLivingBase entityliving = this.field_75299_d.func_70643_av();

        return i != this.field_142052_b && entityliving != null && this.func_75296_a(entityliving, false);
    }

    public void func_75249_e() {
        this.field_75299_d.setGoalTarget(this.field_75299_d.func_70643_av(), org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY, true); // CraftBukkit - reason
        this.field_188509_g = this.field_75299_d.func_70638_az();
        this.field_142052_b = this.field_75299_d.func_142015_aE();
        this.field_188510_h = 300;
        if (this.field_75312_a) {
            this.func_190105_f();
        }

        super.func_75249_e();
    }

    protected void func_190105_f() {
        double d0 = this.func_111175_f();
        List list = this.field_75299_d.field_70170_p.func_72872_a(this.field_75299_d.getClass(), (new AxisAlignedBB(this.field_75299_d.field_70165_t, this.field_75299_d.field_70163_u, this.field_75299_d.field_70161_v, this.field_75299_d.field_70165_t + 1.0D, this.field_75299_d.field_70163_u + 1.0D, this.field_75299_d.field_70161_v + 1.0D)).func_72314_b(d0, 10.0D, d0));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityCreature entitycreature = (EntityCreature) iterator.next();

            if (this.field_75299_d != entitycreature && entitycreature.func_70638_az() == null && (!(this.field_75299_d instanceof EntityTameable) || ((EntityTameable) this.field_75299_d).func_70902_q() == ((EntityTameable) entitycreature).func_70902_q()) && !entitycreature.func_184191_r(this.field_75299_d.func_70643_av())) {
                boolean flag = false;
                Class[] aclass = this.field_179447_c;
                int i = aclass.length;

                for (int j = 0; j < i; ++j) {
                    Class oclass = aclass[j];

                    if (entitycreature.getClass() == oclass) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    this.func_179446_a(entitycreature, this.field_75299_d.func_70643_av());
                }
            }
        }

    }

    protected void func_179446_a(EntityCreature entitycreature, EntityLivingBase entityliving) {
        entitycreature.setGoalTarget(entityliving, org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY, true); // CraftBukkit - reason
    }
}

package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitySenses {

    EntityLiving field_75526_a;
    List<Entity> field_75524_b = Lists.newArrayList();
    List<Entity> field_75525_c = Lists.newArrayList();

    public EntitySenses(EntityLiving entityinsentient) {
        this.field_75526_a = entityinsentient;
    }

    public void func_75523_a() {
        this.field_75524_b.clear();
        this.field_75525_c.clear();
    }

    public boolean func_75522_a(Entity entity) {
        if (this.field_75524_b.contains(entity)) {
            return true;
        } else if (this.field_75525_c.contains(entity)) {
            return false;
        } else {
            this.field_75526_a.field_70170_p.field_72984_F.func_76320_a("canSee");
            boolean flag = this.field_75526_a.func_70685_l(entity);

            this.field_75526_a.field_70170_p.field_72984_F.func_76319_b();
            if (flag) {
                this.field_75524_b.add(entity);
            } else {
                this.field_75525_c.add(entity);
            }

            return flag;
        }
    }
}

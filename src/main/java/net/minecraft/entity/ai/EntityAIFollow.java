package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;

public class EntityAIFollow extends EntityAIBase {

    private final EntityLiving field_192372_a;
    private final Predicate<EntityLiving> field_192373_b;
    private EntityLiving field_192374_c;
    private final double field_192375_d;
    private final PathNavigate field_192376_e;
    private int field_192377_f;
    private final float field_192378_g;
    private float field_192379_h;
    private final float field_192380_i;

    public EntityAIFollow(final EntityLiving entityinsentient, double d0, float f, float f1) {
        this.field_192372_a = entityinsentient;
        this.field_192373_b = new Predicate<EntityLiving>() {
            @Override
            public boolean apply(@Nullable EntityLiving entity) {
                return entity != null && entityinsentient.getClass() != entity.getClass();
            }
        };
        this.field_192375_d = d0;
        this.field_192376_e = entityinsentient.func_70661_as();
        this.field_192378_g = f;
        this.field_192380_i = f1;
        this.func_75248_a(3);
        if (!(entityinsentient.func_70661_as() instanceof PathNavigateGround) && !(entityinsentient.func_70661_as() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    @Override
    public boolean func_75250_a() {
        List list = this.field_192372_a.field_70170_p.func_175647_a(EntityLiving.class, this.field_192372_a.func_174813_aQ().func_186662_g(this.field_192380_i), this.field_192373_b);

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLiving entityinsentient = (EntityLiving) iterator.next();

                if (!entityinsentient.func_82150_aj()) {
                    this.field_192374_c = entityinsentient;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean func_75253_b() {
        return this.field_192374_c != null && !this.field_192376_e.func_75500_f() && this.field_192372_a.func_70068_e(this.field_192374_c) > this.field_192378_g * this.field_192378_g;
    }

    @Override
    public void func_75249_e() {
        this.field_192377_f = 0;
        this.field_192379_h = this.field_192372_a.func_184643_a(PathNodeType.WATER);
        this.field_192372_a.func_184644_a(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void func_75251_c() {
        this.field_192374_c = null;
        this.field_192376_e.func_75499_g();
        this.field_192372_a.func_184644_a(PathNodeType.WATER, this.field_192379_h);
    }

    @Override
    public void func_75246_d() {
        if (this.field_192374_c != null && !this.field_192372_a.func_110167_bD()) {
            this.field_192372_a.func_70671_ap().func_75651_a(this.field_192374_c, 10.0F, this.field_192372_a.func_70646_bf());
            if (--this.field_192377_f <= 0) {
                this.field_192377_f = 10;
                double d0 = this.field_192372_a.field_70165_t - this.field_192374_c.field_70165_t;
                double d1 = this.field_192372_a.field_70163_u - this.field_192374_c.field_70163_u;
                double d2 = this.field_192372_a.field_70161_v - this.field_192374_c.field_70161_v;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > this.field_192378_g * this.field_192378_g) {
                    this.field_192376_e.func_75497_a(this.field_192374_c, this.field_192375_d);
                } else {
                    this.field_192376_e.func_75499_g();
                    EntityLookHelper controllerlook = this.field_192374_c.func_70671_ap();

                    if (d3 <= this.field_192378_g || controllerlook.func_180423_e() == this.field_192372_a.field_70165_t && controllerlook.func_180422_f() == this.field_192372_a.field_70163_u && controllerlook.func_180421_g() == this.field_192372_a.field_70161_v) {
                        double d4 = this.field_192374_c.field_70165_t - this.field_192372_a.field_70165_t;
                        double d5 = this.field_192374_c.field_70161_v - this.field_192372_a.field_70161_v;

                        this.field_192376_e.func_75492_a(this.field_192372_a.field_70165_t - d4, this.field_192372_a.field_70163_u, this.field_192372_a.field_70161_v - d5, this.field_192375_d);
                    }

                }
            }
        }
    }
}

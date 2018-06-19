package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase {

    private final Predicate<Entity> field_179509_a;
    protected EntityCreature field_75380_a;
    private final double field_75378_b;
    private final double field_75379_c;
    protected T field_75376_d;
    private final float field_179508_f;
    private Path field_75374_f;
    private final PathNavigate field_75375_g;
    private final Class<T> field_181064_i;
    private final Predicate<? super T> field_179510_i;

    public EntityAIAvoidEntity(EntityCreature entitycreature, Class<T> oclass, float f, double d0, double d1) {
        this(entitycreature, oclass, Predicates.alwaysTrue(), f, d0, d1);
    }

    public EntityAIAvoidEntity(EntityCreature entitycreature, Class<T> oclass, Predicate<? super T> predicate, float f, double d0, double d1) {
        this.field_179509_a = new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return entity.func_70089_S() && EntityAIAvoidEntity.this.field_75380_a.func_70635_at().func_75522_a(entity) && !EntityAIAvoidEntity.this.field_75380_a.func_184191_r(entity);
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        };
        this.field_75380_a = entitycreature;
        this.field_181064_i = oclass;
        this.field_179510_i = predicate;
        this.field_179508_f = f;
        this.field_75378_b = d0;
        this.field_75379_c = d1;
        this.field_75375_g = entitycreature.func_70661_as();
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        List list = this.field_75380_a.field_70170_p.func_175647_a(this.field_181064_i, this.field_75380_a.func_174813_aQ().func_72314_b((double) this.field_179508_f, 3.0D, (double) this.field_179508_f), Predicates.and(new Predicate[] { EntitySelectors.field_188444_d, this.field_179509_a, this.field_179510_i}));

        if (list.isEmpty()) {
            return false;
        } else {
            this.field_75376_d = (Entity) list.get(0);
            Vec3d vec3d = RandomPositionGenerator.func_75461_b(this.field_75380_a, 16, 7, new Vec3d(this.field_75376_d.field_70165_t, this.field_75376_d.field_70163_u, this.field_75376_d.field_70161_v));

            if (vec3d == null) {
                return false;
            } else if (this.field_75376_d.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c) < this.field_75376_d.func_70068_e(this.field_75380_a)) {
                return false;
            } else {
                this.field_75374_f = this.field_75375_g.func_75488_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
                return this.field_75374_f != null;
            }
        }
    }

    public boolean func_75253_b() {
        return !this.field_75375_g.func_75500_f();
    }

    public void func_75249_e() {
        this.field_75375_g.func_75484_a(this.field_75374_f, this.field_75378_b);
    }

    public void func_75251_c() {
        this.field_75376_d = null;
    }

    public void func_75246_d() {
        if (this.field_75380_a.func_70068_e(this.field_75376_d) < 49.0D) {
            this.field_75380_a.func_70661_as().func_75489_a(this.field_75379_c);
        } else {
            this.field_75380_a.func_70661_as().func_75489_a(this.field_75378_b);
        }

    }
}

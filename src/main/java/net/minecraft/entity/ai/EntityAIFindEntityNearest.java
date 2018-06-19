package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;

public class EntityAIFindEntityNearest extends EntityAIBase {

    private static final Logger field_179444_a = LogManager.getLogger();
    private final EntityLiving field_179442_b;
    private final Predicate<EntityLivingBase> field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private EntityLivingBase field_179441_e;
    private final Class<? extends EntityLivingBase> field_179439_f;

    public EntityAIFindEntityNearest(EntityLiving entityinsentient, Class<? extends EntityLivingBase> oclass) {
        this.field_179442_b = entityinsentient;
        this.field_179439_f = oclass;
        if (entityinsentient instanceof EntityCreature) {
            EntityAIFindEntityNearest.field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.field_179443_c = new Predicate() {
            public boolean a(@Nullable EntityLivingBase entityliving) {
                double d0 = EntityAIFindEntityNearest.this.func_179438_f();

                if (entityliving.func_70093_af()) {
                    d0 *= 0.800000011920929D;
                }

                return entityliving.func_82150_aj() ? false : ((double) entityliving.func_70032_d(EntityAIFindEntityNearest.this.field_179442_b) > d0 ? false : EntityAITarget.func_179445_a(EntityAIFindEntityNearest.this.field_179442_b, entityliving, false, true));
            }

            public boolean apply(@Nullable Object object) {
                return this.a((EntityLivingBase) object);
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(entityinsentient);
    }

    public boolean func_75250_a() {
        double d0 = this.func_179438_f();
        List list = this.field_179442_b.field_70170_p.func_175647_a(this.field_179439_f, this.field_179442_b.func_174813_aQ().func_72314_b(d0, 4.0D, d0), this.field_179443_c);

        Collections.sort(list, this.field_179440_d);
        if (list.isEmpty()) {
            return false;
        } else {
            this.field_179441_e = (EntityLivingBase) list.get(0);
            return true;
        }
    }

    public boolean func_75253_b() {
        EntityLivingBase entityliving = this.field_179442_b.func_70638_az();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.func_70089_S()) {
            return false;
        } else {
            double d0 = this.func_179438_f();

            return this.field_179442_b.func_70068_e(entityliving) > d0 * d0 ? false : !(entityliving instanceof EntityPlayerMP) || !((EntityPlayerMP) entityliving).field_71134_c.func_73083_d();
        }
    }

    public void func_75249_e() {
        this.field_179442_b.setGoalTarget(this.field_179441_e, org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true); // CraftBukkit - reason
        super.func_75249_e();
    }

    public void func_75251_c() {
        this.field_179442_b.func_70624_b((EntityLivingBase) null);
        super.func_75249_e();
    }

    protected double func_179438_f() {
        IAttributeInstance attributeinstance = this.field_179442_b.func_110148_a(SharedMonsterAttributes.field_111265_b);

        return attributeinstance == null ? 16.0D : attributeinstance.func_111126_e();
    }
}

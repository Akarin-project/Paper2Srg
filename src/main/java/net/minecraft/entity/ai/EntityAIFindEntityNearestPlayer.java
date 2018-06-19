package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase {

    private static final Logger field_179436_a = LogManager.getLogger();
    private final EntityLiving field_179434_b;
    private final Predicate<Entity> field_179435_c;
    private final EntityAINearestAttackableTarget.Sorter field_179432_d;
    private EntityLivingBase field_179433_e;

    public EntityAIFindEntityNearestPlayer(EntityLiving entityinsentient) {
        this.field_179434_b = entityinsentient;
        if (entityinsentient instanceof EntityCreature) {
            EntityAIFindEntityNearestPlayer.field_179436_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.field_179435_c = new Predicate() {
            public boolean a(@Nullable Entity entity) {
                if (!(entity instanceof EntityPlayer)) {
                    return false;
                } else if (((EntityPlayer) entity).field_71075_bZ.field_75102_a) {
                    return false;
                } else {
                    double d0 = EntityAIFindEntityNearestPlayer.this.func_179431_f();

                    if (entity.func_70093_af()) {
                        d0 *= 0.800000011920929D;
                    }

                    if (entity.func_82150_aj()) {
                        float f = ((EntityPlayer) entity).func_82243_bO();

                        if (f < 0.1F) {
                            f = 0.1F;
                        }

                        d0 *= (double) (0.7F * f);
                    }

                    return (double) entity.func_70032_d(EntityAIFindEntityNearestPlayer.this.field_179434_b) > d0 ? false : EntityAITarget.func_179445_a(EntityAIFindEntityNearestPlayer.this.field_179434_b, (EntityLivingBase) entity, false, true);
                }
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        };
        this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(entityinsentient);
    }

    public boolean func_75250_a() {
        double d0 = this.func_179431_f();
        List list = this.field_179434_b.field_70170_p.func_175647_a(EntityPlayer.class, this.field_179434_b.func_174813_aQ().func_72314_b(d0, 4.0D, d0), this.field_179435_c);

        Collections.sort(list, this.field_179432_d);
        if (list.isEmpty()) {
            return false;
        } else {
            this.field_179433_e = (EntityLivingBase) list.get(0);
            return true;
        }
    }

    public boolean func_75253_b() {
        EntityLivingBase entityliving = this.field_179434_b.func_70638_az();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.func_70089_S()) {
            return false;
        } else if (entityliving instanceof EntityPlayer && ((EntityPlayer) entityliving).field_71075_bZ.field_75102_a) {
            return false;
        } else {
            Team scoreboardteambase = this.field_179434_b.func_96124_cp();
            Team scoreboardteambase1 = entityliving.func_96124_cp();

            if (scoreboardteambase != null && scoreboardteambase1 == scoreboardteambase) {
                return false;
            } else {
                double d0 = this.func_179431_f();

                return this.field_179434_b.func_70068_e(entityliving) > d0 * d0 ? false : !(entityliving instanceof EntityPlayerMP) || !((EntityPlayerMP) entityliving).field_71134_c.func_73083_d();
            }
        }
    }

    public void func_75249_e() {
        this.field_179434_b.setGoalTarget(this.field_179433_e, org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true); // CraftBukkit - added reason
        super.func_75249_e();
    }

    public void func_75251_c() {
        this.field_179434_b.func_70624_b((EntityLivingBase) null);
        super.func_75249_e();
    }

    protected double func_179431_f() {
        IAttributeInstance attributeinstance = this.field_179434_b.func_110148_a(SharedMonsterAttributes.field_111265_b);

        return attributeinstance == null ? 16.0D : attributeinstance.func_111126_e();
    }
}

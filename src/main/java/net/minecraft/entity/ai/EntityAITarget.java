package net.minecraft.entity.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.bukkit.event.entity.EntityTargetEvent;

public abstract class EntityAITarget extends EntityAIBase {

    protected final EntityCreature field_75299_d;
    protected boolean field_75297_f;
    private final boolean field_75303_a;
    private int field_75301_b;
    private int field_75302_c;
    private int field_75298_g;
    protected EntityLivingBase field_188509_g;
    protected int field_188510_h;

    public EntityAITarget(EntityCreature entitycreature, boolean flag) {
        this(entitycreature, flag, false);
    }

    public EntityAITarget(EntityCreature entitycreature, boolean flag, boolean flag1) {
        this.field_188510_h = 60;
        this.field_75299_d = entitycreature;
        this.field_75297_f = flag;
        this.field_75303_a = flag1;
    }

    public boolean func_75253_b() {
        EntityLivingBase entityliving = this.field_75299_d.func_70638_az();

        if (entityliving == null) {
            entityliving = this.field_188509_g;
        }

        if (entityliving == null) {
            return false;
        } else if (!entityliving.func_70089_S()) {
            return false;
        } else {
            Team scoreboardteambase = this.field_75299_d.func_96124_cp();
            Team scoreboardteambase1 = entityliving.func_96124_cp();

            if (scoreboardteambase != null && scoreboardteambase1 == scoreboardteambase) {
                return false;
            } else {
                double d0 = this.func_111175_f();

                if (this.field_75299_d.func_70068_e(entityliving) > d0 * d0) {
                    return false;
                } else {
                    if (this.field_75297_f) {
                        if (this.field_75299_d.func_70635_at().func_75522_a(entityliving)) {
                            this.field_75298_g = 0;
                        } else if (++this.field_75298_g > this.field_188510_h) {
                            return false;
                        }
                    }

                    if (entityliving instanceof EntityPlayer && ((EntityPlayer) entityliving).field_71075_bZ.field_75102_a) {
                        return false;
                    } else {
                        this.field_75299_d.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true); // CraftBukkit
                        return true;
                    }
                }
            }
        }
    }

    protected double func_111175_f() {
        IAttributeInstance attributeinstance = this.field_75299_d.func_110148_a(SharedMonsterAttributes.field_111265_b);

        return attributeinstance == null ? 16.0D : attributeinstance.func_111126_e();
    }

    public void func_75249_e() {
        this.field_75301_b = 0;
        this.field_75302_c = 0;
        this.field_75298_g = 0;
    }

    public void func_75251_c() {
        this.field_75299_d.setGoalTarget((EntityLivingBase) null, EntityTargetEvent.TargetReason.FORGOT_TARGET, true); // CraftBukkit
        this.field_188509_g = null;
    }

    public static boolean func_179445_a(EntityLiving entityinsentient, @Nullable EntityLivingBase entityliving, boolean flag, boolean flag1) {
        if (entityliving == null) {
            return false;
        } else if (entityliving == entityinsentient) {
            return false;
        } else if (!entityliving.func_70089_S()) {
            return false;
        } else if (!entityinsentient.func_70686_a(entityliving.getClass())) {
            return false;
        } else if (entityinsentient.func_184191_r(entityliving)) {
            return false;
        } else {
            if (entityinsentient instanceof IEntityOwnable && ((IEntityOwnable) entityinsentient).func_184753_b() != null) {
                if (entityliving instanceof IEntityOwnable && ((IEntityOwnable) entityinsentient).func_184753_b().equals(((IEntityOwnable) entityliving).func_184753_b())) {
                    return false;
                }

                if (entityliving == ((IEntityOwnable) entityinsentient).func_70902_q()) {
                    return false;
                }
            } else if (entityliving instanceof EntityPlayer && !flag && ((EntityPlayer) entityliving).field_71075_bZ.field_75102_a) {
                return false;
            }

            return !flag1 || entityinsentient.func_70635_at().func_75522_a(entityliving);
        }
    }

    protected boolean func_75296_a(@Nullable EntityLivingBase entityliving, boolean flag) {
        if (!func_179445_a(this.field_75299_d, entityliving, flag, this.field_75297_f)) {
            return false;
        } else if (!this.field_75299_d.func_180485_d(new BlockPos(entityliving))) {
            return false;
        } else {
            if (this.field_75303_a) {
                if (--this.field_75302_c <= 0) {
                    this.field_75301_b = 0;
                }

                if (this.field_75301_b == 0) {
                    this.field_75301_b = this.func_75295_a(entityliving) ? 1 : 2;
                }

                if (this.field_75301_b == 2) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean func_75295_a(EntityLivingBase entityliving) {
        this.field_75302_c = 10 + this.field_75299_d.func_70681_au().nextInt(5);
        Path pathentity = this.field_75299_d.func_70661_as().func_75494_a((Entity) entityliving);

        if (pathentity == null) {
            return false;
        } else {
            PathPoint pathpoint = pathentity.func_75870_c();

            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.field_75839_a - MathHelper.func_76128_c(entityliving.field_70165_t);
                int j = pathpoint.field_75838_c - MathHelper.func_76128_c(entityliving.field_70161_v);

                return (double) (i * i + j * j) <= 2.25D;
            }
        }
    }

    public EntityAITarget func_190882_b(int i) {
        this.field_188510_h = i;
        return this;
    }
}

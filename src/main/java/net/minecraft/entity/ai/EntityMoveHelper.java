package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;


public class EntityMoveHelper {

    protected final EntityLiving field_75648_a;
    protected double field_75646_b;
    protected double field_75647_c;
    protected double field_75644_d;
    protected double field_75645_e;
    protected float field_188489_f;
    protected float field_188490_g;
    public EntityMoveHelper.Action field_188491_h;

    public EntityMoveHelper(EntityLiving entityinsentient) {
        this.field_188491_h = EntityMoveHelper.Action.WAIT;
        this.field_75648_a = entityinsentient;
    }

    public boolean func_75640_a() {
        return this.field_188491_h == EntityMoveHelper.Action.MOVE_TO;
    }

    public double func_75638_b() {
        return this.field_75645_e;
    }

    public void func_75642_a(double d0, double d1, double d2, double d3) {
        this.field_75646_b = d0;
        this.field_75647_c = d1;
        this.field_75644_d = d2;
        this.field_75645_e = d3;
        this.field_188491_h = EntityMoveHelper.Action.MOVE_TO;
    }

    public void func_188488_a(float f, float f1) {
        this.field_188491_h = EntityMoveHelper.Action.STRAFE;
        this.field_188489_f = f;
        this.field_188490_g = f1;
        this.field_75645_e = 0.25D;
    }

    public void func_188487_a(EntityMoveHelper controllermove) {
        this.field_188491_h = controllermove.field_188491_h;
        this.field_75646_b = controllermove.field_75646_b;
        this.field_75647_c = controllermove.field_75647_c;
        this.field_75644_d = controllermove.field_75644_d;
        this.field_75645_e = Math.max(controllermove.field_75645_e, 1.0D);
        this.field_188489_f = controllermove.field_188489_f;
        this.field_188490_g = controllermove.field_188490_g;
    }

    public void func_75641_c() {
        float f;

        if (this.field_188491_h == EntityMoveHelper.Action.STRAFE) {
            float f1 = (float) this.field_75648_a.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e();
            float f2 = (float) this.field_75645_e * f1;
            float f3 = this.field_188489_f;
            float f4 = this.field_188490_g;
            float f5 = MathHelper.func_76129_c(f3 * f3 + f4 * f4);

            if (f5 < 1.0F) {
                f5 = 1.0F;
            }

            f5 = f2 / f5;
            f3 *= f5;
            f4 *= f5;
            float f6 = MathHelper.func_76126_a(this.field_75648_a.field_70177_z * 0.017453292F);
            float f7 = MathHelper.func_76134_b(this.field_75648_a.field_70177_z * 0.017453292F);
            float f8 = f3 * f7 - f4 * f6;

            f = f4 * f7 + f3 * f6;
            PathNavigate navigationabstract = this.field_75648_a.func_70661_as();

            if (navigationabstract != null) {
                NodeProcessor pathfinderabstract = navigationabstract.func_189566_q();

                if (pathfinderabstract != null && pathfinderabstract.func_186330_a(this.field_75648_a.field_70170_p, MathHelper.func_76128_c(this.field_75648_a.field_70165_t + (double) f8), MathHelper.func_76128_c(this.field_75648_a.field_70163_u), MathHelper.func_76128_c(this.field_75648_a.field_70161_v + (double) f)) != PathNodeType.WALKABLE) {
                    this.field_188489_f = 1.0F;
                    this.field_188490_g = 0.0F;
                    f2 = f1;
                }
            }

            this.field_75648_a.func_70659_e(f2);
            this.field_75648_a.func_191989_p(this.field_188489_f);
            this.field_75648_a.func_184646_p(this.field_188490_g);
            this.field_188491_h = EntityMoveHelper.Action.WAIT;
        } else if (this.field_188491_h == EntityMoveHelper.Action.MOVE_TO) {
            this.field_188491_h = EntityMoveHelper.Action.WAIT;
            double d0 = this.field_75646_b - this.field_75648_a.field_70165_t;
            double d1 = this.field_75644_d - this.field_75648_a.field_70161_v;
            double d2 = this.field_75647_c - this.field_75648_a.field_70163_u;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D) {
                this.field_75648_a.func_191989_p(0.0F);
                return;
            }

            f = (float) (MathHelper.func_181159_b(d1, d0) * 57.2957763671875D) - 90.0F;
            this.field_75648_a.field_70177_z = this.func_75639_a(this.field_75648_a.field_70177_z, f, 90.0F);
            this.field_75648_a.func_70659_e((float) (this.field_75645_e * this.field_75648_a.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
            if (d2 > (double) this.field_75648_a.field_70138_W && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.field_75648_a.field_70130_N)) {
                this.field_75648_a.func_70683_ar().func_75660_a();
                this.field_188491_h = EntityMoveHelper.Action.JUMPING;
            }
        } else if (this.field_188491_h == EntityMoveHelper.Action.JUMPING) {
            this.field_75648_a.func_70659_e((float) (this.field_75645_e * this.field_75648_a.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
            if (this.field_75648_a.field_70122_E) {
                this.field_188491_h = EntityMoveHelper.Action.WAIT;
            }
        } else {
            this.field_75648_a.func_191989_p(0.0F);
        }

    }

    protected float func_75639_a(float f, float f1, float f2) {
        float f3 = MathHelper.func_76142_g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        float f4 = f + f3;

        if (f4 < 0.0F) {
            f4 += 360.0F;
        } else if (f4 > 360.0F) {
            f4 -= 360.0F;
        }

        return f4;
    }

    public double func_179917_d() {
        return this.field_75646_b;
    }

    public double func_179919_e() {
        return this.field_75647_c;
    }

    public double func_179918_f() {
        return this.field_75644_d;
    }

    public static enum Action {

        WAIT, MOVE_TO, STRAFE, JUMPING;

        private Action() {}
    }
}

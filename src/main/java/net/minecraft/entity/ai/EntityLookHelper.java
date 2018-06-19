package net.minecraft.entity.ai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;


public class EntityLookHelper {

    private final EntityLiving field_75659_a;
    private float field_75657_b;
    private float field_75658_c;
    private boolean field_75655_d;
    private double field_75656_e;
    private double field_75653_f;
    private double field_75654_g;

    public EntityLookHelper(EntityLiving entityinsentient) {
        this.field_75659_a = entityinsentient;
    }

    public void func_75651_a(Entity entity, float f, float f1) {
        this.field_75656_e = entity.field_70165_t;
        if (entity instanceof EntityLivingBase) {
            this.field_75653_f = entity.field_70163_u + (double) entity.func_70047_e();
        } else {
            this.field_75653_f = (entity.func_174813_aQ().field_72338_b + entity.func_174813_aQ().field_72337_e) / 2.0D;
        }

        this.field_75654_g = entity.field_70161_v;
        this.field_75657_b = f;
        this.field_75658_c = f1;
        this.field_75655_d = true;
    }

    public void func_75650_a(double d0, double d1, double d2, float f, float f1) {
        this.field_75656_e = d0;
        this.field_75653_f = d1;
        this.field_75654_g = d2;
        this.field_75657_b = f;
        this.field_75658_c = f1;
        this.field_75655_d = true;
    }

    public void func_75649_a() {
        this.field_75659_a.field_70125_A = 0.0F;
        if (this.field_75655_d) {
            this.field_75655_d = false;
            double d0 = this.field_75656_e - this.field_75659_a.field_70165_t;
            double d1 = this.field_75653_f - (this.field_75659_a.field_70163_u + (double) this.field_75659_a.func_70047_e());
            double d2 = this.field_75654_g - this.field_75659_a.field_70161_v;
            double d3 = (double) MathHelper.func_76133_a(d0 * d0 + d2 * d2);
            float f = (float) (MathHelper.func_181159_b(d2, d0) * 57.2957763671875D) - 90.0F;
            float f1 = (float) (-(MathHelper.func_181159_b(d1, d3) * 57.2957763671875D));

            this.field_75659_a.field_70125_A = this.func_75652_a(this.field_75659_a.field_70125_A, f1, this.field_75658_c);
            this.field_75659_a.field_70759_as = this.func_75652_a(this.field_75659_a.field_70759_as, f, this.field_75657_b);
        } else {
            this.field_75659_a.field_70759_as = this.func_75652_a(this.field_75659_a.field_70759_as, this.field_75659_a.field_70761_aq, 10.0F);
        }

        float f2 = MathHelper.func_76142_g(this.field_75659_a.field_70759_as - this.field_75659_a.field_70761_aq);

        if (!this.field_75659_a.func_70661_as().func_75500_f()) {
            if (f2 < -75.0F) {
                this.field_75659_a.field_70759_as = this.field_75659_a.field_70761_aq - 75.0F;
            }

            if (f2 > 75.0F) {
                this.field_75659_a.field_70759_as = this.field_75659_a.field_70761_aq + 75.0F;
            }
        }

    }

    private float func_75652_a(float f, float f1, float f2) {
        float f3 = MathHelper.func_76142_g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public boolean func_180424_b() {
        return this.field_75655_d;
    }

    public double func_180423_e() {
        return this.field_75656_e;
    }

    public double func_180422_f() {
        return this.field_75653_f;
    }

    public double func_180421_g() {
        return this.field_75654_g;
    }
}

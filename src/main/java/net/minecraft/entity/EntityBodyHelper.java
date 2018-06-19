package net.minecraft.entity;
import net.minecraft.util.math.MathHelper;


public class EntityBodyHelper {

    private final EntityLivingBase field_75668_a;
    private int field_75666_b;
    private float field_75667_c;

    public EntityBodyHelper(EntityLivingBase entityliving) {
        this.field_75668_a = entityliving;
    }

    public void func_75664_a() {
        double d0 = this.field_75668_a.field_70165_t - this.field_75668_a.field_70169_q;
        double d1 = this.field_75668_a.field_70161_v - this.field_75668_a.field_70166_s;

        if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D) {
            this.field_75668_a.field_70761_aq = this.field_75668_a.field_70177_z;
            this.field_75668_a.field_70759_as = this.func_75665_a(this.field_75668_a.field_70761_aq, this.field_75668_a.field_70759_as, 75.0F);
            this.field_75667_c = this.field_75668_a.field_70759_as;
            this.field_75666_b = 0;
        } else {
            if (this.field_75668_a.func_184188_bt().isEmpty() || !(this.field_75668_a.func_184188_bt().get(0) instanceof EntityLiving)) {
                float f = 75.0F;

                if (Math.abs(this.field_75668_a.field_70759_as - this.field_75667_c) > 15.0F) {
                    this.field_75666_b = 0;
                    this.field_75667_c = this.field_75668_a.field_70759_as;
                } else {
                    ++this.field_75666_b;
                    boolean flag = true;

                    if (this.field_75666_b > 10) {
                        f = Math.max(1.0F - (float) (this.field_75666_b - 10) / 10.0F, 0.0F) * 75.0F;
                    }
                }

                this.field_75668_a.field_70761_aq = this.func_75665_a(this.field_75668_a.field_70759_as, this.field_75668_a.field_70761_aq, f);
            }

        }
    }

    private float func_75665_a(float f, float f1, float f2) {
        float f3 = MathHelper.func_76142_g(f - f1);

        if (f3 < -f2) {
            f3 = -f2;
        }

        if (f3 >= f2) {
            f3 = f2;
        }

        return f - f3;
    }
}

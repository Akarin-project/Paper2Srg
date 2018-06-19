package net.minecraft.entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public abstract class EntityFlying extends EntityLiving {

    public EntityFlying(World world) {
        super(world);
    }

    public void func_180430_e(float f, float f1) {}

    protected void func_184231_a(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {}

    public void func_191986_a(float f, float f1, float f2) {
        if (this.func_70090_H()) {
            this.func_191958_b(f, f1, f2, 0.02F);
            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.800000011920929D;
            this.field_70181_x *= 0.800000011920929D;
            this.field_70179_y *= 0.800000011920929D;
        } else if (this.func_180799_ab()) {
            this.func_191958_b(f, f1, f2, 0.02F);
            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.5D;
            this.field_70181_x *= 0.5D;
            this.field_70179_y *= 0.5D;
        } else {
            float f3 = 0.91F;

            if (this.field_70122_E) {
                f3 = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v))).func_177230_c().field_149765_K * 0.91F;
            }

            float f4 = 0.16277136F / (f3 * f3 * f3);

            this.func_191958_b(f, f1, f2, this.field_70122_E ? 0.1F * f4 : 0.02F);
            f3 = 0.91F;
            if (this.field_70122_E) {
                f3 = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v))).func_177230_c().field_149765_K * 0.91F;
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= (double) f3;
            this.field_70181_x *= (double) f3;
            this.field_70179_y *= (double) f3;
        }

        this.field_184618_aE = this.field_70721_aZ;
        double d0 = this.field_70165_t - this.field_70169_q;
        double d1 = this.field_70161_v - this.field_70166_s;
        float f5 = MathHelper.func_76133_a(d0 * d0 + d1 * d1) * 4.0F;

        if (f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.field_70721_aZ += (f5 - this.field_70721_aZ) * 0.4F;
        this.field_184619_aG += this.field_70721_aZ;
    }

    public boolean func_70617_f_() {
        return false;
    }
}

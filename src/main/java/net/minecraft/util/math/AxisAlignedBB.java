package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;

public class AxisAlignedBB {

    public final double field_72340_a;
    public final double field_72338_b;
    public final double field_72339_c;
    public final double field_72336_d;
    public final double field_72337_e;
    public final double field_72334_f;

    public AxisAlignedBB(double d0, double d1, double d2, double d3, double d4, double d5) {
        this.field_72340_a = Math.min(d0, d3);
        this.field_72338_b = Math.min(d1, d4);
        this.field_72339_c = Math.min(d2, d5);
        this.field_72336_d = Math.max(d0, d3);
        this.field_72337_e = Math.max(d1, d4);
        this.field_72334_f = Math.max(d2, d5);
    }

    public AxisAlignedBB(BlockPos blockposition) {
        this((double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p(), (double) (blockposition.func_177958_n() + 1), (double) (blockposition.func_177956_o() + 1), (double) (blockposition.func_177952_p() + 1));
    }

    public AxisAlignedBB(BlockPos blockposition, BlockPos blockposition1) {
        this((double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p(), (double) blockposition1.func_177958_n(), (double) blockposition1.func_177956_o(), (double) blockposition1.func_177952_p());
    }

    public AxisAlignedBB func_186666_e(double d0) {
        return new AxisAlignedBB(this.field_72340_a, this.field_72338_b, this.field_72339_c, this.field_72336_d, d0, this.field_72334_f);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof AxisAlignedBB)) {
            return false;
        } else {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB) object;

            return Double.compare(axisalignedbb.field_72340_a, this.field_72340_a) != 0 ? false : (Double.compare(axisalignedbb.field_72338_b, this.field_72338_b) != 0 ? false : (Double.compare(axisalignedbb.field_72339_c, this.field_72339_c) != 0 ? false : (Double.compare(axisalignedbb.field_72336_d, this.field_72336_d) != 0 ? false : (Double.compare(axisalignedbb.field_72337_e, this.field_72337_e) != 0 ? false : Double.compare(axisalignedbb.field_72334_f, this.field_72334_f) == 0))));
        }
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.field_72340_a);
        int j = (int) (i ^ i >>> 32);

        i = Double.doubleToLongBits(this.field_72338_b);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.field_72339_c);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.field_72336_d);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.field_72337_e);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.field_72334_f);
        j = 31 * j + (int) (i ^ i >>> 32);
        return j;
    }

    public AxisAlignedBB func_191195_a(double d0, double d1, double d2) {
        double d3 = this.field_72340_a;
        double d4 = this.field_72338_b;
        double d5 = this.field_72339_c;
        double d6 = this.field_72336_d;
        double d7 = this.field_72337_e;
        double d8 = this.field_72334_f;

        if (d0 < 0.0D) {
            d3 -= d0;
        } else if (d0 > 0.0D) {
            d6 -= d0;
        }

        if (d1 < 0.0D) {
            d4 -= d1;
        } else if (d1 > 0.0D) {
            d7 -= d1;
        }

        if (d2 < 0.0D) {
            d5 -= d2;
        } else if (d2 > 0.0D) {
            d8 -= d2;
        }

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB func_72321_a(double d0, double d1, double d2) {
        double d3 = this.field_72340_a;
        double d4 = this.field_72338_b;
        double d5 = this.field_72339_c;
        double d6 = this.field_72336_d;
        double d7 = this.field_72337_e;
        double d8 = this.field_72334_f;

        if (d0 < 0.0D) {
            d3 += d0;
        } else if (d0 > 0.0D) {
            d6 += d0;
        }

        if (d1 < 0.0D) {
            d4 += d1;
        } else if (d1 > 0.0D) {
            d7 += d1;
        }

        if (d2 < 0.0D) {
            d5 += d2;
        } else if (d2 > 0.0D) {
            d8 += d2;
        }

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB func_72314_b(double d0, double d1, double d2) {
        double d3 = this.field_72340_a - d0;
        double d4 = this.field_72338_b - d1;
        double d5 = this.field_72339_c - d2;
        double d6 = this.field_72336_d + d0;
        double d7 = this.field_72337_e + d1;
        double d8 = this.field_72334_f + d2;

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB func_186662_g(double d0) {
        return this.func_72314_b(d0, d0, d0);
    }

    public AxisAlignedBB func_191500_a(AxisAlignedBB axisalignedbb) {
        double d0 = Math.max(this.field_72340_a, axisalignedbb.field_72340_a);
        double d1 = Math.max(this.field_72338_b, axisalignedbb.field_72338_b);
        double d2 = Math.max(this.field_72339_c, axisalignedbb.field_72339_c);
        double d3 = Math.min(this.field_72336_d, axisalignedbb.field_72336_d);
        double d4 = Math.min(this.field_72337_e, axisalignedbb.field_72337_e);
        double d5 = Math.min(this.field_72334_f, axisalignedbb.field_72334_f);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB func_111270_a(AxisAlignedBB axisalignedbb) {
        double d0 = Math.min(this.field_72340_a, axisalignedbb.field_72340_a);
        double d1 = Math.min(this.field_72338_b, axisalignedbb.field_72338_b);
        double d2 = Math.min(this.field_72339_c, axisalignedbb.field_72339_c);
        double d3 = Math.max(this.field_72336_d, axisalignedbb.field_72336_d);
        double d4 = Math.max(this.field_72337_e, axisalignedbb.field_72337_e);
        double d5 = Math.max(this.field_72334_f, axisalignedbb.field_72334_f);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB func_72317_d(double d0, double d1, double d2) {
        return new AxisAlignedBB(this.field_72340_a + d0, this.field_72338_b + d1, this.field_72339_c + d2, this.field_72336_d + d0, this.field_72337_e + d1, this.field_72334_f + d2);
    }

    public AxisAlignedBB func_186670_a(BlockPos blockposition) {
        return new AxisAlignedBB(this.field_72340_a + (double) blockposition.func_177958_n(), this.field_72338_b + (double) blockposition.func_177956_o(), this.field_72339_c + (double) blockposition.func_177952_p(), this.field_72336_d + (double) blockposition.func_177958_n(), this.field_72337_e + (double) blockposition.func_177956_o(), this.field_72334_f + (double) blockposition.func_177952_p());
    }

    public AxisAlignedBB func_191194_a(Vec3d vec3d) {
        return this.func_72317_d(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    public double func_72316_a(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.field_72337_e > this.field_72338_b && axisalignedbb.field_72338_b < this.field_72337_e && axisalignedbb.field_72334_f > this.field_72339_c && axisalignedbb.field_72339_c < this.field_72334_f) {
            double d1;

            if (d0 > 0.0D && axisalignedbb.field_72336_d <= this.field_72340_a) {
                d1 = this.field_72340_a - axisalignedbb.field_72336_d;
                if (d1 < d0) {
                    d0 = d1;
                }
            } else if (d0 < 0.0D && axisalignedbb.field_72340_a >= this.field_72336_d) {
                d1 = this.field_72336_d - axisalignedbb.field_72340_a;
                if (d1 > d0) {
                    d0 = d1;
                }
            }

            return d0;
        } else {
            return d0;
        }
    }

    public double func_72323_b(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.field_72336_d > this.field_72340_a && axisalignedbb.field_72340_a < this.field_72336_d && axisalignedbb.field_72334_f > this.field_72339_c && axisalignedbb.field_72339_c < this.field_72334_f) {
            double d1;

            if (d0 > 0.0D && axisalignedbb.field_72337_e <= this.field_72338_b) {
                d1 = this.field_72338_b - axisalignedbb.field_72337_e;
                if (d1 < d0) {
                    d0 = d1;
                }
            } else if (d0 < 0.0D && axisalignedbb.field_72338_b >= this.field_72337_e) {
                d1 = this.field_72337_e - axisalignedbb.field_72338_b;
                if (d1 > d0) {
                    d0 = d1;
                }
            }

            return d0;
        } else {
            return d0;
        }
    }

    public double func_72322_c(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.field_72336_d > this.field_72340_a && axisalignedbb.field_72340_a < this.field_72336_d && axisalignedbb.field_72337_e > this.field_72338_b && axisalignedbb.field_72338_b < this.field_72337_e) {
            double d1;

            if (d0 > 0.0D && axisalignedbb.field_72334_f <= this.field_72339_c) {
                d1 = this.field_72339_c - axisalignedbb.field_72334_f;
                if (d1 < d0) {
                    d0 = d1;
                }
            } else if (d0 < 0.0D && axisalignedbb.field_72339_c >= this.field_72334_f) {
                d1 = this.field_72334_f - axisalignedbb.field_72339_c;
                if (d1 > d0) {
                    d0 = d1;
                }
            }

            return d0;
        } else {
            return d0;
        }
    }

    public final boolean intersects(AxisAlignedBB intersecting) { return this.func_72326_a(intersecting); } // Paper - OBFHELPER
    public boolean func_72326_a(AxisAlignedBB axisalignedbb) {
        return this.func_186668_a(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c, axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f);
    }

    public boolean func_186668_a(double d0, double d1, double d2, double d3, double d4, double d5) {
        return this.field_72340_a < d3 && this.field_72336_d > d0 && this.field_72338_b < d4 && this.field_72337_e > d1 && this.field_72339_c < d5 && this.field_72334_f > d2;
    }

    public boolean func_72318_a(Vec3d vec3d) {
        return vec3d.field_72450_a > this.field_72340_a && vec3d.field_72450_a < this.field_72336_d ? (vec3d.field_72448_b > this.field_72338_b && vec3d.field_72448_b < this.field_72337_e ? vec3d.field_72449_c > this.field_72339_c && vec3d.field_72449_c < this.field_72334_f : false) : false;
    }

    public double func_72320_b() {
        double d0 = this.field_72336_d - this.field_72340_a;
        double d1 = this.field_72337_e - this.field_72338_b;
        double d2 = this.field_72334_f - this.field_72339_c;

        return (d0 + d1 + d2) / 3.0D;
    }

    public AxisAlignedBB func_186664_h(double d0) {
        return this.func_186662_g(-d0);
    }

    @Nullable
    public RayTraceResult func_72327_a(Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = this.func_186671_a(this.field_72340_a, vec3d, vec3d1);
        EnumFacing enumdirection = EnumFacing.WEST;
        Vec3d vec3d3 = this.func_186671_a(this.field_72336_d, vec3d, vec3d1);

        if (vec3d3 != null && this.func_186661_a(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.EAST;
        }

        vec3d3 = this.func_186663_b(this.field_72338_b, vec3d, vec3d1);
        if (vec3d3 != null && this.func_186661_a(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.DOWN;
        }

        vec3d3 = this.func_186663_b(this.field_72337_e, vec3d, vec3d1);
        if (vec3d3 != null && this.func_186661_a(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.UP;
        }

        vec3d3 = this.func_186665_c(this.field_72339_c, vec3d, vec3d1);
        if (vec3d3 != null && this.func_186661_a(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.NORTH;
        }

        vec3d3 = this.func_186665_c(this.field_72334_f, vec3d, vec3d1);
        if (vec3d3 != null && this.func_186661_a(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.SOUTH;
        }

        return vec3d2 == null ? null : new RayTraceResult(vec3d2, enumdirection);
    }

    @VisibleForTesting
    boolean func_186661_a(Vec3d vec3d, @Nullable Vec3d vec3d1, Vec3d vec3d2) {
        return vec3d1 == null || vec3d.func_72436_e(vec3d2) < vec3d.func_72436_e(vec3d1);
    }

    @Nullable
    @VisibleForTesting
    Vec3d func_186671_a(double d0, Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = vec3d.func_72429_b(vec3d1, d0);

        return vec3d2 != null && this.func_186660_b(vec3d2) ? vec3d2 : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d func_186663_b(double d0, Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = vec3d.func_72435_c(vec3d1, d0);

        return vec3d2 != null && this.func_186667_c(vec3d2) ? vec3d2 : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d func_186665_c(double d0, Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = vec3d.func_72434_d(vec3d1, d0);

        return vec3d2 != null && this.func_186669_d(vec3d2) ? vec3d2 : null;
    }

    @VisibleForTesting
    public boolean func_186660_b(Vec3d vec3d) {
        return vec3d.field_72448_b >= this.field_72338_b && vec3d.field_72448_b <= this.field_72337_e && vec3d.field_72449_c >= this.field_72339_c && vec3d.field_72449_c <= this.field_72334_f;
    }

    @VisibleForTesting
    public boolean func_186667_c(Vec3d vec3d) {
        return vec3d.field_72450_a >= this.field_72340_a && vec3d.field_72450_a <= this.field_72336_d && vec3d.field_72449_c >= this.field_72339_c && vec3d.field_72449_c <= this.field_72334_f;
    }

    @VisibleForTesting
    public boolean func_186669_d(Vec3d vec3d) {
        return vec3d.field_72450_a >= this.field_72340_a && vec3d.field_72450_a <= this.field_72336_d && vec3d.field_72448_b >= this.field_72338_b && vec3d.field_72448_b <= this.field_72337_e;
    }

    public String toString() {
        return "box[" + this.field_72340_a + ", " + this.field_72338_b + ", " + this.field_72339_c + " -> " + this.field_72336_d + ", " + this.field_72337_e + ", " + this.field_72334_f + "]";
    }
}

package net.minecraft.util.math;

import javax.annotation.Nullable;

public class Vec3d {

    public static final Vec3d field_186680_a = new Vec3d(0.0D, 0.0D, 0.0D);
    public final double field_72450_a;
    public final double field_72448_b;
    public final double field_72449_c;

    public Vec3d(double d0, double d1, double d2) {
        if (d0 == -0.0D) {
            d0 = 0.0D;
        }

        if (d1 == -0.0D) {
            d1 = 0.0D;
        }

        if (d2 == -0.0D) {
            d2 = 0.0D;
        }

        this.field_72450_a = d0;
        this.field_72448_b = d1;
        this.field_72449_c = d2;
    }

    public Vec3d(Vec3i baseblockposition) {
        this((double) baseblockposition.func_177958_n(), (double) baseblockposition.func_177956_o(), (double) baseblockposition.func_177952_p());
    }

    public Vec3d func_72444_a(Vec3d vec3d) {
        return new Vec3d(vec3d.field_72450_a - this.field_72450_a, vec3d.field_72448_b - this.field_72448_b, vec3d.field_72449_c - this.field_72449_c);
    }

    public Vec3d func_72432_b() {
        double d0 = (double) MathHelper.func_76133_a(this.field_72450_a * this.field_72450_a + this.field_72448_b * this.field_72448_b + this.field_72449_c * this.field_72449_c);

        return d0 < 1.0E-4D ? Vec3d.field_186680_a : new Vec3d(this.field_72450_a / d0, this.field_72448_b / d0, this.field_72449_c / d0);
    }

    public double func_72430_b(Vec3d vec3d) {
        return this.field_72450_a * vec3d.field_72450_a + this.field_72448_b * vec3d.field_72448_b + this.field_72449_c * vec3d.field_72449_c;
    }

    public Vec3d func_178788_d(Vec3d vec3d) {
        return this.func_178786_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    public Vec3d func_178786_a(double d0, double d1, double d2) {
        return this.func_72441_c(-d0, -d1, -d2);
    }

    public Vec3d func_178787_e(Vec3d vec3d) {
        return this.func_72441_c(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    public Vec3d func_72441_c(double d0, double d1, double d2) {
        return new Vec3d(this.field_72450_a + d0, this.field_72448_b + d1, this.field_72449_c + d2);
    }

    public double func_72438_d(Vec3d vec3d) {
        double d0 = vec3d.field_72450_a - this.field_72450_a;
        double d1 = vec3d.field_72448_b - this.field_72448_b;
        double d2 = vec3d.field_72449_c - this.field_72449_c;

        return (double) MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double func_72436_e(Vec3d vec3d) {
        double d0 = vec3d.field_72450_a - this.field_72450_a;
        double d1 = vec3d.field_72448_b - this.field_72448_b;
        double d2 = vec3d.field_72449_c - this.field_72449_c;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double func_186679_c(double d0, double d1, double d2) {
        double d3 = d0 - this.field_72450_a;
        double d4 = d1 - this.field_72448_b;
        double d5 = d2 - this.field_72449_c;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public Vec3d func_186678_a(double d0) {
        return new Vec3d(this.field_72450_a * d0, this.field_72448_b * d0, this.field_72449_c * d0);
    }

    public double func_72433_c() {
        return (double) MathHelper.func_76133_a(this.field_72450_a * this.field_72450_a + this.field_72448_b * this.field_72448_b + this.field_72449_c * this.field_72449_c);
    }

    @Nullable
    public Vec3d func_72429_b(Vec3d vec3d, double d0) {
        double d1 = vec3d.field_72450_a - this.field_72450_a;
        double d2 = vec3d.field_72448_b - this.field_72448_b;
        double d3 = vec3d.field_72449_c - this.field_72449_c;

        if (d1 * d1 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.field_72450_a) / d1;

            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3d(this.field_72450_a + d1 * d4, this.field_72448_b + d2 * d4, this.field_72449_c + d3 * d4) : null;
        }
    }

    @Nullable
    public Vec3d func_72435_c(Vec3d vec3d, double d0) {
        double d1 = vec3d.field_72450_a - this.field_72450_a;
        double d2 = vec3d.field_72448_b - this.field_72448_b;
        double d3 = vec3d.field_72449_c - this.field_72449_c;

        if (d2 * d2 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.field_72448_b) / d2;

            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3d(this.field_72450_a + d1 * d4, this.field_72448_b + d2 * d4, this.field_72449_c + d3 * d4) : null;
        }
    }

    @Nullable
    public Vec3d func_72434_d(Vec3d vec3d, double d0) {
        double d1 = vec3d.field_72450_a - this.field_72450_a;
        double d2 = vec3d.field_72448_b - this.field_72448_b;
        double d3 = vec3d.field_72449_c - this.field_72449_c;

        if (d3 * d3 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.field_72449_c) / d3;

            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3d(this.field_72450_a + d1 * d4, this.field_72448_b + d2 * d4, this.field_72449_c + d3 * d4) : null;
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Vec3d)) {
            return false;
        } else {
            Vec3d vec3d = (Vec3d) object;

            return Double.compare(vec3d.field_72450_a, this.field_72450_a) != 0 ? false : (Double.compare(vec3d.field_72448_b, this.field_72448_b) != 0 ? false : Double.compare(vec3d.field_72449_c, this.field_72449_c) == 0);
        }
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.field_72450_a);
        int j = (int) (i ^ i >>> 32);

        i = Double.doubleToLongBits(this.field_72448_b);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.field_72449_c);
        j = 31 * j + (int) (i ^ i >>> 32);
        return j;
    }

    public String toString() {
        return "(" + this.field_72450_a + ", " + this.field_72448_b + ", " + this.field_72449_c + ")";
    }

    public Vec3d func_178789_a(float f) {
        float f1 = MathHelper.func_76134_b(f);
        float f2 = MathHelper.func_76126_a(f);
        double d0 = this.field_72450_a;
        double d1 = this.field_72448_b * (double) f1 + this.field_72449_c * (double) f2;
        double d2 = this.field_72449_c * (double) f1 - this.field_72448_b * (double) f2;

        return new Vec3d(d0, d1, d2);
    }

    public Vec3d func_178785_b(float f) {
        float f1 = MathHelper.func_76134_b(f);
        float f2 = MathHelper.func_76126_a(f);
        double d0 = this.field_72450_a * (double) f1 + this.field_72449_c * (double) f2;
        double d1 = this.field_72448_b;
        double d2 = this.field_72449_c * (double) f1 - this.field_72450_a * (double) f2;

        return new Vec3d(d0, d1, d2);
    }
}

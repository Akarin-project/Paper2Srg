package net.minecraft.util.math;

import javax.annotation.Nullable;

public class Vec3d {

    public static final Vec3d ZERO = new Vec3d(0.0D, 0.0D, 0.0D);
    public final double x;
    public final double y;
    public final double z;

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

        this.x = d0;
        this.y = d1;
        this.z = d2;
    }

    public Vec3d(Vec3i baseblockposition) {
        this((double) baseblockposition.getX(), (double) baseblockposition.getY(), (double) baseblockposition.getZ());
    }

    public Vec3d subtractReverse(Vec3d vec3d) {
        return new Vec3d(vec3d.x - this.x, vec3d.y - this.y, vec3d.z - this.z);
    }

    public Vec3d normalize() {
        double d0 = (double) MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);

        return d0 < 1.0E-4D ? Vec3d.ZERO : new Vec3d(this.x / d0, this.y / d0, this.z / d0);
    }

    public double dotProduct(Vec3d vec3d) {
        return this.x * vec3d.x + this.y * vec3d.y + this.z * vec3d.z;
    }

    public Vec3d subtract(Vec3d vec3d) {
        return this.subtract(vec3d.x, vec3d.y, vec3d.z);
    }

    public Vec3d subtract(double d0, double d1, double d2) {
        return this.addVector(-d0, -d1, -d2);
    }

    public Vec3d add(Vec3d vec3d) {
        return this.addVector(vec3d.x, vec3d.y, vec3d.z);
    }

    public Vec3d addVector(double d0, double d1, double d2) {
        return new Vec3d(this.x + d0, this.y + d1, this.z + d2);
    }

    public double distanceTo(Vec3d vec3d) {
        double d0 = vec3d.x - this.x;
        double d1 = vec3d.y - this.y;
        double d2 = vec3d.z - this.z;

        return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double squareDistanceTo(Vec3d vec3d) {
        double d0 = vec3d.x - this.x;
        double d1 = vec3d.y - this.y;
        double d2 = vec3d.z - this.z;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double squareDistanceTo(double d0, double d1, double d2) {
        double d3 = d0 - this.x;
        double d4 = d1 - this.y;
        double d5 = d2 - this.z;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public Vec3d scale(double d0) {
        return new Vec3d(this.x * d0, this.y * d0, this.z * d0);
    }

    public double lengthVector() {
        return (double) MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Nullable
    public Vec3d getIntermediateWithXValue(Vec3d vec3d, double d0) {
        double d1 = vec3d.x - this.x;
        double d2 = vec3d.y - this.y;
        double d3 = vec3d.z - this.z;

        if (d1 * d1 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.x) / d1;

            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3d(this.x + d1 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
        }
    }

    @Nullable
    public Vec3d getIntermediateWithYValue(Vec3d vec3d, double d0) {
        double d1 = vec3d.x - this.x;
        double d2 = vec3d.y - this.y;
        double d3 = vec3d.z - this.z;

        if (d2 * d2 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.y) / d2;

            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3d(this.x + d1 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
        }
    }

    @Nullable
    public Vec3d getIntermediateWithZValue(Vec3d vec3d, double d0) {
        double d1 = vec3d.x - this.x;
        double d2 = vec3d.y - this.y;
        double d3 = vec3d.z - this.z;

        if (d3 * d3 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.z) / d3;

            return d4 >= 0.0D && d4 <= 1.0D ? new Vec3d(this.x + d1 * d4, this.y + d2 * d4, this.z + d3 * d4) : null;
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Vec3d)) {
            return false;
        } else {
            Vec3d vec3d = (Vec3d) object;

            return Double.compare(vec3d.x, this.x) != 0 ? false : (Double.compare(vec3d.y, this.y) != 0 ? false : Double.compare(vec3d.z, this.z) == 0);
        }
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.x);
        int j = (int) (i ^ i >>> 32);

        i = Double.doubleToLongBits(this.y);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.z);
        j = 31 * j + (int) (i ^ i >>> 32);
        return j;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public Vec3d rotatePitch(float f) {
        float f1 = MathHelper.cos(f);
        float f2 = MathHelper.sin(f);
        double d0 = this.x;
        double d1 = this.y * (double) f1 + this.z * (double) f2;
        double d2 = this.z * (double) f1 - this.y * (double) f2;

        return new Vec3d(d0, d1, d2);
    }

    public Vec3d rotateYaw(float f) {
        float f1 = MathHelper.cos(f);
        float f2 = MathHelper.sin(f);
        double d0 = this.x * (double) f1 + this.z * (double) f2;
        double d1 = this.y;
        double d2 = this.z * (double) f1 - this.x * (double) f2;

        return new Vec3d(d0, d1, d2);
    }
}

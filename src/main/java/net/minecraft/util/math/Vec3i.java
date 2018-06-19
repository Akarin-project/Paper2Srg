package net.minecraft.util.math;

import com.google.common.base.MoreObjects;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Vec3i implements Comparable<Vec3i> {

    public static final Vec3i field_177959_e = new Vec3i(0, 0, 0);
    // Paper start - Make mutable and protected for MutableBlockPos and PooledBlockPos
    protected int field_177962_a;
    protected int field_177960_b;
    protected int field_177961_c;

    public final boolean isValidLocation() {
        return field_177962_a >= -30000000 && field_177961_c >= -30000000 && field_177962_a < 30000000 && field_177961_c < 30000000 && field_177960_b >= 0 && field_177960_b < 256;
    }
    public boolean isInvalidYLocation() {
        return field_177960_b < 0 || field_177960_b >= 256;
    }
    // Paper end

    public Vec3i(int i, int j, int k) {
        this.field_177962_a = i;
        this.field_177960_b = j;
        this.field_177961_c = k;
    }

    public Vec3i(double d0, double d1, double d2) {
        this(MathHelper.func_76128_c(d0), MathHelper.func_76128_c(d1), MathHelper.func_76128_c(d2));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Vec3i)) {
            return false;
        } else {
            Vec3i baseblockposition = (Vec3i) object;

            return this.func_177958_n() != baseblockposition.func_177958_n() ? false : (this.func_177956_o() != baseblockposition.func_177956_o() ? false : this.func_177952_p() == baseblockposition.func_177952_p());
        }
    }

    public int hashCode() {
        return (this.func_177956_o() + this.func_177952_p() * 31) * 31 + this.func_177958_n();
    }

    public int compareTo(Vec3i baseblockposition) {
        return this.func_177956_o() == baseblockposition.func_177956_o() ? (this.func_177952_p() == baseblockposition.func_177952_p() ? this.func_177958_n() - baseblockposition.func_177958_n() : this.func_177952_p() - baseblockposition.func_177952_p()) : this.func_177956_o() - baseblockposition.func_177956_o();
    }

    // Paper start - Only allow a single implementation
    public final int func_177958_n() {
        return this.field_177962_a;
    }

    public final int func_177956_o() {
        return this.field_177960_b;
    }

    public final int func_177952_p() {
        return this.field_177961_c;
    }
    // Paper end

    public Vec3i func_177955_d(Vec3i baseblockposition) {
        return new Vec3i(this.func_177956_o() * baseblockposition.func_177952_p() - this.func_177952_p() * baseblockposition.func_177956_o(), this.func_177952_p() * baseblockposition.func_177958_n() - this.func_177958_n() * baseblockposition.func_177952_p(), this.func_177958_n() * baseblockposition.func_177956_o() - this.func_177956_o() * baseblockposition.func_177958_n());
    }

    public double func_185332_f(int i, int j, int k) {
        double d0 = (double) (this.func_177958_n() - i);
        double d1 = (double) (this.func_177956_o() - j);
        double d2 = (double) (this.func_177952_p() - k);

        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double func_177954_c(double d0, double d1, double d2) {
        double d3 = (double) this.func_177958_n() - d0;
        double d4 = (double) this.func_177956_o() - d1;
        double d5 = (double) this.func_177952_p() - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double func_177957_d(double d0, double d1, double d2) {
        double d3 = (double) this.func_177958_n() + 0.5D - d0;
        double d4 = (double) this.func_177956_o() + 0.5D - d1;
        double d5 = (double) this.func_177952_p() + 0.5D - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double func_177951_i(Vec3i baseblockposition) {
        return this.func_177954_c((double) baseblockposition.func_177958_n(), (double) baseblockposition.func_177956_o(), (double) baseblockposition.func_177952_p());
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.func_177958_n()).add("y", this.func_177956_o()).add("z", this.func_177952_p()).toString();
    }

    public int compareTo(Vec3i object) { // Paper - decompile fix
        return this.compareTo((Vec3i) object);
    }
}

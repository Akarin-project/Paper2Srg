package net.minecraft.util.math;

import com.google.common.base.MoreObjects;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Vec3i implements Comparable<Vec3i> {

    public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);
    // Paper start - Make mutable and protected for MutableBlockPos and PooledBlockPos
    protected int x;
    protected int y;
    protected int z;

    public final boolean isValidLocation() {
        return x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000 && y >= 0 && y < 256;
    }
    public boolean isInvalidYLocation() {
        return y < 0 || y >= 256;
    }
    // Paper end

    public Vec3i(int i, int j, int k) {
        this.x = i;
        this.y = j;
        this.z = k;
    }

    public Vec3i(double d0, double d1, double d2) {
        this(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Vec3i)) {
            return false;
        } else {
            Vec3i baseblockposition = (Vec3i) object;

            return this.getX() != baseblockposition.getX() ? false : (this.getY() != baseblockposition.getY() ? false : this.getZ() == baseblockposition.getZ());
        }
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(Vec3i baseblockposition) {
        return this.getY() == baseblockposition.getY() ? (this.getZ() == baseblockposition.getZ() ? this.getX() - baseblockposition.getX() : this.getZ() - baseblockposition.getZ()) : this.getY() - baseblockposition.getY();
    }

    // Paper start - Only allow a single implementation
    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public final int getZ() {
        return this.z;
    }
    // Paper end

    public Vec3i crossProduct(Vec3i baseblockposition) {
        return new Vec3i(this.getY() * baseblockposition.getZ() - this.getZ() * baseblockposition.getY(), this.getZ() * baseblockposition.getX() - this.getX() * baseblockposition.getZ(), this.getX() * baseblockposition.getY() - this.getY() * baseblockposition.getX());
    }

    public double getDistance(int i, int j, int k) {
        double d0 = (double) (this.getX() - i);
        double d1 = (double) (this.getY() - j);
        double d2 = (double) (this.getZ() - k);

        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double distanceSq(double d0, double d1, double d2) {
        double d3 = (double) this.getX() - d0;
        double d4 = (double) this.getY() - d1;
        double d5 = (double) this.getZ() - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double distanceSqToCenter(double d0, double d1, double d2) {
        double d3 = (double) this.getX() + 0.5D - d0;
        double d4 = (double) this.getY() + 0.5D - d1;
        double d5 = (double) this.getZ() + 0.5D - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double distanceSq(Vec3i baseblockposition) {
        return this.distanceSq((double) baseblockposition.getX(), (double) baseblockposition.getY(), (double) baseblockposition.getZ());
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    public int compareTo(Vec3i object) { // Paper - decompile fix
        return this.compareTo((Vec3i) object);
    }
}

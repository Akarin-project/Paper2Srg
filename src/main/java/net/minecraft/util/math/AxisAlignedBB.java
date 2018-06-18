package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;

public class AxisAlignedBB {

    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double d0, double d1, double d2, double d3, double d4, double d5) {
        this.minX = Math.min(d0, d3);
        this.minY = Math.min(d1, d4);
        this.minZ = Math.min(d2, d5);
        this.maxX = Math.max(d0, d3);
        this.maxY = Math.max(d1, d4);
        this.maxZ = Math.max(d2, d5);
    }

    public AxisAlignedBB(BlockPos blockposition) {
        this((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 1), (double) (blockposition.getZ() + 1));
    }

    public AxisAlignedBB(BlockPos blockposition, BlockPos blockposition1) {
        this((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
    }

    public AxisAlignedBB setMaxY(double d0) {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, d0, this.maxZ);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof AxisAlignedBB)) {
            return false;
        } else {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB) object;

            return Double.compare(axisalignedbb.minX, this.minX) != 0 ? false : (Double.compare(axisalignedbb.minY, this.minY) != 0 ? false : (Double.compare(axisalignedbb.minZ, this.minZ) != 0 ? false : (Double.compare(axisalignedbb.maxX, this.maxX) != 0 ? false : (Double.compare(axisalignedbb.maxY, this.maxY) != 0 ? false : Double.compare(axisalignedbb.maxZ, this.maxZ) == 0))));
        }
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.minX);
        int j = (int) (i ^ i >>> 32);

        i = Double.doubleToLongBits(this.minY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxX);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        return j;
    }

    public AxisAlignedBB contract(double d0, double d1, double d2) {
        double d3 = this.minX;
        double d4 = this.minY;
        double d5 = this.minZ;
        double d6 = this.maxX;
        double d7 = this.maxY;
        double d8 = this.maxZ;

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

    public AxisAlignedBB expand(double d0, double d1, double d2) {
        double d3 = this.minX;
        double d4 = this.minY;
        double d5 = this.minZ;
        double d6 = this.maxX;
        double d7 = this.maxY;
        double d8 = this.maxZ;

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

    public AxisAlignedBB grow(double d0, double d1, double d2) {
        double d3 = this.minX - d0;
        double d4 = this.minY - d1;
        double d5 = this.minZ - d2;
        double d6 = this.maxX + d0;
        double d7 = this.maxY + d1;
        double d8 = this.maxZ + d2;

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB grow(double d0) {
        return this.grow(d0, d0, d0);
    }

    public AxisAlignedBB intersect(AxisAlignedBB axisalignedbb) {
        double d0 = Math.max(this.minX, axisalignedbb.minX);
        double d1 = Math.max(this.minY, axisalignedbb.minY);
        double d2 = Math.max(this.minZ, axisalignedbb.minZ);
        double d3 = Math.min(this.maxX, axisalignedbb.maxX);
        double d4 = Math.min(this.maxY, axisalignedbb.maxY);
        double d5 = Math.min(this.maxZ, axisalignedbb.maxZ);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB union(AxisAlignedBB axisalignedbb) {
        double d0 = Math.min(this.minX, axisalignedbb.minX);
        double d1 = Math.min(this.minY, axisalignedbb.minY);
        double d2 = Math.min(this.minZ, axisalignedbb.minZ);
        double d3 = Math.max(this.maxX, axisalignedbb.maxX);
        double d4 = Math.max(this.maxY, axisalignedbb.maxY);
        double d5 = Math.max(this.maxZ, axisalignedbb.maxZ);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB offset(double d0, double d1, double d2) {
        return new AxisAlignedBB(this.minX + d0, this.minY + d1, this.minZ + d2, this.maxX + d0, this.maxY + d1, this.maxZ + d2);
    }

    public AxisAlignedBB offset(BlockPos blockposition) {
        return new AxisAlignedBB(this.minX + (double) blockposition.getX(), this.minY + (double) blockposition.getY(), this.minZ + (double) blockposition.getZ(), this.maxX + (double) blockposition.getX(), this.maxY + (double) blockposition.getY(), this.maxZ + (double) blockposition.getZ());
    }

    public AxisAlignedBB offset(Vec3d vec3d) {
        return this.offset(vec3d.x, vec3d.y, vec3d.z);
    }

    public double calculateXOffset(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.maxY > this.minY && axisalignedbb.minY < this.maxY && axisalignedbb.maxZ > this.minZ && axisalignedbb.minZ < this.maxZ) {
            double d1;

            if (d0 > 0.0D && axisalignedbb.maxX <= this.minX) {
                d1 = this.minX - axisalignedbb.maxX;
                if (d1 < d0) {
                    d0 = d1;
                }
            } else if (d0 < 0.0D && axisalignedbb.minX >= this.maxX) {
                d1 = this.maxX - axisalignedbb.minX;
                if (d1 > d0) {
                    d0 = d1;
                }
            }

            return d0;
        } else {
            return d0;
        }
    }

    public double calculateYOffset(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.maxX > this.minX && axisalignedbb.minX < this.maxX && axisalignedbb.maxZ > this.minZ && axisalignedbb.minZ < this.maxZ) {
            double d1;

            if (d0 > 0.0D && axisalignedbb.maxY <= this.minY) {
                d1 = this.minY - axisalignedbb.maxY;
                if (d1 < d0) {
                    d0 = d1;
                }
            } else if (d0 < 0.0D && axisalignedbb.minY >= this.maxY) {
                d1 = this.maxY - axisalignedbb.minY;
                if (d1 > d0) {
                    d0 = d1;
                }
            }

            return d0;
        } else {
            return d0;
        }
    }

    public double calculateZOffset(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.maxX > this.minX && axisalignedbb.minX < this.maxX && axisalignedbb.maxY > this.minY && axisalignedbb.minY < this.maxY) {
            double d1;

            if (d0 > 0.0D && axisalignedbb.maxZ <= this.minZ) {
                d1 = this.minZ - axisalignedbb.maxZ;
                if (d1 < d0) {
                    d0 = d1;
                }
            } else if (d0 < 0.0D && axisalignedbb.minZ >= this.maxZ) {
                d1 = this.maxZ - axisalignedbb.minZ;
                if (d1 > d0) {
                    d0 = d1;
                }
            }

            return d0;
        } else {
            return d0;
        }
    }

    public final boolean intersects(AxisAlignedBB intersecting) { return this.intersects(intersecting); } // Paper - OBFHELPER
    public boolean intersects(AxisAlignedBB axisalignedbb) {
        return this.intersects(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    }

    public boolean intersects(double d0, double d1, double d2, double d3, double d4, double d5) {
        return this.minX < d3 && this.maxX > d0 && this.minY < d4 && this.maxY > d1 && this.minZ < d5 && this.maxZ > d2;
    }

    public boolean contains(Vec3d vec3d) {
        return vec3d.x > this.minX && vec3d.x < this.maxX ? (vec3d.y > this.minY && vec3d.y < this.maxY ? vec3d.z > this.minZ && vec3d.z < this.maxZ : false) : false;
    }

    public double getAverageEdgeLength() {
        double d0 = this.maxX - this.minX;
        double d1 = this.maxY - this.minY;
        double d2 = this.maxZ - this.minZ;

        return (d0 + d1 + d2) / 3.0D;
    }

    public AxisAlignedBB shrink(double d0) {
        return this.grow(-d0);
    }

    @Nullable
    public RayTraceResult calculateIntercept(Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = this.collideWithXPlane(this.minX, vec3d, vec3d1);
        EnumFacing enumdirection = EnumFacing.WEST;
        Vec3d vec3d3 = this.collideWithXPlane(this.maxX, vec3d, vec3d1);

        if (vec3d3 != null && this.isClosest(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.EAST;
        }

        vec3d3 = this.collideWithYPlane(this.minY, vec3d, vec3d1);
        if (vec3d3 != null && this.isClosest(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.DOWN;
        }

        vec3d3 = this.collideWithYPlane(this.maxY, vec3d, vec3d1);
        if (vec3d3 != null && this.isClosest(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.UP;
        }

        vec3d3 = this.collideWithZPlane(this.minZ, vec3d, vec3d1);
        if (vec3d3 != null && this.isClosest(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.NORTH;
        }

        vec3d3 = this.collideWithZPlane(this.maxZ, vec3d, vec3d1);
        if (vec3d3 != null && this.isClosest(vec3d, vec3d2, vec3d3)) {
            vec3d2 = vec3d3;
            enumdirection = EnumFacing.SOUTH;
        }

        return vec3d2 == null ? null : new RayTraceResult(vec3d2, enumdirection);
    }

    @VisibleForTesting
    boolean isClosest(Vec3d vec3d, @Nullable Vec3d vec3d1, Vec3d vec3d2) {
        return vec3d1 == null || vec3d.squareDistanceTo(vec3d2) < vec3d.squareDistanceTo(vec3d1);
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithXPlane(double d0, Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = vec3d.getIntermediateWithXValue(vec3d1, d0);

        return vec3d2 != null && this.intersectsWithYZ(vec3d2) ? vec3d2 : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithYPlane(double d0, Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = vec3d.getIntermediateWithYValue(vec3d1, d0);

        return vec3d2 != null && this.intersectsWithXZ(vec3d2) ? vec3d2 : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithZPlane(double d0, Vec3d vec3d, Vec3d vec3d1) {
        Vec3d vec3d2 = vec3d.getIntermediateWithZValue(vec3d1, d0);

        return vec3d2 != null && this.intersectsWithXY(vec3d2) ? vec3d2 : null;
    }

    @VisibleForTesting
    public boolean intersectsWithYZ(Vec3d vec3d) {
        return vec3d.y >= this.minY && vec3d.y <= this.maxY && vec3d.z >= this.minZ && vec3d.z <= this.maxZ;
    }

    @VisibleForTesting
    public boolean intersectsWithXZ(Vec3d vec3d) {
        return vec3d.x >= this.minX && vec3d.x <= this.maxX && vec3d.z >= this.minZ && vec3d.z <= this.maxZ;
    }

    @VisibleForTesting
    public boolean intersectsWithXY(Vec3d vec3d) {
        return vec3d.x >= this.minX && vec3d.x <= this.maxX && vec3d.y >= this.minY && vec3d.y <= this.maxY;
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}

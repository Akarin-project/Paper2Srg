package net.minecraft.util.math;
import net.minecraft.entity.Entity;


public class ChunkPos {

    public final int x;
    public final int z;

    public ChunkPos(int i, int j) {
        this.x = i;
        this.z = j;
    }

    public ChunkPos(BlockPos blockposition) {
        this.x = blockposition.getX() >> 4;
        this.z = blockposition.getZ() >> 4;
    }

    public static long asLong(final BlockPos pos) { return asLong(pos.getX() >> 4, pos.getZ() >> 4); } // Paper - OBFHELPER
    public static long asLong(int x, int z) { return asLong(x, z); } // Paper - OBFHELPER
    public static long asLong(int i, int j) {
        return (long) i & 4294967295L | ((long) j & 4294967295L) << 32;
    }

    public int hashCode() {
        int i = 1664525 * this.x + 1013904223;
        int j = 1664525 * (this.z ^ -559038737) + 1013904223;

        return i ^ j;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChunkPos)) {
            return false;
        } else {
            ChunkPos chunkcoordintpair = (ChunkPos) object;

            return this.x == chunkcoordintpair.x && this.z == chunkcoordintpair.z;
        }
    }

    public double getDistanceSq(Entity entity) {
        double d0 = (double) (this.x * 16 + 8);
        double d1 = (double) (this.z * 16 + 8);
        double d2 = d0 - entity.posX;
        double d3 = d1 - entity.posZ;

        return d2 * d2 + d3 * d3;
    }

    public int getXStart() {
        return this.x << 4;
    }

    public int getZStart() {
        return this.z << 4;
    }

    public int getXEnd() {
        return (this.x << 4) + 15;
    }

    public int getZEnd() {
        return (this.z << 4) + 15;
    }

    public BlockPos getBlock(int i, int j, int k) {
        return new BlockPos((this.x << 4) + i, j, (this.z << 4) + k);
    }

    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }
}

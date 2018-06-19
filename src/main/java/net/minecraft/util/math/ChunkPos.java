package net.minecraft.util.math;
import net.minecraft.entity.Entity;


public class ChunkPos {

    public final int field_77276_a;
    public final int field_77275_b;

    public ChunkPos(int i, int j) {
        this.field_77276_a = i;
        this.field_77275_b = j;
    }

    public ChunkPos(BlockPos blockposition) {
        this.field_77276_a = blockposition.func_177958_n() >> 4;
        this.field_77275_b = blockposition.func_177952_p() >> 4;
    }

    public static long asLong(final BlockPos pos) { return func_77272_a(pos.func_177958_n() >> 4, pos.func_177952_p() >> 4); } // Paper - OBFHELPER
    public static long asLong(int x, int z) { return func_77272_a(x, z); } // Paper - OBFHELPER
    public static long func_77272_a(int i, int j) {
        return (long) i & 4294967295L | ((long) j & 4294967295L) << 32;
    }

    public int hashCode() {
        int i = 1664525 * this.field_77276_a + 1013904223;
        int j = 1664525 * (this.field_77275_b ^ -559038737) + 1013904223;

        return i ^ j;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChunkPos)) {
            return false;
        } else {
            ChunkPos chunkcoordintpair = (ChunkPos) object;

            return this.field_77276_a == chunkcoordintpair.field_77276_a && this.field_77275_b == chunkcoordintpair.field_77275_b;
        }
    }

    public double func_185327_a(Entity entity) {
        double d0 = (double) (this.field_77276_a * 16 + 8);
        double d1 = (double) (this.field_77275_b * 16 + 8);
        double d2 = d0 - entity.field_70165_t;
        double d3 = d1 - entity.field_70161_v;

        return d2 * d2 + d3 * d3;
    }

    public int func_180334_c() {
        return this.field_77276_a << 4;
    }

    public int func_180333_d() {
        return this.field_77275_b << 4;
    }

    public int func_180332_e() {
        return (this.field_77276_a << 4) + 15;
    }

    public int func_180330_f() {
        return (this.field_77275_b << 4) + 15;
    }

    public BlockPos func_180331_a(int i, int j, int k) {
        return new BlockPos((this.field_77276_a << 4) + i, j, (this.field_77275_b << 4) + k);
    }

    public String toString() {
        return "[" + this.field_77276_a + ", " + this.field_77275_b + "]";
    }
}

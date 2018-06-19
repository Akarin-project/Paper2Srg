package net.minecraft.world;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;


public class NextTickListEntry implements Comparable<NextTickListEntry> {

    private static long field_77177_f;
    private final Block field_151352_g;
    public final BlockPos field_180282_a;
    public long field_77180_e;
    public int field_82754_f;
    private final long field_77178_g;

    public NextTickListEntry(BlockPos blockposition, Block block) {
        this.field_77178_g = (long) (NextTickListEntry.field_77177_f++);
        this.field_180282_a = blockposition.func_185334_h();
        this.field_151352_g = block;
    }

    public boolean equals(Object object) {
        if (!(object instanceof NextTickListEntry)) {
            return false;
        } else {
            NextTickListEntry nextticklistentry = (NextTickListEntry) object;

            return this.field_180282_a.equals(nextticklistentry.field_180282_a) && Block.func_149680_a(this.field_151352_g, nextticklistentry.field_151352_g);
        }
    }

    public int hashCode() {
        return this.field_180282_a.hashCode();
    }

    public NextTickListEntry func_77176_a(long i) {
        this.field_77180_e = i;
        return this;
    }

    public void func_82753_a(int i) {
        this.field_82754_f = i;
    }

    public int compareTo(NextTickListEntry nextticklistentry) {
        return this.field_77180_e < nextticklistentry.field_77180_e ? -1 : (this.field_77180_e > nextticklistentry.field_77180_e ? 1 : (this.field_82754_f != nextticklistentry.field_82754_f ? this.field_82754_f - nextticklistentry.field_82754_f : (this.field_77178_g < nextticklistentry.field_77178_g ? -1 : (this.field_77178_g > nextticklistentry.field_77178_g ? 1 : 0))));
    }

    public String toString() {
        return Block.func_149682_b(this.field_151352_g) + ": " + this.field_180282_a + ", " + this.field_77180_e + ", " + this.field_82754_f + ", " + this.field_77178_g;
    }

    public Block func_151351_a() {
        return this.field_151352_g;
    }

    public int compareTo(Object object) {
        return this.compareTo((NextTickListEntry) object);
    }
}

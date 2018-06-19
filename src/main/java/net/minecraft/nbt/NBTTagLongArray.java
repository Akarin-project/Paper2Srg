package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagLongArray extends NBTBase {

    private long[] field_193587_b;

    NBTTagLongArray() {}

    public NBTTagLongArray(long[] along) {
        this.field_193587_b = along;
    }

    public NBTTagLongArray(List<Long> list) {
        this(func_193586_a(list));
    }

    private static long[] func_193586_a(List<Long> list) {
        long[] along = new long[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Long olong = (Long) list.get(i);

            along[i] = olong == null ? 0L : olong.longValue();
        }

        return along;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.field_193587_b.length);
        long[] along = this.field_193587_b;
        int i = along.length;

        for (int j = 0; j < i; ++j) {
            long k = along[j];

            dataoutput.writeLong(k);
        }

    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(192L);
        int j = datainput.readInt();

        nbtreadlimiter.func_152450_a((long) (64 * j));
        this.field_193587_b = new long[j];

        for (int k = 0; k < j; ++k) {
            this.field_193587_b[k] = datainput.readLong();
        }

    }

    public byte func_74732_a() {
        return (byte) 12;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[L;");

        for (int i = 0; i < this.field_193587_b.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.field_193587_b[i]).append('L');
        }

        return stringbuilder.append(']').toString();
    }

    public NBTTagLongArray func_74737_b() {
        long[] along = new long[this.field_193587_b.length];

        System.arraycopy(this.field_193587_b, 0, along, 0, this.field_193587_b.length);
        return new NBTTagLongArray(along);
    }

    public boolean equals(Object object) {
        return super.equals(object) && Arrays.equals(this.field_193587_b, ((NBTTagLongArray) object).field_193587_b);
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.field_193587_b);
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

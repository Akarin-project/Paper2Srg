package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagLongArray extends NBTBase {

    private long[] data;

    NBTTagLongArray() {}

    public NBTTagLongArray(long[] along) {
        this.data = along;
    }

    public NBTTagLongArray(List<Long> list) {
        this(toArray(list));
    }

    private static long[] toArray(List<Long> list) {
        long[] along = new long[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Long olong = (Long) list.get(i);

            along[i] = olong == null ? 0L : olong.longValue();
        }

        return along;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.data.length);
        long[] along = this.data;
        int i = along.length;

        for (int j = 0; j < i; ++j) {
            long k = along[j];

            dataoutput.writeLong(k);
        }

    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(192L);
        int j = datainput.readInt();

        nbtreadlimiter.read((long) (64 * j));
        this.data = new long[j];

        for (int k = 0; k < j; ++k) {
            this.data[k] = datainput.readLong();
        }

    }

    public byte getId() {
        return (byte) 12;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[L;");

        for (int i = 0; i < this.data.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.data[i]).append('L');
        }

        return stringbuilder.append(']').toString();
    }

    public NBTTagLongArray copy() {
        long[] along = new long[this.data.length];

        System.arraycopy(this.data, 0, along, 0, this.data.length);
        return new NBTTagLongArray(along);
    }

    public boolean equals(Object object) {
        return super.equals(object) && Arrays.equals(this.data, ((NBTTagLongArray) object).data);
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    public NBTBase clone() {
        return this.copy();
    }
}

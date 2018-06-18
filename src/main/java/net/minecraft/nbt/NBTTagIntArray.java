package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagIntArray extends NBTBase {

    private int[] intArray;

    NBTTagIntArray() {}

    public NBTTagIntArray(int[] aint) {
        this.intArray = aint;
    }

    public NBTTagIntArray(List<Integer> list) {
        this(toArray(list));
    }

    private static int[] toArray(List<Integer> list) {
        int[] aint = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Integer integer = (Integer) list.get(i);

            aint[i] = integer == null ? 0 : integer.intValue();
        }

        return aint;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.intArray.length);
        int[] aint = this.intArray;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint[j];

            dataoutput.writeInt(k);
        }

    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(192L);
        int j = datainput.readInt();
       com.google.common.base.Preconditions.checkArgument( j < 1 << 24);

        nbtreadlimiter.read((long) (32 * j));
        this.intArray = new int[j];

        for (int k = 0; k < j; ++k) {
            this.intArray[k] = datainput.readInt();
        }

    }

    public byte getId() {
        return (byte) 11;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[I;");

        for (int i = 0; i < this.intArray.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.intArray[i]);
        }

        return stringbuilder.append(']').toString();
    }

    public NBTTagIntArray copy() {
        int[] aint = new int[this.intArray.length];

        System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
        return new NBTTagIntArray(aint);
    }

    public boolean equals(Object object) {
        return super.equals(object) && Arrays.equals(this.intArray, ((NBTTagIntArray) object).intArray);
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.intArray);
    }

    public int[] getIntArray() {
        return this.intArray;
    }

    public NBTBase clone() {
        return this.copy();
    }
}

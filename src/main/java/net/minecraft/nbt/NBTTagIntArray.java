package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagIntArray extends NBTBase {

    private int[] field_74749_a;

    NBTTagIntArray() {}

    public NBTTagIntArray(int[] aint) {
        this.field_74749_a = aint;
    }

    public NBTTagIntArray(List<Integer> list) {
        this(func_193584_a(list));
    }

    private static int[] func_193584_a(List<Integer> list) {
        int[] aint = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Integer integer = (Integer) list.get(i);

            aint[i] = integer == null ? 0 : integer.intValue();
        }

        return aint;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.field_74749_a.length);
        int[] aint = this.field_74749_a;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint[j];

            dataoutput.writeInt(k);
        }

    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(192L);
        int j = datainput.readInt();
       com.google.common.base.Preconditions.checkArgument( j < 1 << 24);

        nbtreadlimiter.func_152450_a((long) (32 * j));
        this.field_74749_a = new int[j];

        for (int k = 0; k < j; ++k) {
            this.field_74749_a[k] = datainput.readInt();
        }

    }

    public byte func_74732_a() {
        return (byte) 11;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[I;");

        for (int i = 0; i < this.field_74749_a.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.field_74749_a[i]);
        }

        return stringbuilder.append(']').toString();
    }

    public NBTTagIntArray func_74737_b() {
        int[] aint = new int[this.field_74749_a.length];

        System.arraycopy(this.field_74749_a, 0, aint, 0, this.field_74749_a.length);
        return new NBTTagIntArray(aint);
    }

    public boolean equals(Object object) {
        return super.equals(object) && Arrays.equals(this.field_74749_a, ((NBTTagIntArray) object).field_74749_a);
    }

    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.field_74749_a);
    }

    public int[] func_150302_c() {
        return this.field_74749_a;
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

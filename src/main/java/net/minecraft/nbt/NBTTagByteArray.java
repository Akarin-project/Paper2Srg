package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTBase {

    private byte[] field_74754_a;

    NBTTagByteArray() {}

    public NBTTagByteArray(byte[] abyte) {
        this.field_74754_a = abyte;
    }

    public NBTTagByteArray(List<Byte> list) {
        this(func_193589_a(list));
    }

    private static byte[] func_193589_a(List<Byte> list) {
        byte[] abyte = new byte[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Byte obyte = list.get(i);

            abyte[i] = obyte == null ? 0 : obyte.byteValue();
        }

        return abyte;
    }

    @Override
    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.field_74754_a.length);
        dataoutput.write(this.field_74754_a);
    }

    @Override
    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(192L);
        int j = datainput.readInt();
       com.google.common.base.Preconditions.checkArgument( j < 1 << 24);

        nbtreadlimiter.func_152450_a(8 * j);
        this.field_74754_a = new byte[j];
        datainput.readFully(this.field_74754_a);
    }

    @Override
    public byte func_74732_a() {
        return (byte) 7;
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[B;");

        for (int i = 0; i < this.field_74754_a.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.field_74754_a[i]).append('B');
        }

        return stringbuilder.append(']').toString();
    }

    @Override
    public NBTBase clone() {
        byte[] abyte = new byte[this.field_74754_a.length];

        System.arraycopy(this.field_74754_a, 0, abyte, 0, this.field_74754_a.length);
        return new NBTTagByteArray(abyte);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object) && Arrays.equals(this.field_74754_a, ((NBTTagByteArray) object).field_74754_a);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.field_74754_a);
    }

    public byte[] func_150292_c() {
        return this.field_74754_a;
    }

    @Override
    public NBTBase func_74737_b() {
        return clone();
    }
}

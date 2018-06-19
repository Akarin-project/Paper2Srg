package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTBase {

    private byte[] data;

    NBTTagByteArray() {}

    public NBTTagByteArray(byte[] abyte) {
        this.data = abyte;
    }

    public NBTTagByteArray(List<Byte> list) {
        this(toArray(list));
    }

    private static byte[] toArray(List<Byte> list) {
        byte[] abyte = new byte[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Byte obyte = list.get(i);

            abyte[i] = obyte == null ? 0 : obyte.byteValue();
        }

        return abyte;
    }

    @Override
    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.data.length);
        dataoutput.write(this.data);
    }

    @Override
    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(192L);
        int j = datainput.readInt();
       com.google.common.base.Preconditions.checkArgument( j < 1 << 24);

        nbtreadlimiter.read(8 * j);
        this.data = new byte[j];
        datainput.readFully(this.data);
    }

    @Override
    public byte getId() {
        return (byte) 7;
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[B;");

        for (int i = 0; i < this.data.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.data[i]).append('B');
        }

        return stringbuilder.append(']').toString();
    }

    @Override
    public NBTBase copy() { return clone(); } // Akarin
    @Override
    public NBTBase clone() {
        byte[] abyte = new byte[this.data.length];

        System.arraycopy(this.data, 0, abyte, 0, this.data.length);
        return new NBTTagByteArray(abyte);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object) && Arrays.equals(this.data, ((NBTTagByteArray) object).data);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    public byte[] getByteArray() {
        return this.data;
    }
}

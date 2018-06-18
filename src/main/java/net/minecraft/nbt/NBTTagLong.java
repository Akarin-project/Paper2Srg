package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTPrimitive {

    private long data;

    NBTTagLong() {}

    public NBTTagLong(long i) {
        this.data = i;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeLong(this.data);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(128L);
        this.data = datainput.readLong();
    }

    public byte getId() {
        return (byte) 4;
    }

    public String toString() {
        return this.data + "L";
    }

    public NBTTagLong copy() {
        return new NBTTagLong(this.data);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.data == ((NBTTagLong) object).data;
    }

    public int hashCode() {
        return super.hashCode() ^ (int) (this.data ^ this.data >>> 32);
    }

    public long getLong() {
        return this.data;
    }

    public int getInt() {
        return (int) (this.data & -1L);
    }

    public short getShort() {
        return (short) ((int) (this.data & 65535L));
    }

    public byte getByte() {
        return (byte) ((int) (this.data & 255L));
    }

    public double getDouble() {
        return (double) this.data;
    }

    public float getFloat() {
        return (float) this.data;
    }

    public NBTBase clone() {
        return this.copy();
    }
}

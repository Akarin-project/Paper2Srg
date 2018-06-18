package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTPrimitive {

    private byte data;

    NBTTagByte() {}

    public NBTTagByte(byte b0) {
        this.data = b0;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(this.data);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(72L);
        this.data = datainput.readByte();
    }

    public byte getId() {
        return (byte) 1;
    }

    public String toString() {
        return this.data + "b";
    }

    public NBTTagByte copy() {
        return new NBTTagByte(this.data);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.data == ((NBTTagByte) object).data;
    }

    public int hashCode() {
        return super.hashCode() ^ this.data;
    }

    public long getLong() {
        return (long) this.data;
    }

    public int getInt() {
        return this.data;
    }

    public short getShort() {
        return (short) this.data;
    }

    public byte getByte() {
        return this.data;
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

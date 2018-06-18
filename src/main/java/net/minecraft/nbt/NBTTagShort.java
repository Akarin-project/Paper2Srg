package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTPrimitive {

    private short data;

    public NBTTagShort() {}

    public NBTTagShort(short short0) {
        this.data = short0;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeShort(this.data);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(80L);
        this.data = datainput.readShort();
    }

    public byte getId() {
        return (byte) 2;
    }

    public String toString() {
        return this.data + "s";
    }

    public NBTTagShort copy() {
        return new NBTTagShort(this.data);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.data == ((NBTTagShort) object).data;
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
        return this.data;
    }

    public byte getByte() {
        return (byte) (this.data & 255);
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

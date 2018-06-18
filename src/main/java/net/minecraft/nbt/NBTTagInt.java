package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTPrimitive {

    private int data;

    NBTTagInt() {}

    public NBTTagInt(int i) {
        this.data = i;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.data);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(96L);
        this.data = datainput.readInt();
    }

    public byte getId() {
        return (byte) 3;
    }

    public String toString() {
        return String.valueOf(this.data);
    }

    public NBTTagInt copy() {
        return new NBTTagInt(this.data);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.data == ((NBTTagInt) object).data;
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
        return (short) (this.data & '\uffff');
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

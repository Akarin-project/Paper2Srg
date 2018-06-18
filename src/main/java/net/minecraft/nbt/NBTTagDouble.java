package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.util.math.MathHelper;

public class NBTTagDouble extends NBTPrimitive {

    private double data;

    NBTTagDouble() {}

    public NBTTagDouble(double d0) {
        this.data = d0;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeDouble(this.data);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(128L);
        this.data = datainput.readDouble();
    }

    public byte getId() {
        return (byte) 6;
    }

    public String toString() {
        return this.data + "d";
    }

    public NBTTagDouble copy() {
        return new NBTTagDouble(this.data);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.data == ((NBTTagDouble) object).data;
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.data);

        return super.hashCode() ^ (int) (i ^ i >>> 32);
    }

    public long getLong() {
        return (long) Math.floor(this.data);
    }

    public int getInt() {
        return MathHelper.floor(this.data);
    }

    public short getShort() {
        return (short) (MathHelper.floor(this.data) & '\uffff');
    }

    public byte getByte() {
        return (byte) (MathHelper.floor(this.data) & 255);
    }

    public double getDouble() {
        return this.data;
    }

    public float getFloat() {
        return (float) this.data;
    }

    public NBTBase clone() {
        return this.copy();
    }
}

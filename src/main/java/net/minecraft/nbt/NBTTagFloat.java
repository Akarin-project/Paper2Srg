package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.util.math.MathHelper;

public class NBTTagFloat extends NBTPrimitive {

    private float data;

    NBTTagFloat() {}

    public NBTTagFloat(float f) {
        this.data = f;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeFloat(this.data);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(96L);
        this.data = datainput.readFloat();
    }

    public byte getId() {
        return (byte) 5;
    }

    public String toString() {
        return this.data + "f";
    }

    public NBTTagFloat copy() {
        return new NBTTagFloat(this.data);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.data == ((NBTTagFloat) object).data;
    }

    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits(this.data);
    }

    public long getLong() {
        return (long) this.data;
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
        return (double) this.data;
    }

    public float getFloat() {
        return this.data;
    }

    public NBTBase clone() {
        return this.copy();
    }
}

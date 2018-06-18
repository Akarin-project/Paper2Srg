package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {

    NBTTagEnd() {}

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(64L);
    }

    void write(DataOutput dataoutput) throws IOException {}

    public byte getId() {
        return (byte) 0;
    }

    public String toString() {
        return "END";
    }

    public NBTTagEnd copy() {
        return new NBTTagEnd();
    }

    public NBTBase clone() {
        return this.copy();
    }
}

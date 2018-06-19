package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {

    NBTTagEnd() {}

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(64L);
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {}

    public byte func_74732_a() {
        return (byte) 0;
    }

    public String toString() {
        return "END";
    }

    public NBTTagEnd func_74737_b() {
        return new NBTTagEnd();
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

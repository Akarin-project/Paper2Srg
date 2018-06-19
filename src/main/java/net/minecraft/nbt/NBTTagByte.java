package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTPrimitive {

    private byte field_74756_a;

    NBTTagByte() {}

    public NBTTagByte(byte b0) {
        this.field_74756_a = b0;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(this.field_74756_a);
    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(72L);
        this.field_74756_a = datainput.readByte();
    }

    public byte func_74732_a() {
        return (byte) 1;
    }

    public String toString() {
        return this.field_74756_a + "b";
    }

    public NBTTagByte func_74737_b() {
        return new NBTTagByte(this.field_74756_a);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.field_74756_a == ((NBTTagByte) object).field_74756_a;
    }

    public int hashCode() {
        return super.hashCode() ^ this.field_74756_a;
    }

    public long func_150291_c() {
        return (long) this.field_74756_a;
    }

    public int func_150287_d() {
        return this.field_74756_a;
    }

    public short func_150289_e() {
        return (short) this.field_74756_a;
    }

    public byte func_150290_f() {
        return this.field_74756_a;
    }

    public double func_150286_g() {
        return (double) this.field_74756_a;
    }

    public float func_150288_h() {
        return (float) this.field_74756_a;
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

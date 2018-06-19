package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTPrimitive {

    private short field_74752_a;

    public NBTTagShort() {}

    public NBTTagShort(short short0) {
        this.field_74752_a = short0;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeShort(this.field_74752_a);
    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(80L);
        this.field_74752_a = datainput.readShort();
    }

    public byte func_74732_a() {
        return (byte) 2;
    }

    public String toString() {
        return this.field_74752_a + "s";
    }

    public NBTTagShort func_74737_b() {
        return new NBTTagShort(this.field_74752_a);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.field_74752_a == ((NBTTagShort) object).field_74752_a;
    }

    public int hashCode() {
        return super.hashCode() ^ this.field_74752_a;
    }

    public long func_150291_c() {
        return (long) this.field_74752_a;
    }

    public int func_150287_d() {
        return this.field_74752_a;
    }

    public short func_150289_e() {
        return this.field_74752_a;
    }

    public byte func_150290_f() {
        return (byte) (this.field_74752_a & 255);
    }

    public double func_150286_g() {
        return (double) this.field_74752_a;
    }

    public float func_150288_h() {
        return (float) this.field_74752_a;
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

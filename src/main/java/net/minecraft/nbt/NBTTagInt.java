package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTPrimitive {

    private int field_74748_a;

    NBTTagInt() {}

    public NBTTagInt(int i) {
        this.field_74748_a = i;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeInt(this.field_74748_a);
    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(96L);
        this.field_74748_a = datainput.readInt();
    }

    public byte func_74732_a() {
        return (byte) 3;
    }

    public String toString() {
        return String.valueOf(this.field_74748_a);
    }

    public NBTTagInt func_74737_b() {
        return new NBTTagInt(this.field_74748_a);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.field_74748_a == ((NBTTagInt) object).field_74748_a;
    }

    public int hashCode() {
        return super.hashCode() ^ this.field_74748_a;
    }

    public long func_150291_c() {
        return (long) this.field_74748_a;
    }

    public int func_150287_d() {
        return this.field_74748_a;
    }

    public short func_150289_e() {
        return (short) (this.field_74748_a & '\uffff');
    }

    public byte func_150290_f() {
        return (byte) (this.field_74748_a & 255);
    }

    public double func_150286_g() {
        return (double) this.field_74748_a;
    }

    public float func_150288_h() {
        return (float) this.field_74748_a;
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.util.math.MathHelper;

public class NBTTagDouble extends NBTPrimitive {

    private double field_74755_a;

    NBTTagDouble() {}

    public NBTTagDouble(double d0) {
        this.field_74755_a = d0;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeDouble(this.field_74755_a);
    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(128L);
        this.field_74755_a = datainput.readDouble();
    }

    public byte func_74732_a() {
        return (byte) 6;
    }

    public String toString() {
        return this.field_74755_a + "d";
    }

    public NBTTagDouble func_74737_b() {
        return new NBTTagDouble(this.field_74755_a);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.field_74755_a == ((NBTTagDouble) object).field_74755_a;
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.field_74755_a);

        return super.hashCode() ^ (int) (i ^ i >>> 32);
    }

    public long func_150291_c() {
        return (long) Math.floor(this.field_74755_a);
    }

    public int func_150287_d() {
        return MathHelper.func_76128_c(this.field_74755_a);
    }

    public short func_150289_e() {
        return (short) (MathHelper.func_76128_c(this.field_74755_a) & '\uffff');
    }

    public byte func_150290_f() {
        return (byte) (MathHelper.func_76128_c(this.field_74755_a) & 255);
    }

    public double func_150286_g() {
        return this.field_74755_a;
    }

    public float func_150288_h() {
        return (float) this.field_74755_a;
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

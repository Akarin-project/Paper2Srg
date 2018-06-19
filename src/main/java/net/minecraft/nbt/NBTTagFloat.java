package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.util.math.MathHelper;

public class NBTTagFloat extends NBTPrimitive {

    private float field_74750_a;

    NBTTagFloat() {}

    public NBTTagFloat(float f) {
        this.field_74750_a = f;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeFloat(this.field_74750_a);
    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(96L);
        this.field_74750_a = datainput.readFloat();
    }

    public byte func_74732_a() {
        return (byte) 5;
    }

    public String toString() {
        return this.field_74750_a + "f";
    }

    public NBTTagFloat func_74737_b() {
        return new NBTTagFloat(this.field_74750_a);
    }

    public boolean equals(Object object) {
        return super.equals(object) && this.field_74750_a == ((NBTTagFloat) object).field_74750_a;
    }

    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits(this.field_74750_a);
    }

    public long func_150291_c() {
        return (long) this.field_74750_a;
    }

    public int func_150287_d() {
        return MathHelper.func_76141_d(this.field_74750_a);
    }

    public short func_150289_e() {
        return (short) (MathHelper.func_76141_d(this.field_74750_a) & '\uffff');
    }

    public byte func_150290_f() {
        return (byte) (MathHelper.func_76141_d(this.field_74750_a) & 255);
    }

    public double func_150286_g() {
        return (double) this.field_74750_a;
    }

    public float func_150288_h() {
        return this.field_74750_a;
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

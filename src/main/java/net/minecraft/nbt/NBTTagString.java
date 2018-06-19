package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class NBTTagString extends NBTBase {

    private String field_74751_a;

    public NBTTagString() {
        this("");
    }

    public NBTTagString(String s) {
        Objects.requireNonNull(s, "Null string not allowed");
        this.field_74751_a = s;
    }

    void func_74734_a(DataOutput dataoutput) throws IOException {
        dataoutput.writeUTF(this.field_74751_a);
    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(288L);
        this.field_74751_a = datainput.readUTF();
        nbtreadlimiter.func_152450_a((long) (16 * this.field_74751_a.length()));
    }

    public byte func_74732_a() {
        return (byte) 8;
    }

    public String toString() {
        return func_193588_a(this.field_74751_a);
    }

    public NBTTagString func_74737_b() {
        return new NBTTagString(this.field_74751_a);
    }

    public boolean func_82582_d() {
        return this.field_74751_a.isEmpty();
    }

    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        } else {
            NBTTagString nbttagstring = (NBTTagString) object;

            return this.field_74751_a == null && nbttagstring.field_74751_a == null || Objects.equals(this.field_74751_a, nbttagstring.field_74751_a);
        }
    }

    public int hashCode() {
        return super.hashCode() ^ this.field_74751_a.hashCode();
    }

    public String func_150285_a_() {
        return this.field_74751_a;
    }

    public static String func_193588_a(String s) {
        StringBuilder stringbuilder = new StringBuilder("\"");

        for (int i = 0; i < s.length(); ++i) {
            char c0 = s.charAt(i);

            if (c0 == 92 || c0 == 34) {
                stringbuilder.append('\\');
            }

            stringbuilder.append(c0);
        }

        return stringbuilder.append('\"').toString();
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class NBTTagString extends NBTBase {

    private String data;

    public NBTTagString() {
        this("");
    }

    public NBTTagString(String s) {
        Objects.requireNonNull(s, "Null string not allowed");
        this.data = s;
    }

    void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeUTF(this.data);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(288L);
        this.data = datainput.readUTF();
        nbtreadlimiter.read((long) (16 * this.data.length()));
    }

    public byte getId() {
        return (byte) 8;
    }

    public String toString() {
        return quoteAndEscape(this.data);
    }

    public NBTTagString copy() {
        return new NBTTagString(this.data);
    }

    public boolean hasNoTags() {
        return this.data.isEmpty();
    }

    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        } else {
            NBTTagString nbttagstring = (NBTTagString) object;

            return this.data == null && nbttagstring.data == null || Objects.equals(this.data, nbttagstring.data);
        }
    }

    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }

    public String getString() {
        return this.data;
    }

    public static String quoteAndEscape(String s) {
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
        return this.copy();
    }
}

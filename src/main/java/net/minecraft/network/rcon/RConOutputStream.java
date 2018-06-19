package net.minecraft.network.rcon;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RConOutputStream {

    private final ByteArrayOutputStream field_72674_a;
    private final DataOutputStream field_72673_b;

    public RConOutputStream(int i) {
        this.field_72674_a = new ByteArrayOutputStream(i);
        this.field_72673_b = new DataOutputStream(this.field_72674_a);
    }

    public void func_72670_a(byte[] abyte) throws IOException {
        this.field_72673_b.write(abyte, 0, abyte.length);
    }

    public void func_72671_a(String s) throws IOException {
        this.field_72673_b.writeBytes(s);
        this.field_72673_b.write(0);
    }

    public void func_72667_a(int i) throws IOException {
        this.field_72673_b.write(i);
    }

    public void func_72668_a(short short0) throws IOException {
        this.field_72673_b.writeShort(Short.reverseBytes(short0));
    }

    public byte[] func_72672_a() {
        return this.field_72674_a.toByteArray();
    }

    public void func_72669_b() {
        this.field_72674_a.reset();
    }
}

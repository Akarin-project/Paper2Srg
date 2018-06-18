package net.minecraft.network.rcon;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RConOutputStream {

    private final ByteArrayOutputStream byteArrayOutput;
    private final DataOutputStream output;

    public RConOutputStream(int i) {
        this.byteArrayOutput = new ByteArrayOutputStream(i);
        this.output = new DataOutputStream(this.byteArrayOutput);
    }

    public void writeByteArray(byte[] abyte) throws IOException {
        this.output.write(abyte, 0, abyte.length);
    }

    public void writeString(String s) throws IOException {
        this.output.writeBytes(s);
        this.output.write(0);
    }

    public void writeInt(int i) throws IOException {
        this.output.write(i);
    }

    public void writeShort(short short0) throws IOException {
        this.output.writeShort(Short.reverseBytes(short0));
    }

    public byte[] toByteArray() {
        return this.byteArrayOutput.toByteArray();
    }

    public void reset() {
        this.byteArrayOutput.reset();
    }
}

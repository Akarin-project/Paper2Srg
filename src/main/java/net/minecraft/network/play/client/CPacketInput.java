package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketInput implements Packet<INetHandlerPlayServer> {

    private float field_149624_a;
    private float field_192621_b;
    private boolean field_149623_c;
    private boolean field_149621_d;

    public CPacketInput() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149624_a = packetdataserializer.readFloat();
        this.field_192621_b = packetdataserializer.readFloat();
        byte b0 = packetdataserializer.readByte();

        this.field_149623_c = (b0 & 1) > 0;
        this.field_149621_d = (b0 & 2) > 0;
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat(this.field_149624_a);
        packetdataserializer.writeFloat(this.field_192621_b);
        byte b0 = 0;

        if (this.field_149623_c) {
            b0 = (byte) (b0 | 1);
        }

        if (this.field_149621_d) {
            b0 = (byte) (b0 | 2);
        }

        packetdataserializer.writeByte(b0);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147358_a(this);
    }

    public float func_149620_c() {
        return this.field_149624_a;
    }

    public float func_192620_b() {
        return this.field_192621_b;
    }

    public boolean func_149618_e() {
        return this.field_149623_c;
    }

    public boolean func_149617_f() {
        return this.field_149621_d;
    }
}

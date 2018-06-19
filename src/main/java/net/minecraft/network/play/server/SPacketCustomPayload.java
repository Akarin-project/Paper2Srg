package net.minecraft.network.play.server;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCustomPayload implements Packet<INetHandlerPlayClient> {

    private String field_149172_a;
    private PacketBuffer field_149171_b;

    public SPacketCustomPayload() {}

    public SPacketCustomPayload(String s, PacketBuffer packetdataserializer) {
        this.field_149172_a = s;
        this.field_149171_b = packetdataserializer;
        if (packetdataserializer.writerIndex() > 1048576) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149172_a = packetdataserializer.func_150789_c(20);
        int i = packetdataserializer.readableBytes();

        if (i >= 0 && i <= 1048576) {
            this.field_149171_b = new PacketBuffer(packetdataserializer.readBytes(i));
        } else {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149172_a);
        packetdataserializer.writeBytes((ByteBuf) this.field_149171_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147240_a(this);
    }
}

package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCustomPayload implements Packet<INetHandlerPlayServer> {

    private String field_149562_a;
    private PacketBuffer field_149561_c;

    public CPacketCustomPayload() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149562_a = packetdataserializer.func_150789_c(20);
        int i = packetdataserializer.readableBytes();

        if (i >= 0 && i <= 32767) {
            this.field_149561_c = new PacketBuffer(packetdataserializer.readBytes(i));
        } else {
            throw new IOException("Payload may not be larger than 32767 bytes");
        }
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149562_a);
        packetdataserializer.writeBytes((ByteBuf) this.field_149561_c);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147349_a(this);
        if (this.field_149561_c != null) {
            this.field_149561_c.release();
        }

    }

    public String func_149559_c() {
        return this.field_149562_a;
    }

    public PacketBuffer func_180760_b() {
        return this.field_149561_c;
    }
}

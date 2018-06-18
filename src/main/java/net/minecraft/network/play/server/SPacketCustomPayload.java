package net.minecraft.network.play.server;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCustomPayload implements Packet<INetHandlerPlayClient> {

    private String channel;
    private PacketBuffer data;

    public SPacketCustomPayload() {}

    public SPacketCustomPayload(String s, PacketBuffer packetdataserializer) {
        this.channel = s;
        this.data = packetdataserializer;
        if (packetdataserializer.writerIndex() > 1048576) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.channel = packetdataserializer.readString(20);
        int i = packetdataserializer.readableBytes();

        if (i >= 0 && i <= 1048576) {
            this.data = new PacketBuffer(packetdataserializer.readBytes(i));
        } else {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.channel);
        packetdataserializer.writeBytes((ByteBuf) this.data);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleCustomPayload(this);
    }
}

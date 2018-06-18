package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCustomPayload implements Packet<INetHandlerPlayServer> {

    private String channel;
    private PacketBuffer data;

    public CPacketCustomPayload() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.channel = packetdataserializer.readString(20);
        int i = packetdataserializer.readableBytes();

        if (i >= 0 && i <= 32767) {
            this.data = new PacketBuffer(packetdataserializer.readBytes(i));
        } else {
            throw new IOException("Payload may not be larger than 32767 bytes");
        }
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.channel);
        packetdataserializer.writeBytes((ByteBuf) this.data);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processCustomPayload(this);
        if (this.data != null) {
            this.data.release();
        }

    }

    public String getChannelName() {
        return this.channel;
    }

    public PacketBuffer getBufferData() {
        return this.data;
    }
}

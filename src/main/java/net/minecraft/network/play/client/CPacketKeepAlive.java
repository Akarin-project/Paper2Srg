package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketKeepAlive implements Packet<INetHandlerPlayServer> {

    private long key;

    public CPacketKeepAlive() {}

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processKeepAlive(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.key = packetdataserializer.readLong();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.key);
    }

    public long getKey() {
        return this.key;
    }
}

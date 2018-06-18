package net.minecraft.network.status.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketPing implements Packet<INetHandlerStatusServer> {

    private long clientTime;

    public CPacketPing() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.clientTime = packetdataserializer.readLong();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.clientTime);
    }

    public void processPacket(INetHandlerStatusServer packetstatusinlistener) {
        packetstatusinlistener.processPing(this);
    }

    public long getClientTime() {
        return this.clientTime;
    }
}

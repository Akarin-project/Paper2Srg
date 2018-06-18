package net.minecraft.network.status.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusClient;

public class SPacketPong implements Packet<INetHandlerStatusClient> {

    private long clientTime;

    public SPacketPong() {}

    public SPacketPong(long i) {
        this.clientTime = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.clientTime = packetdataserializer.readLong();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.clientTime);
    }

    public void processPacket(INetHandlerStatusClient packetstatusoutlistener) {
        packetstatusoutlistener.handlePong(this);
    }
}

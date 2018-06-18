package net.minecraft.network.status.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketServerQuery implements Packet<INetHandlerStatusServer> {

    public CPacketServerQuery() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {}

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {}

    public void processPacket(INetHandlerStatusServer packetstatusinlistener) {
        packetstatusinlistener.processServerQuery(this);
    }
}

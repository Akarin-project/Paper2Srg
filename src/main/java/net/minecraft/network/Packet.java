package net.minecraft.network;

import java.io.IOException;

public interface Packet<T extends INetHandler> {

    void readPacketData(PacketBuffer packetdataserializer) throws IOException;

    void writePacketData(PacketBuffer packetdataserializer) throws IOException;

    void processPacket(T t0);
}

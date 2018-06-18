package net.minecraft.network.login.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketEnableCompression implements Packet<INetHandlerLoginClient> {

    private int compressionThreshold;

    public SPacketEnableCompression() {}

    public SPacketEnableCompression(int i) {
        this.compressionThreshold = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.compressionThreshold = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.compressionThreshold);
    }

    public void processPacket(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.handleEnableCompression(this);
    }
}

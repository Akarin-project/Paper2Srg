package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUnloadChunk implements Packet<INetHandlerPlayClient> {

    private int x;
    private int z;

    public SPacketUnloadChunk() {}

    public SPacketUnloadChunk(int i, int j) {
        this.x = i;
        this.z = j;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.x = packetdataserializer.readInt();
        this.z = packetdataserializer.readInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.x);
        packetdataserializer.writeInt(this.z);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.processChunkUnload(this);
    }
}

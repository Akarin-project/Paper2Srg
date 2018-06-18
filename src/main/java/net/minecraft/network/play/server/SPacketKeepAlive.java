package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketKeepAlive implements Packet<INetHandlerPlayClient> {

    private long id;

    public SPacketKeepAlive() {}

    public SPacketKeepAlive(long i) {
        this.id = i;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleKeepAlive(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.id = packetdataserializer.readLong();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.id);
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCloseWindow implements Packet<INetHandlerPlayClient> {

    private int windowId;

    public SPacketCloseWindow() {}

    public SPacketCloseWindow(int i) {
        this.windowId = i;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleCloseWindow(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
    }
}

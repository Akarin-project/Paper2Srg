package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketWindowProperty implements Packet<INetHandlerPlayClient> {

    private int windowId;
    private int property;
    private int value;

    public SPacketWindowProperty() {}

    public SPacketWindowProperty(int i, int j, int k) {
        this.windowId = i;
        this.property = j;
        this.value = k;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleWindowProperty(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readUnsignedByte();
        this.property = packetdataserializer.readShort();
        this.value = packetdataserializer.readShort();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeShort(this.property);
        packetdataserializer.writeShort(this.value);
    }
}

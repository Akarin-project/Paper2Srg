package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketHeldItemChange implements Packet<INetHandlerPlayClient> {

    private int heldItemHotbarIndex;

    public SPacketHeldItemChange() {}

    public SPacketHeldItemChange(int i) {
        this.heldItemHotbarIndex = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.heldItemHotbarIndex = packetdataserializer.readByte();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.heldItemHotbarIndex);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleHeldItemChange(this);
    }
}

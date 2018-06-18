package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketHeldItemChange implements Packet<INetHandlerPlayServer> {

    private int slotId;

    public CPacketHeldItemChange() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.slotId = packetdataserializer.readShort();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeShort(this.slotId);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processHeldItemChange(this);
    }

    public int getSlotId() {
        return this.slotId;
    }
}

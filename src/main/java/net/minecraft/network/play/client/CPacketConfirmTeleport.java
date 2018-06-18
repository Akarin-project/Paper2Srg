package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketConfirmTeleport implements Packet<INetHandlerPlayServer> {

    private int telportId;

    public CPacketConfirmTeleport() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.telportId = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.telportId);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processConfirmTeleport(this);
    }

    public int getTeleportId() {
        return this.telportId;
    }
}

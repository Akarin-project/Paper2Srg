package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClientStatus implements Packet<INetHandlerPlayServer> {

    private CPacketClientStatus.State status;

    public CPacketClientStatus() {}

    public CPacketClientStatus(CPacketClientStatus.State packetplayinclientcommand_enumclientcommand) {
        this.status = packetplayinclientcommand_enumclientcommand;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.status = (CPacketClientStatus.State) packetdataserializer.readEnumValue(CPacketClientStatus.State.class);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.status);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processClientStatus(this);
    }

    public CPacketClientStatus.State getStatus() {
        return this.status;
    }

    public static enum State {

        PERFORM_RESPAWN, REQUEST_STATS;

        private State() {}
    }
}

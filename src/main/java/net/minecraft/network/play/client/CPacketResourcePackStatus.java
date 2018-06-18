package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketResourcePackStatus implements Packet<INetHandlerPlayServer> {

    public CPacketResourcePackStatus.Action action;

    public CPacketResourcePackStatus() {}

    public CPacketResourcePackStatus(CPacketResourcePackStatus.Action packetplayinresourcepackstatus_enumresourcepackstatus) {
        this.action = packetplayinresourcepackstatus_enumresourcepackstatus;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.action = (CPacketResourcePackStatus.Action) packetdataserializer.readEnumValue(CPacketResourcePackStatus.Action.class);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.action);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.handleResourcePackStatus(this);
    }

    public static enum Action {

        SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED;

        private Action() {}
    }
}

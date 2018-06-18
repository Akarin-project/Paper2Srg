package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketConfirmTransaction implements Packet<INetHandlerPlayServer> {

    private int windowId;
    private short uid;
    private boolean accepted;

    public CPacketConfirmTransaction() {}

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processConfirmTransaction(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readByte();
        this.uid = packetdataserializer.readShort();
        this.accepted = packetdataserializer.readByte() != 0;
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeShort(this.uid);
        packetdataserializer.writeByte(this.accepted ? 1 : 0);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public short getUid() {
        return this.uid;
    }
}

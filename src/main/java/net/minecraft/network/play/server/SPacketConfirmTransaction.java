package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketConfirmTransaction implements Packet<INetHandlerPlayClient> {

    private int windowId;
    private short actionNumber;
    private boolean accepted;

    public SPacketConfirmTransaction() {}

    public SPacketConfirmTransaction(int i, short short0, boolean flag) {
        this.windowId = i;
        this.actionNumber = short0;
        this.accepted = flag;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleConfirmTransaction(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readUnsignedByte();
        this.actionNumber = packetdataserializer.readShort();
        this.accepted = packetdataserializer.readBoolean();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeShort(this.actionNumber);
        packetdataserializer.writeBoolean(this.accepted);
    }
}

package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketSteerBoat implements Packet<INetHandlerPlayServer> {

    private boolean left;
    private boolean right;

    public CPacketSteerBoat() {}

    public CPacketSteerBoat(boolean flag, boolean flag1) {
        this.left = flag;
        this.right = flag1;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.left = packetdataserializer.readBoolean();
        this.right = packetdataserializer.readBoolean();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBoolean(this.left);
        packetdataserializer.writeBoolean(this.right);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processSteerBoat(this);
    }

    public boolean getLeft() {
        return this.left;
    }

    public boolean getRight() {
        return this.right;
    }
}

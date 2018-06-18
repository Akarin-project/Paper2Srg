package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketEnchantItem implements Packet<INetHandlerPlayServer> {

    private int windowId;
    private int button;

    public CPacketEnchantItem() {}

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processEnchantItem(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readByte();
        this.button = packetdataserializer.readByte();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeByte(this.button);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getButton() {
        return this.button;
    }
}

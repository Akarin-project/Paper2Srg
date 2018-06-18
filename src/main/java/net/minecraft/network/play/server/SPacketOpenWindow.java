package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketOpenWindow implements Packet<INetHandlerPlayClient> {

    private int windowId;
    private String inventoryType;
    private ITextComponent windowTitle;
    private int slotCount;
    private int entityId;

    public SPacketOpenWindow() {}

    public SPacketOpenWindow(int i, String s, ITextComponent ichatbasecomponent) {
        this(i, s, ichatbasecomponent, 0);
    }

    public SPacketOpenWindow(int i, String s, ITextComponent ichatbasecomponent, int j) {
        this.windowId = i;
        this.inventoryType = s;
        this.windowTitle = ichatbasecomponent;
        this.slotCount = j;
    }

    public SPacketOpenWindow(int i, String s, ITextComponent ichatbasecomponent, int j, int k) {
        this(i, s, ichatbasecomponent, j);
        this.entityId = k;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleOpenWindow(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readUnsignedByte();
        this.inventoryType = packetdataserializer.readString(32);
        this.windowTitle = packetdataserializer.readTextComponent();
        this.slotCount = packetdataserializer.readUnsignedByte();
        if (this.inventoryType.equals("EntityHorse")) {
            this.entityId = packetdataserializer.readInt();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeString(this.inventoryType);
        packetdataserializer.writeTextComponent(this.windowTitle);
        packetdataserializer.writeByte(this.slotCount);
        if (this.inventoryType.equals("EntityHorse")) {
            packetdataserializer.writeInt(this.entityId);
        }

    }
}

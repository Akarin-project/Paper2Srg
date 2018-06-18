package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer> {

    private int windowId;
    private int slotId;
    private int packedClickData;
    private short actionNumber;
    private ItemStack clickedItem;
    private ClickType mode;

    public CPacketClickWindow() {
        this.clickedItem = ItemStack.EMPTY;
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processClickWindow(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readByte();
        this.slotId = packetdataserializer.readShort();
        this.packedClickData = packetdataserializer.readByte();
        this.actionNumber = packetdataserializer.readShort();
        this.mode = (ClickType) packetdataserializer.readEnumValue(ClickType.class);
        this.clickedItem = packetdataserializer.readItemStack();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeShort(this.slotId);
        packetdataserializer.writeByte(this.packedClickData);
        packetdataserializer.writeShort(this.actionNumber);
        packetdataserializer.writeEnumValue((Enum) this.mode);
        packetdataserializer.writeItemStack(this.clickedItem);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public int getUsedButton() {
        return this.packedClickData;
    }

    public short getActionNumber() {
        return this.actionNumber;
    }

    public ItemStack getClickedItem() {
        return this.clickedItem;
    }

    public ClickType getClickType() {
        return this.mode;
    }
}

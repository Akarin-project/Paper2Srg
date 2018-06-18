package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetSlot implements Packet<INetHandlerPlayClient> {

    private int windowId;
    private int slot;
    private ItemStack item;

    public SPacketSetSlot() {
        this.item = ItemStack.EMPTY;
    }

    public SPacketSetSlot(int i, int j, ItemStack itemstack) {
        this.item = ItemStack.EMPTY;
        this.windowId = i;
        this.slot = j;
        this.item = itemstack.copy();
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSetSlot(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readByte();
        this.slot = packetdataserializer.readShort();
        this.item = packetdataserializer.readItemStack();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeShort(this.slot);
        packetdataserializer.writeItemStack(this.item);
    }
}

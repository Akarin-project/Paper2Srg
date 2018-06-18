package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCreativeInventoryAction implements Packet<INetHandlerPlayServer> {

    private int slotId;
    private ItemStack stack;

    public CPacketCreativeInventoryAction() {
        this.stack = ItemStack.EMPTY;
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processCreativeInventoryAction(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.slotId = packetdataserializer.readShort();
        this.stack = packetdataserializer.readItemStack();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeShort(this.slotId);
        packetdataserializer.writeItemStack(this.stack);
    }

    public int getSlotId() {
        return this.slotId;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}

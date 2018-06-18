package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.NonNullList;

public class SPacketWindowItems implements Packet<INetHandlerPlayClient> {

    private int windowId;
    private List<ItemStack> itemStacks;

    public SPacketWindowItems() {}

    public SPacketWindowItems(int i, NonNullList<ItemStack> nonnulllist) {
        this.windowId = i;
        this.itemStacks = NonNullList.withSize(nonnulllist.size(), ItemStack.EMPTY);

        for (int j = 0; j < this.itemStacks.size(); ++j) {
            ItemStack itemstack = (ItemStack) nonnulllist.get(j);

            this.itemStacks.set(j, itemstack.copy());
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.windowId = packetdataserializer.readUnsignedByte();
        short short0 = packetdataserializer.readShort();

        this.itemStacks = NonNullList.withSize(short0, ItemStack.EMPTY);

        for (int i = 0; i < short0; ++i) {
            this.itemStacks.set(i, packetdataserializer.readItemStack());
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.windowId);
        packetdataserializer.writeShort(this.itemStacks.size());
        Iterator iterator = this.itemStacks.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            packetdataserializer.writeItemStack(itemstack);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleWindowItems(this);
    }
}

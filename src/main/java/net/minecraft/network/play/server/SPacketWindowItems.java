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

    private int field_148914_a;
    private List<ItemStack> field_148913_b;

    public SPacketWindowItems() {}

    public SPacketWindowItems(int i, NonNullList<ItemStack> nonnulllist) {
        this.field_148914_a = i;
        this.field_148913_b = NonNullList.func_191197_a(nonnulllist.size(), ItemStack.field_190927_a);

        for (int j = 0; j < this.field_148913_b.size(); ++j) {
            ItemStack itemstack = (ItemStack) nonnulllist.get(j);

            this.field_148913_b.set(j, itemstack.func_77946_l());
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148914_a = packetdataserializer.readUnsignedByte();
        short short0 = packetdataserializer.readShort();

        this.field_148913_b = NonNullList.func_191197_a(short0, ItemStack.field_190927_a);

        for (int i = 0; i < short0; ++i) {
            this.field_148913_b.set(i, packetdataserializer.func_150791_c());
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_148914_a);
        packetdataserializer.writeShort(this.field_148913_b.size());
        Iterator iterator = this.field_148913_b.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            packetdataserializer.func_150788_a(itemstack);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147241_a(this);
    }
}

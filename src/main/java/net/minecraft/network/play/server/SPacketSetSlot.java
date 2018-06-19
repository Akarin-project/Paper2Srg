package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetSlot implements Packet<INetHandlerPlayClient> {

    private int field_149179_a;
    private int field_149177_b;
    private ItemStack field_149178_c;

    public SPacketSetSlot() {
        this.field_149178_c = ItemStack.field_190927_a;
    }

    public SPacketSetSlot(int i, int j, ItemStack itemstack) {
        this.field_149178_c = ItemStack.field_190927_a;
        this.field_149179_a = i;
        this.field_149177_b = j;
        this.field_149178_c = itemstack.func_77946_l();
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147266_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149179_a = packetdataserializer.readByte();
        this.field_149177_b = packetdataserializer.readShort();
        this.field_149178_c = packetdataserializer.func_150791_c();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149179_a);
        packetdataserializer.writeShort(this.field_149177_b);
        packetdataserializer.func_150788_a(this.field_149178_c);
    }
}

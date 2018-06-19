package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCreativeInventoryAction implements Packet<INetHandlerPlayServer> {

    private int field_149629_a;
    private ItemStack field_149628_b;

    public CPacketCreativeInventoryAction() {
        this.field_149628_b = ItemStack.field_190927_a;
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147344_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149629_a = packetdataserializer.readShort();
        this.field_149628_b = packetdataserializer.func_150791_c();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeShort(this.field_149629_a);
        packetdataserializer.func_150788_a(this.field_149628_b);
    }

    public int func_149627_c() {
        return this.field_149629_a;
    }

    public ItemStack func_149625_d() {
        return this.field_149628_b;
    }
}

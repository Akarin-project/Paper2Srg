package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCooldown implements Packet<INetHandlerPlayClient> {

    private Item field_186923_a;
    private int field_186924_b;

    public SPacketCooldown() {}

    public SPacketCooldown(Item item, int i) {
        this.field_186923_a = item;
        this.field_186924_b = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_186923_a = Item.func_150899_d(packetdataserializer.func_150792_a());
        this.field_186924_b = packetdataserializer.func_150792_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(Item.func_150891_b(this.field_186923_a));
        packetdataserializer.func_150787_b(this.field_186924_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_184324_a(this);
    }
}

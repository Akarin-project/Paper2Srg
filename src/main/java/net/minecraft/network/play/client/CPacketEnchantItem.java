package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketEnchantItem implements Packet<INetHandlerPlayServer> {

    private int field_149541_a;
    private int field_149540_b;

    public CPacketEnchantItem() {}

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147338_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149541_a = packetdataserializer.readByte();
        this.field_149540_b = packetdataserializer.readByte();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149541_a);
        packetdataserializer.writeByte(this.field_149540_b);
    }

    public int func_149539_c() {
        return this.field_149541_a;
    }

    public int func_149537_d() {
        return this.field_149540_b;
    }
}

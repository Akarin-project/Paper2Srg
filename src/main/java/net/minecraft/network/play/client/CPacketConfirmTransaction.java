package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketConfirmTransaction implements Packet<INetHandlerPlayServer> {

    private int field_149536_a;
    private short field_149534_b;
    private boolean field_149535_c;

    public CPacketConfirmTransaction() {}

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147339_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149536_a = packetdataserializer.readByte();
        this.field_149534_b = packetdataserializer.readShort();
        this.field_149535_c = packetdataserializer.readByte() != 0;
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149536_a);
        packetdataserializer.writeShort(this.field_149534_b);
        packetdataserializer.writeByte(this.field_149535_c ? 1 : 0);
    }

    public int func_149532_c() {
        return this.field_149536_a;
    }

    public short func_149533_d() {
        return this.field_149534_b;
    }
}

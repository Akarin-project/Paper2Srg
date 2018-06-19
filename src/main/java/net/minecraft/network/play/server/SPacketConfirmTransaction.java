package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketConfirmTransaction implements Packet<INetHandlerPlayClient> {

    private int field_148894_a;
    private short field_148892_b;
    private boolean field_148893_c;

    public SPacketConfirmTransaction() {}

    public SPacketConfirmTransaction(int i, short short0, boolean flag) {
        this.field_148894_a = i;
        this.field_148892_b = short0;
        this.field_148893_c = flag;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147239_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148894_a = packetdataserializer.readUnsignedByte();
        this.field_148892_b = packetdataserializer.readShort();
        this.field_148893_c = packetdataserializer.readBoolean();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_148894_a);
        packetdataserializer.writeShort(this.field_148892_b);
        packetdataserializer.writeBoolean(this.field_148893_c);
    }
}

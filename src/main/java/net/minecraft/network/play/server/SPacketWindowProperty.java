package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketWindowProperty implements Packet<INetHandlerPlayClient> {

    private int field_149186_a;
    private int field_149184_b;
    private int field_149185_c;

    public SPacketWindowProperty() {}

    public SPacketWindowProperty(int i, int j, int k) {
        this.field_149186_a = i;
        this.field_149184_b = j;
        this.field_149185_c = k;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147245_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149186_a = packetdataserializer.readUnsignedByte();
        this.field_149184_b = packetdataserializer.readShort();
        this.field_149185_c = packetdataserializer.readShort();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149186_a);
        packetdataserializer.writeShort(this.field_149184_b);
        packetdataserializer.writeShort(this.field_149185_c);
    }
}

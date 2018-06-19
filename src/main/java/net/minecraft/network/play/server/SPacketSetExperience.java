package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetExperience implements Packet<INetHandlerPlayClient> {

    private float field_149401_a;
    private int field_149399_b;
    private int field_149400_c;

    public SPacketSetExperience() {}

    public SPacketSetExperience(float f, int i, int j) {
        this.field_149401_a = f;
        this.field_149399_b = i;
        this.field_149400_c = j;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149401_a = packetdataserializer.readFloat();
        this.field_149400_c = packetdataserializer.func_150792_a();
        this.field_149399_b = packetdataserializer.func_150792_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat(this.field_149401_a);
        packetdataserializer.func_150787_b(this.field_149400_c);
        packetdataserializer.func_150787_b(this.field_149399_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147295_a(this);
    }
}

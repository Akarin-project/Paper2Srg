package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUpdateHealth implements Packet<INetHandlerPlayClient> {

    private float field_149336_a;
    private int field_149334_b;
    private float field_149335_c;

    public SPacketUpdateHealth() {}

    public SPacketUpdateHealth(float f, int i, float f1) {
        this.field_149336_a = f;
        this.field_149334_b = i;
        this.field_149335_c = f1;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149336_a = packetdataserializer.readFloat();
        this.field_149334_b = packetdataserializer.func_150792_a();
        this.field_149335_c = packetdataserializer.readFloat();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat(this.field_149336_a);
        packetdataserializer.func_150787_b(this.field_149334_b);
        packetdataserializer.writeFloat(this.field_149335_c);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147249_a(this);
    }
}

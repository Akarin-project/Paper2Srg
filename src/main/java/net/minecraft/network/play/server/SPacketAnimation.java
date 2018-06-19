package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketAnimation implements Packet<INetHandlerPlayClient> {

    private int field_148981_a;
    private int field_148980_b;

    public SPacketAnimation() {}

    public SPacketAnimation(Entity entity, int i) {
        this.field_148981_a = entity.func_145782_y();
        this.field_148980_b = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148981_a = packetdataserializer.func_150792_a();
        this.field_148980_b = packetdataserializer.readUnsignedByte();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_148981_a);
        packetdataserializer.writeByte(this.field_148980_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147279_a(this);
    }
}

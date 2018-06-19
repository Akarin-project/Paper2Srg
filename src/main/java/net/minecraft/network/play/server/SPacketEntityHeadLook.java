package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityHeadLook implements Packet<INetHandlerPlayClient> {

    private int field_149384_a;
    private byte field_149383_b;

    public SPacketEntityHeadLook() {}

    public SPacketEntityHeadLook(Entity entity, byte b0) {
        this.field_149384_a = entity.func_145782_y();
        this.field_149383_b = b0;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149384_a = packetdataserializer.func_150792_a();
        this.field_149383_b = packetdataserializer.readByte();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149384_a);
        packetdataserializer.writeByte(this.field_149383_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147267_a(this);
    }
}

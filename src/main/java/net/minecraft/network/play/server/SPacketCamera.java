package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCamera implements Packet<INetHandlerPlayClient> {

    public int field_179781_a;

    public SPacketCamera() {}

    public SPacketCamera(Entity entity) {
        this.field_179781_a = entity.func_145782_y();
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179781_a = packetdataserializer.func_150792_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_179781_a);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_175094_a(this);
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityStatus implements Packet<INetHandlerPlayClient> {

    private int field_149164_a;
    private byte field_149163_b;

    public SPacketEntityStatus() {}

    public SPacketEntityStatus(Entity entity, byte b0) {
        this.field_149164_a = entity.func_145782_y();
        this.field_149163_b = b0;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149164_a = packetdataserializer.readInt();
        this.field_149163_b = packetdataserializer.readByte();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_149164_a);
        packetdataserializer.writeByte(this.field_149163_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147236_a(this);
    }
}

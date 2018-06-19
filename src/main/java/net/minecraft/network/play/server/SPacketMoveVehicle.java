package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketMoveVehicle implements Packet<INetHandlerPlayClient> {

    private double field_186960_a;
    private double field_186961_b;
    private double field_186962_c;
    private float field_186963_d;
    private float field_186964_e;

    public SPacketMoveVehicle() {}

    public SPacketMoveVehicle(Entity entity) {
        this.field_186960_a = entity.field_70165_t;
        this.field_186961_b = entity.field_70163_u;
        this.field_186962_c = entity.field_70161_v;
        this.field_186963_d = entity.field_70177_z;
        this.field_186964_e = entity.field_70125_A;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_186960_a = packetdataserializer.readDouble();
        this.field_186961_b = packetdataserializer.readDouble();
        this.field_186962_c = packetdataserializer.readDouble();
        this.field_186963_d = packetdataserializer.readFloat();
        this.field_186964_e = packetdataserializer.readFloat();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeDouble(this.field_186960_a);
        packetdataserializer.writeDouble(this.field_186961_b);
        packetdataserializer.writeDouble(this.field_186962_c);
        packetdataserializer.writeFloat(this.field_186963_d);
        packetdataserializer.writeFloat(this.field_186964_e);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_184323_a(this);
    }
}

package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketVehicleMove implements Packet<INetHandlerPlayServer> {

    private double field_187007_a;
    private double field_187008_b;
    private double field_187009_c;
    private float field_187010_d;
    private float field_187011_e;

    public CPacketVehicleMove() {}

    public CPacketVehicleMove(Entity entity) {
        this.field_187007_a = entity.field_70165_t;
        this.field_187008_b = entity.field_70163_u;
        this.field_187009_c = entity.field_70161_v;
        this.field_187010_d = entity.field_70177_z;
        this.field_187011_e = entity.field_70125_A;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_187007_a = packetdataserializer.readDouble();
        this.field_187008_b = packetdataserializer.readDouble();
        this.field_187009_c = packetdataserializer.readDouble();
        this.field_187010_d = packetdataserializer.readFloat();
        this.field_187011_e = packetdataserializer.readFloat();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeDouble(this.field_187007_a);
        packetdataserializer.writeDouble(this.field_187008_b);
        packetdataserializer.writeDouble(this.field_187009_c);
        packetdataserializer.writeFloat(this.field_187010_d);
        packetdataserializer.writeFloat(this.field_187011_e);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_184338_a(this);
    }

    public double func_187004_a() {
        return this.field_187007_a;
    }

    public double func_187002_b() {
        return this.field_187008_b;
    }

    public double func_187003_c() {
        return this.field_187009_c;
    }

    public float func_187006_d() {
        return this.field_187010_d;
    }

    public float func_187005_e() {
        return this.field_187011_e;
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnGlobalEntity implements Packet<INetHandlerPlayClient> {

    private int field_149059_a;
    private double field_149057_b;
    private double field_149058_c;
    private double field_149055_d;
    private int field_149056_e;

    public SPacketSpawnGlobalEntity() {}

    public SPacketSpawnGlobalEntity(Entity entity) {
        this.field_149059_a = entity.func_145782_y();
        this.field_149057_b = entity.field_70165_t;
        this.field_149058_c = entity.field_70163_u;
        this.field_149055_d = entity.field_70161_v;
        if (entity instanceof EntityLightningBolt) {
            this.field_149056_e = 1;
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149059_a = packetdataserializer.func_150792_a();
        this.field_149056_e = packetdataserializer.readByte();
        this.field_149057_b = packetdataserializer.readDouble();
        this.field_149058_c = packetdataserializer.readDouble();
        this.field_149055_d = packetdataserializer.readDouble();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149059_a);
        packetdataserializer.writeByte(this.field_149056_e);
        packetdataserializer.writeDouble(this.field_149057_b);
        packetdataserializer.writeDouble(this.field_149058_c);
        packetdataserializer.writeDouble(this.field_149055_d);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147292_a(this);
    }
}

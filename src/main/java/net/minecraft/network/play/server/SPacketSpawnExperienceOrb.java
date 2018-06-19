package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnExperienceOrb implements Packet<INetHandlerPlayClient> {

    private int field_148992_a;
    private double field_148990_b;
    private double field_148991_c;
    private double field_148988_d;
    private int field_148989_e;

    public SPacketSpawnExperienceOrb() {}

    public SPacketSpawnExperienceOrb(EntityXPOrb entityexperienceorb) {
        this.field_148992_a = entityexperienceorb.func_145782_y();
        this.field_148990_b = entityexperienceorb.field_70165_t;
        this.field_148991_c = entityexperienceorb.field_70163_u;
        this.field_148988_d = entityexperienceorb.field_70161_v;
        this.field_148989_e = entityexperienceorb.func_70526_d();
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148992_a = packetdataserializer.func_150792_a();
        this.field_148990_b = packetdataserializer.readDouble();
        this.field_148991_c = packetdataserializer.readDouble();
        this.field_148988_d = packetdataserializer.readDouble();
        this.field_148989_e = packetdataserializer.readShort();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_148992_a);
        packetdataserializer.writeDouble(this.field_148990_b);
        packetdataserializer.writeDouble(this.field_148991_c);
        packetdataserializer.writeDouble(this.field_148988_d);
        packetdataserializer.writeShort(this.field_148989_e);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147286_a(this);
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityTeleport implements Packet<INetHandlerPlayClient> {

    private int field_149458_a;
    private double field_149456_b;
    private double field_149457_c;
    private double field_149454_d;
    private byte field_149455_e;
    private byte field_149453_f;
    private boolean field_179698_g;

    public SPacketEntityTeleport() {}

    public SPacketEntityTeleport(Entity entity) {
        this.field_149458_a = entity.func_145782_y();
        this.field_149456_b = entity.field_70165_t;
        this.field_149457_c = entity.field_70163_u;
        this.field_149454_d = entity.field_70161_v;
        this.field_149455_e = (byte) ((int) (entity.field_70177_z * 256.0F / 360.0F));
        this.field_149453_f = (byte) ((int) (entity.field_70125_A * 256.0F / 360.0F));
        this.field_179698_g = entity.field_70122_E;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149458_a = packetdataserializer.func_150792_a();
        this.field_149456_b = packetdataserializer.readDouble();
        this.field_149457_c = packetdataserializer.readDouble();
        this.field_149454_d = packetdataserializer.readDouble();
        this.field_149455_e = packetdataserializer.readByte();
        this.field_149453_f = packetdataserializer.readByte();
        this.field_179698_g = packetdataserializer.readBoolean();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149458_a);
        packetdataserializer.writeDouble(this.field_149456_b);
        packetdataserializer.writeDouble(this.field_149457_c);
        packetdataserializer.writeDouble(this.field_149454_d);
        packetdataserializer.writeByte(this.field_149455_e);
        packetdataserializer.writeByte(this.field_149453_f);
        packetdataserializer.writeBoolean(this.field_179698_g);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147275_a(this);
    }
}

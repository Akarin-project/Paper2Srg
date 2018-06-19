package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnPlayer implements Packet<INetHandlerPlayClient> {

    private int field_148957_a;
    private UUID field_179820_b;
    private double field_148956_c;
    private double field_148953_d;
    private double field_148954_e;
    private byte field_148951_f;
    private byte field_148952_g;
    private EntityDataManager field_148960_i;
    private List<EntityDataManager.DataEntry<?>> field_148958_j;

    public SPacketSpawnPlayer() {}

    public SPacketSpawnPlayer(EntityPlayer entityhuman) {
        this.field_148957_a = entityhuman.func_145782_y();
        this.field_179820_b = entityhuman.func_146103_bH().getId();
        this.field_148956_c = entityhuman.field_70165_t;
        this.field_148953_d = entityhuman.field_70163_u;
        this.field_148954_e = entityhuman.field_70161_v;
        this.field_148951_f = (byte) ((int) (entityhuman.field_70177_z * 256.0F / 360.0F));
        this.field_148952_g = (byte) ((int) (entityhuman.field_70125_A * 256.0F / 360.0F));
        this.field_148960_i = entityhuman.func_184212_Q();
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148957_a = packetdataserializer.func_150792_a();
        this.field_179820_b = packetdataserializer.func_179253_g();
        this.field_148956_c = packetdataserializer.readDouble();
        this.field_148953_d = packetdataserializer.readDouble();
        this.field_148954_e = packetdataserializer.readDouble();
        this.field_148951_f = packetdataserializer.readByte();
        this.field_148952_g = packetdataserializer.readByte();
        this.field_148958_j = EntityDataManager.func_187215_b(packetdataserializer);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_148957_a);
        packetdataserializer.func_179252_a(this.field_179820_b);
        packetdataserializer.writeDouble(this.field_148956_c);
        packetdataserializer.writeDouble(this.field_148953_d);
        packetdataserializer.writeDouble(this.field_148954_e);
        packetdataserializer.writeByte(this.field_148951_f);
        packetdataserializer.writeByte(this.field_148952_g);
        this.field_148960_i.func_187216_a(packetdataserializer);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147237_a(this);
    }
}

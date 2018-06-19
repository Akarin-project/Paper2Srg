package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SPacketSpawnObject implements Packet<INetHandlerPlayClient> {

    private int field_149018_a;
    private UUID field_186883_b;
    private double field_149016_b;
    private double field_149017_c;
    private double field_149014_d;
    private int field_149015_e;
    private int field_149012_f;
    private int field_149013_g;
    private int field_149021_h;
    private int field_149022_i;
    private int field_149019_j;
    private int field_149020_k;

    public SPacketSpawnObject() {}

    public SPacketSpawnObject(Entity entity, int i) {
        this(entity, i, 0);
    }

    public SPacketSpawnObject(Entity entity, int i, int j) {
        this.field_149018_a = entity.func_145782_y();
        this.field_186883_b = entity.func_110124_au();
        this.field_149016_b = entity.field_70165_t;
        this.field_149017_c = entity.field_70163_u;
        this.field_149014_d = entity.field_70161_v;
        this.field_149021_h = MathHelper.func_76141_d(entity.field_70125_A * 256.0F / 360.0F);
        this.field_149022_i = MathHelper.func_76141_d(entity.field_70177_z * 256.0F / 360.0F);
        this.field_149019_j = i;
        this.field_149020_k = j;
        double d0 = 3.9D;

        this.field_149015_e = (int) (MathHelper.func_151237_a(entity.field_70159_w, -3.9D, 3.9D) * 8000.0D);
        this.field_149012_f = (int) (MathHelper.func_151237_a(entity.field_70181_x, -3.9D, 3.9D) * 8000.0D);
        this.field_149013_g = (int) (MathHelper.func_151237_a(entity.field_70179_y, -3.9D, 3.9D) * 8000.0D);
    }

    public SPacketSpawnObject(Entity entity, int i, int j, BlockPos blockposition) {
        this(entity, i, j);
        this.field_149016_b = (double) blockposition.func_177958_n();
        this.field_149017_c = (double) blockposition.func_177956_o();
        this.field_149014_d = (double) blockposition.func_177952_p();
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149018_a = packetdataserializer.func_150792_a();
        this.field_186883_b = packetdataserializer.func_179253_g();
        this.field_149019_j = packetdataserializer.readByte();
        this.field_149016_b = packetdataserializer.readDouble();
        this.field_149017_c = packetdataserializer.readDouble();
        this.field_149014_d = packetdataserializer.readDouble();
        this.field_149021_h = packetdataserializer.readByte();
        this.field_149022_i = packetdataserializer.readByte();
        this.field_149020_k = packetdataserializer.readInt();
        this.field_149015_e = packetdataserializer.readShort();
        this.field_149012_f = packetdataserializer.readShort();
        this.field_149013_g = packetdataserializer.readShort();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149018_a);
        packetdataserializer.func_179252_a(this.field_186883_b);
        packetdataserializer.writeByte(this.field_149019_j);
        packetdataserializer.writeDouble(this.field_149016_b);
        packetdataserializer.writeDouble(this.field_149017_c);
        packetdataserializer.writeDouble(this.field_149014_d);
        packetdataserializer.writeByte(this.field_149021_h);
        packetdataserializer.writeByte(this.field_149022_i);
        packetdataserializer.writeInt(this.field_149020_k);
        packetdataserializer.writeShort(this.field_149015_e);
        packetdataserializer.writeShort(this.field_149012_f);
        packetdataserializer.writeShort(this.field_149013_g);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147235_a(this);
    }

    public void func_149003_d(int i) {
        this.field_149015_e = i;
    }

    public void func_149000_e(int i) {
        this.field_149012_f = i;
    }

    public void func_149007_f(int i) {
        this.field_149013_g = i;
    }
}

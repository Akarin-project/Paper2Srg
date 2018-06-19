package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnMob implements Packet<INetHandlerPlayClient> {

    private int field_149042_a;
    private UUID field_186894_b;
    private int field_149040_b;
    private double field_149041_c;
    private double field_149038_d;
    private double field_149039_e;
    private int field_149036_f;
    private int field_149037_g;
    private int field_149047_h;
    private byte field_149048_i;
    private byte field_149045_j;
    private byte field_149046_k;
    private EntityDataManager field_149043_l;
    private List<EntityDataManager.DataEntry<?>> field_149044_m;

    public SPacketSpawnMob() {}

    public SPacketSpawnMob(EntityLivingBase entityliving) {
        this.field_149042_a = entityliving.func_145782_y();
        this.field_186894_b = entityliving.func_110124_au();
        this.field_149040_b = EntityList.field_191308_b.func_148757_b((Object) entityliving.getClass());
        this.field_149041_c = entityliving.field_70165_t;
        this.field_149038_d = entityliving.field_70163_u;
        this.field_149039_e = entityliving.field_70161_v;
        this.field_149048_i = (byte) ((int) (entityliving.field_70177_z * 256.0F / 360.0F));
        this.field_149045_j = (byte) ((int) (entityliving.field_70125_A * 256.0F / 360.0F));
        this.field_149046_k = (byte) ((int) (entityliving.field_70759_as * 256.0F / 360.0F));
        double d0 = 3.9D;
        double d1 = entityliving.field_70159_w;
        double d2 = entityliving.field_70181_x;
        double d3 = entityliving.field_70179_y;

        if (d1 < -3.9D) {
            d1 = -3.9D;
        }

        if (d2 < -3.9D) {
            d2 = -3.9D;
        }

        if (d3 < -3.9D) {
            d3 = -3.9D;
        }

        if (d1 > 3.9D) {
            d1 = 3.9D;
        }

        if (d2 > 3.9D) {
            d2 = 3.9D;
        }

        if (d3 > 3.9D) {
            d3 = 3.9D;
        }

        this.field_149036_f = (int) (d1 * 8000.0D);
        this.field_149037_g = (int) (d2 * 8000.0D);
        this.field_149047_h = (int) (d3 * 8000.0D);
        this.field_149043_l = entityliving.func_184212_Q();
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149042_a = packetdataserializer.func_150792_a();
        this.field_186894_b = packetdataserializer.func_179253_g();
        this.field_149040_b = packetdataserializer.func_150792_a();
        this.field_149041_c = packetdataserializer.readDouble();
        this.field_149038_d = packetdataserializer.readDouble();
        this.field_149039_e = packetdataserializer.readDouble();
        this.field_149048_i = packetdataserializer.readByte();
        this.field_149045_j = packetdataserializer.readByte();
        this.field_149046_k = packetdataserializer.readByte();
        this.field_149036_f = packetdataserializer.readShort();
        this.field_149037_g = packetdataserializer.readShort();
        this.field_149047_h = packetdataserializer.readShort();
        this.field_149044_m = EntityDataManager.func_187215_b(packetdataserializer);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149042_a);
        packetdataserializer.func_179252_a(this.field_186894_b);
        packetdataserializer.func_150787_b(this.field_149040_b);
        packetdataserializer.writeDouble(this.field_149041_c);
        packetdataserializer.writeDouble(this.field_149038_d);
        packetdataserializer.writeDouble(this.field_149039_e);
        packetdataserializer.writeByte(this.field_149048_i);
        packetdataserializer.writeByte(this.field_149045_j);
        packetdataserializer.writeByte(this.field_149046_k);
        packetdataserializer.writeShort(this.field_149036_f);
        packetdataserializer.writeShort(this.field_149037_g);
        packetdataserializer.writeShort(this.field_149047_h);
        this.field_149043_l.func_187216_a(packetdataserializer);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147281_a(this);
    }
}

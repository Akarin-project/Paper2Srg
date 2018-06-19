package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityVelocity implements Packet<INetHandlerPlayClient> {

    private int field_149417_a;
    private int field_149415_b;
    private int field_149416_c;
    private int field_149414_d;

    public SPacketEntityVelocity() {}

    public SPacketEntityVelocity(Entity entity) {
        this(entity.func_145782_y(), entity.field_70159_w, entity.field_70181_x, entity.field_70179_y);
    }

    public SPacketEntityVelocity(int i, double d0, double d1, double d2) {
        this.field_149417_a = i;
        double d3 = 3.9D;

        if (d0 < -3.9D) {
            d0 = -3.9D;
        }

        if (d1 < -3.9D) {
            d1 = -3.9D;
        }

        if (d2 < -3.9D) {
            d2 = -3.9D;
        }

        if (d0 > 3.9D) {
            d0 = 3.9D;
        }

        if (d1 > 3.9D) {
            d1 = 3.9D;
        }

        if (d2 > 3.9D) {
            d2 = 3.9D;
        }

        this.field_149415_b = (int) (d0 * 8000.0D);
        this.field_149416_c = (int) (d1 * 8000.0D);
        this.field_149414_d = (int) (d2 * 8000.0D);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149417_a = packetdataserializer.func_150792_a();
        this.field_149415_b = packetdataserializer.readShort();
        this.field_149416_c = packetdataserializer.readShort();
        this.field_149414_d = packetdataserializer.readShort();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149417_a);
        packetdataserializer.writeShort(this.field_149415_b);
        packetdataserializer.writeShort(this.field_149416_c);
        packetdataserializer.writeShort(this.field_149414_d);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147244_a(this);
    }
}

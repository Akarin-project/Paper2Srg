package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetPassengers implements Packet<INetHandlerPlayClient> {

    private int field_186973_a;
    private int[] field_186974_b;

    public SPacketSetPassengers() {}

    public SPacketSetPassengers(Entity entity) {
        this.field_186973_a = entity.func_145782_y();
        List list = entity.func_184188_bt();

        this.field_186974_b = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            this.field_186974_b[i] = ((Entity) list.get(i)).func_145782_y();
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_186973_a = packetdataserializer.func_150792_a();
        this.field_186974_b = packetdataserializer.func_186863_b();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_186973_a);
        packetdataserializer.func_186875_a(this.field_186974_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_184328_a(this);
    }
}

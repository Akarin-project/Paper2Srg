package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCollectItem implements Packet<INetHandlerPlayClient> {

    private int field_149357_a;
    private int field_149356_b;
    private int field_191209_c;

    public SPacketCollectItem() {}

    public SPacketCollectItem(int i, int j, int k) {
        this.field_149357_a = i;
        this.field_149356_b = j;
        this.field_191209_c = k;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149357_a = packetdataserializer.func_150792_a();
        this.field_149356_b = packetdataserializer.func_150792_a();
        this.field_191209_c = packetdataserializer.func_150792_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149357_a);
        packetdataserializer.func_150787_b(this.field_149356_b);
        packetdataserializer.func_150787_b(this.field_191209_c);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147246_a(this);
    }
}

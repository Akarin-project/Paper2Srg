package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SPacketEntityEffect implements Packet<INetHandlerPlayClient> {

    private int field_149434_a;
    private byte field_149432_b;
    private byte field_149433_c;
    private int field_149431_d;
    private byte field_186985_e;

    public SPacketEntityEffect() {}

    public SPacketEntityEffect(int i, PotionEffect mobeffect) {
        this.field_149434_a = i;
        this.field_149432_b = (byte) (Potion.func_188409_a(mobeffect.func_188419_a()) & 255);
        this.field_149433_c = (byte) (mobeffect.func_76458_c() & 255);
        if (mobeffect.func_76459_b() > 32767) {
            this.field_149431_d = 32767;
        } else {
            this.field_149431_d = mobeffect.func_76459_b();
        }

        this.field_186985_e = 0;
        if (mobeffect.func_82720_e()) {
            this.field_186985_e = (byte) (this.field_186985_e | 1);
        }

        if (mobeffect.func_188418_e()) {
            this.field_186985_e = (byte) (this.field_186985_e | 2);
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149434_a = packetdataserializer.func_150792_a();
        this.field_149432_b = packetdataserializer.readByte();
        this.field_149433_c = packetdataserializer.readByte();
        this.field_149431_d = packetdataserializer.func_150792_a();
        this.field_186985_e = packetdataserializer.readByte();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149434_a);
        packetdataserializer.writeByte(this.field_149432_b);
        packetdataserializer.writeByte(this.field_149433_c);
        packetdataserializer.func_150787_b(this.field_149431_d);
        packetdataserializer.writeByte(this.field_186985_e);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147260_a(this);
    }
}

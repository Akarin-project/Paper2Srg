package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;

public class SPacketParticles implements Packet<INetHandlerPlayClient> {

    private EnumParticleTypes field_179751_a;
    private float field_149234_b;
    private float field_149235_c;
    private float field_149232_d;
    private float field_149233_e;
    private float field_149230_f;
    private float field_149231_g;
    private float field_149237_h;
    private int field_149238_i;
    private boolean field_179752_j;
    private int[] field_179753_k;

    public SPacketParticles() {}

    public SPacketParticles(EnumParticleTypes enumparticle, boolean flag, float f, float f1, float f2, float f3, float f4, float f5, float f6, int i, int... aint) {
        this.field_179751_a = enumparticle;
        this.field_179752_j = flag;
        this.field_149234_b = f;
        this.field_149235_c = f1;
        this.field_149232_d = f2;
        this.field_149233_e = f3;
        this.field_149230_f = f4;
        this.field_149231_g = f5;
        this.field_149237_h = f6;
        this.field_149238_i = i;
        this.field_179753_k = aint;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179751_a = EnumParticleTypes.func_179342_a(packetdataserializer.readInt());
        if (this.field_179751_a == null) {
            this.field_179751_a = EnumParticleTypes.BARRIER;
        }

        this.field_179752_j = packetdataserializer.readBoolean();
        this.field_149234_b = packetdataserializer.readFloat();
        this.field_149235_c = packetdataserializer.readFloat();
        this.field_149232_d = packetdataserializer.readFloat();
        this.field_149233_e = packetdataserializer.readFloat();
        this.field_149230_f = packetdataserializer.readFloat();
        this.field_149231_g = packetdataserializer.readFloat();
        this.field_149237_h = packetdataserializer.readFloat();
        this.field_149238_i = packetdataserializer.readInt();
        int i = this.field_179751_a.func_179345_d();

        this.field_179753_k = new int[i];

        for (int j = 0; j < i; ++j) {
            this.field_179753_k[j] = packetdataserializer.func_150792_a();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_179751_a.func_179348_c());
        packetdataserializer.writeBoolean(this.field_179752_j);
        packetdataserializer.writeFloat(this.field_149234_b);
        packetdataserializer.writeFloat(this.field_149235_c);
        packetdataserializer.writeFloat(this.field_149232_d);
        packetdataserializer.writeFloat(this.field_149233_e);
        packetdataserializer.writeFloat(this.field_149230_f);
        packetdataserializer.writeFloat(this.field_149231_g);
        packetdataserializer.writeFloat(this.field_149237_h);
        packetdataserializer.writeInt(this.field_149238_i);
        int i = this.field_179751_a.func_179345_d();

        for (int j = 0; j < i; ++j) {
            packetdataserializer.func_150787_b(this.field_179753_k[j]);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147289_a(this);
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlayerAbilities implements Packet<INetHandlerPlayClient> {

    private boolean field_149119_a;
    private boolean field_149117_b;
    private boolean field_149118_c;
    private boolean field_149115_d;
    private float field_149116_e;
    private float field_149114_f;

    public SPacketPlayerAbilities() {}

    public SPacketPlayerAbilities(PlayerCapabilities playerabilities) {
        this.func_149108_a(playerabilities.field_75102_a);
        this.func_149102_b(playerabilities.field_75100_b);
        this.func_149109_c(playerabilities.field_75101_c);
        this.func_149111_d(playerabilities.field_75098_d);
        this.func_149104_a(playerabilities.func_75093_a());
        this.func_149110_b(playerabilities.func_75094_b());
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        byte b0 = packetdataserializer.readByte();

        this.func_149108_a((b0 & 1) > 0);
        this.func_149102_b((b0 & 2) > 0);
        this.func_149109_c((b0 & 4) > 0);
        this.func_149111_d((b0 & 8) > 0);
        this.func_149104_a(packetdataserializer.readFloat());
        this.func_149110_b(packetdataserializer.readFloat());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        byte b0 = 0;

        if (this.func_149112_c()) {
            b0 = (byte) (b0 | 1);
        }

        if (this.func_149106_d()) {
            b0 = (byte) (b0 | 2);
        }

        if (this.func_149105_e()) {
            b0 = (byte) (b0 | 4);
        }

        if (this.func_149103_f()) {
            b0 = (byte) (b0 | 8);
        }

        packetdataserializer.writeByte(b0);
        packetdataserializer.writeFloat(this.field_149116_e);
        packetdataserializer.writeFloat(this.field_149114_f);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147270_a(this);
    }

    public boolean func_149112_c() {
        return this.field_149119_a;
    }

    public void func_149108_a(boolean flag) {
        this.field_149119_a = flag;
    }

    public boolean func_149106_d() {
        return this.field_149117_b;
    }

    public void func_149102_b(boolean flag) {
        this.field_149117_b = flag;
    }

    public boolean func_149105_e() {
        return this.field_149118_c;
    }

    public void func_149109_c(boolean flag) {
        this.field_149118_c = flag;
    }

    public boolean func_149103_f() {
        return this.field_149115_d;
    }

    public void func_149111_d(boolean flag) {
        this.field_149115_d = flag;
    }

    public void func_149104_a(float f) {
        this.field_149116_e = f;
    }

    public void func_149110_b(float f) {
        this.field_149114_f = f;
    }
}

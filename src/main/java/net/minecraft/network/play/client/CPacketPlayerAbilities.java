package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlayerAbilities implements Packet<INetHandlerPlayServer> {

    private boolean field_149500_a;
    private boolean field_149498_b;
    private boolean field_149499_c;
    private boolean field_149496_d;
    private float field_149497_e;
    private float field_149495_f;

    public CPacketPlayerAbilities() {}

    public CPacketPlayerAbilities(PlayerCapabilities playerabilities) {
        this.func_149490_a(playerabilities.field_75102_a);
        this.func_149483_b(playerabilities.field_75100_b);
        this.func_149491_c(playerabilities.field_75101_c);
        this.func_149493_d(playerabilities.field_75098_d);
        this.func_149485_a(playerabilities.func_75093_a());
        this.func_149492_b(playerabilities.func_75094_b());
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        byte b0 = packetdataserializer.readByte();

        this.func_149490_a((b0 & 1) > 0);
        this.func_149483_b((b0 & 2) > 0);
        this.func_149491_c((b0 & 4) > 0);
        this.func_149493_d((b0 & 8) > 0);
        this.func_149485_a(packetdataserializer.readFloat());
        this.func_149492_b(packetdataserializer.readFloat());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        byte b0 = 0;

        if (this.func_149494_c()) {
            b0 = (byte) (b0 | 1);
        }

        if (this.func_149488_d()) {
            b0 = (byte) (b0 | 2);
        }

        if (this.func_149486_e()) {
            b0 = (byte) (b0 | 4);
        }

        if (this.func_149484_f()) {
            b0 = (byte) (b0 | 8);
        }

        packetdataserializer.writeByte(b0);
        packetdataserializer.writeFloat(this.field_149497_e);
        packetdataserializer.writeFloat(this.field_149495_f);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147348_a(this);
    }

    public boolean func_149494_c() {
        return this.field_149500_a;
    }

    public void func_149490_a(boolean flag) {
        this.field_149500_a = flag;
    }

    public boolean func_149488_d() {
        return this.field_149498_b;
    }

    public void func_149483_b(boolean flag) {
        this.field_149498_b = flag;
    }

    public boolean func_149486_e() {
        return this.field_149499_c;
    }

    public void func_149491_c(boolean flag) {
        this.field_149499_c = flag;
    }

    public boolean func_149484_f() {
        return this.field_149496_d;
    }

    public void func_149493_d(boolean flag) {
        this.field_149496_d = flag;
    }

    public void func_149485_a(float f) {
        this.field_149497_e = f;
    }

    public void func_149492_b(float f) {
        this.field_149495_f = f;
    }
}

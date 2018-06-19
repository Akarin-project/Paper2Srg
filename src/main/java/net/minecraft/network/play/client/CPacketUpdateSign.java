package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;

public class CPacketUpdateSign implements Packet<INetHandlerPlayServer> {

    private BlockPos field_179723_a;
    private String[] field_149590_d;

    public CPacketUpdateSign() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179723_a = packetdataserializer.func_179259_c();
        this.field_149590_d = new String[4];

        for (int i = 0; i < 4; ++i) {
            this.field_149590_d[i] = packetdataserializer.func_150789_c(384);
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179255_a(this.field_179723_a);

        for (int i = 0; i < 4; ++i) {
            packetdataserializer.func_180714_a(this.field_149590_d[i]);
        }

    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147343_a(this);
    }

    public BlockPos func_179722_a() {
        return this.field_179723_a;
    }

    public String[] func_187017_b() {
        return this.field_149590_d;
    }
}

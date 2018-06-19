package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketDisplayObjective implements Packet<INetHandlerPlayClient> {

    private int field_149374_a;
    private String field_149373_b;

    public SPacketDisplayObjective() {}

    public SPacketDisplayObjective(int i, ScoreObjective scoreboardobjective) {
        this.field_149374_a = i;
        if (scoreboardobjective == null) {
            this.field_149373_b = "";
        } else {
            this.field_149373_b = scoreboardobjective.func_96679_b();
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149374_a = packetdataserializer.readByte();
        this.field_149373_b = packetdataserializer.func_150789_c(16);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149374_a);
        packetdataserializer.func_180714_a(this.field_149373_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147254_a(this);
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketScoreboardObjective implements Packet<INetHandlerPlayClient> {

    private String field_149343_a;
    private String field_149341_b;
    private IScoreCriteria.EnumRenderType field_179818_c;
    private int field_149342_c;

    public SPacketScoreboardObjective() {}

    public SPacketScoreboardObjective(ScoreObjective scoreboardobjective, int i) {
        this.field_149343_a = scoreboardobjective.func_96679_b();
        this.field_149341_b = scoreboardobjective.func_96678_d();
        this.field_179818_c = scoreboardobjective.func_96680_c().func_178790_c();
        this.field_149342_c = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149343_a = packetdataserializer.func_150789_c(16);
        this.field_149342_c = packetdataserializer.readByte();
        if (this.field_149342_c == 0 || this.field_149342_c == 2) {
            this.field_149341_b = packetdataserializer.func_150789_c(32);
            this.field_179818_c = IScoreCriteria.EnumRenderType.func_178795_a(packetdataserializer.func_150789_c(16));
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149343_a);
        packetdataserializer.writeByte(this.field_149342_c);
        if (this.field_149342_c == 0 || this.field_149342_c == 2) {
            packetdataserializer.func_180714_a(this.field_149341_b);
            packetdataserializer.func_180714_a(this.field_179818_c.func_178796_a());
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147291_a(this);
    }
}

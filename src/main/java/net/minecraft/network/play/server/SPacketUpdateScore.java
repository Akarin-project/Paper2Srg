package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketUpdateScore implements Packet<INetHandlerPlayClient> {

    private String field_149329_a = "";
    private String field_149327_b = "";
    private int field_149328_c;
    private SPacketUpdateScore.Action field_149326_d;

    public SPacketUpdateScore() {}

    public SPacketUpdateScore(Score scoreboardscore) {
        this.field_149329_a = scoreboardscore.func_96653_e();
        this.field_149327_b = scoreboardscore.func_96645_d().func_96679_b();
        this.field_149328_c = scoreboardscore.func_96652_c();
        this.field_149326_d = SPacketUpdateScore.Action.CHANGE;
    }

    public SPacketUpdateScore(String s) {
        this.field_149329_a = s;
        this.field_149327_b = "";
        this.field_149328_c = 0;
        this.field_149326_d = SPacketUpdateScore.Action.REMOVE;
    }

    public SPacketUpdateScore(String s, ScoreObjective scoreboardobjective) {
        this.field_149329_a = s;
        this.field_149327_b = scoreboardobjective.func_96679_b();
        this.field_149328_c = 0;
        this.field_149326_d = SPacketUpdateScore.Action.REMOVE;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149329_a = packetdataserializer.func_150789_c(40);
        this.field_149326_d = (SPacketUpdateScore.Action) packetdataserializer.func_179257_a(SPacketUpdateScore.Action.class);
        this.field_149327_b = packetdataserializer.func_150789_c(16);
        if (this.field_149326_d != SPacketUpdateScore.Action.REMOVE) {
            this.field_149328_c = packetdataserializer.func_150792_a();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149329_a);
        packetdataserializer.func_179249_a((Enum) this.field_149326_d);
        packetdataserializer.func_180714_a(this.field_149327_b);
        if (this.field_149326_d != SPacketUpdateScore.Action.REMOVE) {
            packetdataserializer.func_150787_b(this.field_149328_c);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147250_a(this);
    }

    public static enum Action {

        CHANGE, REMOVE;

        private Action() {}
    }
}

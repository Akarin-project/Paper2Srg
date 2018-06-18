package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketUpdateScore implements Packet<INetHandlerPlayClient> {

    private String name = "";
    private String objective = "";
    private int value;
    private SPacketUpdateScore.Action action;

    public SPacketUpdateScore() {}

    public SPacketUpdateScore(Score scoreboardscore) {
        this.name = scoreboardscore.getPlayerName();
        this.objective = scoreboardscore.getObjective().getName();
        this.value = scoreboardscore.getScorePoints();
        this.action = SPacketUpdateScore.Action.CHANGE;
    }

    public SPacketUpdateScore(String s) {
        this.name = s;
        this.objective = "";
        this.value = 0;
        this.action = SPacketUpdateScore.Action.REMOVE;
    }

    public SPacketUpdateScore(String s, ScoreObjective scoreboardobjective) {
        this.name = s;
        this.objective = scoreboardobjective.getName();
        this.value = 0;
        this.action = SPacketUpdateScore.Action.REMOVE;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.name = packetdataserializer.readString(40);
        this.action = (SPacketUpdateScore.Action) packetdataserializer.readEnumValue(SPacketUpdateScore.Action.class);
        this.objective = packetdataserializer.readString(16);
        if (this.action != SPacketUpdateScore.Action.REMOVE) {
            this.value = packetdataserializer.readVarInt();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.name);
        packetdataserializer.writeEnumValue((Enum) this.action);
        packetdataserializer.writeString(this.objective);
        if (this.action != SPacketUpdateScore.Action.REMOVE) {
            packetdataserializer.writeVarInt(this.value);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleUpdateScore(this);
    }

    public static enum Action {

        CHANGE, REMOVE;

        private Action() {}
    }
}

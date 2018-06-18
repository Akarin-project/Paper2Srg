package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketScoreboardObjective implements Packet<INetHandlerPlayClient> {

    private String objectiveName;
    private String objectiveValue;
    private IScoreCriteria.EnumRenderType type;
    private int action;

    public SPacketScoreboardObjective() {}

    public SPacketScoreboardObjective(ScoreObjective scoreboardobjective, int i) {
        this.objectiveName = scoreboardobjective.getName();
        this.objectiveValue = scoreboardobjective.getDisplayName();
        this.type = scoreboardobjective.getCriteria().getRenderType();
        this.action = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.objectiveName = packetdataserializer.readString(16);
        this.action = packetdataserializer.readByte();
        if (this.action == 0 || this.action == 2) {
            this.objectiveValue = packetdataserializer.readString(32);
            this.type = IScoreCriteria.EnumRenderType.getByName(packetdataserializer.readString(16));
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.objectiveName);
        packetdataserializer.writeByte(this.action);
        if (this.action == 0 || this.action == 2) {
            packetdataserializer.writeString(this.objectiveValue);
            packetdataserializer.writeString(this.type.getRenderType());
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleScoreboardObjective(this);
    }
}

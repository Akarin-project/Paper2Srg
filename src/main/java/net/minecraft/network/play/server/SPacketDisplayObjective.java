package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketDisplayObjective implements Packet<INetHandlerPlayClient> {

    private int position;
    private String scoreName;

    public SPacketDisplayObjective() {}

    public SPacketDisplayObjective(int i, ScoreObjective scoreboardobjective) {
        this.position = i;
        if (scoreboardobjective == null) {
            this.scoreName = "";
        } else {
            this.scoreName = scoreboardobjective.getName();
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.position = packetdataserializer.readByte();
        this.scoreName = packetdataserializer.readString(16);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.position);
        packetdataserializer.writeString(this.scoreName);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleDisplayObjective(this);
    }
}

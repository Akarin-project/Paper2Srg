package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;

public class CPacketUpdateSign implements Packet<INetHandlerPlayServer> {

    private BlockPos pos;
    private String[] lines;

    public CPacketUpdateSign() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.pos = packetdataserializer.readBlockPos();
        this.lines = new String[4];

        for (int i = 0; i < 4; ++i) {
            this.lines[i] = packetdataserializer.readString(384);
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBlockPos(this.pos);

        for (int i = 0; i < 4; ++i) {
            packetdataserializer.writeString(this.lines[i]);
        }

    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processUpdateSign(this);
    }

    public BlockPos getPosition() {
        return this.pos;
    }

    public String[] getLines() {
        return this.lines;
    }
}

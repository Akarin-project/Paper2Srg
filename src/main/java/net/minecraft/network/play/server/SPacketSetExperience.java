package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetExperience implements Packet<INetHandlerPlayClient> {

    private float experienceBar;
    private int totalExperience;
    private int level;

    public SPacketSetExperience() {}

    public SPacketSetExperience(float f, int i, int j) {
        this.experienceBar = f;
        this.totalExperience = i;
        this.level = j;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.experienceBar = packetdataserializer.readFloat();
        this.level = packetdataserializer.readVarInt();
        this.totalExperience = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat(this.experienceBar);
        packetdataserializer.writeVarInt(this.level);
        packetdataserializer.writeVarInt(this.totalExperience);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSetExperience(this);
    }
}

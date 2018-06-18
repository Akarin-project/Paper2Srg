package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUpdateHealth implements Packet<INetHandlerPlayClient> {

    private float health;
    private int foodLevel;
    private float saturationLevel;

    public SPacketUpdateHealth() {}

    public SPacketUpdateHealth(float f, int i, float f1) {
        this.health = f;
        this.foodLevel = i;
        this.saturationLevel = f1;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.health = packetdataserializer.readFloat();
        this.foodLevel = packetdataserializer.readVarInt();
        this.saturationLevel = packetdataserializer.readFloat();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat(this.health);
        packetdataserializer.writeVarInt(this.foodLevel);
        packetdataserializer.writeFloat(this.saturationLevel);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleUpdateHealth(this);
    }
}

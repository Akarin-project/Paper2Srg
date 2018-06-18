package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnExperienceOrb implements Packet<INetHandlerPlayClient> {

    private int entityID;
    private double posX;
    private double posY;
    private double posZ;
    private int xpValue;

    public SPacketSpawnExperienceOrb() {}

    public SPacketSpawnExperienceOrb(EntityXPOrb entityexperienceorb) {
        this.entityID = entityexperienceorb.getEntityId();
        this.posX = entityexperienceorb.posX;
        this.posY = entityexperienceorb.posY;
        this.posZ = entityexperienceorb.posZ;
        this.xpValue = entityexperienceorb.getXpValue();
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityID = packetdataserializer.readVarInt();
        this.posX = packetdataserializer.readDouble();
        this.posY = packetdataserializer.readDouble();
        this.posZ = packetdataserializer.readDouble();
        this.xpValue = packetdataserializer.readShort();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityID);
        packetdataserializer.writeDouble(this.posX);
        packetdataserializer.writeDouble(this.posY);
        packetdataserializer.writeDouble(this.posZ);
        packetdataserializer.writeShort(this.xpValue);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSpawnExperienceOrb(this);
    }
}

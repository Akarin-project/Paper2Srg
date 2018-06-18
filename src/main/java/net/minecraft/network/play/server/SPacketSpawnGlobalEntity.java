package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnGlobalEntity implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private double x;
    private double y;
    private double z;
    private int type;

    public SPacketSpawnGlobalEntity() {}

    public SPacketSpawnGlobalEntity(Entity entity) {
        this.entityId = entity.getEntityId();
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
        if (entity instanceof EntityLightningBolt) {
            this.type = 1;
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.type = packetdataserializer.readByte();
        this.x = packetdataserializer.readDouble();
        this.y = packetdataserializer.readDouble();
        this.z = packetdataserializer.readDouble();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeByte(this.type);
        packetdataserializer.writeDouble(this.x);
        packetdataserializer.writeDouble(this.y);
        packetdataserializer.writeDouble(this.z);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSpawnGlobalEntity(this);
    }
}

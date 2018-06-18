package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityTeleport implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private double posX;
    private double posY;
    private double posZ;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public SPacketEntityTeleport() {}

    public SPacketEntityTeleport(Entity entity) {
        this.entityId = entity.getEntityId();
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.posZ = entity.posZ;
        this.yaw = (byte) ((int) (entity.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte) ((int) (entity.rotationPitch * 256.0F / 360.0F));
        this.onGround = entity.onGround;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.posX = packetdataserializer.readDouble();
        this.posY = packetdataserializer.readDouble();
        this.posZ = packetdataserializer.readDouble();
        this.yaw = packetdataserializer.readByte();
        this.pitch = packetdataserializer.readByte();
        this.onGround = packetdataserializer.readBoolean();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeDouble(this.posX);
        packetdataserializer.writeDouble(this.posY);
        packetdataserializer.writeDouble(this.posZ);
        packetdataserializer.writeByte(this.yaw);
        packetdataserializer.writeByte(this.pitch);
        packetdataserializer.writeBoolean(this.onGround);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityTeleport(this);
    }
}

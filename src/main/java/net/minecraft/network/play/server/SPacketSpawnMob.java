package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnMob implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private UUID uniqueId;
    private int type;
    private double x;
    private double y;
    private double z;
    private int velocityX;
    private int velocityY;
    private int velocityZ;
    private byte yaw;
    private byte pitch;
    private byte headPitch;
    private EntityDataManager dataManager;
    private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

    public SPacketSpawnMob() {}

    public SPacketSpawnMob(EntityLivingBase entityliving) {
        this.entityId = entityliving.getEntityId();
        this.uniqueId = entityliving.getUniqueID();
        this.type = EntityList.REGISTRY.getIDForObject((Object) entityliving.getClass());
        this.x = entityliving.posX;
        this.y = entityliving.posY;
        this.z = entityliving.posZ;
        this.yaw = (byte) ((int) (entityliving.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte) ((int) (entityliving.rotationPitch * 256.0F / 360.0F));
        this.headPitch = (byte) ((int) (entityliving.rotationYawHead * 256.0F / 360.0F));
        double d0 = 3.9D;
        double d1 = entityliving.motionX;
        double d2 = entityliving.motionY;
        double d3 = entityliving.motionZ;

        if (d1 < -3.9D) {
            d1 = -3.9D;
        }

        if (d2 < -3.9D) {
            d2 = -3.9D;
        }

        if (d3 < -3.9D) {
            d3 = -3.9D;
        }

        if (d1 > 3.9D) {
            d1 = 3.9D;
        }

        if (d2 > 3.9D) {
            d2 = 3.9D;
        }

        if (d3 > 3.9D) {
            d3 = 3.9D;
        }

        this.velocityX = (int) (d1 * 8000.0D);
        this.velocityY = (int) (d2 * 8000.0D);
        this.velocityZ = (int) (d3 * 8000.0D);
        this.dataManager = entityliving.getDataManager();
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.uniqueId = packetdataserializer.readUniqueId();
        this.type = packetdataserializer.readVarInt();
        this.x = packetdataserializer.readDouble();
        this.y = packetdataserializer.readDouble();
        this.z = packetdataserializer.readDouble();
        this.yaw = packetdataserializer.readByte();
        this.pitch = packetdataserializer.readByte();
        this.headPitch = packetdataserializer.readByte();
        this.velocityX = packetdataserializer.readShort();
        this.velocityY = packetdataserializer.readShort();
        this.velocityZ = packetdataserializer.readShort();
        this.dataManagerEntries = EntityDataManager.readEntries(packetdataserializer);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeUniqueId(this.uniqueId);
        packetdataserializer.writeVarInt(this.type);
        packetdataserializer.writeDouble(this.x);
        packetdataserializer.writeDouble(this.y);
        packetdataserializer.writeDouble(this.z);
        packetdataserializer.writeByte(this.yaw);
        packetdataserializer.writeByte(this.pitch);
        packetdataserializer.writeByte(this.headPitch);
        packetdataserializer.writeShort(this.velocityX);
        packetdataserializer.writeShort(this.velocityY);
        packetdataserializer.writeShort(this.velocityZ);
        this.dataManager.writeEntries(packetdataserializer);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSpawnMob(this);
    }
}

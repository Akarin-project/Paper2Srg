package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnPlayer implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private byte yaw;
    private byte pitch;
    private EntityDataManager watcher;
    private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

    public SPacketSpawnPlayer() {}

    public SPacketSpawnPlayer(EntityPlayer entityhuman) {
        this.entityId = entityhuman.getEntityId();
        this.uniqueId = entityhuman.getGameProfile().getId();
        this.x = entityhuman.posX;
        this.y = entityhuman.posY;
        this.z = entityhuman.posZ;
        this.yaw = (byte) ((int) (entityhuman.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte) ((int) (entityhuman.rotationPitch * 256.0F / 360.0F));
        this.watcher = entityhuman.getDataManager();
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.uniqueId = packetdataserializer.readUniqueId();
        this.x = packetdataserializer.readDouble();
        this.y = packetdataserializer.readDouble();
        this.z = packetdataserializer.readDouble();
        this.yaw = packetdataserializer.readByte();
        this.pitch = packetdataserializer.readByte();
        this.dataManagerEntries = EntityDataManager.readEntries(packetdataserializer);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeUniqueId(this.uniqueId);
        packetdataserializer.writeDouble(this.x);
        packetdataserializer.writeDouble(this.y);
        packetdataserializer.writeDouble(this.z);
        packetdataserializer.writeByte(this.yaw);
        packetdataserializer.writeByte(this.pitch);
        this.watcher.writeEntries(packetdataserializer);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSpawnPlayer(this);
    }
}

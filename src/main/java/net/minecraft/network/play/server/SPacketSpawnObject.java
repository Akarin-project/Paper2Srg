package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SPacketSpawnObject implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private int speedX;
    private int speedY;
    private int speedZ;
    private int pitch;
    private int yaw;
    private int type;
    private int data;

    public SPacketSpawnObject() {}

    public SPacketSpawnObject(Entity entity, int i) {
        this(entity, i, 0);
    }

    public SPacketSpawnObject(Entity entity, int i, int j) {
        this.entityId = entity.getEntityId();
        this.uniqueId = entity.getUniqueID();
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
        this.pitch = MathHelper.floor(entity.rotationPitch * 256.0F / 360.0F);
        this.yaw = MathHelper.floor(entity.rotationYaw * 256.0F / 360.0F);
        this.type = i;
        this.data = j;
        double d0 = 3.9D;

        this.speedX = (int) (MathHelper.clamp(entity.motionX, -3.9D, 3.9D) * 8000.0D);
        this.speedY = (int) (MathHelper.clamp(entity.motionY, -3.9D, 3.9D) * 8000.0D);
        this.speedZ = (int) (MathHelper.clamp(entity.motionZ, -3.9D, 3.9D) * 8000.0D);
    }

    public SPacketSpawnObject(Entity entity, int i, int j, BlockPos blockposition) {
        this(entity, i, j);
        this.x = (double) blockposition.getX();
        this.y = (double) blockposition.getY();
        this.z = (double) blockposition.getZ();
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.uniqueId = packetdataserializer.readUniqueId();
        this.type = packetdataserializer.readByte();
        this.x = packetdataserializer.readDouble();
        this.y = packetdataserializer.readDouble();
        this.z = packetdataserializer.readDouble();
        this.pitch = packetdataserializer.readByte();
        this.yaw = packetdataserializer.readByte();
        this.data = packetdataserializer.readInt();
        this.speedX = packetdataserializer.readShort();
        this.speedY = packetdataserializer.readShort();
        this.speedZ = packetdataserializer.readShort();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeUniqueId(this.uniqueId);
        packetdataserializer.writeByte(this.type);
        packetdataserializer.writeDouble(this.x);
        packetdataserializer.writeDouble(this.y);
        packetdataserializer.writeDouble(this.z);
        packetdataserializer.writeByte(this.pitch);
        packetdataserializer.writeByte(this.yaw);
        packetdataserializer.writeInt(this.data);
        packetdataserializer.writeShort(this.speedX);
        packetdataserializer.writeShort(this.speedY);
        packetdataserializer.writeShort(this.speedZ);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSpawnObject(this);
    }

    public void setSpeedX(int i) {
        this.speedX = i;
    }

    public void setSpeedY(int i) {
        this.speedY = i;
    }

    public void setSpeedZ(int i) {
        this.speedZ = i;
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketMoveVehicle implements Packet<INetHandlerPlayClient> {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public SPacketMoveVehicle() {}

    public SPacketMoveVehicle(Entity entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
        this.yaw = entity.rotationYaw;
        this.pitch = entity.rotationPitch;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.x = packetdataserializer.readDouble();
        this.y = packetdataserializer.readDouble();
        this.z = packetdataserializer.readDouble();
        this.yaw = packetdataserializer.readFloat();
        this.pitch = packetdataserializer.readFloat();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeDouble(this.x);
        packetdataserializer.writeDouble(this.y);
        packetdataserializer.writeDouble(this.z);
        packetdataserializer.writeFloat(this.yaw);
        packetdataserializer.writeFloat(this.pitch);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleMoveVehicle(this);
    }
}

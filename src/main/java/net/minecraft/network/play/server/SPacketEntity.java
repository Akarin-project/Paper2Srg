package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntity implements Packet<INetHandlerPlayClient> {

    protected int entityId;
    protected int posX;
    protected int posY;
    protected int posZ;
    protected byte yaw;
    protected byte pitch;
    protected boolean onGround;
    protected boolean rotating;

    public SPacketEntity() {}

    public SPacketEntity(int i) {
        this.entityId = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityMovement(this);
    }

    public String toString() {
        return "Entity_" + super.toString();
    }

    public static class S16PacketEntityLook extends SPacketEntity {

        public S16PacketEntityLook() {
            this.rotating = true;
        }

        public S16PacketEntityLook(int i, byte b0, byte b1, boolean flag) {
            super(i);
            this.yaw = b0;
            this.pitch = b1;
            this.rotating = true;
            this.onGround = flag;
        }

        public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
            super.readPacketData(packetdataserializer);
            this.yaw = packetdataserializer.readByte();
            this.pitch = packetdataserializer.readByte();
            this.onGround = packetdataserializer.readBoolean();
        }

        public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
            super.writePacketData(packetdataserializer);
            packetdataserializer.writeByte(this.yaw);
            packetdataserializer.writeByte(this.pitch);
            packetdataserializer.writeBoolean(this.onGround);
        }
    }

    public static class S15PacketEntityRelMove extends SPacketEntity {

        public S15PacketEntityRelMove() {}

        public S15PacketEntityRelMove(int i, long j, long k, long l, boolean flag) {
            super(i);
            this.posX = (int) j;
            this.posY = (int) k;
            this.posZ = (int) l;
            this.onGround = flag;
        }

        public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
            super.readPacketData(packetdataserializer);
            this.posX = packetdataserializer.readShort();
            this.posY = packetdataserializer.readShort();
            this.posZ = packetdataserializer.readShort();
            this.onGround = packetdataserializer.readBoolean();
        }

        public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
            super.writePacketData(packetdataserializer);
            packetdataserializer.writeShort(this.posX);
            packetdataserializer.writeShort(this.posY);
            packetdataserializer.writeShort(this.posZ);
            packetdataserializer.writeBoolean(this.onGround);
        }
    }

    public static class S17PacketEntityLookMove extends SPacketEntity {

        public S17PacketEntityLookMove() {
            this.rotating = true;
        }

        public S17PacketEntityLookMove(int i, long j, long k, long l, byte b0, byte b1, boolean flag) {
            super(i);
            this.posX = (int) j;
            this.posY = (int) k;
            this.posZ = (int) l;
            this.yaw = b0;
            this.pitch = b1;
            this.onGround = flag;
            this.rotating = true;
        }

        public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
            super.readPacketData(packetdataserializer);
            this.posX = packetdataserializer.readShort();
            this.posY = packetdataserializer.readShort();
            this.posZ = packetdataserializer.readShort();
            this.yaw = packetdataserializer.readByte();
            this.pitch = packetdataserializer.readByte();
            this.onGround = packetdataserializer.readBoolean();
        }

        public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
            super.writePacketData(packetdataserializer);
            packetdataserializer.writeShort(this.posX);
            packetdataserializer.writeShort(this.posY);
            packetdataserializer.writeShort(this.posZ);
            packetdataserializer.writeByte(this.yaw);
            packetdataserializer.writeByte(this.pitch);
            packetdataserializer.writeBoolean(this.onGround);
        }
    }
}

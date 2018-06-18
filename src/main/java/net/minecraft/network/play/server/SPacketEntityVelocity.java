package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityVelocity implements Packet<INetHandlerPlayClient> {

    private int entityID;
    private int motionX;
    private int motionY;
    private int motionZ;

    public SPacketEntityVelocity() {}

    public SPacketEntityVelocity(Entity entity) {
        this(entity.getEntityId(), entity.motionX, entity.motionY, entity.motionZ);
    }

    public SPacketEntityVelocity(int i, double d0, double d1, double d2) {
        this.entityID = i;
        double d3 = 3.9D;

        if (d0 < -3.9D) {
            d0 = -3.9D;
        }

        if (d1 < -3.9D) {
            d1 = -3.9D;
        }

        if (d2 < -3.9D) {
            d2 = -3.9D;
        }

        if (d0 > 3.9D) {
            d0 = 3.9D;
        }

        if (d1 > 3.9D) {
            d1 = 3.9D;
        }

        if (d2 > 3.9D) {
            d2 = 3.9D;
        }

        this.motionX = (int) (d0 * 8000.0D);
        this.motionY = (int) (d1 * 8000.0D);
        this.motionZ = (int) (d2 * 8000.0D);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityID = packetdataserializer.readVarInt();
        this.motionX = packetdataserializer.readShort();
        this.motionY = packetdataserializer.readShort();
        this.motionZ = packetdataserializer.readShort();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityID);
        packetdataserializer.writeShort(this.motionX);
        packetdataserializer.writeShort(this.motionY);
        packetdataserializer.writeShort(this.motionZ);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityVelocity(this);
    }
}

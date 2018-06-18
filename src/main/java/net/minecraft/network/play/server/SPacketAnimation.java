package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketAnimation implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private int type;

    public SPacketAnimation() {}

    public SPacketAnimation(Entity entity, int i) {
        this.entityId = entity.getEntityId();
        this.type = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.type = packetdataserializer.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeByte(this.type);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleAnimation(this);
    }
}

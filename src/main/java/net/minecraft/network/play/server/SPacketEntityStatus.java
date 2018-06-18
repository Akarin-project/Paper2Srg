package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityStatus implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private byte logicOpcode;

    public SPacketEntityStatus() {}

    public SPacketEntityStatus(Entity entity, byte b0) {
        this.entityId = entity.getEntityId();
        this.logicOpcode = b0;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readInt();
        this.logicOpcode = packetdataserializer.readByte();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.entityId);
        packetdataserializer.writeByte(this.logicOpcode);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityStatus(this);
    }
}

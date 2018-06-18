package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityAttach implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private int vehicleEntityId;

    public SPacketEntityAttach() {}

    public SPacketEntityAttach(Entity entity, @Nullable Entity entity1) {
        this.entityId = entity.getEntityId();
        this.vehicleEntityId = entity1 != null ? entity1.getEntityId() : -1;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readInt();
        this.vehicleEntityId = packetdataserializer.readInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.entityId);
        packetdataserializer.writeInt(this.vehicleEntityId);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityAttach(this);
    }
}

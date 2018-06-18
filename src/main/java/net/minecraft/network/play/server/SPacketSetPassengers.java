package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetPassengers implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private int[] passengerIds;

    public SPacketSetPassengers() {}

    public SPacketSetPassengers(Entity entity) {
        this.entityId = entity.getEntityId();
        List list = entity.getPassengers();

        this.passengerIds = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            this.passengerIds[i] = ((Entity) list.get(i)).getEntityId();
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.passengerIds = packetdataserializer.readVarIntArray();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeVarIntArray(this.passengerIds);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSetPassengers(this);
    }
}

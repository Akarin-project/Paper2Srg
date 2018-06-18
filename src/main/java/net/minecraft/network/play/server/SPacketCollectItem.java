package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCollectItem implements Packet<INetHandlerPlayClient> {

    private int collectedItemEntityId;
    private int entityId;
    private int collectedQuantity;

    public SPacketCollectItem() {}

    public SPacketCollectItem(int i, int j, int k) {
        this.collectedItemEntityId = i;
        this.entityId = j;
        this.collectedQuantity = k;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.collectedItemEntityId = packetdataserializer.readVarInt();
        this.entityId = packetdataserializer.readVarInt();
        this.collectedQuantity = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.collectedItemEntityId);
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeVarInt(this.collectedQuantity);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleCollectItem(this);
    }
}

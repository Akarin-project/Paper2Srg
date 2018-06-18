package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;

public class SPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private Potion effectId;

    public SPacketRemoveEntityEffect() {}

    public SPacketRemoveEntityEffect(int i, Potion mobeffectlist) {
        this.entityId = i;
        this.effectId = mobeffectlist;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.effectId = Potion.getPotionById(packetdataserializer.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeByte(Potion.getIdFromPotion(this.effectId));
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleRemoveEntityEffect(this);
    }
}

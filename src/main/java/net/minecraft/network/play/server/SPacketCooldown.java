package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCooldown implements Packet<INetHandlerPlayClient> {

    private Item item;
    private int ticks;

    public SPacketCooldown() {}

    public SPacketCooldown(Item item, int i) {
        this.item = item;
        this.ticks = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.item = Item.getItemById(packetdataserializer.readVarInt());
        this.ticks = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(Item.getIdFromItem(this.item));
        packetdataserializer.writeVarInt(this.ticks);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleCooldown(this);
    }
}

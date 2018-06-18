package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;

public class CPacketPlayerTryUseItem implements Packet<INetHandlerPlayServer> {

    private EnumHand hand;
    public long timestamp; // Spigot

    public CPacketPlayerTryUseItem() {}

    public CPacketPlayerTryUseItem(EnumHand enumhand) {
        this.hand = enumhand;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.timestamp = System.currentTimeMillis(); // Spigot
        this.hand = (EnumHand) packetdataserializer.readEnumValue(EnumHand.class);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.hand);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processTryUseItem(this);
    }

    public EnumHand getHand() {
        return this.hand;
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect implements Packet<INetHandlerPlayClient> {

    private ITextComponent reason;

    public SPacketDisconnect() {}

    public SPacketDisconnect(ITextComponent ichatbasecomponent) {
        this.reason = ichatbasecomponent;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.reason = packetdataserializer.readTextComponent();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeTextComponent(this.reason);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleDisconnect(this);
    }
}

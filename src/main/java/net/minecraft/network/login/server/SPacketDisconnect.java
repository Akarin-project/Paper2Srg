package net.minecraft.network.login.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect implements Packet<INetHandlerLoginClient> {

    private ITextComponent reason;

    public SPacketDisconnect() {}

    public SPacketDisconnect(ITextComponent ichatbasecomponent) {
        this.reason = ichatbasecomponent;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.reason = ITextComponent.Serializer.fromJsonLenient(packetdataserializer.readString(32767));
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeTextComponent(this.reason);
    }

    public void processPacket(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.handleDisconnect(this);
    }
}

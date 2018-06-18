package net.minecraft.network.handshake.client;

import java.io.IOException;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class C00Handshake implements Packet<INetHandlerHandshakeServer> {

    private int protocolVersion;
    public String ip;
    public int port;
    private EnumConnectionState requestedState;

    public C00Handshake() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.protocolVersion = packetdataserializer.readVarInt();
        this.ip = packetdataserializer.readString(Short.MAX_VALUE); // Spigot
        this.port = packetdataserializer.readUnsignedShort();
        this.requestedState = EnumConnectionState.getById(packetdataserializer.readVarInt());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.protocolVersion);
        packetdataserializer.writeString(this.ip);
        packetdataserializer.writeShort(this.port);
        packetdataserializer.writeVarInt(this.requestedState.getId());
    }

    public void processPacket(INetHandlerHandshakeServer packethandshakinginlistener) {
        packethandshakinginlistener.processHandshake(this);
    }

    public EnumConnectionState getRequestedState() {
        return this.requestedState;
    }

    public int getProtocolVersion() { return getProtocolVersion(); } // Paper - OBFHELPER
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
}

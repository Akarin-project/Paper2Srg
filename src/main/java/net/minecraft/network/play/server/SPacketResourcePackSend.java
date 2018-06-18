package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketResourcePackSend implements Packet<INetHandlerPlayClient> {

    private String url;
    private String hash;

    public SPacketResourcePackSend() {}

    public SPacketResourcePackSend(String s, String s1) {
        this.url = s;
        this.hash = s1;
        if (s1.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + s1.length() + ")");
        }
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.url = packetdataserializer.readString(32767);
        this.hash = packetdataserializer.readString(40);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.url);
        packetdataserializer.writeString(this.hash);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleResourcePack(this);
    }
}

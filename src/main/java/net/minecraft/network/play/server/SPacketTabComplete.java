package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient> {

    private String[] matches;

    public SPacketTabComplete() {}

    public SPacketTabComplete(String[] astring) {
        this.matches = astring;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.matches = new String[packetdataserializer.readVarInt()];

        for (int i = 0; i < this.matches.length; ++i) {
            this.matches[i] = packetdataserializer.readString(32767);
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.matches.length);
        String[] astring = this.matches;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            packetdataserializer.writeString(s);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleTabComplete(this);
    }
}

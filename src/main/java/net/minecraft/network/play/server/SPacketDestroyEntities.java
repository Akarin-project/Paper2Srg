package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketDestroyEntities implements Packet<INetHandlerPlayClient> {

    private int[] entityIDs;

    public SPacketDestroyEntities() {}

    public SPacketDestroyEntities(int... aint) {
        this.entityIDs = aint;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityIDs = new int[packetdataserializer.readVarInt()];

        for (int i = 0; i < this.entityIDs.length; ++i) {
            this.entityIDs[i] = packetdataserializer.readVarInt();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityIDs.length);
        int[] aint = this.entityIDs;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint[j];

            packetdataserializer.writeVarInt(k);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleDestroyEntities(this);
    }
}

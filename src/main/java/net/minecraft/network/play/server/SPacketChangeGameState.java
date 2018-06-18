package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketChangeGameState implements Packet<INetHandlerPlayClient> {

    public static final String[] MESSAGE_NAMES = new String[] { "tile.bed.notValid"};
    private int state;
    private float value;

    public SPacketChangeGameState() {}

    public SPacketChangeGameState(int i, float f) {
        this.state = i;
        this.value = f;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.state = packetdataserializer.readUnsignedByte();
        this.value = packetdataserializer.readFloat();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.state);
        packetdataserializer.writeFloat(this.value);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleChangeGameState(this);
    }
}

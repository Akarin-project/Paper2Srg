package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketChangeGameState implements Packet<INetHandlerPlayClient> {

    public static final String[] field_149142_a = new String[] { "tile.bed.notValid"};
    private int field_149140_b;
    private float field_149141_c;

    public SPacketChangeGameState() {}

    public SPacketChangeGameState(int i, float f) {
        this.field_149140_b = i;
        this.field_149141_c = f;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149140_b = packetdataserializer.readUnsignedByte();
        this.field_149141_c = packetdataserializer.readFloat();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149140_b);
        packetdataserializer.writeFloat(this.field_149141_c);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147252_a(this);
    }
}

package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUnloadChunk implements Packet<INetHandlerPlayClient> {

    private int field_186942_a;
    private int field_186943_b;

    public SPacketUnloadChunk() {}

    public SPacketUnloadChunk(int i, int j) {
        this.field_186942_a = i;
        this.field_186943_b = j;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_186942_a = packetdataserializer.readInt();
        this.field_186943_b = packetdataserializer.readInt();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_186942_a);
        packetdataserializer.writeInt(this.field_186943_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_184326_a(this);
    }
}

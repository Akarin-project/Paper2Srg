package net.minecraft.network.status.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusClient;

public class SPacketPong implements Packet<INetHandlerStatusClient> {

    private long field_149293_a;

    public SPacketPong() {}

    public SPacketPong(long i) {
        this.field_149293_a = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149293_a = packetdataserializer.readLong();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.field_149293_a);
    }

    public void func_148833_a(INetHandlerStatusClient packetstatusoutlistener) {
        packetstatusoutlistener.func_147398_a(this);
    }
}

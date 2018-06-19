package net.minecraft.network.status.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketPing implements Packet<INetHandlerStatusServer> {

    private long field_149290_a;

    public CPacketPing() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149290_a = packetdataserializer.readLong();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.field_149290_a);
    }

    public void func_148833_a(INetHandlerStatusServer packetstatusinlistener) {
        packetstatusinlistener.func_147311_a(this);
    }

    public long func_149289_c() {
        return this.field_149290_a;
    }
}

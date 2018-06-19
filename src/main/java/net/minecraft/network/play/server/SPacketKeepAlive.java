package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketKeepAlive implements Packet<INetHandlerPlayClient> {

    private long field_149136_a;

    public SPacketKeepAlive() {}

    public SPacketKeepAlive(long i) {
        this.field_149136_a = i;
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147272_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149136_a = packetdataserializer.readLong();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.field_149136_a);
    }
}

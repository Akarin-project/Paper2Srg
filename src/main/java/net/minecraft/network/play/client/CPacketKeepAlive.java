package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketKeepAlive implements Packet<INetHandlerPlayServer> {

    private long field_149461_a;

    public CPacketKeepAlive() {}

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147353_a(this);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149461_a = packetdataserializer.readLong();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.field_149461_a);
    }

    public long func_149460_c() {
        return this.field_149461_a;
    }
}

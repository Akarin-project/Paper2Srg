package net.minecraft.network.login.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketEnableCompression implements Packet<INetHandlerLoginClient> {

    private int field_179733_a;

    public SPacketEnableCompression() {}

    public SPacketEnableCompression(int i) {
        this.field_179733_a = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179733_a = packetdataserializer.func_150792_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_179733_a);
    }

    public void func_148833_a(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.func_180464_a(this);
    }
}

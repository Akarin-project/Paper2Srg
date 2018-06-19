package net.minecraft.network.status.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketServerQuery implements Packet<INetHandlerStatusServer> {

    public CPacketServerQuery() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {}

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {}

    public void func_148833_a(INetHandlerStatusServer packetstatusinlistener) {
        packetstatusinlistener.func_147312_a(this);
    }
}

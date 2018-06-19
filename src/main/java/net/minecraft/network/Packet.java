package net.minecraft.network;

import java.io.IOException;

public interface Packet<T extends INetHandler> {

    void func_148837_a(PacketBuffer packetdataserializer) throws IOException;

    void func_148840_b(PacketBuffer packetdataserializer) throws IOException;

    void func_148833_a(T t0);
}

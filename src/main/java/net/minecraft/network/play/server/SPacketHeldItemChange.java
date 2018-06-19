package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketHeldItemChange implements Packet<INetHandlerPlayClient> {

    private int field_149387_a;

    public SPacketHeldItemChange() {}

    public SPacketHeldItemChange(int i) {
        this.field_149387_a = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149387_a = packetdataserializer.readByte();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149387_a);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147257_a(this);
    }
}

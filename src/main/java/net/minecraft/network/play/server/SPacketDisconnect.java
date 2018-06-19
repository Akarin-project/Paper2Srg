package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect implements Packet<INetHandlerPlayClient> {

    private ITextComponent field_149167_a;

    public SPacketDisconnect() {}

    public SPacketDisconnect(ITextComponent ichatbasecomponent) {
        this.field_149167_a = ichatbasecomponent;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149167_a = packetdataserializer.func_179258_d();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179256_a(this.field_149167_a);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147253_a(this);
    }
}

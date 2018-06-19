package net.minecraft.network.login.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect implements Packet<INetHandlerLoginClient> {

    private ITextComponent field_149605_a;

    public SPacketDisconnect() {}

    public SPacketDisconnect(ITextComponent ichatbasecomponent) {
        this.field_149605_a = ichatbasecomponent;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149605_a = ITextComponent.Serializer.func_186877_b(packetdataserializer.func_150789_c(32767));
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179256_a(this.field_149605_a);
    }

    public void func_148833_a(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.func_147388_a(this);
    }
}

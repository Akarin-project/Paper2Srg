package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;

public class CPacketAnimation implements Packet<INetHandlerPlayServer> {

    private EnumHand field_187019_a;

    public CPacketAnimation() {}

    public CPacketAnimation(EnumHand enumhand) {
        this.field_187019_a = enumhand;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_187019_a = (EnumHand) packetdataserializer.func_179257_a(EnumHand.class);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_187019_a);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_175087_a(this);
    }

    public EnumHand func_187018_a() {
        return this.field_187019_a;
    }
}

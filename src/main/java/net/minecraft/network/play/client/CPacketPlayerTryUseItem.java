package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;

public class CPacketPlayerTryUseItem implements Packet<INetHandlerPlayServer> {

    private EnumHand field_187029_a;
    public long timestamp; // Spigot

    public CPacketPlayerTryUseItem() {}

    public CPacketPlayerTryUseItem(EnumHand enumhand) {
        this.field_187029_a = enumhand;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.timestamp = System.currentTimeMillis(); // Spigot
        this.field_187029_a = (EnumHand) packetdataserializer.func_179257_a(EnumHand.class);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_187029_a);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147346_a(this);
    }

    public EnumHand func_187028_a() {
        return this.field_187029_a;
    }
}

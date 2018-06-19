package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClientStatus implements Packet<INetHandlerPlayServer> {

    private CPacketClientStatus.State field_149437_a;

    public CPacketClientStatus() {}

    public CPacketClientStatus(CPacketClientStatus.State packetplayinclientcommand_enumclientcommand) {
        this.field_149437_a = packetplayinclientcommand_enumclientcommand;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149437_a = (CPacketClientStatus.State) packetdataserializer.func_179257_a(CPacketClientStatus.State.class);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_149437_a);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147342_a(this);
    }

    public CPacketClientStatus.State func_149435_c() {
        return this.field_149437_a;
    }

    public static enum State {

        PERFORM_RESPAWN, REQUEST_STATS;

        private State() {}
    }
}

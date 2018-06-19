package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.ResourceLocation;

public class CPacketSeenAdvancements implements Packet<INetHandlerPlayServer> {

    private CPacketSeenAdvancements.Action field_194166_a;
    private ResourceLocation field_194167_b;

    public CPacketSeenAdvancements() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_194166_a = (CPacketSeenAdvancements.Action) packetdataserializer.func_179257_a(CPacketSeenAdvancements.Action.class);
        if (this.field_194166_a == CPacketSeenAdvancements.Action.OPENED_TAB) {
            this.field_194167_b = packetdataserializer.func_192575_l();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_194166_a);
        if (this.field_194166_a == CPacketSeenAdvancements.Action.OPENED_TAB) {
            packetdataserializer.func_192572_a(this.field_194167_b);
        }

    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_194027_a(this);
    }

    public CPacketSeenAdvancements.Action func_194162_b() {
        return this.field_194166_a;
    }

    public ResourceLocation func_194165_c() {
        return this.field_194167_b;
    }

    public static enum Action {

        OPENED_TAB, CLOSED_SCREEN;

        private Action() {}
    }
}

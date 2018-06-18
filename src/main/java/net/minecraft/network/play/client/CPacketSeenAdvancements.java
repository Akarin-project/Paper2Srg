package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.ResourceLocation;

public class CPacketSeenAdvancements implements Packet<INetHandlerPlayServer> {

    private CPacketSeenAdvancements.Action action;
    private ResourceLocation tab;

    public CPacketSeenAdvancements() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.action = (CPacketSeenAdvancements.Action) packetdataserializer.readEnumValue(CPacketSeenAdvancements.Action.class);
        if (this.action == CPacketSeenAdvancements.Action.OPENED_TAB) {
            this.tab = packetdataserializer.readResourceLocation();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.action);
        if (this.action == CPacketSeenAdvancements.Action.OPENED_TAB) {
            packetdataserializer.writeResourceLocation(this.tab);
        }

    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.handleSeenAdvancements(this);
    }

    public CPacketSeenAdvancements.Action getAction() {
        return this.action;
    }

    public ResourceLocation getTab() {
        return this.tab;
    }

    public static enum Action {

        OPENED_TAB, CLOSED_SCREEN;

        private Action() {}
    }
}

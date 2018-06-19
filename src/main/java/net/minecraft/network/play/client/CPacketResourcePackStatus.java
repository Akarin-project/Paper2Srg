package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketResourcePackStatus implements Packet<INetHandlerPlayServer> {

    public CPacketResourcePackStatus.Action field_179719_b;

    public CPacketResourcePackStatus() {}

    public CPacketResourcePackStatus(CPacketResourcePackStatus.Action packetplayinresourcepackstatus_enumresourcepackstatus) {
        this.field_179719_b = packetplayinresourcepackstatus_enumresourcepackstatus;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179719_b = (CPacketResourcePackStatus.Action) packetdataserializer.func_179257_a(CPacketResourcePackStatus.Action.class);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_179719_b);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_175086_a(this);
    }

    public static enum Action {

        SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED;

        private Action() {}
    }
}

package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class CPacketLoginStart implements Packet<INetHandlerLoginServer> {

    private GameProfile field_149305_a;

    public CPacketLoginStart() {}

    public CPacketLoginStart(GameProfile gameprofile) {
        this.field_149305_a = gameprofile;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149305_a = new GameProfile((UUID) null, packetdataserializer.func_150789_c(16));
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149305_a.getName());
    }

    public void func_148833_a(INetHandlerLoginServer packetlogininlistener) {
        packetlogininlistener.func_147316_a(this);
    }

    public GameProfile func_149304_c() {
        return this.field_149305_a;
    }
}

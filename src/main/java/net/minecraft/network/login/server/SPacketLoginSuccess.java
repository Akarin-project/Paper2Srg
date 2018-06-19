package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketLoginSuccess implements Packet<INetHandlerLoginClient> {

    private GameProfile field_149602_a;

    public SPacketLoginSuccess() {}

    public SPacketLoginSuccess(GameProfile gameprofile) {
        this.field_149602_a = gameprofile;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        String s = packetdataserializer.func_150789_c(36);
        String s1 = packetdataserializer.func_150789_c(16);
        UUID uuid = UUID.fromString(s);

        this.field_149602_a = new GameProfile(uuid, s1);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        UUID uuid = this.field_149602_a.getId();

        packetdataserializer.func_180714_a(uuid == null ? "" : uuid.toString());
        packetdataserializer.func_180714_a(this.field_149602_a.getName());
    }

    public void func_148833_a(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.func_147390_a(this);
    }
}

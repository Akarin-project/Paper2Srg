package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketLoginSuccess implements Packet<INetHandlerLoginClient> {

    private GameProfile profile;

    public SPacketLoginSuccess() {}

    public SPacketLoginSuccess(GameProfile gameprofile) {
        this.profile = gameprofile;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        String s = packetdataserializer.readString(36);
        String s1 = packetdataserializer.readString(16);
        UUID uuid = UUID.fromString(s);

        this.profile = new GameProfile(uuid, s1);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        UUID uuid = this.profile.getId();

        packetdataserializer.writeString(uuid == null ? "" : uuid.toString());
        packetdataserializer.writeString(this.profile.getName());
    }

    public void processPacket(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.handleLoginSuccess(this);
    }
}

package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class CPacketLoginStart implements Packet<INetHandlerLoginServer> {

    private GameProfile profile;

    public CPacketLoginStart() {}

    public CPacketLoginStart(GameProfile gameprofile) {
        this.profile = gameprofile;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.profile = new GameProfile((UUID) null, packetdataserializer.readString(16));
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.profile.getName());
    }

    public void processPacket(INetHandlerLoginServer packetlogininlistener) {
        packetlogininlistener.processLoginStart(this);
    }

    public GameProfile getProfile() {
        return this.profile;
    }
}

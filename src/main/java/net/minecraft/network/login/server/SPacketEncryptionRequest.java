package net.minecraft.network.login.server;

import java.io.IOException;
import java.security.PublicKey;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class SPacketEncryptionRequest implements Packet<INetHandlerLoginClient> {

    private String hashedServerId;
    private PublicKey publicKey;
    private byte[] verifyToken;

    public SPacketEncryptionRequest() {}

    public SPacketEncryptionRequest(String s, PublicKey publickey, byte[] abyte) {
        this.hashedServerId = s;
        this.publicKey = publickey;
        this.verifyToken = abyte;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.hashedServerId = packetdataserializer.readString(20);
        this.publicKey = CryptManager.decodePublicKey(packetdataserializer.readByteArray());
        this.verifyToken = packetdataserializer.readByteArray();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.hashedServerId);
        packetdataserializer.writeByteArray(this.publicKey.getEncoded());
        packetdataserializer.writeByteArray(this.verifyToken);
    }

    public void processPacket(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.handleEncryptionRequest(this);
    }
}

package net.minecraft.network.login.client;

import java.io.IOException;
import java.security.PrivateKey;
import javax.crypto.SecretKey;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;

public class CPacketEncryptionResponse implements Packet<INetHandlerLoginServer> {

    private byte[] secretKeyEncrypted = new byte[0];
    private byte[] verifyTokenEncrypted = new byte[0];

    public CPacketEncryptionResponse() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.secretKeyEncrypted = packetdataserializer.readByteArray();
        this.verifyTokenEncrypted = packetdataserializer.readByteArray();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByteArray(this.secretKeyEncrypted);
        packetdataserializer.writeByteArray(this.verifyTokenEncrypted);
    }

    public void processPacket(INetHandlerLoginServer packetlogininlistener) {
        packetlogininlistener.processEncryptionResponse(this);
    }

    public SecretKey getSecretKey(PrivateKey privatekey) {
        return CryptManager.decryptSharedKey(privatekey, this.secretKeyEncrypted);
    }

    public byte[] getVerifyToken(PrivateKey privatekey) {
        return privatekey == null ? this.verifyTokenEncrypted : CryptManager.decryptData(privatekey, this.verifyTokenEncrypted);
    }
}

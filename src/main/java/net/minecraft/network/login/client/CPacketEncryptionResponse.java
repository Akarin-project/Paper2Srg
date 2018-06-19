package net.minecraft.network.login.client;

import java.io.IOException;
import java.security.PrivateKey;
import javax.crypto.SecretKey;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;

public class CPacketEncryptionResponse implements Packet<INetHandlerLoginServer> {

    private byte[] field_149302_a = new byte[0];
    private byte[] field_149301_b = new byte[0];

    public CPacketEncryptionResponse() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149302_a = packetdataserializer.func_179251_a();
        this.field_149301_b = packetdataserializer.func_179251_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179250_a(this.field_149302_a);
        packetdataserializer.func_179250_a(this.field_149301_b);
    }

    public void func_148833_a(INetHandlerLoginServer packetlogininlistener) {
        packetlogininlistener.func_147315_a(this);
    }

    public SecretKey func_149300_a(PrivateKey privatekey) {
        return CryptManager.func_75887_a(privatekey, this.field_149302_a);
    }

    public byte[] func_149299_b(PrivateKey privatekey) {
        return privatekey == null ? this.field_149301_b : CryptManager.func_75889_b(privatekey, this.field_149301_b);
    }
}

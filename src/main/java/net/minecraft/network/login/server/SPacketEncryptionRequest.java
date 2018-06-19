package net.minecraft.network.login.server;

import java.io.IOException;
import java.security.PublicKey;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class SPacketEncryptionRequest implements Packet<INetHandlerLoginClient> {

    private String field_149612_a;
    private PublicKey field_149610_b;
    private byte[] field_149611_c;

    public SPacketEncryptionRequest() {}

    public SPacketEncryptionRequest(String s, PublicKey publickey, byte[] abyte) {
        this.field_149612_a = s;
        this.field_149610_b = publickey;
        this.field_149611_c = abyte;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149612_a = packetdataserializer.func_150789_c(20);
        this.field_149610_b = CryptManager.func_75896_a(packetdataserializer.func_179251_a());
        this.field_149611_c = packetdataserializer.func_179251_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149612_a);
        packetdataserializer.func_179250_a(this.field_149610_b.getEncoded());
        packetdataserializer.func_179250_a(this.field_149611_c);
    }

    public void func_148833_a(INetHandlerLoginClient packetloginoutlistener) {
        packetloginoutlistener.func_147389_a(this);
    }
}

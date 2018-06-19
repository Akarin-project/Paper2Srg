package net.minecraft.network.handshake.client;

import java.io.IOException;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class C00Handshake implements Packet<INetHandlerHandshakeServer> {

    private int field_149600_a;
    public String field_149598_b;
    public int field_149599_c;
    private EnumConnectionState field_149597_d;

    public C00Handshake() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149600_a = packetdataserializer.func_150792_a();
        this.field_149598_b = packetdataserializer.func_150789_c(Short.MAX_VALUE); // Spigot
        this.field_149599_c = packetdataserializer.readUnsignedShort();
        this.field_149597_d = EnumConnectionState.func_150760_a(packetdataserializer.func_150792_a());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149600_a);
        packetdataserializer.func_180714_a(this.field_149598_b);
        packetdataserializer.writeShort(this.field_149599_c);
        packetdataserializer.func_150787_b(this.field_149597_d.func_150759_c());
    }

    public void func_148833_a(INetHandlerHandshakeServer packethandshakinginlistener) {
        packethandshakinginlistener.func_147383_a(this);
    }

    public EnumConnectionState func_149594_c() {
        return this.field_149597_d;
    }

    public int getProtocolVersion() { return func_149595_d(); } // Paper - OBFHELPER
    public int func_149595_d() {
        return this.field_149600_a;
    }
}

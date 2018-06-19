package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketResourcePackSend implements Packet<INetHandlerPlayClient> {

    private String field_179786_a;
    private String field_179785_b;

    public SPacketResourcePackSend() {}

    public SPacketResourcePackSend(String s, String s1) {
        this.field_179786_a = s;
        this.field_179785_b = s1;
        if (s1.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + s1.length() + ")");
        }
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179786_a = packetdataserializer.func_150789_c(32767);
        this.field_179785_b = packetdataserializer.func_150789_c(40);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_179786_a);
        packetdataserializer.func_180714_a(this.field_179785_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_175095_a(this);
    }
}

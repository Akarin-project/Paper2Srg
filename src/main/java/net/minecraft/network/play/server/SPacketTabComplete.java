package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient> {

    private String[] field_149632_a;

    public SPacketTabComplete() {}

    public SPacketTabComplete(String[] astring) {
        this.field_149632_a = astring;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149632_a = new String[packetdataserializer.func_150792_a()];

        for (int i = 0; i < this.field_149632_a.length; ++i) {
            this.field_149632_a[i] = packetdataserializer.func_150789_c(32767);
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149632_a.length);
        String[] astring = this.field_149632_a;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            packetdataserializer.func_180714_a(s);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147274_a(this);
    }
}

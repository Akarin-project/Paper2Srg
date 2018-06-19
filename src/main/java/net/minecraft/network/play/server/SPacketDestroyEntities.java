package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketDestroyEntities implements Packet<INetHandlerPlayClient> {

    private int[] field_149100_a;

    public SPacketDestroyEntities() {}

    public SPacketDestroyEntities(int... aint) {
        this.field_149100_a = aint;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149100_a = new int[packetdataserializer.func_150792_a()];

        for (int i = 0; i < this.field_149100_a.length; ++i) {
            this.field_149100_a[i] = packetdataserializer.func_150792_a();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149100_a.length);
        int[] aint = this.field_149100_a;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint[j];

            packetdataserializer.func_150787_b(k);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147238_a(this);
    }
}

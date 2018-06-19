package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;

public class SPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient> {

    private int field_149079_a;
    private Potion field_149078_b;

    public SPacketRemoveEntityEffect() {}

    public SPacketRemoveEntityEffect(int i, Potion mobeffectlist) {
        this.field_149079_a = i;
        this.field_149078_b = mobeffectlist;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149079_a = packetdataserializer.func_150792_a();
        this.field_149078_b = Potion.func_188412_a(packetdataserializer.readUnsignedByte());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149079_a);
        packetdataserializer.writeByte(Potion.func_188409_a(this.field_149078_b));
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147262_a(this);
    }
}

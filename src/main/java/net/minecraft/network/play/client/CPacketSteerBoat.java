package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketSteerBoat implements Packet<INetHandlerPlayServer> {

    private boolean field_187015_a;
    private boolean field_187016_b;

    public CPacketSteerBoat() {}

    public CPacketSteerBoat(boolean flag, boolean flag1) {
        this.field_187015_a = flag;
        this.field_187016_b = flag1;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_187015_a = packetdataserializer.readBoolean();
        this.field_187016_b = packetdataserializer.readBoolean();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBoolean(this.field_187015_a);
        packetdataserializer.writeBoolean(this.field_187016_b);
    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_184340_a(this);
    }

    public boolean func_187012_a() {
        return this.field_187015_a;
    }

    public boolean func_187014_b() {
        return this.field_187016_b;
    }
}

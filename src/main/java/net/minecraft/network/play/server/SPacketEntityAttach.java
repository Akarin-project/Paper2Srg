package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityAttach implements Packet<INetHandlerPlayClient> {

    private int field_149406_b;
    private int field_149407_c;

    public SPacketEntityAttach() {}

    public SPacketEntityAttach(Entity entity, @Nullable Entity entity1) {
        this.field_149406_b = entity.func_145782_y();
        this.field_149407_c = entity1 != null ? entity1.func_145782_y() : -1;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149406_b = packetdataserializer.readInt();
        this.field_149407_c = packetdataserializer.readInt();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_149406_b);
        packetdataserializer.writeInt(this.field_149407_c);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147243_a(this);
    }
}

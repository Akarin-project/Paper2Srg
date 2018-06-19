package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketUseBed implements Packet<INetHandlerPlayClient> {

    private int field_149097_a;
    private BlockPos field_179799_b;

    public SPacketUseBed() {}

    public SPacketUseBed(EntityPlayer entityhuman, BlockPos blockposition) {
        this.field_149097_a = entityhuman.func_145782_y();
        this.field_179799_b = blockposition;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149097_a = packetdataserializer.func_150792_a();
        this.field_179799_b = packetdataserializer.func_179259_c();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149097_a);
        packetdataserializer.func_179255_a(this.field_179799_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147278_a(this);
    }
}

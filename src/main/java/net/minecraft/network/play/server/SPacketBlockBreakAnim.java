package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockBreakAnim implements Packet<INetHandlerPlayClient> {

    private int field_148852_a;
    private BlockPos field_179822_b;
    private int field_148849_e;

    public SPacketBlockBreakAnim() {}

    public SPacketBlockBreakAnim(int i, BlockPos blockposition, int j) {
        this.field_148852_a = i;
        this.field_179822_b = blockposition;
        this.field_148849_e = j;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148852_a = packetdataserializer.func_150792_a();
        this.field_179822_b = packetdataserializer.func_179259_c();
        this.field_148849_e = packetdataserializer.readUnsignedByte();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_148852_a);
        packetdataserializer.func_179255_a(this.field_179822_b);
        packetdataserializer.writeByte(this.field_148849_e);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147294_a(this);
    }
}

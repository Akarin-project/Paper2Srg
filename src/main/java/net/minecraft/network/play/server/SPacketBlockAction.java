package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockAction implements Packet<INetHandlerPlayClient> {

    private BlockPos field_179826_a;
    private int field_148872_d;
    private int field_148873_e;
    private Block field_148871_f;

    public SPacketBlockAction() {}

    public SPacketBlockAction(BlockPos blockposition, Block block, int i, int j) {
        this.field_179826_a = blockposition;
        this.field_148872_d = i;
        this.field_148873_e = j;
        this.field_148871_f = block;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179826_a = packetdataserializer.func_179259_c();
        this.field_148872_d = packetdataserializer.readUnsignedByte();
        this.field_148873_e = packetdataserializer.readUnsignedByte();
        this.field_148871_f = Block.func_149729_e(packetdataserializer.func_150792_a() & 4095);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179255_a(this.field_179826_a);
        packetdataserializer.writeByte(this.field_148872_d);
        packetdataserializer.writeByte(this.field_148873_e);
        packetdataserializer.func_150787_b(Block.func_149682_b(this.field_148871_f) & 4095);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147261_a(this);
    }
}

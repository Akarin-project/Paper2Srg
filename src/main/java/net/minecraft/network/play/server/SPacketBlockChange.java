package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketBlockChange implements Packet<INetHandlerPlayClient> {

    private BlockPos field_179828_a;
    public IBlockState field_148883_d;

    public SPacketBlockChange() {}

    public SPacketBlockChange(World world, BlockPos blockposition) {
        this.field_179828_a = blockposition;
        this.field_148883_d = world.func_180495_p(blockposition);
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179828_a = packetdataserializer.func_179259_c();
        this.field_148883_d = (IBlockState) Block.field_176229_d.func_148745_a(packetdataserializer.func_150792_a());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179255_a(this.field_179828_a);
        packetdataserializer.func_150787_b(Block.field_176229_d.func_148747_b(this.field_148883_d));
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147234_a(this);
    }
}

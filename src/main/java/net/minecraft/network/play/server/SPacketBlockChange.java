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

    private BlockPos blockPosition;
    public IBlockState blockState;

    public SPacketBlockChange() {}

    public SPacketBlockChange(World world, BlockPos blockposition) {
        this.blockPosition = blockposition;
        this.blockState = world.getBlockState(blockposition);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.blockPosition = packetdataserializer.readBlockPos();
        this.blockState = (IBlockState) Block.BLOCK_STATE_IDS.getByValue(packetdataserializer.readVarInt());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeBlockPos(this.blockPosition);
        packetdataserializer.writeVarInt(Block.BLOCK_STATE_IDS.get(this.blockState));
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleBlockChange(this);
    }
}

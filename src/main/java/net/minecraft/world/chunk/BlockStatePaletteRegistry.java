package net.minecraft.world.chunk;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;


public class BlockStatePaletteRegistry implements IBlockStatePalette {

    public BlockStatePaletteRegistry() {}

    public int idFor(IBlockState iblockdata) {
        int i = Block.BLOCK_STATE_IDS.get(iblockdata);

        return i == -1 ? 0 : i;
    }

    public IBlockState getBlockState(int i) {
        IBlockState iblockdata = (IBlockState) Block.BLOCK_STATE_IDS.getByValue(i);

        return iblockdata == null ? Blocks.AIR.getDefaultState() : iblockdata;
    }

    public void write(PacketBuffer packetdataserializer) {
        packetdataserializer.writeVarInt(0);
    }

    public int getSerializedSize() {
        return PacketBuffer.getVarIntSize(0);
    }
}

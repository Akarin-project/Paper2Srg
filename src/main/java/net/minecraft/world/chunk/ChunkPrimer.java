package net.minecraft.world.chunk;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;


public class ChunkPrimer {

    private static final IBlockState DEFAULT_STATE = Blocks.AIR.getDefaultState();
    private final char[] data = new char[65536];

    public ChunkPrimer() {}

    public IBlockState getBlockState(int i, int j, int k) {
        IBlockState iblockdata = (IBlockState) Block.BLOCK_STATE_IDS.getByValue(this.data[getBlockIndex(i, j, k)]);

        return iblockdata == null ? ChunkPrimer.DEFAULT_STATE : iblockdata;
    }

    public void setBlockState(int i, int j, int k, IBlockState iblockdata) {
        this.data[getBlockIndex(i, j, k)] = (char) Block.BLOCK_STATE_IDS.get(iblockdata);
    }

    private static int getBlockIndex(int i, int j, int k) {
        return i << 12 | k << 8 | j;
    }

    public int findGroundBlockIdx(int i, int j) {
        int k = (i << 12 | j << 8) + 256 - 1;

        for (int l = 255; l >= 0; --l) {
            IBlockState iblockdata = (IBlockState) Block.BLOCK_STATE_IDS.getByValue(this.data[k + l]);

            if (iblockdata != null && iblockdata != ChunkPrimer.DEFAULT_STATE) {
                return l;
            }
        }

        return 0;
    }
}

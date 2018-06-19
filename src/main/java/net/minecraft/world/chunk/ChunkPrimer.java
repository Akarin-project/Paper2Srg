package net.minecraft.world.chunk;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;


public class ChunkPrimer {

    private static final IBlockState field_177859_b = Blocks.field_150350_a.func_176223_P();
    private final char[] field_177860_a = new char[65536];

    public ChunkPrimer() {}

    public IBlockState func_177856_a(int i, int j, int k) {
        IBlockState iblockdata = (IBlockState) Block.field_176229_d.func_148745_a(this.field_177860_a[func_186137_b(i, j, k)]);

        return iblockdata == null ? ChunkPrimer.field_177859_b : iblockdata;
    }

    public void func_177855_a(int i, int j, int k, IBlockState iblockdata) {
        this.field_177860_a[func_186137_b(i, j, k)] = (char) Block.field_176229_d.func_148747_b(iblockdata);
    }

    private static int func_186137_b(int i, int j, int k) {
        return i << 12 | k << 8 | j;
    }

    public int func_186138_a(int i, int j) {
        int k = (i << 12 | j << 8) + 256 - 1;

        for (int l = 255; l >= 0; --l) {
            IBlockState iblockdata = (IBlockState) Block.field_176229_d.func_148745_a(this.field_177860_a[k + l]);

            if (iblockdata != null && iblockdata != ChunkPrimer.field_177859_b) {
                return l;
            }
        }

        return 0;
    }
}

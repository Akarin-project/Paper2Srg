package net.minecraft.world.chunk.storage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.NibbleArray;


public class ExtendedBlockStorage {

    private final int field_76684_a;
    private int field_76682_b;
    private int field_76683_c;
    final BlockStateContainer field_177488_d; // Paper - package
    private NibbleArray field_76679_g;
    private NibbleArray field_76685_h;

    // Paper start - Anti-Xray - Support default constructor
    public ExtendedBlockStorage(int i, boolean flag) {
        this(i, flag, (IBlockState[]) null);
    }
    // Paper end

    public ExtendedBlockStorage(int i, boolean flag, IBlockState[] predefinedBlockData) { // Paper - Anti-Xray - Add predefined block data
        this.field_76684_a = i;
        this.field_177488_d = new BlockStateContainer(predefinedBlockData); // Paper - Anti-Xray - Add predefined block data
        this.field_76679_g = new NibbleArray();
        if (flag) {
            this.field_76685_h = new NibbleArray();
        }

    }

    // Paper start - Anti-Xray - Support default constructor
    public ExtendedBlockStorage(int y, boolean flag, char[] blockIds) {
        this(y, flag, blockIds, null);
    }
    // Paper end

    // CraftBukkit start
    public ExtendedBlockStorage(int y, boolean flag, char[] blockIds, IBlockState[] predefinedBlockData) { // Paper - Anti-Xray - Add predefined block data
        this.field_76684_a = y;
        this.field_177488_d = new BlockStateContainer(predefinedBlockData); // Paper - Anti-Xray - Add predefined block data
        for (int i = 0; i < blockIds.length; i++) {
            int xx = i & 15;
            int yy = (i >> 8) & 15;
            int zz = (i >> 4) & 15;
            this.field_177488_d.func_186013_a(xx, yy, zz, Block.field_176229_d.func_148745_a(blockIds[i]));
        }
        this.field_76679_g = new NibbleArray();
        if (flag) {
            this.field_76685_h = new NibbleArray();
        }
        func_76672_e();
    }
    // CraftBukkit end

    public IBlockState func_177485_a(int i, int j, int k) {
        return this.field_177488_d.func_186016_a(i, j, k);
    }

    public void func_177484_a(int i, int j, int k, IBlockState iblockdata) {
        IBlockState iblockdata1 = this.func_177485_a(i, j, k);
        Block block = iblockdata1.func_177230_c();
        Block block1 = iblockdata.func_177230_c();

        if (block != Blocks.field_150350_a) {
            --this.field_76682_b;
            if (block.func_149653_t()) {
                --this.field_76683_c;
            }
        }

        if (block1 != Blocks.field_150350_a) {
            ++this.field_76682_b;
            if (block1.func_149653_t()) {
                ++this.field_76683_c;
            }
        }

        this.field_177488_d.func_186013_a(i, j, k, iblockdata);
    }

    public boolean func_76663_a() {
        return false; // CraftBukkit - MC-80966
    }

    public boolean func_76675_b() {
        return this.field_76683_c > 0;
    }

    public int func_76662_d() {
        return this.field_76684_a;
    }

    public void func_76657_c(int i, int j, int k, int l) {
        this.field_76685_h.func_76581_a(i, j, k, l);
    }

    public int func_76670_c(int i, int j, int k) {
        return this.field_76685_h.func_76582_a(i, j, k);
    }

    public void func_76677_d(int i, int j, int k, int l) {
        this.field_76679_g.func_76581_a(i, j, k, l);
    }

    public int func_76674_d(int i, int j, int k) {
        return this.field_76679_g.func_76582_a(i, j, k);
    }

    public void func_76672_e() {
        this.field_76682_b = 0;
        this.field_76683_c = 0;

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    Block block = this.func_177485_a(i, j, k).func_177230_c();

                    if (block != Blocks.field_150350_a) {
                        ++this.field_76682_b;
                        if (block.func_149653_t()) {
                            ++this.field_76683_c;
                        }
                    }
                }
            }
        }

    }

    public BlockStateContainer func_186049_g() {
        return this.field_177488_d;
    }

    public NibbleArray func_76661_k() {
        return this.field_76679_g;
    }

    public NibbleArray func_76671_l() {
        return this.field_76685_h;
    }

    public void func_76659_c(NibbleArray nibblearray) {
        this.field_76679_g = nibblearray;
    }

    public void func_76666_d(NibbleArray nibblearray) {
        this.field_76685_h = nibblearray;
    }
}

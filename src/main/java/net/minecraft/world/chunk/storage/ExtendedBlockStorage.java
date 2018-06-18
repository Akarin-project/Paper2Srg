package net.minecraft.world.chunk.storage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.NibbleArray;


public class ExtendedBlockStorage {

    private final int yBase;
    private int blockRefCount;
    private int tickRefCount;
    final BlockStateContainer data; // Paper - package
    private NibbleArray blockLight;
    private NibbleArray skyLight;

    // Paper start - Anti-Xray - Support default constructor
    public ExtendedBlockStorage(int i, boolean flag) {
        this(i, flag, (IBlockState[]) null);
    }
    // Paper end

    public ExtendedBlockStorage(int i, boolean flag, IBlockState[] predefinedBlockData) { // Paper - Anti-Xray - Add predefined block data
        this.yBase = i;
        this.data = new BlockStateContainer(predefinedBlockData); // Paper - Anti-Xray - Add predefined block data
        this.blockLight = new NibbleArray();
        if (flag) {
            this.skyLight = new NibbleArray();
        }

    }

    // Paper start - Anti-Xray - Support default constructor
    public ExtendedBlockStorage(int y, boolean flag, char[] blockIds) {
        this(y, flag, blockIds, null);
    }
    // Paper end

    // CraftBukkit start
    public ExtendedBlockStorage(int y, boolean flag, char[] blockIds, IBlockState[] predefinedBlockData) { // Paper - Anti-Xray - Add predefined block data
        this.yBase = y;
        this.data = new BlockStateContainer(predefinedBlockData); // Paper - Anti-Xray - Add predefined block data
        for (int i = 0; i < blockIds.length; i++) {
            int xx = i & 15;
            int yy = (i >> 8) & 15;
            int zz = (i >> 4) & 15;
            this.data.set(xx, yy, zz, Block.BLOCK_STATE_IDS.getByValue(blockIds[i]));
        }
        this.blockLight = new NibbleArray();
        if (flag) {
            this.skyLight = new NibbleArray();
        }
        recalculateRefCounts();
    }
    // CraftBukkit end

    public IBlockState get(int i, int j, int k) {
        return this.data.get(i, j, k);
    }

    public void set(int i, int j, int k, IBlockState iblockdata) {
        IBlockState iblockdata1 = this.get(i, j, k);
        Block block = iblockdata1.getBlock();
        Block block1 = iblockdata.getBlock();

        if (block != Blocks.AIR) {
            --this.blockRefCount;
            if (block.getTickRandomly()) {
                --this.tickRefCount;
            }
        }

        if (block1 != Blocks.AIR) {
            ++this.blockRefCount;
            if (block1.getTickRandomly()) {
                ++this.tickRefCount;
            }
        }

        this.data.set(i, j, k, iblockdata);
    }

    public boolean isEmpty() {
        return false; // CraftBukkit - MC-80966
    }

    public boolean needsRandomTick() {
        return this.tickRefCount > 0;
    }

    public int getYLocation() {
        return this.yBase;
    }

    public void setSkyLight(int i, int j, int k, int l) {
        this.skyLight.set(i, j, k, l);
    }

    public int getSkyLight(int i, int j, int k) {
        return this.skyLight.get(i, j, k);
    }

    public void setBlockLight(int i, int j, int k, int l) {
        this.blockLight.set(i, j, k, l);
    }

    public int getBlockLight(int i, int j, int k) {
        return this.blockLight.get(i, j, k);
    }

    public void recalculateRefCounts() {
        this.blockRefCount = 0;
        this.tickRefCount = 0;

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    Block block = this.get(i, j, k).getBlock();

                    if (block != Blocks.AIR) {
                        ++this.blockRefCount;
                        if (block.getTickRandomly()) {
                            ++this.tickRefCount;
                        }
                    }
                }
            }
        }

    }

    public BlockStateContainer getData() {
        return this.data;
    }

    public NibbleArray getBlockLight() {
        return this.blockLight;
    }

    public NibbleArray getSkyLight() {
        return this.skyLight;
    }

    public void setBlockLight(NibbleArray nibblearray) {
        this.blockLight = nibblearray;
    }

    public void setSkyLight(NibbleArray nibblearray) {
        this.skyLight = nibblearray;
    }
}

package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import com.destroystokyo.paper.antixray.PacketPlayOutMapChunkInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BitArray;
import net.minecraft.util.math.MathHelper;

// Paper start
import com.destroystokyo.paper.antixray.PacketPlayOutMapChunkInfo; // Anti-Xray
// Paper end

public class BlockStateContainer implements IBlockStatePaletteResizer {

    private static final IBlockStatePalette field_186023_d = new BlockStatePaletteRegistry();
    protected static final IBlockState field_186020_a = Blocks.field_150350_a.func_176223_P(); public static final IBlockState DEFAULT_BLOCK_DATA = BlockStateContainer.field_186020_a; // Paper - OBFHELPER
    protected BitArray field_186021_b; protected BitArray getDataBits() { return this.field_186021_b; } // Paper - Anti-Xray - OBFHELPER
    protected IBlockStatePalette field_186022_c; protected IBlockStatePalette getDataPalette() { return this.field_186022_c; } // Paper - Anti-Xray - OBFHELPER
    private int field_186024_e; private int getBitsPerValue() { return this.field_186024_e; } // Paper - Anti-Xray - OBFHELPER
    private final IBlockState[] predefinedBlockData; // Paper - Anti-Xray - Add predefined block data

    // Paper start - Anti-Xray - Support default constructor
    public BlockStateContainer() {
        this(null);
    }
    // Paper end

    // Paper start - Anti-Xray - Add predefined block data
    public BlockStateContainer(IBlockState[] predefinedBlockData) {
        this.predefinedBlockData = predefinedBlockData;

        if (predefinedBlockData == null) {
            // Default constructor
            this.setBitsPerValue(4);
        } else {
            // Count the bits of the maximum array index to initialize a data palette with enough space from the beginning
            // The length of the array is used because air is also added to the data palette from the beginning
            // Start with at least 4
            int maxIndex = predefinedBlockData.length >> 4;
            int bitCount = 4;

            while (maxIndex != 0) {
                maxIndex >>= 1;
                bitCount++;
            }

            // Initialize with at least 15 free indixes
            this.setBitsPerValue((1 << bitCount) - predefinedBlockData.length < 16 ? bitCount + 1 : bitCount);
        }
    }
    // Paper end

    private static int func_186011_b(int i, int j, int k) {
        return j << 8 | k << 4 | i;
    }

    private void setBitsPerValue(int bitsPerValue) { this.func_186012_b(bitsPerValue); } // Paper - Anti-Xray - OBFHELPER
    private void func_186012_b(int i) {
        if (i != this.field_186024_e) {
            this.field_186024_e = i;
            if (this.field_186024_e <= 4) {
                this.field_186024_e = 4;
                this.field_186022_c = new BlockStatePaletteLinear(this.field_186024_e, this);
            } else if (this.field_186024_e <= 8) {
                this.field_186022_c = new BlockStatePaletteHashMap(this.field_186024_e, this);
            } else {
                this.field_186022_c = BlockStateContainer.field_186023_d;
                this.field_186024_e = MathHelper.func_151241_e(Block.field_176229_d.func_186804_a());
            }

            this.field_186022_c.func_186041_a(BlockStateContainer.field_186020_a);

            // Paper start - Anti-Xray - Add predefined block data
            if (this.predefinedBlockData != null) {
                for (int j = 0; j < this.predefinedBlockData.length; j++) {
                    this.getDataPalette().getDataBits(this.predefinedBlockData[j]);
                }
            }
            // Paper end

            this.field_186021_b = new BitArray(this.field_186024_e, 4096);
        }
    }

    public int func_186008_a(int i, IBlockState iblockdata) {
        BitArray databits = this.field_186021_b;
        IBlockStatePalette datapalette = this.field_186022_c;

        this.func_186012_b(i);

        for (int j = 0; j < databits.func_188144_b(); ++j) {
            IBlockState iblockdata1 = datapalette.func_186039_a(databits.func_188142_a(j));

            if (iblockdata1 != null) {
                this.func_186014_b(j, iblockdata1);
            }
        }

        return this.field_186022_c.func_186041_a(iblockdata);
    }

    public void func_186013_a(int i, int j, int k, IBlockState iblockdata) {
        this.func_186014_b(func_186011_b(i, j, k), iblockdata);
    }

    protected void func_186014_b(int i, IBlockState iblockdata) {
        int j = this.field_186022_c.func_186041_a(iblockdata);

        this.field_186021_b.func_188141_a(i, j);
    }

    public IBlockState func_186016_a(int i, int j, int k) {
        return this.func_186015_a(func_186011_b(i, j, k));
    }

    protected IBlockState func_186015_a(int i) {
        IBlockState iblockdata = this.field_186022_c.func_186039_a(this.field_186021_b.func_188142_a(i));

        return iblockdata == null ? BlockStateContainer.field_186020_a : iblockdata;
    }

    // Paper start - Anti-Xray - Support default method
    public void writeBlocks(PacketBuffer packetDataSerializer) { this.func_186009_b(packetDataSerializer); } // OBFHELPER
    public void func_186009_b(PacketBuffer packetdataserializer) {
        this.b(packetdataserializer, null, 0);
    }
    // Paper end

    public void writeBlocks(PacketBuffer packetDataSerializer, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo, int chunkSectionIndex) { this.b(packetDataSerializer, packetPlayOutMapChunkInfo, chunkSectionIndex); } // Paper - Anti-Xray - OBFHELPER
    public void b(PacketBuffer packetdataserializer, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo, int chunkSectionIndex) { // Paper - Anti-Xray - Add chunk packet info
        packetdataserializer.writeByte(this.field_186024_e);
        this.field_186022_c.func_186037_b(packetdataserializer);

        // Paper start - Anti-Xray - Add chunk packet info
        if (packetPlayOutMapChunkInfo != null) {
            packetPlayOutMapChunkInfo.setBitsPerValue(chunkSectionIndex, this.getBitsPerValue());
            packetPlayOutMapChunkInfo.setDataPalette(chunkSectionIndex, this.getDataPalette());
            packetPlayOutMapChunkInfo.setDataBitsIndex(chunkSectionIndex, packetdataserializer.writerIndex() + PacketBuffer.countBytes(this.getDataBits().getDataBits().length));
            packetPlayOutMapChunkInfo.setPredefinedBlockData(chunkSectionIndex, this.predefinedBlockData);
        }
        // Paper end

        packetdataserializer.func_186865_a(this.field_186021_b.func_188143_a());
    }

    @Nullable
    public NibbleArray func_186017_a(byte[] abyte, NibbleArray nibblearray) {
        NibbleArray nibblearray1 = null;

        for (int i = 0; i < 4096; ++i) {
            int j = Block.field_176229_d.func_148747_b(this.func_186015_a(i));
            int k = i & 15;
            int l = i >> 8 & 15;
            int i1 = i >> 4 & 15;

            if ((j >> 12 & 15) != 0) {
                if (nibblearray1 == null) {
                    nibblearray1 = new NibbleArray();
                }

                nibblearray1.func_76581_a(k, l, i1, j >> 12 & 15);
            }

            abyte[i] = (byte) (j >> 4 & 255);
            nibblearray.func_76581_a(k, l, i1, j & 15);
        }

        return nibblearray1;
    }

    public void func_186019_a(byte[] abyte, NibbleArray nibblearray, @Nullable NibbleArray nibblearray1) {
        for (int i = 0; i < 4096; ++i) {
            int j = i & 15;
            int k = i >> 8 & 15;
            int l = i >> 4 & 15;
            int i1 = nibblearray1 == null ? 0 : nibblearray1.func_76582_a(j, k, l);
            int j1 = i1 << 12 | (abyte[i] & 255) << 4 | nibblearray.func_76582_a(j, k, l);

            // CraftBukkit start - fix blocks with random data values (caused by plugins)
            IBlockState data = Block.field_176229_d.func_148745_a(j1);
            if (data == null) {
                Block block = Block.func_149729_e(j1 >> 4);
                if (block != null) {
                    try {
                        data = block.func_176203_a(j1 & 0xF);
                    } catch (Exception ignored) {
                        data = block.func_176223_P();
                    }
                }
            }
            this.func_186014_b(i, data);
            // this.setBlockIndex(i, (IBlockData) Block.REGISTRY_ID.fromId(j1));
            // CraftBukkit end
        }

    }

    public int func_186018_a() {
        return 1 + this.field_186022_c.func_186040_a() + PacketBuffer.func_150790_a(this.field_186021_b.func_188144_b()) + this.field_186021_b.func_188143_a().length * 8;
    }
}

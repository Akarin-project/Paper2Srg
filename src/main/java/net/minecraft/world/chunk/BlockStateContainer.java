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

    private static final IBlockStatePalette REGISTRY_BASED_PALETTE = new BlockStatePaletteRegistry();
    protected static final IBlockState AIR_BLOCK_STATE = Blocks.AIR.getDefaultState(); public static final IBlockState DEFAULT_BLOCK_DATA = BlockStateContainer.AIR_BLOCK_STATE; // Paper - OBFHELPER
    protected BitArray storage; protected BitArray getDataBits() { return this.storage; } // Paper - Anti-Xray - OBFHELPER
    protected IBlockStatePalette palette; protected IBlockStatePalette getDataPalette() { return this.palette; } // Paper - Anti-Xray - OBFHELPER
    private int bits; private int getBitsPerValue() { return this.bits; } // Paper - Anti-Xray - OBFHELPER
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

    private static int getIndex(int i, int j, int k) {
        return j << 8 | k << 4 | i;
    }

    private void setBitsPerValue(int bitsPerValue) { this.setBits(bitsPerValue); } // Paper - Anti-Xray - OBFHELPER
    private void setBits(int i) {
        if (i != this.bits) {
            this.bits = i;
            if (this.bits <= 4) {
                this.bits = 4;
                this.palette = new BlockStatePaletteLinear(this.bits, this);
            } else if (this.bits <= 8) {
                this.palette = new BlockStatePaletteHashMap(this.bits, this);
            } else {
                this.palette = BlockStateContainer.REGISTRY_BASED_PALETTE;
                this.bits = MathHelper.log2DeBruijn(Block.BLOCK_STATE_IDS.size());
            }

            this.palette.idFor(BlockStateContainer.AIR_BLOCK_STATE);

            // Paper start - Anti-Xray - Add predefined block data
            if (this.predefinedBlockData != null) {
                for (int j = 0; j < this.predefinedBlockData.length; j++) {
                    this.getDataPalette().getDataBits(this.predefinedBlockData[j]);
                }
            }
            // Paper end

            this.storage = new BitArray(this.bits, 4096);
        }
    }

    public int onResize(int i, IBlockState iblockdata) {
        BitArray databits = this.storage;
        IBlockStatePalette datapalette = this.palette;

        this.setBits(i);

        for (int j = 0; j < databits.size(); ++j) {
            IBlockState iblockdata1 = datapalette.getBlockState(databits.getAt(j));

            if (iblockdata1 != null) {
                this.set(j, iblockdata1);
            }
        }

        return this.palette.idFor(iblockdata);
    }

    public void set(int i, int j, int k, IBlockState iblockdata) {
        this.set(getIndex(i, j, k), iblockdata);
    }

    protected void set(int i, IBlockState iblockdata) {
        int j = this.palette.idFor(iblockdata);

        this.storage.setAt(i, j);
    }

    public IBlockState get(int i, int j, int k) {
        return this.get(getIndex(i, j, k));
    }

    protected IBlockState get(int i) {
        IBlockState iblockdata = this.palette.getBlockState(this.storage.getAt(i));

        return iblockdata == null ? BlockStateContainer.AIR_BLOCK_STATE : iblockdata;
    }

    // Paper start - Anti-Xray - Support default method
    public void writeBlocks(PacketBuffer packetDataSerializer) { this.write(packetDataSerializer); } // OBFHELPER
    public void write(PacketBuffer packetdataserializer) {
        this.b(packetdataserializer, null, 0);
    }
    // Paper end

    public void writeBlocks(PacketBuffer packetDataSerializer, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo, int chunkSectionIndex) { this.b(packetDataSerializer, packetPlayOutMapChunkInfo, chunkSectionIndex); } // Paper - Anti-Xray - OBFHELPER
    public void b(PacketBuffer packetdataserializer, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo, int chunkSectionIndex) { // Paper - Anti-Xray - Add chunk packet info
        packetdataserializer.writeByte(this.bits);
        this.palette.write(packetdataserializer);

        // Paper start - Anti-Xray - Add chunk packet info
        if (packetPlayOutMapChunkInfo != null) {
            packetPlayOutMapChunkInfo.setBitsPerValue(chunkSectionIndex, this.getBitsPerValue());
            packetPlayOutMapChunkInfo.setDataPalette(chunkSectionIndex, this.getDataPalette());
            packetPlayOutMapChunkInfo.setDataBitsIndex(chunkSectionIndex, packetdataserializer.writerIndex() + PacketBuffer.countBytes(this.getDataBits().getDataBits().length));
            packetPlayOutMapChunkInfo.setPredefinedBlockData(chunkSectionIndex, this.predefinedBlockData);
        }
        // Paper end

        packetdataserializer.writeLongArray(this.storage.getBackingLongArray());
    }

    @Nullable
    public NibbleArray getDataForNBT(byte[] abyte, NibbleArray nibblearray) {
        NibbleArray nibblearray1 = null;

        for (int i = 0; i < 4096; ++i) {
            int j = Block.BLOCK_STATE_IDS.get(this.get(i));
            int k = i & 15;
            int l = i >> 8 & 15;
            int i1 = i >> 4 & 15;

            if ((j >> 12 & 15) != 0) {
                if (nibblearray1 == null) {
                    nibblearray1 = new NibbleArray();
                }

                nibblearray1.set(k, l, i1, j >> 12 & 15);
            }

            abyte[i] = (byte) (j >> 4 & 255);
            nibblearray.set(k, l, i1, j & 15);
        }

        return nibblearray1;
    }

    public void setDataFromNBT(byte[] abyte, NibbleArray nibblearray, @Nullable NibbleArray nibblearray1) {
        for (int i = 0; i < 4096; ++i) {
            int j = i & 15;
            int k = i >> 8 & 15;
            int l = i >> 4 & 15;
            int i1 = nibblearray1 == null ? 0 : nibblearray1.get(j, k, l);
            int j1 = i1 << 12 | (abyte[i] & 255) << 4 | nibblearray.get(j, k, l);

            // CraftBukkit start - fix blocks with random data values (caused by plugins)
            IBlockState data = Block.BLOCK_STATE_IDS.getByValue(j1);
            if (data == null) {
                Block block = Block.getBlockById(j1 >> 4);
                if (block != null) {
                    try {
                        data = block.getStateFromMeta(j1 & 0xF);
                    } catch (Exception ignored) {
                        data = block.getDefaultState();
                    }
                }
            }
            this.set(i, data);
            // this.setBlockIndex(i, (IBlockData) Block.REGISTRY_ID.fromId(j1));
            // CraftBukkit end
        }

    }

    public int getSerializedSize() {
        return 1 + this.palette.getSerializedSize() + PacketBuffer.getVarIntSize(this.storage.size()) + this.storage.getBackingLongArray().length * 8;
    }
}

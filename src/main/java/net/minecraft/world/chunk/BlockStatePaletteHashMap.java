package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntIdentityHashBiMap;

public class BlockStatePaletteHashMap implements IBlockStatePalette {

    private final IntIdentityHashBiMap<IBlockState> statePaletteMap;
    private final IBlockStatePaletteResizer paletteResizer;
    private final int bits;

    public BlockStatePaletteHashMap(int i, IBlockStatePaletteResizer datapaletteexpandable) {
        this.bits = i;
        this.paletteResizer = datapaletteexpandable;
        this.statePaletteMap = new IntIdentityHashBiMap(1 << i);
    }

    public int idFor(IBlockState iblockdata) {
        int i = this.statePaletteMap.getId(iblockdata);

        if (i == -1) {
            i = this.statePaletteMap.add(iblockdata);
            if (i >= 1 << this.bits) {
                i = this.paletteResizer.onResize(this.bits + 1, iblockdata);
            }
        }

        return i;
    }

    @Nullable
    public IBlockState getBlockState(int i) {
        return (IBlockState) this.statePaletteMap.get(i);
    }

    public void write(PacketBuffer packetdataserializer) {
        int i = this.statePaletteMap.size();

        packetdataserializer.writeVarInt(i);

        for (int j = 0; j < i; ++j) {
            packetdataserializer.writeVarInt(Block.BLOCK_STATE_IDS.get(this.statePaletteMap.get(j)));
        }

    }

    public int getSerializedSize() {
        int i = PacketBuffer.getVarIntSize(this.statePaletteMap.size());

        for (int j = 0; j < this.statePaletteMap.size(); ++j) {
            i += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(this.statePaletteMap.get(j)));
        }

        return i;
    }
}

package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public class BlockStatePaletteLinear implements IBlockStatePalette {

    private final IBlockState[] states;
    private final IBlockStatePaletteResizer resizeHandler;
    private final int bits;
    private int arraySize;

    public BlockStatePaletteLinear(int i, IBlockStatePaletteResizer datapaletteexpandable) {
        this.states = new IBlockState[1 << i];
        this.bits = i;
        this.resizeHandler = datapaletteexpandable;
    }

    public int idFor(IBlockState iblockdata) {
        int i;

        for (i = 0; i < this.arraySize; ++i) {
            if (this.states[i] == iblockdata) {
                return i;
            }
        }

        i = this.arraySize;
        if (i < this.states.length) {
            this.states[i] = iblockdata;
            ++this.arraySize;
            return i;
        } else {
            return this.resizeHandler.onResize(this.bits + 1, iblockdata);
        }
    }

    @Nullable
    public IBlockState getBlockState(int i) {
        return i >= 0 && i < this.arraySize ? this.states[i] : null;
    }

    public void write(PacketBuffer packetdataserializer) {
        packetdataserializer.writeVarInt(this.arraySize);

        for (int i = 0; i < this.arraySize; ++i) {
            packetdataserializer.writeVarInt(Block.BLOCK_STATE_IDS.get(this.states[i]));
        }

    }

    public int getSerializedSize() {
        int i = PacketBuffer.getVarIntSize(this.arraySize);

        for (int j = 0; j < this.arraySize; ++j) {
            i += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(this.states[j]));
        }

        return i;
    }
}

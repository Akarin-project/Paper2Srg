package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntIdentityHashBiMap;

public class BlockStatePaletteHashMap implements IBlockStatePalette {

    private final IntIdentityHashBiMap<IBlockState> field_186046_a;
    private final IBlockStatePaletteResizer field_186047_b;
    private final int field_186048_c;

    public BlockStatePaletteHashMap(int i, IBlockStatePaletteResizer datapaletteexpandable) {
        this.field_186048_c = i;
        this.field_186047_b = datapaletteexpandable;
        this.field_186046_a = new IntIdentityHashBiMap(1 << i);
    }

    public int func_186041_a(IBlockState iblockdata) {
        int i = this.field_186046_a.func_186815_a(iblockdata);

        if (i == -1) {
            i = this.field_186046_a.func_186808_c(iblockdata);
            if (i >= 1 << this.field_186048_c) {
                i = this.field_186047_b.func_186008_a(this.field_186048_c + 1, iblockdata);
            }
        }

        return i;
    }

    @Nullable
    public IBlockState func_186039_a(int i) {
        return (IBlockState) this.field_186046_a.func_186813_a(i);
    }

    public void func_186037_b(PacketBuffer packetdataserializer) {
        int i = this.field_186046_a.func_186810_b();

        packetdataserializer.func_150787_b(i);

        for (int j = 0; j < i; ++j) {
            packetdataserializer.func_150787_b(Block.field_176229_d.func_148747_b(this.field_186046_a.func_186813_a(j)));
        }

    }

    public int func_186040_a() {
        int i = PacketBuffer.func_150790_a(this.field_186046_a.func_186810_b());

        for (int j = 0; j < this.field_186046_a.func_186810_b(); ++j) {
            i += PacketBuffer.func_150790_a(Block.field_176229_d.func_148747_b(this.field_186046_a.func_186813_a(j)));
        }

        return i;
    }
}

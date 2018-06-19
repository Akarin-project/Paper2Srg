package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public class BlockStatePaletteLinear implements IBlockStatePalette {

    private final IBlockState[] field_186042_a;
    private final IBlockStatePaletteResizer field_186043_b;
    private final int field_186044_c;
    private int field_186045_d;

    public BlockStatePaletteLinear(int i, IBlockStatePaletteResizer datapaletteexpandable) {
        this.field_186042_a = new IBlockState[1 << i];
        this.field_186044_c = i;
        this.field_186043_b = datapaletteexpandable;
    }

    public int func_186041_a(IBlockState iblockdata) {
        int i;

        for (i = 0; i < this.field_186045_d; ++i) {
            if (this.field_186042_a[i] == iblockdata) {
                return i;
            }
        }

        i = this.field_186045_d;
        if (i < this.field_186042_a.length) {
            this.field_186042_a[i] = iblockdata;
            ++this.field_186045_d;
            return i;
        } else {
            return this.field_186043_b.func_186008_a(this.field_186044_c + 1, iblockdata);
        }
    }

    @Nullable
    public IBlockState func_186039_a(int i) {
        return i >= 0 && i < this.field_186045_d ? this.field_186042_a[i] : null;
    }

    public void func_186037_b(PacketBuffer packetdataserializer) {
        packetdataserializer.func_150787_b(this.field_186045_d);

        for (int i = 0; i < this.field_186045_d; ++i) {
            packetdataserializer.func_150787_b(Block.field_176229_d.func_148747_b(this.field_186042_a[i]));
        }

    }

    public int func_186040_a() {
        int i = PacketBuffer.func_150790_a(this.field_186045_d);

        for (int j = 0; j < this.field_186045_d; ++j) {
            i += PacketBuffer.func_150790_a(Block.field_176229_d.func_148747_b(this.field_186042_a[j]));
        }

        return i;
    }
}

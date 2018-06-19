package net.minecraft.world.chunk;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;


public class BlockStatePaletteRegistry implements IBlockStatePalette {

    public BlockStatePaletteRegistry() {}

    public int func_186041_a(IBlockState iblockdata) {
        int i = Block.field_176229_d.func_148747_b(iblockdata);

        return i == -1 ? 0 : i;
    }

    public IBlockState func_186039_a(int i) {
        IBlockState iblockdata = (IBlockState) Block.field_176229_d.func_148745_a(i);

        return iblockdata == null ? Blocks.field_150350_a.func_176223_P() : iblockdata;
    }

    public void func_186037_b(PacketBuffer packetdataserializer) {
        packetdataserializer.func_150787_b(0);
    }

    public int func_186040_a() {
        return PacketBuffer.func_150790_a(0);
    }
}

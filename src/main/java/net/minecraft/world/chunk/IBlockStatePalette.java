package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public interface IBlockStatePalette {

    default int getDataBits(IBlockState blockData) { return this.func_186041_a(blockData); } // Paper - Anti-Xray - OBFHELPER
    int func_186041_a(IBlockState iblockdata);

    @Nullable default IBlockState getBlockData(int dataBits) { return this.func_186039_a(dataBits); } // Paper - Anti-Xray - OBFHELPER
    @Nullable
    IBlockState func_186039_a(int i);

    void func_186037_b(PacketBuffer packetdataserializer);

    int func_186040_a();
}

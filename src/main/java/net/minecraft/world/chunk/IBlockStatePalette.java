package net.minecraft.world.chunk;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public interface IBlockStatePalette {

    default int getDataBits(IBlockState blockData) { return this.idFor(blockData); } // Paper - Anti-Xray - OBFHELPER
    int idFor(IBlockState iblockdata);

    @Nullable default IBlockState getBlockData(int dataBits) { return this.getBlockState(dataBits); } // Paper - Anti-Xray - OBFHELPER
    @Nullable
    IBlockState getBlockState(int i);

    void write(PacketBuffer packetdataserializer);

    int getSerializedSize();
}

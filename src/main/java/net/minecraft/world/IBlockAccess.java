package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IBlockAccess {

    @Nullable
    TileEntity getTileEntity(BlockPos blockposition);

    IBlockState getBlockState(BlockPos blockposition);

    boolean isAirBlock(BlockPos blockposition);

    int getStrongPower(BlockPos blockposition, EnumFacing enumdirection);
}

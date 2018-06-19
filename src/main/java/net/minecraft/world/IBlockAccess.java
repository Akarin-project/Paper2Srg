package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IBlockAccess {

    @Nullable
    TileEntity func_175625_s(BlockPos blockposition);

    IBlockState func_180495_p(BlockPos blockposition);

    boolean func_175623_d(BlockPos blockposition);

    int func_175627_a(BlockPos blockposition, EnumFacing enumdirection);
}

package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGrowable {

    boolean canGrow(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag);

    boolean canUseBonemeal(World world, Random random, BlockPos blockposition, IBlockState iblockdata);

    void grow(World world, Random random, BlockPos blockposition, IBlockState iblockdata);
}

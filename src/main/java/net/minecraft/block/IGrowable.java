package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGrowable {

    boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag);

    boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata);

    void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata);
}

package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenPumpkin extends WorldGenerator {

    public WorldGenPumpkin() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockposition1) && world.getBlockState(blockposition1.down()).getBlock() == Blocks.GRASS && Blocks.PUMPKIN.canPlaceBlockAt(world, blockposition1)) {
                world.setBlockState(blockposition1, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.Plane.HORIZONTAL.random(random)), 2);
            }
        }

        return true;
    }
}

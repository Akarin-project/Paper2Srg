package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator {

    public WorldGenWaterlily() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 10; ++i) {
            int j = blockposition.getX() + random.nextInt(8) - random.nextInt(8);
            int k = blockposition.getY() + random.nextInt(4) - random.nextInt(4);
            int l = blockposition.getZ() + random.nextInt(8) - random.nextInt(8);

            if (world.isAirBlock(new BlockPos(j, k, l)) && Blocks.WATERLILY.canPlaceBlockAt(world, new BlockPos(j, k, l))) {
                world.setBlockState(new BlockPos(j, k, l), Blocks.WATERLILY.getDefaultState(), 2);
            }
        }

        return true;
    }
}

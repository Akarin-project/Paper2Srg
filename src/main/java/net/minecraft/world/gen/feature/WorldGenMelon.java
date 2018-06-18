package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenMelon extends WorldGenerator {

    public WorldGenMelon() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (Blocks.MELON_BLOCK.canPlaceBlockAt(world, blockposition1) && world.getBlockState(blockposition1.down()).getBlock() == Blocks.GRASS) {
                world.setBlockState(blockposition1, Blocks.MELON_BLOCK.getDefaultState(), 2);
            }
        }

        return true;
    }
}

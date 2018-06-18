package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator {

    public WorldGenCactus() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 10; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockposition1)) {
                int j = 1 + random.nextInt(random.nextInt(3) + 1);

                for (int k = 0; k < j; ++k) {
                    if (Blocks.CACTUS.canBlockStay(world, blockposition1)) {
                        world.setBlockState(blockposition1.up(k), Blocks.CACTUS.getDefaultState(), 2);
                    }
                }
            }
        }

        return true;
    }
}

package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator {

    public WorldGenCactus() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 10; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.func_175623_d(blockposition1)) {
                int j = 1 + random.nextInt(random.nextInt(3) + 1);

                for (int k = 0; k < j; ++k) {
                    if (Blocks.field_150434_aF.func_176586_d(world, blockposition1)) {
                        world.func_180501_a(blockposition1.func_177981_b(k), Blocks.field_150434_aF.func_176223_P(), 2);
                    }
                }
            }
        }

        return true;
    }
}

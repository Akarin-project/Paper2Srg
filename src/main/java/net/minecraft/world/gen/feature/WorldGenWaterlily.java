package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator {

    public WorldGenWaterlily() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 10; ++i) {
            int j = blockposition.func_177958_n() + random.nextInt(8) - random.nextInt(8);
            int k = blockposition.func_177956_o() + random.nextInt(4) - random.nextInt(4);
            int l = blockposition.func_177952_p() + random.nextInt(8) - random.nextInt(8);

            if (world.func_175623_d(new BlockPos(j, k, l)) && Blocks.field_150392_bi.func_176196_c(world, new BlockPos(j, k, l))) {
                world.func_180501_a(new BlockPos(j, k, l), Blocks.field_150392_bi.func_176223_P(), 2);
            }
        }

        return true;
    }
}

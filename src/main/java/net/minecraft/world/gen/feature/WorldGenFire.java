package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenFire extends WorldGenerator {

    public WorldGenFire() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.func_175623_d(blockposition1) && world.func_180495_p(blockposition1.func_177977_b()).func_177230_c() == Blocks.field_150424_aL) {
                world.func_180501_a(blockposition1, Blocks.field_150480_ab.func_176223_P(), 2);
            }
        }

        return true;
    }
}

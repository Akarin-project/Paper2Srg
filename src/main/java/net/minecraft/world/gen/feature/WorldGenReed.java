package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenReed extends WorldGenerator {

    public WorldGenReed() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 20; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));

            if (world.func_175623_d(blockposition1)) {
                BlockPos blockposition2 = blockposition1.func_177977_b();

                if (world.func_180495_p(blockposition2.func_177976_e()).func_185904_a() == Material.field_151586_h || world.func_180495_p(blockposition2.func_177974_f()).func_185904_a() == Material.field_151586_h || world.func_180495_p(blockposition2.func_177978_c()).func_185904_a() == Material.field_151586_h || world.func_180495_p(blockposition2.func_177968_d()).func_185904_a() == Material.field_151586_h) {
                    int j = 2 + random.nextInt(random.nextInt(3) + 1);

                    for (int k = 0; k < j; ++k) {
                        if (Blocks.field_150436_aH.func_176354_d(world, blockposition1)) {
                            world.func_180501_a(blockposition1.func_177981_b(k), Blocks.field_150436_aH.func_176223_P(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}

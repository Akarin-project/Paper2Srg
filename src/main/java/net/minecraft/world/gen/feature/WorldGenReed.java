package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenReed extends WorldGenerator {

    public WorldGenReed() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 20; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));

            if (world.isAirBlock(blockposition1)) {
                BlockPos blockposition2 = blockposition1.down();

                if (world.getBlockState(blockposition2.west()).getMaterial() == Material.WATER || world.getBlockState(blockposition2.east()).getMaterial() == Material.WATER || world.getBlockState(blockposition2.north()).getMaterial() == Material.WATER || world.getBlockState(blockposition2.south()).getMaterial() == Material.WATER) {
                    int j = 2 + random.nextInt(random.nextInt(3) + 1);

                    for (int k = 0; k < j; ++k) {
                        if (Blocks.REEDS.canBlockStay(world, blockposition1)) {
                            world.setBlockState(blockposition1.up(k), Blocks.REEDS.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}

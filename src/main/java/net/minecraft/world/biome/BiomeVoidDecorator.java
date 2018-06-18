package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeVoidDecorator extends BiomeDecorator {

    public BiomeVoidDecorator() {}

    public void decorate(World world, Random random, Biome biomebase, BlockPos blockposition) {
        BlockPos blockposition1 = world.getSpawnPoint();
        boolean flag = true;
        double d0 = blockposition1.distanceSq(blockposition.add(8, blockposition1.getY(), 8));

        if (d0 <= 1024.0D) {
            BlockPos blockposition2 = new BlockPos(blockposition1.getX() - 16, blockposition1.getY() - 1, blockposition1.getZ() - 16);
            BlockPos blockposition3 = new BlockPos(blockposition1.getX() + 16, blockposition1.getY() - 1, blockposition1.getZ() + 16);
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(blockposition2);

            for (int i = blockposition.getZ(); i < blockposition.getZ() + 16; ++i) {
                for (int j = blockposition.getX(); j < blockposition.getX() + 16; ++j) {
                    if (i >= blockposition2.getZ() && i <= blockposition3.getZ() && j >= blockposition2.getX() && j <= blockposition3.getX()) {
                        blockposition_mutableblockposition.setPos(j, blockposition_mutableblockposition.getY(), i);
                        if (blockposition1.getX() == j && blockposition1.getZ() == i) {
                            world.setBlockState(blockposition_mutableblockposition, Blocks.COBBLESTONE.getDefaultState(), 2);
                        } else {
                            world.setBlockState(blockposition_mutableblockposition, Blocks.STONE.getDefaultState(), 2);
                        }
                    }
                }
            }

        }
    }
}

package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSand extends WorldGenerator {

    private final Block block;
    private final int radius;

    public WorldGenSand(Block block, int i) {
        this.block = block;
        this.radius = i;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        if (world.getBlockState(blockposition).getMaterial() != Material.WATER) {
            return false;
        } else {
            int i = random.nextInt(this.radius - 2) + 2;
            boolean flag = true;

            for (int j = blockposition.getX() - i; j <= blockposition.getX() + i; ++j) {
                for (int k = blockposition.getZ() - i; k <= blockposition.getZ() + i; ++k) {
                    int l = j - blockposition.getX();
                    int i1 = k - blockposition.getZ();

                    if (l * l + i1 * i1 <= i * i) {
                        for (int j1 = blockposition.getY() - 2; j1 <= blockposition.getY() + 2; ++j1) {
                            BlockPos blockposition1 = new BlockPos(j, j1, k);
                            Block block = world.getBlockState(blockposition1).getBlock();

                            if (block == Blocks.DIRT || block == Blocks.GRASS) {
                                world.setBlockState(blockposition1, this.block.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}

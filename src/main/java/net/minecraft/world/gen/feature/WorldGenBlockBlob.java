package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator {

    private final Block block;
    private final int startRadius;

    public WorldGenBlockBlob(Block block, int i) {
        super(false);
        this.block = block;
        this.startRadius = i;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        while (true) {
            if (blockposition.getY() > 3) {
                label47: {
                    if (!world.isAirBlock(blockposition.down())) {
                        Block block = world.getBlockState(blockposition.down()).getBlock();

                        if (block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.STONE) {
                            break label47;
                        }
                    }

                    blockposition = blockposition.down();
                    continue;
                }
            }

            if (blockposition.getY() <= 3) {
                return false;
            }

            int i = this.startRadius;

            for (int j = 0; i >= 0 && j < 3; ++j) {
                int k = i + random.nextInt(2);
                int l = i + random.nextInt(2);
                int i1 = i + random.nextInt(2);
                float f = (float) (k + l + i1) * 0.333F + 0.5F;
                Iterator iterator = BlockPos.getAllInBox(blockposition.add(-k, -l, -i1), blockposition.add(k, l, i1)).iterator();

                while (iterator.hasNext()) {
                    BlockPos blockposition1 = (BlockPos) iterator.next();

                    if (blockposition1.distanceSq(blockposition) <= (double) (f * f)) {
                        world.setBlockState(blockposition1, this.block.getDefaultState(), 4);
                    }
                }

                blockposition = blockposition.add(-(i + 1) + random.nextInt(2 + i * 2), 0 - random.nextInt(2), -(i + 1) + random.nextInt(2 + i * 2));
            }

            return true;
        }
    }
}

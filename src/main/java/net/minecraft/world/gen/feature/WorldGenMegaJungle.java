package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaJungle extends WorldGenHugeTrees {

    public WorldGenMegaJungle(boolean flag, int i, int j, IBlockState iblockdata, IBlockState iblockdata1) {
        super(flag, i, j, iblockdata, iblockdata1);
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i = this.getHeight(random);

        if (!this.ensureGrowable(world, random, blockposition, i)) {
            return false;
        } else {
            this.createCrown(world, blockposition.up(i), 2);

            for (int j = blockposition.getY() + i - 2 - random.nextInt(4); j > blockposition.getY() + i / 2; j -= 2 + random.nextInt(4)) {
                float f = random.nextFloat() * 6.2831855F;
                int k = blockposition.getX() + (int) (0.5F + MathHelper.cos(f) * 4.0F);
                int l = blockposition.getZ() + (int) (0.5F + MathHelper.sin(f) * 4.0F);

                int i1;

                for (i1 = 0; i1 < 5; ++i1) {
                    k = blockposition.getX() + (int) (1.5F + MathHelper.cos(f) * (float) i1);
                    l = blockposition.getZ() + (int) (1.5F + MathHelper.sin(f) * (float) i1);
                    this.setBlockAndNotifyAdequately(world, new BlockPos(k, j - 3 + i1 / 2, l), this.woodMetadata);
                }

                i1 = 1 + random.nextInt(2);
                int j1 = j;

                for (int k1 = j - i1; k1 <= j1; ++k1) {
                    int l1 = k1 - j1;

                    this.growLeavesLayer(world, new BlockPos(k, k1, l), 1 - l1);
                }
            }

            for (int i2 = 0; i2 < i; ++i2) {
                BlockPos blockposition1 = blockposition.up(i2);

                if (this.canGrowInto(world.getBlockState(blockposition1).getBlock())) {
                    this.setBlockAndNotifyAdequately(world, blockposition1, this.woodMetadata);
                    if (i2 > 0) {
                        this.placeVine(world, random, blockposition1.west(), BlockVine.EAST);
                        this.placeVine(world, random, blockposition1.north(), BlockVine.SOUTH);
                    }
                }

                if (i2 < i - 1) {
                    BlockPos blockposition2 = blockposition1.east();

                    if (this.canGrowInto(world.getBlockState(blockposition2).getBlock())) {
                        this.setBlockAndNotifyAdequately(world, blockposition2, this.woodMetadata);
                        if (i2 > 0) {
                            this.placeVine(world, random, blockposition2.east(), BlockVine.WEST);
                            this.placeVine(world, random, blockposition2.north(), BlockVine.SOUTH);
                        }
                    }

                    BlockPos blockposition3 = blockposition1.south().east();

                    if (this.canGrowInto(world.getBlockState(blockposition3).getBlock())) {
                        this.setBlockAndNotifyAdequately(world, blockposition3, this.woodMetadata);
                        if (i2 > 0) {
                            this.placeVine(world, random, blockposition3.east(), BlockVine.WEST);
                            this.placeVine(world, random, blockposition3.south(), BlockVine.NORTH);
                        }
                    }

                    BlockPos blockposition4 = blockposition1.south();

                    if (this.canGrowInto(world.getBlockState(blockposition4).getBlock())) {
                        this.setBlockAndNotifyAdequately(world, blockposition4, this.woodMetadata);
                        if (i2 > 0) {
                            this.placeVine(world, random, blockposition4.west(), BlockVine.EAST);
                            this.placeVine(world, random, blockposition4.south(), BlockVine.NORTH);
                        }
                    }
                }
            }

            return true;
        }
    }

    private void placeVine(World world, Random random, BlockPos blockposition, PropertyBool blockstateboolean) {
        if (random.nextInt(3) > 0 && world.isAirBlock(blockposition)) {
            this.setBlockAndNotifyAdequately(world, blockposition, Blocks.VINE.getDefaultState().withProperty(blockstateboolean, Boolean.valueOf(true)));
        }

    }

    private void createCrown(World world, BlockPos blockposition, int i) {
        boolean flag = true;

        for (int j = -2; j <= 0; ++j) {
            this.growLeavesLayerStrict(world, blockposition.up(j), i + 1 - j);
        }

    }
}

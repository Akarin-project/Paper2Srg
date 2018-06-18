package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenHugeTrees extends WorldGenAbstractTree {

    protected final int baseHeight;
    protected final IBlockState woodMetadata;
    protected final IBlockState leavesMetadata;
    protected int extraRandomHeight;

    public WorldGenHugeTrees(boolean flag, int i, int j, IBlockState iblockdata, IBlockState iblockdata1) {
        super(flag);
        this.baseHeight = i;
        this.extraRandomHeight = j;
        this.woodMetadata = iblockdata;
        this.leavesMetadata = iblockdata1;
    }

    protected int getHeight(Random random) {
        int i = random.nextInt(3) + this.baseHeight;

        if (this.extraRandomHeight > 1) {
            i += random.nextInt(this.extraRandomHeight);
        }

        return i;
    }

    private boolean isSpaceAt(World world, BlockPos blockposition, int i) {
        boolean flag = true;

        if (blockposition.getY() >= 1 && blockposition.getY() + i + 1 <= 256) {
            for (int j = 0; j <= 1 + i; ++j) {
                byte b0 = 2;

                if (j == 0) {
                    b0 = 1;
                } else if (j >= 1 + i - 2) {
                    b0 = 2;
                }

                for (int k = -b0; k <= b0 && flag; ++k) {
                    for (int l = -b0; l <= b0 && flag; ++l) {
                        if (blockposition.getY() + j < 0 || blockposition.getY() + j >= 256 || (!this.canGrowInto(world.getBlockState(blockposition.add(k, j, l)).getBlock()) && world.getBlockState(blockposition.add(k, j, l)).getBlock() != Blocks.SAPLING)) { // CraftBukkit - ignore our own saplings
                            flag = false;
                        }
                    }
                }
            }

            return flag;
        } else {
            return false;
        }
    }

    private boolean ensureDirtsUnderneath(BlockPos blockposition, World world) {
        BlockPos blockposition1 = blockposition.down();
        Block block = world.getBlockState(blockposition1).getBlock();

        if ((block == Blocks.GRASS || block == Blocks.DIRT) && blockposition.getY() >= 2) {
            this.setDirtAt(world, blockposition1);
            this.setDirtAt(world, blockposition1.east());
            this.setDirtAt(world, blockposition1.south());
            this.setDirtAt(world, blockposition1.south().east());
            return true;
        } else {
            return false;
        }
    }

    protected boolean ensureGrowable(World world, Random random, BlockPos blockposition, int i) {
        return this.isSpaceAt(world, blockposition, i) && this.ensureDirtsUnderneath(blockposition, world);
    }

    protected void growLeavesLayerStrict(World world, BlockPos blockposition, int i) {
        int j = i * i;

        for (int k = -i; k <= i + 1; ++k) {
            for (int l = -i; l <= i + 1; ++l) {
                int i1 = k - 1;
                int j1 = l - 1;

                if (k * k + l * l <= j || i1 * i1 + j1 * j1 <= j || k * k + j1 * j1 <= j || i1 * i1 + l * l <= j) {
                    BlockPos blockposition1 = blockposition.add(k, 0, l);
                    Material material = world.getBlockState(blockposition1).getMaterial();

                    if (material == Material.AIR || material == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(world, blockposition1, this.leavesMetadata);
                    }
                }
            }
        }

    }

    protected void growLeavesLayer(World world, BlockPos blockposition, int i) {
        int j = i * i;

        for (int k = -i; k <= i; ++k) {
            for (int l = -i; l <= i; ++l) {
                if (k * k + l * l <= j) {
                    BlockPos blockposition1 = blockposition.add(k, 0, l);
                    Material material = world.getBlockState(blockposition1).getMaterial();

                    if (material == Material.AIR || material == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(world, blockposition1, this.leavesMetadata);
                    }
                }
            }
        }

    }
}

package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBirchTree extends WorldGenAbstractTree {

    private static final IBlockState LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false));
    private final boolean useExtraRandomHeight;

    public WorldGenBirchTree(boolean flag, boolean flag1) {
        super(flag);
        this.useExtraRandomHeight = flag1;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + 5;

        if (this.useExtraRandomHeight) {
            i += random.nextInt(7);
        }

        boolean flag = true;

        if (blockposition.getY() >= 1 && blockposition.getY() + i + 1 <= 256) {
            int j;
            int k;

            for (int l = blockposition.getY(); l <= blockposition.getY() + 1 + i; ++l) {
                byte b0 = 1;

                if (l == blockposition.getY()) {
                    b0 = 0;
                }

                if (l >= blockposition.getY() + 1 + i - 2) {
                    b0 = 2;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (j = blockposition.getX() - b0; j <= blockposition.getX() + b0 && flag; ++j) {
                    for (k = blockposition.getZ() - b0; k <= blockposition.getZ() + b0 && flag; ++k) {
                        if (l >= 0 && l < 256) {
                            if (!this.canGrowInto(world.getBlockState(blockposition_mutableblockposition.setPos(j, l, k)).getBlock())) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                Block block = world.getBlockState(blockposition.down()).getBlock();

                if ((block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.FARMLAND) && blockposition.getY() < 256 - i - 1) {
                    this.setDirtAt(world, blockposition.down());

                    int i1;

                    for (i1 = blockposition.getY() - 3 + i; i1 <= blockposition.getY() + i; ++i1) {
                        int j1 = i1 - (blockposition.getY() + i);

                        j = 1 - j1 / 2;

                        for (k = blockposition.getX() - j; k <= blockposition.getX() + j; ++k) {
                            int k1 = k - blockposition.getX();

                            for (int l1 = blockposition.getZ() - j; l1 <= blockposition.getZ() + j; ++l1) {
                                int i2 = l1 - blockposition.getZ();

                                if (Math.abs(k1) != j || Math.abs(i2) != j || random.nextInt(2) != 0 && j1 != 0) {
                                    BlockPos blockposition1 = new BlockPos(k, i1, l1);
                                    Material material = world.getBlockState(blockposition1).getMaterial();

                                    if (material == Material.AIR || material == Material.LEAVES) {
                                        this.setBlockAndNotifyAdequately(world, blockposition1, WorldGenBirchTree.LEAF);
                                    }
                                }
                            }
                        }
                    }

                    for (i1 = 0; i1 < i; ++i1) {
                        Material material1 = world.getBlockState(blockposition.up(i1)).getMaterial();

                        if (material1 == Material.AIR || material1 == Material.LEAVES) {
                            this.setBlockAndNotifyAdequately(world, blockposition.up(i1), WorldGenBirchTree.LOG);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}

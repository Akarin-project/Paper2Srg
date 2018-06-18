package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga2 extends WorldGenAbstractTree {

    private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public WorldGenTaiga2(boolean flag) {
        super(flag);
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(4) + 6;
        int j = 1 + random.nextInt(2);
        int k = i - j;
        int l = 2 + random.nextInt(2);
        boolean flag = true;

        if (blockposition.getY() >= 1 && blockposition.getY() + i + 1 <= 256) {
            int i1;
            int j1;

            for (int k1 = blockposition.getY(); k1 <= blockposition.getY() + 1 + i && flag; ++k1) {
                if (k1 - blockposition.getY() < j) {
                    i1 = 0;
                } else {
                    i1 = l;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (int l1 = blockposition.getX() - i1; l1 <= blockposition.getX() + i1 && flag; ++l1) {
                    for (j1 = blockposition.getZ() - i1; j1 <= blockposition.getZ() + i1 && flag; ++j1) {
                        if (k1 >= 0 && k1 < 256) {
                            Material material = world.getBlockState(blockposition_mutableblockposition.setPos(l1, k1, j1)).getMaterial();

                            if (material != Material.AIR && material != Material.LEAVES) {
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
                    i1 = random.nextInt(2);
                    int i2 = 1;
                    byte b0 = 0;

                    int j2;

                    for (j1 = 0; j1 <= k; ++j1) {
                        j2 = blockposition.getY() + i - j1;

                        for (int k2 = blockposition.getX() - i1; k2 <= blockposition.getX() + i1; ++k2) {
                            int l2 = k2 - blockposition.getX();

                            for (int i3 = blockposition.getZ() - i1; i3 <= blockposition.getZ() + i1; ++i3) {
                                int j3 = i3 - blockposition.getZ();

                                if (Math.abs(l2) != i1 || Math.abs(j3) != i1 || i1 <= 0) {
                                    BlockPos blockposition1 = new BlockPos(k2, j2, i3);

                                    if (!world.getBlockState(blockposition1).isFullBlock()) {
                                        this.setBlockAndNotifyAdequately(world, blockposition1, WorldGenTaiga2.LEAF);
                                    }
                                }
                            }
                        }

                        if (i1 >= i2) {
                            i1 = b0;
                            b0 = 1;
                            ++i2;
                            if (i2 > l) {
                                i2 = l;
                            }
                        } else {
                            ++i1;
                        }
                    }

                    j1 = random.nextInt(3);

                    for (j2 = 0; j2 < i - j1; ++j2) {
                        Material material1 = world.getBlockState(blockposition.up(j2)).getMaterial();

                        if (material1 == Material.AIR || material1 == Material.LEAVES) {
                            this.setBlockAndNotifyAdequately(world, blockposition.up(j2), WorldGenTaiga2.TRUNK);
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

package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree {

    private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false));

    public WorldGenSwamp() {
        super(false);
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i;

        for (i = random.nextInt(4) + 5; world.getBlockState(blockposition.down()).getMaterial() == Material.WATER; blockposition = blockposition.down()) {
            ;
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
                    b0 = 3;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (j = blockposition.getX() - b0; j <= blockposition.getX() + b0 && flag; ++j) {
                    for (k = blockposition.getZ() - b0; k <= blockposition.getZ() + b0 && flag; ++k) {
                        if (l >= 0 && l < 256) {
                            IBlockState iblockdata = world.getBlockState(blockposition_mutableblockposition.setPos(j, l, k));
                            Block block = iblockdata.getBlock();

                            if (iblockdata.getMaterial() != Material.AIR && iblockdata.getMaterial() != Material.LEAVES) {
                                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
                                    flag = false;
                                } else if (l > blockposition.getY()) {
                                    flag = false;
                                }
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
                Block block1 = world.getBlockState(blockposition.down()).getBlock();

                if ((block1 == Blocks.GRASS || block1 == Blocks.DIRT) && blockposition.getY() < 256 - i - 1) {
                    this.setDirtAt(world, blockposition.down());

                    BlockPos blockposition1;
                    int i1;
                    int j1;
                    int k1;
                    int l1;

                    for (i1 = blockposition.getY() - 3 + i; i1 <= blockposition.getY() + i; ++i1) {
                        j1 = i1 - (blockposition.getY() + i);
                        j = 2 - j1 / 2;

                        for (k = blockposition.getX() - j; k <= blockposition.getX() + j; ++k) {
                            k1 = k - blockposition.getX();

                            for (l1 = blockposition.getZ() - j; l1 <= blockposition.getZ() + j; ++l1) {
                                int i2 = l1 - blockposition.getZ();

                                if (Math.abs(k1) != j || Math.abs(i2) != j || random.nextInt(2) != 0 && j1 != 0) {
                                    blockposition1 = new BlockPos(k, i1, l1);
                                    if (!world.getBlockState(blockposition1).isFullBlock()) {
                                        this.setBlockAndNotifyAdequately(world, blockposition1, WorldGenSwamp.LEAF);
                                    }
                                }
                            }
                        }
                    }

                    for (i1 = 0; i1 < i; ++i1) {
                        IBlockState iblockdata1 = world.getBlockState(blockposition.up(i1));
                        Block block2 = iblockdata1.getBlock();

                        if (iblockdata1.getMaterial() == Material.AIR || iblockdata1.getMaterial() == Material.LEAVES || block2 == Blocks.FLOWING_WATER || block2 == Blocks.WATER) {
                            this.setBlockAndNotifyAdequately(world, blockposition.up(i1), WorldGenSwamp.TRUNK);
                        }
                    }

                    for (i1 = blockposition.getY() - 3 + i; i1 <= blockposition.getY() + i; ++i1) {
                        j1 = i1 - (blockposition.getY() + i);
                        j = 2 - j1 / 2;
                        BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();

                        for (k1 = blockposition.getX() - j; k1 <= blockposition.getX() + j; ++k1) {
                            for (l1 = blockposition.getZ() - j; l1 <= blockposition.getZ() + j; ++l1) {
                                blockposition_mutableblockposition1.setPos(k1, i1, l1);
                                if (world.getBlockState(blockposition_mutableblockposition1).getMaterial() == Material.LEAVES) {
                                    BlockPos blockposition2 = blockposition_mutableblockposition1.west();

                                    blockposition1 = blockposition_mutableblockposition1.east();
                                    BlockPos blockposition3 = blockposition_mutableblockposition1.north();
                                    BlockPos blockposition4 = blockposition_mutableblockposition1.south();

                                    if (random.nextInt(4) == 0 && world.getBlockState(blockposition2).getMaterial() == Material.AIR) {
                                        this.addVine(world, blockposition2, BlockVine.EAST);
                                    }

                                    if (random.nextInt(4) == 0 && world.getBlockState(blockposition1).getMaterial() == Material.AIR) {
                                        this.addVine(world, blockposition1, BlockVine.WEST);
                                    }

                                    if (random.nextInt(4) == 0 && world.getBlockState(blockposition3).getMaterial() == Material.AIR) {
                                        this.addVine(world, blockposition3, BlockVine.SOUTH);
                                    }

                                    if (random.nextInt(4) == 0 && world.getBlockState(blockposition4).getMaterial() == Material.AIR) {
                                        this.addVine(world, blockposition4, BlockVine.NORTH);
                                    }
                                }
                            }
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

    private void addVine(World world, BlockPos blockposition, PropertyBool blockstateboolean) {
        IBlockState iblockdata = Blocks.VINE.getDefaultState().withProperty(blockstateboolean, Boolean.valueOf(true));

        this.setBlockAndNotifyAdequately(world, blockposition, iblockdata);
        int i = 4;

        for (blockposition = blockposition.down(); world.getBlockState(blockposition).getMaterial() == Material.AIR && i > 0; --i) {
            this.setBlockAndNotifyAdequately(world, blockposition, iblockdata);
            blockposition = blockposition.down();
        }

    }
}

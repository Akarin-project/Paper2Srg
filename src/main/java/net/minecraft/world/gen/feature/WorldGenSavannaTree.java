package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSavannaTree extends WorldGenAbstractTree {

    private static final IBlockState TRUNK = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
    private static final IBlockState LEAF = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public WorldGenSavannaTree(boolean flag) {
        super(flag);
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + random.nextInt(3) + 5;
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

                if ((block == Blocks.GRASS || block == Blocks.DIRT) && blockposition.getY() < 256 - i - 1) {
                    this.setDirtAt(world, blockposition.down());
                    EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.random(random);
                    int i1 = i - random.nextInt(4) - 1;

                    j = 3 - random.nextInt(3);
                    k = blockposition.getX();
                    int j1 = blockposition.getZ();
                    int k1 = 0;

                    int l1;

                    for (int i2 = 0; i2 < i; ++i2) {
                        l1 = blockposition.getY() + i2;
                        if (i2 >= i1 && j > 0) {
                            k += enumdirection.getFrontOffsetX();
                            j1 += enumdirection.getFrontOffsetZ();
                            --j;
                        }

                        BlockPos blockposition1 = new BlockPos(k, l1, j1);
                        Material material = world.getBlockState(blockposition1).getMaterial();

                        if (material == Material.AIR || material == Material.LEAVES) {
                            this.placeLogAt(world, blockposition1);
                            k1 = l1;
                        }
                    }

                    BlockPos blockposition2 = new BlockPos(k, k1, j1);

                    int j2;

                    for (l1 = -3; l1 <= 3; ++l1) {
                        for (j2 = -3; j2 <= 3; ++j2) {
                            if (Math.abs(l1) != 3 || Math.abs(j2) != 3) {
                                this.placeLeafAt(world, blockposition2.add(l1, 0, j2));
                            }
                        }
                    }

                    blockposition2 = blockposition2.up();

                    for (l1 = -1; l1 <= 1; ++l1) {
                        for (j2 = -1; j2 <= 1; ++j2) {
                            this.placeLeafAt(world, blockposition2.add(l1, 0, j2));
                        }
                    }

                    this.placeLeafAt(world, blockposition2.east(2));
                    this.placeLeafAt(world, blockposition2.west(2));
                    this.placeLeafAt(world, blockposition2.south(2));
                    this.placeLeafAt(world, blockposition2.north(2));
                    k = blockposition.getX();
                    j1 = blockposition.getZ();
                    EnumFacing enumdirection1 = EnumFacing.Plane.HORIZONTAL.random(random);

                    if (enumdirection1 != enumdirection) {
                        l1 = i1 - random.nextInt(2) - 1;
                        j2 = 1 + random.nextInt(3);
                        k1 = 0;

                        int k2;

                        for (int l2 = l1; l2 < i && j2 > 0; --j2) {
                            if (l2 >= 1) {
                                k2 = blockposition.getY() + l2;
                                k += enumdirection1.getFrontOffsetX();
                                j1 += enumdirection1.getFrontOffsetZ();
                                BlockPos blockposition3 = new BlockPos(k, k2, j1);
                                Material material1 = world.getBlockState(blockposition3).getMaterial();

                                if (material1 == Material.AIR || material1 == Material.LEAVES) {
                                    this.placeLogAt(world, blockposition3);
                                    k1 = k2;
                                }
                            }

                            ++l2;
                        }

                        if (k1 > 0) {
                            BlockPos blockposition4 = new BlockPos(k, k1, j1);

                            int i3;

                            for (k2 = -2; k2 <= 2; ++k2) {
                                for (i3 = -2; i3 <= 2; ++i3) {
                                    if (Math.abs(k2) != 2 || Math.abs(i3) != 2) {
                                        this.placeLeafAt(world, blockposition4.add(k2, 0, i3));
                                    }
                                }
                            }

                            blockposition4 = blockposition4.up();

                            for (k2 = -1; k2 <= 1; ++k2) {
                                for (i3 = -1; i3 <= 1; ++i3) {
                                    this.placeLeafAt(world, blockposition4.add(k2, 0, i3));
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

    private void placeLogAt(World world, BlockPos blockposition) {
        this.setBlockAndNotifyAdequately(world, blockposition, WorldGenSavannaTree.TRUNK);
    }

    private void placeLeafAt(World world, BlockPos blockposition) {
        Material material = world.getBlockState(blockposition).getMaterial();

        if (material == Material.AIR || material == Material.LEAVES) {
            this.setBlockAndNotifyAdequately(world, blockposition, WorldGenSavannaTree.LEAF);
        }

    }
}

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

public class WorldGenCanopyTree extends WorldGenAbstractTree {

    private static final IBlockState DARK_OAK_LOG = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
    private static final IBlockState DARK_OAK_LEAVES = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public WorldGenCanopyTree(boolean flag) {
        super(flag);
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + random.nextInt(2) + 6;
        int j = blockposition.getX();
        int k = blockposition.getY();
        int l = blockposition.getZ();

        if (k >= 1 && k + i + 1 < 256) {
            BlockPos blockposition1 = blockposition.down();
            Block block = world.getBlockState(blockposition1).getBlock();

            if (block != Blocks.GRASS && block != Blocks.DIRT) {
                return false;
            } else if (!this.placeTreeOfHeight(world, blockposition, i)) {
                return false;
            } else {
                this.setDirtAt(world, blockposition1);
                this.setDirtAt(world, blockposition1.east());
                this.setDirtAt(world, blockposition1.south());
                this.setDirtAt(world, blockposition1.south().east());
                EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.random(random);
                int i1 = i - random.nextInt(4);
                int j1 = 2 - random.nextInt(3);
                int k1 = j;
                int l1 = l;
                int i2 = k + i - 1;

                int j2;
                int k2;

                for (j2 = 0; j2 < i; ++j2) {
                    if (j2 >= i1 && j1 > 0) {
                        k1 += enumdirection.getFrontOffsetX();
                        l1 += enumdirection.getFrontOffsetZ();
                        --j1;
                    }

                    k2 = k + j2;
                    BlockPos blockposition2 = new BlockPos(k1, k2, l1);
                    Material material = world.getBlockState(blockposition2).getMaterial();

                    if (material == Material.AIR || material == Material.LEAVES) {
                        this.placeLogAt(world, blockposition2);
                        this.placeLogAt(world, blockposition2.east());
                        this.placeLogAt(world, blockposition2.south());
                        this.placeLogAt(world, blockposition2.east().south());
                    }
                }

                for (j2 = -2; j2 <= 0; ++j2) {
                    for (k2 = -2; k2 <= 0; ++k2) {
                        byte b0 = -1;

                        this.placeLeafAt(world, k1 + j2, i2 + b0, l1 + k2);
                        this.placeLeafAt(world, 1 + k1 - j2, i2 + b0, l1 + k2);
                        this.placeLeafAt(world, k1 + j2, i2 + b0, 1 + l1 - k2);
                        this.placeLeafAt(world, 1 + k1 - j2, i2 + b0, 1 + l1 - k2);
                        if ((j2 > -2 || k2 > -1) && (j2 != -1 || k2 != -2)) {
                            byte b1 = 1;

                            this.placeLeafAt(world, k1 + j2, i2 + b1, l1 + k2);
                            this.placeLeafAt(world, 1 + k1 - j2, i2 + b1, l1 + k2);
                            this.placeLeafAt(world, k1 + j2, i2 + b1, 1 + l1 - k2);
                            this.placeLeafAt(world, 1 + k1 - j2, i2 + b1, 1 + l1 - k2);
                        }
                    }
                }

                if (random.nextBoolean()) {
                    this.placeLeafAt(world, k1, i2 + 2, l1);
                    this.placeLeafAt(world, k1 + 1, i2 + 2, l1);
                    this.placeLeafAt(world, k1 + 1, i2 + 2, l1 + 1);
                    this.placeLeafAt(world, k1, i2 + 2, l1 + 1);
                }

                for (j2 = -3; j2 <= 4; ++j2) {
                    for (k2 = -3; k2 <= 4; ++k2) {
                        if ((j2 != -3 || k2 != -3) && (j2 != -3 || k2 != 4) && (j2 != 4 || k2 != -3) && (j2 != 4 || k2 != 4) && (Math.abs(j2) < 3 || Math.abs(k2) < 3)) {
                            this.placeLeafAt(world, k1 + j2, i2, l1 + k2);
                        }
                    }
                }

                for (j2 = -1; j2 <= 2; ++j2) {
                    for (k2 = -1; k2 <= 2; ++k2) {
                        if ((j2 < 0 || j2 > 1 || k2 < 0 || k2 > 1) && random.nextInt(3) <= 0) {
                            int l2 = random.nextInt(3) + 2;

                            int i3;

                            for (i3 = 0; i3 < l2; ++i3) {
                                this.placeLogAt(world, new BlockPos(j + j2, i2 - i3 - 1, l + k2));
                            }

                            int j3;

                            for (i3 = -1; i3 <= 1; ++i3) {
                                for (j3 = -1; j3 <= 1; ++j3) {
                                    this.placeLeafAt(world, k1 + j2 + i3, i2, l1 + k2 + j3);
                                }
                            }

                            for (i3 = -2; i3 <= 2; ++i3) {
                                for (j3 = -2; j3 <= 2; ++j3) {
                                    if (Math.abs(i3) != 2 || Math.abs(j3) != 2) {
                                        this.placeLeafAt(world, k1 + j2 + i3, i2 - 1, l1 + k2 + j3);
                                    }
                                }
                            }
                        }
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    private boolean placeTreeOfHeight(World world, BlockPos blockposition, int i) {
        int j = blockposition.getX();
        int k = blockposition.getY();
        int l = blockposition.getZ();
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int i1 = 0; i1 <= i + 1; ++i1) {
            byte b0 = 1;

            if (i1 == 0) {
                b0 = 0;
            }

            if (i1 >= i - 1) {
                b0 = 2;
            }

            for (int j1 = -b0; j1 <= b0; ++j1) {
                for (int k1 = -b0; k1 <= b0; ++k1) {
                    if (!this.canGrowInto(world.getBlockState(blockposition_mutableblockposition.setPos(j + j1, k + i1, l + k1)).getBlock())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void placeLogAt(World world, BlockPos blockposition) {
        if (this.canGrowInto(world.getBlockState(blockposition).getBlock())) {
            this.setBlockAndNotifyAdequately(world, blockposition, WorldGenCanopyTree.DARK_OAK_LOG);
        }

    }

    private void placeLeafAt(World world, int i, int j, int k) {
        BlockPos blockposition = new BlockPos(i, j, k);
        Material material = world.getBlockState(blockposition).getMaterial();

        if (material == Material.AIR) {
            this.setBlockAndNotifyAdequately(world, blockposition, WorldGenCanopyTree.DARK_OAK_LEAVES);
        }

    }
}

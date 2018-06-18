package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBigMushroom extends WorldGenerator {

    private final Block mushroomType;

    public WorldGenBigMushroom(Block block) {
        super(true);
        this.mushroomType = block;
    }

    public WorldGenBigMushroom() {
        super(false);
        this.mushroomType = null;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        Block block = this.mushroomType;

        if (block == null) {
            block = random.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK;
        }

        int i = random.nextInt(3) + 4;

        if (random.nextInt(12) == 0) {
            i *= 2;
        }

        boolean flag = true;

        if (blockposition.getY() >= 1 && blockposition.getY() + i + 1 < 256) {
            int j;
            int k;

            for (int l = blockposition.getY(); l <= blockposition.getY() + 1 + i; ++l) {
                byte b0 = 3;

                if (l <= blockposition.getY() + 3) {
                    b0 = 0;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (j = blockposition.getX() - b0; j <= blockposition.getX() + b0 && flag; ++j) {
                    for (k = blockposition.getZ() - b0; k <= blockposition.getZ() + b0 && flag; ++k) {
                        if (l >= 0 && l < 256) {
                            Material material = world.getBlockState(blockposition_mutableblockposition.setPos(j, l, k)).getMaterial();

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
                Block block1 = world.getBlockState(blockposition.down()).getBlock();

                if (block1 != Blocks.DIRT && block1 != Blocks.GRASS && block1 != Blocks.MYCELIUM) {
                    return false;
                } else {
                    int i1 = blockposition.getY() + i;

                    if (block == Blocks.RED_MUSHROOM_BLOCK) {
                        i1 = blockposition.getY() + i - 3;
                    }

                    int j1;

                    for (j1 = i1; j1 <= blockposition.getY() + i; ++j1) {
                        j = 1;
                        if (j1 < blockposition.getY() + i) {
                            ++j;
                        }

                        if (block == Blocks.BROWN_MUSHROOM_BLOCK) {
                            j = 3;
                        }

                        k = blockposition.getX() - j;
                        int k1 = blockposition.getX() + j;
                        int l1 = blockposition.getZ() - j;
                        int i2 = blockposition.getZ() + j;

                        for (int j2 = k; j2 <= k1; ++j2) {
                            for (int k2 = l1; k2 <= i2; ++k2) {
                                int l2 = 5;

                                if (j2 == k) {
                                    --l2;
                                } else if (j2 == k1) {
                                    ++l2;
                                }

                                if (k2 == l1) {
                                    l2 -= 3;
                                } else if (k2 == i2) {
                                    l2 += 3;
                                }

                                BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.byMetadata(l2);

                                if (block == Blocks.BROWN_MUSHROOM_BLOCK || j1 < blockposition.getY() + i) {
                                    if ((j2 == k || j2 == k1) && (k2 == l1 || k2 == i2)) {
                                        continue;
                                    }

                                    if (j2 == blockposition.getX() - (j - 1) && k2 == l1) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_WEST;
                                    }

                                    if (j2 == k && k2 == blockposition.getZ() - (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_WEST;
                                    }

                                    if (j2 == blockposition.getX() + (j - 1) && k2 == l1) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_EAST;
                                    }

                                    if (j2 == k1 && k2 == blockposition.getZ() - (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_EAST;
                                    }

                                    if (j2 == blockposition.getX() - (j - 1) && k2 == i2) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                    }

                                    if (j2 == k && k2 == blockposition.getZ() + (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                    }

                                    if (j2 == blockposition.getX() + (j - 1) && k2 == i2) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                    }

                                    if (j2 == k1 && k2 == blockposition.getZ() + (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                    }
                                }

                                if (blockhugemushroom_enumhugemushroomvariant == BlockHugeMushroom.EnumType.CENTER && j1 < blockposition.getY() + i) {
                                    blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.ALL_INSIDE;
                                }

                                if (blockposition.getY() >= blockposition.getY() + i - 1 || blockhugemushroom_enumhugemushroomvariant != BlockHugeMushroom.EnumType.ALL_INSIDE) {
                                    BlockPos blockposition1 = new BlockPos(j2, j1, k2);

                                    if (!world.getBlockState(blockposition1).isFullBlock()) {
                                        this.setBlockAndNotifyAdequately(world, blockposition1, block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom_enumhugemushroomvariant));
                                    }
                                }
                            }
                        }
                    }

                    for (j1 = 0; j1 < i; ++j1) {
                        IBlockState iblockdata = world.getBlockState(blockposition.up(j1));

                        if (!iblockdata.isFullBlock()) {
                            this.setBlockAndNotifyAdequately(world, blockposition.up(j1), block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
                        }
                    }

                    return true;
                }
            }
        } else {
            return false;
        }
    }
}

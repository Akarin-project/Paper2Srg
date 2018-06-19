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

    private final Block field_76523_a;

    public WorldGenBigMushroom(Block block) {
        super(true);
        this.field_76523_a = block;
    }

    public WorldGenBigMushroom() {
        super(false);
        this.field_76523_a = null;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        Block block = this.field_76523_a;

        if (block == null) {
            block = random.nextBoolean() ? Blocks.field_150420_aW : Blocks.field_150419_aX;
        }

        int i = random.nextInt(3) + 4;

        if (random.nextInt(12) == 0) {
            i *= 2;
        }

        boolean flag = true;

        if (blockposition.func_177956_o() >= 1 && blockposition.func_177956_o() + i + 1 < 256) {
            int j;
            int k;

            for (int l = blockposition.func_177956_o(); l <= blockposition.func_177956_o() + 1 + i; ++l) {
                byte b0 = 3;

                if (l <= blockposition.func_177956_o() + 3) {
                    b0 = 0;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (j = blockposition.func_177958_n() - b0; j <= blockposition.func_177958_n() + b0 && flag; ++j) {
                    for (k = blockposition.func_177952_p() - b0; k <= blockposition.func_177952_p() + b0 && flag; ++k) {
                        if (l >= 0 && l < 256) {
                            Material material = world.func_180495_p(blockposition_mutableblockposition.func_181079_c(j, l, k)).func_185904_a();

                            if (material != Material.field_151579_a && material != Material.field_151584_j) {
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
                Block block1 = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

                if (block1 != Blocks.field_150346_d && block1 != Blocks.field_150349_c && block1 != Blocks.field_150391_bh) {
                    return false;
                } else {
                    int i1 = blockposition.func_177956_o() + i;

                    if (block == Blocks.field_150419_aX) {
                        i1 = blockposition.func_177956_o() + i - 3;
                    }

                    int j1;

                    for (j1 = i1; j1 <= blockposition.func_177956_o() + i; ++j1) {
                        j = 1;
                        if (j1 < blockposition.func_177956_o() + i) {
                            ++j;
                        }

                        if (block == Blocks.field_150420_aW) {
                            j = 3;
                        }

                        k = blockposition.func_177958_n() - j;
                        int k1 = blockposition.func_177958_n() + j;
                        int l1 = blockposition.func_177952_p() - j;
                        int i2 = blockposition.func_177952_p() + j;

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

                                BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.func_176895_a(l2);

                                if (block == Blocks.field_150420_aW || j1 < blockposition.func_177956_o() + i) {
                                    if ((j2 == k || j2 == k1) && (k2 == l1 || k2 == i2)) {
                                        continue;
                                    }

                                    if (j2 == blockposition.func_177958_n() - (j - 1) && k2 == l1) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_WEST;
                                    }

                                    if (j2 == k && k2 == blockposition.func_177952_p() - (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_WEST;
                                    }

                                    if (j2 == blockposition.func_177958_n() + (j - 1) && k2 == l1) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_EAST;
                                    }

                                    if (j2 == k1 && k2 == blockposition.func_177952_p() - (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.NORTH_EAST;
                                    }

                                    if (j2 == blockposition.func_177958_n() - (j - 1) && k2 == i2) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                    }

                                    if (j2 == k && k2 == blockposition.func_177952_p() + (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                    }

                                    if (j2 == blockposition.func_177958_n() + (j - 1) && k2 == i2) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                    }

                                    if (j2 == k1 && k2 == blockposition.func_177952_p() + (j - 1)) {
                                        blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                    }
                                }

                                if (blockhugemushroom_enumhugemushroomvariant == BlockHugeMushroom.EnumType.CENTER && j1 < blockposition.func_177956_o() + i) {
                                    blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.ALL_INSIDE;
                                }

                                if (blockposition.func_177956_o() >= blockposition.func_177956_o() + i - 1 || blockhugemushroom_enumhugemushroomvariant != BlockHugeMushroom.EnumType.ALL_INSIDE) {
                                    BlockPos blockposition1 = new BlockPos(j2, j1, k2);

                                    if (!world.func_180495_p(blockposition1).func_185913_b()) {
                                        this.func_175903_a(world, blockposition1, block.func_176223_P().func_177226_a(BlockHugeMushroom.field_176380_a, blockhugemushroom_enumhugemushroomvariant));
                                    }
                                }
                            }
                        }
                    }

                    for (j1 = 0; j1 < i; ++j1) {
                        IBlockState iblockdata = world.func_180495_p(blockposition.func_177981_b(j1));

                        if (!iblockdata.func_185913_b()) {
                            this.func_175903_a(world, blockposition.func_177981_b(j1), block.func_176223_P().func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.STEM));
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

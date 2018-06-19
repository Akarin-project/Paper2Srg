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

    private static final IBlockState field_181648_a = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.OAK);
    private static final IBlockState field_181649_b = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.OAK).func_177226_a(BlockOldLeaf.field_176236_b, Boolean.valueOf(false));

    public WorldGenSwamp() {
        super(false);
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i;

        for (i = random.nextInt(4) + 5; world.func_180495_p(blockposition.func_177977_b()).func_185904_a() == Material.field_151586_h; blockposition = blockposition.func_177977_b()) {
            ;
        }

        boolean flag = true;

        if (blockposition.func_177956_o() >= 1 && blockposition.func_177956_o() + i + 1 <= 256) {
            int j;
            int k;

            for (int l = blockposition.func_177956_o(); l <= blockposition.func_177956_o() + 1 + i; ++l) {
                byte b0 = 1;

                if (l == blockposition.func_177956_o()) {
                    b0 = 0;
                }

                if (l >= blockposition.func_177956_o() + 1 + i - 2) {
                    b0 = 3;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (j = blockposition.func_177958_n() - b0; j <= blockposition.func_177958_n() + b0 && flag; ++j) {
                    for (k = blockposition.func_177952_p() - b0; k <= blockposition.func_177952_p() + b0 && flag; ++k) {
                        if (l >= 0 && l < 256) {
                            IBlockState iblockdata = world.func_180495_p(blockposition_mutableblockposition.func_181079_c(j, l, k));
                            Block block = iblockdata.func_177230_c();

                            if (iblockdata.func_185904_a() != Material.field_151579_a && iblockdata.func_185904_a() != Material.field_151584_j) {
                                if (block != Blocks.field_150355_j && block != Blocks.field_150358_i) {
                                    flag = false;
                                } else if (l > blockposition.func_177956_o()) {
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
                Block block1 = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

                if ((block1 == Blocks.field_150349_c || block1 == Blocks.field_150346_d) && blockposition.func_177956_o() < 256 - i - 1) {
                    this.func_175921_a(world, blockposition.func_177977_b());

                    BlockPos blockposition1;
                    int i1;
                    int j1;
                    int k1;
                    int l1;

                    for (i1 = blockposition.func_177956_o() - 3 + i; i1 <= blockposition.func_177956_o() + i; ++i1) {
                        j1 = i1 - (blockposition.func_177956_o() + i);
                        j = 2 - j1 / 2;

                        for (k = blockposition.func_177958_n() - j; k <= blockposition.func_177958_n() + j; ++k) {
                            k1 = k - blockposition.func_177958_n();

                            for (l1 = blockposition.func_177952_p() - j; l1 <= blockposition.func_177952_p() + j; ++l1) {
                                int i2 = l1 - blockposition.func_177952_p();

                                if (Math.abs(k1) != j || Math.abs(i2) != j || random.nextInt(2) != 0 && j1 != 0) {
                                    blockposition1 = new BlockPos(k, i1, l1);
                                    if (!world.func_180495_p(blockposition1).func_185913_b()) {
                                        this.func_175903_a(world, blockposition1, WorldGenSwamp.field_181649_b);
                                    }
                                }
                            }
                        }
                    }

                    for (i1 = 0; i1 < i; ++i1) {
                        IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177981_b(i1));
                        Block block2 = iblockdata1.func_177230_c();

                        if (iblockdata1.func_185904_a() == Material.field_151579_a || iblockdata1.func_185904_a() == Material.field_151584_j || block2 == Blocks.field_150358_i || block2 == Blocks.field_150355_j) {
                            this.func_175903_a(world, blockposition.func_177981_b(i1), WorldGenSwamp.field_181648_a);
                        }
                    }

                    for (i1 = blockposition.func_177956_o() - 3 + i; i1 <= blockposition.func_177956_o() + i; ++i1) {
                        j1 = i1 - (blockposition.func_177956_o() + i);
                        j = 2 - j1 / 2;
                        BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();

                        for (k1 = blockposition.func_177958_n() - j; k1 <= blockposition.func_177958_n() + j; ++k1) {
                            for (l1 = blockposition.func_177952_p() - j; l1 <= blockposition.func_177952_p() + j; ++l1) {
                                blockposition_mutableblockposition1.func_181079_c(k1, i1, l1);
                                if (world.func_180495_p(blockposition_mutableblockposition1).func_185904_a() == Material.field_151584_j) {
                                    BlockPos blockposition2 = blockposition_mutableblockposition1.func_177976_e();

                                    blockposition1 = blockposition_mutableblockposition1.func_177974_f();
                                    BlockPos blockposition3 = blockposition_mutableblockposition1.func_177978_c();
                                    BlockPos blockposition4 = blockposition_mutableblockposition1.func_177968_d();

                                    if (random.nextInt(4) == 0 && world.func_180495_p(blockposition2).func_185904_a() == Material.field_151579_a) {
                                        this.func_181647_a(world, blockposition2, BlockVine.field_176278_M);
                                    }

                                    if (random.nextInt(4) == 0 && world.func_180495_p(blockposition1).func_185904_a() == Material.field_151579_a) {
                                        this.func_181647_a(world, blockposition1, BlockVine.field_176280_O);
                                    }

                                    if (random.nextInt(4) == 0 && world.func_180495_p(blockposition3).func_185904_a() == Material.field_151579_a) {
                                        this.func_181647_a(world, blockposition3, BlockVine.field_176279_N);
                                    }

                                    if (random.nextInt(4) == 0 && world.func_180495_p(blockposition4).func_185904_a() == Material.field_151579_a) {
                                        this.func_181647_a(world, blockposition4, BlockVine.field_176273_b);
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

    private void func_181647_a(World world, BlockPos blockposition, PropertyBool blockstateboolean) {
        IBlockState iblockdata = Blocks.field_150395_bd.func_176223_P().func_177226_a(blockstateboolean, Boolean.valueOf(true));

        this.func_175903_a(world, blockposition, iblockdata);
        int i = 4;

        for (blockposition = blockposition.func_177977_b(); world.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a && i > 0; --i) {
            this.func_175903_a(world, blockposition, iblockdata);
            blockposition = blockposition.func_177977_b();
        }

    }
}

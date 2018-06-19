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

    private static final IBlockState field_181645_a = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181646_b = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.SPRUCE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));

    public WorldGenTaiga2(boolean flag) {
        super(flag);
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(4) + 6;
        int j = 1 + random.nextInt(2);
        int k = i - j;
        int l = 2 + random.nextInt(2);
        boolean flag = true;

        if (blockposition.func_177956_o() >= 1 && blockposition.func_177956_o() + i + 1 <= 256) {
            int i1;
            int j1;

            for (int k1 = blockposition.func_177956_o(); k1 <= blockposition.func_177956_o() + 1 + i && flag; ++k1) {
                if (k1 - blockposition.func_177956_o() < j) {
                    i1 = 0;
                } else {
                    i1 = l;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (int l1 = blockposition.func_177958_n() - i1; l1 <= blockposition.func_177958_n() + i1 && flag; ++l1) {
                    for (j1 = blockposition.func_177952_p() - i1; j1 <= blockposition.func_177952_p() + i1 && flag; ++j1) {
                        if (k1 >= 0 && k1 < 256) {
                            Material material = world.func_180495_p(blockposition_mutableblockposition.func_181079_c(l1, k1, j1)).func_185904_a();

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
                Block block = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

                if ((block == Blocks.field_150349_c || block == Blocks.field_150346_d || block == Blocks.field_150458_ak) && blockposition.func_177956_o() < 256 - i - 1) {
                    this.func_175921_a(world, blockposition.func_177977_b());
                    i1 = random.nextInt(2);
                    int i2 = 1;
                    byte b0 = 0;

                    int j2;

                    for (j1 = 0; j1 <= k; ++j1) {
                        j2 = blockposition.func_177956_o() + i - j1;

                        for (int k2 = blockposition.func_177958_n() - i1; k2 <= blockposition.func_177958_n() + i1; ++k2) {
                            int l2 = k2 - blockposition.func_177958_n();

                            for (int i3 = blockposition.func_177952_p() - i1; i3 <= blockposition.func_177952_p() + i1; ++i3) {
                                int j3 = i3 - blockposition.func_177952_p();

                                if (Math.abs(l2) != i1 || Math.abs(j3) != i1 || i1 <= 0) {
                                    BlockPos blockposition1 = new BlockPos(k2, j2, i3);

                                    if (!world.func_180495_p(blockposition1).func_185913_b()) {
                                        this.func_175903_a(world, blockposition1, WorldGenTaiga2.field_181646_b);
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
                        Material material1 = world.func_180495_p(blockposition.func_177981_b(j2)).func_185904_a();

                        if (material1 == Material.field_151579_a || material1 == Material.field_151584_j) {
                            this.func_175903_a(world, blockposition.func_177981_b(j2), WorldGenTaiga2.field_181645_a);
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

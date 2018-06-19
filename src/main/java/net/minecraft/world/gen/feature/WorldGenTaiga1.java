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

public class WorldGenTaiga1 extends WorldGenAbstractTree {

    private static final IBlockState field_181636_a = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181637_b = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.SPRUCE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));

    public WorldGenTaiga1() {
        super(false);
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(5) + 7;
        int j = i - random.nextInt(2) - 3;
        int k = i - j;
        int l = 1 + random.nextInt(k + 1);

        if (blockposition.func_177956_o() >= 1 && blockposition.func_177956_o() + i + 1 <= 256) {
            boolean flag = true;

            int i1;
            int j1;
            int k1;

            for (int l1 = blockposition.func_177956_o(); l1 <= blockposition.func_177956_o() + 1 + i && flag; ++l1) {
                boolean flag1 = true;

                if (l1 - blockposition.func_177956_o() < j) {
                    k1 = 0;
                } else {
                    k1 = l;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (i1 = blockposition.func_177958_n() - k1; i1 <= blockposition.func_177958_n() + k1 && flag; ++i1) {
                    for (j1 = blockposition.func_177952_p() - k1; j1 <= blockposition.func_177952_p() + k1 && flag; ++j1) {
                        if (l1 >= 0 && l1 < 256) {
                            if (!this.func_150523_a(world.func_180495_p(blockposition_mutableblockposition.func_181079_c(i1, l1, j1)).func_177230_c())) {
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

                if ((block == Blocks.field_150349_c || block == Blocks.field_150346_d) && blockposition.func_177956_o() < 256 - i - 1) {
                    this.func_175921_a(world, blockposition.func_177977_b());
                    k1 = 0;

                    int i2;

                    for (i2 = blockposition.func_177956_o() + i; i2 >= blockposition.func_177956_o() + j; --i2) {
                        for (i1 = blockposition.func_177958_n() - k1; i1 <= blockposition.func_177958_n() + k1; ++i1) {
                            j1 = i1 - blockposition.func_177958_n();

                            for (int j2 = blockposition.func_177952_p() - k1; j2 <= blockposition.func_177952_p() + k1; ++j2) {
                                int k2 = j2 - blockposition.func_177952_p();

                                if (Math.abs(j1) != k1 || Math.abs(k2) != k1 || k1 <= 0) {
                                    BlockPos blockposition1 = new BlockPos(i1, i2, j2);

                                    if (!world.func_180495_p(blockposition1).func_185913_b()) {
                                        this.func_175903_a(world, blockposition1, WorldGenTaiga1.field_181637_b);
                                    }
                                }
                            }
                        }

                        if (k1 >= 1 && i2 == blockposition.func_177956_o() + j + 1) {
                            --k1;
                        } else if (k1 < l) {
                            ++k1;
                        }
                    }

                    for (i2 = 0; i2 < i - 1; ++i2) {
                        Material material = world.func_180495_p(blockposition.func_177981_b(i2)).func_185904_a();

                        if (material == Material.field_151579_a || material == Material.field_151584_j) {
                            this.func_175903_a(world, blockposition.func_177981_b(i2), WorldGenTaiga1.field_181636_a);
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

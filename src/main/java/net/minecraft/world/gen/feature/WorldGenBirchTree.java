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

    private static final IBlockState field_181629_a = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.BIRCH);
    private static final IBlockState field_181630_b = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.BIRCH).func_177226_a(BlockOldLeaf.field_176236_b, Boolean.valueOf(false));
    private final boolean field_150531_a;

    public WorldGenBirchTree(boolean flag, boolean flag1) {
        super(flag);
        this.field_150531_a = flag1;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + 5;

        if (this.field_150531_a) {
            i += random.nextInt(7);
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
                    b0 = 2;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (j = blockposition.func_177958_n() - b0; j <= blockposition.func_177958_n() + b0 && flag; ++j) {
                    for (k = blockposition.func_177952_p() - b0; k <= blockposition.func_177952_p() + b0 && flag; ++k) {
                        if (l >= 0 && l < 256) {
                            if (!this.func_150523_a(world.func_180495_p(blockposition_mutableblockposition.func_181079_c(j, l, k)).func_177230_c())) {
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

                    int i1;

                    for (i1 = blockposition.func_177956_o() - 3 + i; i1 <= blockposition.func_177956_o() + i; ++i1) {
                        int j1 = i1 - (blockposition.func_177956_o() + i);

                        j = 1 - j1 / 2;

                        for (k = blockposition.func_177958_n() - j; k <= blockposition.func_177958_n() + j; ++k) {
                            int k1 = k - blockposition.func_177958_n();

                            for (int l1 = blockposition.func_177952_p() - j; l1 <= blockposition.func_177952_p() + j; ++l1) {
                                int i2 = l1 - blockposition.func_177952_p();

                                if (Math.abs(k1) != j || Math.abs(i2) != j || random.nextInt(2) != 0 && j1 != 0) {
                                    BlockPos blockposition1 = new BlockPos(k, i1, l1);
                                    Material material = world.func_180495_p(blockposition1).func_185904_a();

                                    if (material == Material.field_151579_a || material == Material.field_151584_j) {
                                        this.func_175903_a(world, blockposition1, WorldGenBirchTree.field_181630_b);
                                    }
                                }
                            }
                        }
                    }

                    for (i1 = 0; i1 < i; ++i1) {
                        Material material1 = world.func_180495_p(blockposition.func_177981_b(i1)).func_185904_a();

                        if (material1 == Material.field_151579_a || material1 == Material.field_151584_j) {
                            this.func_175903_a(world, blockposition.func_177981_b(i1), WorldGenBirchTree.field_181629_a);
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

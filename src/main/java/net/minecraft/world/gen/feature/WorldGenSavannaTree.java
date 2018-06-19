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

    private static final IBlockState field_181643_a = Blocks.field_150363_s.func_176223_P().func_177226_a(BlockNewLog.field_176300_b, BlockPlanks.EnumType.ACACIA);
    private static final IBlockState field_181644_b = Blocks.field_150361_u.func_176223_P().func_177226_a(BlockNewLeaf.field_176240_P, BlockPlanks.EnumType.ACACIA).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));

    public WorldGenSavannaTree(boolean flag) {
        super(flag);
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + random.nextInt(3) + 5;
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

                if ((block == Blocks.field_150349_c || block == Blocks.field_150346_d) && blockposition.func_177956_o() < 256 - i - 1) {
                    this.func_175921_a(world, blockposition.func_177977_b());
                    EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.func_179518_a(random);
                    int i1 = i - random.nextInt(4) - 1;

                    j = 3 - random.nextInt(3);
                    k = blockposition.func_177958_n();
                    int j1 = blockposition.func_177952_p();
                    int k1 = 0;

                    int l1;

                    for (int i2 = 0; i2 < i; ++i2) {
                        l1 = blockposition.func_177956_o() + i2;
                        if (i2 >= i1 && j > 0) {
                            k += enumdirection.func_82601_c();
                            j1 += enumdirection.func_82599_e();
                            --j;
                        }

                        BlockPos blockposition1 = new BlockPos(k, l1, j1);
                        Material material = world.func_180495_p(blockposition1).func_185904_a();

                        if (material == Material.field_151579_a || material == Material.field_151584_j) {
                            this.func_181642_b(world, blockposition1);
                            k1 = l1;
                        }
                    }

                    BlockPos blockposition2 = new BlockPos(k, k1, j1);

                    int j2;

                    for (l1 = -3; l1 <= 3; ++l1) {
                        for (j2 = -3; j2 <= 3; ++j2) {
                            if (Math.abs(l1) != 3 || Math.abs(j2) != 3) {
                                this.func_175924_b(world, blockposition2.func_177982_a(l1, 0, j2));
                            }
                        }
                    }

                    blockposition2 = blockposition2.func_177984_a();

                    for (l1 = -1; l1 <= 1; ++l1) {
                        for (j2 = -1; j2 <= 1; ++j2) {
                            this.func_175924_b(world, blockposition2.func_177982_a(l1, 0, j2));
                        }
                    }

                    this.func_175924_b(world, blockposition2.func_177965_g(2));
                    this.func_175924_b(world, blockposition2.func_177985_f(2));
                    this.func_175924_b(world, blockposition2.func_177970_e(2));
                    this.func_175924_b(world, blockposition2.func_177964_d(2));
                    k = blockposition.func_177958_n();
                    j1 = blockposition.func_177952_p();
                    EnumFacing enumdirection1 = EnumFacing.Plane.HORIZONTAL.func_179518_a(random);

                    if (enumdirection1 != enumdirection) {
                        l1 = i1 - random.nextInt(2) - 1;
                        j2 = 1 + random.nextInt(3);
                        k1 = 0;

                        int k2;

                        for (int l2 = l1; l2 < i && j2 > 0; --j2) {
                            if (l2 >= 1) {
                                k2 = blockposition.func_177956_o() + l2;
                                k += enumdirection1.func_82601_c();
                                j1 += enumdirection1.func_82599_e();
                                BlockPos blockposition3 = new BlockPos(k, k2, j1);
                                Material material1 = world.func_180495_p(blockposition3).func_185904_a();

                                if (material1 == Material.field_151579_a || material1 == Material.field_151584_j) {
                                    this.func_181642_b(world, blockposition3);
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
                                        this.func_175924_b(world, blockposition4.func_177982_a(k2, 0, i3));
                                    }
                                }
                            }

                            blockposition4 = blockposition4.func_177984_a();

                            for (k2 = -1; k2 <= 1; ++k2) {
                                for (i3 = -1; i3 <= 1; ++i3) {
                                    this.func_175924_b(world, blockposition4.func_177982_a(k2, 0, i3));
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

    private void func_181642_b(World world, BlockPos blockposition) {
        this.func_175903_a(world, blockposition, WorldGenSavannaTree.field_181643_a);
    }

    private void func_175924_b(World world, BlockPos blockposition) {
        Material material = world.func_180495_p(blockposition).func_185904_a();

        if (material == Material.field_151579_a || material == Material.field_151584_j) {
            this.func_175903_a(world, blockposition, WorldGenSavannaTree.field_181644_b);
        }

    }
}

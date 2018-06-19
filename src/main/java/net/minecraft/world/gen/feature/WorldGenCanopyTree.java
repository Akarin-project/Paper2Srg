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

    private static final IBlockState field_181640_a = Blocks.field_150363_s.func_176223_P().func_177226_a(BlockNewLog.field_176300_b, BlockPlanks.EnumType.DARK_OAK);
    private static final IBlockState field_181641_b = Blocks.field_150361_u.func_176223_P().func_177226_a(BlockNewLeaf.field_176240_P, BlockPlanks.EnumType.DARK_OAK).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));

    public WorldGenCanopyTree(boolean flag) {
        super(flag);
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + random.nextInt(2) + 6;
        int j = blockposition.func_177958_n();
        int k = blockposition.func_177956_o();
        int l = blockposition.func_177952_p();

        if (k >= 1 && k + i + 1 < 256) {
            BlockPos blockposition1 = blockposition.func_177977_b();
            Block block = world.func_180495_p(blockposition1).func_177230_c();

            if (block != Blocks.field_150349_c && block != Blocks.field_150346_d) {
                return false;
            } else if (!this.func_181638_a(world, blockposition, i)) {
                return false;
            } else {
                this.func_175921_a(world, blockposition1);
                this.func_175921_a(world, blockposition1.func_177974_f());
                this.func_175921_a(world, blockposition1.func_177968_d());
                this.func_175921_a(world, blockposition1.func_177968_d().func_177974_f());
                EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.func_179518_a(random);
                int i1 = i - random.nextInt(4);
                int j1 = 2 - random.nextInt(3);
                int k1 = j;
                int l1 = l;
                int i2 = k + i - 1;

                int j2;
                int k2;

                for (j2 = 0; j2 < i; ++j2) {
                    if (j2 >= i1 && j1 > 0) {
                        k1 += enumdirection.func_82601_c();
                        l1 += enumdirection.func_82599_e();
                        --j1;
                    }

                    k2 = k + j2;
                    BlockPos blockposition2 = new BlockPos(k1, k2, l1);
                    Material material = world.func_180495_p(blockposition2).func_185904_a();

                    if (material == Material.field_151579_a || material == Material.field_151584_j) {
                        this.func_181639_b(world, blockposition2);
                        this.func_181639_b(world, blockposition2.func_177974_f());
                        this.func_181639_b(world, blockposition2.func_177968_d());
                        this.func_181639_b(world, blockposition2.func_177974_f().func_177968_d());
                    }
                }

                for (j2 = -2; j2 <= 0; ++j2) {
                    for (k2 = -2; k2 <= 0; ++k2) {
                        byte b0 = -1;

                        this.func_150526_a(world, k1 + j2, i2 + b0, l1 + k2);
                        this.func_150526_a(world, 1 + k1 - j2, i2 + b0, l1 + k2);
                        this.func_150526_a(world, k1 + j2, i2 + b0, 1 + l1 - k2);
                        this.func_150526_a(world, 1 + k1 - j2, i2 + b0, 1 + l1 - k2);
                        if ((j2 > -2 || k2 > -1) && (j2 != -1 || k2 != -2)) {
                            byte b1 = 1;

                            this.func_150526_a(world, k1 + j2, i2 + b1, l1 + k2);
                            this.func_150526_a(world, 1 + k1 - j2, i2 + b1, l1 + k2);
                            this.func_150526_a(world, k1 + j2, i2 + b1, 1 + l1 - k2);
                            this.func_150526_a(world, 1 + k1 - j2, i2 + b1, 1 + l1 - k2);
                        }
                    }
                }

                if (random.nextBoolean()) {
                    this.func_150526_a(world, k1, i2 + 2, l1);
                    this.func_150526_a(world, k1 + 1, i2 + 2, l1);
                    this.func_150526_a(world, k1 + 1, i2 + 2, l1 + 1);
                    this.func_150526_a(world, k1, i2 + 2, l1 + 1);
                }

                for (j2 = -3; j2 <= 4; ++j2) {
                    for (k2 = -3; k2 <= 4; ++k2) {
                        if ((j2 != -3 || k2 != -3) && (j2 != -3 || k2 != 4) && (j2 != 4 || k2 != -3) && (j2 != 4 || k2 != 4) && (Math.abs(j2) < 3 || Math.abs(k2) < 3)) {
                            this.func_150526_a(world, k1 + j2, i2, l1 + k2);
                        }
                    }
                }

                for (j2 = -1; j2 <= 2; ++j2) {
                    for (k2 = -1; k2 <= 2; ++k2) {
                        if ((j2 < 0 || j2 > 1 || k2 < 0 || k2 > 1) && random.nextInt(3) <= 0) {
                            int l2 = random.nextInt(3) + 2;

                            int i3;

                            for (i3 = 0; i3 < l2; ++i3) {
                                this.func_181639_b(world, new BlockPos(j + j2, i2 - i3 - 1, l + k2));
                            }

                            int j3;

                            for (i3 = -1; i3 <= 1; ++i3) {
                                for (j3 = -1; j3 <= 1; ++j3) {
                                    this.func_150526_a(world, k1 + j2 + i3, i2, l1 + k2 + j3);
                                }
                            }

                            for (i3 = -2; i3 <= 2; ++i3) {
                                for (j3 = -2; j3 <= 2; ++j3) {
                                    if (Math.abs(i3) != 2 || Math.abs(j3) != 2) {
                                        this.func_150526_a(world, k1 + j2 + i3, i2 - 1, l1 + k2 + j3);
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

    private boolean func_181638_a(World world, BlockPos blockposition, int i) {
        int j = blockposition.func_177958_n();
        int k = blockposition.func_177956_o();
        int l = blockposition.func_177952_p();
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
                    if (!this.func_150523_a(world.func_180495_p(blockposition_mutableblockposition.func_181079_c(j + j1, k + i1, l + k1)).func_177230_c())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void func_181639_b(World world, BlockPos blockposition) {
        if (this.func_150523_a(world.func_180495_p(blockposition).func_177230_c())) {
            this.func_175903_a(world, blockposition, WorldGenCanopyTree.field_181640_a);
        }

    }

    private void func_150526_a(World world, int i, int j, int k) {
        BlockPos blockposition = new BlockPos(i, j, k);
        Material material = world.func_180495_p(blockposition).func_185904_a();

        if (material == Material.field_151579_a) {
            this.func_175903_a(world, blockposition, WorldGenCanopyTree.field_181641_b);
        }

    }
}

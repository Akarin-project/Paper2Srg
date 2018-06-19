package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree {

    private static final IBlockState field_181653_a = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.OAK);
    private static final IBlockState field_181654_b = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.OAK).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));
    private final int field_76533_a;
    private final boolean field_76531_b;
    private final IBlockState field_76532_c;
    private final IBlockState field_76530_d;

    public WorldGenTrees(boolean flag) {
        this(flag, 4, WorldGenTrees.field_181653_a, WorldGenTrees.field_181654_b, false);
    }

    public WorldGenTrees(boolean flag, int i, IBlockState iblockdata, IBlockState iblockdata1, boolean flag1) {
        super(flag);
        this.field_76533_a = i;
        this.field_76532_c = iblockdata;
        this.field_76530_d = iblockdata1;
        this.field_76531_b = flag1;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + this.field_76533_a;
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
                    boolean flag1 = true;
                    boolean flag2 = false;

                    int i1;
                    int j1;
                    int k1;
                    BlockPos blockposition1;

                    for (j = blockposition.func_177956_o() - 3 + i; j <= blockposition.func_177956_o() + i; ++j) {
                        k = j - (blockposition.func_177956_o() + i);
                        i1 = 1 - k / 2;

                        for (int l1 = blockposition.func_177958_n() - i1; l1 <= blockposition.func_177958_n() + i1; ++l1) {
                            j1 = l1 - blockposition.func_177958_n();

                            for (k1 = blockposition.func_177952_p() - i1; k1 <= blockposition.func_177952_p() + i1; ++k1) {
                                int i2 = k1 - blockposition.func_177952_p();

                                if (Math.abs(j1) != i1 || Math.abs(i2) != i1 || random.nextInt(2) != 0 && k != 0) {
                                    blockposition1 = new BlockPos(l1, j, k1);
                                    Material material = world.func_180495_p(blockposition1).func_185904_a();

                                    if (material == Material.field_151579_a || material == Material.field_151584_j || material == Material.field_151582_l) {
                                        this.func_175903_a(world, blockposition1, this.field_76530_d);
                                    }
                                }
                            }
                        }
                    }

                    for (j = 0; j < i; ++j) {
                        Material material1 = world.func_180495_p(blockposition.func_177981_b(j)).func_185904_a();

                        if (material1 == Material.field_151579_a || material1 == Material.field_151584_j || material1 == Material.field_151582_l) {
                            this.func_175903_a(world, blockposition.func_177981_b(j), this.field_76532_c);
                            if (this.field_76531_b && j > 0) {
                                if (random.nextInt(3) > 0 && world.func_175623_d(blockposition.func_177982_a(-1, j, 0))) {
                                    this.func_181651_a(world, blockposition.func_177982_a(-1, j, 0), BlockVine.field_176278_M);
                                }

                                if (random.nextInt(3) > 0 && world.func_175623_d(blockposition.func_177982_a(1, j, 0))) {
                                    this.func_181651_a(world, blockposition.func_177982_a(1, j, 0), BlockVine.field_176280_O);
                                }

                                if (random.nextInt(3) > 0 && world.func_175623_d(blockposition.func_177982_a(0, j, -1))) {
                                    this.func_181651_a(world, blockposition.func_177982_a(0, j, -1), BlockVine.field_176279_N);
                                }

                                if (random.nextInt(3) > 0 && world.func_175623_d(blockposition.func_177982_a(0, j, 1))) {
                                    this.func_181651_a(world, blockposition.func_177982_a(0, j, 1), BlockVine.field_176273_b);
                                }
                            }
                        }
                    }

                    if (this.field_76531_b) {
                        for (j = blockposition.func_177956_o() - 3 + i; j <= blockposition.func_177956_o() + i; ++j) {
                            k = j - (blockposition.func_177956_o() + i);
                            i1 = 2 - k / 2;
                            BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();

                            for (j1 = blockposition.func_177958_n() - i1; j1 <= blockposition.func_177958_n() + i1; ++j1) {
                                for (k1 = blockposition.func_177952_p() - i1; k1 <= blockposition.func_177952_p() + i1; ++k1) {
                                    blockposition_mutableblockposition1.func_181079_c(j1, j, k1);
                                    if (world.func_180495_p(blockposition_mutableblockposition1).func_185904_a() == Material.field_151584_j) {
                                        BlockPos blockposition2 = blockposition_mutableblockposition1.func_177976_e();

                                        blockposition1 = blockposition_mutableblockposition1.func_177974_f();
                                        BlockPos blockposition3 = blockposition_mutableblockposition1.func_177978_c();
                                        BlockPos blockposition4 = blockposition_mutableblockposition1.func_177968_d();

                                        if (random.nextInt(4) == 0 && world.func_180495_p(blockposition2).func_185904_a() == Material.field_151579_a) {
                                            this.func_181650_b(world, blockposition2, BlockVine.field_176278_M);
                                        }

                                        if (random.nextInt(4) == 0 && world.func_180495_p(blockposition1).func_185904_a() == Material.field_151579_a) {
                                            this.func_181650_b(world, blockposition1, BlockVine.field_176280_O);
                                        }

                                        if (random.nextInt(4) == 0 && world.func_180495_p(blockposition3).func_185904_a() == Material.field_151579_a) {
                                            this.func_181650_b(world, blockposition3, BlockVine.field_176279_N);
                                        }

                                        if (random.nextInt(4) == 0 && world.func_180495_p(blockposition4).func_185904_a() == Material.field_151579_a) {
                                            this.func_181650_b(world, blockposition4, BlockVine.field_176273_b);
                                        }
                                    }
                                }
                            }
                        }

                        if (random.nextInt(5) == 0 && i > 5) {
                            for (j = 0; j < 2; ++j) {
                                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator.hasNext()) {
                                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                                    if (random.nextInt(4 - j) == 0) {
                                        EnumFacing enumdirection1 = enumdirection.func_176734_d();

                                        this.func_181652_a(world, random.nextInt(3), blockposition.func_177982_a(enumdirection1.func_82601_c(), i - 5 + j, enumdirection1.func_82599_e()), enumdirection);
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

    private void func_181652_a(World world, int i, BlockPos blockposition, EnumFacing enumdirection) {
        this.func_175903_a(world, blockposition, Blocks.field_150375_by.func_176223_P().func_177226_a(BlockCocoa.field_176501_a, Integer.valueOf(i)).func_177226_a(BlockCocoa.field_185512_D, enumdirection));
    }

    private void func_181651_a(World world, BlockPos blockposition, PropertyBool blockstateboolean) {
        this.func_175903_a(world, blockposition, Blocks.field_150395_bd.func_176223_P().func_177226_a(blockstateboolean, Boolean.valueOf(true)));
    }

    private void func_181650_b(World world, BlockPos blockposition, PropertyBool blockstateboolean) {
        this.func_181651_a(world, blockposition, blockstateboolean);
        int i = 4;

        for (blockposition = blockposition.func_177977_b(); world.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a && i > 0; --i) {
            this.func_181651_a(world, blockposition, blockstateboolean);
            blockposition = blockposition.func_177977_b();
        }

    }
}

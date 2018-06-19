package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenIceSpike extends WorldGenerator {

    public WorldGenIceSpike() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        while (world.func_175623_d(blockposition) && blockposition.func_177956_o() > 2) {
            blockposition = blockposition.func_177977_b();
        }

        if (world.func_180495_p(blockposition).func_177230_c() != Blocks.field_150433_aE) {
            return false;
        } else {
            blockposition = blockposition.func_177981_b(random.nextInt(4));
            int i = random.nextInt(4) + 7;
            int j = i / 4 + random.nextInt(2);

            if (j > 1 && random.nextInt(60) == 0) {
                blockposition = blockposition.func_177981_b(10 + random.nextInt(30));
            }

            int k;
            int l;

            for (k = 0; k < i; ++k) {
                float f = (1.0F - (float) k / (float) i) * (float) j;

                l = MathHelper.func_76123_f(f);

                for (int i1 = -l; i1 <= l; ++i1) {
                    float f1 = (float) MathHelper.func_76130_a(i1) - 0.25F;

                    for (int j1 = -l; j1 <= l; ++j1) {
                        float f2 = (float) MathHelper.func_76130_a(j1) - 0.25F;

                        if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= f * f) && (i1 != -l && i1 != l && j1 != -l && j1 != l || random.nextFloat() <= 0.75F)) {
                            IBlockState iblockdata = world.func_180495_p(blockposition.func_177982_a(i1, k, j1));
                            Block block = iblockdata.func_177230_c();

                            if (iblockdata.func_185904_a() == Material.field_151579_a || block == Blocks.field_150346_d || block == Blocks.field_150433_aE || block == Blocks.field_150432_aD) {
                                this.func_175903_a(world, blockposition.func_177982_a(i1, k, j1), Blocks.field_150403_cj.func_176223_P());
                            }

                            if (k != 0 && l > 1) {
                                iblockdata = world.func_180495_p(blockposition.func_177982_a(i1, -k, j1));
                                block = iblockdata.func_177230_c();
                                if (iblockdata.func_185904_a() == Material.field_151579_a || block == Blocks.field_150346_d || block == Blocks.field_150433_aE || block == Blocks.field_150432_aD) {
                                    this.func_175903_a(world, blockposition.func_177982_a(i1, -k, j1), Blocks.field_150403_cj.func_176223_P());
                                }
                            }
                        }
                    }
                }
            }

            k = j - 1;
            if (k < 0) {
                k = 0;
            } else if (k > 1) {
                k = 1;
            }

            for (int k1 = -k; k1 <= k; ++k1) {
                l = -k;

                while (l <= k) {
                    BlockPos blockposition1 = blockposition.func_177982_a(k1, -1, l);
                    int l1 = 50;

                    if (Math.abs(k1) == 1 && Math.abs(l) == 1) {
                        l1 = random.nextInt(5);
                    }

                    while (true) {
                        if (blockposition1.func_177956_o() > 50) {
                            IBlockState iblockdata1 = world.func_180495_p(blockposition1);
                            Block block1 = iblockdata1.func_177230_c();

                            if (iblockdata1.func_185904_a() == Material.field_151579_a || block1 == Blocks.field_150346_d || block1 == Blocks.field_150433_aE || block1 == Blocks.field_150432_aD || block1 == Blocks.field_150403_cj) {
                                this.func_175903_a(world, blockposition1, Blocks.field_150403_cj.func_176223_P());
                                blockposition1 = blockposition1.func_177977_b();
                                --l1;
                                if (l1 <= 0) {
                                    blockposition1 = blockposition1.func_177979_c(random.nextInt(5) + 1);
                                    l1 = random.nextInt(5);
                                }
                                continue;
                            }
                        }

                        ++l;
                        break;
                    }
                }
            }

            return true;
        }
    }
}

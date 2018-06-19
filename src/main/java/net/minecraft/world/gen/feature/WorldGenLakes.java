package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGenLakes extends WorldGenerator {

    private final Block field_150556_a;

    public WorldGenLakes(Block block) {
        this.field_150556_a = block;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (blockposition = blockposition.func_177982_a(-8, 0, -8); blockposition.func_177956_o() > 5 && world.func_175623_d(blockposition); blockposition = blockposition.func_177977_b()) {
            ;
        }

        if (blockposition.func_177956_o() <= 4) {
            return false;
        } else {
            blockposition = blockposition.func_177979_c(4);
            boolean[] aboolean = new boolean[2048];
            int i = random.nextInt(4) + 4;

            int j;

            for (j = 0; j < i; ++j) {
                double d0 = random.nextDouble() * 6.0D + 3.0D;
                double d1 = random.nextDouble() * 4.0D + 2.0D;
                double d2 = random.nextDouble() * 6.0D + 3.0D;
                double d3 = random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for (int k = 1; k < 15; ++k) {
                    for (int l = 1; l < 15; ++l) {
                        for (int i1 = 1; i1 < 7; ++i1) {
                            double d6 = ((double) k - d3) / (d0 / 2.0D);
                            double d7 = ((double) i1 - d4) / (d1 / 2.0D);
                            double d8 = ((double) l - d5) / (d2 / 2.0D);
                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                            if (d9 < 1.0D) {
                                aboolean[(k * 16 + l) * 8 + i1] = true;
                            }
                        }
                    }
                }
            }

            int j1;
            int k1;
            boolean flag;

            for (j = 0; j < 16; ++j) {
                for (k1 = 0; k1 < 16; ++k1) {
                    for (j1 = 0; j1 < 8; ++j1) {
                        flag = !aboolean[(j * 16 + k1) * 8 + j1] && (j < 15 && aboolean[((j + 1) * 16 + k1) * 8 + j1] || j > 0 && aboolean[((j - 1) * 16 + k1) * 8 + j1] || k1 < 15 && aboolean[(j * 16 + k1 + 1) * 8 + j1] || k1 > 0 && aboolean[(j * 16 + (k1 - 1)) * 8 + j1] || j1 < 7 && aboolean[(j * 16 + k1) * 8 + j1 + 1] || j1 > 0 && aboolean[(j * 16 + k1) * 8 + (j1 - 1)]);
                        if (flag) {
                            Material material = world.func_180495_p(blockposition.func_177982_a(j, j1, k1)).func_185904_a();

                            if (j1 >= 4 && material.func_76224_d()) {
                                return false;
                            }

                            if (j1 < 4 && !material.func_76220_a() && world.func_180495_p(blockposition.func_177982_a(j, j1, k1)).func_177230_c() != this.field_150556_a) {
                                return false;
                            }
                        }
                    }
                }
            }

            for (j = 0; j < 16; ++j) {
                for (k1 = 0; k1 < 16; ++k1) {
                    for (j1 = 0; j1 < 8; ++j1) {
                        if (aboolean[(j * 16 + k1) * 8 + j1]) {
                            world.func_180501_a(blockposition.func_177982_a(j, j1, k1), j1 >= 4 ? Blocks.field_150350_a.func_176223_P() : this.field_150556_a.func_176223_P(), 2);
                        }
                    }
                }
            }

            for (j = 0; j < 16; ++j) {
                for (k1 = 0; k1 < 16; ++k1) {
                    for (j1 = 4; j1 < 8; ++j1) {
                        if (aboolean[(j * 16 + k1) * 8 + j1]) {
                            BlockPos blockposition1 = blockposition.func_177982_a(j, j1 - 1, k1);

                            if (world.func_180495_p(blockposition1).func_177230_c() == Blocks.field_150346_d && world.func_175642_b(EnumSkyBlock.SKY, blockposition.func_177982_a(j, j1, k1)) > 0) {
                                Biome biomebase = world.func_180494_b(blockposition1);

                                if (biomebase.field_76752_A.func_177230_c() == Blocks.field_150391_bh) {
                                    world.func_180501_a(blockposition1, Blocks.field_150391_bh.func_176223_P(), 2);
                                } else {
                                    world.func_180501_a(blockposition1, Blocks.field_150349_c.func_176223_P(), 2);
                                }
                            }
                        }
                    }
                }
            }

            if (this.field_150556_a.func_176223_P().func_185904_a() == Material.field_151587_i) {
                for (j = 0; j < 16; ++j) {
                    for (k1 = 0; k1 < 16; ++k1) {
                        for (j1 = 0; j1 < 8; ++j1) {
                            flag = !aboolean[(j * 16 + k1) * 8 + j1] && (j < 15 && aboolean[((j + 1) * 16 + k1) * 8 + j1] || j > 0 && aboolean[((j - 1) * 16 + k1) * 8 + j1] || k1 < 15 && aboolean[(j * 16 + k1 + 1) * 8 + j1] || k1 > 0 && aboolean[(j * 16 + (k1 - 1)) * 8 + j1] || j1 < 7 && aboolean[(j * 16 + k1) * 8 + j1 + 1] || j1 > 0 && aboolean[(j * 16 + k1) * 8 + (j1 - 1)]);
                            if (flag && (j1 < 4 || random.nextInt(2) != 0) && world.func_180495_p(blockposition.func_177982_a(j, j1, k1)).func_185904_a().func_76220_a()) {
                                world.func_180501_a(blockposition.func_177982_a(j, j1, k1), Blocks.field_150348_b.func_176223_P(), 2);
                            }
                        }
                    }
                }
            }

            if (this.field_150556_a.func_176223_P().func_185904_a() == Material.field_151586_h) {
                for (j = 0; j < 16; ++j) {
                    for (k1 = 0; k1 < 16; ++k1) {
                        boolean flag1 = true;

                        if (world.func_175675_v(blockposition.func_177982_a(j, 4, k1))) {
                            world.func_180501_a(blockposition.func_177982_a(j, 4, k1), Blocks.field_150432_aD.func_176223_P(), 2);
                        }
                    }
                }
            }

            return true;
        }
    }
}

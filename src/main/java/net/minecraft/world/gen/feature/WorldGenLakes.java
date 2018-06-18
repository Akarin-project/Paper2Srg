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

    private final Block block;

    public WorldGenLakes(Block block) {
        this.block = block;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (blockposition = blockposition.add(-8, 0, -8); blockposition.getY() > 5 && world.isAirBlock(blockposition); blockposition = blockposition.down()) {
            ;
        }

        if (blockposition.getY() <= 4) {
            return false;
        } else {
            blockposition = blockposition.down(4);
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
                            Material material = world.getBlockState(blockposition.add(j, j1, k1)).getMaterial();

                            if (j1 >= 4 && material.isLiquid()) {
                                return false;
                            }

                            if (j1 < 4 && !material.isSolid() && world.getBlockState(blockposition.add(j, j1, k1)).getBlock() != this.block) {
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
                            world.setBlockState(blockposition.add(j, j1, k1), j1 >= 4 ? Blocks.AIR.getDefaultState() : this.block.getDefaultState(), 2);
                        }
                    }
                }
            }

            for (j = 0; j < 16; ++j) {
                for (k1 = 0; k1 < 16; ++k1) {
                    for (j1 = 4; j1 < 8; ++j1) {
                        if (aboolean[(j * 16 + k1) * 8 + j1]) {
                            BlockPos blockposition1 = blockposition.add(j, j1 - 1, k1);

                            if (world.getBlockState(blockposition1).getBlock() == Blocks.DIRT && world.getLightFor(EnumSkyBlock.SKY, blockposition.add(j, j1, k1)) > 0) {
                                Biome biomebase = world.getBiome(blockposition1);

                                if (biomebase.topBlock.getBlock() == Blocks.MYCELIUM) {
                                    world.setBlockState(blockposition1, Blocks.MYCELIUM.getDefaultState(), 2);
                                } else {
                                    world.setBlockState(blockposition1, Blocks.GRASS.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }

            if (this.block.getDefaultState().getMaterial() == Material.LAVA) {
                for (j = 0; j < 16; ++j) {
                    for (k1 = 0; k1 < 16; ++k1) {
                        for (j1 = 0; j1 < 8; ++j1) {
                            flag = !aboolean[(j * 16 + k1) * 8 + j1] && (j < 15 && aboolean[((j + 1) * 16 + k1) * 8 + j1] || j > 0 && aboolean[((j - 1) * 16 + k1) * 8 + j1] || k1 < 15 && aboolean[(j * 16 + k1 + 1) * 8 + j1] || k1 > 0 && aboolean[(j * 16 + (k1 - 1)) * 8 + j1] || j1 < 7 && aboolean[(j * 16 + k1) * 8 + j1 + 1] || j1 > 0 && aboolean[(j * 16 + k1) * 8 + (j1 - 1)]);
                            if (flag && (j1 < 4 || random.nextInt(2) != 0) && world.getBlockState(blockposition.add(j, j1, k1)).getMaterial().isSolid()) {
                                world.setBlockState(blockposition.add(j, j1, k1), Blocks.STONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            if (this.block.getDefaultState().getMaterial() == Material.WATER) {
                for (j = 0; j < 16; ++j) {
                    for (k1 = 0; k1 < 16; ++k1) {
                        boolean flag1 = true;

                        if (world.canBlockFreezeWater(blockposition.add(j, 4, k1))) {
                            world.setBlockState(blockposition.add(j, 4, k1), Blocks.ICE.getDefaultState(), 2);
                        }
                    }
                }
            }

            return true;
        }
    }
}

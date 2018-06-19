package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenRavine extends MapGenBase {

    protected static final IBlockState field_186135_a = Blocks.field_150356_k.func_176223_P();
    protected static final IBlockState field_186136_b = Blocks.field_150350_a.func_176223_P();
    private final float[] field_75046_d = new float[1024];

    public MapGenRavine() {}

    protected void func_180707_a(long i, int j, int k, ChunkPrimer chunksnapshot, double d0, double d1, double d2, float f, float f1, float f2, int l, int i1, double d3) {
        Random random = new Random(i);
        double d4 = (double) (j * 16 + 8);
        double d5 = (double) (k * 16 + 8);
        float f3 = 0.0F;
        float f4 = 0.0F;

        if (i1 <= 0) {
            int j1 = this.field_75040_a * 16 - 16;

            i1 = j1 - random.nextInt(j1 / 4);
        }

        boolean flag = false;

        if (l == -1) {
            l = i1 / 2;
            flag = true;
        }

        float f5 = 1.0F;

        for (int k1 = 0; k1 < 256; ++k1) {
            if (k1 == 0 || random.nextInt(3) == 0) {
                f5 = 1.0F + random.nextFloat() * random.nextFloat();
            }

            this.field_75046_d[k1] = f5 * f5;
        }

        for (; l < i1; ++l) {
            double d6 = 1.5D + (double) (MathHelper.func_76126_a((float) l * 3.1415927F / (float) i1) * f);
            double d7 = d6 * d3;

            d6 *= (double) random.nextFloat() * 0.25D + 0.75D;
            d7 *= (double) random.nextFloat() * 0.25D + 0.75D;
            float f6 = MathHelper.func_76134_b(f2);
            float f7 = MathHelper.func_76126_a(f2);

            d0 += (double) (MathHelper.func_76134_b(f1) * f6);
            d1 += (double) f7;
            d2 += (double) (MathHelper.func_76126_a(f1) * f6);
            f2 *= 0.7F;
            f2 += f4 * 0.05F;
            f1 += f3 * 0.05F;
            f4 *= 0.8F;
            f3 *= 0.5F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if (flag || random.nextInt(4) != 0) {
                double d8 = d0 - d4;
                double d9 = d2 - d5;
                double d10 = (double) (i1 - l);
                double d11 = (double) (f + 2.0F + 16.0F);

                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
                    return;
                }

                if (d0 >= d4 - 16.0D - d6 * 2.0D && d2 >= d5 - 16.0D - d6 * 2.0D && d0 <= d4 + 16.0D + d6 * 2.0D && d2 <= d5 + 16.0D + d6 * 2.0D) {
                    int l1 = MathHelper.func_76128_c(d0 - d6) - j * 16 - 1;
                    int i2 = MathHelper.func_76128_c(d0 + d6) - j * 16 + 1;
                    int j2 = MathHelper.func_76128_c(d1 - d7) - 1;
                    int k2 = MathHelper.func_76128_c(d1 + d7) + 1;
                    int l2 = MathHelper.func_76128_c(d2 - d6) - k * 16 - 1;
                    int i3 = MathHelper.func_76128_c(d2 + d6) - k * 16 + 1;

                    if (l1 < 0) {
                        l1 = 0;
                    }

                    if (i2 > 16) {
                        i2 = 16;
                    }

                    if (j2 < 1) {
                        j2 = 1;
                    }

                    if (k2 > 248) {
                        k2 = 248;
                    }

                    if (l2 < 0) {
                        l2 = 0;
                    }

                    if (i3 > 16) {
                        i3 = 16;
                    }

                    boolean flag1 = false;

                    int j3;

                    for (int k3 = l1; !flag1 && k3 < i2; ++k3) {
                        for (j3 = l2; !flag1 && j3 < i3; ++j3) {
                            for (int l3 = k2 + 1; !flag1 && l3 >= j2 - 1; --l3) {
                                if (l3 >= 0 && l3 < 256) {
                                    IBlockState iblockdata = chunksnapshot.func_177856_a(k3, l3, j3);

                                    if (iblockdata.func_177230_c() == Blocks.field_150358_i || iblockdata.func_177230_c() == Blocks.field_150355_j) {
                                        flag1 = true;
                                    }

                                    if (l3 != j2 - 1 && k3 != l1 && k3 != i2 - 1 && j3 != l2 && j3 != i3 - 1) {
                                        l3 = j2;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag1) {
                        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                        for (j3 = l1; j3 < i2; ++j3) {
                            double d12 = ((double) (j3 + j * 16) + 0.5D - d0) / d6;

                            for (int i4 = l2; i4 < i3; ++i4) {
                                double d13 = ((double) (i4 + k * 16) + 0.5D - d2) / d6;
                                boolean flag2 = false;

                                if (d12 * d12 + d13 * d13 < 1.0D) {
                                    for (int j4 = k2; j4 > j2; --j4) {
                                        double d14 = ((double) (j4 - 1) + 0.5D - d1) / d7;

                                        if ((d12 * d12 + d13 * d13) * (double) this.field_75046_d[j4 - 1] + d14 * d14 / 6.0D < 1.0D) {
                                            IBlockState iblockdata1 = chunksnapshot.func_177856_a(j3, j4, i4);

                                            if (iblockdata1.func_177230_c() == Blocks.field_150349_c) {
                                                flag2 = true;
                                            }

                                            if (iblockdata1.func_177230_c() == Blocks.field_150348_b || iblockdata1.func_177230_c() == Blocks.field_150346_d || iblockdata1.func_177230_c() == Blocks.field_150349_c) {
                                                if (j4 - 1 < 10) {
                                                    chunksnapshot.func_177855_a(j3, j4, i4, MapGenRavine.field_186135_a);
                                                } else {
                                                    chunksnapshot.func_177855_a(j3, j4, i4, MapGenRavine.field_186136_b);
                                                    if (flag2 && chunksnapshot.func_177856_a(j3, j4 - 1, i4).func_177230_c() == Blocks.field_150346_d) {
                                                        blockposition_mutableblockposition.func_181079_c(j3 + j * 16, 0, i4 + k * 16);
                                                        chunksnapshot.func_177855_a(j3, j4 - 1, i4, this.field_75039_c.func_180494_b(blockposition_mutableblockposition).field_76752_A);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (flag) {
                            break;
                        }
                    }
                }
            }
        }

    }

    protected void func_180701_a(World world, int i, int j, int k, int l, ChunkPrimer chunksnapshot) {
        if (this.field_75038_b.nextInt(50) == 0) {
            double d0 = (double) (i * 16 + this.field_75038_b.nextInt(16));
            double d1 = (double) (this.field_75038_b.nextInt(this.field_75038_b.nextInt(40) + 8) + 20);
            double d2 = (double) (j * 16 + this.field_75038_b.nextInt(16));
            boolean flag = true;

            for (int i1 = 0; i1 < 1; ++i1) {
                float f = this.field_75038_b.nextFloat() * 6.2831855F;
                float f1 = (this.field_75038_b.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = (this.field_75038_b.nextFloat() * 2.0F + this.field_75038_b.nextFloat()) * 2.0F;

                this.func_180707_a(this.field_75038_b.nextLong(), k, l, chunksnapshot, d0, d1, d2, f2, f, f1, 0, 0, 3.0D);
            }

        }
    }
}

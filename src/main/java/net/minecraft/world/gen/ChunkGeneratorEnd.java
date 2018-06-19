package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockChorusFlower;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import net.minecraft.world.gen.structure.MapGenEndCity;

public class ChunkGeneratorEnd implements IChunkGenerator {

    private final Random field_73220_k;
    protected static final IBlockState field_185964_a = Blocks.field_150377_bs.func_176223_P();
    protected static final IBlockState field_185965_b = Blocks.field_150350_a.func_176223_P();
    private final NoiseGeneratorOctaves field_185969_i;
    private final NoiseGeneratorOctaves field_185970_j;
    private final NoiseGeneratorOctaves field_185971_k;
    public NoiseGeneratorOctaves field_73214_a;
    public NoiseGeneratorOctaves field_73212_b;
    private final World field_73230_p;
    private final boolean field_73229_q;
    private final BlockPos field_191061_n;
    private final MapGenEndCity field_185972_n = new MapGenEndCity(this);
    private final NoiseGeneratorSimplex field_185973_o;
    private double[] field_185974_p;
    private Biome[] field_73231_z;
    double[] field_185966_e;
    double[] field_185967_f;
    double[] field_185968_g;
    private final WorldGenEndIsland field_185975_r = new WorldGenEndIsland();

    public ChunkGeneratorEnd(World world, boolean flag, long i, BlockPos blockposition) {
        this.field_73230_p = world;
        this.field_73229_q = flag;
        this.field_191061_n = blockposition;
        this.field_73220_k = new Random(i);
        this.field_185969_i = new NoiseGeneratorOctaves(this.field_73220_k, 16);
        this.field_185970_j = new NoiseGeneratorOctaves(this.field_73220_k, 16);
        this.field_185971_k = new NoiseGeneratorOctaves(this.field_73220_k, 8);
        this.field_73214_a = new NoiseGeneratorOctaves(this.field_73220_k, 10);
        this.field_73212_b = new NoiseGeneratorOctaves(this.field_73220_k, 16);
        this.field_185973_o = new NoiseGeneratorSimplex(this.field_73220_k);
    }

    public void func_180518_a(int i, int j, ChunkPrimer chunksnapshot) {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;

        this.field_185974_p = this.func_185963_a(this.field_185974_p, i * 2, 0, j * 2, 3, 33, 3);

        for (int k = 0; k < 2; ++k) {
            for (int l = 0; l < 2; ++l) {
                for (int i1 = 0; i1 < 32; ++i1) {
                    double d0 = 0.25D;
                    double d1 = this.field_185974_p[((k + 0) * 3 + l + 0) * 33 + i1 + 0];
                    double d2 = this.field_185974_p[((k + 0) * 3 + l + 1) * 33 + i1 + 0];
                    double d3 = this.field_185974_p[((k + 1) * 3 + l + 0) * 33 + i1 + 0];
                    double d4 = this.field_185974_p[((k + 1) * 3 + l + 1) * 33 + i1 + 0];
                    double d5 = (this.field_185974_p[((k + 0) * 3 + l + 0) * 33 + i1 + 1] - d1) * 0.25D;
                    double d6 = (this.field_185974_p[((k + 0) * 3 + l + 1) * 33 + i1 + 1] - d2) * 0.25D;
                    double d7 = (this.field_185974_p[((k + 1) * 3 + l + 0) * 33 + i1 + 1] - d3) * 0.25D;
                    double d8 = (this.field_185974_p[((k + 1) * 3 + l + 1) * 33 + i1 + 1] - d4) * 0.25D;

                    for (int j1 = 0; j1 < 4; ++j1) {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.125D;
                        double d13 = (d4 - d2) * 0.125D;

                        for (int k1 = 0; k1 < 8; ++k1) {
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * 0.125D;

                            for (int l1 = 0; l1 < 8; ++l1) {
                                IBlockState iblockdata = ChunkGeneratorEnd.field_185965_b;

                                if (d15 > 0.0D) {
                                    iblockdata = ChunkGeneratorEnd.field_185964_a;
                                }

                                int i2 = k1 + k * 8;
                                int j2 = j1 + i1 * 4;
                                int k2 = l1 + l * 8;

                                chunksnapshot.func_177855_a(i2, j2, k2, iblockdata);
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }

    }

    public void func_185962_a(ChunkPrimer chunksnapshot) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                boolean flag = true;
                int k = -1;
                IBlockState iblockdata = ChunkGeneratorEnd.field_185964_a;
                IBlockState iblockdata1 = ChunkGeneratorEnd.field_185964_a;

                for (int l = 127; l >= 0; --l) {
                    IBlockState iblockdata2 = chunksnapshot.func_177856_a(i, l, j);

                    if (iblockdata2.func_185904_a() == Material.field_151579_a) {
                        k = -1;
                    } else if (iblockdata2.func_177230_c() == Blocks.field_150348_b) {
                        if (k == -1) {
                            k = 1;
                            if (l >= 0) {
                                chunksnapshot.func_177855_a(i, l, j, iblockdata);
                            } else {
                                chunksnapshot.func_177855_a(i, l, j, iblockdata1);
                            }
                        } else if (k > 0) {
                            --k;
                            chunksnapshot.func_177855_a(i, l, j, iblockdata1);
                        }
                    }
                }
            }
        }

    }

    public Chunk func_185932_a(int i, int j) {
        this.field_73220_k.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        this.field_73231_z = this.field_73230_p.func_72959_q().func_76933_b(this.field_73231_z, i * 16, j * 16, 16, 16);
        this.func_180518_a(i, j, chunksnapshot);
        this.func_185962_a(chunksnapshot);
        if (this.field_73229_q) {
            this.field_185972_n.func_186125_a(this.field_73230_p, i, j, chunksnapshot);
        }

        Chunk chunk = new Chunk(this.field_73230_p, chunksnapshot, i, j);
        byte[] abyte = chunk.func_76605_m();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.func_185362_a(this.field_73231_z[k]);
        }

        chunk.func_76603_b();
        return chunk;
    }

    private float func_185960_a(int i, int j, int k, int l) {
        float f = (float) (i * 2 + k);
        float f1 = (float) (j * 2 + l);
        float f2 = 100.0F - MathHelper.func_76129_c(f * f + f1 * f1) * 8.0F;

        if (f2 > 80.0F) {
            f2 = 80.0F;
        }

        if (f2 < -100.0F) {
            f2 = -100.0F;
        }

        for (int i1 = -12; i1 <= 12; ++i1) {
            for (int j1 = -12; j1 <= 12; ++j1) {
                long k1 = (long) (i + i1);
                long l1 = (long) (j + j1);

                if (k1 * k1 + l1 * l1 > 4096L && this.field_185973_o.func_151605_a((double) k1, (double) l1) < -0.8999999761581421D) {
                    float f3 = (MathHelper.func_76135_e((float) k1) * 3439.0F + MathHelper.func_76135_e((float) l1) * 147.0F) % 13.0F + 9.0F;

                    f = (float) (k - i1 * 2);
                    f1 = (float) (l - j1 * 2);
                    float f4 = 100.0F - MathHelper.func_76129_c(f * f + f1 * f1) * f3;

                    if (f4 > 80.0F) {
                        f4 = 80.0F;
                    }

                    if (f4 < -100.0F) {
                        f4 = -100.0F;
                    }

                    if (f4 > f2) {
                        f2 = f4;
                    }
                }
            }
        }

        return f2;
    }

    public boolean func_185961_c(int i, int j) {
        return (long) i * (long) i + (long) j * (long) j > 4096L && this.func_185960_a(i, j, 1, 1) >= 0.0F;
    }

    private double[] func_185963_a(double[] adouble, int i, int j, int k, int l, int i1, int j1) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;

        d0 *= 2.0D;
        this.field_185966_e = this.field_185971_k.func_76304_a(this.field_185966_e, i, j, k, l, i1, j1, d0 / 80.0D, 4.277575000000001D, d0 / 80.0D);
        this.field_185967_f = this.field_185969_i.func_76304_a(this.field_185967_f, i, j, k, l, i1, j1, d0, 684.412D, d0);
        this.field_185968_g = this.field_185970_j.func_76304_a(this.field_185968_g, i, j, k, l, i1, j1, d0, 684.412D, d0);
        int k1 = i / 2;
        int l1 = k / 2;
        int i2 = 0;

        for (int j2 = 0; j2 < l; ++j2) {
            for (int k2 = 0; k2 < j1; ++k2) {
                float f = this.func_185960_a(k1, l1, j2, k2);

                for (int l2 = 0; l2 < i1; ++l2) {
                    double d2 = this.field_185967_f[i2] / 512.0D;
                    double d3 = this.field_185968_g[i2] / 512.0D;
                    double d4 = (this.field_185966_e[i2] / 10.0D + 1.0D) / 2.0D;
                    double d5;

                    if (d4 < 0.0D) {
                        d5 = d2;
                    } else if (d4 > 1.0D) {
                        d5 = d3;
                    } else {
                        d5 = d2 + (d3 - d2) * d4;
                    }

                    d5 -= 8.0D;
                    d5 += (double) f;
                    byte b0 = 2;
                    double d6;

                    if (l2 > i1 / 2 - b0) {
                        d6 = (double) ((float) (l2 - (i1 / 2 - b0)) / 64.0F);
                        d6 = MathHelper.func_151237_a(d6, 0.0D, 1.0D);
                        d5 = d5 * (1.0D - d6) + -3000.0D * d6;
                    }

                    b0 = 8;
                    if (l2 < b0) {
                        d6 = (double) ((float) (b0 - l2) / ((float) b0 - 1.0F));
                        d5 = d5 * (1.0D - d6) + -30.0D * d6;
                    }

                    adouble[i2] = d5;
                    ++i2;
                }
            }
        }

        return adouble;
    }

    public void func_185931_b(int i, int j) {
        BlockFalling.field_149832_M = true;
        BlockPos blockposition = new BlockPos(i * 16, 0, j * 16);

        if (this.field_73229_q) {
            this.field_185972_n.func_175794_a(this.field_73230_p, this.field_73220_k, new ChunkPos(i, j));
        }

        this.field_73230_p.func_180494_b(blockposition.func_177982_a(16, 0, 16)).func_180624_a(this.field_73230_p, this.field_73230_p.field_73012_v, blockposition);
        long k = (long) i * (long) i + (long) j * (long) j;

        if (k > 4096L) {
            float f = this.func_185960_a(i, j, 1, 1);

            if (f < -20.0F && this.field_73220_k.nextInt(14) == 0) {
                this.field_185975_r.func_180709_b(this.field_73230_p, this.field_73220_k, blockposition.func_177982_a(this.field_73220_k.nextInt(16) + 8, 55 + this.field_73220_k.nextInt(16), this.field_73220_k.nextInt(16) + 8));
                if (this.field_73220_k.nextInt(4) == 0) {
                    this.field_185975_r.func_180709_b(this.field_73230_p, this.field_73220_k, blockposition.func_177982_a(this.field_73220_k.nextInt(16) + 8, 55 + this.field_73220_k.nextInt(16), this.field_73220_k.nextInt(16) + 8));
                }
            }

            if (this.func_185960_a(i, j, 1, 1) > 40.0F) {
                int l = this.field_73220_k.nextInt(5);

                int i1;
                int j1;
                int k1;
                int l1;

                for (i1 = 0; i1 < l; ++i1) {
                    j1 = this.field_73220_k.nextInt(16) + 8;
                    k1 = this.field_73220_k.nextInt(16) + 8;
                    l1 = this.field_73230_p.func_175645_m(blockposition.func_177982_a(j1, 0, k1)).func_177956_o();
                    if (l1 > 0) {
                        int i2 = l1 - 1;

                        if (this.field_73230_p.func_175623_d(blockposition.func_177982_a(j1, i2 + 1, k1)) && this.field_73230_p.func_180495_p(blockposition.func_177982_a(j1, i2, k1)).func_177230_c() == Blocks.field_150377_bs) {
                            BlockChorusFlower.func_185603_a(this.field_73230_p, blockposition.func_177982_a(j1, i2 + 1, k1), this.field_73220_k, 8);
                        }
                    }
                }

                if (this.field_73220_k.nextInt(700) == 0) {
                    i1 = this.field_73220_k.nextInt(16) + 8;
                    j1 = this.field_73220_k.nextInt(16) + 8;
                    k1 = this.field_73230_p.func_175645_m(blockposition.func_177982_a(i1, 0, j1)).func_177956_o();
                    if (k1 > 0) {
                        l1 = k1 + 3 + this.field_73220_k.nextInt(7);
                        BlockPos blockposition1 = blockposition.func_177982_a(i1, l1, j1);

                        (new WorldGenEndGateway()).func_180709_b(this.field_73230_p, this.field_73220_k, blockposition1);
                        TileEntity tileentity = this.field_73230_p.func_175625_s(blockposition1);

                        if (tileentity instanceof TileEntityEndGateway) {
                            TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway) tileentity;

                            tileentityendgateway.func_190603_b(this.field_191061_n);
                        }
                    }
                }
            }
        }

        BlockFalling.field_149832_M = false;
    }

    public boolean func_185933_a(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        return this.field_73230_p.func_180494_b(blockposition).func_76747_a(enumcreaturetype);
    }

    @Nullable
    public BlockPos func_180513_a(World world, String s, BlockPos blockposition, boolean flag) {
        return "EndCity".equals(s) && this.field_185972_n != null ? this.field_185972_n.func_180706_b(world, blockposition, flag) : null;
    }

    public boolean func_193414_a(World world, String s, BlockPos blockposition) {
        return "EndCity".equals(s) && this.field_185972_n != null ? this.field_185972_n.func_175795_b(blockposition) : false;
    }

    public void func_180514_a(Chunk chunk, int i, int j) {}
}

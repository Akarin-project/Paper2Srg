package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class ChunkGeneratorHell implements IChunkGenerator {

    protected static final IBlockState field_185940_a = Blocks.field_150350_a.func_176223_P();
    protected static final IBlockState field_185941_b = Blocks.field_150424_aL.func_176223_P();
    protected static final IBlockState field_185942_c = Blocks.field_150357_h.func_176223_P();
    protected static final IBlockState field_185943_d = Blocks.field_150353_l.func_176223_P();
    protected static final IBlockState field_185944_e = Blocks.field_150351_n.func_176223_P();
    protected static final IBlockState field_185945_f = Blocks.field_150425_aM.func_176223_P();
    private final World field_185952_n;
    private final boolean field_185953_o;
    private final Random field_185954_p;
    private double[] field_73185_q = new double[256];
    private double[] field_73184_r = new double[256];
    private double[] field_185955_s = new double[256];
    private double[] field_185956_t;
    private final NoiseGeneratorOctaves field_185957_u;
    private final NoiseGeneratorOctaves field_185958_v;
    private final NoiseGeneratorOctaves field_185959_w;
    private final NoiseGeneratorOctaves field_73177_m;
    private final NoiseGeneratorOctaves field_73174_n;
    public final NoiseGeneratorOctaves field_185946_g;
    public final NoiseGeneratorOctaves field_185947_h;
    private final WorldGenFire field_177470_t = new WorldGenFire();
    private final WorldGenGlowStone1 field_177469_u = new WorldGenGlowStone1();
    private final WorldGenGlowStone2 field_177468_v = new WorldGenGlowStone2();
    private final WorldGenerator field_177467_w;
    private final WorldGenerator field_189888_D;
    private final WorldGenHellLava field_177473_x;
    private final WorldGenHellLava field_177472_y;
    private final WorldGenBush field_177471_z;
    private final WorldGenBush field_177465_A;
    private final MapGenNetherBridge field_73172_c;
    private final MapGenBase field_185939_I;
    double[] field_185948_i;
    double[] field_185949_j;
    double[] field_185950_k;
    double[] field_73168_g;
    double[] field_185951_m;

    public ChunkGeneratorHell(World world, boolean flag, long i) {
        this.field_177467_w = new WorldGenMinable(Blocks.field_150449_bY.func_176223_P(), 14, BlockMatcher.func_177642_a(Blocks.field_150424_aL));
        this.field_189888_D = new WorldGenMinable(Blocks.field_189877_df.func_176223_P(), 33, BlockMatcher.func_177642_a(Blocks.field_150424_aL));
        this.field_177473_x = new WorldGenHellLava(Blocks.field_150356_k, true);
        this.field_177472_y = new WorldGenHellLava(Blocks.field_150356_k, false);
        this.field_177471_z = new WorldGenBush(Blocks.field_150338_P);
        this.field_177465_A = new WorldGenBush(Blocks.field_150337_Q);
        this.field_73172_c = new MapGenNetherBridge();
        this.field_185939_I = new MapGenCavesHell();
        this.field_185952_n = world;
        this.field_185953_o = flag;
        this.field_185954_p = new Random(i);
        this.field_185957_u = new NoiseGeneratorOctaves(this.field_185954_p, 16);
        this.field_185958_v = new NoiseGeneratorOctaves(this.field_185954_p, 16);
        this.field_185959_w = new NoiseGeneratorOctaves(this.field_185954_p, 8);
        this.field_73177_m = new NoiseGeneratorOctaves(this.field_185954_p, 4);
        this.field_73174_n = new NoiseGeneratorOctaves(this.field_185954_p, 4);
        this.field_185946_g = new NoiseGeneratorOctaves(this.field_185954_p, 10);
        this.field_185947_h = new NoiseGeneratorOctaves(this.field_185954_p, 16);
        world.func_181544_b(63);
    }

    public void func_185936_a(int i, int j, ChunkPrimer chunksnapshot) {
        boolean flag = true;
        int k = this.field_185952_n.func_181545_F() / 2 + 1;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;

        this.field_185956_t = this.func_185938_a(this.field_185956_t, i * 4, 0, j * 4, 5, 17, 5);

        for (int l = 0; l < 4; ++l) {
            for (int i1 = 0; i1 < 4; ++i1) {
                for (int j1 = 0; j1 < 16; ++j1) {
                    double d0 = 0.125D;
                    double d1 = this.field_185956_t[((l + 0) * 5 + i1 + 0) * 17 + j1 + 0];
                    double d2 = this.field_185956_t[((l + 0) * 5 + i1 + 1) * 17 + j1 + 0];
                    double d3 = this.field_185956_t[((l + 1) * 5 + i1 + 0) * 17 + j1 + 0];
                    double d4 = this.field_185956_t[((l + 1) * 5 + i1 + 1) * 17 + j1 + 0];
                    double d5 = (this.field_185956_t[((l + 0) * 5 + i1 + 0) * 17 + j1 + 1] - d1) * 0.125D;
                    double d6 = (this.field_185956_t[((l + 0) * 5 + i1 + 1) * 17 + j1 + 1] - d2) * 0.125D;
                    double d7 = (this.field_185956_t[((l + 1) * 5 + i1 + 0) * 17 + j1 + 1] - d3) * 0.125D;
                    double d8 = (this.field_185956_t[((l + 1) * 5 + i1 + 1) * 17 + j1 + 1] - d4) * 0.125D;

                    for (int k1 = 0; k1 < 8; ++k1) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25D;
                        double d13 = (d4 - d2) * 0.25D;

                        for (int l1 = 0; l1 < 4; ++l1) {
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * 0.25D;

                            for (int i2 = 0; i2 < 4; ++i2) {
                                IBlockState iblockdata = null;

                                if (j1 * 8 + k1 < k) {
                                    iblockdata = ChunkGeneratorHell.field_185943_d;
                                }

                                if (d15 > 0.0D) {
                                    iblockdata = ChunkGeneratorHell.field_185941_b;
                                }

                                int j2 = l1 + l * 4;
                                int k2 = k1 + j1 * 8;
                                int l2 = i2 + i1 * 4;

                                chunksnapshot.func_177855_a(j2, k2, l2, iblockdata);
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

    public void func_185937_b(int i, int j, ChunkPrimer chunksnapshot) {
        int k = this.field_185952_n.func_181545_F() + 1;
        double d0 = 0.03125D;

        this.field_73185_q = this.field_73177_m.func_76304_a(this.field_73185_q, i * 16, j * 16, 0, 16, 16, 1, 0.03125D, 0.03125D, 1.0D);
        this.field_73184_r = this.field_73177_m.func_76304_a(this.field_73184_r, i * 16, 109, j * 16, 16, 1, 16, 0.03125D, 1.0D, 0.03125D);
        this.field_185955_s = this.field_73174_n.func_76304_a(this.field_185955_s, i * 16, j * 16, 0, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                boolean flag = this.field_73185_q[l + i1 * 16] + this.field_185954_p.nextDouble() * 0.2D > 0.0D;
                boolean flag1 = this.field_73184_r[l + i1 * 16] + this.field_185954_p.nextDouble() * 0.2D > 0.0D;
                int j1 = (int) (this.field_185955_s[l + i1 * 16] / 3.0D + 3.0D + this.field_185954_p.nextDouble() * 0.25D);
                int k1 = -1;
                IBlockState iblockdata = ChunkGeneratorHell.field_185941_b;
                IBlockState iblockdata1 = ChunkGeneratorHell.field_185941_b;

                for (int l1 = 127; l1 >= 0; --l1) {
                    // Paper start - Configurable flat bedrock worldgen
                    if (l1 < 127 - (field_185952_n.paperConfig.generateFlatBedrock ? 0 : this.field_185954_p.nextInt(5)) &&
                            l1 > (field_185952_n.paperConfig.generateFlatBedrock ? 0 : this.field_185954_p.nextInt(5))) {
                        // Paper end
                        IBlockState iblockdata2 = chunksnapshot.func_177856_a(i1, l1, l);

                        if (iblockdata2.func_177230_c() != null && iblockdata2.func_185904_a() != Material.field_151579_a) {
                            if (iblockdata2.func_177230_c() == Blocks.field_150424_aL) {
                                if (k1 == -1) {
                                    if (j1 <= 0) {
                                        iblockdata = ChunkGeneratorHell.field_185940_a;
                                        iblockdata1 = ChunkGeneratorHell.field_185941_b;
                                    } else if (l1 >= k - 4 && l1 <= k + 1) {
                                        iblockdata = ChunkGeneratorHell.field_185941_b;
                                        iblockdata1 = ChunkGeneratorHell.field_185941_b;
                                        if (flag1) {
                                            iblockdata = ChunkGeneratorHell.field_185944_e;
                                            iblockdata1 = ChunkGeneratorHell.field_185941_b;
                                        }

                                        if (flag) {
                                            iblockdata = ChunkGeneratorHell.field_185945_f;
                                            iblockdata1 = ChunkGeneratorHell.field_185945_f;
                                        }
                                    }

                                    if (l1 < k && (iblockdata == null || iblockdata.func_185904_a() == Material.field_151579_a)) {
                                        iblockdata = ChunkGeneratorHell.field_185943_d;
                                    }

                                    k1 = j1;
                                    if (l1 >= k - 1) {
                                        chunksnapshot.func_177855_a(i1, l1, l, iblockdata);
                                    } else {
                                        chunksnapshot.func_177855_a(i1, l1, l, iblockdata1);
                                    }
                                } else if (k1 > 0) {
                                    --k1;
                                    chunksnapshot.func_177855_a(i1, l1, l, iblockdata1);
                                }
                            }
                        } else {
                            k1 = -1;
                        }
                    } else {
                        chunksnapshot.func_177855_a(i1, l1, l, ChunkGeneratorHell.field_185942_c);
                    }
                }
            }
        }

    }

    public Chunk func_185932_a(int i, int j) {
        this.field_185954_p.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        this.func_185936_a(i, j, chunksnapshot);
        this.func_185937_b(i, j, chunksnapshot);
        this.field_185939_I.func_186125_a(this.field_185952_n, i, j, chunksnapshot);
        if (this.field_185953_o) {
            this.field_73172_c.func_186125_a(this.field_185952_n, i, j, chunksnapshot);
        }

        Chunk chunk = new Chunk(this.field_185952_n, chunksnapshot, i, j);
        Biome[] abiomebase = this.field_185952_n.func_72959_q().func_76933_b((Biome[]) null, i * 16, j * 16, 16, 16);
        byte[] abyte = chunk.func_76605_m();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.func_185362_a(abiomebase[k]);
        }

        chunk.func_76613_n();
        return chunk;
    }

    private double[] func_185938_a(double[] adouble, int i, int j, int k, int l, int i1, int j1) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        }

        double d0 = 684.412D;
        double d1 = 2053.236D;

        this.field_73168_g = this.field_185946_g.func_76304_a(this.field_73168_g, i, j, k, l, 1, j1, 1.0D, 0.0D, 1.0D);
        this.field_185951_m = this.field_185947_h.func_76304_a(this.field_185951_m, i, j, k, l, 1, j1, 100.0D, 0.0D, 100.0D);
        this.field_185948_i = this.field_185959_w.func_76304_a(this.field_185948_i, i, j, k, l, i1, j1, 8.555150000000001D, 34.2206D, 8.555150000000001D);
        this.field_185949_j = this.field_185957_u.func_76304_a(this.field_185949_j, i, j, k, l, i1, j1, 684.412D, 2053.236D, 684.412D);
        this.field_185950_k = this.field_185958_v.func_76304_a(this.field_185950_k, i, j, k, l, i1, j1, 684.412D, 2053.236D, 684.412D);
        int k1 = 0;
        double[] adouble1 = new double[i1];

        int l1;

        for (l1 = 0; l1 < i1; ++l1) {
            adouble1[l1] = Math.cos((double) l1 * 3.141592653589793D * 6.0D / (double) i1) * 2.0D;
            double d2 = (double) l1;

            if (l1 > i1 / 2) {
                d2 = (double) (i1 - 1 - l1);
            }

            if (d2 < 4.0D) {
                d2 = 4.0D - d2;
                adouble1[l1] -= d2 * d2 * d2 * 10.0D;
            }
        }

        for (l1 = 0; l1 < l; ++l1) {
            for (int i2 = 0; i2 < j1; ++i2) {
                double d3 = 0.0D;

                for (int j2 = 0; j2 < i1; ++j2) {
                    double d4 = adouble1[j2];
                    double d5 = this.field_185949_j[k1] / 512.0D;
                    double d6 = this.field_185950_k[k1] / 512.0D;
                    double d7 = (this.field_185948_i[k1] / 10.0D + 1.0D) / 2.0D;
                    double d8;

                    if (d7 < 0.0D) {
                        d8 = d5;
                    } else if (d7 > 1.0D) {
                        d8 = d6;
                    } else {
                        d8 = d5 + (d6 - d5) * d7;
                    }

                    d8 -= d4;
                    double d9;

                    if (j2 > i1 - 4) {
                        d9 = (double) ((float) (j2 - (i1 - 4)) / 3.0F);
                        d8 = d8 * (1.0D - d9) + -10.0D * d9;
                    }

                    if ((double) j2 < 0.0D) {
                        d9 = (0.0D - (double) j2) / 4.0D;
                        d9 = MathHelper.func_151237_a(d9, 0.0D, 1.0D);
                        d8 = d8 * (1.0D - d9) + -10.0D * d9;
                    }

                    adouble[k1] = d8;
                    ++k1;
                }
            }
        }

        return adouble;
    }

    public void func_185931_b(int i, int j) {
        BlockFalling.field_149832_M = true;
        int k = i * 16;
        int l = j * 16;
        BlockPos blockposition = new BlockPos(k, 0, l);
        Biome biomebase = this.field_185952_n.func_180494_b(blockposition.func_177982_a(16, 0, 16));
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);

        this.field_73172_c.func_175794_a(this.field_185952_n, this.field_185954_p, chunkcoordintpair);

        int i1;

        for (i1 = 0; i1 < 8; ++i1) {
            this.field_177472_y.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16) + 8, this.field_185954_p.nextInt(120) + 4, this.field_185954_p.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < this.field_185954_p.nextInt(this.field_185954_p.nextInt(10) + 1) + 1; ++i1) {
            this.field_177470_t.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16) + 8, this.field_185954_p.nextInt(120) + 4, this.field_185954_p.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < this.field_185954_p.nextInt(this.field_185954_p.nextInt(10) + 1); ++i1) {
            this.field_177469_u.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16) + 8, this.field_185954_p.nextInt(120) + 4, this.field_185954_p.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < 10; ++i1) {
            this.field_177468_v.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16) + 8, this.field_185954_p.nextInt(128), this.field_185954_p.nextInt(16) + 8));
        }

        if (this.field_185954_p.nextBoolean()) {
            this.field_177471_z.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16) + 8, this.field_185954_p.nextInt(128), this.field_185954_p.nextInt(16) + 8));
        }

        if (this.field_185954_p.nextBoolean()) {
            this.field_177465_A.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16) + 8, this.field_185954_p.nextInt(128), this.field_185954_p.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < 16; ++i1) {
            this.field_177467_w.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16), this.field_185954_p.nextInt(108) + 10, this.field_185954_p.nextInt(16)));
        }

        i1 = this.field_185952_n.func_181545_F() / 2 + 1;

        int j1;

        for (j1 = 0; j1 < 4; ++j1) {
            this.field_189888_D.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16), i1 - 5 + this.field_185954_p.nextInt(10), this.field_185954_p.nextInt(16)));
        }

        for (j1 = 0; j1 < 16; ++j1) {
            this.field_177473_x.func_180709_b(this.field_185952_n, this.field_185954_p, blockposition.func_177982_a(this.field_185954_p.nextInt(16), this.field_185954_p.nextInt(108) + 10, this.field_185954_p.nextInt(16)));
        }

        biomebase.func_180624_a(this.field_185952_n, this.field_185954_p, new BlockPos(k, 0, l));
        BlockFalling.field_149832_M = false;
    }

    public boolean func_185933_a(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        if (enumcreaturetype == EnumCreatureType.MONSTER) {
            if (this.field_73172_c.func_175795_b(blockposition)) {
                return this.field_73172_c.func_75059_a();
            }

            if (this.field_73172_c.func_175796_a(this.field_185952_n, blockposition) && this.field_185952_n.func_180495_p(blockposition.func_177977_b()).func_177230_c() == Blocks.field_150385_bj) {
                return this.field_73172_c.func_75059_a();
            }
        }

        Biome biomebase = this.field_185952_n.func_180494_b(blockposition);

        return biomebase.func_76747_a(enumcreaturetype);
    }

    @Nullable
    public BlockPos func_180513_a(World world, String s, BlockPos blockposition, boolean flag) {
        return "Fortress".equals(s) && this.field_73172_c != null ? this.field_73172_c.func_180706_b(world, blockposition, flag) : null;
    }

    public boolean func_193414_a(World world, String s, BlockPos blockposition) {
        return "Fortress".equals(s) && this.field_73172_c != null ? this.field_73172_c.func_175795_b(blockposition) : false;
    }

    public void func_180514_a(Chunk chunk, int i, int j) {
        if (this.field_185952_n.paperConfig.generateFortress) this.field_73172_c.func_186125_a(this.field_185952_n, i, j, (ChunkPrimer) null);
    }
}

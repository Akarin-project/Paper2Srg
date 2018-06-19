package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.WoodlandMansion;

public class ChunkGeneratorOverworld implements IChunkGenerator {

    protected static final IBlockState field_185982_a = Blocks.field_150348_b.func_176223_P();
    private final Random field_185990_i;
    private final NoiseGeneratorOctaves field_185991_j;
    private final NoiseGeneratorOctaves field_185992_k;
    private final NoiseGeneratorOctaves field_185993_l;
    private final NoiseGeneratorPerlin field_185994_m;
    public NoiseGeneratorOctaves field_185983_b;
    public NoiseGeneratorOctaves field_185984_c;
    public NoiseGeneratorOctaves field_185985_d;
    private final World field_185995_n;
    private final boolean field_185996_o;
    private final WorldType field_185997_p;
    private final double[] field_185998_q;
    private final float[] field_185999_r;
    private ChunkGeneratorSettings field_186000_s;
    private IBlockState field_186001_t;
    private double[] field_186002_u;
    private final MapGenBase field_186003_v;
    private final MapGenStronghold field_186004_w;
    private final MapGenVillage field_186005_x;
    private final MapGenMineshaft field_186006_y;
    private final MapGenScatteredFeature field_186007_z;
    private final MapGenBase field_185979_A;
    private final StructureOceanMonument field_185980_B;
    private final WoodlandMansion field_191060_C;
    private Biome[] field_185981_C;
    double[] field_185986_e;
    double[] field_185987_f;
    double[] field_185988_g;
    double[] field_185989_h;

    public ChunkGeneratorOverworld(World world, long i, boolean flag, String s) {
        this.field_186001_t = Blocks.field_150355_j.func_176223_P();
        this.field_186002_u = new double[256];
        this.field_186003_v = new MapGenCaves();
        this.field_186004_w = new MapGenStronghold();
        this.field_186005_x = new MapGenVillage();
        this.field_186006_y = new MapGenMineshaft();
        this.field_186007_z = new MapGenScatteredFeature();
        this.field_185979_A = new MapGenRavine();
        this.field_185980_B = new StructureOceanMonument();
        this.field_191060_C = new WoodlandMansion(this);
        this.field_185995_n = world;
        this.field_185996_o = flag;
        this.field_185997_p = world.func_72912_H().func_76067_t();
        this.field_185990_i = new Random(i);
        this.field_185991_j = new NoiseGeneratorOctaves(this.field_185990_i, 16);
        this.field_185992_k = new NoiseGeneratorOctaves(this.field_185990_i, 16);
        this.field_185993_l = new NoiseGeneratorOctaves(this.field_185990_i, 8);
        this.field_185994_m = new NoiseGeneratorPerlin(this.field_185990_i, 4);
        this.field_185983_b = new NoiseGeneratorOctaves(this.field_185990_i, 10);
        this.field_185984_c = new NoiseGeneratorOctaves(this.field_185990_i, 16);
        this.field_185985_d = new NoiseGeneratorOctaves(this.field_185990_i, 8);
        this.field_185998_q = new double[825];
        this.field_185999_r = new float[25];

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                float f = 10.0F / MathHelper.func_76129_c((float) (j * j + k * k) + 0.2F);

                this.field_185999_r[j + 2 + (k + 2) * 5] = f;
            }
        }

        if (s != null) {
            this.field_186000_s = ChunkGeneratorSettings.Factory.func_177865_a(s).func_177864_b();
            this.field_186001_t = this.field_186000_s.field_177778_E ? Blocks.field_150353_l.func_176223_P() : Blocks.field_150355_j.func_176223_P();
            world.func_181544_b(this.field_186000_s.field_177841_q);
        }

    }

    public void func_185976_a(int i, int j, ChunkPrimer chunksnapshot) {
        this.field_185981_C = this.field_185995_n.func_72959_q().func_76937_a(this.field_185981_C, i * 4 - 2, j * 4 - 2, 10, 10);
        this.func_185978_a(i * 4, 0, j * 4);

        for (int k = 0; k < 4; ++k) {
            int l = k * 5;
            int i1 = (k + 1) * 5;

            for (int j1 = 0; j1 < 4; ++j1) {
                int k1 = (l + j1) * 33;
                int l1 = (l + j1 + 1) * 33;
                int i2 = (i1 + j1) * 33;
                int j2 = (i1 + j1 + 1) * 33;

                for (int k2 = 0; k2 < 32; ++k2) {
                    double d0 = 0.125D;
                    double d1 = this.field_185998_q[k1 + k2];
                    double d2 = this.field_185998_q[l1 + k2];
                    double d3 = this.field_185998_q[i2 + k2];
                    double d4 = this.field_185998_q[j2 + k2];
                    double d5 = (this.field_185998_q[k1 + k2 + 1] - d1) * 0.125D;
                    double d6 = (this.field_185998_q[l1 + k2 + 1] - d2) * 0.125D;
                    double d7 = (this.field_185998_q[i2 + k2 + 1] - d3) * 0.125D;
                    double d8 = (this.field_185998_q[j2 + k2 + 1] - d4) * 0.125D;

                    for (int l2 = 0; l2 < 8; ++l2) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25D;
                        double d13 = (d4 - d2) * 0.25D;

                        for (int i3 = 0; i3 < 4; ++i3) {
                            double d14 = 0.25D;
                            double d15 = (d11 - d10) * 0.25D;
                            double d16 = d10 - d15;

                            for (int j3 = 0; j3 < 4; ++j3) {
                                if ((d16 += d15) > 0.0D) {
                                    chunksnapshot.func_177855_a(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, ChunkGeneratorOverworld.field_185982_a);
                                } else if (k2 * 8 + l2 < this.field_186000_s.field_177841_q) {
                                    chunksnapshot.func_177855_a(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, this.field_186001_t);
                                }
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

    public void func_185977_a(int i, int j, ChunkPrimer chunksnapshot, Biome[] abiomebase) {
        double d0 = 0.03125D;

        this.field_186002_u = this.field_185994_m.func_151599_a(this.field_186002_u, (double) (i * 16), (double) (j * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                Biome biomebase = abiomebase[l + k * 16];

                biomebase.func_180622_a(this.field_185995_n, this.field_185990_i, chunksnapshot, i * 16 + k, j * 16 + l, this.field_186002_u[l + k * 16]);
            }
        }

    }

    public Chunk func_185932_a(int i, int j) {
        this.field_185990_i.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        this.func_185976_a(i, j, chunksnapshot);
        this.field_185981_C = this.field_185995_n.func_72959_q().func_76933_b(this.field_185981_C, i * 16, j * 16, 16, 16);
        this.func_185977_a(i, j, chunksnapshot, this.field_185981_C);
        if (this.field_186000_s.field_177839_r && this.field_185995_n.paperConfig.generateCaves) { // Paper
            this.field_186003_v.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
        }

        if (this.field_186000_s.field_177850_z && this.field_185995_n.paperConfig.generateCanyon) { // Paper
            this.field_185979_A.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
        }

        if (this.field_185996_o) {
            if (this.field_186000_s.field_177829_w && this.field_185995_n.paperConfig.generateMineshaft) { // Paper
                this.field_186006_y.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
            }

            if (this.field_186000_s.field_177831_v&& this.field_185995_n.paperConfig.generateVillage) { // Paper
                this.field_186005_x.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
            }

            if (this.field_186000_s.field_177833_u && this.field_185995_n.paperConfig.generateStronghold) { // Paper
                this.field_186004_w.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
            }

            if (this.field_186000_s.field_177854_x && this.field_185995_n.paperConfig.generateTemple) { // Paper
                this.field_186007_z.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
            }

            if (this.field_186000_s.field_177852_y && this.field_185995_n.paperConfig.generateMonument) { // Paper
                this.field_185980_B.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
            }

            if (this.field_186000_s.field_191077_z) {
                this.field_191060_C.func_186125_a(this.field_185995_n, i, j, chunksnapshot);
            }
        }

        Chunk chunk = new Chunk(this.field_185995_n, chunksnapshot, i, j);
        byte[] abyte = chunk.func_76605_m();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.func_185362_a(this.field_185981_C[k]);
        }

        chunk.func_76603_b();
        return chunk;
    }

    private void func_185978_a(int i, int j, int k) {
        this.field_185989_h = this.field_185984_c.func_76305_a(this.field_185989_h, i, k, 5, 5, (double) this.field_186000_s.field_177808_e, (double) this.field_186000_s.field_177803_f, (double) this.field_186000_s.field_177804_g);
        float f = this.field_186000_s.field_177811_a;
        float f1 = this.field_186000_s.field_177809_b;

        this.field_185986_e = this.field_185993_l.func_76304_a(this.field_185986_e, i, j, k, 5, 33, 5, (double) (f / this.field_186000_s.field_177825_h), (double) (f1 / this.field_186000_s.field_177827_i), (double) (f / this.field_186000_s.field_177821_j));
        this.field_185987_f = this.field_185991_j.func_76304_a(this.field_185987_f, i, j, k, 5, 33, 5, (double) f, (double) f1, (double) f);
        this.field_185988_g = this.field_185992_k.func_76304_a(this.field_185988_g, i, j, k, 5, 33, 5, (double) f, (double) f1, (double) f);
        int l = 0;
        int i1 = 0;

        for (int j1 = 0; j1 < 5; ++j1) {
            for (int k1 = 0; k1 < 5; ++k1) {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                boolean flag = true;
                Biome biomebase = this.field_185981_C[j1 + 2 + (k1 + 2) * 10];

                for (int l1 = -2; l1 <= 2; ++l1) {
                    for (int i2 = -2; i2 <= 2; ++i2) {
                        Biome biomebase1 = this.field_185981_C[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
                        float f5 = this.field_186000_s.field_177813_n + biomebase1.func_185355_j() * this.field_186000_s.field_177819_m;
                        float f6 = this.field_186000_s.field_177843_p + biomebase1.func_185360_m() * this.field_186000_s.field_177815_o;

                        if (this.field_185997_p == WorldType.field_151360_e && f5 > 0.0F) {
                            f5 = 1.0F + f5 * 2.0F;
                            f6 = 1.0F + f6 * 4.0F;
                        }
                        // CraftBukkit start - fix MC-54738
                        if (f5 < -1.8F) {
                            f5 = -1.8F;
                        }
                        // CraftBukkit end

                        float f7 = this.field_185999_r[l1 + 2 + (i2 + 2) * 5] / (f5 + 2.0F);

                        if (biomebase1.func_185355_j() > biomebase.func_185355_j()) {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 /= f4;
                f3 /= f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d0 = this.field_185989_h[i1] / 8000.0D;

                if (d0 < 0.0D) {
                    d0 = -d0 * 0.3D;
                }

                d0 = d0 * 3.0D - 2.0D;
                if (d0 < 0.0D) {
                    d0 /= 2.0D;
                    if (d0 < -1.0D) {
                        d0 = -1.0D;
                    }

                    d0 /= 1.4D;
                    d0 /= 2.0D;
                } else {
                    if (d0 > 1.0D) {
                        d0 = 1.0D;
                    }

                    d0 /= 8.0D;
                }

                ++i1;
                double d1 = (double) f3;
                double d2 = (double) f2;

                d1 += d0 * 0.2D;
                d1 = d1 * (double) this.field_186000_s.field_177823_k / 8.0D;
                double d3 = (double) this.field_186000_s.field_177823_k + d1 * 4.0D;

                for (int j2 = 0; j2 < 33; ++j2) {
                    double d4 = ((double) j2 - d3) * (double) this.field_186000_s.field_177817_l * 128.0D / 256.0D / d2;

                    if (d4 < 0.0D) {
                        d4 *= 4.0D;
                    }

                    double d5 = this.field_185987_f[l] / (double) this.field_186000_s.field_177806_d;
                    double d6 = this.field_185988_g[l] / (double) this.field_186000_s.field_177810_c;
                    double d7 = (this.field_185986_e[l] / 10.0D + 1.0D) / 2.0D;
                    double d8 = MathHelper.func_151238_b(d5, d6, d7) - d4;

                    if (j2 > 29) {
                        double d9 = (double) ((float) (j2 - 29) / 3.0F);

                        d8 = d8 * (1.0D - d9) + -10.0D * d9;
                    }

                    this.field_185998_q[l] = d8;
                    ++l;
                }
            }
        }

    }

    public void func_185931_b(int i, int j) {
        BlockFalling.field_149832_M = true;
        int k = i * 16;
        int l = j * 16;
        BlockPos blockposition = new BlockPos(k, 0, l);
        Biome biomebase = this.field_185995_n.func_180494_b(blockposition.func_177982_a(16, 0, 16));

        this.field_185990_i.setSeed(this.field_185995_n.func_72905_C());
        long i1 = this.field_185990_i.nextLong() / 2L * 2L + 1L;
        long j1 = this.field_185990_i.nextLong() / 2L * 2L + 1L;

        this.field_185990_i.setSeed((long) i * i1 + (long) j * j1 ^ this.field_185995_n.func_72905_C());
        boolean flag = false;
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);

        if (this.field_185996_o) {
            if (this.field_186000_s.field_177829_w && this.field_185995_n.paperConfig.generateMineshaft) { // Paper
                this.field_186006_y.func_175794_a(this.field_185995_n, this.field_185990_i, chunkcoordintpair);
            }

            if (this.field_186000_s.field_177831_v && this.field_185995_n.paperConfig.generateVillage) { // Paper
                flag = this.field_186005_x.func_175794_a(this.field_185995_n, this.field_185990_i, chunkcoordintpair);
            }

            if (this.field_186000_s.field_177833_u && this.field_185995_n.paperConfig.generateStronghold) { // Paper
                this.field_186004_w.func_175794_a(this.field_185995_n, this.field_185990_i, chunkcoordintpair);
            }

            if (this.field_186000_s.field_177854_x && this.field_185995_n.paperConfig.generateTemple) { // Paper
                this.field_186007_z.func_175794_a(this.field_185995_n, this.field_185990_i, chunkcoordintpair);
            }

            if (this.field_186000_s.field_177852_y && this.field_185995_n.paperConfig.generateMonument) { // Paper
                this.field_185980_B.func_175794_a(this.field_185995_n, this.field_185990_i, chunkcoordintpair);
            }

            if (this.field_186000_s.field_191077_z) {
                this.field_191060_C.func_175794_a(this.field_185995_n, this.field_185990_i, chunkcoordintpair);
            }
        }

        int k1;
        int l1;
        int i2;

        if (biomebase != Biomes.field_76769_d && biomebase != Biomes.field_76786_s && this.field_186000_s.field_177781_A && !flag && this.field_185990_i.nextInt(this.field_186000_s.field_177782_B) == 0) {
            k1 = this.field_185990_i.nextInt(16) + 8;
            l1 = this.field_185990_i.nextInt(256);
            i2 = this.field_185990_i.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.field_150355_j)).func_180709_b(this.field_185995_n, this.field_185990_i, blockposition.func_177982_a(k1, l1, i2));
        }

        if (!flag && this.field_185990_i.nextInt(this.field_186000_s.field_177777_D / 10) == 0 && this.field_186000_s.field_177783_C) {
            k1 = this.field_185990_i.nextInt(16) + 8;
            l1 = this.field_185990_i.nextInt(this.field_185990_i.nextInt(248) + 8);
            i2 = this.field_185990_i.nextInt(16) + 8;
            if (l1 < this.field_185995_n.func_181545_F() || this.field_185990_i.nextInt(this.field_186000_s.field_177777_D / 8) == 0) {
                (new WorldGenLakes(Blocks.field_150353_l)).func_180709_b(this.field_185995_n, this.field_185990_i, blockposition.func_177982_a(k1, l1, i2));
            }
        }

        if (this.field_186000_s.field_177837_s && this.field_185995_n.paperConfig.generateDungeon) { // Paper
            for (k1 = 0; k1 < this.field_186000_s.field_177835_t; ++k1) {
                l1 = this.field_185990_i.nextInt(16) + 8;
                i2 = this.field_185990_i.nextInt(256);
                int j2 = this.field_185990_i.nextInt(16) + 8;

                (new WorldGenDungeons()).func_180709_b(this.field_185995_n, this.field_185990_i, blockposition.func_177982_a(l1, i2, j2));
            }
        }

        biomebase.func_180624_a(this.field_185995_n, this.field_185990_i, new BlockPos(k, 0, l));
        WorldEntitySpawner.func_77191_a(this.field_185995_n, biomebase, k + 8, l + 8, 16, 16, this.field_185990_i);
        blockposition = blockposition.func_177982_a(8, 0, 8);

        for (k1 = 0; k1 < 16; ++k1) {
            for (l1 = 0; l1 < 16; ++l1) {
                BlockPos blockposition1 = this.field_185995_n.func_175725_q(blockposition.func_177982_a(k1, 0, l1));
                BlockPos blockposition2 = blockposition1.func_177977_b();

                if (this.field_185995_n.func_175675_v(blockposition2)) {
                    this.field_185995_n.func_180501_a(blockposition2, Blocks.field_150432_aD.func_176223_P(), 2);
                }

                if (this.field_185995_n.func_175708_f(blockposition1, true)) {
                    this.field_185995_n.func_180501_a(blockposition1, Blocks.field_150431_aC.func_176223_P(), 2);
                }
            }
        }

        BlockFalling.field_149832_M = false;
    }

    public boolean func_185933_a(Chunk chunk, int i, int j) {
        boolean flag = false;

        if (this.field_186000_s.field_177852_y && this.field_185996_o && chunk.func_177416_w() < 3600L) {
            flag |= this.field_185980_B.func_175794_a(this.field_185995_n, this.field_185990_i, new ChunkPos(i, j));
        }

        return flag;
    }

    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        Biome biomebase = this.field_185995_n.func_180494_b(blockposition);

        if (this.field_185996_o) {
            if (enumcreaturetype == EnumCreatureType.MONSTER && this.field_186007_z.func_175798_a(blockposition)) {
                return this.field_186007_z.func_82667_a();
            }

            if (enumcreaturetype == EnumCreatureType.MONSTER && this.field_186000_s.field_177852_y && this.field_185980_B.func_175796_a(this.field_185995_n, blockposition)) {
                return this.field_185980_B.func_175799_b();
            }
        }

        return biomebase.func_76747_a(enumcreaturetype);
    }

    public boolean func_193414_a(World world, String s, BlockPos blockposition) {
        return !this.field_185996_o ? false : ("Stronghold".equals(s) && this.field_186004_w != null ? this.field_186004_w.func_175795_b(blockposition) : ("Mansion".equals(s) && this.field_191060_C != null ? this.field_191060_C.func_175795_b(blockposition) : ("Monument".equals(s) && this.field_185980_B != null ? this.field_185980_B.func_175795_b(blockposition) : ("Village".equals(s) && this.field_186005_x != null ? this.field_186005_x.func_175795_b(blockposition) : ("Mineshaft".equals(s) && this.field_186006_y != null ? this.field_186006_y.func_175795_b(blockposition) : ("Temple".equals(s) && this.field_186007_z != null ? this.field_186007_z.func_175795_b(blockposition) : false))))));
    }

    @Nullable
    public BlockPos func_180513_a(World world, String s, BlockPos blockposition, boolean flag) {
        return !this.field_185996_o ? null : ("Stronghold".equals(s) && this.field_186004_w != null ? this.field_186004_w.func_180706_b(world, blockposition, flag) : ("Mansion".equals(s) && this.field_191060_C != null ? this.field_191060_C.func_180706_b(world, blockposition, flag) : ("Monument".equals(s) && this.field_185980_B != null ? this.field_185980_B.func_180706_b(world, blockposition, flag) : ("Village".equals(s) && this.field_186005_x != null ? this.field_186005_x.func_180706_b(world, blockposition, flag) : ("Mineshaft".equals(s) && this.field_186006_y != null ? this.field_186006_y.func_180706_b(world, blockposition, flag) : ("Temple".equals(s) && this.field_186007_z != null ? this.field_186007_z.func_180706_b(world, blockposition, flag) : null))))));
    }

    public void func_180514_a(Chunk chunk, int i, int j) {
        if (this.field_185996_o) {
            if (this.field_186000_s.field_177829_w && this.field_185995_n.paperConfig.generateMineshaft) { // Paper
                this.field_186006_y.func_186125_a(this.field_185995_n, i, j, (ChunkPrimer) null);
            }

            if (this.field_186000_s.field_177831_v && this.field_185995_n.paperConfig.generateVillage) { // Paper
                this.field_186005_x.func_186125_a(this.field_185995_n, i, j, (ChunkPrimer) null);
            }

            if (this.field_186000_s.field_177833_u && this.field_185995_n.paperConfig.generateStronghold) { // Paper
                this.field_186004_w.func_186125_a(this.field_185995_n, i, j, (ChunkPrimer) null);
            }

            if (this.field_186000_s.field_177854_x && this.field_185995_n.paperConfig.generateTemple) { // Paper
                this.field_186007_z.func_186125_a(this.field_185995_n, i, j, (ChunkPrimer) null);
            }

            if (this.field_186000_s.field_177852_y && this.field_185995_n.paperConfig.generateMonument) { // Paper
                this.field_185980_B.func_186125_a(this.field_185995_n, i, j, (ChunkPrimer) null);
            }

            if (this.field_186000_s.field_191077_z) {
                this.field_191060_C.func_186125_a(this.field_185995_n, i, j, (ChunkPrimer) null);
            }
        }

    }
}

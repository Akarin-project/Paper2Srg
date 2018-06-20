package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeMesa extends Biome {

    protected static final IBlockState field_185385_y = Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.COARSE_DIRT);
    protected static final IBlockState field_185386_z = Blocks.field_150349_c.func_176223_P();
    protected static final IBlockState field_185381_A = Blocks.field_150405_ch.func_176223_P();
    protected static final IBlockState field_185382_B = Blocks.field_150406_ce.func_176223_P();
    protected static final IBlockState field_185383_C = BiomeMesa.field_185382_B.func_177226_a(BlockColored.field_176581_a, EnumDyeColor.ORANGE);
    protected static final IBlockState field_185384_D = Blocks.field_150354_m.func_176223_P().func_177226_a(BlockSand.field_176504_a, BlockSand.EnumType.RED_SAND);
    private IBlockState[] field_150621_aC;
    private long field_150622_aD;
    private NoiseGeneratorPerlin field_150623_aE;
    private NoiseGeneratorPerlin field_150624_aF;
    private NoiseGeneratorPerlin field_150625_aG;
    private final boolean field_150626_aH;
    private final boolean field_150620_aI;

    public BiomeMesa(boolean flag, boolean flag1, Biome.a biomebase_a) {
        super(biomebase_a);
        this.field_150626_aH = flag;
        this.field_150620_aI = flag1;
        this.field_76762_K.clear();
        this.field_76752_A = BiomeMesa.field_185384_D;
        this.field_76753_B = BiomeMesa.field_185382_B;
        this.field_76760_I.field_76832_z = -999;
        this.field_76760_I.field_76804_C = 20;
        this.field_76760_I.field_76799_E = 3;
        this.field_76760_I.field_76800_F = 5;
        this.field_76760_I.field_76802_A = 0;
        this.field_76762_K.clear();
        if (flag1) {
            this.field_76760_I.field_76832_z = 5;
        }

    }

    @Override
    protected BiomeDecorator func_76729_a() {
        return new BiomeMesa.a(null);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        return BiomeMesa.field_76757_N;
    }

    @Override
    public void func_180622_a(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        if (this.field_150621_aC == null || this.field_150622_aD != world.func_72905_C()) {
            this.func_150619_a(world.func_72905_C());
        }

        if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != world.func_72905_C()) {
            Random random1 = new Random(this.field_150622_aD);

            this.field_150623_aE = new NoiseGeneratorPerlin(random1, 4);
            this.field_150624_aF = new NoiseGeneratorPerlin(random1, 1);
        }

        this.field_150622_aD = world.func_72905_C();
        double d1 = 0.0D;
        int k;
        int l;

        if (this.field_150626_aH) {
            k = (i & -16) + (j & 15);
            l = (j & -16) + (i & 15);
            double d2 = Math.min(Math.abs(d0), this.field_150623_aE.func_151601_a(k * 0.25D, l * 0.25D));

            if (d2 > 0.0D) {
                double d3 = 0.001953125D;
                double d4 = Math.abs(this.field_150624_aF.func_151601_a(k * 0.001953125D, l * 0.001953125D));

                d1 = d2 * d2 * 2.5D;
                double d5 = Math.ceil(d4 * 50.0D) + 14.0D;

                if (d1 > d5) {
                    d1 = d5;
                }

                d1 += 64.0D;
            }
        }

        k = i & 15;
        l = j & 15;
        int i1 = world.func_181545_F();
        IBlockState iblockdata = BiomeMesa.field_185382_B;
        IBlockState iblockdata1 = this.field_76753_B;
        int j1 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        boolean flag = Math.cos(d0 / 3.0D * 3.141592653589793D) > 0.0D;
        int k1 = -1;
        boolean flag1 = false;
        int l1 = 0;

        for (int i2 = 255; i2 >= 0; --i2) {
            if (chunksnapshot.func_177856_a(l, i2, k).func_185904_a() == Material.field_151579_a && i2 < (int) d1) {
                chunksnapshot.func_177855_a(l, i2, k, BiomeMesa.field_185365_a);
            }

            if (i2 <= (world.paperConfig.generateFlatBedrock ? 0 : random.nextInt(5))) { // Paper - Configurable flat bedrock
                chunksnapshot.func_177855_a(l, i2, k, BiomeMesa.field_185367_c);
            } else if (l1 < 15 || this.field_150626_aH) {
                IBlockState iblockdata2 = chunksnapshot.func_177856_a(l, i2, k);

                if (iblockdata2.func_185904_a() == Material.field_151579_a) {
                    k1 = -1;
                } else if (iblockdata2.func_177230_c() == Blocks.field_150348_b) {
                    if (k1 == -1) {
                        flag1 = false;
                        if (j1 <= 0) {
                            iblockdata = BiomeMesa.field_185366_b;
                            iblockdata1 = BiomeMesa.field_185365_a;
                        } else if (i2 >= i1 - 4 && i2 <= i1 + 1) {
                            iblockdata = BiomeMesa.field_185382_B;
                            iblockdata1 = this.field_76753_B;
                        }

                        if (i2 < i1 && (iblockdata == null || iblockdata.func_185904_a() == Material.field_151579_a)) {
                            iblockdata = BiomeMesa.field_185372_h;
                        }

                        k1 = j1 + Math.max(0, i2 - i1);
                        if (i2 >= i1 - 1) {
                            if (this.field_150620_aI && i2 > 86 + j1 * 2) {
                                if (flag) {
                                    chunksnapshot.func_177855_a(l, i2, k, BiomeMesa.field_185385_y);
                                } else {
                                    chunksnapshot.func_177855_a(l, i2, k, BiomeMesa.field_185386_z);
                                }
                            } else if (i2 > i1 + 3 + j1) {
                                IBlockState iblockdata3;

                                if (i2 >= 64 && i2 <= 127) {
                                    if (flag) {
                                        iblockdata3 = BiomeMesa.field_185381_A;
                                    } else {
                                        iblockdata3 = this.func_180629_a(i, i2, j);
                                    }
                                } else {
                                    iblockdata3 = BiomeMesa.field_185383_C;
                                }

                                chunksnapshot.func_177855_a(l, i2, k, iblockdata3);
                            } else {
                                chunksnapshot.func_177855_a(l, i2, k, this.field_76752_A);
                                flag1 = true;
                            }
                        } else {
                            chunksnapshot.func_177855_a(l, i2, k, iblockdata1);
                            if (iblockdata1.func_177230_c() == Blocks.field_150406_ce) {
                                chunksnapshot.func_177855_a(l, i2, k, BiomeMesa.field_185383_C);
                            }
                        }
                    } else if (k1 > 0) {
                        --k1;
                        if (flag1) {
                            chunksnapshot.func_177855_a(l, i2, k, BiomeMesa.field_185383_C);
                        } else {
                            chunksnapshot.func_177855_a(l, i2, k, this.func_180629_a(i, i2, j));
                        }
                    }

                    ++l1;
                }
            }
        }

    }

    private void func_150619_a(long i) {
        this.field_150621_aC = new IBlockState[64];
        Arrays.fill(this.field_150621_aC, BiomeMesa.field_185381_A);
        Random random = new Random(i);

        this.field_150625_aG = new NoiseGeneratorPerlin(random, 1);

        int j;

        for (j = 0; j < 64; ++j) {
            j += random.nextInt(5) + 1;
            if (j < 64) {
                this.field_150621_aC[j] = BiomeMesa.field_185383_C;
            }
        }

        j = random.nextInt(4) + 2;

        int k;
        int l;
        int i1;
        int j1;

        for (k = 0; k < j; ++k) {
            l = random.nextInt(3) + 1;
            i1 = random.nextInt(64);

            for (j1 = 0; i1 + j1 < 64 && j1 < l; ++j1) {
                this.field_150621_aC[i1 + j1] = BiomeMesa.field_185382_B.func_177226_a(BlockColored.field_176581_a, EnumDyeColor.YELLOW);
            }
        }

        k = random.nextInt(4) + 2;

        int k1;

        for (l = 0; l < k; ++l) {
            i1 = random.nextInt(3) + 2;
            j1 = random.nextInt(64);

            for (k1 = 0; j1 + k1 < 64 && k1 < i1; ++k1) {
                this.field_150621_aC[j1 + k1] = BiomeMesa.field_185382_B.func_177226_a(BlockColored.field_176581_a, EnumDyeColor.BROWN);
            }
        }

        l = random.nextInt(4) + 2;

        for (i1 = 0; i1 < l; ++i1) {
            j1 = random.nextInt(3) + 1;
            k1 = random.nextInt(64);

            for (int l1 = 0; k1 + l1 < 64 && l1 < j1; ++l1) {
                this.field_150621_aC[k1 + l1] = BiomeMesa.field_185382_B.func_177226_a(BlockColored.field_176581_a, EnumDyeColor.RED);
            }
        }

        i1 = random.nextInt(3) + 3;
        j1 = 0;

        for (k1 = 0; k1 < i1; ++k1) {
            boolean flag = true;

            j1 += random.nextInt(16) + 4;

            for (int i2 = 0; j1 + i2 < 64 && i2 < 1; ++i2) {
                this.field_150621_aC[j1 + i2] = BiomeMesa.field_185382_B.func_177226_a(BlockColored.field_176581_a, EnumDyeColor.WHITE);
                if (j1 + i2 > 1 && random.nextBoolean()) {
                    this.field_150621_aC[j1 + i2 - 1] = BiomeMesa.field_185382_B.func_177226_a(BlockColored.field_176581_a, EnumDyeColor.SILVER);
                }

                if (j1 + i2 < 63 && random.nextBoolean()) {
                    this.field_150621_aC[j1 + i2 + 1] = BiomeMesa.field_185382_B.func_177226_a(BlockColored.field_176581_a, EnumDyeColor.SILVER);
                }
            }
        }

    }

    private IBlockState func_180629_a(int i, int j, int k) {
        int l = (int) Math.round(this.field_150625_aG.func_151601_a(i / 512.0D, i / 512.0D) * 2.0D);

        return this.field_150621_aC[(j + l + 64) % 64];
    }

    class a extends BiomeDecorator {

        private a() {}

        @Override
        protected void func_76797_b(World world, Random random) {
            super.func_76797_b(world, random);
            if (world.paperConfig.disableMesaAdditionalGold) return; // Paper
            this.func_76795_a(world, random, 20, this.field_76819_m, 32, 80);
        }

        a(Object object) {
            this();
        }
    }
}

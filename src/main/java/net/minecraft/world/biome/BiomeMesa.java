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
import net.minecraft.server.BiomeMesa.a;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeMesa extends Biome {

    protected static final IBlockState COARSE_DIRT = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
    protected static final IBlockState GRASS = Blocks.GRASS.getDefaultState();
    protected static final IBlockState HARDENED_CLAY = Blocks.HARDENED_CLAY.getDefaultState();
    protected static final IBlockState STAINED_HARDENED_CLAY = Blocks.STAINED_HARDENED_CLAY.getDefaultState();
    protected static final IBlockState ORANGE_STAINED_HARDENED_CLAY = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
    protected static final IBlockState RED_SAND = Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
    private IBlockState[] clayBands;
    private long worldSeed;
    private NoiseGeneratorPerlin pillarNoise;
    private NoiseGeneratorPerlin pillarRoofNoise;
    private NoiseGeneratorPerlin clayBandsOffsetNoise;
    private final boolean brycePillars;
    private final boolean hasForest;

    public BiomeMesa(boolean flag, boolean flag1, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.brycePillars = flag;
        this.hasForest = flag1;
        this.spawnableCreatureList.clear();
        this.topBlock = BiomeMesa.RED_SAND;
        this.fillerBlock = BiomeMesa.STAINED_HARDENED_CLAY;
        this.decorator.treesPerChunk = -999;
        this.decorator.deadBushPerChunk = 20;
        this.decorator.reedsPerChunk = 3;
        this.decorator.cactiPerChunk = 5;
        this.decorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        if (flag1) {
            this.decorator.treesPerChunk = 5;
        }

    }

    protected BiomeDecorator createBiomeDecorator() {
        return new BiomeMesa.a(null);
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return BiomeMesa.TREE_FEATURE;
    }

    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        if (this.clayBands == null || this.worldSeed != world.getSeed()) {
            this.generateBands(world.getSeed());
        }

        if (this.pillarNoise == null || this.pillarRoofNoise == null || this.worldSeed != world.getSeed()) {
            Random random1 = new Random(this.worldSeed);

            this.pillarNoise = new NoiseGeneratorPerlin(random1, 4);
            this.pillarRoofNoise = new NoiseGeneratorPerlin(random1, 1);
        }

        this.worldSeed = world.getSeed();
        double d1 = 0.0D;
        int k;
        int l;

        if (this.brycePillars) {
            k = (i & -16) + (j & 15);
            l = (j & -16) + (i & 15);
            double d2 = Math.min(Math.abs(d0), this.pillarNoise.getValue((double) k * 0.25D, (double) l * 0.25D));

            if (d2 > 0.0D) {
                double d3 = 0.001953125D;
                double d4 = Math.abs(this.pillarRoofNoise.getValue((double) k * 0.001953125D, (double) l * 0.001953125D));

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
        int i1 = world.getSeaLevel();
        IBlockState iblockdata = BiomeMesa.STAINED_HARDENED_CLAY;
        IBlockState iblockdata1 = this.fillerBlock;
        int j1 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        boolean flag = Math.cos(d0 / 3.0D * 3.141592653589793D) > 0.0D;
        int k1 = -1;
        boolean flag1 = false;
        int l1 = 0;

        for (int i2 = 255; i2 >= 0; --i2) {
            if (chunksnapshot.getBlockState(l, i2, k).getMaterial() == Material.AIR && i2 < (int) d1) {
                chunksnapshot.setBlockState(l, i2, k, BiomeMesa.STONE);
            }

            if (i2 <= (world.paperConfig.generateFlatBedrock ? 0 : random.nextInt(5))) { // Paper - Configurable flat bedrock
                chunksnapshot.setBlockState(l, i2, k, BiomeMesa.BEDROCK);
            } else if (l1 < 15 || this.brycePillars) {
                IBlockState iblockdata2 = chunksnapshot.getBlockState(l, i2, k);

                if (iblockdata2.getMaterial() == Material.AIR) {
                    k1 = -1;
                } else if (iblockdata2.getBlock() == Blocks.STONE) {
                    if (k1 == -1) {
                        flag1 = false;
                        if (j1 <= 0) {
                            iblockdata = BiomeMesa.AIR;
                            iblockdata1 = BiomeMesa.STONE;
                        } else if (i2 >= i1 - 4 && i2 <= i1 + 1) {
                            iblockdata = BiomeMesa.STAINED_HARDENED_CLAY;
                            iblockdata1 = this.fillerBlock;
                        }

                        if (i2 < i1 && (iblockdata == null || iblockdata.getMaterial() == Material.AIR)) {
                            iblockdata = BiomeMesa.WATER;
                        }

                        k1 = j1 + Math.max(0, i2 - i1);
                        if (i2 >= i1 - 1) {
                            if (this.hasForest && i2 > 86 + j1 * 2) {
                                if (flag) {
                                    chunksnapshot.setBlockState(l, i2, k, BiomeMesa.COARSE_DIRT);
                                } else {
                                    chunksnapshot.setBlockState(l, i2, k, BiomeMesa.GRASS);
                                }
                            } else if (i2 > i1 + 3 + j1) {
                                IBlockState iblockdata3;

                                if (i2 >= 64 && i2 <= 127) {
                                    if (flag) {
                                        iblockdata3 = BiomeMesa.HARDENED_CLAY;
                                    } else {
                                        iblockdata3 = this.getBand(i, i2, j);
                                    }
                                } else {
                                    iblockdata3 = BiomeMesa.ORANGE_STAINED_HARDENED_CLAY;
                                }

                                chunksnapshot.setBlockState(l, i2, k, iblockdata3);
                            } else {
                                chunksnapshot.setBlockState(l, i2, k, this.topBlock);
                                flag1 = true;
                            }
                        } else {
                            chunksnapshot.setBlockState(l, i2, k, iblockdata1);
                            if (iblockdata1.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
                                chunksnapshot.setBlockState(l, i2, k, BiomeMesa.ORANGE_STAINED_HARDENED_CLAY);
                            }
                        }
                    } else if (k1 > 0) {
                        --k1;
                        if (flag1) {
                            chunksnapshot.setBlockState(l, i2, k, BiomeMesa.ORANGE_STAINED_HARDENED_CLAY);
                        } else {
                            chunksnapshot.setBlockState(l, i2, k, this.getBand(i, i2, j));
                        }
                    }

                    ++l1;
                }
            }
        }

    }

    private void generateBands(long i) {
        this.clayBands = new IBlockState[64];
        Arrays.fill(this.clayBands, BiomeMesa.HARDENED_CLAY);
        Random random = new Random(i);

        this.clayBandsOffsetNoise = new NoiseGeneratorPerlin(random, 1);

        int j;

        for (j = 0; j < 64; ++j) {
            j += random.nextInt(5) + 1;
            if (j < 64) {
                this.clayBands[j] = BiomeMesa.ORANGE_STAINED_HARDENED_CLAY;
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
                this.clayBands[i1 + j1] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
            }
        }

        k = random.nextInt(4) + 2;

        int k1;

        for (l = 0; l < k; ++l) {
            i1 = random.nextInt(3) + 2;
            j1 = random.nextInt(64);

            for (k1 = 0; j1 + k1 < 64 && k1 < i1; ++k1) {
                this.clayBands[j1 + k1] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
            }
        }

        l = random.nextInt(4) + 2;

        for (i1 = 0; i1 < l; ++i1) {
            j1 = random.nextInt(3) + 1;
            k1 = random.nextInt(64);

            for (int l1 = 0; k1 + l1 < 64 && l1 < j1; ++l1) {
                this.clayBands[k1 + l1] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.RED);
            }
        }

        i1 = random.nextInt(3) + 3;
        j1 = 0;

        for (k1 = 0; k1 < i1; ++k1) {
            boolean flag = true;

            j1 += random.nextInt(16) + 4;

            for (int i2 = 0; j1 + i2 < 64 && i2 < 1; ++i2) {
                this.clayBands[j1 + i2] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
                if (j1 + i2 > 1 && random.nextBoolean()) {
                    this.clayBands[j1 + i2 - 1] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }

                if (j1 + i2 < 63 && random.nextBoolean()) {
                    this.clayBands[j1 + i2 + 1] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
            }
        }

    }

    private IBlockState getBand(int i, int j, int k) {
        int l = (int) Math.round(this.clayBandsOffsetNoise.getValue((double) i / 512.0D, (double) i / 512.0D) * 2.0D);

        return this.clayBands[(j + l + 64) % 64];
    }

    class a extends BiomeDecorator {

        private a() {}

        protected void generateOres(World world, Random random) {
            super.generateOres(world, random);
            if (world.paperConfig.disableMesaAdditionalGold) return; // Paper
            this.genStandardOre1(world, random, 20, this.goldGen, 32, 80);
        }

        a(Object object) {
            this();
        }
    }
}

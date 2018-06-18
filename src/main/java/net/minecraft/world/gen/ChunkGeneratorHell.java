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

    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState NETHERRACK = Blocks.NETHERRACK.getDefaultState();
    protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
    protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    protected static final IBlockState SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
    private final World world;
    private final boolean generateStructures;
    private final Random rand;
    private double[] slowsandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] depthBuffer = new double[256];
    private double[] buffer;
    private final NoiseGeneratorOctaves lperlinNoise1;
    private final NoiseGeneratorOctaves lperlinNoise2;
    private final NoiseGeneratorOctaves perlinNoise1;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public final NoiseGeneratorOctaves scaleNoise;
    public final NoiseGeneratorOctaves depthNoise;
    private final WorldGenFire fireFeature = new WorldGenFire();
    private final WorldGenGlowStone1 lightGemGen = new WorldGenGlowStone1();
    private final WorldGenGlowStone2 hellPortalGen = new WorldGenGlowStone2();
    private final WorldGenerator quartzGen;
    private final WorldGenerator magmaGen;
    private final WorldGenHellLava lavaTrapGen;
    private final WorldGenHellLava hellSpringGen;
    private final WorldGenBush brownMushroomFeature;
    private final WorldGenBush redMushroomFeature;
    private final MapGenNetherBridge genNetherBridge;
    private final MapGenBase genNetherCaves;
    double[] pnr;
    double[] ar;
    double[] br;
    double[] noiseData4;
    double[] dr;

    public ChunkGeneratorHell(World world, boolean flag, long i) {
        this.quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, BlockMatcher.forBlock(Blocks.NETHERRACK));
        this.magmaGen = new WorldGenMinable(Blocks.MAGMA.getDefaultState(), 33, BlockMatcher.forBlock(Blocks.NETHERRACK));
        this.lavaTrapGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, true);
        this.hellSpringGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, false);
        this.brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
        this.redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
        this.genNetherBridge = new MapGenNetherBridge();
        this.genNetherCaves = new MapGenCavesHell();
        this.world = world;
        this.generateStructures = flag;
        this.rand = new Random(i);
        this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        world.setSeaLevel(63);
    }

    public void prepareHeights(int i, int j, ChunkPrimer chunksnapshot) {
        boolean flag = true;
        int k = this.world.getSeaLevel() / 2 + 1;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;

        this.buffer = this.getHeights(this.buffer, i * 4, 0, j * 4, 5, 17, 5);

        for (int l = 0; l < 4; ++l) {
            for (int i1 = 0; i1 < 4; ++i1) {
                for (int j1 = 0; j1 < 16; ++j1) {
                    double d0 = 0.125D;
                    double d1 = this.buffer[((l + 0) * 5 + i1 + 0) * 17 + j1 + 0];
                    double d2 = this.buffer[((l + 0) * 5 + i1 + 1) * 17 + j1 + 0];
                    double d3 = this.buffer[((l + 1) * 5 + i1 + 0) * 17 + j1 + 0];
                    double d4 = this.buffer[((l + 1) * 5 + i1 + 1) * 17 + j1 + 0];
                    double d5 = (this.buffer[((l + 0) * 5 + i1 + 0) * 17 + j1 + 1] - d1) * 0.125D;
                    double d6 = (this.buffer[((l + 0) * 5 + i1 + 1) * 17 + j1 + 1] - d2) * 0.125D;
                    double d7 = (this.buffer[((l + 1) * 5 + i1 + 0) * 17 + j1 + 1] - d3) * 0.125D;
                    double d8 = (this.buffer[((l + 1) * 5 + i1 + 1) * 17 + j1 + 1] - d4) * 0.125D;

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
                                    iblockdata = ChunkGeneratorHell.LAVA;
                                }

                                if (d15 > 0.0D) {
                                    iblockdata = ChunkGeneratorHell.NETHERRACK;
                                }

                                int j2 = l1 + l * 4;
                                int k2 = k1 + j1 * 8;
                                int l2 = i2 + i1 * 4;

                                chunksnapshot.setBlockState(j2, k2, l2, iblockdata);
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

    public void buildSurfaces(int i, int j, ChunkPrimer chunksnapshot) {
        int k = this.world.getSeaLevel() + 1;
        double d0 = 0.03125D;

        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, i * 16, j * 16, 0, 16, 16, 1, 0.03125D, 0.03125D, 1.0D);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, i * 16, 109, j * 16, 16, 1, 16, 0.03125D, 1.0D, 0.03125D);
        this.depthBuffer = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.depthBuffer, i * 16, j * 16, 0, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                boolean flag = this.slowsandNoise[l + i1 * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
                boolean flag1 = this.gravelNoise[l + i1 * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
                int j1 = (int) (this.depthBuffer[l + i1 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int k1 = -1;
                IBlockState iblockdata = ChunkGeneratorHell.NETHERRACK;
                IBlockState iblockdata1 = ChunkGeneratorHell.NETHERRACK;

                for (int l1 = 127; l1 >= 0; --l1) {
                    // Paper start - Configurable flat bedrock worldgen
                    if (l1 < 127 - (world.paperConfig.generateFlatBedrock ? 0 : this.rand.nextInt(5)) &&
                            l1 > (world.paperConfig.generateFlatBedrock ? 0 : this.rand.nextInt(5))) {
                        // Paper end
                        IBlockState iblockdata2 = chunksnapshot.getBlockState(i1, l1, l);

                        if (iblockdata2.getBlock() != null && iblockdata2.getMaterial() != Material.AIR) {
                            if (iblockdata2.getBlock() == Blocks.NETHERRACK) {
                                if (k1 == -1) {
                                    if (j1 <= 0) {
                                        iblockdata = ChunkGeneratorHell.AIR;
                                        iblockdata1 = ChunkGeneratorHell.NETHERRACK;
                                    } else if (l1 >= k - 4 && l1 <= k + 1) {
                                        iblockdata = ChunkGeneratorHell.NETHERRACK;
                                        iblockdata1 = ChunkGeneratorHell.NETHERRACK;
                                        if (flag1) {
                                            iblockdata = ChunkGeneratorHell.GRAVEL;
                                            iblockdata1 = ChunkGeneratorHell.NETHERRACK;
                                        }

                                        if (flag) {
                                            iblockdata = ChunkGeneratorHell.SOUL_SAND;
                                            iblockdata1 = ChunkGeneratorHell.SOUL_SAND;
                                        }
                                    }

                                    if (l1 < k && (iblockdata == null || iblockdata.getMaterial() == Material.AIR)) {
                                        iblockdata = ChunkGeneratorHell.LAVA;
                                    }

                                    k1 = j1;
                                    if (l1 >= k - 1) {
                                        chunksnapshot.setBlockState(i1, l1, l, iblockdata);
                                    } else {
                                        chunksnapshot.setBlockState(i1, l1, l, iblockdata1);
                                    }
                                } else if (k1 > 0) {
                                    --k1;
                                    chunksnapshot.setBlockState(i1, l1, l, iblockdata1);
                                }
                            }
                        } else {
                            k1 = -1;
                        }
                    } else {
                        chunksnapshot.setBlockState(i1, l1, l, ChunkGeneratorHell.BEDROCK);
                    }
                }
            }
        }

    }

    public Chunk generateChunk(int i, int j) {
        this.rand.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        this.prepareHeights(i, j, chunksnapshot);
        this.buildSurfaces(i, j, chunksnapshot);
        this.genNetherCaves.generate(this.world, i, j, chunksnapshot);
        if (this.generateStructures) {
            this.genNetherBridge.generate(this.world, i, j, chunksnapshot);
        }

        Chunk chunk = new Chunk(this.world, chunksnapshot, i, j);
        Biome[] abiomebase = this.world.getBiomeProvider().getBiomes((Biome[]) null, i * 16, j * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.getIdForBiome(abiomebase[k]);
        }

        chunk.resetRelightChecks();
        return chunk;
    }

    private double[] getHeights(double[] adouble, int i, int j, int k, int l, int i1, int j1) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        }

        double d0 = 684.412D;
        double d1 = 2053.236D;

        this.noiseData4 = this.scaleNoise.generateNoiseOctaves(this.noiseData4, i, j, k, l, 1, j1, 1.0D, 0.0D, 1.0D);
        this.dr = this.depthNoise.generateNoiseOctaves(this.dr, i, j, k, l, 1, j1, 100.0D, 0.0D, 100.0D);
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, i, j, k, l, i1, j1, 8.555150000000001D, 34.2206D, 8.555150000000001D);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, i, j, k, l, i1, j1, 684.412D, 2053.236D, 684.412D);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, i, j, k, l, i1, j1, 684.412D, 2053.236D, 684.412D);
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
                    double d5 = this.ar[k1] / 512.0D;
                    double d6 = this.br[k1] / 512.0D;
                    double d7 = (this.pnr[k1] / 10.0D + 1.0D) / 2.0D;
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
                        d9 = MathHelper.clamp(d9, 0.0D, 1.0D);
                        d8 = d8 * (1.0D - d9) + -10.0D * d9;
                    }

                    adouble[k1] = d8;
                    ++k1;
                }
            }
        }

        return adouble;
    }

    public void populate(int i, int j) {
        BlockFalling.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        BlockPos blockposition = new BlockPos(k, 0, l);
        Biome biomebase = this.world.getBiome(blockposition.add(16, 0, 16));
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);

        this.genNetherBridge.generateStructure(this.world, this.rand, chunkcoordintpair);

        int i1;

        for (i1 = 0; i1 < 8; ++i1) {
            this.hellSpringGen.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < this.rand.nextInt(this.rand.nextInt(10) + 1) + 1; ++i1) {
            this.fireFeature.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < this.rand.nextInt(this.rand.nextInt(10) + 1); ++i1) {
            this.lightGemGen.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < 10; ++i1) {
            this.hellPortalGen.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }

        if (this.rand.nextBoolean()) {
            this.brownMushroomFeature.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }

        if (this.rand.nextBoolean()) {
            this.redMushroomFeature.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }

        for (i1 = 0; i1 < 16; ++i1) {
            this.quartzGen.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
        }

        i1 = this.world.getSeaLevel() / 2 + 1;

        int j1;

        for (j1 = 0; j1 < 4; ++j1) {
            this.magmaGen.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16), i1 - 5 + this.rand.nextInt(10), this.rand.nextInt(16)));
        }

        for (j1 = 0; j1 < 16; ++j1) {
            this.lavaTrapGen.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
        }

        biomebase.decorate(this.world, this.rand, new BlockPos(k, 0, l));
        BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        if (enumcreaturetype == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.isInsideStructure(blockposition)) {
                return this.genNetherBridge.getSpawnList();
            }

            if (this.genNetherBridge.isPositionInStructure(this.world, blockposition) && this.world.getBlockState(blockposition.down()).getBlock() == Blocks.NETHER_BRICK) {
                return this.genNetherBridge.getSpawnList();
            }
        }

        Biome biomebase = this.world.getBiome(blockposition);

        return biomebase.getSpawnableList(enumcreaturetype);
    }

    @Nullable
    public BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag) {
        return "Fortress".equals(s) && this.genNetherBridge != null ? this.genNetherBridge.getNearestStructurePos(world, blockposition, flag) : null;
    }

    public boolean isInsideStructure(World world, String s, BlockPos blockposition) {
        return "Fortress".equals(s) && this.genNetherBridge != null ? this.genNetherBridge.isInsideStructure(blockposition) : false;
    }

    public void recreateStructures(Chunk chunk, int i, int j) {
        if (this.world.paperConfig.generateFortress) this.genNetherBridge.generate(this.world, i, j, (ChunkPrimer) null);
    }
}

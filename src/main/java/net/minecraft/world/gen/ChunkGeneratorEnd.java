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

    private final Random rand;
    protected static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private final NoiseGeneratorOctaves lperlinNoise1;
    private final NoiseGeneratorOctaves lperlinNoise2;
    private final NoiseGeneratorOctaves perlinNoise1;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    private final World world;
    private final boolean mapFeaturesEnabled;
    private final BlockPos spawnPoint;
    private final MapGenEndCity endCityGen = new MapGenEndCity(this);
    private final NoiseGeneratorSimplex islandNoise;
    private double[] buffer;
    private Biome[] biomesForGeneration;
    double[] pnr;
    double[] ar;
    double[] br;
    private final WorldGenEndIsland endIslands = new WorldGenEndIsland();

    public ChunkGeneratorEnd(World world, boolean flag, long i, BlockPos blockposition) {
        this.world = world;
        this.mapFeaturesEnabled = flag;
        this.spawnPoint = blockposition;
        this.rand = new Random(i);
        this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.islandNoise = new NoiseGeneratorSimplex(this.rand);
    }

    public void setBlocksInChunk(int i, int j, ChunkPrimer chunksnapshot) {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;

        this.buffer = this.getHeights(this.buffer, i * 2, 0, j * 2, 3, 33, 3);

        for (int k = 0; k < 2; ++k) {
            for (int l = 0; l < 2; ++l) {
                for (int i1 = 0; i1 < 32; ++i1) {
                    double d0 = 0.25D;
                    double d1 = this.buffer[((k + 0) * 3 + l + 0) * 33 + i1 + 0];
                    double d2 = this.buffer[((k + 0) * 3 + l + 1) * 33 + i1 + 0];
                    double d3 = this.buffer[((k + 1) * 3 + l + 0) * 33 + i1 + 0];
                    double d4 = this.buffer[((k + 1) * 3 + l + 1) * 33 + i1 + 0];
                    double d5 = (this.buffer[((k + 0) * 3 + l + 0) * 33 + i1 + 1] - d1) * 0.25D;
                    double d6 = (this.buffer[((k + 0) * 3 + l + 1) * 33 + i1 + 1] - d2) * 0.25D;
                    double d7 = (this.buffer[((k + 1) * 3 + l + 0) * 33 + i1 + 1] - d3) * 0.25D;
                    double d8 = (this.buffer[((k + 1) * 3 + l + 1) * 33 + i1 + 1] - d4) * 0.25D;

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
                                IBlockState iblockdata = ChunkGeneratorEnd.AIR;

                                if (d15 > 0.0D) {
                                    iblockdata = ChunkGeneratorEnd.END_STONE;
                                }

                                int i2 = k1 + k * 8;
                                int j2 = j1 + i1 * 4;
                                int k2 = l1 + l * 8;

                                chunksnapshot.setBlockState(i2, j2, k2, iblockdata);
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

    public void buildSurfaces(ChunkPrimer chunksnapshot) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                boolean flag = true;
                int k = -1;
                IBlockState iblockdata = ChunkGeneratorEnd.END_STONE;
                IBlockState iblockdata1 = ChunkGeneratorEnd.END_STONE;

                for (int l = 127; l >= 0; --l) {
                    IBlockState iblockdata2 = chunksnapshot.getBlockState(i, l, j);

                    if (iblockdata2.getMaterial() == Material.AIR) {
                        k = -1;
                    } else if (iblockdata2.getBlock() == Blocks.STONE) {
                        if (k == -1) {
                            k = 1;
                            if (l >= 0) {
                                chunksnapshot.setBlockState(i, l, j, iblockdata);
                            } else {
                                chunksnapshot.setBlockState(i, l, j, iblockdata1);
                            }
                        } else if (k > 0) {
                            --k;
                            chunksnapshot.setBlockState(i, l, j, iblockdata1);
                        }
                    }
                }
            }
        }

    }

    public Chunk generateChunk(int i, int j) {
        this.rand.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, i * 16, j * 16, 16, 16);
        this.setBlocksInChunk(i, j, chunksnapshot);
        this.buildSurfaces(chunksnapshot);
        if (this.mapFeaturesEnabled) {
            this.endCityGen.generate(this.world, i, j, chunksnapshot);
        }

        Chunk chunk = new Chunk(this.world, chunksnapshot, i, j);
        byte[] abyte = chunk.getBiomeArray();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.getIdForBiome(this.biomesForGeneration[k]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private float getIslandHeightValue(int i, int j, int k, int l) {
        float f = (float) (i * 2 + k);
        float f1 = (float) (j * 2 + l);
        float f2 = 100.0F - MathHelper.sqrt(f * f + f1 * f1) * 8.0F;

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

                if (k1 * k1 + l1 * l1 > 4096L && this.islandNoise.getValue((double) k1, (double) l1) < -0.8999999761581421D) {
                    float f3 = (MathHelper.abs((float) k1) * 3439.0F + MathHelper.abs((float) l1) * 147.0F) % 13.0F + 9.0F;

                    f = (float) (k - i1 * 2);
                    f1 = (float) (l - j1 * 2);
                    float f4 = 100.0F - MathHelper.sqrt(f * f + f1 * f1) * f3;

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

    public boolean isIslandChunk(int i, int j) {
        return (long) i * (long) i + (long) j * (long) j > 4096L && this.getIslandHeightValue(i, j, 1, 1) >= 0.0F;
    }

    private double[] getHeights(double[] adouble, int i, int j, int k, int l, int i1, int j1) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;

        d0 *= 2.0D;
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, i, j, k, l, i1, j1, d0 / 80.0D, 4.277575000000001D, d0 / 80.0D);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, i, j, k, l, i1, j1, d0, 684.412D, d0);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, i, j, k, l, i1, j1, d0, 684.412D, d0);
        int k1 = i / 2;
        int l1 = k / 2;
        int i2 = 0;

        for (int j2 = 0; j2 < l; ++j2) {
            for (int k2 = 0; k2 < j1; ++k2) {
                float f = this.getIslandHeightValue(k1, l1, j2, k2);

                for (int l2 = 0; l2 < i1; ++l2) {
                    double d2 = this.ar[i2] / 512.0D;
                    double d3 = this.br[i2] / 512.0D;
                    double d4 = (this.pnr[i2] / 10.0D + 1.0D) / 2.0D;
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
                        d6 = MathHelper.clamp(d6, 0.0D, 1.0D);
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

    public void populate(int i, int j) {
        BlockFalling.fallInstantly = true;
        BlockPos blockposition = new BlockPos(i * 16, 0, j * 16);

        if (this.mapFeaturesEnabled) {
            this.endCityGen.generateStructure(this.world, this.rand, new ChunkPos(i, j));
        }

        this.world.getBiome(blockposition.add(16, 0, 16)).decorate(this.world, this.world.rand, blockposition);
        long k = (long) i * (long) i + (long) j * (long) j;

        if (k > 4096L) {
            float f = this.getIslandHeightValue(i, j, 1, 1);

            if (f < -20.0F && this.rand.nextInt(14) == 0) {
                this.endIslands.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
                if (this.rand.nextInt(4) == 0) {
                    this.endIslands.generate(this.world, this.rand, blockposition.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
                }
            }

            if (this.getIslandHeightValue(i, j, 1, 1) > 40.0F) {
                int l = this.rand.nextInt(5);

                int i1;
                int j1;
                int k1;
                int l1;

                for (i1 = 0; i1 < l; ++i1) {
                    j1 = this.rand.nextInt(16) + 8;
                    k1 = this.rand.nextInt(16) + 8;
                    l1 = this.world.getHeight(blockposition.add(j1, 0, k1)).getY();
                    if (l1 > 0) {
                        int i2 = l1 - 1;

                        if (this.world.isAirBlock(blockposition.add(j1, i2 + 1, k1)) && this.world.getBlockState(blockposition.add(j1, i2, k1)).getBlock() == Blocks.END_STONE) {
                            BlockChorusFlower.generatePlant(this.world, blockposition.add(j1, i2 + 1, k1), this.rand, 8);
                        }
                    }
                }

                if (this.rand.nextInt(700) == 0) {
                    i1 = this.rand.nextInt(16) + 8;
                    j1 = this.rand.nextInt(16) + 8;
                    k1 = this.world.getHeight(blockposition.add(i1, 0, j1)).getY();
                    if (k1 > 0) {
                        l1 = k1 + 3 + this.rand.nextInt(7);
                        BlockPos blockposition1 = blockposition.add(i1, l1, j1);

                        (new WorldGenEndGateway()).generate(this.world, this.rand, blockposition1);
                        TileEntity tileentity = this.world.getTileEntity(blockposition1);

                        if (tileentity instanceof TileEntityEndGateway) {
                            TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway) tileentity;

                            tileentityendgateway.setExactPosition(this.spawnPoint);
                        }
                    }
                }
            }
        }

        BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        return this.world.getBiome(blockposition).getSpawnableList(enumcreaturetype);
    }

    @Nullable
    public BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag) {
        return "EndCity".equals(s) && this.endCityGen != null ? this.endCityGen.getNearestStructurePos(world, blockposition, flag) : null;
    }

    public boolean isInsideStructure(World world, String s, BlockPos blockposition) {
        return "EndCity".equals(s) && this.endCityGen != null ? this.endCityGen.isInsideStructure(blockposition) : false;
    }

    public void recreateStructures(Chunk chunk, int i, int j) {}
}

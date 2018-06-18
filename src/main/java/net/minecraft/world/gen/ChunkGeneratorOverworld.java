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

    protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
    private final Random rand;
    private final NoiseGeneratorOctaves minLimitPerlinNoise;
    private final NoiseGeneratorOctaves maxLimitPerlinNoise;
    private final NoiseGeneratorOctaves mainPerlinNoise;
    private final NoiseGeneratorPerlin surfaceNoise;
    public NoiseGeneratorOctaves scaleNoise;
    public NoiseGeneratorOctaves depthNoise;
    public NoiseGeneratorOctaves forestNoise;
    private final World world;
    private final boolean mapFeaturesEnabled;
    private final WorldType terrainType;
    private final double[] heightMap;
    private final float[] biomeWeights;
    private ChunkGeneratorSettings settings;
    private IBlockState oceanBlock;
    private double[] depthBuffer;
    private final MapGenBase caveGenerator;
    private final MapGenStronghold strongholdGenerator;
    private final MapGenVillage villageGenerator;
    private final MapGenMineshaft mineshaftGenerator;
    private final MapGenScatteredFeature scatteredFeatureGenerator;
    private final MapGenBase ravineGenerator;
    private final StructureOceanMonument oceanMonumentGenerator;
    private final WoodlandMansion woodlandMansionGenerator;
    private Biome[] biomesForGeneration;
    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;

    public ChunkGeneratorOverworld(World world, long i, boolean flag, String s) {
        this.oceanBlock = Blocks.WATER.getDefaultState();
        this.depthBuffer = new double[256];
        this.caveGenerator = new MapGenCaves();
        this.strongholdGenerator = new MapGenStronghold();
        this.villageGenerator = new MapGenVillage();
        this.mineshaftGenerator = new MapGenMineshaft();
        this.scatteredFeatureGenerator = new MapGenScatteredFeature();
        this.ravineGenerator = new MapGenRavine();
        this.oceanMonumentGenerator = new StructureOceanMonument();
        this.woodlandMansionGenerator = new WoodlandMansion(this);
        this.world = world;
        this.mapFeaturesEnabled = flag;
        this.terrainType = world.getWorldInfo().getTerrainType();
        this.rand = new Random(i);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.heightMap = new double[825];
        this.biomeWeights = new float[25];

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                float f = 10.0F / MathHelper.sqrt((float) (j * j + k * k) + 0.2F);

                this.biomeWeights[j + 2 + (k + 2) * 5] = f;
            }
        }

        if (s != null) {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(s).build();
            this.oceanBlock = this.settings.useLavaOceans ? Blocks.LAVA.getDefaultState() : Blocks.WATER.getDefaultState();
            world.setSeaLevel(this.settings.seaLevel);
        }

    }

    public void setBlocksInChunk(int i, int j, ChunkPrimer chunksnapshot) {
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, i * 4 - 2, j * 4 - 2, 10, 10);
        this.generateHeightmap(i * 4, 0, j * 4);

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
                    double d1 = this.heightMap[k1 + k2];
                    double d2 = this.heightMap[l1 + k2];
                    double d3 = this.heightMap[i2 + k2];
                    double d4 = this.heightMap[j2 + k2];
                    double d5 = (this.heightMap[k1 + k2 + 1] - d1) * 0.125D;
                    double d6 = (this.heightMap[l1 + k2 + 1] - d2) * 0.125D;
                    double d7 = (this.heightMap[i2 + k2 + 1] - d3) * 0.125D;
                    double d8 = (this.heightMap[j2 + k2 + 1] - d4) * 0.125D;

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
                                    chunksnapshot.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, ChunkGeneratorOverworld.STONE);
                                } else if (k2 * 8 + l2 < this.settings.seaLevel) {
                                    chunksnapshot.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, this.oceanBlock);
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

    public void replaceBiomeBlocks(int i, int j, ChunkPrimer chunksnapshot, Biome[] abiomebase) {
        double d0 = 0.03125D;

        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double) (i * 16), (double) (j * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                Biome biomebase = abiomebase[l + k * 16];

                biomebase.genTerrainBlocks(this.world, this.rand, chunksnapshot, i * 16 + k, j * 16 + l, this.depthBuffer[l + k * 16]);
            }
        }

    }

    public Chunk generateChunk(int i, int j) {
        this.rand.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        this.setBlocksInChunk(i, j, chunksnapshot);
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, i * 16, j * 16, 16, 16);
        this.replaceBiomeBlocks(i, j, chunksnapshot, this.biomesForGeneration);
        if (this.settings.useCaves && this.world.paperConfig.generateCaves) { // Paper
            this.caveGenerator.generate(this.world, i, j, chunksnapshot);
        }

        if (this.settings.useRavines && this.world.paperConfig.generateCanyon) { // Paper
            this.ravineGenerator.generate(this.world, i, j, chunksnapshot);
        }

        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts && this.world.paperConfig.generateMineshaft) { // Paper
                this.mineshaftGenerator.generate(this.world, i, j, chunksnapshot);
            }

            if (this.settings.useVillages&& this.world.paperConfig.generateVillage) { // Paper
                this.villageGenerator.generate(this.world, i, j, chunksnapshot);
            }

            if (this.settings.useStrongholds && this.world.paperConfig.generateStronghold) { // Paper
                this.strongholdGenerator.generate(this.world, i, j, chunksnapshot);
            }

            if (this.settings.useTemples && this.world.paperConfig.generateTemple) { // Paper
                this.scatteredFeatureGenerator.generate(this.world, i, j, chunksnapshot);
            }

            if (this.settings.useMonuments && this.world.paperConfig.generateMonument) { // Paper
                this.oceanMonumentGenerator.generate(this.world, i, j, chunksnapshot);
            }

            if (this.settings.useMansions) {
                this.woodlandMansionGenerator.generate(this.world, i, j, chunksnapshot);
            }
        }

        Chunk chunk = new Chunk(this.world, chunksnapshot, i, j);
        byte[] abyte = chunk.getBiomeArray();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.getIdForBiome(this.biomesForGeneration[k]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void generateHeightmap(int i, int j, int k) {
        this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, i, k, 5, 5, (double) this.settings.depthNoiseScaleX, (double) this.settings.depthNoiseScaleZ, (double) this.settings.depthNoiseScaleExponent);
        float f = this.settings.coordinateScale;
        float f1 = this.settings.heightScale;

        this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, i, j, k, 5, 33, 5, (double) (f / this.settings.mainNoiseScaleX), (double) (f1 / this.settings.mainNoiseScaleY), (double) (f / this.settings.mainNoiseScaleZ));
        this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, i, j, k, 5, 33, 5, (double) f, (double) f1, (double) f);
        this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, i, j, k, 5, 33, 5, (double) f, (double) f1, (double) f);
        int l = 0;
        int i1 = 0;

        for (int j1 = 0; j1 < 5; ++j1) {
            for (int k1 = 0; k1 < 5; ++k1) {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                boolean flag = true;
                Biome biomebase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

                for (int l1 = -2; l1 <= 2; ++l1) {
                    for (int i2 = -2; i2 <= 2; ++i2) {
                        Biome biomebase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
                        float f5 = this.settings.biomeDepthOffSet + biomebase1.getBaseHeight() * this.settings.biomeDepthWeight;
                        float f6 = this.settings.biomeScaleOffset + biomebase1.getHeightVariation() * this.settings.biomeScaleWeight;

                        if (this.terrainType == WorldType.AMPLIFIED && f5 > 0.0F) {
                            f5 = 1.0F + f5 * 2.0F;
                            f6 = 1.0F + f6 * 4.0F;
                        }
                        // CraftBukkit start - fix MC-54738
                        if (f5 < -1.8F) {
                            f5 = -1.8F;
                        }
                        // CraftBukkit end

                        float f7 = this.biomeWeights[l1 + 2 + (i2 + 2) * 5] / (f5 + 2.0F);

                        if (biomebase1.getBaseHeight() > biomebase.getBaseHeight()) {
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
                double d0 = this.depthRegion[i1] / 8000.0D;

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
                d1 = d1 * (double) this.settings.baseSize / 8.0D;
                double d3 = (double) this.settings.baseSize + d1 * 4.0D;

                for (int j2 = 0; j2 < 33; ++j2) {
                    double d4 = ((double) j2 - d3) * (double) this.settings.stretchY * 128.0D / 256.0D / d2;

                    if (d4 < 0.0D) {
                        d4 *= 4.0D;
                    }

                    double d5 = this.minLimitRegion[l] / (double) this.settings.lowerLimitScale;
                    double d6 = this.maxLimitRegion[l] / (double) this.settings.upperLimitScale;
                    double d7 = (this.mainNoiseRegion[l] / 10.0D + 1.0D) / 2.0D;
                    double d8 = MathHelper.clampedLerp(d5, d6, d7) - d4;

                    if (j2 > 29) {
                        double d9 = (double) ((float) (j2 - 29) / 3.0F);

                        d8 = d8 * (1.0D - d9) + -10.0D * d9;
                    }

                    this.heightMap[l] = d8;
                    ++l;
                }
            }
        }

    }

    public void populate(int i, int j) {
        BlockFalling.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        BlockPos blockposition = new BlockPos(k, 0, l);
        Biome biomebase = this.world.getBiome(blockposition.add(16, 0, 16));

        this.rand.setSeed(this.world.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;

        this.rand.setSeed((long) i * i1 + (long) j * j1 ^ this.world.getSeed());
        boolean flag = false;
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);

        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts && this.world.paperConfig.generateMineshaft) { // Paper
                this.mineshaftGenerator.generateStructure(this.world, this.rand, chunkcoordintpair);
            }

            if (this.settings.useVillages && this.world.paperConfig.generateVillage) { // Paper
                flag = this.villageGenerator.generateStructure(this.world, this.rand, chunkcoordintpair);
            }

            if (this.settings.useStrongholds && this.world.paperConfig.generateStronghold) { // Paper
                this.strongholdGenerator.generateStructure(this.world, this.rand, chunkcoordintpair);
            }

            if (this.settings.useTemples && this.world.paperConfig.generateTemple) { // Paper
                this.scatteredFeatureGenerator.generateStructure(this.world, this.rand, chunkcoordintpair);
            }

            if (this.settings.useMonuments && this.world.paperConfig.generateMonument) { // Paper
                this.oceanMonumentGenerator.generateStructure(this.world, this.rand, chunkcoordintpair);
            }

            if (this.settings.useMansions) {
                this.woodlandMansionGenerator.generateStructure(this.world, this.rand, chunkcoordintpair);
            }
        }

        int k1;
        int l1;
        int i2;

        if (biomebase != Biomes.DESERT && biomebase != Biomes.DESERT_HILLS && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
            k1 = this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(256);
            i2 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.WATER)).generate(this.world, this.rand, blockposition.add(k1, l1, i2));
        }

        if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
            k1 = this.rand.nextInt(16) + 8;
            l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            i2 = this.rand.nextInt(16) + 8;
            if (l1 < this.world.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
                (new WorldGenLakes(Blocks.LAVA)).generate(this.world, this.rand, blockposition.add(k1, l1, i2));
            }
        }

        if (this.settings.useDungeons && this.world.paperConfig.generateDungeon) { // Paper
            for (k1 = 0; k1 < this.settings.dungeonChance; ++k1) {
                l1 = this.rand.nextInt(16) + 8;
                i2 = this.rand.nextInt(256);
                int j2 = this.rand.nextInt(16) + 8;

                (new WorldGenDungeons()).generate(this.world, this.rand, blockposition.add(l1, i2, j2));
            }
        }

        biomebase.decorate(this.world, this.rand, new BlockPos(k, 0, l));
        WorldEntitySpawner.performWorldGenSpawning(this.world, biomebase, k + 8, l + 8, 16, 16, this.rand);
        blockposition = blockposition.add(8, 0, 8);

        for (k1 = 0; k1 < 16; ++k1) {
            for (l1 = 0; l1 < 16; ++l1) {
                BlockPos blockposition1 = this.world.getPrecipitationHeight(blockposition.add(k1, 0, l1));
                BlockPos blockposition2 = blockposition1.down();

                if (this.world.canBlockFreezeWater(blockposition2)) {
                    this.world.setBlockState(blockposition2, Blocks.ICE.getDefaultState(), 2);
                }

                if (this.world.canSnowAt(blockposition1, true)) {
                    this.world.setBlockState(blockposition1, Blocks.SNOW_LAYER.getDefaultState(), 2);
                }
            }
        }

        BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunk, int i, int j) {
        boolean flag = false;

        if (this.settings.useMonuments && this.mapFeaturesEnabled && chunk.getInhabitedTime() < 3600L) {
            flag |= this.oceanMonumentGenerator.generateStructure(this.world, this.rand, new ChunkPos(i, j));
        }

        return flag;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        Biome biomebase = this.world.getBiome(blockposition);

        if (this.mapFeaturesEnabled) {
            if (enumcreaturetype == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.isSwampHut(blockposition)) {
                return this.scatteredFeatureGenerator.getMonsters();
            }

            if (enumcreaturetype == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.isPositionInStructure(this.world, blockposition)) {
                return this.oceanMonumentGenerator.getMonsters();
            }
        }

        return biomebase.getSpawnableList(enumcreaturetype);
    }

    public boolean isInsideStructure(World world, String s, BlockPos blockposition) {
        return !this.mapFeaturesEnabled ? false : ("Stronghold".equals(s) && this.strongholdGenerator != null ? this.strongholdGenerator.isInsideStructure(blockposition) : ("Mansion".equals(s) && this.woodlandMansionGenerator != null ? this.woodlandMansionGenerator.isInsideStructure(blockposition) : ("Monument".equals(s) && this.oceanMonumentGenerator != null ? this.oceanMonumentGenerator.isInsideStructure(blockposition) : ("Village".equals(s) && this.villageGenerator != null ? this.villageGenerator.isInsideStructure(blockposition) : ("Mineshaft".equals(s) && this.mineshaftGenerator != null ? this.mineshaftGenerator.isInsideStructure(blockposition) : ("Temple".equals(s) && this.scatteredFeatureGenerator != null ? this.scatteredFeatureGenerator.isInsideStructure(blockposition) : false))))));
    }

    @Nullable
    public BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag) {
        return !this.mapFeaturesEnabled ? null : ("Stronghold".equals(s) && this.strongholdGenerator != null ? this.strongholdGenerator.getNearestStructurePos(world, blockposition, flag) : ("Mansion".equals(s) && this.woodlandMansionGenerator != null ? this.woodlandMansionGenerator.getNearestStructurePos(world, blockposition, flag) : ("Monument".equals(s) && this.oceanMonumentGenerator != null ? this.oceanMonumentGenerator.getNearestStructurePos(world, blockposition, flag) : ("Village".equals(s) && this.villageGenerator != null ? this.villageGenerator.getNearestStructurePos(world, blockposition, flag) : ("Mineshaft".equals(s) && this.mineshaftGenerator != null ? this.mineshaftGenerator.getNearestStructurePos(world, blockposition, flag) : ("Temple".equals(s) && this.scatteredFeatureGenerator != null ? this.scatteredFeatureGenerator.getNearestStructurePos(world, blockposition, flag) : null))))));
    }

    public void recreateStructures(Chunk chunk, int i, int j) {
        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts && this.world.paperConfig.generateMineshaft) { // Paper
                this.mineshaftGenerator.generate(this.world, i, j, (ChunkPrimer) null);
            }

            if (this.settings.useVillages && this.world.paperConfig.generateVillage) { // Paper
                this.villageGenerator.generate(this.world, i, j, (ChunkPrimer) null);
            }

            if (this.settings.useStrongholds && this.world.paperConfig.generateStronghold) { // Paper
                this.strongholdGenerator.generate(this.world, i, j, (ChunkPrimer) null);
            }

            if (this.settings.useTemples && this.world.paperConfig.generateTemple) { // Paper
                this.scatteredFeatureGenerator.generate(this.world, i, j, (ChunkPrimer) null);
            }

            if (this.settings.useMonuments && this.world.paperConfig.generateMonument) { // Paper
                this.oceanMonumentGenerator.generate(this.world, i, j, (ChunkPrimer) null);
            }

            if (this.settings.useMansions) {
                this.woodlandMansionGenerator.generate(this.world, i, j, (ChunkPrimer) null);
            }
        }

    }
}

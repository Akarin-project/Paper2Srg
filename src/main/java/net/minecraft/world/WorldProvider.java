package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class WorldProvider {

    public static final float[] MOON_PHASE_FACTORS = new float[] { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
    protected World world;
    private WorldType terrainType;
    private String generatorSettings;
    protected BiomeProvider biomeProvider;
    protected boolean doesWaterVaporize;
    protected boolean nether;
    protected boolean hasSkyLight;
    protected final float[] lightBrightnessTable = new float[16];
    private final float[] colorsSunriseSunset = new float[4];

    public WorldProvider() {}

    public final void setWorld(World world) {
        this.world = world;
        this.terrainType = world.getWorldInfo().getTerrainType();
        this.generatorSettings = world.getWorldInfo().getGeneratorOptions();
        this.init();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable() {
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;

            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 1.0F + 0.0F;
        }

    }

    protected void init() {
        this.hasSkyLight = true;
        WorldType worldtype = this.world.getWorldInfo().getTerrainType();

        if (worldtype == WorldType.FLAT) {
            FlatGeneratorInfo worldgenflatinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.world.getWorldInfo().getGeneratorOptions());

            this.biomeProvider = new BiomeProviderSingle(Biome.getBiome(worldgenflatinfo.getBiome(), Biomes.DEFAULT));
        } else if (worldtype == WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
        } else {
            this.biomeProvider = new BiomeProvider(this.world.getWorldInfo());
        }

    }

    public IChunkGenerator createChunkGenerator() {
        return (IChunkGenerator) (this.terrainType == WorldType.FLAT ? new ChunkGeneratorFlat(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : (this.terrainType == WorldType.DEBUG_ALL_BLOCK_STATES ? new ChunkGeneratorDebug(this.world) : (this.terrainType == WorldType.CUSTOMIZED ? new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings))));
    }

    public boolean canCoordinateBeSpawn(int i, int j) {
        BlockPos blockposition = new BlockPos(i, 0, j);

        return this.world.getBiome(blockposition).ignorePlayerSpawnSuitability() ? true : this.world.getGroundAboveSeaLevel(blockposition).getBlock() == Blocks.GRASS;
    }

    public float calculateCelestialAngle(long i, float f) {
        int j = (int) (i % 24000L);
        float f1 = ((float) j + f) / 24000.0F - 0.25F;

        if (f1 < 0.0F) {
            ++f1;
        }

        if (f1 > 1.0F) {
            --f1;
        }

        float f2 = f1;

        f1 = 1.0F - (float) ((Math.cos((double) f1 * 3.141592653589793D) + 1.0D) / 2.0D);
        f1 = f2 + (f1 - f2) / 3.0F;
        return f1;
    }

    public int getMoonPhase(long i) {
        return (int) (i / 24000L % 8L + 8L) % 8;
    }

    public boolean isSurfaceWorld() {
        return true;
    }

    public boolean canRespawnHere() {
        return true;
    }

    @Nullable
    public BlockPos getSpawnCoordinate() {
        return null;
    }

    public int getAverageGroundLevel() {
        return this.terrainType == WorldType.FLAT ? 4 : this.world.getSeaLevel() + 1;
    }

    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }

    public boolean doesWaterVaporize() {
        return this.doesWaterVaporize;
    }

    public boolean hasSkyLight() {
        return this.hasSkyLight;
    }

    public final boolean isSkyMissing() { return this.isNether(); } // Paper - OBFHELPER
    public boolean isNether() {
        return this.nether;
    }

    public float[] getLightBrightnessTable() {
        return this.lightBrightnessTable;
    }

    public WorldBorder createWorldBorder() {
        return new WorldBorder();
    }

    public void onPlayerAdded(EntityPlayerMP entityplayer) {}

    public void onPlayerRemoved(EntityPlayerMP entityplayer) {}

    public abstract DimensionType getDimensionType();

    public void onWorldSave() {}

    public void onWorldUpdateEntities() {}

    public boolean canDropChunk(int i, int j) {
        return !this.world.shouldStayLoaded(i, j); // Paper - Use shouldStayLoaded check for all worlds
    }
}

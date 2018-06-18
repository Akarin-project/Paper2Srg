package net.minecraft.world;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;


public class WorldProviderHell extends WorldProvider {

    public WorldProviderHell() {}

    public void init() {
        this.biomeProvider = new BiomeProviderSingle(Biomes.HELL);
        this.doesWaterVaporize = true;
        this.nether = true;
    }

    protected void generateLightBrightnessTable() {
        float f = 0.1F;

        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;

            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.9F + 0.1F;
        }

    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorHell(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed());
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    public boolean canCoordinateBeSpawn(int i, int j) {
        return false;
    }

    public float calculateCelestialAngle(long i, float f) {
        return 0.5F;
    }

    public boolean canRespawnHere() {
        return false;
    }

    public WorldBorder createWorldBorder() {
        return new WorldBorder() {
            public double getCenterX() {
                return super.getCenterX(); // CraftBukkit
            }

            public double getCenterZ() {
                return super.getCenterZ(); // CraftBukkit
            }
        };
    }

    public DimensionType getDimensionType() {
        return DimensionType.NETHER;
    }
}

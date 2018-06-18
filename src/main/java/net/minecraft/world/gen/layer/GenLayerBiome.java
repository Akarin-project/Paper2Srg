package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;


public class GenLayerBiome extends GenLayer {

    private Biome[] warmBiomes;
    private final Biome[] mediumBiomes;
    private final Biome[] coldBiomes;
    private final Biome[] iceBiomes;
    private final ChunkGeneratorSettings settings;

    public GenLayerBiome(long i, GenLayer genlayer, WorldType worldtype, ChunkGeneratorSettings customworldsettingsfinal) {
        super(i);
        this.warmBiomes = new Biome[] { Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA, Biomes.PLAINS};
        this.mediumBiomes = new Biome[] { Biomes.FOREST, Biomes.ROOFED_FOREST, Biomes.EXTREME_HILLS, Biomes.PLAINS, Biomes.BIRCH_FOREST, Biomes.SWAMPLAND};
        this.coldBiomes = new Biome[] { Biomes.FOREST, Biomes.EXTREME_HILLS, Biomes.TAIGA, Biomes.PLAINS};
        this.iceBiomes = new Biome[] { Biomes.ICE_PLAINS, Biomes.ICE_PLAINS, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA};
        this.parent = genlayer;
        if (worldtype == WorldType.DEFAULT_1_1) {
            this.warmBiomes = new Biome[] { Biomes.DESERT, Biomes.FOREST, Biomes.EXTREME_HILLS, Biomes.SWAMPLAND, Biomes.PLAINS, Biomes.TAIGA};
            this.settings = null;
        } else {
            this.settings = customworldsettingsfinal;
        }

    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] aint = this.parent.getInts(i, j, k, l);
        int[] aint1 = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.initChunkSeed((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + i1 * k];
                int l1 = (k1 & 3840) >> 8;

                k1 &= -3841;
                if (this.settings != null && this.settings.fixedBiome >= 0) {
                    aint1[j1 + i1 * k] = this.settings.fixedBiome;
                } else if (isBiomeOceanic(k1)) {
                    aint1[j1 + i1 * k] = k1;
                } else if (k1 == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
                    aint1[j1 + i1 * k] = k1;
                } else if (k1 == 1) {
                    if (l1 > 0) {
                        if (this.nextInt(3) == 0) {
                            aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK);
                        } else {
                            aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.MESA_ROCK);
                        }
                    } else {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(this.warmBiomes[this.nextInt(this.warmBiomes.length)]);
                    }
                } else if (k1 == 2) {
                    if (l1 > 0) {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.JUNGLE);
                    } else {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(this.mediumBiomes[this.nextInt(this.mediumBiomes.length)]);
                    }
                } else if (k1 == 3) {
                    if (l1 > 0) {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.REDWOOD_TAIGA);
                    } else {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(this.coldBiomes[this.nextInt(this.coldBiomes.length)]);
                    }
                } else if (k1 == 4) {
                    aint1[j1 + i1 * k] = Biome.getIdForBiome(this.iceBiomes[this.nextInt(this.iceBiomes.length)]);
                } else {
                    aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
                }
            }
        }

        return aint1;
    }
}

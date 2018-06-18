package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;


public class GenLayerShore extends GenLayer {

    public GenLayerShore(long i, GenLayer genlayer) {
        super(i);
        this.parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] aint = this.parent.getInts(i - 1, j - 1, k + 2, l + 2);
        int[] aint1 = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.initChunkSeed((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + 1 + (i1 + 1) * (k + 2)];
                Biome biomebase = Biome.getBiome(k1);
                int l1;
                int i2;
                int j2;
                int k2;

                if (k1 == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
                    l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                    i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                    k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (l1 != Biome.getIdForBiome(Biomes.OCEAN) && i2 != Biome.getIdForBiome(Biomes.OCEAN) && j2 != Biome.getIdForBiome(Biomes.OCEAN) && k2 != Biome.getIdForBiome(Biomes.OCEAN)) {
                        aint1[j1 + i1 * k] = k1;
                    } else {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                    }
                } else if (biomebase != null && biomebase.getBiomeClass() == BiomeJungle.class) {
                    l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                    i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                    k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (this.isJungleCompatible(l1) && this.isJungleCompatible(i2) && this.isJungleCompatible(j2) && this.isJungleCompatible(k2)) {
                        if (!isBiomeOceanic(l1) && !isBiomeOceanic(i2) && !isBiomeOceanic(j2) && !isBiomeOceanic(k2)) {
                            aint1[j1 + i1 * k] = k1;
                        } else {
                            aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.BEACH);
                        }
                    } else {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                    }
                } else if (k1 != Biome.getIdForBiome(Biomes.EXTREME_HILLS) && k1 != Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES) && k1 != Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)) {
                    if (biomebase != null && biomebase.isSnowyBiome()) {
                        this.replaceIfNeighborOcean(aint, aint1, j1, i1, k, k1, Biome.getIdForBiome(Biomes.COLD_BEACH));
                    } else if (k1 != Biome.getIdForBiome(Biomes.MESA) && k1 != Biome.getIdForBiome(Biomes.MESA_ROCK)) {
                        if (k1 != Biome.getIdForBiome(Biomes.OCEAN) && k1 != Biome.getIdForBiome(Biomes.DEEP_OCEAN) && k1 != Biome.getIdForBiome(Biomes.RIVER) && k1 != Biome.getIdForBiome(Biomes.SWAMPLAND)) {
                            l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                            i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                            j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                            k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                            if (!isBiomeOceanic(l1) && !isBiomeOceanic(i2) && !isBiomeOceanic(j2) && !isBiomeOceanic(k2)) {
                                aint1[j1 + i1 * k] = k1;
                            } else {
                                aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.BEACH);
                            }
                        } else {
                            aint1[j1 + i1 * k] = k1;
                        }
                    } else {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        if (!isBiomeOceanic(l1) && !isBiomeOceanic(i2) && !isBiomeOceanic(j2) && !isBiomeOceanic(k2)) {
                            if (this.isMesa(l1) && this.isMesa(i2) && this.isMesa(j2) && this.isMesa(k2)) {
                                aint1[j1 + i1 * k] = k1;
                            } else {
                                aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.DESERT);
                            }
                        } else {
                            aint1[j1 + i1 * k] = k1;
                        }
                    }
                } else {
                    this.replaceIfNeighborOcean(aint, aint1, j1, i1, k, k1, Biome.getIdForBiome(Biomes.STONE_BEACH));
                }
            }
        }

        return aint1;
    }

    private void replaceIfNeighborOcean(int[] aint, int[] aint1, int i, int j, int k, int l, int i1) {
        if (isBiomeOceanic(l)) {
            aint1[i + j * k] = l;
        } else {
            int j1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int k1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int l1 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (!isBiomeOceanic(j1) && !isBiomeOceanic(k1) && !isBiomeOceanic(l1) && !isBiomeOceanic(i2)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = i1;
            }

        }
    }

    private boolean isJungleCompatible(int i) {
        return Biome.getBiome(i) != null && Biome.getBiome(i).getBiomeClass() == BiomeJungle.class ? true : i == Biome.getIdForBiome(Biomes.JUNGLE_EDGE) || i == Biome.getIdForBiome(Biomes.JUNGLE) || i == Biome.getIdForBiome(Biomes.JUNGLE_HILLS) || i == Biome.getIdForBiome(Biomes.FOREST) || i == Biome.getIdForBiome(Biomes.TAIGA) || isBiomeOceanic(i);
    }

    private boolean isMesa(int i) {
        return Biome.getBiome(i) instanceof BiomeMesa;
    }
}

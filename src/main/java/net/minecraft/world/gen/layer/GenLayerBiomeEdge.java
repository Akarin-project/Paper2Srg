package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;


public class GenLayerBiomeEdge extends GenLayer {

    public GenLayerBiomeEdge(long i, GenLayer genlayer) {
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

                if (!this.replaceBiomeEdgeIfNecessary(aint, aint1, j1, i1, k, k1, Biome.getIdForBiome(Biomes.EXTREME_HILLS), Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)) && !this.replaceBiomeEdge(aint, aint1, j1, i1, k, k1, Biome.getIdForBiome(Biomes.MESA_ROCK), Biome.getIdForBiome(Biomes.MESA)) && !this.replaceBiomeEdge(aint, aint1, j1, i1, k, k1, Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK), Biome.getIdForBiome(Biomes.MESA)) && !this.replaceBiomeEdge(aint, aint1, j1, i1, k, k1, Biome.getIdForBiome(Biomes.REDWOOD_TAIGA), Biome.getIdForBiome(Biomes.TAIGA))) {
                    int l1;
                    int i2;
                    int j2;
                    int k2;

                    if (k1 == Biome.getIdForBiome(Biomes.DESERT)) {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        if (l1 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && i2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && j2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && k2 != Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                            aint1[j1 + i1 * k] = k1;
                        } else {
                            aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES);
                        }
                    } else if (k1 == Biome.getIdForBiome(Biomes.SWAMPLAND)) {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        if (l1 != Biome.getIdForBiome(Biomes.DESERT) && i2 != Biome.getIdForBiome(Biomes.DESERT) && j2 != Biome.getIdForBiome(Biomes.DESERT) && k2 != Biome.getIdForBiome(Biomes.DESERT) && l1 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && i2 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && j2 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && k2 != Biome.getIdForBiome(Biomes.COLD_TAIGA) && l1 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && i2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && j2 != Biome.getIdForBiome(Biomes.ICE_PLAINS) && k2 != Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                            if (l1 != Biome.getIdForBiome(Biomes.JUNGLE) && k2 != Biome.getIdForBiome(Biomes.JUNGLE) && i2 != Biome.getIdForBiome(Biomes.JUNGLE) && j2 != Biome.getIdForBiome(Biomes.JUNGLE)) {
                                aint1[j1 + i1 * k] = k1;
                            } else {
                                aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                            }
                        } else {
                            aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.PLAINS);
                        }
                    } else {
                        aint1[j1 + i1 * k] = k1;
                    }
                }
            }
        }

        return aint1;
    }

    private boolean replaceBiomeEdgeIfNecessary(int[] aint, int[] aint1, int i, int j, int k, int l, int i1, int j1) {
        if (!biomesEqualOrMesaPlateau(l, i1)) {
            return false;
        } else {
            int k1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int l1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int j2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (this.canBiomesBeNeighbors(k1, i1) && this.canBiomesBeNeighbors(l1, i1) && this.canBiomesBeNeighbors(i2, i1) && this.canBiomesBeNeighbors(j2, i1)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = j1;
            }

            return true;
        }
    }

    private boolean replaceBiomeEdge(int[] aint, int[] aint1, int i, int j, int k, int l, int i1, int j1) {
        if (l != i1) {
            return false;
        } else {
            int k1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int l1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int j2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (biomesEqualOrMesaPlateau(k1, i1) && biomesEqualOrMesaPlateau(l1, i1) && biomesEqualOrMesaPlateau(i2, i1) && biomesEqualOrMesaPlateau(j2, i1)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = j1;
            }

            return true;
        }
    }

    private boolean canBiomesBeNeighbors(int i, int j) {
        if (biomesEqualOrMesaPlateau(i, j)) {
            return true;
        } else {
            Biome biomebase = Biome.getBiome(i);
            Biome biomebase1 = Biome.getBiome(j);

            if (biomebase != null && biomebase1 != null) {
                Biome.TempCategory biomebase_enumtemperature = biomebase.getTempCategory();
                Biome.TempCategory biomebase_enumtemperature1 = biomebase1.getTempCategory();

                return biomebase_enumtemperature == biomebase_enumtemperature1 || biomebase_enumtemperature == Biome.TempCategory.MEDIUM || biomebase_enumtemperature1 == Biome.TempCategory.MEDIUM;
            } else {
                return false;
            }
        }
    }
}

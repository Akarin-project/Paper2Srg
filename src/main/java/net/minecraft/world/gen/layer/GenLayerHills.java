package net.minecraft.world.gen.layer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerHills extends GenLayer {

    private static final Logger LOGGER = LogManager.getLogger();
    private final GenLayer riverLayer;

    public GenLayerHills(long i, GenLayer genlayer, GenLayer genlayer1) {
        super(i);
        this.parent = genlayer;
        this.riverLayer = genlayer1;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] aint = this.parent.getInts(i - 1, j - 1, k + 2, l + 2);
        int[] aint1 = this.riverLayer.getInts(i - 1, j - 1, k + 2, l + 2);
        int[] aint2 = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.initChunkSeed((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + 1 + (i1 + 1) * (k + 2)];
                int l1 = aint1[j1 + 1 + (i1 + 1) * (k + 2)];
                boolean flag = (l1 - 2) % 29 == 0;

                if (k1 > 255) {
                    GenLayerHills.LOGGER.debug("old! {}", Integer.valueOf(k1));
                }

                Biome biomebase = Biome.getBiomeForId(k1);
                boolean flag1 = biomebase != null && biomebase.isMutation();
                Biome biomebase1;

                if (k1 != 0 && l1 >= 2 && (l1 - 2) % 29 == 1 && !flag1) {
                    biomebase1 = Biome.getMutationForBiome(biomebase);
                    aint2[j1 + i1 * k] = biomebase1 == null ? k1 : Biome.getIdForBiome(biomebase1);
                } else if (this.nextInt(3) != 0 && !flag) {
                    aint2[j1 + i1 * k] = k1;
                } else {
                    biomebase1 = biomebase;
                    int i2;

                    if (biomebase == Biomes.DESERT) {
                        biomebase1 = Biomes.DESERT_HILLS;
                    } else if (biomebase == Biomes.FOREST) {
                        biomebase1 = Biomes.FOREST_HILLS;
                    } else if (biomebase == Biomes.BIRCH_FOREST) {
                        biomebase1 = Biomes.BIRCH_FOREST_HILLS;
                    } else if (biomebase == Biomes.ROOFED_FOREST) {
                        biomebase1 = Biomes.PLAINS;
                    } else if (biomebase == Biomes.TAIGA) {
                        biomebase1 = Biomes.TAIGA_HILLS;
                    } else if (biomebase == Biomes.REDWOOD_TAIGA) {
                        biomebase1 = Biomes.REDWOOD_TAIGA_HILLS;
                    } else if (biomebase == Biomes.COLD_TAIGA) {
                        biomebase1 = Biomes.COLD_TAIGA_HILLS;
                    } else if (biomebase == Biomes.PLAINS) {
                        if (this.nextInt(3) == 0) {
                            biomebase1 = Biomes.FOREST_HILLS;
                        } else {
                            biomebase1 = Biomes.FOREST;
                        }
                    } else if (biomebase == Biomes.ICE_PLAINS) {
                        biomebase1 = Biomes.ICE_MOUNTAINS;
                    } else if (biomebase == Biomes.JUNGLE) {
                        biomebase1 = Biomes.JUNGLE_HILLS;
                    } else if (biomebase == Biomes.OCEAN) {
                        biomebase1 = Biomes.DEEP_OCEAN;
                    } else if (biomebase == Biomes.EXTREME_HILLS) {
                        biomebase1 = Biomes.EXTREME_HILLS_WITH_TREES;
                    } else if (biomebase == Biomes.SAVANNA) {
                        biomebase1 = Biomes.SAVANNA_PLATEAU;
                    } else if (biomesEqualOrMesaPlateau(k1, Biome.getIdForBiome(Biomes.MESA_ROCK))) {
                        biomebase1 = Biomes.MESA;
                    } else if (biomebase == Biomes.DEEP_OCEAN && this.nextInt(3) == 0) {
                        i2 = this.nextInt(2);
                        if (i2 == 0) {
                            biomebase1 = Biomes.PLAINS;
                        } else {
                            biomebase1 = Biomes.FOREST;
                        }
                    }

                    i2 = Biome.getIdForBiome(biomebase1);
                    if (flag && i2 != k1) {
                        Biome biomebase2 = Biome.getMutationForBiome(biomebase1);

                        i2 = biomebase2 == null ? k1 : Biome.getIdForBiome(biomebase2);
                    }

                    if (i2 == k1) {
                        aint2[j1 + i1 * k] = k1;
                    } else {
                        int j2 = aint[j1 + 1 + (i1 + 0) * (k + 2)];
                        int k2 = aint[j1 + 2 + (i1 + 1) * (k + 2)];
                        int l2 = aint[j1 + 0 + (i1 + 1) * (k + 2)];
                        int i3 = aint[j1 + 1 + (i1 + 2) * (k + 2)];
                        int j3 = 0;

                        if (biomesEqualOrMesaPlateau(j2, k1)) {
                            ++j3;
                        }

                        if (biomesEqualOrMesaPlateau(k2, k1)) {
                            ++j3;
                        }

                        if (biomesEqualOrMesaPlateau(l2, k1)) {
                            ++j3;
                        }

                        if (biomesEqualOrMesaPlateau(i3, k1)) {
                            ++j3;
                        }

                        if (j3 >= 3) {
                            aint2[j1 + i1 * k] = i2;
                        } else {
                            aint2[j1 + i1 * k] = k1;
                        }
                    }
                }
            }
        }

        return aint2;
    }
}

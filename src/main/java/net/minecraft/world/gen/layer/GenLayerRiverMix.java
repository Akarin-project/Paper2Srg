package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;


public class GenLayerRiverMix extends GenLayer {

    private final GenLayer biomePatternGeneratorChain;
    private final GenLayer riverPatternGeneratorChain;

    public GenLayerRiverMix(long i, GenLayer genlayer, GenLayer genlayer1) {
        super(i);
        this.biomePatternGeneratorChain = genlayer;
        this.riverPatternGeneratorChain = genlayer1;
    }

    public void initWorldGenSeed(long i) {
        this.biomePatternGeneratorChain.initWorldGenSeed(i);
        this.riverPatternGeneratorChain.initWorldGenSeed(i);
        super.initWorldGenSeed(i);
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] aint = this.biomePatternGeneratorChain.getInts(i, j, k, l);
        int[] aint1 = this.riverPatternGeneratorChain.getInts(i, j, k, l);
        int[] aint2 = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < k * l; ++i1) {
            if (aint[i1] != Biome.getIdForBiome(Biomes.OCEAN) && aint[i1] != Biome.getIdForBiome(Biomes.DEEP_OCEAN)) {
                if (aint1[i1] == Biome.getIdForBiome(Biomes.RIVER)) {
                    if (aint[i1] == Biome.getIdForBiome(Biomes.ICE_PLAINS)) {
                        aint2[i1] = Biome.getIdForBiome(Biomes.FROZEN_RIVER);
                    } else if (aint[i1] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND) && aint[i1] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE)) {
                        aint2[i1] = aint1[i1] & 255;
                    } else {
                        aint2[i1] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                    }
                } else {
                    aint2[i1] = aint[i1];
                }
            } else {
                aint2[i1] = aint[i1];
            }
        }

        return aint2;
    }
}

package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;


public class GenLayerRareBiome extends GenLayer {

    public GenLayerRareBiome(long i, GenLayer genlayer) {
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

                if (this.nextInt(57) == 0) {
                    if (k1 == Biome.getIdForBiome(Biomes.PLAINS)) {
                        aint1[j1 + i1 * k] = Biome.getIdForBiome(Biomes.MUTATED_PLAINS);
                    } else {
                        aint1[j1 + i1 * k] = k1;
                    }
                } else {
                    aint1[j1 + i1 * k] = k1;
                }
            }
        }

        return aint1;
    }
}

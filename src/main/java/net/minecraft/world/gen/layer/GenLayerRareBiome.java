package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;


public class GenLayerRareBiome extends GenLayer {

    public GenLayerRareBiome(long i, GenLayer genlayer) {
        super(i);
        this.field_75909_a = genlayer;
    }

    public int[] func_75904_a(int i, int j, int k, int l) {
        int[] aint = this.field_75909_a.func_75904_a(i - 1, j - 1, k + 2, l + 2);
        int[] aint1 = IntCache.func_76445_a(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.func_75903_a((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + 1 + (i1 + 1) * (k + 2)];

                if (this.func_75902_a(57) == 0) {
                    if (k1 == Biome.func_185362_a(Biomes.field_76772_c)) {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_185441_Q);
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

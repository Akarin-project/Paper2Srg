package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer {

    public GenLayerRiverInit(long i, GenLayer genlayer) {
        super(i);
        this.parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] aint = this.parent.getInts(i, j, k, l);
        int[] aint1 = IntCache.getIntCache(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.initChunkSeed((long) (j1 + i), (long) (i1 + j));
                aint1[j1 + i1 * k] = aint[j1 + i1 * k] > 0 ? this.nextInt(299999) + 2 : 0;
            }
        }

        return aint1;
    }
}

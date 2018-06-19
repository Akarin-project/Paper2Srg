package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer {

    public GenLayerRiverInit(long i, GenLayer genlayer) {
        super(i);
        this.field_75909_a = genlayer;
    }

    public int[] func_75904_a(int i, int j, int k, int l) {
        int[] aint = this.field_75909_a.func_75904_a(i, j, k, l);
        int[] aint1 = IntCache.func_76445_a(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.func_75903_a((long) (j1 + i), (long) (i1 + j));
                aint1[j1 + i1 * k] = aint[j1 + i1 * k] > 0 ? this.func_75902_a(299999) + 2 : 0;
            }
        }

        return aint1;
    }
}

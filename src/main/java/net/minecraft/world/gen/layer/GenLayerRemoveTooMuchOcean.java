package net.minecraft.world.gen.layer;

public class GenLayerRemoveTooMuchOcean extends GenLayer {

    public GenLayerRemoveTooMuchOcean(long i, GenLayer genlayer) {
        super(i);
        this.parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int i1 = i - 1;
        int j1 = j - 1;
        int k1 = k + 2;
        int l1 = l + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(k * l);

        for (int i2 = 0; i2 < l; ++i2) {
            for (int j2 = 0; j2 < k; ++j2) {
                int k2 = aint[j2 + 1 + (i2 + 1 - 1) * (k + 2)];
                int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (k + 2)];
                int i3 = aint[j2 + 1 - 1 + (i2 + 1) * (k + 2)];
                int j3 = aint[j2 + 1 + (i2 + 1 + 1) * (k + 2)];
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];

                aint1[j2 + i2 * k] = k3;
                this.initChunkSeed((long) (j2 + i), (long) (i2 + j));
                if (k3 == 0 && k2 == 0 && l2 == 0 && i3 == 0 && j3 == 0 && this.nextInt(2) == 0) {
                    aint1[j2 + i2 * k] = 1;
                }
            }
        }

        return aint1;
    }
}

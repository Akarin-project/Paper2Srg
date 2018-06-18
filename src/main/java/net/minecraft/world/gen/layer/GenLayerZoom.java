package net.minecraft.world.gen.layer;

public class GenLayerZoom extends GenLayer {

    public GenLayerZoom(long i, GenLayer genlayer) {
        super(i);
        super.parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int i1 = i >> 1;
        int j1 = j >> 1;
        int k1 = (k >> 1) + 2;
        int l1 = (l >> 1) + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int i2 = k1 - 1 << 1;
        int j2 = l1 - 1 << 1;
        int[] aint1 = IntCache.getIntCache(i2 * j2);

        int k2;

        for (int l2 = 0; l2 < l1 - 1; ++l2) {
            k2 = (l2 << 1) * i2;
            int i3 = 0;
            int j3 = aint[i3 + 0 + (l2 + 0) * k1];

            for (int k3 = aint[i3 + 0 + (l2 + 1) * k1]; i3 < k1 - 1; ++i3) {
                this.initChunkSeed((long) (i3 + i1 << 1), (long) (l2 + j1 << 1));
                int l3 = aint[i3 + 1 + (l2 + 0) * k1];
                int i4 = aint[i3 + 1 + (l2 + 1) * k1];

                aint1[k2] = j3;
                aint1[k2++ + i2] = this.selectRandom(new int[] { j3, k3});
                aint1[k2] = this.selectRandom(new int[] { j3, l3});
                aint1[k2++ + i2] = this.selectModeOrRandom(j3, l3, k3, i4);
                j3 = l3;
                k3 = i4;
            }
        }

        int[] aint2 = IntCache.getIntCache(k * l);

        for (k2 = 0; k2 < l; ++k2) {
            System.arraycopy(aint1, (k2 + (j & 1)) * i2 + (i & 1), aint2, k2 * k, k);
        }

        return aint2;
    }

    public static GenLayer magnify(long i, GenLayer genlayer, int j) {
        Object object = genlayer;

        for (int k = 0; k < j; ++k) {
            object = new GenLayerZoom(i + (long) k, (GenLayer) object);
        }

        return (GenLayer) object;
    }
}

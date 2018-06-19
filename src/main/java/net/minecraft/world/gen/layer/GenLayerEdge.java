package net.minecraft.world.gen.layer;

public class GenLayerEdge extends GenLayer {

    private final GenLayerEdge.Mode field_151627_c;

    public GenLayerEdge(long i, GenLayer genlayer, GenLayerEdge.Mode genlayerspecial_enumgenlayerspecial) {
        super(i);
        this.field_75909_a = genlayer;
        this.field_151627_c = genlayerspecial_enumgenlayerspecial;
    }

    public int[] func_75904_a(int i, int j, int k, int l) {
        switch (this.field_151627_c) {
        case COOL_WARM:
        default:
            return this.func_151626_c(i, j, k, l);

        case HEAT_ICE:
            return this.func_151624_d(i, j, k, l);

        case SPECIAL:
            return this.func_151625_e(i, j, k, l);
        }
    }

    private int[] func_151626_c(int i, int j, int k, int l) {
        int i1 = i - 1;
        int j1 = j - 1;
        int k1 = 1 + k + 1;
        int l1 = 1 + l + 1;
        int[] aint = this.field_75909_a.func_75904_a(i1, j1, k1, l1);
        int[] aint1 = IntCache.func_76445_a(k * l);

        for (int i2 = 0; i2 < l; ++i2) {
            for (int j2 = 0; j2 < k; ++j2) {
                this.func_75903_a((long) (j2 + i), (long) (i2 + j));
                int k2 = aint[j2 + 1 + (i2 + 1) * k1];

                if (k2 == 1) {
                    int l2 = aint[j2 + 1 + (i2 + 1 - 1) * k1];
                    int i3 = aint[j2 + 1 + 1 + (i2 + 1) * k1];
                    int j3 = aint[j2 + 1 - 1 + (i2 + 1) * k1];
                    int k3 = aint[j2 + 1 + (i2 + 1 + 1) * k1];
                    boolean flag = l2 == 3 || i3 == 3 || j3 == 3 || k3 == 3;
                    boolean flag1 = l2 == 4 || i3 == 4 || j3 == 4 || k3 == 4;

                    if (flag || flag1) {
                        k2 = 2;
                    }
                }

                aint1[j2 + i2 * k] = k2;
            }
        }

        return aint1;
    }

    private int[] func_151624_d(int i, int j, int k, int l) {
        int i1 = i - 1;
        int j1 = j - 1;
        int k1 = 1 + k + 1;
        int l1 = 1 + l + 1;
        int[] aint = this.field_75909_a.func_75904_a(i1, j1, k1, l1);
        int[] aint1 = IntCache.func_76445_a(k * l);

        for (int i2 = 0; i2 < l; ++i2) {
            for (int j2 = 0; j2 < k; ++j2) {
                int k2 = aint[j2 + 1 + (i2 + 1) * k1];

                if (k2 == 4) {
                    int l2 = aint[j2 + 1 + (i2 + 1 - 1) * k1];
                    int i3 = aint[j2 + 1 + 1 + (i2 + 1) * k1];
                    int j3 = aint[j2 + 1 - 1 + (i2 + 1) * k1];
                    int k3 = aint[j2 + 1 + (i2 + 1 + 1) * k1];
                    boolean flag = l2 == 2 || i3 == 2 || j3 == 2 || k3 == 2;
                    boolean flag1 = l2 == 1 || i3 == 1 || j3 == 1 || k3 == 1;

                    if (flag1 || flag) {
                        k2 = 3;
                    }
                }

                aint1[j2 + i2 * k] = k2;
            }
        }

        return aint1;
    }

    private int[] func_151625_e(int i, int j, int k, int l) {
        int[] aint = this.field_75909_a.func_75904_a(i, j, k, l);
        int[] aint1 = IntCache.func_76445_a(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.func_75903_a((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + i1 * k];

                if (k1 != 0 && this.func_75902_a(13) == 0) {
                    k1 |= 1 + this.func_75902_a(15) << 8 & 3840;
                }

                aint1[j1 + i1 * k] = k1;
            }
        }

        return aint1;
    }

    public static enum Mode {

        COOL_WARM, HEAT_ICE, SPECIAL;

        private Mode() {}
    }
}

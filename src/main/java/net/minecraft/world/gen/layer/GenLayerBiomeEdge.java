package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;


public class GenLayerBiomeEdge extends GenLayer {

    public GenLayerBiomeEdge(long i, GenLayer genlayer) {
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

                if (!this.func_151636_a(aint, aint1, j1, i1, k, k1, Biome.func_185362_a(Biomes.field_76770_e), Biome.func_185362_a(Biomes.field_76783_v)) && !this.func_151635_b(aint, aint1, j1, i1, k, k1, Biome.func_185362_a(Biomes.field_150607_aa), Biome.func_185362_a(Biomes.field_150589_Z)) && !this.func_151635_b(aint, aint1, j1, i1, k, k1, Biome.func_185362_a(Biomes.field_150608_ab), Biome.func_185362_a(Biomes.field_150589_Z)) && !this.func_151635_b(aint, aint1, j1, i1, k, k1, Biome.func_185362_a(Biomes.field_150578_U), Biome.func_185362_a(Biomes.field_76768_g))) {
                    int l1;
                    int i2;
                    int j2;
                    int k2;

                    if (k1 == Biome.func_185362_a(Biomes.field_76769_d)) {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        if (l1 != Biome.func_185362_a(Biomes.field_76774_n) && i2 != Biome.func_185362_a(Biomes.field_76774_n) && j2 != Biome.func_185362_a(Biomes.field_76774_n) && k2 != Biome.func_185362_a(Biomes.field_76774_n)) {
                            aint1[j1 + i1 * k] = k1;
                        } else {
                            aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_150580_W);
                        }
                    } else if (k1 == Biome.func_185362_a(Biomes.field_76780_h)) {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        if (l1 != Biome.func_185362_a(Biomes.field_76769_d) && i2 != Biome.func_185362_a(Biomes.field_76769_d) && j2 != Biome.func_185362_a(Biomes.field_76769_d) && k2 != Biome.func_185362_a(Biomes.field_76769_d) && l1 != Biome.func_185362_a(Biomes.field_150584_S) && i2 != Biome.func_185362_a(Biomes.field_150584_S) && j2 != Biome.func_185362_a(Biomes.field_150584_S) && k2 != Biome.func_185362_a(Biomes.field_150584_S) && l1 != Biome.func_185362_a(Biomes.field_76774_n) && i2 != Biome.func_185362_a(Biomes.field_76774_n) && j2 != Biome.func_185362_a(Biomes.field_76774_n) && k2 != Biome.func_185362_a(Biomes.field_76774_n)) {
                            if (l1 != Biome.func_185362_a(Biomes.field_76782_w) && k2 != Biome.func_185362_a(Biomes.field_76782_w) && i2 != Biome.func_185362_a(Biomes.field_76782_w) && j2 != Biome.func_185362_a(Biomes.field_76782_w)) {
                                aint1[j1 + i1 * k] = k1;
                            } else {
                                aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_150574_L);
                            }
                        } else {
                            aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_76772_c);
                        }
                    } else {
                        aint1[j1 + i1 * k] = k1;
                    }
                }
            }
        }

        return aint1;
    }

    private boolean func_151636_a(int[] aint, int[] aint1, int i, int j, int k, int l, int i1, int j1) {
        if (!func_151616_a(l, i1)) {
            return false;
        } else {
            int k1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int l1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int j2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (this.func_151634_b(k1, i1) && this.func_151634_b(l1, i1) && this.func_151634_b(i2, i1) && this.func_151634_b(j2, i1)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = j1;
            }

            return true;
        }
    }

    private boolean func_151635_b(int[] aint, int[] aint1, int i, int j, int k, int l, int i1, int j1) {
        if (l != i1) {
            return false;
        } else {
            int k1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int l1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int j2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (func_151616_a(k1, i1) && func_151616_a(l1, i1) && func_151616_a(i2, i1) && func_151616_a(j2, i1)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = j1;
            }

            return true;
        }
    }

    private boolean func_151634_b(int i, int j) {
        if (func_151616_a(i, j)) {
            return true;
        } else {
            Biome biomebase = Biome.func_150568_d(i);
            Biome biomebase1 = Biome.func_150568_d(j);

            if (biomebase != null && biomebase1 != null) {
                Biome.TempCategory biomebase_enumtemperature = biomebase.func_150561_m();
                Biome.TempCategory biomebase_enumtemperature1 = biomebase1.func_150561_m();

                return biomebase_enumtemperature == biomebase_enumtemperature1 || biomebase_enumtemperature == Biome.TempCategory.MEDIUM || biomebase_enumtemperature1 == Biome.TempCategory.MEDIUM;
            } else {
                return false;
            }
        }
    }
}

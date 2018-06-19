package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;


public class GenLayerShore extends GenLayer {

    public GenLayerShore(long i, GenLayer genlayer) {
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
                Biome biomebase = Biome.func_150568_d(k1);
                int l1;
                int i2;
                int j2;
                int k2;

                if (k1 == Biome.func_185362_a(Biomes.field_76789_p)) {
                    l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                    i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                    k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (l1 != Biome.func_185362_a(Biomes.field_76771_b) && i2 != Biome.func_185362_a(Biomes.field_76771_b) && j2 != Biome.func_185362_a(Biomes.field_76771_b) && k2 != Biome.func_185362_a(Biomes.field_76771_b)) {
                        aint1[j1 + i1 * k] = k1;
                    } else {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_76788_q);
                    }
                } else if (biomebase != null && biomebase.func_150562_l() == BiomeJungle.class) {
                    l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                    i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                    k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (this.func_151631_c(l1) && this.func_151631_c(i2) && this.func_151631_c(j2) && this.func_151631_c(k2)) {
                        if (!func_151618_b(l1) && !func_151618_b(i2) && !func_151618_b(j2) && !func_151618_b(k2)) {
                            aint1[j1 + i1 * k] = k1;
                        } else {
                            aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_76787_r);
                        }
                    } else {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_150574_L);
                    }
                } else if (k1 != Biome.func_185362_a(Biomes.field_76770_e) && k1 != Biome.func_185362_a(Biomes.field_150580_W) && k1 != Biome.func_185362_a(Biomes.field_76783_v)) {
                    if (biomebase != null && biomebase.func_150559_j()) {
                        this.func_151632_a(aint, aint1, j1, i1, k, k1, Biome.func_185362_a(Biomes.field_150577_O));
                    } else if (k1 != Biome.func_185362_a(Biomes.field_150589_Z) && k1 != Biome.func_185362_a(Biomes.field_150607_aa)) {
                        if (k1 != Biome.func_185362_a(Biomes.field_76771_b) && k1 != Biome.func_185362_a(Biomes.field_150575_M) && k1 != Biome.func_185362_a(Biomes.field_76781_i) && k1 != Biome.func_185362_a(Biomes.field_76780_h)) {
                            l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                            i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                            j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                            k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                            if (!func_151618_b(l1) && !func_151618_b(i2) && !func_151618_b(j2) && !func_151618_b(k2)) {
                                aint1[j1 + i1 * k] = k1;
                            } else {
                                aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_76787_r);
                            }
                        } else {
                            aint1[j1 + i1 * k] = k1;
                        }
                    } else {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        if (!func_151618_b(l1) && !func_151618_b(i2) && !func_151618_b(j2) && !func_151618_b(k2)) {
                            if (this.func_151633_d(l1) && this.func_151633_d(i2) && this.func_151633_d(j2) && this.func_151633_d(k2)) {
                                aint1[j1 + i1 * k] = k1;
                            } else {
                                aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_76769_d);
                            }
                        } else {
                            aint1[j1 + i1 * k] = k1;
                        }
                    }
                } else {
                    this.func_151632_a(aint, aint1, j1, i1, k, k1, Biome.func_185362_a(Biomes.field_150576_N));
                }
            }
        }

        return aint1;
    }

    private void func_151632_a(int[] aint, int[] aint1, int i, int j, int k, int l, int i1) {
        if (func_151618_b(l)) {
            aint1[i + j * k] = l;
        } else {
            int j1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int k1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int l1 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (!func_151618_b(j1) && !func_151618_b(k1) && !func_151618_b(l1) && !func_151618_b(i2)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = i1;
            }

        }
    }

    private boolean func_151631_c(int i) {
        return Biome.func_150568_d(i) != null && Biome.func_150568_d(i).func_150562_l() == BiomeJungle.class ? true : i == Biome.func_185362_a(Biomes.field_150574_L) || i == Biome.func_185362_a(Biomes.field_76782_w) || i == Biome.func_185362_a(Biomes.field_76792_x) || i == Biome.func_185362_a(Biomes.field_76767_f) || i == Biome.func_185362_a(Biomes.field_76768_g) || func_151618_b(i);
    }

    private boolean func_151633_d(int i) {
        return Biome.func_150568_d(i) instanceof BiomeMesa;
    }
}

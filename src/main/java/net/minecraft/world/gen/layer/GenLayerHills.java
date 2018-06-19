package net.minecraft.world.gen.layer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerHills extends GenLayer {

    private static final Logger field_151629_c = LogManager.getLogger();
    private final GenLayer field_151628_d;

    public GenLayerHills(long i, GenLayer genlayer, GenLayer genlayer1) {
        super(i);
        this.field_75909_a = genlayer;
        this.field_151628_d = genlayer1;
    }

    public int[] func_75904_a(int i, int j, int k, int l) {
        int[] aint = this.field_75909_a.func_75904_a(i - 1, j - 1, k + 2, l + 2);
        int[] aint1 = this.field_151628_d.func_75904_a(i - 1, j - 1, k + 2, l + 2);
        int[] aint2 = IntCache.func_76445_a(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.func_75903_a((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + 1 + (i1 + 1) * (k + 2)];
                int l1 = aint1[j1 + 1 + (i1 + 1) * (k + 2)];
                boolean flag = (l1 - 2) % 29 == 0;

                if (k1 > 255) {
                    GenLayerHills.field_151629_c.debug("old! {}", Integer.valueOf(k1));
                }

                Biome biomebase = Biome.func_185357_a(k1);
                boolean flag1 = biomebase != null && biomebase.func_185363_b();
                Biome biomebase1;

                if (k1 != 0 && l1 >= 2 && (l1 - 2) % 29 == 1 && !flag1) {
                    biomebase1 = Biome.func_185356_b(biomebase);
                    aint2[j1 + i1 * k] = biomebase1 == null ? k1 : Biome.func_185362_a(biomebase1);
                } else if (this.func_75902_a(3) != 0 && !flag) {
                    aint2[j1 + i1 * k] = k1;
                } else {
                    biomebase1 = biomebase;
                    int i2;

                    if (biomebase == Biomes.field_76769_d) {
                        biomebase1 = Biomes.field_76786_s;
                    } else if (biomebase == Biomes.field_76767_f) {
                        biomebase1 = Biomes.field_76785_t;
                    } else if (biomebase == Biomes.field_150583_P) {
                        biomebase1 = Biomes.field_150582_Q;
                    } else if (biomebase == Biomes.field_150585_R) {
                        biomebase1 = Biomes.field_76772_c;
                    } else if (biomebase == Biomes.field_76768_g) {
                        biomebase1 = Biomes.field_76784_u;
                    } else if (biomebase == Biomes.field_150578_U) {
                        biomebase1 = Biomes.field_150581_V;
                    } else if (biomebase == Biomes.field_150584_S) {
                        biomebase1 = Biomes.field_150579_T;
                    } else if (biomebase == Biomes.field_76772_c) {
                        if (this.func_75902_a(3) == 0) {
                            biomebase1 = Biomes.field_76785_t;
                        } else {
                            biomebase1 = Biomes.field_76767_f;
                        }
                    } else if (biomebase == Biomes.field_76774_n) {
                        biomebase1 = Biomes.field_76775_o;
                    } else if (biomebase == Biomes.field_76782_w) {
                        biomebase1 = Biomes.field_76792_x;
                    } else if (biomebase == Biomes.field_76771_b) {
                        biomebase1 = Biomes.field_150575_M;
                    } else if (biomebase == Biomes.field_76770_e) {
                        biomebase1 = Biomes.field_150580_W;
                    } else if (biomebase == Biomes.field_150588_X) {
                        biomebase1 = Biomes.field_150587_Y;
                    } else if (func_151616_a(k1, Biome.func_185362_a(Biomes.field_150607_aa))) {
                        biomebase1 = Biomes.field_150589_Z;
                    } else if (biomebase == Biomes.field_150575_M && this.func_75902_a(3) == 0) {
                        i2 = this.func_75902_a(2);
                        if (i2 == 0) {
                            biomebase1 = Biomes.field_76772_c;
                        } else {
                            biomebase1 = Biomes.field_76767_f;
                        }
                    }

                    i2 = Biome.func_185362_a(biomebase1);
                    if (flag && i2 != k1) {
                        Biome biomebase2 = Biome.func_185356_b(biomebase1);

                        i2 = biomebase2 == null ? k1 : Biome.func_185362_a(biomebase2);
                    }

                    if (i2 == k1) {
                        aint2[j1 + i1 * k] = k1;
                    } else {
                        int j2 = aint[j1 + 1 + (i1 + 0) * (k + 2)];
                        int k2 = aint[j1 + 2 + (i1 + 1) * (k + 2)];
                        int l2 = aint[j1 + 0 + (i1 + 1) * (k + 2)];
                        int i3 = aint[j1 + 1 + (i1 + 2) * (k + 2)];
                        int j3 = 0;

                        if (func_151616_a(j2, k1)) {
                            ++j3;
                        }

                        if (func_151616_a(k2, k1)) {
                            ++j3;
                        }

                        if (func_151616_a(l2, k1)) {
                            ++j3;
                        }

                        if (func_151616_a(i3, k1)) {
                            ++j3;
                        }

                        if (j3 >= 3) {
                            aint2[j1 + i1 * k] = i2;
                        } else {
                            aint2[j1 + i1 * k] = k1;
                        }
                    }
                }
            }
        }

        return aint2;
    }
}

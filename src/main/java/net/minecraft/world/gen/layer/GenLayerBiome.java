package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;


public class GenLayerBiome extends GenLayer {

    private Biome[] field_151623_c;
    private final Biome[] field_151621_d;
    private final Biome[] field_151622_e;
    private final Biome[] field_151620_f;
    private final ChunkGeneratorSettings field_175973_g;

    public GenLayerBiome(long i, GenLayer genlayer, WorldType worldtype, ChunkGeneratorSettings customworldsettingsfinal) {
        super(i);
        this.field_151623_c = new Biome[] { Biomes.field_76769_d, Biomes.field_76769_d, Biomes.field_76769_d, Biomes.field_150588_X, Biomes.field_150588_X, Biomes.field_76772_c};
        this.field_151621_d = new Biome[] { Biomes.field_76767_f, Biomes.field_150585_R, Biomes.field_76770_e, Biomes.field_76772_c, Biomes.field_150583_P, Biomes.field_76780_h};
        this.field_151622_e = new Biome[] { Biomes.field_76767_f, Biomes.field_76770_e, Biomes.field_76768_g, Biomes.field_76772_c};
        this.field_151620_f = new Biome[] { Biomes.field_76774_n, Biomes.field_76774_n, Biomes.field_76774_n, Biomes.field_150584_S};
        this.field_75909_a = genlayer;
        if (worldtype == WorldType.field_77136_e) {
            this.field_151623_c = new Biome[] { Biomes.field_76769_d, Biomes.field_76767_f, Biomes.field_76770_e, Biomes.field_76780_h, Biomes.field_76772_c, Biomes.field_76768_g};
            this.field_175973_g = null;
        } else {
            this.field_175973_g = customworldsettingsfinal;
        }

    }

    public int[] func_75904_a(int i, int j, int k, int l) {
        int[] aint = this.field_75909_a.func_75904_a(i, j, k, l);
        int[] aint1 = IntCache.func_76445_a(k * l);

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.func_75903_a((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + i1 * k];
                int l1 = (k1 & 3840) >> 8;

                k1 &= -3841;
                if (this.field_175973_g != null && this.field_175973_g.field_177779_F >= 0) {
                    aint1[j1 + i1 * k] = this.field_175973_g.field_177779_F;
                } else if (func_151618_b(k1)) {
                    aint1[j1 + i1 * k] = k1;
                } else if (k1 == Biome.func_185362_a(Biomes.field_76789_p)) {
                    aint1[j1 + i1 * k] = k1;
                } else if (k1 == 1) {
                    if (l1 > 0) {
                        if (this.func_75902_a(3) == 0) {
                            aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_150608_ab);
                        } else {
                            aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_150607_aa);
                        }
                    } else {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(this.field_151623_c[this.func_75902_a(this.field_151623_c.length)]);
                    }
                } else if (k1 == 2) {
                    if (l1 > 0) {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_76782_w);
                    } else {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(this.field_151621_d[this.func_75902_a(this.field_151621_d.length)]);
                    }
                } else if (k1 == 3) {
                    if (l1 > 0) {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_150578_U);
                    } else {
                        aint1[j1 + i1 * k] = Biome.func_185362_a(this.field_151622_e[this.func_75902_a(this.field_151622_e.length)]);
                    }
                } else if (k1 == 4) {
                    aint1[j1 + i1 * k] = Biome.func_185362_a(this.field_151620_f[this.func_75902_a(this.field_151620_f.length)]);
                } else {
                    aint1[j1 + i1 * k] = Biome.func_185362_a(Biomes.field_76789_p);
                }
            }
        }

        return aint1;
    }
}

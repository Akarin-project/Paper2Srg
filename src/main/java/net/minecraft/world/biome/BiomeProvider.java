package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;

public class BiomeProvider {

    private ChunkGeneratorSettings field_190945_a;
    private GenLayer field_76944_d;
    private GenLayer field_76945_e;
    private final BiomeCache field_76942_f;
    private final List<Biome> field_76943_g;

    protected BiomeProvider() {
        this.field_76942_f = new BiomeCache(this);
        this.field_76943_g = Lists.newArrayList(new Biome[] { Biomes.field_76767_f, Biomes.field_76772_c, Biomes.field_76768_g, Biomes.field_76784_u, Biomes.field_76785_t, Biomes.field_76782_w, Biomes.field_76792_x});
    }

    private BiomeProvider(long i, WorldType worldtype, String s) {
        this();
        if (worldtype == WorldType.field_180271_f && !s.isEmpty()) {
            this.field_190945_a = ChunkGeneratorSettings.Factory.func_177865_a(s).func_177864_b();
        }

        GenLayer[] agenlayer = GenLayer.func_180781_a(i, worldtype, this.field_190945_a);

        this.field_76944_d = agenlayer[0];
        this.field_76945_e = agenlayer[1];
    }

    public BiomeProvider(WorldInfo worlddata) {
        this(worlddata.func_76063_b(), worlddata.func_76067_t(), worlddata.func_82571_y());
    }

    public List<Biome> func_76932_a() {
        return this.field_76943_g;
    }

    public Biome func_180631_a(BlockPos blockposition) {
        return this.func_180300_a(blockposition, (Biome) null);
    }

    public Biome func_180300_a(BlockPos blockposition, Biome biomebase) {
        return this.field_76942_f.func_180284_a(blockposition.func_177958_n(), blockposition.func_177952_p(), biomebase);
    }

    public float func_76939_a(float f, int i) {
        return f;
    }

    public Biome[] func_76937_a(Biome[] abiomebase, int i, int j, int k, int l) {
        IntCache.func_76446_a();
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        int[] aint = this.field_76944_d.func_75904_a(i, j, k, l);

        try {
            for (int i1 = 0; i1 < k * l; ++i1) {
                abiomebase[i1] = Biome.func_180276_a(aint[i1], Biomes.field_180279_ad);
            }

            return abiomebase;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Invalid Biome id");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("RawBiomeBlock");

            crashreportsystemdetails.func_71507_a("biomes[] size", (Object) Integer.valueOf(abiomebase.length));
            crashreportsystemdetails.func_71507_a("x", (Object) Integer.valueOf(i));
            crashreportsystemdetails.func_71507_a("z", (Object) Integer.valueOf(j));
            crashreportsystemdetails.func_71507_a("w", (Object) Integer.valueOf(k));
            crashreportsystemdetails.func_71507_a("h", (Object) Integer.valueOf(l));
            throw new ReportedException(crashreport);
        }
    }

    public Biome[] func_76933_b(@Nullable Biome[] abiomebase, int i, int j, int k, int l) {
        return this.func_76931_a(abiomebase, i, j, k, l, true);
    }

    public Biome[] func_76931_a(@Nullable Biome[] abiomebase, int i, int j, int k, int l, boolean flag) {
        IntCache.func_76446_a();
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        if (flag && k == 16 && l == 16 && (i & 15) == 0 && (j & 15) == 0) {
            Biome[] abiomebase1 = this.field_76942_f.func_76839_e(i, j);

            System.arraycopy(abiomebase1, 0, abiomebase, 0, k * l);
            return abiomebase;
        } else {
            int[] aint = this.field_76945_e.func_75904_a(i, j, k, l);

            for (int i1 = 0; i1 < k * l; ++i1) {
                abiomebase[i1] = Biome.func_180276_a(aint[i1], Biomes.field_180279_ad);
            }

            return abiomebase;
        }
    }

    public boolean func_76940_a(int i, int j, int k, List<Biome> list) {
        IntCache.func_76446_a();
        int l = i - k >> 2;
        int i1 = j - k >> 2;
        int j1 = i + k >> 2;
        int k1 = j + k >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.field_76944_d.func_75904_a(l, i1, l1, i2);

        try {
            for (int j2 = 0; j2 < l1 * i2; ++j2) {
                Biome biomebase = Biome.func_150568_d(aint[j2]);

                if (!list.contains(biomebase)) {
                    return false;
                }
            }

            return true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Invalid Biome id");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Layer");

            crashreportsystemdetails.func_71507_a("Layer", (Object) this.field_76944_d.toString());
            crashreportsystemdetails.func_71507_a("x", (Object) Integer.valueOf(i));
            crashreportsystemdetails.func_71507_a("z", (Object) Integer.valueOf(j));
            crashreportsystemdetails.func_71507_a("radius", (Object) Integer.valueOf(k));
            crashreportsystemdetails.func_71507_a("allowed", (Object) list);
            throw new ReportedException(crashreport);
        }
    }

    @Nullable
    public BlockPos func_180630_a(int i, int j, int k, List<Biome> list, Random random) {
        IntCache.func_76446_a();
        int l = i - k >> 2;
        int i1 = j - k >> 2;
        int j1 = i + k >> 2;
        int k1 = j + k >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.field_76944_d.func_75904_a(l, i1, l1, i2);
        BlockPos blockposition = null;
        int j2 = 0;

        for (int k2 = 0; k2 < l1 * i2; ++k2) {
            int l2 = l + k2 % l1 << 2;
            int i3 = i1 + k2 / l1 << 2;
            Biome biomebase = Biome.func_150568_d(aint[k2]);

            if (list.contains(biomebase) && (blockposition == null || random.nextInt(j2 + 1) == 0)) {
                blockposition = new BlockPos(l2, 0, i3);
                ++j2;
            }
        }

        return blockposition;
    }

    public void func_76938_b() {
        this.field_76942_f.func_76838_a();
    }

    public boolean func_190944_c() {
        return this.field_190945_a != null && this.field_190945_a.field_177779_F >= 0;
    }

    public Biome func_190943_d() {
        return this.field_190945_a != null && this.field_190945_a.field_177779_F >= 0 ? Biome.func_185357_a(this.field_190945_a.field_177779_F) : null;
    }
}

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

    private ChunkGeneratorSettings settings;
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private final BiomeCache biomeCache;
    private final List<Biome> biomesToSpawnIn;

    protected BiomeProvider() {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = Lists.newArrayList(new Biome[] { Biomes.FOREST, Biomes.PLAINS, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.FOREST_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS});
    }

    private BiomeProvider(long i, WorldType worldtype, String s) {
        this();
        if (worldtype == WorldType.CUSTOMIZED && !s.isEmpty()) {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(s).build();
        }

        GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(i, worldtype, this.settings);

        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
    }

    public BiomeProvider(WorldInfo worlddata) {
        this(worlddata.getSeed(), worlddata.getTerrainType(), worlddata.getGeneratorOptions());
    }

    public List<Biome> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    public Biome getBiome(BlockPos blockposition) {
        return this.getBiome(blockposition, (Biome) null);
    }

    public Biome getBiome(BlockPos blockposition, Biome biomebase) {
        return this.biomeCache.getBiome(blockposition.getX(), blockposition.getZ(), biomebase);
    }

    public float getTemperatureAtHeight(float f, int i) {
        return f;
    }

    public Biome[] getBiomesForGeneration(Biome[] abiomebase, int i, int j, int k, int l) {
        IntCache.resetIntCache();
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        int[] aint = this.genBiomes.getInts(i, j, k, l);

        try {
            for (int i1 = 0; i1 < k * l; ++i1) {
                abiomebase[i1] = Biome.getBiome(aint[i1], Biomes.DEFAULT);
            }

            return abiomebase;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("RawBiomeBlock");

            crashreportsystemdetails.addCrashSection("biomes[] size", (Object) Integer.valueOf(abiomebase.length));
            crashreportsystemdetails.addCrashSection("x", (Object) Integer.valueOf(i));
            crashreportsystemdetails.addCrashSection("z", (Object) Integer.valueOf(j));
            crashreportsystemdetails.addCrashSection("w", (Object) Integer.valueOf(k));
            crashreportsystemdetails.addCrashSection("h", (Object) Integer.valueOf(l));
            throw new ReportedException(crashreport);
        }
    }

    public Biome[] getBiomes(@Nullable Biome[] abiomebase, int i, int j, int k, int l) {
        return this.getBiomes(abiomebase, i, j, k, l, true);
    }

    public Biome[] getBiomes(@Nullable Biome[] abiomebase, int i, int j, int k, int l, boolean flag) {
        IntCache.resetIntCache();
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new Biome[k * l];
        }

        if (flag && k == 16 && l == 16 && (i & 15) == 0 && (j & 15) == 0) {
            Biome[] abiomebase1 = this.biomeCache.getCachedBiomes(i, j);

            System.arraycopy(abiomebase1, 0, abiomebase, 0, k * l);
            return abiomebase;
        } else {
            int[] aint = this.biomeIndexLayer.getInts(i, j, k, l);

            for (int i1 = 0; i1 < k * l; ++i1) {
                abiomebase[i1] = Biome.getBiome(aint[i1], Biomes.DEFAULT);
            }

            return abiomebase;
        }
    }

    public boolean areBiomesViable(int i, int j, int k, List<Biome> list) {
        IntCache.resetIntCache();
        int l = i - k >> 2;
        int i1 = j - k >> 2;
        int j1 = i + k >> 2;
        int k1 = j + k >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

        try {
            for (int j2 = 0; j2 < l1 * i2; ++j2) {
                Biome biomebase = Biome.getBiome(aint[j2]);

                if (!list.contains(biomebase)) {
                    return false;
                }
            }

            return true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Layer");

            crashreportsystemdetails.addCrashSection("Layer", (Object) this.genBiomes.toString());
            crashreportsystemdetails.addCrashSection("x", (Object) Integer.valueOf(i));
            crashreportsystemdetails.addCrashSection("z", (Object) Integer.valueOf(j));
            crashreportsystemdetails.addCrashSection("radius", (Object) Integer.valueOf(k));
            crashreportsystemdetails.addCrashSection("allowed", (Object) list);
            throw new ReportedException(crashreport);
        }
    }

    @Nullable
    public BlockPos findBiomePosition(int i, int j, int k, List<Biome> list, Random random) {
        IntCache.resetIntCache();
        int l = i - k >> 2;
        int i1 = j - k >> 2;
        int j1 = i + k >> 2;
        int k1 = j + k >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
        BlockPos blockposition = null;
        int j2 = 0;

        for (int k2 = 0; k2 < l1 * i2; ++k2) {
            int l2 = l + k2 % l1 << 2;
            int i3 = i1 + k2 / l1 << 2;
            Biome biomebase = Biome.getBiome(aint[k2]);

            if (list.contains(biomebase) && (blockposition == null || random.nextInt(j2 + 1) == 0)) {
                blockposition = new BlockPos(l2, 0, i3);
                ++j2;
            }
        }

        return blockposition;
    }

    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }

    public boolean isFixedBiome() {
        return this.settings != null && this.settings.fixedBiome >= 0;
    }

    public Biome getFixedBiome() {
        return this.settings != null && this.settings.fixedBiome >= 0 ? Biome.getBiomeForId(this.settings.fixedBiome) : null;
    }
}

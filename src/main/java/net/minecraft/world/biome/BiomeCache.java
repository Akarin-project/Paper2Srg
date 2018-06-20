package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;

import net.minecraft.server.MinecraftServer;

public class BiomeCache {

    private final BiomeProvider field_76844_a;
    private long field_76842_b;
    private final Long2ObjectMap<BiomeCache.a> field_76843_c = new Long2ObjectOpenHashMap(4096);
    private final List<BiomeCache.a> field_76841_d = Lists.newArrayList();

    public BiomeCache(BiomeProvider worldchunkmanager) {
        this.field_76844_a = worldchunkmanager;
    }

    public BiomeCache.a a(int i, int j) {
        i >>= 4;
        j >>= 4;
        long k = i & 4294967295L | (j & 4294967295L) << 32;
        BiomeCache.a biomecache_a = this.field_76843_c.get(k);

        if (biomecache_a == null) {
            biomecache_a = new BiomeCache.a(i, j);
            this.field_76843_c.put(k, biomecache_a);
            this.field_76841_d.add(biomecache_a);
        }

        biomecache_a.d = MinecraftServer.func_130071_aq();
        return biomecache_a;
    }

    public Biome func_180284_a(int i, int j, Biome biomebase) {
        Biome biomebase1 = this.a(i, j).a(i, j);

        return biomebase1 == null ? biomebase : biomebase1;
    }

    public void func_76838_a() {
        long i = MinecraftServer.func_130071_aq();
        long j = i - this.field_76842_b;

        if (j > 7500L || j < 0L) {
            this.field_76842_b = i;

            for (int k = 0; k < this.field_76841_d.size(); ++k) {
                BiomeCache.a biomecache_a = this.field_76841_d.get(k);
                long l = i - biomecache_a.d;

                if (l > 30000L || l < 0L) {
                    this.field_76841_d.remove(k--);
                    long i1 = biomecache_a.b & 4294967295L | (biomecache_a.c & 4294967295L) << 32;

                    this.field_76843_c.remove(i1);
                }
            }
        }

    }

    public Biome[] func_76839_e(int i, int j) {
        return this.a(i, j).a;
    }

    public class a {

        public Biome[] a = new Biome[256];
        public int b;
        public int c;
        public long d;

        public a(int i, int j) {
            this.b = i;
            this.c = j;
            BiomeCache.this.field_76844_a.func_76931_a(this.a, i << 4, j << 4, 16, 16, false);
        }

        public Biome a(int i, int j) {
            return this.a[i & 15 | (j & 15) << 4];
        }
    }
}

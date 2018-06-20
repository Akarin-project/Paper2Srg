package net.minecraft.world.biome;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSpikes;

public class BiomeEndDecorator extends BiomeDecorator {

    private static final LoadingCache<Long, WorldGenSpikes.EndSpike[]> field_185427_L = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build(new BiomeEndDecorator.SpikeCacheLoader(null));
    private final WorldGenSpikes field_76835_L = new WorldGenSpikes();

    public BiomeEndDecorator() {}

    @Override
    protected void func_150513_a(Biome biomebase, World world, Random random) {
        this.func_76797_b(world, random);
        WorldGenSpikes.EndSpike[] aworldgenender_spike = func_185426_a(world);
        WorldGenSpikes.EndSpike[] aworldgenender_spike1 = aworldgenender_spike;
        int i = aworldgenender_spike.length;

        for (int j = 0; j < i; ++j) {
            WorldGenSpikes.EndSpike worldgenender_spike = aworldgenender_spike1[j];

            if (worldgenender_spike.func_186154_a(this.field_180294_c)) {
                this.field_76835_L.func_186143_a(worldgenender_spike);
                this.field_76835_L.func_180709_b(world, random, new BlockPos(worldgenender_spike.func_186151_a(), 45, worldgenender_spike.func_186152_b()));
            }
        }

    }

    public static WorldGenSpikes.EndSpike[] func_185426_a(World world) {
        Random random = new Random(world.func_72905_C());
        long i = random.nextLong() & 65535L;

        return BiomeEndDecorator.field_185427_L.getUnchecked(Long.valueOf(i));
    }

    static class SpikeCacheLoader extends CacheLoader<Long, WorldGenSpikes.EndSpike[]> {

        private SpikeCacheLoader() {}

        @Override
        public WorldGenSpikes.EndSpike[] load(Long olong) throws Exception {
            ArrayList arraylist = Lists.newArrayList(ContiguousSet.create(Range.closedOpen(Integer.valueOf(0), Integer.valueOf(10)), DiscreteDomain.integers()));

            Collections.shuffle(arraylist, new Random(olong.longValue()));
            WorldGenSpikes.EndSpike[] aworldgenender_spike = new WorldGenSpikes.EndSpike[10];

            for (int i = 0; i < 10; ++i) {
                int j = (int) (42.0D * Math.cos(2.0D * (-3.141592653589793D + 0.3141592653589793D * i)));
                int k = (int) (42.0D * Math.sin(2.0D * (-3.141592653589793D + 0.3141592653589793D * i)));
                int l = ((Integer) arraylist.get(i)).intValue();
                int i1 = 2 + l / 3;
                int j1 = 76 + l * 3;
                boolean flag = l == 1 || l == 2;

                aworldgenender_spike[i] = new WorldGenSpikes.EndSpike(j, k, i1, j1, flag);
            }

            return aworldgenender_spike;
        }

        SpikeCacheLoader(Object object) {
            this();
        }
    }
}

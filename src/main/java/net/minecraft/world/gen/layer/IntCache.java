package net.minecraft.world.gen.layer;

import com.google.common.collect.Lists;
import java.util.List;

public class IntCache {

    private static int intCacheSize = 256;
    private static final List<int[]> freeSmallArrays = Lists.newArrayList();
    private static final List<int[]> inUseSmallArrays = Lists.newArrayList();
    private static final List<int[]> freeLargeArrays = Lists.newArrayList();
    private static final List<int[]> inUseLargeArrays = Lists.newArrayList();

    public static synchronized int[] getIntCache(int i) {
        int[] aint;

        if (i <= 256) {
            if (IntCache.freeSmallArrays.isEmpty()) {
                aint = new int[256];
                if (inUseSmallArrays.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.inUseSmallArrays.add(aint);
                return aint;
            } else {
                aint = (int[]) IntCache.freeSmallArrays.remove(IntCache.freeSmallArrays.size() - 1);
                if (inUseSmallArrays.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.inUseSmallArrays.add(aint);
                return aint;
            }
        } else if (i > IntCache.intCacheSize) {
            IntCache.intCacheSize = i;
            IntCache.freeLargeArrays.clear();
            IntCache.inUseLargeArrays.clear();
            aint = new int[IntCache.intCacheSize];
            if (inUseLargeArrays.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.inUseLargeArrays.add(aint);
            return aint;
        } else if (IntCache.freeLargeArrays.isEmpty()) {
            aint = new int[IntCache.intCacheSize];
            if (inUseLargeArrays.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.inUseLargeArrays.add(aint);
            return aint;
        } else {
            aint = (int[]) IntCache.freeLargeArrays.remove(IntCache.freeLargeArrays.size() - 1);
            if (inUseLargeArrays.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.inUseLargeArrays.add(aint);
            return aint;
        }
    }

    public static synchronized void resetIntCache() {
        if (!IntCache.freeLargeArrays.isEmpty()) {
            IntCache.freeLargeArrays.remove(IntCache.freeLargeArrays.size() - 1);
        }

        if (!IntCache.freeSmallArrays.isEmpty()) {
            IntCache.freeSmallArrays.remove(IntCache.freeSmallArrays.size() - 1);
        }

        IntCache.freeLargeArrays.addAll(IntCache.inUseLargeArrays);
        IntCache.freeSmallArrays.addAll(IntCache.inUseSmallArrays);
        IntCache.inUseLargeArrays.clear();
        IntCache.inUseSmallArrays.clear();
    }

    public static synchronized String getCacheSizes() {
        return "cache: " + IntCache.freeLargeArrays.size() + ", tcache: " + IntCache.freeSmallArrays.size() + ", allocated: " + IntCache.inUseLargeArrays.size() + ", tallocated: " + IntCache.inUseSmallArrays.size();
    }
}

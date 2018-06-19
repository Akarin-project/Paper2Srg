package net.minecraft.world.gen.layer;

import com.google.common.collect.Lists;
import java.util.List;

public class IntCache {

    private static int field_76451_a = 256;
    private static final List<int[]> field_76449_b = Lists.newArrayList();
    private static final List<int[]> field_76450_c = Lists.newArrayList();
    private static final List<int[]> field_76447_d = Lists.newArrayList();
    private static final List<int[]> field_76448_e = Lists.newArrayList();

    public static synchronized int[] func_76445_a(int i) {
        int[] aint;

        if (i <= 256) {
            if (IntCache.field_76449_b.isEmpty()) {
                aint = new int[256];
                if (field_76450_c.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.field_76450_c.add(aint);
                return aint;
            } else {
                aint = (int[]) IntCache.field_76449_b.remove(IntCache.field_76449_b.size() - 1);
                if (field_76450_c.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.field_76450_c.add(aint);
                return aint;
            }
        } else if (i > IntCache.field_76451_a) {
            IntCache.field_76451_a = i;
            IntCache.field_76447_d.clear();
            IntCache.field_76448_e.clear();
            aint = new int[IntCache.field_76451_a];
            if (field_76448_e.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.field_76448_e.add(aint);
            return aint;
        } else if (IntCache.field_76447_d.isEmpty()) {
            aint = new int[IntCache.field_76451_a];
            if (field_76448_e.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.field_76448_e.add(aint);
            return aint;
        } else {
            aint = (int[]) IntCache.field_76447_d.remove(IntCache.field_76447_d.size() - 1);
            if (field_76448_e.size() < org.spigotmc.SpigotConfig.intCacheLimit) IntCache.field_76448_e.add(aint);
            return aint;
        }
    }

    public static synchronized void func_76446_a() {
        if (!IntCache.field_76447_d.isEmpty()) {
            IntCache.field_76447_d.remove(IntCache.field_76447_d.size() - 1);
        }

        if (!IntCache.field_76449_b.isEmpty()) {
            IntCache.field_76449_b.remove(IntCache.field_76449_b.size() - 1);
        }

        IntCache.field_76447_d.addAll(IntCache.field_76448_e);
        IntCache.field_76449_b.addAll(IntCache.field_76450_c);
        IntCache.field_76448_e.clear();
        IntCache.field_76450_c.clear();
    }

    public static synchronized String func_85144_b() {
        return "cache: " + IntCache.field_76447_d.size() + ", tcache: " + IntCache.field_76449_b.size() + ", allocated: " + IntCache.field_76448_e.size() + ", tallocated: " + IntCache.field_76450_c.size();
    }
}

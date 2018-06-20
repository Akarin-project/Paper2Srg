package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {

    public static final boolean ENABLED = Boolean.getBoolean("enableDebugMethodProfiler"); // CraftBukkit - disable unless specified in JVM arguments
    private static final Logger field_151234_b = LogManager.getLogger();
    private final List<String> field_76325_b = Lists.newArrayList();
    private final List<Long> field_76326_c = Lists.newArrayList();
    public boolean field_76327_a;
    private String field_76323_d = "";
    private final Map<String, Long> field_76324_e = Maps.newHashMap();

    public Profiler() {}

    public void func_76317_a() {
        if (!ENABLED) return;  // CraftBukkit
        this.field_76324_e.clear();
        this.field_76323_d = "";
        this.field_76325_b.clear();
    }

    public void func_76320_a(String s) {
        if (!ENABLED) return;  // CraftBukkit
        if (this.field_76327_a) {
            if (!this.field_76323_d.isEmpty()) {
                this.field_76323_d = this.field_76323_d + ".";
            }

            this.field_76323_d = this.field_76323_d + s;
            this.field_76325_b.add(this.field_76323_d);
            this.field_76326_c.add(Long.valueOf(System.nanoTime()));
        }
    }

    public void func_194340_a(Supplier<String> supplier) {
        if (!ENABLED) return;  // CraftBukkit
        if (this.field_76327_a) {
            this.func_76320_a(supplier.get());
        }
    }

    public void func_76319_b() {
        if (!ENABLED) return;  // CraftBukkit
        if (this.field_76327_a) {
            long i = System.nanoTime();
            long j = this.field_76326_c.remove(this.field_76326_c.size() - 1).longValue();

            this.field_76325_b.remove(this.field_76325_b.size() - 1);
            long k = i - j;

            if (this.field_76324_e.containsKey(this.field_76323_d)) {
                this.field_76324_e.put(this.field_76323_d, Long.valueOf(this.field_76324_e.get(this.field_76323_d).longValue() + k));
            } else {
                this.field_76324_e.put(this.field_76323_d, Long.valueOf(k));
            }

            if (k > 100000000L) {
                Profiler.field_151234_b.warn("Something\'s taking too long! \'{}\' took aprox {} ms", this.field_76323_d, Double.valueOf(k / 1000000.0D));
            }

            this.field_76323_d = this.field_76325_b.isEmpty() ? "" : (String) this.field_76325_b.get(this.field_76325_b.size() - 1);
        }
    }

    public List<Profiler.Result> func_76321_b(String s) {
        if (!ENABLED || !this.field_76327_a) {  // CraftBukkit
            return Collections.emptyList();
        } else {
            long i = this.field_76324_e.containsKey("root") ? this.field_76324_e.get("root").longValue() : 0L;
            long j = this.field_76324_e.containsKey(s) ? this.field_76324_e.get(s).longValue() : -1L;
            ArrayList arraylist = Lists.newArrayList();

            if (!s.isEmpty()) {
                s = s + ".";
            }

            long k = 0L;
            Iterator iterator = this.field_76324_e.keySet().iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                if (s1.length() > s.length() && s1.startsWith(s) && s1.indexOf(".", s.length() + 1) < 0) {
                    k += this.field_76324_e.get(s1).longValue();
                }
            }

            float f = k;

            if (k < j) {
                k = j;
            }

            if (i < k) {
                i = k;
            }

            Iterator iterator1 = this.field_76324_e.keySet().iterator();

            String s2;

            while (iterator1.hasNext()) {
                s2 = (String) iterator1.next();
                if (s2.length() > s.length() && s2.startsWith(s) && s2.indexOf(".", s.length() + 1) < 0) {
                    long l = this.field_76324_e.get(s2).longValue();
                    double d0 = l * 100.0D / k;
                    double d1 = l * 100.0D / i;
                    String s3 = s2.substring(s.length());

                    arraylist.add(new Profiler.Result(s3, d0, d1));
                }
            }

            iterator1 = this.field_76324_e.keySet().iterator();

            while (iterator1.hasNext()) {
                s2 = (String) iterator1.next();
                this.field_76324_e.put(s2, Long.valueOf(this.field_76324_e.get(s2).longValue() * 999L / 1000L));
            }

            if (k > f) {
                arraylist.add(new Profiler.Result("unspecified", (k - f) * 100.0D / k, (k - f) * 100.0D / i));
            }

            Collections.sort(arraylist);
            arraylist.add(0, new Profiler.Result(s, 100.0D, k * 100.0D / i));
            return arraylist;
        }
    }

    public void func_76318_c(String s) {
        if (!ENABLED) return;  // CraftBukkit
        this.func_76319_b();
        this.func_76320_a(s);
    }

    public String func_76322_c() {
        if (!ENABLED) return "[DISABLED]";  // CraftBukkit
        return this.field_76325_b.isEmpty() ? "[UNKNOWN]" : (String) this.field_76325_b.get(this.field_76325_b.size() - 1);
    }

    public static final class Result implements Comparable<Profiler.Result> {

        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;

        public Result(String s, double d0, double d1) {
            this.field_76331_c = s;
            this.field_76332_a = d0;
            this.field_76330_b = d1;
        }

        @Override
        public int compareTo(Profiler.Result methodprofiler_profilerinfo) {
            return methodprofiler_profilerinfo.field_76332_a < this.field_76332_a ? -1 : (methodprofiler_profilerinfo.field_76332_a > this.field_76332_a ? 1 : methodprofiler_profilerinfo.field_76331_c.compareTo(this.field_76331_c));
        }
    }
}

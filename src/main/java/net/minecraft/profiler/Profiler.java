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
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<String> sectionList = Lists.newArrayList();
    private final List<Long> timestampList = Lists.newArrayList();
    public boolean profilingEnabled;
    private String profilingSection = "";
    private final Map<String, Long> profilingMap = Maps.newHashMap();

    public Profiler() {}

    public void clearProfiling() {
        if (!ENABLED) return;  // CraftBukkit
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
    }

    public void startSection(String s) {
        if (!ENABLED) return;  // CraftBukkit
        if (this.profilingEnabled) {
            if (!this.profilingSection.isEmpty()) {
                this.profilingSection = this.profilingSection + ".";
            }

            this.profilingSection = this.profilingSection + s;
            this.sectionList.add(this.profilingSection);
            this.timestampList.add(Long.valueOf(System.nanoTime()));
        }
    }

    public void func_194340_a(Supplier<String> supplier) {
        if (!ENABLED) return;  // CraftBukkit
        if (this.profilingEnabled) {
            this.startSection((String) supplier.get());
        }
    }

    public void endSection() {
        if (!ENABLED) return;  // CraftBukkit
        if (this.profilingEnabled) {
            long i = System.nanoTime();
            long j = ((Long) this.timestampList.remove(this.timestampList.size() - 1)).longValue();

            this.sectionList.remove(this.sectionList.size() - 1);
            long k = i - j;

            if (this.profilingMap.containsKey(this.profilingSection)) {
                this.profilingMap.put(this.profilingSection, Long.valueOf(((Long) this.profilingMap.get(this.profilingSection)).longValue() + k));
            } else {
                this.profilingMap.put(this.profilingSection, Long.valueOf(k));
            }

            if (k > 100000000L) {
                Profiler.LOGGER.warn("Something\'s taking too long! \'{}\' took aprox {} ms", this.profilingSection, Double.valueOf((double) k / 1000000.0D));
            }

            this.profilingSection = this.sectionList.isEmpty() ? "" : (String) this.sectionList.get(this.sectionList.size() - 1);
        }
    }

    public List<Profiler.Result> getProfilingData(String s) {
        if (!ENABLED || !this.profilingEnabled) {  // CraftBukkit
            return Collections.emptyList();
        } else {
            long i = this.profilingMap.containsKey("root") ? ((Long) this.profilingMap.get("root")).longValue() : 0L;
            long j = this.profilingMap.containsKey(s) ? ((Long) this.profilingMap.get(s)).longValue() : -1L;
            ArrayList arraylist = Lists.newArrayList();

            if (!s.isEmpty()) {
                s = s + ".";
            }

            long k = 0L;
            Iterator iterator = this.profilingMap.keySet().iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                if (s1.length() > s.length() && s1.startsWith(s) && s1.indexOf(".", s.length() + 1) < 0) {
                    k += ((Long) this.profilingMap.get(s1)).longValue();
                }
            }

            float f = (float) k;

            if (k < j) {
                k = j;
            }

            if (i < k) {
                i = k;
            }

            Iterator iterator1 = this.profilingMap.keySet().iterator();

            String s2;

            while (iterator1.hasNext()) {
                s2 = (String) iterator1.next();
                if (s2.length() > s.length() && s2.startsWith(s) && s2.indexOf(".", s.length() + 1) < 0) {
                    long l = ((Long) this.profilingMap.get(s2)).longValue();
                    double d0 = (double) l * 100.0D / (double) k;
                    double d1 = (double) l * 100.0D / (double) i;
                    String s3 = s2.substring(s.length());

                    arraylist.add(new Profiler.Result(s3, d0, d1));
                }
            }

            iterator1 = this.profilingMap.keySet().iterator();

            while (iterator1.hasNext()) {
                s2 = (String) iterator1.next();
                this.profilingMap.put(s2, Long.valueOf(((Long) this.profilingMap.get(s2)).longValue() * 999L / 1000L));
            }

            if ((float) k > f) {
                arraylist.add(new Profiler.Result("unspecified", (double) ((float) k - f) * 100.0D / (double) k, (double) ((float) k - f) * 100.0D / (double) i));
            }

            Collections.sort(arraylist);
            arraylist.add(0, new Profiler.Result(s, 100.0D, (double) k * 100.0D / (double) i));
            return arraylist;
        }
    }

    public void endStartSection(String s) {
        if (!ENABLED) return;  // CraftBukkit
        this.endSection();
        this.startSection(s);
    }

    public String getNameOfLastSection() {
        if (!ENABLED) return "[DISABLED]";  // CraftBukkit
        return this.sectionList.isEmpty() ? "[UNKNOWN]" : (String) this.sectionList.get(this.sectionList.size() - 1);
    }

    public static final class Result implements Comparable<Profiler.Result> {

        public double usePercentage;
        public double totalUsePercentage;
        public String profilerName;

        public Result(String s, double d0, double d1) {
            this.profilerName = s;
            this.usePercentage = d0;
            this.totalUsePercentage = d1;
        }

        public int compareTo(Profiler.Result methodprofiler_profilerinfo) {
            return methodprofiler_profilerinfo.usePercentage < this.usePercentage ? -1 : (methodprofiler_profilerinfo.usePercentage > this.usePercentage ? 1 : methodprofiler_profilerinfo.profilerName.compareTo(this.profilerName));
        }

        public int compareTo(Profiler.Result object) { // CraftBukkit: decompile error
            return this.compareTo((Profiler.Result) object);
        }
    }
}

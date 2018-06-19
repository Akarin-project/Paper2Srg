package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;

public class CrashReport {

    private static final Logger field_147150_a = LogManager.getLogger();
    private final String field_71513_a;
    private final Throwable field_71511_b;
    private final CrashReportCategory field_85061_c = new CrashReportCategory(this, "System Details");
    private final List<CrashReportCategory> field_71512_c = Lists.newArrayList();
    private File field_71510_d;
    private boolean field_85059_f = true;
    private StackTraceElement[] field_85060_g = new StackTraceElement[0];

    public CrashReport(String s, Throwable throwable) {
        this.field_71513_a = s;
        this.field_71511_b = throwable;
        this.func_71504_g();
    }

    private void func_71504_g() {
        this.field_85061_c.func_189529_a("Minecraft Version", new ICrashReportDetail() {
            public String a() {
                return "1.12.2";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.field_85061_c.func_189529_a("Operating System", new ICrashReportDetail() {
            public String a() {
                return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.field_85061_c.func_189529_a("Java Version", new ICrashReportDetail() {
            public String a() {
                return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.field_85061_c.func_189529_a("Java VM Version", new ICrashReportDetail() {
            public String a() {
                return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.field_85061_c.func_189529_a("Memory", new ICrashReportDetail() {
            public String a() {
                Runtime runtime = Runtime.getRuntime();
                long i = runtime.maxMemory();
                long j = runtime.totalMemory();
                long k = runtime.freeMemory();
                long l = i / 1024L / 1024L;
                long i1 = j / 1024L / 1024L;
                long j1 = k / 1024L / 1024L;

                return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.field_85061_c.func_189529_a("JVM Flags", new ICrashReportDetail() {
            public String a() {
                RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
                List list = runtimemxbean.getInputArguments();
                int i = 0;
                StringBuilder stringbuilder = new StringBuilder();
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();

                    if (s.startsWith("-X")) {
                        if (i++ > 0) {
                            stringbuilder.append(" ");
                        }

                        stringbuilder.append(s);
                    }
                }

                return String.format("%d total; %s", new Object[] { Integer.valueOf(i), stringbuilder.toString()});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.field_85061_c.func_189529_a("IntCache", new ICrashReportDetail() {
            public String a() throws Exception {
                return IntCache.func_85144_b();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.field_85061_c.func_189529_a("CraftBukkit Information", (ICrashReportDetail) new org.bukkit.craftbukkit.CraftCrashReport()); // CraftBukkit
    }

    public String func_71501_a() {
        return this.field_71513_a;
    }

    public Throwable func_71505_b() {
        return this.field_71511_b;
    }

    public void func_71506_a(StringBuilder stringbuilder) {
        if ((this.field_85060_g == null || this.field_85060_g.length <= 0) && !this.field_71512_c.isEmpty()) {
            this.field_85060_g = (StackTraceElement[]) ArrayUtils.subarray(((CrashReportCategory) this.field_71512_c.get(0)).func_147152_a(), 0, 1);
        }

        if (this.field_85060_g != null && this.field_85060_g.length > 0) {
            stringbuilder.append("-- Head --\n");
            stringbuilder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
            stringbuilder.append("Stacktrace:\n");
            StackTraceElement[] astacktraceelement = this.field_85060_g;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j) {
                StackTraceElement stacktraceelement = astacktraceelement[j];

                stringbuilder.append("\t").append("at ").append(stacktraceelement);
                stringbuilder.append("\n");
            }

            stringbuilder.append("\n");
        }

        Iterator iterator = this.field_71512_c.iterator();

        while (iterator.hasNext()) {
            CrashReportCategory crashreportsystemdetails = (CrashReportCategory) iterator.next();

            crashreportsystemdetails.func_85072_a(stringbuilder);
            stringbuilder.append("\n\n");
        }

        this.field_85061_c.func_85072_a(stringbuilder);
    }

    public String func_71498_d() {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        Object object = this.field_71511_b;

        if (((Throwable) object).getMessage() == null) {
            if (object instanceof NullPointerException) {
                object = new NullPointerException(this.field_71513_a);
            } else if (object instanceof StackOverflowError) {
                object = new StackOverflowError(this.field_71513_a);
            } else if (object instanceof OutOfMemoryError) {
                object = new OutOfMemoryError(this.field_71513_a);
            }

            ((Throwable) object).setStackTrace(this.field_71511_b.getStackTrace());
        }

        String s = ((Throwable) object).toString();

        try {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter(stringwriter);
            ((Throwable) object).printStackTrace(printwriter);
            s = stringwriter.toString();
        } finally {
            IOUtils.closeQuietly(stringwriter);
            IOUtils.closeQuietly(printwriter);
        }

        return s;
    }

    public String func_71502_e() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("---- Minecraft Crash Report ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(func_71503_h());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.field_71513_a);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.func_71498_d());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; ++i) {
            stringbuilder.append("-");
        }

        stringbuilder.append("\n\n");
        this.func_71506_a(stringbuilder);
        return stringbuilder.toString();
    }

    public boolean func_147149_a(File file) {
        if (this.field_71510_d != null) {
            return false;
        } else {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            OutputStreamWriter outputstreamwriter = null;

            boolean flag;

            try {
                outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                outputstreamwriter.write(this.func_71502_e());
                this.field_71510_d = file;
                boolean flag1 = true;

                return flag1;
            } catch (Throwable throwable) {
                CrashReport.field_147150_a.error("Could not save crash report to {}", file, throwable);
                flag = false;
            } finally {
                IOUtils.closeQuietly(outputstreamwriter);
            }

            return flag;
        }
    }

    public CrashReportCategory func_85056_g() {
        return this.field_85061_c;
    }

    public CrashReportCategory func_85058_a(String s) {
        return this.func_85057_a(s, 1);
    }

    public CrashReportCategory func_85057_a(String s, int i) {
        CrashReportCategory crashreportsystemdetails = new CrashReportCategory(this, s);

        if (this.field_85059_f) {
            int j = crashreportsystemdetails.func_85073_a(i);
            StackTraceElement[] astacktraceelement = this.field_71511_b.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement1 = null;
            int k = astacktraceelement.length - j;

            if (k < 0) {
                System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + j + ")");
            }

            if (astacktraceelement != null && 0 <= k && k < astacktraceelement.length) {
                stacktraceelement = astacktraceelement[k];
                if (astacktraceelement.length + 1 - j < astacktraceelement.length) {
                    stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - j];
                }
            }

            this.field_85059_f = crashreportsystemdetails.func_85069_a(stacktraceelement, stacktraceelement1);
            if (j > 0 && !this.field_71512_c.isEmpty()) {
                CrashReportCategory crashreportsystemdetails1 = (CrashReportCategory) this.field_71512_c.get(this.field_71512_c.size() - 1);

                crashreportsystemdetails1.func_85070_b(j);
            } else if (astacktraceelement != null && astacktraceelement.length >= j && 0 <= k && k < astacktraceelement.length) {
                this.field_85060_g = new StackTraceElement[k];
                System.arraycopy(astacktraceelement, 0, this.field_85060_g, 0, this.field_85060_g.length);
            } else {
                this.field_85059_f = false;
            }
        }

        this.field_71512_c.add(crashreportsystemdetails);
        return crashreportsystemdetails;
    }

    private static String func_71503_h() {
        String[] astring = new String[] { "Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

        try {
            return astring[(int) (System.nanoTime() % (long) astring.length)];
        } catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport func_85055_a(Throwable throwable, String s) {
        CrashReport crashreport;

        if (throwable instanceof ReportedException) {
            crashreport = ((ReportedException) throwable).func_71575_a();
        } else {
            crashreport = new CrashReport(s, throwable);
        }

        return crashreport;
    }
}

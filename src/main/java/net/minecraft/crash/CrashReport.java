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

    private static final Logger LOGGER = LogManager.getLogger();
    private final String description;
    private final Throwable cause;
    private final CrashReportCategory systemDetailsCategory = new CrashReportCategory(this, "System Details");
    private final List<CrashReportCategory> crashReportSections = Lists.newArrayList();
    private File crashReportFile;
    private boolean firstCategoryInCrashReport = true;
    private StackTraceElement[] stacktrace = new StackTraceElement[0];

    public CrashReport(String s, Throwable throwable) {
        this.description = s;
        this.cause = throwable;
        this.populateEnvironment();
    }

    private void populateEnvironment() {
        this.systemDetailsCategory.addDetail("Minecraft Version", new ICrashReportDetail() {
            public String a() {
                return "1.12.2";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.systemDetailsCategory.addDetail("Operating System", new ICrashReportDetail() {
            public String a() {
                return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.systemDetailsCategory.addDetail("Java Version", new ICrashReportDetail() {
            public String a() {
                return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.systemDetailsCategory.addDetail("Java VM Version", new ICrashReportDetail() {
            public String a() {
                return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.systemDetailsCategory.addDetail("Memory", new ICrashReportDetail() {
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
        this.systemDetailsCategory.addDetail("JVM Flags", new ICrashReportDetail() {
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
        this.systemDetailsCategory.addDetail("IntCache", new ICrashReportDetail() {
            public String a() throws Exception {
                return IntCache.getCacheSizes();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        this.systemDetailsCategory.addDetail("CraftBukkit Information", (ICrashReportDetail) new org.bukkit.craftbukkit.CraftCrashReport()); // CraftBukkit
    }

    public String getDescription() {
        return this.description;
    }

    public Throwable getCrashCause() {
        return this.cause;
    }

    public void getSectionsInStringBuilder(StringBuilder stringbuilder) {
        if ((this.stacktrace == null || this.stacktrace.length <= 0) && !this.crashReportSections.isEmpty()) {
            this.stacktrace = (StackTraceElement[]) ArrayUtils.subarray(((CrashReportCategory) this.crashReportSections.get(0)).getStackTrace(), 0, 1);
        }

        if (this.stacktrace != null && this.stacktrace.length > 0) {
            stringbuilder.append("-- Head --\n");
            stringbuilder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
            stringbuilder.append("Stacktrace:\n");
            StackTraceElement[] astacktraceelement = this.stacktrace;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j) {
                StackTraceElement stacktraceelement = astacktraceelement[j];

                stringbuilder.append("\t").append("at ").append(stacktraceelement);
                stringbuilder.append("\n");
            }

            stringbuilder.append("\n");
        }

        Iterator iterator = this.crashReportSections.iterator();

        while (iterator.hasNext()) {
            CrashReportCategory crashreportsystemdetails = (CrashReportCategory) iterator.next();

            crashreportsystemdetails.appendToStringBuilder(stringbuilder);
            stringbuilder.append("\n\n");
        }

        this.systemDetailsCategory.appendToStringBuilder(stringbuilder);
    }

    public String getCauseStackTraceOrString() {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        Object object = this.cause;

        if (((Throwable) object).getMessage() == null) {
            if (object instanceof NullPointerException) {
                object = new NullPointerException(this.description);
            } else if (object instanceof StackOverflowError) {
                object = new StackOverflowError(this.description);
            } else if (object instanceof OutOfMemoryError) {
                object = new OutOfMemoryError(this.description);
            }

            ((Throwable) object).setStackTrace(this.cause.getStackTrace());
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

    public String getCompleteReport() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("---- Minecraft Crash Report ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.description);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; ++i) {
            stringbuilder.append("-");
        }

        stringbuilder.append("\n\n");
        this.getSectionsInStringBuilder(stringbuilder);
        return stringbuilder.toString();
    }

    public boolean saveToFile(File file) {
        if (this.crashReportFile != null) {
            return false;
        } else {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            OutputStreamWriter outputstreamwriter = null;

            boolean flag;

            try {
                outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                outputstreamwriter.write(this.getCompleteReport());
                this.crashReportFile = file;
                boolean flag1 = true;

                return flag1;
            } catch (Throwable throwable) {
                CrashReport.LOGGER.error("Could not save crash report to {}", file, throwable);
                flag = false;
            } finally {
                IOUtils.closeQuietly(outputstreamwriter);
            }

            return flag;
        }
    }

    public CrashReportCategory getCategory() {
        return this.systemDetailsCategory;
    }

    public CrashReportCategory makeCategory(String s) {
        return this.makeCategoryDepth(s, 1);
    }

    public CrashReportCategory makeCategoryDepth(String s, int i) {
        CrashReportCategory crashreportsystemdetails = new CrashReportCategory(this, s);

        if (this.firstCategoryInCrashReport) {
            int j = crashreportsystemdetails.getPrunedStackTrace(i);
            StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
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

            this.firstCategoryInCrashReport = crashreportsystemdetails.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
            if (j > 0 && !this.crashReportSections.isEmpty()) {
                CrashReportCategory crashreportsystemdetails1 = (CrashReportCategory) this.crashReportSections.get(this.crashReportSections.size() - 1);

                crashreportsystemdetails1.trimStackTraceEntriesFromBottom(j);
            } else if (astacktraceelement != null && astacktraceelement.length >= j && 0 <= k && k < astacktraceelement.length) {
                this.stacktrace = new StackTraceElement[k];
                System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
            } else {
                this.firstCategoryInCrashReport = false;
            }
        }

        this.crashReportSections.add(crashreportsystemdetails);
        return crashreportsystemdetails;
    }

    private static String getWittyComment() {
        String[] astring = new String[] { "Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

        try {
            return astring[(int) (System.nanoTime() % (long) astring.length)];
        } catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport makeCrashReport(Throwable throwable, String s) {
        CrashReport crashreport;

        if (throwable instanceof ReportedException) {
            crashreport = ((ReportedException) throwable).getCrashReport();
        } else {
            crashreport = new CrashReport(s, throwable);
        }

        return crashreport;
    }
}

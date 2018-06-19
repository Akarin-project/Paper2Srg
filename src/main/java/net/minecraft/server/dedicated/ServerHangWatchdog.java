package net.minecraft.server.dedicated;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;

public class ServerHangWatchdog implements Runnable {

    private static final Logger field_180251_a = LogManager.getLogger();
    private final DedicatedServer field_180249_b;
    private final long field_180250_c;

    public ServerHangWatchdog(DedicatedServer dedicatedserver) {
        this.field_180249_b = dedicatedserver;
        this.field_180250_c = dedicatedserver.func_175593_aQ();
    }

    public void run() {
        while (this.field_180249_b.func_71278_l()) {
            long i = this.field_180249_b.func_175587_aJ();
            long j = MinecraftServer.func_130071_aq();
            long k = j - i;

            if (k > this.field_180250_c) {
                ServerHangWatchdog.field_180251_a.fatal("A single server tick took {} seconds (should be max {})", String.format("%.2f", new Object[] { Float.valueOf((float) k / 1000.0F)}), String.format("%.2f", new Object[] { Float.valueOf(0.05F)}));
                ServerHangWatchdog.field_180251_a.fatal("Considering it to be crashed, server will forcibly shutdown.");
                ThreadMXBean threadmxbean = ManagementFactory.getThreadMXBean();
                ThreadInfo[] athreadinfo = threadmxbean.dumpAllThreads(true, true);
                StringBuilder stringbuilder = new StringBuilder();
                Error error = new Error();
                ThreadInfo[] athreadinfo1 = athreadinfo;
                int l = athreadinfo.length;

                for (int i1 = 0; i1 < l; ++i1) {
                    ThreadInfo threadinfo = athreadinfo1[i1];

                    if (threadinfo.getThreadId() == this.field_180249_b.func_175583_aK().getId()) {
                        error.setStackTrace(threadinfo.getStackTrace());
                    }

                    stringbuilder.append(threadinfo);
                    stringbuilder.append("\n");
                }

                CrashReport crashreport = new CrashReport("Watching Server", error);

                this.field_180249_b.func_71230_b(crashreport);
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Thread Dump");

                crashreportsystemdetails.func_71507_a("Threads", (Object) stringbuilder);
                File file = new File(new File(this.field_180249_b.func_71238_n(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

                if (crashreport.func_147149_a(file)) {
                    ServerHangWatchdog.field_180251_a.error("This crash report has been saved to: {}", file.getAbsolutePath());
                } else {
                    ServerHangWatchdog.field_180251_a.error("We were unable to save this crash report to disk.");
                }

                this.func_180248_a();
            }

            try {
                Thread.sleep(i + this.field_180250_c - j);
            } catch (InterruptedException interruptedexception) {
                ;
            }
        }

    }

    private void func_180248_a() {
        try {
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                public void run() {
                    Runtime.getRuntime().halt(1);
                }
            }, 10000L);
            System.exit(1);
        } catch (Throwable throwable) {
            Runtime.getRuntime().halt(1);
        }

    }
}

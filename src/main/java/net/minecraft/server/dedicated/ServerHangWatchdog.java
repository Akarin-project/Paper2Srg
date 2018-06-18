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

    private static final Logger LOGGER = LogManager.getLogger();
    private final DedicatedServer server;
    private final long maxTickTime;

    public ServerHangWatchdog(DedicatedServer dedicatedserver) {
        this.server = dedicatedserver;
        this.maxTickTime = dedicatedserver.getMaxTickTime();
    }

    public void run() {
        while (this.server.isServerRunning()) {
            long i = this.server.getCurrentTime();
            long j = MinecraftServer.getCurrentTimeMillis();
            long k = j - i;

            if (k > this.maxTickTime) {
                ServerHangWatchdog.LOGGER.fatal("A single server tick took {} seconds (should be max {})", String.format("%.2f", new Object[] { Float.valueOf((float) k / 1000.0F)}), String.format("%.2f", new Object[] { Float.valueOf(0.05F)}));
                ServerHangWatchdog.LOGGER.fatal("Considering it to be crashed, server will forcibly shutdown.");
                ThreadMXBean threadmxbean = ManagementFactory.getThreadMXBean();
                ThreadInfo[] athreadinfo = threadmxbean.dumpAllThreads(true, true);
                StringBuilder stringbuilder = new StringBuilder();
                Error error = new Error();
                ThreadInfo[] athreadinfo1 = athreadinfo;
                int l = athreadinfo.length;

                for (int i1 = 0; i1 < l; ++i1) {
                    ThreadInfo threadinfo = athreadinfo1[i1];

                    if (threadinfo.getThreadId() == this.server.getServerThread().getId()) {
                        error.setStackTrace(threadinfo.getStackTrace());
                    }

                    stringbuilder.append(threadinfo);
                    stringbuilder.append("\n");
                }

                CrashReport crashreport = new CrashReport("Watching Server", error);

                this.server.addServerInfoToCrashReport(crashreport);
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Thread Dump");

                crashreportsystemdetails.addCrashSection("Threads", (Object) stringbuilder);
                File file = new File(new File(this.server.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

                if (crashreport.saveToFile(file)) {
                    ServerHangWatchdog.LOGGER.error("This crash report has been saved to: {}", file.getAbsolutePath());
                } else {
                    ServerHangWatchdog.LOGGER.error("We were unable to save this crash report to disk.");
                }

                this.scheduleHalt();
            }

            try {
                Thread.sleep(i + this.maxTickTime - j);
            } catch (InterruptedException interruptedexception) {
                ;
            }
        }

    }

    private void scheduleHalt() {
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

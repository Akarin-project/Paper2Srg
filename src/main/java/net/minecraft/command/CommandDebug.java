package net.minecraft.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandDebug extends CommandBase {

    private static final Logger LOGGER = LogManager.getLogger();
    private long profileStartTime;
    private int profileStartTick;

    public CommandDebug() {}

    public String getName() {
        return "debug";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.debug.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        // CraftBukkit start - only allow use when enabled (so that no blank profile results occur)
        if (!minecraftserver.profiler.ENABLED) {
            icommandlistener.sendMessage(new TextComponentString("Vanilla debug profiling is disabled."));
            icommandlistener.sendMessage(new TextComponentString("To enable, restart the server with `-DenableDebugMethodProfiler=true' before `-jar'."));
            icommandlistener.sendMessage(new TextComponentString("Use `/timings' for plugin timings."));
            return;
        }
        // CraftBukkit end
        if (astring.length < 1) {
            throw new WrongUsageException("commands.debug.usage", new Object[0]);
        } else {
            if ("start".equals(astring[0])) {
                if (astring.length != 1) {
                    throw new WrongUsageException("commands.debug.usage", new Object[0]);
                }

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.debug.start", new Object[0]);
                minecraftserver.enableProfiling();
                this.profileStartTime = MinecraftServer.getCurrentTimeMillis();
                this.profileStartTick = minecraftserver.getTickCounter();
            } else {
                if (!"stop".equals(astring[0])) {
                    throw new WrongUsageException("commands.debug.usage", new Object[0]);
                }

                if (astring.length != 1) {
                    throw new WrongUsageException("commands.debug.usage", new Object[0]);
                }

                if (!minecraftserver.profiler.profilingEnabled) {
                    throw new CommandException("commands.debug.notStarted", new Object[0]);
                }

                long i = MinecraftServer.getCurrentTimeMillis();
                int j = minecraftserver.getTickCounter();
                long k = i - this.profileStartTime;
                int l = j - this.profileStartTick;

                this.saveProfilerResults(k, l, minecraftserver);
                minecraftserver.profiler.profilingEnabled = false;
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.debug.stop", new Object[] { String.format("%.2f", new Object[] { Float.valueOf((float) k / 1000.0F)}), Integer.valueOf(l)});
            }

        }
    }

    private void saveProfilerResults(long i, int j, MinecraftServer minecraftserver) {
        File file = new File(minecraftserver.getFile("debug"), "profile-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");

        file.getParentFile().mkdirs();
        OutputStreamWriter outputstreamwriter = null;

        try {
            outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            outputstreamwriter.write(this.getProfilerResults(i, j, minecraftserver));
        } catch (Throwable throwable) {
            CommandDebug.LOGGER.error("Could not save profiler results to {}", file, throwable);
        } finally {
            IOUtils.closeQuietly(outputstreamwriter);
        }

    }

    private String getProfilerResults(long i, int j, MinecraftServer minecraftserver) {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("---- Minecraft Profiler Results ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time span: ").append(i).append(" ms\n");
        stringbuilder.append("Tick span: ").append(j).append(" ticks\n");
        stringbuilder.append("// This is approximately ").append(String.format("%.2f", new Object[] { Float.valueOf((float) j / ((float) i / 1000.0F))})).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.appendProfilerResults(0, "root", stringbuilder, minecraftserver);
        stringbuilder.append("--- END PROFILE DUMP ---\n\n");
        return stringbuilder.toString();
    }

    private void appendProfilerResults(int i, String s, StringBuilder stringbuilder, MinecraftServer minecraftserver) {
        List list = minecraftserver.profiler.getProfilingData(s);

        if (list != null && list.size() >= 3) {
            for (int j = 1; j < list.size(); ++j) {
                Profiler.Result methodprofiler_profilerinfo = (Profiler.Result) list.get(j);

                stringbuilder.append(String.format("[%02d] ", new Object[] { Integer.valueOf(i)}));

                for (int k = 0; k < i; ++k) {
                    stringbuilder.append("|   ");
                }

                stringbuilder.append(methodprofiler_profilerinfo.profilerName).append(" - ").append(String.format("%.2f", new Object[] { Double.valueOf(methodprofiler_profilerinfo.usePercentage)})).append("%/").append(String.format("%.2f", new Object[] { Double.valueOf(methodprofiler_profilerinfo.totalUsePercentage)})).append("%\n");
                if (!"unspecified".equals(methodprofiler_profilerinfo.profilerName)) {
                    try {
                        this.appendProfilerResults(i + 1, s + "." + methodprofiler_profilerinfo.profilerName, stringbuilder, minecraftserver);
                    } catch (Exception exception) {
                        stringbuilder.append("[[ EXCEPTION ").append(exception).append(" ]]");
                    }
                }
            }

        }
    }

    private static String getWittyComment() {
        String[] astring = new String[] { "Shiny numbers!", "Am I not running fast enough? :(", "I\'m working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it\'ll have more motivation to work faster! Poor server."};

        try {
            return astring[(int) (System.nanoTime() % (long) astring.length)];
        } catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "start", "stop"}) : Collections.<String>emptyList(); // CraftBukkit - decompile error
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}

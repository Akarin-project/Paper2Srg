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

    private static final Logger field_147208_a = LogManager.getLogger();
    private long field_147206_b;
    private int field_147207_c;

    public CommandDebug() {}

    public String func_71517_b() {
        return "debug";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.debug.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        // CraftBukkit start - only allow use when enabled (so that no blank profile results occur)
        if (!minecraftserver.field_71304_b.ENABLED) {
            icommandlistener.func_145747_a(new TextComponentString("Vanilla debug profiling is disabled."));
            icommandlistener.func_145747_a(new TextComponentString("To enable, restart the server with `-DenableDebugMethodProfiler=true' before `-jar'."));
            icommandlistener.func_145747_a(new TextComponentString("Use `/timings' for plugin timings."));
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

                func_152373_a(icommandlistener, (ICommand) this, "commands.debug.start", new Object[0]);
                minecraftserver.func_71223_ag();
                this.field_147206_b = MinecraftServer.func_130071_aq();
                this.field_147207_c = minecraftserver.func_71259_af();
            } else {
                if (!"stop".equals(astring[0])) {
                    throw new WrongUsageException("commands.debug.usage", new Object[0]);
                }

                if (astring.length != 1) {
                    throw new WrongUsageException("commands.debug.usage", new Object[0]);
                }

                if (!minecraftserver.field_71304_b.field_76327_a) {
                    throw new CommandException("commands.debug.notStarted", new Object[0]);
                }

                long i = MinecraftServer.func_130071_aq();
                int j = minecraftserver.func_71259_af();
                long k = i - this.field_147206_b;
                int l = j - this.field_147207_c;

                this.func_184894_a(k, l, minecraftserver);
                minecraftserver.field_71304_b.field_76327_a = false;
                func_152373_a(icommandlistener, (ICommand) this, "commands.debug.stop", new Object[] { String.format("%.2f", new Object[] { Float.valueOf((float) k / 1000.0F)}), Integer.valueOf(l)});
            }

        }
    }

    private void func_184894_a(long i, int j, MinecraftServer minecraftserver) {
        File file = new File(minecraftserver.func_71209_f("debug"), "profile-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");

        file.getParentFile().mkdirs();
        OutputStreamWriter outputstreamwriter = null;

        try {
            outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            outputstreamwriter.write(this.func_184893_b(i, j, minecraftserver));
        } catch (Throwable throwable) {
            CommandDebug.field_147208_a.error("Could not save profiler results to {}", file, throwable);
        } finally {
            IOUtils.closeQuietly(outputstreamwriter);
        }

    }

    private String func_184893_b(long i, int j, MinecraftServer minecraftserver) {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("---- Minecraft Profiler Results ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(func_147203_d());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time span: ").append(i).append(" ms\n");
        stringbuilder.append("Tick span: ").append(j).append(" ticks\n");
        stringbuilder.append("// This is approximately ").append(String.format("%.2f", new Object[] { Float.valueOf((float) j / ((float) i / 1000.0F))})).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.func_184895_a(0, "root", stringbuilder, minecraftserver);
        stringbuilder.append("--- END PROFILE DUMP ---\n\n");
        return stringbuilder.toString();
    }

    private void func_184895_a(int i, String s, StringBuilder stringbuilder, MinecraftServer minecraftserver) {
        List list = minecraftserver.field_71304_b.func_76321_b(s);

        if (list != null && list.size() >= 3) {
            for (int j = 1; j < list.size(); ++j) {
                Profiler.Result methodprofiler_profilerinfo = (Profiler.Result) list.get(j);

                stringbuilder.append(String.format("[%02d] ", new Object[] { Integer.valueOf(i)}));

                for (int k = 0; k < i; ++k) {
                    stringbuilder.append("|   ");
                }

                stringbuilder.append(methodprofiler_profilerinfo.field_76331_c).append(" - ").append(String.format("%.2f", new Object[] { Double.valueOf(methodprofiler_profilerinfo.field_76332_a)})).append("%/").append(String.format("%.2f", new Object[] { Double.valueOf(methodprofiler_profilerinfo.field_76330_b)})).append("%\n");
                if (!"unspecified".equals(methodprofiler_profilerinfo.field_76331_c)) {
                    try {
                        this.func_184895_a(i + 1, s + "." + methodprofiler_profilerinfo.field_76331_c, stringbuilder, minecraftserver);
                    } catch (Exception exception) {
                        stringbuilder.append("[[ EXCEPTION ").append(exception).append(" ]]");
                    }
                }
            }

        }
    }

    private static String func_147203_d() {
        String[] astring = new String[] { "Shiny numbers!", "Am I not running fast enough? :(", "I\'m working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it\'ll have more motivation to work faster! Poor server."};

        try {
            return astring[(int) (System.nanoTime() % (long) astring.length)];
        } catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "start", "stop"}) : Collections.<String>emptyList(); // CraftBukkit - decompile error
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase {

    public CommandTime() {}

    public String getName() {
        return "time";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.time.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 1) {
            int i;

            if ("set".equals(astring[0])) {
                if ("day".equals(astring[1])) {
                    i = 1000;
                } else if ("night".equals(astring[1])) {
                    i = 13000;
                } else {
                    i = parseInt(astring[1], 0);
                }

                this.setAllWorldTimes(minecraftserver, i);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.time.set", new Object[] { Integer.valueOf(i)});
                return;
            }

            if ("add".equals(astring[0])) {
                i = parseInt(astring[1], 0);
                this.incrementAllWorldTimes(minecraftserver, i);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.time.added", new Object[] { Integer.valueOf(i)});
                return;
            }

            if ("query".equals(astring[0])) {
                if ("daytime".equals(astring[1])) {
                    i = (int) (icommandlistener.getEntityWorld().getWorldTime() % 24000L);
                    icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.time.query", new Object[] { Integer.valueOf(i)});
                    return;
                }

                if ("day".equals(astring[1])) {
                    i = (int) (icommandlistener.getEntityWorld().getWorldTime() / 24000L % 2147483647L);
                    icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.time.query", new Object[] { Integer.valueOf(i)});
                    return;
                }

                if ("gametime".equals(astring[1])) {
                    i = (int) (icommandlistener.getEntityWorld().getTotalWorldTime() % 2147483647L);
                    icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.time.query", new Object[] { Integer.valueOf(i)});
                    return;
                }
            }
        }

        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "set", "add", "query"}) : (astring.length == 2 && "set".equals(astring[0]) ? getListOfStringsMatchingLastWord(astring, new String[] { "day", "night"}) : (astring.length == 2 && "query".equals(astring[0]) ? getListOfStringsMatchingLastWord(astring, new String[] { "daytime", "gametime", "day"}) : Collections.emptyList()));
    }

    protected void setAllWorldTimes(MinecraftServer minecraftserver, int i) {
        for (int j = 0; j < minecraftserver.worlds.length; ++j) {
            minecraftserver.worlds[j].setWorldTime((long) i);
        }

    }

    protected void incrementAllWorldTimes(MinecraftServer minecraftserver, int i) {
        for (int j = 0; j < minecraftserver.worlds.length; ++j) {
            WorldServer worldserver = minecraftserver.worlds[j];

            worldserver.setWorldTime(worldserver.getWorldTime() + (long) i);
        }

    }
}

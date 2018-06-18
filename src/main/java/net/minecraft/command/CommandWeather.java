package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase {

    public CommandWeather() {}

    public String getName() {
        return "weather";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.weather.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring.length <= 2) {
            int i = (300 + (new Random()).nextInt(600)) * 20;

            if (astring.length >= 2) {
                i = parseInt(astring[1], 1, 1000000) * 20;
            }

            WorldServer worldserver = minecraftserver.worlds[0];
            WorldInfo worlddata = worldserver.getWorldInfo();

            if ("clear".equalsIgnoreCase(astring[0])) {
                worlddata.setCleanWeatherTime(i);
                worlddata.setRainTime(0);
                worlddata.setThunderTime(0);
                worlddata.setRaining(false);
                worlddata.setThundering(false);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.weather.clear", new Object[0]);
            } else if ("rain".equalsIgnoreCase(astring[0])) {
                worlddata.setCleanWeatherTime(0);
                worlddata.setRainTime(i);
                worlddata.setThunderTime(i);
                worlddata.setRaining(true);
                worlddata.setThundering(false);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.weather.rain", new Object[0]);
            } else {
                if (!"thunder".equalsIgnoreCase(astring[0])) {
                    throw new WrongUsageException("commands.weather.usage", new Object[0]);
                }

                worlddata.setCleanWeatherTime(0);
                worlddata.setRainTime(i);
                worlddata.setThunderTime(i);
                worlddata.setRaining(true);
                worlddata.setThundering(true);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.weather.thunder", new Object[0]);
            }

        } else {
            throw new WrongUsageException("commands.weather.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "clear", "rain", "thunder"}) : Collections.emptyList();
    }
}

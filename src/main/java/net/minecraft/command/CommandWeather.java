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

    public String func_71517_b() {
        return "weather";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.weather.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring.length <= 2) {
            int i = (300 + (new Random()).nextInt(600)) * 20;

            if (astring.length >= 2) {
                i = func_175764_a(astring[1], 1, 1000000) * 20;
            }

            WorldServer worldserver = minecraftserver.field_71305_c[0];
            WorldInfo worlddata = worldserver.func_72912_H();

            if ("clear".equalsIgnoreCase(astring[0])) {
                worlddata.func_176142_i(i);
                worlddata.func_76080_g(0);
                worlddata.func_76090_f(0);
                worlddata.func_76084_b(false);
                worlddata.func_76069_a(false);
                func_152373_a(icommandlistener, (ICommand) this, "commands.weather.clear", new Object[0]);
            } else if ("rain".equalsIgnoreCase(astring[0])) {
                worlddata.func_176142_i(0);
                worlddata.func_76080_g(i);
                worlddata.func_76090_f(i);
                worlddata.func_76084_b(true);
                worlddata.func_76069_a(false);
                func_152373_a(icommandlistener, (ICommand) this, "commands.weather.rain", new Object[0]);
            } else {
                if (!"thunder".equalsIgnoreCase(astring[0])) {
                    throw new WrongUsageException("commands.weather.usage", new Object[0]);
                }

                worlddata.func_176142_i(0);
                worlddata.func_76080_g(i);
                worlddata.func_76090_f(i);
                worlddata.func_76084_b(true);
                worlddata.func_76069_a(true);
                func_152373_a(icommandlistener, (ICommand) this, "commands.weather.thunder", new Object[0]);
            }

        } else {
            throw new WrongUsageException("commands.weather.usage", new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "clear", "rain", "thunder"}) : Collections.emptyList();
    }
}

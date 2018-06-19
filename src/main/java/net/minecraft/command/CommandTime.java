package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase {

    public CommandTime() {}

    public String func_71517_b() {
        return "time";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.time.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 1) {
            int i;

            if ("set".equals(astring[0])) {
                if ("day".equals(astring[1])) {
                    i = 1000;
                } else if ("night".equals(astring[1])) {
                    i = 13000;
                } else {
                    i = func_180528_a(astring[1], 0);
                }

                this.func_184929_a(minecraftserver, i);
                func_152373_a(icommandlistener, (ICommand) this, "commands.time.set", new Object[] { Integer.valueOf(i)});
                return;
            }

            if ("add".equals(astring[0])) {
                i = func_180528_a(astring[1], 0);
                this.func_184928_b(minecraftserver, i);
                func_152373_a(icommandlistener, (ICommand) this, "commands.time.added", new Object[] { Integer.valueOf(i)});
                return;
            }

            if ("query".equals(astring[0])) {
                if ("daytime".equals(astring[1])) {
                    i = (int) (icommandlistener.func_130014_f_().func_72820_D() % 24000L);
                    icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.time.query", new Object[] { Integer.valueOf(i)});
                    return;
                }

                if ("day".equals(astring[1])) {
                    i = (int) (icommandlistener.func_130014_f_().func_72820_D() / 24000L % 2147483647L);
                    icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.time.query", new Object[] { Integer.valueOf(i)});
                    return;
                }

                if ("gametime".equals(astring[1])) {
                    i = (int) (icommandlistener.func_130014_f_().func_82737_E() % 2147483647L);
                    icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.time.query", new Object[] { Integer.valueOf(i)});
                    return;
                }
            }
        }

        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "set", "add", "query"}) : (astring.length == 2 && "set".equals(astring[0]) ? func_71530_a(astring, new String[] { "day", "night"}) : (astring.length == 2 && "query".equals(astring[0]) ? func_71530_a(astring, new String[] { "daytime", "gametime", "day"}) : Collections.emptyList()));
    }

    protected void func_184929_a(MinecraftServer minecraftserver, int i) {
        for (int j = 0; j < minecraftserver.field_71305_c.length; ++j) {
            minecraftserver.field_71305_c[j].func_72877_b((long) i);
        }

    }

    protected void func_184928_b(MinecraftServer minecraftserver, int i) {
        for (int j = 0; j < minecraftserver.field_71305_c.length; ++j) {
            WorldServer worldserver = minecraftserver.field_71305_c[j];

            worldserver.func_72877_b(worldserver.func_72820_D() + (long) i);
        }

    }
}

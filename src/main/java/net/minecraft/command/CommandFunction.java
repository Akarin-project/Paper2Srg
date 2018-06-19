package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CommandFunction extends CommandBase {

    public CommandFunction() {}

    public String func_71517_b() {
        return "function";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.function.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length != 1 && astring.length != 3) {
            throw new WrongUsageException("commands.function.usage", new Object[0]);
        } else {
            ResourceLocation minecraftkey = new ResourceLocation(astring[0]);
            FunctionObject customfunction = minecraftserver.func_193030_aL().func_193058_a(minecraftkey);

            if (customfunction == null) {
                throw new CommandException("commands.function.unknown", new Object[] { minecraftkey});
            } else {
                if (astring.length == 3) {
                    String s = astring[1];
                    boolean flag;

                    if ("if".equals(s)) {
                        flag = true;
                    } else {
                        if (!"unless".equals(s)) {
                            throw new WrongUsageException("commands.function.usage", new Object[0]);
                        }

                        flag = false;
                    }

                    boolean flag1 = false;

                    try {
                        flag1 = !func_184890_c(minecraftserver, icommandlistener, astring[2]).isEmpty();
                    } catch (EntityNotFoundException exceptionentitynotfound) {
                        ;
                    }

                    if (flag != flag1) {
                        throw new CommandException("commands.function.skipped", new Object[] { minecraftkey});
                    }
                }

                int i = minecraftserver.func_193030_aL().func_194019_a(customfunction, CommandSenderWrapper.func_193998_a(icommandlistener).func_194000_i().func_193999_a(2).func_194001_a(false));

                func_152373_a(icommandlistener, (ICommand) this, "commands.function.success", new Object[] { minecraftkey, Integer.valueOf(i)});
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_175762_a(astring, (Collection) minecraftserver.func_193030_aL().func_193066_d().keySet()) : (astring.length == 2 ? func_71530_a(astring, new String[] { "if", "unless"}) : (astring.length == 3 ? func_71530_a(astring, minecraftserver.func_71213_z()) : Collections.emptyList()));
    }
}

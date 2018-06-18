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

    public String getName() {
        return "function";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.function.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length != 1 && astring.length != 3) {
            throw new WrongUsageException("commands.function.usage", new Object[0]);
        } else {
            ResourceLocation minecraftkey = new ResourceLocation(astring[0]);
            FunctionObject customfunction = minecraftserver.getFunctionManager().getFunction(minecraftkey);

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
                        flag1 = !getEntityList(minecraftserver, icommandlistener, astring[2]).isEmpty();
                    } catch (EntityNotFoundException exceptionentitynotfound) {
                        ;
                    }

                    if (flag != flag1) {
                        throw new CommandException("commands.function.skipped", new Object[] { minecraftkey});
                    }
                }

                int i = minecraftserver.getFunctionManager().execute(customfunction, CommandSenderWrapper.create(icommandlistener).computePositionVector().withPermissionLevel(2).withSendCommandFeedback(false));

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.function.success", new Object[] { minecraftkey, Integer.valueOf(i)});
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, (Collection) minecraftserver.getFunctionManager().getFunctions().keySet()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, new String[] { "if", "unless"}) : (astring.length == 3 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList()));
    }
}

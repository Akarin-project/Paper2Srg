package net.minecraft.command;
import net.minecraft.server.MinecraftServer;


public class CommandSetPlayerTimeout extends CommandBase {

    public CommandSetPlayerTimeout() {}

    public String getName() {
        return "setidletimeout";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.setidletimeout.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length != 1) {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        } else {
            int i = parseInt(astring[0], 0);

            minecraftserver.setPlayerIdleTimeout(i);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.setidletimeout.success", new Object[] { Integer.valueOf(i)});
        }
    }
}

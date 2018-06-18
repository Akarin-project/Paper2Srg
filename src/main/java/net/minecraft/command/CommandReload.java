package net.minecraft.command;
import net.minecraft.server.MinecraftServer;


public class CommandReload extends CommandBase {

    public CommandReload() {}

    public String getName() {
        return "reload";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.reload.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 0) {
            throw new WrongUsageException("commands.reload.usage", new Object[0]);
        } else {
            minecraftserver.reload();
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.reload.success", new Object[0]);
        }
    }
}

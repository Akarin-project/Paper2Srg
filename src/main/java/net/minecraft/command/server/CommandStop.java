package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;


public class CommandStop extends CommandBase {

    public CommandStop() {}

    public String getName() {
        return "stop";
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.stop.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (minecraftserver.worlds != null) {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.stop.start", new Object[0]);
        }

        minecraftserver.initiateShutdown();
    }
}

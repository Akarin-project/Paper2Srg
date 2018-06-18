package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;


public class CommandPublishLocalServer extends CommandBase {

    public CommandPublishLocalServer() {}

    public String getName() {
        return "publish";
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.publish.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        String s = minecraftserver.shareToLAN(GameType.SURVIVAL, false);

        if (s != null) {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.publish.started", new Object[] { s});
        } else {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.publish.failed", new Object[0]);
        }

    }
}

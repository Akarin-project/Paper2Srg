package net.minecraft.server.dedicated;
import net.minecraft.command.ICommandSender;


public class PendingCommand {

    public final String command;
    public final ICommandSender sender;

    public PendingCommand(String s, ICommandSender icommandlistener) {
        this.command = s;
        this.sender = icommandlistener;
    }
}

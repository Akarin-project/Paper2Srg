package net.minecraft.server.dedicated;
import net.minecraft.command.ICommandSender;


public class PendingCommand {

    public final String field_73702_a;
    public final ICommandSender field_73701_b;

    public PendingCommand(String s, ICommandSender icommandlistener) {
        this.field_73702_a = s;
        this.field_73701_b = icommandlistener;
    }
}

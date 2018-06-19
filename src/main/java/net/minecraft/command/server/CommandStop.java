package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;


public class CommandStop extends CommandBase {

    public CommandStop() {}

    public String func_71517_b() {
        return "stop";
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.stop.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (minecraftserver.field_71305_c != null) {
            func_152373_a(icommandlistener, (ICommand) this, "commands.stop.start", new Object[0]);
        }

        minecraftserver.func_71263_m();
    }
}

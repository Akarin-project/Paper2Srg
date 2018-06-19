package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;


public class CommandPublishLocalServer extends CommandBase {

    public CommandPublishLocalServer() {}

    public String func_71517_b() {
        return "publish";
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.publish.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        String s = minecraftserver.func_71206_a(GameType.SURVIVAL, false);

        if (s != null) {
            func_152373_a(icommandlistener, (ICommand) this, "commands.publish.started", new Object[] { s});
        } else {
            func_152373_a(icommandlistener, (ICommand) this, "commands.publish.failed", new Object[0]);
        }

    }
}

package net.minecraft.command;
import net.minecraft.server.MinecraftServer;


public class CommandReload extends CommandBase {

    public CommandReload() {}

    public String func_71517_b() {
        return "reload";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.reload.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 0) {
            throw new WrongUsageException("commands.reload.usage", new Object[0]);
        } else {
            minecraftserver.func_193031_aM();
            func_152373_a(icommandlistener, (ICommand) this, "commands.reload.success", new Object[0]);
        }
    }
}

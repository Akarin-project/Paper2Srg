package net.minecraft.command;
import net.minecraft.server.MinecraftServer;


public class CommandSetPlayerTimeout extends CommandBase {

    public CommandSetPlayerTimeout() {}

    public String func_71517_b() {
        return "setidletimeout";
    }

    public int func_82362_a() {
        return 3;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.setidletimeout.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length != 1) {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        } else {
            int i = func_180528_a(astring[0], 0);

            minecraftserver.func_143006_e(i);
            func_152373_a(icommandlistener, (ICommand) this, "commands.setidletimeout.success", new Object[] { Integer.valueOf(i)});
        }
    }
}

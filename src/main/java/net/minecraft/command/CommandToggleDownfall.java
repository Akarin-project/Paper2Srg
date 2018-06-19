package net.minecraft.command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;


public class CommandToggleDownfall extends CommandBase {

    public CommandToggleDownfall() {}

    public String func_71517_b() {
        return "toggledownfall";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.downfall.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        this.func_184930_a(minecraftserver);
        func_152373_a(icommandlistener, (ICommand) this, "commands.downfall.success", new Object[0]);
    }

    protected void func_184930_a(MinecraftServer minecraftserver) {
        WorldInfo worlddata = minecraftserver.field_71305_c[0].func_72912_H();

        worlddata.func_76084_b(!worlddata.func_76059_o());
    }
}

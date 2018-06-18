package net.minecraft.command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;


public class CommandToggleDownfall extends CommandBase {

    public CommandToggleDownfall() {}

    public String getName() {
        return "toggledownfall";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.downfall.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        this.toggleRainfall(minecraftserver);
        notifyCommandListener(icommandlistener, (ICommand) this, "commands.downfall.success", new Object[0]);
    }

    protected void toggleRainfall(MinecraftServer minecraftserver) {
        WorldInfo worlddata = minecraftserver.worlds[0].getWorldInfo();

        worlddata.setRaining(!worlddata.isRaining());
    }
}

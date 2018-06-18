package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;


public class CommandSaveOn extends CommandBase {

    public CommandSaveOn() {}

    public String getName() {
        return "save-on";
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.save-on.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        boolean flag = false;

        for (int i = 0; i < minecraftserver.worlds.length; ++i) {
            if (minecraftserver.worlds[i] != null) {
                WorldServer worldserver = minecraftserver.worlds[i];

                if (worldserver.disableLevelSaving) {
                    worldserver.disableLevelSaving = false;
                    flag = true;
                }
            }
        }

        if (flag) {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.save.enabled", new Object[0]);
        } else {
            throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
        }
    }
}

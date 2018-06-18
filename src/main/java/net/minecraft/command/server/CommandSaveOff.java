package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;


public class CommandSaveOff extends CommandBase {

    public CommandSaveOff() {}

    public String getName() {
        return "save-off";
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.save-off.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        boolean flag = false;

        for (int i = 0; i < minecraftserver.worlds.length; ++i) {
            if (minecraftserver.worlds[i] != null) {
                WorldServer worldserver = minecraftserver.worlds[i];

                if (!worldserver.disableLevelSaving) {
                    worldserver.disableLevelSaving = true;
                    flag = true;
                }
            }
        }

        if (flag) {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.save.disabled", new Object[0]);
        } else {
            throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
        }
    }
}

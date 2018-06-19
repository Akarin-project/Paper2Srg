package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;


public class CommandSaveOn extends CommandBase {

    public CommandSaveOn() {}

    public String func_71517_b() {
        return "save-on";
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.save-on.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        boolean flag = false;

        for (int i = 0; i < minecraftserver.field_71305_c.length; ++i) {
            if (minecraftserver.field_71305_c[i] != null) {
                WorldServer worldserver = minecraftserver.field_71305_c[i];

                if (worldserver.field_73058_d) {
                    worldserver.field_73058_d = false;
                    flag = true;
                }
            }
        }

        if (flag) {
            func_152373_a(icommandlistener, (ICommand) this, "commands.save.enabled", new Object[0]);
        } else {
            throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
        }
    }
}

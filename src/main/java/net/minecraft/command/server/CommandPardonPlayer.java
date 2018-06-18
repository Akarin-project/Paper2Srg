package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPardonPlayer extends CommandBase {

    public CommandPardonPlayer() {}

    public String getName() {
        return "pardon";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.unban.usage";
    }

    public boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(minecraftserver, icommandlistener);
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.getPlayerList().getBannedPlayers().getBannedProfile(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.unban.failed", new Object[] { astring[0]});
            } else {
                minecraftserver.getPlayerList().getBannedPlayers().removeEntry(gameprofile);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.unban.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.unban.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getPlayerList().getBannedPlayers().getKeys()) : Collections.emptyList();
    }
}

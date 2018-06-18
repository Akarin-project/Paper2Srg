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

public class CommandDeOp extends CommandBase {

    public CommandDeOp() {}

    public String getName() {
        return "deop";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.deop.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.getPlayerList().getOppedPlayers().getGameProfileFromName(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.deop.failed", new Object[] { astring[0]});
            } else {
                minecraftserver.getPlayerList().removeOp(gameprofile);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.deop.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.deop.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getPlayerList().getOppedPlayerNames()) : Collections.emptyList();
    }
}

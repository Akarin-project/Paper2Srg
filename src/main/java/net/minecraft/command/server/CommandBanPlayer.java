package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanPlayer extends CommandBase {

    public CommandBanPlayer() {}

    public String getName() {
        return "ban";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.ban.usage";
    }

    public boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(minecraftserver, icommandlistener);
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring[0].length() > 0) {
            GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.ban.failed", new Object[] { astring[0]});
            } else {
                String s = null;

                if (astring.length >= 2) {
                    s = getChatComponentFromNthArg(icommandlistener, astring, 1).getUnformattedText();
                }

                UserListBansEntry gameprofilebanentry = new UserListBansEntry(gameprofile, (Date) null, icommandlistener.getName(), (Date) null, s);

                minecraftserver.getPlayerList().getBannedPlayers().addEntry(gameprofilebanentry);
                EntityPlayerMP entityplayer = minecraftserver.getPlayerList().getPlayerByUsername(astring[0]);

                if (entityplayer != null) {
                    entityplayer.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.banned", new Object[0]));
                }

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.ban.success", new Object[] { astring[0]});
            }
        } else {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length >= 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }
}

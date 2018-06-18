package net.minecraft.command.server;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListIPBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanIp extends CommandBase {

    public static final Pattern IP_PATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public CommandBanIp() {}

    public String getName() {
        return "ban-ip";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.getPlayerList().getBannedIPs().isLanServer() && super.checkPermission(minecraftserver, icommandlistener);
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.banip.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring[0].length() > 1) {
            ITextComponent ichatbasecomponent = astring.length >= 2 ? getChatComponentFromNthArg(icommandlistener, astring, 1) : null;
            Matcher matcher = CommandBanIp.IP_PATTERN.matcher(astring[0]);

            if (matcher.matches()) {
                this.banIp(minecraftserver, icommandlistener, astring[0], ichatbasecomponent == null ? null : ichatbasecomponent.getUnformattedText());
            } else {
                EntityPlayerMP entityplayer = minecraftserver.getPlayerList().getPlayerByUsername(astring[0]);

                if (entityplayer == null) {
                    throw new PlayerNotFoundException("commands.banip.invalid");
                }

                this.banIp(minecraftserver, icommandlistener, entityplayer.getPlayerIP(), ichatbasecomponent == null ? null : ichatbasecomponent.getUnformattedText());
            }

        } else {
            throw new WrongUsageException("commands.banip.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }

    protected void banIp(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s, @Nullable String s1) {
        UserListIPBansEntry ipbanentry = new UserListIPBansEntry(s, (Date) null, icommandlistener.getName(), (Date) null, s1);

        minecraftserver.getPlayerList().getBannedIPs().addEntry(ipbanentry);
        List list = minecraftserver.getPlayerList().getPlayersMatchingAddress(s);
        String[] astring = new String[list.size()];
        int i = 0;

        EntityPlayerMP entityplayer;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); astring[i++] = entityplayer.getName()) {
            entityplayer = (EntityPlayerMP) iterator.next();
            entityplayer.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.ip_banned", new Object[0]));
        }

        if (list.isEmpty()) {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.banip.success", new Object[] { s});
        } else {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.banip.success.players", new Object[] { s, joinNiceString((Object[]) astring)});
        }

    }
}

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListBans extends CommandBase {

    public CommandListBans() {}

    public String getName() {
        return "banlist";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return (minecraftserver.getPlayerList().getBannedIPs().isLanServer() || minecraftserver.getPlayerList().getBannedPlayers().isLanServer()) && super.checkPermission(minecraftserver, icommandlistener);
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.banlist.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && "ips".equalsIgnoreCase(astring[0])) {
            icommandlistener.sendMessage(new TextComponentTranslation("commands.banlist.ips", new Object[] { Integer.valueOf(minecraftserver.getPlayerList().getBannedIPs().getKeys().length)}));
            icommandlistener.sendMessage(new TextComponentString(joinNiceString((Object[]) minecraftserver.getPlayerList().getBannedIPs().getKeys())));
        } else {
            icommandlistener.sendMessage(new TextComponentTranslation("commands.banlist.players", new Object[] { Integer.valueOf(minecraftserver.getPlayerList().getBannedPlayers().getKeys().length)}));
            icommandlistener.sendMessage(new TextComponentString(joinNiceString((Object[]) minecraftserver.getPlayerList().getBannedPlayers().getKeys())));
        }

    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "players", "ips"}) : Collections.emptyList();
    }
}

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPardonIp extends CommandBase {

    public CommandPardonIp() {}

    public String getName() {
        return "pardon-ip";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.getPlayerList().getBannedIPs().isLanServer() && super.checkPermission(minecraftserver, icommandlistener);
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.unbanip.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 1) {
            Matcher matcher = CommandBanIp.IP_PATTERN.matcher(astring[0]);

            if (matcher.matches()) {
                minecraftserver.getPlayerList().getBannedIPs().removeEntry(astring[0]);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.unbanip.success", new Object[] { astring[0]});
            } else {
                throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
            }
        } else {
            throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getPlayerList().getBannedIPs().getKeys()) : Collections.emptyList();
    }
}

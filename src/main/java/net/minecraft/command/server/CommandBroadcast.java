package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBroadcast extends CommandBase {

    public CommandBroadcast() {}

    public String getName() {
        return "say";
    }

    public int getRequiredPermissionLevel() {
        return 1;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.say.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 0 && astring[0].length() > 0) {
            ITextComponent ichatbasecomponent = getChatComponentFromNthArg(icommandlistener, astring, 0, true);

            minecraftserver.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.announcement", new Object[] { icommandlistener.getDisplayName(), ichatbasecomponent}));
        } else {
            throw new WrongUsageException("commands.say.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length >= 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }
}

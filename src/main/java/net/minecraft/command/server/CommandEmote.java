package net.minecraft.command.server;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEmote extends CommandBase {

    public CommandEmote() {}

    public String getName() {
        return "me";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.me.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        } else {
            ITextComponent ichatbasecomponent = getChatComponentFromNthArg(icommandlistener, astring, 0, !(icommandlistener instanceof EntityPlayer));

            minecraftserver.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.emote", new Object[] { icommandlistener.getDisplayName(), ichatbasecomponent}));
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
    }
}

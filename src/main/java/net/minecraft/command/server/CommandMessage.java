package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage extends CommandBase {

    public CommandMessage() {}

    public List<String> getAliases() {
        return Arrays.asList(new String[] { "w", "msg"});
    }

    public String getName() {
        return "tell";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.message.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = getPlayer(minecraftserver, icommandlistener, astring[0]);

            if (entityplayer == icommandlistener) {
                throw new PlayerNotFoundException("commands.message.sameTarget");
            } else {
                ITextComponent ichatbasecomponent = getChatComponentFromNthArg(icommandlistener, astring, 1, !(icommandlistener instanceof EntityPlayer));
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.message.display.incoming", new Object[] { icommandlistener.getDisplayName(), ichatbasecomponent.createCopy()});
                TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.message.display.outgoing", new Object[] { entityplayer.getDisplayName(), ichatbasecomponent.createCopy()});

                chatmessage.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.valueOf(true));
                chatmessage1.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.valueOf(true));
                entityplayer.sendMessage(chatmessage);
                icommandlistener.sendMessage(chatmessage1);
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}

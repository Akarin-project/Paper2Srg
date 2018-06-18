package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandServerKick extends CommandBase {

    public CommandServerKick() {}

    public String getName() {
        return "kick";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.kick.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 0 && astring[0].length() > 1) {
            EntityPlayerMP entityplayer = minecraftserver.getPlayerList().getPlayerByUsername(astring[0]);

            if (entityplayer == null) {
                throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { astring[0]});
            } else {
                if (astring.length >= 2) {
                    ITextComponent ichatbasecomponent = getChatComponentFromNthArg(icommandlistener, astring, 1);

                    entityplayer.connection.disconnect(ichatbasecomponent);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.kick.success.reason", new Object[] { entityplayer.getName(), ichatbasecomponent.getUnformattedText()});
                } else {
                    entityplayer.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.kicked", new Object[0]));
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.kick.success", new Object[] { entityplayer.getName()});
                }

            }
        } else {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length >= 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }
}

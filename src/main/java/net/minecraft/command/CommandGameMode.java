package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;

public class CommandGameMode extends CommandBase {

    public CommandGameMode() {}

    public String getName() {
        return "gamemode";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.gamemode.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        } else {
            GameType enumgamemode = this.getGameModeFromCommand(icommandlistener, astring[0]);
            EntityPlayerMP entityplayer = astring.length >= 2 ? getPlayer(minecraftserver, icommandlistener, astring[1]) : getCommandSenderAsPlayer(icommandlistener);

            entityplayer.setGameType(enumgamemode);
            // CraftBukkit start - handle event cancelling the change
            if (entityplayer.interactionManager.getGameType() != enumgamemode) {
                icommandlistener.sendMessage(new TextComponentString("Failed to set the gamemode of '" + entityplayer.getName() + "'"));
                return;
            }
            // CraftBukkit end
            TextComponentTranslation chatmessage = new TextComponentTranslation("gameMode." + enumgamemode.getName(), new Object[0]);

            if (icommandlistener.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
                entityplayer.sendMessage(new TextComponentTranslation("gameMode.changed", new Object[] { chatmessage}));
            }

            if (entityplayer == icommandlistener) {
                notifyCommandListener(icommandlistener, this, 1, "commands.gamemode.success.self", new Object[] { chatmessage});
            } else {
                notifyCommandListener(icommandlistener, this, 1, "commands.gamemode.success.other", new Object[] { entityplayer.getName(), chatmessage});
            }

        }
    }

    protected GameType getGameModeFromCommand(ICommandSender icommandlistener, String s) throws NumberInvalidException {
        GameType enumgamemode = GameType.parseGameTypeWithDefault(s, GameType.NOT_SET);

        return enumgamemode == GameType.NOT_SET ? WorldSettings.getGameTypeById(parseInt(s, 0, GameType.values().length - 2)) : enumgamemode;
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "survival", "creative", "adventure", "spectator"}) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.<String>emptyList()); // CraftBukkit - decompile error
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 1;
    }

    // CraftBukkit start - fix decompiler error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}

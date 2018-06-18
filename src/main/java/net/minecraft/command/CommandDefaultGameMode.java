package net.minecraft.command;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class CommandDefaultGameMode extends CommandGameMode {

    public CommandDefaultGameMode() {}

    public String getName() {
        return "defaultgamemode";
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.defaultgamemode.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        } else {
            GameType enumgamemode = this.getGameModeFromCommand(icommandlistener, astring[0]);

            this.setDefaultGameType(enumgamemode, minecraftserver);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.defaultgamemode.success", new Object[] { new TextComponentTranslation("gameMode." + enumgamemode.getName(), new Object[0])});
        }
    }

    protected void setDefaultGameType(GameType enumgamemode, MinecraftServer minecraftserver) {
        minecraftserver.setGameType(enumgamemode);
        if (minecraftserver.getForceGamemode()) {
            Iterator iterator = minecraftserver.getPlayerList().getPlayers().iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.setGameType(enumgamemode);
            }
        }

    }
}

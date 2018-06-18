package net.minecraft.command.server;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;


public class CommandListPlayers extends CommandBase {

    public CommandListPlayers() {}

    public String getName() {
        return "list";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.players.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        int i = minecraftserver.getCurrentPlayerCount();

        icommandlistener.sendMessage(new TextComponentTranslation("commands.players.list", new Object[] { Integer.valueOf(i), Integer.valueOf(minecraftserver.getMaxPlayers())}));
        icommandlistener.sendMessage(new TextComponentString(minecraftserver.getPlayerList().getFormattedListOfPlayers(astring.length > 0 && "uuids".equalsIgnoreCase(astring[0]))));
        icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
    }
}

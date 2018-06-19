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

    public String func_71517_b() {
        return "list";
    }

    public int func_82362_a() {
        return 0;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.players.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        int i = minecraftserver.func_71233_x();

        icommandlistener.func_145747_a(new TextComponentTranslation("commands.players.list", new Object[] { Integer.valueOf(i), Integer.valueOf(minecraftserver.func_71275_y())}));
        icommandlistener.func_145747_a(new TextComponentString(minecraftserver.func_184103_al().func_181058_b(astring.length > 0 && "uuids".equalsIgnoreCase(astring[0]))));
        icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, i);
    }
}

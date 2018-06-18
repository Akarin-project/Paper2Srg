package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandLocate extends CommandBase {

    public CommandLocate() {}

    public String getName() {
        return "locate";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.locate.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length != 1) {
            throw new WrongUsageException("commands.locate.usage", new Object[0]);
        } else {
            String s = astring[0];
            BlockPos blockposition = icommandlistener.getEntityWorld().findNearestStructure(s, icommandlistener.getPosition(), false);

            if (blockposition != null) {
                icommandlistener.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[] { s, Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getZ())}));
            } else {
                throw new CommandException("commands.locate.failure", new Object[] { s});
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple", "Mineshaft"}) : Collections.emptyList();
    }
}

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase {

    public CommandSaveAll() {}

    public String getName() {
        return "save-all";
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.save.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        icommandlistener.sendMessage(new TextComponentTranslation("commands.save.start", new Object[0]));
        if (minecraftserver.getPlayerList() != null) {
            minecraftserver.getPlayerList().saveAllPlayerData();
        }

        try {
            int i;
            WorldServer worldserver;
            boolean flag;

            for (i = 0; i < minecraftserver.worlds.length; ++i) {
                if (minecraftserver.worlds[i] != null) {
                    worldserver = minecraftserver.worlds[i];
                    flag = worldserver.disableLevelSaving;
                    worldserver.disableLevelSaving = false;
                    worldserver.saveAllChunks(true, (IProgressUpdate) null);
                    worldserver.disableLevelSaving = flag;
                }
            }

            if (astring.length > 0 && "flush".equals(astring[0])) {
                icommandlistener.sendMessage(new TextComponentTranslation("commands.save.flushStart", new Object[0]));

                for (i = 0; i < minecraftserver.worlds.length; ++i) {
                    if (minecraftserver.worlds[i] != null) {
                        worldserver = minecraftserver.worlds[i];
                        flag = worldserver.disableLevelSaving;
                        worldserver.disableLevelSaving = false;
                        worldserver.flushToDisk();
                        worldserver.disableLevelSaving = flag;
                    }
                }

                icommandlistener.sendMessage(new TextComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        } catch (MinecraftException exceptionworldconflict) {
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.save.failed", new Object[] { exceptionworldconflict.getMessage()});
            return;
        }

        notifyCommandListener(icommandlistener, (ICommand) this, "commands.save.success", new Object[0]);
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "flush"}) : Collections.emptyList();
    }
}

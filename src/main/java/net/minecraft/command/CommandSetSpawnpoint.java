package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetSpawnpoint extends CommandBase {

    public CommandSetSpawnpoint() {}

    public String getName() {
        return "spawnpoint";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.spawnpoint.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 1 && astring.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = astring.length > 0 ? getPlayer(minecraftserver, icommandlistener, astring[0]) : getCommandSenderAsPlayer(icommandlistener);
            BlockPos blockposition = astring.length > 3 ? parseBlockPos(icommandlistener, astring, 1, true) : entityplayer.getPosition();

            if (entityplayer.world != null) {
                entityplayer.setSpawnPoint(blockposition, true);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.spawnpoint.success", new Object[] { entityplayer.getName(), Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
            }

        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length > 1 && astring.length <= 4 ? getTabCompletionCoordinate(astring, 1, blockposition) : Collections.emptyList());
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetDefaultSpawnpoint extends CommandBase {

    public CommandSetDefaultSpawnpoint() {}

    public String getName() {
        return "setworldspawn";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.setworldspawn.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        BlockPos blockposition;

        if (astring.length == 0) {
            blockposition = getCommandSenderAsPlayer(icommandlistener).getPosition();
        } else {
            if (astring.length != 3 || icommandlistener.getEntityWorld() == null) {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }

            blockposition = parseBlockPos(icommandlistener, astring, 0, true);
        }

        icommandlistener.getEntityWorld().setSpawnPoint(blockposition);
        minecraftserver.getPlayerList().sendPacketToAllPlayers(new SPacketSpawnPosition(blockposition));
        notifyCommandListener(icommandlistener, (ICommand) this, "commands.setworldspawn.success", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? getTabCompletionCoordinate(astring, 0, blockposition) : Collections.emptyList();
    }
}

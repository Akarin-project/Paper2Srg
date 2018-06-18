package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKill extends CommandBase {

    public CommandKill() {}

    public String getName() {
        return "kill";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.kill.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 0) {
            EntityPlayerMP entityplayer = getCommandSenderAsPlayer(icommandlistener);

            entityplayer.onKillCommand();
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.kill.successful", new Object[] { entityplayer.getDisplayName()});
        } else {
            Entity entity = getEntity(minecraftserver, icommandlistener, astring[0]);

            entity.onKillCommand();
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.kill.successful", new Object[] { entity.getDisplayName()});
        }
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }
}

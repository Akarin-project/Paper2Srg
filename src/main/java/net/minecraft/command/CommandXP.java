package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandXP extends CommandBase {

    public CommandXP() {}

    public String getName() {
        return "xp";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.xp.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        } else {
            String s = astring[0];
            boolean flag = s.endsWith("l") || s.endsWith("L");

            if (flag && s.length() > 1) {
                s = s.substring(0, s.length() - 1);
            }

            int i = parseInt(s);
            boolean flag1 = i < 0;

            if (flag1) {
                i *= -1;
            }

            EntityPlayerMP entityplayer = astring.length > 1 ? getPlayer(minecraftserver, icommandlistener, astring[1]) : getCommandSenderAsPlayer(icommandlistener);

            if (flag) {
                icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);
                if (flag1) {
                    entityplayer.addExperienceLevel(-i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.xp.success.negative.levels", new Object[] { Integer.valueOf(i), entityplayer.getName()});
                } else {
                    entityplayer.addExperienceLevel(i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.xp.success.levels", new Object[] { Integer.valueOf(i), entityplayer.getName()});
                }
            } else {
                icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);
                if (flag1) {
                    throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
                }

                entityplayer.addExperience(i);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.xp.success", new Object[] { Integer.valueOf(i), entityplayer.getName()});
            }

        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 2 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 1;
    }
}

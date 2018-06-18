package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTrigger extends CommandBase {

    public CommandTrigger() {}

    public String getName() {
        return "trigger";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.trigger.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 3) {
            throw new WrongUsageException("commands.trigger.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer;

            if (icommandlistener instanceof EntityPlayerMP) {
                entityplayer = (EntityPlayerMP) icommandlistener;
            } else {
                Entity entity = icommandlistener.getCommandSenderEntity();

                if (!(entity instanceof EntityPlayerMP)) {
                    throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
                }

                entityplayer = (EntityPlayerMP) entity;
            }

            Scoreboard scoreboard = minecraftserver.getWorld(0).getScoreboard();
            ScoreObjective scoreboardobjective = scoreboard.getObjective(astring[0]);

            if (scoreboardobjective != null && scoreboardobjective.getCriteria() == IScoreCriteria.TRIGGER) {
                int i = parseInt(astring[2]);

                if (!scoreboard.entityHasObjective(entityplayer.getName(), scoreboardobjective)) {
                    throw new CommandException("commands.trigger.invalidObjective", new Object[] { astring[0]});
                } else {
                    Score scoreboardscore = scoreboard.getOrCreateScore(entityplayer.getName(), scoreboardobjective);

                    if (scoreboardscore.isLocked()) {
                        throw new CommandException("commands.trigger.disabled", new Object[] { astring[0]});
                    } else {
                        if ("set".equals(astring[1])) {
                            scoreboardscore.setScorePoints(i);
                        } else {
                            if (!"add".equals(astring[1])) {
                                throw new CommandException("commands.trigger.invalidMode", new Object[] { astring[1]});
                            }

                            scoreboardscore.increaseScore(i);
                        }

                        scoreboardscore.setLocked(true);
                        if (entityplayer.interactionManager.isCreative()) {
                            notifyCommandListener(icommandlistener, (ICommand) this, "commands.trigger.success", new Object[] { astring[0], astring[1], astring[2]});
                        }

                    }
                }
            } else {
                throw new CommandException("commands.trigger.invalidObjective", new Object[] { astring[0]});
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            Scoreboard scoreboard = minecraftserver.getWorld(0).getScoreboard();
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = scoreboard.getScoreObjectives().iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                if (scoreboardobjective.getCriteria() == IScoreCriteria.TRIGGER) {
                    arraylist.add(scoreboardobjective.getName());
                }
            }

            return getListOfStringsMatchingLastWord(astring, (String[]) arraylist.toArray(new String[arraylist.size()]));
        } else {
            return astring.length == 2 ? getListOfStringsMatchingLastWord(astring, new String[] { "add", "set"}) : Collections.emptyList();
        }
    }
}

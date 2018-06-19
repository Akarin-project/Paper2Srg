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

    public String func_71517_b() {
        return "trigger";
    }

    public int func_82362_a() {
        return 0;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.trigger.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 3) {
            throw new WrongUsageException("commands.trigger.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer;

            if (icommandlistener instanceof EntityPlayerMP) {
                entityplayer = (EntityPlayerMP) icommandlistener;
            } else {
                Entity entity = icommandlistener.func_174793_f();

                if (!(entity instanceof EntityPlayerMP)) {
                    throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
                }

                entityplayer = (EntityPlayerMP) entity;
            }

            Scoreboard scoreboard = minecraftserver.func_71218_a(0).func_96441_U();
            ScoreObjective scoreboardobjective = scoreboard.func_96518_b(astring[0]);

            if (scoreboardobjective != null && scoreboardobjective.func_96680_c() == IScoreCriteria.field_178791_c) {
                int i = func_175755_a(astring[2]);

                if (!scoreboard.func_178819_b(entityplayer.func_70005_c_(), scoreboardobjective)) {
                    throw new CommandException("commands.trigger.invalidObjective", new Object[] { astring[0]});
                } else {
                    Score scoreboardscore = scoreboard.func_96529_a(entityplayer.func_70005_c_(), scoreboardobjective);

                    if (scoreboardscore.func_178816_g()) {
                        throw new CommandException("commands.trigger.disabled", new Object[] { astring[0]});
                    } else {
                        if ("set".equals(astring[1])) {
                            scoreboardscore.func_96647_c(i);
                        } else {
                            if (!"add".equals(astring[1])) {
                                throw new CommandException("commands.trigger.invalidMode", new Object[] { astring[1]});
                            }

                            scoreboardscore.func_96649_a(i);
                        }

                        scoreboardscore.func_178815_a(true);
                        if (entityplayer.field_71134_c.func_73083_d()) {
                            func_152373_a(icommandlistener, (ICommand) this, "commands.trigger.success", new Object[] { astring[0], astring[1], astring[2]});
                        }

                    }
                }
            } else {
                throw new CommandException("commands.trigger.invalidObjective", new Object[] { astring[0]});
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            Scoreboard scoreboard = minecraftserver.func_71218_a(0).func_96441_U();
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = scoreboard.func_96514_c().iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                if (scoreboardobjective.func_96680_c() == IScoreCriteria.field_178791_c) {
                    arraylist.add(scoreboardobjective.func_96679_b());
                }
            }

            return func_71530_a(astring, (String[]) arraylist.toArray(new String[arraylist.size()]));
        } else {
            return astring.length == 2 ? func_71530_a(astring, new String[] { "add", "set"}) : Collections.emptyList();
        }
    }
}

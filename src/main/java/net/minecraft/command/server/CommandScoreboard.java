package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandScoreboard extends CommandBase {

    public CommandScoreboard() {}

    public String func_71517_b() {
        return "scoreboard";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.scoreboard.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (!this.func_184909_b(minecraftserver, icommandlistener, astring)) {
            if (astring.length < 1) {
                throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
            } else {
                if ("objectives".equalsIgnoreCase(astring[0])) {
                    if (astring.length == 1) {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(astring[1])) {
                        this.func_184925_a(icommandlistener, minecraftserver);
                    } else if ("add".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 4) {
                            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                        }

                        this.func_184908_a(icommandlistener, astring, 2, minecraftserver);
                    } else if ("remove".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3) {
                            throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                        }

                        this.func_184905_a(icommandlistener, astring[2], minecraftserver);
                    } else {
                        if (!"setdisplay".equalsIgnoreCase(astring[1])) {
                            throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                        }

                        if (astring.length != 3 && astring.length != 4) {
                            throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                        }

                        this.func_184919_i(icommandlistener, astring, 2, minecraftserver);
                    }
                } else if ("players".equalsIgnoreCase(astring[0])) {
                    if (astring.length == 1) {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(astring[1])) {
                        if (astring.length > 3) {
                            throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                        }

                        this.func_184920_j(icommandlistener, astring, 2, minecraftserver);
                    } else if ("add".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 5) {
                            throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                        }

                        this.func_184918_k(icommandlistener, astring, 2, minecraftserver);
                    } else if ("remove".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 5) {
                            throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                        }

                        this.func_184918_k(icommandlistener, astring, 2, minecraftserver);
                    } else if ("set".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 5) {
                            throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                        }

                        this.func_184918_k(icommandlistener, astring, 2, minecraftserver);
                    } else if ("reset".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3 && astring.length != 4) {
                            throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                        }

                        this.func_184912_l(icommandlistener, astring, 2, minecraftserver);
                    } else if ("enable".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 4) {
                            throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
                        }

                        this.func_184914_m(icommandlistener, astring, 2, minecraftserver);
                    } else if ("test".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 5 && astring.length != 6) {
                            throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
                        }

                        this.func_184907_n(icommandlistener, astring, 2, minecraftserver);
                    } else if ("operation".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 7) {
                            throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
                        }

                        this.func_184906_o(icommandlistener, astring, 2, minecraftserver);
                    } else {
                        if (!"tag".equalsIgnoreCase(astring[1])) {
                            throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                        }

                        if (astring.length < 4) {
                            throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                        }

                        this.func_184924_a(minecraftserver, icommandlistener, astring, 2);
                    }
                } else {
                    if (!"teams".equalsIgnoreCase(astring[0])) {
                        throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
                    }

                    if (astring.length == 1) {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(astring[1])) {
                        if (astring.length > 3) {
                            throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                        }

                        this.func_184922_e(icommandlistener, astring, 2, minecraftserver);
                    } else if ("add".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 3) {
                            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                        }

                        this.func_184910_b(icommandlistener, astring, 2, minecraftserver);
                    } else if ("remove".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3) {
                            throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                        }

                        this.func_184921_d(icommandlistener, astring, 2, minecraftserver);
                    } else if ("empty".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3) {
                            throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                        }

                        this.func_184917_h(icommandlistener, astring, 2, minecraftserver);
                    } else if ("join".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 4 && (astring.length != 3 || !(icommandlistener instanceof EntityPlayer))) {
                            throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                        }

                        this.func_184916_f(icommandlistener, astring, 2, minecraftserver);
                    } else if ("leave".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 3 && !(icommandlistener instanceof EntityPlayer)) {
                            throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                        }

                        this.func_184911_g(icommandlistener, astring, 2, minecraftserver);
                    } else {
                        if (!"option".equalsIgnoreCase(astring[1])) {
                            throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                        }

                        if (astring.length != 4 && astring.length != 5) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                        }

                        // Paper start - Block setting options on our collideRule team as it is not persistent
                        if (astring[2].equals(MinecraftServer.getServer().func_184103_al().collideRuleTeamName)) {
                            icommandlistener.func_145747_a(new TextComponentTranslation("You cannot set team options on the collideRule team"));
                            return;
                        }
                        // Paper  end

                        this.func_184923_c(icommandlistener, astring, 2, minecraftserver);
                    }
                }

            }
        }
    }

    private boolean func_184909_b(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        int i = -1;

        for (int j = 0; j < astring.length; ++j) {
            if (this.func_82358_a(astring, j) && "*".equals(astring[j])) {
                if (i >= 0) {
                    throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
                }

                i = j;
            }
        }

        if (i < 0) {
            return false;
        } else {
            ArrayList arraylist = Lists.newArrayList(this.func_184913_a(minecraftserver).func_96526_d());
            String s = astring[i];
            ArrayList arraylist1 = Lists.newArrayList();
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                astring[i] = s1;

                try {
                    this.func_184881_a(minecraftserver, icommandlistener, astring);
                    arraylist1.add(s1);
                } catch (CommandException commandexception) {
                    TextComponentTranslation chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.func_74844_a());

                    chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
                    icommandlistener.func_145747_a(chatmessage);
                }
            }

            astring[i] = s;
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, arraylist1.size());
            if (arraylist1.isEmpty()) {
                throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
            } else {
                return true;
            }
        }
    }

    protected Scoreboard func_184913_a(MinecraftServer minecraftserver) {
        return minecraftserver.func_71218_a(0).func_96441_U();
    }

    protected ScoreObjective func_184903_a(String s, boolean flag, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        ScoreObjective scoreboardobjective = scoreboard.func_96518_b(s);

        if (scoreboardobjective == null) {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { s});
        } else if (flag && scoreboardobjective.func_96680_c().func_96637_b()) {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { s});
        } else {
            return scoreboardobjective;
        }
    }

    protected ScorePlayerTeam func_184915_a(String s, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        ScorePlayerTeam scoreboardteam = scoreboard.func_96508_e(s);

        if (scoreboardteam == null) {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { s});
        } else {
            return scoreboardteam;
        }
    }

    protected void func_184908_a(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        String s = astring[i++];
        String s1 = astring[i++];
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        IScoreCriteria iscoreboardcriteria = (IScoreCriteria) IScoreCriteria.field_96643_a.get(s1);

        if (iscoreboardcriteria == null) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] { s1});
        } else if (scoreboard.func_96518_b(s) != null) {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { s});
        } else if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] { s, Integer.valueOf(16)});
        } else if (s.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        } else {
            if (astring.length > i) {
                String s2 = func_147178_a(icommandlistener, astring, i).func_150260_c();

                if (s2.length() > 32) {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] { s2, Integer.valueOf(32)});
                }

                if (s2.isEmpty()) {
                    scoreboard.func_96535_a(s, iscoreboardcriteria);
                } else {
                    scoreboard.func_96535_a(s, iscoreboardcriteria).func_96681_a(s2);
                }
            } else {
                scoreboard.func_96535_a(s, iscoreboardcriteria);
            }

            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.add.success", new Object[] { s});
        }
    }

    protected void func_184910_b(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        String s = astring[i++];
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);

        if (scoreboard.func_96508_e(s) != null) {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { s});
        } else if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] { s, Integer.valueOf(16)});
        } else if (s.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        } else {
            if (astring.length > i) {
                String s1 = func_147178_a(icommandlistener, astring, i).func_150260_c();

                if (s1.length() > 32) {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] { s1, Integer.valueOf(32)});
                }

                if (s1.isEmpty()) {
                    scoreboard.func_96527_f(s);
                } else {
                    scoreboard.func_96527_f(s).func_96664_a(s1);
                }
            } else {
                scoreboard.func_96527_f(s);
            }

            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.teams.add.success", new Object[] { s});
        }
    }

    protected void func_184923_c(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        ScorePlayerTeam scoreboardteam = this.func_184915_a(astring[i++], minecraftserver);

        if (scoreboardteam != null) {
            String s = astring[i++].toLowerCase(Locale.ROOT);

            if (!"color".equalsIgnoreCase(s) && !"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s) && !"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s) && !"collisionRule".equalsIgnoreCase(s)) {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            } else if (astring.length == 4) {
                if ("color".equalsIgnoreCase(s)) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_96333_a(TextFormatting.func_96296_a(true, false))});
                } else if (!"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s)) {
                    if (!"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s)) {
                        if ("collisionRule".equalsIgnoreCase(s)) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_71527_a((Object[]) Team.CollisionRule.func_186687_a())});
                        } else {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                        }
                    } else {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_71527_a((Object[]) Team.EnumVisible.func_178825_a())});
                    }
                } else {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_96333_a((Collection) Arrays.asList(new String[] { "true", "false"}))});
                }
            } else {
                String s1 = astring[i];

                if ("color".equalsIgnoreCase(s)) {
                    TextFormatting enumchatformat = TextFormatting.func_96300_b(s1);

                    if (enumchatformat == null || enumchatformat.func_96301_b()) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_96333_a(TextFormatting.func_96296_a(true, false))});
                    }

                    scoreboardteam.func_178774_a(enumchatformat);
                    scoreboardteam.func_96666_b(enumchatformat.toString());
                    scoreboardteam.func_96662_c(TextFormatting.RESET.toString());
                } else if ("friendlyfire".equalsIgnoreCase(s)) {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1)) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_96333_a((Collection) Arrays.asList(new String[] { "true", "false"}))});
                    }

                    scoreboardteam.func_96660_a("true".equalsIgnoreCase(s1));
                } else if ("seeFriendlyInvisibles".equalsIgnoreCase(s)) {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1)) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_96333_a((Collection) Arrays.asList(new String[] { "true", "false"}))});
                    }

                    scoreboardteam.func_98300_b("true".equalsIgnoreCase(s1));
                } else {
                    Team.EnumVisible scoreboardteambase_enumnametagvisibility;

                    if ("nametagVisibility".equalsIgnoreCase(s)) {
                        scoreboardteambase_enumnametagvisibility = Team.EnumVisible.func_178824_a(s1);
                        if (scoreboardteambase_enumnametagvisibility == null) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_71527_a((Object[]) Team.EnumVisible.func_178825_a())});
                        }

                        scoreboardteam.func_178772_a(scoreboardteambase_enumnametagvisibility);
                    } else if ("deathMessageVisibility".equalsIgnoreCase(s)) {
                        scoreboardteambase_enumnametagvisibility = Team.EnumVisible.func_178824_a(s1);
                        if (scoreboardteambase_enumnametagvisibility == null) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_71527_a((Object[]) Team.EnumVisible.func_178825_a())});
                        }

                        scoreboardteam.func_178773_b(scoreboardteambase_enumnametagvisibility);
                    } else if ("collisionRule".equalsIgnoreCase(s)) {
                        Team.CollisionRule scoreboardteambase_enumteampush = Team.CollisionRule.func_186686_a(s1);

                        if (scoreboardteambase_enumteampush == null) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, func_71527_a((Object[]) Team.CollisionRule.func_186687_a())});
                        }

                        scoreboardteam.func_186682_a(scoreboardteambase_enumteampush);
                    }
                }

                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.teams.option.success", new Object[] { s, scoreboardteam.func_96661_b(), s1});
            }
        }
    }

    protected void func_184921_d(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        ScorePlayerTeam scoreboardteam = this.func_184915_a(astring[i], minecraftserver);

        if (scoreboardteam != null) {
            scoreboard.func_96511_d(scoreboardteam);
            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.teams.remove.success", new Object[] { scoreboardteam.func_96661_b()});
        }
    }

    protected void func_184922_e(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);

        if (astring.length > i) {
            ScorePlayerTeam scoreboardteam = this.func_184915_a(astring[i], minecraftserver);

            if (scoreboardteam == null) {
                return;
            }

            Collection collection = scoreboardteam.func_96670_d();

            icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] { scoreboardteam.func_96661_b()});
            }

            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] { Integer.valueOf(collection.size()), scoreboardteam.func_96661_b()});

            chatmessage.func_150256_b().func_150238_a(TextFormatting.DARK_GREEN);
            icommandlistener.func_145747_a(chatmessage);
            icommandlistener.func_145747_a(new TextComponentString(func_71527_a(collection.toArray())));
        } else {
            Collection collection1 = scoreboard.func_96525_g();

            icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, collection1.size());
            if (collection1.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }

            TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.scoreboard.teams.list.count", new Object[] { Integer.valueOf(collection1.size())});

            chatmessage1.func_150256_b().func_150238_a(TextFormatting.DARK_GREEN);
            icommandlistener.func_145747_a(chatmessage1);
            Iterator iterator = collection1.iterator();

            while (iterator.hasNext()) {
                ScorePlayerTeam scoreboardteam1 = (ScorePlayerTeam) iterator.next();

                icommandlistener.func_145747_a(new TextComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] { scoreboardteam1.func_96661_b(), scoreboardteam1.func_96669_c(), Integer.valueOf(scoreboardteam1.func_96670_d().size())}));
            }
        }

    }

    protected void func_184916_f(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        String s = astring[i++];
        HashSet hashset = Sets.newHashSet();
        HashSet hashset1 = Sets.newHashSet();
        String s1;

        if (icommandlistener instanceof EntityPlayer && i == astring.length) {
            s1 = func_71521_c(icommandlistener).func_70005_c_();
            if (scoreboard.func_151392_a(s1, s)) {
                hashset.add(s1);
            } else {
                hashset1.add(s1);
            }
        } else {
            while (i < astring.length) {
                s1 = astring[i++];
                if (EntitySelector.func_82378_b(s1)) {
                    List list = func_184890_c(minecraftserver, icommandlistener, s1);
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();
                        if (!entity.field_70170_p.paperConfig.nonPlayerEntitiesOnScoreboards && !(entity instanceof EntityPlayer)) { continue; } // Paper
                        String s2 = func_184891_e(minecraftserver, icommandlistener, entity.func_189512_bd());

                        if (scoreboard.func_151392_a(s2, s)) {
                            hashset.add(s2);
                        } else {
                            hashset1.add(s2);
                        }
                    }
                } else {
                    String s3 = func_184891_e(minecraftserver, icommandlistener, s1);

                    if (scoreboard.func_151392_a(s3, s)) {
                        hashset.add(s3);
                    } else {
                        hashset1.add(s3);
                    }
                }
            }
        }

        if (!hashset.isEmpty()) {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, hashset.size());
            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.teams.join.success", new Object[] { Integer.valueOf(hashset.size()), s, func_71527_a(hashset.toArray(new String[hashset.size()]))});
        }

        if (!hashset1.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] { Integer.valueOf(hashset1.size()), s, func_71527_a(hashset1.toArray(new String[hashset1.size()]))});
        }
    }

    protected void func_184911_g(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        HashSet hashset = Sets.newHashSet();
        HashSet hashset1 = Sets.newHashSet();
        String s;

        if (icommandlistener instanceof EntityPlayer && i == astring.length) {
            s = func_71521_c(icommandlistener).func_70005_c_();
            if (scoreboard.func_96524_g(s)) {
                hashset.add(s);
            } else {
                hashset1.add(s);
            }
        } else {
            while (i < astring.length) {
                s = astring[i++];
                if (EntitySelector.func_82378_b(s)) {
                    List list = func_184890_c(minecraftserver, icommandlistener, s);
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();
                        String s1 = func_184891_e(minecraftserver, icommandlistener, entity.func_189512_bd());

                        if (scoreboard.func_96524_g(s1)) {
                            hashset.add(s1);
                        } else {
                            hashset1.add(s1);
                        }
                    }
                } else {
                    String s2 = func_184891_e(minecraftserver, icommandlistener, s);

                    if (scoreboard.func_96524_g(s2)) {
                        hashset.add(s2);
                    } else {
                        hashset1.add(s2);
                    }
                }
            }
        }

        if (!hashset.isEmpty()) {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, hashset.size());
            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.teams.leave.success", new Object[] { Integer.valueOf(hashset.size()), func_71527_a(hashset.toArray(new String[hashset.size()]))});
        }

        if (!hashset1.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] { Integer.valueOf(hashset1.size()), func_71527_a(hashset1.toArray(new String[hashset1.size()]))});
        }
    }

    protected void func_184917_h(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        ScorePlayerTeam scoreboardteam = this.func_184915_a(astring[i], minecraftserver);

        if (scoreboardteam != null) {
            ArrayList arraylist = Lists.newArrayList(scoreboardteam.func_96670_d());

            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, arraylist.size());
            if (arraylist.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] { scoreboardteam.func_96661_b()});
            } else {
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();

                    scoreboard.func_96512_b(s, scoreboardteam);
                }

                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.teams.empty.success", new Object[] { Integer.valueOf(arraylist.size()), scoreboardteam.func_96661_b()});
            }
        }
    }

    protected void func_184905_a(ICommandSender icommandlistener, String s, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        ScoreObjective scoreboardobjective = this.func_184903_a(s, false, minecraftserver);

        scoreboard.func_96519_k(scoreboardobjective);
        func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.remove.success", new Object[] { s});
    }

    protected void func_184925_a(ICommandSender icommandlistener, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        Collection collection = scoreboard.func_96514_c();

        if (collection.isEmpty()) {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        } else {
            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] { Integer.valueOf(collection.size())});

            chatmessage.func_150256_b().func_150238_a(TextFormatting.DARK_GREEN);
            icommandlistener.func_145747_a(chatmessage);
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                icommandlistener.func_145747_a(new TextComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] { scoreboardobjective.func_96679_b(), scoreboardobjective.func_96678_d(), scoreboardobjective.func_96680_c().func_96636_a()}));
            }

        }
    }

    protected void func_184919_i(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        String s = astring[i++];
        int j = Scoreboard.func_96537_j(s);
        ScoreObjective scoreboardobjective = null;

        if (astring.length == 4) {
            scoreboardobjective = this.func_184903_a(astring[i], false, minecraftserver);
        }

        if (j < 0) {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { s});
        } else {
            scoreboard.func_96530_a(j, scoreboardobjective);
            if (scoreboardobjective != null) {
                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] { Scoreboard.func_96517_b(j), scoreboardobjective.func_96679_b()});
            } else {
                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] { Scoreboard.func_96517_b(j)});
            }

        }
    }

    protected void func_184920_j(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);

        if (astring.length > i) {
            String s = func_184891_e(minecraftserver, icommandlistener, astring[i]);
            Map map = scoreboard.func_96510_d(s);

            icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, map.size());
            if (map.isEmpty()) {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { s});
            }

            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] { Integer.valueOf(map.size()), s});

            chatmessage.func_150256_b().func_150238_a(TextFormatting.DARK_GREEN);
            icommandlistener.func_145747_a(chatmessage);
            Iterator iterator = map.values().iterator();

            while (iterator.hasNext()) {
                Score scoreboardscore = (Score) iterator.next();

                icommandlistener.func_145747_a(new TextComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] { Integer.valueOf(scoreboardscore.func_96652_c()), scoreboardscore.func_96645_d().func_96678_d(), scoreboardscore.func_96645_d().func_96679_b()}));
            }
        } else {
            Collection collection = scoreboard.func_96526_d();

            icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }

            TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.scoreboard.players.list.count", new Object[] { Integer.valueOf(collection.size())});

            chatmessage1.func_150256_b().func_150238_a(TextFormatting.DARK_GREEN);
            icommandlistener.func_145747_a(chatmessage1);
            icommandlistener.func_145747_a(new TextComponentString(func_71527_a(collection.toArray())));
        }

    }

    protected void func_184918_k(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        String s = astring[i - 1];
        int j = i;
        String s1 = func_184891_e(minecraftserver, icommandlistener, astring[i++]);

        if (s1.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s1, Integer.valueOf(40)});
        } else {
            ScoreObjective scoreboardobjective = this.func_184903_a(astring[i++], true, minecraftserver);
            int k = "set".equalsIgnoreCase(s) ? func_175755_a(astring[i++]) : func_180528_a(astring[i++], 0);

            if (astring.length > i) {
                Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[j]);

                try {
                    NBTTagCompound nbttagcompound = JsonToNBT.func_180713_a(func_180529_a(astring, i));
                    NBTTagCompound nbttagcompound1 = func_184887_a(entity);

                    if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true)) {
                        throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] { s1});
                    }
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            Scoreboard scoreboard = this.func_184913_a(minecraftserver);
            Score scoreboardscore = scoreboard.func_96529_a(s1, scoreboardobjective);

            if ("set".equalsIgnoreCase(s)) {
                scoreboardscore.func_96647_c(k);
            } else if ("add".equalsIgnoreCase(s)) {
                scoreboardscore.func_96649_a(k);
            } else {
                scoreboardscore.func_96646_b(k);
            }

            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.set.success", new Object[] { scoreboardobjective.func_96679_b(), s1, Integer.valueOf(scoreboardscore.func_96652_c())});
        }
    }

    protected void func_184912_l(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        String s = func_184891_e(minecraftserver, icommandlistener, astring[i++]);

        if (astring.length > i) {
            ScoreObjective scoreboardobjective = this.func_184903_a(astring[i++], false, minecraftserver);

            scoreboard.func_178822_d(s, scoreboardobjective);
            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.resetscore.success", new Object[] { scoreboardobjective.func_96679_b(), s});
        } else {
            scoreboard.func_178822_d(s, (ScoreObjective) null);
            func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.reset.success", new Object[] { s});
        }

    }

    protected void func_184914_m(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        String s = func_184886_d(minecraftserver, icommandlistener, astring[i++]);

        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40)});
        } else {
            ScoreObjective scoreboardobjective = this.func_184903_a(astring[i], false, minecraftserver);

            if (scoreboardobjective.func_96680_c() != IScoreCriteria.field_178791_c) {
                throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] { scoreboardobjective.func_96679_b()});
            } else {
                Score scoreboardscore = scoreboard.func_96529_a(s, scoreboardobjective);

                scoreboardscore.func_178815_a(false);
                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.enable.success", new Object[] { scoreboardobjective.func_96679_b(), s});
            }
        }
    }

    protected void func_184907_n(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        String s = func_184891_e(minecraftserver, icommandlistener, astring[i++]);

        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40)});
        } else {
            ScoreObjective scoreboardobjective = this.func_184903_a(astring[i++], false, minecraftserver);

            if (!scoreboard.func_178819_b(s, scoreboardobjective)) {
                throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] { scoreboardobjective.func_96679_b(), s});
            } else {
                int j = astring[i].equals("*") ? Integer.MIN_VALUE : func_175755_a(astring[i]);

                ++i;
                int k = i < astring.length && !astring[i].equals("*") ? func_180528_a(astring[i], j) : Integer.MAX_VALUE;
                Score scoreboardscore = scoreboard.func_96529_a(s, scoreboardobjective);

                if (scoreboardscore.func_96652_c() >= j && scoreboardscore.func_96652_c() <= k) {
                    func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.test.success", new Object[] { Integer.valueOf(scoreboardscore.func_96652_c()), Integer.valueOf(j), Integer.valueOf(k)});
                } else {
                    throw new CommandException("commands.scoreboard.players.test.failed", new Object[] { Integer.valueOf(scoreboardscore.func_96652_c()), Integer.valueOf(j), Integer.valueOf(k)});
                }
            }
        }
    }

    protected void func_184906_o(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.func_184913_a(minecraftserver);
        String s = func_184891_e(minecraftserver, icommandlistener, astring[i++]);
        ScoreObjective scoreboardobjective = this.func_184903_a(astring[i++], true, minecraftserver);
        String s1 = astring[i++];
        String s2 = func_184891_e(minecraftserver, icommandlistener, astring[i++]);
        ScoreObjective scoreboardobjective1 = this.func_184903_a(astring[i], false, minecraftserver);

        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40)});
        } else if (s2.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s2, Integer.valueOf(40)});
        } else {
            Score scoreboardscore = scoreboard.func_96529_a(s, scoreboardobjective);

            if (!scoreboard.func_178819_b(s2, scoreboardobjective1)) {
                throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] { scoreboardobjective1.func_96679_b(), s2});
            } else {
                Score scoreboardscore1 = scoreboard.func_96529_a(s2, scoreboardobjective1);

                if ("+=".equals(s1)) {
                    scoreboardscore.func_96647_c(scoreboardscore.func_96652_c() + scoreboardscore1.func_96652_c());
                } else if ("-=".equals(s1)) {
                    scoreboardscore.func_96647_c(scoreboardscore.func_96652_c() - scoreboardscore1.func_96652_c());
                } else if ("*=".equals(s1)) {
                    scoreboardscore.func_96647_c(scoreboardscore.func_96652_c() * scoreboardscore1.func_96652_c());
                } else if ("/=".equals(s1)) {
                    if (scoreboardscore1.func_96652_c() != 0) {
                        scoreboardscore.func_96647_c(scoreboardscore.func_96652_c() / scoreboardscore1.func_96652_c());
                    }
                } else if ("%=".equals(s1)) {
                    if (scoreboardscore1.func_96652_c() != 0) {
                        scoreboardscore.func_96647_c(scoreboardscore.func_96652_c() % scoreboardscore1.func_96652_c());
                    }
                } else if ("=".equals(s1)) {
                    scoreboardscore.func_96647_c(scoreboardscore1.func_96652_c());
                } else if ("<".equals(s1)) {
                    scoreboardscore.func_96647_c(Math.min(scoreboardscore.func_96652_c(), scoreboardscore1.func_96652_c()));
                } else if (">".equals(s1)) {
                    scoreboardscore.func_96647_c(Math.max(scoreboardscore.func_96652_c(), scoreboardscore1.func_96652_c()));
                } else {
                    if (!"><".equals(s1)) {
                        throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] { s1});
                    }

                    int j = scoreboardscore.func_96652_c();

                    scoreboardscore.func_96647_c(scoreboardscore1.func_96652_c());
                    scoreboardscore1.func_96647_c(j);
                }

                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.operation.success", new Object[0]);
            }
        }
    }

    protected void func_184924_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, int i) throws CommandException {
        String s = func_184891_e(minecraftserver, icommandlistener, astring[i]);
        Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[i++]);
        String s1 = astring[i++];
        Set set = entity.func_184216_O();

        if ("list".equals(s1)) {
            if (!set.isEmpty()) {
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.players.tag.list", new Object[] { s});

                chatmessage.func_150256_b().func_150238_a(TextFormatting.DARK_GREEN);
                icommandlistener.func_145747_a(chatmessage);
                icommandlistener.func_145747_a(new TextComponentString(func_71527_a(set.toArray())));
            }

            icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, set.size());
        } else if (astring.length < 5) {
            throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
        } else {
            String s2 = astring[i++];

            if (astring.length > i) {
                try {
                    NBTTagCompound nbttagcompound = JsonToNBT.func_180713_a(func_180529_a(astring, i));
                    NBTTagCompound nbttagcompound1 = func_184887_a(entity);

                    if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true)) {
                        throw new CommandException("commands.scoreboard.players.tag.tagMismatch", new Object[] { s});
                    }
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.scoreboard.players.tag.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            if ("add".equals(s1)) {
                if (!entity.func_184211_a(s2)) {
                    throw new CommandException("commands.scoreboard.players.tag.tooMany", new Object[] { Integer.valueOf(1024)});
                }

                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.tag.success.add", new Object[] { s2});
            } else {
                if (!"remove".equals(s1)) {
                    throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                }

                if (!entity.func_184197_b(s2)) {
                    throw new CommandException("commands.scoreboard.players.tag.notFound", new Object[] { s2});
                }

                func_152373_a(icommandlistener, (ICommand) this, "commands.scoreboard.players.tag.success.remove", new Object[] { s2});
            }

        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            return func_71530_a(astring, new String[] { "objectives", "players", "teams"});
        } else {
            if ("objectives".equalsIgnoreCase(astring[0])) {
                if (astring.length == 2) {
                    return func_71530_a(astring, new String[] { "list", "add", "remove", "setdisplay"});
                }

                if ("add".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 4) {
                        Set set = IScoreCriteria.field_96643_a.keySet();

                        return func_175762_a(astring, (Collection) set);
                    }
                } else if ("remove".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 3) {
                        return func_175762_a(astring, (Collection) this.func_184926_a(false, minecraftserver));
                    }
                } else if ("setdisplay".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 3) {
                        return func_71530_a(astring, Scoreboard.func_178821_h());
                    }

                    if (astring.length == 4) {
                        return func_175762_a(astring, (Collection) this.func_184926_a(false, minecraftserver));
                    }
                }
            } else if ("players".equalsIgnoreCase(astring[0])) {
                if (astring.length == 2) {
                    return func_71530_a(astring, new String[] { "set", "add", "remove", "reset", "list", "enable", "test", "operation", "tag"});
                }

                if (!"set".equalsIgnoreCase(astring[1]) && !"add".equalsIgnoreCase(astring[1]) && !"remove".equalsIgnoreCase(astring[1]) && !"reset".equalsIgnoreCase(astring[1])) {
                    if ("enable".equalsIgnoreCase(astring[1])) {
                        if (astring.length == 3) {
                            return func_71530_a(astring, minecraftserver.func_71213_z());
                        }

                        if (astring.length == 4) {
                            return func_175762_a(astring, (Collection) this.func_184904_b(minecraftserver));
                        }
                    } else if (!"list".equalsIgnoreCase(astring[1]) && !"test".equalsIgnoreCase(astring[1])) {
                        if ("operation".equalsIgnoreCase(astring[1])) {
                            if (astring.length == 3) {
                                return func_175762_a(astring, this.func_184913_a(minecraftserver).func_96526_d());
                            }

                            if (astring.length == 4) {
                                return func_175762_a(astring, (Collection) this.func_184926_a(true, minecraftserver));
                            }

                            if (astring.length == 5) {
                                return func_71530_a(astring, new String[] { "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><"});
                            }

                            if (astring.length == 6) {
                                return func_71530_a(astring, minecraftserver.func_71213_z());
                            }

                            if (astring.length == 7) {
                                return func_175762_a(astring, (Collection) this.func_184926_a(false, minecraftserver));
                            }
                        } else if ("tag".equalsIgnoreCase(astring[1])) {
                            if (astring.length == 3) {
                                return func_175762_a(astring, this.func_184913_a(minecraftserver).func_96526_d());
                            }

                            if (astring.length == 4) {
                                return func_71530_a(astring, new String[] { "add", "remove", "list"});
                            }
                        }
                    } else {
                        if (astring.length == 3) {
                            return func_175762_a(astring, this.func_184913_a(minecraftserver).func_96526_d());
                        }

                        if (astring.length == 4 && "test".equalsIgnoreCase(astring[1])) {
                            return func_175762_a(astring, (Collection) this.func_184926_a(false, minecraftserver));
                        }
                    }
                } else {
                    if (astring.length == 3) {
                        return func_71530_a(astring, minecraftserver.func_71213_z());
                    }

                    if (astring.length == 4) {
                        return func_175762_a(astring, (Collection) this.func_184926_a(true, minecraftserver));
                    }
                }
            } else if ("teams".equalsIgnoreCase(astring[0])) {
                if (astring.length == 2) {
                    return func_71530_a(astring, new String[] { "add", "remove", "join", "leave", "empty", "list", "option"});
                }

                if ("join".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 3) {
                        return func_175762_a(astring, this.func_184913_a(minecraftserver).func_96531_f());
                    }

                    if (astring.length >= 4) {
                        return func_71530_a(astring, minecraftserver.func_71213_z());
                    }
                } else {
                    if ("leave".equalsIgnoreCase(astring[1])) {
                        return func_71530_a(astring, minecraftserver.func_71213_z());
                    }

                    if (!"empty".equalsIgnoreCase(astring[1]) && !"list".equalsIgnoreCase(astring[1]) && !"remove".equalsIgnoreCase(astring[1])) {
                        if ("option".equalsIgnoreCase(astring[1])) {
                            if (astring.length == 3) {
                                return func_175762_a(astring, this.func_184913_a(minecraftserver).func_96531_f());
                            }

                            if (astring.length == 4) {
                                return func_71530_a(astring, new String[] { "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility", "collisionRule"});
                            }

                            if (astring.length == 5) {
                                if ("color".equalsIgnoreCase(astring[3])) {
                                    return func_175762_a(astring, TextFormatting.func_96296_a(true, false));
                                }

                                if ("nametagVisibility".equalsIgnoreCase(astring[3]) || "deathMessageVisibility".equalsIgnoreCase(astring[3])) {
                                    return func_71530_a(astring, Team.EnumVisible.func_178825_a());
                                }

                                if ("collisionRule".equalsIgnoreCase(astring[3])) {
                                    return func_71530_a(astring, Team.CollisionRule.func_186687_a());
                                }

                                if ("friendlyfire".equalsIgnoreCase(astring[3]) || "seeFriendlyInvisibles".equalsIgnoreCase(astring[3])) {
                                    return func_71530_a(astring, new String[] { "true", "false"});
                                }
                            }
                        }
                    } else if (astring.length == 3) {
                        return func_175762_a(astring, this.func_184913_a(minecraftserver).func_96531_f());
                    }
                }
            }

            return Collections.emptyList();
        }
    }

    protected List<String> func_184926_a(boolean flag, MinecraftServer minecraftserver) {
        Collection collection = this.func_184913_a(minecraftserver).func_96514_c();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (!flag || !scoreboardobjective.func_96680_c().func_96637_b()) {
                arraylist.add(scoreboardobjective.func_96679_b());
            }
        }

        return arraylist;
    }

    protected List<String> func_184904_b(MinecraftServer minecraftserver) {
        Collection collection = this.func_184913_a(minecraftserver).func_96514_c();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (scoreboardobjective.func_96680_c() == IScoreCriteria.field_178791_c) {
                arraylist.add(scoreboardobjective.func_96679_b());
            }
        }

        return arraylist;
    }

    public boolean func_82358_a(String[] astring, int i) {
        return !"players".equalsIgnoreCase(astring[0]) ? ("teams".equalsIgnoreCase(astring[0]) ? i == 2 : false) : (astring.length > 1 && "operation".equalsIgnoreCase(astring[1]) ? i == 2 || i == 5 : i == 2);
    }
}

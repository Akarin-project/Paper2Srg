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

    public String getName() {
        return "scoreboard";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.scoreboard.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (!this.handleUserWildcards(minecraftserver, icommandlistener, astring)) {
            if (astring.length < 1) {
                throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
            } else {
                if ("objectives".equalsIgnoreCase(astring[0])) {
                    if (astring.length == 1) {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(astring[1])) {
                        this.listObjectives(icommandlistener, minecraftserver);
                    } else if ("add".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 4) {
                            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                        }

                        this.addObjective(icommandlistener, astring, 2, minecraftserver);
                    } else if ("remove".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3) {
                            throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                        }

                        this.removeObjective(icommandlistener, astring[2], minecraftserver);
                    } else {
                        if (!"setdisplay".equalsIgnoreCase(astring[1])) {
                            throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                        }

                        if (astring.length != 3 && astring.length != 4) {
                            throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                        }

                        this.setDisplayObjective(icommandlistener, astring, 2, minecraftserver);
                    }
                } else if ("players".equalsIgnoreCase(astring[0])) {
                    if (astring.length == 1) {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(astring[1])) {
                        if (astring.length > 3) {
                            throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                        }

                        this.listPlayers(icommandlistener, astring, 2, minecraftserver);
                    } else if ("add".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 5) {
                            throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                        }

                        this.addPlayerScore(icommandlistener, astring, 2, minecraftserver);
                    } else if ("remove".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 5) {
                            throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                        }

                        this.addPlayerScore(icommandlistener, astring, 2, minecraftserver);
                    } else if ("set".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 5) {
                            throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                        }

                        this.addPlayerScore(icommandlistener, astring, 2, minecraftserver);
                    } else if ("reset".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3 && astring.length != 4) {
                            throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                        }

                        this.resetPlayerScore(icommandlistener, astring, 2, minecraftserver);
                    } else if ("enable".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 4) {
                            throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
                        }

                        this.enablePlayerTrigger(icommandlistener, astring, 2, minecraftserver);
                    } else if ("test".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 5 && astring.length != 6) {
                            throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
                        }

                        this.testPlayerScore(icommandlistener, astring, 2, minecraftserver);
                    } else if ("operation".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 7) {
                            throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
                        }

                        this.applyPlayerOperation(icommandlistener, astring, 2, minecraftserver);
                    } else {
                        if (!"tag".equalsIgnoreCase(astring[1])) {
                            throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                        }

                        if (astring.length < 4) {
                            throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                        }

                        this.applyPlayerTag(minecraftserver, icommandlistener, astring, 2);
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

                        this.listTeams(icommandlistener, astring, 2, minecraftserver);
                    } else if ("add".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 3) {
                            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                        }

                        this.addTeam(icommandlistener, astring, 2, minecraftserver);
                    } else if ("remove".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3) {
                            throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                        }

                        this.removeTeam(icommandlistener, astring, 2, minecraftserver);
                    } else if ("empty".equalsIgnoreCase(astring[1])) {
                        if (astring.length != 3) {
                            throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                        }

                        this.emptyTeam(icommandlistener, astring, 2, minecraftserver);
                    } else if ("join".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 4 && (astring.length != 3 || !(icommandlistener instanceof EntityPlayer))) {
                            throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                        }

                        this.joinTeam(icommandlistener, astring, 2, minecraftserver);
                    } else if ("leave".equalsIgnoreCase(astring[1])) {
                        if (astring.length < 3 && !(icommandlistener instanceof EntityPlayer)) {
                            throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                        }

                        this.leaveTeam(icommandlistener, astring, 2, minecraftserver);
                    } else {
                        if (!"option".equalsIgnoreCase(astring[1])) {
                            throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                        }

                        if (astring.length != 4 && astring.length != 5) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                        }

                        // Paper start - Block setting options on our collideRule team as it is not persistent
                        if (astring[2].equals(MinecraftServer.getServer().getPlayerList().collideRuleTeamName)) {
                            icommandlistener.sendMessage(new TextComponentTranslation("You cannot set team options on the collideRule team"));
                            return;
                        }
                        // Paper  end

                        this.setTeamOption(icommandlistener, astring, 2, minecraftserver);
                    }
                }

            }
        }
    }

    private boolean handleUserWildcards(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        int i = -1;

        for (int j = 0; j < astring.length; ++j) {
            if (this.isUsernameIndex(astring, j) && "*".equals(astring[j])) {
                if (i >= 0) {
                    throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
                }

                i = j;
            }
        }

        if (i < 0) {
            return false;
        } else {
            ArrayList arraylist = Lists.newArrayList(this.getScoreboard(minecraftserver).getObjectiveNames());
            String s = astring[i];
            ArrayList arraylist1 = Lists.newArrayList();
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                astring[i] = s1;

                try {
                    this.execute(minecraftserver, icommandlistener, astring);
                    arraylist1.add(s1);
                } catch (CommandException commandexception) {
                    TextComponentTranslation chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());

                    chatmessage.getStyle().setColor(TextFormatting.RED);
                    icommandlistener.sendMessage(chatmessage);
                }
            }

            astring[i] = s;
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, arraylist1.size());
            if (arraylist1.isEmpty()) {
                throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
            } else {
                return true;
            }
        }
    }

    protected Scoreboard getScoreboard(MinecraftServer minecraftserver) {
        return minecraftserver.getWorld(0).getScoreboard();
    }

    protected ScoreObjective convertToObjective(String s, boolean flag, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        ScoreObjective scoreboardobjective = scoreboard.getObjective(s);

        if (scoreboardobjective == null) {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { s});
        } else if (flag && scoreboardobjective.getCriteria().isReadOnly()) {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { s});
        } else {
            return scoreboardobjective;
        }
    }

    protected ScorePlayerTeam convertToTeam(String s, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        ScorePlayerTeam scoreboardteam = scoreboard.getTeam(s);

        if (scoreboardteam == null) {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { s});
        } else {
            return scoreboardteam;
        }
    }

    protected void addObjective(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        String s = astring[i++];
        String s1 = astring[i++];
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        IScoreCriteria iscoreboardcriteria = (IScoreCriteria) IScoreCriteria.INSTANCES.get(s1);

        if (iscoreboardcriteria == null) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] { s1});
        } else if (scoreboard.getObjective(s) != null) {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { s});
        } else if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] { s, Integer.valueOf(16)});
        } else if (s.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        } else {
            if (astring.length > i) {
                String s2 = getChatComponentFromNthArg(icommandlistener, astring, i).getUnformattedText();

                if (s2.length() > 32) {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] { s2, Integer.valueOf(32)});
                }

                if (s2.isEmpty()) {
                    scoreboard.addScoreObjective(s, iscoreboardcriteria);
                } else {
                    scoreboard.addScoreObjective(s, iscoreboardcriteria).setDisplayName(s2);
                }
            } else {
                scoreboard.addScoreObjective(s, iscoreboardcriteria);
            }

            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.add.success", new Object[] { s});
        }
    }

    protected void addTeam(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        String s = astring[i++];
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);

        if (scoreboard.getTeam(s) != null) {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { s});
        } else if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] { s, Integer.valueOf(16)});
        } else if (s.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        } else {
            if (astring.length > i) {
                String s1 = getChatComponentFromNthArg(icommandlistener, astring, i).getUnformattedText();

                if (s1.length() > 32) {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] { s1, Integer.valueOf(32)});
                }

                if (s1.isEmpty()) {
                    scoreboard.createTeam(s);
                } else {
                    scoreboard.createTeam(s).setDisplayName(s1);
                }
            } else {
                scoreboard.createTeam(s);
            }

            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.teams.add.success", new Object[] { s});
        }
    }

    protected void setTeamOption(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        ScorePlayerTeam scoreboardteam = this.convertToTeam(astring[i++], minecraftserver);

        if (scoreboardteam != null) {
            String s = astring[i++].toLowerCase(Locale.ROOT);

            if (!"color".equalsIgnoreCase(s) && !"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s) && !"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s) && !"collisionRule".equalsIgnoreCase(s)) {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            } else if (astring.length == 4) {
                if ("color".equalsIgnoreCase(s)) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(TextFormatting.getValidValues(true, false))});
                } else if (!"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s)) {
                    if (!"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s)) {
                        if ("collisionRule".equalsIgnoreCase(s)) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString((Object[]) Team.CollisionRule.getNames())});
                        } else {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                        }
                    } else {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString((Object[]) Team.EnumVisible.getNames())});
                    }
                } else {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection((Collection) Arrays.asList(new String[] { "true", "false"}))});
                }
            } else {
                String s1 = astring[i];

                if ("color".equalsIgnoreCase(s)) {
                    TextFormatting enumchatformat = TextFormatting.getValueByName(s1);

                    if (enumchatformat == null || enumchatformat.isFancyStyling()) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection(TextFormatting.getValidValues(true, false))});
                    }

                    scoreboardteam.setColor(enumchatformat);
                    scoreboardteam.setPrefix(enumchatformat.toString());
                    scoreboardteam.setSuffix(TextFormatting.RESET.toString());
                } else if ("friendlyfire".equalsIgnoreCase(s)) {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1)) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection((Collection) Arrays.asList(new String[] { "true", "false"}))});
                    }

                    scoreboardteam.setAllowFriendlyFire("true".equalsIgnoreCase(s1));
                } else if ("seeFriendlyInvisibles".equalsIgnoreCase(s)) {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1)) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceStringFromCollection((Collection) Arrays.asList(new String[] { "true", "false"}))});
                    }

                    scoreboardteam.setSeeFriendlyInvisiblesEnabled("true".equalsIgnoreCase(s1));
                } else {
                    Team.EnumVisible scoreboardteambase_enumnametagvisibility;

                    if ("nametagVisibility".equalsIgnoreCase(s)) {
                        scoreboardteambase_enumnametagvisibility = Team.EnumVisible.getByName(s1);
                        if (scoreboardteambase_enumnametagvisibility == null) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString((Object[]) Team.EnumVisible.getNames())});
                        }

                        scoreboardteam.setNameTagVisibility(scoreboardteambase_enumnametagvisibility);
                    } else if ("deathMessageVisibility".equalsIgnoreCase(s)) {
                        scoreboardteambase_enumnametagvisibility = Team.EnumVisible.getByName(s1);
                        if (scoreboardteambase_enumnametagvisibility == null) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString((Object[]) Team.EnumVisible.getNames())});
                        }

                        scoreboardteam.setDeathMessageVisibility(scoreboardteambase_enumnametagvisibility);
                    } else if ("collisionRule".equalsIgnoreCase(s)) {
                        Team.CollisionRule scoreboardteambase_enumteampush = Team.CollisionRule.getByName(s1);

                        if (scoreboardteambase_enumteampush == null) {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, joinNiceString((Object[]) Team.CollisionRule.getNames())});
                        }

                        scoreboardteam.setCollisionRule(scoreboardteambase_enumteampush);
                    }
                }

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.teams.option.success", new Object[] { s, scoreboardteam.getName(), s1});
            }
        }
    }

    protected void removeTeam(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        ScorePlayerTeam scoreboardteam = this.convertToTeam(astring[i], minecraftserver);

        if (scoreboardteam != null) {
            scoreboard.removeTeam(scoreboardteam);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.teams.remove.success", new Object[] { scoreboardteam.getName()});
        }
    }

    protected void listTeams(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);

        if (astring.length > i) {
            ScorePlayerTeam scoreboardteam = this.convertToTeam(astring[i], minecraftserver);

            if (scoreboardteam == null) {
                return;
            }

            Collection collection = scoreboardteam.getMembershipCollection();

            icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] { scoreboardteam.getName()});
            }

            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] { Integer.valueOf(collection.size()), scoreboardteam.getName()});

            chatmessage.getStyle().setColor(TextFormatting.DARK_GREEN);
            icommandlistener.sendMessage(chatmessage);
            icommandlistener.sendMessage(new TextComponentString(joinNiceString(collection.toArray())));
        } else {
            Collection collection1 = scoreboard.getTeams();

            icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection1.size());
            if (collection1.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }

            TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.scoreboard.teams.list.count", new Object[] { Integer.valueOf(collection1.size())});

            chatmessage1.getStyle().setColor(TextFormatting.DARK_GREEN);
            icommandlistener.sendMessage(chatmessage1);
            Iterator iterator = collection1.iterator();

            while (iterator.hasNext()) {
                ScorePlayerTeam scoreboardteam1 = (ScorePlayerTeam) iterator.next();

                icommandlistener.sendMessage(new TextComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] { scoreboardteam1.getName(), scoreboardteam1.getDisplayName(), Integer.valueOf(scoreboardteam1.getMembershipCollection().size())}));
            }
        }

    }

    protected void joinTeam(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        String s = astring[i++];
        HashSet hashset = Sets.newHashSet();
        HashSet hashset1 = Sets.newHashSet();
        String s1;

        if (icommandlistener instanceof EntityPlayer && i == astring.length) {
            s1 = getCommandSenderAsPlayer(icommandlistener).getName();
            if (scoreboard.addPlayerToTeam(s1, s)) {
                hashset.add(s1);
            } else {
                hashset1.add(s1);
            }
        } else {
            while (i < astring.length) {
                s1 = astring[i++];
                if (EntitySelector.isSelector(s1)) {
                    List list = getEntityList(minecraftserver, icommandlistener, s1);
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();
                        if (!entity.world.paperConfig.nonPlayerEntitiesOnScoreboards && !(entity instanceof EntityPlayer)) { continue; } // Paper
                        String s2 = getEntityName(minecraftserver, icommandlistener, entity.getCachedUniqueIdString());

                        if (scoreboard.addPlayerToTeam(s2, s)) {
                            hashset.add(s2);
                        } else {
                            hashset1.add(s2);
                        }
                    }
                } else {
                    String s3 = getEntityName(minecraftserver, icommandlistener, s1);

                    if (scoreboard.addPlayerToTeam(s3, s)) {
                        hashset.add(s3);
                    } else {
                        hashset1.add(s3);
                    }
                }
            }
        }

        if (!hashset.isEmpty()) {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, hashset.size());
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.teams.join.success", new Object[] { Integer.valueOf(hashset.size()), s, joinNiceString(hashset.toArray(new String[hashset.size()]))});
        }

        if (!hashset1.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] { Integer.valueOf(hashset1.size()), s, joinNiceString(hashset1.toArray(new String[hashset1.size()]))});
        }
    }

    protected void leaveTeam(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        HashSet hashset = Sets.newHashSet();
        HashSet hashset1 = Sets.newHashSet();
        String s;

        if (icommandlistener instanceof EntityPlayer && i == astring.length) {
            s = getCommandSenderAsPlayer(icommandlistener).getName();
            if (scoreboard.removePlayerFromTeams(s)) {
                hashset.add(s);
            } else {
                hashset1.add(s);
            }
        } else {
            while (i < astring.length) {
                s = astring[i++];
                if (EntitySelector.isSelector(s)) {
                    List list = getEntityList(minecraftserver, icommandlistener, s);
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();
                        String s1 = getEntityName(minecraftserver, icommandlistener, entity.getCachedUniqueIdString());

                        if (scoreboard.removePlayerFromTeams(s1)) {
                            hashset.add(s1);
                        } else {
                            hashset1.add(s1);
                        }
                    }
                } else {
                    String s2 = getEntityName(minecraftserver, icommandlistener, s);

                    if (scoreboard.removePlayerFromTeams(s2)) {
                        hashset.add(s2);
                    } else {
                        hashset1.add(s2);
                    }
                }
            }
        }

        if (!hashset.isEmpty()) {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, hashset.size());
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.teams.leave.success", new Object[] { Integer.valueOf(hashset.size()), joinNiceString(hashset.toArray(new String[hashset.size()]))});
        }

        if (!hashset1.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] { Integer.valueOf(hashset1.size()), joinNiceString(hashset1.toArray(new String[hashset1.size()]))});
        }
    }

    protected void emptyTeam(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        ScorePlayerTeam scoreboardteam = this.convertToTeam(astring[i], minecraftserver);

        if (scoreboardteam != null) {
            ArrayList arraylist = Lists.newArrayList(scoreboardteam.getMembershipCollection());

            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, arraylist.size());
            if (arraylist.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] { scoreboardteam.getName()});
            } else {
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();

                    scoreboard.removePlayerFromTeam(s, scoreboardteam);
                }

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.teams.empty.success", new Object[] { Integer.valueOf(arraylist.size()), scoreboardteam.getName()});
            }
        }
    }

    protected void removeObjective(ICommandSender icommandlistener, String s, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        ScoreObjective scoreboardobjective = this.convertToObjective(s, false, minecraftserver);

        scoreboard.removeObjective(scoreboardobjective);
        notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.remove.success", new Object[] { s});
    }

    protected void listObjectives(ICommandSender icommandlistener, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        Collection collection = scoreboard.getScoreObjectives();

        if (collection.isEmpty()) {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        } else {
            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] { Integer.valueOf(collection.size())});

            chatmessage.getStyle().setColor(TextFormatting.DARK_GREEN);
            icommandlistener.sendMessage(chatmessage);
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                icommandlistener.sendMessage(new TextComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] { scoreboardobjective.getName(), scoreboardobjective.getDisplayName(), scoreboardobjective.getCriteria().getName()}));
            }

        }
    }

    protected void setDisplayObjective(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        String s = astring[i++];
        int j = Scoreboard.getObjectiveDisplaySlotNumber(s);
        ScoreObjective scoreboardobjective = null;

        if (astring.length == 4) {
            scoreboardobjective = this.convertToObjective(astring[i], false, minecraftserver);
        }

        if (j < 0) {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { s});
        } else {
            scoreboard.setObjectiveInDisplaySlot(j, scoreboardobjective);
            if (scoreboardobjective != null) {
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] { Scoreboard.getObjectiveDisplaySlot(j), scoreboardobjective.getName()});
            } else {
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] { Scoreboard.getObjectiveDisplaySlot(j)});
            }

        }
    }

    protected void listPlayers(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);

        if (astring.length > i) {
            String s = getEntityName(minecraftserver, icommandlistener, astring[i]);
            Map map = scoreboard.getObjectivesForEntity(s);

            icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, map.size());
            if (map.isEmpty()) {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { s});
            }

            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] { Integer.valueOf(map.size()), s});

            chatmessage.getStyle().setColor(TextFormatting.DARK_GREEN);
            icommandlistener.sendMessage(chatmessage);
            Iterator iterator = map.values().iterator();

            while (iterator.hasNext()) {
                Score scoreboardscore = (Score) iterator.next();

                icommandlistener.sendMessage(new TextComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] { Integer.valueOf(scoreboardscore.getScorePoints()), scoreboardscore.getObjective().getDisplayName(), scoreboardscore.getObjective().getName()}));
            }
        } else {
            Collection collection = scoreboard.getObjectiveNames();

            icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }

            TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.scoreboard.players.list.count", new Object[] { Integer.valueOf(collection.size())});

            chatmessage1.getStyle().setColor(TextFormatting.DARK_GREEN);
            icommandlistener.sendMessage(chatmessage1);
            icommandlistener.sendMessage(new TextComponentString(joinNiceString(collection.toArray())));
        }

    }

    protected void addPlayerScore(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        String s = astring[i - 1];
        int j = i;
        String s1 = getEntityName(minecraftserver, icommandlistener, astring[i++]);

        if (s1.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s1, Integer.valueOf(40)});
        } else {
            ScoreObjective scoreboardobjective = this.convertToObjective(astring[i++], true, minecraftserver);
            int k = "set".equalsIgnoreCase(s) ? parseInt(astring[i++]) : parseInt(astring[i++], 0);

            if (astring.length > i) {
                Entity entity = getEntity(minecraftserver, icommandlistener, astring[j]);

                try {
                    NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(buildString(astring, i));
                    NBTTagCompound nbttagcompound1 = entityToNBT(entity);

                    if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true)) {
                        throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] { s1});
                    }
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            Scoreboard scoreboard = this.getScoreboard(minecraftserver);
            Score scoreboardscore = scoreboard.getOrCreateScore(s1, scoreboardobjective);

            if ("set".equalsIgnoreCase(s)) {
                scoreboardscore.setScorePoints(k);
            } else if ("add".equalsIgnoreCase(s)) {
                scoreboardscore.increaseScore(k);
            } else {
                scoreboardscore.decreaseScore(k);
            }

            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.set.success", new Object[] { scoreboardobjective.getName(), s1, Integer.valueOf(scoreboardscore.getScorePoints())});
        }
    }

    protected void resetPlayerScore(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        String s = getEntityName(minecraftserver, icommandlistener, astring[i++]);

        if (astring.length > i) {
            ScoreObjective scoreboardobjective = this.convertToObjective(astring[i++], false, minecraftserver);

            scoreboard.removeObjectiveFromEntity(s, scoreboardobjective);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.resetscore.success", new Object[] { scoreboardobjective.getName(), s});
        } else {
            scoreboard.removeObjectiveFromEntity(s, (ScoreObjective) null);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.reset.success", new Object[] { s});
        }

    }

    protected void enablePlayerTrigger(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        String s = getPlayerName(minecraftserver, icommandlistener, astring[i++]);

        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40)});
        } else {
            ScoreObjective scoreboardobjective = this.convertToObjective(astring[i], false, minecraftserver);

            if (scoreboardobjective.getCriteria() != IScoreCriteria.TRIGGER) {
                throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] { scoreboardobjective.getName()});
            } else {
                Score scoreboardscore = scoreboard.getOrCreateScore(s, scoreboardobjective);

                scoreboardscore.setLocked(false);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.enable.success", new Object[] { scoreboardobjective.getName(), s});
            }
        }
    }

    protected void testPlayerScore(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        String s = getEntityName(minecraftserver, icommandlistener, astring[i++]);

        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40)});
        } else {
            ScoreObjective scoreboardobjective = this.convertToObjective(astring[i++], false, minecraftserver);

            if (!scoreboard.entityHasObjective(s, scoreboardobjective)) {
                throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] { scoreboardobjective.getName(), s});
            } else {
                int j = astring[i].equals("*") ? Integer.MIN_VALUE : parseInt(astring[i]);

                ++i;
                int k = i < astring.length && !astring[i].equals("*") ? parseInt(astring[i], j) : Integer.MAX_VALUE;
                Score scoreboardscore = scoreboard.getOrCreateScore(s, scoreboardobjective);

                if (scoreboardscore.getScorePoints() >= j && scoreboardscore.getScorePoints() <= k) {
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.test.success", new Object[] { Integer.valueOf(scoreboardscore.getScorePoints()), Integer.valueOf(j), Integer.valueOf(k)});
                } else {
                    throw new CommandException("commands.scoreboard.players.test.failed", new Object[] { Integer.valueOf(scoreboardscore.getScorePoints()), Integer.valueOf(j), Integer.valueOf(k)});
                }
            }
        }
    }

    protected void applyPlayerOperation(ICommandSender icommandlistener, String[] astring, int i, MinecraftServer minecraftserver) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard(minecraftserver);
        String s = getEntityName(minecraftserver, icommandlistener, astring[i++]);
        ScoreObjective scoreboardobjective = this.convertToObjective(astring[i++], true, minecraftserver);
        String s1 = astring[i++];
        String s2 = getEntityName(minecraftserver, icommandlistener, astring[i++]);
        ScoreObjective scoreboardobjective1 = this.convertToObjective(astring[i], false, minecraftserver);

        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, Integer.valueOf(40)});
        } else if (s2.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s2, Integer.valueOf(40)});
        } else {
            Score scoreboardscore = scoreboard.getOrCreateScore(s, scoreboardobjective);

            if (!scoreboard.entityHasObjective(s2, scoreboardobjective1)) {
                throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] { scoreboardobjective1.getName(), s2});
            } else {
                Score scoreboardscore1 = scoreboard.getOrCreateScore(s2, scoreboardobjective1);

                if ("+=".equals(s1)) {
                    scoreboardscore.setScorePoints(scoreboardscore.getScorePoints() + scoreboardscore1.getScorePoints());
                } else if ("-=".equals(s1)) {
                    scoreboardscore.setScorePoints(scoreboardscore.getScorePoints() - scoreboardscore1.getScorePoints());
                } else if ("*=".equals(s1)) {
                    scoreboardscore.setScorePoints(scoreboardscore.getScorePoints() * scoreboardscore1.getScorePoints());
                } else if ("/=".equals(s1)) {
                    if (scoreboardscore1.getScorePoints() != 0) {
                        scoreboardscore.setScorePoints(scoreboardscore.getScorePoints() / scoreboardscore1.getScorePoints());
                    }
                } else if ("%=".equals(s1)) {
                    if (scoreboardscore1.getScorePoints() != 0) {
                        scoreboardscore.setScorePoints(scoreboardscore.getScorePoints() % scoreboardscore1.getScorePoints());
                    }
                } else if ("=".equals(s1)) {
                    scoreboardscore.setScorePoints(scoreboardscore1.getScorePoints());
                } else if ("<".equals(s1)) {
                    scoreboardscore.setScorePoints(Math.min(scoreboardscore.getScorePoints(), scoreboardscore1.getScorePoints()));
                } else if (">".equals(s1)) {
                    scoreboardscore.setScorePoints(Math.max(scoreboardscore.getScorePoints(), scoreboardscore1.getScorePoints()));
                } else {
                    if (!"><".equals(s1)) {
                        throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] { s1});
                    }

                    int j = scoreboardscore.getScorePoints();

                    scoreboardscore.setScorePoints(scoreboardscore1.getScorePoints());
                    scoreboardscore1.setScorePoints(j);
                }

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.operation.success", new Object[0]);
            }
        }
    }

    protected void applyPlayerTag(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, int i) throws CommandException {
        String s = getEntityName(minecraftserver, icommandlistener, astring[i]);
        Entity entity = getEntity(minecraftserver, icommandlistener, astring[i++]);
        String s1 = astring[i++];
        Set set = entity.getTags();

        if ("list".equals(s1)) {
            if (!set.isEmpty()) {
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.scoreboard.players.tag.list", new Object[] { s});

                chatmessage.getStyle().setColor(TextFormatting.DARK_GREEN);
                icommandlistener.sendMessage(chatmessage);
                icommandlistener.sendMessage(new TextComponentString(joinNiceString(set.toArray())));
            }

            icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, set.size());
        } else if (astring.length < 5) {
            throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
        } else {
            String s2 = astring[i++];

            if (astring.length > i) {
                try {
                    NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(buildString(astring, i));
                    NBTTagCompound nbttagcompound1 = entityToNBT(entity);

                    if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true)) {
                        throw new CommandException("commands.scoreboard.players.tag.tagMismatch", new Object[] { s});
                    }
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.scoreboard.players.tag.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            if ("add".equals(s1)) {
                if (!entity.addTag(s2)) {
                    throw new CommandException("commands.scoreboard.players.tag.tooMany", new Object[] { Integer.valueOf(1024)});
                }

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.tag.success.add", new Object[] { s2});
            } else {
                if (!"remove".equals(s1)) {
                    throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                }

                if (!entity.removeTag(s2)) {
                    throw new CommandException("commands.scoreboard.players.tag.notFound", new Object[] { s2});
                }

                notifyCommandListener(icommandlistener, (ICommand) this, "commands.scoreboard.players.tag.success.remove", new Object[] { s2});
            }

        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        if (astring.length == 1) {
            return getListOfStringsMatchingLastWord(astring, new String[] { "objectives", "players", "teams"});
        } else {
            if ("objectives".equalsIgnoreCase(astring[0])) {
                if (astring.length == 2) {
                    return getListOfStringsMatchingLastWord(astring, new String[] { "list", "add", "remove", "setdisplay"});
                }

                if ("add".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 4) {
                        Set set = IScoreCriteria.INSTANCES.keySet();

                        return getListOfStringsMatchingLastWord(astring, (Collection) set);
                    }
                } else if ("remove".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 3) {
                        return getListOfStringsMatchingLastWord(astring, (Collection) this.getObjectiveNames(false, minecraftserver));
                    }
                } else if ("setdisplay".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 3) {
                        return getListOfStringsMatchingLastWord(astring, Scoreboard.getDisplaySlotStrings());
                    }

                    if (astring.length == 4) {
                        return getListOfStringsMatchingLastWord(astring, (Collection) this.getObjectiveNames(false, minecraftserver));
                    }
                }
            } else if ("players".equalsIgnoreCase(astring[0])) {
                if (astring.length == 2) {
                    return getListOfStringsMatchingLastWord(astring, new String[] { "set", "add", "remove", "reset", "list", "enable", "test", "operation", "tag"});
                }

                if (!"set".equalsIgnoreCase(astring[1]) && !"add".equalsIgnoreCase(astring[1]) && !"remove".equalsIgnoreCase(astring[1]) && !"reset".equalsIgnoreCase(astring[1])) {
                    if ("enable".equalsIgnoreCase(astring[1])) {
                        if (astring.length == 3) {
                            return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
                        }

                        if (astring.length == 4) {
                            return getListOfStringsMatchingLastWord(astring, (Collection) this.getTriggerNames(minecraftserver));
                        }
                    } else if (!"list".equalsIgnoreCase(astring[1]) && !"test".equalsIgnoreCase(astring[1])) {
                        if ("operation".equalsIgnoreCase(astring[1])) {
                            if (astring.length == 3) {
                                return getListOfStringsMatchingLastWord(astring, this.getScoreboard(minecraftserver).getObjectiveNames());
                            }

                            if (astring.length == 4) {
                                return getListOfStringsMatchingLastWord(astring, (Collection) this.getObjectiveNames(true, minecraftserver));
                            }

                            if (astring.length == 5) {
                                return getListOfStringsMatchingLastWord(astring, new String[] { "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><"});
                            }

                            if (astring.length == 6) {
                                return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
                            }

                            if (astring.length == 7) {
                                return getListOfStringsMatchingLastWord(astring, (Collection) this.getObjectiveNames(false, minecraftserver));
                            }
                        } else if ("tag".equalsIgnoreCase(astring[1])) {
                            if (astring.length == 3) {
                                return getListOfStringsMatchingLastWord(astring, this.getScoreboard(minecraftserver).getObjectiveNames());
                            }

                            if (astring.length == 4) {
                                return getListOfStringsMatchingLastWord(astring, new String[] { "add", "remove", "list"});
                            }
                        }
                    } else {
                        if (astring.length == 3) {
                            return getListOfStringsMatchingLastWord(astring, this.getScoreboard(minecraftserver).getObjectiveNames());
                        }

                        if (astring.length == 4 && "test".equalsIgnoreCase(astring[1])) {
                            return getListOfStringsMatchingLastWord(astring, (Collection) this.getObjectiveNames(false, minecraftserver));
                        }
                    }
                } else {
                    if (astring.length == 3) {
                        return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
                    }

                    if (astring.length == 4) {
                        return getListOfStringsMatchingLastWord(astring, (Collection) this.getObjectiveNames(true, minecraftserver));
                    }
                }
            } else if ("teams".equalsIgnoreCase(astring[0])) {
                if (astring.length == 2) {
                    return getListOfStringsMatchingLastWord(astring, new String[] { "add", "remove", "join", "leave", "empty", "list", "option"});
                }

                if ("join".equalsIgnoreCase(astring[1])) {
                    if (astring.length == 3) {
                        return getListOfStringsMatchingLastWord(astring, this.getScoreboard(minecraftserver).getTeamNames());
                    }

                    if (astring.length >= 4) {
                        return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
                    }
                } else {
                    if ("leave".equalsIgnoreCase(astring[1])) {
                        return getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames());
                    }

                    if (!"empty".equalsIgnoreCase(astring[1]) && !"list".equalsIgnoreCase(astring[1]) && !"remove".equalsIgnoreCase(astring[1])) {
                        if ("option".equalsIgnoreCase(astring[1])) {
                            if (astring.length == 3) {
                                return getListOfStringsMatchingLastWord(astring, this.getScoreboard(minecraftserver).getTeamNames());
                            }

                            if (astring.length == 4) {
                                return getListOfStringsMatchingLastWord(astring, new String[] { "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility", "collisionRule"});
                            }

                            if (astring.length == 5) {
                                if ("color".equalsIgnoreCase(astring[3])) {
                                    return getListOfStringsMatchingLastWord(astring, TextFormatting.getValidValues(true, false));
                                }

                                if ("nametagVisibility".equalsIgnoreCase(astring[3]) || "deathMessageVisibility".equalsIgnoreCase(astring[3])) {
                                    return getListOfStringsMatchingLastWord(astring, Team.EnumVisible.getNames());
                                }

                                if ("collisionRule".equalsIgnoreCase(astring[3])) {
                                    return getListOfStringsMatchingLastWord(astring, Team.CollisionRule.getNames());
                                }

                                if ("friendlyfire".equalsIgnoreCase(astring[3]) || "seeFriendlyInvisibles".equalsIgnoreCase(astring[3])) {
                                    return getListOfStringsMatchingLastWord(astring, new String[] { "true", "false"});
                                }
                            }
                        }
                    } else if (astring.length == 3) {
                        return getListOfStringsMatchingLastWord(astring, this.getScoreboard(minecraftserver).getTeamNames());
                    }
                }
            }

            return Collections.emptyList();
        }
    }

    protected List<String> getObjectiveNames(boolean flag, MinecraftServer minecraftserver) {
        Collection collection = this.getScoreboard(minecraftserver).getScoreObjectives();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (!flag || !scoreboardobjective.getCriteria().isReadOnly()) {
                arraylist.add(scoreboardobjective.getName());
            }
        }

        return arraylist;
    }

    protected List<String> getTriggerNames(MinecraftServer minecraftserver) {
        Collection collection = this.getScoreboard(minecraftserver).getScoreObjectives();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (scoreboardobjective.getCriteria() == IScoreCriteria.TRIGGER) {
                arraylist.add(scoreboardobjective.getName());
            }
        }

        return arraylist;
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return !"players".equalsIgnoreCase(astring[0]) ? ("teams".equalsIgnoreCase(astring[0]) ? i == 2 : false) : (astring.length > 1 && "operation".equalsIgnoreCase(astring[1]) ? i == 2 || i == 5 : i == 2);
    }
}

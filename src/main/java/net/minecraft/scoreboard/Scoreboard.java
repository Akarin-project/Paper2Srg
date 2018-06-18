package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class Scoreboard {

    private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
    private final Map<IScoreCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
    private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();
    private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
    private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
    private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
    private static String[] displaySlots;

    public Scoreboard() {}

    @Nullable
    public ScoreObjective getObjective(String s) {
        return (ScoreObjective) this.scoreObjectives.get(s);
    }

    public ScoreObjective addScoreObjective(String s, IScoreCriteria iscoreboardcriteria) {
        if (s.length() > 16) {
            throw new IllegalArgumentException("The objective name \'" + s + "\' is too long!");
        } else {
            ScoreObjective scoreboardobjective = this.getObjective(s);

            if (scoreboardobjective != null) {
                throw new IllegalArgumentException("An objective with the name \'" + s + "\' already exists!");
            } else {
                scoreboardobjective = new ScoreObjective(this, s, iscoreboardcriteria);
                Object object = (List) this.scoreObjectiveCriterias.get(iscoreboardcriteria);

                if (object == null) {
                    object = Lists.newArrayList();
                    this.scoreObjectiveCriterias.put(iscoreboardcriteria, object);
                }

                ((List) object).add(scoreboardobjective);
                this.scoreObjectives.put(s, scoreboardobjective);
                this.onScoreObjectiveAdded(scoreboardobjective);
                return scoreboardobjective;
            }
        }
    }

    public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreCriteria iscoreboardcriteria) {
        Collection collection = (Collection) this.scoreObjectiveCriterias.get(iscoreboardcriteria);

        return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
    }

    public boolean entityHasObjective(String s, ScoreObjective scoreboardobjective) {
        Map map = (Map) this.entitiesScoreObjectives.get(s);

        if (map == null) {
            return false;
        } else {
            Score scoreboardscore = (Score) map.get(scoreboardobjective);

            return scoreboardscore != null;
        }
    }

    public Score getOrCreateScore(String s, ScoreObjective scoreboardobjective) {
        if (s.length() > 40) {
            throw new IllegalArgumentException("The player name \'" + s + "\' is too long!");
        } else {
            Object object = (Map) this.entitiesScoreObjectives.get(s);

            if (object == null) {
                object = Maps.newHashMap();
                this.entitiesScoreObjectives.put(s, object);
            }

            Score scoreboardscore = (Score) ((Map) object).get(scoreboardobjective);

            if (scoreboardscore == null) {
                scoreboardscore = new Score(this, scoreboardobjective, s);
                ((Map) object).put(scoreboardobjective, scoreboardscore);
            }

            return scoreboardscore;
        }
    }

    public Collection<Score> getSortedScores(ScoreObjective scoreboardobjective) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.entitiesScoreObjectives.values().iterator();

        while (iterator.hasNext()) {
            Map map = (Map) iterator.next();
            Score scoreboardscore = (Score) map.get(scoreboardobjective);

            if (scoreboardscore != null) {
                arraylist.add(scoreboardscore);
            }
        }

        Collections.sort(arraylist, Score.SCORE_COMPARATOR);
        return arraylist;
    }

    public Collection<ScoreObjective> getScoreObjectives() {
        return this.scoreObjectives.values();
    }

    public Collection<String> getObjectiveNames() {
        return this.entitiesScoreObjectives.keySet();
    }

    public void removeObjectiveFromEntity(String s, ScoreObjective scoreboardobjective) {
        Map map;

        if (scoreboardobjective == null) {
            map = (Map) this.entitiesScoreObjectives.remove(s);
            if (map != null) {
                this.broadcastScoreUpdate(s);
            }
        } else {
            map = (Map) this.entitiesScoreObjectives.get(s);
            if (map != null) {
                Score scoreboardscore = (Score) map.remove(scoreboardobjective);

                if (map.size() < 1) {
                    Map map1 = (Map) this.entitiesScoreObjectives.remove(s);

                    if (map1 != null) {
                        this.broadcastScoreUpdate(s);
                    }
                } else if (scoreboardscore != null) {
                    this.broadcastScoreUpdate(s, scoreboardobjective);
                }
            }
        }

    }

    public Collection<Score> getScores() {
        Collection collection = this.entitiesScoreObjectives.values();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Map map = (Map) iterator.next();

            arraylist.addAll(map.values());
        }

        return arraylist;
    }

    public Map<ScoreObjective, Score> getObjectivesForEntity(String s) {
        Object object = (Map) this.entitiesScoreObjectives.get(s);

        if (object == null) {
            object = Maps.newHashMap();
        }

        return (Map) object;
    }

    public void removeObjective(ScoreObjective scoreboardobjective) {
        this.scoreObjectives.remove(scoreboardobjective.getName());

        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) == scoreboardobjective) {
                this.setObjectiveInDisplaySlot(i, (ScoreObjective) null);
            }
        }

        List list = (List) this.scoreObjectiveCriterias.get(scoreboardobjective.getCriteria());

        if (list != null) {
            list.remove(scoreboardobjective);
        }

        Iterator iterator = this.entitiesScoreObjectives.values().iterator();

        while (iterator.hasNext()) {
            Map map = (Map) iterator.next();

            map.remove(scoreboardobjective);
        }

        this.onScoreObjectiveRemoved(scoreboardobjective);
    }

    public void setObjectiveInDisplaySlot(int i, ScoreObjective scoreboardobjective) {
        this.objectiveDisplaySlots[i] = scoreboardobjective;
    }

    @Nullable
    public ScoreObjective getObjectiveInDisplaySlot(int i) {
        return this.objectiveDisplaySlots[i];
    }

    public ScorePlayerTeam getTeam(String s) {
        return (ScorePlayerTeam) this.teams.get(s);
    }

    public ScorePlayerTeam createTeam(String s) {
        if (s.length() > 16) {
            throw new IllegalArgumentException("The team name \'" + s + "\' is too long!");
        } else {
            ScorePlayerTeam scoreboardteam = this.getTeam(s);

            if (scoreboardteam != null) {
                throw new IllegalArgumentException("A team with the name \'" + s + "\' already exists!");
            } else {
                scoreboardteam = new ScorePlayerTeam(this, s);
                this.teams.put(s, scoreboardteam);
                this.broadcastTeamCreated(scoreboardteam);
                return scoreboardteam;
            }
        }
    }

    public void removeTeam(ScorePlayerTeam scoreboardteam) {
        this.teams.remove(scoreboardteam.getName());
        Iterator iterator = scoreboardteam.getMembershipCollection().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.teamMemberships.remove(s);
        }

        this.broadcastTeamRemove(scoreboardteam);
    }

    public boolean addPlayerToTeam(String s, String s1) {
        if (s.length() > 40) {
            throw new IllegalArgumentException("The player name \'" + s + "\' is too long!");
        } else if (!this.teams.containsKey(s1)) {
            return false;
        } else {
            ScorePlayerTeam scoreboardteam = this.getTeam(s1);

            if (this.getPlayersTeam(s) != null) {
                this.removePlayerFromTeams(s);
            }

            this.teamMemberships.put(s, scoreboardteam);
            scoreboardteam.getMembershipCollection().add(s);
            return true;
        }
    }

    public boolean removePlayerFromTeams(String s) {
        ScorePlayerTeam scoreboardteam = this.getPlayersTeam(s);

        if (scoreboardteam != null) {
            this.removePlayerFromTeam(s, scoreboardteam);
            return true;
        } else {
            return false;
        }
    }

    public void removePlayerFromTeam(String s, ScorePlayerTeam scoreboardteam) {
        if (this.getPlayersTeam(s) != scoreboardteam) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + scoreboardteam.getName() + "\'.");
        } else {
            this.teamMemberships.remove(s);
            scoreboardteam.getMembershipCollection().remove(s);
        }
    }

    public Collection<String> getTeamNames() {
        return this.teams.keySet();
    }

    public Collection<ScorePlayerTeam> getTeams() {
        return this.teams.values();
    }

    @Nullable
    public ScorePlayerTeam getPlayersTeam(String s) {
        return (ScorePlayerTeam) this.teamMemberships.get(s);
    }

    public void onScoreObjectiveAdded(ScoreObjective scoreboardobjective) {}

    public void onObjectiveDisplayNameChanged(ScoreObjective scoreboardobjective) {}

    public void onScoreObjectiveRemoved(ScoreObjective scoreboardobjective) {}

    public void onScoreUpdated(Score scoreboardscore) {}

    public void broadcastScoreUpdate(String s) {}

    public void broadcastScoreUpdate(String s, ScoreObjective scoreboardobjective) {}

    public void broadcastTeamCreated(ScorePlayerTeam scoreboardteam) {}

    public void broadcastTeamInfoUpdate(ScorePlayerTeam scoreboardteam) {}

    public void broadcastTeamRemove(ScorePlayerTeam scoreboardteam) {}

    public static String getObjectiveDisplaySlot(int i) {
        switch (i) {
        case 0:
            return "list";

        case 1:
            return "sidebar";

        case 2:
            return "belowName";

        default:
            if (i >= 3 && i <= 18) {
                TextFormatting enumchatformat = TextFormatting.fromColorIndex(i - 3);

                if (enumchatformat != null && enumchatformat != TextFormatting.RESET) {
                    return "sidebar.team." + enumchatformat.getFriendlyName();
                }
            }

            return null;
        }
    }

    public static int getObjectiveDisplaySlotNumber(String s) {
        if ("list".equalsIgnoreCase(s)) {
            return 0;
        } else if ("sidebar".equalsIgnoreCase(s)) {
            return 1;
        } else if ("belowName".equalsIgnoreCase(s)) {
            return 2;
        } else {
            if (s.startsWith("sidebar.team.")) {
                String s1 = s.substring("sidebar.team.".length());
                TextFormatting enumchatformat = TextFormatting.getValueByName(s1);

                if (enumchatformat != null && enumchatformat.getColorIndex() >= 0) {
                    return enumchatformat.getColorIndex() + 3;
                }
            }

            return -1;
        }
    }

    public static String[] getDisplaySlotStrings() {
        if (Scoreboard.displaySlots == null) {
            Scoreboard.displaySlots = new String[19];

            for (int i = 0; i < 19; ++i) {
                Scoreboard.displaySlots[i] = getObjectiveDisplaySlot(i);
            }
        }

        return Scoreboard.displaySlots;
    }

    public void removeEntity(Entity entity) {
        if (entity != null && !(entity instanceof EntityPlayer) && !entity.isEntityAlive()) {
            String s = entity.getCachedUniqueIdString();

            this.removeObjectiveFromEntity(s, (ScoreObjective) null);
            this.removePlayerFromTeams(s);
        }
    }
}

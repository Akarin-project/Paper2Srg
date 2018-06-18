package net.minecraft.scoreboard;

import java.util.Collection;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.WorldSavedData;

public class ScoreboardSaveData extends WorldSavedData {

    private static final Logger LOGGER = LogManager.getLogger();
    private Scoreboard scoreboard;
    private NBTTagCompound delayedInitNbt;

    public ScoreboardSaveData() {
        this("scoreboard");
    }

    public ScoreboardSaveData(String s) {
        super(s);
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        if (this.delayedInitNbt != null) {
            this.readFromNBT(this.delayedInitNbt);
        }

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        if (this.scoreboard == null) {
            this.delayedInitNbt = nbttagcompound;
        } else {
            this.readObjectives(nbttagcompound.getTagList("Objectives", 10));
            this.readScores(nbttagcompound.getTagList("PlayerScores", 10));
            if (nbttagcompound.hasKey("DisplaySlots", 10)) {
                this.readDisplayConfig(nbttagcompound.getCompoundTag("DisplaySlots"));
            }

            if (nbttagcompound.hasKey("Teams", 9)) {
                this.readTeams(nbttagcompound.getTagList("Teams", 10));
            }

        }
    }

    protected void readTeams(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            String s = nbttagcompound.getString("Name");

            if (s.length() > 16) {
                s = s.substring(0, 16);
            }

            ScorePlayerTeam scoreboardteam = this.scoreboard.createTeam(s);
            String s1 = nbttagcompound.getString("DisplayName");

            if (s1.length() > 32) {
                s1 = s1.substring(0, 32);
            }

            scoreboardteam.setDisplayName(s1);
            if (nbttagcompound.hasKey("TeamColor", 8)) {
                scoreboardteam.setColor(TextFormatting.getValueByName(nbttagcompound.getString("TeamColor")));
            }

            scoreboardteam.setPrefix(nbttagcompound.getString("Prefix"));
            scoreboardteam.setSuffix(nbttagcompound.getString("Suffix"));
            if (nbttagcompound.hasKey("AllowFriendlyFire", 99)) {
                scoreboardteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
            }

            if (nbttagcompound.hasKey("SeeFriendlyInvisibles", 99)) {
                scoreboardteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
            }

            Team.EnumVisible scoreboardteambase_enumnametagvisibility;

            if (nbttagcompound.hasKey("NameTagVisibility", 8)) {
                scoreboardteambase_enumnametagvisibility = Team.EnumVisible.getByName(nbttagcompound.getString("NameTagVisibility"));
                if (scoreboardteambase_enumnametagvisibility != null) {
                    scoreboardteam.setNameTagVisibility(scoreboardteambase_enumnametagvisibility);
                }
            }

            if (nbttagcompound.hasKey("DeathMessageVisibility", 8)) {
                scoreboardteambase_enumnametagvisibility = Team.EnumVisible.getByName(nbttagcompound.getString("DeathMessageVisibility"));
                if (scoreboardteambase_enumnametagvisibility != null) {
                    scoreboardteam.setDeathMessageVisibility(scoreboardteambase_enumnametagvisibility);
                }
            }

            if (nbttagcompound.hasKey("CollisionRule", 8)) {
                Team.CollisionRule scoreboardteambase_enumteampush = Team.CollisionRule.getByName(nbttagcompound.getString("CollisionRule"));

                if (scoreboardteambase_enumteampush != null) {
                    scoreboardteam.setCollisionRule(scoreboardteambase_enumteampush);
                }
            }

            this.loadTeamPlayers(scoreboardteam, nbttagcompound.getTagList("Players", 8));
        }

    }

    protected void loadTeamPlayers(ScorePlayerTeam scoreboardteam, NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            this.scoreboard.addPlayerToTeam(nbttaglist.getStringTagAt(i), scoreboardteam.getName());
        }

    }

    protected void readDisplayConfig(NBTTagCompound nbttagcompound) {
        for (int i = 0; i < 19; ++i) {
            if (nbttagcompound.hasKey("slot_" + i, 8)) {
                String s = nbttagcompound.getString("slot_" + i);
                ScoreObjective scoreboardobjective = this.scoreboard.getObjective(s);

                this.scoreboard.setObjectiveInDisplaySlot(i, scoreboardobjective);
            }
        }

    }

    protected void readObjectives(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            IScoreCriteria iscoreboardcriteria = (IScoreCriteria) IScoreCriteria.INSTANCES.get(nbttagcompound.getString("CriteriaName"));

            if (iscoreboardcriteria != null) {
                String s = nbttagcompound.getString("Name");

                if (s.length() > 16) {
                    s = s.substring(0, 16);
                }

                ScoreObjective scoreboardobjective = this.scoreboard.addScoreObjective(s, iscoreboardcriteria);

                scoreboardobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
                scoreboardobjective.setRenderType(IScoreCriteria.EnumRenderType.getByName(nbttagcompound.getString("RenderType")));
            }
        }

    }

    protected void readScores(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            ScoreObjective scoreboardobjective = this.scoreboard.getObjective(nbttagcompound.getString("Objective"));
            String s = nbttagcompound.getString("Name");

            if (s.length() > 40) {
                s = s.substring(0, 40);
            }

            Score scoreboardscore = this.scoreboard.getOrCreateScore(s, scoreboardobjective);

            scoreboardscore.setScorePoints(nbttagcompound.getInteger("Score"));
            if (nbttagcompound.hasKey("Locked")) {
                scoreboardscore.setLocked(nbttagcompound.getBoolean("Locked"));
            }
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        if (this.scoreboard == null) {
            ScoreboardSaveData.LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
            return nbttagcompound;
        } else {
            nbttagcompound.setTag("Objectives", this.objectivesToNbt());
            nbttagcompound.setTag("PlayerScores", this.scoresToNbt());
            nbttagcompound.setTag("Teams", this.teamsToNbt());
            this.fillInDisplaySlots(nbttagcompound);
            return nbttagcompound;
        }
    }

    protected NBTTagList teamsToNbt() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection collection = this.scoreboard.getTeams();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScorePlayerTeam scoreboardteam = (ScorePlayerTeam) iterator.next();
            if (!com.destroystokyo.paper.PaperConfig.saveEmptyScoreboardTeams && scoreboardteam.getMembershipCollection().isEmpty()) continue; // Paper
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            nbttagcompound.setString("Name", scoreboardteam.getName());
            nbttagcompound.setString("DisplayName", scoreboardteam.getDisplayName());
            if (scoreboardteam.getColor().getColorIndex() >= 0) {
                nbttagcompound.setString("TeamColor", scoreboardteam.getColor().getFriendlyName());
            }

            nbttagcompound.setString("Prefix", scoreboardteam.getPrefix());
            nbttagcompound.setString("Suffix", scoreboardteam.getSuffix());
            nbttagcompound.setBoolean("AllowFriendlyFire", scoreboardteam.getAllowFriendlyFire());
            nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreboardteam.getSeeFriendlyInvisiblesEnabled());
            nbttagcompound.setString("NameTagVisibility", scoreboardteam.getNameTagVisibility().internalName);
            nbttagcompound.setString("DeathMessageVisibility", scoreboardteam.getDeathMessageVisibility().internalName);
            nbttagcompound.setString("CollisionRule", scoreboardteam.getCollisionRule().name);
            NBTTagList nbttaglist1 = new NBTTagList();
            Iterator iterator1 = scoreboardteam.getMembershipCollection().iterator();

            while (iterator1.hasNext()) {
                String s = (String) iterator1.next();

                nbttaglist1.appendTag(new NBTTagString(s));
            }

            nbttagcompound.setTag("Players", nbttaglist1);
            nbttaglist.appendTag(nbttagcompound);
        }

        return nbttaglist;
    }

    protected void fillInDisplaySlots(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        boolean flag = false;

        for (int i = 0; i < 19; ++i) {
            ScoreObjective scoreboardobjective = this.scoreboard.getObjectiveInDisplaySlot(i);

            if (scoreboardobjective != null) {
                nbttagcompound1.setString("slot_" + i, scoreboardobjective.getName());
                flag = true;
            }
        }

        if (flag) {
            nbttagcompound.setTag("DisplaySlots", nbttagcompound1);
        }

    }

    protected NBTTagList objectivesToNbt() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection collection = this.scoreboard.getScoreObjectives();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

            if (scoreboardobjective.getCriteria() != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setString("Name", scoreboardobjective.getName());
                nbttagcompound.setString("CriteriaName", scoreboardobjective.getCriteria().getName());
                nbttagcompound.setString("DisplayName", scoreboardobjective.getDisplayName());
                nbttagcompound.setString("RenderType", scoreboardobjective.getRenderType().getRenderType());
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    protected NBTTagList scoresToNbt() {
        NBTTagList nbttaglist = new NBTTagList();
        Collection collection = this.scoreboard.getScores();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Score scoreboardscore = (Score) iterator.next();

            if (scoreboardscore.getObjective() != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setString("Name", scoreboardscore.getPlayerName());
                nbttagcompound.setString("Objective", scoreboardscore.getObjective().getName());
                nbttagcompound.setInteger("Score", scoreboardscore.getScorePoints());
                nbttagcompound.setBoolean("Locked", scoreboardscore.isLocked());
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }
}

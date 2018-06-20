package org.bukkit.craftbukkit.scoreboard;

import java.util.Map;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Score;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;

/**
 * TL;DR: This class is special and lazily grabs a handle...
 * ...because a handle is a full fledged (I think permanent) hashMap for the associated name.
 * <p>
 * Also, as an added perk, a CraftScore will (intentionally) stay a valid reference so long as objective is valid.
 */
final class CraftScore implements org.bukkit.scoreboard.Score {
    private final String entry;
    private final CraftObjective objective;

    CraftScore(CraftObjective objective, String entry) {
        this.objective = objective;
        this.entry = entry;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(entry);
    }

    @Override
    public String getEntry() {
        return entry;
    }

    @Override
    public Objective getObjective() {
        return objective;
    }

    @Override
    public int getScore() throws IllegalStateException {
        Scoreboard board = objective.checkState().board;

        if (board.func_96526_d().contains(entry)) { // Lazy
            Map<ScoreObjective, Score> scores = board.func_96510_d(entry);
            Score score = scores.get(objective.getHandle());
            if (score != null) { // Lazy
                return score.func_96652_c();
            }
        }

        return 0; // Lazy
    }

    @Override
    public void setScore(int score) throws IllegalStateException {
        objective.checkState().board.func_96529_a(entry, objective.getHandle()).func_96647_c(score);
    }

    @Override
    public boolean isScoreSet() throws IllegalStateException {
        Scoreboard board = objective.checkState().board;

        return board.func_96526_d().contains(entry) && board.func_96510_d(entry).containsKey(objective.getHandle());
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return objective.getScoreboard();
    }
}

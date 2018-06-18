package net.minecraft.scoreboard;

import java.util.Comparator;

public class Score {

    public static final Comparator<Score> SCORE_COMPARATOR = new Comparator() {
        public int a(Score scoreboardscore, Score scoreboardscore1) {
            return scoreboardscore.getScorePoints() > scoreboardscore1.getScorePoints() ? 1 : (scoreboardscore.getScorePoints() < scoreboardscore1.getScorePoints() ? -1 : scoreboardscore1.getPlayerName().compareToIgnoreCase(scoreboardscore.getPlayerName()));
        }

        public int compare(Object object, Object object1) {
            return this.a((Score) object, (Score) object1);
        }
    };
    private final Scoreboard scoreboard;
    private final ScoreObjective objective;
    private final String scorePlayerName;
    private int scorePoints;
    private boolean locked;
    private boolean forceUpdate;

    public Score(Scoreboard scoreboard, ScoreObjective scoreboardobjective, String s) {
        this.scoreboard = scoreboard;
        this.objective = scoreboardobjective;
        this.scorePlayerName = s;
        this.forceUpdate = true;
    }

    public void increaseScore(int i) {
        if (this.objective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            this.setScorePoints(this.getScorePoints() + i);
        }
    }

    public void decreaseScore(int i) {
        if (this.objective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            this.setScorePoints(this.getScorePoints() - i);
        }
    }

    public void incrementScore() {
        if (this.objective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            this.increaseScore(1);
        }
    }

    public int getScorePoints() {
        return this.scorePoints;
    }

    public void setScorePoints(int i) {
        int j = this.scorePoints;

        this.scorePoints = i;
        if (j != i || this.forceUpdate) {
            this.forceUpdate = false;
            this.getScoreScoreboard().onScoreUpdated(this);
        }

    }

    public ScoreObjective getObjective() {
        return this.objective;
    }

    public String getPlayerName() {
        return this.scorePlayerName;
    }

    public Scoreboard getScoreScoreboard() {
        return this.scoreboard;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean flag) {
        this.locked = flag;
    }
}

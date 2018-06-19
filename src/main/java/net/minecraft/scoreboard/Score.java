package net.minecraft.scoreboard;

import java.util.Comparator;

public class Score {

    public static final Comparator<Score> field_96658_a = new Comparator() {
        public int a(Score scoreboardscore, Score scoreboardscore1) {
            return scoreboardscore.func_96652_c() > scoreboardscore1.func_96652_c() ? 1 : (scoreboardscore.func_96652_c() < scoreboardscore1.func_96652_c() ? -1 : scoreboardscore1.func_96653_e().compareToIgnoreCase(scoreboardscore.func_96653_e()));
        }

        public int compare(Object object, Object object1) {
            return this.a((Score) object, (Score) object1);
        }
    };
    private final Scoreboard field_96656_b;
    private final ScoreObjective field_96657_c;
    private final String field_96654_d;
    private int field_96655_e;
    private boolean field_178817_f;
    private boolean field_178818_g;

    public Score(Scoreboard scoreboard, ScoreObjective scoreboardobjective, String s) {
        this.field_96656_b = scoreboard;
        this.field_96657_c = scoreboardobjective;
        this.field_96654_d = s;
        this.field_178818_g = true;
    }

    public void func_96649_a(int i) {
        if (this.field_96657_c.func_96680_c().func_96637_b()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            this.func_96647_c(this.func_96652_c() + i);
        }
    }

    public void func_96646_b(int i) {
        if (this.field_96657_c.func_96680_c().func_96637_b()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            this.func_96647_c(this.func_96652_c() - i);
        }
    }

    public void func_96648_a() {
        if (this.field_96657_c.func_96680_c().func_96637_b()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            this.func_96649_a(1);
        }
    }

    public int func_96652_c() {
        return this.field_96655_e;
    }

    public void func_96647_c(int i) {
        int j = this.field_96655_e;

        this.field_96655_e = i;
        if (j != i || this.field_178818_g) {
            this.field_178818_g = false;
            this.func_96650_f().func_96536_a(this);
        }

    }

    public ScoreObjective func_96645_d() {
        return this.field_96657_c;
    }

    public String func_96653_e() {
        return this.field_96654_d;
    }

    public Scoreboard func_96650_f() {
        return this.field_96656_b;
    }

    public boolean func_178816_g() {
        return this.field_178817_f;
    }

    public void func_178815_a(boolean flag) {
        this.field_178817_f = flag;
    }
}

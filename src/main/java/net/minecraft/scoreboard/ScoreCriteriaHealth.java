package net.minecraft.scoreboard;

public class ScoreCriteriaHealth extends ScoreCriteria {

    public ScoreCriteriaHealth(String s) {
        super(s);
    }

    public boolean func_96637_b() {
        return true;
    }

    public IScoreCriteria.EnumRenderType func_178790_c() {
        return IScoreCriteria.EnumRenderType.HEARTS;
    }
}

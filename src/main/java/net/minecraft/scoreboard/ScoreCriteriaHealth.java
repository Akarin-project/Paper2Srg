package net.minecraft.scoreboard;

public class ScoreCriteriaHealth extends ScoreCriteria {

    public ScoreCriteriaHealth(String s) {
        super(s);
    }

    public boolean isReadOnly() {
        return true;
    }

    public IScoreCriteria.EnumRenderType getRenderType() {
        return IScoreCriteria.EnumRenderType.HEARTS;
    }
}

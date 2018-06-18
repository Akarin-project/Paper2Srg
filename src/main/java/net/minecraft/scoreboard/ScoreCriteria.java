package net.minecraft.scoreboard;

public class ScoreCriteria implements IScoreCriteria {

    private final String dummyName;

    public ScoreCriteria(String s) {
        this.dummyName = s;
        IScoreCriteria.INSTANCES.put(s, this);
    }

    public String getName() {
        return this.dummyName;
    }

    public boolean isReadOnly() {
        return false;
    }

    public IScoreCriteria.EnumRenderType getRenderType() {
        return IScoreCriteria.EnumRenderType.INTEGER;
    }
}

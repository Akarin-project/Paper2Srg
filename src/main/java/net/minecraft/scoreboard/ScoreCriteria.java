package net.minecraft.scoreboard;

public class ScoreCriteria implements IScoreCriteria {

    private final String field_96644_g;

    public ScoreCriteria(String s) {
        this.field_96644_g = s;
        IScoreCriteria.field_96643_a.put(s, this);
    }

    public String func_96636_a() {
        return this.field_96644_g;
    }

    public boolean func_96637_b() {
        return false;
    }

    public IScoreCriteria.EnumRenderType func_178790_c() {
        return IScoreCriteria.EnumRenderType.INTEGER;
    }
}

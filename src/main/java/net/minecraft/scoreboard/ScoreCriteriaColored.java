package net.minecraft.scoreboard;
import net.minecraft.util.text.TextFormatting;


public class ScoreCriteriaColored implements IScoreCriteria {

    private final String field_178794_j;

    public ScoreCriteriaColored(String s, TextFormatting enumchatformat) {
        this.field_178794_j = s + enumchatformat.func_96297_d();
        IScoreCriteria.field_96643_a.put(this.field_178794_j, this);
    }

    public String func_96636_a() {
        return this.field_178794_j;
    }

    public boolean func_96637_b() {
        return false;
    }

    public IScoreCriteria.EnumRenderType func_178790_c() {
        return IScoreCriteria.EnumRenderType.INTEGER;
    }
}

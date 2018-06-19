package net.minecraft.scoreboard;

public class ScoreObjective {

    private final Scoreboard field_96686_a;
    private final String field_96684_b;
    private final IScoreCriteria field_96685_c;
    private IScoreCriteria.EnumRenderType field_178768_d;
    private String field_96683_d;

    public ScoreObjective(Scoreboard scoreboard, String s, IScoreCriteria iscoreboardcriteria) {
        this.field_96686_a = scoreboard;
        this.field_96684_b = s;
        this.field_96685_c = iscoreboardcriteria;
        this.field_96683_d = s;
        this.field_178768_d = iscoreboardcriteria.func_178790_c();
    }

    public String func_96679_b() {
        return this.field_96684_b;
    }

    public IScoreCriteria func_96680_c() {
        return this.field_96685_c;
    }

    public String func_96678_d() {
        return this.field_96683_d;
    }

    public void func_96681_a(String s) {
        this.field_96683_d = s;
        this.field_96686_a.func_96532_b(this);
    }

    public IScoreCriteria.EnumRenderType func_178766_e() {
        return this.field_178768_d;
    }

    public void func_178767_a(IScoreCriteria.EnumRenderType iscoreboardcriteria_enumscoreboardhealthdisplay) {
        this.field_178768_d = iscoreboardcriteria_enumscoreboardhealthdisplay;
        this.field_96686_a.func_96532_b(this);
    }
}

package net.minecraft.scoreboard;

public class ScoreObjective {

    private final Scoreboard scoreboard;
    private final String name;
    private final IScoreCriteria objectiveCriteria;
    private IScoreCriteria.EnumRenderType renderType;
    private String displayName;

    public ScoreObjective(Scoreboard scoreboard, String s, IScoreCriteria iscoreboardcriteria) {
        this.scoreboard = scoreboard;
        this.name = s;
        this.objectiveCriteria = iscoreboardcriteria;
        this.displayName = s;
        this.renderType = iscoreboardcriteria.getRenderType();
    }

    public String getName() {
        return this.name;
    }

    public IScoreCriteria getCriteria() {
        return this.objectiveCriteria;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String s) {
        this.displayName = s;
        this.scoreboard.onObjectiveDisplayNameChanged(this);
    }

    public IScoreCriteria.EnumRenderType getRenderType() {
        return this.renderType;
    }

    public void setRenderType(IScoreCriteria.EnumRenderType iscoreboardcriteria_enumscoreboardhealthdisplay) {
        this.renderType = iscoreboardcriteria_enumscoreboardhealthdisplay;
        this.scoreboard.onObjectiveDisplayNameChanged(this);
    }
}

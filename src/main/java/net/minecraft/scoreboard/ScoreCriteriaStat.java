package net.minecraft.scoreboard;
import net.minecraft.stats.StatBase;


public class ScoreCriteriaStat extends ScoreCriteria {

    private final StatBase field_151459_g;

    public ScoreCriteriaStat(StatBase statistic) {
        super(statistic.field_75975_e);
        this.field_151459_g = statistic;
    }
}

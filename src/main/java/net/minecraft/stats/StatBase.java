package net.minecraft.stats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreCriteriaStat;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class StatBase {

    public final String statId;
    private final ITextComponent statName;
    public boolean isIndependent;
    private final IStatType formatter;
    private final IScoreCriteria objectiveCriteria;
    private Class<? extends IJsonSerializable> serializableClazz;
    private static final NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
    public static IStatType simpleStatType = new IStatType() {
    };
    private static final DecimalFormat decimalFormat = new DecimalFormat("########0.00");
    public static IStatType timeStatType = new IStatType() {
    };
    public static IStatType distanceStatType = new IStatType() {
    };
    public static IStatType divideByTen = new IStatType() {
    };

    public StatBase(String s, ITextComponent ichatbasecomponent, IStatType counter) {
        this.statId = s;
        this.statName = ichatbasecomponent;
        this.formatter = counter;
        this.objectiveCriteria = new ScoreCriteriaStat(this);
        IScoreCriteria.INSTANCES.put(this.objectiveCriteria.getName(), this.objectiveCriteria);
    }

    public StatBase(String s, ITextComponent ichatbasecomponent) {
        this(s, ichatbasecomponent, StatBase.simpleStatType);
    }

    public StatBase initIndependentStat() {
        this.isIndependent = true;
        return this;
    }

    public StatBase registerStat() {
        if (StatList.ID_TO_STAT_MAP.containsKey(this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + ((StatBase) StatList.ID_TO_STAT_MAP.get(this.statId)).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        } else {
            StatList.ALL_STATS.add(this);
            StatList.ID_TO_STAT_MAP.put(this.statId, this);
            return this;
        }
    }

    public ITextComponent getStatName() {
        ITextComponent ichatbasecomponent = this.statName.createCopy();

        ichatbasecomponent.getStyle().setColor(TextFormatting.GRAY);
        return ichatbasecomponent;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            StatBase statistic = (StatBase) object;

            return this.statId.equals(statistic.statId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.statId.hashCode();
    }

    public String toString() {
        return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.formatter + ", objectiveCriteria=" + this.objectiveCriteria + '}';
    }

    public IScoreCriteria getCriteria() {
        return this.objectiveCriteria;
    }

    public Class<? extends IJsonSerializable> getSerializableClazz() {
        return this.serializableClazz;
    }
}

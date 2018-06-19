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

    public final String field_75975_e;
    private final ITextComponent field_75978_a;
    public boolean field_75972_f;
    private final IStatType field_75976_b;
    private final IScoreCriteria field_150957_c;
    private Class<? extends IJsonSerializable> field_150956_d;
    private static final NumberFormat field_75977_c = NumberFormat.getIntegerInstance(Locale.US);
    public static IStatType field_75980_h = new IStatType() {
    };
    private static final DecimalFormat field_75974_d = new DecimalFormat("########0.00");
    public static IStatType field_75981_i = new IStatType() {
    };
    public static IStatType field_75979_j = new IStatType() {
    };
    public static IStatType field_111202_k = new IStatType() {
    };

    public StatBase(String s, ITextComponent ichatbasecomponent, IStatType counter) {
        this.field_75975_e = s;
        this.field_75978_a = ichatbasecomponent;
        this.field_75976_b = counter;
        this.field_150957_c = new ScoreCriteriaStat(this);
        IScoreCriteria.field_96643_a.put(this.field_150957_c.func_96636_a(), this.field_150957_c);
    }

    public StatBase(String s, ITextComponent ichatbasecomponent) {
        this(s, ichatbasecomponent, StatBase.field_75980_h);
    }

    public StatBase func_75966_h() {
        this.field_75972_f = true;
        return this;
    }

    public StatBase func_75971_g() {
        if (StatList.field_188093_a.containsKey(this.field_75975_e)) {
            throw new RuntimeException("Duplicate stat id: \"" + ((StatBase) StatList.field_188093_a.get(this.field_75975_e)).field_75978_a + "\" and \"" + this.field_75978_a + "\" at id " + this.field_75975_e);
        } else {
            StatList.field_75940_b.add(this);
            StatList.field_188093_a.put(this.field_75975_e, this);
            return this;
        }
    }

    public ITextComponent func_150951_e() {
        ITextComponent ichatbasecomponent = this.field_75978_a.func_150259_f();

        ichatbasecomponent.func_150256_b().func_150238_a(TextFormatting.GRAY);
        return ichatbasecomponent;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            StatBase statistic = (StatBase) object;

            return this.field_75975_e.equals(statistic.field_75975_e);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.field_75975_e.hashCode();
    }

    public String toString() {
        return "Stat{id=" + this.field_75975_e + ", nameId=" + this.field_75978_a + ", awardLocallyOnly=" + this.field_75972_f + ", formatter=" + this.field_75976_b + ", objectiveCriteria=" + this.field_150957_c + '}';
    }

    public IScoreCriteria func_150952_k() {
        return this.field_150957_c;
    }

    public Class<? extends IJsonSerializable> func_150954_l() {
        return this.field_150956_d;
    }
}

package net.minecraft.stats;
import net.minecraft.util.text.ITextComponent;


public class StatBasic extends StatBase {

    public StatBasic(String s, ITextComponent ichatbasecomponent, IStatType counter) {
        super(s, ichatbasecomponent, counter);
    }

    public StatBasic(String s, ITextComponent ichatbasecomponent) {
        super(s, ichatbasecomponent);
    }

    public StatBase registerStat() {
        super.registerStat();
        StatList.BASIC_STATS.add(this);
        return this;
    }
}

package net.minecraft.stats;
import net.minecraft.util.text.ITextComponent;


public class StatBasic extends StatBase {

    public StatBasic(String s, ITextComponent ichatbasecomponent, IStatType counter) {
        super(s, ichatbasecomponent, counter);
    }

    public StatBasic(String s, ITextComponent ichatbasecomponent) {
        super(s, ichatbasecomponent);
    }

    public StatBase func_75971_g() {
        super.func_75971_g();
        StatList.field_188094_c.add(this);
        return this;
    }
}

package net.minecraft.util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;


public enum EnumHandSide {

    LEFT(new TextComponentTranslation("options.mainHand.left", new Object[0])), RIGHT(new TextComponentTranslation("options.mainHand.right", new Object[0]));

    private final ITextComponent field_188471_c;

    private EnumHandSide(ITextComponent ichatbasecomponent) {
        this.field_188471_c = ichatbasecomponent;
    }

    public String toString() {
        return this.field_188471_c.func_150260_c();
    }
}

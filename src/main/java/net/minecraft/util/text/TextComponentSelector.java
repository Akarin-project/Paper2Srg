package net.minecraft.util.text;

import java.util.Iterator;

public class TextComponentSelector extends TextComponentBase {

    private final String field_179993_b;

    public TextComponentSelector(String s) {
        this.field_179993_b = s;
    }

    public String func_179992_g() {
        return this.field_179993_b;
    }

    public String func_150261_e() {
        return this.field_179993_b;
    }

    public TextComponentSelector func_150259_f() {
        TextComponentSelector chatcomponentselector = new TextComponentSelector(this.field_179993_b);

        chatcomponentselector.func_150255_a(this.func_150256_b().func_150232_l());
        Iterator iterator = this.func_150253_a().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatcomponentselector.func_150257_a(ichatbasecomponent.func_150259_f());
        }

        return chatcomponentselector;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentSelector)) {
            return false;
        } else {
            TextComponentSelector chatcomponentselector = (TextComponentSelector) object;

            return this.field_179993_b.equals(chatcomponentselector.field_179993_b) && super.equals(object);
        }
    }

    public String toString() {
        return "SelectorComponent{pattern=\'" + this.field_179993_b + '\'' + ", siblings=" + this.field_150264_a + ", style=" + this.func_150256_b() + '}';
    }

    public ITextComponent func_150259_f() {
        return this.func_150259_f();
    }
}

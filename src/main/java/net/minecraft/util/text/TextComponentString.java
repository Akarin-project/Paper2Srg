package net.minecraft.util.text;

import java.util.Iterator;

public class TextComponentString extends TextComponentBase {

    private final String field_150267_b;

    public TextComponentString(String s) {
        this.field_150267_b = s;
    }

    public String func_150265_g() {
        return this.field_150267_b;
    }

    @Override
    public String func_150261_e() {
        return this.field_150267_b;
    }

    @Override
    public TextComponentString func_150259_f() {
        TextComponentString chatcomponenttext = new TextComponentString(this.field_150267_b);

        chatcomponenttext.func_150255_a(this.func_150256_b().func_150232_l());
        Iterator iterator = this.func_150253_a().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatcomponenttext.func_150257_a(ichatbasecomponent.func_150259_f());
        }

        return chatcomponenttext;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentString)) {
            return false;
        } else {
            TextComponentString chatcomponenttext = (TextComponentString) object;

            return this.field_150267_b.equals(chatcomponenttext.func_150265_g()) && super.equals(object);
        }
    }

    @Override
    public String toString() {
        return "TextComponent{text=\'" + this.field_150267_b + '\'' + ", siblings=" + this.field_150264_a + ", style=" + this.func_150256_b() + '}';
    }
}

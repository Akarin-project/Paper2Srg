package net.minecraft.util.text;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public class TextComponentKeybind extends TextComponentBase {

    public static Function<String, Supplier<String>> field_193637_b = (var0) -> {
        return () -> {
            return var0;
        };
    };
    private final String field_193638_c;
    private Supplier<String> field_193639_d;

    public TextComponentKeybind(String s) {
        this.field_193638_c = s;
    }

    @Override
    public String func_150261_e() {
        if (this.field_193639_d == null) {
            this.field_193639_d = TextComponentKeybind.field_193637_b.apply(this.field_193638_c);
        }

        return this.field_193639_d.get();
    }

    @Override
    public TextComponentKeybind func_150259_f() {
        TextComponentKeybind chatcomponentkeybind = new TextComponentKeybind(this.field_193638_c);

        chatcomponentkeybind.func_150255_a(this.func_150256_b().func_150232_l());
        Iterator iterator = this.func_150253_a().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatcomponentkeybind.func_150257_a(ichatbasecomponent.func_150259_f());
        }

        return chatcomponentkeybind;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentKeybind)) {
            return false;
        } else {
            TextComponentKeybind chatcomponentkeybind = (TextComponentKeybind) object;

            return this.field_193638_c.equals(chatcomponentkeybind.field_193638_c) && super.equals(object);
        }
    }

    @Override
    public String toString() {
        return "KeybindComponent{keybind=\'" + this.field_193638_c + '\'' + ", siblings=" + this.field_150264_a + ", style=" + this.func_150256_b() + '}';
    }

    public String func_193633_h() {
        return this.field_193638_c;
    }
}

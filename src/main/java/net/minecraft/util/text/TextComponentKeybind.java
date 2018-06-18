package net.minecraft.util.text;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public class TextComponentKeybind extends TextComponentBase {

    public static Function<String, Supplier<String>> displaySupplierFunction = (var0) -> {
        return () -> {
            return s;
        };
    };
    private final String keybind;
    private Supplier<String> displaySupplier;

    public TextComponentKeybind(String s) {
        this.keybind = s;
    }

    @Override
    public String getUnformattedComponentText() {
        if (this.displaySupplier == null) {
            this.displaySupplier = TextComponentKeybind.displaySupplierFunction.apply(this.keybind);
        }

        return this.displaySupplier.get();
    }

    @Override
    public TextComponentKeybind createCopy() {
        TextComponentKeybind chatcomponentkeybind = new TextComponentKeybind(this.keybind);

        chatcomponentkeybind.setStyle(this.getStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatcomponentkeybind.appendSibling(ichatbasecomponent.createCopy());
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

            return this.keybind.equals(chatcomponentkeybind.keybind) && super.equals(object);
        }
    }

    @Override
    public String toString() {
        return "KeybindComponent{keybind=\'" + this.keybind + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

    public String getKeybind() {
        return this.keybind;
    }
}

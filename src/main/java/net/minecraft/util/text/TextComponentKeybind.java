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

    public String getUnformattedComponentText() {
        if (this.displaySupplier == null) {
            this.displaySupplier = (Supplier) TextComponentKeybind.displaySupplierFunction.apply(this.keybind);
        }

        return (String) this.displaySupplier.get();
    }

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

    public String toString() {
        return "KeybindComponent{keybind=\'" + this.keybind + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

    public String getKeybind() {
        return this.keybind;
    }

    public ITextComponent createCopy() {
        return this.createCopy();
    }
}

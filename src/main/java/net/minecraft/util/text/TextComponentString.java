package net.minecraft.util.text;

import java.util.Iterator;

public class TextComponentString extends TextComponentBase {

    private final String text;

    public TextComponentString(String s) {
        this.text = s;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String getUnformattedComponentText() {
        return this.text;
    }

    @Override
    public TextComponentString createCopy() {
        TextComponentString chatcomponenttext = new TextComponentString(this.text);

        chatcomponenttext.setStyle(this.getStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatcomponenttext.appendSibling(ichatbasecomponent.createCopy());
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

            return this.text.equals(chatcomponenttext.getText()) && super.equals(object);
        }
    }

    @Override
    public String toString() {
        return "TextComponent{text=\'" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}

package net.minecraft.util.text;

public class TextComponentTranslationFormatException extends IllegalArgumentException {

    public TextComponentTranslationFormatException(TextComponentTranslation chatmessage, String s) {
        super(String.format("Error parsing: %s: %s", new Object[] { chatmessage, s}));
    }

    public TextComponentTranslationFormatException(TextComponentTranslation chatmessage, int i) {
        super(String.format("Invalid index %d requested for %s", new Object[] { Integer.valueOf(i), chatmessage}));
    }

    public TextComponentTranslationFormatException(TextComponentTranslation chatmessage, Throwable throwable) {
        super(String.format("Error while parsing: %s", new Object[] { chatmessage}), throwable);
    }
}

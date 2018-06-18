package net.minecraft.util.text;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public enum TextFormatting {

    BLACK("BLACK", '0', 0), DARK_BLUE("DARK_BLUE", '1', 1), DARK_GREEN("DARK_GREEN", '2', 2), DARK_AQUA("DARK_AQUA", '3', 3), DARK_RED("DARK_RED", '4', 4), DARK_PURPLE("DARK_PURPLE", '5', 5), GOLD("GOLD", '6', 6), GRAY("GRAY", '7', 7), DARK_GRAY("DARK_GRAY", '8', 8), BLUE("BLUE", '9', 9), GREEN("GREEN", 'a', 10), AQUA("AQUA", 'b', 11), RED("RED", 'c', 12), LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13), YELLOW("YELLOW", 'e', 14), WHITE("WHITE", 'f', 15), OBFUSCATED("OBFUSCATED", 'k', true), BOLD("BOLD", 'l', true), STRIKETHROUGH("STRIKETHROUGH", 'm', true), UNDERLINE("UNDERLINE", 'n', true), ITALIC("ITALIC", 'o', true), RESET("RESET", 'r', -1);

    private static final Map<String, TextFormatting> NAME_MAPPING = Maps.newHashMap();
    private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
    private final String name;
    public final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private final int colorIndex;

    private static String lowercaseAlpha(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    private TextFormatting(String s, char c0, int i) {
        this(s, c0, false, i);
    }

    private TextFormatting(String s, char c0, boolean flag) {
        this(s, c0, flag, -1);
    }

    private TextFormatting(String s, char c0, boolean flag, int i) {
        this.name = s;
        this.formattingCode = c0;
        this.fancyStyling = flag;
        this.colorIndex = i;
        this.controlString = "\u00a7" + c0;
    }

    public int getColorIndex() {
        return this.colorIndex;
    }

    public boolean isFancyStyling() {
        return this.fancyStyling;
    }

    public boolean isColor() {
        return !this.fancyStyling && this != TextFormatting.RESET;
    }

    public String getFriendlyName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.controlString;
    }

    @Nullable
    public static String getTextWithoutFormattingCodes(@Nullable String s) {
        return s == null ? null : TextFormatting.FORMATTING_CODE_PATTERN.matcher(s).replaceAll("");
    }

    @Nullable
    public static TextFormatting getValueByName(@Nullable String s) {
        return s == null ? null : (TextFormatting) TextFormatting.NAME_MAPPING.get(lowercaseAlpha(s));
    }

    @Nullable
    public static TextFormatting fromColorIndex(int i) {
        if (i < 0) {
            return TextFormatting.RESET;
        } else {
            TextFormatting[] aenumchatformat = values();
            int j = aenumchatformat.length;

            for (int k = 0; k < j; ++k) {
                TextFormatting enumchatformat = aenumchatformat[k];

                if (enumchatformat.getColorIndex() == i) {
                    return enumchatformat;
                }
            }

            return null;
        }
    }

    public static Collection<String> getValidValues(boolean flag, boolean flag1) {
        ArrayList arraylist = Lists.newArrayList();
        TextFormatting[] aenumchatformat = values();
        int i = aenumchatformat.length;

        for (int j = 0; j < i; ++j) {
            TextFormatting enumchatformat = aenumchatformat[j];

            if ((!enumchatformat.isColor() || flag) && (!enumchatformat.isFancyStyling() || flag1)) {
                arraylist.add(enumchatformat.getFriendlyName());
            }
        }

        return arraylist;
    }

    static {
        TextFormatting[] aenumchatformat = values();
        int i = aenumchatformat.length;

        for (int j = 0; j < i; ++j) {
            TextFormatting enumchatformat = aenumchatformat[j];

            TextFormatting.NAME_MAPPING.put(lowercaseAlpha(enumchatformat.name), enumchatformat);
        }

    }
}

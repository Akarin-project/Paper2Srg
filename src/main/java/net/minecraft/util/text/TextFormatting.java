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

    private static final Map<String, TextFormatting> field_96331_x = Maps.newHashMap();
    private static final Pattern field_96330_y = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
    private final String field_175748_y;
    public final char field_96329_z;
    private final boolean field_96303_A;
    private final String field_96304_B;
    private final int field_175747_C;

    private static String func_175745_c(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    private TextFormatting(String s, char c0, int i) {
        this(s, c0, false, i);
    }

    private TextFormatting(String s, char c0, boolean flag) {
        this(s, c0, flag, -1);
    }

    private TextFormatting(String s, char c0, boolean flag, int i) {
        this.field_175748_y = s;
        this.field_96329_z = c0;
        this.field_96303_A = flag;
        this.field_175747_C = i;
        this.field_96304_B = "\u00a7" + c0;
    }

    public int func_175746_b() {
        return this.field_175747_C;
    }

    public boolean func_96301_b() {
        return this.field_96303_A;
    }

    public boolean func_96302_c() {
        return !this.field_96303_A && this != TextFormatting.RESET;
    }

    public String func_96297_d() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.field_96304_B;
    }

    @Nullable
    public static String func_110646_a(@Nullable String s) {
        return s == null ? null : TextFormatting.field_96330_y.matcher(s).replaceAll("");
    }

    @Nullable
    public static TextFormatting func_96300_b(@Nullable String s) {
        return s == null ? null : (TextFormatting) TextFormatting.field_96331_x.get(func_175745_c(s));
    }

    @Nullable
    public static TextFormatting func_175744_a(int i) {
        if (i < 0) {
            return TextFormatting.RESET;
        } else {
            TextFormatting[] aenumchatformat = values();
            int j = aenumchatformat.length;

            for (int k = 0; k < j; ++k) {
                TextFormatting enumchatformat = aenumchatformat[k];

                if (enumchatformat.func_175746_b() == i) {
                    return enumchatformat;
                }
            }

            return null;
        }
    }

    public static Collection<String> func_96296_a(boolean flag, boolean flag1) {
        ArrayList arraylist = Lists.newArrayList();
        TextFormatting[] aenumchatformat = values();
        int i = aenumchatformat.length;

        for (int j = 0; j < i; ++j) {
            TextFormatting enumchatformat = aenumchatformat[j];

            if ((!enumchatformat.func_96302_c() || flag) && (!enumchatformat.func_96301_b() || flag1)) {
                arraylist.add(enumchatformat.func_96297_d());
            }
        }

        return arraylist;
    }

    static {
        TextFormatting[] aenumchatformat = values();
        int i = aenumchatformat.length;

        for (int j = 0; j < i; ++j) {
            TextFormatting enumchatformat = aenumchatformat[j];

            TextFormatting.field_96331_x.put(func_175745_c(enumchatformat.field_175748_y), enumchatformat);
        }

    }
}

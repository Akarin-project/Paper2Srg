package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

public class ChatAllowedCharacters {

    public static final Level NETTY_LEAK_DETECTION = Level.DISABLED;
    public static final char[] ILLEGAL_STRUCTURE_CHARACTERS = new char[] { '.', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"'};
    public static final char[] ILLEGAL_FILE_CHARACTERS = new char[] { '/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static boolean isAllowedCharacter(char c0) {
        return c0 != 167 && c0 >= 32 && c0 != 127;
    }

    public static String filterAllowedCharacters(String s) {
        StringBuilder stringbuilder = new StringBuilder();
        char[] achar = s.toCharArray();
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c0 = achar[j];

            if (isAllowedCharacter(c0)) {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

    static {
        ResourceLeakDetector.setLevel(ChatAllowedCharacters.NETTY_LEAK_DETECTION);
    }
}

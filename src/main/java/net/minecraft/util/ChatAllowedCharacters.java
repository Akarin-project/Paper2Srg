package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

public class ChatAllowedCharacters {

    public static final Level field_184877_a = Level.DISABLED;
    public static final char[] field_189861_b = new char[] { '.', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"'};
    public static final char[] field_71567_b = new char[] { '/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static boolean func_71566_a(char c0) {
        return c0 != 167 && c0 >= 32 && c0 != 127;
    }

    public static String func_71565_a(String s) {
        StringBuilder stringbuilder = new StringBuilder();
        char[] achar = s.toCharArray();
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c0 = achar[j];

            if (func_71566_a(c0)) {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

    static {
        ResourceLeakDetector.setLevel(ChatAllowedCharacters.field_184877_a);
    }
}

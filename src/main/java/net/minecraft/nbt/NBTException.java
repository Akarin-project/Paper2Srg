package net.minecraft.nbt;

public class NBTException extends Exception {

    public NBTException(String s, String s1, int i) {
        super(s + " at: " + func_193592_a(s1, i));
    }

    private static String func_193592_a(String s, int i) {
        StringBuilder stringbuilder = new StringBuilder();
        int j = Math.min(s.length(), i);

        if (j > 35) {
            stringbuilder.append("...");
        }

        stringbuilder.append(s.substring(Math.max(0, j - 35), j));
        stringbuilder.append("<--[HERE]");
        return stringbuilder.toString();
    }
}

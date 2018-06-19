package net.minecraft.item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;


public enum EnumDyeColor implements IStringSerializable {

    WHITE(0, 15, "white", "white", 16383998, TextFormatting.WHITE), ORANGE(1, 14, "orange", "orange", 16351261, TextFormatting.GOLD), MAGENTA(2, 13, "magenta", "magenta", 13061821, TextFormatting.AQUA), LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, TextFormatting.BLUE), YELLOW(4, 11, "yellow", "yellow", 16701501, TextFormatting.YELLOW), LIME(5, 10, "lime", "lime", 8439583, TextFormatting.GREEN), PINK(6, 9, "pink", "pink", 15961002, TextFormatting.LIGHT_PURPLE), GRAY(7, 8, "gray", "gray", 4673362, TextFormatting.DARK_GRAY), SILVER(8, 7, "silver", "silver", 10329495, TextFormatting.GRAY), CYAN(9, 6, "cyan", "cyan", 1481884, TextFormatting.DARK_AQUA), PURPLE(10, 5, "purple", "purple", 8991416, TextFormatting.DARK_PURPLE), BLUE(11, 4, "blue", "blue", 3949738, TextFormatting.DARK_BLUE), BROWN(12, 3, "brown", "brown", 8606770, TextFormatting.GOLD), GREEN(13, 2, "green", "green", 6192150, TextFormatting.DARK_GREEN), RED(14, 1, "red", "red", 11546150, TextFormatting.DARK_RED), BLACK(15, 0, "black", "black", 1908001, TextFormatting.BLACK);

    private static final EnumDyeColor[] field_176790_q = new EnumDyeColor[values().length];
    private static final EnumDyeColor[] field_176789_r = new EnumDyeColor[values().length];
    private final int field_176788_s;
    private final int field_176787_t;
    private final String field_176786_u;
    private final String field_176785_v;
    private final int field_193351_w;
    private final float[] field_193352_x;
    private final TextFormatting field_176793_x;

    private EnumDyeColor(int i, int j, String s, String s1, int k, TextFormatting enumchatformat) {
        this.field_176788_s = i;
        this.field_176787_t = j;
        this.field_176786_u = s;
        this.field_176785_v = s1;
        this.field_193351_w = k;
        this.field_176793_x = enumchatformat;
        int l = (k & 16711680) >> 16;
        int i1 = (k & '\uff00') >> 8;
        int j1 = (k & 255) >> 0;

        this.field_193352_x = new float[] { (float) l / 255.0F, (float) i1 / 255.0F, (float) j1 / 255.0F};
    }

    public int func_176765_a() {
        return this.field_176788_s;
    }

    public int func_176767_b() {
        return this.field_176787_t;
    }

    public String func_176762_d() {
        return this.field_176785_v;
    }

    public float[] func_193349_f() {
        return this.field_193352_x;
    }

    public static EnumDyeColor func_176766_a(int i) {
        if (i < 0 || i >= EnumDyeColor.field_176789_r.length) {
            i = 0;
        }

        return EnumDyeColor.field_176789_r[i];
    }

    public static EnumDyeColor func_176764_b(int i) {
        if (i < 0 || i >= EnumDyeColor.field_176790_q.length) {
            i = 0;
        }

        return EnumDyeColor.field_176790_q[i];
    }

    public String toString() {
        return this.field_176785_v;
    }

    public String func_176610_l() {
        return this.field_176786_u;
    }

    static {
        EnumDyeColor[] aenumcolor = values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumDyeColor enumcolor = aenumcolor[j];

            EnumDyeColor.field_176790_q[enumcolor.func_176765_a()] = enumcolor;
            EnumDyeColor.field_176789_r[enumcolor.func_176767_b()] = enumcolor;
        }

    }
}

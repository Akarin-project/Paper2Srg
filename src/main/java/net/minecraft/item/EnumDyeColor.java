package net.minecraft.item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;


public enum EnumDyeColor implements IStringSerializable {

    WHITE(0, 15, "white", "white", 16383998, TextFormatting.WHITE), ORANGE(1, 14, "orange", "orange", 16351261, TextFormatting.GOLD), MAGENTA(2, 13, "magenta", "magenta", 13061821, TextFormatting.AQUA), LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, TextFormatting.BLUE), YELLOW(4, 11, "yellow", "yellow", 16701501, TextFormatting.YELLOW), LIME(5, 10, "lime", "lime", 8439583, TextFormatting.GREEN), PINK(6, 9, "pink", "pink", 15961002, TextFormatting.LIGHT_PURPLE), GRAY(7, 8, "gray", "gray", 4673362, TextFormatting.DARK_GRAY), SILVER(8, 7, "silver", "silver", 10329495, TextFormatting.GRAY), CYAN(9, 6, "cyan", "cyan", 1481884, TextFormatting.DARK_AQUA), PURPLE(10, 5, "purple", "purple", 8991416, TextFormatting.DARK_PURPLE), BLUE(11, 4, "blue", "blue", 3949738, TextFormatting.DARK_BLUE), BROWN(12, 3, "brown", "brown", 8606770, TextFormatting.GOLD), GREEN(13, 2, "green", "green", 6192150, TextFormatting.DARK_GREEN), RED(14, 1, "red", "red", 11546150, TextFormatting.DARK_RED), BLACK(15, 0, "black", "black", 1908001, TextFormatting.BLACK);

    private static final EnumDyeColor[] META_LOOKUP = new EnumDyeColor[values().length];
    private static final EnumDyeColor[] DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String unlocalizedName;
    private final int colorValue;
    private final float[] colorComponentValues;
    private final TextFormatting chatColor;

    private EnumDyeColor(int i, int j, String s, String s1, int k, TextFormatting enumchatformat) {
        this.meta = i;
        this.dyeDamage = j;
        this.name = s;
        this.unlocalizedName = s1;
        this.colorValue = k;
        this.chatColor = enumchatformat;
        int l = (k & 16711680) >> 16;
        int i1 = (k & '\uff00') >> 8;
        int j1 = (k & 255) >> 0;

        this.colorComponentValues = new float[] { (float) l / 255.0F, (float) i1 / 255.0F, (float) j1 / 255.0F};
    }

    public int getMetadata() {
        return this.meta;
    }

    public int getDyeDamage() {
        return this.dyeDamage;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public float[] getColorComponentValues() {
        return this.colorComponentValues;
    }

    public static EnumDyeColor byDyeDamage(int i) {
        if (i < 0 || i >= EnumDyeColor.DYE_DMG_LOOKUP.length) {
            i = 0;
        }

        return EnumDyeColor.DYE_DMG_LOOKUP[i];
    }

    public static EnumDyeColor byMetadata(int i) {
        if (i < 0 || i >= EnumDyeColor.META_LOOKUP.length) {
            i = 0;
        }

        return EnumDyeColor.META_LOOKUP[i];
    }

    public String toString() {
        return this.unlocalizedName;
    }

    public String getName() {
        return this.name;
    }

    static {
        EnumDyeColor[] aenumcolor = values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumDyeColor enumcolor = aenumcolor[j];

            EnumDyeColor.META_LOOKUP[enumcolor.getMetadata()] = enumcolor;
            EnumDyeColor.DYE_DMG_LOOKUP[enumcolor.getDyeDamage()] = enumcolor;
        }

    }
}

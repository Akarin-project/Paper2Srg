package net.minecraft.advancements;
import net.minecraft.util.text.TextFormatting;


public enum FrameType {

    TASK("task", 0, TextFormatting.GREEN), CHALLENGE("challenge", 26, TextFormatting.DARK_PURPLE), GOAL("goal", 52, TextFormatting.GREEN);

    private final String name;
    private final int icon;
    private final TextFormatting format;

    private FrameType(String s, int i, TextFormatting enumchatformat) {
        this.name = s;
        this.icon = i;
        this.format = enumchatformat;
    }

    public String getName() {
        return this.name;
    }

    public static FrameType byName(String s) {
        FrameType[] aadvancementframetype = values();
        int i = aadvancementframetype.length;

        for (int j = 0; j < i; ++j) {
            FrameType advancementframetype = aadvancementframetype[j];

            if (advancementframetype.name.equals(s)) {
                return advancementframetype;
            }
        }

        throw new IllegalArgumentException("Unknown frame type \'" + s + "\'");
    }

    public TextFormatting getFormat() {
        return this.format;
    }
}

package net.minecraft.advancements;
import net.minecraft.util.text.TextFormatting;


public enum FrameType {

    TASK("task", 0, TextFormatting.GREEN), CHALLENGE("challenge", 26, TextFormatting.DARK_PURPLE), GOAL("goal", 52, TextFormatting.GREEN);

    private final String field_192313_d;
    private final int field_192314_e;
    private final TextFormatting field_193230_f;

    private FrameType(String s, int i, TextFormatting enumchatformat) {
        this.field_192313_d = s;
        this.field_192314_e = i;
        this.field_193230_f = enumchatformat;
    }

    public String func_192307_a() {
        return this.field_192313_d;
    }

    public static FrameType func_192308_a(String s) {
        FrameType[] aadvancementframetype = values();
        int i = aadvancementframetype.length;

        for (int j = 0; j < i; ++j) {
            FrameType advancementframetype = aadvancementframetype[j];

            if (advancementframetype.field_192313_d.equals(s)) {
                return advancementframetype;
            }
        }

        throw new IllegalArgumentException("Unknown frame type \'" + s + "\'");
    }

    public TextFormatting func_193229_c() {
        return this.field_193230_f;
    }
}

package net.minecraft.item;
import net.minecraft.util.text.TextFormatting;


public enum EnumRarity {

    COMMON(TextFormatting.WHITE, "Common"), UNCOMMON(TextFormatting.YELLOW, "Uncommon"), RARE(TextFormatting.AQUA, "Rare"), EPIC(TextFormatting.LIGHT_PURPLE, "Epic");

    public final TextFormatting field_77937_e;
    public final String field_77934_f;

    private EnumRarity(TextFormatting enumchatformat, String s) {
        this.field_77937_e = enumchatformat;
        this.field_77934_f = s;
    }
}

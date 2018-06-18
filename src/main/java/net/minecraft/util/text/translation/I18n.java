package net.minecraft.util.text.translation;

@Deprecated
public class I18n {

    private static final LanguageMap localizedName = LanguageMap.getInstance();
    private static final LanguageMap fallbackTranslator = new LanguageMap();

    @Deprecated
    public static String translateToLocal(String s) {
        return I18n.localizedName.translateKey(s);
    }

    @Deprecated
    public static String translateToLocalFormatted(String s, Object... aobject) {
        return I18n.localizedName.translateKeyFormat(s, aobject);
    }

    @Deprecated
    public static String translateToFallback(String s) {
        return I18n.fallbackTranslator.translateKey(s);
    }

    @Deprecated
    public static boolean canTranslate(String s) {
        return I18n.localizedName.isKeyTranslated(s);
    }

    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return I18n.localizedName.getLastUpdateTimeInMilliseconds();
    }
}

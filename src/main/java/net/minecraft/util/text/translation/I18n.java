package net.minecraft.util.text.translation;

@Deprecated
public class I18n {

    private static final LanguageMap field_74839_a = LanguageMap.func_74808_a();
    private static final LanguageMap field_150828_b = new LanguageMap();

    @Deprecated
    public static String func_74838_a(String s) {
        return I18n.field_74839_a.func_74805_b(s);
    }

    @Deprecated
    public static String func_74837_a(String s, Object... aobject) {
        return I18n.field_74839_a.func_74803_a(s, aobject);
    }

    @Deprecated
    public static String func_150826_b(String s) {
        return I18n.field_150828_b.func_74805_b(s);
    }

    @Deprecated
    public static boolean func_94522_b(String s) {
        return I18n.field_74839_a.func_94520_b(s);
    }

    public static long func_150827_a() {
        return I18n.field_74839_a.func_150510_c();
    }
}

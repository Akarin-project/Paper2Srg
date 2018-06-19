package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;

public enum SoundCategory {

    MASTER("master"), MUSIC("music"), RECORDS("record"), WEATHER("weather"), BLOCKS("block"), HOSTILE("hostile"), NEUTRAL("neutral"), PLAYERS("player"), AMBIENT("ambient"), VOICE("voice");

    private static final Map<String, SoundCategory> field_187961_k = Maps.newHashMap();
    private final String field_187962_l;

    private SoundCategory(String s) {
        this.field_187962_l = s;
    }

    public String func_187948_a() {
        return this.field_187962_l;
    }

    public static SoundCategory func_187950_a(String s) {
        return (SoundCategory) SoundCategory.field_187961_k.get(s);
    }

    public static Set<String> func_187949_b() {
        return SoundCategory.field_187961_k.keySet();
    }

    static {
        SoundCategory[] asoundcategory = values();
        int i = asoundcategory.length;

        for (int j = 0; j < i; ++j) {
            SoundCategory soundcategory = asoundcategory[j];

            if (SoundCategory.field_187961_k.containsKey(soundcategory.func_187948_a())) {
                throw new Error("Clash in Sound Category name pools! Cannot insert " + soundcategory);
            }

            SoundCategory.field_187961_k.put(soundcategory.func_187948_a(), soundcategory);
        }

    }
}

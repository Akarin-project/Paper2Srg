package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;

public enum SoundCategory {

    MASTER("master"), MUSIC("music"), RECORDS("record"), WEATHER("weather"), BLOCKS("block"), HOSTILE("hostile"), NEUTRAL("neutral"), PLAYERS("player"), AMBIENT("ambient"), VOICE("voice");

    private static final Map<String, SoundCategory> SOUND_CATEGORIES = Maps.newHashMap();
    private final String name;

    private SoundCategory(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public static SoundCategory getByName(String s) {
        return (SoundCategory) SoundCategory.SOUND_CATEGORIES.get(s);
    }

    public static Set<String> getSoundCategoryNames() {
        return SoundCategory.SOUND_CATEGORIES.keySet();
    }

    static {
        SoundCategory[] asoundcategory = values();
        int i = asoundcategory.length;

        for (int j = 0; j < i; ++j) {
            SoundCategory soundcategory = asoundcategory[j];

            if (SoundCategory.SOUND_CATEGORIES.containsKey(soundcategory.getName())) {
                throw new Error("Clash in Sound Category name pools! Cannot insert " + soundcategory);
            }

            SoundCategory.SOUND_CATEGORIES.put(soundcategory.getName(), soundcategory);
        }

    }
}

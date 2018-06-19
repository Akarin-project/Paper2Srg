package net.minecraft.world;

public enum EnumDifficulty {

    PEACEFUL(0, "options.difficulty.peaceful"), EASY(1, "options.difficulty.easy"), NORMAL(2, "options.difficulty.normal"), HARD(3, "options.difficulty.hard");

    private static final EnumDifficulty[] field_151530_e = new EnumDifficulty[values().length];
    private final int field_151527_f;
    private final String field_151528_g;

    private EnumDifficulty(int i, String s) {
        this.field_151527_f = i;
        this.field_151528_g = s;
    }

    public int func_151525_a() {
        return this.field_151527_f;
    }

    public static EnumDifficulty func_151523_a(int i) {
        return EnumDifficulty.field_151530_e[i % EnumDifficulty.field_151530_e.length];
    }

    public String func_151526_b() {
        return this.field_151528_g;
    }

    static {
        EnumDifficulty[] aenumdifficulty = values();
        int i = aenumdifficulty.length;

        for (int j = 0; j < i; ++j) {
            EnumDifficulty enumdifficulty = aenumdifficulty[j];

            EnumDifficulty.field_151530_e[enumdifficulty.field_151527_f] = enumdifficulty;
        }

    }
}

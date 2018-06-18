package net.minecraft.world;

public enum EnumDifficulty {

    PEACEFUL(0, "options.difficulty.peaceful"), EASY(1, "options.difficulty.easy"), NORMAL(2, "options.difficulty.normal"), HARD(3, "options.difficulty.hard");

    private static final EnumDifficulty[] ID_MAPPING = new EnumDifficulty[values().length];
    private final int difficultyId;
    private final String difficultyResourceKey;

    private EnumDifficulty(int i, String s) {
        this.difficultyId = i;
        this.difficultyResourceKey = s;
    }

    public int getDifficultyId() {
        return this.difficultyId;
    }

    public static EnumDifficulty getDifficultyEnum(int i) {
        return EnumDifficulty.ID_MAPPING[i % EnumDifficulty.ID_MAPPING.length];
    }

    public String getDifficultyResourceKey() {
        return this.difficultyResourceKey;
    }

    static {
        EnumDifficulty[] aenumdifficulty = values();
        int i = aenumdifficulty.length;

        for (int j = 0; j < i; ++j) {
            EnumDifficulty enumdifficulty = aenumdifficulty[j];

            EnumDifficulty.ID_MAPPING[enumdifficulty.difficultyId] = enumdifficulty;
        }

    }
}

package net.minecraft.world;
import net.minecraft.entity.player.PlayerCapabilities;


public enum GameType {

    NOT_SET(-1, "", ""), SURVIVAL(0, "survival", "s"), CREATIVE(1, "creative", "c"), ADVENTURE(2, "adventure", "a"), SPECTATOR(3, "spectator", "sp");

    int id;
    String name;
    String shortName;

    private GameType(int i, String s, String s1) {
        this.id = i;
        this.name = s;
        this.shortName = s1;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void configurePlayerCapabilities(PlayerCapabilities playerabilities) {
        if (this == GameType.CREATIVE) {
            playerabilities.allowFlying = true;
            playerabilities.isCreativeMode = true;
            playerabilities.disableDamage = true;
        } else if (this == GameType.SPECTATOR) {
            playerabilities.allowFlying = true;
            playerabilities.isCreativeMode = false;
            playerabilities.disableDamage = true;
            playerabilities.isFlying = true;
        } else {
            playerabilities.allowFlying = false;
            playerabilities.isCreativeMode = false;
            playerabilities.disableDamage = false;
            playerabilities.isFlying = false;
        }

        playerabilities.allowEdit = !this.hasLimitedInteractions();
    }

    public boolean hasLimitedInteractions() {
        return this == GameType.ADVENTURE || this == GameType.SPECTATOR;
    }

    public boolean isCreative() {
        return this == GameType.CREATIVE;
    }

    public boolean isSurvivalOrAdventure() {
        return this == GameType.SURVIVAL || this == GameType.ADVENTURE;
    }

    public static GameType getByID(int i) {
        return parseGameTypeWithDefault(i, GameType.SURVIVAL);
    }

    public static GameType parseGameTypeWithDefault(int i, GameType enumgamemode) {
        GameType[] aenumgamemode = values();
        int j = aenumgamemode.length;

        for (int k = 0; k < j; ++k) {
            GameType enumgamemode1 = aenumgamemode[k];

            if (enumgamemode1.id == i) {
                return enumgamemode1;
            }
        }

        return enumgamemode;
    }

    public static GameType parseGameTypeWithDefault(String s, GameType enumgamemode) {
        GameType[] aenumgamemode = values();
        int i = aenumgamemode.length;

        for (int j = 0; j < i; ++j) {
            GameType enumgamemode1 = aenumgamemode[j];

            if (enumgamemode1.name.equals(s) || enumgamemode1.shortName.equals(s)) {
                return enumgamemode1;
            }
        }

        return enumgamemode;
    }
}

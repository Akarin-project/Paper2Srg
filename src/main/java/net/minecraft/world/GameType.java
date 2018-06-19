package net.minecraft.world;
import net.minecraft.entity.player.PlayerCapabilities;


public enum GameType {

    NOT_SET(-1, "", ""), SURVIVAL(0, "survival", "s"), CREATIVE(1, "creative", "c"), ADVENTURE(2, "adventure", "a"), SPECTATOR(3, "spectator", "sp");

    int field_77154_e;
    String field_77151_f;
    String field_185330_h;

    private GameType(int i, String s, String s1) {
        this.field_77154_e = i;
        this.field_77151_f = s;
        this.field_185330_h = s1;
    }

    public int func_77148_a() {
        return this.field_77154_e;
    }

    public String func_77149_b() {
        return this.field_77151_f;
    }

    public void func_77147_a(PlayerCapabilities playerabilities) {
        if (this == GameType.CREATIVE) {
            playerabilities.field_75101_c = true;
            playerabilities.field_75098_d = true;
            playerabilities.field_75102_a = true;
        } else if (this == GameType.SPECTATOR) {
            playerabilities.field_75101_c = true;
            playerabilities.field_75098_d = false;
            playerabilities.field_75102_a = true;
            playerabilities.field_75100_b = true;
        } else {
            playerabilities.field_75101_c = false;
            playerabilities.field_75098_d = false;
            playerabilities.field_75102_a = false;
            playerabilities.field_75100_b = false;
        }

        playerabilities.field_75099_e = !this.func_82752_c();
    }

    public boolean func_82752_c() {
        return this == GameType.ADVENTURE || this == GameType.SPECTATOR;
    }

    public boolean func_77145_d() {
        return this == GameType.CREATIVE;
    }

    public boolean func_77144_e() {
        return this == GameType.SURVIVAL || this == GameType.ADVENTURE;
    }

    public static GameType func_77146_a(int i) {
        return func_185329_a(i, GameType.SURVIVAL);
    }

    public static GameType func_185329_a(int i, GameType enumgamemode) {
        GameType[] aenumgamemode = values();
        int j = aenumgamemode.length;

        for (int k = 0; k < j; ++k) {
            GameType enumgamemode1 = aenumgamemode[k];

            if (enumgamemode1.field_77154_e == i) {
                return enumgamemode1;
            }
        }

        return enumgamemode;
    }

    public static GameType func_185328_a(String s, GameType enumgamemode) {
        GameType[] aenumgamemode = values();
        int i = aenumgamemode.length;

        for (int j = 0; j < i; ++j) {
            GameType enumgamemode1 = aenumgamemode[j];

            if (enumgamemode1.field_77151_f.equals(s) || enumgamemode1.field_185330_h.equals(s)) {
                return enumgamemode1;
            }
        }

        return enumgamemode;
    }
}

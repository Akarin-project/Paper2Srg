package net.minecraft.world;
import net.minecraft.world.storage.WorldInfo;


public final class WorldSettings {

    private final long seed;
    private final GameType gameType;
    private final boolean mapFeaturesEnabled;
    private final boolean hardcoreEnabled;
    private final WorldType terrainType;
    private boolean commandsAllowed;
    private boolean bonusChestEnabled;
    private String generatorOptions;

    public WorldSettings(long i, GameType enumgamemode, boolean flag, boolean flag1, WorldType worldtype) {
        this.generatorOptions = "";
        this.seed = i;
        this.gameType = enumgamemode;
        this.mapFeaturesEnabled = flag;
        this.hardcoreEnabled = flag1;
        this.terrainType = worldtype;
    }

    public WorldSettings(WorldInfo worlddata) {
        this(worlddata.getSeed(), worlddata.getGameType(), worlddata.isMapFeaturesEnabled(), worlddata.isHardcoreModeEnabled(), worlddata.getTerrainType());
    }

    public WorldSettings enableBonusChest() {
        this.bonusChestEnabled = true;
        return this;
    }

    public WorldSettings setGeneratorOptions(String s) {
        this.generatorOptions = s;
        return this;
    }

    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public long getSeed() {
        return this.seed;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public boolean getHardcoreEnabled() {
        return this.hardcoreEnabled;
    }

    public boolean isMapFeaturesEnabled() {
        return this.mapFeaturesEnabled;
    }

    public WorldType getTerrainType() {
        return this.terrainType;
    }

    public boolean areCommandsAllowed() {
        return this.commandsAllowed;
    }

    public static GameType getGameTypeById(int i) {
        return GameType.getByID(i);
    }

    public String getGeneratorOptions() {
        return this.generatorOptions;
    }
}

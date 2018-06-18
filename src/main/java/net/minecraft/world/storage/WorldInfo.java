package net.minecraft.world.storage;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import org.bukkit.Bukkit;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
// CraftBukkit end

public class WorldInfo {

    private String versionName;
    private int versionId;
    private boolean versionSnapshot;
    public static final EnumDifficulty DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
    private long randomSeed;
    private WorldType terrainType;
    private String generatorOptions;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long totalTime;
    private long worldTime;
    private long lastTimePlayed;
    private long sizeOnDisk;
    private NBTTagCompound playerTag;
    private int dimension;
    private String levelName;
    private int saveVersion;
    private int cleanWeatherTime;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;
    private GameType gameType;
    private boolean mapFeaturesEnabled;
    private boolean hardcore;
    private boolean allowCommands;
    private boolean initialized;
    private EnumDifficulty difficulty;
    private boolean difficultyLocked;
    private double borderCenterX;
    private double borderCenterZ;
    private double borderSize;
    private long borderSizeLerpTime;
    private double borderSizeLerpTarget;
    private double borderSafeZone;
    private double borderDamagePerBlock;
    private int borderWarningDistance;
    private int borderWarningTime;
    private final Map<DimensionType, NBTTagCompound> dimensionData;
    private GameRules gameRules;
    public WorldServer world; // CraftBukkit

    protected WorldInfo() {
        this.terrainType = WorldType.DEFAULT;
        this.generatorOptions = "";
        this.borderSize = 6.0E7D;
        this.borderSafeZone = 5.0D;
        this.borderDamagePerBlock = 0.2D;
        this.borderWarningDistance = 5;
        this.borderWarningTime = 15;
        this.dimensionData = Maps.newEnumMap(DimensionType.class);
        this.gameRules = new GameRules();
    }

    public static void registerFixes(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.LEVEL, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.hasKey("Player", 10)) {
                    nbttagcompound.setTag("Player", dataconverter.process(FixTypes.PLAYER, nbttagcompound.getCompoundTag("Player"), i));
                }

                return nbttagcompound;
            }
        });
    }

    public WorldInfo(NBTTagCompound nbttagcompound) {
        this.terrainType = WorldType.DEFAULT;
        this.generatorOptions = "";
        this.borderSize = 6.0E7D;
        this.borderSafeZone = 5.0D;
        this.borderDamagePerBlock = 0.2D;
        this.borderWarningDistance = 5;
        this.borderWarningTime = 15;
        this.dimensionData = Maps.newEnumMap(DimensionType.class);
        this.gameRules = new GameRules();
        NBTTagCompound nbttagcompound1;

        if (nbttagcompound.hasKey("Version", 10)) {
            nbttagcompound1 = nbttagcompound.getCompoundTag("Version");
            this.versionName = nbttagcompound1.getString("Name");
            this.versionId = nbttagcompound1.getInteger("Id");
            this.versionSnapshot = nbttagcompound1.getBoolean("Snapshot");
        }

        this.randomSeed = nbttagcompound.getLong("RandomSeed");
        if (nbttagcompound.hasKey("generatorName", 8)) {
            String s = nbttagcompound.getString("generatorName");

            this.terrainType = WorldType.parseWorldType(s);
            if (this.terrainType == null) {
                this.terrainType = WorldType.DEFAULT;
            } else if (this.terrainType.isVersioned()) {
                int i = 0;

                if (nbttagcompound.hasKey("generatorVersion", 99)) {
                    i = nbttagcompound.getInteger("generatorVersion");
                }

                this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(i);
            }

            if (nbttagcompound.hasKey("generatorOptions", 8)) {
                this.generatorOptions = nbttagcompound.getString("generatorOptions");
            }
        }

        this.gameType = GameType.getByID(nbttagcompound.getInteger("GameType"));
        if (nbttagcompound.hasKey("MapFeatures", 99)) {
            this.mapFeaturesEnabled = nbttagcompound.getBoolean("MapFeatures");
        } else {
            this.mapFeaturesEnabled = true;
        }

        this.spawnX = nbttagcompound.getInteger("SpawnX");
        this.spawnY = nbttagcompound.getInteger("SpawnY");
        this.spawnZ = nbttagcompound.getInteger("SpawnZ");
        this.totalTime = nbttagcompound.getLong("Time");
        if (nbttagcompound.hasKey("DayTime", 99)) {
            this.worldTime = nbttagcompound.getLong("DayTime");
        } else {
            this.worldTime = this.totalTime;
        }

        this.lastTimePlayed = nbttagcompound.getLong("LastPlayed");
        this.sizeOnDisk = nbttagcompound.getLong("SizeOnDisk");
        this.levelName = nbttagcompound.getString("LevelName");
        this.saveVersion = nbttagcompound.getInteger("version");
        this.cleanWeatherTime = nbttagcompound.getInteger("clearWeatherTime");
        this.rainTime = nbttagcompound.getInteger("rainTime");
        this.raining = nbttagcompound.getBoolean("raining");
        this.thunderTime = nbttagcompound.getInteger("thunderTime");
        this.thundering = nbttagcompound.getBoolean("thundering");
        this.hardcore = nbttagcompound.getBoolean("hardcore");
        if (nbttagcompound.hasKey("initialized", 99)) {
            this.initialized = nbttagcompound.getBoolean("initialized");
        } else {
            this.initialized = true;
        }

        if (nbttagcompound.hasKey("allowCommands", 99)) {
            this.allowCommands = nbttagcompound.getBoolean("allowCommands");
        } else {
            this.allowCommands = this.gameType == GameType.CREATIVE;
        }

        if (nbttagcompound.hasKey("Player", 10)) {
            this.playerTag = nbttagcompound.getCompoundTag("Player");
            this.dimension = this.playerTag.getInteger("Dimension");
        }

        if (nbttagcompound.hasKey("GameRules", 10)) {
            this.gameRules.readFromNBT(nbttagcompound.getCompoundTag("GameRules"));
        }

        if (nbttagcompound.hasKey("Difficulty", 99)) {
            this.difficulty = EnumDifficulty.getDifficultyEnum(nbttagcompound.getByte("Difficulty"));
        }

        if (nbttagcompound.hasKey("DifficultyLocked", 1)) {
            this.difficultyLocked = nbttagcompound.getBoolean("DifficultyLocked");
        }

        if (nbttagcompound.hasKey("BorderCenterX", 99)) {
            this.borderCenterX = nbttagcompound.getDouble("BorderCenterX");
        }

        if (nbttagcompound.hasKey("BorderCenterZ", 99)) {
            this.borderCenterZ = nbttagcompound.getDouble("BorderCenterZ");
        }

        if (nbttagcompound.hasKey("BorderSize", 99)) {
            this.borderSize = nbttagcompound.getDouble("BorderSize");
        }

        if (nbttagcompound.hasKey("BorderSizeLerpTime", 99)) {
            this.borderSizeLerpTime = nbttagcompound.getLong("BorderSizeLerpTime");
        }

        if (nbttagcompound.hasKey("BorderSizeLerpTarget", 99)) {
            this.borderSizeLerpTarget = nbttagcompound.getDouble("BorderSizeLerpTarget");
        }

        if (nbttagcompound.hasKey("BorderSafeZone", 99)) {
            this.borderSafeZone = nbttagcompound.getDouble("BorderSafeZone");
        }

        if (nbttagcompound.hasKey("BorderDamagePerBlock", 99)) {
            this.borderDamagePerBlock = nbttagcompound.getDouble("BorderDamagePerBlock");
        }

        if (nbttagcompound.hasKey("BorderWarningBlocks", 99)) {
            this.borderWarningDistance = nbttagcompound.getInteger("BorderWarningBlocks");
        }

        if (nbttagcompound.hasKey("BorderWarningTime", 99)) {
            this.borderWarningTime = nbttagcompound.getInteger("BorderWarningTime");
        }

        if (nbttagcompound.hasKey("DimensionData", 10)) {
            nbttagcompound1 = nbttagcompound.getCompoundTag("DimensionData");
            Iterator iterator = nbttagcompound1.getKeySet().iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                this.dimensionData.put(DimensionType.getById(Integer.parseInt(s1)), nbttagcompound1.getCompoundTag(s1));
            }
        }

    }

    public WorldInfo(WorldSettings worldsettings, String s) {
        this.terrainType = WorldType.DEFAULT;
        this.generatorOptions = "";
        this.borderSize = 6.0E7D;
        this.borderSafeZone = 5.0D;
        this.borderDamagePerBlock = 0.2D;
        this.borderWarningDistance = 5;
        this.borderWarningTime = 15;
        this.dimensionData = Maps.newEnumMap(DimensionType.class);
        this.gameRules = new GameRules();
        this.populateFromWorldSettings(worldsettings);
        this.levelName = s;
        this.difficulty = WorldInfo.DEFAULT_DIFFICULTY;
        this.initialized = false;
    }

    public void populateFromWorldSettings(WorldSettings worldsettings) {
        this.randomSeed = worldsettings.getSeed();
        this.gameType = worldsettings.getGameType();
        this.mapFeaturesEnabled = worldsettings.isMapFeaturesEnabled();
        this.hardcore = worldsettings.getHardcoreEnabled();
        this.terrainType = worldsettings.getTerrainType();
        this.generatorOptions = worldsettings.getGeneratorOptions();
        this.allowCommands = worldsettings.areCommandsAllowed();
    }

    public WorldInfo(WorldInfo worlddata) {
        this.terrainType = WorldType.DEFAULT;
        this.generatorOptions = "";
        this.borderSize = 6.0E7D;
        this.borderSafeZone = 5.0D;
        this.borderDamagePerBlock = 0.2D;
        this.borderWarningDistance = 5;
        this.borderWarningTime = 15;
        this.dimensionData = Maps.newEnumMap(DimensionType.class);
        this.gameRules = new GameRules();
        this.randomSeed = worlddata.randomSeed;
        this.terrainType = worlddata.terrainType;
        this.generatorOptions = worlddata.generatorOptions;
        this.gameType = worlddata.gameType;
        this.mapFeaturesEnabled = worlddata.mapFeaturesEnabled;
        this.spawnX = worlddata.spawnX;
        this.spawnY = worlddata.spawnY;
        this.spawnZ = worlddata.spawnZ;
        this.totalTime = worlddata.totalTime;
        this.worldTime = worlddata.worldTime;
        this.lastTimePlayed = worlddata.lastTimePlayed;
        this.sizeOnDisk = worlddata.sizeOnDisk;
        this.playerTag = worlddata.playerTag;
        this.dimension = worlddata.dimension;
        this.levelName = worlddata.levelName;
        this.saveVersion = worlddata.saveVersion;
        this.rainTime = worlddata.rainTime;
        this.raining = worlddata.raining;
        this.thunderTime = worlddata.thunderTime;
        this.thundering = worlddata.thundering;
        this.hardcore = worlddata.hardcore;
        this.allowCommands = worlddata.allowCommands;
        this.initialized = worlddata.initialized;
        this.gameRules = worlddata.gameRules;
        this.difficulty = worlddata.difficulty;
        this.difficultyLocked = worlddata.difficultyLocked;
        this.borderCenterX = worlddata.borderCenterX;
        this.borderCenterZ = worlddata.borderCenterZ;
        this.borderSize = worlddata.borderSize;
        this.borderSizeLerpTime = worlddata.borderSizeLerpTime;
        this.borderSizeLerpTarget = worlddata.borderSizeLerpTarget;
        this.borderSafeZone = worlddata.borderSafeZone;
        this.borderDamagePerBlock = worlddata.borderDamagePerBlock;
        this.borderWarningTime = worlddata.borderWarningTime;
        this.borderWarningDistance = worlddata.borderWarningDistance;
    }

    public NBTTagCompound cloneNBTCompound(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            nbttagcompound = this.playerTag;
        }

        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        this.updateTagCompound(nbttagcompound1, nbttagcompound);
        return nbttagcompound1;
    }

    private void updateTagCompound(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();

        nbttagcompound2.setString("Name", "1.12.2");
        nbttagcompound2.setInteger("Id", 1343);
        nbttagcompound2.setBoolean("Snapshot", false);
        nbttagcompound.setTag("Version", nbttagcompound2);
        nbttagcompound.setInteger("DataVersion", 1343);
        nbttagcompound.setLong("RandomSeed", this.randomSeed);
        nbttagcompound.setString("generatorName", this.terrainType.getName());
        nbttagcompound.setInteger("generatorVersion", this.terrainType.getVersion());
        nbttagcompound.setString("generatorOptions", this.generatorOptions);
        nbttagcompound.setInteger("GameType", this.gameType.getID());
        nbttagcompound.setBoolean("MapFeatures", this.mapFeaturesEnabled);
        nbttagcompound.setInteger("SpawnX", this.spawnX);
        nbttagcompound.setInteger("SpawnY", this.spawnY);
        nbttagcompound.setInteger("SpawnZ", this.spawnZ);
        nbttagcompound.setLong("Time", this.totalTime);
        nbttagcompound.setLong("DayTime", this.worldTime);
        nbttagcompound.setLong("SizeOnDisk", this.sizeOnDisk);
        nbttagcompound.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());
        nbttagcompound.setString("LevelName", this.levelName);
        nbttagcompound.setInteger("version", this.saveVersion);
        nbttagcompound.setInteger("clearWeatherTime", this.cleanWeatherTime);
        nbttagcompound.setInteger("rainTime", this.rainTime);
        nbttagcompound.setBoolean("raining", this.raining);
        nbttagcompound.setInteger("thunderTime", this.thunderTime);
        nbttagcompound.setBoolean("thundering", this.thundering);
        nbttagcompound.setBoolean("hardcore", this.hardcore);
        nbttagcompound.setBoolean("allowCommands", this.allowCommands);
        nbttagcompound.setBoolean("initialized", this.initialized);
        nbttagcompound.setDouble("BorderCenterX", this.borderCenterX);
        nbttagcompound.setDouble("BorderCenterZ", this.borderCenterZ);
        nbttagcompound.setDouble("BorderSize", this.borderSize);
        nbttagcompound.setLong("BorderSizeLerpTime", this.borderSizeLerpTime);
        nbttagcompound.setDouble("BorderSafeZone", this.borderSafeZone);
        nbttagcompound.setDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
        nbttagcompound.setDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
        nbttagcompound.setDouble("BorderWarningBlocks", (double) this.borderWarningDistance);
        nbttagcompound.setDouble("BorderWarningTime", (double) this.borderWarningTime);
        if (this.difficulty != null) {
            nbttagcompound.setByte("Difficulty", (byte) this.difficulty.getDifficultyId());
        }

        nbttagcompound.setBoolean("DifficultyLocked", this.difficultyLocked);
        nbttagcompound.setTag("GameRules", this.gameRules.writeToNBT());
        NBTTagCompound nbttagcompound3 = new NBTTagCompound();
        Iterator iterator = this.dimensionData.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            nbttagcompound3.setTag(String.valueOf(((DimensionType) entry.getKey()).getId()), (NBTBase) entry.getValue());
        }

        nbttagcompound.setTag("DimensionData", nbttagcompound3);
        if (nbttagcompound1 != null) {
            nbttagcompound.setTag("Player", nbttagcompound1);
        }

    }

    public long getSeed() {
        return this.randomSeed;
    }

    public int getSpawnX() {
        return this.spawnX;
    }

    public int getSpawnY() {
        return this.spawnY;
    }

    public int getSpawnZ() {
        return this.spawnZ;
    }

    public long getWorldTotalTime() {
        return this.totalTime;
    }

    public long getWorldTime() {
        return this.worldTime;
    }

    public NBTTagCompound getPlayerNBTTagCompound() {
        return this.playerTag;
    }

    public void setWorldTotalTime(long i) {
        this.totalTime = i;
    }

    public void setWorldTime(long i) {
        this.worldTime = i;
    }

    public void setSpawn(BlockPos blockposition) {
        this.spawnX = blockposition.getX();
        this.spawnY = blockposition.getY();
        this.spawnZ = blockposition.getZ();
    }

    public String getWorldName() {
        return this.levelName;
    }

    public void setWorldName(String s) {
        this.levelName = s;
    }

    public int getSaveVersion() {
        return this.saveVersion;
    }

    public void setSaveVersion(int i) {
        this.saveVersion = i;
    }

    public int getCleanWeatherTime() {
        return this.cleanWeatherTime;
    }

    public void setCleanWeatherTime(int i) {
        this.cleanWeatherTime = i;
    }

    public boolean isThundering() {
        return this.thundering;
    }

    public void setThundering(boolean flag) {
        // CraftBukkit start
        org.bukkit.World world = Bukkit.getWorld(getWorldName());
        if (world != null) {
            ThunderChangeEvent thunder = new ThunderChangeEvent(world, flag);
            Bukkit.getServer().getPluginManager().callEvent(thunder);
            if (thunder.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end
        this.thundering = flag;
    }

    public int getThunderTime() {
        return this.thunderTime;
    }

    public void setThunderTime(int i) {
        this.thunderTime = i;
    }

    public boolean isRaining() {
        return this.raining;
    }

    public void setRaining(boolean flag) {
        // CraftBukkit start
        org.bukkit.World world = Bukkit.getWorld(getWorldName());
        if (world != null) {
            WeatherChangeEvent weather = new WeatherChangeEvent(world, flag);
            Bukkit.getServer().getPluginManager().callEvent(weather);
            if (weather.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end
        this.raining = flag;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public void setRainTime(int i) {
        this.rainTime = i;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public boolean isMapFeaturesEnabled() {
        return this.mapFeaturesEnabled;
    }

    public void setMapFeaturesEnabled(boolean flag) {
        this.mapFeaturesEnabled = flag;
    }

    public void setGameType(GameType enumgamemode) {
        this.gameType = enumgamemode;
    }

    public boolean isHardcoreModeEnabled() {
        return this.hardcore;
    }

    public void setHardcore(boolean flag) {
        this.hardcore = flag;
    }

    public WorldType getTerrainType() {
        return this.terrainType;
    }

    public void setTerrainType(WorldType worldtype) {
        this.terrainType = worldtype;
    }

    public String getGeneratorOptions() {
        return this.generatorOptions == null ? "" : this.generatorOptions;
    }

    public boolean areCommandsAllowed() {
        return this.allowCommands;
    }

    public void setAllowCommands(boolean flag) {
        this.allowCommands = flag;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setServerInitialized(boolean flag) {
        this.initialized = flag;
    }

    public GameRules getGameRulesInstance() {
        return this.gameRules;
    }

    public double getBorderCenterX() {
        return this.borderCenterX;
    }

    public double getBorderCenterZ() {
        return this.borderCenterZ;
    }

    public double getBorderSize() {
        return this.borderSize;
    }

    public void setBorderSize(double d0) {
        this.borderSize = d0;
    }

    public long getBorderLerpTime() {
        return this.borderSizeLerpTime;
    }

    public void setBorderLerpTime(long i) {
        this.borderSizeLerpTime = i;
    }

    public double getBorderLerpTarget() {
        return this.borderSizeLerpTarget;
    }

    public void setBorderLerpTarget(double d0) {
        this.borderSizeLerpTarget = d0;
    }

    public void getBorderCenterZ(double d0) {
        this.borderCenterZ = d0;
    }

    public void getBorderCenterX(double d0) {
        this.borderCenterX = d0;
    }

    public double getBorderSafeZone() {
        return this.borderSafeZone;
    }

    public void setBorderSafeZone(double d0) {
        this.borderSafeZone = d0;
    }

    public double getBorderDamagePerBlock() {
        return this.borderDamagePerBlock;
    }

    public void setBorderDamagePerBlock(double d0) {
        this.borderDamagePerBlock = d0;
    }

    public int getBorderWarningDistance() {
        return this.borderWarningDistance;
    }

    public int getBorderWarningTime() {
        return this.borderWarningTime;
    }

    public void setBorderWarningDistance(int i) {
        this.borderWarningDistance = i;
    }

    public void setBorderWarningTime(int i) {
        this.borderWarningTime = i;
    }

    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(EnumDifficulty enumdifficulty) {
        this.difficulty = enumdifficulty;
        // CraftBukkit start
        SPacketServerDifficulty packet = new SPacketServerDifficulty(this.getDifficulty(), this.isDifficultyLocked());
        for (EntityPlayerMP player : (java.util.List<EntityPlayerMP>) (java.util.List) world.playerEntities) {
            player.connection.sendPacket(packet);
        }
        // CraftBukkit end
    }

    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }

    public void setDifficultyLocked(boolean flag) {
        this.difficultyLocked = flag;
    }

    public void addToCrashReport(CrashReportCategory crashreportsystemdetails) {
        crashreportsystemdetails.addDetail("Level seed", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.valueOf(WorldInfo.this.getSeed());
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level generator", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[] { Integer.valueOf(WorldInfo.this.terrainType.getId()), WorldInfo.this.terrainType.getName(), Integer.valueOf(WorldInfo.this.terrainType.getVersion()), Boolean.valueOf(WorldInfo.this.mapFeaturesEnabled)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level generator options", new ICrashReportDetail() {
            public String a() throws Exception {
                return WorldInfo.this.generatorOptions;
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level spawn location", new ICrashReportDetail() {
            public String a() throws Exception {
                return CrashReportCategory.getCoordinateInfo(WorldInfo.this.spawnX, WorldInfo.this.spawnY, WorldInfo.this.spawnZ);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level time", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("%d game time, %d day time", new Object[] { Long.valueOf(WorldInfo.this.totalTime), Long.valueOf(WorldInfo.this.worldTime)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level dimension", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.valueOf(WorldInfo.this.dimension);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level storage version", new ICrashReportDetail() {
            public String a() throws Exception {
                String s = "Unknown?";

                try {
                    switch (WorldInfo.this.saveVersion) {
                    case 19132:
                        s = "McRegion";
                        break;

                    case 19133:
                        s = "Anvil";
                    }
                } catch (Throwable throwable) {
                    ;
                }

                return String.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.this.saveVersion), s});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level weather", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { Integer.valueOf(WorldInfo.this.rainTime), Boolean.valueOf(WorldInfo.this.raining), Integer.valueOf(WorldInfo.this.thunderTime), Boolean.valueOf(WorldInfo.this.thundering)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Level game mode", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] { WorldInfo.this.gameType.getName(), Integer.valueOf(WorldInfo.this.gameType.getID()), Boolean.valueOf(WorldInfo.this.hardcore), Boolean.valueOf(WorldInfo.this.allowCommands)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    public NBTTagCompound getDimensionData(DimensionType dimensionmanager) {
        NBTTagCompound nbttagcompound = (NBTTagCompound) this.dimensionData.get(dimensionmanager);

        return nbttagcompound == null ? new NBTTagCompound() : nbttagcompound;
    }

    public void setDimensionData(DimensionType dimensionmanager, NBTTagCompound nbttagcompound) {
        this.dimensionData.put(dimensionmanager, nbttagcompound);
    }

    // CraftBukkit start - Check if the name stored in NBT is the correct one
    public void checkName( String name ) {
        if ( !this.levelName.equals( name ) ) {
            this.levelName = name;
        }
    }
    // CraftBukkit end
}

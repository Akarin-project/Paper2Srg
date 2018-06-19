package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import co.aikar.timings.MinecraftTimings;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Bootstrap;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Snooper;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spigotmc.SlackActivityAccountant;

// CraftBukkit start
import joptsimple.OptionSet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.Main;
// CraftBukkit end
import org.spigotmc.SlackActivityAccountant; // Spigot
import co.aikar.timings.MinecraftTimings; // Paper

public abstract class MinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {

    private static MinecraftServer SERVER; // Paper
    public static final Logger LOGGER = LogManager.getLogger();
    public static final File USER_CACHE_FILE = new File("usercache.json");
    public ISaveFormat anvilConverterForAnvilFile;
    private final Snooper usageSnooper = new Snooper("server", this, getCurrentTimeMillis());
    public File anvilFile;
    private final List<ITickable> tickables = Lists.newArrayList();
    public final ICommandManager commandManager;
    public final Profiler profiler = new Profiler();
    private NetworkSystem networkSystem; // Spigot
    private final ServerStatusResponse statusResponse = new ServerStatusResponse();
    private final Random random = new Random();
    public final DataFixer dataFixer;
    private String hostname;
    private int serverPort = -1;
    public WorldServer[] worlds;
    private PlayerList playerList;
    private boolean serverRunning = true;
    private boolean isRestarting = false; // Paper - flag to signify we're attempting to restart
    private boolean serverStopped;
    private int tickCounter;
    protected final Proxy serverProxy;
    public String currentTask;
    public int percentDone;
    private boolean onlineMode;
    private boolean preventProxyConnections;
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;
    private boolean pvpEnabled;
    private boolean allowFlight;
    private String motd;
    private int buildLimit;
    private int maxPlayerIdleMinutes;
    public final long[] tickTimeArray = new long[100];
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;
    private String serverOwner;
    private String folderName;
    private boolean isDemo;
    private boolean enableBonusChest;
    private String resourcePackUrl = "";
    private String resourcePackHash = "";
    private boolean serverIsRunning;
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    private long nanoTimeSinceStatusRefresh;
    protected final Queue<FutureTask<?>> futureTaskQueue = new com.destroystokyo.paper.utils.CachedSizeConcurrentLinkedQueue<>(); // Spigot, PAIL: Rename // Paper - Make size() constant-time
    private Thread serverThread;
    private long currentTime = getCurrentTimeMillis();

    // CraftBukkit start
    public List<WorldServer> worlds = new ArrayList<WorldServer>();
    public org.bukkit.craftbukkit.CraftServer server;
    public OptionSet options;
    public org.bukkit.command.ConsoleCommandSender console;
    public org.bukkit.command.RemoteConsoleCommandSender remoteConsole;
    //public ConsoleReader reader; // Paper
    public static int currentTick = 0; // Paper - Further improve tick loop
    public final Thread primaryThread;
    public java.util.Queue<Runnable> processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
    public int autosavePeriod;
    public boolean serverAutoSave = false; // Paper
    // CraftBukkit end
    // Spigot start
    public final SlackActivityAccountant slackActivityAccountant = new SlackActivityAccountant();
    // Spigot end

    public MinecraftServer(OptionSet options, Proxy proxy, DataFixer dataconvertermanager, YggdrasilAuthenticationService yggdrasilauthenticationservice, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, PlayerProfileCache usercache) {
        SERVER = this; // Paper - better singleton
        io.netty.util.ResourceLeakDetector.setEnabled( false ); // Spigot - disable
        this.serverProxy = proxy;
        this.authService = yggdrasilauthenticationservice;
        this.sessionService = minecraftsessionservice;
        this.profileRepo = gameprofilerepository;
        this.profileCache = usercache;
        // this.universe = file; // CraftBukkit
        // this.p = new ServerConnection(this); // Spigot
        this.commandManager = this.createCommandManager();
        // this.convertable = new WorldLoaderServer(file); // CraftBukkit - moved to DedicatedServer.init
        this.dataFixer = dataconvertermanager;
        // CraftBukkit start
        this.options = options;
        // Paper start - Handled by TerminalConsoleAppender
        // Try to see if we're actually running in a terminal, disable jline if not
        /*
        if (System.console() == null && System.getProperty("jline.terminal") == null) {
            System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
            Main.useJline = false;
        }

        try {
            reader = new ConsoleReader(System.in, System.out);
            reader.setExpandEvents(false); // Avoid parsing exceptions for uncommonly used event designators
        } catch (Throwable e) {
            try {
                // Try again with jline disabled for Windows users without C++ 2008 Redistributable
                System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
                System.setProperty("user.language", "en");
                Main.useJline = false;
                reader = new ConsoleReader(System.in, System.out);
                reader.setExpandEvents(false);
            } catch (IOException ex) {
                LOGGER.warn((String) null, ex);
            }
        }
        */
        // Paper end
        Runtime.getRuntime().addShutdownHook(new org.bukkit.craftbukkit.util.ServerShutdownThread(this));

        this.serverThread = primaryThread = new Thread(this, "Server thread"); // Moved from main
    }

    public abstract PropertyManager getPropertyManager();
    // CraftBukkit end

    protected ServerCommandManager createCommandManager() {
        return new ServerCommandManager(this);
    }

    public abstract boolean init() throws IOException;

    protected void convertMapIfNeeded(String s) {
        if (this.getActiveAnvilConverter().isOldMapFormat(s)) {
            MinecraftServer.LOGGER.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(s, new IProgressUpdate() {
                private long startTime = System.currentTimeMillis();

                @Override
                public void displaySavingString(String s) {}

                @Override
                public void setLoadingProgress(int i) {
                    if (System.currentTimeMillis() - this.startTime >= 1000L) {
                        this.startTime = System.currentTimeMillis();
                        MinecraftServer.LOGGER.info("Converting... {}%", Integer.valueOf(i));
                    }

                }

                @Override
                public void displayLoadingString(String s) {}
            });
        }

    }

    protected synchronized void setUserMessage(String s) {
        this.userMessage = s;
    }

    public void loadAllWorlds(String s, String s1, long i, WorldType worldtype, String s2) {
        this.convertMapIfNeeded(s);
        this.setUserMessage("menu.loadingLevel");
        this.worlds = new WorldServer[3];
        /* CraftBukkit start - Remove ticktime arrays and worldsettings
        this.i = new long[this.worldServer.length][100];
        IDataManager idatamanager = this.convertable.a(s, true);

        this.a(this.S(), idatamanager);
        WorldData worlddata = idatamanager.getWorldData();
        WorldSettings worldsettings;

        if (worlddata == null) {
            if (this.V()) {
                worldsettings = DemoWorldServer.a;
            } else {
                worldsettings = new WorldSettings(i, this.getGamemode(), this.getGenerateStructures(), this.isHardcore(), worldtype);
                worldsettings.setGeneratorSettings(s2);
                if (this.N) {
                    worldsettings.a();
                }
            }

            worlddata = new WorldData(worldsettings, s1);
        } else {
            worlddata.a(s1);
            worldsettings = new WorldSettings(worlddata);
        }
        */
        int worldCount = 3;

        for (int j = 0; j < worldCount; ++j) {
            WorldServer world;
            byte dimension = 0;

            if (j == 1) {
                if (getAllowNether()) {
                    dimension = -1;
                } else {
                    continue;
                }
            }

            if (j == 2) {
                if (server.getAllowEnd()) {
                    dimension = 1;
                } else {
                    continue;
                }
            }

            String worldType = org.bukkit.World.Environment.getEnvironment(dimension).toString().toLowerCase();
            String name = (dimension == 0) ? s : s + "_" + worldType;

            org.bukkit.generator.ChunkGenerator gen = this.server.getGenerator(name);
            WorldSettings worldsettings = new WorldSettings(i, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), worldtype);
            worldsettings.setGeneratorOptions(s2);

            if (j == 0) {
                ISaveHandler idatamanager = new AnvilSaveHandler(server.getWorldContainer(), s1, true, this.dataFixer);
                WorldInfo worlddata = idatamanager.loadWorldInfo();
                if (worlddata == null) {
                    worlddata = new WorldInfo(worldsettings, s1);
                }
                worlddata.checkName(s1); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
                if (this.isDemo()) {
                    world = (WorldServer) (new WorldServerDemo(this, idatamanager, worlddata, dimension, this.profiler)).init();
                } else {
                    world = (WorldServer) (new WorldServer(this, idatamanager, worlddata, dimension, this.profiler, org.bukkit.World.Environment.getEnvironment(dimension), gen)).init();
                }

                world.initialize(worldsettings);
                this.server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(this, world.getScoreboard());
            } else {
                String dim = "DIM" + dimension;

                File newWorld = new File(new File(name), dim);
                File oldWorld = new File(new File(s), dim);

                if ((!newWorld.isDirectory()) && (oldWorld.isDirectory())) {
                    MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder required ----");
                    MinecraftServer.LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
                    MinecraftServer.LOGGER.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
                    MinecraftServer.LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

                    if (newWorld.exists()) {
                        MinecraftServer.LOGGER.warn("A file or folder already exists at " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    } else if (newWorld.getParentFile().mkdirs()) {
                        if (oldWorld.renameTo(newWorld)) {
                            MinecraftServer.LOGGER.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                            // Migrate world data too.
                            try {
                                com.google.common.io.Files.copy(new File(new File(s), "level.dat"), new File(new File(name), "level.dat"));
                                org.apache.commons.io.FileUtils.copyDirectory(new File(new File(s), "data"), new File(new File(name), "data"));
                            } catch (IOException exception) {
                                MinecraftServer.LOGGER.warn("Unable to migrate world data.");
                            }
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
                        } else {
                            MinecraftServer.LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                        }
                    } else {
                        MinecraftServer.LOGGER.warn("Could not create path for " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    }
                }

                ISaveHandler idatamanager = new AnvilSaveHandler(server.getWorldContainer(), name, true, this.dataFixer);
                // world =, b0 to dimension, s1 to name, added Environment and gen
                WorldInfo worlddata = idatamanager.loadWorldInfo();
                if (worlddata == null) {
                    worlddata = new WorldInfo(worldsettings, name);
                }
                worlddata.checkName(name); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
                world = (WorldServer) new WorldServerMulti(this, idatamanager, dimension, this.worlds.get(0), this.profiler, worlddata, org.bukkit.World.Environment.getEnvironment(dimension), gen).init();
            }

            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(world.getWorld()));

            world.addEventListener(new ServerWorldEventHandler(this, world));
            if (!this.isSinglePlayer()) {
                world.getWorldInfo().setGameType(this.getGameType());
            }

            worlds.add(world);
            getPlayerList().setPlayerManager(worlds.toArray(new WorldServer[worlds.size()]));
        }
        // CraftBukkit end
        this.playerList.setPlayerManager(this.worlds);
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();

        // Paper start - Handle collideRule team for player collision toggle
        final Scoreboard scoreboard = this.getEntityWorld().getScoreboard();
        final java.util.Collection<String> toRemove = scoreboard.getTeams().stream().filter(team -> team.getName().startsWith("collideRule_")).map(ScorePlayerTeam::getName).collect(java.util.stream.Collectors.toList());
        for (String teamName : toRemove) {
            scoreboard.removeTeam(scoreboard.getTeam(teamName)); // Clean up after ourselves
        }

        if (!com.destroystokyo.paper.PaperConfig.enablePlayerCollisions) {
            this.getPlayerList().collideRuleTeamName = org.apache.commons.lang3.StringUtils.left("collideRule_" + this.getEntityWorld().rand.nextInt(), 16);
            ScorePlayerTeam collideTeam = scoreboard.createTeam(this.getPlayerList().collideRuleTeamName);
            collideTeam.setSeeFriendlyInvisiblesEnabled(false); // Because we want to mimic them not being on a team at all
        }
        // Paper end
    }

    protected void initialWorldChunkLoad() {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        int i = 0;

        this.setUserMessage("menu.generatingTerrain");
        boolean flag4 = false;

        // CraftBukkit start - fire WorldLoadEvent and handle whether or not to keep the spawn in memory
        for (int m = 0; m < worlds.size(); m++) {
            WorldServer worldserver = this.worlds.get(m);
            MinecraftServer.LOGGER.info("Preparing start region for level " + m + " (Seed: " + worldserver.getSeed() + ")");

            if (!worldserver.getWorld().getKeepSpawnInMemory()) {
                continue;
            }

            BlockPos blockposition = worldserver.getSpawnPoint();
            long j = getCurrentTimeMillis();
            i = 0;

            // Paper start
            short radius = worldserver.paperConfig.keepLoadedRange;
            for (int k = -radius; k <= radius && this.isServerRunning(); k += 16) {
                for (int l = -radius; l <= radius && this.isServerRunning(); l += 16) {
            // Paper end
                    long i1 = getCurrentTimeMillis();

                    if (i1 - j > 1000L) {
                        this.outputPercentRemaining("Preparing spawn area", i * 100 / 625);
                        j = i1;
                    }

                    ++i;
                    worldserver.getChunkProvider().provideChunk(blockposition.getX() + k >> 4, blockposition.getZ() + l >> 4);
                }
            }
        }

        for (WorldServer world : this.worlds) {
            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(world.getWorld()));
        }
        // CraftBukkit end
        this.clearCurrentTask();
    }

    protected void setResourcePackFromWorld(String s, ISaveHandler idatamanager) {
        File file = new File(idatamanager.getWorldDirectory(), "resources.zip");

        if (file.isFile()) {
            try {
                this.setResourcePack("level://" + URLEncoder.encode(s, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                MinecraftServer.LOGGER.warn("Something went wrong url encoding {}", s);
            }
        }

    }

    public abstract boolean canStructuresSpawn();

    public abstract GameType getGameType();

    public abstract EnumDifficulty getDifficulty();

    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    public abstract boolean shouldBroadcastRconToOps();

    public abstract boolean shouldBroadcastConsoleToOps();

    protected void outputPercentRemaining(String s, int i) {
        this.currentTask = s;
        this.percentDone = i;
        MinecraftServer.LOGGER.info("{}: {}%", s, Integer.valueOf(i));
    }

    protected void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
        this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD); // CraftBukkit
    }

    protected void saveAllWorlds(boolean flag) {
        WorldServer[] aworldserver = this.worlds;
        int i = aworldserver.length;

        // CraftBukkit start
        for (int j = 0; j < worlds.size(); ++j) {
            WorldServer worldserver = worlds.get(j);
            // CraftBukkit end

            if (worldserver != null) {
                if (!flag) {
                    MinecraftServer.LOGGER.info("Saving chunks for level \'{}\'/{}", worldserver.getWorldInfo().getWorldName(), worldserver.provider.getDimensionType().getName());
                }

                try {
                    worldserver.saveAllChunks(true, (IProgressUpdate) null);
                    worldserver.flush(); // CraftBukkit
                } catch (MinecraftException exceptionworldconflict) {
                    MinecraftServer.LOGGER.warn(exceptionworldconflict.getMessage());
                }
            }
        }

    }

    // CraftBukkit start
    private boolean hasStopped = false;
    private final Object stopLock = new Object();
    // CraftBukkit end

    public void stopServer() throws MinecraftException { // CraftBukkit - added throws
        // CraftBukkit start - prevent double stopping on multiple threads
        synchronized(stopLock) {
            if (hasStopped) return;
            hasStopped = true;
        }
        // CraftBukkit end
        MinecraftServer.LOGGER.info("Stopping server");
        MinecraftTimings.stopServer(); // Paper
        // CraftBukkit start
        if (this.server != null) {
            this.server.disablePlugins();
        }
        // CraftBukkit end
        if (this.getNetworkSystem() != null) {
            this.getNetworkSystem().terminateEndpoints();
        }

        if (this.playerList != null) {
            MinecraftServer.LOGGER.info("Saving players");
            this.playerList.saveAllPlayerData();
            this.playerList.u(isRestarting);
            try { Thread.sleep(100); } catch (InterruptedException ex) {} // CraftBukkit - SPIGOT-625 - give server at least a chance to send packets
        }

        if (this.worlds != null) {
            MinecraftServer.LOGGER.info("Saving worlds");
            WorldServer[] aworldserver = this.worlds;
            int i = aworldserver.length;

            int j;
            WorldServer worldserver;

            for (j = 0; j < i; ++j) {
                worldserver = aworldserver[j];
                if (worldserver != null) {
                    worldserver.disableLevelSaving = false;
                }
            }

            this.saveAllWorlds(false);
            aworldserver = this.worlds;
            i = aworldserver.length;

            /* CraftBukkit start - Handled in saveChunks
            for (j = 0; j < i; ++j) {
                worldserver = aworldserver[j];
                if (worldserver != null) {
                    worldserver.saveLevel();
                }
            }
            // CraftBukkit end */
        }

        if (this.usageSnooper.isSnooperRunning()) {
            this.usageSnooper.stopSnooper();
        }

        // Spigot start
        if (org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly) {
            LOGGER.info("Saving usercache.json");
            this.profileCache.c(false); // Paper
        }
        // Spigot end
    }

    public String getServerHostname() {
        return this.hostname;
    }

    public void setHostname(String s) {
        this.hostname = s;
    }

    public boolean isServerRunning() {
        return this.serverRunning;
    }

    // Paper start - allow passing of the intent to restart
    public void initiateShutdown() {
        safeShutdown(false);
    }

    public void safeShutdown(boolean isRestarting) {
        this.serverRunning = false;
        this.isRestarting = isRestarting;
    }

    // Paper end

    // Paper start - Further improve server tick loop
    private static final int TPS = 20;
    private static final long SEC_IN_NANO = 1000000000;
    public static final long TICK_TIME = SEC_IN_NANO / TPS;
    private static final long MAX_CATCHUP_BUFFER = TICK_TIME * TPS * 60L;
    private static final int SAMPLE_INTERVAL = 20;
    public final RollingAverage tps1 = new RollingAverage(60);
    public final RollingAverage tps5 = new RollingAverage(60 * 5);
    public final RollingAverage tps15 = new RollingAverage(60 * 15);
    public double[] recentTps = new double[3]; // Paper - Fine have your darn compat with bad plugins

    public static class RollingAverage {
        private final int size;
        private long time;
        private double total;
        private int index = 0;
        private final double[] samples;
        private final long[] times;

        RollingAverage(int size) {
            this.size = size;
            this.time = size * SEC_IN_NANO;
            this.total = TPS * SEC_IN_NANO * size;
            this.samples = new double[size];
            this.times = new long[size];
            for (int i = 0; i < size; i++) {
                this.samples[i] = TPS;
                this.times[i] = SEC_IN_NANO;
            }
        }

        public void add(double x, long t) {
            time -= times[index];
            total -= samples[index] * times[index];
            samples[index] = x;
            times[index] = t;
            time += t;
            total += x * t;
            if (++index == size) {
                index = 0;
            }
        }

        public double getAverage() {
            return total / time;
        }
    }
    // Paper End

    @Override
    public void run() {
        try {
            if (this.init()) {
                this.currentTime = getCurrentTimeMillis();
                long i = 0L;

                this.statusResponse.setServerDescription(new TextComponentString(this.motd));
                this.statusResponse.setVersion(new ServerStatusResponse.Version("1.12.2", 340));
                this.applyServerIconToResponse(this.statusResponse);

                // Spigot start
                Arrays.fill( recentTps, 20 );
                long start = System.nanoTime(), lastTick = start - TICK_TIME, catchupTime = 0, curTime, wait, tickSection = start; // Paper - Further improve server tick loop
                while (this.serverRunning) {
                    curTime = System.nanoTime();
                    // Paper start - Further improve server tick loop
                    wait = TICK_TIME - (curTime - lastTick);
                    if (wait > 0) {
                        if (catchupTime < 2E6) {
                            wait += Math.abs(catchupTime);
                        } else if (wait < catchupTime) {
                            catchupTime -= wait;
                            wait = 0;
                        } else {
                            wait -= catchupTime;
                            catchupTime = 0;
                        }
                    }
                    if (wait > 0) {
                        Thread.sleep(wait / 1000000);
                        curTime = System.nanoTime();
                        wait = TICK_TIME - (curTime - lastTick);
                    }

                    catchupTime = Math.min(MAX_CATCHUP_BUFFER, catchupTime - wait);
                    if ( ++MinecraftServer.currentTick % SAMPLE_INTERVAL == 0 )
                    {
                        final long diff = curTime - tickSection;
                        double currentTps = 1E9 / diff * SAMPLE_INTERVAL;
                        tps1.add(currentTps, diff);
                        tps5.add(currentTps, diff);
                        tps15.add(currentTps, diff);
                        // Backwards compat with bad plugins
                        recentTps[0] = tps1.getAverage();
                        recentTps[1] = tps5.getAverage();
                        recentTps[2] = tps15.getAverage();
                        // Paper end
                        tickSection = curTime;
                    }
                    lastTick = curTime;

                    this.tick();
                    this.serverIsRunning = true;
                }
                // Spigot end
            } else {
                this.finalTick((CrashReport) null);
            }
        } catch (Throwable throwable) {
            MinecraftServer.LOGGER.error("Encountered an unexpected exception", throwable);
            // Spigot Start
            if ( throwable.getCause() != null )
            {
                MinecraftServer.LOGGER.error( "\tCause of unexpected exception was", throwable.getCause() );
            }
            // Spigot End
            CrashReport crashreport = null;

            if (throwable instanceof ReportedException) {
                crashreport = this.addServerInfoToCrashReport(((ReportedException) throwable).getCrashReport());
            } else {
                crashreport = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable));
            }

            File file = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.saveToFile(file)) {
                MinecraftServer.LOGGER.error("This crash report has been saved to: {}", file.getAbsolutePath());
            } else {
                MinecraftServer.LOGGER.error("We were unable to save this crash report to disk.");
            }

            this.finalTick(crashreport);
        } finally {
            try {
                org.spigotmc.WatchdogThread.doStop();
                this.serverStopped = true;
                this.stopServer();
            } catch (Throwable throwable1) {
                MinecraftServer.LOGGER.error("Exception stopping the server", throwable1);
            } finally {
                // CraftBukkit start - Restore terminal to original settings
                try {
                    net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Paper - Use TerminalConsoleAppender
                } catch (Exception ignored) {
                }
                // CraftBukkit end
                this.systemExitNow();
            }

        }

    }

    public void applyServerIconToResponse(ServerStatusResponse serverping) {
        File file = this.getFile("server-icon.png");

        if (!file.exists()) {
            file = this.getActiveAnvilConverter().getFile(this.getFolderName(), "icon.png");
        }

        if (file.isFile()) {
            ByteBuf bytebuf = Unpooled.buffer();

            try {
                BufferedImage bufferedimage = ImageIO.read(file);

                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuf bytebuf1 = Base64.encode(bytebuf);

                serverping.setFavicon("data:image/png;base64," + bytebuf1.toString(StandardCharsets.UTF_8));
            } catch (Exception exception) {
                MinecraftServer.LOGGER.error("Couldn\'t load server icon", exception);
            } finally {
                bytebuf.release();
            }
        }

    }

    public File getDataDirectory() {
        return new File(".");
    }

    protected void finalTick(CrashReport crashreport) {}

    public void systemExitNow() {}

    protected void tick() throws MinecraftException { // CraftBukkit - added throws
        co.aikar.timings.TimingsManager.FULL_SERVER_TICK.startTiming(); // Paper
        this.slackActivityAccountant.tickStarted(); // Spigot
        long i = System.nanoTime(); long startTime = i; // Paper

        ++this.tickCounter;
        if (this.startProfiling) {
            this.startProfiling = false;
            this.profiler.profilingEnabled = true;
            this.profiler.clearProfiling();
        }

        this.profiler.startSection("root");
        this.updateTimeLightAndEntities();
        if (i - this.nanoTimeSinceStatusRefresh >= 5000000000L) {
            this.nanoTimeSinceStatusRefresh = i;
            this.statusResponse.setPlayers(new ServerStatusResponse.Players(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.getCurrentPlayerCount(), org.spigotmc.SpigotConfig.playerSample)]; // Paper
            int j = MathHelper.getInt(this.random, 0, this.getCurrentPlayerCount() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k) {
                agameprofile[k] = this.playerList.getPlayers().get(j + k).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            this.statusResponse.getPlayers().setPlayers(agameprofile);
        }

            this.profiler.startSection("save");

        serverAutoSave = (autosavePeriod > 0 && this.tickCounter % autosavePeriod == 0); // Paper
        int playerSaveInterval = com.destroystokyo.paper.PaperConfig.playerAutoSaveRate;
        if (playerSaveInterval < 0) {
            playerSaveInterval = autosavePeriod;
        }
        if (playerSaveInterval > 0) { // CraftBukkit // Paper
            this.playerList.savePlayers(playerSaveInterval);
            // Spigot Start
        } // Paper - Incremental Auto Saving

            // We replace this with saving each individual world as this.saveChunks(...) is broken,
            // and causes the main thread to sleep for random amounts of time depending on chunk activity
            // Also pass flag to only save modified chunks
            server.playerCommandState = true;
            for (World world : worlds) {
                if (world.paperConfig.autoSavePeriod > 0) world.getWorld().save(false); // Paper - Incremental / Configurable Auto Saving
            }
            server.playerCommandState = false;
            // this.saveChunks(true);
            // Spigot End
            this.profiler.endSection();
        //} // Paper - Incremental Auto Saving

        this.profiler.startSection("tallying");
        // Spigot start
        long tickNanos;
        this.tickTimeArray[this.tickCounter % 100] = tickNanos = System.nanoTime() - i;
        // Spigot end
        this.profiler.endSection();
        this.profiler.startSection("snooper");
        if (isSnooperEnabled() && !this.usageSnooper.isSnooperRunning() && this.tickCounter > 100) {  // Spigot
            this.usageSnooper.startSnooper();
        }

        if (isSnooperEnabled() && this.tickCounter % 6000 == 0) { // Spigot
            this.usageSnooper.addMemoryStatsToSnooper();
        }

        this.profiler.endSection();
        this.profiler.endSection();

        org.spigotmc.WatchdogThread.tick(); // Spigot
        PaperLightingQueue.processQueue(startTime); // Paper
        this.slackActivityAccountant.tickEnded(tickNanos); // Spigot
        co.aikar.timings.TimingsManager.FULL_SERVER_TICK.stopTiming(); // Paper
    }

    public void updateTimeLightAndEntities() {
        MinecraftTimings.bukkitSchedulerTimer.startTiming(); // Paper
        this.server.getScheduler().mainThreadHeartbeat(this.tickCounter); // CraftBukkit
        MinecraftTimings.bukkitSchedulerTimer.stopTiming(); // Paper
        MinecraftTimings.minecraftSchedulerTimer.startTiming(); // Paper
        this.profiler.startSection("jobs");
        Queue queue = this.futureTaskQueue;

        // Spigot start
        FutureTask<?> entry;
        int count = this.futureTaskQueue.size();
        while (count-- > 0 && (entry = this.futureTaskQueue.poll()) != null) {
            Util.runTask(entry, MinecraftServer.LOGGER);
         }
        // Spigot end
        MinecraftTimings.minecraftSchedulerTimer.stopTiming(); // Paper

        this.profiler.endStartSection("levels");

        // CraftBukkit start
        // Run tasks that are waiting on processing
        MinecraftTimings.processQueueTimer.startTiming(); // Spigot
        while (!processQueue.isEmpty()) {
            processQueue.remove().run();
        }
        MinecraftTimings.processQueueTimer.stopTiming(); // Spigot

        MinecraftTimings.chunkIOTickTimer.startTiming(); // Spigot
        org.bukkit.craftbukkit.chunkio.ChunkIOExecutor.tick();
        MinecraftTimings.chunkIOTickTimer.stopTiming(); // Spigot

        MinecraftTimings.timeUpdateTimer.startTiming(); // Spigot
        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (this.tickCounter % 20 == 0) {
            for (int i = 0; i < this.getPlayerList().playerEntityList.size(); ++i) {
                EntityPlayerMP entityplayer = this.getPlayerList().playerEntityList.get(i);
                entityplayer.connection.sendPacket(new SPacketTimeUpdate(entityplayer.world.getTotalWorldTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean("doDaylightCycle"))); // Add support for per player time
            }
        }
        MinecraftTimings.timeUpdateTimer.stopTiming(); // Spigot

        int i;

        for (i = 0; i < this.worlds.size(); ++i) { // CraftBukkit
            long j = System.nanoTime();

            // if (i == 0 || this.getAllowNether()) {
                WorldServer worldserver = this.worlds.get(i);
                TileEntityHopper.skipHopperEvents = worldserver.paperConfig.disableHopperMoveEvents || org.bukkit.event.inventory.InventoryMoveItemEvent.getHandlerList().getRegisteredListeners().length == 0;

                this.profiler.func_194340_a(() -> {
                    return worldserver.getWorldInfo().getWorldName();
                });
                /* Drop global time updates
                if (this.ticks % 20 == 0) {
                    this.methodProfiler.a("timeSync");
                    this.v.a((Packet) (new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean("doDaylightCycle"))), worldserver.worldProvider.getDimensionManager().getDimensionID());
                    this.methodProfiler.b();
                }
                // CraftBukkit end */

                this.profiler.startSection("tick");

                CrashReport crashreport;

                try {
                    worldserver.timings.doTick.startTiming(); // Spigot
                    worldserver.tick();
                    worldserver.timings.doTick.stopTiming(); // Spigot
                } catch (Throwable throwable) {
                    // Spigot Start
                    try {
                    crashreport = CrashReport.makeCrashReport(throwable, "Exception ticking world");
                    } catch (Throwable t){
                        throw new RuntimeException("Error generating crash report", t);
                    }
                    // Spigot End
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                try {
                    worldserver.timings.tickEntities.startTiming(); // Spigot
                    worldserver.updateEntities();
                    worldserver.timings.tickEntities.stopTiming(); // Spigot
                } catch (Throwable throwable1) {
                    // Spigot Start
                    try {
                    crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world entities");
                    } catch (Throwable t){
                        throw new RuntimeException("Error generating crash report", t);
                    }
                    // Spigot End
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                this.profiler.endSection();
                this.profiler.startSection("tracker");
                worldserver.getEntityTracker().tick();
                this.profiler.endSection();
                this.profiler.endSection();
                worldserver.explosionDensityCache.clear(); // Paper - Optimize explosions
            // } // CraftBukkit

            // this.i[i][this.ticks % 100] = System.nanoTime() - j; // CraftBukkit
        }

        this.profiler.endStartSection("connection");
        MinecraftTimings.connectionTimer.startTiming(); // Spigot
        this.getNetworkSystem().networkTick();
        MinecraftTimings.connectionTimer.stopTiming(); // Spigot
        this.profiler.endStartSection("players");
        MinecraftTimings.playerListTimer.startTiming(); // Spigot
        this.playerList.onTick();
        MinecraftTimings.playerListTimer.stopTiming(); // Spigot
        this.profiler.endStartSection("commandFunctions");
        MinecraftTimings.commandFunctionsTimer.startTiming(); // Spigot
        this.getFunctionManager().update();
        MinecraftTimings.commandFunctionsTimer.stopTiming(); // Spigot
        this.profiler.endStartSection("tickables");

        MinecraftTimings.tickablesTimer.startTiming(); // Spigot
        for (i = 0; i < this.tickables.size(); ++i) {
            this.tickables.get(i).update();
        }
        MinecraftTimings.tickablesTimer.stopTiming(); // Spigot

        this.profiler.endSection();
    }

    public boolean getAllowNether() {
        return true;
    }

    public void registerTickable(ITickable itickable) {
        this.tickables.add(itickable);
    }

    public static void main(final OptionSet options) { // CraftBukkit - replaces main(String[] astring)
        Bootstrap.register();

        try {
            /* CraftBukkit start - Replace everything
            boolean flag = true;
            String s = null;
            String s1 = ".";
            String s2 = null;
            boolean flag1 = false;
            boolean flag2 = false;
            int i = -1;

            for (int j = 0; j < astring.length; ++j) {
                String s3 = astring[j];
                String s4 = j == astring.length - 1 ? null : astring[j + 1];
                boolean flag3 = false;

                if (!"nogui".equals(s3) && !"--nogui".equals(s3)) {
                    if ("--port".equals(s3) && s4 != null) {
                        flag3 = true;

                        try {
                            i = Integer.parseInt(s4);
                        } catch (NumberFormatException numberformatexception) {
                            ;
                        }
                    } else if ("--singleplayer".equals(s3) && s4 != null) {
                        flag3 = true;
                        s = s4;
                    } else if ("--universe".equals(s3) && s4 != null) {
                        flag3 = true;
                        s1 = s4;
                    } else if ("--world".equals(s3) && s4 != null) {
                        flag3 = true;
                        s2 = s4;
                    } else if ("--demo".equals(s3)) {
                        flag1 = true;
                    } else if ("--bonusChest".equals(s3)) {
                        flag2 = true;
                    }
                } else {
                    flag = false;
                }

                if (flag3) {
                    ++j;
                }
            }
            */ // CraftBukkit end

            String s1 = "."; // PAIL?
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new com.destroystokyo.paper.profile.PaperAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()); // Paper
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache usercache = new PlayerProfileCache(gameprofilerepository, new File(s1, MinecraftServer.USER_CACHE_FILE.getName()));
            final DedicatedServer dedicatedserver = new DedicatedServer(options, DataFixesManager.createFixer(), yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache);

            /* CraftBukkit start
            if (s != null) {
                dedicatedserver.i(s);
            }

            if (s2 != null) {
                dedicatedserver.setWorld(s2);
            }

            if (i >= 0) {
                dedicatedserver.setPort(i);
            }

            if (flag1) {
                dedicatedserver.b(true);
            }

            if (flag2) {
                dedicatedserver.c(true);
            }

            if (flag && !GraphicsEnvironment.isHeadless()) {
                dedicatedserver.aR();
            }

            dedicatedserver.F();
            Runtime.getRuntime().addShutdownHook(new Thread("Server Shutdown Thread") {
                public void run() {
                    dedicatedserver.stop();
                }
            });
            */

            if (options.has("port")) {
                int port = (Integer) options.valueOf("port");
                if (port > 0) {
                    dedicatedserver.setServerPort(port);
                }
            }

            if (options.has("universe")) {
                dedicatedserver.anvilFile = (File) options.valueOf("universe");
            }

            if (options.has("world")) {
                dedicatedserver.setFolderName((String) options.valueOf("world"));
            }

            dedicatedserver.primaryThread.start();
            // CraftBukkit end
        } catch (Exception exception) {
            MinecraftServer.LOGGER.fatal("Failed to start the minecraft server", exception);
        }

    }

    public void startServerThread() {
        /* CraftBukkit start - prevent abuse
        this.serverThread = new Thread(this, "Server thread");
        this.serverThread.start();
        // CraftBukkit end */
    }

    public File getFile(String s) {
        return new File(this.getDataDirectory(), s);
    }

    public void logInfo(String s) {
        MinecraftServer.LOGGER.info(s);
    }

    public void logWarning(String s) {
        MinecraftServer.LOGGER.warn(s);
    }

    public WorldServer getWorld(int i) {
        // CraftBukkit start
        for (WorldServer world : worlds) {
            if (world.dimension == i) {
                return world;
            }
        }
        return worlds.get(0);
        // CraftBukkit end
    }

    public String getMinecraftVersion() {
        return "1.12.2";
    }

    public int getPlayerCount() { return getCurrentPlayerCount(); } // Paper - OBFHELPER
    public int getCurrentPlayerCount() {
        return this.playerList.getCurrentPlayerCount();
    }

    public int getMaxPlayers() { return getMaxPlayers(); } // Paper - OBFHELPER
    public int getMaxPlayers() {
        return this.playerList.getMaxPlayers();
    }

    public String[] getOnlinePlayerNames() {
        return this.playerList.getOnlinePlayerNames();
    }

    public GameProfile[] getOnlinePlayerProfiles() {
        return this.playerList.getOnlinePlayerProfiles();
    }

    public boolean isDebuggingEnabled() {
        return this.getPropertyManager().getBooleanProperty("debug", false); // CraftBukkit - don't hardcode
    }

    public void logSevere(String s) {
        MinecraftServer.LOGGER.error(s);
    }

    public void logDebug(String s) {
        if (this.isDebuggingEnabled()) {
            MinecraftServer.LOGGER.info(s);
        }

    }

    public String getServerModName() {
        return "Paper"; //Paper - Paper > // Spigot - Spigot > // CraftBukkit - cb > vanilla!
    }

    public CrashReport addServerInfoToCrashReport(CrashReport crashreport) {
        crashreport.getCategory().addDetail("Profiler Position", new ICrashReportDetail() {
            public String a() throws Exception {
                return MinecraftServer.this.profiler.profilingEnabled ? MinecraftServer.this.profiler.getNameOfLastSection() : "N/A (disabled)";
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        if (this.playerList != null) {
            crashreport.getCategory().addDetail("Player Count", new ICrashReportDetail() {
                @Override
                public String call() {
                    return MinecraftServer.this.playerList.getCurrentPlayerCount() + " / " + MinecraftServer.this.playerList.getMaxPlayers() + "; " + MinecraftServer.this.playerList.getPlayers();
                }

                public Object call() throws Exception {
                    return this.call();
                }
            });
        }

        return crashreport;
    }

    public List<String> getTabCompletions(ICommandSender icommandlistener, String s, @Nullable BlockPos blockposition, boolean flag) {
        /* CraftBukkit start - Allow tab-completion of Bukkit commands
        ArrayList arraylist = Lists.newArrayList();
        boolean flag1 = s.startsWith("/");

        if (flag1) {
            s = s.substring(1);
        }

        if (!flag1 && !flag) {
            String[] astring = s.split(" ", -1);
            String s1 = astring[astring.length - 1];
            String[] astring1 = this.v.f();
            int i = astring1.length;

            for (int j = 0; j < i; ++j) {
                String s2 = astring1[j];

                if (CommandAbstract.a(s1, s2)) {
                    arraylist.add(s2);
                }
            }

            return arraylist;
        } else {
            boolean flag2 = !s.contains(" ");
            List list = this.b.a(icommandlistener, s, blockposition);

            if (!list.isEmpty()) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    String s3 = (String) iterator.next();

                    if (flag2 && !flag) {
                        arraylist.add("/" + s3);
                    } else {
                        arraylist.add(s3);
                    }
                }
            }

            return arraylist;
        }
        */
        return server.tabComplete(icommandlistener, s, blockposition, flag);
        // CraftBukkit end
    }

    public boolean isAnvilFileSet() {
        return true; // CraftBukkit
    }

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public void sendMessage(ITextComponent ichatbasecomponent) {
        // Paper - Log message with colors
        MinecraftServer.LOGGER.info(org.bukkit.craftbukkit.util.CraftChatMessage.fromComponent(ichatbasecomponent, minecraft.util.text.TextFormatting.WHITE));
    }

    @Override
    public boolean canUseCommand(int i, String s) {
        return true;
    }

    public ICommandManager getCommandManager() {
        return this.commandManager;
    }

    public KeyPair getKeyPair() {
        return this.serverKeyPair;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(int i) {
        this.serverPort = i;
    }

    public String getServerOwner() {
        return this.serverOwner;
    }

    public void setServerOwner(String s) {
        this.serverOwner = s;
    }

    public boolean isSinglePlayer() {
        return this.serverOwner != null;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String s) {
        this.folderName = s;
    }

    public void setKeyPair(KeyPair keypair) {
        this.serverKeyPair = keypair;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty enumdifficulty) {
        // CraftBukkit start
        // WorldServer[] aworldserver = this.worldServer;
        int i = this.worlds.size();

        for (int j = 0; j < i; ++j) {
            WorldServer worldserver = this.worlds.get(j);
            // CraftBukkit end

            if (worldserver != null) {
                if (worldserver.getWorldInfo().isHardcoreModeEnabled()) {
                    worldserver.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    worldserver.setAllowedSpawnTypes(true, true);
                } else if (this.isSinglePlayer()) {
                    worldserver.getWorldInfo().setDifficulty(enumdifficulty);
                    worldserver.setAllowedSpawnTypes(worldserver.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                } else {
                    worldserver.getWorldInfo().setDifficulty(enumdifficulty);
                    worldserver.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
        }

    }

    public boolean allowSpawnMonsters() {
        return true;
    }

    public boolean isDemo() {
        return this.isDemo;
    }

    public void setDemo(boolean flag) {
        this.isDemo = flag;
    }

    public void canCreateBonusChest(boolean flag) {
        this.enableBonusChest = flag;
    }

    public ISaveFormat getActiveAnvilConverter() {
        return this.anvilConverterForAnvilFile;
    }

    public String getResourcePackUrl() {
        return this.resourcePackUrl;
    }

    public String getResourcePackHash() {
        return this.resourcePackHash;
    }

    public void setResourcePack(String s, String s1) {
        this.resourcePackUrl = s;
        this.resourcePackHash = s1;
    }

    @Override
    public void addServerStatsToSnooper(Snooper mojangstatisticsgenerator) {
        mojangstatisticsgenerator.addClientStat("whitelist_enabled", Boolean.valueOf(false));
        mojangstatisticsgenerator.addClientStat("whitelist_count", Integer.valueOf(0));
        if (this.playerList != null) {
            mojangstatisticsgenerator.addClientStat("players_current", Integer.valueOf(this.getCurrentPlayerCount()));
            mojangstatisticsgenerator.addClientStat("players_max", Integer.valueOf(this.getMaxPlayers()));
            mojangstatisticsgenerator.addClientStat("players_seen", Integer.valueOf(this.playerList.getAvailablePlayerDat().length));
        }

        mojangstatisticsgenerator.addClientStat("uses_auth", Boolean.valueOf(this.onlineMode));
        mojangstatisticsgenerator.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        mojangstatisticsgenerator.addClientStat("run_time", Long.valueOf((getCurrentTimeMillis() - mojangstatisticsgenerator.getMinecraftStartTimeMillis()) / 60L * 1000L));
        mojangstatisticsgenerator.addClientStat("avg_tick_ms", Integer.valueOf((int) (MathHelper.average(this.tickTimeArray) * 1.0E-6D)));
        int i = 0;

        if (this.worlds != null) {
            // CraftBukkit start
            for (int j = 0; j < this.worlds.size(); ++j) {
                WorldServer worldserver = this.worlds.get(j);
                if (worldserver != null) {
                    // CraftBukkit end
                    WorldInfo worlddata = worldserver.getWorldInfo();

                    mojangstatisticsgenerator.addClientStat("world[" + i + "][dimension]", Integer.valueOf(worldserver.provider.getDimensionType().getId()));
                    mojangstatisticsgenerator.addClientStat("world[" + i + "][mode]", worlddata.getGameType());
                    mojangstatisticsgenerator.addClientStat("world[" + i + "][difficulty]", worldserver.getDifficulty());
                    mojangstatisticsgenerator.addClientStat("world[" + i + "][hardcore]", Boolean.valueOf(worlddata.isHardcoreModeEnabled()));
                    mojangstatisticsgenerator.addClientStat("world[" + i + "][generator_name]", worlddata.getTerrainType().getName());
                    mojangstatisticsgenerator.addClientStat("world[" + i + "][generator_version]", Integer.valueOf(worlddata.getTerrainType().getVersion()));
                    mojangstatisticsgenerator.addClientStat("world[" + i + "][height]", Integer.valueOf(this.buildLimit));
                    mojangstatisticsgenerator.addClientStat("world[" + i + "][chunks_loaded]", Integer.valueOf(worldserver.getChunkProvider().getLoadedChunkCount()));
                    ++i;
                }
            }
        }

        mojangstatisticsgenerator.addClientStat("worlds", Integer.valueOf(i));
    }

    @Override
    public void addServerTypeToSnooper(Snooper mojangstatisticsgenerator) {
        mojangstatisticsgenerator.addStatToSnooper("singleplayer", Boolean.valueOf(this.isSinglePlayer()));
        mojangstatisticsgenerator.addStatToSnooper("server_brand", this.getServerModName());
        mojangstatisticsgenerator.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        mojangstatisticsgenerator.addStatToSnooper("dedicated", Boolean.valueOf(this.isDedicatedServer()));
    }

    @Override
    public boolean isSnooperEnabled() {
        return true;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode() {
        return server.getOnlineMode(); // CraftBukkit
    }

    public void setOnlineMode(boolean flag) {
        this.onlineMode = flag;
    }

    public boolean getPreventProxyConnections() {
        return this.preventProxyConnections;
    }

    public void setPreventProxyConnections(boolean flag) {
        this.preventProxyConnections = flag;
    }

    public boolean getCanSpawnAnimals() {
        return this.canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean flag) {
        this.canSpawnAnimals = flag;
    }

    public boolean getCanSpawnNPCs() {
        return this.canSpawnNPCs;
    }

    public abstract boolean shouldUseNativeTransport();

    public void setCanSpawnNPCs(boolean flag) {
        this.canSpawnNPCs = flag;
    }

    public boolean isPVPEnabled() {
        return this.pvpEnabled;
    }

    public void setAllowPvp(boolean flag) {
        this.pvpEnabled = flag;
    }

    public boolean isFlightAllowed() {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean flag) {
        this.allowFlight = flag;
    }

    public abstract boolean isCommandBlockEnabled();

    public String getMOTD() {
        return this.motd;
    }

    public void setMOTD(String s) {
        this.motd = s;
    }

    public int getBuildLimit() {
        return this.buildLimit;
    }

    public void setBuildLimit(int i) {
        this.buildLimit = i;
    }

    public boolean isServerStopped() {
        return this.serverStopped;
    }

    public PlayerList getPlayerList() {
        return this.playerList;
    }

    public void setPlayerList(PlayerList playerlist) {
        this.playerList = playerlist;
    }

    public void setGameType(GameType enumgamemode) {
        // CraftBukkit start
        for (int i = 0; i < this.worlds.size(); ++i) {
            worlds.get(i).getWorldInfo().setGameType(enumgamemode);
        }

    }

    // Spigot Start
    public NetworkSystem getServerConnection()
    {
        return this.networkSystem;
    }
    // Spigot End
    public NetworkSystem getNetworkSystem() {
        return this.networkSystem == null ? this.networkSystem = new NetworkSystem(this) : this.networkSystem; // Spigot
    }

    public boolean getGuiEnabled() {
        return false;
    }

    public abstract String shareToLAN(GameType enumgamemode, boolean flag);

    public int getTickCounter() {
        return this.tickCounter;
    }

    public void enableProfiling() {
        this.startProfiling = true;
    }

    @Override
    public World getEntityWorld() {
        return this.worlds.get(0); // CraftBukkit
    }

    public int getSpawnProtectionSize() {
        return 16;
    }

    public boolean isBlockProtected(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        return false;
    }

    public void setForceGamemode(boolean flag) {
        this.isGamemodeForced = flag;
    }

    public boolean getForceGamemode() {
        return this.isGamemodeForced;
    }

    public Proxy getServerProxy() {
        return this.serverProxy;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public int getMaxPlayerIdleMinutes() {
        return this.maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int i) {
        this.maxPlayerIdleMinutes = i;
    }

    public MinecraftSessionService getSessionService() { return getMinecraftSessionService(); } // Paper - OBFHELPER
    public MinecraftSessionService getMinecraftSessionService() {
        return this.sessionService;
    }

    public GameProfileRepository getGameProfileRepository() {
        return this.profileRepo;
    }

    public PlayerProfileCache getPlayerProfileCache() {
        return this.profileCache;
    }

    public ServerStatusResponse getServerStatusResponse() {
        return this.statusResponse;
    }

    public void refreshStatusNextTick() {
        this.nanoTimeSinceStatusRefresh = 0L;
    }

    @Nullable
    public Entity getEntityFromUuid(UUID uuid) {
        WorldServer[] aworldserver = this.worlds;
        int i = aworldserver.length;

        // CraftBukkit start
        for (int j = 0; j < worlds.size(); ++j) {
            WorldServer worldserver = worlds.get(j);
            // CraftBukkit end

            if (worldserver != null) {
                Entity entity = worldserver.getEntityFromUuid(uuid);

                if (entity != null) {
                    return entity;
                }
            }
        }

        return null;
    }

    @Override
    public boolean sendCommandFeedback() {
        return worlds.get(0).getGameRules().getBoolean("sendCommandFeedback");
    }

    // Akarin start
    /* @Override
    public MinecraftServer getServer() {
        return this;
    } */ // Akarin end

    public int getMaxWorldSize() {
        return 29999984;
    }

    public <V> ListenableFuture<V> callFromMainThread(Callable<V> callable) {
        Validate.notNull(callable);
        if (!this.isCallingFromMinecraftThread()) { // CraftBukkit && !this.isStopped()) {
            ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);
            Queue queue = this.futureTaskQueue;

            // Spigot start
            this.futureTaskQueue.add(listenablefuturetask);
            return listenablefuturetask;
            // Spigot end
        } else {
            try {
                return Futures.immediateFuture(callable.call());
            } catch (Exception exception) {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
    }

    @Override
    public ListenableFuture<Object> addScheduledTask(Runnable runnable) {
        Validate.notNull(runnable);
        return this.callFromMainThread(Executors.callable(runnable));
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.serverThread;
    }

    public int getNetworkCompressionThreshold() {
        return 256;
    }

    public long getCurrentTime() {
        return this.currentTime;
    }

    public final Thread getServerThread() { return this.getServerThread(); } // Paper - OBFHELPER
    public Thread getServerThread() {
        return this.serverThread;
    }

    public int getSpawnRadius(@Nullable WorldServer worldserver) {
        return worldserver != null ? worldserver.getGameRules().getInt("spawnRadius") : 10;
    }

    public AdvancementManager getAdvancementManager() {
        return this.worlds.get(0).getAdvancementManager(); // CraftBukkit
    }

    public FunctionManager getFunctionManager() {
        return this.worlds.get(0).getFunctionManager(); // CraftBukkit
    }

    public void reload() {
        if (this.isCallingFromMinecraftThread()) {
            this.getPlayerList().saveAllPlayerData();
            this.worlds.get(0).getLootTableManager().reloadLootTables(); // CraftBukkit
            this.getAdvancementManager().reload();
            this.getFunctionManager().reload();
            this.getPlayerList().reloadResources();
        } else {
            this.addScheduledTask(this::reload);
        }

    }

    // CraftBukkit start
    // @Deprecated // Akarin
    public static MinecraftServer getServer() {
        return SERVER;
    }
    // CraftBukkit end
}

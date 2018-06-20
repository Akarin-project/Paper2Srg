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
    public static final Logger field_147145_h = LogManager.getLogger();
    public static final File field_152367_a = new File("usercache.json");
    public ISaveFormat field_71310_m;
    private final Snooper field_71307_n = new Snooper("server", this, func_130071_aq());
    public File field_71308_o;
    private final List<ITickable> field_71322_p = Lists.newArrayList();
    public final ICommandManager field_71321_q;
    public final Profiler field_71304_b = new Profiler();
    private NetworkSystem field_147144_o; // Spigot
    private final ServerStatusResponse field_147147_p = new ServerStatusResponse();
    private final Random field_147146_q = new Random();
    public final DataFixer field_184112_s;
    private String field_71320_r;
    private int field_71319_s = -1;
    public WorldServer[] field_71305_c;
    private PlayerList field_71318_t;
    private boolean field_71317_u = true;
    private boolean isRestarting = false; // Paper - flag to signify we're attempting to restart
    private boolean field_71316_v;
    private int field_71315_w;
    protected final Proxy field_110456_c;
    public String field_71302_d;
    public int field_71303_e;
    private boolean field_71325_x;
    private boolean field_190519_A;
    private boolean field_71324_y;
    private boolean field_71323_z;
    private boolean field_71284_A;
    private boolean field_71285_B;
    private String field_71286_C;
    private int field_71280_D;
    private int field_143008_E;
    public final long[] field_71311_j = new long[100];
    public long[][] field_71312_k;
    private KeyPair field_71292_I;
    private String field_71293_J;
    private String field_71294_K;
    private boolean field_71288_M;
    private boolean field_71289_N;
    private String field_147141_M = "";
    private String field_175588_P = "";
    private boolean field_71296_Q;
    private long field_71299_R;
    private String field_71298_S;
    private boolean field_71295_T;
    private boolean field_104057_T;
    private final YggdrasilAuthenticationService field_152364_T;
    private final MinecraftSessionService field_147143_S;
    private final GameProfileRepository field_152365_W;
    private final PlayerProfileCache field_152366_X;
    private long field_147142_T;
    protected final Queue<FutureTask<?>> field_175589_i = new com.destroystokyo.paper.utils.CachedSizeConcurrentLinkedQueue<>(); // Spigot, PAIL: Rename // Paper - Make size() constant-time
    private Thread field_175590_aa;
    private long field_175591_ab = func_130071_aq();

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
        this.field_110456_c = proxy;
        this.field_152364_T = yggdrasilauthenticationservice;
        this.field_147143_S = minecraftsessionservice;
        this.field_152365_W = gameprofilerepository;
        this.field_152366_X = usercache;
        // this.universe = file; // CraftBukkit
        // this.p = new ServerConnection(this); // Spigot
        this.field_71321_q = this.func_175582_h();
        // this.convertable = new WorldLoaderServer(file); // CraftBukkit - moved to DedicatedServer.init
        this.field_184112_s = dataconvertermanager;
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

        this.field_175590_aa = primaryThread = new Thread(this, "Server thread"); // Moved from main
    }

    public abstract PropertyManager getPropertyManager();
    // CraftBukkit end

    protected ServerCommandManager func_175582_h() {
        return new ServerCommandManager(this);
    }

    public abstract boolean func_71197_b() throws IOException;

    protected void func_71237_c(String s) {
        if (this.func_71254_M().func_75801_b(s)) {
            MinecraftServer.field_147145_h.info("Converting map!");
            this.func_71192_d("menu.convertingLevel");
            this.func_71254_M().func_75805_a(s, new IProgressUpdate() {
                private long field_96245_b = System.currentTimeMillis();

                @Override
                public void func_73720_a(String s) {}

                @Override
                public void func_73718_a(int i) {
                    if (System.currentTimeMillis() - this.field_96245_b >= 1000L) {
                        this.field_96245_b = System.currentTimeMillis();
                        MinecraftServer.field_147145_h.info("Converting... {}%", Integer.valueOf(i));
                    }

                }

                @Override
                public void func_73719_c(String s) {}
            });
        }

    }

    protected synchronized void func_71192_d(String s) {
        this.field_71298_S = s;
    }

    public void func_71247_a(String s, String s1, long i, WorldType worldtype, String s2) {
        this.func_71237_c(s);
        this.func_71192_d("menu.loadingLevel");
        this.field_71305_c = new WorldServer[3];
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
                if (func_71255_r()) {
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
            WorldSettings worldsettings = new WorldSettings(i, this.func_71265_f(), this.func_71225_e(), this.func_71199_h(), worldtype);
            worldsettings.func_82750_a(s2);

            if (j == 0) {
                ISaveHandler idatamanager = new AnvilSaveHandler(server.getWorldContainer(), s1, true, this.field_184112_s);
                WorldInfo worlddata = idatamanager.func_75757_d();
                if (worlddata == null) {
                    worlddata = new WorldInfo(worldsettings, s1);
                }
                worlddata.checkName(s1); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
                if (this.func_71242_L()) {
                    world = (WorldServer) (new WorldServerDemo(this, idatamanager, worlddata, dimension, this.field_71304_b)).func_175643_b();
                } else {
                    world = (WorldServer) (new WorldServer(this, idatamanager, worlddata, dimension, this.field_71304_b, org.bukkit.World.Environment.getEnvironment(dimension), gen)).func_175643_b();
                }

                world.func_72963_a(worldsettings);
                this.server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(this, world.func_96441_U());
            } else {
                String dim = "DIM" + dimension;

                File newWorld = new File(new File(name), dim);
                File oldWorld = new File(new File(s), dim);

                if ((!newWorld.isDirectory()) && (oldWorld.isDirectory())) {
                    MinecraftServer.field_147145_h.info("---- Migration of old " + worldType + " folder required ----");
                    MinecraftServer.field_147145_h.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
                    MinecraftServer.field_147145_h.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
                    MinecraftServer.field_147145_h.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

                    if (newWorld.exists()) {
                        MinecraftServer.field_147145_h.warn("A file or folder already exists at " + newWorld + "!");
                        MinecraftServer.field_147145_h.info("---- Migration of old " + worldType + " folder failed ----");
                    } else if (newWorld.getParentFile().mkdirs()) {
                        if (oldWorld.renameTo(newWorld)) {
                            MinecraftServer.field_147145_h.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                            // Migrate world data too.
                            try {
                                com.google.common.io.Files.copy(new File(new File(s), "level.dat"), new File(new File(name), "level.dat"));
                                org.apache.commons.io.FileUtils.copyDirectory(new File(new File(s), "data"), new File(new File(name), "data"));
                            } catch (IOException exception) {
                                MinecraftServer.field_147145_h.warn("Unable to migrate world data.");
                            }
                            MinecraftServer.field_147145_h.info("---- Migration of old " + worldType + " folder complete ----");
                        } else {
                            MinecraftServer.field_147145_h.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                            MinecraftServer.field_147145_h.info("---- Migration of old " + worldType + " folder failed ----");
                        }
                    } else {
                        MinecraftServer.field_147145_h.warn("Could not create path for " + newWorld + "!");
                        MinecraftServer.field_147145_h.info("---- Migration of old " + worldType + " folder failed ----");
                    }
                }

                ISaveHandler idatamanager = new AnvilSaveHandler(server.getWorldContainer(), name, true, this.field_184112_s);
                // world =, b0 to dimension, s1 to name, added Environment and gen
                WorldInfo worlddata = idatamanager.func_75757_d();
                if (worlddata == null) {
                    worlddata = new WorldInfo(worldsettings, name);
                }
                worlddata.checkName(name); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
                world = (WorldServer) new WorldServerMulti(this, idatamanager, dimension, this.worlds.get(0), this.field_71304_b, worlddata, org.bukkit.World.Environment.getEnvironment(dimension), gen).func_175643_b();
            }

            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(world.getWorld()));

            world.func_72954_a(new ServerWorldEventHandler(this, world));
            if (!this.func_71264_H()) {
                world.func_72912_H().func_76060_a(this.func_71265_f());
            }

            worlds.add(world);
            func_184103_al().func_72364_a(worlds.toArray(new WorldServer[worlds.size()]));
        }
        // CraftBukkit end
        this.field_71318_t.func_72364_a(this.field_71305_c);
        this.func_147139_a(this.func_147135_j());
        this.func_71222_d();

        // Paper start - Handle collideRule team for player collision toggle
        final Scoreboard scoreboard = this.func_130014_f_().func_96441_U();
        final java.util.Collection<String> toRemove = scoreboard.func_96525_g().stream().filter(team -> team.func_96661_b().startsWith("collideRule_")).map(ScorePlayerTeam::func_96661_b).collect(java.util.stream.Collectors.toList());
        for (String teamName : toRemove) {
            scoreboard.func_96511_d(scoreboard.func_96508_e(teamName)); // Clean up after ourselves
        }

        if (!com.destroystokyo.paper.PaperConfig.enablePlayerCollisions) {
            this.func_184103_al().collideRuleTeamName = org.apache.commons.lang3.StringUtils.left("collideRule_" + this.func_130014_f_().field_73012_v.nextInt(), 16);
            ScorePlayerTeam collideTeam = scoreboard.func_96527_f(this.func_184103_al().collideRuleTeamName);
            collideTeam.func_98300_b(false); // Because we want to mimic them not being on a team at all
        }
        // Paper end
    }

    protected void func_71222_d() {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        int i = 0;

        this.func_71192_d("menu.generatingTerrain");
        boolean flag4 = false;

        // CraftBukkit start - fire WorldLoadEvent and handle whether or not to keep the spawn in memory
        for (int m = 0; m < worlds.size(); m++) {
            WorldServer worldserver = this.worlds.get(m);
            MinecraftServer.field_147145_h.info("Preparing start region for level " + m + " (Seed: " + worldserver.func_72905_C() + ")");

            if (!worldserver.getWorld().getKeepSpawnInMemory()) {
                continue;
            }

            BlockPos blockposition = worldserver.func_175694_M();
            long j = func_130071_aq();
            i = 0;

            // Paper start
            short radius = worldserver.paperConfig.keepLoadedRange;
            for (int k = -radius; k <= radius && this.func_71278_l(); k += 16) {
                for (int l = -radius; l <= radius && this.func_71278_l(); l += 16) {
            // Paper end
                    long i1 = func_130071_aq();

                    if (i1 - j > 1000L) {
                        this.func_71216_a_("Preparing spawn area", i * 100 / 625);
                        j = i1;
                    }

                    ++i;
                    worldserver.func_72863_F().func_186025_d(blockposition.func_177958_n() + k >> 4, blockposition.func_177952_p() + l >> 4);
                }
            }
        }

        for (WorldServer world : this.worlds) {
            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(world.getWorld()));
        }
        // CraftBukkit end
        this.func_71243_i();
    }

    protected void func_175584_a(String s, ISaveHandler idatamanager) {
        File file = new File(idatamanager.func_75765_b(), "resources.zip");

        if (file.isFile()) {
            try {
                this.func_180507_a_("level://" + URLEncoder.encode(s, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                MinecraftServer.field_147145_h.warn("Something went wrong url encoding {}", s);
            }
        }

    }

    public abstract boolean func_71225_e();

    public abstract GameType func_71265_f();

    public abstract EnumDifficulty func_147135_j();

    public abstract boolean func_71199_h();

    public abstract int func_110455_j();

    public abstract boolean func_181034_q();

    public abstract boolean func_183002_r();

    protected void func_71216_a_(String s, int i) {
        this.field_71302_d = s;
        this.field_71303_e = i;
        MinecraftServer.field_147145_h.info("{}: {}%", s, Integer.valueOf(i));
    }

    protected void func_71243_i() {
        this.field_71302_d = null;
        this.field_71303_e = 0;
        this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD); // CraftBukkit
    }

    protected void func_71267_a(boolean flag) {
        WorldServer[] aworldserver = this.field_71305_c;
        int i = aworldserver.length;

        // CraftBukkit start
        for (int j = 0; j < worlds.size(); ++j) {
            WorldServer worldserver = worlds.get(j);
            // CraftBukkit end

            if (worldserver != null) {
                if (!flag) {
                    MinecraftServer.field_147145_h.info("Saving chunks for level \'{}\'/{}", worldserver.func_72912_H().func_76065_j(), worldserver.field_73011_w.func_186058_p().func_186065_b());
                }

                try {
                    worldserver.func_73044_a(true, (IProgressUpdate) null);
                    worldserver.func_73041_k(); // CraftBukkit
                } catch (MinecraftException exceptionworldconflict) {
                    MinecraftServer.field_147145_h.warn(exceptionworldconflict.getMessage());
                }
            }
        }

    }

    // CraftBukkit start
    private boolean hasStopped = false;
    private final Object stopLock = new Object();
    // CraftBukkit end

    public void func_71260_j() throws MinecraftException { // CraftBukkit - added throws
        // CraftBukkit start - prevent double stopping on multiple threads
        synchronized(stopLock) {
            if (hasStopped) return;
            hasStopped = true;
        }
        // CraftBukkit end
        MinecraftServer.field_147145_h.info("Stopping server");
        MinecraftTimings.stopServer(); // Paper
        // CraftBukkit start
        if (this.server != null) {
            this.server.disablePlugins();
        }
        // CraftBukkit end
        if (this.func_147137_ag() != null) {
            this.func_147137_ag().func_151268_b();
        }

        if (this.field_71318_t != null) {
            MinecraftServer.field_147145_h.info("Saving players");
            this.field_71318_t.func_72389_g();
            this.field_71318_t.u(isRestarting);
            try { Thread.sleep(100); } catch (InterruptedException ex) {} // CraftBukkit - SPIGOT-625 - give server at least a chance to send packets
        }

        if (this.field_71305_c != null) {
            MinecraftServer.field_147145_h.info("Saving worlds");
            WorldServer[] aworldserver = this.field_71305_c;
            int i = aworldserver.length;

            int j;
            WorldServer worldserver;

            for (j = 0; j < i; ++j) {
                worldserver = aworldserver[j];
                if (worldserver != null) {
                    worldserver.field_73058_d = false;
                }
            }

            this.func_71267_a(false);
            aworldserver = this.field_71305_c;
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

        if (this.field_71307_n.func_76468_d()) {
            this.field_71307_n.func_76470_e();
        }

        // Spigot start
        if (org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly) {
            field_147145_h.info("Saving usercache.json");
            this.field_152366_X.c(false); // Paper
        }
        // Spigot end
    }

    public String func_71211_k() {
        return this.field_71320_r;
    }

    public void func_71189_e(String s) {
        this.field_71320_r = s;
    }

    public boolean func_71278_l() {
        return this.field_71317_u;
    }

    // Paper start - allow passing of the intent to restart
    public void func_71263_m() {
        safeShutdown(false);
    }

    public void safeShutdown(boolean isRestarting) {
        this.field_71317_u = false;
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
            if (this.func_71197_b()) {
                this.field_175591_ab = func_130071_aq();
                long i = 0L;

                this.field_147147_p.func_151315_a(new TextComponentString(this.field_71286_C));
                this.field_147147_p.func_151321_a(new ServerStatusResponse.Version("1.12.2", 340));
                this.func_184107_a(this.field_147147_p);

                // Spigot start
                Arrays.fill( recentTps, 20 );
                long start = System.nanoTime(), lastTick = start - TICK_TIME, catchupTime = 0, curTime, wait, tickSection = start; // Paper - Further improve server tick loop
                while (this.field_71317_u) {
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

                    this.func_71217_p();
                    this.field_71296_Q = true;
                }
                // Spigot end
            } else {
                this.func_71228_a((CrashReport) null);
            }
        } catch (Throwable throwable) {
            MinecraftServer.field_147145_h.error("Encountered an unexpected exception", throwable);
            // Spigot Start
            if ( throwable.getCause() != null )
            {
                MinecraftServer.field_147145_h.error( "\tCause of unexpected exception was", throwable.getCause() );
            }
            // Spigot End
            CrashReport crashreport = null;

            if (throwable instanceof ReportedException) {
                crashreport = this.func_71230_b(((ReportedException) throwable).func_71575_a());
            } else {
                crashreport = this.func_71230_b(new CrashReport("Exception in server tick loop", throwable));
            }

            File file = new File(new File(this.func_71238_n(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.func_147149_a(file)) {
                MinecraftServer.field_147145_h.error("This crash report has been saved to: {}", file.getAbsolutePath());
            } else {
                MinecraftServer.field_147145_h.error("We were unable to save this crash report to disk.");
            }

            this.func_71228_a(crashreport);
        } finally {
            try {
                org.spigotmc.WatchdogThread.doStop();
                this.field_71316_v = true;
                this.func_71260_j();
            } catch (Throwable throwable1) {
                MinecraftServer.field_147145_h.error("Exception stopping the server", throwable1);
            } finally {
                // CraftBukkit start - Restore terminal to original settings
                try {
                    net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Paper - Use TerminalConsoleAppender
                } catch (Exception ignored) {
                }
                // CraftBukkit end
                this.func_71240_o();
            }

        }

    }

    public void func_184107_a(ServerStatusResponse serverping) {
        File file = this.func_71209_f("server-icon.png");

        if (!file.exists()) {
            file = this.func_71254_M().func_186352_b(this.func_71270_I(), "icon.png");
        }

        if (file.isFile()) {
            ByteBuf bytebuf = Unpooled.buffer();

            try {
                BufferedImage bufferedimage = ImageIO.read(file);

                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuf bytebuf1 = Base64.encode(bytebuf);

                serverping.func_151320_a("data:image/png;base64," + bytebuf1.toString(StandardCharsets.UTF_8));
            } catch (Exception exception) {
                MinecraftServer.field_147145_h.error("Couldn\'t load server icon", exception);
            } finally {
                bytebuf.release();
            }
        }

    }

    public File func_71238_n() {
        return new File(".");
    }

    protected void func_71228_a(CrashReport crashreport) {}

    public void func_71240_o() {}

    protected void func_71217_p() throws MinecraftException { // CraftBukkit - added throws
        co.aikar.timings.TimingsManager.FULL_SERVER_TICK.startTiming(); // Paper
        this.slackActivityAccountant.tickStarted(); // Spigot
        long i = System.nanoTime(); long startTime = i; // Paper

        ++this.field_71315_w;
        if (this.field_71295_T) {
            this.field_71295_T = false;
            this.field_71304_b.field_76327_a = true;
            this.field_71304_b.func_76317_a();
        }

        this.field_71304_b.func_76320_a("root");
        this.func_71190_q();
        if (i - this.field_147142_T >= 5000000000L) {
            this.field_147142_T = i;
            this.field_147147_p.func_151319_a(new ServerStatusResponse.Players(this.func_71275_y(), this.func_71233_x()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.func_71233_x(), org.spigotmc.SpigotConfig.playerSample)]; // Paper
            int j = MathHelper.func_76136_a(this.field_147146_q, 0, this.func_71233_x() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k) {
                agameprofile[k] = this.field_71318_t.func_181057_v().get(j + k).func_146103_bH();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            this.field_147147_p.func_151318_b().func_151330_a(agameprofile);
        }

            this.field_71304_b.func_76320_a("save");

        serverAutoSave = (autosavePeriod > 0 && this.field_71315_w % autosavePeriod == 0); // Paper
        int playerSaveInterval = com.destroystokyo.paper.PaperConfig.playerAutoSaveRate;
        if (playerSaveInterval < 0) {
            playerSaveInterval = autosavePeriod;
        }
        if (playerSaveInterval > 0) { // CraftBukkit // Paper
            this.field_71318_t.savePlayers(playerSaveInterval);
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
            this.field_71304_b.func_76319_b();
        //} // Paper - Incremental Auto Saving

        this.field_71304_b.func_76320_a("tallying");
        // Spigot start
        long tickNanos;
        this.field_71311_j[this.field_71315_w % 100] = tickNanos = System.nanoTime() - i;
        // Spigot end
        this.field_71304_b.func_76319_b();
        this.field_71304_b.func_76320_a("snooper");
        if (func_70002_Q() && !this.field_71307_n.func_76468_d() && this.field_71315_w > 100) {  // Spigot
            this.field_71307_n.func_76463_a();
        }

        if (func_70002_Q() && this.field_71315_w % 6000 == 0) { // Spigot
            this.field_71307_n.func_76471_b();
        }

        this.field_71304_b.func_76319_b();
        this.field_71304_b.func_76319_b();

        org.spigotmc.WatchdogThread.tick(); // Spigot
        PaperLightingQueue.processQueue(startTime); // Paper
        this.slackActivityAccountant.tickEnded(tickNanos); // Spigot
        co.aikar.timings.TimingsManager.FULL_SERVER_TICK.stopTiming(); // Paper
    }

    public void func_71190_q() {
        MinecraftTimings.bukkitSchedulerTimer.startTiming(); // Paper
        this.server.getScheduler().mainThreadHeartbeat(this.field_71315_w); // CraftBukkit
        MinecraftTimings.bukkitSchedulerTimer.stopTiming(); // Paper
        MinecraftTimings.minecraftSchedulerTimer.startTiming(); // Paper
        this.field_71304_b.func_76320_a("jobs");
        Queue queue = this.field_175589_i;

        // Spigot start
        FutureTask<?> entry;
        int count = this.field_175589_i.size();
        while (count-- > 0 && (entry = this.field_175589_i.poll()) != null) {
            Util.func_181617_a(entry, MinecraftServer.field_147145_h);
         }
        // Spigot end
        MinecraftTimings.minecraftSchedulerTimer.stopTiming(); // Paper

        this.field_71304_b.func_76318_c("levels");

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
        if (this.field_71315_w % 20 == 0) {
            for (int i = 0; i < this.func_184103_al().field_72404_b.size(); ++i) {
                EntityPlayerMP entityplayer = this.func_184103_al().field_72404_b.get(i);
                entityplayer.field_71135_a.func_147359_a(new SPacketTimeUpdate(entityplayer.field_70170_p.func_82737_E(), entityplayer.getPlayerTime(), entityplayer.field_70170_p.func_82736_K().func_82766_b("doDaylightCycle"))); // Add support for per player time
            }
        }
        MinecraftTimings.timeUpdateTimer.stopTiming(); // Spigot

        int i;

        for (i = 0; i < this.worlds.size(); ++i) { // CraftBukkit
            long j = System.nanoTime();

            // if (i == 0 || this.getAllowNether()) {
                WorldServer worldserver = this.worlds.get(i);
                TileEntityHopper.skipHopperEvents = worldserver.paperConfig.disableHopperMoveEvents || org.bukkit.event.inventory.InventoryMoveItemEvent.getHandlerList().getRegisteredListeners().length == 0;

                this.field_71304_b.func_194340_a(() -> {
                    return worldserver.func_72912_H().func_76065_j();
                });
                /* Drop global time updates
                if (this.ticks % 20 == 0) {
                    this.methodProfiler.a("timeSync");
                    this.v.a((Packet) (new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean("doDaylightCycle"))), worldserver.worldProvider.getDimensionManager().getDimensionID());
                    this.methodProfiler.b();
                }
                // CraftBukkit end */

                this.field_71304_b.func_76320_a("tick");

                CrashReport crashreport;

                try {
                    worldserver.timings.doTick.startTiming(); // Spigot
                    worldserver.func_72835_b();
                    worldserver.timings.doTick.stopTiming(); // Spigot
                } catch (Throwable throwable) {
                    // Spigot Start
                    try {
                    crashreport = CrashReport.func_85055_a(throwable, "Exception ticking world");
                    } catch (Throwable t){
                        throw new RuntimeException("Error generating crash report", t);
                    }
                    // Spigot End
                    worldserver.func_72914_a(crashreport);
                    throw new ReportedException(crashreport);
                }

                try {
                    worldserver.timings.tickEntities.startTiming(); // Spigot
                    worldserver.func_72939_s();
                    worldserver.timings.tickEntities.stopTiming(); // Spigot
                } catch (Throwable throwable1) {
                    // Spigot Start
                    try {
                    crashreport = CrashReport.func_85055_a(throwable1, "Exception ticking world entities");
                    } catch (Throwable t){
                        throw new RuntimeException("Error generating crash report", t);
                    }
                    // Spigot End
                    worldserver.func_72914_a(crashreport);
                    throw new ReportedException(crashreport);
                }

                this.field_71304_b.func_76319_b();
                this.field_71304_b.func_76320_a("tracker");
                worldserver.func_73039_n().func_72788_a();
                this.field_71304_b.func_76319_b();
                this.field_71304_b.func_76319_b();
                worldserver.explosionDensityCache.clear(); // Paper - Optimize explosions
            // } // CraftBukkit

            // this.i[i][this.ticks % 100] = System.nanoTime() - j; // CraftBukkit
        }

        this.field_71304_b.func_76318_c("connection");
        MinecraftTimings.connectionTimer.startTiming(); // Spigot
        this.func_147137_ag().func_151269_c();
        MinecraftTimings.connectionTimer.stopTiming(); // Spigot
        this.field_71304_b.func_76318_c("players");
        MinecraftTimings.playerListTimer.startTiming(); // Spigot
        this.field_71318_t.func_72374_b();
        MinecraftTimings.playerListTimer.stopTiming(); // Spigot
        this.field_71304_b.func_76318_c("commandFunctions");
        MinecraftTimings.commandFunctionsTimer.startTiming(); // Spigot
        this.func_193030_aL().func_73660_a();
        MinecraftTimings.commandFunctionsTimer.stopTiming(); // Spigot
        this.field_71304_b.func_76318_c("tickables");

        MinecraftTimings.tickablesTimer.startTiming(); // Spigot
        for (i = 0; i < this.field_71322_p.size(); ++i) {
            this.field_71322_p.get(i).func_73660_a();
        }
        MinecraftTimings.tickablesTimer.stopTiming(); // Spigot

        this.field_71304_b.func_76319_b();
    }

    public boolean func_71255_r() {
        return true;
    }

    public void func_82010_a(ITickable itickable) {
        this.field_71322_p.add(itickable);
    }

    public static void main(final OptionSet options) { // CraftBukkit - replaces main(String[] astring)
        Bootstrap.func_151354_b();

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
            PlayerProfileCache usercache = new PlayerProfileCache(gameprofilerepository, new File(s1, MinecraftServer.field_152367_a.getName()));
            final DedicatedServer dedicatedserver = new DedicatedServer(options, DataFixesManager.func_188279_a(), yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache);

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
                    dedicatedserver.func_71208_b(port);
                }
            }

            if (options.has("universe")) {
                dedicatedserver.field_71308_o = (File) options.valueOf("universe");
            }

            if (options.has("world")) {
                dedicatedserver.func_71261_m((String) options.valueOf("world"));
            }

            dedicatedserver.primaryThread.start();
            // CraftBukkit end
        } catch (Exception exception) {
            MinecraftServer.field_147145_h.fatal("Failed to start the minecraft server", exception);
        }

    }

    public void func_71256_s() {
        /* CraftBukkit start - prevent abuse
        this.serverThread = new Thread(this, "Server thread");
        this.serverThread.start();
        // CraftBukkit end */
    }

    public File func_71209_f(String s) {
        return new File(this.func_71238_n(), s);
    }

    public void func_71244_g(String s) {
        MinecraftServer.field_147145_h.info(s);
    }

    public void func_71236_h(String s) {
        MinecraftServer.field_147145_h.warn(s);
    }

    public WorldServer func_71218_a(int i) {
        // CraftBukkit start
        for (WorldServer world : worlds) {
            if (world.dimension == i) {
                return world;
            }
        }
        return worlds.get(0);
        // CraftBukkit end
    }

    public String func_71249_w() {
        return "1.12.2";
    }

    public int getPlayerCount() { return func_71233_x(); } // Paper - OBFHELPER
    public int func_71233_x() {
        return this.field_71318_t.func_72394_k();
    }

    public int getMaxPlayers() { return func_71275_y(); } // Paper - OBFHELPER
    public int func_71275_y() {
        return this.field_71318_t.func_72352_l();
    }

    public String[] func_71213_z() {
        return this.field_71318_t.func_72369_d();
    }

    public GameProfile[] func_152357_F() {
        return this.field_71318_t.func_152600_g();
    }

    public boolean func_71239_B() {
        return this.getPropertyManager().func_73670_a("debug", false); // CraftBukkit - don't hardcode
    }

    public void func_71201_j(String s) {
        MinecraftServer.field_147145_h.error(s);
    }

    public void func_71198_k(String s) {
        if (this.func_71239_B()) {
            MinecraftServer.field_147145_h.info(s);
        }

    }

    public String getServerModName() {
        return "Paper"; //Paper - Paper > // Spigot - Spigot > // CraftBukkit - cb > vanilla!
    }

    public CrashReport func_71230_b(CrashReport crashreport) {
        crashreport.func_85056_g().func_189529_a("Profiler Position", new ICrashReportDetail() {
            public String a() throws Exception {
                return MinecraftServer.this.field_71304_b.field_76327_a ? MinecraftServer.this.field_71304_b.func_76322_c() : "N/A (disabled)";
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        if (this.field_71318_t != null) {
            crashreport.func_85056_g().func_189529_a("Player Count", new ICrashReportDetail() {
                @Override
                public String call() {
                    return MinecraftServer.this.field_71318_t.func_72394_k() + " / " + MinecraftServer.this.field_71318_t.func_72352_l() + "; " + MinecraftServer.this.field_71318_t.func_181057_v();
                }
            });
        }

        return crashreport;
    }

    public List<String> func_184104_a(ICommandSender icommandlistener, String s, @Nullable BlockPos blockposition, boolean flag) {
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

    public boolean func_175578_N() {
        return true; // CraftBukkit
    }

    @Override
    public String func_70005_c_() {
        return "Server";
    }

    @Override
    public void func_145747_a(ITextComponent ichatbasecomponent) {
        // Paper - Log message with colors
        MinecraftServer.field_147145_h.info(org.bukkit.craftbukkit.util.CraftChatMessage.fromComponent(ichatbasecomponent, net.minecraft.util.text.TextFormatting.WHITE));
    }

    @Override
    public boolean func_70003_b(int i, String s) {
        return true;
    }

    public ICommandManager func_71187_D() {
        return this.field_71321_q;
    }

    public KeyPair func_71250_E() {
        return this.field_71292_I;
    }

    public int func_71215_F() {
        return this.field_71319_s;
    }

    public void func_71208_b(int i) {
        this.field_71319_s = i;
    }

    public String func_71214_G() {
        return this.field_71293_J;
    }

    public void func_71224_l(String s) {
        this.field_71293_J = s;
    }

    public boolean func_71264_H() {
        return this.field_71293_J != null;
    }

    public String func_71270_I() {
        return this.field_71294_K;
    }

    public void func_71261_m(String s) {
        this.field_71294_K = s;
    }

    public void func_71253_a(KeyPair keypair) {
        this.field_71292_I = keypair;
    }

    public void func_147139_a(EnumDifficulty enumdifficulty) {
        // CraftBukkit start
        // WorldServer[] aworldserver = this.worldServer;
        int i = this.worlds.size();

        for (int j = 0; j < i; ++j) {
            WorldServer worldserver = this.worlds.get(j);
            // CraftBukkit end

            if (worldserver != null) {
                if (worldserver.func_72912_H().func_76093_s()) {
                    worldserver.func_72912_H().func_176144_a(EnumDifficulty.HARD);
                    worldserver.func_72891_a(true, true);
                } else if (this.func_71264_H()) {
                    worldserver.func_72912_H().func_176144_a(enumdifficulty);
                    worldserver.func_72891_a(worldserver.func_175659_aa() != EnumDifficulty.PEACEFUL, true);
                } else {
                    worldserver.func_72912_H().func_176144_a(enumdifficulty);
                    worldserver.func_72891_a(this.func_71193_K(), this.field_71324_y);
                }
            }
        }

    }

    public boolean func_71193_K() {
        return true;
    }

    public boolean func_71242_L() {
        return this.field_71288_M;
    }

    public void func_71204_b(boolean flag) {
        this.field_71288_M = flag;
    }

    public void func_71194_c(boolean flag) {
        this.field_71289_N = flag;
    }

    public ISaveFormat func_71254_M() {
        return this.field_71310_m;
    }

    public String func_147133_T() {
        return this.field_147141_M;
    }

    public String func_175581_ab() {
        return this.field_175588_P;
    }

    public void func_180507_a_(String s, String s1) {
        this.field_147141_M = s;
        this.field_175588_P = s1;
    }

    @Override
    public void func_70000_a(Snooper mojangstatisticsgenerator) {
        mojangstatisticsgenerator.func_152768_a("whitelist_enabled", Boolean.valueOf(false));
        mojangstatisticsgenerator.func_152768_a("whitelist_count", Integer.valueOf(0));
        if (this.field_71318_t != null) {
            mojangstatisticsgenerator.func_152768_a("players_current", Integer.valueOf(this.func_71233_x()));
            mojangstatisticsgenerator.func_152768_a("players_max", Integer.valueOf(this.func_71275_y()));
            mojangstatisticsgenerator.func_152768_a("players_seen", Integer.valueOf(this.field_71318_t.func_72373_m().length));
        }

        mojangstatisticsgenerator.func_152768_a("uses_auth", Boolean.valueOf(this.field_71325_x));
        mojangstatisticsgenerator.func_152768_a("gui_state", this.func_71279_ae() ? "enabled" : "disabled");
        mojangstatisticsgenerator.func_152768_a("run_time", Long.valueOf((func_130071_aq() - mojangstatisticsgenerator.func_130105_g()) / 60L * 1000L));
        mojangstatisticsgenerator.func_152768_a("avg_tick_ms", Integer.valueOf((int) (MathHelper.func_76127_a(this.field_71311_j) * 1.0E-6D)));
        int i = 0;

        if (this.field_71305_c != null) {
            // CraftBukkit start
            for (int j = 0; j < this.worlds.size(); ++j) {
                WorldServer worldserver = this.worlds.get(j);
                if (worldserver != null) {
                    // CraftBukkit end
                    WorldInfo worlddata = worldserver.func_72912_H();

                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][dimension]", Integer.valueOf(worldserver.field_73011_w.func_186058_p().func_186068_a()));
                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][mode]", worlddata.func_76077_q());
                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][difficulty]", worldserver.func_175659_aa());
                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][hardcore]", Boolean.valueOf(worlddata.func_76093_s()));
                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][generator_name]", worlddata.func_76067_t().func_77127_a());
                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][generator_version]", Integer.valueOf(worlddata.func_76067_t().func_77131_c()));
                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][height]", Integer.valueOf(this.field_71280_D));
                    mojangstatisticsgenerator.func_152768_a("world[" + i + "][chunks_loaded]", Integer.valueOf(worldserver.func_72863_F().func_73152_e()));
                    ++i;
                }
            }
        }

        mojangstatisticsgenerator.func_152768_a("worlds", Integer.valueOf(i));
    }

    @Override
    public void func_70001_b(Snooper mojangstatisticsgenerator) {
        mojangstatisticsgenerator.func_152767_b("singleplayer", Boolean.valueOf(this.func_71264_H()));
        mojangstatisticsgenerator.func_152767_b("server_brand", this.getServerModName());
        mojangstatisticsgenerator.func_152767_b("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        mojangstatisticsgenerator.func_152767_b("dedicated", Boolean.valueOf(this.func_71262_S()));
    }

    @Override
    public boolean func_70002_Q() {
        return true;
    }

    public abstract boolean func_71262_S();

    public boolean func_71266_T() {
        return server.getOnlineMode(); // CraftBukkit
    }

    public void func_71229_d(boolean flag) {
        this.field_71325_x = flag;
    }

    public boolean func_190518_ac() {
        return this.field_190519_A;
    }

    public void func_190517_e(boolean flag) {
        this.field_190519_A = flag;
    }

    public boolean func_71268_U() {
        return this.field_71324_y;
    }

    public void func_71251_e(boolean flag) {
        this.field_71324_y = flag;
    }

    public boolean func_71220_V() {
        return this.field_71323_z;
    }

    public abstract boolean func_181035_ah();

    public void func_71257_f(boolean flag) {
        this.field_71323_z = flag;
    }

    public boolean func_71219_W() {
        return this.field_71284_A;
    }

    public void func_71188_g(boolean flag) {
        this.field_71284_A = flag;
    }

    public boolean func_71231_X() {
        return this.field_71285_B;
    }

    public void func_71245_h(boolean flag) {
        this.field_71285_B = flag;
    }

    public abstract boolean func_82356_Z();

    public String func_71273_Y() {
        return this.field_71286_C;
    }

    public void func_71205_p(String s) {
        this.field_71286_C = s;
    }

    public int func_71207_Z() {
        return this.field_71280_D;
    }

    public void func_71191_d(int i) {
        this.field_71280_D = i;
    }

    public boolean func_71241_aa() {
        return this.field_71316_v;
    }

    public PlayerList func_184103_al() {
        return this.field_71318_t;
    }

    public void func_184105_a(PlayerList playerlist) {
        this.field_71318_t = playerlist;
    }

    public void func_71235_a(GameType enumgamemode) {
        // CraftBukkit start
        for (int i = 0; i < this.worlds.size(); ++i) {
            worlds.get(i).func_72912_H().func_76060_a(enumgamemode);
        }

    }

    // Spigot Start
    public NetworkSystem getServerConnection()
    {
        return this.field_147144_o;
    }
    // Spigot End
    public NetworkSystem func_147137_ag() {
        return this.field_147144_o == null ? this.field_147144_o = new NetworkSystem(this) : this.field_147144_o; // Spigot
    }

    public boolean func_71279_ae() {
        return false;
    }

    public abstract String func_71206_a(GameType enumgamemode, boolean flag);

    public int func_71259_af() {
        return this.field_71315_w;
    }

    public void func_71223_ag() {
        this.field_71295_T = true;
    }

    @Override
    public World func_130014_f_() {
        return this.worlds.get(0); // CraftBukkit
    }

    public int func_82357_ak() {
        return 16;
    }

    public boolean func_175579_a(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        return false;
    }

    public void func_104055_i(boolean flag) {
        this.field_104057_T = flag;
    }

    public boolean func_104056_am() {
        return this.field_104057_T;
    }

    public Proxy func_110454_ao() {
        return this.field_110456_c;
    }

    public static long func_130071_aq() {
        return System.currentTimeMillis();
    }

    public int func_143007_ar() {
        return this.field_143008_E;
    }

    public void func_143006_e(int i) {
        this.field_143008_E = i;
    }

    public MinecraftSessionService getSessionService() { return func_147130_as(); } // Paper - OBFHELPER
    public MinecraftSessionService func_147130_as() {
        return this.field_147143_S;
    }

    public GameProfileRepository func_152359_aw() {
        return this.field_152365_W;
    }

    public PlayerProfileCache func_152358_ax() {
        return this.field_152366_X;
    }

    public ServerStatusResponse func_147134_at() {
        return this.field_147147_p;
    }

    public void func_147132_au() {
        this.field_147142_T = 0L;
    }

    @Nullable
    public Entity func_175576_a(UUID uuid) {
        WorldServer[] aworldserver = this.field_71305_c;
        int i = aworldserver.length;

        // CraftBukkit start
        for (int j = 0; j < worlds.size(); ++j) {
            WorldServer worldserver = worlds.get(j);
            // CraftBukkit end

            if (worldserver != null) {
                Entity entity = worldserver.func_175733_a(uuid);

                if (entity != null) {
                    return entity;
                }
            }
        }

        return null;
    }

    @Override
    public boolean func_174792_t_() {
        return worlds.get(0).func_82736_K().func_82766_b("sendCommandFeedback");
    }

    @Override
    public MinecraftServer func_184102_h() {
        return this;
    }

    public int func_175580_aG() {
        return 29999984;
    }

    public <V> ListenableFuture<V> func_175586_a(Callable<V> callable) {
        Validate.notNull(callable);
        if (!this.func_152345_ab()) { // CraftBukkit && !this.isStopped()) {
            ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);
            Queue queue = this.field_175589_i;

            // Spigot start
            this.field_175589_i.add(listenablefuturetask);
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
    public ListenableFuture<Object> func_152344_a(Runnable runnable) {
        Validate.notNull(runnable);
        return this.func_175586_a(Executors.callable(runnable));
    }

    @Override
    public boolean func_152345_ab() {
        return Thread.currentThread() == this.field_175590_aa;
    }

    public int func_175577_aI() {
        return 256;
    }

    public long func_175587_aJ() {
        return this.field_175591_ab;
    }

    public final Thread getServerThread() { return this.func_175583_aK(); } // Paper - OBFHELPER
    public Thread func_175583_aK() {
        return this.field_175590_aa;
    }

    public int func_184108_a(@Nullable WorldServer worldserver) {
        return worldserver != null ? worldserver.func_82736_K().func_180263_c("spawnRadius") : 10;
    }

    public AdvancementManager func_191949_aK() {
        return this.worlds.get(0).func_191952_z(); // CraftBukkit
    }

    public FunctionManager func_193030_aL() {
        return this.worlds.get(0).func_193037_A(); // CraftBukkit
    }

    public void func_193031_aM() {
        if (this.func_152345_ab()) {
            this.func_184103_al().func_72389_g();
            this.worlds.get(0).func_184146_ak().func_186522_a(); // CraftBukkit
            this.func_191949_aK().func_192779_a();
            this.func_193030_aL().func_193059_f();
            this.func_184103_al().func_193244_w();
        } else {
            this.func_152344_a(this::func_193031_aM);
        }

    }

    // CraftBukkit start
    @Deprecated
    public static MinecraftServer getServer() {
        return SERVER;
    }
    // CraftBukkit end
}

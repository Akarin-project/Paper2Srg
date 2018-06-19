package net.minecraft.server.dedicated;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.aikar.timings.MinecraftTimings;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.rcon.IServer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.network.rcon.RConThreadMain;
import net.minecraft.network.rcon.RConThreadQuery;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerEula;
import net.minecraft.server.gui.MinecraftServerGui;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.CryptManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import org.apache.logging.log4j.Level;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

// CraftBukkit start
import java.io.PrintStream;
import org.apache.logging.log4j.Level;

import org.bukkit.craftbukkit.LoggerOutputStream;
import co.aikar.timings.MinecraftTimings; // Paper
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.server.RemoteServerCommandEvent;
// CraftBukkit end

public class DedicatedServer extends MinecraftServer implements IServer {

    private static final Logger field_155771_h = LogManager.getLogger();
    private static final Pattern field_189647_l = Pattern.compile("^[a-fA-F0-9]{40}$");
    private final List<PendingCommand> field_71341_l = Collections.synchronizedList(Lists.<PendingCommand>newArrayList()); // CraftBukkit - fix decompile error
    private RConThreadQuery field_71342_m;
    public final RConConsoleSource field_184115_n = new RConConsoleSource(this);
    private RConThreadMain field_71339_n;
    public PropertyManager field_71340_o;
    private ServerEula field_154332_n;
    private boolean field_71338_p;
    private GameType field_71337_q;
    private boolean field_71335_s;

    // CraftBukkit start - Signature changed
    public DedicatedServer(joptsimple.OptionSet options, DataFixer dataconvertermanager, YggdrasilAuthenticationService yggdrasilauthenticationservice, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, PlayerProfileCache usercache) {
        super(options, Proxy.NO_PROXY, dataconvertermanager, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache);
        // CraftBukkit end
        Thread thread = new Thread("Server Infinisleeper") {
            {
                this.setDaemon(true);
                this.start();
            }

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2147483647L);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }
                }
            }
        };
    }

    public boolean func_71197_b() throws IOException { // CraftBukkit - decompile error
        Thread thread = new Thread("Server console handler") {
            public void run() {
                // CraftBukkit start
                if (!org.bukkit.craftbukkit.Main.useConsole) {
                    return;
                }
                // Paper start - Use TerminalConsoleAppender implementation
                if (com.destroystokyo.paper.console.TerminalHandler.handleCommands(DedicatedServer.this)) return;
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                // Paper end
                // CraftBukkit end

                String s;

                try {
                    // CraftBukkit start - JLine disabling compatibility
                    while (!func_71241_aa() && func_71278_l()) {
                        // Paper start - code is not used for jline
                        /*
                        if (org.bukkit.craftbukkit.Main.useJline) {
                            s = bufferedreader.readLine(">", null);
                        } else {
                            s = bufferedreader.readLine();
                        }
                        */
                        s = bufferedreader.readLine();
                        // Paper end

                        if (s != null && s.trim().length() > 0) { // Trim to filter lines which are just spaces
                            func_71331_a(s, DedicatedServer.this);
                        }
                        // CraftBukkit end
                    }
                } catch (IOException ioexception) {
                    DedicatedServer.field_155771_h.error("Exception handling console input", ioexception);
                }

            }
        };

        // CraftBukkit start - TODO: handle command-line logging arguments
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        global.setUseParentHandlers(false);
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }
        global.addHandler(new org.bukkit.craftbukkit.util.ForwardLogHandler());

        // Paper start - Not needed with TerminalConsoleAppender
        final org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();
        /*
        final org.apache.logging.log4j.core.Logger logger = ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger());
        for (org.apache.logging.log4j.core.Appender appender : logger.getAppenders().values()) {
            if (appender instanceof org.apache.logging.log4j.core.appender.ConsoleAppender) {
                logger.removeAppender(appender);
            }
        }

        new Thread(new org.bukkit.craftbukkit.util.TerminalConsoleWriterThread(System.out, this.reader)).start();
        */
        // Paper end

        // Paper start - Use Log4j IOStreams
        System.setOut(org.apache.logging.log4j.io.IoBuilder.forLogger(logger).setLevel(Level.INFO).buildPrintStream());
        System.setErr(org.apache.logging.log4j.io.IoBuilder.forLogger(logger).setLevel(Level.WARN).buildPrintStream());
        // Paper end
        // CraftBukkit end

        thread.setDaemon(true);
        thread.start();
        DedicatedServer.field_155771_h.info("Starting minecraft server version 1.12.2");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            DedicatedServer.field_155771_h.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        DedicatedServer.field_155771_h.info("Loading properties");
        this.field_71340_o = new PropertyManager(this.options); // CraftBukkit - CLI argument support
        this.field_154332_n = new ServerEula(new File("eula.txt"));
        // Spigot Start
        boolean eulaAgreed = Boolean.getBoolean( "com.mojang.eula.agree" );
        if ( eulaAgreed )
        {
            System.err.println( "You have used the Spigot command line EULA agreement flag." );
            System.err.println( "By using this setting you are indicating your agreement to Mojang's EULA (https://account.mojang.com/documents/minecraft_eula)." );
            System.err.println( "If you do not agree to the above EULA please stop your server and remove this flag immediately." );
        }
        // Spigot End
        if (!this.field_154332_n.func_154346_a() && !eulaAgreed) { // Spigot
            DedicatedServer.field_155771_h.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
            this.field_154332_n.func_154348_b();
            return false;
        } else {
            if (this.func_71264_H()) {
                this.func_71189_e("127.0.0.1");
            } else {
                this.func_71229_d(this.field_71340_o.func_73670_a("online-mode", true));
                this.func_190517_e(this.field_71340_o.func_73670_a("prevent-proxy-connections", false));
                this.func_71189_e(this.field_71340_o.func_73671_a("server-ip", ""));
            }

            this.func_71251_e(this.field_71340_o.func_73670_a("spawn-animals", true));
            this.func_71257_f(this.field_71340_o.func_73670_a("spawn-npcs", true));
            this.func_71188_g(this.field_71340_o.func_73670_a("pvp", true));
            this.func_71245_h(this.field_71340_o.func_73670_a("allow-flight", false));
            this.func_180507_a_(this.field_71340_o.func_73671_a("resource-pack", ""), this.func_184113_aK());
            this.func_71205_p(this.field_71340_o.func_73671_a("motd", "A Minecraft Server"));
            this.func_104055_i(this.field_71340_o.func_73670_a("force-gamemode", false));
            this.func_143006_e(this.field_71340_o.func_73669_a("player-idle-timeout", 0));
            if (this.field_71340_o.func_73669_a("difficulty", 1) < 0) {
                this.field_71340_o.func_73667_a("difficulty", Integer.valueOf(0));
            } else if (this.field_71340_o.func_73669_a("difficulty", 1) > 3) {
                this.field_71340_o.func_73667_a("difficulty", Integer.valueOf(3));
            }

            this.field_71338_p = this.field_71340_o.func_73670_a("generate-structures", true);
            int i = this.field_71340_o.func_73669_a("gamemode", GameType.SURVIVAL.func_77148_a());

            this.field_71337_q = WorldSettings.func_77161_a(i);
            DedicatedServer.field_155771_h.info("Default game type: {}", this.field_71337_q);
            InetAddress inetaddress = null;

            if (!this.func_71211_k().isEmpty()) {
                inetaddress = InetAddress.getByName(this.func_71211_k());
            }

            if (this.func_71215_F() < 0) {
                this.func_71208_b(this.field_71340_o.func_73669_a("server-port", 25565));
            }
            // Spigot start
            this.func_184105_a((PlayerList) (new DedicatedPlayerList(this)));
            org.spigotmc.SpigotConfig.init((File) options.valueOf("spigot-settings"));
            org.spigotmc.SpigotConfig.registerCommands();
            // Spigot end
            // Paper start
            com.destroystokyo.paper.PaperConfig.init((File) options.valueOf("paper-settings"));
            com.destroystokyo.paper.PaperConfig.registerCommands();
            com.destroystokyo.paper.VersionHistoryManager.INSTANCE.getClass(); // load version history now
            // Paper end

            DedicatedServer.field_155771_h.info("Generating keypair");
            this.func_71253_a(CryptManager.func_75891_b());
            DedicatedServer.field_155771_h.info("Starting Minecraft server on {}:{}", this.func_71211_k().isEmpty() ? "*" : this.func_71211_k(), Integer.valueOf(this.func_71215_F()));

        if (!org.spigotmc.SpigotConfig.lateBind) {
            try {
                this.func_147137_ag().func_151265_a(inetaddress, this.func_71215_F());
            } catch (IOException ioexception) {
                DedicatedServer.field_155771_h.warn("**** FAILED TO BIND TO PORT!");
                DedicatedServer.field_155771_h.warn("The exception was: {}", ioexception.toString());
                DedicatedServer.field_155771_h.warn("Perhaps a server is already running on that port?");
                return false;
            }
        }

            // CraftBukkit start
            // this.a((PlayerList) (new DedicatedPlayerList(this))); // Spigot - moved up
            server.loadPlugins();
            server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
            // CraftBukkit end

            if (!this.func_71266_T()) {
                DedicatedServer.field_155771_h.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
                DedicatedServer.field_155771_h.warn("The server will make no attempt to authenticate usernames. Beware.");
                // Spigot start
                if (org.spigotmc.SpigotConfig.bungee) {
                    DedicatedServer.field_155771_h.warn("Whilst this makes it possible to use BungeeCord, unless access to your server is properly restricted, it also opens up the ability for hackers to connect with any username they choose.");
                    DedicatedServer.field_155771_h.warn("Please see http://www.spigotmc.org/wiki/firewall-guide/ for further information.");
                } else {
                    DedicatedServer.field_155771_h.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
                }
                // Spigot end
                DedicatedServer.field_155771_h.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
            }

            if (this.func_152368_aE()) {
                this.func_152358_ax().func_152658_c();
            }

            if (!PreYggdrasilConverter.func_152714_a(this.field_71340_o)) {
                return false;
            } else {
                this.field_71310_m = new AnvilSaveConverter(server.getWorldContainer(), this.field_184112_s); // CraftBukkit - moved from MinecraftServer constructor
                long j = System.nanoTime();

                if (this.func_71270_I() == null) {
                    this.func_71261_m(this.field_71340_o.func_73671_a("level-name", "world"));
                }

                String s = this.field_71340_o.func_73671_a("level-seed", "");
                String s1 = this.field_71340_o.func_73671_a("level-type", "DEFAULT");
                String s2 = this.field_71340_o.func_73671_a("generator-settings", "");
                long k = (new Random()).nextLong();

                if (!s.isEmpty()) {
                    try {
                        long l = Long.parseLong(s);

                        if (l != 0L) {
                            k = l;
                        }
                    } catch (NumberFormatException numberformatexception) {
                        k = (long) s.hashCode();
                    }
                }

                WorldType worldtype = WorldType.func_77130_a(s1);

                if (worldtype == null) {
                    worldtype = WorldType.field_77137_b;
                }

                this.func_82356_Z();
                this.func_110455_j();
                this.func_70002_Q();
                this.func_175577_aI();
                this.func_71191_d(this.field_71340_o.func_73669_a("max-build-height", 256));
                this.func_71191_d((this.func_71207_Z() + 8) / 16 * 16);
                this.func_71191_d(MathHelper.func_76125_a(this.func_71207_Z(), 64, 256));
                this.field_71340_o.func_73667_a("max-build-height", Integer.valueOf(this.func_71207_Z()));
                TileEntitySkull.func_184293_a(this.func_152358_ax());
                TileEntitySkull.func_184294_a(this.func_147130_as());
                PlayerProfileCache.func_187320_a(this.func_71266_T());
                DedicatedServer.field_155771_h.info("Preparing level \"{}\"", this.func_71270_I());
                this.func_71247_a(this.func_71270_I(), this.func_71270_I(), k, worldtype, s2);
                long i1 = System.nanoTime() - j;
                String s3 = String.format("%.3fs", new Object[] { Double.valueOf((double) i1 / 1.0E9D)});

                DedicatedServer.field_155771_h.info("Done ({})! For help, type \"help\" or \"?\"", s3);
                if (this.field_71340_o.func_187239_a("announce-player-achievements")) {
                    this.worlds.get(0).func_82736_K().func_82764_b("announceAdvancements", this.field_71340_o.func_73670_a("announce-player-achievements", true) ? "true" : "false"); // CraftBukkit
                    this.field_71340_o.func_187238_b("announce-player-achievements");
                    this.field_71340_o.func_73668_b();
                }

                if (this.field_71340_o.func_73670_a("enable-query", false)) {
                    DedicatedServer.field_155771_h.info("Starting GS4 status listener");
                    this.field_71342_m = new RConThreadQuery(this);
                    this.field_71342_m.func_72602_a();
                }

                if (this.field_71340_o.func_73670_a("enable-rcon", false)) {
                    DedicatedServer.field_155771_h.info("Starting remote control listener");
                    this.field_71339_n = new RConThreadMain(this);
                    this.field_71339_n.func_72602_a();
                    this.remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(this.field_184115_n); // CraftBukkit
                }

                // CraftBukkit start
                if (this.server.getBukkitSpawnRadius() > -1) {
                    DedicatedServer.field_155771_h.info("'settings.spawn-radius' in bukkit.yml has been moved to 'spawn-protection' in server.properties. I will move your config for you.");
                    this.field_71340_o.field_73672_b.remove("spawn-protection");
                    this.field_71340_o.func_73669_a("spawn-protection", this.server.getBukkitSpawnRadius());
                    this.server.removeBukkitSpawnRadius();
                    this.field_71340_o.func_73668_b();
                }
                // CraftBukkit end

        if (org.spigotmc.SpigotConfig.lateBind) {
            try {
                this.func_147137_ag().func_151265_a(inetaddress, this.func_71215_F());
            } catch (IOException ioexception) {
                DedicatedServer.field_155771_h.warn("**** FAILED TO BIND TO PORT!");
                DedicatedServer.field_155771_h.warn("The exception was: {}", new Object[] { ioexception.toString()});
                DedicatedServer.field_155771_h.warn("Perhaps a server is already running on that port?");
                return false;
            }
        }

                if (false && this.func_175593_aQ() > 0L) {  // Spigot - disable
                    Thread thread1 = new Thread(new ServerHangWatchdog(this));

                    thread1.setName("Server Watchdog");
                    thread1.setDaemon(true);
                    thread1.start();
                }

                Items.field_190931_a.func_150895_a(CreativeTabs.field_78027_g, NonNullList.func_191196_a());
                return true;
            }
        }
    }

    public String func_184113_aK() {
        if (this.field_71340_o.func_187239_a("resource-pack-hash")) {
            if (this.field_71340_o.func_187239_a("resource-pack-sha1")) {
                DedicatedServer.field_155771_h.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
            } else {
                DedicatedServer.field_155771_h.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
                this.field_71340_o.func_73671_a("resource-pack-sha1", this.field_71340_o.func_73671_a("resource-pack-hash", ""));
                this.field_71340_o.func_187238_b("resource-pack-hash");
            }
        }

        String s = this.field_71340_o.func_73671_a("resource-pack-sha1", "");

        if (!s.isEmpty() && !DedicatedServer.field_189647_l.matcher(s).matches()) {
            DedicatedServer.field_155771_h.warn("Invalid sha1 for ressource-pack-sha1");
        }

        if (!this.field_71340_o.func_73671_a("resource-pack", "").isEmpty() && s.isEmpty()) {
            DedicatedServer.field_155771_h.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
        }

        return s;
    }

    public void func_71235_a(GameType enumgamemode) {
        super.func_71235_a(enumgamemode);
        this.field_71337_q = enumgamemode;
    }

    public boolean func_71225_e() {
        return this.field_71338_p;
    }

    public GameType func_71265_f() {
        return this.field_71337_q;
    }

    public EnumDifficulty func_147135_j() {
        return EnumDifficulty.func_151523_a(this.field_71340_o.func_73669_a("difficulty", EnumDifficulty.NORMAL.func_151525_a()));
    }

    public boolean func_71199_h() {
        return this.field_71340_o.func_73670_a("hardcore", false);
    }

    public CrashReport func_71230_b(CrashReport crashreport) {
        crashreport = super.func_71230_b(crashreport);
        crashreport.func_85056_g().func_189529_a("Is Modded", new ICrashReportDetail() {
            public String a() throws Exception {
                String s = DedicatedServer.this.getServerModName();

                return !"vanilla".equals(s) ? "Definitely; Server brand changed to \'" + s + "\'" : "Unknown (can\'t tell)";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreport.func_85056_g().func_189529_a("Type", new ICrashReportDetail() {
            public String a() throws Exception {
                return "Dedicated Server (map_server.txt)";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        return crashreport;
    }

    public void func_71240_o() {
        System.exit(0);
    }

    public void func_71190_q() { // CraftBukkit - fix decompile error
        super.func_71190_q();
        this.func_71333_ah();
    }

    public boolean func_71255_r() {
        return this.field_71340_o.func_73670_a("allow-nether", true);
    }

    public boolean func_71193_K() {
        return this.field_71340_o.func_73670_a("spawn-monsters", true);
    }

    public void func_70000_a(Snooper mojangstatisticsgenerator) {
        mojangstatisticsgenerator.func_152768_a("whitelist_enabled", Boolean.valueOf(this.func_184103_al().func_72383_n()));
        mojangstatisticsgenerator.func_152768_a("whitelist_count", Integer.valueOf(this.func_184103_al().func_152598_l().length));
        super.func_70000_a(mojangstatisticsgenerator);
    }

    public boolean func_70002_Q() {
        return this.field_71340_o.func_73670_a("snooper-enabled", true);
    }

    public void func_71331_a(String s, ICommandSender icommandlistener) {
        this.field_71341_l.add(new PendingCommand(s, icommandlistener));
    }

    public void func_71333_ah() {
        MinecraftTimings.serverCommandTimer.startTiming(); // Spigot
        while (!this.field_71341_l.isEmpty()) {
            PendingCommand servercommand = (PendingCommand) this.field_71341_l.remove(0);

            // CraftBukkit start - ServerCommand for preprocessing
            ServerCommandEvent event = new ServerCommandEvent(console, servercommand.field_73702_a);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) continue;
            servercommand = new PendingCommand(event.getCommand(), servercommand.field_73701_b);

            // this.getCommandHandler().a(servercommand.source, servercommand.command); // Called in dispatchServerCommand
            server.dispatchServerCommand(console, servercommand);
            // CraftBukkit end
        }

        MinecraftTimings.serverCommandTimer.stopTiming(); // Spigot
    }

    public boolean func_71262_S() {
        return true;
    }

    public boolean func_181035_ah() {
        return this.field_71340_o.func_73670_a("use-native-transport", true);
    }

    public DedicatedPlayerList func_184103_al() {
        return (DedicatedPlayerList) super.func_184103_al();
    }

    public int func_71327_a(String s, int i) {
        return this.field_71340_o.func_73669_a(s, i);
    }

    public String func_71330_a(String s, String s1) {
        return this.field_71340_o.func_73671_a(s, s1);
    }

    public boolean func_71332_a(String s, boolean flag) {
        return this.field_71340_o.func_73670_a(s, flag);
    }

    public void func_71328_a(String s, Object object) {
        this.field_71340_o.func_73667_a(s, object);
    }

    public void func_71326_a() {
        this.field_71340_o.func_73668_b();
    }

    public String func_71329_c() {
        File file = this.field_71340_o.func_73665_c();

        return file != null ? file.getAbsolutePath() : "No settings file";
    }

    public String func_71277_t() {
        return this.func_71211_k();
    }

    public int func_71234_u() {
        return this.func_71215_F();
    }

    public String func_71274_v() {
        return this.func_71273_Y();
    }

    public void func_120011_ar() {
        MinecraftServerGui.func_120016_a(this);
        this.field_71335_s = true;
    }

    public boolean func_71279_ae() {
        return this.field_71335_s;
    }

    public String func_71206_a(GameType enumgamemode, boolean flag) {
        return "";
    }

    public boolean func_82356_Z() {
        return this.field_71340_o.func_73670_a("enable-command-block", false);
    }

    public int func_82357_ak() {
        return this.field_71340_o.func_73669_a("spawn-protection", super.func_82357_ak());
    }

    public boolean func_175579_a(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        if (world.field_73011_w.func_186058_p().func_186068_a() != 0) {
            return false;
        } else if (this.func_184103_al().func_152603_m().func_152690_d()) {
            return false;
        } else if (this.func_184103_al().func_152596_g(entityhuman.func_146103_bH())) {
            return false;
        } else if (this.func_82357_ak() <= 0) {
            return false;
        } else {
            BlockPos blockposition1 = world.func_175694_M();
            int i = MathHelper.func_76130_a(blockposition.func_177958_n() - blockposition1.func_177958_n());
            int j = MathHelper.func_76130_a(blockposition.func_177952_p() - blockposition1.func_177952_p());
            int k = Math.max(i, j);

            return k <= this.func_82357_ak();
        }
    }

    public int func_110455_j() {
        return this.field_71340_o.func_73669_a("op-permission-level", 4);
    }

    public void func_143006_e(int i) {
        super.func_143006_e(i);
        this.field_71340_o.func_73667_a("player-idle-timeout", Integer.valueOf(i));
        this.func_71326_a();
    }

    public boolean func_181034_q() {
        return this.field_71340_o.func_73670_a("broadcast-rcon-to-ops", true);
    }

    public boolean func_183002_r() {
        return this.field_71340_o.func_73670_a("broadcast-console-to-ops", true);
    }

    public int func_175580_aG() {
        int i = this.field_71340_o.func_73669_a("max-world-size", super.func_175580_aG());

        if (i < 1) {
            i = 1;
        } else if (i > super.func_175580_aG()) {
            i = super.func_175580_aG();
        }

        return i;
    }

    public int func_175577_aI() {
        return this.field_71340_o.func_73669_a("network-compression-threshold", super.func_175577_aI());
    }

    protected boolean func_152368_aE() {
        server.getLogger().info( "**** Beginning UUID conversion, this may take A LONG time ****"); // Spigot, let the user know whats up!
        boolean flag = false;

        int i;

        for (i = 0; !flag && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.field_155771_h.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
                this.func_152369_aG();
            }

            flag = PreYggdrasilConverter.func_152724_a((MinecraftServer) this);
        }

        boolean flag1 = false;

        for (i = 0; !flag1 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.field_155771_h.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
                this.func_152369_aG();
            }

            flag1 = PreYggdrasilConverter.func_152722_b((MinecraftServer) this);
        }

        boolean flag2 = false;

        for (i = 0; !flag2 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.field_155771_h.warn("Encountered a problem while converting the op list, retrying in a few seconds");
                this.func_152369_aG();
            }

            flag2 = PreYggdrasilConverter.func_152718_c((MinecraftServer) this);
        }

        boolean flag3 = false;

        for (i = 0; !flag3 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.field_155771_h.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
                this.func_152369_aG();
            }

            flag3 = PreYggdrasilConverter.func_152710_d((MinecraftServer) this);
        }

        boolean flag4 = false;

        for (i = 0; !flag4 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.field_155771_h.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
                this.func_152369_aG();
            }

            flag4 = PreYggdrasilConverter.func_152723_a(this, this.field_71340_o);
        }

        return flag || flag1 || flag2 || flag3 || flag4;
    }

    private void func_152369_aG() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException interruptedexception) {
            ;
        }
    }

    public long func_175593_aQ() {
        return this.field_71340_o.func_179885_a("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
    }

    public String func_71258_A() {
        // CraftBukkit start - Whole method
        StringBuilder result = new StringBuilder();
        org.bukkit.plugin.Plugin[] plugins = server.getPluginManager().getPlugins();

        result.append(server.getName());
        result.append(" on Bukkit ");
        result.append(server.getBukkitVersion());

        if (plugins.length > 0 && server.getQueryPlugins()) {
            result.append(": ");

            for (int i = 0; i < plugins.length; i++) {
                if (i > 0) {
                    result.append("; ");
                }

                result.append(plugins[i].getDescription().getName());
                result.append(" ");
                result.append(plugins[i].getDescription().getVersion().replaceAll(";", ","));
            }
        }

        return result.toString();
        // CraftBukkit end
    }

    // CraftBukkit start - fire RemoteServerCommandEvent
    public String func_71252_i(final String s) {
        Waitable<String> waitable = new Waitable<String>() {
            @Override
            protected String evaluate() {
                field_184115_n.func_70007_b();
                // Event changes start
                RemoteServerCommandEvent event = new RemoteServerCommandEvent(remoteConsole, s);
                server.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return "";
                }
                // Event change end
                PendingCommand serverCommand = new PendingCommand(event.getCommand(), field_184115_n);
                server.dispatchServerCommand(remoteConsole, serverCommand);
                return field_184115_n.func_70008_c();
            }
        };
        // Paper start
        if (s.toLowerCase().startsWith("timings") && s.toLowerCase().matches("timings (report|paste|get|merged|seperate)")) {
            org.bukkit.command.BufferedCommandSender sender = new org.bukkit.command.BufferedCommandSender();
            waitable = new Waitable<String>() {
                @Override
                protected String evaluate() {
                    return sender.getBuffer();
                }
            };
            co.aikar.timings.Timings.generateReport(new co.aikar.timings.TimingsReportListener(sender, waitable));
        } else {
            processQueue.add(waitable);
        }
        // Paper end
        try {
            return waitable.get();
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Exception processing rcon command " + s, e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Maintain interrupted state
            throw new RuntimeException("Interrupted processing rcon command " + s, e);
        }
        // CraftBukkit end
    }

    public PlayerList func_184103_al() {
        return this.func_184103_al();
    }

    // CraftBukkit start
    @Override
    public PropertyManager getPropertyManager() {
        return this.field_71340_o;
    }
    // CraftBukkit end
}

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

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern RESOURCE_PACK_SHA1_PATTERN = Pattern.compile("^[a-fA-F0-9]{40}$");
    private final List<PendingCommand> pendingCommandList = Collections.synchronizedList(Lists.<PendingCommand>newArrayList()); // CraftBukkit - fix decompile error
    private RConThreadQuery rconQueryThread;
    public final RConConsoleSource rconConsoleSource = new RConConsoleSource(this);
    private RConThreadMain rconThread;
    public PropertyManager settings;
    private ServerEula eula;
    private boolean canSpawnStructures;
    private GameType gameType;
    private boolean guiIsEnabled;

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

    public boolean init() throws IOException { // CraftBukkit - decompile error
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
                    while (!isServerStopped() && isServerRunning()) {
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
                            addPendingCommand(s, DedicatedServer.this);
                        }
                        // CraftBukkit end
                    }
                } catch (IOException ioexception) {
                    DedicatedServer.LOGGER.error("Exception handling console input", ioexception);
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
        DedicatedServer.LOGGER.info("Starting minecraft server version 1.12.2");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            DedicatedServer.LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        DedicatedServer.LOGGER.info("Loading properties");
        this.settings = new PropertyManager(this.options); // CraftBukkit - CLI argument support
        this.eula = new ServerEula(new File("eula.txt"));
        // Spigot Start
        boolean eulaAgreed = Boolean.getBoolean( "com.mojang.eula.agree" );
        if ( eulaAgreed )
        {
            System.err.println( "You have used the Spigot command line EULA agreement flag." );
            System.err.println( "By using this setting you are indicating your agreement to Mojang's EULA (https://account.mojang.com/documents/minecraft_eula)." );
            System.err.println( "If you do not agree to the above EULA please stop your server and remove this flag immediately." );
        }
        // Spigot End
        if (!this.eula.hasAcceptedEULA() && !eulaAgreed) { // Spigot
            DedicatedServer.LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
            this.eula.createEULAFile();
            return false;
        } else {
            if (this.isSinglePlayer()) {
                this.setHostname("127.0.0.1");
            } else {
                this.setOnlineMode(this.settings.getBooleanProperty("online-mode", true));
                this.setPreventProxyConnections(this.settings.getBooleanProperty("prevent-proxy-connections", false));
                this.setHostname(this.settings.getStringProperty("server-ip", ""));
            }

            this.setCanSpawnAnimals(this.settings.getBooleanProperty("spawn-animals", true));
            this.setCanSpawnNPCs(this.settings.getBooleanProperty("spawn-npcs", true));
            this.setAllowPvp(this.settings.getBooleanProperty("pvp", true));
            this.setAllowFlight(this.settings.getBooleanProperty("allow-flight", false));
            this.setResourcePack(this.settings.getStringProperty("resource-pack", ""), this.loadResourcePackSHA());
            this.setMOTD(this.settings.getStringProperty("motd", "A Minecraft Server"));
            this.setForceGamemode(this.settings.getBooleanProperty("force-gamemode", false));
            this.setPlayerIdleTimeout(this.settings.getIntProperty("player-idle-timeout", 0));
            if (this.settings.getIntProperty("difficulty", 1) < 0) {
                this.settings.setProperty("difficulty", Integer.valueOf(0));
            } else if (this.settings.getIntProperty("difficulty", 1) > 3) {
                this.settings.setProperty("difficulty", Integer.valueOf(3));
            }

            this.canSpawnStructures = this.settings.getBooleanProperty("generate-structures", true);
            int i = this.settings.getIntProperty("gamemode", GameType.SURVIVAL.getID());

            this.gameType = WorldSettings.getGameTypeById(i);
            DedicatedServer.LOGGER.info("Default game type: {}", this.gameType);
            InetAddress inetaddress = null;

            if (!this.getServerHostname().isEmpty()) {
                inetaddress = InetAddress.getByName(this.getServerHostname());
            }

            if (this.getServerPort() < 0) {
                this.setServerPort(this.settings.getIntProperty("server-port", 25565));
            }
            // Spigot start
            this.setPlayerList((PlayerList) (new DedicatedPlayerList(this)));
            org.spigotmc.SpigotConfig.init((File) options.valueOf("spigot-settings"));
            org.spigotmc.SpigotConfig.registerCommands();
            // Spigot end
            // Paper start
            com.destroystokyo.paper.PaperConfig.init((File) options.valueOf("paper-settings"));
            com.destroystokyo.paper.PaperConfig.registerCommands();
            com.destroystokyo.paper.VersionHistoryManager.INSTANCE.getClass(); // load version history now
            // Paper end

            DedicatedServer.LOGGER.info("Generating keypair");
            this.setKeyPair(CryptManager.generateKeyPair());
            DedicatedServer.LOGGER.info("Starting Minecraft server on {}:{}", this.getServerHostname().isEmpty() ? "*" : this.getServerHostname(), Integer.valueOf(this.getServerPort()));

        if (!org.spigotmc.SpigotConfig.lateBind) {
            try {
                this.getNetworkSystem().addLanEndpoint(inetaddress, this.getServerPort());
            } catch (IOException ioexception) {
                DedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
                DedicatedServer.LOGGER.warn("The exception was: {}", ioexception.toString());
                DedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
                return false;
            }
        }

            // CraftBukkit start
            // this.a((PlayerList) (new DedicatedPlayerList(this))); // Spigot - moved up
            server.loadPlugins();
            server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
            // CraftBukkit end

            if (!this.isServerInOnlineMode()) {
                DedicatedServer.LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
                DedicatedServer.LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
                // Spigot start
                if (org.spigotmc.SpigotConfig.bungee) {
                    DedicatedServer.LOGGER.warn("Whilst this makes it possible to use BungeeCord, unless access to your server is properly restricted, it also opens up the ability for hackers to connect with any username they choose.");
                    DedicatedServer.LOGGER.warn("Please see http://www.spigotmc.org/wiki/firewall-guide/ for further information.");
                } else {
                    DedicatedServer.LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
                }
                // Spigot end
                DedicatedServer.LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
            }

            if (this.convertFiles()) {
                this.getPlayerProfileCache().save();
            }

            if (!PreYggdrasilConverter.tryConvert(this.settings)) {
                return false;
            } else {
                this.anvilConverterForAnvilFile = new AnvilSaveConverter(server.getWorldContainer(), this.dataFixer); // CraftBukkit - moved from MinecraftServer constructor
                long j = System.nanoTime();

                if (this.getFolderName() == null) {
                    this.setFolderName(this.settings.getStringProperty("level-name", "world"));
                }

                String s = this.settings.getStringProperty("level-seed", "");
                String s1 = this.settings.getStringProperty("level-type", "DEFAULT");
                String s2 = this.settings.getStringProperty("generator-settings", "");
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

                WorldType worldtype = WorldType.parseWorldType(s1);

                if (worldtype == null) {
                    worldtype = WorldType.DEFAULT;
                }

                this.isCommandBlockEnabled();
                this.getOpPermissionLevel();
                this.isSnooperEnabled();
                this.getNetworkCompressionThreshold();
                this.setBuildLimit(this.settings.getIntProperty("max-build-height", 256));
                this.setBuildLimit((this.getBuildLimit() + 8) / 16 * 16);
                this.setBuildLimit(MathHelper.clamp(this.getBuildLimit(), 64, 256));
                this.settings.setProperty("max-build-height", Integer.valueOf(this.getBuildLimit()));
                TileEntitySkull.setProfileCache(this.getPlayerProfileCache());
                TileEntitySkull.setSessionService(this.getMinecraftSessionService());
                PlayerProfileCache.setOnlineMode(this.isServerInOnlineMode());
                DedicatedServer.LOGGER.info("Preparing level \"{}\"", this.getFolderName());
                this.loadAllWorlds(this.getFolderName(), this.getFolderName(), k, worldtype, s2);
                long i1 = System.nanoTime() - j;
                String s3 = String.format("%.3fs", new Object[] { Double.valueOf((double) i1 / 1.0E9D)});

                DedicatedServer.LOGGER.info("Done ({})! For help, type \"help\" or \"?\"", s3);
                if (this.settings.hasProperty("announce-player-achievements")) {
                    this.worlds.get(0).getGameRules().setOrCreateGameRule("announceAdvancements", this.settings.getBooleanProperty("announce-player-achievements", true) ? "true" : "false"); // CraftBukkit
                    this.settings.removeProperty("announce-player-achievements");
                    this.settings.saveProperties();
                }

                if (this.settings.getBooleanProperty("enable-query", false)) {
                    DedicatedServer.LOGGER.info("Starting GS4 status listener");
                    this.rconQueryThread = new RConThreadQuery(this);
                    this.rconQueryThread.startThread();
                }

                if (this.settings.getBooleanProperty("enable-rcon", false)) {
                    DedicatedServer.LOGGER.info("Starting remote control listener");
                    this.rconThread = new RConThreadMain(this);
                    this.rconThread.startThread();
                    this.remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(this.rconConsoleSource); // CraftBukkit
                }

                // CraftBukkit start
                if (this.server.getBukkitSpawnRadius() > -1) {
                    DedicatedServer.LOGGER.info("'settings.spawn-radius' in bukkit.yml has been moved to 'spawn-protection' in server.properties. I will move your config for you.");
                    this.settings.serverProperties.remove("spawn-protection");
                    this.settings.getIntProperty("spawn-protection", this.server.getBukkitSpawnRadius());
                    this.server.removeBukkitSpawnRadius();
                    this.settings.saveProperties();
                }
                // CraftBukkit end

        if (org.spigotmc.SpigotConfig.lateBind) {
            try {
                this.getNetworkSystem().addLanEndpoint(inetaddress, this.getServerPort());
            } catch (IOException ioexception) {
                DedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
                DedicatedServer.LOGGER.warn("The exception was: {}", new Object[] { ioexception.toString()});
                DedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
                return false;
            }
        }

                if (false && this.getMaxTickTime() > 0L) {  // Spigot - disable
                    Thread thread1 = new Thread(new ServerHangWatchdog(this));

                    thread1.setName("Server Watchdog");
                    thread1.setDaemon(true);
                    thread1.start();
                }

                Items.AIR.getSubItems(CreativeTabs.SEARCH, NonNullList.create());
                return true;
            }
        }
    }

    public String loadResourcePackSHA() {
        if (this.settings.hasProperty("resource-pack-hash")) {
            if (this.settings.hasProperty("resource-pack-sha1")) {
                DedicatedServer.LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
            } else {
                DedicatedServer.LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
                this.settings.getStringProperty("resource-pack-sha1", this.settings.getStringProperty("resource-pack-hash", ""));
                this.settings.removeProperty("resource-pack-hash");
            }
        }

        String s = this.settings.getStringProperty("resource-pack-sha1", "");

        if (!s.isEmpty() && !DedicatedServer.RESOURCE_PACK_SHA1_PATTERN.matcher(s).matches()) {
            DedicatedServer.LOGGER.warn("Invalid sha1 for ressource-pack-sha1");
        }

        if (!this.settings.getStringProperty("resource-pack", "").isEmpty() && s.isEmpty()) {
            DedicatedServer.LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
        }

        return s;
    }

    public void setGameType(GameType enumgamemode) {
        super.setGameType(enumgamemode);
        this.gameType = enumgamemode;
    }

    public boolean canStructuresSpawn() {
        return this.canSpawnStructures;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public EnumDifficulty getDifficulty() {
        return EnumDifficulty.getDifficultyEnum(this.settings.getIntProperty("difficulty", EnumDifficulty.NORMAL.getDifficultyId()));
    }

    public boolean isHardcore() {
        return this.settings.getBooleanProperty("hardcore", false);
    }

    public CrashReport addServerInfoToCrashReport(CrashReport crashreport) {
        crashreport = super.addServerInfoToCrashReport(crashreport);
        crashreport.getCategory().addDetail("Is Modded", new ICrashReportDetail() {
            public String a() throws Exception {
                String s = DedicatedServer.this.getServerModName();

                return !"vanilla".equals(s) ? "Definitely; Server brand changed to \'" + s + "\'" : "Unknown (can\'t tell)";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreport.getCategory().addDetail("Type", new ICrashReportDetail() {
            public String a() throws Exception {
                return "Dedicated Server (map_server.txt)";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        return crashreport;
    }

    public void systemExitNow() {
        System.exit(0);
    }

    public void updateTimeLightAndEntities() { // CraftBukkit - fix decompile error
        super.updateTimeLightAndEntities();
        this.executePendingCommands();
    }

    public boolean getAllowNether() {
        return this.settings.getBooleanProperty("allow-nether", true);
    }

    public boolean allowSpawnMonsters() {
        return this.settings.getBooleanProperty("spawn-monsters", true);
    }

    public void addServerStatsToSnooper(Snooper mojangstatisticsgenerator) {
        mojangstatisticsgenerator.addClientStat("whitelist_enabled", Boolean.valueOf(this.getPlayerList().isWhiteListEnabled()));
        mojangstatisticsgenerator.addClientStat("whitelist_count", Integer.valueOf(this.getPlayerList().getWhitelistedPlayerNames().length));
        super.addServerStatsToSnooper(mojangstatisticsgenerator);
    }

    public boolean isSnooperEnabled() {
        return this.settings.getBooleanProperty("snooper-enabled", true);
    }

    public void addPendingCommand(String s, ICommandSender icommandlistener) {
        this.pendingCommandList.add(new PendingCommand(s, icommandlistener));
    }

    public void executePendingCommands() {
        MinecraftTimings.serverCommandTimer.startTiming(); // Spigot
        while (!this.pendingCommandList.isEmpty()) {
            PendingCommand servercommand = (PendingCommand) this.pendingCommandList.remove(0);

            // CraftBukkit start - ServerCommand for preprocessing
            ServerCommandEvent event = new ServerCommandEvent(console, servercommand.command);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) continue;
            servercommand = new PendingCommand(event.getCommand(), servercommand.sender);

            // this.getCommandHandler().a(servercommand.source, servercommand.command); // Called in dispatchServerCommand
            server.dispatchServerCommand(console, servercommand);
            // CraftBukkit end
        }

        MinecraftTimings.serverCommandTimer.stopTiming(); // Spigot
    }

    public boolean isDedicatedServer() {
        return true;
    }

    public boolean shouldUseNativeTransport() {
        return this.settings.getBooleanProperty("use-native-transport", true);
    }

    public DedicatedPlayerList getPlayerList() {
        return (DedicatedPlayerList) super.getPlayerList();
    }

    public int getIntProperty(String s, int i) {
        return this.settings.getIntProperty(s, i);
    }

    public String getStringProperty(String s, String s1) {
        return this.settings.getStringProperty(s, s1);
    }

    public boolean getBooleanProperty(String s, boolean flag) {
        return this.settings.getBooleanProperty(s, flag);
    }

    public void setProperty(String s, Object object) {
        this.settings.setProperty(s, object);
    }

    public void saveProperties() {
        this.settings.saveProperties();
    }

    public String getSettingsFilename() {
        File file = this.settings.getPropertiesFile();

        return file != null ? file.getAbsolutePath() : "No settings file";
    }

    public String getHostname() {
        return this.getServerHostname();
    }

    public int getPort() {
        return this.getServerPort();
    }

    public String getMotd() {
        return this.getMOTD();
    }

    public void setGuiEnabled() {
        MinecraftServerGui.createServerGui(this);
        this.guiIsEnabled = true;
    }

    public boolean getGuiEnabled() {
        return this.guiIsEnabled;
    }

    public String shareToLAN(GameType enumgamemode, boolean flag) {
        return "";
    }

    public boolean isCommandBlockEnabled() {
        return this.settings.getBooleanProperty("enable-command-block", false);
    }

    public int getSpawnProtectionSize() {
        return this.settings.getIntProperty("spawn-protection", super.getSpawnProtectionSize());
    }

    public boolean isBlockProtected(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        if (world.provider.getDimensionType().getId() != 0) {
            return false;
        } else if (this.getPlayerList().getOppedPlayers().isEmpty()) {
            return false;
        } else if (this.getPlayerList().canSendCommands(entityhuman.getGameProfile())) {
            return false;
        } else if (this.getSpawnProtectionSize() <= 0) {
            return false;
        } else {
            BlockPos blockposition1 = world.getSpawnPoint();
            int i = MathHelper.abs(blockposition.getX() - blockposition1.getX());
            int j = MathHelper.abs(blockposition.getZ() - blockposition1.getZ());
            int k = Math.max(i, j);

            return k <= this.getSpawnProtectionSize();
        }
    }

    public int getOpPermissionLevel() {
        return this.settings.getIntProperty("op-permission-level", 4);
    }

    public void setPlayerIdleTimeout(int i) {
        super.setPlayerIdleTimeout(i);
        this.settings.setProperty("player-idle-timeout", Integer.valueOf(i));
        this.saveProperties();
    }

    public boolean shouldBroadcastRconToOps() {
        return this.settings.getBooleanProperty("broadcast-rcon-to-ops", true);
    }

    public boolean shouldBroadcastConsoleToOps() {
        return this.settings.getBooleanProperty("broadcast-console-to-ops", true);
    }

    public int getMaxWorldSize() {
        int i = this.settings.getIntProperty("max-world-size", super.getMaxWorldSize());

        if (i < 1) {
            i = 1;
        } else if (i > super.getMaxWorldSize()) {
            i = super.getMaxWorldSize();
        }

        return i;
    }

    public int getNetworkCompressionThreshold() {
        return this.settings.getIntProperty("network-compression-threshold", super.getNetworkCompressionThreshold());
    }

    protected boolean convertFiles() {
        server.getLogger().info( "**** Beginning UUID conversion, this may take A LONG time ****"); // Spigot, let the user know whats up!
        boolean flag = false;

        int i;

        for (i = 0; !flag && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
                this.sleepFiveSeconds();
            }

            flag = PreYggdrasilConverter.convertUserBanlist((MinecraftServer) this);
        }

        boolean flag1 = false;

        for (i = 0; !flag1 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
                this.sleepFiveSeconds();
            }

            flag1 = PreYggdrasilConverter.convertIpBanlist((MinecraftServer) this);
        }

        boolean flag2 = false;

        for (i = 0; !flag2 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the op list, retrying in a few seconds");
                this.sleepFiveSeconds();
            }

            flag2 = PreYggdrasilConverter.convertOplist((MinecraftServer) this);
        }

        boolean flag3 = false;

        for (i = 0; !flag3 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
                this.sleepFiveSeconds();
            }

            flag3 = PreYggdrasilConverter.convertWhitelist((MinecraftServer) this);
        }

        boolean flag4 = false;

        for (i = 0; !flag4 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
                this.sleepFiveSeconds();
            }

            flag4 = PreYggdrasilConverter.convertSaveFiles(this, this.settings);
        }

        return flag || flag1 || flag2 || flag3 || flag4;
    }

    private void sleepFiveSeconds() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException interruptedexception) {
            ;
        }
    }

    public long getMaxTickTime() {
        return this.settings.getLongProperty("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
    }

    public String getPlugins() {
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
    public String handleRConCommand(final String s) {
        Waitable<String> waitable = new Waitable<String>() {
            @Override
            protected String evaluate() {
                rconConsoleSource.resetLog();
                // Event changes start
                RemoteServerCommandEvent event = new RemoteServerCommandEvent(remoteConsole, s);
                server.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return "";
                }
                // Event change end
                PendingCommand serverCommand = new PendingCommand(event.getCommand(), rconConsoleSource);
                server.dispatchServerCommand(remoteConsole, serverCommand);
                return rconConsoleSource.getLogContents();
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

    public PlayerList getPlayerList() {
        return this.getPlayerList();
    }

    // CraftBukkit start
    @Override
    public PropertyManager getPropertyManager() {
        return this.settings;
    }
    // CraftBukkit end
}

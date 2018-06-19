package net.minecraft.server.management;

import co.aikar.timings.MinecraftTimings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

// CraftBukkit start
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
// CraftBukkit end

public abstract class PlayerList {

    public static final File field_152613_a = new File("banned-players.json");
    public static final File field_152614_b = new File("banned-ips.json");
    public static final File field_152615_c = new File("ops.json");
    public static final File field_152616_d = new File("whitelist.json");
    private static final Logger field_148546_d = LogManager.getLogger();
    private static final SimpleDateFormat field_72403_e = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
    private final MinecraftServer field_72400_f;
    public final List<EntityPlayerMP> field_72404_b = new java.util.concurrent.CopyOnWriteArrayList(); // CraftBukkit - ArrayList -> CopyOnWriteArrayList: Iterator safety
    private final Map<UUID, EntityPlayerMP> field_177454_f = Maps.newHashMap();
    private final UserListBans field_72401_g;
    private final UserListIPBans field_72413_h;
    private final UserListOps field_72414_i;
    private final UserListWhitelist field_72411_j;
    // CraftBukkit start
    // private final Map<UUID, ServerStatisticManager> o;
    // private final Map<UUID, AdvancementDataPlayer> p;
    // CraftBukkit end
    public IPlayerFileData field_72412_k;
    private boolean field_72409_l;
    protected int field_72405_c;
    private int field_72402_d;
    private GameType field_72410_m;
    private boolean field_72407_n;
    private int field_72408_o;

    // CraftBukkit start
    private CraftServer cserver;
    private final Map<String,EntityPlayerMP> playersByName = new org.spigotmc.CaseInsensitiveMap<EntityPlayerMP>();
    @Nullable String collideRuleTeamName; // Paper - Team name used for collideRule

    public PlayerList(MinecraftServer minecraftserver) {
        this.cserver = minecraftserver.server = new CraftServer(minecraftserver, this);
        minecraftserver.console = new com.destroystokyo.paper.console.TerminalConsoleCommandSender(); // Paper
        // CraftBukkit end

        this.field_72401_g = new UserListBans(PlayerList.field_152613_a);
        this.field_72413_h = new UserListIPBans(PlayerList.field_152614_b);
        this.field_72414_i = new UserListOps(PlayerList.field_152615_c);
        this.field_72411_j = new UserListWhitelist(PlayerList.field_152616_d);
        // CraftBukkit start
        // this.o = Maps.newHashMap();
        // this.p = Maps.newHashMap();
        // CraftBukkit end
        this.field_72400_f = minecraftserver;
        this.field_72401_g.func_152686_a(false);
        this.field_72413_h.func_152686_a(false);
        this.field_72405_c = 8;
    }

    public void func_72355_a(NetworkManager networkmanager, EntityPlayerMP entityplayer) {
        GameProfile gameprofile = entityplayer.func_146103_bH();
        PlayerProfileCache usercache = this.field_72400_f.func_152358_ax();
        GameProfile gameprofile1 = usercache.func_152652_a(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();

        usercache.func_152649_a(gameprofile);
        NBTTagCompound nbttagcompound = this.func_72380_a(entityplayer);
        // CraftBukkit start - Better rename detection
        if (nbttagcompound != null && nbttagcompound.func_74764_b("bukkit")) {
            NBTTagCompound bukkit = nbttagcompound.func_74775_l("bukkit");
            s = bukkit.func_150297_b("lastKnownName", 8) ? bukkit.func_74779_i("lastKnownName") : s;
        }
        // CraftBukkit end

        // Paper start - support PlayerInitialSpawnEvent
        Location originalLoc = new Location(entityplayer.field_70170_p.getWorld(), entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v, entityplayer.field_70177_z, entityplayer.field_70125_A);
        com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent event = new com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent(entityplayer.getBukkitEntity(), originalLoc);
        this.field_72400_f.server.getPluginManager().callEvent(event);

        Location newLoc = event.getSpawnLocation();
        entityplayer.field_70170_p = ((CraftWorld) newLoc.getWorld()).getHandle();
        entityplayer.field_70165_t = newLoc.getX();
        entityplayer.field_70163_u = newLoc.getY();
        entityplayer.field_70161_v = newLoc.getZ();
        entityplayer.field_70177_z = newLoc.getYaw();
        entityplayer.field_70125_A = newLoc.getPitch();
        entityplayer.field_71093_bK = ((CraftWorld) newLoc.getWorld()).getHandle().dimension;
        // Paper end

        entityplayer.func_70029_a(this.field_72400_f.func_71218_a(entityplayer.field_71093_bK));
        entityplayer.field_71134_c.func_73080_a((WorldServer) entityplayer.field_70170_p);
        String s1 = "local";

        if (networkmanager.func_74430_c() != null) {
            s1 = networkmanager.func_74430_c().toString();
        }

        // Spigot start - spawn location event
        Player bukkitPlayer = entityplayer.getBukkitEntity();
        PlayerSpawnLocationEvent ev = new PlayerSpawnLocationEvent(bukkitPlayer, bukkitPlayer.getLocation());
        Bukkit.getPluginManager().callEvent(ev);

        Location loc = ev.getSpawnLocation();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

        entityplayer.func_70029_a(world);
        entityplayer.func_70107_b(loc.getX(), loc.getY(), loc.getZ());
        entityplayer.func_70101_b(loc.getYaw(), loc.getPitch()); 
        // Spigot end

        // CraftBukkit - Moved message to after join
        // PlayerList.f.info("{}[{}] logged in with entity id {} at ({}, {}, {})", entityplayer.getName(), s1, Integer.valueOf(entityplayer.getId()), Double.valueOf(entityplayer.locX), Double.valueOf(entityplayer.locY), Double.valueOf(entityplayer.locZ));
        WorldServer worldserver = this.field_72400_f.func_71218_a(entityplayer.field_71093_bK);
        WorldInfo worlddata = worldserver.func_72912_H();

        this.func_72381_a(entityplayer, (EntityPlayerMP) null, worldserver);
        NetHandlerPlayServer playerconnection = new NetHandlerPlayServer(this.field_72400_f, networkmanager, entityplayer);

        playerconnection.func_147359_a(new SPacketJoinGame(entityplayer.func_145782_y(), entityplayer.field_71134_c.func_73081_b(), worlddata.func_76093_s(), worldserver.field_73011_w.func_186058_p().func_186068_a(), worldserver.func_175659_aa(), this.func_72352_l(), worlddata.func_76067_t(), worldserver.func_82736_K().func_82766_b("reducedDebugInfo")));
        entityplayer.getBukkitEntity().sendSupportedChannels(); // CraftBukkit
        playerconnection.func_147359_a(new SPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).func_180714_a(this.func_72365_p().getServerModName())));
        playerconnection.func_147359_a(new SPacketServerDifficulty(worlddata.func_176130_y(), worlddata.func_176123_z()));
        playerconnection.func_147359_a(new SPacketPlayerAbilities(entityplayer.field_71075_bZ));
        playerconnection.func_147359_a(new SPacketHeldItemChange(entityplayer.field_71071_by.field_70461_c));
        playerconnection.func_147359_a(new SPacketEntityStatus(entityplayer, (byte) (worldserver.func_82736_K().func_82766_b("reducedDebugInfo") ? 22 : 23))); // Paper - fix this rule not being initialized on the client
        this.func_187243_f(entityplayer);
        entityplayer.func_147099_x().func_150877_d();
        entityplayer.func_192037_E().func_192826_c(entityplayer);
        this.func_96456_a((ServerScoreboard) worldserver.func_96441_U(), entityplayer);
        this.field_72400_f.func_147132_au();
        // CraftBukkit start - login message is handled in the event
        // ChatMessage chatmessage;

        String joinMessage;
        if (entityplayer.func_70005_c_().equalsIgnoreCase(s)) {
            // chatmessage = new ChatMessage("multiplayer.player.joined", new Object[] { entityplayer.getScoreboardDisplayName()});
            joinMessage = "\u00A7e" + I18n.func_74837_a("multiplayer.player.joined", entityplayer.func_70005_c_());
        } else {
            // chatmessage = new ChatMessage("multiplayer.player.joined.renamed", new Object[] { entityplayer.getScoreboardDisplayName(), s});
            joinMessage = "\u00A7e" + I18n.func_74837_a("multiplayer.player.joined.renamed", entityplayer.func_70005_c_(), s);
        }

        // chatmessage.getChatModifier().setColor(EnumChatFormat.YELLOW);
        // this.sendMessage(chatmessage);
        this.onPlayerJoin(entityplayer, joinMessage);
        // CraftBukkit end
        worldserver = field_72400_f.func_71218_a(entityplayer.field_71093_bK);  // CraftBukkit - Update in case join event changed it
        playerconnection.func_147364_a(entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v, entityplayer.field_70177_z, entityplayer.field_70125_A);
        this.func_72354_b(entityplayer, worldserver);
        if (!this.field_72400_f.func_147133_T().isEmpty()) {
            entityplayer.func_175397_a(this.field_72400_f.func_147133_T(), this.field_72400_f.func_175581_ab());
        }

        Iterator iterator = entityplayer.func_70651_bq().iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            playerconnection.func_147359_a(new SPacketEntityEffect(entityplayer.func_145782_y(), mobeffect));
        }

        if (nbttagcompound != null && nbttagcompound.func_150297_b("RootVehicle", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("RootVehicle");
            Entity entity = AnvilChunkLoader.func_186051_a(nbttagcompound1.func_74775_l("Entity"), worldserver, true);

            if (entity != null) {
                UUID uuid = nbttagcompound1.func_186857_a("Attach");
                Iterator iterator1;
                Entity entity1;

                if (entity.func_110124_au().equals(uuid)) {
                    entityplayer.func_184205_a(entity, true);
                } else {
                    iterator1 = entity.func_184182_bu().iterator();

                    while (iterator1.hasNext()) {
                        entity1 = (Entity) iterator1.next();
                        if (entity1.func_110124_au().equals(uuid)) {
                            entityplayer.func_184205_a(entity1, true);
                            break;
                        }
                    }
                }

                if (!entityplayer.func_184218_aH()) {
                    PlayerList.field_148546_d.warn("Couldn\'t reattach entity to player");
                    worldserver.func_72973_f(entity);
                    iterator1 = entity.func_184182_bu().iterator();

                    while (iterator1.hasNext()) {
                        entity1 = (Entity) iterator1.next();
                        worldserver.func_72973_f(entity1);
                    }
                }
            }
        }

        entityplayer.func_71116_b();
        // Paper start - Add to collideRule team if needed
        final Scoreboard scoreboard = this.func_72365_p().func_130014_f_().func_96441_U();
        if (this.collideRuleTeamName != null && scoreboard.func_96508_e(collideRuleTeamName) != null && entityplayer.getTeam() == null) {
            scoreboard.func_151392_a(entityplayer.func_70005_c_(), collideRuleTeamName);
        }
        // Paper end
        // CraftBukkit - Moved from above, added world
        PlayerList.field_148546_d.info(entityplayer.func_70005_c_() + "[" + s1 + "] logged in with entity id " + entityplayer.func_145782_y() + " at ([" + entityplayer.field_70170_p.field_72986_A.func_76065_j() + "]" + entityplayer.field_70165_t + ", " + entityplayer.field_70163_u + ", " + entityplayer.field_70161_v + ")");
    }

    public void func_96456_a(ServerScoreboard scoreboardserver, EntityPlayerMP entityplayer) {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = scoreboardserver.func_96525_g().iterator();

        while (iterator.hasNext()) {
            ScorePlayerTeam scoreboardteam = (ScorePlayerTeam) iterator.next();

            entityplayer.field_71135_a.func_147359_a(new SPacketTeams(scoreboardteam, 0));
        }

        for (int i = 0; i < 19; ++i) {
            ScoreObjective scoreboardobjective = scoreboardserver.func_96539_a(i);

            if (scoreboardobjective != null && !hashset.contains(scoreboardobjective)) {
                List list = scoreboardserver.func_96550_d(scoreboardobjective);
                Iterator iterator1 = list.iterator();

                while (iterator1.hasNext()) {
                    Packet packet = (Packet) iterator1.next();

                    entityplayer.field_71135_a.func_147359_a(packet);
                }

                hashset.add(scoreboardobjective);
            }
        }

    }

    public void func_72364_a(WorldServer[] aworldserver) {
        if (field_72412_k != null) return; // CraftBukkit
        this.field_72412_k = aworldserver[0].func_72860_G().func_75756_e();
        aworldserver[0].func_175723_af().func_177737_a(new IBorderListener() {
            public void func_177694_a(WorldBorder worldborder, double d0) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_SIZE), worldborder.world);
            }

            public void func_177692_a(WorldBorder worldborder, double d0, double d1, long i) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.LERP_SIZE), worldborder.world);
            }

            public void func_177693_a(WorldBorder worldborder, double d0, double d1) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_CENTER), worldborder.world);
            }

            public void func_177691_a(WorldBorder worldborder, int i) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_TIME), worldborder.world);
            }

            public void func_177690_b(WorldBorder worldborder, int i) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_BLOCKS), worldborder.world);
            }

            public void func_177696_b(WorldBorder worldborder, double d0) {}

            public void func_177695_c(WorldBorder worldborder, double d0) {}
        });
    }

    public void func_72375_a(EntityPlayerMP entityplayer, @Nullable WorldServer worldserver) {
        WorldServer worldserver1 = entityplayer.func_71121_q();

        if (worldserver != null) {
            worldserver.func_184164_w().func_72695_c(entityplayer);
        }

        worldserver1.func_184164_w().func_72683_a(entityplayer);
        worldserver1.func_72863_F().func_186025_d((int) entityplayer.field_70165_t >> 4, (int) entityplayer.field_70161_v >> 4);
        if (worldserver != null) {
            CriteriaTriggers.field_193134_u.func_193143_a(entityplayer, worldserver.field_73011_w.func_186058_p(), worldserver1.field_73011_w.func_186058_p());
            if (worldserver.field_73011_w.func_186058_p() == DimensionType.NETHER && entityplayer.field_70170_p.field_73011_w.func_186058_p() == DimensionType.OVERWORLD && entityplayer.func_193106_Q() != null) {
                CriteriaTriggers.field_193131_B.func_193168_a(entityplayer, entityplayer.func_193106_Q());
            }
        }

    }

    public int func_72372_a() {
        return PlayerChunkMap.func_72686_a(this.func_72395_o());
    }

    @Nullable
    public NBTTagCompound func_72380_a(EntityPlayerMP entityplayer) {
        NBTTagCompound nbttagcompound = this.field_72400_f.worlds.get(0).func_72912_H().func_76072_h(); // CraftBukkit
        NBTTagCompound nbttagcompound1;

        if (entityplayer.func_70005_c_().equals(this.field_72400_f.func_71214_G()) && nbttagcompound != null) {
            nbttagcompound1 = nbttagcompound;
            entityplayer.func_70020_e(nbttagcompound);
            PlayerList.field_148546_d.debug("loading single player");
        } else {
            nbttagcompound1 = this.field_72412_k.func_75752_b(entityplayer);
        }

        return nbttagcompound1;
    }

    protected void func_72391_b(EntityPlayerMP entityplayer) {
        entityplayer.lastSave = MinecraftServer.currentTick; // Paper
        this.field_72412_k.func_75753_a(entityplayer);
        StatisticsManagerServer serverstatisticmanager = (StatisticsManagerServer) entityplayer.func_147099_x(); // CraftBukkit

        if (serverstatisticmanager != null) {
            serverstatisticmanager.func_150883_b();
        }

        PlayerAdvancements advancementdataplayer = (PlayerAdvancements) entityplayer.func_192039_O(); // CraftBukkit

        if (advancementdataplayer != null) {
            advancementdataplayer.func_192749_b();
        }

    }

    public void onPlayerJoin(EntityPlayerMP entityplayer, String joinMessage) { // CraftBukkit added param
        this.field_72404_b.add(entityplayer);
        this.playersByName.put(entityplayer.func_70005_c_(), entityplayer); // Spigot
        this.field_177454_f.put(entityplayer.func_110124_au(), entityplayer);
        // this.sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { entityplayer})); // CraftBukkit - replaced with loop below
        WorldServer worldserver = this.field_72400_f.func_71218_a(entityplayer.field_71093_bK);

        // CraftBukkit start
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(cserver.getPlayer(entityplayer), joinMessage);
        cserver.getPluginManager().callEvent(playerJoinEvent);

        if (!entityplayer.field_71135_a.field_147371_a.func_150724_d()) {
            return;
        }

        joinMessage = playerJoinEvent.getJoinMessage();

        if (joinMessage != null && joinMessage.length() > 0) {
            for (ITextComponent line : org.bukkit.craftbukkit.util.CraftChatMessage.fromString(joinMessage)) {
                field_72400_f.func_184103_al().func_148540_a(new SPacketChat(line));
            }
        }

        ChunkIOExecutor.adjustPoolSize(func_72394_k());
        // CraftBukkit end

        // CraftBukkit start - sendAll above replaced with this loop
        SPacketPlayerListItem packet = new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, entityplayer);

        for (int i = 0; i < this.field_72404_b.size(); ++i) {
            EntityPlayerMP entityplayer1 = (EntityPlayerMP) this.field_72404_b.get(i);

            if (entityplayer1.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer1.field_71135_a.func_147359_a(packet);
            }

            if (!entityplayer.getBukkitEntity().canSee(entityplayer1.getBukkitEntity())) {
                continue;
            }

            entityplayer.field_71135_a.func_147359_a(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { entityplayer1}));
        }
        entityplayer.sentListPacket = true;
        // CraftBukkit end

        entityplayer.field_71135_a.func_147359_a(new SPacketEntityMetadata(entityplayer.func_145782_y(), entityplayer.field_70180_af, true)); // CraftBukkit - BungeeCord#2321, send complete data to self on spawn

        // CraftBukkit start - Only add if the player wasn't moved in the event
        if (entityplayer.field_70170_p == worldserver && !worldserver.field_73010_i.contains(entityplayer)) {
            worldserver.func_72838_d(entityplayer);
            this.func_72375_a(entityplayer, (WorldServer) null);
        }
        // CraftBukkit end
    }

    public void func_72358_d(EntityPlayerMP entityplayer) {
        entityplayer.func_71121_q().func_184164_w().func_72685_d(entityplayer);
    }

    public String disconnect(EntityPlayerMP entityplayer) { // CraftBukkit - return string
        WorldServer worldserver = entityplayer.func_71121_q();

        entityplayer.func_71029_a(StatList.field_75947_j);

        // CraftBukkit start - Quitting must be before we do final save of data, in case plugins need to modify it
        org.bukkit.craftbukkit.event.CraftEventFactory.handleInventoryCloseEvent(entityplayer);

        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.func_70005_c_() + " left the game");
        cserver.getPluginManager().callEvent(playerQuitEvent);
        entityplayer.getBukkitEntity().disconnect(playerQuitEvent.getQuitMessage());

        entityplayer.func_71127_g();// SPIGOT-924
        // CraftBukkit end

        // Paper start - Remove from collideRule team if needed
        if (this.collideRuleTeamName != null) {
            final Scoreboard scoreBoard = this.field_72400_f.func_130014_f_().func_96441_U();
            final ScorePlayerTeam team = scoreBoard.func_96508_e(this.collideRuleTeamName);
            if (entityplayer.getTeam() == team && team != null) {
                scoreBoard.func_96512_b(entityplayer.func_70005_c_(), team);
            }
        }
        // Paper end

        this.func_72391_b(entityplayer);
        if (entityplayer.func_184218_aH()) {
            Entity entity = entityplayer.func_184208_bv();

            if (entity.func_184180_b(EntityPlayerMP.class).size() == 1) {
                PlayerList.field_148546_d.debug("Removing player mount");
                entityplayer.func_184210_p();
                worldserver.func_72973_f(entity);
                Iterator iterator = entity.func_184182_bu().iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    worldserver.func_72973_f(entity1);
                }

                worldserver.func_72964_e(entityplayer.field_70176_ah, entityplayer.field_70164_aj).func_76630_e();
            }
        }

        worldserver.func_72900_e(entityplayer);
        worldserver.func_184164_w().func_72695_c(entityplayer);
        entityplayer.func_192039_O().func_192745_a();
        this.field_72404_b.remove(entityplayer);
        this.playersByName.remove(entityplayer.func_70005_c_()); // Spigot
        UUID uuid = entityplayer.func_110124_au();
        EntityPlayerMP entityplayer1 = (EntityPlayerMP) this.field_177454_f.get(uuid);

        if (entityplayer1 == entityplayer) {
            this.field_177454_f.remove(uuid);
            // CraftBukkit start
            // this.o.remove(uuid);
            // this.p.remove(uuid);
            // CraftBukkit end
        }

        // CraftBukkit start
        //  this.sendAll(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { entityplayer}));
        SPacketPlayerListItem packet = new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, entityplayer);
        for (int i = 0; i < field_72404_b.size(); i++) {
            EntityPlayerMP entityplayer2 = (EntityPlayerMP) this.field_72404_b.get(i);

            if (entityplayer2.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer2.field_71135_a.func_147359_a(packet);
            } else {
                entityplayer2.getBukkitEntity().removeDisconnectingPlayer(entityplayer.getBukkitEntity());
            }
        }
        // This removes the scoreboard (and player reference) for the specific player in the manager
        cserver.getScoreboardManager().removePlayer(entityplayer.getBukkitEntity());
        // CraftBukkit end

        ChunkIOExecutor.adjustPoolSize(this.func_72394_k()); // CraftBukkit

        return playerQuitEvent.getQuitMessage(); // CraftBukkit
    }

    // CraftBukkit start - Whole method, SocketAddress to LoginListener, added hostname to signature, return EntityPlayer
    public EntityPlayerMP attemptLogin(NetHandlerLoginServer loginlistener, GameProfile gameprofile, String hostname) {
        // Moved from processLogin
        UUID uuid = EntityPlayer.func_146094_a(gameprofile);
        ArrayList arraylist = Lists.newArrayList();

        EntityPlayerMP entityplayer;

        for (int i = 0; i < this.field_72404_b.size(); ++i) {
            entityplayer = (EntityPlayerMP) this.field_72404_b.get(i);
            if (entityplayer.func_110124_au().equals(uuid)) {
                arraylist.add(entityplayer);
            }
        }

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayerMP) iterator.next();
            func_72391_b(entityplayer); // CraftBukkit - Force the player's inventory to be saved
            entityplayer.field_71135_a.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.duplicate_login", new Object[0]));
        }

        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome.
        SocketAddress socketaddress = loginlistener.field_147333_a.func_74430_c();

        EntityPlayerMP entity = new EntityPlayerMP(field_72400_f, field_72400_f.func_71218_a(0), gameprofile, new PlayerInteractionManager(field_72400_f.func_71218_a(0)));
        Player player = entity.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(player, hostname, ((java.net.InetSocketAddress) socketaddress).getAddress(), ((java.net.InetSocketAddress) loginlistener.field_147333_a.getRawAddress()).getAddress());
        String s;

        if (func_152608_h().func_152702_a(gameprofile) && !func_152608_h().func_152683_b(gameprofile).func_73682_e()) {
            UserListBansEntry gameprofilebanentry = (UserListBansEntry) this.field_72401_g.func_152683_b(gameprofile);

            s = "You are banned from this server!\nReason: " + gameprofilebanentry.func_73686_f();
            if (gameprofilebanentry.func_73680_d() != null) {
                s = s + "\nYour ban will be removed on " + PlayerList.field_72403_e.format(gameprofilebanentry.func_73680_d());
            }

            // return s;
            if (!gameprofilebanentry.func_73682_e()) event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s); // Spigot
        } else if (!this.isWhitelisted(gameprofile, event)) { // Paper
            // return "You are not white-listed on this server!";
            //event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, org.spigotmc.SpigotConfig.whitelistMessage); // Spigot // Paper - moved to isWhitelisted
        } else if (func_72363_f().func_152708_a(socketaddress) && !func_72363_f().func_152709_b(socketaddress).func_73682_e()) {
            UserListIPBansEntry ipbanentry = this.field_72413_h.func_152709_b(socketaddress);

            s = "Your IP address is banned from this server!\nReason: " + ipbanentry.func_73686_f();
            if (ipbanentry.func_73680_d() != null) {
                s = s + "\nYour ban will be removed on " + PlayerList.field_72403_e.format(ipbanentry.func_73680_d());
            }

            // return s;
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s);
        } else {
            // return this.players.size() >= this.maxPlayers && !this.f(gameprofile) ? "The server is full!" : null;
            if (this.field_72404_b.size() >= this.field_72405_c && !this.func_183023_f(gameprofile)) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, org.spigotmc.SpigotConfig.serverFullMessage); // Spigot
            }
        }

        cserver.getPluginManager().callEvent(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            loginlistener.disconnect(event.getKickMessage());
            return null;
        }
        return entity;
    }

    public EntityPlayerMP processLogin(GameProfile gameprofile, EntityPlayerMP player) { // CraftBukkit - added EntityPlayer
        /* CraftBukkit startMoved up
        UUID uuid = EntityHuman.a(gameprofile);
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(i);

            if (entityplayer.getUniqueID().equals(uuid)) {
                arraylist.add(entityplayer);
            }
        }

        EntityPlayer entityplayer1 = (EntityPlayer) this.j.get(gameprofile.getId());

        if (entityplayer1 != null && !arraylist.contains(entityplayer1)) {
            arraylist.add(entityplayer1);
        }

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer2 = (EntityPlayer) iterator.next();

            entityplayer2.playerConnection.disconnect(new ChatMessage("multiplayer.disconnect.duplicate_login", new Object[0]));
        }

        Object object;

        if (this.server.V()) {
            object = new DemoPlayerInteractManager(this.server.getWorldServer(0));
        } else {
            object = new PlayerInteractManager(this.server.getWorldServer(0));
        }

        return new EntityPlayer(this.server, this.server.getWorldServer(0), gameprofile, (PlayerInteractManager) object);
        */
        return player;
        // CraftBukkit end
    }

    // CraftBukkit start
    public EntityPlayerMP func_72368_a(EntityPlayerMP entityplayer, int i, boolean flag) {
        return this.moveToWorld(entityplayer, i, flag, null, true);
    }

    public EntityPlayerMP moveToWorld(EntityPlayerMP entityplayer, int i, boolean flag, Location location, boolean avoidSuffocation) {
        entityplayer.func_184210_p(); // CraftBukkit
        entityplayer.func_71121_q().func_73039_n().func_72787_a(entityplayer);
        // entityplayer.x().getTracker().untrackEntity(entityplayer); // CraftBukkit
        entityplayer.func_71121_q().func_184164_w().func_72695_c(entityplayer);
        this.field_72404_b.remove(entityplayer);
        this.playersByName.remove(entityplayer.func_70005_c_()); // Spigot
        this.field_72400_f.func_71218_a(entityplayer.field_71093_bK).func_72973_f(entityplayer);
        BlockPos blockposition = entityplayer.func_180470_cg();
        boolean flag1 = entityplayer.func_82245_bX();

        /* CraftBukkit start
        entityplayer.dimension = i;
        Object object;

        if (this.server.V()) {
            object = new DemoPlayerInteractManager(this.server.getWorldServer(entityplayer.dimension));
        } else {
            object = new PlayerInteractManager(this.server.getWorldServer(entityplayer.dimension));
        }

        EntityPlayer entityplayer1 = new EntityPlayer(this.server, this.server.getWorldServer(entityplayer.dimension), entityplayer.getProfile(), (PlayerInteractManager) object);
        // */
        EntityPlayerMP entityplayer1 = entityplayer;
        org.bukkit.World fromWorld = entityplayer.getBukkitEntity().getWorld();
        entityplayer.field_71136_j = false;
        // CraftBukkit end

        entityplayer1.field_71135_a = entityplayer.field_71135_a;
        entityplayer1.func_193104_a(entityplayer, flag);
        entityplayer1.func_145769_d(entityplayer.func_145782_y());
        entityplayer1.func_174817_o(entityplayer);
        entityplayer1.func_184819_a(entityplayer.func_184591_cq());
        Iterator iterator = entityplayer.func_184216_O().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            entityplayer1.func_184211_a(s);
        }

        // WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);  // CraftBukkit - handled later

        // this.a(entityplayer1, entityplayer, worldserver); // CraftBukkit - removed
        BlockPos blockposition1;

        // CraftBukkit start - fire PlayerRespawnEvent
        if (location == null) {
            boolean isBedSpawn = false;
            CraftWorld cworld = (CraftWorld) this.field_72400_f.server.getWorld(entityplayer.spawnWorld);
            if (cworld != null && blockposition != null) {
                blockposition1 = EntityPlayer.func_180467_a(cworld.getHandle(), blockposition, flag1);
                if (blockposition1 != null) {
                    isBedSpawn = true;
                    location = new Location(cworld, (double) ((float) blockposition1.func_177958_n() + 0.5F), (double) ((float) blockposition1.func_177956_o() + 0.1F), (double) ((float) blockposition1.func_177952_p() + 0.5F));
                } else {
                    entityplayer1.func_180473_a(null, true);
                    entityplayer1.field_71135_a.func_147359_a(new SPacketChangeGameState(0, 0.0F));
                }
            }

            if (location == null) {
                cworld = (CraftWorld) this.field_72400_f.server.getWorlds().get(0);
                blockposition = entityplayer1.getSpawnPoint(this.field_72400_f, cworld.getHandle());
                location = new Location(cworld, (double) ((float) blockposition.func_177958_n() + 0.5F), (double) ((float) blockposition.func_177956_o() + 0.1F), (double) ((float) blockposition.func_177952_p() + 0.5F));
            }

            Player respawnPlayer = cserver.getPlayer(entityplayer1);
            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, location, isBedSpawn);
            cserver.getPluginManager().callEvent(respawnEvent);
            // Spigot Start
            if (entityplayer.field_71135_a.isDisconnected()) {
                return entityplayer;
            }
            // Spigot End

            location = respawnEvent.getRespawnLocation();
            entityplayer.reset();
        } else {
            location.setWorld(field_72400_f.func_71218_a(i).getWorld());
        }
        WorldServer worldserver = ((CraftWorld) location.getWorld()).getHandle();
        entityplayer1.forceSetPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // CraftBukkit end

        worldserver.func_72863_F().func_186025_d((int) entityplayer1.field_70165_t >> 4, (int) entityplayer1.field_70161_v >> 4);

        while (avoidSuffocation && !worldserver.func_184144_a(entityplayer1, entityplayer1.func_174813_aQ()).isEmpty() && entityplayer1.field_70163_u < 256.0D) {
            entityplayer1.func_70107_b(entityplayer1.field_70165_t, entityplayer1.field_70163_u + 1.0D, entityplayer1.field_70161_v);
        }
        // CraftBukkit start
        byte actualDimension = (byte) (worldserver.getWorld().getEnvironment().getId());
        // Force the client to refresh their chunk cache
        if (fromWorld.getEnvironment() == worldserver.getWorld().getEnvironment()) {
            entityplayer1.field_71135_a.func_147359_a(new SPacketRespawn((byte) (actualDimension >= 0 ? -1 : 0), worldserver.func_175659_aa(), worldserver.func_72912_H().func_76067_t(), entityplayer.field_71134_c.func_73081_b()));
        }

        entityplayer1.field_71135_a.func_147359_a(new SPacketRespawn(actualDimension, worldserver.func_175659_aa(), worldserver.func_72912_H().func_76067_t(), entityplayer1.field_71134_c.func_73081_b()));
        entityplayer1.func_70029_a(worldserver);
        entityplayer1.field_70128_L = false;
        entityplayer1.field_71135_a.teleport(new Location(worldserver.getWorld(), entityplayer1.field_70165_t, entityplayer1.field_70163_u, entityplayer1.field_70161_v, entityplayer1.field_70177_z, entityplayer1.field_70125_A));
        entityplayer1.func_70095_a(false);
        blockposition1 = worldserver.func_175694_M();
        // entityplayer1.playerConnection.a(entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);
        entityplayer1.field_71135_a.func_147359_a(new SPacketSpawnPosition(blockposition1));
        entityplayer1.field_71135_a.func_147359_a(new SPacketSetExperience(entityplayer1.field_71106_cc, entityplayer1.field_71067_cb, entityplayer1.field_71068_ca));
        this.func_72354_b(entityplayer1, worldserver);
        this.func_187243_f(entityplayer1);
        if (!entityplayer.field_71135_a.isDisconnected()) {
            worldserver.func_184164_w().func_72683_a(entityplayer1);
            worldserver.func_72838_d(entityplayer1);
            this.field_72404_b.add(entityplayer1);
            this.playersByName.put(entityplayer1.func_70005_c_(), entityplayer1); // Spigot
            this.field_177454_f.put(entityplayer1.func_110124_au(), entityplayer1);
        }
        // entityplayer1.syncInventory();
        entityplayer1.func_70606_j(entityplayer1.func_110143_aJ());
        // Added from changeDimension
        func_72385_f(entityplayer); // Update health, etc...
        entityplayer.func_71016_p();
        for (Object o1 : entityplayer.func_70651_bq()) {
            PotionEffect mobEffect = (PotionEffect) o1;
            entityplayer.field_71135_a.func_147359_a(new SPacketEntityEffect(entityplayer.func_145782_y(), mobEffect));
        }

        // Fire advancement trigger
        CriteriaTriggers.field_193134_u.func_193143_a(entityplayer, ((CraftWorld) fromWorld).getHandle().field_73011_w.func_186058_p(), worldserver.field_73011_w.func_186058_p());
        if (((CraftWorld) fromWorld).getHandle().field_73011_w.func_186058_p() == DimensionType.NETHER && worldserver.field_73011_w.func_186058_p() == DimensionType.OVERWORLD && entityplayer.func_193106_Q() != null) {
            CriteriaTriggers.field_193131_B.func_193168_a(entityplayer, entityplayer.func_193106_Q());
        }

        // Don't fire on respawn
        if (fromWorld != location.getWorld()) {
            PlayerChangedWorldEvent event = new PlayerChangedWorldEvent(entityplayer.getBukkitEntity(), fromWorld);
            field_72400_f.server.getPluginManager().callEvent(event);
        }

        // Save player file again if they were disconnected
        if (entityplayer.field_71135_a.isDisconnected()) {
            this.func_72391_b(entityplayer);
        }
        // CraftBukkit end
        return entityplayer1;
    }

    // CraftBukkit start - Replaced the standard handling of portals with a more customised method.
    public void changeDimension(EntityPlayerMP entityplayer, int i, TeleportCause cause) {
        WorldServer exitWorld = null;
        if (entityplayer.field_71093_bK < CraftWorld.CUSTOM_DIMENSION_OFFSET) { // plugins must specify exit from custom Bukkit worlds
            // only target existing worlds (compensate for allow-nether/allow-end as false)
            for (WorldServer world : this.field_72400_f.worlds) {
                if (world.dimension == i) {
                    exitWorld = world;
                }
            }
        }

        Location enter = entityplayer.getBukkitEntity().getLocation();
        Location exit = null;
        boolean useTravelAgent = false; // don't use agent for custom worlds or return from THE_END
        if (exitWorld != null) {
            if ((cause == TeleportCause.END_PORTAL) && (i == 0)) {
                // THE_END -> NORMAL; use bed if available, otherwise default spawn
                exit = ((org.bukkit.craftbukkit.entity.CraftPlayer) entityplayer.getBukkitEntity()).getBedSpawnLocation();
                if (exit == null || ((CraftWorld) exit.getWorld()).getHandle().dimension != 0) {
                    BlockPos randomSpawn = entityplayer.getSpawnPoint(field_72400_f, exitWorld);
                    exit = new Location(exitWorld.getWorld(), randomSpawn.func_177958_n(), randomSpawn.func_177956_o(), randomSpawn.func_177952_p());
                } else {
                    exit = exit.add(0.5F, 0.1F, 0.5F); // SPIGOT-3879
                }
            } else {
                // NORMAL <-> NETHER or NORMAL -> THE_END
                exit = this.calculateTarget(enter, exitWorld);
                useTravelAgent = true;
            }
        }

        TravelAgent agent = exit != null ? (TravelAgent) ((CraftWorld) exit.getWorld()).getHandle().func_85176_s() : org.bukkit.craftbukkit.CraftTravelAgent.DEFAULT; // return arbitrary TA to compensate for implementation dependent plugins
        PlayerPortalEvent event = new PlayerPortalEvent(entityplayer.getBukkitEntity(), enter, exit, agent, cause);
        event.useTravelAgent(useTravelAgent);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled() || event.getTo() == null) {
            return;
        }

        exit = event.useTravelAgent() ? event.getPortalTravelAgent().findOrCreate(event.getTo()) : event.getTo();
        if (exit == null) {
            return;
        }
        exitWorld = ((CraftWorld) exit.getWorld()).getHandle();

        org.bukkit.event.player.PlayerTeleportEvent tpEvent = new org.bukkit.event.player.PlayerTeleportEvent(entityplayer.getBukkitEntity(), enter, exit, cause);
        Bukkit.getServer().getPluginManager().callEvent(tpEvent);
        if (tpEvent.isCancelled() || tpEvent.getTo() == null) {
            return;
        }

        Vector velocity = entityplayer.getBukkitEntity().getVelocity();
        exitWorld.func_85176_s().adjustExit(entityplayer, exit, velocity);

        entityplayer.field_184851_cj = true; // CraftBukkit - Set teleport invulnerability only if player changing worlds
        this.moveToWorld(entityplayer, exitWorld.dimension, true, exit, true); // SPIGOT-3864
        if (entityplayer.field_70159_w != velocity.getX() || entityplayer.field_70181_x != velocity.getY() || entityplayer.field_70179_y != velocity.getZ()) {
            entityplayer.getBukkitEntity().setVelocity(velocity);
        }
    }

    public void func_187243_f(EntityPlayerMP entityplayer) {
        GameProfile gameprofile = entityplayer.func_146103_bH();
        int i = this.func_152596_g(gameprofile) ? this.field_72414_i.func_152681_a(gameprofile) : 0;

        i = this.field_72400_f.func_71264_H() && this.field_72400_f.field_71305_c[0].func_72912_H().func_76086_u() ? 4 : i;
        i = this.field_72407_n ? 4 : i;
        this.func_187245_a(entityplayer, i);
    }

    public void func_187242_a(EntityPlayerMP entityplayer, int i) {
        int j = entityplayer.field_71093_bK;
        WorldServer worldserver = this.field_72400_f.func_71218_a(entityplayer.field_71093_bK);

        entityplayer.field_71093_bK = i;
        WorldServer worldserver1 = this.field_72400_f.func_71218_a(entityplayer.field_71093_bK);

        entityplayer.field_71135_a.func_147359_a(new SPacketRespawn(entityplayer.field_71093_bK, entityplayer.field_70170_p.func_175659_aa(), entityplayer.field_70170_p.func_72912_H().func_76067_t(), entityplayer.field_71134_c.func_73081_b()));
        this.func_187243_f(entityplayer);
        worldserver.func_72973_f(entityplayer);
        entityplayer.field_70128_L = false;
        this.func_82448_a(entityplayer, j, worldserver, worldserver1);
        this.func_72375_a(entityplayer, worldserver);
        entityplayer.field_71135_a.func_147364_a(entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v, entityplayer.field_70177_z, entityplayer.field_70125_A);
        entityplayer.field_71134_c.func_73080_a(worldserver1);
        entityplayer.field_71135_a.func_147359_a(new SPacketPlayerAbilities(entityplayer.field_71075_bZ));
        this.func_72354_b(entityplayer, worldserver1);
        this.func_72385_f(entityplayer);
        Iterator iterator = entityplayer.func_70651_bq().iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            entityplayer.field_71135_a.func_147359_a(new SPacketEntityEffect(entityplayer.func_145782_y(), mobeffect));
        }

    }

    public void func_82448_a(Entity entity, int i, WorldServer worldserver, WorldServer worldserver1) {
        // CraftBukkit start - Split into modular functions
        Location exit = calculateTarget(entity.getBukkitEntity().getLocation(), worldserver1);
        repositionEntity(entity, exit, true);
    }

    // Copy of original changeWorld(Entity, int, WorldServer, WorldServer) method with only location calculation logic
    public Location calculateTarget(Location enter, World target) {
        WorldServer worldserver = ((CraftWorld) enter.getWorld()).getHandle();
        WorldServer worldserver1 = ((CraftWorld) target.getWorld()).getHandle();
        int i = worldserver.dimension;

        double y = enter.getY();
        float yaw = enter.getYaw();
        float pitch = enter.getPitch();
        double d0 = enter.getX();
        double d1 = enter.getZ();
         double d2 = 8.0D;
        /*
        double d0 = entity.locX;
        double d1 = entity.locZ;
        double d2 = 8.0D;
        float f = entity.yaw;
        */

        worldserver.field_72984_F.func_76320_a("moving");
        if (worldserver1.dimension == -1) {
            d0 = MathHelper.func_151237_a(d0 / d2, worldserver1.func_175723_af().func_177726_b()+ 16.0D, worldserver1.func_175723_af().func_177728_d() - 16.0D);
            d1 = MathHelper.func_151237_a(d1 / d2, worldserver1.func_175723_af().func_177736_c() + 16.0D, worldserver1.func_175723_af().func_177733_e() - 16.0D);
            /*
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        } else if (worldserver1.dimension == 0) {
            d0 = MathHelper.func_151237_a(d0 * d2, worldserver1.func_175723_af().func_177726_b() + 16.0D, worldserver1.func_175723_af().func_177728_d() - 16.0D);
            d1 = MathHelper.func_151237_a(d1 * d2, worldserver1.func_175723_af().func_177736_c() + 16.0D, worldserver1.func_175723_af().func_177733_e() - 16.0D);
            /*
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        } else {
            BlockPos blockposition;

            if (i == 1) {
                // use default NORMAL world spawn instead of target
                worldserver1 = this.field_72400_f.worlds.get(0);
                blockposition = worldserver1.func_175694_M();
            } else {
                blockposition = worldserver1.func_180504_m();
            }

            d0 = (double) blockposition.func_177958_n();
            y = (double) blockposition.func_177956_o();
            d1 = (double) blockposition.func_177952_p();
            /*
            entity.setPositionRotation(d0, entity.locY, d1, 90.0F, 0.0F);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        }

        worldserver.field_72984_F.func_76319_b();
        if (i != 1) {
            worldserver.field_72984_F.func_76320_a("placing");
            d0 = (double) MathHelper.func_76125_a((int) d0, -29999872, 29999872);
            d1 = (double) MathHelper.func_76125_a((int) d1, -29999872, 29999872);
            /*
            if (entity.isAlive()) {
                entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
                worldserver1.getTravelAgent().a(entity, f);
                worldserver1.addEntity(entity);
                worldserver1.entityJoinedWorld(entity, false);
            }
            */

            worldserver.field_72984_F.func_76319_b();
        }

        // entity.spawnIn(worldserver1);
        return new Location(worldserver1.getWorld(), d0, y, d1, yaw, pitch);
    }

    // copy of original a(Entity, int, WorldServer, WorldServer) method with only entity repositioning logic
    public void repositionEntity(Entity entity, Location exit, boolean portal) {
        WorldServer worldserver = (WorldServer) entity.field_70170_p;
        WorldServer worldserver1 = ((CraftWorld) exit.getWorld()).getHandle();
        int i = worldserver.dimension;

        /*
        double d0 = entity.locX;
        double d1 = entity.locZ;
        double d2 = 8.0D;
        float f = entity.yaw;
        */

        worldserver.field_72984_F.func_76320_a("moving");
        entity.func_70012_b(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
        if (entity.func_70089_S()) {
            worldserver.func_72866_a(entity, false);
        }
        /*
        if (entity.dimension == -1) {
            d0 = MathHelper.a(d0 / 8.0D, worldserver1.getWorldBorder().b() + 16.0D, worldserver1.getWorldBorder().d() - 16.0D);
            d1 = MathHelper.a(d1 / 8.0D, worldserver1.getWorldBorder().c() + 16.0D, worldserver1.getWorldBorder().e() - 16.0D);
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
        } else if (entity.dimension == 0) {
            d0 = MathHelper.a(d0 * 8.0D, worldserver1.getWorldBorder().b() + 16.0D, worldserver1.getWorldBorder().d() - 16.0D);
            d1 = MathHelper.a(d1 * 8.0D, worldserver1.getWorldBorder().c() + 16.0D, worldserver1.getWorldBorder().e() - 16.0D);
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
        } else {
            BlockPosition blockposition;

            if (i == 1) {
                // use default NORMAL world spawn instead of target
                worldserver1 = this.server.worlds.get(0);
                blockposition = worldserver1.getSpawn();
            } else {
                blockposition = worldserver1.getDimensionSpawn();
            }

            d0 = (double) blockposition.getX();
            entity.locY = (double) blockposition.getY();
            d1 = (double) blockposition.getZ();
            entity.setPositionRotation(d0, entity.locY, d1, 90.0F, 0.0F);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
        }
        */

        worldserver.field_72984_F.func_76319_b();
        if (i != 1) {
            worldserver.field_72984_F.func_76320_a("placing");
            /*
            d0 = (double) MathHelper.clamp((int) d0, -29999872, 29999872);
            d1 = (double) MathHelper.clamp((int) d1, -29999872, 29999872);
            */
            if (entity.func_70089_S()) {
                // entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
                // worldserver1.getTravelAgent().a(entity, f);
                if (portal) {
                    Vector velocity = entity.getBukkitEntity().getVelocity();
                    worldserver1.func_85176_s().adjustExit(entity, exit, velocity);
                    entity.func_70012_b(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
                    if (entity.field_70159_w != velocity.getX() || entity.field_70181_x != velocity.getY() || entity.field_70179_y != velocity.getZ()) {
                        entity.getBukkitEntity().setVelocity(velocity);
                    }
                }
                // worldserver1.addEntity(entity);
                worldserver1.func_72866_a(entity, false);
            }

            worldserver.field_72984_F.func_76319_b();
        }

        entity.func_70029_a(worldserver1);
        // CraftBukkit end
    }

    public void func_72374_b() {
        if (++this.field_72408_o > 600) {
            // CraftBukkit start
            for (int i = 0; i < this.field_72404_b.size(); ++i) {
                final EntityPlayerMP target = (EntityPlayerMP) this.field_72404_b.get(i);

                target.field_71135_a.func_147359_a(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_LATENCY, Iterables.filter(this.field_72404_b, new Predicate<EntityPlayerMP>() {
                    @Override
                    public boolean apply(EntityPlayerMP input) {
                        return target.getBukkitEntity().canSee(input.getBukkitEntity());
                    }
                })));
            }
            // CraftBukkit end
            this.field_72408_o = 0;
        }

    }

    public void func_148540_a(Packet<?> packet) {
        for (int i = 0; i < this.field_72404_b.size(); ++i) {
            ((EntityPlayerMP) this.field_72404_b.get(i)).field_71135_a.func_147359_a(packet);
        }

    }

    // CraftBukkit start - add a world/entity limited version
    public void sendAll(Packet packet, EntityPlayer entityhuman) {
        for (int i = 0; i < this.field_72404_b.size(); ++i) {
            EntityPlayerMP entityplayer =  this.field_72404_b.get(i);
            if (entityhuman != null && entityhuman instanceof EntityPlayerMP && !entityplayer.getBukkitEntity().canSee(((EntityPlayerMP) entityhuman).getBukkitEntity())) {
                continue;
            }
            ((EntityPlayerMP) this.field_72404_b.get(i)).field_71135_a.func_147359_a(packet);
        }
    }

    public void sendAll(Packet packet, World world) {
        for (int i = 0; i < world.field_73010_i.size(); ++i) {
            ((EntityPlayerMP) world.field_73010_i.get(i)).field_71135_a.func_147359_a(packet);
        }

    }
    // CraftBukkit end

    public void func_148537_a(Packet<?> packet, int i) {
        for (int j = 0; j < this.field_72404_b.size(); ++j) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) this.field_72404_b.get(j);

            if (entityplayer.field_71093_bK == i) {
                entityplayer.field_71135_a.func_147359_a(packet);
            }
        }

    }

    public void func_177453_a(EntityPlayer entityhuman, ITextComponent ichatbasecomponent) {
        Team scoreboardteambase = entityhuman.func_96124_cp();

        if (scoreboardteambase != null) {
            Collection collection = scoreboardteambase.func_96670_d();
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                EntityPlayerMP entityplayer = this.func_152612_a(s);

                if (entityplayer != null && entityplayer != entityhuman) {
                    entityplayer.func_145747_a(ichatbasecomponent);
                }
            }

        }
    }

    public void func_177452_b(EntityPlayer entityhuman, ITextComponent ichatbasecomponent) {
        Team scoreboardteambase = entityhuman.func_96124_cp();

        if (scoreboardteambase == null) {
            this.func_148539_a(ichatbasecomponent);
        } else {
            for (int i = 0; i < this.field_72404_b.size(); ++i) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) this.field_72404_b.get(i);

                if (entityplayer.func_96124_cp() != scoreboardteambase) {
                    entityplayer.func_145747_a(ichatbasecomponent);
                }
            }

        }
    }

    public String func_181058_b(boolean flag) {
        String s = "";
        ArrayList arraylist = Lists.newArrayList(this.field_72404_b);

        for (int i = 0; i < arraylist.size(); ++i) {
            if (i > 0) {
                s = s + ", ";
            }

            s = s + ((EntityPlayerMP) arraylist.get(i)).func_70005_c_();
            if (flag) {
                s = s + " (" + ((EntityPlayerMP) arraylist.get(i)).func_189512_bd() + ")";
            }
        }

        return s;
    }

    public String[] func_72369_d() {
        String[] astring = new String[this.field_72404_b.size()];

        for (int i = 0; i < this.field_72404_b.size(); ++i) {
            astring[i] = ((EntityPlayerMP) this.field_72404_b.get(i)).func_70005_c_();
        }

        return astring;
    }

    public GameProfile[] func_152600_g() {
        GameProfile[] agameprofile = new GameProfile[this.field_72404_b.size()];

        for (int i = 0; i < this.field_72404_b.size(); ++i) {
            agameprofile[i] = ((EntityPlayerMP) this.field_72404_b.get(i)).func_146103_bH();
        }

        return agameprofile;
    }

    public UserListBans func_152608_h() {
        return this.field_72401_g;
    }

    public UserListIPBans func_72363_f() {
        return this.field_72413_h;
    }

    public void func_152605_a(GameProfile gameprofile) {
        int i = this.field_72400_f.func_110455_j();

        this.field_72414_i.func_152687_a(new UserListOpsEntry(gameprofile, this.field_72400_f.func_110455_j(), this.field_72414_i.func_183026_b(gameprofile)));
        this.func_187245_a(this.func_177451_a(gameprofile.getId()), i);
        // CraftBukkit start
        Player player = field_72400_f.server.getPlayer(gameprofile.getId());
        if (player != null) {
           player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    public void func_152610_b(GameProfile gameprofile) {
        this.field_72414_i.func_152684_c(gameprofile);
        this.func_187245_a(this.func_177451_a(gameprofile.getId()), 0);
        // CraftBukkit start
        Player player = field_72400_f.server.getPlayer(gameprofile.getId());
        if (player != null) {
            player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    private void func_187245_a(EntityPlayerMP entityplayer, int i) {
        if (entityplayer != null && entityplayer.field_71135_a != null) {
            byte b0;

            if (i <= 0) {
                b0 = 24;
            } else if (i >= 4) {
                b0 = 28;
            } else {
                b0 = (byte) (24 + i);
            }

            entityplayer.field_71135_a.func_147359_a(new SPacketEntityStatus(entityplayer, b0));
        }

    }

    // Paper start
    public boolean func_152607_e(GameProfile gameprofile) {
        return isWhitelisted(gameprofile, null);
    }
    public boolean isWhitelisted(GameProfile gameprofile, org.bukkit.event.player.PlayerLoginEvent loginEvent) {
        boolean isOp = this.field_72414_i.func_152692_d(gameprofile);
        boolean isWhitelisted = !this.field_72409_l || isOp || this.field_72411_j.func_152692_d(gameprofile);
        final com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent event;
        event = new com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent(MCUtil.toBukkit(gameprofile), this.field_72409_l, isWhitelisted, isOp, org.spigotmc.SpigotConfig.whitelistMessage);
        event.callEvent();
        if (!event.isWhitelisted()) {
            if (loginEvent != null) {
                loginEvent.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, event.getKickMessage() == null ? org.spigotmc.SpigotConfig.whitelistMessage : event.getKickMessage());
            }
            return false;
        }
        return true;
    }
    // Paper end

    public boolean func_152596_g(GameProfile gameprofile) {
        return this.field_72414_i.func_152692_d(gameprofile) || this.field_72400_f.func_71264_H() && this.field_72400_f.worlds.get(0).func_72912_H().func_76086_u() && this.field_72400_f.func_71214_G().equalsIgnoreCase(gameprofile.getName()) || this.field_72407_n; // CraftBukkit
    }

    @Nullable
    public EntityPlayerMP func_152612_a(String s) {
        return this.playersByName.get(s); // Spigot
    }

    public void func_148543_a(@Nullable EntityPlayer entityhuman, double d0, double d1, double d2, double d3, int i, Packet<?> packet) {
        for (int j = 0; j < this.field_72404_b.size(); ++j) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) this.field_72404_b.get(j);

            // CraftBukkit start - Test if player receiving packet can see the source of the packet
            if (entityhuman != null && entityhuman instanceof EntityPlayerMP && !entityplayer.getBukkitEntity().canSee(((EntityPlayerMP) entityhuman).getBukkitEntity())) {
               continue;
            }
            // CraftBukkit end

            if (entityplayer != entityhuman && entityplayer.field_71093_bK == i) {
                double d4 = d0 - entityplayer.field_70165_t;
                double d5 = d1 - entityplayer.field_70163_u;
                double d6 = d2 - entityplayer.field_70161_v;

                if (d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3) {
                    entityplayer.field_71135_a.func_147359_a(packet);
                }
            }
        }

    }

    // Paper start
    public void func_72389_g() {
        savePlayers(null);
    }

    public void savePlayers(Integer interval) {
        MCUtil.ensureMain("Save Players", () -> { // Paper - ensure main
        long now = MinecraftServer.currentTick;
        MinecraftTimings.savePlayers.startTiming(); // Paper
        int numSaved = 0; // Paper
        for (int i = 0; i < this.field_72404_b.size(); ++i) {
            EntityPlayerMP entityplayer = this.field_72404_b.get(i);
            if (interval == null || now - entityplayer.lastSave >= interval) {
                this.func_72391_b(entityplayer);
                if (interval != null && ++numSaved <= com.destroystokyo.paper.PaperConfig.maxPlayerAutoSavePerTick) { break; } // Paper
            }
        }
        MinecraftTimings.savePlayers.stopTiming(); // Paper
        return null; }); // Paper - ensure main
    }
    // Paper end

    public void func_152601_d(GameProfile gameprofile) {
        this.field_72411_j.func_152687_a(new UserListWhitelistEntry(gameprofile));
    }

    public void func_152597_c(GameProfile gameprofile) {
        this.field_72411_j.func_152684_c(gameprofile);
    }

    public UserListWhitelist func_152599_k() {
        return this.field_72411_j;
    }

    public String[] func_152598_l() {
        return this.field_72411_j.func_152685_a();
    }

    public UserListOps func_152603_m() {
        return this.field_72414_i;
    }

    public String[] func_152606_n() {
        return this.field_72414_i.func_152685_a();
    }

    public void func_187244_a() {}

    public void func_72354_b(EntityPlayerMP entityplayer, WorldServer worldserver) {
        WorldBorder worldborder = entityplayer.field_70170_p.func_175723_af(); // CraftBukkit

        entityplayer.field_71135_a.func_147359_a(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.INITIALIZE));
        entityplayer.field_71135_a.func_147359_a(new SPacketTimeUpdate(worldserver.func_82737_E(), worldserver.func_72820_D(), worldserver.func_82736_K().func_82766_b("doDaylightCycle")));
        BlockPos blockposition = worldserver.func_175694_M();

        entityplayer.field_71135_a.func_147359_a(new SPacketSpawnPosition(blockposition));
        if (worldserver.func_72896_J()) {
            // CraftBukkit start - handle player weather
            // entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(1, 0.0F));
            // entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(7, worldserver.j(1.0F)));
            // entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(8, worldserver.h(1.0F)));
            entityplayer.setPlayerWeather(org.bukkit.WeatherType.DOWNFALL, false);
            entityplayer.updateWeather(-worldserver.field_73004_o, worldserver.field_73004_o, -worldserver.field_73017_q, worldserver.field_73017_q);
            // CraftBukkit end
        }

    }

    public void func_72385_f(EntityPlayerMP entityplayer) {
        entityplayer.func_71120_a(entityplayer.field_71069_bz);
        // entityplayer.triggerHealthUpdate();
        entityplayer.getBukkitEntity().updateScaledHealth(); // CraftBukkit - Update scaled health on respawn and worldchange
        entityplayer.field_71135_a.func_147359_a(new SPacketHeldItemChange(entityplayer.field_71071_by.field_70461_c));
    }

    public int func_72394_k() {
        return this.field_72404_b.size();
    }

    public int func_72352_l() {
        return this.field_72405_c;
    }

    public String[] func_72373_m() {
        return this.field_72400_f.worlds.get(0).func_72860_G().func_75756_e().func_75754_f(); // CraftBukkit
    }

    public boolean func_72383_n() {
        return this.field_72409_l;
    }

    public void func_72371_a(boolean flag) {
        this.field_72409_l = flag;
    }

    public List<EntityPlayerMP> func_72382_j(String s) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_72404_b.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            if (entityplayer.func_71114_r().equals(s)) {
                arraylist.add(entityplayer);
            }
        }

        return arraylist;
    }

    public int func_72395_o() {
        return this.field_72402_d;
    }

    public MinecraftServer func_72365_p() {
        return this.field_72400_f;
    }

    public NBTTagCompound func_72378_q() {
        return null;
    }

    private void func_72381_a(EntityPlayerMP entityplayer, EntityPlayerMP entityplayer1, World world) {
        if (entityplayer1 != null) {
            entityplayer.field_71134_c.func_73076_a(entityplayer1.field_71134_c.func_73081_b());
        } else if (this.field_72410_m != null) {
            entityplayer.field_71134_c.func_73076_a(this.field_72410_m);
        }

        entityplayer.field_71134_c.func_73077_b(world.func_72912_H().func_76077_q());
    }

    // Paper start - Extract method to allow for restarting flag
    public void func_72392_r() {
        u(false);
    }

    public void u(boolean isRestarting) {
        // CraftBukkit start - disconnect safely
        for (EntityPlayerMP player : this.field_72404_b) {
            player.field_71135_a.disconnect(!isRestarting ? this.field_72400_f.server.getShutdownMessage() : org.spigotmc.SpigotConfig.restartMessage); // CraftBukkit - add custom shutdown message // Paper - add isRestarting flag
        }
        // CraftBukkit end
        // Paper start - Remove collideRule team if it exists
        if (this.collideRuleTeamName != null) {
            final Scoreboard scoreboard = this.func_72365_p().func_130014_f_().func_96441_U();
            final ScorePlayerTeam team = scoreboard.func_96508_e(this.collideRuleTeamName);
            if (team != null) scoreboard.func_96511_d(team);
        }
        // Paper end
    }
    // Paper end

    // CraftBukkit start
    public void sendMessage(ITextComponent[] iChatBaseComponents) {
        for (ITextComponent component : iChatBaseComponents) {
            func_148544_a(component, true);
        }
    }
    // CraftBukkit end

    public void func_148544_a(ITextComponent ichatbasecomponent, boolean flag) {
        this.field_72400_f.func_145747_a(ichatbasecomponent);
        ChatType chatmessagetype = flag ? ChatType.SYSTEM : ChatType.CHAT;

        // CraftBukkit start - we run this through our processor first so we can get web links etc
        this.func_148540_a(new SPacketChat(CraftChatMessage.fixComponent(ichatbasecomponent), chatmessagetype));
        // CraftBukkit end
    }

    public void func_148539_a(ITextComponent ichatbasecomponent) {
        this.func_148544_a(ichatbasecomponent, true);
    }

    public StatisticsManagerServer getStatisticManager(EntityPlayerMP entityhuman) {
        UUID uuid = entityhuman.func_110124_au();
        StatisticsManagerServer serverstatisticmanager = uuid == null ? null : (StatisticsManagerServer) entityhuman.func_147099_x();
        // CraftBukkit end

        if (serverstatisticmanager == null) {
            File file = new File(this.field_72400_f.func_71218_a(0).func_72860_G().func_75765_b(), "stats");
            File file1 = new File(file, uuid + ".json");

            if (!file1.exists()) {
                File file2 = new File(file, entityhuman.func_70005_c_() + ".json");

                if (file2.exists() && file2.isFile()) {
                    file2.renameTo(file1);
                }
            }

            serverstatisticmanager = new StatisticsManagerServer(this.field_72400_f, file1);
            serverstatisticmanager.func_150882_a();
            // this.o.put(uuid, serverstatisticmanager); // CraftBukkit
        }

        return serverstatisticmanager;
    }

    public PlayerAdvancements func_192054_h(EntityPlayerMP entityplayer) {
        UUID uuid = entityplayer.func_110124_au();
        PlayerAdvancements advancementdataplayer = (PlayerAdvancements) entityplayer.func_192039_O(); // CraftBukkit

        if (advancementdataplayer == null) {
            File file = new File(this.field_72400_f.func_71218_a(0).func_72860_G().func_75765_b(), "advancements");
            File file1 = new File(file, uuid + ".json");

            advancementdataplayer = new PlayerAdvancements(this.field_72400_f, file1, entityplayer);
            // this.p.put(uuid, advancementdataplayer); // CraftBukkit
        }

        advancementdataplayer.func_192739_a(entityplayer);
        return advancementdataplayer;
    }

    public void func_152611_a(int i) {
        this.field_72402_d = i;
        if (this.field_72400_f.field_71305_c != null) {
            WorldServer[] aworldserver = this.field_72400_f.field_71305_c;
            int j = aworldserver.length;

            // CraftBukkit start
            for (int k = 0; k < field_72400_f.worlds.size(); ++k) {
                WorldServer worldserver = field_72400_f.worlds.get(0);
                // CraftBukkit end

                if (worldserver != null) {
                    worldserver.func_184164_w().func_152622_a(i);
                    worldserver.func_73039_n().func_187252_a(i);
                }
            }

        }
    }

    public List<EntityPlayerMP> func_181057_v() {
        return this.field_72404_b;
    }

    public EntityPlayerMP func_177451_a(UUID uuid) {
        return (EntityPlayerMP) this.field_177454_f.get(uuid);
    }

    public boolean func_183023_f(GameProfile gameprofile) {
        return false;
    }

    public void func_193244_w() {
        // CraftBukkit start
        /*Iterator iterator = this.p.values().iterator();

        while (iterator.hasNext()) {
            AdvancementDataPlayer advancementdataplayer = (AdvancementDataPlayer) iterator.next();

            advancementdataplayer.b();
        }*/

        for (EntityPlayerMP player : field_72404_b) {
            player.func_192039_O().func_193766_b();
            player.func_192039_O().func_192741_b(player); // CraftBukkit - trigger immediate flush of advancements
        }
        // CraftBukkit end

    }
}

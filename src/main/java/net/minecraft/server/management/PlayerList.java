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

    public static final File FILE_PLAYERBANS = new File("banned-players.json");
    public static final File FILE_IPBANS = new File("banned-ips.json");
    public static final File FILE_OPS = new File("ops.json");
    public static final File FILE_WHITELIST = new File("whitelist.json");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
    private final MinecraftServer mcServer;
    public final List<EntityPlayerMP> playerEntityList = new java.util.concurrent.CopyOnWriteArrayList(); // CraftBukkit - ArrayList -> CopyOnWriteArrayList: Iterator safety
    private final Map<UUID, EntityPlayerMP> uuidToPlayerMap = Maps.newHashMap();
    private final UserListBans bannedPlayers;
    private final UserListIPBans bannedIPs;
    private final UserListOps ops;
    private final UserListWhitelist whiteListedPlayers;
    // CraftBukkit start
    // private final Map<UUID, ServerStatisticManager> o;
    // private final Map<UUID, AdvancementDataPlayer> p;
    // CraftBukkit end
    public IPlayerFileData playerDataManager;
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;

    // CraftBukkit start
    private CraftServer cserver;
    private final Map<String,EntityPlayerMP> playersByName = new org.spigotmc.CaseInsensitiveMap<EntityPlayerMP>();
    @Nullable
    public String collideRuleTeamName; // Paper - Team name used for collideRule

    public PlayerList(MinecraftServer minecraftserver) {
        this.cserver = minecraftserver.server = new CraftServer(minecraftserver, this);
        minecraftserver.console = new com.destroystokyo.paper.console.TerminalConsoleCommandSender(); // Paper
        // CraftBukkit end

        this.bannedPlayers = new UserListBans(PlayerList.FILE_PLAYERBANS);
        this.bannedIPs = new UserListIPBans(PlayerList.FILE_IPBANS);
        this.ops = new UserListOps(PlayerList.FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(PlayerList.FILE_WHITELIST);
        // CraftBukkit start
        // this.o = Maps.newHashMap();
        // this.p = Maps.newHashMap();
        // CraftBukkit end
        this.mcServer = minecraftserver;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }

    public void initializeConnectionToPlayer(NetworkManager networkmanager, EntityPlayerMP entityplayer) {
        GameProfile gameprofile = entityplayer.getGameProfile();
        PlayerProfileCache usercache = this.mcServer.getPlayerProfileCache();
        GameProfile gameprofile1 = usercache.getProfileByUUID(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();

        usercache.addEntry(gameprofile);
        NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(entityplayer);
        // CraftBukkit start - Better rename detection
        if (nbttagcompound != null && nbttagcompound.hasKey("bukkit")) {
            NBTTagCompound bukkit = nbttagcompound.getCompoundTag("bukkit");
            s = bukkit.hasKey("lastKnownName", 8) ? bukkit.getString("lastKnownName") : s;
        }
        // CraftBukkit end

        // Paper start - support PlayerInitialSpawnEvent
        Location originalLoc = new Location(entityplayer.world.getWorld(), entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
        com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent event = new com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent(entityplayer.getBukkitEntity(), originalLoc);
        this.mcServer.server.getPluginManager().callEvent(event);

        Location newLoc = event.getSpawnLocation();
        entityplayer.world = ((CraftWorld) newLoc.getWorld()).getHandle();
        entityplayer.posX = newLoc.getX();
        entityplayer.posY = newLoc.getY();
        entityplayer.posZ = newLoc.getZ();
        entityplayer.rotationYaw = newLoc.getYaw();
        entityplayer.rotationPitch = newLoc.getPitch();
        entityplayer.dimension = ((CraftWorld) newLoc.getWorld()).getHandle().dimension;
        // Paper end

        entityplayer.setWorld(this.mcServer.getWorld(entityplayer.dimension));
        entityplayer.interactionManager.setWorld((WorldServer) entityplayer.world);
        String s1 = "local";

        if (networkmanager.getRemoteAddress() != null) {
            s1 = networkmanager.getRemoteAddress().toString();
        }

        // Spigot start - spawn location event
        Player bukkitPlayer = entityplayer.getBukkitEntity();
        PlayerSpawnLocationEvent ev = new PlayerSpawnLocationEvent(bukkitPlayer, bukkitPlayer.getLocation());
        Bukkit.getPluginManager().callEvent(ev);

        Location loc = ev.getSpawnLocation();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

        entityplayer.setWorld(world);
        entityplayer.setPosition(loc.getX(), loc.getY(), loc.getZ());
        entityplayer.setRotation(loc.getYaw(), loc.getPitch()); 
        // Spigot end

        // CraftBukkit - Moved message to after join
        // PlayerList.f.info("{}[{}] logged in with entity id {} at ({}, {}, {})", entityplayer.getName(), s1, Integer.valueOf(entityplayer.getId()), Double.valueOf(entityplayer.locX), Double.valueOf(entityplayer.locY), Double.valueOf(entityplayer.locZ));
        WorldServer worldserver = this.mcServer.getWorld(entityplayer.dimension);
        WorldInfo worlddata = worldserver.getWorldInfo();

        this.setPlayerGameTypeBasedOnOther(entityplayer, (EntityPlayerMP) null, worldserver);
        NetHandlerPlayServer playerconnection = new NetHandlerPlayServer(this.mcServer, networkmanager, entityplayer);

        playerconnection.sendPacket(new SPacketJoinGame(entityplayer.getEntityId(), entityplayer.interactionManager.getGameType(), worlddata.isHardcoreModeEnabled(), worldserver.provider.getDimensionType().getId(), worldserver.getDifficulty(), this.getMaxPlayers(), worlddata.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
        entityplayer.getBukkitEntity().sendSupportedChannels(); // CraftBukkit
        playerconnection.sendPacket(new SPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(this.getServerInstance().getServerModName())));
        playerconnection.sendPacket(new SPacketServerDifficulty(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
        playerconnection.sendPacket(new SPacketPlayerAbilities(entityplayer.capabilities));
        playerconnection.sendPacket(new SPacketHeldItemChange(entityplayer.inventory.currentItem));
        playerconnection.sendPacket(new SPacketEntityStatus(entityplayer, (byte) (worldserver.getGameRules().getBoolean("reducedDebugInfo") ? 22 : 23))); // Paper - fix this rule not being initialized on the client
        this.updatePermissionLevel(entityplayer);
        entityplayer.getStatFile().markAllDirty();
        entityplayer.getRecipeBook().init(entityplayer);
        this.sendScoreboard((ServerScoreboard) worldserver.getScoreboard(), entityplayer);
        this.mcServer.refreshStatusNextTick();
        // CraftBukkit start - login message is handled in the event
        // ChatMessage chatmessage;

        String joinMessage;
        if (entityplayer.getName().equalsIgnoreCase(s)) {
            // chatmessage = new ChatMessage("multiplayer.player.joined", new Object[] { entityplayer.getScoreboardDisplayName()});
            joinMessage = "\u00A7e" + I18n.translateToLocalFormatted("multiplayer.player.joined", entityplayer.getName());
        } else {
            // chatmessage = new ChatMessage("multiplayer.player.joined.renamed", new Object[] { entityplayer.getScoreboardDisplayName(), s});
            joinMessage = "\u00A7e" + I18n.translateToLocalFormatted("multiplayer.player.joined.renamed", entityplayer.getName(), s);
        }

        // chatmessage.getChatModifier().setColor(EnumChatFormat.YELLOW);
        // this.sendMessage(chatmessage);
        this.onPlayerJoin(entityplayer, joinMessage);
        // CraftBukkit end
        worldserver = mcServer.getWorld(entityplayer.dimension);  // CraftBukkit - Update in case join event changed it
        playerconnection.setPlayerLocation(entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
        this.updateTimeAndWeatherForPlayer(entityplayer, worldserver);
        if (!this.mcServer.getResourcePackUrl().isEmpty()) {
            entityplayer.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }

        Iterator iterator = entityplayer.getActivePotionEffects().iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            playerconnection.sendPacket(new SPacketEntityEffect(entityplayer.getEntityId(), mobeffect));
        }

        if (nbttagcompound != null && nbttagcompound.hasKey("RootVehicle", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("RootVehicle");
            Entity entity = AnvilChunkLoader.readWorldEntity(nbttagcompound1.getCompoundTag("Entity"), worldserver, true);

            if (entity != null) {
                UUID uuid = nbttagcompound1.getUniqueId("Attach");
                Iterator iterator1;
                Entity entity1;

                if (entity.getUniqueID().equals(uuid)) {
                    entityplayer.startRiding(entity, true);
                } else {
                    iterator1 = entity.getRecursivePassengers().iterator();

                    while (iterator1.hasNext()) {
                        entity1 = (Entity) iterator1.next();
                        if (entity1.getUniqueID().equals(uuid)) {
                            entityplayer.startRiding(entity1, true);
                            break;
                        }
                    }
                }

                if (!entityplayer.isRiding()) {
                    PlayerList.LOGGER.warn("Couldn\'t reattach entity to player");
                    worldserver.removeEntityDangerously(entity);
                    iterator1 = entity.getRecursivePassengers().iterator();

                    while (iterator1.hasNext()) {
                        entity1 = (Entity) iterator1.next();
                        worldserver.removeEntityDangerously(entity1);
                    }
                }
            }
        }

        entityplayer.addSelfToInternalCraftingInventory();
        // Paper start - Add to collideRule team if needed
        final Scoreboard scoreboard = this.getServerInstance().getEntityWorld().getScoreboard();
        if (this.collideRuleTeamName != null && scoreboard.getTeam(collideRuleTeamName) != null && entityplayer.getTeam() == null) {
            scoreboard.addPlayerToTeam(entityplayer.getName(), collideRuleTeamName);
        }
        // Paper end
        // CraftBukkit - Moved from above, added world
        PlayerList.LOGGER.info(entityplayer.getName() + "[" + s1 + "] logged in with entity id " + entityplayer.getEntityId() + " at ([" + entityplayer.world.worldInfo.getWorldName() + "]" + entityplayer.posX + ", " + entityplayer.posY + ", " + entityplayer.posZ + ")");
    }

    public void sendScoreboard(ServerScoreboard scoreboardserver, EntityPlayerMP entityplayer) {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = scoreboardserver.getTeams().iterator();

        while (iterator.hasNext()) {
            ScorePlayerTeam scoreboardteam = (ScorePlayerTeam) iterator.next();

            entityplayer.connection.sendPacket(new SPacketTeams(scoreboardteam, 0));
        }

        for (int i = 0; i < 19; ++i) {
            ScoreObjective scoreboardobjective = scoreboardserver.getObjectiveInDisplaySlot(i);

            if (scoreboardobjective != null && !hashset.contains(scoreboardobjective)) {
                List list = scoreboardserver.getCreatePackets(scoreboardobjective);
                Iterator iterator1 = list.iterator();

                while (iterator1.hasNext()) {
                    Packet packet = (Packet) iterator1.next();

                    entityplayer.connection.sendPacket(packet);
                }

                hashset.add(scoreboardobjective);
            }
        }

    }

    public void setPlayerManager(WorldServer[] aworldserver) {
        if (playerDataManager != null) return; // CraftBukkit
        this.playerDataManager = aworldserver[0].getSaveHandler().getPlayerNBTManager();
        aworldserver[0].getWorldBorder().addListener(new IBorderListener() {
            @Override
            public void onSizeChanged(WorldBorder worldborder, double d0) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_SIZE), worldborder.world);
            }

            @Override
            public void onTransitionStarted(WorldBorder worldborder, double d0, double d1, long i) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.LERP_SIZE), worldborder.world);
            }

            @Override
            public void onCenterChanged(WorldBorder worldborder, double d0, double d1) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_CENTER), worldborder.world);
            }

            @Override
            public void onWarningTimeChanged(WorldBorder worldborder, int i) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_TIME), worldborder.world);
            }

            @Override
            public void onWarningDistanceChanged(WorldBorder worldborder, int i) {
                PlayerList.this.sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_BLOCKS), worldborder.world);
            }

            @Override
            public void onDamageAmountChanged(WorldBorder worldborder, double d0) {}

            @Override
            public void onDamageBufferChanged(WorldBorder worldborder, double d0) {}
        });
    }

    public void preparePlayer(EntityPlayerMP entityplayer, @Nullable WorldServer worldserver) {
        WorldServer worldserver1 = entityplayer.getServerWorld();

        if (worldserver != null) {
            worldserver.getPlayerChunkMap().removePlayer(entityplayer);
        }

        worldserver1.getPlayerChunkMap().addPlayer(entityplayer);
        worldserver1.getChunkProvider().provideChunk((int) entityplayer.posX >> 4, (int) entityplayer.posZ >> 4);
        if (worldserver != null) {
            CriteriaTriggers.CHANGED_DIMENSION.trigger(entityplayer, worldserver.provider.getDimensionType(), worldserver1.provider.getDimensionType());
            if (worldserver.provider.getDimensionType() == DimensionType.NETHER && entityplayer.world.provider.getDimensionType() == DimensionType.OVERWORLD && entityplayer.getEnteredNetherPosition() != null) {
                CriteriaTriggers.NETHER_TRAVEL.trigger(entityplayer, entityplayer.getEnteredNetherPosition());
            }
        }

    }

    public int getEntityViewDistance() {
        return PlayerChunkMap.getFurthestViewableBlock(this.getViewDistance());
    }

    @Nullable
    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP entityplayer) {
        NBTTagCompound nbttagcompound = this.mcServer.worlds.get(0).getWorldInfo().getPlayerNBTTagCompound(); // CraftBukkit
        NBTTagCompound nbttagcompound1;

        if (entityplayer.getName().equals(this.mcServer.getServerOwner()) && nbttagcompound != null) {
            nbttagcompound1 = nbttagcompound;
            entityplayer.readFromNBT(nbttagcompound);
            PlayerList.LOGGER.debug("loading single player");
        } else {
            nbttagcompound1 = this.playerDataManager.readPlayerData(entityplayer);
        }

        return nbttagcompound1;
    }

    protected void writePlayerData(EntityPlayerMP entityplayer) {
        entityplayer.lastSave = MinecraftServer.currentTick; // Paper
        this.playerDataManager.writePlayerData(entityplayer);
        StatisticsManagerServer serverstatisticmanager = entityplayer.getStatFile(); // CraftBukkit

        if (serverstatisticmanager != null) {
            serverstatisticmanager.saveStatFile();
        }

        PlayerAdvancements advancementdataplayer = entityplayer.getAdvancements(); // CraftBukkit

        if (advancementdataplayer != null) {
            advancementdataplayer.save();
        }

    }

    public void onPlayerJoin(EntityPlayerMP entityplayer, String joinMessage) { // CraftBukkit added param
        this.playerEntityList.add(entityplayer);
        this.playersByName.put(entityplayer.getName(), entityplayer); // Spigot
        this.uuidToPlayerMap.put(entityplayer.getUniqueID(), entityplayer);
        // this.sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { entityplayer})); // CraftBukkit - replaced with loop below
        WorldServer worldserver = this.mcServer.getWorld(entityplayer.dimension);

        // CraftBukkit start
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(cserver.getPlayer(entityplayer), joinMessage);
        cserver.getPluginManager().callEvent(playerJoinEvent);

        if (!entityplayer.connection.netManager.isChannelOpen()) {
            return;
        }

        joinMessage = playerJoinEvent.getJoinMessage();

        if (joinMessage != null && joinMessage.length() > 0) {
            for (ITextComponent line : org.bukkit.craftbukkit.util.CraftChatMessage.fromString(joinMessage)) {
                mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChat(line));
            }
        }

        ChunkIOExecutor.adjustPoolSize(getCurrentPlayerCount());
        // CraftBukkit end

        // CraftBukkit start - sendAll above replaced with this loop
        SPacketPlayerListItem packet = new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, entityplayer);

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayerMP entityplayer1 = this.playerEntityList.get(i);

            if (entityplayer1.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer1.connection.sendPacket(packet);
            }

            if (!entityplayer.getBukkitEntity().canSee(entityplayer1.getBukkitEntity())) {
                continue;
            }

            entityplayer.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { entityplayer1}));
        }
        entityplayer.sentListPacket = true;
        // CraftBukkit end

        entityplayer.connection.sendPacket(new SPacketEntityMetadata(entityplayer.getEntityId(), entityplayer.dataManager, true)); // CraftBukkit - BungeeCord#2321, send complete data to self on spawn

        // CraftBukkit start - Only add if the player wasn't moved in the event
        if (entityplayer.world == worldserver && !worldserver.playerEntities.contains(entityplayer)) {
            worldserver.spawnEntity(entityplayer);
            this.preparePlayer(entityplayer, (WorldServer) null);
        }
        // CraftBukkit end
    }

    public void serverUpdateMovingPlayer(EntityPlayerMP entityplayer) {
        entityplayer.getServerWorld().getPlayerChunkMap().updateMovingPlayer(entityplayer);
    }

    public String disconnect(EntityPlayerMP entityplayer) { // CraftBukkit - return string
        WorldServer worldserver = entityplayer.getServerWorld();

        entityplayer.addStat(StatList.LEAVE_GAME);

        // CraftBukkit start - Quitting must be before we do final save of data, in case plugins need to modify it
        org.bukkit.craftbukkit.event.CraftEventFactory.handleInventoryCloseEvent(entityplayer);

        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.getName() + " left the game");
        cserver.getPluginManager().callEvent(playerQuitEvent);
        entityplayer.getBukkitEntity().disconnect(playerQuitEvent.getQuitMessage());

        entityplayer.onUpdateEntity();// SPIGOT-924
        // CraftBukkit end

        // Paper start - Remove from collideRule team if needed
        if (this.collideRuleTeamName != null) {
            final Scoreboard scoreBoard = this.mcServer.getEntityWorld().getScoreboard();
            final ScorePlayerTeam team = scoreBoard.getTeam(this.collideRuleTeamName);
            if (entityplayer.getTeam() == team && team != null) {
                scoreBoard.removePlayerFromTeam(entityplayer.getName(), team);
            }
        }
        // Paper end

        this.writePlayerData(entityplayer);
        if (entityplayer.isRiding()) {
            Entity entity = entityplayer.getLowestRidingEntity();

            if (entity.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
                PlayerList.LOGGER.debug("Removing player mount");
                entityplayer.dismountRidingEntity();
                worldserver.removeEntityDangerously(entity);
                Iterator iterator = entity.getRecursivePassengers().iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    worldserver.removeEntityDangerously(entity1);
                }

                worldserver.getChunkFromChunkCoords(entityplayer.chunkCoordX, entityplayer.chunkCoordZ).markDirty();
            }
        }

        worldserver.removeEntity(entityplayer);
        worldserver.getPlayerChunkMap().removePlayer(entityplayer);
        entityplayer.getAdvancements().dispose();
        this.playerEntityList.remove(entityplayer);
        this.playersByName.remove(entityplayer.getName()); // Spigot
        UUID uuid = entityplayer.getUniqueID();
        EntityPlayerMP entityplayer1 = this.uuidToPlayerMap.get(uuid);

        if (entityplayer1 == entityplayer) {
            this.uuidToPlayerMap.remove(uuid);
            // CraftBukkit start
            // this.o.remove(uuid);
            // this.p.remove(uuid);
            // CraftBukkit end
        }

        // CraftBukkit start
        //  this.sendAll(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { entityplayer}));
        SPacketPlayerListItem packet = new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, entityplayer);
        for (int i = 0; i < playerEntityList.size(); i++) {
            EntityPlayerMP entityplayer2 = this.playerEntityList.get(i);

            if (entityplayer2.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer2.connection.sendPacket(packet);
            } else {
                entityplayer2.getBukkitEntity().removeDisconnectingPlayer(entityplayer.getBukkitEntity());
            }
        }
        // This removes the scoreboard (and player reference) for the specific player in the manager
        cserver.getScoreboardManager().removePlayer(entityplayer.getBukkitEntity());
        // CraftBukkit end

        ChunkIOExecutor.adjustPoolSize(this.getCurrentPlayerCount()); // CraftBukkit

        return playerQuitEvent.getQuitMessage(); // CraftBukkit
    }

    // CraftBukkit start - Whole method, SocketAddress to LoginListener, added hostname to signature, return EntityPlayer
    public EntityPlayerMP attemptLogin(NetHandlerLoginServer loginlistener, GameProfile gameprofile, String hostname) {
        // Moved from processLogin
        UUID uuid = EntityPlayer.getUUID(gameprofile);
        ArrayList arraylist = Lists.newArrayList();

        EntityPlayerMP entityplayer;

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            entityplayer = this.playerEntityList.get(i);
            if (entityplayer.getUniqueID().equals(uuid)) {
                arraylist.add(entityplayer);
            }
        }

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayerMP) iterator.next();
            writePlayerData(entityplayer); // CraftBukkit - Force the player's inventory to be saved
            entityplayer.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.duplicate_login", new Object[0]));
        }

        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome.
        SocketAddress socketaddress = loginlistener.networkManager.getRemoteAddress();

        EntityPlayerMP entity = new EntityPlayerMP(mcServer, mcServer.getWorld(0), gameprofile, new PlayerInteractionManager(mcServer.getWorld(0)));
        Player player = entity.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(player, hostname, ((java.net.InetSocketAddress) socketaddress).getAddress(), ((java.net.InetSocketAddress) loginlistener.networkManager.getRawAddress()).getAddress());
        String s;

        if (getBannedPlayers().isBanned(gameprofile) && !getBannedPlayers().getEntry(gameprofile).hasBanExpired()) {
            UserListBansEntry gameprofilebanentry = this.bannedPlayers.getEntry(gameprofile);

            s = "You are banned from this server!\nReason: " + gameprofilebanentry.getBanReason();
            if (gameprofilebanentry.getBanEndDate() != null) {
                s = s + "\nYour ban will be removed on " + PlayerList.DATE_FORMAT.format(gameprofilebanentry.getBanEndDate());
            }

            // return s;
            if (!gameprofilebanentry.hasBanExpired()) event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s); // Spigot
        } else if (!this.isWhitelisted(gameprofile, event)) { // Paper
            // return "You are not white-listed on this server!";
            //event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, org.spigotmc.SpigotConfig.whitelistMessage); // Spigot // Paper - moved to isWhitelisted
        } else if (getBannedIPs().isBanned(socketaddress) && !getBannedIPs().getBanEntry(socketaddress).hasBanExpired()) {
            UserListIPBansEntry ipbanentry = this.bannedIPs.getBanEntry(socketaddress);

            s = "Your IP address is banned from this server!\nReason: " + ipbanentry.getBanReason();
            if (ipbanentry.getBanEndDate() != null) {
                s = s + "\nYour ban will be removed on " + PlayerList.DATE_FORMAT.format(ipbanentry.getBanEndDate());
            }

            // return s;
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s);
        } else {
            // return this.players.size() >= this.maxPlayers && !this.f(gameprofile) ? "The server is full!" : null;
            if (this.playerEntityList.size() >= this.maxPlayers && !this.bypassesPlayerLimit(gameprofile)) {
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
    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP entityplayer, int i, boolean flag) {
        return this.moveToWorld(entityplayer, i, flag, null, true);
    }

    public EntityPlayerMP moveToWorld(EntityPlayerMP entityplayer, int i, boolean flag, Location location, boolean avoidSuffocation) {
        entityplayer.dismountRidingEntity(); // CraftBukkit
        entityplayer.getServerWorld().getEntityTracker().removePlayerFromTrackers(entityplayer);
        // entityplayer.x().getTracker().untrackEntity(entityplayer); // CraftBukkit
        entityplayer.getServerWorld().getPlayerChunkMap().removePlayer(entityplayer);
        this.playerEntityList.remove(entityplayer);
        this.playersByName.remove(entityplayer.getName()); // Spigot
        this.mcServer.getWorld(entityplayer.dimension).removeEntityDangerously(entityplayer);
        BlockPos blockposition = entityplayer.getBedLocation();
        boolean flag1 = entityplayer.isSpawnForced();

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
        entityplayer.queuedEndExit = false;
        // CraftBukkit end

        entityplayer1.connection = entityplayer.connection;
        entityplayer1.copyFrom(entityplayer, flag);
        entityplayer1.setEntityId(entityplayer.getEntityId());
        entityplayer1.setCommandStats(entityplayer);
        entityplayer1.setPrimaryHand(entityplayer.getPrimaryHand());
        Iterator iterator = entityplayer.getTags().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            entityplayer1.addTag(s);
        }

        // WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);  // CraftBukkit - handled later

        // this.a(entityplayer1, entityplayer, worldserver); // CraftBukkit - removed
        BlockPos blockposition1;

        // CraftBukkit start - fire PlayerRespawnEvent
        if (location == null) {
            boolean isBedSpawn = false;
            CraftWorld cworld = (CraftWorld) this.mcServer.server.getWorld(entityplayer.spawnWorld);
            if (cworld != null && blockposition != null) {
                blockposition1 = EntityPlayer.getBedSpawnLocation(cworld.getHandle(), blockposition, flag1);
                if (blockposition1 != null) {
                    isBedSpawn = true;
                    location = new Location(cworld, blockposition1.getX() + 0.5F, blockposition1.getY() + 0.1F, blockposition1.getZ() + 0.5F);
                } else {
                    entityplayer1.setSpawnPoint(null, true);
                    entityplayer1.connection.sendPacket(new SPacketChangeGameState(0, 0.0F));
                }
            }

            if (location == null) {
                cworld = (CraftWorld) this.mcServer.server.getWorlds().get(0);
                blockposition = entityplayer1.getSpawnPoint(this.mcServer, cworld.getHandle());
                location = new Location(cworld, blockposition.getX() + 0.5F, blockposition.getY() + 0.1F, blockposition.getZ() + 0.5F);
            }

            Player respawnPlayer = cserver.getPlayer(entityplayer1);
            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, location, isBedSpawn);
            cserver.getPluginManager().callEvent(respawnEvent);
            // Spigot Start
            if (entityplayer.connection.isDisconnected()) {
                return entityplayer;
            }
            // Spigot End

            location = respawnEvent.getRespawnLocation();
            entityplayer.reset();
        } else {
            location.setWorld(mcServer.getWorld(i).getWorld());
        }
        WorldServer worldserver = ((CraftWorld) location.getWorld()).getHandle();
        entityplayer1.forceSetPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // CraftBukkit end

        worldserver.getChunkProvider().provideChunk((int) entityplayer1.posX >> 4, (int) entityplayer1.posZ >> 4);

        while (avoidSuffocation && !worldserver.getCollisionBoxes(entityplayer1, entityplayer1.getEntityBoundingBox()).isEmpty() && entityplayer1.posY < 256.0D) {
            entityplayer1.setPosition(entityplayer1.posX, entityplayer1.posY + 1.0D, entityplayer1.posZ);
        }
        // CraftBukkit start
        byte actualDimension = (byte) (worldserver.getWorld().getEnvironment().getId());
        // Force the client to refresh their chunk cache
        if (fromWorld.getEnvironment() == worldserver.getWorld().getEnvironment()) {
            entityplayer1.connection.sendPacket(new SPacketRespawn((byte) (actualDimension >= 0 ? -1 : 0), worldserver.getDifficulty(), worldserver.getWorldInfo().getTerrainType(), entityplayer.interactionManager.getGameType()));
        }

        entityplayer1.connection.sendPacket(new SPacketRespawn(actualDimension, worldserver.getDifficulty(), worldserver.getWorldInfo().getTerrainType(), entityplayer1.interactionManager.getGameType()));
        entityplayer1.setWorld(worldserver);
        entityplayer1.isDead = false;
        entityplayer1.connection.teleport(new Location(worldserver.getWorld(), entityplayer1.posX, entityplayer1.posY, entityplayer1.posZ, entityplayer1.rotationYaw, entityplayer1.rotationPitch));
        entityplayer1.setSneaking(false);
        blockposition1 = worldserver.getSpawnPoint();
        // entityplayer1.playerConnection.a(entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch);
        entityplayer1.connection.sendPacket(new SPacketSpawnPosition(blockposition1));
        entityplayer1.connection.sendPacket(new SPacketSetExperience(entityplayer1.experience, entityplayer1.experienceTotal, entityplayer1.experienceLevel));
        this.updateTimeAndWeatherForPlayer(entityplayer1, worldserver);
        this.updatePermissionLevel(entityplayer1);
        if (!entityplayer.connection.isDisconnected()) {
            worldserver.getPlayerChunkMap().addPlayer(entityplayer1);
            worldserver.spawnEntity(entityplayer1);
            this.playerEntityList.add(entityplayer1);
            this.playersByName.put(entityplayer1.getName(), entityplayer1); // Spigot
            this.uuidToPlayerMap.put(entityplayer1.getUniqueID(), entityplayer1);
        }
        // entityplayer1.syncInventory();
        entityplayer1.setHealth(entityplayer1.getHealth());
        // Added from changeDimension
        syncPlayerInventory(entityplayer); // Update health, etc...
        entityplayer.sendPlayerAbilities();
        for (Object o1 : entityplayer.getActivePotionEffects()) {
            PotionEffect mobEffect = (PotionEffect) o1;
            entityplayer.connection.sendPacket(new SPacketEntityEffect(entityplayer.getEntityId(), mobEffect));
        }

        // Fire advancement trigger
        CriteriaTriggers.CHANGED_DIMENSION.trigger(entityplayer, ((CraftWorld) fromWorld).getHandle().provider.getDimensionType(), worldserver.provider.getDimensionType());
        if (((CraftWorld) fromWorld).getHandle().provider.getDimensionType() == DimensionType.NETHER && worldserver.provider.getDimensionType() == DimensionType.OVERWORLD && entityplayer.getEnteredNetherPosition() != null) {
            CriteriaTriggers.NETHER_TRAVEL.trigger(entityplayer, entityplayer.getEnteredNetherPosition());
        }

        // Don't fire on respawn
        if (fromWorld != location.getWorld()) {
            PlayerChangedWorldEvent event = new PlayerChangedWorldEvent(entityplayer.getBukkitEntity(), fromWorld);
            mcServer.server.getPluginManager().callEvent(event);
        }

        // Save player file again if they were disconnected
        if (entityplayer.connection.isDisconnected()) {
            this.writePlayerData(entityplayer);
        }
        // CraftBukkit end
        return entityplayer1;
    }

    // CraftBukkit start - Replaced the standard handling of portals with a more customised method.
    public void changeDimension(EntityPlayerMP entityplayer, int i, TeleportCause cause) {
        WorldServer exitWorld = null;
        if (entityplayer.dimension < CraftWorld.CUSTOM_DIMENSION_OFFSET) { // plugins must specify exit from custom Bukkit worlds
            // only target existing worlds (compensate for allow-nether/allow-end as false)
            for (WorldServer world : this.mcServer.worlds) {
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
                exit = entityplayer.getBukkitEntity().getBedSpawnLocation();
                if (exit == null || ((CraftWorld) exit.getWorld()).getHandle().dimension != 0) {
                    BlockPos randomSpawn = entityplayer.getSpawnPoint(mcServer, exitWorld);
                    exit = new Location(exitWorld.getWorld(), randomSpawn.getX(), randomSpawn.getY(), randomSpawn.getZ());
                } else {
                    exit = exit.add(0.5F, 0.1F, 0.5F); // SPIGOT-3879
                }
            } else {
                // NORMAL <-> NETHER or NORMAL -> THE_END
                exit = this.calculateTarget(enter, exitWorld);
                useTravelAgent = true;
            }
        }

        TravelAgent agent = exit != null ? (TravelAgent) ((CraftWorld) exit.getWorld()).getHandle().getDefaultTeleporter() : org.bukkit.craftbukkit.CraftTravelAgent.DEFAULT; // return arbitrary TA to compensate for implementation dependent plugins
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
        exitWorld.getDefaultTeleporter().adjustExit(entityplayer, exit, velocity);

        entityplayer.invulnerableDimensionChange = true; // CraftBukkit - Set teleport invulnerability only if player changing worlds
        this.moveToWorld(entityplayer, exitWorld.dimension, true, exit, true); // SPIGOT-3864
        if (entityplayer.motionX != velocity.getX() || entityplayer.motionY != velocity.getY() || entityplayer.motionZ != velocity.getZ()) {
            entityplayer.getBukkitEntity().setVelocity(velocity);
        }
    }

    public void updatePermissionLevel(EntityPlayerMP entityplayer) {
        GameProfile gameprofile = entityplayer.getGameProfile();
        int i = this.canSendCommands(gameprofile) ? this.ops.getObjectKey(gameprofile) : 0;

        i = this.mcServer.isSinglePlayer() && this.mcServer.worlds[0].getWorldInfo().areCommandsAllowed() ? 4 : i;
        i = this.commandsAllowedForAll ? 4 : i;
        this.sendPlayerPermissionLevel(entityplayer, i);
    }

    public void changePlayerDimension(EntityPlayerMP entityplayer, int i) {
        int j = entityplayer.dimension;
        WorldServer worldserver = this.mcServer.getWorld(entityplayer.dimension);

        entityplayer.dimension = i;
        WorldServer worldserver1 = this.mcServer.getWorld(entityplayer.dimension);

        entityplayer.connection.sendPacket(new SPacketRespawn(entityplayer.dimension, entityplayer.world.getDifficulty(), entityplayer.world.getWorldInfo().getTerrainType(), entityplayer.interactionManager.getGameType()));
        this.updatePermissionLevel(entityplayer);
        worldserver.removeEntityDangerously(entityplayer);
        entityplayer.isDead = false;
        this.transferEntityToWorld(entityplayer, j, worldserver, worldserver1);
        this.preparePlayer(entityplayer, worldserver);
        entityplayer.connection.setPlayerLocation(entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
        entityplayer.interactionManager.setWorld(worldserver1);
        entityplayer.connection.sendPacket(new SPacketPlayerAbilities(entityplayer.capabilities));
        this.updateTimeAndWeatherForPlayer(entityplayer, worldserver1);
        this.syncPlayerInventory(entityplayer);
        Iterator iterator = entityplayer.getActivePotionEffects().iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            entityplayer.connection.sendPacket(new SPacketEntityEffect(entityplayer.getEntityId(), mobeffect));
        }

    }

    public void transferEntityToWorld(Entity entity, int i, WorldServer worldserver, WorldServer worldserver1) {
        // CraftBukkit start - Split into modular functions
        Location exit = calculateTarget(entity.getBukkitEntity().getLocation(), worldserver1);
        repositionEntity(entity, exit, true);
    }

    // Copy of original changeWorld(Entity, int, WorldServer, WorldServer) method with only location calculation logic
    public Location calculateTarget(Location enter, World target) {
        WorldServer worldserver = ((CraftWorld) enter.getWorld()).getHandle();
        WorldServer worldserver1 = target.getWorld().getHandle();
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

        worldserver.profiler.startSection("moving");
        if (worldserver1.dimension == -1) {
            d0 = MathHelper.clamp(d0 / d2, worldserver1.getWorldBorder().minX()+ 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
            d1 = MathHelper.clamp(d1 / d2, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
            /*
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        } else if (worldserver1.dimension == 0) {
            d0 = MathHelper.clamp(d0 * d2, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
            d1 = MathHelper.clamp(d1 * d2, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
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
                worldserver1 = this.mcServer.worlds.get(0);
                blockposition = worldserver1.getSpawnPoint();
            } else {
                blockposition = worldserver1.getSpawnCoordinate();
            }

            d0 = blockposition.getX();
            y = blockposition.getY();
            d1 = blockposition.getZ();
            /*
            entity.setPositionRotation(d0, entity.locY, d1, 90.0F, 0.0F);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        }

        worldserver.profiler.endSection();
        if (i != 1) {
            worldserver.profiler.startSection("placing");
            d0 = MathHelper.clamp((int) d0, -29999872, 29999872);
            d1 = MathHelper.clamp((int) d1, -29999872, 29999872);
            /*
            if (entity.isAlive()) {
                entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
                worldserver1.getTravelAgent().a(entity, f);
                worldserver1.addEntity(entity);
                worldserver1.entityJoinedWorld(entity, false);
            }
            */

            worldserver.profiler.endSection();
        }

        // entity.spawnIn(worldserver1);
        return new Location(worldserver1.getWorld(), d0, y, d1, yaw, pitch);
    }

    // copy of original a(Entity, int, WorldServer, WorldServer) method with only entity repositioning logic
    public void repositionEntity(Entity entity, Location exit, boolean portal) {
        WorldServer worldserver = (WorldServer) entity.world;
        WorldServer worldserver1 = ((CraftWorld) exit.getWorld()).getHandle();
        int i = worldserver.dimension;

        /*
        double d0 = entity.locX;
        double d1 = entity.locZ;
        double d2 = 8.0D;
        float f = entity.yaw;
        */

        worldserver.profiler.startSection("moving");
        entity.setLocationAndAngles(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
        if (entity.isEntityAlive()) {
            worldserver.updateEntityWithOptionalForce(entity, false);
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

        worldserver.profiler.endSection();
        if (i != 1) {
            worldserver.profiler.startSection("placing");
            /*
            d0 = (double) MathHelper.clamp((int) d0, -29999872, 29999872);
            d1 = (double) MathHelper.clamp((int) d1, -29999872, 29999872);
            */
            if (entity.isEntityAlive()) {
                // entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
                // worldserver1.getTravelAgent().a(entity, f);
                if (portal) {
                    Vector velocity = entity.getBukkitEntity().getVelocity();
                    worldserver1.getDefaultTeleporter().adjustExit(entity, exit, velocity);
                    entity.setLocationAndAngles(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
                    if (entity.motionX != velocity.getX() || entity.motionY != velocity.getY() || entity.motionZ != velocity.getZ()) {
                        entity.getBukkitEntity().setVelocity(velocity);
                    }
                }
                // worldserver1.addEntity(entity);
                worldserver1.updateEntityWithOptionalForce(entity, false);
            }

            worldserver.profiler.endSection();
        }

        entity.setWorld(worldserver1);
        // CraftBukkit end
    }

    public void onTick() {
        if (++this.playerPingIndex > 600) {
            // CraftBukkit start
            for (int i = 0; i < this.playerEntityList.size(); ++i) {
                final EntityPlayerMP target = this.playerEntityList.get(i);

                target.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_LATENCY, Iterables.filter(this.playerEntityList, new Predicate<EntityPlayerMP>() {
                    @Override
                    public boolean apply(EntityPlayerMP input) {
                        return target.getBukkitEntity().canSee(input.getBukkitEntity());
                    }
                })));
            }
            // CraftBukkit end
            this.playerPingIndex = 0;
        }

    }

    public void sendPacketToAllPlayers(Packet<?> packet) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            this.playerEntityList.get(i).connection.sendPacket(packet);
        }

    }

    // CraftBukkit start - add a world/entity limited version
    public void sendAll(Packet packet, EntityPlayer entityhuman) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayerMP entityplayer =  this.playerEntityList.get(i);
            if (entityhuman != null && entityhuman instanceof EntityPlayerMP && !entityplayer.getBukkitEntity().canSee(((EntityPlayerMP) entityhuman).getBukkitEntity())) {
                continue;
            }
            this.playerEntityList.get(i).connection.sendPacket(packet);
        }
    }

    public void sendAll(Packet packet, World world) {
        for (int i = 0; i < world.playerEntities.size(); ++i) {
            ((EntityPlayerMP) world.playerEntities.get(i)).connection.sendPacket(packet);
        }

    }
    // CraftBukkit end

    public void sendPacketToAllPlayersInDimension(Packet<?> packet, int i) {
        for (int j = 0; j < this.playerEntityList.size(); ++j) {
            EntityPlayerMP entityplayer = this.playerEntityList.get(j);

            if (entityplayer.dimension == i) {
                entityplayer.connection.sendPacket(packet);
            }
        }

    }

    public void sendMessageToAllTeamMembers(EntityPlayer entityhuman, ITextComponent ichatbasecomponent) {
        Team scoreboardteambase = entityhuman.getTeam();

        if (scoreboardteambase != null) {
            Collection collection = scoreboardteambase.getMembershipCollection();
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                EntityPlayerMP entityplayer = this.getPlayerByUsername(s);

                if (entityplayer != null && entityplayer != entityhuman) {
                    entityplayer.sendMessage(ichatbasecomponent);
                }
            }

        }
    }

    public void sendMessageToTeamOrAllPlayers(EntityPlayer entityhuman, ITextComponent ichatbasecomponent) {
        Team scoreboardteambase = entityhuman.getTeam();

        if (scoreboardteambase == null) {
            this.sendMessage(ichatbasecomponent);
        } else {
            for (int i = 0; i < this.playerEntityList.size(); ++i) {
                EntityPlayerMP entityplayer = this.playerEntityList.get(i);

                if (entityplayer.getTeam() != scoreboardteambase) {
                    entityplayer.sendMessage(ichatbasecomponent);
                }
            }

        }
    }

    public String getFormattedListOfPlayers(boolean flag) {
        String s = "";
        ArrayList arraylist = Lists.newArrayList(this.playerEntityList);

        for (int i = 0; i < arraylist.size(); ++i) {
            if (i > 0) {
                s = s + ", ";
            }

            s = s + ((EntityPlayerMP) arraylist.get(i)).getName();
            if (flag) {
                s = s + " (" + ((EntityPlayerMP) arraylist.get(i)).getCachedUniqueIdString() + ")";
            }
        }

        return s;
    }

    public String[] getOnlinePlayerNames() {
        String[] astring = new String[this.playerEntityList.size()];

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            astring[i] = this.playerEntityList.get(i).getName();
        }

        return astring;
    }

    public GameProfile[] getOnlinePlayerProfiles() {
        GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            agameprofile[i] = this.playerEntityList.get(i).getGameProfile();
        }

        return agameprofile;
    }

    public UserListBans getBannedPlayers() {
        return this.bannedPlayers;
    }

    public UserListIPBans getBannedIPs() {
        return this.bannedIPs;
    }

    public void addOp(GameProfile gameprofile) {
        int i = this.mcServer.getOpPermissionLevel();

        this.ops.addEntry(new UserListOpsEntry(gameprofile, this.mcServer.getOpPermissionLevel(), this.ops.bypassesPlayerLimit(gameprofile)));
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(gameprofile.getId()), i);
        // CraftBukkit start
        Player player = mcServer.server.getPlayer(gameprofile.getId());
        if (player != null) {
           player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    public void removeOp(GameProfile gameprofile) {
        this.ops.removeEntry(gameprofile);
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(gameprofile.getId()), 0);
        // CraftBukkit start
        Player player = mcServer.server.getPlayer(gameprofile.getId());
        if (player != null) {
            player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    private void sendPlayerPermissionLevel(EntityPlayerMP entityplayer, int i) {
        if (entityplayer != null && entityplayer.connection != null) {
            byte b0;

            if (i <= 0) {
                b0 = 24;
            } else if (i >= 4) {
                b0 = 28;
            } else {
                b0 = (byte) (24 + i);
            }

            entityplayer.connection.sendPacket(new SPacketEntityStatus(entityplayer, b0));
        }

    }

    // Paper start
    public boolean canJoin(GameProfile gameprofile) {
        return isWhitelisted(gameprofile, null);
    }
    public boolean isWhitelisted(GameProfile gameprofile, org.bukkit.event.player.PlayerLoginEvent loginEvent) {
        boolean isOp = this.ops.hasEntry(gameprofile);
        boolean isWhitelisted = !this.whiteListEnforced || isOp || this.whiteListedPlayers.hasEntry(gameprofile);
        final com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent event;
        event = new com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent(MCUtil.toBukkit(gameprofile), this.whiteListEnforced, isWhitelisted, isOp, org.spigotmc.SpigotConfig.whitelistMessage);
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

    public boolean canSendCommands(GameProfile gameprofile) {
        return this.ops.hasEntry(gameprofile) || this.mcServer.isSinglePlayer() && this.mcServer.worlds.get(0).getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(gameprofile.getName()) || this.commandsAllowedForAll; // CraftBukkit
    }

    @Nullable
    public EntityPlayerMP getPlayerByUsername(String s) {
        return this.playersByName.get(s); // Spigot
    }

    public void sendToAllNearExcept(@Nullable EntityPlayer entityhuman, double d0, double d1, double d2, double d3, int i, Packet<?> packet) {
        for (int j = 0; j < this.playerEntityList.size(); ++j) {
            EntityPlayerMP entityplayer = this.playerEntityList.get(j);

            // CraftBukkit start - Test if player receiving packet can see the source of the packet
            if (entityhuman != null && entityhuman instanceof EntityPlayerMP && !entityplayer.getBukkitEntity().canSee(((EntityPlayerMP) entityhuman).getBukkitEntity())) {
               continue;
            }
            // CraftBukkit end

            if (entityplayer != entityhuman && entityplayer.dimension == i) {
                double d4 = d0 - entityplayer.posX;
                double d5 = d1 - entityplayer.posY;
                double d6 = d2 - entityplayer.posZ;

                if (d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3) {
                    entityplayer.connection.sendPacket(packet);
                }
            }
        }

    }

    // Paper start
    public void saveAllPlayerData() {
        savePlayers(null);
    }

    public void savePlayers(Integer interval) {
        MCUtil.ensureMain("Save Players", () -> { // Paper - ensure main
        long now = MinecraftServer.currentTick;
        MinecraftTimings.savePlayers.startTiming(); // Paper
        int numSaved = 0; // Paper
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayerMP entityplayer = this.playerEntityList.get(i);
            if (interval == null || now - entityplayer.lastSave >= interval) {
                this.writePlayerData(entityplayer);
                if (interval != null && ++numSaved <= com.destroystokyo.paper.PaperConfig.maxPlayerAutoSavePerTick) { break; } // Paper
            }
        }
        MinecraftTimings.savePlayers.stopTiming(); // Paper
        return null; }); // Paper - ensure main
    }
    // Paper end

    public void addWhitelistedPlayer(GameProfile gameprofile) {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(gameprofile));
    }

    public void removePlayerFromWhitelist(GameProfile gameprofile) {
        this.whiteListedPlayers.removeEntry(gameprofile);
    }

    public UserListWhitelist getWhitelistedPlayers() {
        return this.whiteListedPlayers;
    }

    public String[] getWhitelistedPlayerNames() {
        return this.whiteListedPlayers.getKeys();
    }

    public UserListOps getOppedPlayers() {
        return this.ops;
    }

    public String[] getOppedPlayerNames() {
        return this.ops.getKeys();
    }

    public void reloadWhitelist() {}

    public void updateTimeAndWeatherForPlayer(EntityPlayerMP entityplayer, WorldServer worldserver) {
        WorldBorder worldborder = entityplayer.world.getWorldBorder(); // CraftBukkit

        entityplayer.connection.sendPacket(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.INITIALIZE));
        entityplayer.connection.sendPacket(new SPacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")));
        BlockPos blockposition = worldserver.getSpawnPoint();

        entityplayer.connection.sendPacket(new SPacketSpawnPosition(blockposition));
        if (worldserver.isRaining()) {
            // CraftBukkit start - handle player weather
            // entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(1, 0.0F));
            // entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(7, worldserver.j(1.0F)));
            // entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(8, worldserver.h(1.0F)));
            entityplayer.setPlayerWeather(org.bukkit.WeatherType.DOWNFALL, false);
            entityplayer.updateWeather(-worldserver.rainingStrength, worldserver.rainingStrength, -worldserver.thunderingStrength, worldserver.thunderingStrength);
            // CraftBukkit end
        }

    }

    public void syncPlayerInventory(EntityPlayerMP entityplayer) {
        entityplayer.sendContainerToPlayer(entityplayer.inventoryContainer);
        // entityplayer.triggerHealthUpdate();
        entityplayer.getBukkitEntity().updateScaledHealth(); // CraftBukkit - Update scaled health on respawn and worldchange
        entityplayer.connection.sendPacket(new SPacketHeldItemChange(entityplayer.inventory.currentItem));
    }

    public int getCurrentPlayerCount() {
        return this.playerEntityList.size();
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public String[] getAvailablePlayerDat() {
        return this.mcServer.worlds.get(0).getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat(); // CraftBukkit
    }

    public boolean isWhiteListEnabled() {
        return this.whiteListEnforced;
    }

    public void setWhiteListEnabled(boolean flag) {
        this.whiteListEnforced = flag;
    }

    public List<EntityPlayerMP> getPlayersMatchingAddress(String s) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.playerEntityList.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            if (entityplayer.getPlayerIP().equals(s)) {
                arraylist.add(entityplayer);
            }
        }

        return arraylist;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public MinecraftServer getServerInstance() {
        return this.mcServer;
    }

    public NBTTagCompound getHostPlayerData() {
        return null;
    }

    private void setPlayerGameTypeBasedOnOther(EntityPlayerMP entityplayer, EntityPlayerMP entityplayer1, World world) {
        if (entityplayer1 != null) {
            entityplayer.interactionManager.setGameType(entityplayer1.interactionManager.getGameType());
        } else if (this.gameType != null) {
            entityplayer.interactionManager.setGameType(this.gameType);
        }

        entityplayer.interactionManager.initializeGameType(world.getWorldInfo().getGameType());
    }

    // Paper start - Extract method to allow for restarting flag
    public void removeAllPlayers() {
        u(false);
    }

    public void u(boolean isRestarting) {
        // CraftBukkit start - disconnect safely
        for (EntityPlayerMP player : this.playerEntityList) {
            player.connection.disconnect(!isRestarting ? this.mcServer.server.getShutdownMessage() : org.spigotmc.SpigotConfig.restartMessage); // CraftBukkit - add custom shutdown message // Paper - add isRestarting flag
        }
        // CraftBukkit end
        // Paper start - Remove collideRule team if it exists
        if (this.collideRuleTeamName != null) {
            final Scoreboard scoreboard = this.getServerInstance().getEntityWorld().getScoreboard();
            final ScorePlayerTeam team = scoreboard.getTeam(this.collideRuleTeamName);
            if (team != null) scoreboard.removeTeam(team);
        }
        // Paper end
    }
    // Paper end

    // CraftBukkit start
    public void sendMessage(ITextComponent[] iChatBaseComponents) {
        for (ITextComponent component : iChatBaseComponents) {
            sendMessage(component, true);
        }
    }
    // CraftBukkit end

    public void sendMessage(ITextComponent ichatbasecomponent, boolean flag) {
        this.mcServer.sendMessage(ichatbasecomponent);
        ChatType chatmessagetype = flag ? ChatType.SYSTEM : ChatType.CHAT;

        // CraftBukkit start - we run this through our processor first so we can get web links etc
        this.sendPacketToAllPlayers(new SPacketChat(CraftChatMessage.fixComponent(ichatbasecomponent), chatmessagetype));
        // CraftBukkit end
    }

    public void sendMessage(ITextComponent ichatbasecomponent) {
        this.sendMessage(ichatbasecomponent, true);
    }

    public StatisticsManagerServer getStatisticManager(EntityPlayerMP entityhuman) {
        UUID uuid = entityhuman.getUniqueID();
        StatisticsManagerServer serverstatisticmanager = uuid == null ? null : (StatisticsManagerServer) entityhuman.getStatFile();
        // CraftBukkit end

        if (serverstatisticmanager == null) {
            File file = new File(this.mcServer.getWorld(0).getSaveHandler().getWorldDirectory(), "stats");
            File file1 = new File(file, uuid + ".json");

            if (!file1.exists()) {
                File file2 = new File(file, entityhuman.getName() + ".json");

                if (file2.exists() && file2.isFile()) {
                    file2.renameTo(file1);
                }
            }

            serverstatisticmanager = new StatisticsManagerServer(this.mcServer, file1);
            serverstatisticmanager.readStatFile();
            // this.o.put(uuid, serverstatisticmanager); // CraftBukkit
        }

        return serverstatisticmanager;
    }

    public PlayerAdvancements getPlayerAdvancements(EntityPlayerMP entityplayer) {
        UUID uuid = entityplayer.getUniqueID();
        PlayerAdvancements advancementdataplayer = entityplayer.getAdvancements(); // CraftBukkit

        if (advancementdataplayer == null) {
            File file = new File(this.mcServer.getWorld(0).getSaveHandler().getWorldDirectory(), "advancements");
            File file1 = new File(file, uuid + ".json");

            advancementdataplayer = new PlayerAdvancements(this.mcServer, file1, entityplayer);
            // this.p.put(uuid, advancementdataplayer); // CraftBukkit
        }

        advancementdataplayer.setPlayer(entityplayer);
        return advancementdataplayer;
    }

    public void setViewDistance(int i) {
        this.viewDistance = i;
        if (this.mcServer.worlds != null) {
            WorldServer[] aworldserver = this.mcServer.worlds;
            int j = aworldserver.length;

            // CraftBukkit start
            for (int k = 0; k < mcServer.worlds.size(); ++k) {
                WorldServer worldserver = mcServer.worlds.get(0);
                // CraftBukkit end

                if (worldserver != null) {
                    worldserver.getPlayerChunkMap().setPlayerViewRadius(i);
                    worldserver.getEntityTracker().setViewDistance(i);
                }
            }

        }
    }

    public List<EntityPlayerMP> getPlayers() {
        return this.playerEntityList;
    }

    public EntityPlayerMP getPlayerByUUID(UUID uuid) {
        return this.uuidToPlayerMap.get(uuid);
    }

    public boolean bypassesPlayerLimit(GameProfile gameprofile) {
        return false;
    }

    public void reloadResources() {
        // CraftBukkit start
        /*Iterator iterator = this.p.values().iterator();

        while (iterator.hasNext()) {
            AdvancementDataPlayer advancementdataplayer = (AdvancementDataPlayer) iterator.next();

            advancementdataplayer.b();
        }*/

        for (EntityPlayerMP player : playerEntityList) {
            player.getAdvancements().reload();
            player.getAdvancements().flushDirty(player); // CraftBukkit - trigger immediate flush of advancements
        }
        // CraftBukkit end

    }
}

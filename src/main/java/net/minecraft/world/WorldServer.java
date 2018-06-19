package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.logging.Level;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraft.world.storage.loot.LootTableManager;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.util.HashTreeSet;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.LightningStrikeEvent;

// CraftBukkit start
import java.util.logging.Level;

import org.bukkit.WeatherType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.HashTreeSet;

import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.LightningStrikeEvent;
// CraftBukkit end

public class WorldServer extends World implements IThreadListener {

    private static final Logger LOGGER = LogManager.getLogger();
    boolean stopPhysicsEvent = false; // Paper
    private final MinecraftServer mcServer;
    public EntityTracker entityTracker;
    private final PlayerChunkMap playerChunkMap;
    // private final Set<NextTickListEntry> nextTickListHash = Sets.newHashSet();
    private final HashTreeSet<NextTickListEntry> pendingTickListEntriesTreeSet = new HashTreeSet<NextTickListEntry>(); // CraftBukkit - HashTreeSet
    private final Map<UUID, Entity> entitiesByUuid = Maps.newHashMap();
    public boolean disableLevelSaving;
    private boolean allPlayersSleeping;
    private int updateEntityTick;
    private final Teleporter worldTeleporter;
    private final WorldEntitySpawner entitySpawner = new WorldEntitySpawner();
    protected final VillageSiege villageSiege = new VillageSiege(this);
    private final WorldServer.ServerBlockEventList[] blockEventQueue = new WorldServer.ServerBlockEventList[] { new WorldServer.ServerBlockEventList(null), new WorldServer.ServerBlockEventList(null)};
    private int blockEventCacheIndex;
    private final List<NextTickListEntry> pendingTickListEntriesThisTick = Lists.newArrayList();

    // CraftBukkit start
    public final int dimension;

    // Add env and gen to constructor
    public WorldServer(MinecraftServer minecraftserver, ISaveHandler idatamanager, WorldInfo worlddata, int i, Profiler methodprofiler, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(idatamanager, worlddata, DimensionType.getById(env.getId()).createDimension(), methodprofiler, false, gen, env);
        this.dimension = i;
        this.pvpMode = minecraftserver.isPVPEnabled();
        worlddata.world = this;
        // CraftBukkit end
        this.mcServer = minecraftserver;
        this.entityTracker = new EntityTracker(this);
        this.playerChunkMap = new PlayerChunkMap(this);
        this.provider.setWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.worldTeleporter = new org.bukkit.craftbukkit.CraftTravelAgent(this); // CraftBukkit
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.getWorldBorder().setSize(minecraftserver.getMaxWorldSize());
    }

    @Override
    public World init() {
        this.mapStorage = new MapStorage(this.saveHandler);
        String s = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection persistentvillage = (VillageCollection) this.mapStorage.getOrLoadData(VillageCollection.class, s);

        if (persistentvillage == null) {
            this.villageCollection = new VillageCollection(this);
            this.mapStorage.setData(s, this.villageCollection);
        } else {
            this.villageCollection = persistentvillage;
            this.villageCollection.setWorldsForAll(this);
        }

        if (getServer().getScoreboardManager() == null) { // CraftBukkit
        this.worldScoreboard = new ServerScoreboard(this.mcServer);
        ScoreboardSaveData persistentscoreboard = (ScoreboardSaveData) this.mapStorage.getOrLoadData(ScoreboardSaveData.class, "scoreboard");

        if (persistentscoreboard == null) {
            persistentscoreboard = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", persistentscoreboard);
        }

        persistentscoreboard.setScoreboard(this.worldScoreboard);
        ((ServerScoreboard) this.worldScoreboard).addDirtyRunnable((new WorldSavedDataCallableSave(persistentscoreboard)));
        // CraftBukkit start
        } else {
            this.worldScoreboard = getServer().getScoreboardManager().getMainScoreboard().getHandle();
        }
        // CraftBukkit end

        this.lootTable = new LootTableManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "loot_tables"));
        // CraftBukkit start
        if (this.dimension != 0) { // SPIGOT-3899 multiple worlds of advancements not supported
            this.advancementManager = this.mcServer.getAdvancementManager();
        }
        if (this.advancementManager == null) {
            this.advancementManager = new AdvancementManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "advancements"));
        }
        if (this.functionManager == null) {
            this.functionManager = new FunctionManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "functions"), this.mcServer);
        }
        // CraftBukkit end
        this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
        this.getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
        this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
        this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
        this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
        if (this.worldInfo.getBorderLerpTime() > 0L) {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
        } else {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize());
        }

        // CraftBukkit start
        if (generator != null) {
            getWorld().getPopulators().addAll(generator.getDefaultPopulators(getWorld()));
        }
        // CraftBukkit end

        return this;
    }

    // CraftBukkit start
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        TileEntity result = super.getTileEntity(pos);
        Block type = getBlockState(pos).getBlock();

        if (type == Blocks.CHEST || type == Blocks.TRAPPED_CHEST) { // Spigot
            if (!(result instanceof TileEntityChest)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.FURNACE) {
            if (!(result instanceof TileEntityFurnace)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.DROPPER) {
            if (!(result instanceof TileEntityDropper)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.DISPENSER) {
            if (!(result instanceof TileEntityDispenser)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.JUKEBOX) {
            if (!(result instanceof BlockJukebox.TileEntityJukebox)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.NOTEBLOCK) {
            if (!(result instanceof TileEntityNote)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.MOB_SPAWNER) {
            if (!(result instanceof TileEntityMobSpawner)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if ((type == Blocks.STANDING_SIGN) || (type == Blocks.WALL_SIGN)) {
            if (!(result instanceof TileEntitySign)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.ENDER_CHEST) {
            if (!(result instanceof TileEntityEnderChest)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.BREWING_STAND) {
            if (!(result instanceof TileEntityBrewingStand)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.BEACON) {
            if (!(result instanceof TileEntityBeacon)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.HOPPER) {
            if (!(result instanceof TileEntityHopper)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.ENCHANTING_TABLE) {
            if (!(result instanceof TileEntityEnchantmentTable)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.END_PORTAL) {
            if (!(result instanceof TileEntityEndPortal)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.SKULL) {
            if (!(result instanceof TileEntitySkull)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.DAYLIGHT_DETECTOR || type == Blocks.DAYLIGHT_DETECTOR_INVERTED) {
            if (!(result instanceof TileEntityDaylightDetector)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.POWERED_COMPARATOR || type == Blocks.UNPOWERED_COMPARATOR) {
            if (!(result instanceof TileEntityComparator)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.FLOWER_POT) {
            if (!(result instanceof TileEntityFlowerPot)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.STANDING_BANNER || type == Blocks.WALL_BANNER) {
            if (!(result instanceof TileEntityBanner)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.STRUCTURE_BLOCK) {
            if (!(result instanceof TileEntityStructure)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.END_GATEWAY) {
            if (!(result instanceof TileEntityEndGateway)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.COMMAND_BLOCK) {
            if (!(result instanceof TileEntityCommandBlock)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.STRUCTURE_BLOCK) {
            if (!(result instanceof TileEntityStructure)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.BED) {
            if (!(result instanceof TileEntityBed)) {
                result = fixTileEntity(pos, type, result);
            }
        }
        // Paper Start - add TE fix checks for shulkers, see nms.BlockShulkerBox
        else if (type instanceof BlockShulkerBox) {
            if (!(result instanceof TileEntityShulkerBox)) {
                result = fixTileEntity(pos, type, result);
            }
        }
        // Paper end

        return result;
    }

    private TileEntity fixTileEntity(BlockPos pos, Block type, TileEntity found) {
        this.getServer().getLogger().log(Level.SEVERE, "Block at {0},{1},{2} is {3} but has {4}" + ". "
                + "Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.", new Object[]{pos.getX(), pos.getY(), pos.getZ(), org.bukkit.Material.getMaterial(Block.getIdFromBlock(type)).toString(), found});

        if (type instanceof ITileEntityProvider) {
            TileEntity replacement = ((ITileEntityProvider) type).createNewTileEntity(this, type.getMetaFromState(this.getBlockState(pos)));
            replacement.world = this;
            this.setTileEntity(pos, replacement);
            return replacement;
        } else {
            this.getServer().getLogger().severe("Don't know how to fix for this type... Can't do anything! :(");
            return found;
        }
    }

    private boolean canSpawn(int x, int z) {
        if (this.generator != null) {
            return this.generator.canSpawn(this.getWorld(), x, z);
        } else {
            return this.provider.canCoordinateBeSpawn(x, z);
        }
    }
    // CraftBukkit end

    @Override
    public void tick() {
        super.tick();
        if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD) {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }

        this.provider.getBiomeProvider().cleanupCache();
        if (this.areAllPlayersAsleep()) {
            if (this.getGameRules().getBoolean("doDaylightCycle")) {
                long i = this.worldInfo.getWorldTime() + 24000L;

                this.worldInfo.setWorldTime(i - i % 24000L);
            }

            this.wakeAllPlayers();
        }

        this.profiler.startSection("mobSpawner");
        // CraftBukkit start - Only call spawner if we have players online and the world allows for mobs or animals
        long time = this.worldInfo.getWorldTotalTime();
        if (this.getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES && (this.spawnHostileMobs || this.spawnPeacefulMobs) && (this instanceof WorldServer && this.playerEntities.size() > 0)) {
            timings.mobSpawn.startTiming(); // Spigot
            this.entitySpawner.findChunksForSpawning(this, this.spawnHostileMobs && (this.ticksPerMonsterSpawns != 0 && time % this.ticksPerMonsterSpawns == 0L), this.spawnPeacefulMobs && (this.ticksPerAnimalSpawns != 0 && time % this.ticksPerAnimalSpawns == 0L), this.worldInfo.getWorldTotalTime() % 400L == 0L);
            timings.mobSpawn.stopTiming(); // Spigot
            // CraftBukkit end
        }

        timings.doChunkUnload.startTiming(); // Spigot
        this.profiler.endStartSection("chunkSource");
        this.chunkProvider.tick();
        int j = this.calculateSkylightSubtracted(1.0F);

        if (j != this.getSkylightSubtracted()) {
            this.setSkylightSubtracted(j);
        }

        this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
        }

        timings.doChunkUnload.stopTiming(); // Spigot
        this.profiler.endStartSection("tickPending");
        timings.scheduledBlocks.startTiming(); // Paper
        this.tickUpdates(false);
        timings.scheduledBlocks.stopTiming(); // Paper
        this.profiler.endStartSection("tickBlocks");
        timings.chunkTicks.startTiming(); // Paper
        this.updateBlocks();
        timings.chunkTicks.stopTiming(); // Paper
        this.profiler.endStartSection("chunkMap");
        timings.doChunkMap.startTiming(); // Spigot
        this.playerChunkMap.tick();
        timings.doChunkMap.stopTiming(); // Spigot
        this.profiler.endStartSection("village");
        timings.doVillages.startTiming(); // Spigot
        this.villageCollection.tick();
        this.villageSiege.tick();
        timings.doVillages.stopTiming(); // Spigot
        this.profiler.endStartSection("portalForcer");
        timings.doPortalForcer.startTiming(); // Spigot
        this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
        timings.doPortalForcer.stopTiming(); // Spigot
        this.profiler.endSection();
        timings.doSounds.startTiming(); // Spigot
        this.sendQueuedBlockEvents();
        timings.doSounds.stopTiming(); // Spigot

        timings.doChunkGC.startTiming();// Spigot
        this.getWorld().processChunkGC(); // CraftBukkit
        timings.doChunkGC.stopTiming(); // Spigot
    }

    @Nullable
    public Biome.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        List list = this.getChunkProvider().getPossibleCreatures(enumcreaturetype, blockposition);

        return list != null && !list.isEmpty() ? (Biome.SpawnListEntry) WeightedRandom.getRandomItem(this.rand, list) : null;
    }

    public boolean canCreatureTypeSpawnHere(EnumCreatureType enumcreaturetype, Biome.SpawnListEntry biomebase_biomemeta, BlockPos blockposition) {
        List list = this.getChunkProvider().getPossibleCreatures(enumcreaturetype, blockposition);

        return list != null && !list.isEmpty() ? list.contains(biomebase_biomemeta) : false;
    }

    @Override
    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = false;
        if (!this.playerEntities.isEmpty()) {
            int i = 0;
            int j = 0;
            Iterator iterator = this.playerEntities.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityhuman = (EntityPlayer) iterator.next();

                if (entityhuman.isSpectator()) {
                    ++i;
                } else if (entityhuman.isPlayerSleeping() || entityhuman.fauxSleeping) {
                    ++j;
                }
            }

            this.allPlayersSleeping = j > 0 && j >= this.playerEntities.size() - i;
        }

    }

    protected void wakeAllPlayers() {
        this.allPlayersSleeping = false;
        List list = this.playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).collect(Collectors.toList());
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityhuman = (EntityPlayer) iterator.next();

            entityhuman.wakeUpPlayer(false, false, true);
        }

        if (this.getGameRules().getBoolean("doWeatherCycle")) {
            this.resetRainAndThunder();
        }

    }

    private void resetRainAndThunder() {
        this.worldInfo.setRaining(false);
        // CraftBukkit start
        // If we stop due to everyone sleeping we should reset the weather duration to some other random value.
        // Not that everyone ever manages to get the whole server to sleep at the same time....
        if (!this.worldInfo.isRaining()) {
            this.worldInfo.setRainTime(0);
        }
        // CraftBukkit end
        this.worldInfo.setThundering(false);
        // CraftBukkit start
        // If we stop due to everyone sleeping we should reset the weather duration to some other random value.
        // Not that everyone ever manages to get the whole server to sleep at the same time....
        if (!this.worldInfo.isThundering()) {
            this.worldInfo.setThunderTime(0);
        }
        // CraftBukkit end
    }

    public boolean areAllPlayersAsleep() {
        if (this.allPlayersSleeping && !this.isRemote) {
            Iterator iterator = this.playerEntities.iterator();

            // CraftBukkit - This allows us to assume that some people are in bed but not really, allowing time to pass in spite of AFKers
            boolean foundActualSleepers = false;

            EntityPlayer entityhuman;

            do {
                if (!iterator.hasNext()) {
                    return foundActualSleepers;
                }

                entityhuman = (EntityPlayer) iterator.next();

                // CraftBukkit start
                if (entityhuman.isPlayerFullyAsleep()) {
                    foundActualSleepers = true;
                }
            } while (!entityhuman.isSpectator() || entityhuman.isPlayerFullyAsleep() || entityhuman.fauxSleeping);
            // CraftBukkit end

            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean isChunkLoaded(int i, int j, boolean flag) {
        return this.getChunkProvider().chunkExists(i, j);
    }

    protected void playerCheckLight() {
        this.profiler.startSection("playerCheckLight");
        if (spigotConfig.randomLightUpdates && !this.playerEntities.isEmpty()) { // Spigot
            int i = this.rand.nextInt(this.playerEntities.size());
            EntityPlayer entityhuman = this.playerEntities.get(i);
            int j = MathHelper.floor(entityhuman.posX) + this.rand.nextInt(11) - 5;
            int k = MathHelper.floor(entityhuman.posY) + this.rand.nextInt(11) - 5;
            int l = MathHelper.floor(entityhuman.posZ) + this.rand.nextInt(11) - 5;

            this.checkLight(new BlockPos(j, k, l));
        }

        this.profiler.endSection();
    }

    @Override
    protected void updateBlocks() {
        this.playerCheckLight();
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            Iterator iterator = this.playerChunkMap.getChunkIterator();

            while (iterator.hasNext()) {
                ((Chunk) iterator.next()).onTick(false);
            }

        } else {
            int i = this.getGameRules().getInt("randomTickSpeed");
            boolean flag = this.isRaining();
            boolean flag1 = this.isThundering();

            this.profiler.startSection("pollingChunks");

            for (Iterator iterator1 = this.playerChunkMap.getChunkIterator(); iterator1.hasNext(); this.profiler.endSection()) {
                this.profiler.startSection("getChunk");
                Chunk chunk = (Chunk) iterator1.next();
                int j = chunk.x * 16;
                int k = chunk.z * 16;

                this.profiler.endStartSection("checkNextLight");
                chunk.enqueueRelightChecks();
                this.profiler.endStartSection("tickChunk");
                chunk.onTick(false);
                if ( !chunk.areNeighborsLoaded( 1 ) ) continue; // Spigot
                this.profiler.endStartSection("thunder");
                int l;
                BlockPos blockposition;

                // Paper - Disable thunder
                if (!this.paperConfig.disableThunder && flag && flag1 && this.rand.nextInt(100000) == 0) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    l = this.updateLCG >> 2;
                    blockposition = this.adjustPosToNearbyEntity(new BlockPos(j + (l & 15), 0, k + (l >> 8 & 15)));
                    if (this.isRainingAt(blockposition)) {
                        DifficultyInstance difficultydamagescaler = this.getDifficultyForLocation(blockposition);

                        if (this.getGameRules().getBoolean("doMobSpawning") && this.rand.nextDouble() < difficultydamagescaler.getAdditionalDifficulty() * paperConfig.skeleHorseSpawnChance) {
                            EntitySkeletonHorse entityhorseskeleton = new EntitySkeletonHorse(this);

                            entityhorseskeleton.setTrap(true);
                            entityhorseskeleton.setGrowingAge(0);
                            entityhorseskeleton.setPosition(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                            this.addEntity(entityhorseskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.LIGHTNING); // CraftBukkit
                            this.addWeatherEffect(new EntityLightningBolt(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), true));
                        } else {
                            this.addWeatherEffect(new EntityLightningBolt(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), false));
                        }
                    }
                }

                this.profiler.endStartSection("iceandsnow");
                if (!this.paperConfig.disableIceAndSnow && this.rand.nextInt(16) == 0) { // Paper - Disable ice and snow
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    l = this.updateLCG >> 2;
                    blockposition = this.getPrecipitationHeight(new BlockPos(j + (l & 15), 0, k + (l >> 8 & 15)));
                    BlockPos blockposition1 = blockposition.down();

                    if (this.canBlockFreezeNoWater(blockposition1)) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition1, Blocks.ICE.getDefaultState(), null); // CraftBukkit
                    }

                    if (flag && this.canSnowAt(blockposition, true)) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition, Blocks.SNOW_LAYER.getDefaultState(), null); // CraftBukkit
                    }

                    if (flag && this.getBiome(blockposition1).canRain()) {
                        this.getBlockState(blockposition1).getBlock().fillWithRain(this, blockposition1);
                    }
                }

                timings.chunkTicksBlocks.startTiming(); // Paper
                if (i > 0) {
                    ExtendedBlockStorage[] achunksection = chunk.getBlockStorageArray();
                    int i1 = achunksection.length;

                    for (int j1 = 0; j1 < i1; ++j1) {
                        ExtendedBlockStorage chunksection = achunksection[j1];

                        if (chunksection != Chunk.NULL_BLOCK_STORAGE && chunksection.needsRandomTick()) {
                            for (int k1 = 0; k1 < i; ++k1) {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                int l1 = this.updateLCG >> 2;
                                int i2 = l1 & 15;
                                int j2 = l1 >> 8 & 15;
                                int k2 = l1 >> 16 & 15;
                                IBlockState iblockdata = chunksection.get(i2, k2, j2);
                                Block block = iblockdata.getBlock();

                                this.profiler.startSection("randomTick");
                                if (block.getTickRandomly()) {
                                    block.randomTick(this, new BlockPos(i2 + j, k2 + chunksection.getYLocation(), j2 + k), iblockdata, this.rand);
                                }

                                this.profiler.endSection();
                            }
                        }
                    }
                }
                timings.chunkTicksBlocks.stopTiming(); // Paper
            }

            this.profiler.endSection();
        }
    }

    protected BlockPos adjustPosToNearbyEntity(BlockPos blockposition) {
        BlockPos blockposition1 = this.getPrecipitationHeight(blockposition);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockposition1, new BlockPos(blockposition1.getX(), this.getHeight(), blockposition1.getZ()))).grow(3.0D);
        List list = this.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new com.google.common.base.Predicate() {
            public boolean a(@Nullable EntityLivingBase entityliving) {
                return entityliving != null && entityliving.isEntityAlive() && WorldServer.this.canSeeSky(entityliving.getPosition());
            }

            @Override
            public boolean apply(@Nullable Object object) {
                return this.a((EntityLivingBase) object);
            }
        });

        if (!list.isEmpty()) {
            return ((EntityLivingBase) list.get(this.rand.nextInt(list.size()))).getPosition();
        } else {
            if (blockposition1.getY() == -1) {
                blockposition1 = blockposition1.up(2);
            }

            return blockposition1;
        }
    }

    @Override
    public boolean isBlockTickPending(BlockPos blockposition, Block block) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
    }

    @Override
    public boolean isUpdateScheduled(BlockPos blockposition, Block block) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        return this.pendingTickListEntriesTreeSet.contains(nextticklistentry); // CraftBukkit
    }

    @Override
    public void scheduleUpdate(BlockPos blockposition, Block block, int i) {
        this.updateBlockTick(blockposition, block, i, 0);
    }

    @Override
    public void updateBlockTick(BlockPos blockposition, Block block, int i, int j) {
        Material material = block.getDefaultState().getMaterial();

        if (this.scheduledUpdatesAreImmediate && material != Material.AIR) {
            if (block.requiresUpdates()) {
                if (this.isAreaLoaded(blockposition.add(-8, -8, -8), blockposition.add(8, 8, 8))) {
                    IBlockState iblockdata = this.getBlockState(blockposition);

                    if (iblockdata.getMaterial() != Material.AIR && iblockdata.getBlock() == block) {
                        iblockdata.getBlock().updateTick(this, blockposition, iblockdata, this.rand);
                    }
                }

                return;
            }

            i = 1;
        }

        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        if (this.isBlockLoaded(blockposition)) {
            if (material != Material.AIR) {
                nextticklistentry.setScheduledTime(i + this.worldInfo.getWorldTotalTime());
                nextticklistentry.setPriority(j);
            }

            // CraftBukkit - use nextTickList
            if (!this.pendingTickListEntriesTreeSet.contains(nextticklistentry)) {
                this.pendingTickListEntriesTreeSet.add(nextticklistentry);
            }
        }

    }

    @Override
    public void scheduleBlockUpdate(BlockPos blockposition, Block block, int i, int j) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        nextticklistentry.setPriority(j);
        Material material = block.getDefaultState().getMaterial();

        if (material != Material.AIR) {
            nextticklistentry.setScheduledTime(i + this.worldInfo.getWorldTotalTime());
        }

        // CraftBukkit - use nextTickList
        if (!this.pendingTickListEntriesTreeSet.contains(nextticklistentry)) {
            this.pendingTickListEntriesTreeSet.add(nextticklistentry);
        }

    }

    @Override
    public void updateEntities() {
        if (false && this.playerEntities.isEmpty()) { // CraftBukkit - this prevents entity cleanup, other issues on servers with no players
            if (this.updateEntityTick++ >= 300) {
                return;
            }
        } else {
            this.resetUpdateEntityTick();
        }

        this.provider.onWorldUpdateEntities();
        super.updateEntities();
        spigotConfig.currentPrimedTnt = 0; // Spigot
    }

    @Override
    protected void tickPlayers() {
        super.tickPlayers();
        this.profiler.endStartSection("players");

        for (int i = 0; i < this.playerEntities.size(); ++i) {
            Entity entity = this.playerEntities.get(i);
            Entity entity1 = entity.getRidingEntity();

            if (entity1 != null) {
                if (!entity1.isDead && entity1.isPassenger(entity)) {
                    continue;
                }

                entity.dismountRidingEntity();
            }

            this.profiler.startSection("tick");
            if (!entity.isDead) {
                try {
                    this.updateEntity(entity);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
                    CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Player being ticked");

                    entity.addEntityCrashInfo(crashreportsystemdetails);
                    throw new ReportedException(crashreport);
                }
            }

            this.profiler.endSection();
            this.profiler.startSection("remove");
            if (entity.isDead) {
                int j = entity.chunkCoordX;
                int k = entity.chunkCoordZ;

                if (entity.addedToChunk && this.isChunkLoaded(j, k, true)) {
                    this.getChunkFromChunkCoords(j, k).removeEntity(entity);
                }

                this.loadedEntityList.remove(entity);
                this.onEntityRemoved(entity);
            }

            this.profiler.endSection();
        }

    }

    public void resetUpdateEntityTick() {
        this.updateEntityTick = 0;
    }

    @Override
    public boolean tickUpdates(boolean flag) {
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        } else {
            int i = this.pendingTickListEntriesTreeSet.size();

            if (false) { // CraftBukkit
                throw new IllegalStateException("TickNextTick list out of synch");
            } else {
                if (i > 65536) {
                    // CraftBukkit start - If the server has too much to process over time, try to alleviate that
                    if (i > 20 * 65536) {
                        i = i / 20;
                    } else {
                        i = 65536;
                    }
                    // CraftBukkit end
                }

                this.profiler.startSection("cleaning");

                timings.scheduledBlocksCleanup.startTiming(); // Paper
                NextTickListEntry nextticklistentry;

                for (int j = 0; j < i; ++j) {
                    nextticklistentry = this.pendingTickListEntriesTreeSet.first();
                    if (!flag && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime()) {
                        break;
                    }

                    // CraftBukkit - use nextTickList
                    this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
                    // this.nextTickListHash.remove(nextticklistentry);
                    this.pendingTickListEntriesThisTick.add(nextticklistentry);
                }
                timings.scheduledBlocksCleanup.stopTiming(); // Paper

                this.profiler.endSection();
                this.profiler.startSection("ticking");
                Iterator iterator = this.pendingTickListEntriesThisTick.iterator();
                timings.scheduledBlocksTicking.startTiming(); // Paper

                while (iterator.hasNext()) {
                    nextticklistentry = (NextTickListEntry) iterator.next();
                    iterator.remove();
                    boolean flag1 = false;

                    if (this.isAreaLoaded(nextticklistentry.position.add(0, 0, 0), nextticklistentry.position.add(0, 0, 0))) {
                        IBlockState iblockdata = this.getBlockState(nextticklistentry.position);
                        co.aikar.timings.Timing timing = iblockdata.getBlock().getTiming(); // Paper
                        timing.startTiming(); // Paper

                        if (iblockdata.getMaterial() != Material.AIR && Block.isEqualTo(iblockdata.getBlock(), nextticklistentry.getBlock())) {
                            try {
                                stopPhysicsEvent = !paperConfig.firePhysicsEventForRedstone && (iblockdata.getBlock() instanceof BlockRedstoneDiode || iblockdata.getBlock() instanceof BlockRedstoneTorch); // Paper
                                iblockdata.getBlock().updateTick(this, nextticklistentry.position, iblockdata, this.rand);
                            } catch (Throwable throwable) {
                                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Block being ticked");

                                CrashReportCategory.addBlockInfo(crashreportsystemdetails, nextticklistentry.position, iblockdata);
                                throw new ReportedException(crashreport);
                            } finally { stopPhysicsEvent = false; } // Paper
                        }
                        timing.stopTiming(); // Paper
                    } else {
                        this.scheduleUpdate(nextticklistentry.position, nextticklistentry.getBlock(), 0);
                    }
                }
                timings.scheduledBlocksTicking.stopTiming(); // Paper

                this.profiler.endSection();
                this.pendingTickListEntriesThisTick.clear();
                return !this.pendingTickListEntriesTreeSet.isEmpty();
            }
        }
    }

    @Override
    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunk, boolean flag) {
        ChunkPos chunkcoordintpair = chunk.getPos();
        int i = (chunkcoordintpair.x << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkcoordintpair.z << 4) - 2;
        int l = k + 16 + 2;

        return this.getPendingBlockUpdates(new StructureBoundingBox(i, 0, k, j, 256, l), flag);
    }

    @Override
    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureboundingbox, boolean flag) {
        ArrayList arraylist = null;

        for (int i = 0; i < 2; ++i) {
            Iterator iterator;

            if (i == 0) {
                iterator = this.pendingTickListEntriesTreeSet.iterator();
            } else {
                iterator = this.pendingTickListEntriesThisTick.iterator();
            }

            while (iterator.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator.next();
                BlockPos blockposition = nextticklistentry.position;

                if (blockposition.getX() >= structureboundingbox.minX && blockposition.getX() < structureboundingbox.maxX && blockposition.getZ() >= structureboundingbox.minZ && blockposition.getZ() < structureboundingbox.maxZ) {
                    if (flag) {
                        if (i == 0) {
                            // this.nextTickListHash.remove(nextticklistentry); // CraftBukkit - removed
                        }

                        iterator.remove();
                    }

                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(nextticklistentry);
                }
            }
        }

        return arraylist;
    }

    /* CraftBukkit start - We prevent spawning in general, so this butchering is not needed
    public void entityJoinedWorld(Entity entity, boolean flag) {
        if (!this.getSpawnAnimals() && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
            entity.die();
        }

        if (!this.getSpawnNPCs() && entity instanceof NPC) {
            entity.die();
        }

        super.entityJoinedWorld(entity, flag);
    }
    // CraftBukkit end */

    private boolean canSpawnNPCs() {
        return this.mcServer.getCanSpawnNPCs();
    }

    private boolean canSpawnAnimals() {
        return this.mcServer.getCanSpawnAnimals();
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        IChunkLoader ichunkloader = this.saveHandler.getChunkLoader(this.provider);

        // CraftBukkit start
        org.bukkit.craftbukkit.generator.InternalChunkGenerator gen;

        if (this.generator != null) {
            gen = new org.bukkit.craftbukkit.generator.CustomChunkGenerator(this, this.getSeed(), this.generator);
        } else if (this.provider instanceof WorldProviderHell) {
            gen = new org.bukkit.craftbukkit.generator.NetherChunkGenerator(this, this.getSeed());
        } else if (this.provider instanceof WorldProviderEnd) {
            gen = new org.bukkit.craftbukkit.generator.SkyLandsChunkGenerator(this, this.getSeed());
        } else {
            gen = new org.bukkit.craftbukkit.generator.NormalChunkGenerator(this, this.getSeed());
        }

        return new ChunkProviderServer(this, ichunkloader, new co.aikar.timings.TimedChunkGenerator(this, gen)); // Paper
        // CraftBukkit end
    }

    public List<TileEntity> a(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = Lists.newArrayList();

        // CraftBukkit start - Get tile entities from chunks instead of world
        for (int chunkX = (i >> 4); chunkX <= ((l - 1) >> 4); chunkX++) {
            for (int chunkZ = (k >> 4); chunkZ <= ((j1 - 1) >> 4); chunkZ++) {
                Chunk chunk = getChunkFromChunkCoords(chunkX, chunkZ);
                if (chunk == null) {
                    continue;
                }
                for (Object te : chunk.tileEntities.values()) {
                    TileEntity tileentity = (TileEntity) te;
                    if ((tileentity.pos.getX() >= i) && (tileentity.pos.getY() >= j) && (tileentity.pos.getZ() >= k) && (tileentity.pos.getX() < l) && (tileentity.pos.getY() < i1) && (tileentity.pos.getZ() < j1)) {
                        arraylist.add(tileentity);
                    }
                }
            }
        }
        /*
        for (int k1 = 0; k1 < this.tileEntityList.size(); ++k1) {
            TileEntity tileentity = (TileEntity) this.tileEntityList.get(k1);
            BlockPosition blockposition = tileentity.getPosition();

            if (blockposition.getX() >= i && blockposition.getY() >= j && blockposition.getZ() >= k && blockposition.getX() < l && blockposition.getY() < i1 && blockposition.getZ() < j1) {
                arraylist.add(tileentity);
            }
        }
        */
        // CraftBukkit end

        return arraylist;
    }

    @Override
    public boolean isBlockModifiable(EntityPlayer entityhuman, BlockPos blockposition) {
        return !this.mcServer.isBlockProtected(this, blockposition, entityhuman) && this.getWorldBorder().contains(blockposition);
    }

    @Override
    public void initialize(WorldSettings worldsettings) {
        if (!this.worldInfo.isInitialized()) {
            try {
                this.createSpawnPosition(worldsettings);
                if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
                    this.setDebugWorldSettings();
                }

                super.initialize(worldsettings);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");

                try {
                    this.addWorldInfoToCrashReport(crashreport);
                } catch (Throwable throwable1) {
                    ;
                }

                throw new ReportedException(crashreport);
            }

            this.worldInfo.setServerInitialized(true);
        }

    }

    private void setDebugWorldSettings() {
        this.worldInfo.setMapFeaturesEnabled(false);
        this.worldInfo.setAllowCommands(true);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThundering(false);
        this.worldInfo.setCleanWeatherTime(1000000000);
        this.worldInfo.setWorldTime(6000L);
        this.worldInfo.setGameType(GameType.SPECTATOR);
        this.worldInfo.setHardcore(false);
        this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        this.worldInfo.setDifficultyLocked(true);
        this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
    }

    private void createSpawnPosition(WorldSettings worldsettings) {
        if (!this.provider.canRespawnHere()) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
        } else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
        } else {
            this.findingSpawnPoint = true;
            BiomeProvider worldchunkmanager = this.provider.getBiomeProvider();
            List list = worldchunkmanager.getBiomesToSpawnIn();
            Random random = new Random(this.getSeed());
            BlockPos blockposition = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
            int i = 8;
            int j = this.provider.getAverageGroundLevel();
            int k = 8;

            // CraftBukkit start
            if (this.generator != null) {
                Random rand = new Random(this.getSeed());
                org.bukkit.Location spawn = this.generator.getFixedSpawnLocation(this.getWorld(), rand);

                if (spawn != null) {
                    if (spawn.getWorld() != this.getWorld()) {
                        throw new IllegalStateException("Cannot set spawn point for " + this.worldInfo.getWorldName() + " to be in another world (" + spawn.getWorld().getName() + ")");
                    } else {
                        this.worldInfo.setSpawn(new BlockPos(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()));
                        this.findingSpawnPoint = false;
                        return;
                    }
                }
            }
            // CraftBukkit end

            if (blockposition != null) {
                i = blockposition.getX();
                k = blockposition.getZ();
            } else {
                WorldServer.LOGGER.warn("Unable to find spawn biome");
            }

            int l = 0;

            while (!this.canSpawn(i, k)) { // CraftBukkit - use our own canSpawn
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                ++l;
                if (l == 1000) {
                    break;
                }
            }

            this.worldInfo.setSpawn(new BlockPos(i, j, k));
            this.findingSpawnPoint = false;
            if (worldsettings.isBonusChestEnabled()) {
                this.createBonusChest();
            }

        }
    }

    protected void createBonusChest() {
        WorldGeneratorBonusChest worldgenbonuschest = new WorldGeneratorBonusChest();

        for (int i = 0; i < 10; ++i) {
            int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
            BlockPos blockposition = this.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();

            if (worldgenbonuschest.generate(this, this.rand, blockposition)) {
                break;
            }
        }

    }

    @Nullable
    public BlockPos getSpawnCoordinate() {
        return this.provider.getSpawnCoordinate();
    }

    public void saveAllChunks(boolean flag, @Nullable IProgressUpdate iprogressupdate) throws MinecraftException {
        ChunkProviderServer chunkproviderserver = this.getChunkProvider();

        if (chunkproviderserver.canSave()) {
            if (flag) org.bukkit.Bukkit.getPluginManager().callEvent(new org.bukkit.event.world.WorldSaveEvent(getWorld())); // CraftBukkit // Paper - Incremental Auto Saving - Only fire event on full save
            timings.worldSave.startTiming(); // Paper
            if (flag || mcServer.serverAutoSave) { // Paper
            if (iprogressupdate != null) {
                iprogressupdate.displaySavingString("Saving level");
            }

            this.saveLevel();
            if (iprogressupdate != null) {
                iprogressupdate.displayLoadingString("Saving chunks");
            }
            } // Paper

            timings.worldSaveChunks.startTiming(); // Paper
            chunkproviderserver.saveChunks(flag);
            timings.worldSaveChunks.stopTiming(); // Paper
            // CraftBukkit - ArrayList -> Collection
            /* //Paper start Collection arraylist = chunkproviderserver.a();
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                Chunk chunk = (Chunk) iterator.next();

                if (chunk != null && !this.manager.a(chunk.locX, chunk.locZ)) {
                    chunkproviderserver.unload(chunk);
                }
            }*/
            // Paper end
            timings.worldSave.stopTiming(); // Paper
        }
    }

    public void flushToDisk() {
        ChunkProviderServer chunkproviderserver = this.getChunkProvider();

        if (chunkproviderserver.canSave()) {
            chunkproviderserver.flushToDisk();
        }
    }

    protected void saveLevel() throws MinecraftException {
        timings.worldSaveLevel.startTiming(); // Paper
        this.checkSessionLock();
        WorldServer[] aworldserver = this.mcServer.worlds;
        int i = aworldserver.length;

        for (int j = 0; j < i; ++j) {
            WorldServer worldserver = aworldserver[j];

            if (worldserver instanceof WorldServerMulti) {
                ((WorldServerMulti) worldserver).saveAdditionalData();
            }
        }

        // CraftBukkit start - Save secondary data for nether/end
        if (this instanceof WorldServerMulti) {
            ((WorldServerMulti) this).saveAdditionalData();
        }
        // CraftBukkit end

        this.worldInfo.setBorderSize(this.getWorldBorder().getDiameter());
        this.worldInfo.getBorderCenterX(this.getWorldBorder().getCenterX());
        this.worldInfo.getBorderCenterZ(this.getWorldBorder().getCenterZ());
        this.worldInfo.setBorderSafeZone(this.getWorldBorder().getDamageBuffer());
        this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder().getDamageAmount());
        this.worldInfo.setBorderWarningDistance(this.getWorldBorder().getWarningDistance());
        this.worldInfo.setBorderWarningTime(this.getWorldBorder().getWarningTime());
        this.worldInfo.setBorderLerpTarget(this.getWorldBorder().getTargetSize());
        this.worldInfo.setBorderLerpTime(this.getWorldBorder().getTimeUntilTarget());
        this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getPlayerList().getHostPlayerData());
        this.mapStorage.saveAllData();
        timings.worldSaveLevel.stopTiming(); // Paper
    }

    // CraftBukkit start
    @Override
    public boolean addEntity(Entity entity, SpawnReason spawnReason) { // Changed signature, added SpawnReason
        // World.addEntity(Entity) will call this, and we still want to perform
        // existing entity checking when it's called with a SpawnReason
        return this.canAddEntity(entity) ? super.addEntity(entity, spawnReason) : false;
    }
    // CraftBukkit end

    @Override
    public void loadEntities(Collection<Entity> collection) {
        ArrayList arraylist = Lists.newArrayList(collection);
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (this.canAddEntity(entity)) {
                this.loadedEntityList.add(entity);
                this.onEntityAdded(entity);
            }
        }

    }

    private boolean canAddEntity(Entity entity) {
        if (entity.isDead) {
            // WorldServer.a.warn("Tried to add entity {} but it was marked as removed already", EntityTypes.a(entity)); // CraftBukkit
            return false;
        } else {
            UUID uuid = entity.getUniqueID();

            if (this.entitiesByUuid.containsKey(uuid)) {
                Entity entity1 = this.entitiesByUuid.get(uuid);

                if (this.unloadedEntityList.contains(entity1)) {
                    this.unloadedEntityList.remove(entity1);
                } else {
                    if (!(entity instanceof EntityPlayer)) {
                        // WorldServer.a.warn("Keeping entity {} that already exists with UUID {}", EntityTypes.a(entity1), uuid.toString()); // CraftBukkit
                        return false;
                    }

                    WorldServer.LOGGER.warn("Force-added player with duplicate UUID {}", uuid.toString());
                }

                this.removeEntityDangerously(entity1);
            }

            return true;
        }
    }

    @Override
    protected void onEntityAdded(Entity entity) {
        super.onEntityAdded(entity);
        this.entitiesById.addKey(entity.getEntityId(), entity);
        this.entitiesByUuid.put(entity.getUniqueID(), entity);
        Entity[] aentity = entity.getParts();

        if (aentity != null) {
            Entity[] aentity1 = aentity;
            int i = aentity.length;

            for (int j = 0; j < i; ++j) {
                Entity entity1 = aentity1[j];

                this.entitiesById.addKey(entity1.getEntityId(), entity1);
            }
        }

    }

    @Override
    protected void onEntityRemoved(Entity entity) {
        if (!entity.valid) return; // Paper - Already removed, dont fire twice - this looks like it can happen even without our changes
        super.onEntityRemoved(entity);
        this.entitiesById.removeObject(entity.getEntityId());
        this.entitiesByUuid.remove(entity.getUniqueID());
        Entity[] aentity = entity.getParts();

        if (aentity != null) {
            Entity[] aentity1 = aentity;
            int i = aentity.length;

            for (int j = 0; j < i; ++j) {
                Entity entity1 = aentity1[j];

                this.entitiesById.removeObject(entity1.getEntityId());
            }
        }

    }

    @Override
    public boolean addWeatherEffect(Entity entity) {
        // CraftBukkit start
        LightningStrikeEvent lightning = new LightningStrikeEvent(this.getWorld(), (org.bukkit.entity.LightningStrike) entity.getBukkitEntity());
        this.getServer().getPluginManager().callEvent(lightning);

        if (lightning.isCancelled()) {
            return false;
        }
        // CraftBukkit end
        if (super.addWeatherEffect(entity)) {
            this.mcServer.getPlayerList().sendToAllNearExcept((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, 512.0D, dimension, new SPacketSpawnGlobalEntity(entity)); // CraftBukkit - Use dimension
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setEntityState(Entity entity, byte b0) {
        this.getEntityTracker().sendToTrackingAndSelf(entity, new SPacketEntityStatus(entity, b0));
    }

    @Override
    public ChunkProviderServer getChunkProvider() {
        return (ChunkProviderServer) super.getChunkProvider();
    }

    @Override
    public Explosion newExplosion(@Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        // CraftBukkit start
        Explosion explosion = super.newExplosion(entity, d0, d1, d2, f, flag, flag1);

        if (explosion.wasCanceled) {
            return explosion;
        }

        /* Remove
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f, flag, flag1);

        explosion.a();
        explosion.a(false);
        */
        // CraftBukkit end - TODO: Check if explosions are still properly implemented
        if (!flag1) {
            explosion.clearAffectedBlockPositions();
        }

        Iterator iterator = this.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityhuman = (EntityPlayer) iterator.next();

            if (entityhuman.getDistanceSq(d0, d1, d2) < 4096.0D) {
                ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketExplosion(d0, d1, d2, f, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityhuman)));
            }
        }

        return explosion;
    }

    @Override
    public void addBlockEvent(BlockPos blockposition, Block block, int i, int j) {
        BlockEventData blockactiondata = new BlockEventData(blockposition, block, i, j);
        Iterator iterator = this.blockEventQueue[this.blockEventCacheIndex].iterator();

        BlockEventData blockactiondata1;

        do {
            if (!iterator.hasNext()) {
                this.blockEventQueue[this.blockEventCacheIndex].add(blockactiondata);
                return;
            }

            blockactiondata1 = (BlockEventData) iterator.next();
        } while (!blockactiondata1.equals(blockactiondata));

    }

    private void sendQueuedBlockEvents() {
        while (!this.blockEventQueue[this.blockEventCacheIndex].isEmpty()) {
            int i = this.blockEventCacheIndex;

            this.blockEventCacheIndex ^= 1;
            Iterator iterator = this.blockEventQueue[i].iterator();

            while (iterator.hasNext()) {
                BlockEventData blockactiondata = (BlockEventData) iterator.next();

                if (this.fireBlockEvent(blockactiondata)) {
                    // CraftBukkit - this.worldProvider.dimension -> this.dimension
                    this.mcServer.getPlayerList().sendToAllNearExcept((EntityPlayer) null, blockactiondata.getPosition().getX(), blockactiondata.getPosition().getY(), blockactiondata.getPosition().getZ(), 64.0D, dimension, new SPacketBlockAction(blockactiondata.getPosition(), blockactiondata.getBlock(), blockactiondata.getEventID(), blockactiondata.getEventParameter()));
                }
            }

            this.blockEventQueue[i].clear();
        }

    }

    private boolean fireBlockEvent(BlockEventData blockactiondata) {
        IBlockState iblockdata = this.getBlockState(blockactiondata.getPosition());

        return iblockdata.getBlock() == blockactiondata.getBlock() ? iblockdata.onBlockEventReceived(this, blockactiondata.getPosition(), blockactiondata.getEventID(), blockactiondata.getEventParameter()) : false;
    }

    public void flush() {
        this.saveHandler.flush();
    }

    @Override
    protected void updateWeather() {
        boolean flag = this.isRaining();

        super.updateWeather();
        /* CraftBukkit start
        if (this.n != this.o) {
            this.server.getPlayerList().a((Packet) (new PacketPlayOutGameStateChange(7, this.o)), this.worldProvider.getDimensionManager().getDimensionID());
        }

        if (this.p != this.q) {
            this.server.getPlayerList().a((Packet) (new PacketPlayOutGameStateChange(8, this.q)), this.worldProvider.getDimensionManager().getDimensionID());
        }

        if (flag != this.isRaining()) {
            if (flag) {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(2, 0.0F));
            } else {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(1, 0.0F));
            }

            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(7, this.o));
            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(8, this.q));
        }
        // */
        if (flag != this.isRaining()) {
            // Only send weather packets to those affected
            for (int i = 0; i < this.playerEntities.size(); ++i) {
                if (((EntityPlayerMP) this.playerEntities.get(i)).world == this) {
                    ((EntityPlayerMP) this.playerEntities.get(i)).setPlayerWeather((!flag ? WeatherType.DOWNFALL : WeatherType.CLEAR), false);
                }
            }
        }
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            if (((EntityPlayerMP) this.playerEntities.get(i)).world == this) {
                ((EntityPlayerMP) this.playerEntities.get(i)).updateWeather(this.prevRainingStrength, this.rainingStrength, this.prevThunderingStrength, this.thunderingStrength);
            }
        }
        // CraftBukkit end

    }

    @Override
    @Nullable
    public MinecraftServer getMinecraftServer() {
        return this.mcServer;
    }

    public EntityTracker getEntityTracker() {
        return this.entityTracker;
    }

    public PlayerChunkMap getPlayerChunkMap() {
        return this.playerChunkMap;
    }

    public Teleporter getDefaultTeleporter() {
        return this.worldTeleporter;
    }

    public TemplateManager getStructureTemplateManager() {
        return this.saveHandler.getStructureTemplateManager();
    }

    public void spawnParticle(EnumParticleTypes enumparticle, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        this.spawnParticle(enumparticle, false, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }

    public void spawnParticle(EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        // CraftBukkit - visibility api support
        sendParticles(null, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }

    // Paper start - Particle API Expansion
    public void sendParticles(EntityPlayerMP sender, EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        sendParticles(this.playerEntities, sender, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }
    public void sendParticles(List<? extends EntityPlayer> receivers, EntityPlayerMP sender, EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        // Paper end
        // CraftBukkit end
        SPacketParticles packetplayoutworldparticles = new SPacketParticles(enumparticle, flag, (float) d0, (float) d1, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, i, aint);

        for (EntityPlayer entityhuman : receivers) { // Paper - Particle API Expansion
            EntityPlayerMP entityplayer = (EntityPlayerMP) entityhuman; // Paper - Particle API Expansion
            if (sender != null && !entityplayer.getBukkitEntity().canSee(sender.getBukkitEntity())) continue; // CraftBukkit
            BlockPos blockposition = entityplayer.getPosition();
            double d7 = blockposition.distanceSq(d0, d1, d2);


            this.sendPacketWithinDistance(entityplayer, flag, d0, d1, d2, packetplayoutworldparticles);
        }

    }

    public void spawnParticle(EntityPlayerMP entityplayer, EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        SPacketParticles packetplayoutworldparticles = new SPacketParticles(enumparticle, flag, (float) d0, (float) d1, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, i, aint);

        this.sendPacketWithinDistance(entityplayer, flag, d0, d1, d2, packetplayoutworldparticles);
    }

    private void sendPacketWithinDistance(EntityPlayerMP entityplayer, boolean flag, double d0, double d1, double d2, Packet<?> packet) {
        BlockPos blockposition = entityplayer.getPosition();
        double d3 = blockposition.distanceSq(d0, d1, d2);

        if (d3 <= 1024.0D || flag && d3 <= 262144.0D) {
            entityplayer.connection.sendPacket(packet);
        }

    }

    @Nullable
    public Entity getEntityFromUuid(UUID uuid) {
        return this.entitiesByUuid.get(uuid);
    }

    @Override
    public ListenableFuture<Object> addScheduledTask(Runnable runnable) {
        return this.mcServer.addScheduledTask(runnable);
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return this.mcServer.isCallingFromMinecraftThread();
    }

    @Override
    @Nullable
    public BlockPos findNearestStructure(String s, BlockPos blockposition, boolean flag) {
        return this.getChunkProvider().getNearestStructurePos(this, s, blockposition, flag);
    }

    public AdvancementManager getAdvancementManager() {
        return this.advancementManager;
    }

    public FunctionManager getFunctionManager() {
        return this.functionManager;
    }

    static class ServerBlockEventList extends ArrayList<BlockEventData> {

        private ServerBlockEventList() {}

        ServerBlockEventList(Object object) {
            this();
        }
    }
}

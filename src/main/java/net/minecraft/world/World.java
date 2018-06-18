package net.minecraft.world;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

import com.destroystokyo.paper.antixray.ChunkPacketBlockController;
import com.destroystokyo.paper.antixray.ChunkPacketBlockControllerAntiXray;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.pathfinding.PathWorldListener;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.loot.LootTableManager;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.ChunkGenerator;

// CraftBukkit start
import com.google.common.collect.Maps;
import java.util.HashMap; // Paper
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.LongHashSet; // Paper
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.ChunkGenerator;
// CraftBukkit end

// Paper start
import java.util.Set;
import com.destroystokyo.paper.antixray.ChunkPacketBlockController; // Anti-Xray
import com.destroystokyo.paper.antixray.ChunkPacketBlockControllerAntiXray; // Anti-Xray
import com.google.common.collect.Sets;
// Paper end

public abstract class World implements IBlockAccess {

    private int seaLevel = 63;
    protected boolean scheduledUpdatesAreImmediate;
    // Spigot start - guard entity list from removals
    public final List<Entity> loadedEntityList = new java.util.ArrayList<Entity>()
    {
        @Override
        public Entity remove(int index)
        {
            guard();
            return super.remove( index );
        }

        @Override
        public boolean remove(Object o)
        {
            guard();
            return super.remove( o );
        }

        private void guard()
        {
            if ( guardEntityList )
            {
                throw new java.util.ConcurrentModificationException();
            }
        }
    };
    // Spigot end
    protected final Set<Entity> unloadedEntityList = Sets.newHashSet(); // Paper
    //public final List<TileEntity> tileEntityList = Lists.newArrayList(); // Paper - remove unused list
    public final List<TileEntity> tickableTileEntities = Lists.newArrayList();
    private final List<TileEntity> addedTileEntityList = Lists.newArrayList();
    private final Set<TileEntity> tileEntitiesToBeRemoved = Sets.newHashSet(); // Paper
    public final List<EntityPlayer> playerEntities = Lists.newArrayList();
    public final List<Entity> weatherEffects = Lists.newArrayList();
    protected final IntHashMap<Entity> entitiesById = new IntHashMap();
    private final long cloudColour = 16777215L;
    private int skylightSubtracted;
    protected int updateLCG = (new Random()).nextInt();
    protected final int DIST_HASH_MAGIC = 1013904223;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    private int lastLightningBolt;
    public final Random rand = new Random();
    public WorldProvider provider;
    protected PathWorldListener pathListener = new PathWorldListener();
    protected List<IWorldEventListener> eventListeners;
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;
    public WorldInfo worldInfo;
    protected boolean findingSpawnPoint;
    public MapStorage mapStorage;
    protected VillageCollection villageCollection;
    protected LootTableManager lootTable;
    protected AdvancementManager advancementManager;
    protected FunctionManager functionManager;
    public final Profiler profiler;
    private final Calendar calendar;
    public Scoreboard worldScoreboard;
    public final boolean isRemote;
    public boolean spawnHostileMobs;
    public boolean spawnPeacefulMobs;
    private boolean processingLoadedTiles;
    private final WorldBorder worldBorder;
    int[] lightUpdateBlockList;

    // CraftBukkit start Added the following
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory = true;
    public ChunkGenerator generator;

    public boolean captureBlockStates = false;
    public boolean captureTreeGeneration = false;
    public ArrayList<BlockState> capturedBlockStates= new ArrayList<BlockState>(){
        @Override
        public boolean add( BlockState blockState ) {
            Iterator<BlockState> blockStateIterator = this.iterator();
            while( blockStateIterator.hasNext() ) {
                BlockState blockState1 = blockStateIterator.next();
                if ( blockState1.getLocation().equals( blockState.getLocation() ) ) {
                    return false;
                }
            }

            return super.add( blockState );
        }
    };
    public List<EntityItem> captureDrops;
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    public boolean populating;
    private int tickPosition;
    public final org.spigotmc.SpigotWorldConfig spigotConfig; // Spigot

    public final com.destroystokyo.paper.PaperWorldConfig paperConfig; // Paper
    public final ChunkPacketBlockController chunkPacketBlockController; // Paper - Anti-Xray

    public final co.aikar.timings.WorldTimingsHandler timings; // Paper
    private boolean guardEntityList; // Spigot
    public static boolean haveWeSilencedAPhysicsCrash;
    public static String blockLocation;
    private org.spigotmc.TickLimiter entityLimiter;
    private org.spigotmc.TickLimiter tileLimiter;
    private int tileTickPosition;
    public final Map<Explosion.CacheKey, Float> explosionDensityCache = new HashMap<>(); // Paper - Optimize explosions

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    // Paper start
    public Chunk getChunkIfLoaded(BlockPos blockposition) {
        return ((ChunkProviderServer) this.chunkProvider).getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }
    // Paper end

    public Chunk getChunkIfLoaded(int x, int z) {
        return ((ChunkProviderServer) this.chunkProvider).getChunkIfLoaded(x, z);
    }

    protected World(ISaveHandler idatamanager, WorldInfo worlddata, WorldProvider worldprovider, Profiler methodprofiler, boolean flag, ChunkGenerator gen, org.bukkit.World.Environment env) {
        this.spigotConfig = new org.spigotmc.SpigotWorldConfig( worlddata.getWorldName() ); // Spigot
        this.paperConfig = new com.destroystokyo.paper.PaperWorldConfig(worlddata.getWorldName(), this.spigotConfig); // Paper
        this.chunkPacketBlockController = this.paperConfig.antiXray ? new ChunkPacketBlockControllerAntiXray(this.paperConfig) : ChunkPacketBlockController.NO_OPERATION_INSTANCE; // Paper - Anti-Xray
        this.generator = gen;
        this.world = new CraftWorld((WorldServer) this, gen, env);
        this.ticksPerAnimalSpawns = this.getServer().getTicksPerAnimalSpawns(); // CraftBukkit
        this.ticksPerMonsterSpawns = this.getServer().getTicksPerMonsterSpawns(); // CraftBukkit
        // CraftBukkit end
        this.eventListeners = Lists.newArrayList(new IWorldEventListener[] { this.pathListener});
        this.calendar = Calendar.getInstance();
        this.worldScoreboard = new Scoreboard();
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.lightUpdateBlockList = new int['\u8000'];
        this.saveHandler = idatamanager;
        this.profiler = methodprofiler;
        this.worldInfo = worlddata;
        this.provider = worldprovider;
        this.isRemote = flag;
        this.worldBorder = worldprovider.createWorldBorder();
        // CraftBukkit start
        getWorldBorder().world = (WorldServer) this;
        // From PlayerList.setPlayerFileData
        getWorldBorder().addListener(new IBorderListener() {
            @Override
            public void onSizeChanged(WorldBorder worldborder, double d0) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_SIZE), worldborder.world);
            }

            @Override
            public void onTransitionStarted(WorldBorder worldborder, double d0, double d1, long i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.LERP_SIZE), worldborder.world);
            }

            @Override
            public void onCenterChanged(WorldBorder worldborder, double d0, double d1) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_CENTER), worldborder.world);
            }

            @Override
            public void onWarningTimeChanged(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_TIME), worldborder.world);
            }

            @Override
            public void onWarningDistanceChanged(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_BLOCKS), worldborder.world);
            }

            @Override
            public void onDamageAmountChanged(WorldBorder worldborder, double d0) {}

            @Override
            public void onDamageBufferChanged(WorldBorder worldborder, double d0) {}
        });
        this.getServer().addWorld(this.world);
        // CraftBukkit end
        timings = new co.aikar.timings.WorldTimingsHandler(this); // Paper - code below can generate new world and access timings
        this.keepSpawnInMemory = this.paperConfig.keepSpawnInMemory; // Paper
                this.entityLimiter = new org.spigotmc.TickLimiter(spigotConfig.entityMaxTickTime);
        this.tileLimiter = new org.spigotmc.TickLimiter(spigotConfig.tileMaxTickTime);
    }

    public World init() {
        return this;
    }

    public Biome getBiome(final BlockPos blockposition) {
        if (this.isBlockLoaded(blockposition)) {
            Chunk chunk = this.getChunkFromBlockCoords(blockposition);

            try {
                return chunk.getBiome(blockposition, this.provider.getBiomeProvider());
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Coordinates of biome request");

                crashreportsystemdetails.addDetail("Location", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return CrashReportCategory.getCoordinateInfo(blockposition);
                    }

                    @Override
                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        } else {
            return this.provider.getBiomeProvider().getBiome(blockposition, Biomes.PLAINS);
        }
    }

    public BiomeProvider getBiomeProvider() {
        return this.provider.getBiomeProvider();
    }

    protected abstract IChunkProvider createChunkProvider();

    public void initialize(WorldSettings worldsettings) {
        this.worldInfo.setServerInitialized(true);
    }

    @Nullable
    public MinecraftServer getMinecraftServer() {
        return null;
    }

    public IBlockState getGroundAboveSeaLevel(BlockPos blockposition) {
        BlockPos blockposition1;

        for (blockposition1 = new BlockPos(blockposition.getX(), this.getSeaLevel(), blockposition.getZ()); !this.isAirBlock(blockposition1.up()); blockposition1 = blockposition1.up()) {
            ;
        }

        return this.getBlockState(blockposition1);
    }

    private static boolean isValid(BlockPos blockposition) { // Paper - unused but incase reflection / future uses
        return blockposition.isValidLocation(); // Paper
    }

    private static boolean isOutsideBuildHeight(BlockPos blockposition) { // Paper - unused but incase reflection / future uses
        return blockposition.isInvalidYLocation(); // Paper
    }

    @Override
    public boolean isAirBlock(BlockPos blockposition) {
        return this.getBlockState(blockposition).getMaterial() == Material.AIR;
    }

    public boolean isBlockLoaded(BlockPos blockposition) {
        return getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4) != null; // Paper
    }

    public boolean isBlockLoaded(BlockPos blockposition, boolean flag) {
        return this.isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4, flag);
    }

    public boolean isAreaLoaded(BlockPos blockposition, int i) {
        return this.isAreaLoaded(blockposition, i, true);
    }

    public boolean isAreaLoaded(BlockPos blockposition, int i, boolean flag) {
        return this.isAreaLoaded(blockposition.getX() - i, blockposition.getY() - i, blockposition.getZ() - i, blockposition.getX() + i, blockposition.getY() + i, blockposition.getZ() + i, flag);
    }

    public boolean isAreaLoaded(BlockPos blockposition, BlockPos blockposition1) {
        return this.isAreaLoaded(blockposition, blockposition1, true);
    }

    public boolean isAreaLoaded(BlockPos blockposition, BlockPos blockposition1, boolean flag) {
        return this.isAreaLoaded(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), flag);
    }

    public boolean isAreaLoaded(StructureBoundingBox structureboundingbox) {
        return this.isAreaLoaded(structureboundingbox, true);
    }

    public boolean isAreaLoaded(StructureBoundingBox structureboundingbox, boolean flag) {
        return this.isAreaLoaded(structureboundingbox.minX, structureboundingbox.minY, structureboundingbox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ, flag);
    }

    private boolean isAreaLoaded(int i, int j, int k, int l, int i1, int j1, boolean flag) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    if (!this.isChunkLoaded(k1, l1, flag)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected abstract boolean isChunkLoaded(int i, int j, boolean flag);

    public Chunk getChunkFromBlockCoords(BlockPos blockposition) {
        return this.getChunkFromChunkCoords(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    public Chunk getChunkFromChunkCoords(int i, int j) {
        return this.chunkProvider.provideChunk(i, j);
    }

    public boolean isChunkGeneratedAt(int i, int j) {
        return this.isChunkLoaded(i, j, false) ? true : this.chunkProvider.isChunkGeneratedAt(i, j);
    }

    public boolean setBlockState(BlockPos blockposition, IBlockState iblockdata, int i) {
        // CraftBukkit start - tree generation
        if (this.captureTreeGeneration) {
            BlockState blockstate = null;
            Iterator<BlockState> it = capturedBlockStates.iterator();
            while (it.hasNext()) {
                BlockState previous = it.next();
                if (previous.getX() == blockposition.getX() && previous.getY() == blockposition.getY() && previous.getZ() == blockposition.getZ()) {
                    blockstate = previous;
                    it.remove();
                    break;
                }
            }
            if (blockstate == null) {
                blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i);
            }
            blockstate.setTypeId(CraftMagicNumbers.getId(iblockdata.getBlock()));
            blockstate.setRawData((byte) iblockdata.getBlock().getMetaFromState(iblockdata));
            this.capturedBlockStates.add(blockstate);
            return true;
        }
        // CraftBukkit end
        if (blockposition.isInvalidYLocation()) { // Paper
            return false;
        } else if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        } else {
            Chunk chunk = this.getChunkFromBlockCoords(blockposition);
            Block block = iblockdata.getBlock();

            // CraftBukkit start - capture blockstates
            BlockState blockstate = null;
            if (this.captureBlockStates) {
                //blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i); // Paper
                blockstate = world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getState(); // Paper - use CB getState to get a suitable snapshot
                this.capturedBlockStates.add(blockstate);
            }
            // CraftBukkit end

            IBlockState iblockdata1 = chunk.setBlockState(blockposition, iblockdata);

            if (iblockdata1 == null) {
                // CraftBukkit start - remove blockstate if failed
                if (this.captureBlockStates) {
                    this.capturedBlockStates.remove(blockstate);
                }
                // CraftBukkit end
                return false;
            } else {
                if (iblockdata.getLightOpacity() != iblockdata1.getLightOpacity() || iblockdata.getLightValue() != iblockdata1.getLightValue()) {
                    this.profiler.startSection("checkLight");
                    chunk.runOrQueueLightUpdate(() -> this.checkLight(blockposition)); // Paper - Queue light update
                    this.profiler.endSection();
                }

                /*
                if ((i & 2) != 0 && (!this.isClientSide || (i & 4) == 0) && chunk.isReady()) {
                    this.notify(blockposition, iblockdata1, iblockdata, i);
                }

                if (!this.isClientSide && (i & 1) != 0) {
                    this.update(blockposition, iblockdata1.getBlock(), true);
                    if (iblockdata.n()) {
                        this.updateAdjacentComparators(blockposition, block);
                    }
                } else if (!this.isClientSide && (i & 16) == 0) {
                    this.c(blockposition, block);
                }
                */

                // CraftBukkit start
                if (!this.captureBlockStates) { // Don't notify clients or update physics while capturing blockstates
                    // Modularize client and physic updates
                    notifyAndUpdatePhysics(blockposition, chunk, iblockdata1, iblockdata, i);
                }
                // CraftBukkit end

                return true;
            }
        }
    }

    // CraftBukkit start - Split off from above in order to directly send client and physic updates
    public void notifyAndUpdatePhysics(BlockPos blockposition, Chunk chunk, IBlockState oldBlock, IBlockState newBlock, int i) {
        if ((i & 2) != 0 && (!this.isRemote || (i & 4) == 0) && (chunk == null || chunk.isPopulated())) { // allow chunk to be null here as chunk.isReady() is false when we send our notification during block placement
            this.notifyBlockUpdate(blockposition, oldBlock, newBlock, i);
        }

        if (!this.isRemote && (i & 1) != 0) {
            this.notifyNeighborsRespectDebug(blockposition, oldBlock.getBlock(), true);
            if (newBlock.hasComparatorInputOverride()) {
                this.updateComparatorOutputLevel(blockposition, newBlock.getBlock());
            }
        } else if (!this.isRemote && (i & 16) == 0) {
            this.updateObservingBlocksAt(blockposition, newBlock.getBlock());
        }
    }
    // CraftBukkit end

    public boolean setBlockToAir(BlockPos blockposition) {
        return this.setBlockState(blockposition, Blocks.AIR.getDefaultState(), 3);
    }

    public boolean destroyBlock(BlockPos blockposition, boolean flag) {
        IBlockState iblockdata = this.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        if (iblockdata.getMaterial() == Material.AIR) {
            return false;
        } else {
            this.playEvent(2001, blockposition, Block.getStateId(iblockdata));
            if (flag) {
                block.dropBlockAsItem(this, blockposition, iblockdata, 0);
            }

            return this.setBlockState(blockposition, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public boolean setBlockState(BlockPos blockposition, IBlockState iblockdata) {
        return this.setBlockState(blockposition, iblockdata, 3);
    }

    public void notifyBlockUpdate(BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i) {
        for (int j = 0; j < this.eventListeners.size(); ++j) {
            this.eventListeners.get(j).notifyBlockUpdate(this, blockposition, iblockdata, iblockdata1, i);
        }

    }

    public void notifyNeighborsRespectDebug(BlockPos blockposition, Block block, boolean flag) {
        if (this.worldInfo.getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
            // CraftBukkit start
            if (populating) {
                return;
            }
            // CraftBukkit end
            this.notifyNeighborsOfStateChange(blockposition, block, flag);
        }

    }

    public void markBlocksDirtyVertical(int i, int j, int k, int l) {
        int i1;

        if (k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if (this.provider.hasSkyLight()) {
            for (i1 = k; i1 <= l; ++i1) {
                this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(i, i1, j));
            }
        }

        this.markBlockRangeForRenderUpdate(i, k, j, i, l, j);
    }

    public void markBlockRangeForRenderUpdate(BlockPos blockposition, BlockPos blockposition1) {
        this.markBlockRangeForRenderUpdate(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
    }

    public void markBlockRangeForRenderUpdate(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < this.eventListeners.size(); ++k1) {
            this.eventListeners.get(k1).markBlockRangeForRenderUpdate(i, j, k, l, i1, j1);
        }

    }

    public void updateObservingBlocksAt(BlockPos blockposition, Block block) {
        this.observedNeighborChanged(blockposition.west(), block, blockposition);
        this.observedNeighborChanged(blockposition.east(), block, blockposition);
        this.observedNeighborChanged(blockposition.down(), block, blockposition);
        this.observedNeighborChanged(blockposition.up(), block, blockposition);
        this.observedNeighborChanged(blockposition.north(), block, blockposition);
        this.observedNeighborChanged(blockposition.south(), block, blockposition);
    }

    public void notifyNeighborsOfStateChange(BlockPos blockposition, Block block, boolean flag) {
        if (captureBlockStates) { return; } // Paper - Cancel all physics during placement
        this.neighborChanged(blockposition.west(), block, blockposition);
        this.neighborChanged(blockposition.east(), block, blockposition);
        this.neighborChanged(blockposition.down(), block, blockposition);
        this.neighborChanged(blockposition.up(), block, blockposition);
        this.neighborChanged(blockposition.north(), block, blockposition);
        this.neighborChanged(blockposition.south(), block, blockposition);
        if (flag) {
            this.updateObservingBlocksAt(blockposition, block);
        }

        this.chunkPacketBlockController.updateNearbyBlocks(this, blockposition); // Paper - Anti-Xray
    }

    public void notifyNeighborsOfStateExcept(BlockPos blockposition, Block block, EnumFacing enumdirection) {
        if (enumdirection != EnumFacing.WEST) {
            this.neighborChanged(blockposition.west(), block, blockposition);
        }

        if (enumdirection != EnumFacing.EAST) {
            this.neighborChanged(blockposition.east(), block, blockposition);
        }

        if (enumdirection != EnumFacing.DOWN) {
            this.neighborChanged(blockposition.down(), block, blockposition);
        }

        if (enumdirection != EnumFacing.UP) {
            this.neighborChanged(blockposition.up(), block, blockposition);
        }

        if (enumdirection != EnumFacing.NORTH) {
            this.neighborChanged(blockposition.north(), block, blockposition);
        }

        if (enumdirection != EnumFacing.SOUTH) {
            this.neighborChanged(blockposition.south(), block, blockposition);
        }

    }

    public void neighborChanged(BlockPos blockposition, final Block block, BlockPos blockposition1) {
        if (!this.isRemote) {
            IBlockState iblockdata = this.getBlockState(blockposition);

            try {
                // CraftBukkit start
                CraftWorld world = ((WorldServer) this).getWorld();
                if (world != null && !((WorldServer)this).stopPhysicsEvent) { // Paper
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block));
                    this.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit end
                iblockdata.neighborChanged(this, blockposition, block, blockposition1);
            } catch (StackOverflowError stackoverflowerror) { // Spigot Start
                haveWeSilencedAPhysicsCrash = true;
                blockLocation = blockposition.getX() + ", " + blockposition.getY() + ", " + blockposition.getZ();
                // Spigot End
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Block being updated");

                crashreportsystemdetails.addDetail("Source block type", new ICrashReportDetail() {
                    public String a() throws Exception {
                        try {
                            return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.getIdFromBlock(block)), block.getUnlocalizedName(), block.getClass().getCanonicalName()});
                        } catch (Throwable throwable) {
                            return "ID #" + Block.getIdFromBlock(block);
                        }
                    }

                    @Override
                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                CrashReportCategory.addBlockInfo(crashreportsystemdetails, blockposition, iblockdata);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void observedNeighborChanged(BlockPos blockposition, final Block block, BlockPos blockposition1) {
        if (!this.isRemote) {
            IBlockState iblockdata = this.getBlockState(blockposition);

            if (iblockdata.getBlock() == Blocks.OBSERVER) {
                try {
                    ((BlockObserver) iblockdata.getBlock()).observedNeighborChanged(iblockdata, this, blockposition, block, blockposition1);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                    CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Block being updated");

                    crashreportsystemdetails.addDetail("Source block type", new ICrashReportDetail() {
                        public String a() throws Exception {
                            try {
                                return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.getIdFromBlock(block)), block.getUnlocalizedName(), block.getClass().getCanonicalName()});
                            } catch (Throwable throwable) {
                                return "ID #" + Block.getIdFromBlock(block);
                            }
                        }

                        @Override
                        public Object call() throws Exception {
                            return this.a();
                        }
                    });
                    CrashReportCategory.addBlockInfo(crashreportsystemdetails, blockposition, iblockdata);
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    public boolean isBlockTickPending(BlockPos blockposition, Block block) {
        return false;
    }

    public boolean canSeeSky(BlockPos blockposition) {
        return this.getChunkFromBlockCoords(blockposition).canSeeSky(blockposition);
    }

    public boolean canBlockSeeSky(BlockPos blockposition) {
        if (blockposition.getY() >= this.getSeaLevel()) {
            return this.canSeeSky(blockposition);
        } else {
            BlockPos blockposition1 = new BlockPos(blockposition.getX(), this.getSeaLevel(), blockposition.getZ());

            if (!this.canSeeSky(blockposition1)) {
                return false;
            } else {
                for (blockposition1 = blockposition1.down(); blockposition1.getY() > blockposition.getY(); blockposition1 = blockposition1.down()) {
                    IBlockState iblockdata = this.getBlockState(blockposition1);

                    if (iblockdata.getLightOpacity() > 0 && !iblockdata.getMaterial().isLiquid()) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int getLight(BlockPos blockposition) {
        if (blockposition.getY() < 0) {
            return 0;
        } else {
            if (blockposition.getY() >= 256) {
                blockposition = new BlockPos(blockposition.getX(), 255, blockposition.getZ());
            }

            return this.getChunkFromBlockCoords(blockposition).getLightSubtracted(blockposition, 0);
        }
    }

    // Paper start - test if meets light level, return faster
    // logic copied from below
    public boolean isLightLevel(BlockPos blockposition, int level) {
        if (blockposition.isValidLocation()) {
            if (this.getBlockState(blockposition).useNeighborBrightness()) {
                if (this.getLight(blockposition.up(), false) >= level) {
                    return true;
                }
                if (this.getLight(blockposition.east(), false) >= level) {
                    return true;
                }
                if (this.getLight(blockposition.west(), false) >= level) {
                    return true;
                }
                if (this.getLight(blockposition.south(), false) >= level) {
                    return true;
                }
                if (this.getLight(blockposition.north(), false) >= level) {
                    return true;
                }
                return false;
            } else {
                if (blockposition.getY() >= 256) {
                    blockposition = new BlockPos(blockposition.getX(), 255, blockposition.getZ());
                }

                Chunk chunk = this.getChunkFromBlockCoords(blockposition);
                return chunk.getLightSubtracted(blockposition, this.getSkylightSubtracted()) >= level;
            }
        } else {
            return true;
        }
    }
    // Paper end

    public int getLightFromNeighbors(BlockPos blockposition) {
        return this.getLight(blockposition, true);
    }

    public int getLight(BlockPos blockposition, boolean flag) {
        if (blockposition.getX() >= -30000000 && blockposition.getZ() >= -30000000 && blockposition.getX() < 30000000 && blockposition.getZ() < 30000000) {
            if (flag && this.getBlockState(blockposition).useNeighborBrightness()) {
                int i = this.getLight(blockposition.up(), false);
                int j = this.getLight(blockposition.east(), false);
                int k = this.getLight(blockposition.west(), false);
                int l = this.getLight(blockposition.south(), false);
                int i1 = this.getLight(blockposition.north(), false);

                if (j > i) {
                    i = j;
                }

                if (k > i) {
                    i = k;
                }

                if (l > i) {
                    i = l;
                }

                if (i1 > i) {
                    i = i1;
                }

                return i;
            } else if (blockposition.getY() < 0) {
                return 0;
            } else {
                if (blockposition.getY() >= 256) {
                    blockposition = new BlockPos(blockposition.getX(), 255, blockposition.getZ());
                }
                if (!this.isBlockLoaded(blockposition)) return 0; // Paper

                Chunk chunk = this.getChunkFromBlockCoords(blockposition);

                return chunk.getLightSubtracted(blockposition, this.skylightSubtracted);
            }
        } else {
            return 15;
        }
    }

    public BlockPos getHeight(BlockPos blockposition) {
        return new BlockPos(blockposition.getX(), this.getHeight(blockposition.getX(), blockposition.getZ()), blockposition.getZ());
    }

    public int getHeight(int i, int j) {
        int k;

        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (this.isChunkLoaded(i >> 4, j >> 4, true)) {
                k = this.getChunkFromChunkCoords(i >> 4, j >> 4).getHeightValue(i & 15, j & 15);
            } else {
                k = 0;
            }
        } else {
            k = this.getSeaLevel() + 1;
        }

        return k;
    }

    @Deprecated
    public int getChunksLowestHorizon(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.isChunkLoaded(i >> 4, j >> 4, true)) {
                return 0;
            } else {
                Chunk chunk = this.getChunkFromChunkCoords(i >> 4, j >> 4);

                return chunk.getLowestHeight();
            }
        } else {
            return this.getSeaLevel() + 1;
        }
    }

    public int getLightFor(EnumSkyBlock enumskyblock, BlockPos blockposition) {
        if (blockposition.getY() < 0) {
            blockposition = new BlockPos(blockposition.getX(), 0, blockposition.getZ());
        }

        if (!blockposition.isValidLocation()) { // Paper
            return enumskyblock.defaultLightValue;
        } else if (!this.isBlockLoaded(blockposition)) {
            return enumskyblock.defaultLightValue;
        } else {
            Chunk chunk = this.getChunkFromBlockCoords(blockposition);

            return chunk.getLightFor(enumskyblock, blockposition);
        }
    }

    public void setLightFor(EnumSkyBlock enumskyblock, BlockPos blockposition, int i) {
        if (blockposition.isValidLocation()) { // Paper
            if (this.isBlockLoaded(blockposition)) {
                Chunk chunk = this.getChunkFromBlockCoords(blockposition);

                chunk.setLightFor(enumskyblock, blockposition, i);
                this.notifyLightSet(blockposition);
            }
        }
    }

    public void notifyLightSet(BlockPos blockposition) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).notifyLightSet(blockposition);
        }

    }

    public float getLightBrightness(BlockPos blockposition) {
        return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(blockposition)];
    }

    // Paper start - reduces need to do isLoaded before getType
    public IBlockState getTypeIfLoaded(BlockPos blockposition) {
        // CraftBukkit start - tree generation
        final int x = blockposition.getX();
        final int y = blockposition.getY();
        final int z = blockposition.getZ();
        if (captureTreeGeneration) {
            final IBlockState previous = getCapturedBlockType(x, y, z);
            if (previous != null) {
                return previous;
            }
        }
        // CraftBukkit end
        Chunk chunk = ((ChunkProviderServer) this.chunkProvider).getChunkIfLoaded(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockData(x, y, z);
        }
        return null;
    }
    // Paper end

    @Override
    public IBlockState getBlockState(BlockPos blockposition) {
        // CraftBukkit start - tree generation
        // Paper start - optimize getType lookup to reduce instructions - getBlockData already enforces valid Y, move tree out
        final int x = blockposition.getX();
        final int y = blockposition.getY();
        final int z = blockposition.getZ();
        if (captureTreeGeneration) {
            final IBlockState previous = getCapturedBlockType(x, y, z);
            if (previous != null) {
                return previous;
            }
        }
        // CraftBukkit end
        return this.chunkProvider.provideChunk(x >> 4, z >> 4).getBlockData(x, y, z);
        // Paper end
    }

    // Paper start
    private IBlockState getCapturedBlockType(int x, int y, int z) {
        Iterator<BlockState> it = capturedBlockStates.iterator();
        while (it.hasNext()) {
            BlockState previous = it.next();
            if (previous.getX() == x && previous.getY() == y && previous.getZ() == z) {
                return CraftMagicNumbers.getBlock(previous.getTypeId()).getStateFromMeta(previous.getRawData());
            }
        }
        return null;
    }
    // Paper end

    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }

    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d vec3d, Vec3d vec3d1) {
        return this.rayTraceBlocks(vec3d, vec3d1, false, false, false);
    }

    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d vec3d, Vec3d vec3d1, boolean flag) {
        return this.rayTraceBlocks(vec3d, vec3d1, flag, false, false);
    }

    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d vec3d, Vec3d vec3d1, boolean flag, boolean flag1, boolean flag2) {
        if (!Double.isNaN(vec3d.x) && !Double.isNaN(vec3d.y) && !Double.isNaN(vec3d.z)) {
            if (!Double.isNaN(vec3d1.x) && !Double.isNaN(vec3d1.y) && !Double.isNaN(vec3d1.z)) {
                int i = MathHelper.floor(vec3d1.x);
                int j = MathHelper.floor(vec3d1.y);
                int k = MathHelper.floor(vec3d1.z);
                int l = MathHelper.floor(vec3d.x);
                int i1 = MathHelper.floor(vec3d.y);
                int j1 = MathHelper.floor(vec3d.z);
                BlockPos blockposition = new BlockPos(l, i1, j1);
                IBlockState iblockdata = this.getBlockState(blockposition);
                Block block = iblockdata.getBlock();

                if ((!flag1 || iblockdata.getCollisionBoundingBox(this, blockposition) != Block.NULL_AABB) && block.canCollideCheck(iblockdata, flag)) {
                    RayTraceResult movingobjectposition = iblockdata.collisionRayTrace(this, blockposition, vec3d, vec3d1);

                    if (movingobjectposition != null) {
                        return movingobjectposition;
                    }
                }

                RayTraceResult movingobjectposition1 = null;
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return flag2 ? movingobjectposition1 : null;
                    }

                    boolean flag3 = true;
                    boolean flag4 = true;
                    boolean flag5 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = l + 1.0D;
                    } else if (i < l) {
                        d0 = l + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if (j > i1) {
                        d1 = i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = i1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    if (k > j1) {
                        d2 = j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = j1 + 0.0D;
                    } else {
                        flag5 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec3d1.x - vec3d.x;
                    double d7 = vec3d1.y - vec3d.y;
                    double d8 = vec3d1.z - vec3d.z;

                    if (flag3) {
                        d3 = (d0 - vec3d.x) / d6;
                    }

                    if (flag4) {
                        d4 = (d1 - vec3d.y) / d7;
                    }

                    if (flag5) {
                        d5 = (d2 - vec3d.z) / d8;
                    }

                    if (d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumdirection;

                    if (d3 < d4 && d3 < d5) {
                        enumdirection = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec3d = new Vec3d(d0, vec3d.y + d7 * d3, vec3d.z + d8 * d3);
                    } else if (d4 < d5) {
                        enumdirection = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec3d = new Vec3d(vec3d.x + d6 * d4, d1, vec3d.z + d8 * d4);
                    } else {
                        enumdirection = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec3d = new Vec3d(vec3d.x + d6 * d5, vec3d.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec3d.x) - (enumdirection == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec3d.y) - (enumdirection == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec3d.z) - (enumdirection == EnumFacing.SOUTH ? 1 : 0);
                    blockposition = new BlockPos(l, i1, j1);
                    IBlockState iblockdata1 = this.getBlockState(blockposition);
                    Block block1 = iblockdata1.getBlock();

                    if (!flag1 || iblockdata1.getMaterial() == Material.PORTAL || iblockdata1.getCollisionBoundingBox(this, blockposition) != Block.NULL_AABB) {
                        if (block1.canCollideCheck(iblockdata1, flag)) {
                            RayTraceResult movingobjectposition2 = iblockdata1.collisionRayTrace(this, blockposition, vec3d, vec3d1);

                            if (movingobjectposition2 != null) {
                                return movingobjectposition2;
                            }
                        } else {
                            movingobjectposition1 = new RayTraceResult(RayTraceResult.Type.MISS, vec3d, enumdirection, blockposition);
                        }
                    }
                }

                return flag2 ? movingobjectposition1 : null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void playSound(@Nullable EntityPlayer entityhuman, BlockPos blockposition, SoundEvent soundeffect, SoundCategory soundcategory, float f, float f1) {
        this.playSound(entityhuman, blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, soundeffect, soundcategory, f, f1);
    }

    // Paper start - OBFHELPER
    public final void sendSoundEffect(@Nullable EntityPlayer fromEntity, double x, double y, double z, SoundEvent soundeffect, SoundCategory soundcategory, float volume, float pitch) {
        this.playSound(fromEntity, x, y, z, soundeffect, soundcategory, volume, pitch);
    }
    // Paper end

    public void playSound(@Nullable EntityPlayer entityhuman, double d0, double d1, double d2, SoundEvent soundeffect, SoundCategory soundcategory, float f, float f1) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).playSoundToAllNearExcept(entityhuman, soundeffect, soundcategory, d0, d1, d2, f, f1);
        }

    }

    public void playSound(double d0, double d1, double d2, SoundEvent soundeffect, SoundCategory soundcategory, float f, float f1, boolean flag) {}

    public void playRecord(BlockPos blockposition, @Nullable SoundEvent soundeffect) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).playRecord(soundeffect, blockposition);
        }

    }

    public void spawnParticle(EnumParticleTypes enumparticle, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        this.spawnParticle(enumparticle.getParticleID(), enumparticle.getShouldIgnoreRange(), d0, d1, d2, d3, d4, d5, aint);
    }

    public void spawnAlwaysVisibleParticle(int i, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        for (int j = 0; j < this.eventListeners.size(); ++j) {
            this.eventListeners.get(j).spawnParticle(i, false, true, d0, d1, d2, d3, d4, d5, aint);
        }

    }

    private void spawnParticle(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        for (int j = 0; j < this.eventListeners.size(); ++j) {
            this.eventListeners.get(j).spawnParticle(i, flag, d0, d1, d2, d3, d4, d5, aint);
        }

    }

    public boolean addWeatherEffect(Entity entity) {
        this.weatherEffects.add(entity);
        return true;
    }

    public boolean spawnEntity(Entity entity) {
        // CraftBukkit start - Used for entities other than creatures
        return addEntity(entity, SpawnReason.DEFAULT);
    }

    public boolean addEntity(Entity entity, SpawnReason spawnReason) { // Changed signature, added SpawnReason
        org.spigotmc.AsyncCatcher.catchOp( "entity add"); // Spigot
        if (entity == null) return false;
        if (entity.valid) { MinecraftServer.LOGGER.error("Attempted Double World add on " + entity, new Throwable()); return true; } // Paper

        org.bukkit.event.Cancellable event = null;
        if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayerMP)) {
            boolean isAnimal = entity instanceof EntityAnimal || entity instanceof EntityWaterMob || entity instanceof EntityGolem;
            boolean isMonster = entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntitySlime;
            boolean isNpc = entity instanceof INpc;

            if (spawnReason != SpawnReason.CUSTOM) {
                if (isAnimal && !spawnPeacefulMobs || isMonster && !spawnHostileMobs || isNpc && !getServer().getServer().getCanSpawnNPCs()) {
                    entity.isDead = true;
                    return false;
                }
            }

            event = CraftEventFactory.callCreatureSpawnEvent((EntityLivingBase) entity, spawnReason);
        } else if (entity instanceof EntityItem) {
            event = CraftEventFactory.callItemSpawnEvent((EntityItem) entity);
        } else if (entity.getBukkitEntity() instanceof org.bukkit.entity.Projectile) {
            // Not all projectiles extend EntityProjectile, so check for Bukkit interface instead
            event = CraftEventFactory.callProjectileLaunchEvent(entity);
        } else if (entity.getBukkitEntity() instanceof org.bukkit.entity.Vehicle){
            event = CraftEventFactory.callVehicleCreateEvent(entity);
        }
        // Spigot start
        else if (entity instanceof EntityXPOrb) {
            EntityXPOrb xp = (EntityXPOrb) entity;
            double radius = spigotConfig.expMerge;
            if (radius > 0) {
                // Paper start - Maximum exp value when merging - Whole section has been tweaked, see comments for specifics
                final int maxValue = paperConfig.expMergeMaxValue;
                final boolean mergeUnconditionally = maxValue <= 0;
                if (mergeUnconditionally || xp.xpValue < maxValue) { // Paper - Skip iteration if unnecessary

                List<Entity> entities = this.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().grow(radius, radius, radius));
                for (Entity e : entities) {
                    if (e instanceof EntityXPOrb) {
                        EntityXPOrb loopItem = (EntityXPOrb) e;
                        if (!loopItem.isDead && !(maxValue > 0 && loopItem.xpValue >= maxValue) && new com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent((org.bukkit.entity.ExperienceOrb) entity.getBukkitEntity(), (org.bukkit.entity.ExperienceOrb) loopItem.getBukkitEntity()).callEvent()) { // Paper
                            xp.xpValue += loopItem.xpValue;
                            // Paper start
                            if (!mergeUnconditionally && xp.xpValue > maxValue) {
                                loopItem.xpValue = xp.xpValue - maxValue;
                                xp.xpValue = maxValue;
                                break;
                            }
                            // Paper end
                            loopItem.setDead();
                        }
                    }
                }

                } // Paper end - End iteration skip check - All tweaking ends here
            }
        } // Spigot end

        if (event != null && (event.isCancelled() || entity.isDead)) {
            entity.isDead = true;
            return false;
        }
        // CraftBukkit end

        int i = MathHelper.floor(entity.posX / 16.0D);
        int j = MathHelper.floor(entity.posZ / 16.0D);
        boolean flag = entity.forceSpawn;

        // Paper start - Set origin location when the entity is being added to the world
        if (entity.origin == null) {
            entity.origin = entity.getBukkitEntity().getLocation();
        }
        // Paper end

        if (entity instanceof EntityPlayer) {
            flag = true;
        }

        if (!flag && !this.isChunkLoaded(i, j, false)) {
            return false;
        } else {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                this.playerEntities.add(entityhuman);
                this.updateAllPlayersSleepingFlag();
            }

            this.getChunkFromChunkCoords(i, j).addEntity(entity);
            this.loadedEntityList.add(entity);
            this.onEntityAdded(entity);
            return true;
        }
    }

    protected void onEntityAdded(Entity entity) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).onEntityAdded(entity);
        }

        entity.valid = true; // CraftBukkit
        new com.destroystokyo.paper.event.entity.EntityAddToWorldEvent(entity.getBukkitEntity()).callEvent(); // Paper - fire while valid
    }

    protected void onEntityRemoved(Entity entity) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).onEntityRemoved(entity);
        }

        new com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent(entity.getBukkitEntity()).callEvent(); // Paper - fire while valid
        entity.valid = false; // CraftBukkit
    }

    public void removeEntity(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp( "entity kill"); // Spigot
        if (entity.isBeingRidden()) {
            entity.removePassengers();
        }

        if (entity.isRiding()) {
            entity.dismountRidingEntity();
        }

        entity.setDead();
        if (entity instanceof EntityPlayer) {
            this.playerEntities.remove(entity);
            // Spigot start
            for ( Object o : mapStorage.loadedDataList )
            {
                if ( o instanceof MapData )
                {
                    MapData map = (MapData) o;
                    map.playersHashMap.remove( entity );
                    for ( Iterator<MapData.MapInfo> iter = map.playersArrayList.iterator(); iter.hasNext(); )
                    {
                        if ( iter.next().player == entity )
                        {
                            map.mapDecorations.remove(entity.getUniqueID()); // Paper
                            iter.remove();
                        }
                    }
                }
            }
            // Spigot end
            this.updateAllPlayersSleepingFlag();
            this.onEntityRemoved(entity);
        }

    }

    public void removeEntityDangerously(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp( "entity remove"); // Spigot
        entity.setDropItemsWhenDead(false);
        entity.setDead();
        if (entity instanceof EntityPlayer) {
            this.playerEntities.remove(entity);
            this.updateAllPlayersSleepingFlag();
        }

        if (!guardEntityList) { // Spigot - It will get removed after the tick if we are ticking
        int i = entity.chunkCoordX;
        int j = entity.chunkCoordZ;

        if (entity.addedToChunk && this.isChunkLoaded(i, j, true)) {
            this.getChunkFromChunkCoords(i, j).removeEntity(entity);
        }

        // CraftBukkit start - Decrement loop variable field if we've already ticked this entity
        int index = this.loadedEntityList.indexOf(entity);
        if (index != -1) {
            if (index <= this.tickPosition) {
                this.tickPosition--;
            }
            this.loadedEntityList.remove(index);
        }
        // CraftBukkit end
        } // Spigot
        this.onEntityRemoved(entity);
    }

    public void addEventListener(IWorldEventListener iworldaccess) {
        this.eventListeners.add(iworldaccess);
    }

    private boolean getCollisionBoxes(@Nullable Entity entity, AxisAlignedBB axisalignedbb, boolean flag, @Nullable List<AxisAlignedBB> list) {
        int i = MathHelper.floor(axisalignedbb.minX) - 1;
        int j = MathHelper.ceil(axisalignedbb.maxX) + 1;
        int k = MathHelper.floor(axisalignedbb.minY) - 1;
        int l = MathHelper.ceil(axisalignedbb.maxY) + 1;
        int i1 = MathHelper.floor(axisalignedbb.minZ) - 1;
        int j1 = MathHelper.ceil(axisalignedbb.maxZ) + 1;
        WorldBorder worldborder = this.getWorldBorder();
        boolean flag1 = entity != null && entity.isOutsideBorder();
        boolean flag2 = entity != null && this.isInsideWorldBorder(entity);
        IBlockState iblockdata = Blocks.STONE.getDefaultState();
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = i1; l1 < j1; ++l1) {
                    boolean flag3 = k1 == i || k1 == j - 1;
                    boolean flag4 = l1 == i1 || l1 == j1 - 1;

                    if ((!flag3 || !flag4) && this.isBlockLoaded(blockposition_pooledblockposition.setPos(k1, 64, l1))) {
                        for (int i2 = k; i2 < l; ++i2) {
                            if (!flag3 && !flag4 || i2 != l - 1) {
                                if (flag) {
                                    if (k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000) {
                                        boolean flag5 = true;

                                        return flag5;
                                    }
                                } else if (entity != null && flag1 == flag2) {
                                    entity.setOutsideBorder(!flag2);
                                }

                                blockposition_pooledblockposition.setPos(k1, i2, l1);
                                IBlockState iblockdata1;

                                if (!flag && !worldborder.contains(blockposition_pooledblockposition) && flag2) {
                                    iblockdata1 = iblockdata;
                                } else {
                                    iblockdata1 = this.getBlockState(blockposition_pooledblockposition);
                                }

                                iblockdata1.addCollisionBoxToList(this, blockposition_pooledblockposition, axisalignedbb, list, entity, false);
                                if (flag && !list.isEmpty()) {
                                    boolean flag6 = true;

                                    return flag6;
                                }
                            }
                        }
                    }
                }
            }

            return !list.isEmpty();
        } finally {
            blockposition_pooledblockposition.release();
        }
    }

    public List<AxisAlignedBB> getCollisionBoxes(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
        ArrayList arraylist = Lists.newArrayList();

        this.getCollisionBoxes(entity, axisalignedbb, false, arraylist);
        if (entity != null) {
            if (entity instanceof EntityArmorStand && !entity.world.paperConfig.armorStandEntityLookups) return arraylist; // Paper
            List list = this.getEntitiesWithinAABBExcludingEntity(entity, axisalignedbb.grow(0.25D));

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (!entity.isRidingSameEntity(entity1)) {
                    AxisAlignedBB axisalignedbb1 = entity1.getCollisionBoundingBox();

                    if (axisalignedbb1 != null && axisalignedbb1.intersects(axisalignedbb)) {
                        arraylist.add(axisalignedbb1);
                    }

                    axisalignedbb1 = entity.getCollisionBox(entity1);
                    if (axisalignedbb1 != null && axisalignedbb1.intersects(axisalignedbb)) {
                        arraylist.add(axisalignedbb1);
                    }
                }
            }
        }

        return arraylist;
    }

    public boolean isInsideWorldBorder(Entity entity) {
        double d0 = this.worldBorder.minX();
        double d1 = this.worldBorder.minZ();
        double d2 = this.worldBorder.maxX();
        double d3 = this.worldBorder.maxZ();

        if (entity.isOutsideBorder()) {
            ++d0;
            ++d1;
            --d2;
            --d3;
        } else {
            --d0;
            --d1;
            ++d2;
            ++d3;
        }

        return entity.posX > d0 && entity.posX < d2 && entity.posZ > d1 && entity.posZ < d3;
    }

    public boolean collidesWithAnyBlock(AxisAlignedBB axisalignedbb) {
        return this.getCollisionBoxes((Entity) null, axisalignedbb, true, Lists.<AxisAlignedBB>newArrayList()); // CraftBukkit - decompile error
    }

    public int calculateSkylightSubtracted(float f) {
        float f1 = this.getCelestialAngle(f);
        float f2 = 1.0F - (MathHelper.cos(f1 * 6.2831855F) * 2.0F + 0.5F);

        f2 = MathHelper.clamp(f2, 0.0F, 1.0F);
        f2 = 1.0F - f2;
        f2 = (float) (f2 * (1.0D - this.getRainStrength(f) * 5.0F / 16.0D));
        f2 = (float) (f2 * (1.0D - this.getThunderStrength(f) * 5.0F / 16.0D));
        f2 = 1.0F - f2;
        return (int) (f2 * 11.0F);
    }

    public float getCelestialAngle(float f) {
        return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), f);
    }

    public float getCurrentMoonPhaseFactor() {
        return WorldProvider.MOON_PHASE_FACTORS[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
    }

    public float getCelestialAngleRadians(float f) {
        float f1 = this.getCelestialAngle(f);

        return f1 * 6.2831855F;
    }

    public BlockPos getPrecipitationHeight(BlockPos blockposition) {
        return this.getChunkFromBlockCoords(blockposition).getPrecipitationHeight(blockposition);
    }

    public BlockPos getTopSolidOrLiquidBlock(BlockPos blockposition) {
        Chunk chunk = this.getChunkFromBlockCoords(blockposition);

        BlockPos blockposition1;
        BlockPos blockposition2;

        for (blockposition1 = new BlockPos(blockposition.getX(), chunk.getTopFilledSegment() + 16, blockposition.getZ()); blockposition1.getY() >= 0; blockposition1 = blockposition2) {
            blockposition2 = blockposition1.down();
            Material material = chunk.getBlockState(blockposition2).getMaterial();

            if (material.blocksMovement() && material != Material.LEAVES) {
                break;
            }
        }

        return blockposition1;
    }

    public boolean isUpdateScheduled(BlockPos blockposition, Block block) {
        return true;
    }

    public void scheduleUpdate(BlockPos blockposition, Block block, int i) {}

    public void updateBlockTick(BlockPos blockposition, Block block, int i, int j) {}

    public void scheduleBlockUpdate(BlockPos blockposition, Block block, int i, int j) {}

    public void updateEntities() {
        this.profiler.startSection("entities");
        this.profiler.startSection("global");

        int i;
        Entity entity;

        for (i = 0; i < this.weatherEffects.size(); ++i) {
            entity = this.weatherEffects.get(i);
            // CraftBukkit start - Fixed an NPE
            if (entity == null) {
                continue;
            }
            // CraftBukkit end

            try {
                ++entity.ticksExisted;
                entity.onUpdate();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking entity");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Entity being ticked");

                if (entity == null) {
                    crashreportsystemdetails.addCrashSection("Entity", "~~NULL~~");
                } else {
                    entity.addEntityCrashInfo(crashreportsystemdetails);
                }

                throw new ReportedException(crashreport);
            }

            if (entity.isDead) {
                this.weatherEffects.remove(i--);
            }
        }

        this.profiler.endStartSection("remove");
        timings.entityRemoval.startTiming(); // Paper
        this.loadedEntityList.removeAll(this.unloadedEntityList);

        int j;
        // Paper start - Set based removal lists
        for (Entity e : this.unloadedEntityList) {
            j = e.getChunkZ();
            int k = e.getChunkX();

            if (e.isAddedToChunk() && this.isChunkLoaded(k, j, true)) {
                this.getChunkFromChunkCoords(k, j).removeEntity(e);
            }
        }

        for (Entity e : this.unloadedEntityList) {
            this.onEntityRemoved(e);
        }
        // Paper end

        this.unloadedEntityList.clear();
        this.tickPlayers();
        timings.entityRemoval.stopTiming(); // Paper
        this.profiler.endStartSection("regular");

        CrashReportCategory crashreportsystemdetails1;
        CrashReport crashreport1;

        org.spigotmc.ActivationRange.activateEntities(this); // Spigot
        timings.entityTick.startTiming(); // Spigot
        guardEntityList = true; // Spigot
        // CraftBukkit start - Use field for loop variable
        co.aikar.timings.TimingHistory.entityTicks += this.loadedEntityList.size(); // Paper
        int entitiesThisCycle = 0;
        // Paper start - Disable tick limiters
        //if (tickPosition < 0) tickPosition = 0;
        for (tickPosition = 0; tickPosition < loadedEntityList.size(); tickPosition++) {
            // Paper end
            tickPosition = (tickPosition < loadedEntityList.size()) ? tickPosition : 0;
            entity = this.loadedEntityList.get(this.tickPosition);
            // CraftBukkit end
            Entity entity1 = entity.getRidingEntity();

            if (entity1 != null) {
                if (!entity1.isDead && entity1.isPassenger(entity)) {
                    continue;
                }

                entity.dismountRidingEntity();
            }

            this.profiler.startSection("tick");
            if (!entity.isDead && !(entity instanceof EntityPlayerMP)) {
                try {
                    entity.tickTimer.startTiming(); // Paper
                    this.updateEntity(entity);
                    entity.tickTimer.stopTiming(); // Paper
                } catch (Throwable throwable1) {
                    entity.tickTimer.stopTiming();
                    // Paper start - Prevent tile entity and entity crashes
                    String msg = "Entity threw exception at " + entity.world.getWorld().getName() + ":" + entity.posX + "," + entity.posY + "," + entity.posZ;
                    System.err.println(msg);
                    throwable1.printStackTrace();
                    getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(msg, throwable1)));
                    entity.isDead = true;
                    continue;
                    // Paper end
                }
            }

            this.profiler.endSection();
            this.profiler.startSection("remove");
            if (entity.isDead) {
                j = entity.chunkCoordX;
                int l = entity.chunkCoordZ;

                if (entity.addedToChunk && this.isChunkLoaded(j, l, true)) {
                    this.getChunkFromChunkCoords(j, l).removeEntity(entity);
                }

                guardEntityList = false; // Spigot
                this.loadedEntityList.remove(this.tickPosition--); // CraftBukkit - Use field for loop variable
                guardEntityList = true; // Spigot
                this.onEntityRemoved(entity);
            }

            this.profiler.endSection();
        }
        guardEntityList = false; // Spigot

        timings.entityTick.stopTiming(); // Spigot
        this.profiler.endStartSection("blockEntities");
        timings.tileEntityTick.startTiming(); // Spigot
        if (!this.tileEntitiesToBeRemoved.isEmpty()) {
            // Paper start - Use alternate implementation with faster contains
            java.util.Set<TileEntity> toRemove = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
            toRemove.addAll(tileEntitiesToBeRemoved);
            this.tickableTileEntities.removeAll(toRemove);
            // Paper end
            //this.tileEntityList.removeAll(this.tileEntityListUnload); // Paper - remove unused list
            this.tileEntitiesToBeRemoved.clear();
        }

        this.processingLoadedTiles = true;
        // Spigot start
        // Iterator iterator = this.tileEntityListTick.iterator();
        int tilesThisCycle = 0;
        for (tileTickPosition = 0; tileTickPosition < tickableTileEntities.size(); tileTickPosition++) { // Paper - Disable tick limiters
            tileTickPosition = (tileTickPosition < tickableTileEntities.size()) ? tileTickPosition : 0;
            TileEntity tileentity = this.tickableTileEntities.get(tileTickPosition);
            // Spigot start
            if (tileentity == null) {
                getServer().getLogger().severe("Spigot has detected a null entity and has removed it, preventing a crash");
                tilesThisCycle--;
                this.tickableTileEntities.remove(tileTickPosition--);
                continue;
            }
            // Spigot end

            if (!tileentity.isInvalid() && tileentity.hasWorld()) {
                BlockPos blockposition = tileentity.getPos();

                // Paper start - Skip ticking in chunks scheduled for unload
                net.minecraft.world.chunk.Chunk chunk = this.getChunkIfLoaded(blockposition);
                boolean shouldTick = chunk != null;
                if(this.paperConfig.skipEntityTickingInChunksScheduledForUnload)
                    shouldTick = shouldTick && !chunk.isUnloading() && chunk.scheduledForUnload == null;
                if (shouldTick && this.worldBorder.contains(blockposition)) {
                    // Paper end
                    try {
                        this.profiler.func_194340_a(() -> {
                            return String.valueOf(TileEntity.getKey(tileentity.getClass()));
                        });
                        tileentity.tickTimer.startTiming(); // Spigot
                        ((ITickable) tileentity).update();
                        this.profiler.endSection();
                    } catch (Throwable throwable2) {
                        // Paper start - Prevent tile entity and entity crashes
                        String msg = "TileEntity threw exception at " + tileentity.world.getWorld().getName() + ":" + tileentity.pos.getX() + "," + tileentity.pos.getY() + "," + tileentity.pos.getZ();
                        System.err.println(msg);
                        throwable2.printStackTrace();
                        getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(msg, throwable2)));
                        tilesThisCycle--;
                        this.tickableTileEntities.remove(tileTickPosition--);
                        continue;
                        // Paper end
                    }
                    // Spigot start
                    finally {
                        tileentity.tickTimer.stopTiming();
                    }
                    // Spigot end
                }
            }

            if (tileentity.isInvalid()) {
                tilesThisCycle--;
                this.tickableTileEntities.remove(tileTickPosition--);
                //this.tileEntityList.remove(tileentity); // Paper - remove unused list
                if (this.isBlockLoaded(tileentity.getPos())) {
                    this.getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
                }
            }
        }

        timings.tileEntityTick.stopTiming(); // Spigot
        timings.tileEntityPending.startTiming(); // Spigot
        this.processingLoadedTiles = false;
        this.profiler.endStartSection("pendingBlockEntities");
        if (!this.addedTileEntityList.isEmpty()) {
            for (int i1 = 0; i1 < this.addedTileEntityList.size(); ++i1) {
                TileEntity tileentity1 = this.addedTileEntityList.get(i1);

                if (!tileentity1.isInvalid()) {
                    /* CraftBukkit start - Order matters, moved down
                    if (!this.tileEntityList.contains(tileentity1)) {
                        this.a(tileentity1);
                    }
                    // CraftBukkit end */

                    if (this.isBlockLoaded(tileentity1.getPos())) {
                        Chunk chunk = this.getChunkFromBlockCoords(tileentity1.getPos());
                        IBlockState iblockdata = chunk.getBlockState(tileentity1.getPos());

                        chunk.addTileEntity(tileentity1.getPos(), tileentity1);
                        this.notifyBlockUpdate(tileentity1.getPos(), iblockdata, iblockdata, 3);
                        // CraftBukkit start
                        // From above, don't screw this up - SPIGOT-1746
                        if (true) { // Paper - remove unused list
                            this.addTileEntity(tileentity1);
                        }
                        // CraftBukkit end
                    }
                }
            }

            this.addedTileEntityList.clear();
        }

        timings.tileEntityPending.stopTiming(); // Spigot
        co.aikar.timings.TimingHistory.tileEntityTicks += this.tickableTileEntities.size(); // Paper
        this.profiler.endSection();
        this.profiler.endSection();
    }

    protected void tickPlayers() {}

    public boolean addTileEntity(TileEntity tileentity) {
        boolean flag = true; // Paper - remove unused list

        if (flag && tileentity instanceof ITickable && !this.tickableTileEntities.contains(tileentity)) { // Paper
            this.tickableTileEntities.add(tileentity);
        }

        if (this.isRemote) {
            BlockPos blockposition = tileentity.getPos();
            IBlockState iblockdata = this.getBlockState(blockposition);

            this.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 2);
        }

        return flag;
    }

    public void addTileEntities(Collection<TileEntity> collection) {
        if (this.processingLoadedTiles) {
            this.addedTileEntityList.addAll(collection);
        } else {
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                TileEntity tileentity = (TileEntity) iterator.next();

                this.addTileEntity(tileentity);
            }
        }

    }

    public void updateEntity(Entity entity) {
        this.updateEntityWithOptionalForce(entity, true);
    }

    public void updateEntityWithOptionalForce(Entity entity, boolean flag) {
        int i;
        int j;

        // CraftBukkit start - check if chunks are loaded as done in previous versions
        // TODO: Go back to Vanilla behaviour when comfortable
        // Spigot start
        // Chunk startingChunk = this.getChunkIfLoaded(MathHelper.floor(entity.locX) >> 4, MathHelper.floor(entity.locZ) >> 4);
        if (flag && !org.spigotmc.ActivationRange.checkIfActive(entity)) {
            entity.ticksExisted++;
            entity.inactiveTick();
            // Spigot end
            return;
        }
        // CraftBukkit end

        entity.lastTickPosX = entity.posX;
        entity.lastTickPosY = entity.posY;
        entity.lastTickPosZ = entity.posZ;
        entity.prevRotationYaw = entity.rotationYaw;
        entity.prevRotationPitch = entity.rotationPitch;
        if (flag && entity.addedToChunk) {
            ++entity.ticksExisted;
            ++co.aikar.timings.TimingHistory.activatedEntityTicks; // Paper
            if (entity.isRiding()) {
                entity.updateRidden();
            } else {
                entity.onUpdate();
                entity.postTick(); // CraftBukkit
            }
        }

        this.profiler.startSection("chunkCheck");
        if (Double.isNaN(entity.posX) || Double.isInfinite(entity.posX)) {
            entity.posX = entity.lastTickPosX;
        }

        if (Double.isNaN(entity.posY) || Double.isInfinite(entity.posY)) {
            entity.posY = entity.lastTickPosY;
        }

        if (Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ)) {
            entity.posZ = entity.lastTickPosZ;
        }

        if (Double.isNaN(entity.rotationPitch) || Double.isInfinite(entity.rotationPitch)) {
            entity.rotationPitch = entity.prevRotationPitch;
        }

        if (Double.isNaN(entity.rotationYaw) || Double.isInfinite(entity.rotationYaw)) {
            entity.rotationYaw = entity.prevRotationYaw;
        }

        i = MathHelper.floor(entity.posX / 16.0D);
        j = Math.min(15, Math.max(0, MathHelper.floor(entity.posY / 16.0D))); // Paper - stay consistent with chunk add/remove behavior
        int k = MathHelper.floor(entity.posZ / 16.0D);

        if (!entity.addedToChunk || entity.chunkCoordX != i || entity.chunkCoordY != j || entity.chunkCoordZ != k) {
            if (entity.addedToChunk && this.isChunkLoaded(entity.chunkCoordX, entity.chunkCoordZ, true)) {
                this.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ).removeEntityAtIndex(entity, entity.chunkCoordY);
            }

            if (!entity.setPositionNonDirty() && !this.isChunkLoaded(i, k, true)) {
                entity.addedToChunk = false;
            } else {
                this.getChunkFromChunkCoords(i, k).addEntity(entity);
            }
        }

        this.profiler.endSection();
        if (flag && entity.addedToChunk) {
            Iterator iterator = entity.getPassengers().iterator();

            while (iterator.hasNext()) {
                Entity entity1 = (Entity) iterator.next();

                if (!entity1.isDead && entity1.getRidingEntity() == entity) {
                    this.updateEntity(entity1);
                } else {
                    entity1.dismountRidingEntity();
                }
            }
        }
    }

    public boolean checkNoEntityCollision(AxisAlignedBB axisalignedbb) {
        return this.checkNoEntityCollision(axisalignedbb, (Entity) null);
    }

    // Paper start - Based on method below
    /**
     * @param axisalignedbb area to search within
     * @param entity causing the action ex. block placer
     * @return if there are no visible players colliding
     */
    public boolean checkNoVisiblePlayerCollisions(AxisAlignedBB axisalignedbb, @Nullable Entity entity) {
        List list = this.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity instanceof EntityPlayerMP && entity1 instanceof EntityPlayerMP) {
                if (!((EntityPlayerMP) entity).getBukkitEntity().canSee(((EntityPlayerMP) entity1).getBukkitEntity())) {
                    continue;
                }
            }

            if (!entity1.isDead && entity1.blocksEntitySpawning()) {
                return false;
            }
        }

        return true;
    }
    // Paper end

    public boolean checkNoEntityCollision(AxisAlignedBB axisalignedbb, @Nullable Entity entity) {
        List list = this.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (!entity1.isDead && entity1.preventEntitySpawning && entity1 != entity && (entity == null || entity1.isRidingSameEntity(entity))) {
                return false;
            }
        }

        return true;
    }

    public boolean checkBlockCollision(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    IBlockState iblockdata = this.getBlockState(blockposition_pooledblockposition.setPos(k1, l1, i2));

                    if (iblockdata.getMaterial() != Material.AIR) {
                        blockposition_pooledblockposition.release();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.release();
        return false;
    }

    public boolean containsAnyLiquid(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    IBlockState iblockdata = this.getBlockState(blockposition_pooledblockposition.setPos(k1, l1, i2));

                    if (iblockdata.getMaterial().isLiquid()) {
                        blockposition_pooledblockposition.release();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.release();
        return false;
    }

    public boolean isFlammableWithin(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);

        if (this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();
            int k1 = i;

            while (true) {
                if (k1 >= j) {
                    blockposition_pooledblockposition.release();
                    break;
                }

                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = this.getBlockState(blockposition_pooledblockposition.setPos(k1, l1, i2)).getBlock();

                        if (block == Blocks.FIRE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA) {
                            blockposition_pooledblockposition.release();
                            return true;
                        }
                    }
                }

                ++k1;
            }
        }

        return false;
    }

    public boolean handleMaterialAcceleration(AxisAlignedBB axisalignedbb, Material material, Entity entity) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);

        if (!this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
            return false;
        } else {
            boolean flag = false;
            Vec3d vec3d = Vec3d.ZERO;
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.setPos(k1, l1, i2);
                        IBlockState iblockdata = this.getBlockState(blockposition_pooledblockposition);
                        Block block = iblockdata.getBlock();

                        if (iblockdata.getMaterial() == material) {
                            double d0 = l1 + 1 - BlockLiquid.getLiquidHeightPercent(iblockdata.getValue(BlockLiquid.LEVEL).intValue());

                            if (l >= d0) {
                                flag = true;
                                vec3d = block.modifyAcceleration(this, blockposition_pooledblockposition, entity, vec3d);
                            }
                        }
                    }
                }
            }

            blockposition_pooledblockposition.release();
            if (vec3d.lengthVector() > 0.0D && entity.isPushedByWater()) {
                vec3d = vec3d.normalize();
                double d1 = 0.014D;

                entity.motionX += vec3d.x * 0.014D;
                entity.motionY += vec3d.y * 0.014D;
                entity.motionZ += vec3d.z * 0.014D;
            }

            return flag;
        }
    }

    public boolean isMaterialInBB(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    if (this.getBlockState(blockposition_pooledblockposition.setPos(k1, l1, i2)).getMaterial() == material) {
                        blockposition_pooledblockposition.release();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.release();
        return false;
    }

    public Explosion createExplosion(@Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        return this.newExplosion(entity, d0, d1, d2, f, false, flag);
    }

    public Explosion newExplosion(@Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f, flag, flag1);

        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    public float getBlockDensity(Vec3d vec3d, AxisAlignedBB axisalignedbb) {
        double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
            int i = 0;
            int j = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) (f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) (f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) (f2 + d2)) {
                        double d5 = axisalignedbb.minX + (axisalignedbb.maxX - axisalignedbb.minX) * f;
                        double d6 = axisalignedbb.minY + (axisalignedbb.maxY - axisalignedbb.minY) * f1;
                        double d7 = axisalignedbb.minZ + (axisalignedbb.maxZ - axisalignedbb.minZ) * f2;

                        if (this.rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec3d) == null) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    public boolean extinguishFire(@Nullable EntityPlayer entityhuman, BlockPos blockposition, EnumFacing enumdirection) {
        blockposition = blockposition.offset(enumdirection);
        if (this.getBlockState(blockposition).getBlock() == Blocks.FIRE) {
            this.playEvent(entityhuman, 1009, blockposition, 0);
            this.setBlockToAir(blockposition);
            return true;
        } else {
            return false;
        }
    }

    public Map<BlockPos, TileEntity> capturedTileEntities = Maps.newHashMap();
    @Override
    @Nullable
    public TileEntity getTileEntity(BlockPos blockposition) {
        if (blockposition.isInvalidYLocation()) { // Paper
            return null;
        } else {
            // CraftBukkit start
            if (capturedTileEntities.containsKey(blockposition)) {
                return capturedTileEntities.get(blockposition);
            }
            // CraftBukkit end

            TileEntity tileentity = null;

            if (this.processingLoadedTiles) {
                tileentity = this.getPendingTileEntityAt(blockposition);
            }

            if (tileentity == null) {
                tileentity = this.getChunkFromBlockCoords(blockposition).getTileEntity(blockposition, Chunk.EnumCreateEntityType.IMMEDIATE);
            }

            if (tileentity == null) {
                tileentity = this.getPendingTileEntityAt(blockposition);
            }

            return tileentity;
        }
    }

    @Nullable
    private TileEntity getPendingTileEntityAt(BlockPos blockposition) {
        for (int i = 0; i < this.addedTileEntityList.size(); ++i) {
            TileEntity tileentity = this.addedTileEntityList.get(i);

            if (!tileentity.isInvalid() && tileentity.getPos().equals(blockposition)) {
                return tileentity;
            }
        }

        return null;
    }

    public void setTileEntity(BlockPos blockposition, @Nullable TileEntity tileentity) {
        if (!blockposition.isInvalidYLocation()) {
            if (tileentity != null && !tileentity.isInvalid()) {
            // CraftBukkit start
            if (captureBlockStates) {
                tileentity.setWorld(this);
                tileentity.setPos(blockposition);
                capturedTileEntities.put(blockposition, tileentity);
                return;
            }
            // CraftBukkit end
                if (this.processingLoadedTiles) {
                    tileentity.setPos(blockposition);
                    Iterator iterator = this.addedTileEntityList.iterator();

                    while (iterator.hasNext()) {
                        TileEntity tileentity1 = (TileEntity) iterator.next();

                        if (tileentity1.getPos().equals(blockposition)) {
                            tileentity1.invalidate();
                            iterator.remove();
                        }
                    }

                    tileentity.setWorld(this); // Spigot - No null worlds
                    this.addedTileEntityList.add(tileentity);
                } else {
                    this.getChunkFromBlockCoords(blockposition).addTileEntity(blockposition, tileentity);
                    this.addTileEntity(tileentity);
                }
            }

        }
    }

    public void removeTileEntity(BlockPos blockposition) {
        TileEntity tileentity = this.getTileEntity(blockposition);

        if (tileentity != null && this.processingLoadedTiles) {
            tileentity.invalidate();
            this.addedTileEntityList.remove(tileentity);
        } else {
            if (tileentity != null) {
                this.addedTileEntityList.remove(tileentity);
                //this.tileEntityList.remove(tileentity); // Paper - remove unused list
                this.tickableTileEntities.remove(tileentity);
            }

            this.getChunkFromBlockCoords(blockposition).removeTileEntity(blockposition);
        }

    }

    public void markTileEntityForRemoval(TileEntity tileentity) {
        this.tileEntitiesToBeRemoved.add(tileentity);
    }

    public boolean isBlockFullCube(BlockPos blockposition) {
        AxisAlignedBB axisalignedbb = this.getBlockState(blockposition).getCollisionBoundingBox(this, blockposition);

        return axisalignedbb != Block.NULL_AABB && axisalignedbb.getAverageEdgeLength() >= 1.0D;
    }

    public boolean isBlockNormalCube(BlockPos blockposition, boolean flag) {
        if (blockposition.isInvalidYLocation()) { // Paper
            return false;
        } else {
            Chunk chunk = this.chunkProvider.getLoadedChunk(blockposition.getX() >> 4, blockposition.getZ() >> 4);

            if (chunk != null && !chunk.isEmpty()) {
                IBlockState iblockdata = this.getBlockState(blockposition);

                return iblockdata.getMaterial().isOpaque() && iblockdata.isFullCube();
            } else {
                return flag;
            }
        }
    }

    public void calculateInitialSkylight() {
        int i = this.calculateSkylightSubtracted(1.0F);

        if (i != this.skylightSubtracted) {
            this.skylightSubtracted = i;
        }

    }

    public void setAllowedSpawnTypes(boolean flag, boolean flag1) {
        this.spawnHostileMobs = flag;
        this.spawnPeacefulMobs = flag1;
    }

    public void tick() {
        this.updateWeather();
    }

    protected void calculateInitialWeather() {
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = 1.0F;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = 1.0F;
            }
        }

    }

    protected void updateWeather() {
        if (this.provider.hasSkyLight()) {
            if (!this.isRemote) {
                boolean flag = this.getGameRules().getBoolean("doWeatherCycle");

                if (flag) {
                    int i = this.worldInfo.getCleanWeatherTime();

                    if (i > 0) {
                        --i;
                        this.worldInfo.setCleanWeatherTime(i);
                        this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                        this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
                    }

                    int j = this.worldInfo.getThunderTime();

                    if (j <= 0) {
                        if (this.worldInfo.isThundering()) {
                            this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                        } else {
                            this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                        }
                    } else {
                        --j;
                        this.worldInfo.setThunderTime(j);
                        if (j <= 0) {
                            this.worldInfo.setThundering(!this.worldInfo.isThundering());
                        }
                    }

                    int k = this.worldInfo.getRainTime();

                    if (k <= 0) {
                        if (this.worldInfo.isRaining()) {
                            this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                        } else {
                            this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                        }
                    } else {
                        --k;
                        this.worldInfo.setRainTime(k);
                        if (k <= 0) {
                            this.worldInfo.setRaining(!this.worldInfo.isRaining());
                        }
                    }
                }

                this.prevThunderingStrength = this.thunderingStrength;
                if (this.worldInfo.isThundering()) {
                    this.thunderingStrength = (float) (this.thunderingStrength + 0.01D);
                } else {
                    this.thunderingStrength = (float) (this.thunderingStrength - 0.01D);
                }

                this.thunderingStrength = MathHelper.clamp(this.thunderingStrength, 0.0F, 1.0F);
                this.prevRainingStrength = this.rainingStrength;
                if (this.worldInfo.isRaining()) {
                    this.rainingStrength = (float) (this.rainingStrength + 0.01D);
                } else {
                    this.rainingStrength = (float) (this.rainingStrength - 0.01D);
                }

                this.rainingStrength = MathHelper.clamp(this.rainingStrength, 0.0F, 1.0F);

                // CraftBukkit start
                for (int idx = 0; idx < this.playerEntities.size(); ++idx) {
                    if (((EntityPlayerMP) this.playerEntities.get(idx)).world == this) {
                        ((EntityPlayerMP) this.playerEntities.get(idx)).tickWeather();
                    }
                }
                // CraftBukkit end
            }
        }
    }

    protected void updateBlocks() {}

    public void immediateBlockTick(BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.scheduledUpdatesAreImmediate = true;
        iblockdata.getBlock().updateTick(this, blockposition, iblockdata, random);
        this.scheduledUpdatesAreImmediate = false;
    }

    public boolean canBlockFreezeWater(BlockPos blockposition) {
        return this.canBlockFreeze(blockposition, false);
    }

    public boolean canBlockFreezeNoWater(BlockPos blockposition) {
        return this.canBlockFreeze(blockposition, true);
    }

    public boolean canBlockFreeze(BlockPos blockposition, boolean flag) {
        Biome biomebase = this.getBiome(blockposition);
        float f = biomebase.getTemperature(blockposition);

        if (f >= 0.15F) {
            return false;
        } else {
            if (blockposition.getY() >= 0 && blockposition.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockState iblockdata = this.getBlockState(blockposition);
                Block block = iblockdata.getBlock();

                if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && iblockdata.getValue(BlockLiquid.LEVEL).intValue() == 0) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = this.isWater(blockposition.west()) && this.isWater(blockposition.east()) && this.isWater(blockposition.north()) && this.isWater(blockposition.south());

                    if (!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private boolean isWater(BlockPos blockposition) {
        return this.getBlockState(blockposition).getMaterial() == Material.WATER;
    }

    public boolean canSnowAt(BlockPos blockposition, boolean flag) {
        Biome biomebase = this.getBiome(blockposition);
        float f = biomebase.getTemperature(blockposition);

        if (f >= 0.15F) {
            return false;
        } else if (!flag) {
            return true;
        } else {
            if (blockposition.getY() >= 0 && blockposition.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockState iblockdata = this.getBlockState(blockposition);

                if (iblockdata.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(this, blockposition)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean checkLight(BlockPos blockposition) {
        boolean flag = false;

        if (this.provider.hasSkyLight()) {
            flag |= this.checkLightFor(EnumSkyBlock.SKY, blockposition);
        }

        flag |= this.checkLightFor(EnumSkyBlock.BLOCK, blockposition);
        return flag;
    }

    private int getRawLight(BlockPos blockposition, EnumSkyBlock enumskyblock) {
        if (enumskyblock == EnumSkyBlock.SKY && this.canSeeSky(blockposition)) {
            return 15;
        } else {
            IBlockState iblockdata = this.getBlockState(blockposition);
            int i = enumskyblock == EnumSkyBlock.SKY ? 0 : iblockdata.getLightValue();
            int j = iblockdata.getLightOpacity();

            if (j >= 15 && iblockdata.getLightValue() > 0) {
                j = 1;
            }

            if (j < 1) {
                j = 1;
            }

            if (j >= 15) {
                return 0;
            } else if (i >= 14) {
                return i;
            } else {
                BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

                try {
                    EnumFacing[] aenumdirection = EnumFacing.values();
                    int k = aenumdirection.length;

                    for (int l = 0; l < k; ++l) {
                        EnumFacing enumdirection = aenumdirection[l];

                        blockposition_pooledblockposition.setPos(blockposition).move(enumdirection);
                        int i1 = this.getLightFor(enumskyblock, blockposition_pooledblockposition) - j;

                        if (i1 > i) {
                            i = i1;
                        }

                        if (i >= 14) {
                            int j1 = i;

                            return j1;
                        }
                    }

                    return i;
                } finally {
                    blockposition_pooledblockposition.release();
                }
            }
        }
    }

    public boolean checkLightFor(EnumSkyBlock enumskyblock, BlockPos blockposition) {
        // CraftBukkit start - Use neighbor cache instead of looking up
        Chunk chunk = this.getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
        if (chunk == null || !chunk.areNeighborsLoaded(1) /*!this.areChunksLoaded(blockposition, 17, false)*/) {
            // CraftBukkit end
            return false;
        } else {
            int i = 0;
            int j = 0;

            this.profiler.startSection("getBrightness");
            int k = this.getLightFor(enumskyblock, blockposition);
            int l = this.getRawLight(blockposition, enumskyblock);
            int i1 = blockposition.getX();
            int j1 = blockposition.getY();
            int k1 = blockposition.getZ();
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;

            if (l > k) {
                this.lightUpdateBlockList[j++] = 133152;
            } else if (l < k) {
                this.lightUpdateBlockList[j++] = 133152 | k << 18;

                while (i < j) {
                    l1 = this.lightUpdateBlockList[i++];
                    i2 = (l1 & 63) - 32 + i1;
                    j2 = (l1 >> 6 & 63) - 32 + j1;
                    k2 = (l1 >> 12 & 63) - 32 + k1;
                    int l3 = l1 >> 18 & 15;
                    BlockPos blockposition1 = new BlockPos(i2, j2, k2);

                    l2 = this.getLightFor(enumskyblock, blockposition1);
                    if (l2 == l3) {
                        this.setLightFor(enumskyblock, blockposition1, 0);
                        if (l3 > 0) {
                            i3 = MathHelper.abs(i2 - i1);
                            j3 = MathHelper.abs(j2 - j1);
                            k3 = MathHelper.abs(k2 - k1);
                            if (i3 + j3 + k3 < 17) {
                                BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();
                                EnumFacing[] aenumdirection = EnumFacing.values();
                                int i4 = aenumdirection.length;

                                for (int j4 = 0; j4 < i4; ++j4) {
                                    EnumFacing enumdirection = aenumdirection[j4];
                                    int k4 = i2 + enumdirection.getFrontOffsetX();
                                    int l4 = j2 + enumdirection.getFrontOffsetY();
                                    int i5 = k2 + enumdirection.getFrontOffsetZ();

                                    blockposition_pooledblockposition.setPos(k4, l4, i5);
                                    int j5 = Math.max(1, this.getBlockState(blockposition_pooledblockposition).getLightOpacity());

                                    l2 = this.getLightFor(enumskyblock, blockposition_pooledblockposition);
                                    if (l2 == l3 - j5 && j < this.lightUpdateBlockList.length) {
                                        this.lightUpdateBlockList[j++] = k4 - i1 + 32 | l4 - j1 + 32 << 6 | i5 - k1 + 32 << 12 | l3 - j5 << 18;
                                    }
                                }

                                blockposition_pooledblockposition.release();
                            }
                        }
                    }
                }

                i = 0;
            }

            this.profiler.endSection();
            this.profiler.startSection("checkedPosition < toCheckCount");

            while (i < j) {
                l1 = this.lightUpdateBlockList[i++];
                i2 = (l1 & 63) - 32 + i1;
                j2 = (l1 >> 6 & 63) - 32 + j1;
                k2 = (l1 >> 12 & 63) - 32 + k1;
                BlockPos blockposition2 = new BlockPos(i2, j2, k2);
                int k5 = this.getLightFor(enumskyblock, blockposition2);

                l2 = this.getRawLight(blockposition2, enumskyblock);
                if (l2 != k5) {
                    this.setLightFor(enumskyblock, blockposition2, l2);
                    if (l2 > k5) {
                        i3 = Math.abs(i2 - i1);
                        j3 = Math.abs(j2 - j1);
                        k3 = Math.abs(k2 - k1);
                        boolean flag = j < this.lightUpdateBlockList.length - 6;

                        if (i3 + j3 + k3 < 17 && flag) {
                            if (this.getLightFor(enumskyblock, blockposition2.west()) < l2) {
                                this.lightUpdateBlockList[j++] = i2 - 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.getLightFor(enumskyblock, blockposition2.east()) < l2) {
                                this.lightUpdateBlockList[j++] = i2 + 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.getLightFor(enumskyblock, blockposition2.down()) < l2) {
                                this.lightUpdateBlockList[j++] = i2 - i1 + 32 + (j2 - 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.getLightFor(enumskyblock, blockposition2.up()) < l2) {
                                this.lightUpdateBlockList[j++] = i2 - i1 + 32 + (j2 + 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.getLightFor(enumskyblock, blockposition2.north()) < l2) {
                                this.lightUpdateBlockList[j++] = i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - 1 - k1 + 32 << 12);
                            }

                            if (this.getLightFor(enumskyblock, blockposition2.south()) < l2) {
                                this.lightUpdateBlockList[j++] = i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 + 1 - k1 + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.profiler.endSection();
            return true;
        }
    }

    public boolean tickUpdates(boolean flag) {
        return false;
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunk, boolean flag) {
        return null;
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureboundingbox, boolean flag) {
        return null;
    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
        return this.getEntitiesInAABBexcluding(entity, axisalignedbb, EntitySelectors.NOT_SPECTATING);
    }

    public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super Entity> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        int i = MathHelper.floor((axisalignedbb.minX - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxX + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.minZ - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.maxZ + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.isChunkLoaded(i1, j1, true)) {
                    this.getChunkFromChunkCoords(i1, j1).getEntitiesWithinAABBForEntity(entity, axisalignedbb, arraylist, predicate);
                }
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> getEntities(Class<? extends T> oclass, Predicate<? super T> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.loadedEntityList.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (oclass.isAssignableFrom(entity.getClass()) && predicate.apply((T) entity)) {
                arraylist.add(entity);
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> getPlayers(Class<? extends T> oclass, Predicate<? super T> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.playerEntities.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (oclass.isAssignableFrom(entity.getClass()) && predicate.apply((T) entity)) { // CraftBukkit - fix decompile error
                arraylist.add(entity);
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> oclass, AxisAlignedBB axisalignedbb) {
        return this.getEntitiesWithinAABB(oclass, axisalignedbb, EntitySelectors.NOT_SPECTATING);
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate) {
        int i = MathHelper.floor((axisalignedbb.minX - 2.0D) / 16.0D);
        int j = MathHelper.ceil((axisalignedbb.maxX + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.minZ - 2.0D) / 16.0D);
        int l = MathHelper.ceil((axisalignedbb.maxZ + 2.0D) / 16.0D);
        ArrayList arraylist = Lists.newArrayList();

        for (int i1 = i; i1 < j; ++i1) {
            for (int j1 = k; j1 < l; ++j1) {
                if (this.isChunkLoaded(i1, j1, true)) {
                    this.getChunkFromChunkCoords(i1, j1).getEntitiesOfTypeWithinAABB(oclass, axisalignedbb, arraylist, predicate);
                }
            }
        }

        return arraylist;
    }

    @Nullable
    public <T extends Entity> T findNearestEntityWithinAABB(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, T t0) {
        List list = this.getEntitiesWithinAABB(oclass, axisalignedbb);
        Entity entity = null;
        double d0 = Double.MAX_VALUE;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1 != t0 && EntitySelectors.NOT_SPECTATING.apply(entity1)) {
                double d1 = t0.getDistanceSq(entity1);

                if (d1 <= d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        return (T) entity; // CraftBukkit fix decompile error
    }

    @Nullable
    public Entity getEntityByID(int i) {
        return this.entitiesById.lookup(i);
    }

    public void markChunkDirty(BlockPos blockposition, TileEntity tileentity) {
        if (this.isBlockLoaded(blockposition)) {
            this.getChunkFromBlockCoords(blockposition).markDirty();
        }

    }

    public int countEntities(Class<?> oclass) {
        int i = 0;
        Iterator iterator = this.loadedEntityList.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            // CraftBukkit start - Split out persistent check, don't apply it to special persistent mobs
            if (entity instanceof EntityLiving) {
                EntityLiving entityinsentient = (EntityLiving) entity;
                if (entityinsentient.canDespawn() && entityinsentient.isNoDespawnRequired()) {
                    continue;
                }
            }

            if (oclass.isAssignableFrom(entity.getClass())) {
            // if ((!(entity instanceof EntityInsentient) || !((EntityInsentient) entity).isPersistent()) && oclass.isAssignableFrom(entity.getClass())) {
                // CraftBukkit end
                ++i;
            }
        }

        return i;
    }

    public void loadEntities(Collection<Entity> collection) {
        org.spigotmc.AsyncCatcher.catchOp( "entity world add"); // Spigot
        // CraftBukkit start
        // this.entityList.addAll(collection);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity == null) {
                continue;
            }
            this.loadedEntityList.add(entity);
            // CraftBukkit end
            this.onEntityAdded(entity);
        }

    }

    public void unloadEntities(Collection<Entity> collection) {
        this.unloadedEntityList.addAll(collection);
    }

    public boolean mayPlace(Block block, BlockPos blockposition, boolean flag, EnumFacing enumdirection, @Nullable Entity entity) {
        IBlockState iblockdata = this.getBlockState(blockposition);
        AxisAlignedBB axisalignedbb = flag ? null : block.getDefaultState().getCollisionBoundingBox(this, blockposition);

        // CraftBukkit start - store default return
        boolean defaultReturn = axisalignedbb != Block.NULL_AABB && !this.checkNoVisiblePlayerCollisions(axisalignedbb.offset(blockposition), entity) ? false : (iblockdata.getMaterial() == Material.CIRCUITS && block == Blocks.ANVIL ? true : iblockdata.getMaterial().isReplaceable() && block.canPlaceBlockOnSide(this, blockposition, enumdirection)); // Paper - Use our entity search
        BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block), defaultReturn);
        this.getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }

    public int getSeaLevel() {
        return this.seaLevel;
    }

    public void setSeaLevel(int i) {
        this.seaLevel = i;
    }

    @Override
    public int getStrongPower(BlockPos blockposition, EnumFacing enumdirection) {
        return this.getBlockState(blockposition).getStrongPower(this, blockposition, enumdirection);
    }

    public WorldType getWorldType() {
        return this.worldInfo.getTerrainType();
    }

    public int getStrongPower(BlockPos blockposition) {
        byte b0 = 0;
        int i = Math.max(b0, this.getStrongPower(blockposition.down(), EnumFacing.DOWN));

        if (i >= 15) {
            return i;
        } else {
            i = Math.max(i, this.getStrongPower(blockposition.up(), EnumFacing.UP));
            if (i >= 15) {
                return i;
            } else {
                i = Math.max(i, this.getStrongPower(blockposition.north(), EnumFacing.NORTH));
                if (i >= 15) {
                    return i;
                } else {
                    i = Math.max(i, this.getStrongPower(blockposition.south(), EnumFacing.SOUTH));
                    if (i >= 15) {
                        return i;
                    } else {
                        i = Math.max(i, this.getStrongPower(blockposition.west(), EnumFacing.WEST));
                        if (i >= 15) {
                            return i;
                        } else {
                            i = Math.max(i, this.getStrongPower(blockposition.east(), EnumFacing.EAST));
                            return i >= 15 ? i : i;
                        }
                    }
                }
            }
        }
    }

    public boolean isSidePowered(BlockPos blockposition, EnumFacing enumdirection) {
        return this.getRedstonePower(blockposition, enumdirection) > 0;
    }

    public int getRedstonePower(BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = this.getBlockState(blockposition);

        return iblockdata.isNormalCube() ? this.getStrongPower(blockposition) : iblockdata.getWeakPower(this, blockposition, enumdirection);
    }

    public boolean isBlockPowered(BlockPos blockposition) {
        return this.getRedstonePower(blockposition.down(), EnumFacing.DOWN) > 0 ? true : (this.getRedstonePower(blockposition.up(), EnumFacing.UP) > 0 ? true : (this.getRedstonePower(blockposition.north(), EnumFacing.NORTH) > 0 ? true : (this.getRedstonePower(blockposition.south(), EnumFacing.SOUTH) > 0 ? true : (this.getRedstonePower(blockposition.west(), EnumFacing.WEST) > 0 ? true : this.getRedstonePower(blockposition.east(), EnumFacing.EAST) > 0))));
    }

    public int isBlockIndirectlyGettingPowered(BlockPos blockposition) {
        int i = 0;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumFacing enumdirection = aenumdirection[k];
            int l = this.getRedstonePower(blockposition.offset(enumdirection), enumdirection);

            if (l >= 15) {
                return 15;
            }

            if (l > i) {
                i = l;
            }
        }

        return i;
    }

    @Nullable
    public EntityPlayer getClosestPlayerToEntity(Entity entity, double d0) {
        return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, d0, false);
    }

    @Nullable
    public EntityPlayer getNearestPlayerNotCreative(Entity entity, double d0) {
        return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, d0, true);
    }

    @Nullable
    public EntityPlayer getClosestPlayer(double d0, double d1, double d2, double d3, boolean flag) {
        Predicate predicate = flag ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;

        return this.getClosestPlayer(d0, d1, d2, d3, predicate);
    }

    @Nullable
    public EntityPlayer getClosestPlayer(double d0, double d1, double d2, double d3, Predicate<Entity> predicate) {
        double d4 = -1.0D;
        EntityPlayer entityhuman = null;

        for (int i = 0; i < this.playerEntities.size(); ++i) {
            EntityPlayer entityhuman1 = this.playerEntities.get(i);
            // CraftBukkit start - Fixed an NPE
            if (entityhuman1 == null || entityhuman1.isDead) {
                continue;
            }
            // CraftBukkit end

            if (predicate.apply(entityhuman1)) {
                double d5 = entityhuman1.getDistanceSq(d0, d1, d2);

                if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    public boolean isAnyPlayerWithinRangeAt(double d0, double d1, double d2, double d3) {
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            EntityPlayer entityhuman = this.playerEntities.get(i);

            if (EntitySelectors.NOT_SPECTATING.apply(entityhuman) && entityhuman.affectsSpawning) { // Paper - Affects Spawning API
                double d4 = entityhuman.getDistanceSq(d0, d1, d2);

                if (d3 < 0.0D || d4 < d3 * d3) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(Entity entity, double d0, double d1) {
        return this.getNearestAttackablePlayer(entity.posX, entity.posY, entity.posZ, d0, d1, (Function) null, (Predicate) null);
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(BlockPos blockposition, double d0, double d1) {
        return this.getNearestAttackablePlayer(blockposition.getX() + 0.5F, blockposition.getY() + 0.5F, blockposition.getZ() + 0.5F, d0, d1, (Function) null, (Predicate) null);
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(double d0, double d1, double d2, double d3, double d4, @Nullable Function<EntityPlayer, Double> function, @Nullable Predicate<EntityPlayer> predicate) {
        double d5 = -1.0D;
        EntityPlayer entityhuman = null;

        for (int i = 0; i < this.playerEntities.size(); ++i) {
            EntityPlayer entityhuman1 = this.playerEntities.get(i);

            if (!entityhuman1.capabilities.disableDamage && entityhuman1.isEntityAlive() && !entityhuman1.isSpectator() && (predicate == null || predicate.apply(entityhuman1))) {
                double d6 = entityhuman1.getDistanceSq(d0, entityhuman1.posY, d2);
                double d7 = d3;

                if (entityhuman1.isSneaking()) {
                    d7 = d3 * 0.800000011920929D;
                }

                if (entityhuman1.isInvisible()) {
                    float f = entityhuman1.getArmorVisibility();

                    if (f < 0.1F) {
                        f = 0.1F;
                    }

                    d7 *= 0.7F * f;
                }

                if (function != null) {
                    d7 *= MoreObjects.firstNonNull(function.apply(entityhuman1), Double.valueOf(1.0D)).doubleValue();
                }

                if ((d4 < 0.0D || Math.abs(entityhuman1.posY - d1) < d4 * d4) && (d3 < 0.0D || d6 < d7 * d7) && (d5 == -1.0D || d6 < d5)) {
                    d5 = d6;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    @Nullable
    public EntityPlayer getPlayerEntityByName(String s) {
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            EntityPlayer entityhuman = this.playerEntities.get(i);

            if (s.equals(entityhuman.getName())) {
                return entityhuman;
            }
        }

        return null;
    }

    @Nullable
    public EntityPlayer getPlayerEntityByUUID(UUID uuid) {
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            EntityPlayer entityhuman = this.playerEntities.get(i);

            if (uuid.equals(entityhuman.getUniqueID())) {
                return entityhuman;
            }
        }

        return null;
    }

    public void checkSessionLock() throws MinecraftException {
        this.saveHandler.checkSessionLock();
    }

    public long getSeed() {
        return this.worldInfo.getSeed();
    }

    public long getTotalWorldTime() {
        return this.worldInfo.getWorldTotalTime();
    }

    public long getWorldTime() {
        return this.worldInfo.getWorldTime();
    }

    public void setWorldTime(long i) {
        this.worldInfo.setWorldTime(i);
    }

    public BlockPos getSpawnPoint() {
        BlockPos blockposition = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());

        if (!this.getWorldBorder().contains(blockposition)) {
            blockposition = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
        }

        return blockposition;
    }

    public void setSpawnPoint(BlockPos blockposition) {
        this.worldInfo.setSpawn(blockposition);
    }

    public boolean isBlockModifiable(EntityPlayer entityhuman, BlockPos blockposition) {
        return true;
    }

    public void setEntityState(Entity entity, byte b0) {}

    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }

    public void addBlockEvent(BlockPos blockposition, Block block, int i, int j) {
        this.getBlockState(blockposition).onBlockEventReceived(this, blockposition, i, j);
    }

    public ISaveHandler getSaveHandler() {
        return this.saveHandler;
    }

    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }

    public GameRules getGameRules() {
        return this.worldInfo.getGameRulesInstance();
    }

    public void updateAllPlayersSleepingFlag() {}

    // CraftBukkit start
    // Calls the method that checks to see if players are sleeping
    // Called by CraftPlayer.setPermanentSleeping()
    public void checkSleepStatus() {
        if (!this.isRemote) {
            this.updateAllPlayersSleepingFlag();
        }
    }
    // CraftBukkit end

    public float getThunderStrength(float f) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * f) * this.getRainStrength(f);
    }

    public float getRainStrength(float f) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * f;
    }

    public boolean isThundering() {
        return this.getThunderStrength(1.0F) > 0.9D;
    }

    public boolean isRaining() {
        return this.getRainStrength(1.0F) > 0.2D;
    }

    public boolean isRainingAt(BlockPos blockposition) {
        if (!this.isRaining()) {
            return false;
        } else if (!this.canSeeSky(blockposition)) {
            return false;
        } else if (this.getPrecipitationHeight(blockposition).getY() > blockposition.getY()) {
            return false;
        } else {
            Biome biomebase = this.getBiome(blockposition);

            return biomebase.getEnableSnow() ? false : (this.canSnowAt(blockposition, false) ? false : biomebase.canRain());
        }
    }

    public boolean isBlockinHighHumidity(BlockPos blockposition) {
        Biome biomebase = this.getBiome(blockposition);

        return biomebase.isHighHumidity();
    }

    @Nullable
    public MapStorage getMapStorage() {
        return this.mapStorage;
    }

    public void setData(String s, WorldSavedData persistentbase) {
        this.mapStorage.setData(s, persistentbase);
    }

    @Nullable
    public WorldSavedData loadData(Class<? extends WorldSavedData> oclass, String s) {
        return this.mapStorage.getOrLoadData(oclass, s);
    }

    public int getUniqueDataId(String s) {
        return this.mapStorage.getUniqueDataId(s);
    }

    public void playBroadcastSound(int i, BlockPos blockposition, int j) {
        for (int k = 0; k < this.eventListeners.size(); ++k) {
            this.eventListeners.get(k).broadcastSound(i, blockposition, j);
        }

    }

    public void playEvent(int i, BlockPos blockposition, int j) {
        this.playEvent((EntityPlayer) null, i, blockposition, j);
    }

    public void playEvent(@Nullable EntityPlayer entityhuman, int i, BlockPos blockposition, int j) {
        try {
            for (int k = 0; k < this.eventListeners.size(); ++k) {
                this.eventListeners.get(k).playEvent(entityhuman, i, blockposition, j);
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Playing level event");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Level event being played");

            crashreportsystemdetails.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(blockposition));
            crashreportsystemdetails.addCrashSection("Event source", entityhuman);
            crashreportsystemdetails.addCrashSection("Event type", Integer.valueOf(i));
            crashreportsystemdetails.addCrashSection("Event data", Integer.valueOf(j));
            throw new ReportedException(crashreport);
        }
    }

    public int getHeight() {
        return 256;
    }

    public int getActualHeight() {
        return this.provider.isNether() ? 128 : 256;
    }

    public Random setRandomSeed(int i, int j, int k) {
        long l = i * 341873128712L + j * 132897987541L + this.getWorldInfo().getSeed() + k;

        this.rand.setSeed(l);
        return this.rand;
    }

    public CrashReportCategory addWorldInfoToCrashReport(CrashReport crashreport) {
        CrashReportCategory crashreportsystemdetails = crashreport.makeCategoryDepth("Affected level", 1);

        crashreportsystemdetails.addCrashSection("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
        crashreportsystemdetails.addDetail("All players", new ICrashReportDetail() {
            public String a() {
                return World.this.playerEntities.size() + " total; " + World.this.playerEntities;
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Chunk stats", new ICrashReportDetail() {
            public String a() {
                return World.this.chunkProvider.makeString();
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });

        try {
            this.worldInfo.addToCrashReport(crashreportsystemdetails);
        } catch (Throwable throwable) {
            crashreportsystemdetails.addCrashSectionThrowable("Level Data Unobtainable", throwable);
        }

        return crashreportsystemdetails;
    }

    public void sendBlockBreakProgress(int i, BlockPos blockposition, int j) {
        for (int k = 0; k < this.eventListeners.size(); ++k) {
            IWorldEventListener iworldaccess = this.eventListeners.get(k);

            iworldaccess.sendBlockBreakProgress(i, blockposition, j);
        }

    }

    public Calendar getCurrentDate() {
        if (this.getTotalWorldTime() % 600L == 0L) {
            this.calendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
        }

        return this.calendar;
    }

    public Scoreboard getScoreboard() {
        return this.worldScoreboard;
    }

    public void updateComparatorOutputLevel(BlockPos blockposition, Block block) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            BlockPos blockposition1 = blockposition.offset(enumdirection);

            if (this.isBlockLoaded(blockposition1)) {
                IBlockState iblockdata = this.getBlockState(blockposition1);

                if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockdata)) {
                    iblockdata.neighborChanged(this, blockposition1, block, blockposition);
                } else if (iblockdata.isNormalCube()) {
                    blockposition1 = blockposition1.offset(enumdirection);
                    iblockdata = this.getBlockState(blockposition1);
                    if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockdata)) {
                        iblockdata.neighborChanged(this, blockposition1, block, blockposition);
                    }
                }
            }
        }

    }

    public DifficultyInstance getDifficultyForLocation(BlockPos blockposition) {
        long i = 0L;
        float f = 0.0F;

        if (this.isBlockLoaded(blockposition)) {
            f = this.getCurrentMoonPhaseFactor();
            i = this.getChunkFromBlockCoords(blockposition).getInhabitedTime();
        }

        return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), i, f);
    }

    public EnumDifficulty getDifficulty() {
        return this.getWorldInfo().getDifficulty();
    }

    public int getSkylightSubtracted() {
        return this.skylightSubtracted;
    }

    public void setSkylightSubtracted(int i) {
        this.skylightSubtracted = i;
    }

    public void setLastLightningBolt(int i) {
        this.lastLightningBolt = i;
    }

    public VillageCollection getVillageCollection() {
        return this.villageCollection;
    }

    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }

    public boolean shouldStayLoaded(int i,  int j) { return isSpawnChunk(i, j); } // Paper - OBFHELPER
    public boolean isSpawnChunk(int i, int j) {
        BlockPos blockposition = this.getSpawnPoint();
        int k = i * 16 + 8 - blockposition.getX();
        int l = j * 16 + 8 - blockposition.getZ();
        boolean flag = true;
        short keepLoadedRange = paperConfig.keepLoadedRange; // Paper

        return k >= -keepLoadedRange && k <= keepLoadedRange && l >= -keepLoadedRange && l <= keepLoadedRange && this.keepSpawnInMemory; // CraftBukkit - Added 'this.keepSpawnInMemory' // Paper - Re-add range var
    }

    public void sendPacketToServer(Packet<?> packet) {
        throw new UnsupportedOperationException("Can\'t send packets to server unless you\'re on the client.");
    }

    public LootTableManager getLootTableManager() {
        return this.lootTable;
    }

    @Nullable
    public BlockPos findNearestStructure(String s, BlockPos blockposition, boolean flag) {
        return null;
    }
}

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

    private int field_181546_a = 63;
    protected boolean field_72999_e;
    // Spigot start - guard entity list from removals
    public final List<Entity> field_72996_f = new java.util.ArrayList<Entity>()
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
    protected final Set<Entity> field_72997_g = Sets.newHashSet(); // Paper
    //public final List<TileEntity> tileEntityList = Lists.newArrayList(); // Paper - remove unused list
    public final List<TileEntity> field_175730_i = Lists.newArrayList();
    private final List<TileEntity> field_147484_a = Lists.newArrayList();
    private final Set<TileEntity> field_147483_b = Sets.newHashSet(); // Paper
    public final List<EntityPlayer> field_73010_i = Lists.newArrayList();
    public final List<Entity> field_73007_j = Lists.newArrayList();
    protected final IntHashMap<Entity> field_175729_l = new IntHashMap();
    private final long field_73001_c = 16777215L;
    private int field_73008_k; private int getSkylightSubtracted() { return this.field_73008_k; } // Paper - OBFHELPER
    protected int field_73005_l = (new Random()).nextInt();
    protected final int field_73006_m = 1013904223;
    protected float field_73003_n;
    public float field_73004_o;
    protected float field_73018_p;
    public float field_73017_q;
    private int field_73016_r;
    public final Random field_73012_v = new Random();
    public WorldProvider field_73011_w;
    protected PathWorldListener field_184152_t = new PathWorldListener();
    protected List<IWorldEventListener> field_73021_x;
    public IChunkProvider field_73020_y;
    protected final ISaveHandler field_73019_z;
    public WorldInfo field_72986_A;
    protected boolean field_72987_B;
    public MapStorage field_72988_C;
    protected VillageCollection field_72982_D;
    protected LootTableManager field_184151_B;
    protected AdvancementManager field_191951_C;
    protected FunctionManager field_193036_D;
    public final Profiler field_72984_F;
    private final Calendar field_83016_L;
    public Scoreboard field_96442_D;
    public final boolean field_72995_K;
    public boolean field_72985_G;
    public boolean field_72992_H;
    private boolean field_147481_N;
    private final WorldBorder field_175728_M;
    int[] field_72994_J;

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
        return ((ChunkProviderServer) this.field_73020_y).getChunkIfLoaded(blockposition.func_177958_n() >> 4, blockposition.func_177952_p() >> 4);
    }
    // Paper end

    public Chunk getChunkIfLoaded(int x, int z) {
        return ((ChunkProviderServer) this.field_73020_y).getChunkIfLoaded(x, z);
    }

    protected World(ISaveHandler idatamanager, WorldInfo worlddata, WorldProvider worldprovider, Profiler methodprofiler, boolean flag, ChunkGenerator gen, org.bukkit.World.Environment env) {
        this.spigotConfig = new org.spigotmc.SpigotWorldConfig( worlddata.func_76065_j() ); // Spigot
        this.paperConfig = new com.destroystokyo.paper.PaperWorldConfig(worlddata.func_76065_j(), this.spigotConfig); // Paper
        this.chunkPacketBlockController = this.paperConfig.antiXray ? new ChunkPacketBlockControllerAntiXray(this.paperConfig) : ChunkPacketBlockController.NO_OPERATION_INSTANCE; // Paper - Anti-Xray
        this.generator = gen;
        this.world = new CraftWorld((WorldServer) this, gen, env);
        this.ticksPerAnimalSpawns = this.getServer().getTicksPerAnimalSpawns(); // CraftBukkit
        this.ticksPerMonsterSpawns = this.getServer().getTicksPerMonsterSpawns(); // CraftBukkit
        // CraftBukkit end
        this.field_73021_x = Lists.newArrayList(new IWorldEventListener[] { this.field_184152_t});
        this.field_83016_L = Calendar.getInstance();
        this.field_96442_D = new Scoreboard();
        this.field_72985_G = true;
        this.field_72992_H = true;
        this.field_72994_J = new int['\u8000'];
        this.field_73019_z = idatamanager;
        this.field_72984_F = methodprofiler;
        this.field_72986_A = worlddata;
        this.field_73011_w = worldprovider;
        this.field_72995_K = flag;
        this.field_175728_M = worldprovider.func_177501_r();
        // CraftBukkit start
        func_175723_af().world = (WorldServer) this;
        // From PlayerList.setPlayerFileData
        func_175723_af().func_177737_a(new IBorderListener() {
            @Override
            public void func_177694_a(WorldBorder worldborder, double d0) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_SIZE), worldborder.world);
            }

            @Override
            public void func_177692_a(WorldBorder worldborder, double d0, double d1, long i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.LERP_SIZE), worldborder.world);
            }

            @Override
            public void func_177693_a(WorldBorder worldborder, double d0, double d1) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_CENTER), worldborder.world);
            }

            @Override
            public void func_177691_a(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_TIME), worldborder.world);
            }

            @Override
            public void func_177690_b(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_BLOCKS), worldborder.world);
            }

            @Override
            public void func_177696_b(WorldBorder worldborder, double d0) {}

            @Override
            public void func_177695_c(WorldBorder worldborder, double d0) {}
        });
        this.getServer().addWorld(this.world);
        // CraftBukkit end
        timings = new co.aikar.timings.WorldTimingsHandler(this); // Paper - code below can generate new world and access timings
        this.keepSpawnInMemory = this.paperConfig.keepSpawnInMemory; // Paper
                this.entityLimiter = new org.spigotmc.TickLimiter(spigotConfig.entityMaxTickTime);
        this.tileLimiter = new org.spigotmc.TickLimiter(spigotConfig.tileMaxTickTime);
    }

    public World func_175643_b() {
        return this;
    }

    public Biome func_180494_b(final BlockPos blockposition) {
        if (this.func_175667_e(blockposition)) {
            Chunk chunk = this.func_175726_f(blockposition);

            try {
                return chunk.func_177411_a(blockposition, this.field_73011_w.func_177499_m());
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Getting biome");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Coordinates of biome request");

                crashreportsystemdetails.func_189529_a("Location", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return CrashReportCategory.func_180522_a(blockposition);
                    }

                    @Override
                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        } else {
            return this.field_73011_w.func_177499_m().func_180300_a(blockposition, Biomes.field_76772_c);
        }
    }

    public BiomeProvider func_72959_q() {
        return this.field_73011_w.func_177499_m();
    }

    protected abstract IChunkProvider func_72970_h();

    public void func_72963_a(WorldSettings worldsettings) {
        this.field_72986_A.func_76091_d(true);
    }

    @Nullable
    public MinecraftServer func_73046_m() {
        return null;
    }

    public IBlockState func_184141_c(BlockPos blockposition) {
        BlockPos blockposition1;

        for (blockposition1 = new BlockPos(blockposition.func_177958_n(), this.func_181545_F(), blockposition.func_177952_p()); !this.func_175623_d(blockposition1.func_177984_a()); blockposition1 = blockposition1.func_177984_a()) {
            ;
        }

        return this.func_180495_p(blockposition1);
    }

    private static boolean func_175701_a(BlockPos blockposition) { // Paper - unused but incase reflection / future uses
        return blockposition.isValidLocation(); // Paper
    }

    private static boolean func_189509_E(BlockPos blockposition) { // Paper - unused but incase reflection / future uses
        return blockposition.isInvalidYLocation(); // Paper
    }

    @Override
    public boolean func_175623_d(BlockPos blockposition) {
        return this.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a;
    }

    public boolean func_175667_e(BlockPos blockposition) {
        return getChunkIfLoaded(blockposition.func_177958_n() >> 4, blockposition.func_177952_p() >> 4) != null; // Paper
    }

    public boolean func_175668_a(BlockPos blockposition, boolean flag) {
        return this.func_175680_a(blockposition.func_177958_n() >> 4, blockposition.func_177952_p() >> 4, flag);
    }

    public boolean func_175697_a(BlockPos blockposition, int i) {
        return this.func_175648_a(blockposition, i, true);
    }

    public boolean func_175648_a(BlockPos blockposition, int i, boolean flag) {
        return this.func_175663_a(blockposition.func_177958_n() - i, blockposition.func_177956_o() - i, blockposition.func_177952_p() - i, blockposition.func_177958_n() + i, blockposition.func_177956_o() + i, blockposition.func_177952_p() + i, flag);
    }

    public boolean func_175707_a(BlockPos blockposition, BlockPos blockposition1) {
        return this.func_175706_a(blockposition, blockposition1, true);
    }

    public boolean func_175706_a(BlockPos blockposition, BlockPos blockposition1, boolean flag) {
        return this.func_175663_a(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p(), flag);
    }

    public boolean func_175711_a(StructureBoundingBox structureboundingbox) {
        return this.func_175639_b(structureboundingbox, true);
    }

    public boolean func_175639_b(StructureBoundingBox structureboundingbox, boolean flag) {
        return this.func_175663_a(structureboundingbox.field_78897_a, structureboundingbox.field_78895_b, structureboundingbox.field_78896_c, structureboundingbox.field_78893_d, structureboundingbox.field_78894_e, structureboundingbox.field_78892_f, flag);
    }

    private boolean func_175663_a(int i, int j, int k, int l, int i1, int j1, boolean flag) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    if (!this.func_175680_a(k1, l1, flag)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public abstract boolean func_175680_a(int i, int j, boolean flag);

    public Chunk func_175726_f(BlockPos blockposition) {
        return this.func_72964_e(blockposition.func_177958_n() >> 4, blockposition.func_177952_p() >> 4);
    }

    public Chunk func_72964_e(int i, int j) {
        return this.field_73020_y.func_186025_d(i, j);
    }

    public boolean func_190526_b(int i, int j) {
        return this.func_175680_a(i, j, false) ? true : this.field_73020_y.func_191062_e(i, j);
    }

    public boolean func_180501_a(BlockPos blockposition, IBlockState iblockdata, int i) {
        // CraftBukkit start - tree generation
        if (this.captureTreeGeneration) {
            BlockState blockstate = null;
            Iterator<BlockState> it = capturedBlockStates.iterator();
            while (it.hasNext()) {
                BlockState previous = it.next();
                if (previous.getX() == blockposition.func_177958_n() && previous.getY() == blockposition.func_177956_o() && previous.getZ() == blockposition.func_177952_p()) {
                    blockstate = previous;
                    it.remove();
                    break;
                }
            }
            if (blockstate == null) {
                blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(this, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), i);
            }
            blockstate.setTypeId(CraftMagicNumbers.getId(iblockdata.func_177230_c()));
            blockstate.setRawData((byte) iblockdata.func_177230_c().func_176201_c(iblockdata));
            this.capturedBlockStates.add(blockstate);
            return true;
        }
        // CraftBukkit end
        if (blockposition.isInvalidYLocation()) { // Paper
            return false;
        } else if (!this.field_72995_K && this.field_72986_A.func_76067_t() == WorldType.field_180272_g) {
            return false;
        } else {
            Chunk chunk = this.func_175726_f(blockposition);
            Block block = iblockdata.func_177230_c();

            // CraftBukkit start - capture blockstates
            BlockState blockstate = null;
            if (this.captureBlockStates) {
                //blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i); // Paper
                blockstate = world.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()).getState(); // Paper - use CB getState to get a suitable snapshot
                this.capturedBlockStates.add(blockstate);
            }
            // CraftBukkit end

            IBlockState iblockdata1 = chunk.func_177436_a(blockposition, iblockdata);

            if (iblockdata1 == null) {
                // CraftBukkit start - remove blockstate if failed
                if (this.captureBlockStates) {
                    this.capturedBlockStates.remove(blockstate);
                }
                // CraftBukkit end
                return false;
            } else {
                if (iblockdata.func_185891_c() != iblockdata1.func_185891_c() || iblockdata.func_185906_d() != iblockdata1.func_185906_d()) {
                    this.field_72984_F.func_76320_a("checkLight");
                    chunk.runOrQueueLightUpdate(() -> this.func_175664_x(blockposition)); // Paper - Queue light update
                    this.field_72984_F.func_76319_b();
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
        if ((i & 2) != 0 && (!this.field_72995_K || (i & 4) == 0) && (chunk == null || chunk.func_150802_k())) { // allow chunk to be null here as chunk.isReady() is false when we send our notification during block placement
            this.func_184138_a(blockposition, oldBlock, newBlock, i);
        }

        if (!this.field_72995_K && (i & 1) != 0) {
            this.func_175722_b(blockposition, oldBlock.func_177230_c(), true);
            if (newBlock.func_185912_n()) {
                this.func_175666_e(blockposition, newBlock.func_177230_c());
            }
        } else if (!this.field_72995_K && (i & 16) == 0) {
            this.func_190522_c(blockposition, newBlock.func_177230_c());
        }
    }
    // CraftBukkit end

    public boolean func_175698_g(BlockPos blockposition) {
        return this.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 3);
    }

    public boolean func_175655_b(BlockPos blockposition, boolean flag) {
        IBlockState iblockdata = this.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();

        if (iblockdata.func_185904_a() == Material.field_151579_a) {
            return false;
        } else {
            this.func_175718_b(2001, blockposition, Block.func_176210_f(iblockdata));
            if (flag) {
                block.func_176226_b(this, blockposition, iblockdata, 0);
            }

            return this.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 3);
        }
    }

    public boolean func_175656_a(BlockPos blockposition, IBlockState iblockdata) {
        return this.func_180501_a(blockposition, iblockdata, 3);
    }

    public void func_184138_a(BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i) {
        for (int j = 0; j < this.field_73021_x.size(); ++j) {
            this.field_73021_x.get(j).func_184376_a(this, blockposition, iblockdata, iblockdata1, i);
        }

    }

    public void func_175722_b(BlockPos blockposition, Block block, boolean flag) {
        if (this.field_72986_A.func_76067_t() != WorldType.field_180272_g) {
            // CraftBukkit start
            if (populating) {
                return;
            }
            // CraftBukkit end
            this.func_175685_c(blockposition, block, flag);
        }

    }

    public void func_72975_g(int i, int j, int k, int l) {
        int i1;

        if (k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if (this.field_73011_w.func_191066_m()) {
            for (i1 = k; i1 <= l; ++i1) {
                this.func_180500_c(EnumSkyBlock.SKY, new BlockPos(i, i1, j));
            }
        }

        this.func_147458_c(i, k, j, i, l, j);
    }

    public void func_175704_b(BlockPos blockposition, BlockPos blockposition1) {
        this.func_147458_c(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p());
    }

    public void func_147458_c(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < this.field_73021_x.size(); ++k1) {
            this.field_73021_x.get(k1).func_147585_a(i, j, k, l, i1, j1);
        }

    }

    public void func_190522_c(BlockPos blockposition, Block block) {
        this.func_190529_b(blockposition.func_177976_e(), block, blockposition);
        this.func_190529_b(blockposition.func_177974_f(), block, blockposition);
        this.func_190529_b(blockposition.func_177977_b(), block, blockposition);
        this.func_190529_b(blockposition.func_177984_a(), block, blockposition);
        this.func_190529_b(blockposition.func_177978_c(), block, blockposition);
        this.func_190529_b(blockposition.func_177968_d(), block, blockposition);
    }

    public void func_175685_c(BlockPos blockposition, Block block, boolean flag) {
        if (captureBlockStates) { return; } // Paper - Cancel all physics during placement
        this.func_190524_a(blockposition.func_177976_e(), block, blockposition);
        this.func_190524_a(blockposition.func_177974_f(), block, blockposition);
        this.func_190524_a(blockposition.func_177977_b(), block, blockposition);
        this.func_190524_a(blockposition.func_177984_a(), block, blockposition);
        this.func_190524_a(blockposition.func_177978_c(), block, blockposition);
        this.func_190524_a(blockposition.func_177968_d(), block, blockposition);
        if (flag) {
            this.func_190522_c(blockposition, block);
        }

        this.chunkPacketBlockController.updateNearbyBlocks(this, blockposition); // Paper - Anti-Xray
    }

    public void func_175695_a(BlockPos blockposition, Block block, EnumFacing enumdirection) {
        if (enumdirection != EnumFacing.WEST) {
            this.func_190524_a(blockposition.func_177976_e(), block, blockposition);
        }

        if (enumdirection != EnumFacing.EAST) {
            this.func_190524_a(blockposition.func_177974_f(), block, blockposition);
        }

        if (enumdirection != EnumFacing.DOWN) {
            this.func_190524_a(blockposition.func_177977_b(), block, blockposition);
        }

        if (enumdirection != EnumFacing.UP) {
            this.func_190524_a(blockposition.func_177984_a(), block, blockposition);
        }

        if (enumdirection != EnumFacing.NORTH) {
            this.func_190524_a(blockposition.func_177978_c(), block, blockposition);
        }

        if (enumdirection != EnumFacing.SOUTH) {
            this.func_190524_a(blockposition.func_177968_d(), block, blockposition);
        }

    }

    public void func_190524_a(BlockPos blockposition, final Block block, BlockPos blockposition1) {
        if (!this.field_72995_K) {
            IBlockState iblockdata = this.func_180495_p(blockposition);

            try {
                // CraftBukkit start
                CraftWorld world = ((WorldServer) this).getWorld();
                if (world != null && !((WorldServer)this).stopPhysicsEvent) { // Paper
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), CraftMagicNumbers.getId(block));
                    this.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit end
                iblockdata.func_189546_a(this, blockposition, block, blockposition1);
            } catch (StackOverflowError stackoverflowerror) { // Spigot Start
                haveWeSilencedAPhysicsCrash = true;
                blockLocation = blockposition.func_177958_n() + ", " + blockposition.func_177956_o() + ", " + blockposition.func_177952_p();
                // Spigot End
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Exception while updating neighbours");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Block being updated");

                crashreportsystemdetails.func_189529_a("Source block type", new ICrashReportDetail() {
                    public String a() throws Exception {
                        try {
                            return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.func_149682_b(block)), block.func_149739_a(), block.getClass().getCanonicalName()});
                        } catch (Throwable throwable) {
                            return "ID #" + Block.func_149682_b(block);
                        }
                    }

                    @Override
                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                CrashReportCategory.func_175750_a(crashreportsystemdetails, blockposition, iblockdata);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void func_190529_b(BlockPos blockposition, final Block block, BlockPos blockposition1) {
        if (!this.field_72995_K) {
            IBlockState iblockdata = this.func_180495_p(blockposition);

            if (iblockdata.func_177230_c() == Blocks.field_190976_dk) {
                try {
                    ((BlockObserver) iblockdata.func_177230_c()).func_190962_b(iblockdata, this, blockposition, block, blockposition1);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.func_85055_a(throwable, "Exception while updating neighbours");
                    CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Block being updated");

                    crashreportsystemdetails.func_189529_a("Source block type", new ICrashReportDetail() {
                        public String a() throws Exception {
                            try {
                                return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.func_149682_b(block)), block.func_149739_a(), block.getClass().getCanonicalName()});
                            } catch (Throwable throwable) {
                                return "ID #" + Block.func_149682_b(block);
                            }
                        }

                        @Override
                        public Object call() throws Exception {
                            return this.a();
                        }
                    });
                    CrashReportCategory.func_175750_a(crashreportsystemdetails, blockposition, iblockdata);
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    public boolean func_175691_a(BlockPos blockposition, Block block) {
        return false;
    }

    public boolean func_175678_i(BlockPos blockposition) {
        return this.func_175726_f(blockposition).func_177444_d(blockposition);
    }

    public boolean func_175710_j(BlockPos blockposition) {
        if (blockposition.func_177956_o() >= this.func_181545_F()) {
            return this.func_175678_i(blockposition);
        } else {
            BlockPos blockposition1 = new BlockPos(blockposition.func_177958_n(), this.func_181545_F(), blockposition.func_177952_p());

            if (!this.func_175678_i(blockposition1)) {
                return false;
            } else {
                for (blockposition1 = blockposition1.func_177977_b(); blockposition1.func_177956_o() > blockposition.func_177956_o(); blockposition1 = blockposition1.func_177977_b()) {
                    IBlockState iblockdata = this.func_180495_p(blockposition1);

                    if (iblockdata.func_185891_c() > 0 && !iblockdata.func_185904_a().func_76224_d()) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int func_175699_k(BlockPos blockposition) {
        if (blockposition.func_177956_o() < 0) {
            return 0;
        } else {
            if (blockposition.func_177956_o() >= 256) {
                blockposition = new BlockPos(blockposition.func_177958_n(), 255, blockposition.func_177952_p());
            }

            return this.func_175726_f(blockposition).func_177443_a(blockposition, 0);
        }
    }

    // Paper start - test if meets light level, return faster
    // logic copied from below
    public boolean isLightLevel(BlockPos blockposition, int level) {
        if (blockposition.isValidLocation()) {
            if (this.func_180495_p(blockposition).func_185916_f()) {
                if (this.func_175721_c(blockposition.func_177984_a(), false) >= level) {
                    return true;
                }
                if (this.func_175721_c(blockposition.func_177974_f(), false) >= level) {
                    return true;
                }
                if (this.func_175721_c(blockposition.func_177976_e(), false) >= level) {
                    return true;
                }
                if (this.func_175721_c(blockposition.func_177968_d(), false) >= level) {
                    return true;
                }
                if (this.func_175721_c(blockposition.func_177978_c(), false) >= level) {
                    return true;
                }
                return false;
            } else {
                if (blockposition.func_177956_o() >= 256) {
                    blockposition = new BlockPos(blockposition.func_177958_n(), 255, blockposition.func_177952_p());
                }

                Chunk chunk = this.func_175726_f(blockposition);
                return chunk.getLightSubtracted(blockposition, this.getSkylightSubtracted()) >= level;
            }
        } else {
            return true;
        }
    }
    // Paper end

    public int func_175671_l(BlockPos blockposition) {
        return this.func_175721_c(blockposition, true);
    }

    public final int getLight(BlockPos blockposition, boolean checkNeighbors) { return this.func_175721_c(blockposition, checkNeighbors); } // Paper - OBFHELPER
    public int func_175721_c(BlockPos blockposition, boolean flag) {
        if (blockposition.func_177958_n() >= -30000000 && blockposition.func_177952_p() >= -30000000 && blockposition.func_177958_n() < 30000000 && blockposition.func_177952_p() < 30000000) {
            if (flag && this.func_180495_p(blockposition).func_185916_f()) {
                int i = this.func_175721_c(blockposition.func_177984_a(), false);
                int j = this.func_175721_c(blockposition.func_177974_f(), false);
                int k = this.func_175721_c(blockposition.func_177976_e(), false);
                int l = this.func_175721_c(blockposition.func_177968_d(), false);
                int i1 = this.func_175721_c(blockposition.func_177978_c(), false);

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
            } else if (blockposition.func_177956_o() < 0) {
                return 0;
            } else {
                if (blockposition.func_177956_o() >= 256) {
                    blockposition = new BlockPos(blockposition.func_177958_n(), 255, blockposition.func_177952_p());
                }
                if (!this.func_175667_e(blockposition)) return 0; // Paper

                Chunk chunk = this.func_175726_f(blockposition);

                return chunk.func_177443_a(blockposition, this.field_73008_k);
            }
        } else {
            return 15;
        }
    }

    public BlockPos func_175645_m(BlockPos blockposition) {
        return new BlockPos(blockposition.func_177958_n(), this.func_189649_b(blockposition.func_177958_n(), blockposition.func_177952_p()), blockposition.func_177952_p());
    }

    public int func_189649_b(int i, int j) {
        int k;

        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (this.func_175680_a(i >> 4, j >> 4, true)) {
                k = this.func_72964_e(i >> 4, j >> 4).func_76611_b(i & 15, j & 15);
            } else {
                k = 0;
            }
        } else {
            k = this.func_181545_F() + 1;
        }

        return k;
    }

    @Deprecated
    public int func_82734_g(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.func_175680_a(i >> 4, j >> 4, true)) {
                return 0;
            } else {
                Chunk chunk = this.func_72964_e(i >> 4, j >> 4);

                return chunk.func_177442_v();
            }
        } else {
            return this.func_181545_F() + 1;
        }
    }

    public int func_175642_b(EnumSkyBlock enumskyblock, BlockPos blockposition) {
        if (blockposition.func_177956_o() < 0) {
            blockposition = new BlockPos(blockposition.func_177958_n(), 0, blockposition.func_177952_p());
        }

        if (!blockposition.isValidLocation()) { // Paper
            return enumskyblock.field_77198_c;
        } else if (!this.func_175667_e(blockposition)) {
            return enumskyblock.field_77198_c;
        } else {
            Chunk chunk = this.func_175726_f(blockposition);

            return chunk.func_177413_a(enumskyblock, blockposition);
        }
    }

    public void func_175653_a(EnumSkyBlock enumskyblock, BlockPos blockposition, int i) {
        if (blockposition.isValidLocation()) { // Paper
            if (this.func_175667_e(blockposition)) {
                Chunk chunk = this.func_175726_f(blockposition);

                chunk.func_177431_a(enumskyblock, blockposition, i);
                this.func_175679_n(blockposition);
            }
        }
    }

    public void func_175679_n(BlockPos blockposition) {
        for (int i = 0; i < this.field_73021_x.size(); ++i) {
            this.field_73021_x.get(i).func_174959_b(blockposition);
        }

    }

    public float func_175724_o(BlockPos blockposition) {
        return this.field_73011_w.func_177497_p()[this.func_175671_l(blockposition)];
    }

    // Paper start - reduces need to do isLoaded before getType
    public IBlockState getTypeIfLoaded(BlockPos blockposition) {
        // CraftBukkit start - tree generation
        final int x = blockposition.func_177958_n();
        final int y = blockposition.func_177956_o();
        final int z = blockposition.func_177952_p();
        if (captureTreeGeneration) {
            final IBlockState previous = getCapturedBlockType(x, y, z);
            if (previous != null) {
                return previous;
            }
        }
        // CraftBukkit end
        Chunk chunk = ((ChunkProviderServer) this.field_73020_y).getChunkIfLoaded(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockData(x, y, z);
        }
        return null;
    }
    // Paper end

    @Override
    public IBlockState func_180495_p(BlockPos blockposition) {
        // CraftBukkit start - tree generation
        // Paper start - optimize getType lookup to reduce instructions - getBlockData already enforces valid Y, move tree out
        final int x = blockposition.func_177958_n();
        final int y = blockposition.func_177956_o();
        final int z = blockposition.func_177952_p();
        if (captureTreeGeneration) {
            final IBlockState previous = getCapturedBlockType(x, y, z);
            if (previous != null) {
                return previous;
            }
        }
        // CraftBukkit end
        return this.field_73020_y.func_186025_d(x >> 4, z >> 4).getBlockData(x, y, z);
        // Paper end
    }

    // Paper start
    private IBlockState getCapturedBlockType(int x, int y, int z) {
        Iterator<BlockState> it = capturedBlockStates.iterator();
        while (it.hasNext()) {
            BlockState previous = it.next();
            if (previous.getX() == x && previous.getY() == y && previous.getZ() == z) {
                return CraftMagicNumbers.getBlock(previous.getTypeId()).func_176203_a(previous.getRawData());
            }
        }
        return null;
    }
    // Paper end

    public boolean func_72935_r() {
        return this.field_73008_k < 4;
    }

    @Nullable
    public RayTraceResult func_72933_a(Vec3d vec3d, Vec3d vec3d1) {
        return this.func_147447_a(vec3d, vec3d1, false, false, false);
    }

    @Nullable
    public RayTraceResult func_72901_a(Vec3d vec3d, Vec3d vec3d1, boolean flag) {
        return this.func_147447_a(vec3d, vec3d1, flag, false, false);
    }

    @Nullable
    public RayTraceResult func_147447_a(Vec3d vec3d, Vec3d vec3d1, boolean flag, boolean flag1, boolean flag2) {
        if (!Double.isNaN(vec3d.field_72450_a) && !Double.isNaN(vec3d.field_72448_b) && !Double.isNaN(vec3d.field_72449_c)) {
            if (!Double.isNaN(vec3d1.field_72450_a) && !Double.isNaN(vec3d1.field_72448_b) && !Double.isNaN(vec3d1.field_72449_c)) {
                int i = MathHelper.func_76128_c(vec3d1.field_72450_a);
                int j = MathHelper.func_76128_c(vec3d1.field_72448_b);
                int k = MathHelper.func_76128_c(vec3d1.field_72449_c);
                int l = MathHelper.func_76128_c(vec3d.field_72450_a);
                int i1 = MathHelper.func_76128_c(vec3d.field_72448_b);
                int j1 = MathHelper.func_76128_c(vec3d.field_72449_c);
                BlockPos blockposition = new BlockPos(l, i1, j1);
                IBlockState iblockdata = this.func_180495_p(blockposition);
                Block block = iblockdata.func_177230_c();

                if ((!flag1 || iblockdata.func_185890_d(this, blockposition) != Block.field_185506_k) && block.func_176209_a(iblockdata, flag)) {
                    RayTraceResult movingobjectposition = iblockdata.func_185910_a(this, blockposition, vec3d, vec3d1);

                    if (movingobjectposition != null) {
                        return movingobjectposition;
                    }
                }

                RayTraceResult movingobjectposition1 = null;
                int k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec3d.field_72450_a) || Double.isNaN(vec3d.field_72448_b) || Double.isNaN(vec3d.field_72449_c)) {
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
                    double d6 = vec3d1.field_72450_a - vec3d.field_72450_a;
                    double d7 = vec3d1.field_72448_b - vec3d.field_72448_b;
                    double d8 = vec3d1.field_72449_c - vec3d.field_72449_c;

                    if (flag3) {
                        d3 = (d0 - vec3d.field_72450_a) / d6;
                    }

                    if (flag4) {
                        d4 = (d1 - vec3d.field_72448_b) / d7;
                    }

                    if (flag5) {
                        d5 = (d2 - vec3d.field_72449_c) / d8;
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
                        vec3d = new Vec3d(d0, vec3d.field_72448_b + d7 * d3, vec3d.field_72449_c + d8 * d3);
                    } else if (d4 < d5) {
                        enumdirection = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec3d = new Vec3d(vec3d.field_72450_a + d6 * d4, d1, vec3d.field_72449_c + d8 * d4);
                    } else {
                        enumdirection = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec3d = new Vec3d(vec3d.field_72450_a + d6 * d5, vec3d.field_72448_b + d7 * d5, d2);
                    }

                    l = MathHelper.func_76128_c(vec3d.field_72450_a) - (enumdirection == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.func_76128_c(vec3d.field_72448_b) - (enumdirection == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.func_76128_c(vec3d.field_72449_c) - (enumdirection == EnumFacing.SOUTH ? 1 : 0);
                    blockposition = new BlockPos(l, i1, j1);
                    IBlockState iblockdata1 = this.func_180495_p(blockposition);
                    Block block1 = iblockdata1.func_177230_c();

                    if (!flag1 || iblockdata1.func_185904_a() == Material.field_151567_E || iblockdata1.func_185890_d(this, blockposition) != Block.field_185506_k) {
                        if (block1.func_176209_a(iblockdata1, flag)) {
                            RayTraceResult movingobjectposition2 = iblockdata1.func_185910_a(this, blockposition, vec3d, vec3d1);

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

    public void func_184133_a(@Nullable EntityPlayer entityhuman, BlockPos blockposition, SoundEvent soundeffect, SoundCategory soundcategory, float f, float f1) {
        this.func_184148_a(entityhuman, blockposition.func_177958_n() + 0.5D, blockposition.func_177956_o() + 0.5D, blockposition.func_177952_p() + 0.5D, soundeffect, soundcategory, f, f1);
    }

    // Paper start - OBFHELPER
    public final void sendSoundEffect(@Nullable EntityPlayer fromEntity, double x, double y, double z, SoundEvent soundeffect, SoundCategory soundcategory, float volume, float pitch) {
        this.func_184148_a(fromEntity, x, y, z, soundeffect, soundcategory, volume, pitch);
    }
    // Paper end

    public void func_184148_a(@Nullable EntityPlayer entityhuman, double d0, double d1, double d2, SoundEvent soundeffect, SoundCategory soundcategory, float f, float f1) {
        for (int i = 0; i < this.field_73021_x.size(); ++i) {
            this.field_73021_x.get(i).func_184375_a(entityhuman, soundeffect, soundcategory, d0, d1, d2, f, f1);
        }

    }

    public void func_184134_a(double d0, double d1, double d2, SoundEvent soundeffect, SoundCategory soundcategory, float f, float f1, boolean flag) {}

    public void func_184149_a(BlockPos blockposition, @Nullable SoundEvent soundeffect) {
        for (int i = 0; i < this.field_73021_x.size(); ++i) {
            this.field_73021_x.get(i).func_184377_a(soundeffect, blockposition);
        }

    }

    public void func_175688_a(EnumParticleTypes enumparticle, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        this.func_175720_a(enumparticle.func_179348_c(), enumparticle.func_179344_e(), d0, d1, d2, d3, d4, d5, aint);
    }

    public void func_190523_a(int i, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        for (int j = 0; j < this.field_73021_x.size(); ++j) {
            this.field_73021_x.get(j).func_190570_a(i, false, true, d0, d1, d2, d3, d4, d5, aint);
        }

    }

    private void func_175720_a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        for (int j = 0; j < this.field_73021_x.size(); ++j) {
            this.field_73021_x.get(j).func_180442_a(i, flag, d0, d1, d2, d3, d4, d5, aint);
        }

    }

    public boolean func_72942_c(Entity entity) {
        this.field_73007_j.add(entity);
        return true;
    }

    public boolean func_72838_d(Entity entity) {
        // CraftBukkit start - Used for entities other than creatures
        return addEntity(entity, SpawnReason.DEFAULT);
    }

    public boolean addEntity(Entity entity, SpawnReason spawnReason) { // Changed signature, added SpawnReason
        org.spigotmc.AsyncCatcher.catchOp( "entity add"); // Spigot
        if (entity == null) return false;
        if (entity.valid) { MinecraftServer.field_147145_h.error("Attempted Double World add on " + entity, new Throwable()); return true; } // Paper

        org.bukkit.event.Cancellable event = null;
        if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayerMP)) {
            boolean isAnimal = entity instanceof EntityAnimal || entity instanceof EntityWaterMob || entity instanceof EntityGolem;
            boolean isMonster = entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntitySlime;
            boolean isNpc = entity instanceof INpc;

            if (spawnReason != SpawnReason.CUSTOM) {
                if (isAnimal && !field_72992_H || isMonster && !field_72985_G || isNpc && !getServer().getServer().func_71220_V()) {
                    entity.field_70128_L = true;
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
                if (mergeUnconditionally || xp.field_70530_e < maxValue) { // Paper - Skip iteration if unnecessary

                List<Entity> entities = this.func_72839_b(entity, entity.func_174813_aQ().func_72314_b(radius, radius, radius));
                for (Entity e : entities) {
                    if (e instanceof EntityXPOrb) {
                        EntityXPOrb loopItem = (EntityXPOrb) e;
                        if (!loopItem.field_70128_L && !(maxValue > 0 && loopItem.field_70530_e >= maxValue) && new com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent((org.bukkit.entity.ExperienceOrb) entity.getBukkitEntity(), (org.bukkit.entity.ExperienceOrb) loopItem.getBukkitEntity()).callEvent()) { // Paper
                            xp.field_70530_e += loopItem.field_70530_e;
                            // Paper start
                            if (!mergeUnconditionally && xp.field_70530_e > maxValue) {
                                loopItem.field_70530_e = xp.field_70530_e - maxValue;
                                xp.field_70530_e = maxValue;
                                break;
                            }
                            // Paper end
                            loopItem.func_70106_y();
                        }
                    }
                }

                } // Paper end - End iteration skip check - All tweaking ends here
            }
        } // Spigot end

        if (event != null && (event.isCancelled() || entity.field_70128_L)) {
            entity.field_70128_L = true;
            return false;
        }
        // CraftBukkit end

        int i = MathHelper.func_76128_c(entity.field_70165_t / 16.0D);
        int j = MathHelper.func_76128_c(entity.field_70161_v / 16.0D);
        boolean flag = entity.field_98038_p;

        // Paper start - Set origin location when the entity is being added to the world
        if (entity.origin == null) {
            entity.origin = entity.getBukkitEntity().getLocation();
        }
        // Paper end

        if (entity instanceof EntityPlayer) {
            flag = true;
        }

        if (!flag && !this.func_175680_a(i, j, false)) {
            return false;
        } else {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                this.field_73010_i.add(entityhuman);
                this.func_72854_c();
            }

            this.func_72964_e(i, j).func_76612_a(entity);
            this.field_72996_f.add(entity);
            this.func_72923_a(entity);
            return true;
        }
    }

    protected void func_72923_a(Entity entity) {
        for (int i = 0; i < this.field_73021_x.size(); ++i) {
            this.field_73021_x.get(i).func_72703_a(entity);
        }

        entity.valid = true; // CraftBukkit
        new com.destroystokyo.paper.event.entity.EntityAddToWorldEvent(entity.getBukkitEntity()).callEvent(); // Paper - fire while valid
    }

    protected void func_72847_b(Entity entity) {
        for (int i = 0; i < this.field_73021_x.size(); ++i) {
            this.field_73021_x.get(i).func_72709_b(entity);
        }

        new com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent(entity.getBukkitEntity()).callEvent(); // Paper - fire while valid
        entity.valid = false; // CraftBukkit
    }

    public void func_72900_e(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp( "entity kill"); // Spigot
        if (entity.func_184207_aI()) {
            entity.func_184226_ay();
        }

        if (entity.func_184218_aH()) {
            entity.func_184210_p();
        }

        entity.func_70106_y();
        if (entity instanceof EntityPlayer) {
            this.field_73010_i.remove(entity);
            // Spigot start
            for ( Object o : field_72988_C.field_75750_c )
            {
                if ( o instanceof MapData )
                {
                    MapData map = (MapData) o;
                    map.field_76202_j.remove( entity );
                    for ( Iterator<MapData.MapInfo> iter = map.field_76196_g.iterator(); iter.hasNext(); )
                    {
                        if ( iter.next().field_76211_a == entity )
                        {
                            map.field_76203_h.remove(entity.func_110124_au()); // Paper
                            iter.remove();
                        }
                    }
                }
            }
            // Spigot end
            this.func_72854_c();
            this.func_72847_b(entity);
        }

    }

    public void func_72973_f(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp( "entity remove"); // Spigot
        entity.func_184174_b(false);
        entity.func_70106_y();
        if (entity instanceof EntityPlayer) {
            this.field_73010_i.remove(entity);
            this.func_72854_c();
        }

        if (!guardEntityList) { // Spigot - It will get removed after the tick if we are ticking
        int i = entity.field_70176_ah;
        int j = entity.field_70164_aj;

        if (entity.field_70175_ag && this.func_175680_a(i, j, true)) {
            this.func_72964_e(i, j).func_76622_b(entity);
        }

        // CraftBukkit start - Decrement loop variable field if we've already ticked this entity
        int index = this.field_72996_f.indexOf(entity);
        if (index != -1) {
            if (index <= this.tickPosition) {
                this.tickPosition--;
            }
            this.field_72996_f.remove(index);
        }
        // CraftBukkit end
        } // Spigot
        this.func_72847_b(entity);
    }

    public void func_72954_a(IWorldEventListener iworldaccess) {
        this.field_73021_x.add(iworldaccess);
    }

    private boolean func_191504_a(@Nullable Entity entity, AxisAlignedBB axisalignedbb, boolean flag, @Nullable List<AxisAlignedBB> list) {
        int i = MathHelper.func_76128_c(axisalignedbb.field_72340_a) - 1;
        int j = MathHelper.func_76143_f(axisalignedbb.field_72336_d) + 1;
        int k = MathHelper.func_76128_c(axisalignedbb.field_72338_b) - 1;
        int l = MathHelper.func_76143_f(axisalignedbb.field_72337_e) + 1;
        int i1 = MathHelper.func_76128_c(axisalignedbb.field_72339_c) - 1;
        int j1 = MathHelper.func_76143_f(axisalignedbb.field_72334_f) + 1;
        WorldBorder worldborder = this.func_175723_af();
        boolean flag1 = entity != null && entity.func_174832_aS();
        boolean flag2 = entity != null && this.func_191503_g(entity);
        IBlockState iblockdata = Blocks.field_150348_b.func_176223_P();
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = i1; l1 < j1; ++l1) {
                    boolean flag3 = k1 == i || k1 == j - 1;
                    boolean flag4 = l1 == i1 || l1 == j1 - 1;

                    if ((!flag3 || !flag4) && this.func_175667_e(blockposition_pooledblockposition.func_181079_c(k1, 64, l1))) {
                        for (int i2 = k; i2 < l; ++i2) {
                            if (!flag3 && !flag4 || i2 != l - 1) {
                                if (flag) {
                                    if (k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000) {
                                        boolean flag5 = true;

                                        return flag5;
                                    }
                                } else if (entity != null && flag1 == flag2) {
                                    entity.func_174821_h(!flag2);
                                }

                                blockposition_pooledblockposition.func_181079_c(k1, i2, l1);
                                IBlockState iblockdata1;

                                if (!flag && !worldborder.func_177746_a(blockposition_pooledblockposition) && flag2) {
                                    iblockdata1 = iblockdata;
                                } else {
                                    iblockdata1 = this.func_180495_p(blockposition_pooledblockposition);
                                }

                                iblockdata1.func_185908_a(this, blockposition_pooledblockposition, axisalignedbb, list, entity, false);
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
            blockposition_pooledblockposition.func_185344_t();
        }
    }

    public List<AxisAlignedBB> func_184144_a(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
        ArrayList arraylist = Lists.newArrayList();

        this.func_191504_a(entity, axisalignedbb, false, arraylist);
        if (entity != null) {
            if (entity instanceof EntityArmorStand && !entity.field_70170_p.paperConfig.armorStandEntityLookups) return arraylist; // Paper
            List list = this.func_72839_b(entity, axisalignedbb.func_186662_g(0.25D));

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (!entity.func_184223_x(entity1)) {
                    AxisAlignedBB axisalignedbb1 = entity1.func_70046_E();

                    if (axisalignedbb1 != null && axisalignedbb1.func_72326_a(axisalignedbb)) {
                        arraylist.add(axisalignedbb1);
                    }

                    axisalignedbb1 = entity.func_70114_g(entity1);
                    if (axisalignedbb1 != null && axisalignedbb1.func_72326_a(axisalignedbb)) {
                        arraylist.add(axisalignedbb1);
                    }
                }
            }
        }

        return arraylist;
    }

    public boolean func_191503_g(Entity entity) {
        double d0 = this.field_175728_M.func_177726_b();
        double d1 = this.field_175728_M.func_177736_c();
        double d2 = this.field_175728_M.func_177728_d();
        double d3 = this.field_175728_M.func_177733_e();

        if (entity.func_174832_aS()) {
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

        return entity.field_70165_t > d0 && entity.field_70165_t < d2 && entity.field_70161_v > d1 && entity.field_70161_v < d3;
    }

    public boolean func_184143_b(AxisAlignedBB axisalignedbb) {
        return this.func_191504_a((Entity) null, axisalignedbb, true, Lists.<AxisAlignedBB>newArrayList()); // CraftBukkit - decompile error
    }

    public int func_72967_a(float f) {
        float f1 = this.func_72826_c(f);
        float f2 = 1.0F - (MathHelper.func_76134_b(f1 * 6.2831855F) * 2.0F + 0.5F);

        f2 = MathHelper.func_76131_a(f2, 0.0F, 1.0F);
        f2 = 1.0F - f2;
        f2 = (float) (f2 * (1.0D - this.func_72867_j(f) * 5.0F / 16.0D));
        f2 = (float) (f2 * (1.0D - this.func_72819_i(f) * 5.0F / 16.0D));
        f2 = 1.0F - f2;
        return (int) (f2 * 11.0F);
    }

    public float func_72826_c(float f) {
        return this.field_73011_w.func_76563_a(this.field_72986_A.func_76073_f(), f);
    }

    public float func_130001_d() {
        return WorldProvider.field_111203_a[this.field_73011_w.func_76559_b(this.field_72986_A.func_76073_f())];
    }

    public float func_72929_e(float f) {
        float f1 = this.func_72826_c(f);

        return f1 * 6.2831855F;
    }

    public BlockPos func_175725_q(BlockPos blockposition) {
        return this.func_175726_f(blockposition).func_177440_h(blockposition);
    }

    public BlockPos func_175672_r(BlockPos blockposition) {
        Chunk chunk = this.func_175726_f(blockposition);

        BlockPos blockposition1;
        BlockPos blockposition2;

        for (blockposition1 = new BlockPos(blockposition.func_177958_n(), chunk.func_76625_h() + 16, blockposition.func_177952_p()); blockposition1.func_177956_o() >= 0; blockposition1 = blockposition2) {
            blockposition2 = blockposition1.func_177977_b();
            Material material = chunk.func_177435_g(blockposition2).func_185904_a();

            if (material.func_76230_c() && material != Material.field_151584_j) {
                break;
            }
        }

        return blockposition1;
    }

    public boolean func_184145_b(BlockPos blockposition, Block block) {
        return true;
    }

    public void func_175684_a(BlockPos blockposition, Block block, int i) {}

    public void func_175654_a(BlockPos blockposition, Block block, int i, int j) {}

    public void func_180497_b(BlockPos blockposition, Block block, int i, int j) {}

    public void func_72939_s() {
        this.field_72984_F.func_76320_a("entities");
        this.field_72984_F.func_76320_a("global");

        int i;
        Entity entity;

        for (i = 0; i < this.field_73007_j.size(); ++i) {
            entity = this.field_73007_j.get(i);
            // CraftBukkit start - Fixed an NPE
            if (entity == null) {
                continue;
            }
            // CraftBukkit end

            try {
                ++entity.field_70173_aa;
                entity.func_70071_h_();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Ticking entity");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Entity being ticked");

                if (entity == null) {
                    crashreportsystemdetails.func_71507_a("Entity", "~~NULL~~");
                } else {
                    entity.func_85029_a(crashreportsystemdetails);
                }

                throw new ReportedException(crashreport);
            }

            if (entity.field_70128_L) {
                this.field_73007_j.remove(i--);
            }
        }

        this.field_72984_F.func_76318_c("remove");
        timings.entityRemoval.startTiming(); // Paper
        this.field_72996_f.removeAll(this.field_72997_g);

        int j;
        // Paper start - Set based removal lists
        for (Entity e : this.field_72997_g) {
            j = e.getChunkZ();
            int k = e.getChunkX();

            if (e.isAddedToChunk() && this.func_175680_a(k, j, true)) {
                this.func_72964_e(k, j).func_76622_b(e);
            }
        }

        for (Entity e : this.field_72997_g) {
            this.func_72847_b(e);
        }
        // Paper end

        this.field_72997_g.clear();
        this.func_184147_l();
        timings.entityRemoval.stopTiming(); // Paper
        this.field_72984_F.func_76318_c("regular");

        CrashReportCategory crashreportsystemdetails1;
        CrashReport crashreport1;

        org.spigotmc.ActivationRange.activateEntities(this); // Spigot
        timings.entityTick.startTiming(); // Spigot
        guardEntityList = true; // Spigot
        // CraftBukkit start - Use field for loop variable
        co.aikar.timings.TimingHistory.entityTicks += this.field_72996_f.size(); // Paper
        int entitiesThisCycle = 0;
        // Paper start - Disable tick limiters
        //if (tickPosition < 0) tickPosition = 0;
        for (tickPosition = 0; tickPosition < field_72996_f.size(); tickPosition++) {
            // Paper end
            tickPosition = (tickPosition < field_72996_f.size()) ? tickPosition : 0;
            entity = this.field_72996_f.get(this.tickPosition);
            // CraftBukkit end
            Entity entity1 = entity.func_184187_bx();

            if (entity1 != null) {
                if (!entity1.field_70128_L && entity1.func_184196_w(entity)) {
                    continue;
                }

                entity.func_184210_p();
            }

            this.field_72984_F.func_76320_a("tick");
            if (!entity.field_70128_L && !(entity instanceof EntityPlayerMP)) {
                try {
                    entity.tickTimer.startTiming(); // Paper
                    this.func_72870_g(entity);
                    entity.tickTimer.stopTiming(); // Paper
                } catch (Throwable throwable1) {
                    entity.tickTimer.stopTiming();
                    // Paper start - Prevent tile entity and entity crashes
                    String msg = "Entity threw exception at " + entity.field_70170_p.getWorld().getName() + ":" + entity.field_70165_t + "," + entity.field_70163_u + "," + entity.field_70161_v;
                    System.err.println(msg);
                    throwable1.printStackTrace();
                    getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(msg, throwable1)));
                    entity.field_70128_L = true;
                    continue;
                    // Paper end
                }
            }

            this.field_72984_F.func_76319_b();
            this.field_72984_F.func_76320_a("remove");
            if (entity.field_70128_L) {
                j = entity.field_70176_ah;
                int l = entity.field_70164_aj;

                if (entity.field_70175_ag && this.func_175680_a(j, l, true)) {
                    this.func_72964_e(j, l).func_76622_b(entity);
                }

                guardEntityList = false; // Spigot
                this.field_72996_f.remove(this.tickPosition--); // CraftBukkit - Use field for loop variable
                guardEntityList = true; // Spigot
                this.func_72847_b(entity);
            }

            this.field_72984_F.func_76319_b();
        }
        guardEntityList = false; // Spigot

        timings.entityTick.stopTiming(); // Spigot
        this.field_72984_F.func_76318_c("blockEntities");
        timings.tileEntityTick.startTiming(); // Spigot
        if (!this.field_147483_b.isEmpty()) {
            // Paper start - Use alternate implementation with faster contains
            java.util.Set<TileEntity> toRemove = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
            toRemove.addAll(field_147483_b);
            this.field_175730_i.removeAll(toRemove);
            // Paper end
            //this.tileEntityList.removeAll(this.tileEntityListUnload); // Paper - remove unused list
            this.field_147483_b.clear();
        }

        this.field_147481_N = true;
        // Spigot start
        // Iterator iterator = this.tileEntityListTick.iterator();
        int tilesThisCycle = 0;
        for (tileTickPosition = 0; tileTickPosition < field_175730_i.size(); tileTickPosition++) { // Paper - Disable tick limiters
            tileTickPosition = (tileTickPosition < field_175730_i.size()) ? tileTickPosition : 0;
            TileEntity tileentity = this.field_175730_i.get(tileTickPosition);
            // Spigot start
            if (tileentity == null) {
                getServer().getLogger().severe("Spigot has detected a null entity and has removed it, preventing a crash");
                tilesThisCycle--;
                this.field_175730_i.remove(tileTickPosition--);
                continue;
            }
            // Spigot end

            if (!tileentity.func_145837_r() && tileentity.func_145830_o()) {
                BlockPos blockposition = tileentity.func_174877_v();

                // Paper start - Skip ticking in chunks scheduled for unload
                net.minecraft.world.chunk.Chunk chunk = this.getChunkIfLoaded(blockposition);
                boolean shouldTick = chunk != null;
                if(this.paperConfig.skipEntityTickingInChunksScheduledForUnload)
                    shouldTick = shouldTick && !chunk.isUnloading() && chunk.scheduledForUnload == null;
                if (shouldTick && this.field_175728_M.func_177746_a(blockposition)) {
                    // Paper end
                    try {
                        this.field_72984_F.func_194340_a(() -> {
                            return String.valueOf(TileEntity.func_190559_a(tileentity.getClass()));
                        });
                        tileentity.tickTimer.startTiming(); // Spigot
                        ((ITickable) tileentity).func_73660_a();
                        this.field_72984_F.func_76319_b();
                    } catch (Throwable throwable2) {
                        // Paper start - Prevent tile entity and entity crashes
                        String msg = "TileEntity threw exception at " + tileentity.field_145850_b.getWorld().getName() + ":" + tileentity.field_174879_c.func_177958_n() + "," + tileentity.field_174879_c.func_177956_o() + "," + tileentity.field_174879_c.func_177952_p();
                        System.err.println(msg);
                        throwable2.printStackTrace();
                        getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(msg, throwable2)));
                        tilesThisCycle--;
                        this.field_175730_i.remove(tileTickPosition--);
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

            if (tileentity.func_145837_r()) {
                tilesThisCycle--;
                this.field_175730_i.remove(tileTickPosition--);
                //this.tileEntityList.remove(tileentity); // Paper - remove unused list
                if (this.func_175667_e(tileentity.func_174877_v())) {
                    this.func_175726_f(tileentity.func_174877_v()).func_177425_e(tileentity.func_174877_v());
                }
            }
        }

        timings.tileEntityTick.stopTiming(); // Spigot
        timings.tileEntityPending.startTiming(); // Spigot
        this.field_147481_N = false;
        this.field_72984_F.func_76318_c("pendingBlockEntities");
        if (!this.field_147484_a.isEmpty()) {
            for (int i1 = 0; i1 < this.field_147484_a.size(); ++i1) {
                TileEntity tileentity1 = this.field_147484_a.get(i1);

                if (!tileentity1.func_145837_r()) {
                    /* CraftBukkit start - Order matters, moved down
                    if (!this.tileEntityList.contains(tileentity1)) {
                        this.a(tileentity1);
                    }
                    // CraftBukkit end */

                    if (this.func_175667_e(tileentity1.func_174877_v())) {
                        Chunk chunk = this.func_175726_f(tileentity1.func_174877_v());
                        IBlockState iblockdata = chunk.func_177435_g(tileentity1.func_174877_v());

                        chunk.func_177426_a(tileentity1.func_174877_v(), tileentity1);
                        this.func_184138_a(tileentity1.func_174877_v(), iblockdata, iblockdata, 3);
                        // CraftBukkit start
                        // From above, don't screw this up - SPIGOT-1746
                        if (true) { // Paper - remove unused list
                            this.func_175700_a(tileentity1);
                        }
                        // CraftBukkit end
                    }
                }
            }

            this.field_147484_a.clear();
        }

        timings.tileEntityPending.stopTiming(); // Spigot
        co.aikar.timings.TimingHistory.tileEntityTicks += this.field_175730_i.size(); // Paper
        this.field_72984_F.func_76319_b();
        this.field_72984_F.func_76319_b();
    }

    protected void func_184147_l() {}

    public boolean func_175700_a(TileEntity tileentity) {
        boolean flag = true; // Paper - remove unused list

        if (flag && tileentity instanceof ITickable && !this.field_175730_i.contains(tileentity)) { // Paper
            this.field_175730_i.add(tileentity);
        }

        if (this.field_72995_K) {
            BlockPos blockposition = tileentity.func_174877_v();
            IBlockState iblockdata = this.func_180495_p(blockposition);

            this.func_184138_a(blockposition, iblockdata, iblockdata, 2);
        }

        return flag;
    }

    public void func_147448_a(Collection<TileEntity> collection) {
        if (this.field_147481_N) {
            this.field_147484_a.addAll(collection);
        } else {
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                TileEntity tileentity = (TileEntity) iterator.next();

                this.func_175700_a(tileentity);
            }
        }

    }

    public void func_72870_g(Entity entity) {
        this.func_72866_a(entity, true);
    }

    public void func_72866_a(Entity entity, boolean flag) {
        int i;
        int j;

        // CraftBukkit start - check if chunks are loaded as done in previous versions
        // TODO: Go back to Vanilla behaviour when comfortable
        // Spigot start
        // Chunk startingChunk = this.getChunkIfLoaded(MathHelper.floor(entity.locX) >> 4, MathHelper.floor(entity.locZ) >> 4);
        if (flag && !org.spigotmc.ActivationRange.checkIfActive(entity)) {
            entity.field_70173_aa++;
            entity.inactiveTick();
            // Spigot end
            return;
        }
        // CraftBukkit end

        entity.field_70142_S = entity.field_70165_t;
        entity.field_70137_T = entity.field_70163_u;
        entity.field_70136_U = entity.field_70161_v;
        entity.field_70126_B = entity.field_70177_z;
        entity.field_70127_C = entity.field_70125_A;
        if (flag && entity.field_70175_ag) {
            ++entity.field_70173_aa;
            ++co.aikar.timings.TimingHistory.activatedEntityTicks; // Paper
            if (entity.func_184218_aH()) {
                entity.func_70098_U();
            } else {
                entity.func_70071_h_();
                entity.postTick(); // CraftBukkit
            }
        }

        this.field_72984_F.func_76320_a("chunkCheck");
        if (Double.isNaN(entity.field_70165_t) || Double.isInfinite(entity.field_70165_t)) {
            entity.field_70165_t = entity.field_70142_S;
        }

        if (Double.isNaN(entity.field_70163_u) || Double.isInfinite(entity.field_70163_u)) {
            entity.field_70163_u = entity.field_70137_T;
        }

        if (Double.isNaN(entity.field_70161_v) || Double.isInfinite(entity.field_70161_v)) {
            entity.field_70161_v = entity.field_70136_U;
        }

        if (Double.isNaN(entity.field_70125_A) || Double.isInfinite(entity.field_70125_A)) {
            entity.field_70125_A = entity.field_70127_C;
        }

        if (Double.isNaN(entity.field_70177_z) || Double.isInfinite(entity.field_70177_z)) {
            entity.field_70177_z = entity.field_70126_B;
        }

        i = MathHelper.func_76128_c(entity.field_70165_t / 16.0D);
        j = Math.min(15, Math.max(0, MathHelper.func_76128_c(entity.field_70163_u / 16.0D))); // Paper - stay consistent with chunk add/remove behavior
        int k = MathHelper.func_76128_c(entity.field_70161_v / 16.0D);

        if (!entity.field_70175_ag || entity.field_70176_ah != i || entity.field_70162_ai != j || entity.field_70164_aj != k) {
            if (entity.field_70175_ag && this.func_175680_a(entity.field_70176_ah, entity.field_70164_aj, true)) {
                this.func_72964_e(entity.field_70176_ah, entity.field_70164_aj).func_76608_a(entity, entity.field_70162_ai);
            }

            if (!entity.func_184189_br() && !this.func_175680_a(i, k, true)) {
                entity.field_70175_ag = false;
            } else {
                this.func_72964_e(i, k).func_76612_a(entity);
            }
        }

        this.field_72984_F.func_76319_b();
        if (flag && entity.field_70175_ag) {
            Iterator iterator = entity.func_184188_bt().iterator();

            while (iterator.hasNext()) {
                Entity entity1 = (Entity) iterator.next();

                if (!entity1.field_70128_L && entity1.func_184187_bx() == entity) {
                    this.func_72870_g(entity1);
                } else {
                    entity1.func_184210_p();
                }
            }
        }
    }

    public boolean func_72855_b(AxisAlignedBB axisalignedbb) {
        return this.func_72917_a(axisalignedbb, (Entity) null);
    }

    // Paper start - Based on method below
    /**
     * @param axisalignedbb area to search within
     * @param entity causing the action ex. block placer
     * @return if there are no visible players colliding
     */
    public boolean checkNoVisiblePlayerCollisions(AxisAlignedBB axisalignedbb, @Nullable Entity entity) {
        List list = this.func_72839_b((Entity) null, axisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity instanceof EntityPlayerMP && entity1 instanceof EntityPlayerMP) {
                if (!((EntityPlayerMP) entity).getBukkitEntity().canSee(((EntityPlayerMP) entity1).getBukkitEntity())) {
                    continue;
                }
            }

            if (!entity1.field_70128_L && entity1.blocksEntitySpawning()) {
                return false;
            }
        }

        return true;
    }
    // Paper end

    public boolean func_72917_a(AxisAlignedBB axisalignedbb, @Nullable Entity entity) {
        List list = this.func_72839_b((Entity) null, axisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (!entity1.field_70128_L && entity1.field_70156_m && entity1 != entity && (entity == null || entity1.func_184223_x(entity))) {
                return false;
            }
        }

        return true;
    }

    public boolean func_72829_c(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.func_76128_c(axisalignedbb.field_72340_a);
        int j = MathHelper.func_76143_f(axisalignedbb.field_72336_d);
        int k = MathHelper.func_76128_c(axisalignedbb.field_72338_b);
        int l = MathHelper.func_76143_f(axisalignedbb.field_72337_e);
        int i1 = MathHelper.func_76128_c(axisalignedbb.field_72339_c);
        int j1 = MathHelper.func_76143_f(axisalignedbb.field_72334_f);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    IBlockState iblockdata = this.func_180495_p(blockposition_pooledblockposition.func_181079_c(k1, l1, i2));

                    if (iblockdata.func_185904_a() != Material.field_151579_a) {
                        blockposition_pooledblockposition.func_185344_t();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.func_185344_t();
        return false;
    }

    public boolean func_72953_d(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.func_76128_c(axisalignedbb.field_72340_a);
        int j = MathHelper.func_76143_f(axisalignedbb.field_72336_d);
        int k = MathHelper.func_76128_c(axisalignedbb.field_72338_b);
        int l = MathHelper.func_76143_f(axisalignedbb.field_72337_e);
        int i1 = MathHelper.func_76128_c(axisalignedbb.field_72339_c);
        int j1 = MathHelper.func_76143_f(axisalignedbb.field_72334_f);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    IBlockState iblockdata = this.func_180495_p(blockposition_pooledblockposition.func_181079_c(k1, l1, i2));

                    if (iblockdata.func_185904_a().func_76224_d()) {
                        blockposition_pooledblockposition.func_185344_t();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.func_185344_t();
        return false;
    }

    public boolean func_147470_e(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.func_76128_c(axisalignedbb.field_72340_a);
        int j = MathHelper.func_76143_f(axisalignedbb.field_72336_d);
        int k = MathHelper.func_76128_c(axisalignedbb.field_72338_b);
        int l = MathHelper.func_76143_f(axisalignedbb.field_72337_e);
        int i1 = MathHelper.func_76128_c(axisalignedbb.field_72339_c);
        int j1 = MathHelper.func_76143_f(axisalignedbb.field_72334_f);

        if (this.func_175663_a(i, k, i1, j, l, j1, true)) {
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();
            int k1 = i;

            while (true) {
                if (k1 >= j) {
                    blockposition_pooledblockposition.func_185344_t();
                    break;
                }

                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = this.func_180495_p(blockposition_pooledblockposition.func_181079_c(k1, l1, i2)).func_177230_c();

                        if (block == Blocks.field_150480_ab || block == Blocks.field_150356_k || block == Blocks.field_150353_l) {
                            blockposition_pooledblockposition.func_185344_t();
                            return true;
                        }
                    }
                }

                ++k1;
            }
        }

        return false;
    }

    public boolean func_72918_a(AxisAlignedBB axisalignedbb, Material material, Entity entity) {
        int i = MathHelper.func_76128_c(axisalignedbb.field_72340_a);
        int j = MathHelper.func_76143_f(axisalignedbb.field_72336_d);
        int k = MathHelper.func_76128_c(axisalignedbb.field_72338_b);
        int l = MathHelper.func_76143_f(axisalignedbb.field_72337_e);
        int i1 = MathHelper.func_76128_c(axisalignedbb.field_72339_c);
        int j1 = MathHelper.func_76143_f(axisalignedbb.field_72334_f);

        if (!this.func_175663_a(i, k, i1, j, l, j1, true)) {
            return false;
        } else {
            boolean flag = false;
            Vec3d vec3d = Vec3d.field_186680_a;
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.func_181079_c(k1, l1, i2);
                        IBlockState iblockdata = this.func_180495_p(blockposition_pooledblockposition);
                        Block block = iblockdata.func_177230_c();

                        if (iblockdata.func_185904_a() == material) {
                            double d0 = l1 + 1 - BlockLiquid.func_149801_b(iblockdata.func_177229_b(BlockLiquid.field_176367_b).intValue());

                            if (l >= d0) {
                                flag = true;
                                vec3d = block.func_176197_a(this, blockposition_pooledblockposition, entity, vec3d);
                            }
                        }
                    }
                }
            }

            blockposition_pooledblockposition.func_185344_t();
            if (vec3d.func_72433_c() > 0.0D && entity.func_96092_aw()) {
                vec3d = vec3d.func_72432_b();
                double d1 = 0.014D;

                entity.field_70159_w += vec3d.field_72450_a * 0.014D;
                entity.field_70181_x += vec3d.field_72448_b * 0.014D;
                entity.field_70179_y += vec3d.field_72449_c * 0.014D;
            }

            return flag;
        }
    }

    public boolean func_72875_a(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.func_76128_c(axisalignedbb.field_72340_a);
        int j = MathHelper.func_76143_f(axisalignedbb.field_72336_d);
        int k = MathHelper.func_76128_c(axisalignedbb.field_72338_b);
        int l = MathHelper.func_76143_f(axisalignedbb.field_72337_e);
        int i1 = MathHelper.func_76128_c(axisalignedbb.field_72339_c);
        int j1 = MathHelper.func_76143_f(axisalignedbb.field_72334_f);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    if (this.func_180495_p(blockposition_pooledblockposition.func_181079_c(k1, l1, i2)).func_185904_a() == material) {
                        blockposition_pooledblockposition.func_185344_t();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.func_185344_t();
        return false;
    }

    public Explosion func_72876_a(@Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        return this.func_72885_a(entity, d0, d1, d2, f, false, flag);
    }

    public Explosion func_72885_a(@Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f, flag, flag1);

        explosion.func_77278_a();
        explosion.func_77279_a(true);
        return explosion;
    }

    public float func_72842_a(Vec3d vec3d, AxisAlignedBB axisalignedbb) {
        double d0 = 1.0D / ((axisalignedbb.field_72336_d - axisalignedbb.field_72340_a) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.field_72337_e - axisalignedbb.field_72338_b) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.field_72334_f - axisalignedbb.field_72339_c) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
            int i = 0;
            int j = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) (f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) (f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) (f2 + d2)) {
                        double d5 = axisalignedbb.field_72340_a + (axisalignedbb.field_72336_d - axisalignedbb.field_72340_a) * f;
                        double d6 = axisalignedbb.field_72338_b + (axisalignedbb.field_72337_e - axisalignedbb.field_72338_b) * f1;
                        double d7 = axisalignedbb.field_72339_c + (axisalignedbb.field_72334_f - axisalignedbb.field_72339_c) * f2;

                        if (this.func_72933_a(new Vec3d(d5 + d3, d6, d7 + d4), vec3d) == null) {
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

    public boolean func_175719_a(@Nullable EntityPlayer entityhuman, BlockPos blockposition, EnumFacing enumdirection) {
        blockposition = blockposition.func_177972_a(enumdirection);
        if (this.func_180495_p(blockposition).func_177230_c() == Blocks.field_150480_ab) {
            this.func_180498_a(entityhuman, 1009, blockposition, 0);
            this.func_175698_g(blockposition);
            return true;
        } else {
            return false;
        }
    }

    public Map<BlockPos, TileEntity> capturedTileEntities = Maps.newHashMap();
    @Override
    @Nullable
    public TileEntity func_175625_s(BlockPos blockposition) {
        if (blockposition.isInvalidYLocation()) { // Paper
            return null;
        } else {
            // CraftBukkit start
            if (capturedTileEntities.containsKey(blockposition)) {
                return capturedTileEntities.get(blockposition);
            }
            // CraftBukkit end

            TileEntity tileentity = null;

            if (this.field_147481_N) {
                tileentity = this.func_189508_F(blockposition);
            }

            if (tileentity == null) {
                tileentity = this.func_175726_f(blockposition).func_177424_a(blockposition, Chunk.EnumCreateEntityType.IMMEDIATE);
            }

            if (tileentity == null) {
                tileentity = this.func_189508_F(blockposition);
            }

            return tileentity;
        }
    }

    @Nullable
    private TileEntity func_189508_F(BlockPos blockposition) {
        for (int i = 0; i < this.field_147484_a.size(); ++i) {
            TileEntity tileentity = this.field_147484_a.get(i);

            if (!tileentity.func_145837_r() && tileentity.func_174877_v().equals(blockposition)) {
                return tileentity;
            }
        }

        return null;
    }

    public void func_175690_a(BlockPos blockposition, @Nullable TileEntity tileentity) {
        if (!blockposition.isInvalidYLocation()) {
            if (tileentity != null && !tileentity.func_145837_r()) {
            // CraftBukkit start
            if (captureBlockStates) {
                tileentity.func_145834_a(this);
                tileentity.func_174878_a(blockposition);
                capturedTileEntities.put(blockposition, tileentity);
                return;
            }
            // CraftBukkit end
                if (this.field_147481_N) {
                    tileentity.func_174878_a(blockposition);
                    Iterator iterator = this.field_147484_a.iterator();

                    while (iterator.hasNext()) {
                        TileEntity tileentity1 = (TileEntity) iterator.next();

                        if (tileentity1.func_174877_v().equals(blockposition)) {
                            tileentity1.func_145843_s();
                            iterator.remove();
                        }
                    }

                    tileentity.func_145834_a(this); // Spigot - No null worlds
                    this.field_147484_a.add(tileentity);
                } else {
                    this.func_175726_f(blockposition).func_177426_a(blockposition, tileentity);
                    this.func_175700_a(tileentity);
                }
            }

        }
    }

    public void func_175713_t(BlockPos blockposition) {
        TileEntity tileentity = this.func_175625_s(blockposition);

        if (tileentity != null && this.field_147481_N) {
            tileentity.func_145843_s();
            this.field_147484_a.remove(tileentity);
        } else {
            if (tileentity != null) {
                this.field_147484_a.remove(tileentity);
                //this.tileEntityList.remove(tileentity); // Paper - remove unused list
                this.field_175730_i.remove(tileentity);
            }

            this.func_175726_f(blockposition).func_177425_e(blockposition);
        }

    }

    public void func_147457_a(TileEntity tileentity) {
        this.field_147483_b.add(tileentity);
    }

    public boolean func_175665_u(BlockPos blockposition) {
        AxisAlignedBB axisalignedbb = this.func_180495_p(blockposition).func_185890_d(this, blockposition);

        return axisalignedbb != Block.field_185506_k && axisalignedbb.func_72320_b() >= 1.0D;
    }

    public boolean func_175677_d(BlockPos blockposition, boolean flag) {
        if (blockposition.isInvalidYLocation()) { // Paper
            return false;
        } else {
            Chunk chunk = this.field_73020_y.func_186026_b(blockposition.func_177958_n() >> 4, blockposition.func_177952_p() >> 4);

            if (chunk != null && !chunk.func_76621_g()) {
                IBlockState iblockdata = this.func_180495_p(blockposition);

                return iblockdata.func_185904_a().func_76218_k() && iblockdata.func_185917_h();
            } else {
                return flag;
            }
        }
    }

    public void func_72966_v() {
        int i = this.func_72967_a(1.0F);

        if (i != this.field_73008_k) {
            this.field_73008_k = i;
        }

    }

    public void func_72891_a(boolean flag, boolean flag1) {
        this.field_72985_G = flag;
        this.field_72992_H = flag1;
    }

    public void func_72835_b() {
        this.func_72979_l();
    }

    protected void func_72947_a() {
        if (this.field_72986_A.func_76059_o()) {
            this.field_73004_o = 1.0F;
            if (this.field_72986_A.func_76061_m()) {
                this.field_73017_q = 1.0F;
            }
        }

    }

    protected void func_72979_l() {
        if (this.field_73011_w.func_191066_m()) {
            if (!this.field_72995_K) {
                boolean flag = this.func_82736_K().func_82766_b("doWeatherCycle");

                if (flag) {
                    int i = this.field_72986_A.func_176133_A();

                    if (i > 0) {
                        --i;
                        this.field_72986_A.func_176142_i(i);
                        this.field_72986_A.func_76090_f(this.field_72986_A.func_76061_m() ? 1 : 2);
                        this.field_72986_A.func_76080_g(this.field_72986_A.func_76059_o() ? 1 : 2);
                    }

                    int j = this.field_72986_A.func_76071_n();

                    if (j <= 0) {
                        if (this.field_72986_A.func_76061_m()) {
                            this.field_72986_A.func_76090_f(this.field_73012_v.nextInt(12000) + 3600);
                        } else {
                            this.field_72986_A.func_76090_f(this.field_73012_v.nextInt(168000) + 12000);
                        }
                    } else {
                        --j;
                        this.field_72986_A.func_76090_f(j);
                        if (j <= 0) {
                            this.field_72986_A.func_76069_a(!this.field_72986_A.func_76061_m());
                        }
                    }

                    int k = this.field_72986_A.func_76083_p();

                    if (k <= 0) {
                        if (this.field_72986_A.func_76059_o()) {
                            this.field_72986_A.func_76080_g(this.field_73012_v.nextInt(12000) + 12000);
                        } else {
                            this.field_72986_A.func_76080_g(this.field_73012_v.nextInt(168000) + 12000);
                        }
                    } else {
                        --k;
                        this.field_72986_A.func_76080_g(k);
                        if (k <= 0) {
                            this.field_72986_A.func_76084_b(!this.field_72986_A.func_76059_o());
                        }
                    }
                }

                this.field_73018_p = this.field_73017_q;
                if (this.field_72986_A.func_76061_m()) {
                    this.field_73017_q = (float) (this.field_73017_q + 0.01D);
                } else {
                    this.field_73017_q = (float) (this.field_73017_q - 0.01D);
                }

                this.field_73017_q = MathHelper.func_76131_a(this.field_73017_q, 0.0F, 1.0F);
                this.field_73003_n = this.field_73004_o;
                if (this.field_72986_A.func_76059_o()) {
                    this.field_73004_o = (float) (this.field_73004_o + 0.01D);
                } else {
                    this.field_73004_o = (float) (this.field_73004_o - 0.01D);
                }

                this.field_73004_o = MathHelper.func_76131_a(this.field_73004_o, 0.0F, 1.0F);

                // CraftBukkit start
                for (int idx = 0; idx < this.field_73010_i.size(); ++idx) {
                    if (((EntityPlayerMP) this.field_73010_i.get(idx)).field_70170_p == this) {
                        ((EntityPlayerMP) this.field_73010_i.get(idx)).tickWeather();
                    }
                }
                // CraftBukkit end
            }
        }
    }

    protected void func_147456_g() {}

    public void func_189507_a(BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.field_72999_e = true;
        iblockdata.func_177230_c().func_180650_b(this, blockposition, iblockdata, random);
        this.field_72999_e = false;
    }

    public boolean func_175675_v(BlockPos blockposition) {
        return this.func_175670_e(blockposition, false);
    }

    public boolean func_175662_w(BlockPos blockposition) {
        return this.func_175670_e(blockposition, true);
    }

    public boolean func_175670_e(BlockPos blockposition, boolean flag) {
        Biome biomebase = this.func_180494_b(blockposition);
        float f = biomebase.func_180626_a(blockposition);

        if (f >= 0.15F) {
            return false;
        } else {
            if (blockposition.func_177956_o() >= 0 && blockposition.func_177956_o() < 256 && this.func_175642_b(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockState iblockdata = this.func_180495_p(blockposition);
                Block block = iblockdata.func_177230_c();

                if ((block == Blocks.field_150355_j || block == Blocks.field_150358_i) && iblockdata.func_177229_b(BlockLiquid.field_176367_b).intValue() == 0) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = this.func_175696_F(blockposition.func_177976_e()) && this.func_175696_F(blockposition.func_177974_f()) && this.func_175696_F(blockposition.func_177978_c()) && this.func_175696_F(blockposition.func_177968_d());

                    if (!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private boolean func_175696_F(BlockPos blockposition) {
        return this.func_180495_p(blockposition).func_185904_a() == Material.field_151586_h;
    }

    public boolean func_175708_f(BlockPos blockposition, boolean flag) {
        Biome biomebase = this.func_180494_b(blockposition);
        float f = biomebase.func_180626_a(blockposition);

        if (f >= 0.15F) {
            return false;
        } else if (!flag) {
            return true;
        } else {
            if (blockposition.func_177956_o() >= 0 && blockposition.func_177956_o() < 256 && this.func_175642_b(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockState iblockdata = this.func_180495_p(blockposition);

                if (iblockdata.func_185904_a() == Material.field_151579_a && Blocks.field_150431_aC.func_176196_c(this, blockposition)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean func_175664_x(BlockPos blockposition) {
        boolean flag = false;

        if (this.field_73011_w.func_191066_m()) {
            flag |= this.func_180500_c(EnumSkyBlock.SKY, blockposition);
        }

        flag |= this.func_180500_c(EnumSkyBlock.BLOCK, blockposition);
        return flag;
    }

    private int func_175638_a(BlockPos blockposition, EnumSkyBlock enumskyblock) {
        if (enumskyblock == EnumSkyBlock.SKY && this.func_175678_i(blockposition)) {
            return 15;
        } else {
            IBlockState iblockdata = this.func_180495_p(blockposition);
            int i = enumskyblock == EnumSkyBlock.SKY ? 0 : iblockdata.func_185906_d();
            int j = iblockdata.func_185891_c();

            if (j >= 15 && iblockdata.func_185906_d() > 0) {
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
                BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

                try {
                    EnumFacing[] aenumdirection = EnumFacing.values();
                    int k = aenumdirection.length;

                    for (int l = 0; l < k; ++l) {
                        EnumFacing enumdirection = aenumdirection[l];

                        blockposition_pooledblockposition.func_189533_g(blockposition).func_189536_c(enumdirection);
                        int i1 = this.func_175642_b(enumskyblock, blockposition_pooledblockposition) - j;

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
                    blockposition_pooledblockposition.func_185344_t();
                }
            }
        }
    }

    public boolean func_180500_c(EnumSkyBlock enumskyblock, BlockPos blockposition) {
        // CraftBukkit start - Use neighbor cache instead of looking up
        Chunk chunk = this.getChunkIfLoaded(blockposition.func_177958_n() >> 4, blockposition.func_177952_p() >> 4);
        if (chunk == null || !chunk.areNeighborsLoaded(1) /*!this.areChunksLoaded(blockposition, 17, false)*/) {
            // CraftBukkit end
            return false;
        } else {
            int i = 0;
            int j = 0;

            this.field_72984_F.func_76320_a("getBrightness");
            int k = this.func_175642_b(enumskyblock, blockposition);
            int l = this.func_175638_a(blockposition, enumskyblock);
            int i1 = blockposition.func_177958_n();
            int j1 = blockposition.func_177956_o();
            int k1 = blockposition.func_177952_p();
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;

            if (l > k) {
                this.field_72994_J[j++] = 133152;
            } else if (l < k) {
                this.field_72994_J[j++] = 133152 | k << 18;

                while (i < j) {
                    l1 = this.field_72994_J[i++];
                    i2 = (l1 & 63) - 32 + i1;
                    j2 = (l1 >> 6 & 63) - 32 + j1;
                    k2 = (l1 >> 12 & 63) - 32 + k1;
                    int l3 = l1 >> 18 & 15;
                    BlockPos blockposition1 = new BlockPos(i2, j2, k2);

                    l2 = this.func_175642_b(enumskyblock, blockposition1);
                    if (l2 == l3) {
                        this.func_175653_a(enumskyblock, blockposition1, 0);
                        if (l3 > 0) {
                            i3 = MathHelper.func_76130_a(i2 - i1);
                            j3 = MathHelper.func_76130_a(j2 - j1);
                            k3 = MathHelper.func_76130_a(k2 - k1);
                            if (i3 + j3 + k3 < 17) {
                                BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();
                                EnumFacing[] aenumdirection = EnumFacing.values();
                                int i4 = aenumdirection.length;

                                for (int j4 = 0; j4 < i4; ++j4) {
                                    EnumFacing enumdirection = aenumdirection[j4];
                                    int k4 = i2 + enumdirection.func_82601_c();
                                    int l4 = j2 + enumdirection.func_96559_d();
                                    int i5 = k2 + enumdirection.func_82599_e();

                                    blockposition_pooledblockposition.func_181079_c(k4, l4, i5);
                                    int j5 = Math.max(1, this.func_180495_p(blockposition_pooledblockposition).func_185891_c());

                                    l2 = this.func_175642_b(enumskyblock, blockposition_pooledblockposition);
                                    if (l2 == l3 - j5 && j < this.field_72994_J.length) {
                                        this.field_72994_J[j++] = k4 - i1 + 32 | l4 - j1 + 32 << 6 | i5 - k1 + 32 << 12 | l3 - j5 << 18;
                                    }
                                }

                                blockposition_pooledblockposition.func_185344_t();
                            }
                        }
                    }
                }

                i = 0;
            }

            this.field_72984_F.func_76319_b();
            this.field_72984_F.func_76320_a("checkedPosition < toCheckCount");

            while (i < j) {
                l1 = this.field_72994_J[i++];
                i2 = (l1 & 63) - 32 + i1;
                j2 = (l1 >> 6 & 63) - 32 + j1;
                k2 = (l1 >> 12 & 63) - 32 + k1;
                BlockPos blockposition2 = new BlockPos(i2, j2, k2);
                int k5 = this.func_175642_b(enumskyblock, blockposition2);

                l2 = this.func_175638_a(blockposition2, enumskyblock);
                if (l2 != k5) {
                    this.func_175653_a(enumskyblock, blockposition2, l2);
                    if (l2 > k5) {
                        i3 = Math.abs(i2 - i1);
                        j3 = Math.abs(j2 - j1);
                        k3 = Math.abs(k2 - k1);
                        boolean flag = j < this.field_72994_J.length - 6;

                        if (i3 + j3 + k3 < 17 && flag) {
                            if (this.func_175642_b(enumskyblock, blockposition2.func_177976_e()) < l2) {
                                this.field_72994_J[j++] = i2 - 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.func_175642_b(enumskyblock, blockposition2.func_177974_f()) < l2) {
                                this.field_72994_J[j++] = i2 + 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.func_175642_b(enumskyblock, blockposition2.func_177977_b()) < l2) {
                                this.field_72994_J[j++] = i2 - i1 + 32 + (j2 - 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.func_175642_b(enumskyblock, blockposition2.func_177984_a()) < l2) {
                                this.field_72994_J[j++] = i2 - i1 + 32 + (j2 + 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if (this.func_175642_b(enumskyblock, blockposition2.func_177978_c()) < l2) {
                                this.field_72994_J[j++] = i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - 1 - k1 + 32 << 12);
                            }

                            if (this.func_175642_b(enumskyblock, blockposition2.func_177968_d()) < l2) {
                                this.field_72994_J[j++] = i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 + 1 - k1 + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.field_72984_F.func_76319_b();
            return true;
        }
    }

    public boolean func_72955_a(boolean flag) {
        return false;
    }

    @Nullable
    public List<NextTickListEntry> func_72920_a(Chunk chunk, boolean flag) {
        return null;
    }

    @Nullable
    public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureboundingbox, boolean flag) {
        return null;
    }

    public List<Entity> func_72839_b(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
        return this.func_175674_a(entity, axisalignedbb, EntitySelectors.field_180132_d);
    }

    public List<Entity> func_175674_a(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super Entity> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        int i = MathHelper.func_76128_c((axisalignedbb.field_72340_a - 2.0D) / 16.0D);
        int j = MathHelper.func_76128_c((axisalignedbb.field_72336_d + 2.0D) / 16.0D);
        int k = MathHelper.func_76128_c((axisalignedbb.field_72339_c - 2.0D) / 16.0D);
        int l = MathHelper.func_76128_c((axisalignedbb.field_72334_f + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.func_175680_a(i1, j1, true)) {
                    this.func_72964_e(i1, j1).func_177414_a(entity, axisalignedbb, arraylist, predicate);
                }
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> func_175644_a(Class<? extends T> oclass, Predicate<? super T> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_72996_f.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (oclass.isAssignableFrom(entity.getClass()) && predicate.apply((T) entity)) {
                arraylist.add(entity);
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> func_175661_b(Class<? extends T> oclass, Predicate<? super T> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_73010_i.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (oclass.isAssignableFrom(entity.getClass()) && predicate.apply((T) entity)) { // CraftBukkit - fix decompile error
                arraylist.add(entity);
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> func_72872_a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb) {
        return this.func_175647_a(oclass, axisalignedbb, EntitySelectors.field_180132_d);
    }

    public <T extends Entity> List<T> func_175647_a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate) {
        int i = MathHelper.func_76128_c((axisalignedbb.field_72340_a - 2.0D) / 16.0D);
        int j = MathHelper.func_76143_f((axisalignedbb.field_72336_d + 2.0D) / 16.0D);
        int k = MathHelper.func_76128_c((axisalignedbb.field_72339_c - 2.0D) / 16.0D);
        int l = MathHelper.func_76143_f((axisalignedbb.field_72334_f + 2.0D) / 16.0D);
        ArrayList arraylist = Lists.newArrayList();

        for (int i1 = i; i1 < j; ++i1) {
            for (int j1 = k; j1 < l; ++j1) {
                if (this.func_175680_a(i1, j1, true)) {
                    this.func_72964_e(i1, j1).func_177430_a(oclass, axisalignedbb, arraylist, predicate);
                }
            }
        }

        return arraylist;
    }

    @Nullable
    public <T extends Entity> T func_72857_a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, T t0) {
        List list = this.func_72872_a(oclass, axisalignedbb);
        Entity entity = null;
        double d0 = Double.MAX_VALUE;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1 != t0 && EntitySelectors.field_180132_d.apply(entity1)) {
                double d1 = t0.func_70068_e(entity1);

                if (d1 <= d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        return (T) entity; // CraftBukkit fix decompile error
    }

    @Nullable
    public Entity func_73045_a(int i) {
        return this.field_175729_l.func_76041_a(i);
    }

    public void func_175646_b(BlockPos blockposition, TileEntity tileentity) {
        if (this.func_175667_e(blockposition)) {
            this.func_175726_f(blockposition).func_76630_e();
        }

    }

    public int func_72907_a(Class<?> oclass) {
        int i = 0;
        Iterator iterator = this.field_72996_f.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            // CraftBukkit start - Split out persistent check, don't apply it to special persistent mobs
            if (entity instanceof EntityLiving) {
                EntityLiving entityinsentient = (EntityLiving) entity;
                if (entityinsentient.func_70692_ba() && entityinsentient.func_104002_bU()) {
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

    public void func_175650_b(Collection<Entity> collection) {
        org.spigotmc.AsyncCatcher.catchOp( "entity world add"); // Spigot
        // CraftBukkit start
        // this.entityList.addAll(collection);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity == null) {
                continue;
            }
            this.field_72996_f.add(entity);
            // CraftBukkit end
            this.func_72923_a(entity);
        }

    }

    public void func_175681_c(Collection<Entity> collection) {
        this.field_72997_g.addAll(collection);
    }

    public boolean func_190527_a(Block block, BlockPos blockposition, boolean flag, EnumFacing enumdirection, @Nullable Entity entity) {
        IBlockState iblockdata = this.func_180495_p(blockposition);
        AxisAlignedBB axisalignedbb = flag ? null : block.func_176223_P().func_185890_d(this, blockposition);

        // CraftBukkit start - store default return
        boolean defaultReturn = axisalignedbb != Block.field_185506_k && !this.checkNoVisiblePlayerCollisions(axisalignedbb.func_186670_a(blockposition), entity) ? false : (iblockdata.func_185904_a() == Material.field_151594_q && block == Blocks.field_150467_bQ ? true : iblockdata.func_185904_a().func_76222_j() && block.func_176198_a(this, blockposition, enumdirection)); // Paper - Use our entity search
        BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), CraftMagicNumbers.getId(block), defaultReturn);
        this.getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }

    public int func_181545_F() {
        return this.field_181546_a;
    }

    public void func_181544_b(int i) {
        this.field_181546_a = i;
    }

    @Override
    public int func_175627_a(BlockPos blockposition, EnumFacing enumdirection) {
        return this.func_180495_p(blockposition).func_185893_b(this, blockposition, enumdirection);
    }

    public WorldType func_175624_G() {
        return this.field_72986_A.func_76067_t();
    }

    public int func_175676_y(BlockPos blockposition) {
        byte b0 = 0;
        int i = Math.max(b0, this.func_175627_a(blockposition.func_177977_b(), EnumFacing.DOWN));

        if (i >= 15) {
            return i;
        } else {
            i = Math.max(i, this.func_175627_a(blockposition.func_177984_a(), EnumFacing.UP));
            if (i >= 15) {
                return i;
            } else {
                i = Math.max(i, this.func_175627_a(blockposition.func_177978_c(), EnumFacing.NORTH));
                if (i >= 15) {
                    return i;
                } else {
                    i = Math.max(i, this.func_175627_a(blockposition.func_177968_d(), EnumFacing.SOUTH));
                    if (i >= 15) {
                        return i;
                    } else {
                        i = Math.max(i, this.func_175627_a(blockposition.func_177976_e(), EnumFacing.WEST));
                        if (i >= 15) {
                            return i;
                        } else {
                            i = Math.max(i, this.func_175627_a(blockposition.func_177974_f(), EnumFacing.EAST));
                            return i >= 15 ? i : i;
                        }
                    }
                }
            }
        }
    }

    public boolean func_175709_b(BlockPos blockposition, EnumFacing enumdirection) {
        return this.func_175651_c(blockposition, enumdirection) > 0;
    }

    public int func_175651_c(BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = this.func_180495_p(blockposition);

        return iblockdata.func_185915_l() ? this.func_175676_y(blockposition) : iblockdata.func_185911_a(this, blockposition, enumdirection);
    }

    public boolean func_175640_z(BlockPos blockposition) {
        return this.func_175651_c(blockposition.func_177977_b(), EnumFacing.DOWN) > 0 ? true : (this.func_175651_c(blockposition.func_177984_a(), EnumFacing.UP) > 0 ? true : (this.func_175651_c(blockposition.func_177978_c(), EnumFacing.NORTH) > 0 ? true : (this.func_175651_c(blockposition.func_177968_d(), EnumFacing.SOUTH) > 0 ? true : (this.func_175651_c(blockposition.func_177976_e(), EnumFacing.WEST) > 0 ? true : this.func_175651_c(blockposition.func_177974_f(), EnumFacing.EAST) > 0))));
    }

    public int func_175687_A(BlockPos blockposition) {
        int i = 0;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumFacing enumdirection = aenumdirection[k];
            int l = this.func_175651_c(blockposition.func_177972_a(enumdirection), enumdirection);

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
    public EntityPlayer func_72890_a(Entity entity, double d0) {
        return this.func_184137_a(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, d0, false);
    }

    @Nullable
    public EntityPlayer func_184136_b(Entity entity, double d0) {
        return this.func_184137_a(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, d0, true);
    }

    @Nullable
    public EntityPlayer func_184137_a(double d0, double d1, double d2, double d3, boolean flag) {
        Predicate predicate = flag ? EntitySelectors.field_188444_d : EntitySelectors.field_180132_d;

        return this.func_190525_a(d0, d1, d2, d3, predicate);
    }

    @Nullable
    public EntityPlayer func_190525_a(double d0, double d1, double d2, double d3, Predicate<Entity> predicate) {
        double d4 = -1.0D;
        EntityPlayer entityhuman = null;

        for (int i = 0; i < this.field_73010_i.size(); ++i) {
            EntityPlayer entityhuman1 = this.field_73010_i.get(i);
            // CraftBukkit start - Fixed an NPE
            if (entityhuman1 == null || entityhuman1.field_70128_L) {
                continue;
            }
            // CraftBukkit end

            if (predicate.apply(entityhuman1)) {
                double d5 = entityhuman1.func_70092_e(d0, d1, d2);

                if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    public boolean func_175636_b(double d0, double d1, double d2, double d3) {
        for (int i = 0; i < this.field_73010_i.size(); ++i) {
            EntityPlayer entityhuman = this.field_73010_i.get(i);

            if (EntitySelectors.field_180132_d.apply(entityhuman) && entityhuman.affectsSpawning) { // Paper - Affects Spawning API
                double d4 = entityhuman.func_70092_e(d0, d1, d2);

                if (d3 < 0.0D || d4 < d3 * d3) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    public EntityPlayer func_184142_a(Entity entity, double d0, double d1) {
        return this.func_184150_a(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, d0, d1, (Function) null, (Predicate) null);
    }

    @Nullable
    public EntityPlayer func_184139_a(BlockPos blockposition, double d0, double d1) {
        return this.func_184150_a(blockposition.func_177958_n() + 0.5F, blockposition.func_177956_o() + 0.5F, blockposition.func_177952_p() + 0.5F, d0, d1, (Function) null, (Predicate) null);
    }

    @Nullable
    public EntityPlayer func_184150_a(double d0, double d1, double d2, double d3, double d4, @Nullable Function<EntityPlayer, Double> function, @Nullable Predicate<EntityPlayer> predicate) {
        double d5 = -1.0D;
        EntityPlayer entityhuman = null;

        for (int i = 0; i < this.field_73010_i.size(); ++i) {
            EntityPlayer entityhuman1 = this.field_73010_i.get(i);

            if (!entityhuman1.field_71075_bZ.field_75102_a && entityhuman1.func_70089_S() && !entityhuman1.func_175149_v() && (predicate == null || predicate.apply(entityhuman1))) {
                double d6 = entityhuman1.func_70092_e(d0, entityhuman1.field_70163_u, d2);
                double d7 = d3;

                if (entityhuman1.func_70093_af()) {
                    d7 = d3 * 0.800000011920929D;
                }

                if (entityhuman1.func_82150_aj()) {
                    float f = entityhuman1.func_82243_bO();

                    if (f < 0.1F) {
                        f = 0.1F;
                    }

                    d7 *= 0.7F * f;
                }

                if (function != null) {
                    d7 *= MoreObjects.firstNonNull(function.apply(entityhuman1), Double.valueOf(1.0D)).doubleValue();
                }

                if ((d4 < 0.0D || Math.abs(entityhuman1.field_70163_u - d1) < d4 * d4) && (d3 < 0.0D || d6 < d7 * d7) && (d5 == -1.0D || d6 < d5)) {
                    d5 = d6;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    @Nullable
    public EntityPlayer func_72924_a(String s) {
        for (int i = 0; i < this.field_73010_i.size(); ++i) {
            EntityPlayer entityhuman = this.field_73010_i.get(i);

            if (s.equals(entityhuman.func_70005_c_())) {
                return entityhuman;
            }
        }

        return null;
    }

    @Nullable
    public EntityPlayer func_152378_a(UUID uuid) {
        for (int i = 0; i < this.field_73010_i.size(); ++i) {
            EntityPlayer entityhuman = this.field_73010_i.get(i);

            if (uuid.equals(entityhuman.func_110124_au())) {
                return entityhuman;
            }
        }

        return null;
    }

    public void func_72906_B() throws MinecraftException {
        this.field_73019_z.func_75762_c();
    }

    public long func_72905_C() {
        return this.field_72986_A.func_76063_b();
    }

    public long func_82737_E() {
        return this.field_72986_A.func_82573_f();
    }

    public long func_72820_D() {
        return this.field_72986_A.func_76073_f();
    }

    public void func_72877_b(long i) {
        this.field_72986_A.func_76068_b(i);
    }

    public BlockPos func_175694_M() {
        BlockPos blockposition = new BlockPos(this.field_72986_A.func_76079_c(), this.field_72986_A.func_76075_d(), this.field_72986_A.func_76074_e());

        if (!this.func_175723_af().func_177746_a(blockposition)) {
            blockposition = this.func_175645_m(new BlockPos(this.func_175723_af().func_177731_f(), 0.0D, this.func_175723_af().func_177721_g()));
        }

        return blockposition;
    }

    public void func_175652_B(BlockPos blockposition) {
        this.field_72986_A.func_176143_a(blockposition);
    }

    public boolean func_175660_a(EntityPlayer entityhuman, BlockPos blockposition) {
        return true;
    }

    public void func_72960_a(Entity entity, byte b0) {}

    public IChunkProvider func_72863_F() {
        return this.field_73020_y;
    }

    public void func_175641_c(BlockPos blockposition, Block block, int i, int j) {
        this.func_180495_p(blockposition).func_189547_a(this, blockposition, i, j);
    }

    public ISaveHandler func_72860_G() {
        return this.field_73019_z;
    }

    public WorldInfo func_72912_H() {
        return this.field_72986_A;
    }

    public GameRules func_82736_K() {
        return this.field_72986_A.func_82574_x();
    }

    public void func_72854_c() {}

    // CraftBukkit start
    // Calls the method that checks to see if players are sleeping
    // Called by CraftPlayer.setPermanentSleeping()
    public void checkSleepStatus() {
        if (!this.field_72995_K) {
            this.func_72854_c();
        }
    }
    // CraftBukkit end

    public float func_72819_i(float f) {
        return (this.field_73018_p + (this.field_73017_q - this.field_73018_p) * f) * this.func_72867_j(f);
    }

    public float func_72867_j(float f) {
        return this.field_73003_n + (this.field_73004_o - this.field_73003_n) * f;
    }

    public boolean func_72911_I() {
        return this.func_72819_i(1.0F) > 0.9D;
    }

    public boolean func_72896_J() {
        return this.func_72867_j(1.0F) > 0.2D;
    }

    public boolean func_175727_C(BlockPos blockposition) {
        if (!this.func_72896_J()) {
            return false;
        } else if (!this.func_175678_i(blockposition)) {
            return false;
        } else if (this.func_175725_q(blockposition).func_177956_o() > blockposition.func_177956_o()) {
            return false;
        } else {
            Biome biomebase = this.func_180494_b(blockposition);

            return biomebase.func_76746_c() ? false : (this.func_175708_f(blockposition, false) ? false : biomebase.func_76738_d());
        }
    }

    public boolean func_180502_D(BlockPos blockposition) {
        Biome biomebase = this.func_180494_b(blockposition);

        return biomebase.func_76736_e();
    }

    @Nullable
    public MapStorage func_175693_T() {
        return this.field_72988_C;
    }

    public void func_72823_a(String s, WorldSavedData persistentbase) {
        this.field_72988_C.func_75745_a(s, persistentbase);
    }

    @Nullable
    public WorldSavedData func_72943_a(Class<? extends WorldSavedData> oclass, String s) {
        return this.field_72988_C.func_75742_a(oclass, s);
    }

    public int func_72841_b(String s) {
        return this.field_72988_C.func_75743_a(s);
    }

    public void func_175669_a(int i, BlockPos blockposition, int j) {
        for (int k = 0; k < this.field_73021_x.size(); ++k) {
            this.field_73021_x.get(k).func_180440_a(i, blockposition, j);
        }

    }

    public void func_175718_b(int i, BlockPos blockposition, int j) {
        this.func_180498_a((EntityPlayer) null, i, blockposition, j);
    }

    public void func_180498_a(@Nullable EntityPlayer entityhuman, int i, BlockPos blockposition, int j) {
        try {
            for (int k = 0; k < this.field_73021_x.size(); ++k) {
                this.field_73021_x.get(k).func_180439_a(entityhuman, i, blockposition, j);
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Playing level event");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Level event being played");

            crashreportsystemdetails.func_71507_a("Block coordinates", CrashReportCategory.func_180522_a(blockposition));
            crashreportsystemdetails.func_71507_a("Event source", entityhuman);
            crashreportsystemdetails.func_71507_a("Event type", Integer.valueOf(i));
            crashreportsystemdetails.func_71507_a("Event data", Integer.valueOf(j));
            throw new ReportedException(crashreport);
        }
    }

    public int func_72800_K() {
        return 256;
    }

    public int func_72940_L() {
        return this.field_73011_w.func_177495_o() ? 128 : 256;
    }

    public Random func_72843_D(int i, int j, int k) {
        long l = i * 341873128712L + j * 132897987541L + this.func_72912_H().func_76063_b() + k;

        this.field_73012_v.setSeed(l);
        return this.field_73012_v;
    }

    public CrashReportCategory func_72914_a(CrashReport crashreport) {
        CrashReportCategory crashreportsystemdetails = crashreport.func_85057_a("Affected level", 1);

        crashreportsystemdetails.func_71507_a("Level name", this.field_72986_A == null ? "????" : this.field_72986_A.func_76065_j());
        crashreportsystemdetails.func_189529_a("All players", new ICrashReportDetail() {
            public String a() {
                return World.this.field_73010_i.size() + " total; " + World.this.field_73010_i;
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Chunk stats", new ICrashReportDetail() {
            public String a() {
                return World.this.field_73020_y.func_73148_d();
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });

        try {
            this.field_72986_A.func_85118_a(crashreportsystemdetails);
        } catch (Throwable throwable) {
            crashreportsystemdetails.func_71499_a("Level Data Unobtainable", throwable);
        }

        return crashreportsystemdetails;
    }

    public void func_175715_c(int i, BlockPos blockposition, int j) {
        for (int k = 0; k < this.field_73021_x.size(); ++k) {
            IWorldEventListener iworldaccess = this.field_73021_x.get(k);

            iworldaccess.func_180441_b(i, blockposition, j);
        }

    }

    public Calendar func_83015_S() {
        if (this.func_82737_E() % 600L == 0L) {
            this.field_83016_L.setTimeInMillis(MinecraftServer.func_130071_aq());
        }

        return this.field_83016_L;
    }

    public Scoreboard func_96441_U() {
        return this.field_96442_D;
    }

    public void func_175666_e(BlockPos blockposition, Block block) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);

            if (this.func_175667_e(blockposition1)) {
                IBlockState iblockdata = this.func_180495_p(blockposition1);

                if (Blocks.field_150441_bU.func_185547_C(iblockdata)) {
                    iblockdata.func_189546_a(this, blockposition1, block, blockposition);
                } else if (iblockdata.func_185915_l()) {
                    blockposition1 = blockposition1.func_177972_a(enumdirection);
                    iblockdata = this.func_180495_p(blockposition1);
                    if (Blocks.field_150441_bU.func_185547_C(iblockdata)) {
                        iblockdata.func_189546_a(this, blockposition1, block, blockposition);
                    }
                }
            }
        }

    }

    public DifficultyInstance func_175649_E(BlockPos blockposition) {
        long i = 0L;
        float f = 0.0F;

        if (this.func_175667_e(blockposition)) {
            f = this.func_130001_d();
            i = this.func_175726_f(blockposition).func_177416_w();
        }

        return new DifficultyInstance(this.func_175659_aa(), this.func_72820_D(), i, f);
    }

    public EnumDifficulty func_175659_aa() {
        return this.func_72912_H().func_176130_y();
    }

    public int func_175657_ab() {
        return this.field_73008_k;
    }

    public void func_175692_b(int i) {
        this.field_73008_k = i;
    }

    public void func_175702_c(int i) {
        this.field_73016_r = i;
    }

    public VillageCollection func_175714_ae() {
        return this.field_72982_D;
    }

    public WorldBorder func_175723_af() {
        return this.field_175728_M;
    }

    public boolean shouldStayLoaded(int i,  int j) { return func_72916_c(i, j); } // Paper - OBFHELPER
    public boolean func_72916_c(int i, int j) {
        BlockPos blockposition = this.func_175694_M();
        int k = i * 16 + 8 - blockposition.func_177958_n();
        int l = j * 16 + 8 - blockposition.func_177952_p();
        boolean flag = true;
        short keepLoadedRange = paperConfig.keepLoadedRange; // Paper

        return k >= -keepLoadedRange && k <= keepLoadedRange && l >= -keepLoadedRange && l <= keepLoadedRange && this.keepSpawnInMemory; // CraftBukkit - Added 'this.keepSpawnInMemory' // Paper - Re-add range var
    }

    public void func_184135_a(Packet<?> packet) {
        throw new UnsupportedOperationException("Can\'t send packets to server unless you\'re on the client.");
    }

    public LootTableManager func_184146_ak() {
        return this.field_184151_B;
    }

    @Nullable
    public BlockPos func_190528_a(String s, BlockPos blockposition, boolean flag) {
        return null;
    }
}

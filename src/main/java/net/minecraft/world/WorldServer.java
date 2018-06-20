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

    private static final Logger field_147491_a = LogManager.getLogger();
    boolean stopPhysicsEvent = false; // Paper
    private final MinecraftServer field_73061_a;
    public EntityTracker field_73062_L;
    private final PlayerChunkMap field_73063_M;
    // private final Set<NextTickListEntry> nextTickListHash = Sets.newHashSet();
    private final HashTreeSet<NextTickListEntry> field_73065_O = new HashTreeSet<NextTickListEntry>(); // CraftBukkit - HashTreeSet
    private final Map<UUID, Entity> field_175741_N = Maps.newHashMap();
    public boolean field_73058_d;
    private boolean field_73068_P;
    private int field_80004_Q;
    private final Teleporter field_85177_Q;
    private final WorldEntitySpawner field_175742_R = new WorldEntitySpawner();
    protected final VillageSiege field_175740_d = new VillageSiege(this);
    private final WorldServer.ServerBlockEventList[] field_147490_S = new WorldServer.ServerBlockEventList[] { new WorldServer.ServerBlockEventList(null), new WorldServer.ServerBlockEventList(null)};
    private int field_147489_T;
    private final List<NextTickListEntry> field_94579_S = Lists.newArrayList();

    // CraftBukkit start
    public final int dimension;

    // Add env and gen to constructor
    public WorldServer(MinecraftServer minecraftserver, ISaveHandler idatamanager, WorldInfo worlddata, int i, Profiler methodprofiler, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(idatamanager, worlddata, DimensionType.func_186069_a(env.getId()).func_186070_d(), methodprofiler, false, gen, env);
        this.dimension = i;
        this.pvpMode = minecraftserver.func_71219_W();
        worlddata.world = this;
        // CraftBukkit end
        this.field_73061_a = minecraftserver;
        this.field_73062_L = new EntityTracker(this);
        this.field_73063_M = new PlayerChunkMap(this);
        this.field_73011_w.func_76558_a(this);
        this.field_73020_y = this.func_72970_h();
        this.field_85177_Q = new org.bukkit.craftbukkit.CraftTravelAgent(this); // CraftBukkit
        this.func_72966_v();
        this.func_72947_a();
        this.func_175723_af().func_177725_a(minecraftserver.func_175580_aG());
    }

    @Override
    public World func_175643_b() {
        this.field_72988_C = new MapStorage(this.field_73019_z);
        String s = VillageCollection.func_176062_a(this.field_73011_w);
        VillageCollection persistentvillage = (VillageCollection) this.field_72988_C.func_75742_a(VillageCollection.class, s);

        if (persistentvillage == null) {
            this.field_72982_D = new VillageCollection(this);
            this.field_72988_C.func_75745_a(s, this.field_72982_D);
        } else {
            this.field_72982_D = persistentvillage;
            this.field_72982_D.func_82566_a(this);
        }

        if (getServer().getScoreboardManager() == null) { // CraftBukkit
        this.field_96442_D = new ServerScoreboard(this.field_73061_a);
        ScoreboardSaveData persistentscoreboard = (ScoreboardSaveData) this.field_72988_C.func_75742_a(ScoreboardSaveData.class, "scoreboard");

        if (persistentscoreboard == null) {
            persistentscoreboard = new ScoreboardSaveData();
            this.field_72988_C.func_75745_a("scoreboard", persistentscoreboard);
        }

        persistentscoreboard.func_96499_a(this.field_96442_D);
        ((ServerScoreboard) this.field_96442_D).func_186684_a((new WorldSavedDataCallableSave(persistentscoreboard)));
        // CraftBukkit start
        } else {
            this.field_96442_D = getServer().getScoreboardManager().getMainScoreboard().getHandle();
        }
        // CraftBukkit end

        this.field_184151_B = new LootTableManager(new File(new File(this.field_73019_z.func_75765_b(), "data"), "loot_tables"));
        // CraftBukkit start
        if (this.dimension != 0) { // SPIGOT-3899 multiple worlds of advancements not supported
            this.field_191951_C = this.field_73061_a.func_191949_aK();
        }
        if (this.field_191951_C == null) {
            this.field_191951_C = new AdvancementManager(new File(new File(this.field_73019_z.func_75765_b(), "data"), "advancements"));
        }
        if (this.field_193036_D == null) {
            this.field_193036_D = new FunctionManager(new File(new File(this.field_73019_z.func_75765_b(), "data"), "functions"), this.field_73061_a);
        }
        // CraftBukkit end
        this.func_175723_af().func_177739_c(this.field_72986_A.func_176120_C(), this.field_72986_A.func_176126_D());
        this.func_175723_af().func_177744_c(this.field_72986_A.func_176140_I());
        this.func_175723_af().func_177724_b(this.field_72986_A.func_176138_H());
        this.func_175723_af().func_177747_c(this.field_72986_A.func_176131_J());
        this.func_175723_af().func_177723_b(this.field_72986_A.func_176139_K());
        if (this.field_72986_A.func_176134_F() > 0L) {
            this.func_175723_af().func_177738_a(this.field_72986_A.func_176137_E(), this.field_72986_A.func_176132_G(), this.field_72986_A.func_176134_F());
        } else {
            this.func_175723_af().func_177750_a(this.field_72986_A.func_176137_E());
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
    public TileEntity func_175625_s(BlockPos pos) {
        TileEntity result = super.func_175625_s(pos);
        Block type = func_180495_p(pos).func_177230_c();

        if (type == Blocks.field_150486_ae || type == Blocks.field_150447_bR) { // Spigot
            if (!(result instanceof TileEntityChest)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150460_al) {
            if (!(result instanceof TileEntityFurnace)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150409_cd) {
            if (!(result instanceof TileEntityDropper)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150367_z) {
            if (!(result instanceof TileEntityDispenser)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150421_aI) {
            if (!(result instanceof BlockJukebox.TileEntityJukebox)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150323_B) {
            if (!(result instanceof TileEntityNote)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150474_ac) {
            if (!(result instanceof TileEntityMobSpawner)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if ((type == Blocks.field_150472_an) || (type == Blocks.field_150444_as)) {
            if (!(result instanceof TileEntitySign)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150477_bB) {
            if (!(result instanceof TileEntityEnderChest)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150382_bo) {
            if (!(result instanceof TileEntityBrewingStand)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150461_bJ) {
            if (!(result instanceof TileEntityBeacon)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150438_bZ) {
            if (!(result instanceof TileEntityHopper)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150381_bn) {
            if (!(result instanceof TileEntityEnchantmentTable)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150384_bq) {
            if (!(result instanceof TileEntityEndPortal)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150465_bP) {
            if (!(result instanceof TileEntitySkull)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150453_bW || type == Blocks.field_180402_cm) {
            if (!(result instanceof TileEntityDaylightDetector)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150455_bV || type == Blocks.field_150441_bU) {
            if (!(result instanceof TileEntityComparator)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150457_bL) {
            if (!(result instanceof TileEntityFlowerPot)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_180393_cK || type == Blocks.field_180394_cL) {
            if (!(result instanceof TileEntityBanner)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_185779_df) {
            if (!(result instanceof TileEntityStructure)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_185775_db) {
            if (!(result instanceof TileEntityEndGateway)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150483_bI) {
            if (!(result instanceof TileEntityCommandBlock)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_185779_df) {
            if (!(result instanceof TileEntityStructure)) {
                result = fixTileEntity(pos, type, result);
            }
        } else if (type == Blocks.field_150324_C) {
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
                + "Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.", new Object[]{pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), org.bukkit.Material.getMaterial(Block.func_149682_b(type)).toString(), found});

        if (type instanceof ITileEntityProvider) {
            TileEntity replacement = ((ITileEntityProvider) type).func_149915_a(this, type.func_176201_c(this.func_180495_p(pos)));
            replacement.field_145850_b = this;
            this.func_175690_a(pos, replacement);
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
            return this.field_73011_w.func_76566_a(x, z);
        }
    }
    // CraftBukkit end

    @Override
    public void func_72835_b() {
        super.func_72835_b();
        if (this.func_72912_H().func_76093_s() && this.func_175659_aa() != EnumDifficulty.HARD) {
            this.func_72912_H().func_176144_a(EnumDifficulty.HARD);
        }

        this.field_73011_w.func_177499_m().func_76938_b();
        if (this.func_73056_e()) {
            if (this.func_82736_K().func_82766_b("doDaylightCycle")) {
                long i = this.field_72986_A.func_76073_f() + 24000L;

                this.field_72986_A.func_76068_b(i - i % 24000L);
            }

            this.func_73053_d();
        }

        this.field_72984_F.func_76320_a("mobSpawner");
        // CraftBukkit start - Only call spawner if we have players online and the world allows for mobs or animals
        long time = this.field_72986_A.func_82573_f();
        if (this.func_82736_K().func_82766_b("doMobSpawning") && this.field_72986_A.func_76067_t() != WorldType.field_180272_g && (this.field_72985_G || this.field_72992_H) && (this instanceof WorldServer && this.field_73010_i.size() > 0)) {
            timings.mobSpawn.startTiming(); // Spigot
            this.field_175742_R.func_77192_a(this, this.field_72985_G && (this.ticksPerMonsterSpawns != 0 && time % this.ticksPerMonsterSpawns == 0L), this.field_72992_H && (this.ticksPerAnimalSpawns != 0 && time % this.ticksPerAnimalSpawns == 0L), this.field_72986_A.func_82573_f() % 400L == 0L);
            timings.mobSpawn.stopTiming(); // Spigot
            // CraftBukkit end
        }

        timings.doChunkUnload.startTiming(); // Spigot
        this.field_72984_F.func_76318_c("chunkSource");
        this.field_73020_y.func_73156_b();
        int j = this.func_72967_a(1.0F);

        if (j != this.func_175657_ab()) {
            this.func_175692_b(j);
        }

        this.field_72986_A.func_82572_b(this.field_72986_A.func_82573_f() + 1L);
        if (this.func_82736_K().func_82766_b("doDaylightCycle")) {
            this.field_72986_A.func_76068_b(this.field_72986_A.func_76073_f() + 1L);
        }

        timings.doChunkUnload.stopTiming(); // Spigot
        this.field_72984_F.func_76318_c("tickPending");
        timings.scheduledBlocks.startTiming(); // Paper
        this.func_72955_a(false);
        timings.scheduledBlocks.stopTiming(); // Paper
        this.field_72984_F.func_76318_c("tickBlocks");
        timings.chunkTicks.startTiming(); // Paper
        this.func_147456_g();
        timings.chunkTicks.stopTiming(); // Paper
        this.field_72984_F.func_76318_c("chunkMap");
        timings.doChunkMap.startTiming(); // Spigot
        this.field_73063_M.func_72693_b();
        timings.doChunkMap.stopTiming(); // Spigot
        this.field_72984_F.func_76318_c("village");
        timings.doVillages.startTiming(); // Spigot
        this.field_72982_D.func_75544_a();
        this.field_175740_d.func_75528_a();
        timings.doVillages.stopTiming(); // Spigot
        this.field_72984_F.func_76318_c("portalForcer");
        timings.doPortalForcer.startTiming(); // Spigot
        this.field_85177_Q.func_85189_a(this.func_82737_E());
        timings.doPortalForcer.stopTiming(); // Spigot
        this.field_72984_F.func_76319_b();
        timings.doSounds.startTiming(); // Spigot
        this.func_147488_Z();
        timings.doSounds.stopTiming(); // Spigot

        timings.doChunkGC.startTiming();// Spigot
        this.getWorld().processChunkGC(); // CraftBukkit
        timings.doChunkGC.stopTiming(); // Spigot
    }

    @Nullable
    public Biome.SpawnListEntry func_175734_a(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        List list = this.func_72863_F().func_177458_a(enumcreaturetype, blockposition);

        return list != null && !list.isEmpty() ? (Biome.SpawnListEntry) WeightedRandom.func_76271_a(this.field_73012_v, list) : null;
    }

    public boolean func_175732_a(EnumCreatureType enumcreaturetype, Biome.SpawnListEntry biomebase_biomemeta, BlockPos blockposition) {
        List list = this.func_72863_F().func_177458_a(enumcreaturetype, blockposition);

        return list != null && !list.isEmpty() ? list.contains(biomebase_biomemeta) : false;
    }

    @Override
    public void func_72854_c() {
        this.field_73068_P = false;
        if (!this.field_73010_i.isEmpty()) {
            int i = 0;
            int j = 0;
            Iterator iterator = this.field_73010_i.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityhuman = (EntityPlayer) iterator.next();

                if (entityhuman.func_175149_v()) {
                    ++i;
                } else if (entityhuman.func_70608_bn() || entityhuman.fauxSleeping) {
                    ++j;
                }
            }

            this.field_73068_P = j > 0 && j >= this.field_73010_i.size() - i;
        }

    }

    protected void func_73053_d() {
        this.field_73068_P = false;
        List list = this.field_73010_i.stream().filter(EntityPlayer::func_70608_bn).collect(Collectors.toList());
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityhuman = (EntityPlayer) iterator.next();

            entityhuman.func_70999_a(false, false, true);
        }

        if (this.func_82736_K().func_82766_b("doWeatherCycle")) {
            this.func_73051_P();
        }

    }

    private void func_73051_P() {
        this.field_72986_A.func_76084_b(false);
        // CraftBukkit start
        // If we stop due to everyone sleeping we should reset the weather duration to some other random value.
        // Not that everyone ever manages to get the whole server to sleep at the same time....
        if (!this.field_72986_A.func_76059_o()) {
            this.field_72986_A.func_76080_g(0);
        }
        // CraftBukkit end
        this.field_72986_A.func_76069_a(false);
        // CraftBukkit start
        // If we stop due to everyone sleeping we should reset the weather duration to some other random value.
        // Not that everyone ever manages to get the whole server to sleep at the same time....
        if (!this.field_72986_A.func_76061_m()) {
            this.field_72986_A.func_76090_f(0);
        }
        // CraftBukkit end
    }

    public boolean func_73056_e() {
        if (this.field_73068_P && !this.field_72995_K) {
            Iterator iterator = this.field_73010_i.iterator();

            // CraftBukkit - This allows us to assume that some people are in bed but not really, allowing time to pass in spite of AFKers
            boolean foundActualSleepers = false;

            EntityPlayer entityhuman;

            do {
                if (!iterator.hasNext()) {
                    return foundActualSleepers;
                }

                entityhuman = (EntityPlayer) iterator.next();

                // CraftBukkit start
                if (entityhuman.func_71026_bH()) {
                    foundActualSleepers = true;
                }
            } while (!entityhuman.func_175149_v() || entityhuman.func_71026_bH() || entityhuman.fauxSleeping);
            // CraftBukkit end

            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean func_175680_a(int i, int j, boolean flag) {
        return this.func_72863_F().func_73149_a(i, j);
    }

    protected void func_184162_i() {
        this.field_72984_F.func_76320_a("playerCheckLight");
        if (spigotConfig.randomLightUpdates && !this.field_73010_i.isEmpty()) { // Spigot
            int i = this.field_73012_v.nextInt(this.field_73010_i.size());
            EntityPlayer entityhuman = this.field_73010_i.get(i);
            int j = MathHelper.func_76128_c(entityhuman.field_70165_t) + this.field_73012_v.nextInt(11) - 5;
            int k = MathHelper.func_76128_c(entityhuman.field_70163_u) + this.field_73012_v.nextInt(11) - 5;
            int l = MathHelper.func_76128_c(entityhuman.field_70161_v) + this.field_73012_v.nextInt(11) - 5;

            this.func_175664_x(new BlockPos(j, k, l));
        }

        this.field_72984_F.func_76319_b();
    }

    @Override
    protected void func_147456_g() {
        this.func_184162_i();
        if (this.field_72986_A.func_76067_t() == WorldType.field_180272_g) {
            Iterator iterator = this.field_73063_M.func_187300_b();

            while (iterator.hasNext()) {
                ((Chunk) iterator.next()).func_150804_b(false);
            }

        } else {
            int i = this.func_82736_K().func_180263_c("randomTickSpeed");
            boolean flag = this.func_72896_J();
            boolean flag1 = this.func_72911_I();

            this.field_72984_F.func_76320_a("pollingChunks");

            for (Iterator iterator1 = this.field_73063_M.func_187300_b(); iterator1.hasNext(); this.field_72984_F.func_76319_b()) {
                this.field_72984_F.func_76320_a("getChunk");
                Chunk chunk = (Chunk) iterator1.next();
                int j = chunk.field_76635_g * 16;
                int k = chunk.field_76647_h * 16;

                this.field_72984_F.func_76318_c("checkNextLight");
                chunk.func_76594_o();
                this.field_72984_F.func_76318_c("tickChunk");
                chunk.func_150804_b(false);
                if ( !chunk.areNeighborsLoaded( 1 ) ) continue; // Spigot
                this.field_72984_F.func_76318_c("thunder");
                int l;
                BlockPos blockposition;

                // Paper - Disable thunder
                if (!this.paperConfig.disableThunder && flag && flag1 && this.field_73012_v.nextInt(100000) == 0) {
                    this.field_73005_l = this.field_73005_l * 3 + 1013904223;
                    l = this.field_73005_l >> 2;
                    blockposition = this.func_175736_a(new BlockPos(j + (l & 15), 0, k + (l >> 8 & 15)));
                    if (this.func_175727_C(blockposition)) {
                        DifficultyInstance difficultydamagescaler = this.func_175649_E(blockposition);

                        if (this.func_82736_K().func_82766_b("doMobSpawning") && this.field_73012_v.nextDouble() < difficultydamagescaler.func_180168_b() * paperConfig.skeleHorseSpawnChance) {
                            EntitySkeletonHorse entityhorseskeleton = new EntitySkeletonHorse(this);

                            entityhorseskeleton.func_190691_p(true);
                            entityhorseskeleton.func_70873_a(0);
                            entityhorseskeleton.func_70107_b(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                            this.addEntity(entityhorseskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.LIGHTNING); // CraftBukkit
                            this.func_72942_c(new EntityLightningBolt(this, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), true));
                        } else {
                            this.func_72942_c(new EntityLightningBolt(this, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), false));
                        }
                    }
                }

                this.field_72984_F.func_76318_c("iceandsnow");
                if (!this.paperConfig.disableIceAndSnow && this.field_73012_v.nextInt(16) == 0) { // Paper - Disable ice and snow
                    this.field_73005_l = this.field_73005_l * 3 + 1013904223;
                    l = this.field_73005_l >> 2;
                    blockposition = this.func_175725_q(new BlockPos(j + (l & 15), 0, k + (l >> 8 & 15)));
                    BlockPos blockposition1 = blockposition.func_177977_b();

                    if (this.func_175662_w(blockposition1)) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition1, Blocks.field_150432_aD.func_176223_P(), null); // CraftBukkit
                    }

                    if (flag && this.func_175708_f(blockposition, true)) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition, Blocks.field_150431_aC.func_176223_P(), null); // CraftBukkit
                    }

                    if (flag && this.func_180494_b(blockposition1).func_76738_d()) {
                        this.func_180495_p(blockposition1).func_177230_c().func_176224_k(this, blockposition1);
                    }
                }

                timings.chunkTicksBlocks.startTiming(); // Paper
                if (i > 0) {
                    ExtendedBlockStorage[] achunksection = chunk.func_76587_i();
                    int i1 = achunksection.length;

                    for (int j1 = 0; j1 < i1; ++j1) {
                        ExtendedBlockStorage chunksection = achunksection[j1];

                        if (chunksection != Chunk.field_186036_a && chunksection.func_76675_b()) {
                            for (int k1 = 0; k1 < i; ++k1) {
                                this.field_73005_l = this.field_73005_l * 3 + 1013904223;
                                int l1 = this.field_73005_l >> 2;
                                int i2 = l1 & 15;
                                int j2 = l1 >> 8 & 15;
                                int k2 = l1 >> 16 & 15;
                                IBlockState iblockdata = chunksection.func_177485_a(i2, k2, j2);
                                Block block = iblockdata.func_177230_c();

                                this.field_72984_F.func_76320_a("randomTick");
                                if (block.func_149653_t()) {
                                    block.func_180645_a(this, new BlockPos(i2 + j, k2 + chunksection.func_76662_d(), j2 + k), iblockdata, this.field_73012_v);
                                }

                                this.field_72984_F.func_76319_b();
                            }
                        }
                    }
                }
                timings.chunkTicksBlocks.stopTiming(); // Paper
            }

            this.field_72984_F.func_76319_b();
        }
    }

    protected BlockPos func_175736_a(BlockPos blockposition) {
        BlockPos blockposition1 = this.func_175725_q(blockposition);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockposition1, new BlockPos(blockposition1.func_177958_n(), this.func_72800_K(), blockposition1.func_177952_p()))).func_186662_g(3.0D);
        List list = this.func_175647_a(EntityLivingBase.class, axisalignedbb, new com.google.common.base.Predicate() {
            public boolean a(@Nullable EntityLivingBase entityliving) {
                return entityliving != null && entityliving.func_70089_S() && WorldServer.this.func_175678_i(entityliving.func_180425_c());
            }

            @Override
            public boolean apply(@Nullable Object object) {
                return this.a((EntityLivingBase) object);
            }
        });

        if (!list.isEmpty()) {
            return ((EntityLivingBase) list.get(this.field_73012_v.nextInt(list.size()))).func_180425_c();
        } else {
            if (blockposition1.func_177956_o() == -1) {
                blockposition1 = blockposition1.func_177981_b(2);
            }

            return blockposition1;
        }
    }

    @Override
    public boolean func_175691_a(BlockPos blockposition, Block block) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        return this.field_94579_S.contains(nextticklistentry);
    }

    @Override
    public boolean func_184145_b(BlockPos blockposition, Block block) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        return this.field_73065_O.contains(nextticklistentry); // CraftBukkit
    }

    @Override
    public void func_175684_a(BlockPos blockposition, Block block, int i) {
        this.func_175654_a(blockposition, block, i, 0);
    }

    @Override
    public void func_175654_a(BlockPos blockposition, Block block, int i, int j) {
        Material material = block.func_176223_P().func_185904_a();

        if (this.field_72999_e && material != Material.field_151579_a) {
            if (block.func_149698_L()) {
                if (this.func_175707_a(blockposition.func_177982_a(-8, -8, -8), blockposition.func_177982_a(8, 8, 8))) {
                    IBlockState iblockdata = this.func_180495_p(blockposition);

                    if (iblockdata.func_185904_a() != Material.field_151579_a && iblockdata.func_177230_c() == block) {
                        iblockdata.func_177230_c().func_180650_b(this, blockposition, iblockdata, this.field_73012_v);
                    }
                }

                return;
            }

            i = 1;
        }

        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        if (this.func_175667_e(blockposition)) {
            if (material != Material.field_151579_a) {
                nextticklistentry.func_77176_a(i + this.field_72986_A.func_82573_f());
                nextticklistentry.func_82753_a(j);
            }

            // CraftBukkit - use nextTickList
            if (!this.field_73065_O.contains(nextticklistentry)) {
                this.field_73065_O.add(nextticklistentry);
            }
        }

    }

    @Override
    public void func_180497_b(BlockPos blockposition, Block block, int i, int j) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);

        nextticklistentry.func_82753_a(j);
        Material material = block.func_176223_P().func_185904_a();

        if (material != Material.field_151579_a) {
            nextticklistentry.func_77176_a(i + this.field_72986_A.func_82573_f());
        }

        // CraftBukkit - use nextTickList
        if (!this.field_73065_O.contains(nextticklistentry)) {
            this.field_73065_O.add(nextticklistentry);
        }

    }

    @Override
    public void func_72939_s() {
        if (false && this.field_73010_i.isEmpty()) { // CraftBukkit - this prevents entity cleanup, other issues on servers with no players
            if (this.field_80004_Q++ >= 300) {
                return;
            }
        } else {
            this.func_82742_i();
        }

        this.field_73011_w.func_186059_r();
        super.func_72939_s();
        spigotConfig.currentPrimedTnt = 0; // Spigot
    }

    @Override
    protected void func_184147_l() {
        super.func_184147_l();
        this.field_72984_F.func_76318_c("players");

        for (int i = 0; i < this.field_73010_i.size(); ++i) {
            Entity entity = this.field_73010_i.get(i);
            Entity entity1 = entity.func_184187_bx();

            if (entity1 != null) {
                if (!entity1.field_70128_L && entity1.func_184196_w(entity)) {
                    continue;
                }

                entity.func_184210_p();
            }

            this.field_72984_F.func_76320_a("tick");
            if (!entity.field_70128_L) {
                try {
                    this.func_72870_g(entity);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.func_85055_a(throwable, "Ticking player");
                    CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Player being ticked");

                    entity.func_85029_a(crashreportsystemdetails);
                    throw new ReportedException(crashreport);
                }
            }

            this.field_72984_F.func_76319_b();
            this.field_72984_F.func_76320_a("remove");
            if (entity.field_70128_L) {
                int j = entity.field_70176_ah;
                int k = entity.field_70164_aj;

                if (entity.field_70175_ag && this.func_175680_a(j, k, true)) {
                    this.func_72964_e(j, k).func_76622_b(entity);
                }

                this.field_72996_f.remove(entity);
                this.func_72847_b(entity);
            }

            this.field_72984_F.func_76319_b();
        }

    }

    public void func_82742_i() {
        this.field_80004_Q = 0;
    }

    @Override
    public boolean func_72955_a(boolean flag) {
        if (this.field_72986_A.func_76067_t() == WorldType.field_180272_g) {
            return false;
        } else {
            int i = this.field_73065_O.size();

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

                this.field_72984_F.func_76320_a("cleaning");

                timings.scheduledBlocksCleanup.startTiming(); // Paper
                NextTickListEntry nextticklistentry;

                for (int j = 0; j < i; ++j) {
                    nextticklistentry = this.field_73065_O.first();
                    if (!flag && nextticklistentry.field_77180_e > this.field_72986_A.func_82573_f()) {
                        break;
                    }

                    // CraftBukkit - use nextTickList
                    this.field_73065_O.remove(nextticklistentry);
                    // this.nextTickListHash.remove(nextticklistentry);
                    this.field_94579_S.add(nextticklistentry);
                }
                timings.scheduledBlocksCleanup.stopTiming(); // Paper

                this.field_72984_F.func_76319_b();
                this.field_72984_F.func_76320_a("ticking");
                Iterator iterator = this.field_94579_S.iterator();
                timings.scheduledBlocksTicking.startTiming(); // Paper

                while (iterator.hasNext()) {
                    nextticklistentry = (NextTickListEntry) iterator.next();
                    iterator.remove();
                    boolean flag1 = false;

                    if (this.func_175707_a(nextticklistentry.field_180282_a.func_177982_a(0, 0, 0), nextticklistentry.field_180282_a.func_177982_a(0, 0, 0))) {
                        IBlockState iblockdata = this.func_180495_p(nextticklistentry.field_180282_a);
                        co.aikar.timings.Timing timing = iblockdata.func_177230_c().getTiming(); // Paper
                        timing.startTiming(); // Paper

                        if (iblockdata.func_185904_a() != Material.field_151579_a && Block.func_149680_a(iblockdata.func_177230_c(), nextticklistentry.func_151351_a())) {
                            try {
                                stopPhysicsEvent = !paperConfig.firePhysicsEventForRedstone && (iblockdata.func_177230_c() instanceof BlockRedstoneDiode || iblockdata.func_177230_c() instanceof BlockRedstoneTorch); // Paper
                                iblockdata.func_177230_c().func_180650_b(this, nextticklistentry.field_180282_a, iblockdata, this.field_73012_v);
                            } catch (Throwable throwable) {
                                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Exception while ticking a block");
                                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Block being ticked");

                                CrashReportCategory.func_175750_a(crashreportsystemdetails, nextticklistentry.field_180282_a, iblockdata);
                                throw new ReportedException(crashreport);
                            } finally { stopPhysicsEvent = false; } // Paper
                        }
                        timing.stopTiming(); // Paper
                    } else {
                        this.func_175684_a(nextticklistentry.field_180282_a, nextticklistentry.func_151351_a(), 0);
                    }
                }
                timings.scheduledBlocksTicking.stopTiming(); // Paper

                this.field_72984_F.func_76319_b();
                this.field_94579_S.clear();
                return !this.field_73065_O.isEmpty();
            }
        }
    }

    @Override
    @Nullable
    public List<NextTickListEntry> func_72920_a(Chunk chunk, boolean flag) {
        ChunkPos chunkcoordintpair = chunk.func_76632_l();
        int i = (chunkcoordintpair.field_77276_a << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkcoordintpair.field_77275_b << 4) - 2;
        int l = k + 16 + 2;

        return this.func_175712_a(new StructureBoundingBox(i, 0, k, j, 256, l), flag);
    }

    @Override
    @Nullable
    public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureboundingbox, boolean flag) {
        ArrayList arraylist = null;

        for (int i = 0; i < 2; ++i) {
            Iterator iterator;

            if (i == 0) {
                iterator = this.field_73065_O.iterator();
            } else {
                iterator = this.field_94579_S.iterator();
            }

            while (iterator.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator.next();
                BlockPos blockposition = nextticklistentry.field_180282_a;

                if (blockposition.func_177958_n() >= structureboundingbox.field_78897_a && blockposition.func_177958_n() < structureboundingbox.field_78893_d && blockposition.func_177952_p() >= structureboundingbox.field_78896_c && blockposition.func_177952_p() < structureboundingbox.field_78892_f) {
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

    private boolean func_175738_ah() {
        return this.field_73061_a.func_71220_V();
    }

    private boolean func_175735_ai() {
        return this.field_73061_a.func_71268_U();
    }

    @Override
    protected IChunkProvider func_72970_h() {
        IChunkLoader ichunkloader = this.field_73019_z.func_75763_a(this.field_73011_w);

        // CraftBukkit start
        org.bukkit.craftbukkit.generator.InternalChunkGenerator gen;

        if (this.generator != null) {
            gen = new org.bukkit.craftbukkit.generator.CustomChunkGenerator(this, this.func_72905_C(), this.generator);
        } else if (this.field_73011_w instanceof WorldProviderHell) {
            gen = new org.bukkit.craftbukkit.generator.NetherChunkGenerator(this, this.func_72905_C());
        } else if (this.field_73011_w instanceof WorldProviderEnd) {
            gen = new org.bukkit.craftbukkit.generator.SkyLandsChunkGenerator(this, this.func_72905_C());
        } else {
            gen = new org.bukkit.craftbukkit.generator.NormalChunkGenerator(this, this.func_72905_C());
        }

        return new ChunkProviderServer(this, ichunkloader, new co.aikar.timings.TimedChunkGenerator(this, gen)); // Paper
        // CraftBukkit end
    }

    public List<TileEntity> a(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = Lists.newArrayList();

        // CraftBukkit start - Get tile entities from chunks instead of world
        for (int chunkX = (i >> 4); chunkX <= ((l - 1) >> 4); chunkX++) {
            for (int chunkZ = (k >> 4); chunkZ <= ((j1 - 1) >> 4); chunkZ++) {
                Chunk chunk = func_72964_e(chunkX, chunkZ);
                if (chunk == null) {
                    continue;
                }
                for (Object te : chunk.field_150816_i.values()) {
                    TileEntity tileentity = (TileEntity) te;
                    if ((tileentity.field_174879_c.func_177958_n() >= i) && (tileentity.field_174879_c.func_177956_o() >= j) && (tileentity.field_174879_c.func_177952_p() >= k) && (tileentity.field_174879_c.func_177958_n() < l) && (tileentity.field_174879_c.func_177956_o() < i1) && (tileentity.field_174879_c.func_177952_p() < j1)) {
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
    public boolean func_175660_a(EntityPlayer entityhuman, BlockPos blockposition) {
        return !this.field_73061_a.func_175579_a(this, blockposition, entityhuman) && this.func_175723_af().func_177746_a(blockposition);
    }

    @Override
    public void func_72963_a(WorldSettings worldsettings) {
        if (!this.field_72986_A.func_76070_v()) {
            try {
                this.func_73052_b(worldsettings);
                if (this.field_72986_A.func_76067_t() == WorldType.field_180272_g) {
                    this.func_175737_aj();
                }

                super.func_72963_a(worldsettings);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Exception initializing level");

                try {
                    this.func_72914_a(crashreport);
                } catch (Throwable throwable1) {
                    ;
                }

                throw new ReportedException(crashreport);
            }

            this.field_72986_A.func_76091_d(true);
        }

    }

    private void func_175737_aj() {
        this.field_72986_A.func_176128_f(false);
        this.field_72986_A.func_176121_c(true);
        this.field_72986_A.func_76084_b(false);
        this.field_72986_A.func_76069_a(false);
        this.field_72986_A.func_176142_i(1000000000);
        this.field_72986_A.func_76068_b(6000L);
        this.field_72986_A.func_76060_a(GameType.SPECTATOR);
        this.field_72986_A.func_176119_g(false);
        this.field_72986_A.func_176144_a(EnumDifficulty.PEACEFUL);
        this.field_72986_A.func_180783_e(true);
        this.func_82736_K().func_82764_b("doDaylightCycle", "false");
    }

    private void func_73052_b(WorldSettings worldsettings) {
        if (!this.field_73011_w.func_76567_e()) {
            this.field_72986_A.func_176143_a(BlockPos.field_177992_a.func_177981_b(this.field_73011_w.func_76557_i()));
        } else if (this.field_72986_A.func_76067_t() == WorldType.field_180272_g) {
            this.field_72986_A.func_176143_a(BlockPos.field_177992_a.func_177984_a());
        } else {
            this.field_72987_B = true;
            BiomeProvider worldchunkmanager = this.field_73011_w.func_177499_m();
            List list = worldchunkmanager.func_76932_a();
            Random random = new Random(this.func_72905_C());
            BlockPos blockposition = worldchunkmanager.func_180630_a(0, 0, 256, list, random);
            int i = 8;
            int j = this.field_73011_w.func_76557_i();
            int k = 8;

            // CraftBukkit start
            if (this.generator != null) {
                Random rand = new Random(this.func_72905_C());
                org.bukkit.Location spawn = this.generator.getFixedSpawnLocation(this.getWorld(), rand);

                if (spawn != null) {
                    if (spawn.getWorld() != this.getWorld()) {
                        throw new IllegalStateException("Cannot set spawn point for " + this.field_72986_A.func_76065_j() + " to be in another world (" + spawn.getWorld().getName() + ")");
                    } else {
                        this.field_72986_A.func_176143_a(new BlockPos(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()));
                        this.field_72987_B = false;
                        return;
                    }
                }
            }
            // CraftBukkit end

            if (blockposition != null) {
                i = blockposition.func_177958_n();
                k = blockposition.func_177952_p();
            } else {
                WorldServer.field_147491_a.warn("Unable to find spawn biome");
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

            this.field_72986_A.func_176143_a(new BlockPos(i, j, k));
            this.field_72987_B = false;
            if (worldsettings.func_77167_c()) {
                this.func_73047_i();
            }

        }
    }

    protected void func_73047_i() {
        WorldGeneratorBonusChest worldgenbonuschest = new WorldGeneratorBonusChest();

        for (int i = 0; i < 10; ++i) {
            int j = this.field_72986_A.func_76079_c() + this.field_73012_v.nextInt(6) - this.field_73012_v.nextInt(6);
            int k = this.field_72986_A.func_76074_e() + this.field_73012_v.nextInt(6) - this.field_73012_v.nextInt(6);
            BlockPos blockposition = this.func_175672_r(new BlockPos(j, 0, k)).func_177984_a();

            if (worldgenbonuschest.func_180709_b(this, this.field_73012_v, blockposition)) {
                break;
            }
        }

    }

    @Nullable
    public BlockPos func_180504_m() {
        return this.field_73011_w.func_177496_h();
    }

    public void func_73044_a(boolean flag, @Nullable IProgressUpdate iprogressupdate) throws MinecraftException {
        ChunkProviderServer chunkproviderserver = this.func_72863_F();

        if (chunkproviderserver.func_73157_c()) {
            if (flag) org.bukkit.Bukkit.getPluginManager().callEvent(new org.bukkit.event.world.WorldSaveEvent(getWorld())); // CraftBukkit // Paper - Incremental Auto Saving - Only fire event on full save
            timings.worldSave.startTiming(); // Paper
            if (flag || field_73061_a.serverAutoSave) { // Paper
            if (iprogressupdate != null) {
                iprogressupdate.func_73720_a("Saving level");
            }

            this.func_73042_a();
            if (iprogressupdate != null) {
                iprogressupdate.func_73719_c("Saving chunks");
            }
            } // Paper

            timings.worldSaveChunks.startTiming(); // Paper
            chunkproviderserver.func_186027_a(flag);
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

    public void func_104140_m() {
        ChunkProviderServer chunkproviderserver = this.func_72863_F();

        if (chunkproviderserver.func_73157_c()) {
            chunkproviderserver.func_104112_b();
        }
    }

    protected void func_73042_a() throws MinecraftException {
        timings.worldSaveLevel.startTiming(); // Paper
        this.func_72906_B();
        WorldServer[] aworldserver = this.field_73061_a.field_71305_c;
        int i = aworldserver.length;

        for (int j = 0; j < i; ++j) {
            WorldServer worldserver = aworldserver[j];

            if (worldserver instanceof WorldServerMulti) {
                ((WorldServerMulti) worldserver).func_184166_c();
            }
        }

        // CraftBukkit start - Save secondary data for nether/end
        if (this instanceof WorldServerMulti) {
            ((WorldServerMulti) this).func_184166_c();
        }
        // CraftBukkit end

        this.field_72986_A.func_176145_a(this.func_175723_af().func_177741_h());
        this.field_72986_A.func_176124_d(this.func_175723_af().func_177731_f());
        this.field_72986_A.func_176141_c(this.func_175723_af().func_177721_g());
        this.field_72986_A.func_176129_e(this.func_175723_af().func_177742_m());
        this.field_72986_A.func_176125_f(this.func_175723_af().func_177727_n());
        this.field_72986_A.func_176122_j(this.func_175723_af().func_177748_q());
        this.field_72986_A.func_176136_k(this.func_175723_af().func_177740_p());
        this.field_72986_A.func_176118_b(this.func_175723_af().func_177751_j());
        this.field_72986_A.func_176135_e(this.func_175723_af().func_177732_i());
        this.field_73019_z.func_75755_a(this.field_72986_A, this.field_73061_a.func_184103_al().func_72378_q());
        this.field_72988_C.func_75744_a();
        timings.worldSaveLevel.stopTiming(); // Paper
    }

    // CraftBukkit start
    @Override
    public boolean addEntity(Entity entity, SpawnReason spawnReason) { // Changed signature, added SpawnReason
        // World.addEntity(Entity) will call this, and we still want to perform
        // existing entity checking when it's called with a SpawnReason
        return this.func_184165_i(entity) ? super.addEntity(entity, spawnReason) : false;
    }
    // CraftBukkit end

    @Override
    public void func_175650_b(Collection<Entity> collection) {
        ArrayList arraylist = Lists.newArrayList(collection);
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (this.func_184165_i(entity)) {
                this.field_72996_f.add(entity);
                this.func_72923_a(entity);
            }
        }

    }

    private boolean func_184165_i(Entity entity) {
        if (entity.field_70128_L) {
            // WorldServer.a.warn("Tried to add entity {} but it was marked as removed already", EntityTypes.a(entity)); // CraftBukkit
            return false;
        } else {
            UUID uuid = entity.func_110124_au();

            if (this.field_175741_N.containsKey(uuid)) {
                Entity entity1 = this.field_175741_N.get(uuid);

                if (this.field_72997_g.contains(entity1)) {
                    this.field_72997_g.remove(entity1);
                } else {
                    if (!(entity instanceof EntityPlayer)) {
                        // WorldServer.a.warn("Keeping entity {} that already exists with UUID {}", EntityTypes.a(entity1), uuid.toString()); // CraftBukkit
                        return false;
                    }

                    WorldServer.field_147491_a.warn("Force-added player with duplicate UUID {}", uuid.toString());
                }

                this.func_72973_f(entity1);
            }

            return true;
        }
    }

    @Override
    protected void func_72923_a(Entity entity) {
        super.func_72923_a(entity);
        this.field_175729_l.func_76038_a(entity.func_145782_y(), entity);
        this.field_175741_N.put(entity.func_110124_au(), entity);
        Entity[] aentity = entity.func_70021_al();

        if (aentity != null) {
            Entity[] aentity1 = aentity;
            int i = aentity.length;

            for (int j = 0; j < i; ++j) {
                Entity entity1 = aentity1[j];

                this.field_175729_l.func_76038_a(entity1.func_145782_y(), entity1);
            }
        }

    }

    @Override
    protected void func_72847_b(Entity entity) {
        if (!entity.valid) return; // Paper - Already removed, dont fire twice - this looks like it can happen even without our changes
        super.func_72847_b(entity);
        this.field_175729_l.func_76049_d(entity.func_145782_y());
        this.field_175741_N.remove(entity.func_110124_au());
        Entity[] aentity = entity.func_70021_al();

        if (aentity != null) {
            Entity[] aentity1 = aentity;
            int i = aentity.length;

            for (int j = 0; j < i; ++j) {
                Entity entity1 = aentity1[j];

                this.field_175729_l.func_76049_d(entity1.func_145782_y());
            }
        }

    }

    @Override
    public boolean func_72942_c(Entity entity) {
        // CraftBukkit start
        LightningStrikeEvent lightning = new LightningStrikeEvent(this.getWorld(), (org.bukkit.entity.LightningStrike) entity.getBukkitEntity());
        this.getServer().getPluginManager().callEvent(lightning);

        if (lightning.isCancelled()) {
            return false;
        }
        // CraftBukkit end
        if (super.func_72942_c(entity)) {
            this.field_73061_a.func_184103_al().func_148543_a((EntityPlayer) null, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, 512.0D, dimension, new SPacketSpawnGlobalEntity(entity)); // CraftBukkit - Use dimension
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void func_72960_a(Entity entity, byte b0) {
        this.func_73039_n().func_151248_b(entity, new SPacketEntityStatus(entity, b0));
    }

    @Override
    public ChunkProviderServer func_72863_F() {
        return (ChunkProviderServer) super.func_72863_F();
    }

    @Override
    public Explosion func_72885_a(@Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        // CraftBukkit start
        Explosion explosion = super.func_72885_a(entity, d0, d1, d2, f, flag, flag1);

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
            explosion.func_180342_d();
        }

        Iterator iterator = this.field_73010_i.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityhuman = (EntityPlayer) iterator.next();

            if (entityhuman.func_70092_e(d0, d1, d2) < 4096.0D) {
                ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketExplosion(d0, d1, d2, f, explosion.func_180343_e(), explosion.func_77277_b().get(entityhuman)));
            }
        }

        return explosion;
    }

    @Override
    public void func_175641_c(BlockPos blockposition, Block block, int i, int j) {
        BlockEventData blockactiondata = new BlockEventData(blockposition, block, i, j);
        Iterator iterator = this.field_147490_S[this.field_147489_T].iterator();

        BlockEventData blockactiondata1;

        do {
            if (!iterator.hasNext()) {
                this.field_147490_S[this.field_147489_T].add(blockactiondata);
                return;
            }

            blockactiondata1 = (BlockEventData) iterator.next();
        } while (!blockactiondata1.equals(blockactiondata));

    }

    private void func_147488_Z() {
        while (!this.field_147490_S[this.field_147489_T].isEmpty()) {
            int i = this.field_147489_T;

            this.field_147489_T ^= 1;
            Iterator iterator = this.field_147490_S[i].iterator();

            while (iterator.hasNext()) {
                BlockEventData blockactiondata = (BlockEventData) iterator.next();

                if (this.func_147485_a(blockactiondata)) {
                    // CraftBukkit - this.worldProvider.dimension -> this.dimension
                    this.field_73061_a.func_184103_al().func_148543_a((EntityPlayer) null, blockactiondata.func_180328_a().func_177958_n(), blockactiondata.func_180328_a().func_177956_o(), blockactiondata.func_180328_a().func_177952_p(), 64.0D, dimension, new SPacketBlockAction(blockactiondata.func_180328_a(), blockactiondata.func_151337_f(), blockactiondata.func_151339_d(), blockactiondata.func_151338_e()));
                }
            }

            this.field_147490_S[i].clear();
        }

    }

    private boolean func_147485_a(BlockEventData blockactiondata) {
        IBlockState iblockdata = this.func_180495_p(blockactiondata.func_180328_a());

        return iblockdata.func_177230_c() == blockactiondata.func_151337_f() ? iblockdata.func_189547_a(this, blockactiondata.func_180328_a(), blockactiondata.func_151339_d(), blockactiondata.func_151338_e()) : false;
    }

    public void func_73041_k() {
        this.field_73019_z.func_75759_a();
    }

    @Override
    protected void func_72979_l() {
        boolean flag = this.func_72896_J();

        super.func_72979_l();
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
        if (flag != this.func_72896_J()) {
            // Only send weather packets to those affected
            for (int i = 0; i < this.field_73010_i.size(); ++i) {
                if (((EntityPlayerMP) this.field_73010_i.get(i)).field_70170_p == this) {
                    ((EntityPlayerMP) this.field_73010_i.get(i)).setPlayerWeather((!flag ? WeatherType.DOWNFALL : WeatherType.CLEAR), false);
                }
            }
        }
        for (int i = 0; i < this.field_73010_i.size(); ++i) {
            if (((EntityPlayerMP) this.field_73010_i.get(i)).field_70170_p == this) {
                ((EntityPlayerMP) this.field_73010_i.get(i)).updateWeather(this.field_73003_n, this.field_73004_o, this.field_73018_p, this.field_73017_q);
            }
        }
        // CraftBukkit end

    }

    @Override
    @Nullable
    public MinecraftServer func_73046_m() {
        return this.field_73061_a;
    }

    public EntityTracker func_73039_n() {
        return this.field_73062_L;
    }

    public PlayerChunkMap func_184164_w() {
        return this.field_73063_M;
    }

    public Teleporter func_85176_s() {
        return this.field_85177_Q;
    }

    public TemplateManager func_184163_y() {
        return this.field_73019_z.func_186340_h();
    }

    public void func_175739_a(EnumParticleTypes enumparticle, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        this.func_180505_a(enumparticle, false, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }

    public void func_180505_a(EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        // CraftBukkit - visibility api support
        sendParticles(null, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }

    // Paper start - Particle API Expansion
    public void sendParticles(EntityPlayerMP sender, EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        sendParticles(this.field_73010_i, sender, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }
    public void sendParticles(List<? extends EntityPlayer> receivers, EntityPlayerMP sender, EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        // Paper end
        // CraftBukkit end
        SPacketParticles packetplayoutworldparticles = new SPacketParticles(enumparticle, flag, (float) d0, (float) d1, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, i, aint);

        for (EntityPlayer entityhuman : receivers) { // Paper - Particle API Expansion
            EntityPlayerMP entityplayer = (EntityPlayerMP) entityhuman; // Paper - Particle API Expansion
            if (sender != null && !entityplayer.getBukkitEntity().canSee(sender.getBukkitEntity())) continue; // CraftBukkit
            BlockPos blockposition = entityplayer.func_180425_c();
            double d7 = blockposition.func_177954_c(d0, d1, d2);


            this.func_184159_a(entityplayer, flag, d0, d1, d2, packetplayoutworldparticles);
        }

    }

    public void func_184161_a(EntityPlayerMP entityplayer, EnumParticleTypes enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        SPacketParticles packetplayoutworldparticles = new SPacketParticles(enumparticle, flag, (float) d0, (float) d1, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, i, aint);

        this.func_184159_a(entityplayer, flag, d0, d1, d2, packetplayoutworldparticles);
    }

    private void func_184159_a(EntityPlayerMP entityplayer, boolean flag, double d0, double d1, double d2, Packet<?> packet) {
        BlockPos blockposition = entityplayer.func_180425_c();
        double d3 = blockposition.func_177954_c(d0, d1, d2);

        if (d3 <= 1024.0D || flag && d3 <= 262144.0D) {
            entityplayer.field_71135_a.func_147359_a(packet);
        }

    }

    @Nullable
    public Entity func_175733_a(UUID uuid) {
        return this.field_175741_N.get(uuid);
    }

    @Override
    public ListenableFuture<Object> func_152344_a(Runnable runnable) {
        return this.field_73061_a.func_152344_a(runnable);
    }

    @Override
    public boolean func_152345_ab() {
        return this.field_73061_a.func_152345_ab();
    }

    @Override
    @Nullable
    public BlockPos func_190528_a(String s, BlockPos blockposition, boolean flag) {
        return this.func_72863_F().func_180513_a(this, s, blockposition, flag);
    }

    public AdvancementManager func_191952_z() {
        return this.field_191951_C;
    }

    public FunctionManager func_193037_A() {
        return this.field_193036_D;
    }

    static class ServerBlockEventList extends ArrayList<BlockEventData> {

        private ServerBlockEventList() {}

        ServerBlockEventList(Object object) {
            this();
        }
    }
}

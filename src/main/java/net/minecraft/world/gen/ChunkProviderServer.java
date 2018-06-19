package net.minecraft.world.gen;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import com.destroystokyo.paper.exception.ServerInternalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;
import org.bukkit.event.world.ChunkUnloadEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;
import org.bukkit.event.world.ChunkUnloadEvent;
// CraftBukkit end

public class ChunkProviderServer implements IChunkProvider {

    private static final Logger field_147417_b = LogManager.getLogger();
    public final Set<Long> field_73248_b = Sets.newHashSet();
    public final IChunkGenerator field_186029_c;
    private final IChunkLoader field_73247_e;
    // Paper start - chunk save stats
    private long lastQueuedSaves = 0L; // Paper
    private long lastProcessedSaves = 0L; // Paper
    private long lastSaveStatPrinted = System.currentTimeMillis();
    // Paper end
    // Paper start
    protected Chunk lastChunkByPos = null;
    public Long2ObjectOpenHashMap<Chunk> field_73244_f = new Long2ObjectOpenHashMap<Chunk>(8192) {

        @Override
        public Chunk get(long key) {
            if (lastChunkByPos != null && key == lastChunkByPos.chunkKey) {
                return lastChunkByPos;
            }
            return lastChunkByPos = super.get(key);
        }

        @Override
        public Chunk remove(long key) {
            if (lastChunkByPos != null && key == lastChunkByPos.chunkKey) {
                lastChunkByPos = null;
            }
            return super.remove(key);
        }
    }; // CraftBukkit
    // Paper end
    public final WorldServer field_73251_h;

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkGenerator chunkgenerator) {
        this.field_73251_h = worldserver;
        this.field_73247_e = ichunkloader;
        this.field_186029_c = chunkgenerator;
    }

    public Collection<Chunk> func_189548_a() {
        return this.field_73244_f.values();
    }

    public void func_189549_a(Chunk chunk) {
        if (this.field_73251_h.field_73011_w.func_186056_c(chunk.field_76635_g, chunk.field_76647_h)) {
            this.field_73248_b.add(Long.valueOf(ChunkPos.func_77272_a(chunk.field_76635_g, chunk.field_76647_h)));
            chunk.field_189550_d = true;
        }

    }

    public void func_73240_a() {
        ObjectIterator objectiterator = this.field_73244_f.values().iterator();

        while (objectiterator.hasNext()) {
            Chunk chunk = (Chunk) objectiterator.next();

            this.func_189549_a(chunk);
        }

    }

    @Nullable
    public Chunk func_186026_b(int i, int j) {
        long k = ChunkPos.func_77272_a(i, j);
        Chunk chunk = (Chunk) this.field_73244_f.get(k);

        if (chunk != null) {
            chunk.field_189550_d = false;
        }

        return chunk;
    }

    @Nullable
    public Chunk func_186028_c(int i, int j) {
        Chunk chunk = this.func_186026_b(i, j);

        if (chunk == null) {
            // CraftBukkit start
            AnvilChunkLoader loader = null;

            if (this.field_73247_e instanceof AnvilChunkLoader) {
                loader = (AnvilChunkLoader) this.field_73247_e;
            }
            if (loader != null && loader.func_191063_a(i, j)) {
                chunk = ChunkIOExecutor.syncChunkLoad(field_73251_h, loader, this, i, j);
            }
        }

        return chunk;
    }

    @Nullable
    public Chunk originalGetOrLoadChunkAt(int i, int j) {
        // CraftBukkit end
        Chunk chunk = this.func_186026_b(i, j);

        if (chunk == null) {
            chunk = this.func_73239_e(i, j);
            if (chunk != null) {
                this.field_73244_f.put(ChunkPos.func_77272_a(i, j), chunk);
                chunk.func_76631_c();
                chunk.loadNearby(this, this.field_186029_c, false); // CraftBukkit
            }
        }

        return chunk;
    }

    // CraftBukkit start
    public Chunk getChunkIfLoaded(int x, int z) {
        return field_73244_f.get(ChunkPos.func_77272_a(x, z));
    }
    // CraftBukkit end

    public Chunk func_186025_d(int i, int j) {
        return getChunkAt(i, j, null);
    }

    public Chunk getChunkAt(int i, int j, Runnable runnable) {
        return getChunkAt(i, j, runnable, true);
    }

    public Chunk getChunkAt(int i, int j, Runnable runnable, boolean generate) {
        Chunk chunk = field_73251_h.paperConfig.allowPermaChunkLoaders ? func_186026_b(i, j) : getChunkIfLoaded(i, j); // Paper - Configurable perma chunk loaders
        AnvilChunkLoader loader = null;

        if (this.field_73247_e instanceof AnvilChunkLoader) {
            loader = (AnvilChunkLoader) this.field_73247_e;

        }
        // We can only use the queue for already generated chunks
        if (chunk == null && loader != null && loader.func_191063_a(i, j)) {
            if (runnable != null) {
                ChunkIOExecutor.queueChunkLoad(field_73251_h, loader, this, i, j, runnable);
                return null;
            } else {
                chunk = ChunkIOExecutor.syncChunkLoad(field_73251_h, loader, this, i, j);

                // Paper start - If there was an issue loading the chunk from region, stage1 will fail and stage2 will load it sync
                // all we need to do is fetch an instance
                if (chunk == null) {
                    chunk = getChunkIfLoaded(i, j);
                }
                // Paper end
            }
        } else if (chunk == null && generate) {
            chunk = originalGetChunkAt(i, j);
        }

        // If we didn't load the chunk async and have a callback run it now
        if (runnable != null) {
            runnable.run();
        }

        return chunk;
    }

    public Chunk originalGetChunkAt(int i, int j) {
        Chunk chunk = this.originalGetOrLoadChunkAt(i, j);
        // CraftBukkit end

        if (chunk == null) {
            field_73251_h.timings.syncChunkLoadTimer.startTiming(); // Spigot
            long k = ChunkPos.func_77272_a(i, j);

            try {
                chunk = this.field_186029_c.func_185932_a(i, j);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Chunk to be generated");

                crashreportsystemdetails.func_71507_a("Location", (Object) String.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j)}));
                crashreportsystemdetails.func_71507_a("Position hash", (Object) Long.valueOf(k));
                crashreportsystemdetails.func_71507_a("Generator", (Object) this.field_186029_c);
                throw new ReportedException(crashreport);
            }

            this.field_73244_f.put(k, chunk);
            chunk.func_76631_c();
            chunk.loadNearby(this, this.field_186029_c, true); // CraftBukkit
            field_73251_h.timings.syncChunkLoadTimer.stopTiming(); // Spigot
        }

        return chunk;
    }

    @Nullable
    public Chunk func_73239_e(int i, int j) {
        try {
            Chunk chunk = this.field_73247_e.func_75815_a(this.field_73251_h, i, j);

            if (chunk != null) {
                chunk.func_177432_b(this.field_73251_h.func_82737_E());
                this.field_186029_c.func_180514_a(chunk, i, j);
            }

            return chunk;
        } catch (Exception exception) {
            // Paper start
            String msg = "Couldn\'t load chunk";
            ChunkProviderServer.field_147417_b.error(msg, exception);
            ServerInternalException.reportInternalException(exception);
            // Paper end
            return null;
        }
    }

    public void func_73243_a(Chunk chunk) {
        try {
            // this.chunkLoader.b(this.world, chunk); // Spigot
        } catch (Exception exception) {
            // Paper start
            String msg = "Couldn\'t save entities";
            ChunkProviderServer.field_147417_b.error(msg, exception);
            ServerInternalException.reportInternalException(exception);
            // Paper end
        }

    }

    public void saveChunk(Chunk chunk, boolean unloaded) { // Spigot
        try (co.aikar.timings.Timing timed = field_73251_h.timings.chunkSaveData.startTiming()) {
            chunk.func_177432_b(this.field_73251_h.func_82737_E());
            this.field_73247_e.saveChunk(this.field_73251_h, chunk, unloaded); // Spigot
        } catch (IOException ioexception) {
            // Paper start
            String msg = "Couldn\'t save chunk";
            ChunkProviderServer.field_147417_b.error(msg, ioexception);
            ServerInternalException.reportInternalException(ioexception);
        } catch (MinecraftException exceptionworldconflict) {
            String msg = "Couldn\'t save chunk; already in use by another instance of Minecraft?";
            ChunkProviderServer.field_147417_b.error(msg, exceptionworldconflict);
            ServerInternalException.reportInternalException(exceptionworldconflict);
        }

    }

    public boolean func_186027_a(boolean flag) {
        int i = 0;

        // CraftBukkit start
        // Paper start
        final AnvilChunkLoader chunkLoader = (AnvilChunkLoader) field_73251_h.func_72863_F().field_73247_e;
        final int queueSize = chunkLoader.getQueueSize();

        final long now = System.currentTimeMillis();
        final long timeSince = (now - lastSaveStatPrinted) / 1000;
        final Integer printRateSecs = Integer.getInteger("printSaveStats");
        if (printRateSecs != null && timeSince >= printRateSecs) {
            final String timeStr = "/" + timeSince  +"s";
            final long queuedSaves = chunkLoader.getQueuedSaves();
            long queuedDiff = queuedSaves - lastQueuedSaves;
            lastQueuedSaves = queuedSaves;

            final long processedSaves = chunkLoader.getProcessedSaves();
            long processedDiff = processedSaves - lastProcessedSaves;
            lastProcessedSaves = processedSaves;

            lastSaveStatPrinted = now;
            if (processedDiff > 0 || queueSize > 0 || queuedDiff > 0) {
                System.out.println("[Chunk Save Stats] " + field_73251_h.field_72986_A.func_76065_j() +
                    " - Current: " + queueSize +
                    " - Queued: " + queuedDiff + timeStr +
                    " - Processed: " +processedDiff + timeStr
                );
            }
        }

        if (queueSize > field_73251_h.paperConfig.queueSizeAutoSaveThreshold){
            return false;
        }
        final int autoSaveLimit = field_73251_h.paperConfig.maxAutoSaveChunksPerTick;
        // Paper end
        Iterator iterator = this.field_73244_f.values().iterator();
        while (iterator.hasNext()) {
            Chunk chunk = (Chunk) iterator.next();
            // CraftBukkit end

            if (flag) {
                this.func_73243_a(chunk);
            }

            if (chunk.func_76601_a(flag)) {
                this.saveChunk(chunk, false); // Spigot
                chunk.func_177427_f(false);
                ++i;
                if (!flag && i >= autoSaveLimit) { // Spigot - // Paper - Incremental Auto Save - cap max per tick
                    return false;
                }
            }
        }

        return true;
    }

    public void func_104112_b() {
        this.field_73247_e.func_75818_b();
    }

    private static final double UNLOAD_QUEUE_RESIZE_FACTOR = 0.96;

    public boolean func_73156_b() {
        if (!this.field_73251_h.field_73058_d) {
            if (!this.field_73248_b.isEmpty()) {
                // Spigot start
                org.spigotmc.SlackActivityAccountant activityAccountant = this.field_73251_h.func_73046_m().slackActivityAccountant;
                activityAccountant.startActivity(0.5);
                int targetSize = Math.min(this.field_73248_b.size() - 100,  (int) (this.field_73248_b.size() * UNLOAD_QUEUE_RESIZE_FACTOR)); // Paper - Make more aggressive
                // Spigot end

                Iterator iterator = this.field_73248_b.iterator();

                while (iterator.hasNext()) { // Spigot
                    Long olong = (Long) iterator.next();
                    iterator.remove(); // Spigot
                    Chunk chunk = (Chunk) this.field_73244_f.get(olong);

                    if (chunk != null && chunk.field_189550_d) {
                        // CraftBukkit start - move unload logic to own method
                        chunk.setShouldUnload(false); // Paper
                        if (!unloadChunk(chunk, true)) {
                            continue;
                        }
                        // CraftBukkit end

                        // Spigot start
                        if (this.field_73248_b.size() <= targetSize && activityAccountant.activityTimeIsExhausted()) {
                            break;
                        }
                        // Spigot end
                    }
                }

                activityAccountant.endActivity(); // Spigot
            }
            // Paper start - delayed chunk unloads
            long now = System.currentTimeMillis();
            long unloadAfter = field_73251_h.paperConfig.delayChunkUnloadsBy;
            if (unloadAfter > 0) {
                //noinspection Convert2streamapi
                for (Chunk chunk : field_73244_f.values()) {
                    if (chunk.scheduledForUnload != null && now - chunk.scheduledForUnload > unloadAfter) {
                        chunk.scheduledForUnload = null;
                        func_189549_a(chunk);
                    }
                }
            }
            // Paper end

            this.field_73247_e.func_75817_a();
        }

        return false;
    }

    // CraftBukkit start
    public boolean unloadChunk(Chunk chunk, boolean save) {
        ChunkUnloadEvent event = new ChunkUnloadEvent(chunk.bukkitChunk, save);
        this.field_73251_h.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        save = event.isSaveChunk();
        chunk.lightingQueue.processUnload(); // Paper

        // Update neighbor counts
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                Chunk neighbor = this.getChunkIfLoaded(chunk.field_76635_g + x, chunk.field_76647_h + z);
                if (neighbor != null) {
                    neighbor.setNeighborUnloaded(-x, -z);
                    chunk.setNeighborUnloaded(x, z);
                }
            }
        }
        // Moved from unloadChunks above
        chunk.func_76623_d();
        if (save) {
            this.saveChunk(chunk, true); // Spigot
            this.func_73243_a(chunk);
        }
        this.field_73244_f.remove(chunk.chunkKey);
        return true;
    }
    // CraftBukkit end

    public boolean func_73157_c() {
        return !this.field_73251_h.field_73058_d;
    }

    public String func_73148_d() {
        return "ServerChunkCache: " + this.field_73244_f.size() + " Drop: " + this.field_73248_b.size();
    }

    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        return this.field_186029_c.func_177458_a(enumcreaturetype, blockposition);
    }

    @Nullable
    public BlockPos func_180513_a(World world, String s, BlockPos blockposition, boolean flag) {
        return this.field_186029_c.func_180513_a(world, s, blockposition, flag);
    }

    public boolean func_193413_a(World world, String s, BlockPos blockposition) {
        return this.field_186029_c.func_193414_a(world, s, blockposition);
    }

    public int func_73152_e() {
        return this.field_73244_f.size();
    }

    public boolean func_73149_a(int i, int j) {
        return this.field_73244_f.containsKey(ChunkPos.func_77272_a(i, j));
    }

    public boolean func_191062_e(int i, int j) {
        return this.field_73244_f.containsKey(ChunkPos.func_77272_a(i, j)) || this.field_73247_e.func_191063_a(i, j);
    }
}

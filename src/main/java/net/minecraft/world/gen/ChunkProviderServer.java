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

    private static final Logger LOGGER = LogManager.getLogger();
    public final Set<Long> droppedChunksSet = Sets.newHashSet();
    public final IChunkGenerator chunkGenerator;
    private final IChunkLoader chunkLoader;
    // Paper start - chunk save stats
    private long lastQueuedSaves = 0L; // Paper
    private long lastProcessedSaves = 0L; // Paper
    private long lastSaveStatPrinted = System.currentTimeMillis();
    // Paper end
    // Paper start
    protected Chunk lastChunkByPos = null;
    public Long2ObjectOpenHashMap<Chunk> id2ChunkMap = new Long2ObjectOpenHashMap<Chunk>(8192) {

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
    public final WorldServer world;

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkGenerator chunkgenerator) {
        this.world = worldserver;
        this.chunkLoader = ichunkloader;
        this.chunkGenerator = chunkgenerator;
    }

    public Collection<Chunk> getLoadedChunks() {
        return this.id2ChunkMap.values();
    }

    public void queueUnload(Chunk chunk) {
        if (this.world.provider.canDropChunk(chunk.x, chunk.z)) {
            this.droppedChunksSet.add(Long.valueOf(ChunkPos.asLong(chunk.x, chunk.z)));
            chunk.unloadQueued = true;
        }

    }

    public void queueUnloadAll() {
        ObjectIterator objectiterator = this.id2ChunkMap.values().iterator();

        while (objectiterator.hasNext()) {
            Chunk chunk = (Chunk) objectiterator.next();

            this.queueUnload(chunk);
        }

    }

    @Nullable
    public Chunk getLoadedChunk(int i, int j) {
        long k = ChunkPos.asLong(i, j);
        Chunk chunk = (Chunk) this.id2ChunkMap.get(k);

        if (chunk != null) {
            chunk.unloadQueued = false;
        }

        return chunk;
    }

    @Nullable
    public Chunk loadChunk(int i, int j) {
        Chunk chunk = this.getLoadedChunk(i, j);

        if (chunk == null) {
            // CraftBukkit start
            AnvilChunkLoader loader = null;

            if (this.chunkLoader instanceof AnvilChunkLoader) {
                loader = (AnvilChunkLoader) this.chunkLoader;
            }
            if (loader != null && loader.isChunkGeneratedAt(i, j)) {
                chunk = ChunkIOExecutor.syncChunkLoad(world, loader, this, i, j);
            }
        }

        return chunk;
    }

    @Nullable
    public Chunk originalGetOrLoadChunkAt(int i, int j) {
        // CraftBukkit end
        Chunk chunk = this.getLoadedChunk(i, j);

        if (chunk == null) {
            chunk = this.loadChunkFromFile(i, j);
            if (chunk != null) {
                this.id2ChunkMap.put(ChunkPos.asLong(i, j), chunk);
                chunk.onLoad();
                chunk.loadNearby(this, this.chunkGenerator, false); // CraftBukkit
            }
        }

        return chunk;
    }

    // CraftBukkit start
    public Chunk getChunkIfLoaded(int x, int z) {
        return id2ChunkMap.get(ChunkPos.asLong(x, z));
    }
    // CraftBukkit end

    public Chunk provideChunk(int i, int j) {
        return getChunkAt(i, j, null);
    }

    public Chunk getChunkAt(int i, int j, Runnable runnable) {
        return getChunkAt(i, j, runnable, true);
    }

    public Chunk getChunkAt(int i, int j, Runnable runnable, boolean generate) {
        Chunk chunk = world.paperConfig.allowPermaChunkLoaders ? getLoadedChunk(i, j) : getChunkIfLoaded(i, j); // Paper - Configurable perma chunk loaders
        AnvilChunkLoader loader = null;

        if (this.chunkLoader instanceof AnvilChunkLoader) {
            loader = (AnvilChunkLoader) this.chunkLoader;

        }
        // We can only use the queue for already generated chunks
        if (chunk == null && loader != null && loader.isChunkGeneratedAt(i, j)) {
            if (runnable != null) {
                ChunkIOExecutor.queueChunkLoad(world, loader, this, i, j, runnable);
                return null;
            } else {
                chunk = ChunkIOExecutor.syncChunkLoad(world, loader, this, i, j);

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
            world.timings.syncChunkLoadTimer.startTiming(); // Spigot
            long k = ChunkPos.asLong(i, j);

            try {
                chunk = this.chunkGenerator.generateChunk(i, j);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Chunk to be generated");

                crashreportsystemdetails.addCrashSection("Location", (Object) String.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j)}));
                crashreportsystemdetails.addCrashSection("Position hash", (Object) Long.valueOf(k));
                crashreportsystemdetails.addCrashSection("Generator", (Object) this.chunkGenerator);
                throw new ReportedException(crashreport);
            }

            this.id2ChunkMap.put(k, chunk);
            chunk.onLoad();
            chunk.loadNearby(this, this.chunkGenerator, true); // CraftBukkit
            world.timings.syncChunkLoadTimer.stopTiming(); // Spigot
        }

        return chunk;
    }

    @Nullable
    public Chunk loadChunkFromFile(int i, int j) {
        try {
            Chunk chunk = this.chunkLoader.loadChunk(this.world, i, j);

            if (chunk != null) {
                chunk.setLastSaveTime(this.world.getTotalWorldTime());
                this.chunkGenerator.recreateStructures(chunk, i, j);
            }

            return chunk;
        } catch (Exception exception) {
            // Paper start
            String msg = "Couldn\'t load chunk";
            ChunkProviderServer.LOGGER.error(msg, exception);
            ServerInternalException.reportInternalException(exception);
            // Paper end
            return null;
        }
    }

    public void saveChunkExtraData(Chunk chunk) {
        try {
            // this.chunkLoader.b(this.world, chunk); // Spigot
        } catch (Exception exception) {
            // Paper start
            String msg = "Couldn\'t save entities";
            ChunkProviderServer.LOGGER.error(msg, exception);
            ServerInternalException.reportInternalException(exception);
            // Paper end
        }

    }

    public void saveChunk(Chunk chunk, boolean unloaded) { // Spigot
        try (co.aikar.timings.Timing timed = world.timings.chunkSaveData.startTiming()) {
            chunk.setLastSaveTime(this.world.getTotalWorldTime());
            this.chunkLoader.saveChunk(this.world, chunk, unloaded); // Spigot
        } catch (IOException ioexception) {
            // Paper start
            String msg = "Couldn\'t save chunk";
            ChunkProviderServer.LOGGER.error(msg, ioexception);
            ServerInternalException.reportInternalException(ioexception);
        } catch (MinecraftException exceptionworldconflict) {
            String msg = "Couldn\'t save chunk; already in use by another instance of Minecraft?";
            ChunkProviderServer.LOGGER.error(msg, exceptionworldconflict);
            ServerInternalException.reportInternalException(exceptionworldconflict);
        }

    }

    public boolean saveChunks(boolean flag) {
        int i = 0;

        // CraftBukkit start
        // Paper start
        final AnvilChunkLoader chunkLoader = (AnvilChunkLoader) world.getChunkProvider().chunkLoader;
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
                System.out.println("[Chunk Save Stats] " + world.worldInfo.getWorldName() +
                    " - Current: " + queueSize +
                    " - Queued: " + queuedDiff + timeStr +
                    " - Processed: " +processedDiff + timeStr
                );
            }
        }

        if (queueSize > world.paperConfig.queueSizeAutoSaveThreshold){
            return false;
        }
        final int autoSaveLimit = world.paperConfig.maxAutoSaveChunksPerTick;
        // Paper end
        Iterator iterator = this.id2ChunkMap.values().iterator();
        while (iterator.hasNext()) {
            Chunk chunk = (Chunk) iterator.next();
            // CraftBukkit end

            if (flag) {
                this.saveChunkExtraData(chunk);
            }

            if (chunk.needsSaving(flag)) {
                this.saveChunk(chunk, false); // Spigot
                chunk.setModified(false);
                ++i;
                if (!flag && i >= autoSaveLimit) { // Spigot - // Paper - Incremental Auto Save - cap max per tick
                    return false;
                }
            }
        }

        return true;
    }

    public void flushToDisk() {
        this.chunkLoader.flush();
    }

    private static final double UNLOAD_QUEUE_RESIZE_FACTOR = 0.96;

    public boolean tick() {
        if (!this.world.disableLevelSaving) {
            if (!this.droppedChunksSet.isEmpty()) {
                // Spigot start
                org.spigotmc.SlackActivityAccountant activityAccountant = this.world.getMinecraftServer().slackActivityAccountant;
                activityAccountant.startActivity(0.5);
                int targetSize = Math.min(this.droppedChunksSet.size() - 100,  (int) (this.droppedChunksSet.size() * UNLOAD_QUEUE_RESIZE_FACTOR)); // Paper - Make more aggressive
                // Spigot end

                Iterator iterator = this.droppedChunksSet.iterator();

                while (iterator.hasNext()) { // Spigot
                    Long olong = (Long) iterator.next();
                    iterator.remove(); // Spigot
                    Chunk chunk = (Chunk) this.id2ChunkMap.get(olong);

                    if (chunk != null && chunk.unloadQueued) {
                        // CraftBukkit start - move unload logic to own method
                        chunk.setShouldUnload(false); // Paper
                        if (!unloadChunk(chunk, true)) {
                            continue;
                        }
                        // CraftBukkit end

                        // Spigot start
                        if (this.droppedChunksSet.size() <= targetSize && activityAccountant.activityTimeIsExhausted()) {
                            break;
                        }
                        // Spigot end
                    }
                }

                activityAccountant.endActivity(); // Spigot
            }
            // Paper start - delayed chunk unloads
            long now = System.currentTimeMillis();
            long unloadAfter = world.paperConfig.delayChunkUnloadsBy;
            if (unloadAfter > 0) {
                //noinspection Convert2streamapi
                for (Chunk chunk : id2ChunkMap.values()) {
                    if (chunk.scheduledForUnload != null && now - chunk.scheduledForUnload > unloadAfter) {
                        chunk.scheduledForUnload = null;
                        queueUnload(chunk);
                    }
                }
            }
            // Paper end

            this.chunkLoader.chunkTick();
        }

        return false;
    }

    // CraftBukkit start
    public boolean unloadChunk(Chunk chunk, boolean save) {
        ChunkUnloadEvent event = new ChunkUnloadEvent(chunk.bukkitChunk, save);
        this.world.getServer().getPluginManager().callEvent(event);
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

                Chunk neighbor = this.getChunkIfLoaded(chunk.x + x, chunk.z + z);
                if (neighbor != null) {
                    neighbor.setNeighborUnloaded(-x, -z);
                    chunk.setNeighborUnloaded(x, z);
                }
            }
        }
        // Moved from unloadChunks above
        chunk.onUnload();
        if (save) {
            this.saveChunk(chunk, true); // Spigot
            this.saveChunkExtraData(chunk);
        }
        this.id2ChunkMap.remove(chunk.chunkKey);
        return true;
    }
    // CraftBukkit end

    public boolean canSave() {
        return !this.world.disableLevelSaving;
    }

    public String makeString() {
        return "ServerChunkCache: " + this.id2ChunkMap.size() + " Drop: " + this.droppedChunksSet.size();
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        return this.chunkGenerator.getPossibleCreatures(enumcreaturetype, blockposition);
    }

    @Nullable
    public BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag) {
        return this.chunkGenerator.getNearestStructurePos(world, s, blockposition, flag);
    }

    public boolean isInsideStructure(World world, String s, BlockPos blockposition) {
        return this.chunkGenerator.isInsideStructure(world, s, blockposition);
    }

    public int getLoadedChunkCount() {
        return this.id2ChunkMap.size();
    }

    public boolean chunkExists(int i, int j) {
        return this.id2ChunkMap.containsKey(ChunkPos.asLong(i, j));
    }

    public boolean isChunkGeneratedAt(int i, int j) {
        return this.id2ChunkMap.containsKey(ChunkPos.asLong(i, j)) || this.chunkLoader.isChunkGeneratedAt(i, j);
    }
}

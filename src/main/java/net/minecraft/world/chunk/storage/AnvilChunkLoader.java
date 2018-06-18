package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.ChunkRegionLoader.QueuedChunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.spigotmc.SupplierUtils;

// Spigot start
import java.util.function.Supplier;
import org.spigotmc.SupplierUtils;
// Spigot end

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO {

    private ConcurrentLinkedQueue<QueuedChunk> queue = new ConcurrentLinkedQueue<>(); // Paper - Chunk queue improvements
    private final Object lock = new Object(); // Paper - Chunk queue improvements
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ChunkPos, Supplier<NBTTagCompound>> chunksToSave = Maps.newConcurrentMap(); // Spigot
    // CraftBukkit
    // private final Set<ChunkCoordIntPair> c = Collections.newSetFromMap(Maps.newConcurrentMap());
    private final File chunkSaveLocation;
    private final DataFixer fixer;
    // private boolean f;
    // CraftBukkit
    private static final double SAVE_QUEUE_TARGET_SIZE = 625; // Spigot

    public AnvilChunkLoader(File file, DataFixer dataconvertermanager) {
        this.chunkSaveLocation = file;
        this.fixer = dataconvertermanager;
    }

    // Paper start
    private long queuedSaves = 0;
    private final java.util.concurrent.atomic.AtomicLong processedSaves = new java.util.concurrent.atomic.AtomicLong(0L);
    public int getQueueSize() { return queue.size(); }
    public long getQueuedSaves() { return queuedSaves; }
    public long getProcessedSaves() { return processedSaves.longValue(); }
    // Paper end

    // CraftBukkit start - Add async variant, provide compatibility
    @Nullable
    public Chunk loadChunk(World world, int i, int j) throws IOException {
        world.timings.syncChunkLoadDataTimer.startTiming(); // Spigot
        Object[] data = loadChunk(world, i, j);
        world.timings.syncChunkLoadDataTimer.stopTiming(); // Spigot
        if (data != null) {
            Chunk chunk = (Chunk) data[0];
            NBTTagCompound nbttagcompound = (NBTTagCompound) data[1];
            loadEntities(chunk, nbttagcompound.getCompoundTag("Level"), world);
            return chunk;
        }

        return null;
    }

    public Object[] loadChunk(World world, int i, int j) throws IOException {
        // CraftBukkit end
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);
        NBTTagCompound nbttagcompound = SupplierUtils.getIfExists(this.chunksToSave.get(chunkcoordintpair)); // Spigot

        if (nbttagcompound == null) {
            // CraftBukkit start
            nbttagcompound = RegionFileCache.d(this.chunkSaveLocation, i, j);

            if (nbttagcompound == null) {
                return null;
            }

            nbttagcompound = this.fixer.process((IFixType) FixTypes.CHUNK, nbttagcompound);
            // CraftBukkit end
        }

        return this.a(world, i, j, nbttagcompound);
    }

    public boolean isChunkGeneratedAt(int i, int j) {
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);
        Supplier<NBTTagCompound> nbttagcompound = this.chunksToSave.get(chunkcoordintpair); // Spigot

        return nbttagcompound != null ? true : RegionFileCache.chunkExists(this.chunkSaveLocation, i, j);
    }

    @Nullable
    protected Object[] a(World world, int i, int j, NBTTagCompound nbttagcompound) { // CraftBukkit - return Chunk -> Object[]
        if (!nbttagcompound.hasKey("Level", 10)) {
            AnvilChunkLoader.LOGGER.error("Chunk file at {},{} is missing level data, skipping", Integer.valueOf(i), Integer.valueOf(j));
            return null;
        } else {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");

            if (!nbttagcompound1.hasKey("Sections", 9)) {
                AnvilChunkLoader.LOGGER.error("Chunk file at {},{} is missing block data, skipping", Integer.valueOf(i), Integer.valueOf(j));
                return null;
            } else {
                Chunk chunk = this.readChunkFromNBT(world, nbttagcompound1);

                if (!chunk.isAtLocation(i, j)) {
                    AnvilChunkLoader.LOGGER.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(chunk.x), Integer.valueOf(chunk.z));
                    nbttagcompound1.setInteger("xPos", i);
                    nbttagcompound1.setInteger("zPos", j);

                    // CraftBukkit start - Have to move tile entities since we don't load them at this stage
                    NBTTagList tileEntities = nbttagcompound.getCompoundTag("Level").getTagList("TileEntities", 10);
                    if (tileEntities != null) {
                        for (int te = 0; te < tileEntities.tagCount(); te++) {
                            NBTTagCompound tileEntity = (NBTTagCompound) tileEntities.getCompoundTagAt(te);
                            int x = tileEntity.getInteger("x") - chunk.x * 16;
                            int z = tileEntity.getInteger("z") - chunk.z * 16;
                            tileEntity.setInteger("x", i * 16 + x);
                            tileEntity.setInteger("z", j * 16 + z);
                        }
                    }
                    // CraftBukkit end
                    chunk = this.readChunkFromNBT(world, nbttagcompound1);
                }

                // CraftBukkit start
                Object[] data = new Object[2];
                data[0] = chunk;
                data[1] = nbttagcompound;
                return data;
                // CraftBukkit end
            }
        }
    }

    public void saveChunk(World world, Chunk chunk, boolean unloaded) throws IOException, MinecraftException { // Spigot
        world.checkSessionLock();

        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound.setTag("Level", nbttagcompound1);
            nbttagcompound.setInteger("DataVersion", 1343);

            // Spigot start
            final long worldTime = world.getTotalWorldTime();
            final boolean worldHasSkyLight = world.provider.hasSkyLight();
            saveEntities(nbttagcompound1, chunk, world);
            Supplier<NBTTagCompound> completion = new Supplier<NBTTagCompound>() {
                public NBTTagCompound get() {
                    saveBody(nbttagcompound1, chunk, worldTime, worldHasSkyLight);
                    return nbttagcompound;
                }
            };

            this.a(chunk.getPos(), SupplierUtils.createUnivaluedSupplier(completion, unloaded && this.chunksToSave.size() < SAVE_QUEUE_TARGET_SIZE));
            // Spigot end
        } catch (Exception exception) {
            AnvilChunkLoader.LOGGER.error("Failed to save chunk", exception);
        }

    }

    protected void a(ChunkPos chunkcoordintpair, Supplier<NBTTagCompound> nbttagcompound) { // Spigot
        // CraftBukkit
        // if (!this.c.contains(chunkcoordintpair))
        synchronized (lock) {  // Paper - Chunk queue improvements
            this.chunksToSave.put(chunkcoordintpair, nbttagcompound);
        }
        queuedSaves++; // Paper
        queue.add(new QueuedChunk(chunkcoordintpair, nbttagcompound)); // Paper - Chunk queue improvements

        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }

    public boolean writeNextIO() {
        // CraftBukkit start
        return this.processSaveQueueEntry(false);
    }

    private synchronized boolean processSaveQueueEntry(boolean logCompletion) {
        // CraftBukkit start
        // Paper start - Chunk queue improvements
        QueuedChunk chunk = queue.poll();
        if (chunk == null) {
        // Paper - end
            if (logCompletion) {
                // CraftBukkit end
                AnvilChunkLoader.LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.chunkSaveLocation.getName());
            }

            return false;
        } else {
            ChunkPos chunkcoordintpair = chunk.coords; // Paper - Chunk queue improvements
            processedSaves.incrementAndGet(); // Paper

            boolean flag;

            try {
                // this.c.add(chunkcoordintpair);
                NBTTagCompound nbttagcompound = SupplierUtils.getIfExists(chunk.compoundSupplier); // Spigot // Paper
                // CraftBukkit

                if (nbttagcompound != null) {
                    int attempts = 0; Exception laste = null; while (attempts++ < 5) { // Paper
                    try {
                        this.writeChunkData(chunkcoordintpair, nbttagcompound);
                        laste = null; break; // Paper
                    } catch (Exception exception) {
                        //ChunkRegionLoader.a.error("Failed to save chunk", exception); // Paper
                        laste = exception; // Paper
                    }
                    try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();} } // Paper
                    if (laste != null) { com.destroystokyo.paper.exception.ServerInternalException.reportInternalException(laste); MinecraftServer.LOGGER.error("Failed to save chunk", laste); } // Paper
                }
                synchronized (lock) { if (this.chunksToSave.get(chunkcoordintpair) == chunk.compoundSupplier) { this.chunksToSave.remove(chunkcoordintpair); } }// Paper - This will not equal if a newer version is still pending

                flag = true;
            } finally {
                //this.b.remove(chunkcoordintpair, value); // CraftBukkit // Spigot // Paper
            }

            return flag;
        }
    }

    private void writeChunkData(ChunkPos chunkcoordintpair, NBTTagCompound nbttagcompound) throws IOException {
        // CraftBukkit start
        RegionFileCache.e(this.chunkSaveLocation, chunkcoordintpair.x, chunkcoordintpair.z, nbttagcompound);

        /*
        NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) dataoutputstream);
        dataoutputstream.close();
        */
        // CraftBukkit end
    }

    public void saveExtraChunkData(World world, Chunk chunk) throws IOException {}

    public void chunkTick() {}

    public void flush() {
        try {
            // this.f = true; // CraftBukkit

            while (true) {
                if (this.processSaveQueueEntry(true)) { // CraftBukkit
                    continue;
                }
                break; // CraftBukkit - Fix infinite loop when saving chunks
            }
        } finally {
            // this.f = false; // CraftBukkit
        }

    }

    public static void registerFixes(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.CHUNK, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.hasKey("Level", 10)) {
                    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");
                    NBTTagList nbttaglist;
                    int j;

                    if (nbttagcompound1.hasKey("Entities", 9)) {
                        nbttaglist = nbttagcompound1.getTagList("Entities", 10);

                        for (j = 0; j < nbttaglist.tagCount(); ++j) {
                            nbttaglist.set(j, dataconverter.process(FixTypes.ENTITY, (NBTTagCompound) nbttaglist.get(j), i));
                        }
                    }

                    if (nbttagcompound1.hasKey("TileEntities", 9)) {
                        nbttaglist = nbttagcompound1.getTagList("TileEntities", 10);

                        for (j = 0; j < nbttaglist.tagCount(); ++j) {
                            nbttaglist.set(j, dataconverter.process(FixTypes.BLOCK_ENTITY, (NBTTagCompound) nbttaglist.get(j), i));
                        }
                    }
                }

                return nbttagcompound;
            }
        });
    }

    private static void saveBody(NBTTagCompound nbttagcompound, Chunk chunk, long worldTime, boolean worldHasSkyLight) { // Spigot
        nbttagcompound.setInteger("xPos", chunk.x);
        nbttagcompound.setInteger("zPos", chunk.z);
        nbttagcompound.setLong("LastUpdate", worldTime); // Spigot
        nbttagcompound.setIntArray("HeightMap", chunk.getHeightMap());
        nbttagcompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated());
        nbttagcompound.setBoolean("LightPopulated", chunk.isLightPopulated());
        nbttagcompound.setLong("InhabitedTime", chunk.getInhabitedTime());
        ExtendedBlockStorage[] achunksection = chunk.getBlockStorageArray();
        NBTTagList nbttaglist = new NBTTagList();
        boolean flag = worldHasSkyLight; // Spigot
        ExtendedBlockStorage[] achunksection1 = achunksection;
        int i = achunksection.length;

        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j) {
            ExtendedBlockStorage chunksection = achunksection1[j];

            if (chunksection != Chunk.NULL_BLOCK_STORAGE) {
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte) (chunksection.getYLocation() >> 4 & 255));
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = chunksection.getData().getDataForNBT(abyte, nibblearray);

                nbttagcompound1.setByteArray("Blocks", abyte);
                nbttagcompound1.setByteArray("Data", nibblearray.getData());
                if (nibblearray1 != null) {
                    nbttagcompound1.setByteArray("Add", nibblearray1.getData());
                }

                nbttagcompound1.setByteArray("BlockLight", chunksection.getBlockLight().getData());
                if (flag) {
                    nbttagcompound1.setByteArray("SkyLight", chunksection.getSkyLight().getData());
                } else {
                    nbttagcompound1.setByteArray("SkyLight", new byte[chunksection.getBlockLight().getData().length]);
                }

                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Sections", nbttaglist);
        nbttagcompound.setByteArray("Biomes", chunk.getBiomeArray());

        // Spigot start - End this method here and split off entity saving to another method
    }

    private static void saveEntities(NBTTagCompound nbttagcompound, Chunk chunk, World world) {
        int i;
        NBTTagCompound nbttagcompound1;
        // Spigot end

        chunk.setHasEntities(false);
        NBTTagList nbttaglist1 = new NBTTagList();

        Iterator iterator;

        for (i = 0; i < chunk.getEntitySlices().length; ++i) {
            iterator = chunk.getEntitySlices()[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                nbttagcompound1 = new NBTTagCompound();
                if (entity.writeToNBTOptional(nbttagcompound1)) {
                    chunk.setHasEntities(true);
                    nbttaglist1.appendTag(nbttagcompound1);
                }
            }
        }

        nbttagcompound.setTag("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        iterator = chunk.getTileEntityMap().values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());
            nbttaglist2.appendTag(nbttagcompound1);
        }

        nbttagcompound.setTag("TileEntities", nbttaglist2);
        List list = world.getPendingBlockUpdates(chunk, false);

        if (list != null) {
            long k = world.getTotalWorldTime();
            NBTTagList nbttaglist3 = new NBTTagList();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator1.next();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                ResourceLocation minecraftkey = (ResourceLocation) Block.REGISTRY.getNameForObject(nextticklistentry.getBlock());

                nbttagcompound2.setString("i", minecraftkey == null ? "" : minecraftkey.toString());
                nbttagcompound2.setInteger("x", nextticklistentry.position.getX());
                nbttagcompound2.setInteger("y", nextticklistentry.position.getY());
                nbttagcompound2.setInteger("z", nextticklistentry.position.getZ());
                nbttagcompound2.setInteger("t", (int) (nextticklistentry.scheduledTime - k));
                nbttagcompound2.setInteger("p", nextticklistentry.priority);
                nbttaglist3.appendTag(nbttagcompound2);
            }

            nbttagcompound.setTag("TileTicks", nbttaglist3);
        }

    }

    private Chunk readChunkFromNBT(World world, NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInteger("xPos");
        int j = nbttagcompound.getInteger("zPos");
        Chunk chunk = new Chunk(world, i, j);

        chunk.setHeightMap(nbttagcompound.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(nbttagcompound.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(nbttagcompound.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(nbttagcompound.getLong("InhabitedTime"));
        NBTTagList nbttaglist = nbttagcompound.getTagList("Sections", 10);
        boolean flag = true;
        ExtendedBlockStorage[] achunksection = new ExtendedBlockStorage[16];
        boolean flag1 = world.provider.hasSkyLight();

        for (int k = 0; k < nbttaglist.tagCount(); ++k) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(k);
            byte b0 = nbttagcompound1.getByte("Y");
            ExtendedBlockStorage chunksection = new ExtendedBlockStorage(b0 << 4, flag1, world.chunkPacketBlockController.getPredefinedBlockData(chunk, b0)); // Paper - Anti-Xray - Add predefined block data
            byte[] abyte = nbttagcompound1.getByteArray("Blocks");
            NibbleArray nibblearray = new NibbleArray(nbttagcompound1.getByteArray("Data"));
            NibbleArray nibblearray1 = nbttagcompound1.hasKey("Add", 7) ? new NibbleArray(nbttagcompound1.getByteArray("Add")) : null;

            chunksection.getData().setDataFromNBT(abyte, nibblearray, nibblearray1);
            chunksection.setBlockLight(new NibbleArray(nbttagcompound1.getByteArray("BlockLight")));
            if (flag1) {
                chunksection.setSkyLight(new NibbleArray(nbttagcompound1.getByteArray("SkyLight")));
            }

            chunksection.recalculateRefCounts();
            achunksection[b0] = chunksection;
        }

        chunk.setStorageArrays(achunksection);
        if (nbttagcompound.hasKey("Biomes", 7)) {
            chunk.setBiomeArray(nbttagcompound.getByteArray("Biomes"));
        }

        // CraftBukkit start - End this method here and split off entity loading to another method
        return chunk;
    }

    public void loadEntities(Chunk chunk, NBTTagCompound nbttagcompound, World world) {
        // CraftBukkit end
        world.timings.syncChunkLoadNBTTimer.startTiming(); // Spigot
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Entities", 10);

        for (int l = 0; l < nbttaglist1.tagCount(); ++l) {
            NBTTagCompound nbttagcompound2 = nbttaglist1.getCompoundTagAt(l);

            readChunkEntity(nbttagcompound2, world, chunk);
            chunk.setHasEntities(true);
        }
        NBTTagList nbttaglist2 = nbttagcompound.getTagList("TileEntities", 10);

        for (int i1 = 0; i1 < nbttaglist2.tagCount(); ++i1) {
            NBTTagCompound nbttagcompound3 = nbttaglist2.getCompoundTagAt(i1);
            TileEntity tileentity = TileEntity.create(world, nbttagcompound3);

            if (tileentity != null) {
                chunk.addTileEntity(tileentity);
            }
        }

        if (nbttagcompound.hasKey("TileTicks", 9)) {
            NBTTagList nbttaglist3 = nbttagcompound.getTagList("TileTicks", 10);

            for (int j1 = 0; j1 < nbttaglist3.tagCount(); ++j1) {
                NBTTagCompound nbttagcompound4 = nbttaglist3.getCompoundTagAt(j1);
                Block block;

                if (nbttagcompound4.hasKey("i", 8)) {
                    block = Block.getBlockFromName(nbttagcompound4.getString("i"));
                } else {
                    block = Block.getBlockById(nbttagcompound4.getInteger("i"));
                }

                world.scheduleBlockUpdate(new BlockPos(nbttagcompound4.getInteger("x"), nbttagcompound4.getInteger("y"), nbttagcompound4.getInteger("z")), block, nbttagcompound4.getInteger("t"), nbttagcompound4.getInteger("p"));
            }
        }
        world.timings.syncChunkLoadNBTTimer.stopTiming(); // Spigot

        // return chunk; // CraftBukkit
    }

    @Nullable
    public static Entity readChunkEntity(NBTTagCompound nbttagcompound, World world, Chunk chunk) {
        Entity entity = createEntityFromNBT(nbttagcompound, world);

        if (entity == null) {
            return null;
        } else {
            chunk.addEntity(entity);
            if (nbttagcompound.hasKey("Passengers", 9)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("Passengers", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    Entity entity1 = readChunkEntity(nbttaglist.getCompoundTagAt(i), world, chunk);

                    if (entity1 != null) {
                        entity1.startRiding(entity, true);
                    }
                }
            }

            return entity;
        }
    }

    @Nullable
    // CraftBukkit start
    public static Entity readWorldEntityPos(NBTTagCompound nbttagcompound, World world, double d0, double d1, double d2, boolean flag) {
        return spawnEntity(nbttagcompound, world, d0, d1, d2, flag, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public static Entity spawnEntity(NBTTagCompound nbttagcompound, World world, double d0, double d1, double d2, boolean flag, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {
        // CraftBukkit end
        Entity entity = createEntityFromNBT(nbttagcompound, world);

        if (entity == null) {
            return null;
        } else {
            entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
            if (flag && !world.addEntity(entity, spawnReason)) { // CraftBukkit
                return null;
            } else {
                if (nbttagcompound.hasKey("Passengers", 9)) {
                    NBTTagList nbttaglist = nbttagcompound.getTagList("Passengers", 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        Entity entity1 = readWorldEntityPos(nbttaglist.getCompoundTagAt(i), world, d0, d1, d2, flag);

                        if (entity1 != null) {
                            entity1.startRiding(entity, true);
                        }
                    }
                }

                return entity;
            }
        }
    }

    @Nullable
    protected static Entity createEntityFromNBT(NBTTagCompound nbttagcompound, World world) {
        try {
            return EntityList.createEntityFromNBT(nbttagcompound, world);
        } catch (RuntimeException runtimeexception) {
            return null;
        }
    }

    // CraftBukkit start
    public static void spawnEntity(Entity entity, World world) {
        a(entity, world, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public static void a(Entity entity, World world, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason reason) {
        if (!entity.valid && world.addEntity(entity, reason) && entity.isBeingRidden()) { // Paper
            // CraftBukkit end
            Iterator iterator = entity.getPassengers().iterator();

            while (iterator.hasNext()) {
                Entity entity1 = (Entity) iterator.next();

                spawnEntity(entity1, world);
            }
        }

    }

    @Nullable
    public static Entity readWorldEntity(NBTTagCompound nbttagcompound, World world, boolean flag) {
        Entity entity = createEntityFromNBT(nbttagcompound, world);

        if (entity == null) {
            return null;
        } else if (flag && !world.spawnEntity(entity)) {
            return null;
        } else {
            if (nbttagcompound.hasKey("Passengers", 9)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("Passengers", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    Entity entity1 = readWorldEntity(nbttaglist.getCompoundTagAt(i), world, flag);

                    if (entity1 != null) {
                        entity1.startRiding(entity, true);
                    }
                }
            }

            return entity;
        }
    }

    // Paper start - Chunk queue improvements
    private static class QueuedChunk {
        public ChunkPos coords;
        public Supplier<NBTTagCompound> compoundSupplier;

        public QueuedChunk(ChunkPos coords, Supplier<NBTTagCompound> compoundSupplier) {
            this.coords = coords;
            this.compoundSupplier = compoundSupplier;
        }
    }
    // Paper end
}

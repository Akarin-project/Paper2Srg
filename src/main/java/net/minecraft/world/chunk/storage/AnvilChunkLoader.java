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
    private static final Logger field_151505_a = LogManager.getLogger();
    private final Map<ChunkPos, Supplier<NBTTagCompound>> field_75828_a = Maps.newConcurrentMap(); // Spigot
    // CraftBukkit
    // private final Set<ChunkCoordIntPair> c = Collections.newSetFromMap(Maps.newConcurrentMap());
    private final File field_75825_d;
    private final DataFixer field_193416_e;
    // private boolean f;
    // CraftBukkit
    private static final double SAVE_QUEUE_TARGET_SIZE = 625; // Spigot

    public AnvilChunkLoader(File file, DataFixer dataconvertermanager) {
        this.field_75825_d = file;
        this.field_193416_e = dataconvertermanager;
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
    public Chunk func_75815_a(World world, int i, int j) throws IOException {
        world.timings.syncChunkLoadDataTimer.startTiming(); // Spigot
        Object[] data = loadChunk(world, i, j);
        world.timings.syncChunkLoadDataTimer.stopTiming(); // Spigot
        if (data != null) {
            Chunk chunk = (Chunk) data[0];
            NBTTagCompound nbttagcompound = (NBTTagCompound) data[1];
            loadEntities(chunk, nbttagcompound.func_74775_l("Level"), world);
            return chunk;
        }

        return null;
    }

    public Object[] loadChunk(World world, int i, int j) throws IOException {
        // CraftBukkit end
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);
        NBTTagCompound nbttagcompound = SupplierUtils.getIfExists(this.field_75828_a.get(chunkcoordintpair)); // Spigot

        if (nbttagcompound == null) {
            // CraftBukkit start
            nbttagcompound = RegionFileCache.d(this.field_75825_d, i, j);

            if (nbttagcompound == null) {
                return null;
            }

            nbttagcompound = this.field_193416_e.func_188257_a((IFixType) FixTypes.CHUNK, nbttagcompound);
            // CraftBukkit end
        }

        return this.a(world, i, j, nbttagcompound);
    }

    public boolean func_191063_a(int i, int j) {
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);
        Supplier<NBTTagCompound> nbttagcompound = this.field_75828_a.get(chunkcoordintpair); // Spigot

        return nbttagcompound != null ? true : RegionFileCache.func_191064_f(this.field_75825_d, i, j);
    }

    @Nullable
    protected Object[] a(World world, int i, int j, NBTTagCompound nbttagcompound) { // CraftBukkit - return Chunk -> Object[]
        if (!nbttagcompound.func_150297_b("Level", 10)) {
            AnvilChunkLoader.field_151505_a.error("Chunk file at {},{} is missing level data, skipping", Integer.valueOf(i), Integer.valueOf(j));
            return null;
        } else {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Level");

            if (!nbttagcompound1.func_150297_b("Sections", 9)) {
                AnvilChunkLoader.field_151505_a.error("Chunk file at {},{} is missing block data, skipping", Integer.valueOf(i), Integer.valueOf(j));
                return null;
            } else {
                Chunk chunk = this.func_75823_a(world, nbttagcompound1);

                if (!chunk.func_76600_a(i, j)) {
                    AnvilChunkLoader.field_151505_a.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(chunk.field_76635_g), Integer.valueOf(chunk.field_76647_h));
                    nbttagcompound1.func_74768_a("xPos", i);
                    nbttagcompound1.func_74768_a("zPos", j);

                    // CraftBukkit start - Have to move tile entities since we don't load them at this stage
                    NBTTagList tileEntities = nbttagcompound.func_74775_l("Level").func_150295_c("TileEntities", 10);
                    if (tileEntities != null) {
                        for (int te = 0; te < tileEntities.func_74745_c(); te++) {
                            NBTTagCompound tileEntity = (NBTTagCompound) tileEntities.func_150305_b(te);
                            int x = tileEntity.func_74762_e("x") - chunk.field_76635_g * 16;
                            int z = tileEntity.func_74762_e("z") - chunk.field_76647_h * 16;
                            tileEntity.func_74768_a("x", i * 16 + x);
                            tileEntity.func_74768_a("z", j * 16 + z);
                        }
                    }
                    // CraftBukkit end
                    chunk = this.func_75823_a(world, nbttagcompound1);
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
        world.func_72906_B();

        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound.func_74782_a("Level", nbttagcompound1);
            nbttagcompound.func_74768_a("DataVersion", 1343);

            // Spigot start
            final long worldTime = world.func_82737_E();
            final boolean worldHasSkyLight = world.field_73011_w.func_191066_m();
            saveEntities(nbttagcompound1, chunk, world);
            Supplier<NBTTagCompound> completion = new Supplier<NBTTagCompound>() {
                public NBTTagCompound get() {
                    saveBody(nbttagcompound1, chunk, worldTime, worldHasSkyLight);
                    return nbttagcompound;
                }
            };

            this.a(chunk.func_76632_l(), SupplierUtils.createUnivaluedSupplier(completion, unloaded && this.field_75828_a.size() < SAVE_QUEUE_TARGET_SIZE));
            // Spigot end
        } catch (Exception exception) {
            AnvilChunkLoader.field_151505_a.error("Failed to save chunk", exception);
        }

    }

    protected void a(ChunkPos chunkcoordintpair, Supplier<NBTTagCompound> nbttagcompound) { // Spigot
        // CraftBukkit
        // if (!this.c.contains(chunkcoordintpair))
        synchronized (lock) {  // Paper - Chunk queue improvements
            this.field_75828_a.put(chunkcoordintpair, nbttagcompound);
        }
        queuedSaves++; // Paper
        queue.add(new QueuedChunk(chunkcoordintpair, nbttagcompound)); // Paper - Chunk queue improvements

        ThreadedFileIOBase.func_178779_a().func_75735_a(this);
    }

    public boolean func_75814_c() {
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
                AnvilChunkLoader.field_151505_a.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.field_75825_d.getName());
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
                        this.func_183013_b(chunkcoordintpair, nbttagcompound);
                        laste = null; break; // Paper
                    } catch (Exception exception) {
                        //ChunkRegionLoader.a.error("Failed to save chunk", exception); // Paper
                        laste = exception; // Paper
                    }
                    try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();} } // Paper
                    if (laste != null) { com.destroystokyo.paper.exception.ServerInternalException.reportInternalException(laste); MinecraftServer.field_147145_h.error("Failed to save chunk", laste); } // Paper
                }
                synchronized (lock) { if (this.field_75828_a.get(chunkcoordintpair) == chunk.compoundSupplier) { this.field_75828_a.remove(chunkcoordintpair); } }// Paper - This will not equal if a newer version is still pending

                flag = true;
            } finally {
                //this.b.remove(chunkcoordintpair, value); // CraftBukkit // Spigot // Paper
            }

            return flag;
        }
    }

    private void func_183013_b(ChunkPos chunkcoordintpair, NBTTagCompound nbttagcompound) throws IOException {
        // CraftBukkit start
        RegionFileCache.e(this.field_75825_d, chunkcoordintpair.field_77276_a, chunkcoordintpair.field_77275_b, nbttagcompound);

        /*
        NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) dataoutputstream);
        dataoutputstream.close();
        */
        // CraftBukkit end
    }

    public void func_75819_b(World world, Chunk chunk) throws IOException {}

    public void func_75817_a() {}

    public void func_75818_b() {
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

    public static void func_189889_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.CHUNK, new IDataWalker() {
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.func_150297_b("Level", 10)) {
                    NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Level");
                    NBTTagList nbttaglist;
                    int j;

                    if (nbttagcompound1.func_150297_b("Entities", 9)) {
                        nbttaglist = nbttagcompound1.func_150295_c("Entities", 10);

                        for (j = 0; j < nbttaglist.func_74745_c(); ++j) {
                            nbttaglist.func_150304_a(j, dataconverter.func_188251_a(FixTypes.ENTITY, (NBTTagCompound) nbttaglist.func_179238_g(j), i));
                        }
                    }

                    if (nbttagcompound1.func_150297_b("TileEntities", 9)) {
                        nbttaglist = nbttagcompound1.func_150295_c("TileEntities", 10);

                        for (j = 0; j < nbttaglist.func_74745_c(); ++j) {
                            nbttaglist.func_150304_a(j, dataconverter.func_188251_a(FixTypes.BLOCK_ENTITY, (NBTTagCompound) nbttaglist.func_179238_g(j), i));
                        }
                    }
                }

                return nbttagcompound;
            }
        });
    }

    private static void saveBody(NBTTagCompound nbttagcompound, Chunk chunk, long worldTime, boolean worldHasSkyLight) { // Spigot
        nbttagcompound.func_74768_a("xPos", chunk.field_76635_g);
        nbttagcompound.func_74768_a("zPos", chunk.field_76647_h);
        nbttagcompound.func_74772_a("LastUpdate", worldTime); // Spigot
        nbttagcompound.func_74783_a("HeightMap", chunk.func_177445_q());
        nbttagcompound.func_74757_a("TerrainPopulated", chunk.func_177419_t());
        nbttagcompound.func_74757_a("LightPopulated", chunk.func_177423_u());
        nbttagcompound.func_74772_a("InhabitedTime", chunk.func_177416_w());
        ExtendedBlockStorage[] achunksection = chunk.func_76587_i();
        NBTTagList nbttaglist = new NBTTagList();
        boolean flag = worldHasSkyLight; // Spigot
        ExtendedBlockStorage[] achunksection1 = achunksection;
        int i = achunksection.length;

        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j) {
            ExtendedBlockStorage chunksection = achunksection1[j];

            if (chunksection != Chunk.field_186036_a) {
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.func_74774_a("Y", (byte) (chunksection.func_76662_d() >> 4 & 255));
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = chunksection.func_186049_g().func_186017_a(abyte, nibblearray);

                nbttagcompound1.func_74773_a("Blocks", abyte);
                nbttagcompound1.func_74773_a("Data", nibblearray.func_177481_a());
                if (nibblearray1 != null) {
                    nbttagcompound1.func_74773_a("Add", nibblearray1.func_177481_a());
                }

                nbttagcompound1.func_74773_a("BlockLight", chunksection.func_76661_k().func_177481_a());
                if (flag) {
                    nbttagcompound1.func_74773_a("SkyLight", chunksection.func_76671_l().func_177481_a());
                } else {
                    nbttagcompound1.func_74773_a("SkyLight", new byte[chunksection.func_76661_k().func_177481_a().length]);
                }

                nbttaglist.func_74742_a(nbttagcompound1);
            }
        }

        nbttagcompound.func_74782_a("Sections", nbttaglist);
        nbttagcompound.func_74773_a("Biomes", chunk.func_76605_m());

        // Spigot start - End this method here and split off entity saving to another method
    }

    private static void saveEntities(NBTTagCompound nbttagcompound, Chunk chunk, World world) {
        int i;
        NBTTagCompound nbttagcompound1;
        // Spigot end

        chunk.func_177409_g(false);
        NBTTagList nbttaglist1 = new NBTTagList();

        Iterator iterator;

        for (i = 0; i < chunk.getEntitySlices().length; ++i) {
            iterator = chunk.getEntitySlices()[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                nbttagcompound1 = new NBTTagCompound();
                if (entity.func_70039_c(nbttagcompound1)) {
                    chunk.func_177409_g(true);
                    nbttaglist1.func_74742_a(nbttagcompound1);
                }
            }
        }

        nbttagcompound.func_74782_a("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        iterator = chunk.func_177434_r().values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            nbttagcompound1 = tileentity.func_189515_b(new NBTTagCompound());
            nbttaglist2.func_74742_a(nbttagcompound1);
        }

        nbttagcompound.func_74782_a("TileEntities", nbttaglist2);
        List list = world.func_72920_a(chunk, false);

        if (list != null) {
            long k = world.func_82737_E();
            NBTTagList nbttaglist3 = new NBTTagList();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) iterator1.next();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                ResourceLocation minecraftkey = (ResourceLocation) Block.field_149771_c.func_177774_c(nextticklistentry.func_151351_a());

                nbttagcompound2.func_74778_a("i", minecraftkey == null ? "" : minecraftkey.toString());
                nbttagcompound2.func_74768_a("x", nextticklistentry.field_180282_a.func_177958_n());
                nbttagcompound2.func_74768_a("y", nextticklistentry.field_180282_a.func_177956_o());
                nbttagcompound2.func_74768_a("z", nextticklistentry.field_180282_a.func_177952_p());
                nbttagcompound2.func_74768_a("t", (int) (nextticklistentry.field_77180_e - k));
                nbttagcompound2.func_74768_a("p", nextticklistentry.field_82754_f);
                nbttaglist3.func_74742_a(nbttagcompound2);
            }

            nbttagcompound.func_74782_a("TileTicks", nbttaglist3);
        }

    }

    private Chunk func_75823_a(World world, NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.func_74762_e("xPos");
        int j = nbttagcompound.func_74762_e("zPos");
        Chunk chunk = new Chunk(world, i, j);

        chunk.func_177420_a(nbttagcompound.func_74759_k("HeightMap"));
        chunk.func_177446_d(nbttagcompound.func_74767_n("TerrainPopulated"));
        chunk.func_177421_e(nbttagcompound.func_74767_n("LightPopulated"));
        chunk.func_177415_c(nbttagcompound.func_74763_f("InhabitedTime"));
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Sections", 10);
        boolean flag = true;
        ExtendedBlockStorage[] achunksection = new ExtendedBlockStorage[16];
        boolean flag1 = world.field_73011_w.func_191066_m();

        for (int k = 0; k < nbttaglist.func_74745_c(); ++k) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(k);
            byte b0 = nbttagcompound1.func_74771_c("Y");
            ExtendedBlockStorage chunksection = new ExtendedBlockStorage(b0 << 4, flag1, world.chunkPacketBlockController.getPredefinedBlockData(chunk, b0)); // Paper - Anti-Xray - Add predefined block data
            byte[] abyte = nbttagcompound1.func_74770_j("Blocks");
            NibbleArray nibblearray = new NibbleArray(nbttagcompound1.func_74770_j("Data"));
            NibbleArray nibblearray1 = nbttagcompound1.func_150297_b("Add", 7) ? new NibbleArray(nbttagcompound1.func_74770_j("Add")) : null;

            chunksection.func_186049_g().func_186019_a(abyte, nibblearray, nibblearray1);
            chunksection.func_76659_c(new NibbleArray(nbttagcompound1.func_74770_j("BlockLight")));
            if (flag1) {
                chunksection.func_76666_d(new NibbleArray(nbttagcompound1.func_74770_j("SkyLight")));
            }

            chunksection.func_76672_e();
            achunksection[b0] = chunksection;
        }

        chunk.func_76602_a(achunksection);
        if (nbttagcompound.func_150297_b("Biomes", 7)) {
            chunk.func_76616_a(nbttagcompound.func_74770_j("Biomes"));
        }

        // CraftBukkit start - End this method here and split off entity loading to another method
        return chunk;
    }

    public void loadEntities(Chunk chunk, NBTTagCompound nbttagcompound, World world) {
        // CraftBukkit end
        world.timings.syncChunkLoadNBTTimer.startTiming(); // Spigot
        NBTTagList nbttaglist1 = nbttagcompound.func_150295_c("Entities", 10);

        for (int l = 0; l < nbttaglist1.func_74745_c(); ++l) {
            NBTTagCompound nbttagcompound2 = nbttaglist1.func_150305_b(l);

            func_186050_a(nbttagcompound2, world, chunk);
            chunk.func_177409_g(true);
        }
        NBTTagList nbttaglist2 = nbttagcompound.func_150295_c("TileEntities", 10);

        for (int i1 = 0; i1 < nbttaglist2.func_74745_c(); ++i1) {
            NBTTagCompound nbttagcompound3 = nbttaglist2.func_150305_b(i1);
            TileEntity tileentity = TileEntity.func_190200_a(world, nbttagcompound3);

            if (tileentity != null) {
                chunk.func_150813_a(tileentity);
            }
        }

        if (nbttagcompound.func_150297_b("TileTicks", 9)) {
            NBTTagList nbttaglist3 = nbttagcompound.func_150295_c("TileTicks", 10);

            for (int j1 = 0; j1 < nbttaglist3.func_74745_c(); ++j1) {
                NBTTagCompound nbttagcompound4 = nbttaglist3.func_150305_b(j1);
                Block block;

                if (nbttagcompound4.func_150297_b("i", 8)) {
                    block = Block.func_149684_b(nbttagcompound4.func_74779_i("i"));
                } else {
                    block = Block.func_149729_e(nbttagcompound4.func_74762_e("i"));
                }

                world.func_180497_b(new BlockPos(nbttagcompound4.func_74762_e("x"), nbttagcompound4.func_74762_e("y"), nbttagcompound4.func_74762_e("z")), block, nbttagcompound4.func_74762_e("t"), nbttagcompound4.func_74762_e("p"));
            }
        }
        world.timings.syncChunkLoadNBTTimer.stopTiming(); // Spigot

        // return chunk; // CraftBukkit
    }

    @Nullable
    public static Entity func_186050_a(NBTTagCompound nbttagcompound, World world, Chunk chunk) {
        Entity entity = func_186053_a(nbttagcompound, world);

        if (entity == null) {
            return null;
        } else {
            chunk.func_76612_a(entity);
            if (nbttagcompound.func_150297_b("Passengers", 9)) {
                NBTTagList nbttaglist = nbttagcompound.func_150295_c("Passengers", 10);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    Entity entity1 = func_186050_a(nbttaglist.func_150305_b(i), world, chunk);

                    if (entity1 != null) {
                        entity1.func_184205_a(entity, true);
                    }
                }
            }

            return entity;
        }
    }

    @Nullable
    // CraftBukkit start
    public static Entity func_186054_a(NBTTagCompound nbttagcompound, World world, double d0, double d1, double d2, boolean flag) {
        return spawnEntity(nbttagcompound, world, d0, d1, d2, flag, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public static Entity spawnEntity(NBTTagCompound nbttagcompound, World world, double d0, double d1, double d2, boolean flag, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {
        // CraftBukkit end
        Entity entity = func_186053_a(nbttagcompound, world);

        if (entity == null) {
            return null;
        } else {
            entity.func_70012_b(d0, d1, d2, entity.field_70177_z, entity.field_70125_A);
            if (flag && !world.addEntity(entity, spawnReason)) { // CraftBukkit
                return null;
            } else {
                if (nbttagcompound.func_150297_b("Passengers", 9)) {
                    NBTTagList nbttaglist = nbttagcompound.func_150295_c("Passengers", 10);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                        Entity entity1 = func_186054_a(nbttaglist.func_150305_b(i), world, d0, d1, d2, flag);

                        if (entity1 != null) {
                            entity1.func_184205_a(entity, true);
                        }
                    }
                }

                return entity;
            }
        }
    }

    @Nullable
    protected static Entity func_186053_a(NBTTagCompound nbttagcompound, World world) {
        try {
            return EntityList.func_75615_a(nbttagcompound, world);
        } catch (RuntimeException runtimeexception) {
            return null;
        }
    }

    // CraftBukkit start
    public static void func_186052_a(Entity entity, World world) {
        a(entity, world, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public static void a(Entity entity, World world, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason reason) {
        if (!entity.valid && world.addEntity(entity, reason) && entity.func_184207_aI()) { // Paper
            // CraftBukkit end
            Iterator iterator = entity.func_184188_bt().iterator();

            while (iterator.hasNext()) {
                Entity entity1 = (Entity) iterator.next();

                func_186052_a(entity1, world);
            }
        }

    }

    @Nullable
    public static Entity func_186051_a(NBTTagCompound nbttagcompound, World world, boolean flag) {
        Entity entity = func_186053_a(nbttagcompound, world);

        if (entity == null) {
            return null;
        } else if (flag && !world.func_72838_d(entity)) {
            return null;
        } else {
            if (nbttagcompound.func_150297_b("Passengers", 9)) {
                NBTTagList nbttaglist = nbttagcompound.func_150295_c("Passengers", 10);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    Entity entity1 = func_186051_a(nbttaglist.func_150305_b(i), world, flag);

                    if (entity1 != null) {
                        entity1.func_184205_a(entity, true);
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

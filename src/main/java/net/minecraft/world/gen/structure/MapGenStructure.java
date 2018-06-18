package net.minecraft.world.gen.structure;

import co.aikar.timings.MinecraftTimings;
import co.aikar.timings.Timing;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.storage.WorldSavedData;

public abstract class MapGenStructure extends MapGenBase {

    private final Timing timing = MinecraftTimings.getStructureTiming(this); // Paper
    private MapGenStructureData structureData;
    protected Long2ObjectMap<StructureStart> structureMap = new Long2ObjectOpenHashMap(1024);
    protected Long2ObjectMap<StructureStart> allStructures = new Long2ObjectOpenHashMap(1024); // Paper - Holds ref to structures for every chunk its part of, where as the one above this only holds the vanilla oriented ones.

    public MapGenStructure() {}

    public String getName() { return getStructureName(); } // Paper // OBFHELPER
    public abstract String getStructureName();

    protected final synchronized void recursiveGenerate(World world, final int i, final int j, int k, int l, ChunkPrimer chunksnapshot) {
        this.initializeStructureData(world);
        if (!this.structureMap.containsKey(ChunkPos.asLong(i, j))) {
            this.rand.nextInt();

            try {
                if (this.canSpawnStructureAtCoords(i, j)) {
                    StructureStart structurestart = this.getStructureStart(i, j);

                    populateStructure(structurestart); // Paper
                    this.structureMap.put(ChunkPos.asLong(i, j), structurestart);
                    if (structurestart.isSizeableStructure()) {
                        this.setStructureStart(i, j, structurestart);
                    }
                }

            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Feature being prepared");

                crashreportsystemdetails.addDetail("Is feature chunk", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return MapGenStructure.this.canSpawnStructureAtCoords(i, j) ? "True" : "False";
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                crashreportsystemdetails.addCrashSection("Chunk location", (Object) String.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j)}));
                crashreportsystemdetails.addDetail("Chunk pos hash", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return String.valueOf(ChunkPos.asLong(i, j));
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                crashreportsystemdetails.addDetail("Structure type", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    public synchronized boolean generateStructure(World world, Random random, ChunkPos chunkcoordintpair) {
        timing.startTiming(); // Paper
        this.initializeStructureData(world);
        int i = (chunkcoordintpair.x << 4) + 8;
        int j = (chunkcoordintpair.z << 4) + 8;
        boolean flag = false;
        ObjectIterator objectiterator = this.structureMap.values().iterator();

        while (objectiterator.hasNext()) {
            StructureStart structurestart = (StructureStart) objectiterator.next();

            if (structurestart.isSizeableStructure() && structurestart.isValidForPostProcess(chunkcoordintpair) && structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)) {
                structurestart.generateStructure(world, random, new StructureBoundingBox(i, j, i + 15, j + 15));
                structurestart.notifyPostProcessAt(chunkcoordintpair);
                flag = true;
                this.setStructureStart(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
            }
        }
        timing.stopTiming(); // Paper

        return flag;
    }

    public boolean isInsideStructure(BlockPos blockposition) {
        if (this.world == null) {
            return false;
        } else {
            this.initializeStructureData(this.world);
            return this.getStructureAt(blockposition) != null;
        }
    }

    @Nullable
    protected StructureStart getStructureAt(BlockPos blockposition) {
        // Paper start - replace method
        StructureStart structureStart = allStructures.get(ChunkPos.asLong(blockposition));
        if (structureStart != null && structureStart.isSizeable() && structureStart.getBoundingBox().contains(blockposition)) {
            List<StructureComponent> structurePieces = structureStart.getStructurePieces();
            for (StructureComponent piece : structurePieces) {
                if (piece.getBoundingBox().contains(blockposition)) {
                    return structureStart;
                }
            }
        }

        return null;
        /*
        ObjectIterator objectiterator = this.c.values().iterator();

        while (objectiterator.hasNext()) {
            StructureStart structurestart = (StructureStart) objectiterator.next();

            if (structurestart.a() && structurestart.b().b((BaseBlockPosition) blockposition)) {
                Iterator iterator = structurestart.c().iterator();

                while (iterator.hasNext()) {
                    StructurePiece structurepiece = (StructurePiece) iterator.next();

                    if (structurepiece.d().b((BaseBlockPosition) blockposition)) {
                        return structurestart;
                    }
                }
            }
        }

        return null;
        */
    }

    public boolean isPositionInStructure(World world, BlockPos blockposition) {
        if (this.world == null) return false; // Paper
        this.initializeStructureData(world);
        // Paper start - Replace method
        StructureStart structureStart = this.allStructures.get(ChunkPos.asLong(blockposition));
        return structureStart != null && structureStart.isSizeable() && structureStart.getBoundingBox().contains(blockposition);
        /* // comment out rest
        ObjectIterator objectiterator = this.c.values().iterator();

        StructureStart structurestart;

        do {
            if (!objectiterator.hasNext()) {
                return false;
            }

            structurestart = (StructureStart) objectiterator.next();
        } while (!structurestart.a() || !structurestart.b().b((BaseBlockPosition) blockposition));

        return true;*/ // Paper end
    }

    @Nullable
    public abstract BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag);

    protected void initializeStructureData(World world) {
        if (this.structureData == null && world != null) {
            // Spigot Start
            if (world.spigotConfig.saveStructureInfo && !this.getStructureName().equals( "Mineshaft" )) {
                this.structureData = (MapGenStructureData) world.loadData(MapGenStructureData.class, this.getStructureName());
            } else {
                this.structureData = new MapGenStructureData(this.getStructureName());
            }
            // Spigot End
            if (this.structureData == null) {
                this.structureData = new MapGenStructureData(this.getStructureName());
                world.setData(this.getStructureName(), (WorldSavedData) this.structureData);
            } else {
                NBTTagCompound nbttagcompound = this.structureData.getTagCompound();
                Iterator iterator = nbttagcompound.getKeySet().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    NBTBase nbtbase = nbttagcompound.getTag(s);

                    if (nbtbase.getId() == 10) {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtbase;

                        if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
                            int i = nbttagcompound1.getInteger("ChunkX");
                            int j = nbttagcompound1.getInteger("ChunkZ");
                            StructureStart structurestart = MapGenStructureIO.getStructureStart(nbttagcompound1, world);

                            if (structurestart != null) {
                                populateStructure(structurestart); // Paper
                                this.structureMap.put(ChunkPos.asLong(i, j), structurestart);
                            }
                        }
                    }
                }
            }
        }

    }

    // Paper start
    private void populateStructure(StructureStart structurestart) {
        for (StructureComponent piece : structurestart.getStructurePieces()) {
            populateStructure(structurestart, piece.getBoundingBox());
        }
        populateStructure(structurestart, structurestart.getBoundingBox());
    }
    private void populateStructure(StructureStart structurestart, StructureBoundingBox bb) {
        if (bb == null) {
            return;
        }
        final Vec3i low = bb.getLowPosition();
        final Vec3i high = bb.getHighPosition();
        for (int x = low.getX() >> 4, maxX = high.getX() >> 4; x <= maxX; x++) {
            for (int z = low.getZ() >> 4, maxZ = high.getZ() >> 4; z <= maxZ; z++) {
                allStructures.put(ChunkPos.asLong(x, z), structurestart);
            }
        }
    }
    // Paper end

    private void setStructureStart(int i, int j, StructureStart structurestart) {
        this.structureData.writeInstance(structurestart.writeStructureComponentsToNBT(i, j), i, j);
        this.structureData.markDirty();
    }

    protected abstract boolean canSpawnStructureAtCoords(int i, int j);

    protected abstract StructureStart getStructureStart(int i, int j);

    protected static BlockPos findNearestStructurePosBySpacing(World world, MapGenStructure structuregenerator, BlockPos blockposition, int i, int j, int k, boolean flag, int l, boolean flag1) {
        int i1 = blockposition.getX() >> 4;
        int j1 = blockposition.getZ() >> 4;
        int k1 = 0;

        for (Random random = new Random(); k1 <= l; ++k1) {
            for (int l1 = -k1; l1 <= k1; ++l1) {
                boolean flag2 = l1 == -k1 || l1 == k1;

                for (int i2 = -k1; i2 <= k1; ++i2) {
                    boolean flag3 = i2 == -k1 || i2 == k1;

                    if (flag2 || flag3) {
                        int j2 = i1 + i * l1;
                        int k2 = j1 + i * i2;

                        if (j2 < 0) {
                            j2 -= i - 1;
                        }

                        if (k2 < 0) {
                            k2 -= i - 1;
                        }

                        int l2 = j2 / i;
                        int i3 = k2 / i;
                        Random random1 = world.setRandomSeed(l2, i3, k);

                        l2 *= i;
                        i3 *= i;
                        if (flag) {
                            l2 += (random1.nextInt(i - j) + random1.nextInt(i - j)) / 2;
                            i3 += (random1.nextInt(i - j) + random1.nextInt(i - j)) / 2;
                        } else {
                            l2 += random1.nextInt(i - j);
                            i3 += random1.nextInt(i - j);
                        }

                        MapGenBase.setupChunkSeed(world.getSeed(), random, l2, i3);
                        random.nextInt();

                        if (!world.getWorldBorder().isChunkInBounds(l2, i3)) { continue; } // Paper

                        if (structuregenerator.canSpawnStructureAtCoords(l2, i3)) {
                            if (!flag1 || !world.isChunkGeneratedAt(l2, i3)) {
                                return new BlockPos((l2 << 4) + 8, 64, (i3 << 4) + 8);
                            }
                        } else if (k1 == 0) {
                            break;
                        }
                    }
                }

                if (k1 == 0) {
                    break;
                }
            }
        }

        return null;
    }
}

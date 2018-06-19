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
    private MapGenStructureData field_143029_e;
    protected Long2ObjectMap<StructureStart> field_75053_d = new Long2ObjectOpenHashMap(1024);
    protected Long2ObjectMap<StructureStart> allStructures = new Long2ObjectOpenHashMap(1024); // Paper - Holds ref to structures for every chunk its part of, where as the one above this only holds the vanilla oriented ones.

    public MapGenStructure() {}

    public String getName() { return func_143025_a(); } // Paper // OBFHELPER
    public abstract String func_143025_a();

    protected final synchronized void func_180701_a(World world, final int i, final int j, int k, int l, ChunkPrimer chunksnapshot) {
        this.func_143027_a(world);
        if (!this.field_75053_d.containsKey(ChunkPos.func_77272_a(i, j))) {
            this.field_75038_b.nextInt();

            try {
                if (this.func_75047_a(i, j)) {
                    StructureStart structurestart = this.func_75049_b(i, j);

                    populateStructure(structurestart); // Paper
                    this.field_75053_d.put(ChunkPos.func_77272_a(i, j), structurestart);
                    if (structurestart.func_75069_d()) {
                        this.func_143026_a(i, j, structurestart);
                    }
                }

            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Exception preparing structure feature");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Feature being prepared");

                crashreportsystemdetails.func_189529_a("Is feature chunk", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return MapGenStructure.this.func_75047_a(i, j) ? "True" : "False";
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                crashreportsystemdetails.func_71507_a("Chunk location", (Object) String.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j)}));
                crashreportsystemdetails.func_189529_a("Chunk pos hash", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return String.valueOf(ChunkPos.func_77272_a(i, j));
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                crashreportsystemdetails.func_189529_a("Structure type", new ICrashReportDetail() {
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

    public synchronized boolean func_175794_a(World world, Random random, ChunkPos chunkcoordintpair) {
        timing.startTiming(); // Paper
        this.func_143027_a(world);
        int i = (chunkcoordintpair.field_77276_a << 4) + 8;
        int j = (chunkcoordintpair.field_77275_b << 4) + 8;
        boolean flag = false;
        ObjectIterator objectiterator = this.field_75053_d.values().iterator();

        while (objectiterator.hasNext()) {
            StructureStart structurestart = (StructureStart) objectiterator.next();

            if (structurestart.func_75069_d() && structurestart.func_175788_a(chunkcoordintpair) && structurestart.func_75071_a().func_78885_a(i, j, i + 15, j + 15)) {
                structurestart.func_75068_a(world, random, new StructureBoundingBox(i, j, i + 15, j + 15));
                structurestart.func_175787_b(chunkcoordintpair);
                flag = true;
                this.func_143026_a(structurestart.func_143019_e(), structurestart.func_143018_f(), structurestart);
            }
        }
        timing.stopTiming(); // Paper

        return flag;
    }

    public boolean func_175795_b(BlockPos blockposition) {
        if (this.field_75039_c == null) {
            return false;
        } else {
            this.func_143027_a(this.field_75039_c);
            return this.func_175797_c(blockposition) != null;
        }
    }

    @Nullable
    protected StructureStart func_175797_c(BlockPos blockposition) {
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

    public boolean func_175796_a(World world, BlockPos blockposition) {
        if (this.field_75039_c == null) return false; // Paper
        this.func_143027_a(world);
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
    public abstract BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag);

    protected void func_143027_a(World world) {
        if (this.field_143029_e == null && world != null) {
            // Spigot Start
            if (world.spigotConfig.saveStructureInfo && !this.func_143025_a().equals( "Mineshaft" )) {
                this.field_143029_e = (MapGenStructureData) world.func_72943_a(MapGenStructureData.class, this.func_143025_a());
            } else {
                this.field_143029_e = new MapGenStructureData(this.func_143025_a());
            }
            // Spigot End
            if (this.field_143029_e == null) {
                this.field_143029_e = new MapGenStructureData(this.func_143025_a());
                world.func_72823_a(this.func_143025_a(), (WorldSavedData) this.field_143029_e);
            } else {
                NBTTagCompound nbttagcompound = this.field_143029_e.func_143041_a();
                Iterator iterator = nbttagcompound.func_150296_c().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    NBTBase nbtbase = nbttagcompound.func_74781_a(s);

                    if (nbtbase.func_74732_a() == 10) {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtbase;

                        if (nbttagcompound1.func_74764_b("ChunkX") && nbttagcompound1.func_74764_b("ChunkZ")) {
                            int i = nbttagcompound1.func_74762_e("ChunkX");
                            int j = nbttagcompound1.func_74762_e("ChunkZ");
                            StructureStart structurestart = MapGenStructureIO.func_143035_a(nbttagcompound1, world);

                            if (structurestart != null) {
                                populateStructure(structurestart); // Paper
                                this.field_75053_d.put(ChunkPos.func_77272_a(i, j), structurestart);
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
        for (int x = low.func_177958_n() >> 4, maxX = high.func_177958_n() >> 4; x <= maxX; x++) {
            for (int z = low.func_177952_p() >> 4, maxZ = high.func_177952_p() >> 4; z <= maxZ; z++) {
                allStructures.put(ChunkPos.asLong(x, z), structurestart);
            }
        }
    }
    // Paper end

    private void func_143026_a(int i, int j, StructureStart structurestart) {
        this.field_143029_e.func_143043_a(structurestart.func_143021_a(i, j), i, j);
        this.field_143029_e.func_76185_a();
    }

    protected abstract boolean func_75047_a(int i, int j);

    protected abstract StructureStart func_75049_b(int i, int j);

    protected static BlockPos func_191069_a(World world, MapGenStructure structuregenerator, BlockPos blockposition, int i, int j, int k, boolean flag, int l, boolean flag1) {
        int i1 = blockposition.func_177958_n() >> 4;
        int j1 = blockposition.func_177952_p() >> 4;
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
                        Random random1 = world.func_72843_D(l2, i3, k);

                        l2 *= i;
                        i3 *= i;
                        if (flag) {
                            l2 += (random1.nextInt(i - j) + random1.nextInt(i - j)) / 2;
                            i3 += (random1.nextInt(i - j) + random1.nextInt(i - j)) / 2;
                        } else {
                            l2 += random1.nextInt(i - j);
                            i3 += random1.nextInt(i - j);
                        }

                        MapGenBase.func_191068_a(world.func_72905_C(), random, l2, i3);
                        random.nextInt();

                        if (!world.func_175723_af().isChunkInBounds(l2, i3)) { continue; } // Paper

                        if (structuregenerator.func_75047_a(l2, i3)) {
                            if (!flag1 || !world.func_190526_b(l2, i3)) {
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

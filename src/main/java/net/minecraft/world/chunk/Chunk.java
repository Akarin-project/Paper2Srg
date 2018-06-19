package net.minecraft.world.chunk;

import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSand;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MCUtil;
import net.minecraft.server.PaperLightingQueue;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.IChunkGenerator;
import org.bukkit.Server;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class Chunk {

    private static final Logger field_150817_t = LogManager.getLogger();
    public static final ExtendedBlockStorage field_186036_a = null; public static final ExtendedBlockStorage EMPTY_CHUNK_SECTION = Chunk.field_186036_a; // Paper - OBFHELPER
    private final ExtendedBlockStorage[] field_76652_q;
    private final byte[] field_76651_r;
    private final int[] field_76638_b;
    private final boolean[] field_76639_c;
    private boolean field_76636_d;
    public final World field_76637_e;
    public final int[] field_76634_f;
    public Long scheduledForUnload; // Paper - delay chunk unloads
    public final int field_76635_g;
    public final int field_76647_h;
    private boolean field_76650_s;
    public final Map<BlockPos, TileEntity> field_150816_i;
    public final List<Entity>[] field_76645_j; // Spigot
    final PaperLightingQueue.LightingQueue lightingQueue = new PaperLightingQueue.LightingQueue(this); // Paper
    private boolean field_76646_k;
    private boolean field_150814_l;
    private boolean field_150815_m; private boolean isTicked() { return field_150815_m; }; // Paper - OBFHELPER
    private boolean field_76643_l;
    private boolean field_76644_m;
    private long field_76641_n;
    private int field_82912_p;
    private long field_111204_q;
    private int field_76649_t;
    private final ConcurrentLinkedQueue<BlockPos> field_177447_w;
    public boolean field_189550_d; public void setShouldUnload(boolean unload) { this.field_189550_d = unload; } public boolean isUnloading() { return field_189550_d; } // Paper - OBFHELPER
    protected gnu.trove.map.hash.TObjectIntHashMap<Class> entityCount = new gnu.trove.map.hash.TObjectIntHashMap<Class>(); // Spigot

    // Paper start
    // Track the number of minecarts and items
    // Keep this synced with entitySlices.add() and entitySlices.remove()
    private final int[] itemCounts = new int[16];
    private final int[] inventoryEntityCounts = new int[16];
    // Paper end

    // CraftBukkit start - Neighbor loaded cache for chunk lighting and entity ticking
    private int neighbors = 0x1 << 12;
    public long chunkKey;

    public boolean areNeighborsLoaded(final int radius) {
        switch (radius) {
            case 2:
                return this.neighbors == Integer.MAX_VALUE >> 6;
            case 1:
                final int mask =
                        //       x        z   offset          x        z   offset          x         z   offset
                        (0x1 << (1 * 5 +  1 + 12)) | (0x1 << (0 * 5 +  1 + 12)) | (0x1 << (-1 * 5 +  1 + 12)) |
                        (0x1 << (1 * 5 +  0 + 12)) | (0x1 << (0 * 5 +  0 + 12)) | (0x1 << (-1 * 5 +  0 + 12)) |
                        (0x1 << (1 * 5 + -1 + 12)) | (0x1 << (0 * 5 + -1 + 12)) | (0x1 << (-1 * 5 + -1 + 12));
                return (this.neighbors & mask) == mask;
            default:
                throw new UnsupportedOperationException(String.valueOf(radius));
        }
    }

    public void setNeighborLoaded(final int x, final int z) {
        this.neighbors |= 0x1 << (x * 5 + 12 + z);
    }

    public void setNeighborUnloaded(final int x, final int z) {
        this.neighbors &= ~(0x1 << (x * 5 + 12 + z));
    }
    // CraftBukkit end

    public Chunk(World world, int i, int j) {
        this.field_76652_q = new ExtendedBlockStorage[16];
        this.field_76651_r = new byte[256];
        this.field_76638_b = new int[256];
        this.field_76639_c = new boolean[256];
        this.field_150816_i = Maps.newHashMap();
        this.field_76649_t = 4096;
        this.field_177447_w = Queues.newConcurrentLinkedQueue();
        this.field_76645_j = (List[]) (new List[16]); // Spigot
        this.field_76637_e = world;
        this.field_76635_g = i;
        this.field_76647_h = j;
        this.field_76634_f = new int[256];

        for (int k = 0; k < this.field_76645_j.length; ++k) {
            this.field_76645_j[k] = new org.bukkit.craftbukkit.util.UnsafeList(); // Spigot
        }

        Arrays.fill(this.field_76638_b, -999);
        Arrays.fill(this.field_76651_r, (byte) -1);
        // CraftBukkit start
        this.bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(this);
        this.chunkKey = ChunkPos.func_77272_a(this.field_76635_g, this.field_76647_h);
    }

    public org.bukkit.Chunk bukkitChunk;
    public boolean mustSave;
    // CraftBukkit end

    public Chunk(World world, ChunkPrimer chunksnapshot, int i, int j) {
        this(world, i, j);
        boolean flag = true;
        boolean flag1 = world.field_73011_w.func_191066_m();

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                for (int i1 = 0; i1 < 256; ++i1) {
                    IBlockState iblockdata = chunksnapshot.func_177856_a(k, i1, l);

                    if (iblockdata.func_185904_a() != Material.field_151579_a) {
                        int j1 = i1 >> 4;

                        if (this.field_76652_q[j1] == Chunk.field_186036_a) {
                            this.field_76652_q[j1] = new ExtendedBlockStorage(j1 << 4, flag1, world.chunkPacketBlockController.getPredefinedBlockData(this, j1)); // Paper - Anti-Xray - Add predefined block data
                        }

                        this.field_76652_q[j1].func_177484_a(k, i1 & 15, l, iblockdata);
                    }
                }
            }
        }

    }

    public boolean func_76600_a(int i, int j) {
        return i == this.field_76635_g && j == this.field_76647_h;
    }

    public int func_177433_f(BlockPos blockposition) {
        return this.func_76611_b(blockposition.func_177958_n() & 15, blockposition.func_177952_p() & 15);
    }

    public int func_76611_b(int i, int j) {
        return this.field_76634_f[j << 4 | i];
    }

    @Nullable
    private ExtendedBlockStorage func_186031_y() {
        for (int i = this.field_76652_q.length - 1; i >= 0; --i) {
            if (this.field_76652_q[i] != Chunk.field_186036_a) {
                return this.field_76652_q[i];
            }
        }

        return null;
    }

    public int func_76625_h() {
        ExtendedBlockStorage chunksection = this.func_186031_y();

        return chunksection == null ? 0 : chunksection.func_76662_d();
    }

    public ExtendedBlockStorage[] func_76587_i() {
        return this.field_76652_q;
    }

    public void func_76603_b() {
        int i = this.func_76625_h();

        this.field_82912_p = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j) {
            int k = 0;

            while (k < 16) {
                this.field_76638_b[j + (k << 4)] = -999;
                int l = i + 16;

                while (true) {
                    if (l > 0) {
                        if (this.func_150808_b(j, l - 1, k) == 0) {
                            --l;
                            continue;
                        }

                        this.field_76634_f[k << 4 | j] = l;
                        if (l < this.field_82912_p) {
                            this.field_82912_p = l;
                        }
                    }

                    if (this.field_76637_e.field_73011_w.func_191066_m()) {
                        l = 15;
                        int i1 = i + 16 - 1;

                        do {
                            int j1 = this.func_150808_b(j, i1, k);

                            if (j1 == 0 && l != 15) {
                                j1 = 1;
                            }

                            l -= j1;
                            if (l > 0) {
                                ExtendedBlockStorage chunksection = this.field_76652_q[i1 >> 4];

                                if (chunksection != Chunk.field_186036_a) {
                                    chunksection.func_76657_c(j, i1 & 15, k, l);
                                    this.field_76637_e.func_175679_n(new BlockPos((this.field_76635_g << 4) + j, i1, (this.field_76647_h << 4) + k));
                                }
                            }

                            --i1;
                        } while (i1 > 0 && l > 0);
                    }

                    ++k;
                    break;
                }
            }
        }

        this.field_76643_l = true;
    }

    private void func_76595_e(int i, int j) {
        this.field_76639_c[i + j * 16] = true;
        this.field_76650_s = true;
    }

    private void func_150803_c(boolean flag) {
        this.field_76637_e.field_72984_F.func_76320_a("recheckGaps");
        if (this.field_76637_e.func_175697_a(new BlockPos(this.field_76635_g * 16 + 8, 0, this.field_76647_h * 16 + 8), 16)) {
            this.runOrQueueLightUpdate(() -> recheckGaps(flag)); // Paper - Queue light update
        }
    }

    private void recheckGaps(boolean flag) {
        if (true) {
            // Paper end
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (this.field_76639_c[i + j * 16]) {
                        this.field_76639_c[i + j * 16] = false;
                        int k = this.func_76611_b(i, j);
                        int l = this.field_76635_g * 16 + i;
                        int i1 = this.field_76647_h * 16 + j;
                        int j1 = Integer.MAX_VALUE;

                        Iterator iterator;
                        EnumFacing enumdirection;

                        for (iterator = EnumFacing.Plane.HORIZONTAL.iterator(); iterator.hasNext(); j1 = Math.min(j1, this.field_76637_e.func_82734_g(l + enumdirection.func_82601_c(), i1 + enumdirection.func_82599_e()))) {
                            enumdirection = (EnumFacing) iterator.next();
                        }

                        this.func_76599_g(l, i1, j1);
                        iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                        while (iterator.hasNext()) {
                            enumdirection = (EnumFacing) iterator.next();
                            this.func_76599_g(l + enumdirection.func_82601_c(), i1 + enumdirection.func_82599_e(), k);
                        }

                        if (flag) {
                            this.field_76637_e.field_72984_F.func_76319_b();
                            return;
                        }
                    }
                }
            }

            this.field_76650_s = false;
        }

        this.field_76637_e.field_72984_F.func_76319_b();
    }

    private void func_76599_g(int i, int j, int k) {
        int l = this.field_76637_e.func_175645_m(new BlockPos(i, 0, j)).func_177956_o();

        if (l > k) {
            this.func_76609_d(i, j, k, l + 1);
        } else if (l < k) {
            this.func_76609_d(i, j, l, k + 1);
        }

    }

    private void func_76609_d(int i, int j, int k, int l) {
        if (l > k && this.field_76637_e.func_175697_a(new BlockPos(i, 0, j), 16)) {
            for (int i1 = k; i1 < l; ++i1) {
                this.field_76637_e.func_180500_c(EnumSkyBlock.SKY, new BlockPos(i, i1, j));
            }

            this.field_76643_l = true;
        }

    }

    private void func_76615_h(int i, int j, int k) {
        int l = this.field_76634_f[k << 4 | i] & 255;
        int i1 = l;

        if (j > l) {
            i1 = j;
        }

        while (i1 > 0 && this.func_150808_b(i, i1 - 1, k) == 0) {
            --i1;
        }

        if (i1 != l) {
            this.field_76637_e.func_72975_g(i + this.field_76635_g * 16, k + this.field_76647_h * 16, i1, l);
            this.field_76634_f[k << 4 | i] = i1;
            int j1 = this.field_76635_g * 16 + i;
            int k1 = this.field_76647_h * 16 + k;
            int l1;
            int i2;

            if (this.field_76637_e.field_73011_w.func_191066_m()) {
                ExtendedBlockStorage chunksection;

                if (i1 < l) {
                    for (l1 = i1; l1 < l; ++l1) {
                        chunksection = this.field_76652_q[l1 >> 4];
                        if (chunksection != Chunk.field_186036_a) {
                            chunksection.func_76657_c(i, l1 & 15, k, 15);
                            this.field_76637_e.func_175679_n(new BlockPos((this.field_76635_g << 4) + i, l1, (this.field_76647_h << 4) + k));
                        }
                    }
                } else {
                    for (l1 = l; l1 < i1; ++l1) {
                        chunksection = this.field_76652_q[l1 >> 4];
                        if (chunksection != Chunk.field_186036_a) {
                            chunksection.func_76657_c(i, l1 & 15, k, 0);
                            this.field_76637_e.func_175679_n(new BlockPos((this.field_76635_g << 4) + i, l1, (this.field_76647_h << 4) + k));
                        }
                    }
                }

                l1 = 15;

                while (i1 > 0 && l1 > 0) {
                    --i1;
                    i2 = this.func_150808_b(i, i1, k);
                    if (i2 == 0) {
                        i2 = 1;
                    }

                    l1 -= i2;
                    if (l1 < 0) {
                        l1 = 0;
                    }

                    ExtendedBlockStorage chunksection1 = this.field_76652_q[i1 >> 4];

                    if (chunksection1 != Chunk.field_186036_a) {
                        chunksection1.func_76657_c(i, i1 & 15, k, l1);
                    }
                }
            }

            l1 = this.field_76634_f[k << 4 | i];
            i2 = l;
            int j2 = l1;

            if (l1 < l) {
                i2 = l1;
                j2 = l;
            }

            if (l1 < this.field_82912_p) {
                this.field_82912_p = l1;
            }

            if (this.field_76637_e.field_73011_w.func_191066_m()) {
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                    this.func_76609_d(j1 + enumdirection.func_82601_c(), k1 + enumdirection.func_82599_e(), i2, j2);
                }

                this.func_76609_d(j1, k1, i2, j2);
            }

            this.field_76643_l = true;
        }
    }

    public int func_177437_b(BlockPos blockposition) {
        return this.func_177435_g(blockposition).func_185891_c();
    }

    private int func_150808_b(int i, int j, int k) {
        return this.func_186032_a(i, j, k).func_185891_c();
    }

    // Paper start - Optimize getBlockData to reduce instructions
    public final IBlockState func_177435_g(final BlockPos pos) {
        return getBlockData(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
    }

    public final IBlockState getBlockData(final int x, final int y, final int z) {
        // Method body / logic copied from below
        final int i = y >> 4;
        if (y >= 0 && i < this.field_76652_q.length && this.field_76652_q[i] != null) {
            // Inlined ChunkSection.getType() and DataPaletteBlock.a(int,int,int)
            return this.field_76652_q[i].field_177488_d.func_186015_a((y & 15) << 8 | (z & 15) << 4 | x & 15);
        }
        return Blocks.field_150350_a.func_176223_P();
    }

    public IBlockState func_186032_a(final int i, final int j, final int k) {
        return getBlockData(i, j, k);
    }

    public IBlockState unused(final int i, final int j, final int k) {
    // Paper end
        if (this.field_76637_e.func_175624_G() == WorldType.field_180272_g) {
            IBlockState iblockdata = null;

            if (j == 60) {
                iblockdata = Blocks.field_180401_cv.func_176223_P();
            }

            if (j == 70) {
                iblockdata = ChunkGeneratorDebug.func_177461_b(i, k);
            }

            return iblockdata == null ? Blocks.field_150350_a.func_176223_P() : iblockdata;
        } else {
            try {
                if (j >= 0 && j >> 4 < this.field_76652_q.length) {
                    ExtendedBlockStorage chunksection = this.field_76652_q[j >> 4];

                    if (chunksection != Chunk.field_186036_a) {
                        return chunksection.func_177485_a(i & 15, j & 15, k & 15);
                    }
                }

                return Blocks.field_150350_a.func_176223_P();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Getting block state");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Block being got");

                crashreportsystemdetails.func_189529_a("Location", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return CrashReportCategory.func_184876_a(i, j, k);
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    @Nullable
    public IBlockState func_177436_a(BlockPos blockposition, IBlockState iblockdata) {
        int i = blockposition.func_177958_n() & 15;
        int j = blockposition.func_177956_o();
        int k = blockposition.func_177952_p() & 15;
        int l = k << 4 | i;

        if (j >= this.field_76638_b[l] - 1) {
            this.field_76638_b[l] = -999;
        }

        int i1 = this.field_76634_f[l];
        IBlockState iblockdata1 = this.func_177435_g(blockposition);

        if (iblockdata1 == iblockdata) {
            return null;
        } else {
            Block block = iblockdata.func_177230_c();
            Block block1 = iblockdata1.func_177230_c();
            ExtendedBlockStorage chunksection = this.field_76652_q[j >> 4];
            boolean flag = false;

            if (chunksection == Chunk.field_186036_a) {
                if (block == Blocks.field_150350_a) {
                    return null;
                }

                chunksection = new ExtendedBlockStorage(j >> 4 << 4, this.field_76637_e.field_73011_w.func_191066_m(), this.field_76637_e.chunkPacketBlockController.getPredefinedBlockData(this, j >> 4)); // Paper - Anti-Xray - Add predefined block data
                this.field_76652_q[j >> 4] = chunksection;
                flag = j >= i1;
            }

            chunksection.func_177484_a(i, j & 15, k, iblockdata);
            if (block1 != block) {
                if (!this.field_76637_e.field_72995_K) {
                    block1.func_180663_b(this.field_76637_e, blockposition, iblockdata1);
                } else if (block1 instanceof ITileEntityProvider) {
                    this.field_76637_e.func_175713_t(blockposition);
                }
            }

            if (chunksection.func_177485_a(i, j & 15, k).func_177230_c() != block) {
                return null;
            } else {
                if (flag) {
                    this.func_76603_b();
                } else {
                    this.runOrQueueLightUpdate(() -> { // Paper - Queue light update
                    int j1 = iblockdata.func_185891_c();
                    int k1 = iblockdata1.func_185891_c();

                    if (j1 > 0) {
                        if (j >= i1) {
                            this.func_76615_h(i, j + 1, k);
                        }
                    } else if (j == i1 - 1) {
                        this.func_76615_h(i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || this.func_177413_a(EnumSkyBlock.SKY, blockposition) > 0 || this.func_177413_a(EnumSkyBlock.BLOCK, blockposition) > 0)) {
                        this.func_76595_e(i, k);
                    }
                    }); // Paper
                }

                TileEntity tileentity;

                if (block1 instanceof ITileEntityProvider) {
                    tileentity = this.func_177424_a(blockposition, Chunk.EnumCreateEntityType.CHECK);
                    if (tileentity != null) {
                        tileentity.func_145836_u();
                    }
                }

                // CraftBukkit - Don't place while processing the BlockPlaceEvent, unless it's a BlockContainer. Prevents blocks such as TNT from activating when cancelled.
                if (!this.field_76637_e.field_72995_K && block1 != block  && (!this.field_76637_e.captureBlockStates || block instanceof BlockContainer)) {
                    block.func_176213_c(this.field_76637_e, blockposition, iblockdata);
                }

                if (block instanceof ITileEntityProvider) {
                    tileentity = this.func_177424_a(blockposition, Chunk.EnumCreateEntityType.CHECK);
                    if (tileentity == null) {
                        tileentity = ((ITileEntityProvider) block).func_149915_a(this.field_76637_e, block.func_176201_c(iblockdata));
                        this.field_76637_e.func_175690_a(blockposition, tileentity);
                    }

                    if (tileentity != null) {
                        tileentity.func_145836_u();
                    }
                }

                this.field_76643_l = true;
                return iblockdata1;
            }
        }
    }

    public int func_177413_a(EnumSkyBlock enumskyblock, BlockPos blockposition) {
        int i = blockposition.func_177958_n() & 15;
        int j = blockposition.func_177956_o();
        int k = blockposition.func_177952_p() & 15;
        ExtendedBlockStorage chunksection = this.field_76652_q[j >> 4];

        return chunksection == Chunk.field_186036_a ? (this.func_177444_d(blockposition) ? enumskyblock.field_77198_c : 0) : (enumskyblock == EnumSkyBlock.SKY ? (!this.field_76637_e.field_73011_w.func_191066_m() ? 0 : chunksection.func_76670_c(i, j & 15, k)) : (enumskyblock == EnumSkyBlock.BLOCK ? chunksection.func_76674_d(i, j & 15, k) : enumskyblock.field_77198_c));
    }

    public void func_177431_a(EnumSkyBlock enumskyblock, BlockPos blockposition, int i) {
        int j = blockposition.func_177958_n() & 15;
        int k = blockposition.func_177956_o();
        int l = blockposition.func_177952_p() & 15;
        ExtendedBlockStorage chunksection = this.field_76652_q[k >> 4];

        if (chunksection == Chunk.field_186036_a) {
            chunksection = new ExtendedBlockStorage(k >> 4 << 4, this.field_76637_e.field_73011_w.func_191066_m(), this.field_76637_e.chunkPacketBlockController.getPredefinedBlockData(this, k >> 4)); // Paper - Anti-Xray - Add predefined block data
            this.field_76652_q[k >> 4] = chunksection;
            this.func_76603_b();
        }

        this.field_76643_l = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            if (this.field_76637_e.field_73011_w.func_191066_m()) {
                chunksection.func_76657_c(j, k & 15, l, i);
            }
        } else if (enumskyblock == EnumSkyBlock.BLOCK) {
            chunksection.func_76677_d(j, k & 15, l, i);
        }

    }

    public final int getLightSubtracted(BlockPos blockposition, int i) { return this.func_177443_a(blockposition, i); } // Paper - OBFHELPER
    public int func_177443_a(BlockPos blockposition, int i) {
        int j = blockposition.func_177958_n() & 15;
        int k = blockposition.func_177956_o();
        int l = blockposition.func_177952_p() & 15;
        ExtendedBlockStorage chunksection = this.field_76652_q[k >> 4];

        if (chunksection == Chunk.field_186036_a) {
            return this.field_76637_e.field_73011_w.func_191066_m() && i < EnumSkyBlock.SKY.field_77198_c ? EnumSkyBlock.SKY.field_77198_c - i : 0;
        } else {
            int i1 = !this.field_76637_e.field_73011_w.func_191066_m() ? 0 : chunksection.func_76670_c(j, k & 15, l);

            i1 -= i;
            int j1 = chunksection.func_76674_d(j, k & 15, l);

            if (j1 > i1) {
                i1 = j1;
            }

            return i1;
        }
    }

    public void func_76612_a(Entity entity) {
        this.field_76644_m = true;
        int i = MathHelper.func_76128_c(entity.field_70165_t / 16.0D);
        int j = MathHelper.func_76128_c(entity.field_70161_v / 16.0D);

        if (i != this.field_76635_g || j != this.field_76647_h) {
            Chunk.field_150817_t.warn("Wrong location! ({}, {}) should be ({}, {}), {}", Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(this.field_76635_g), Integer.valueOf(this.field_76647_h), entity);
            entity.func_70106_y();
        }

        int k = MathHelper.func_76128_c(entity.field_70163_u / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.field_76645_j.length) {
            k = this.field_76645_j.length - 1;
        }

        entity.field_70175_ag = true;
        entity.field_70176_ah = this.field_76635_g;
        entity.field_70162_ai = k;
        entity.field_70164_aj = this.field_76647_h;
        this.field_76645_j[k].add(entity);
        // Paper start - update count
        if (entity instanceof EntityItem) {
            itemCounts[k]++;
        } else if (entity instanceof IInventory) {
            inventoryEntityCounts[k]++;
        }
        // Paper end
        // Spigot start - increment creature type count
        // Keep this synced up with World.a(Class)
        if (entity instanceof EntityLiving) {
            EntityLiving entityinsentient = (EntityLiving) entity;
            if (entityinsentient.func_70692_ba() && entityinsentient.func_104002_bU()) {
                return;
            }
        }
        for ( EnumCreatureType creatureType : EnumCreatureType.values() )
        {
            if ( creatureType.func_75598_a().isAssignableFrom( entity.getClass() ) )
            {
                this.entityCount.adjustOrPutValue( creatureType.func_75598_a(), 1, 1 );
            }
        }
        // Spigot end
    }

    public void func_76622_b(Entity entity) {
        this.func_76608_a(entity, entity.field_70162_ai);
    }

    public void func_76608_a(Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }

        if (i >= this.field_76645_j.length) {
            i = this.field_76645_j.length - 1;
        }

        if (!this.field_76645_j[i].remove(entity)) { return; } // Paper
        // Paper start - update counts
        if (entity instanceof EntityItem) {
            itemCounts[i]--;
        } else if (entity instanceof IInventory) {
            inventoryEntityCounts[i]--;
        }
        // Paper end
        // Spigot start - decrement creature type count
        // Keep this synced up with World.a(Class)
        if (entity instanceof EntityLiving) {
            EntityLiving entityinsentient = (EntityLiving) entity;
            if (entityinsentient.func_70692_ba() && entityinsentient.func_104002_bU()) {
                return;
            }
        }
        for ( EnumCreatureType creatureType : EnumCreatureType.values() )
        {
            if ( creatureType.func_75598_a().isAssignableFrom( entity.getClass() ) )
            {
                this.entityCount.adjustValue( creatureType.func_75598_a(), -1 );
            }
        }
        // Spigot end
    }

    public boolean func_177444_d(BlockPos blockposition) {
        int i = blockposition.func_177958_n() & 15;
        int j = blockposition.func_177956_o();
        int k = blockposition.func_177952_p() & 15;

        return j >= this.field_76634_f[k << 4 | i];
    }

    @Nullable
    private TileEntity func_177422_i(BlockPos blockposition) {
        IBlockState iblockdata = this.func_177435_g(blockposition);
        Block block = iblockdata.func_177230_c();

        return !block.func_149716_u() ? null : ((ITileEntityProvider) block).func_149915_a(this.field_76637_e, iblockdata.func_177230_c().func_176201_c(iblockdata));
    }

    @Nullable public final TileEntity getTileEntityImmediately(BlockPos pos) { return this.func_177424_a(pos, EnumCreateEntityType.IMMEDIATE); } // Paper - OBFHELPER
    @Nullable
    public TileEntity func_177424_a(BlockPos blockposition, Chunk.EnumCreateEntityType chunk_enumtileentitystate) {
        // CraftBukkit start
        TileEntity tileentity = null;
        if (field_76637_e.captureBlockStates) {
            tileentity = field_76637_e.capturedTileEntities.get(blockposition);
        }
        if (tileentity == null) {
            tileentity = (TileEntity) this.field_150816_i.get(blockposition);
        }
        // CraftBukkit end

        if (tileentity == null) {
            if (chunk_enumtileentitystate == Chunk.EnumCreateEntityType.IMMEDIATE) {
                tileentity = this.func_177422_i(blockposition);
                this.field_76637_e.func_175690_a(blockposition, tileentity);
            } else if (chunk_enumtileentitystate == Chunk.EnumCreateEntityType.QUEUED) {
                this.field_177447_w.add(blockposition);
            }
        } else if (tileentity.func_145837_r()) {
            this.field_150816_i.remove(blockposition);
            return null;
        }

        return tileentity;
    }

    public void func_150813_a(TileEntity tileentity) {
        this.func_177426_a(tileentity.func_174877_v(), tileentity);
        if (this.field_76636_d) {
            this.field_76637_e.func_175700_a(tileentity);
        }

    }

    public void func_177426_a(BlockPos blockposition, TileEntity tileentity) {
        tileentity.func_145834_a(this.field_76637_e);
        tileentity.func_174878_a(blockposition);
        if (this.func_177435_g(blockposition).func_177230_c() instanceof ITileEntityProvider) {
            if (this.field_150816_i.containsKey(blockposition)) {
                ((TileEntity) this.field_150816_i.get(blockposition)).func_145843_s();
            }

            tileentity.func_145829_t();
            this.field_150816_i.put(blockposition, tileentity);
            // CraftBukkit start
            // Paper start - Remove invalid mob spawner tile entities
        } else if (tileentity instanceof TileEntityMobSpawner && org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(func_177435_g(blockposition).func_177230_c()) != org.bukkit.Material.MOB_SPAWNER) {
            this.field_150816_i.remove(blockposition);
            // Paper end
        } else {
            // Paper start
            ServerInternalException e = new ServerInternalException(
                    "Attempted to place a tile entity (" + tileentity + ") at " + tileentity.field_174879_c.func_177958_n() + ","
                            + tileentity.field_174879_c.func_177956_o() + "," + tileentity.field_174879_c.func_177952_p()
                            + " (" + CraftMagicNumbers.getMaterial(func_177435_g(blockposition).func_177230_c()) + ") where there was no entity tile!\n" +
                            "Chunk coordinates: " + (this.field_76635_g * 16) + "," + (this.field_76647_h * 16));
            e.printStackTrace();
            ServerInternalException.reportInternalException(e);

            if (this.field_76637_e.paperConfig.removeCorruptTEs) {
                this.removeTileEntity(tileentity.func_174877_v());
                this.func_76630_e();
                org.bukkit.Bukkit.getLogger().info("Removing corrupt tile entity");
            }
            // Paper end
            // CraftBukkit end
        }
    }

    public void removeTileEntity(BlockPos blockposition) { this.func_177425_e(blockposition); } // Paper - OBFHELPER
    public void func_177425_e(BlockPos blockposition) {
        if (this.field_76636_d) {
            TileEntity tileentity = (TileEntity) this.field_150816_i.remove(blockposition);

            if (tileentity != null) {
                tileentity.func_145843_s();
            }
        }

    }

    public void func_76631_c() {
        this.field_76636_d = true;
        this.field_76637_e.func_147448_a(this.field_150816_i.values());
        List[] aentityslice = this.field_76645_j; // Spigot
        int i = aentityslice.length;

        for (int j = 0; j < i; ++j) {
            List entityslice = aentityslice[j]; // Spigot

            this.field_76637_e.func_175650_b((Collection) entityslice);
        }

    }

    public void func_76623_d() {
        this.field_76636_d = false;
        Iterator iterator = this.field_150816_i.values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();
            // Spigot Start
            if ( tileentity instanceof IInventory )
            {
                for ( org.bukkit.entity.HumanEntity h : Lists.<org.bukkit.entity.HumanEntity>newArrayList((List<org.bukkit.entity.HumanEntity>) ( (IInventory) tileentity ).getViewers() ) )
                {
                    if ( h instanceof org.bukkit.craftbukkit.entity.CraftHumanEntity )
                    {
                       ( (org.bukkit.craftbukkit.entity.CraftHumanEntity) h).getHandle().func_71053_j();
                    }
                }
            }
            // Spigot End

            this.field_76637_e.func_147457_a(tileentity);
        }

        List[] aentityslice = this.field_76645_j; // Spigot
        int i = aentityslice.length;

        for (int j = 0; j < i; ++j) {
            // CraftBukkit start
            List<Entity> newList = Lists.newArrayList(aentityslice[j]);
            java.util.Iterator<Entity> iter = newList.iterator();
            while (iter.hasNext()) {
                Entity entity = iter.next();
                // Spigot Start
                if ( entity instanceof IInventory )
                {
                    for ( org.bukkit.entity.HumanEntity h : Lists.<org.bukkit.entity.HumanEntity>newArrayList( (List<org.bukkit.entity.HumanEntity>) ( (IInventory) entity ).getViewers() ) )
                    {
                        if ( h instanceof org.bukkit.craftbukkit.entity.CraftHumanEntity )
                        {
                           ( (org.bukkit.craftbukkit.entity.CraftHumanEntity) h).getHandle().func_71053_j();
                        }
                    }
                }
                // Spigot End

                // Do not pass along players, as doing so can get them stuck outside of time.
                // (which for example disables inventory icon updates and prevents block breaking)
                if (entity instanceof EntityPlayerMP) {
                    iter.remove();
                }
            }

            this.field_76637_e.func_175681_c(newList);
            // CraftBukkit end
        }

    }

    public void func_76630_e() {
        this.field_76643_l = true;
    }

    public void func_177414_a(@Nullable Entity entity, AxisAlignedBB axisalignedbb, List<Entity> list, Predicate<? super Entity> predicate) {
        int i = MathHelper.func_76128_c((axisalignedbb.field_72338_b - 2.0D) / 16.0D);
        int j = MathHelper.func_76128_c((axisalignedbb.field_72337_e + 2.0D) / 16.0D);

        i = MathHelper.func_76125_a(i, 0, this.field_76645_j.length - 1);
        j = MathHelper.func_76125_a(j, 0, this.field_76645_j.length - 1);

        for (int k = i; k <= j; ++k) {
            if (!this.field_76645_j[k].isEmpty()) {
                Iterator iterator = this.field_76645_j[k].iterator();

                // Paper start - Don't search for inventories if we have none, and that is all we want
                /*
                * We check if they want inventories by seeing if it is the static `IEntitySelector.c`
                *
                * Make sure the inventory selector stays in sync.
                * It should be the one that checks `var1 instanceof IInventory && var1.isAlive()`
                */
                if (predicate == EntitySelectors.field_96566_b && inventoryEntityCounts[k] <= 0) continue;
                // Paper end
                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    if (entity1.func_174813_aQ().func_72326_a(axisalignedbb) && entity1 != entity) {
                        if (predicate == null || predicate.apply(entity1)) {
                            list.add(entity1);
                        }

                        Entity[] aentity = entity1.func_70021_al();

                        if (aentity != null) {
                            Entity[] aentity1 = aentity;
                            int l = aentity.length;

                            for (int i1 = 0; i1 < l; ++i1) {
                                Entity entity2 = aentity1[i1];

                                if (entity2 != entity && entity2.func_174813_aQ().func_72326_a(axisalignedbb) && (predicate == null || predicate.apply(entity2))) {
                                    list.add(entity2);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public <T extends Entity> void func_177430_a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, List<T> list, Predicate<? super T> predicate) {
        int i = MathHelper.func_76128_c((axisalignedbb.field_72338_b - 2.0D) / 16.0D);
        int j = MathHelper.func_76128_c((axisalignedbb.field_72337_e + 2.0D) / 16.0D);

        i = MathHelper.func_76125_a(i, 0, this.field_76645_j.length - 1);
        j = MathHelper.func_76125_a(j, 0, this.field_76645_j.length - 1);

        // Paper start
        int[] counts;
        if (EntityItem.class.isAssignableFrom(oclass)) {
            counts = itemCounts;
        } else if (IInventory.class.isAssignableFrom(oclass)) {
            counts = inventoryEntityCounts;
        } else {
            counts = null;
        }
        // Paper end
        for (int k = i; k <= j; ++k) {
            if (counts != null && counts[k] <= 0) continue; // Paper - Don't check a chunk if it doesn't have the type we are looking for
            Iterator iterator = this.field_76645_j[k].iterator(); // Spigot

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (oclass.isInstance(entity) && entity.func_174813_aQ().func_72326_a(axisalignedbb) && (predicate == null || predicate.apply((T) entity))) { // CraftBukkit - fix decompile error // Spigot - instance check
                    list.add((T) entity); // Fix decompile error
                }
            }
        }

    }

    public boolean func_76601_a(boolean flag) {
        if (flag) {
            if (this.field_76644_m && this.field_76637_e.func_82737_E() != this.field_76641_n || this.field_76643_l) {
                return true;
            }
        }
        // This !flag section should say if s(isModified) or t(hasEntities), then check auto save
        return ((this.field_76643_l || this.field_76644_m) && this.field_76637_e.func_82737_E() >= this.field_76641_n + field_76637_e.paperConfig.autoSavePeriod); // Paper - Make world configurable and incremental
    }

    public Random func_76617_a(long i) {
        return new Random(this.field_76637_e.func_72905_C() + (long) (this.field_76635_g * this.field_76635_g * 4987142) + (long) (this.field_76635_g * 5947611) + (long) (this.field_76647_h * this.field_76647_h) * 4392871L + (long) (this.field_76647_h * 389711) ^ i);
    }

    public boolean func_76621_g() {
        return false;
    }

    // CraftBukkit start
    public void loadNearby(IChunkProvider ichunkprovider, IChunkGenerator chunkgenerator, boolean newChunk) {
        field_76637_e.timings.syncChunkLoadPostTimer.startTiming(); // Paper
        Server server = field_76637_e.getServer();
        if (server != null) {
            /*
             * If it's a new world, the first few chunks are generated inside
             * the World constructor. We can't reliably alter that, so we have
             * no way of creating a CraftWorld/CraftServer at that point.
             */
            server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(bukkitChunk, newChunk));
        }

        // Update neighbor counts
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                Chunk neighbor = func_177412_p().getChunkIfLoaded(field_76635_g + x, field_76647_h + z);
                if (neighbor != null) {
                    neighbor.setNeighborLoaded(-x, -z);
                    setNeighborLoaded(x, z);
                }
            }
        }
        // CraftBukkit end
        field_76637_e.timings.syncChunkLoadPostTimer.stopTiming(); // Paper
        field_76637_e.timings.syncChunkLoadPopulateNeighbors.startTiming(); // Paper
        Chunk chunk = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g, this.field_76647_h - 1); // Paper
        Chunk chunk1 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g + 1, this.field_76647_h); // Paper
        Chunk chunk2 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g, this.field_76647_h + 1); // Paper
        Chunk chunk3 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g - 1, this.field_76647_h); // Paper

        if (chunk1 != null && chunk2 != null && MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g + 1, this.field_76647_h + 1) != null) { // Paper
            this.func_186034_a(chunkgenerator);
        }

        if (chunk3 != null && chunk2 != null && MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g - 1, this.field_76647_h + 1) != null) { // Paper
            chunk3.func_186034_a(chunkgenerator);
        }

        if (chunk != null && chunk1 != null && MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g + 1, this.field_76647_h - 1) != null) { // Paper
            chunk.func_186034_a(chunkgenerator);
        }

        if (chunk != null && chunk3 != null) {
            Chunk chunk4 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.field_76635_g - 1, this.field_76647_h - 1); // Paper

            if (chunk4 != null) {
                chunk4.func_186034_a(chunkgenerator);
            }
        }
        field_76637_e.timings.syncChunkLoadPopulateNeighbors.stopTiming(); // Paper

    }

    protected void func_186034_a(IChunkGenerator chunkgenerator) {
        if (this.func_177419_t()) {
            if (chunkgenerator.func_185933_a(this, this.field_76635_g, this.field_76647_h)) {
                this.func_76630_e();
            }
        } else {
            this.func_150809_p();
            chunkgenerator.func_185931_b(this.field_76635_g, this.field_76647_h);

            // CraftBukkit start
            BlockSand.field_149832_M = true;
            Random random = new Random();
            random.setSeed(field_76637_e.func_72905_C());
            long xRand = random.nextLong() / 2L * 2L + 1L;
            long zRand = random.nextLong() / 2L * 2L + 1L;
            random.setSeed((long) field_76635_g * xRand + (long) field_76647_h * zRand ^ field_76637_e.func_72905_C());

            org.bukkit.World world = this.field_76637_e.getWorld();
            if (world != null) {
                this.field_76637_e.populating = true;
                try {
                    for (org.bukkit.generator.BlockPopulator populator : world.getPopulators()) {
                        populator.populate(world, random, bukkitChunk);
                    }
                } finally {
                    this.field_76637_e.populating = false;
                }
            }
            BlockSand.field_149832_M = false;
            this.field_76637_e.getServer().getPluginManager().callEvent(new org.bukkit.event.world.ChunkPopulateEvent(bukkitChunk));
            // CraftBukkit end
            this.func_76630_e();
        }

    }

    public BlockPos func_177440_h(BlockPos blockposition) {
        int i = blockposition.func_177958_n() & 15;
        int j = blockposition.func_177952_p() & 15;
        int k = i | j << 4;
        BlockPos blockposition1 = new BlockPos(blockposition.func_177958_n(), this.field_76638_b[k], blockposition.func_177952_p());

        if (blockposition1.func_177956_o() == -999) {
            int l = this.func_76625_h() + 15;

            blockposition1 = new BlockPos(blockposition.func_177958_n(), l, blockposition.func_177952_p());
            int i1 = -1;

            while (blockposition1.func_177956_o() > 0 && i1 == -1) {
                IBlockState iblockdata = this.func_177435_g(blockposition1);
                Material material = iblockdata.func_185904_a();

                if (!material.func_76230_c() && !material.func_76224_d()) {
                    blockposition1 = blockposition1.func_177977_b();
                } else {
                    i1 = blockposition1.func_177956_o() + 1;
                }
            }

            this.field_76638_b[k] = i1;
        }

        return new BlockPos(blockposition.func_177958_n(), this.field_76638_b[k], blockposition.func_177952_p());
    }

    public void func_150804_b(boolean flag) {
        if (this.field_76650_s && this.field_76637_e.field_73011_w.func_191066_m() && !flag) {
            this.func_150803_c(this.field_76637_e.field_72995_K);
        }

        this.field_150815_m = true;
        if (!this.field_150814_l && this.field_76646_k && this.field_76637_e.spigotConfig.randomLightUpdates) { // Spigot - also use random light updates setting to determine if we should relight
            this.func_150809_p();
        }

        while (!this.field_177447_w.isEmpty()) {
            BlockPos blockposition = (BlockPos) this.field_177447_w.poll();

            if (this.func_177424_a(blockposition, Chunk.EnumCreateEntityType.CHECK) == null && this.func_177435_g(blockposition).func_177230_c().func_149716_u()) {
                TileEntity tileentity = this.func_177422_i(blockposition);

                this.field_76637_e.func_175690_a(blockposition, tileentity);
                this.field_76637_e.func_175704_b(blockposition, blockposition);
            }
        }

    }

    public boolean func_150802_k() {
        // Spigot Start
        /*
         * As of 1.7, Mojang added a check to make sure that only chunks which have been lit are sent to the client.
         * Unfortunately this interferes with our modified chunk ticking algorithm, which will only tick chunks distant from the player on a very infrequent basis.
         * We cannot unfortunately do this lighting stage during chunk gen as it appears to put a lot more noticeable load on the server, than when it is done at play time.
         * For now at least we will simply send all chunks, in accordance with pre 1.7 behaviour.
         */
        // Paper Start
        // if randomLightUpdates are disabled, we should always return true, otherwise chunks may never send
        // to the client due to not being lit, otherwise retain standard behavior and only send properly lit chunks.
        return !this.field_76637_e.spigotConfig.randomLightUpdates || (this.isTicked() && this.field_76646_k && this.field_150814_l);
        // Paper End
        // Spigot End
    }

    public boolean func_186035_j() {
        return this.field_150815_m;
    }

    public ChunkPos func_76632_l() {
        return new ChunkPos(this.field_76635_g, this.field_76647_h);
    }

    public boolean func_76606_c(int i, int j) {
        if (i < 0) {
            i = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        for (int k = i; k <= j; k += 16) {
            ExtendedBlockStorage chunksection = this.field_76652_q[k >> 4];

            if (chunksection != Chunk.field_186036_a && !chunksection.func_76663_a()) {
                return false;
            }
        }

        return true;
    }

    public void func_76602_a(ExtendedBlockStorage[] achunksection) {
        if (this.field_76652_q.length != achunksection.length) {
            Chunk.field_150817_t.warn("Could not set level chunk sections, array length is {} instead of {}", Integer.valueOf(achunksection.length), Integer.valueOf(this.field_76652_q.length));
        } else {
            System.arraycopy(achunksection, 0, this.field_76652_q, 0, this.field_76652_q.length);
        }
    }

    public Biome func_177411_a(BlockPos blockposition, BiomeProvider worldchunkmanager) {
        int i = blockposition.func_177958_n() & 15;
        int j = blockposition.func_177952_p() & 15;
        int k = this.field_76651_r[j << 4 | i] & 255;
        Biome biomebase;

        if (k == 255) {
            biomebase = worldchunkmanager.func_180300_a(blockposition, Biomes.field_76772_c);
            k = Biome.func_185362_a(biomebase);
            this.field_76651_r[j << 4 | i] = (byte) (k & 255);
        }

        biomebase = Biome.func_150568_d(k);
        return biomebase == null ? Biomes.field_76772_c : biomebase;
    }

    public byte[] func_76605_m() {
        return this.field_76651_r;
    }

    public void func_76616_a(byte[] abyte) {
        if (this.field_76651_r.length != abyte.length) {
            Chunk.field_150817_t.warn("Could not set level chunk biomes, array length is {} instead of {}", Integer.valueOf(abyte.length), Integer.valueOf(this.field_76651_r.length));
        } else {
            System.arraycopy(abyte, 0, this.field_76651_r, 0, this.field_76651_r.length);
        }
    }

    public void func_76613_n() {
        this.field_76649_t = 0;
    }

    public void func_76594_o() {
        if (this.field_76649_t < 4096) {
            BlockPos blockposition = new BlockPos(this.field_76635_g << 4, 0, this.field_76647_h << 4);

            for (int i = 0; i < 8; ++i) {
                if (this.field_76649_t >= 4096) {
                    return;
                }

                int j = this.field_76649_t % 16;
                int k = this.field_76649_t / 16 % 16;
                int l = this.field_76649_t / 256;

                ++this.field_76649_t;

                for (int i1 = 0; i1 < 16; ++i1) {
                    BlockPos blockposition1 = blockposition.func_177982_a(k, (j << 4) + i1, l);
                    boolean flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;

                    if (this.field_76652_q[j] == Chunk.field_186036_a && flag || this.field_76652_q[j] != Chunk.field_186036_a && this.field_76652_q[j].func_177485_a(k, i1, l).func_185904_a() == Material.field_151579_a) {
                        EnumFacing[] aenumdirection = EnumFacing.values();
                        int j1 = aenumdirection.length;

                        for (int k1 = 0; k1 < j1; ++k1) {
                            EnumFacing enumdirection = aenumdirection[k1];
                            BlockPos blockposition2 = blockposition1.func_177972_a(enumdirection);

                            if (this.field_76637_e.func_180495_p(blockposition2).func_185906_d() > 0) {
                                this.field_76637_e.func_175664_x(blockposition2);
                            }
                        }

                        this.field_76637_e.func_175664_x(blockposition1);
                    }
                }
            }

        }
    }

    public void func_150809_p() {
        field_76637_e.timings.lightChunk.startTiming(); // Paper
        this.field_76646_k = true;
        this.field_150814_l = true;
        BlockPos blockposition = new BlockPos(this.field_76635_g << 4, 0, this.field_76647_h << 4);

        if (this.field_76637_e.field_73011_w.func_191066_m()) {
            if (this.field_76637_e.func_175707_a(blockposition.func_177982_a(-1, 0, -1), blockposition.func_177982_a(16, this.field_76637_e.func_181545_F(), 16))) {
                label42:
                for (int i = 0; i < 16; ++i) {
                    for (int j = 0; j < 16; ++j) {
                        if (!this.func_150811_f(i, j)) {
                            this.field_150814_l = false;
                            break label42;
                        }
                    }
                }

                if (this.field_150814_l) {
                    Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                    while (iterator.hasNext()) {
                        EnumFacing enumdirection = (EnumFacing) iterator.next();
                        int k = enumdirection.func_176743_c() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;

                        this.field_76637_e.func_175726_f(blockposition.func_177967_a(enumdirection, k)).func_180700_a(enumdirection.func_176734_d());
                    }

                    this.func_177441_y();
                }
            } else {
                this.field_150814_l = false;
            }
        }

        field_76637_e.timings.lightChunk.stopTiming(); // Paper
    }

    private void func_177441_y() {
        for (int i = 0; i < this.field_76639_c.length; ++i) {
            this.field_76639_c[i] = true;
        }

        this.func_150803_c(false);
    }

    private void func_180700_a(EnumFacing enumdirection) {
        if (this.field_76646_k) {
            int i;

            if (enumdirection == EnumFacing.EAST) {
                for (i = 0; i < 16; ++i) {
                    this.func_150811_f(15, i);
                }
            } else if (enumdirection == EnumFacing.WEST) {
                for (i = 0; i < 16; ++i) {
                    this.func_150811_f(0, i);
                }
            } else if (enumdirection == EnumFacing.SOUTH) {
                for (i = 0; i < 16; ++i) {
                    this.func_150811_f(i, 15);
                }
            } else if (enumdirection == EnumFacing.NORTH) {
                for (i = 0; i < 16; ++i) {
                    this.func_150811_f(i, 0);
                }
            }

        }
    }

    private boolean func_150811_f(int i, int j) {
        int k = this.func_76625_h();
        boolean flag = false;
        boolean flag1 = false;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos((this.field_76635_g << 4) + i, 0, (this.field_76647_h << 4) + j);

        int l;

        for (l = k + 16 - 1; l > this.field_76637_e.func_181545_F() || l > 0 && !flag1; --l) {
            blockposition_mutableblockposition.func_181079_c(blockposition_mutableblockposition.func_177958_n(), l, blockposition_mutableblockposition.func_177952_p());
            int i1 = this.func_177437_b((BlockPos) blockposition_mutableblockposition);

            if (i1 == 255 && blockposition_mutableblockposition.func_177956_o() < this.field_76637_e.func_181545_F()) {
                flag1 = true;
            }

            if (!flag && i1 > 0) {
                flag = true;
            } else if (flag && i1 == 0 && !this.field_76637_e.func_175664_x(blockposition_mutableblockposition)) {
                return false;
            }
        }

        for (l = blockposition_mutableblockposition.func_177956_o(); l > 0; --l) {
            blockposition_mutableblockposition.func_181079_c(blockposition_mutableblockposition.func_177958_n(), l, blockposition_mutableblockposition.func_177952_p());
            if (this.func_177435_g(blockposition_mutableblockposition).func_185906_d() > 0) {
                this.field_76637_e.func_175664_x(blockposition_mutableblockposition);
            }
        }

        return true;
    }

    public boolean func_177410_o() {
        return this.field_76636_d;
    }

    public World func_177412_p() {
        return this.field_76637_e;
    }

    public int[] func_177445_q() {
        return this.field_76634_f;
    }

    public void func_177420_a(int[] aint) {
        if (this.field_76634_f.length != aint.length) {
            Chunk.field_150817_t.warn("Could not set level chunk heightmap, array length is {} instead of {}", Integer.valueOf(aint.length), Integer.valueOf(this.field_76634_f.length));
        } else {
            System.arraycopy(aint, 0, this.field_76634_f, 0, this.field_76634_f.length);
        }
    }

    public Map<BlockPos, TileEntity> func_177434_r() {
        return this.field_150816_i;
    }

    public List<Entity>[] getEntitySlices() {
        return this.field_76645_j;
    }

    public boolean func_177419_t() {
        return this.field_76646_k;
    }

    public void func_177446_d(boolean flag) {
        this.field_76646_k = flag;
    }

    public boolean func_177423_u() {
        return this.field_150814_l;
    }

    public void func_177421_e(boolean flag) {
        this.field_150814_l = flag;
    }

    public void func_177427_f(boolean flag) {
        this.field_76643_l = flag;
    }

    public void func_177409_g(boolean flag) {
        this.field_76644_m = flag;
    }

    public void func_177432_b(long i) {
        this.field_76641_n = i;
    }

    public int func_177442_v() {
        return this.field_82912_p;
    }

    public long func_177416_w() {
        return field_76637_e.paperConfig.useInhabitedTime ? this.field_111204_q : 0; // Paper
    }

    public void func_177415_c(long i) {
        this.field_111204_q = i;
    }

    // Paper start
    public void runOrQueueLightUpdate(Runnable runnable) {
        if (this.field_76637_e.paperConfig.queueLightUpdates) {
            lightingQueue.add(runnable);
        } else {
            runnable.run();
        }
    }
    // Paper end

    public static enum EnumCreateEntityType {

        IMMEDIATE, QUEUED, CHECK;

        private EnumCreateEntityType() {}
    }
}

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

    private static final Logger LOGGER = LogManager.getLogger();
    public static final ExtendedBlockStorage NULL_BLOCK_STORAGE = null; public static final ExtendedBlockStorage EMPTY_CHUNK_SECTION = Chunk.NULL_BLOCK_STORAGE; // Paper - OBFHELPER
    private final ExtendedBlockStorage[] storageArrays;
    private final byte[] blockBiomeArray;
    private final int[] precipitationHeightMap;
    private final boolean[] updateSkylightColumns;
    private boolean loaded;
    public final World world;
    public final int[] heightMap;
    public Long scheduledForUnload; // Paper - delay chunk unloads
    public final int x;
    public final int z;
    private boolean isGapLightingUpdated;
    public final Map<BlockPos, TileEntity> tileEntities;
    public final List<Entity>[] entityLists; // Spigot
    public final PaperLightingQueue.LightingQueue lightingQueue = new PaperLightingQueue.LightingQueue(this); // Paper
    private boolean isTerrainPopulated;
    private boolean isLightPopulated;
    private boolean ticked; private boolean isTicked() { return ticked; }; // Paper - OBFHELPER
    private boolean dirty;
    private boolean hasEntities;
    private long lastSaveTime;
    private int heightMapMinimum;
    private long inhabitedTime;
    private int queuedLightChecks;
    private final ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue;
    public boolean unloadQueued; public void setShouldUnload(boolean unload) { this.unloadQueued = unload; } public boolean isUnloading() { return unloadQueued; } // Paper - OBFHELPER
    public gnu.trove.map.hash.TObjectIntHashMap<Class> entityCount = new gnu.trove.map.hash.TObjectIntHashMap<Class>(); // Spigot

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
        this.storageArrays = new ExtendedBlockStorage[16];
        this.blockBiomeArray = new byte[256];
        this.precipitationHeightMap = new int[256];
        this.updateSkylightColumns = new boolean[256];
        this.tileEntities = Maps.newHashMap();
        this.queuedLightChecks = 4096;
        this.tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
        this.entityLists = (new List[16]); // Spigot
        this.world = world;
        this.x = i;
        this.z = j;
        this.heightMap = new int[256];

        for (int k = 0; k < this.entityLists.length; ++k) {
            this.entityLists[k] = new org.bukkit.craftbukkit.util.UnsafeList(); // Spigot
        }

        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte) -1);
        // CraftBukkit start
        this.bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(this);
        this.chunkKey = ChunkPos.asLong(this.x, this.z);
    }

    public org.bukkit.Chunk bukkitChunk;
    public boolean mustSave;
    // CraftBukkit end

    public Chunk(World world, ChunkPrimer chunksnapshot, int i, int j) {
        this(world, i, j);
        boolean flag = true;
        boolean flag1 = world.provider.hasSkyLight();

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                for (int i1 = 0; i1 < 256; ++i1) {
                    IBlockState iblockdata = chunksnapshot.getBlockState(k, i1, l);

                    if (iblockdata.getMaterial() != Material.AIR) {
                        int j1 = i1 >> 4;

                        if (this.storageArrays[j1] == Chunk.NULL_BLOCK_STORAGE) {
                            this.storageArrays[j1] = new ExtendedBlockStorage(j1 << 4, flag1, world.chunkPacketBlockController.getPredefinedBlockData(this, j1)); // Paper - Anti-Xray - Add predefined block data
                        }

                        this.storageArrays[j1].set(k, i1 & 15, l, iblockdata);
                    }
                }
            }
        }

    }

    public boolean isAtLocation(int i, int j) {
        return i == this.x && j == this.z;
    }

    public int getHeight(BlockPos blockposition) {
        return this.getHeightValue(blockposition.getX() & 15, blockposition.getZ() & 15);
    }

    public int getHeightValue(int i, int j) {
        return this.heightMap[j << 4 | i];
    }

    @Nullable
    private ExtendedBlockStorage getLastExtendedBlockStorage() {
        for (int i = this.storageArrays.length - 1; i >= 0; --i) {
            if (this.storageArrays[i] != Chunk.NULL_BLOCK_STORAGE) {
                return this.storageArrays[i];
            }
        }

        return null;
    }

    public int getTopFilledSegment() {
        ExtendedBlockStorage chunksection = this.getLastExtendedBlockStorage();

        return chunksection == null ? 0 : chunksection.getYLocation();
    }

    public ExtendedBlockStorage[] getBlockStorageArray() {
        return this.storageArrays;
    }

    public void generateSkylightMap() {
        int i = this.getTopFilledSegment();

        this.heightMapMinimum = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j) {
            int k = 0;

            while (k < 16) {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                int l = i + 16;

                while (true) {
                    if (l > 0) {
                        if (this.getBlockLightOpacity(j, l - 1, k) == 0) {
                            --l;
                            continue;
                        }

                        this.heightMap[k << 4 | j] = l;
                        if (l < this.heightMapMinimum) {
                            this.heightMapMinimum = l;
                        }
                    }

                    if (this.world.provider.hasSkyLight()) {
                        l = 15;
                        int i1 = i + 16 - 1;

                        do {
                            int j1 = this.getBlockLightOpacity(j, i1, k);

                            if (j1 == 0 && l != 15) {
                                j1 = 1;
                            }

                            l -= j1;
                            if (l > 0) {
                                ExtendedBlockStorage chunksection = this.storageArrays[i1 >> 4];

                                if (chunksection != Chunk.NULL_BLOCK_STORAGE) {
                                    chunksection.setSkyLight(j, i1 & 15, k, l);
                                    this.world.notifyLightSet(new BlockPos((this.x << 4) + j, i1, (this.z << 4) + k));
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

        this.dirty = true;
    }

    private void propagateSkylightOcclusion(int i, int j) {
        this.updateSkylightColumns[i + j * 16] = true;
        this.isGapLightingUpdated = true;
    }

    private void recheckGaps(boolean flag) {
        this.world.profiler.startSection("recheckGaps");
        if (this.world.isAreaLoaded(new BlockPos(this.x * 16 + 8, 0, this.z * 16 + 8), 16)) {
            this.runOrQueueLightUpdate(() -> recheckGaps(flag)); // Paper - Queue light update
        }
    }

    private void recheckGaps(boolean flag) {
        if (true) {
            // Paper end
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (this.updateSkylightColumns[i + j * 16]) {
                        this.updateSkylightColumns[i + j * 16] = false;
                        int k = this.getHeightValue(i, j);
                        int l = this.x * 16 + i;
                        int i1 = this.z * 16 + j;
                        int j1 = Integer.MAX_VALUE;

                        Iterator iterator;
                        EnumFacing enumdirection;

                        for (iterator = EnumFacing.Plane.HORIZONTAL.iterator(); iterator.hasNext(); j1 = Math.min(j1, this.world.getChunksLowestHorizon(l + enumdirection.getFrontOffsetX(), i1 + enumdirection.getFrontOffsetZ()))) {
                            enumdirection = (EnumFacing) iterator.next();
                        }

                        this.checkSkylightNeighborHeight(l, i1, j1);
                        iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                        while (iterator.hasNext()) {
                            enumdirection = (EnumFacing) iterator.next();
                            this.checkSkylightNeighborHeight(l + enumdirection.getFrontOffsetX(), i1 + enumdirection.getFrontOffsetZ(), k);
                        }

                        if (flag) {
                            this.world.profiler.endSection();
                            return;
                        }
                    }
                }
            }

            this.isGapLightingUpdated = false;
        }

        this.world.profiler.endSection();
    }

    private void checkSkylightNeighborHeight(int i, int j, int k) {
        int l = this.world.getHeight(new BlockPos(i, 0, j)).getY();

        if (l > k) {
            this.updateSkylightNeighborHeight(i, j, k, l + 1);
        } else if (l < k) {
            this.updateSkylightNeighborHeight(i, j, l, k + 1);
        }

    }

    private void updateSkylightNeighborHeight(int i, int j, int k, int l) {
        if (l > k && this.world.isAreaLoaded(new BlockPos(i, 0, j), 16)) {
            for (int i1 = k; i1 < l; ++i1) {
                this.world.checkLightFor(EnumSkyBlock.SKY, new BlockPos(i, i1, j));
            }

            this.dirty = true;
        }

    }

    private void relightBlock(int i, int j, int k) {
        int l = this.heightMap[k << 4 | i] & 255;
        int i1 = l;

        if (j > l) {
            i1 = j;
        }

        while (i1 > 0 && this.getBlockLightOpacity(i, i1 - 1, k) == 0) {
            --i1;
        }

        if (i1 != l) {
            this.world.markBlocksDirtyVertical(i + this.x * 16, k + this.z * 16, i1, l);
            this.heightMap[k << 4 | i] = i1;
            int j1 = this.x * 16 + i;
            int k1 = this.z * 16 + k;
            int l1;
            int i2;

            if (this.world.provider.hasSkyLight()) {
                ExtendedBlockStorage chunksection;

                if (i1 < l) {
                    for (l1 = i1; l1 < l; ++l1) {
                        chunksection = this.storageArrays[l1 >> 4];
                        if (chunksection != Chunk.NULL_BLOCK_STORAGE) {
                            chunksection.setSkyLight(i, l1 & 15, k, 15);
                            this.world.notifyLightSet(new BlockPos((this.x << 4) + i, l1, (this.z << 4) + k));
                        }
                    }
                } else {
                    for (l1 = l; l1 < i1; ++l1) {
                        chunksection = this.storageArrays[l1 >> 4];
                        if (chunksection != Chunk.NULL_BLOCK_STORAGE) {
                            chunksection.setSkyLight(i, l1 & 15, k, 0);
                            this.world.notifyLightSet(new BlockPos((this.x << 4) + i, l1, (this.z << 4) + k));
                        }
                    }
                }

                l1 = 15;

                while (i1 > 0 && l1 > 0) {
                    --i1;
                    i2 = this.getBlockLightOpacity(i, i1, k);
                    if (i2 == 0) {
                        i2 = 1;
                    }

                    l1 -= i2;
                    if (l1 < 0) {
                        l1 = 0;
                    }

                    ExtendedBlockStorage chunksection1 = this.storageArrays[i1 >> 4];

                    if (chunksection1 != Chunk.NULL_BLOCK_STORAGE) {
                        chunksection1.setSkyLight(i, i1 & 15, k, l1);
                    }
                }
            }

            l1 = this.heightMap[k << 4 | i];
            i2 = l;
            int j2 = l1;

            if (l1 < l) {
                i2 = l1;
                j2 = l;
            }

            if (l1 < this.heightMapMinimum) {
                this.heightMapMinimum = l1;
            }

            if (this.world.provider.hasSkyLight()) {
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                    this.updateSkylightNeighborHeight(j1 + enumdirection.getFrontOffsetX(), k1 + enumdirection.getFrontOffsetZ(), i2, j2);
                }

                this.updateSkylightNeighborHeight(j1, k1, i2, j2);
            }

            this.dirty = true;
        }
    }

    public int getBlockLightOpacity(BlockPos blockposition) {
        return this.getBlockState(blockposition).getLightOpacity();
    }

    private int getBlockLightOpacity(int i, int j, int k) {
        return this.getBlockState(i, j, k).getLightOpacity();
    }

    // Paper start - Optimize getBlockData to reduce instructions
    public final IBlockState getBlockState(final BlockPos pos) {
        return getBlockData(pos.getX(), pos.getY(), pos.getZ());
    }

    public final IBlockState getBlockData(final int x, final int y, final int z) {
        // Method body / logic copied from below
        final int i = y >> 4;
        if (y >= 0 && i < this.storageArrays.length && this.storageArrays[i] != null) {
            // Inlined ChunkSection.getType() and DataPaletteBlock.a(int,int,int)
            return this.storageArrays[i].data.get((y & 15) << 8 | (z & 15) << 4 | x & 15);
        }
        return Blocks.AIR.getDefaultState();
    }

    public IBlockState getBlockState(final int i, final int j, final int k) {
        return getBlockData(i, j, k);
    }

    public IBlockState unused(final int i, final int j, final int k) {
    // Paper end
        if (this.world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            IBlockState iblockdata = null;

            if (j == 60) {
                iblockdata = Blocks.BARRIER.getDefaultState();
            }

            if (j == 70) {
                iblockdata = ChunkGeneratorDebug.getBlockStateFor(i, k);
            }

            return iblockdata == null ? Blocks.AIR.getDefaultState() : iblockdata;
        } else {
            try {
                if (j >= 0 && j >> 4 < this.storageArrays.length) {
                    ExtendedBlockStorage chunksection = this.storageArrays[j >> 4];

                    if (chunksection != Chunk.NULL_BLOCK_STORAGE) {
                        return chunksection.get(i & 15, j & 15, k & 15);
                    }
                }

                return Blocks.AIR.getDefaultState();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Block being got");

                crashreportsystemdetails.addDetail("Location", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return CrashReportCategory.getCoordinateInfo(i, j, k);
                    }

                    @Override
                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    @Nullable
    public IBlockState setBlockState(BlockPos blockposition, IBlockState iblockdata) {
        int i = blockposition.getX() & 15;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 15;
        int l = k << 4 | i;

        if (j >= this.precipitationHeightMap[l] - 1) {
            this.precipitationHeightMap[l] = -999;
        }

        int i1 = this.heightMap[l];
        IBlockState iblockdata1 = this.getBlockState(blockposition);

        if (iblockdata1 == iblockdata) {
            return null;
        } else {
            Block block = iblockdata.getBlock();
            Block block1 = iblockdata1.getBlock();
            ExtendedBlockStorage chunksection = this.storageArrays[j >> 4];
            boolean flag = false;

            if (chunksection == Chunk.NULL_BLOCK_STORAGE) {
                if (block == Blocks.AIR) {
                    return null;
                }

                chunksection = new ExtendedBlockStorage(j >> 4 << 4, this.world.provider.hasSkyLight(), this.world.chunkPacketBlockController.getPredefinedBlockData(this, j >> 4)); // Paper - Anti-Xray - Add predefined block data
                this.storageArrays[j >> 4] = chunksection;
                flag = j >= i1;
            }

            chunksection.set(i, j & 15, k, iblockdata);
            if (block1 != block) {
                if (!this.world.isRemote) {
                    block1.breakBlock(this.world, blockposition, iblockdata1);
                } else if (block1 instanceof ITileEntityProvider) {
                    this.world.removeTileEntity(blockposition);
                }
            }

            if (chunksection.get(i, j & 15, k).getBlock() != block) {
                return null;
            } else {
                if (flag) {
                    this.generateSkylightMap();
                } else {
                    this.runOrQueueLightUpdate(() -> { // Paper - Queue light update
                    int j1 = iblockdata.getLightOpacity();
                    int k1 = iblockdata1.getLightOpacity();

                    if (j1 > 0) {
                        if (j >= i1) {
                            this.relightBlock(i, j + 1, k);
                        }
                    } else if (j == i1 - 1) {
                        this.relightBlock(i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, blockposition) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, blockposition) > 0)) {
                        this.propagateSkylightOcclusion(i, k);
                    }
                    }); // Paper
                }

                TileEntity tileentity;

                if (block1 instanceof ITileEntityProvider) {
                    tileentity = this.getTileEntity(blockposition, Chunk.EnumCreateEntityType.CHECK);
                    if (tileentity != null) {
                        tileentity.updateContainingBlockInfo();
                    }
                }

                // CraftBukkit - Don't place while processing the BlockPlaceEvent, unless it's a BlockContainer. Prevents blocks such as TNT from activating when cancelled.
                if (!this.world.isRemote && block1 != block  && (!this.world.captureBlockStates || block instanceof BlockContainer)) {
                    block.onBlockAdded(this.world, blockposition, iblockdata);
                }

                if (block instanceof ITileEntityProvider) {
                    tileentity = this.getTileEntity(blockposition, Chunk.EnumCreateEntityType.CHECK);
                    if (tileentity == null) {
                        tileentity = ((ITileEntityProvider) block).createNewTileEntity(this.world, block.getMetaFromState(iblockdata));
                        this.world.setTileEntity(blockposition, tileentity);
                    }

                    if (tileentity != null) {
                        tileentity.updateContainingBlockInfo();
                    }
                }

                this.dirty = true;
                return iblockdata1;
            }
        }
    }

    public int getLightFor(EnumSkyBlock enumskyblock, BlockPos blockposition) {
        int i = blockposition.getX() & 15;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 15;
        ExtendedBlockStorage chunksection = this.storageArrays[j >> 4];

        return chunksection == Chunk.NULL_BLOCK_STORAGE ? (this.canSeeSky(blockposition) ? enumskyblock.defaultLightValue : 0) : (enumskyblock == EnumSkyBlock.SKY ? (!this.world.provider.hasSkyLight() ? 0 : chunksection.getSkyLight(i, j & 15, k)) : (enumskyblock == EnumSkyBlock.BLOCK ? chunksection.getBlockLight(i, j & 15, k) : enumskyblock.defaultLightValue));
    }

    public void setLightFor(EnumSkyBlock enumskyblock, BlockPos blockposition, int i) {
        int j = blockposition.getX() & 15;
        int k = blockposition.getY();
        int l = blockposition.getZ() & 15;
        ExtendedBlockStorage chunksection = this.storageArrays[k >> 4];

        if (chunksection == Chunk.NULL_BLOCK_STORAGE) {
            chunksection = new ExtendedBlockStorage(k >> 4 << 4, this.world.provider.hasSkyLight(), this.world.chunkPacketBlockController.getPredefinedBlockData(this, k >> 4)); // Paper - Anti-Xray - Add predefined block data
            this.storageArrays[k >> 4] = chunksection;
            this.generateSkylightMap();
        }

        this.dirty = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            if (this.world.provider.hasSkyLight()) {
                chunksection.setSkyLight(j, k & 15, l, i);
            }
        } else if (enumskyblock == EnumSkyBlock.BLOCK) {
            chunksection.setBlockLight(j, k & 15, l, i);
        }

    }

    public final int getLightSubtracted(BlockPos blockposition, int i) { return this.getLightSubtracted(blockposition, i); } // Paper - OBFHELPER
    public int getLightSubtracted(BlockPos blockposition, int i) {
        int j = blockposition.getX() & 15;
        int k = blockposition.getY();
        int l = blockposition.getZ() & 15;
        ExtendedBlockStorage chunksection = this.storageArrays[k >> 4];

        if (chunksection == Chunk.NULL_BLOCK_STORAGE) {
            return this.world.provider.hasSkyLight() && i < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - i : 0;
        } else {
            int i1 = !this.world.provider.hasSkyLight() ? 0 : chunksection.getSkyLight(j, k & 15, l);

            i1 -= i;
            int j1 = chunksection.getBlockLight(j, k & 15, l);

            if (j1 > i1) {
                i1 = j1;
            }

            return i1;
        }
    }

    public void addEntity(Entity entity) {
        this.hasEntities = true;
        int i = MathHelper.floor(entity.posX / 16.0D);
        int j = MathHelper.floor(entity.posZ / 16.0D);

        if (i != this.x || j != this.z) {
            Chunk.LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(this.x), Integer.valueOf(this.z), entity);
            entity.setDead();
        }

        int k = MathHelper.floor(entity.posY / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.entityLists.length) {
            k = this.entityLists.length - 1;
        }

        entity.addedToChunk = true;
        entity.chunkCoordX = this.x;
        entity.chunkCoordY = k;
        entity.chunkCoordZ = this.z;
        this.entityLists[k].add(entity);
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
            if (entityinsentient.canDespawn() && entityinsentient.isNoDespawnRequired()) {
                return;
            }
        }
        for ( EnumCreatureType creatureType : EnumCreatureType.values() )
        {
            if ( creatureType.getCreatureClass().isAssignableFrom( entity.getClass() ) )
            {
                this.entityCount.adjustOrPutValue( creatureType.getCreatureClass(), 1, 1 );
            }
        }
        // Spigot end
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAtIndex(entity, entity.chunkCoordY);
    }

    public void removeEntityAtIndex(Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }

        if (i >= this.entityLists.length) {
            i = this.entityLists.length - 1;
        }

        if (!this.entityLists[i].remove(entity)) { return; } // Paper
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
            if (entityinsentient.canDespawn() && entityinsentient.isNoDespawnRequired()) {
                return;
            }
        }
        for ( EnumCreatureType creatureType : EnumCreatureType.values() )
        {
            if ( creatureType.getCreatureClass().isAssignableFrom( entity.getClass() ) )
            {
                this.entityCount.adjustValue( creatureType.getCreatureClass(), -1 );
            }
        }
        // Spigot end
    }

    public boolean canSeeSky(BlockPos blockposition) {
        int i = blockposition.getX() & 15;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 15;

        return j >= this.heightMap[k << 4 | i];
    }

    @Nullable
    private TileEntity createNewTileEntity(BlockPos blockposition) {
        IBlockState iblockdata = this.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        return !block.hasTileEntity() ? null : ((ITileEntityProvider) block).createNewTileEntity(this.world, iblockdata.getBlock().getMetaFromState(iblockdata));
    }

    @Nullable public final TileEntity getTileEntityImmediately(BlockPos pos) { return this.getTileEntity(pos, EnumCreateEntityType.IMMEDIATE); } // Paper - OBFHELPER
    @Nullable
    public TileEntity getTileEntity(BlockPos blockposition, Chunk.EnumCreateEntityType chunk_enumtileentitystate) {
        // CraftBukkit start
        TileEntity tileentity = null;
        if (world.captureBlockStates) {
            tileentity = world.capturedTileEntities.get(blockposition);
        }
        if (tileentity == null) {
            tileentity = this.tileEntities.get(blockposition);
        }
        // CraftBukkit end

        if (tileentity == null) {
            if (chunk_enumtileentitystate == Chunk.EnumCreateEntityType.IMMEDIATE) {
                tileentity = this.createNewTileEntity(blockposition);
                this.world.setTileEntity(blockposition, tileentity);
            } else if (chunk_enumtileentitystate == Chunk.EnumCreateEntityType.QUEUED) {
                this.tileEntityPosQueue.add(blockposition);
            }
        } else if (tileentity.isInvalid()) {
            this.tileEntities.remove(blockposition);
            return null;
        }

        return tileentity;
    }

    public void addTileEntity(TileEntity tileentity) {
        this.addTileEntity(tileentity.getPos(), tileentity);
        if (this.loaded) {
            this.world.addTileEntity(tileentity);
        }

    }

    public void addTileEntity(BlockPos blockposition, TileEntity tileentity) {
        tileentity.setWorld(this.world);
        tileentity.setPos(blockposition);
        if (this.getBlockState(blockposition).getBlock() instanceof ITileEntityProvider) {
            if (this.tileEntities.containsKey(blockposition)) {
                this.tileEntities.get(blockposition).invalidate();
            }

            tileentity.validate();
            this.tileEntities.put(blockposition, tileentity);
            // CraftBukkit start
            // Paper start - Remove invalid mob spawner tile entities
        } else if (tileentity instanceof TileEntityMobSpawner && org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(getBlockState(blockposition).getBlock()) != org.bukkit.Material.MOB_SPAWNER) {
            this.tileEntities.remove(blockposition);
            // Paper end
        } else {
            // Paper start
            ServerInternalException e = new ServerInternalException(
                    "Attempted to place a tile entity (" + tileentity + ") at " + tileentity.pos.getX() + ","
                            + tileentity.pos.getY() + "," + tileentity.pos.getZ()
                            + " (" + CraftMagicNumbers.getMaterial(getBlockState(blockposition).getBlock()) + ") where there was no entity tile!\n" +
                            "Chunk coordinates: " + (this.x * 16) + "," + (this.z * 16));
            e.printStackTrace();
            ServerInternalException.reportInternalException(e);

            if (this.world.paperConfig.removeCorruptTEs) {
                this.removeTileEntity(tileentity.getPos());
                this.markDirty();
                org.bukkit.Bukkit.getLogger().info("Removing corrupt tile entity");
            }
            // Paper end
            // CraftBukkit end
        }
    }

    public void removeTileEntity(BlockPos blockposition) { this.removeTileEntity(blockposition); } // Paper - OBFHELPER
    public void removeTileEntity(BlockPos blockposition) {
        if (this.loaded) {
            TileEntity tileentity = this.tileEntities.remove(blockposition);

            if (tileentity != null) {
                tileentity.invalidate();
            }
        }

    }

    public void onLoad() {
        this.loaded = true;
        this.world.addTileEntities(this.tileEntities.values());
        List[] aentityslice = this.entityLists; // Spigot
        int i = aentityslice.length;

        for (int j = 0; j < i; ++j) {
            List entityslice = aentityslice[j]; // Spigot

            this.world.loadEntities(entityslice);
        }

    }

    public void onUnload() {
        this.loaded = false;
        Iterator iterator = this.tileEntities.values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();
            // Spigot Start
            if ( tileentity instanceof IInventory )
            {
                for ( org.bukkit.entity.HumanEntity h : Lists.<org.bukkit.entity.HumanEntity>newArrayList(( (IInventory) tileentity ).getViewers() ) )
                {
                    if ( h instanceof org.bukkit.craftbukkit.entity.CraftHumanEntity )
                    {
                       ( (org.bukkit.craftbukkit.entity.CraftHumanEntity) h).getHandle().closeScreen();
                    }
                }
            }
            // Spigot End

            this.world.markTileEntityForRemoval(tileentity);
        }

        List[] aentityslice = this.entityLists; // Spigot
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
                    for ( org.bukkit.entity.HumanEntity h : Lists.<org.bukkit.entity.HumanEntity>newArrayList( ( (IInventory) entity ).getViewers() ) )
                    {
                        if ( h instanceof org.bukkit.craftbukkit.entity.CraftHumanEntity )
                        {
                           ( (org.bukkit.craftbukkit.entity.CraftHumanEntity) h).getHandle().closeScreen();
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

            this.world.unloadEntities(newList);
            // CraftBukkit end
        }

    }

    public void markDirty() {
        this.dirty = true;
    }

    public void getEntitiesWithinAABBForEntity(@Nullable Entity entity, AxisAlignedBB axisalignedbb, List<Entity> list, Predicate<? super Entity> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp(j, 0, this.entityLists.length - 1);

        for (int k = i; k <= j; ++k) {
            if (!this.entityLists[k].isEmpty()) {
                Iterator iterator = this.entityLists[k].iterator();

                // Paper start - Don't search for inventories if we have none, and that is all we want
                /*
                * We check if they want inventories by seeing if it is the static `IEntitySelector.c`
                *
                * Make sure the inventory selector stays in sync.
                * It should be the one that checks `var1 instanceof IInventory && var1.isAlive()`
                */
                if (predicate == EntitySelectors.HAS_INVENTORY && inventoryEntityCounts[k] <= 0) continue;
                // Paper end
                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    if (entity1.getEntityBoundingBox().intersects(axisalignedbb) && entity1 != entity) {
                        if (predicate == null || predicate.apply(entity1)) {
                            list.add(entity1);
                        }

                        Entity[] aentity = entity1.getParts();

                        if (aentity != null) {
                            Entity[] aentity1 = aentity;
                            int l = aentity.length;

                            for (int i1 = 0; i1 < l; ++i1) {
                                Entity entity2 = aentity1[i1];

                                if (entity2 != entity && entity2.getEntityBoundingBox().intersects(axisalignedbb) && (predicate == null || predicate.apply(entity2))) {
                                    list.add(entity2);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, List<T> list, Predicate<? super T> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp(j, 0, this.entityLists.length - 1);

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
            Iterator iterator = this.entityLists[k].iterator(); // Spigot

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (oclass.isInstance(entity) && entity.getEntityBoundingBox().intersects(axisalignedbb) && (predicate == null || predicate.apply((T) entity))) { // CraftBukkit - fix decompile error // Spigot - instance check
                    list.add((T) entity); // Fix decompile error
                }
            }
        }

    }

    public boolean needsSaving(boolean flag) {
        if (flag) {
            if (this.hasEntities && this.world.getTotalWorldTime() != this.lastSaveTime || this.dirty) {
                return true;
            }
        }
        // This !flag section should say if s(isModified) or t(hasEntities), then check auto save
        return ((this.dirty || this.hasEntities) && this.world.getTotalWorldTime() >= this.lastSaveTime + world.paperConfig.autoSavePeriod); // Paper - Make world configurable and incremental
    }

    public Random getRandomWithSeed(long i) {
        return new Random(this.world.getSeed() + this.x * this.x * 4987142 + this.x * 5947611 + this.z * this.z * 4392871L + this.z * 389711 ^ i);
    }

    public boolean isEmpty() {
        return false;
    }

    // CraftBukkit start
    public void loadNearby(IChunkProvider ichunkprovider, IChunkGenerator chunkgenerator, boolean newChunk) {
        world.timings.syncChunkLoadPostTimer.startTiming(); // Paper
        Server server = world.getServer();
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

                Chunk neighbor = getWorld().getChunkIfLoaded(x + x, z + z);
                if (neighbor != null) {
                    neighbor.setNeighborLoaded(-x, -z);
                    setNeighborLoaded(x, z);
                }
            }
        }
        // CraftBukkit end
        world.timings.syncChunkLoadPostTimer.stopTiming(); // Paper
        world.timings.syncChunkLoadPopulateNeighbors.startTiming(); // Paper
        Chunk chunk = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x, this.z - 1); // Paper
        Chunk chunk1 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x + 1, this.z); // Paper
        Chunk chunk2 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x, this.z + 1); // Paper
        Chunk chunk3 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x - 1, this.z); // Paper

        if (chunk1 != null && chunk2 != null && MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x + 1, this.z + 1) != null) { // Paper
            this.populate(chunkgenerator);
        }

        if (chunk3 != null && chunk2 != null && MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x - 1, this.z + 1) != null) { // Paper
            chunk3.populate(chunkgenerator);
        }

        if (chunk != null && chunk1 != null && MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x + 1, this.z - 1) != null) { // Paper
            chunk.populate(chunkgenerator);
        }

        if (chunk != null && chunk3 != null) {
            Chunk chunk4 = MCUtil.getLoadedChunkWithoutMarkingActive(ichunkprovider,this.x - 1, this.z - 1); // Paper

            if (chunk4 != null) {
                chunk4.populate(chunkgenerator);
            }
        }
        world.timings.syncChunkLoadPopulateNeighbors.stopTiming(); // Paper

    }

    protected void populate(IChunkGenerator chunkgenerator) {
        if (this.isTerrainPopulated()) {
            if (chunkgenerator.generateStructures(this, this.x, this.z)) {
                this.markDirty();
            }
        } else {
            this.checkLight();
            chunkgenerator.populate(this.x, this.z);

            // CraftBukkit start
            BlockSand.fallInstantly = true;
            Random random = new Random();
            random.setSeed(world.getSeed());
            long xRand = random.nextLong() / 2L * 2L + 1L;
            long zRand = random.nextLong() / 2L * 2L + 1L;
            random.setSeed(x * xRand + z * zRand ^ world.getSeed());

            org.bukkit.World world = this.world.getWorld();
            if (world != null) {
                this.world.populating = true;
                try {
                    for (org.bukkit.generator.BlockPopulator populator : world.getPopulators()) {
                        populator.populate(world, random, bukkitChunk);
                    }
                } finally {
                    this.world.populating = false;
                }
            }
            BlockSand.fallInstantly = false;
            this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.world.ChunkPopulateEvent(bukkitChunk));
            // CraftBukkit end
            this.markDirty();
        }

    }

    public BlockPos getPrecipitationHeight(BlockPos blockposition) {
        int i = blockposition.getX() & 15;
        int j = blockposition.getZ() & 15;
        int k = i | j << 4;
        BlockPos blockposition1 = new BlockPos(blockposition.getX(), this.precipitationHeightMap[k], blockposition.getZ());

        if (blockposition1.getY() == -999) {
            int l = this.getTopFilledSegment() + 15;

            blockposition1 = new BlockPos(blockposition.getX(), l, blockposition.getZ());
            int i1 = -1;

            while (blockposition1.getY() > 0 && i1 == -1) {
                IBlockState iblockdata = this.getBlockState(blockposition1);
                Material material = iblockdata.getMaterial();

                if (!material.blocksMovement() && !material.isLiquid()) {
                    blockposition1 = blockposition1.down();
                } else {
                    i1 = blockposition1.getY() + 1;
                }
            }

            this.precipitationHeightMap[k] = i1;
        }

        return new BlockPos(blockposition.getX(), this.precipitationHeightMap[k], blockposition.getZ());
    }

    public void onTick(boolean flag) {
        if (this.isGapLightingUpdated && this.world.provider.hasSkyLight() && !flag) {
            this.recheckGaps(this.world.isRemote);
        }

        this.ticked = true;
        if (!this.isLightPopulated && this.isTerrainPopulated && this.world.spigotConfig.randomLightUpdates) { // Spigot - also use random light updates setting to determine if we should relight
            this.checkLight();
        }

        while (!this.tileEntityPosQueue.isEmpty()) {
            BlockPos blockposition = this.tileEntityPosQueue.poll();

            if (this.getTileEntity(blockposition, Chunk.EnumCreateEntityType.CHECK) == null && this.getBlockState(blockposition).getBlock().hasTileEntity()) {
                TileEntity tileentity = this.createNewTileEntity(blockposition);

                this.world.setTileEntity(blockposition, tileentity);
                this.world.markBlockRangeForRenderUpdate(blockposition, blockposition);
            }
        }

    }

    public boolean isPopulated() {
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
        return !this.world.spigotConfig.randomLightUpdates || (this.isTicked() && this.isTerrainPopulated && this.isLightPopulated);
        // Paper End
        // Spigot End
    }

    public boolean wasTicked() {
        return this.ticked;
    }

    public ChunkPos getPos() {
        return new ChunkPos(this.x, this.z);
    }

    public boolean isEmptyBetween(int i, int j) {
        if (i < 0) {
            i = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        for (int k = i; k <= j; k += 16) {
            ExtendedBlockStorage chunksection = this.storageArrays[k >> 4];

            if (chunksection != Chunk.NULL_BLOCK_STORAGE && !chunksection.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void setStorageArrays(ExtendedBlockStorage[] achunksection) {
        if (this.storageArrays.length != achunksection.length) {
            Chunk.LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", Integer.valueOf(achunksection.length), Integer.valueOf(this.storageArrays.length));
        } else {
            System.arraycopy(achunksection, 0, this.storageArrays, 0, this.storageArrays.length);
        }
    }

    public Biome getBiome(BlockPos blockposition, BiomeProvider worldchunkmanager) {
        int i = blockposition.getX() & 15;
        int j = blockposition.getZ() & 15;
        int k = this.blockBiomeArray[j << 4 | i] & 255;
        Biome biomebase;

        if (k == 255) {
            biomebase = worldchunkmanager.getBiome(blockposition, Biomes.PLAINS);
            k = Biome.getIdForBiome(biomebase);
            this.blockBiomeArray[j << 4 | i] = (byte) (k & 255);
        }

        biomebase = Biome.getBiome(k);
        return biomebase == null ? Biomes.PLAINS : biomebase;
    }

    public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }

    public void setBiomeArray(byte[] abyte) {
        if (this.blockBiomeArray.length != abyte.length) {
            Chunk.LOGGER.warn("Could not set level chunk biomes, array length is {} instead of {}", Integer.valueOf(abyte.length), Integer.valueOf(this.blockBiomeArray.length));
        } else {
            System.arraycopy(abyte, 0, this.blockBiomeArray, 0, this.blockBiomeArray.length);
        }
    }

    public void resetRelightChecks() {
        this.queuedLightChecks = 0;
    }

    public void enqueueRelightChecks() {
        if (this.queuedLightChecks < 4096) {
            BlockPos blockposition = new BlockPos(this.x << 4, 0, this.z << 4);

            for (int i = 0; i < 8; ++i) {
                if (this.queuedLightChecks >= 4096) {
                    return;
                }

                int j = this.queuedLightChecks % 16;
                int k = this.queuedLightChecks / 16 % 16;
                int l = this.queuedLightChecks / 256;

                ++this.queuedLightChecks;

                for (int i1 = 0; i1 < 16; ++i1) {
                    BlockPos blockposition1 = blockposition.add(k, (j << 4) + i1, l);
                    boolean flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;

                    if (this.storageArrays[j] == Chunk.NULL_BLOCK_STORAGE && flag || this.storageArrays[j] != Chunk.NULL_BLOCK_STORAGE && this.storageArrays[j].get(k, i1, l).getMaterial() == Material.AIR) {
                        EnumFacing[] aenumdirection = EnumFacing.values();
                        int j1 = aenumdirection.length;

                        for (int k1 = 0; k1 < j1; ++k1) {
                            EnumFacing enumdirection = aenumdirection[k1];
                            BlockPos blockposition2 = blockposition1.offset(enumdirection);

                            if (this.world.getBlockState(blockposition2).getLightValue() > 0) {
                                this.world.checkLight(blockposition2);
                            }
                        }

                        this.world.checkLight(blockposition1);
                    }
                }
            }

        }
    }

    public void checkLight() {
        world.timings.lightChunk.startTiming(); // Paper
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        BlockPos blockposition = new BlockPos(this.x << 4, 0, this.z << 4);

        if (this.world.provider.hasSkyLight()) {
            if (this.world.isAreaLoaded(blockposition.add(-1, 0, -1), blockposition.add(16, this.world.getSeaLevel(), 16))) {
                label42:
                for (int i = 0; i < 16; ++i) {
                    for (int j = 0; j < 16; ++j) {
                        if (!this.checkLight(i, j)) {
                            this.isLightPopulated = false;
                            break label42;
                        }
                    }
                }

                if (this.isLightPopulated) {
                    Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                    while (iterator.hasNext()) {
                        EnumFacing enumdirection = (EnumFacing) iterator.next();
                        int k = enumdirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;

                        this.world.getChunkFromBlockCoords(blockposition.offset(enumdirection, k)).checkLightSide(enumdirection.getOpposite());
                    }

                    this.setSkylightUpdated();
                }
            } else {
                this.isLightPopulated = false;
            }
        }

        world.timings.lightChunk.stopTiming(); // Paper
    }

    private void setSkylightUpdated() {
        for (int i = 0; i < this.updateSkylightColumns.length; ++i) {
            this.updateSkylightColumns[i] = true;
        }

        this.recheckGaps(false);
    }

    private void checkLightSide(EnumFacing enumdirection) {
        if (this.isTerrainPopulated) {
            int i;

            if (enumdirection == EnumFacing.EAST) {
                for (i = 0; i < 16; ++i) {
                    this.checkLight(15, i);
                }
            } else if (enumdirection == EnumFacing.WEST) {
                for (i = 0; i < 16; ++i) {
                    this.checkLight(0, i);
                }
            } else if (enumdirection == EnumFacing.SOUTH) {
                for (i = 0; i < 16; ++i) {
                    this.checkLight(i, 15);
                }
            } else if (enumdirection == EnumFacing.NORTH) {
                for (i = 0; i < 16; ++i) {
                    this.checkLight(i, 0);
                }
            }

        }
    }

    private boolean checkLight(int i, int j) {
        int k = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos((this.x << 4) + i, 0, (this.z << 4) + j);

        int l;

        for (l = k + 16 - 1; l > this.world.getSeaLevel() || l > 0 && !flag1; --l) {
            blockposition_mutableblockposition.setPos(blockposition_mutableblockposition.getX(), l, blockposition_mutableblockposition.getZ());
            int i1 = this.getBlockLightOpacity(blockposition_mutableblockposition);

            if (i1 == 255 && blockposition_mutableblockposition.getY() < this.world.getSeaLevel()) {
                flag1 = true;
            }

            if (!flag && i1 > 0) {
                flag = true;
            } else if (flag && i1 == 0 && !this.world.checkLight(blockposition_mutableblockposition)) {
                return false;
            }
        }

        for (l = blockposition_mutableblockposition.getY(); l > 0; --l) {
            blockposition_mutableblockposition.setPos(blockposition_mutableblockposition.getX(), l, blockposition_mutableblockposition.getZ());
            if (this.getBlockState(blockposition_mutableblockposition).getLightValue() > 0) {
                this.world.checkLight(blockposition_mutableblockposition);
            }
        }

        return true;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public World getWorld() {
        return this.world;
    }

    public int[] getHeightMap() {
        return this.heightMap;
    }

    public void setHeightMap(int[] aint) {
        if (this.heightMap.length != aint.length) {
            Chunk.LOGGER.warn("Could not set level chunk heightmap, array length is {} instead of {}", Integer.valueOf(aint.length), Integer.valueOf(this.heightMap.length));
        } else {
            System.arraycopy(aint, 0, this.heightMap, 0, this.heightMap.length);
        }
    }

    public Map<BlockPos, TileEntity> getTileEntityMap() {
        return this.tileEntities;
    }

    public List<Entity>[] getEntitySlices() {
        return this.entityLists;
    }

    public boolean isTerrainPopulated() {
        return this.isTerrainPopulated;
    }

    public void setTerrainPopulated(boolean flag) {
        this.isTerrainPopulated = flag;
    }

    public boolean isLightPopulated() {
        return this.isLightPopulated;
    }

    public void setLightPopulated(boolean flag) {
        this.isLightPopulated = flag;
    }

    public void setModified(boolean flag) {
        this.dirty = flag;
    }

    public void setHasEntities(boolean flag) {
        this.hasEntities = flag;
    }

    public void setLastSaveTime(long i) {
        this.lastSaveTime = i;
    }

    public int getLowestHeight() {
        return this.heightMapMinimum;
    }

    public long getInhabitedTime() {
        return world.paperConfig.useInhabitedTime ? this.inhabitedTime : 0; // Paper
    }

    public void setInhabitedTime(long i) {
        this.inhabitedTime = i;
    }

    // Paper start
    public void runOrQueueLightUpdate(Runnable runnable) {
        if (this.world.paperConfig.queueLightUpdates) {
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

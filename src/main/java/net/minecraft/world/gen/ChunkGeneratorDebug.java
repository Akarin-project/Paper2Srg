package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class ChunkGeneratorDebug implements IChunkGenerator {

    private static final List<IBlockState> ALL_VALID_STATES = Lists.newArrayList();
    private static final int GRID_WIDTH;
    private static final int GRID_HEIGHT;
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState BARRIER = Blocks.BARRIER.getDefaultState();
    private final World world;

    public ChunkGeneratorDebug(World world) {
        this.world = world;
    }

    public Chunk generateChunk(int i, int j) {
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        int k;

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                int j1 = i * 16 + l;

                k = j * 16 + i1;
                chunksnapshot.setBlockState(l, 60, i1, ChunkGeneratorDebug.BARRIER);
                IBlockState iblockdata = getBlockStateFor(j1, k);

                if (iblockdata != null) {
                    chunksnapshot.setBlockState(l, 70, i1, iblockdata);
                }
            }
        }

        Chunk chunk = new Chunk(this.world, chunksnapshot, i, j);

        chunk.generateSkylightMap();
        Biome[] abiomebase = this.world.getBiomeProvider().getBiomes((Biome[]) null, i * 16, j * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.getIdForBiome(abiomebase[k]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public static IBlockState getBlockStateFor(int i, int j) {
        IBlockState iblockdata = ChunkGeneratorDebug.AIR;

        if (i > 0 && j > 0 && i % 2 != 0 && j % 2 != 0) {
            i /= 2;
            j /= 2;
            if (i <= ChunkGeneratorDebug.GRID_WIDTH && j <= ChunkGeneratorDebug.GRID_HEIGHT) {
                int k = MathHelper.abs(i * ChunkGeneratorDebug.GRID_WIDTH + j);

                if (k < ChunkGeneratorDebug.ALL_VALID_STATES.size()) {
                    iblockdata = (IBlockState) ChunkGeneratorDebug.ALL_VALID_STATES.get(k);
                }
            }
        }

        return iblockdata;
    }

    public void populate(int i, int j) {}

    public boolean generateStructures(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        Biome biomebase = this.world.getBiome(blockposition);

        return biomebase.getSpawnableList(enumcreaturetype);
    }

    @Nullable
    public BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag) {
        return null;
    }

    public boolean isInsideStructure(World world, String s, BlockPos blockposition) {
        return false;
    }

    public void recreateStructures(Chunk chunk, int i, int j) {}

    static {
        Iterator iterator = Block.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Block block = (Block) iterator.next();

            ChunkGeneratorDebug.ALL_VALID_STATES.addAll(block.getBlockState().getValidStates());
        }

        GRID_WIDTH = MathHelper.ceil(MathHelper.sqrt((float) ChunkGeneratorDebug.ALL_VALID_STATES.size()));
        GRID_HEIGHT = MathHelper.ceil((float) ChunkGeneratorDebug.ALL_VALID_STATES.size() / (float) ChunkGeneratorDebug.GRID_WIDTH);
    }
}

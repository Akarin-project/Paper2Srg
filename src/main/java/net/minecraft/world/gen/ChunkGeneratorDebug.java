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

    private static final List<IBlockState> field_177464_a = Lists.newArrayList();
    private static final int field_177462_b;
    private static final int field_181039_c;
    protected static final IBlockState field_185934_a = Blocks.field_150350_a.func_176223_P();
    protected static final IBlockState field_185935_b = Blocks.field_180401_cv.func_176223_P();
    private final World field_177463_c;

    public ChunkGeneratorDebug(World world) {
        this.field_177463_c = world;
    }

    public Chunk func_185932_a(int i, int j) {
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        int k;

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                int j1 = i * 16 + l;

                k = j * 16 + i1;
                chunksnapshot.func_177855_a(l, 60, i1, ChunkGeneratorDebug.field_185935_b);
                IBlockState iblockdata = func_177461_b(j1, k);

                if (iblockdata != null) {
                    chunksnapshot.func_177855_a(l, 70, i1, iblockdata);
                }
            }
        }

        Chunk chunk = new Chunk(this.field_177463_c, chunksnapshot, i, j);

        chunk.func_76603_b();
        Biome[] abiomebase = this.field_177463_c.func_72959_q().func_76933_b((Biome[]) null, i * 16, j * 16, 16, 16);
        byte[] abyte = chunk.func_76605_m();

        for (k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.func_185362_a(abiomebase[k]);
        }

        chunk.func_76603_b();
        return chunk;
    }

    public static IBlockState func_177461_b(int i, int j) {
        IBlockState iblockdata = ChunkGeneratorDebug.field_185934_a;

        if (i > 0 && j > 0 && i % 2 != 0 && j % 2 != 0) {
            i /= 2;
            j /= 2;
            if (i <= ChunkGeneratorDebug.field_177462_b && j <= ChunkGeneratorDebug.field_181039_c) {
                int k = MathHelper.func_76130_a(i * ChunkGeneratorDebug.field_177462_b + j);

                if (k < ChunkGeneratorDebug.field_177464_a.size()) {
                    iblockdata = (IBlockState) ChunkGeneratorDebug.field_177464_a.get(k);
                }
            }
        }

        return iblockdata;
    }

    public void func_185931_b(int i, int j) {}

    public boolean func_185933_a(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        Biome biomebase = this.field_177463_c.func_180494_b(blockposition);

        return biomebase.func_76747_a(enumcreaturetype);
    }

    @Nullable
    public BlockPos func_180513_a(World world, String s, BlockPos blockposition, boolean flag) {
        return null;
    }

    public boolean func_193414_a(World world, String s, BlockPos blockposition) {
        return false;
    }

    public void func_180514_a(Chunk chunk, int i, int j) {}

    static {
        Iterator iterator = Block.field_149771_c.iterator();

        while (iterator.hasNext()) {
            Block block = (Block) iterator.next();

            ChunkGeneratorDebug.field_177464_a.addAll(block.func_176194_O().func_177619_a());
        }

        field_177462_b = MathHelper.func_76123_f(MathHelper.func_76129_c((float) ChunkGeneratorDebug.field_177464_a.size()));
        field_181039_c = MathHelper.func_76123_f((float) ChunkGeneratorDebug.field_177464_a.size() / (float) ChunkGeneratorDebug.field_177462_b);
    }
}

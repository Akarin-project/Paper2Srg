package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess {

    protected int field_72818_a;
    protected int field_72816_b;
    protected Chunk[][] field_72817_c;
    protected boolean field_72814_d;
    protected World field_72815_e;

    public ChunkCache(World world, BlockPos blockposition, BlockPos blockposition1, int i) {
        this.field_72815_e = world;
        this.field_72818_a = blockposition.func_177958_n() - i >> 4;
        this.field_72816_b = blockposition.func_177952_p() - i >> 4;
        int j = blockposition1.func_177958_n() + i >> 4;
        int k = blockposition1.func_177952_p() + i >> 4;

        this.field_72817_c = new Chunk[j - this.field_72818_a + 1][k - this.field_72816_b + 1];
        this.field_72814_d = true;

        int l;
        int i1;

        for (l = this.field_72818_a; l <= j; ++l) {
            for (i1 = this.field_72816_b; i1 <= k; ++i1) {
                this.field_72817_c[l - this.field_72818_a][i1 - this.field_72816_b] = world.getChunkIfLoaded(l, i1); // Paper
            }
        }

        for (l = blockposition.func_177958_n() >> 4; l <= blockposition1.func_177958_n() >> 4; ++l) {
            for (i1 = blockposition.func_177952_p() >> 4; i1 <= blockposition1.func_177952_p() >> 4; ++i1) {
                Chunk chunk = this.field_72817_c[l - this.field_72818_a][i1 - this.field_72816_b];

                if (chunk != null && !chunk.func_76606_c(blockposition.func_177956_o(), blockposition1.func_177956_o())) {
                    this.field_72814_d = false;
                }
            }
        }

    }

    @Nullable
    public TileEntity func_175625_s(BlockPos blockposition) {
        return this.func_190300_a(blockposition, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @Nullable
    public TileEntity func_190300_a(BlockPos blockposition, Chunk.EnumCreateEntityType chunk_enumtileentitystate) {
        int i = (blockposition.func_177958_n() >> 4) - this.field_72818_a;
        int j = (blockposition.func_177952_p() >> 4) - this.field_72816_b;

        return this.field_72817_c[i][j].func_177424_a(blockposition, chunk_enumtileentitystate);
    }

    public IBlockState func_180495_p(BlockPos blockposition) {
        if (blockposition.func_177956_o() >= 0 && blockposition.func_177956_o() < 256) {
            int i = (blockposition.func_177958_n() >> 4) - this.field_72818_a;
            int j = (blockposition.func_177952_p() >> 4) - this.field_72816_b;

            if (i >= 0 && i < this.field_72817_c.length && j >= 0 && j < this.field_72817_c[i].length) {
                Chunk chunk = this.field_72817_c[i][j];

                if (chunk != null) {
                    return chunk.func_177435_g(blockposition);
                }
            }
        }

        return Blocks.field_150350_a.func_176223_P();
    }

    public boolean func_175623_d(BlockPos blockposition) {
        return this.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a;
    }

    public int func_175627_a(BlockPos blockposition, EnumFacing enumdirection) {
        return this.func_180495_p(blockposition).func_185893_b(this, blockposition, enumdirection);
    }
}

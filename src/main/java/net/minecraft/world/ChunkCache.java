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

    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;
    protected boolean empty;
    protected World world;

    public ChunkCache(World world, BlockPos blockposition, BlockPos blockposition1, int i) {
        this.world = world;
        this.chunkX = blockposition.getX() - i >> 4;
        this.chunkZ = blockposition.getZ() - i >> 4;
        int j = blockposition1.getX() + i >> 4;
        int k = blockposition1.getZ() + i >> 4;

        this.chunkArray = new Chunk[j - this.chunkX + 1][k - this.chunkZ + 1];
        this.empty = true;

        int l;
        int i1;

        for (l = this.chunkX; l <= j; ++l) {
            for (i1 = this.chunkZ; i1 <= k; ++i1) {
                this.chunkArray[l - this.chunkX][i1 - this.chunkZ] = world.getChunkIfLoaded(l, i1); // Paper
            }
        }

        for (l = blockposition.getX() >> 4; l <= blockposition1.getX() >> 4; ++l) {
            for (i1 = blockposition.getZ() >> 4; i1 <= blockposition1.getZ() >> 4; ++i1) {
                Chunk chunk = this.chunkArray[l - this.chunkX][i1 - this.chunkZ];

                if (chunk != null && !chunk.isEmptyBetween(blockposition.getY(), blockposition1.getY())) {
                    this.empty = false;
                }
            }
        }

    }

    @Nullable
    public TileEntity getTileEntity(BlockPos blockposition) {
        return this.getTileEntity(blockposition, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos blockposition, Chunk.EnumCreateEntityType chunk_enumtileentitystate) {
        int i = (blockposition.getX() >> 4) - this.chunkX;
        int j = (blockposition.getZ() >> 4) - this.chunkZ;

        return this.chunkArray[i][j].getTileEntity(blockposition, chunk_enumtileentitystate);
    }

    public IBlockState getBlockState(BlockPos blockposition) {
        if (blockposition.getY() >= 0 && blockposition.getY() < 256) {
            int i = (blockposition.getX() >> 4) - this.chunkX;
            int j = (blockposition.getZ() >> 4) - this.chunkZ;

            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                Chunk chunk = this.chunkArray[i][j];

                if (chunk != null) {
                    return chunk.getBlockState(blockposition);
                }
            }
        }

        return Blocks.AIR.getDefaultState();
    }

    public boolean isAirBlock(BlockPos blockposition) {
        return this.getBlockState(blockposition).getMaterial() == Material.AIR;
    }

    public int getStrongPower(BlockPos blockposition, EnumFacing enumdirection) {
        return this.getBlockState(blockposition).getStrongPower(this, blockposition, enumdirection);
    }
}

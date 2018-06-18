package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeSavannaMutated extends BiomeSavanna {

    public BiomeSavannaMutated(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 5;
    }

    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        if (d0 > 1.75D) {
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
        } else if (d0 > -0.5D) {
            this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
        }

        this.generateBiomeTerrain(world, random, chunksnapshot, i, j, d0);
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        this.decorator.decorate(world, random, this, blockposition);
    }
}

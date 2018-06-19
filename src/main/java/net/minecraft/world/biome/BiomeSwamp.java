package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeSwamp extends Biome {

    protected static final IBlockState WATER_LILY = Blocks.WATERLILY.getDefaultState();

    protected BiomeSwamp(Biome.a biomebase_a) {
        super(biomebase_a);
        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 1;
        this.decorator.deadBushPerChunk = 1;
        this.decorator.mushroomsPerChunk = 8;
        this.decorator.reedsPerChunk = 10;
        this.decorator.clayPerChunk = 1;
        this.decorator.waterlilyPerChunk = 4;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
        this.decorator.grassPerChunk = 5;
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySlime.class, 1, 1, 1));
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return BiomeSwamp.SWAMP_FEATURE;
    }

    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockposition) {
        return BlockFlower.EnumFlowerType.BLUE_ORCHID;
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        double d1 = BiomeSwamp.GRASS_COLOR_NOISE.getValue(i * 0.25D, j * 0.25D);

        if (d1 > 0.0D) {
            int k = i & 15;
            int l = j & 15;

            for (int i1 = 255; i1 >= 0; --i1) {
                if (chunksnapshot.getBlockState(l, i1, k).getMaterial() != Material.AIR) {
                    if (i1 == 62 && chunksnapshot.getBlockState(l, i1, k).getBlock() != Blocks.WATER) {
                        chunksnapshot.setBlockState(l, i1, k, BiomeSwamp.WATER);
                        if (d1 < 0.12D) {
                            chunksnapshot.setBlockState(l, i1 + 1, k, BiomeSwamp.WATER_LILY);
                        }
                    }
                    break;
                }
            }
        }

        this.generateBiomeTerrain(world, random, chunksnapshot, i, j, d0);
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockposition) {
        super.decorate(world, random, blockposition);
        if (random.nextInt(64) == 0) {
            (new WorldGenFossils()).generate(world, random, blockposition);
        }

    }
}

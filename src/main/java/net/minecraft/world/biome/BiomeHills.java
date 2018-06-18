package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeHills extends Biome {

    private final WorldGenerator silverfishSpawner;
    private final WorldGenTaiga2 spruceGenerator;
    private final BiomeHills.Type type;

    protected BiomeHills(BiomeHills.Type biomebighills_type, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.silverfishSpawner = new WorldGenMinable(Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 9);
        this.spruceGenerator = new WorldGenTaiga2(false);
        if (biomebighills_type == BiomeHills.Type.EXTRA_TREES) {
            this.decorator.treesPerChunk = 3;
        }

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityLlama.class, 5, 4, 6));
        this.type = biomebighills_type;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(3) > 0 ? this.spruceGenerator : super.getRandomTreeFeature(random));
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        super.decorate(world, random, blockposition);
        int i = 3 + random.nextInt(6);

        int j;
        int k;
        int l;

        // Paper start - Disable extreme hills emeralds
        if (!world.paperConfig.disableExtremeHillsEmeralds) {

        for (j = 0; j < i; ++j) {
            k = random.nextInt(16);
            l = random.nextInt(28) + 4;
            int i1 = random.nextInt(16);
            BlockPos blockposition1 = blockposition.add(k, l, i1);

            if (world.getBlockState(blockposition1).getBlock() == Blocks.STONE) {
                world.setBlockState(blockposition1, Blocks.EMERALD_ORE.getDefaultState(), 2);
            }
        }

        }
        // Paper end block

        // Paper start - Disable extreme hills monster eggs
        if (!world.paperConfig.disableExtremeHillsMonsterEggs) {

        for (i = 0; i < 7; ++i) {
            j = random.nextInt(16);
            k = random.nextInt(64);
            l = random.nextInt(16);
            this.silverfishSpawner.generate(world, random, blockposition.add(j, k, l));
        }

        }
        // Paper end block

    }

    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        if ((d0 < -1.0D || d0 > 2.0D) && this.type == BiomeHills.Type.MUTATED) {
            this.topBlock = Blocks.GRAVEL.getDefaultState();
            this.fillerBlock = Blocks.GRAVEL.getDefaultState();
        } else if (d0 > 1.0D && this.type != BiomeHills.Type.EXTRA_TREES) {
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
        }

        this.generateBiomeTerrain(world, random, chunksnapshot, i, j, d0);
    }

    public static enum Type {

        NORMAL, EXTRA_TREES, MUTATED;

        private Type() {}
    }
}

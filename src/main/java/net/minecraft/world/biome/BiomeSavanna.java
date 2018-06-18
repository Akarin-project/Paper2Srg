package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeSavanna extends Biome {

    private static final WorldGenSavannaTree SAVANNA_TREE = new WorldGenSavannaTree(false);

    protected BiomeSavanna(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityHorse.class, 1, 2, 6));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 1));
        if (this.getBaseHeight() > 1.1F) {
            this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityLlama.class, 8, 4, 4));
        }

        this.decorator.treesPerChunk = 1;
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 20;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(5) > 0 ? BiomeSavanna.SAVANNA_TREE : BiomeSavanna.TREE_FEATURE);
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        BiomeSavanna.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

        for (int i = 0; i < 7; ++i) {
            int j = random.nextInt(16) + 8;
            int k = random.nextInt(16) + 8;
            int l = random.nextInt(world.getHeight(blockposition.add(j, 0, k)).getY() + 32);

            BiomeSavanna.DOUBLE_PLANT_GENERATOR.generate(world, random, blockposition.add(j, l, k));
        }

        super.decorate(world, random, blockposition);
    }

    public Class<? extends Biome> getBiomeClass() {
        return BiomeSavanna.class;
    }
}

package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeForestMutated extends BiomeForest {

    public BiomeForestMutated(BiomeBase.a biomebase_a) {
        super(BiomeForest.Type.BIRCH, biomebase_a);
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return random.nextBoolean() ? BiomeForest.SUPER_BIRCH_TREE : BiomeForest.BIRCH_TREE;
    }
}

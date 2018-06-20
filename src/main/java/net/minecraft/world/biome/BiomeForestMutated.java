package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeForestMutated extends BiomeForest {

    public BiomeForestMutated(Biome.a biomebase_a) {
        super(BiomeForest.Type.BIRCH, biomebase_a);
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return random.nextBoolean() ? BiomeForest.field_150629_aC : BiomeForest.field_150630_aD;
    }
}

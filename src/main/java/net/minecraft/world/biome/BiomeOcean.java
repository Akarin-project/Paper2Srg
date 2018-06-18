package net.minecraft.world.biome;

public class BiomeOcean extends Biome {

    public BiomeOcean(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.spawnableCreatureList.clear();
    }

    public Biome.TempCategory getTempCategory() {
        return Biome.TempCategory.OCEAN;
    }
}

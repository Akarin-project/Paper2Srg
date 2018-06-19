package net.minecraft.world.biome;

public class BiomeOcean extends Biome {

    public BiomeOcean(Biome.a biomebase_a) {
        super(biomebase_a);
        this.spawnableCreatureList.clear();
    }

    @Override
    public Biome.TempCategory getTempCategory() {
        return Biome.TempCategory.OCEAN;
    }
}

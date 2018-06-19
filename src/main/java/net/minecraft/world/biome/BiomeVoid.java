package net.minecraft.world.biome;

public class BiomeVoid extends Biome {

    public BiomeVoid(Biome.a biomebase_a) {
        super(biomebase_a);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.decorator = new BiomeVoidDecorator();
    }

    @Override
    public boolean ignorePlayerSpawnSuitability() {
        return true;
    }
}

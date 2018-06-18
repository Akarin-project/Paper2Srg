package net.minecraft.world.biome;

public class BiomeVoid extends Biome {

    public BiomeVoid(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.decorator = new BiomeVoidDecorator();
    }

    public boolean ignorePlayerSpawnSuitability() {
        return true;
    }
}

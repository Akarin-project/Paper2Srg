package net.minecraft.world.biome;

public class BiomeOcean extends Biome {

    public BiomeOcean(Biome.a biomebase_a) {
        super(biomebase_a);
        this.field_76762_K.clear();
    }

    public Biome.TempCategory func_150561_m() {
        return Biome.TempCategory.OCEAN;
    }
}

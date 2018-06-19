package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider {

    public WorldProviderSurface() {}

    public DimensionType func_186058_p() {
        return DimensionType.OVERWORLD;
    }

    public boolean func_186056_c(int i, int j) {
        return !this.field_76579_a.func_72916_c(i, j);
    }
}

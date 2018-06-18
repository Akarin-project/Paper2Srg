package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider {

    public WorldProviderSurface() {}

    public DimensionType getDimensionType() {
        return DimensionType.OVERWORLD;
    }

    public boolean canDropChunk(int i, int j) {
        return !this.world.isSpawnChunk(i, j);
    }
}

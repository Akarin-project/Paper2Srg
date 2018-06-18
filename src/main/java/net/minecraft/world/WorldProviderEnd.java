package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderEnd extends WorldProvider {

    private DragonFightManager dragonFightManager;

    public WorldProviderEnd() {}

    public void init() {
        this.biomeProvider = new BiomeProviderSingle(Biomes.SKY);
        NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(DimensionType.THE_END);

        this.dragonFightManager = this.world instanceof WorldServer ? new DragonFightManager((WorldServer) this.world, nbttagcompound.getCompoundTag("DragonFight")) : null;
    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorEnd(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed(), this.getSpawnCoordinate());
    }

    public float calculateCelestialAngle(long i, float f) {
        return 0.0F;
    }

    public boolean canRespawnHere() {
        return false;
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    public boolean canCoordinateBeSpawn(int i, int j) {
        return this.world.getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial().blocksMovement();
    }

    public BlockPos getSpawnCoordinate() {
        return new BlockPos(100, 50, 0);
    }

    public int getAverageGroundLevel() {
        return 50;
    }

    public DimensionType getDimensionType() {
        return DimensionType.THE_END;
    }

    public void onWorldSave() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (this.dragonFightManager != null) {
            nbttagcompound.setTag("DragonFight", this.dragonFightManager.getCompound());
        }

        this.world.getWorldInfo().setDimensionData(DimensionType.THE_END, nbttagcompound);
    }

    public void onWorldUpdateEntities() {
        if (this.dragonFightManager != null) {
            this.dragonFightManager.tick();
        }

    }

    @Nullable
    public DragonFightManager getDragonFightManager() {
        return this.dragonFightManager;
    }
}

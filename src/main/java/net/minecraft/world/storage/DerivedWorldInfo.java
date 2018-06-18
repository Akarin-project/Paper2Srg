package net.minecraft.world.storage;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class DerivedWorldInfo extends WorldInfo {

    private final WorldInfo delegate;

    public DerivedWorldInfo(WorldInfo worlddata) {
        this.delegate = worlddata;
    }

    public NBTTagCompound cloneNBTCompound(@Nullable NBTTagCompound nbttagcompound) {
        return this.delegate.cloneNBTCompound(nbttagcompound);
    }

    public long getSeed() {
        return this.delegate.getSeed();
    }

    public int getSpawnX() {
        return this.delegate.getSpawnX();
    }

    public int getSpawnY() {
        return this.delegate.getSpawnY();
    }

    public int getSpawnZ() {
        return this.delegate.getSpawnZ();
    }

    public long getWorldTotalTime() {
        return this.delegate.getWorldTotalTime();
    }

    public long getWorldTime() {
        return this.delegate.getWorldTime();
    }

    public NBTTagCompound getPlayerNBTTagCompound() {
        return this.delegate.getPlayerNBTTagCompound();
    }

    public String getWorldName() {
        return this.delegate.getWorldName();
    }

    public int getSaveVersion() {
        return this.delegate.getSaveVersion();
    }

    public boolean isThundering() {
        return this.delegate.isThundering();
    }

    public int getThunderTime() {
        return this.delegate.getThunderTime();
    }

    public boolean isRaining() {
        return this.delegate.isRaining();
    }

    public int getRainTime() {
        return this.delegate.getRainTime();
    }

    public GameType getGameType() {
        return this.delegate.getGameType();
    }

    public void setWorldTotalTime(long i) {}

    public void setWorldTime(long i) {}

    public void setSpawn(BlockPos blockposition) {}

    public void setWorldName(String s) {}

    public void setSaveVersion(int i) {}

    public void setThundering(boolean flag) {}

    public void setThunderTime(int i) {}

    public void setRaining(boolean flag) {}

    public void setRainTime(int i) {}

    public boolean isMapFeaturesEnabled() {
        return this.delegate.isMapFeaturesEnabled();
    }

    public boolean isHardcoreModeEnabled() {
        return this.delegate.isHardcoreModeEnabled();
    }

    public WorldType getTerrainType() {
        return this.delegate.getTerrainType();
    }

    public void setTerrainType(WorldType worldtype) {}

    public boolean areCommandsAllowed() {
        return this.delegate.areCommandsAllowed();
    }

    public void setAllowCommands(boolean flag) {}

    public boolean isInitialized() {
        return this.delegate.isInitialized();
    }

    public void setServerInitialized(boolean flag) {}

    public GameRules getGameRulesInstance() {
        return this.delegate.getGameRulesInstance();
    }

    public EnumDifficulty getDifficulty() {
        return this.delegate.getDifficulty();
    }

    public void setDifficulty(EnumDifficulty enumdifficulty) {}

    public boolean isDifficultyLocked() {
        return this.delegate.isDifficultyLocked();
    }

    public void setDifficultyLocked(boolean flag) {}

    public void setDimensionData(DimensionType dimensionmanager, NBTTagCompound nbttagcompound) {
        this.delegate.setDimensionData(dimensionmanager, nbttagcompound);
    }

    public NBTTagCompound getDimensionData(DimensionType dimensionmanager) {
        return this.delegate.getDimensionData(dimensionmanager);
    }
}

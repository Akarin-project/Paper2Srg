package net.minecraft.world.storage;
import net.minecraft.nbt.NBTTagCompound;


public abstract class WorldSavedData {

    public final String mapName;
    private boolean dirty;

    public WorldSavedData(String s) {
        this.mapName = s;
    }

    public abstract void readFromNBT(NBTTagCompound nbttagcompound);

    public abstract NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound);

    public void markDirty() {
        this.setDirty(true);
    }

    public void setDirty(boolean flag) {
        this.dirty = flag;
    }

    public boolean isDirty() {
        return this.dirty;
    }
}

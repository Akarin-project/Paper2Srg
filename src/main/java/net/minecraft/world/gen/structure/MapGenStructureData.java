package net.minecraft.world.gen.structure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;


public class MapGenStructureData extends WorldSavedData {

    private NBTTagCompound tagCompound = new NBTTagCompound();

    public MapGenStructureData(String s) {
        super(s);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.tagCompound = nbttagcompound.getCompoundTag("Features");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setTag("Features", this.tagCompound);
        return nbttagcompound;
    }

    public void writeInstance(NBTTagCompound nbttagcompound, int i, int j) {
        this.tagCompound.setTag(formatChunkCoords(i, j), nbttagcompound);
    }

    public static String formatChunkCoords(int i, int j) {
        return "[" + i + "," + j + "]";
    }

    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }
}

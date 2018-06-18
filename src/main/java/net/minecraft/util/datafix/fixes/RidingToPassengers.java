package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class RidingToPassengers implements IFixableData {

    public RidingToPassengers() {}

    public int getFixVersion() {
        return 135;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        while (nbttagcompound.hasKey("Riding", 10)) {
            NBTTagCompound nbttagcompound1 = this.extractVehicle(nbttagcompound);

            this.addPassengerToVehicle(nbttagcompound, nbttagcompound1);
            nbttagcompound = nbttagcompound1;
        }

        return nbttagcompound;
    }

    protected void addPassengerToVehicle(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        NBTTagList nbttaglist = new NBTTagList();

        nbttaglist.appendTag(nbttagcompound);
        nbttagcompound1.setTag("Passengers", nbttaglist);
    }

    protected NBTTagCompound extractVehicle(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Riding");

        nbttagcompound.removeTag("Riding");
        return nbttagcompound1;
    }
}

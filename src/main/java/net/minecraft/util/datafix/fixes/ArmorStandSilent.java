package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ArmorStandSilent implements IFixableData {

    public ArmorStandSilent() {}

    public int getFixVersion() {
        return 147;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("ArmorStand".equals(nbttagcompound.getString("id")) && nbttagcompound.getBoolean("Silent") && !nbttagcompound.getBoolean("Marker")) {
            nbttagcompound.removeTag("Silent");
        }

        return nbttagcompound;
    }
}

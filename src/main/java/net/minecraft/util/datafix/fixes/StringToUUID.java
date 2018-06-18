package net.minecraft.util.datafix.fixes;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class StringToUUID implements IFixableData {

    public StringToUUID() {}

    public int getFixVersion() {
        return 108;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("UUID", 8)) {
            nbttagcompound.setUniqueId("UUID", UUID.fromString(nbttagcompound.getString("UUID")));
        }

        return nbttagcompound;
    }
}

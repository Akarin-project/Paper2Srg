package net.minecraft.util.datafix.fixes;

import java.util.Locale;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class OptionsLowerCaseLanguage implements IFixableData {

    public OptionsLowerCaseLanguage() {}

    public int getFixVersion() {
        return 816;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("lang", 8)) {
            nbttagcompound.setString("lang", nbttagcompound.getString("lang").toLowerCase(Locale.ROOT));
        }

        return nbttagcompound;
    }
}

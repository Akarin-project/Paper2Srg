package net.minecraft.util.datafix.fixes;

import java.util.Locale;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class OptionsLowerCaseLanguage implements IFixableData {

    public OptionsLowerCaseLanguage() {}

    public int func_188216_a() {
        return 816;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("lang", 8)) {
            nbttagcompound.func_74778_a("lang", nbttagcompound.func_74779_i("lang").toLowerCase(Locale.ROOT));
        }

        return nbttagcompound;
    }
}

package net.minecraft.util.datafix.fixes;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class StringToUUID implements IFixableData {

    public StringToUUID() {}

    public int func_188216_a() {
        return 108;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("UUID", 8)) {
            nbttagcompound.func_186854_a("UUID", UUID.fromString(nbttagcompound.func_74779_i("UUID")));
        }

        return nbttagcompound;
    }
}

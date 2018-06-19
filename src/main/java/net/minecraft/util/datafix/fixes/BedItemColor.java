package net.minecraft.util.datafix.fixes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class BedItemColor implements IFixableData {

    public BedItemColor() {}

    public int func_188216_a() {
        return 1125;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("minecraft:bed".equals(nbttagcompound.func_74779_i("id")) && nbttagcompound.func_74765_d("Damage") == 0) {
            nbttagcompound.func_74777_a("Damage", (short) EnumDyeColor.RED.func_176765_a());
        }

        return nbttagcompound;
    }
}

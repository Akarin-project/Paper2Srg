package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class TotemItemRename implements IFixableData {

    public TotemItemRename() {}

    public int func_188216_a() {
        return 820;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("minecraft:totem".equals(nbttagcompound.func_74779_i("id"))) {
            nbttagcompound.func_74778_a("id", "minecraft:totem_of_undying");
        }

        return nbttagcompound;
    }
}

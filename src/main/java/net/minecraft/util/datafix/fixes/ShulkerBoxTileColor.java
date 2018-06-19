package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ShulkerBoxTileColor implements IFixableData {

    public ShulkerBoxTileColor() {}

    public int func_188216_a() {
        return 813;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("minecraft:shulker".equals(nbttagcompound.func_74779_i("id"))) {
            nbttagcompound.func_82580_o("Color");
        }

        return nbttagcompound;
    }
}

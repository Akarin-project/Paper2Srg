package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ShulkerBoxEntityColor implements IFixableData {

    public ShulkerBoxEntityColor() {}

    public int func_188216_a() {
        return 808;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("minecraft:shulker".equals(nbttagcompound.func_74779_i("id")) && !nbttagcompound.func_150297_b("Color", 99)) {
            nbttagcompound.func_74774_a("Color", (byte) 10);
        }

        return nbttagcompound;
    }
}

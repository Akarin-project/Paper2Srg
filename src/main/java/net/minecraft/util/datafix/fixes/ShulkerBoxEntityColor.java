package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ShulkerBoxEntityColor implements IFixableData {

    public ShulkerBoxEntityColor() {}

    public int getFixVersion() {
        return 808;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("minecraft:shulker".equals(nbttagcompound.getString("id")) && !nbttagcompound.hasKey("Color", 99)) {
            nbttagcompound.setByte("Color", (byte) 10);
        }

        return nbttagcompound;
    }
}

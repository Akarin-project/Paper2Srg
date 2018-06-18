package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ShulkerBoxTileColor implements IFixableData {

    public ShulkerBoxTileColor() {}

    public int getFixVersion() {
        return 813;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("minecraft:shulker".equals(nbttagcompound.getString("id"))) {
            nbttagcompound.removeTag("Color");
        }

        return nbttagcompound;
    }
}

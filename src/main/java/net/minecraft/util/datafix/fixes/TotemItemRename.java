package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class TotemItemRename implements IFixableData {

    public TotemItemRename() {}

    public int getFixVersion() {
        return 820;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("minecraft:totem".equals(nbttagcompound.getString("id"))) {
            nbttagcompound.setString("id", "minecraft:totem_of_undying");
        }

        return nbttagcompound;
    }
}

package net.minecraft.util.datafix.fixes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class BedItemColor implements IFixableData {

    public BedItemColor() {}

    public int getFixVersion() {
        return 1125;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("minecraft:bed".equals(nbttagcompound.getString("id")) && nbttagcompound.getShort("Damage") == 0) {
            nbttagcompound.setShort("Damage", (short) EnumDyeColor.RED.getMetadata());
        }

        return nbttagcompound;
    }
}

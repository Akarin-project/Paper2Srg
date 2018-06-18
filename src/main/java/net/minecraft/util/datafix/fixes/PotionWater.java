package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class PotionWater implements IFixableData {

    public PotionWater() {}

    public int getFixVersion() {
        return 806;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.getString("id");

        if ("minecraft:potion".equals(s) || "minecraft:splash_potion".equals(s) || "minecraft:lingering_potion".equals(s) || "minecraft:tipped_arrow".equals(s)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("tag");

            if (!nbttagcompound1.hasKey("Potion", 8)) {
                nbttagcompound1.setString("Potion", "minecraft:water");
            }

            if (!nbttagcompound.hasKey("tag", 10)) {
                nbttagcompound.setTag("tag", nbttagcompound1);
            }
        }

        return nbttagcompound;
    }
}

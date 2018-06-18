package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class RedundantChanceTags implements IFixableData {

    public RedundantChanceTags() {}

    public int getFixVersion() {
        return 113;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist;

        if (nbttagcompound.hasKey("HandDropChances", 9)) {
            nbttaglist = nbttagcompound.getTagList("HandDropChances", 5);
            if (nbttaglist.tagCount() == 2 && nbttaglist.getFloatAt(0) == 0.0F && nbttaglist.getFloatAt(1) == 0.0F) {
                nbttagcompound.removeTag("HandDropChances");
            }
        }

        if (nbttagcompound.hasKey("ArmorDropChances", 9)) {
            nbttaglist = nbttagcompound.getTagList("ArmorDropChances", 5);
            if (nbttaglist.tagCount() == 4 && nbttaglist.getFloatAt(0) == 0.0F && nbttaglist.getFloatAt(1) == 0.0F && nbttaglist.getFloatAt(2) == 0.0F && nbttaglist.getFloatAt(3) == 0.0F) {
                nbttagcompound.removeTag("ArmorDropChances");
            }
        }

        return nbttagcompound;
    }
}

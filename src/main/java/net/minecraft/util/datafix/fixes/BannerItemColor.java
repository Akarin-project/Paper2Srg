package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class BannerItemColor implements IFixableData {

    public BannerItemColor() {}

    public int getFixVersion() {
        return 804;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("minecraft:banner".equals(nbttagcompound.getString("id")) && nbttagcompound.hasKey("tag", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("tag");

            if (nbttagcompound1.hasKey("BlockEntityTag", 10)) {
                NBTTagCompound nbttagcompound2 = nbttagcompound1.getCompoundTag("BlockEntityTag");

                if (nbttagcompound2.hasKey("Base", 99)) {
                    nbttagcompound.setShort("Damage", (short) (nbttagcompound2.getShort("Base") & 15));
                    if (nbttagcompound1.hasKey("display", 10)) {
                        NBTTagCompound nbttagcompound3 = nbttagcompound1.getCompoundTag("display");

                        if (nbttagcompound3.hasKey("Lore", 9)) {
                            NBTTagList nbttaglist = nbttagcompound3.getTagList("Lore", 8);

                            if (nbttaglist.tagCount() == 1 && "(+NBT)".equals(nbttaglist.getStringTagAt(0))) {
                                return nbttagcompound;
                            }
                        }
                    }

                    nbttagcompound2.removeTag("Base");
                    if (nbttagcompound2.hasNoTags()) {
                        nbttagcompound1.removeTag("BlockEntityTag");
                    }

                    if (nbttagcompound1.hasNoTags()) {
                        nbttagcompound.removeTag("tag");
                    }
                }
            }
        }

        return nbttagcompound;
    }
}

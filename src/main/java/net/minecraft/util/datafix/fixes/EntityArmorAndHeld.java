package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class EntityArmorAndHeld implements IFixableData {

    public EntityArmorAndHeld() {}

    public int getFixVersion() {
        return 100;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Equipment", 10);
        NBTTagList nbttaglist1;

        if (!nbttaglist.hasNoTags() && !nbttagcompound.hasKey("HandItems", 10)) {
            nbttaglist1 = new NBTTagList();
            nbttaglist1.appendTag(nbttaglist.get(0));
            nbttaglist1.appendTag(new NBTTagCompound());
            nbttagcompound.setTag("HandItems", nbttaglist1);
        }

        if (nbttaglist.tagCount() > 1 && !nbttagcompound.hasKey("ArmorItem", 10)) {
            nbttaglist1 = new NBTTagList();
            nbttaglist1.appendTag(nbttaglist.getCompoundTagAt(1));
            nbttaglist1.appendTag(nbttaglist.getCompoundTagAt(2));
            nbttaglist1.appendTag(nbttaglist.getCompoundTagAt(3));
            nbttaglist1.appendTag(nbttaglist.getCompoundTagAt(4));
            nbttagcompound.setTag("ArmorItems", nbttaglist1);
        }

        nbttagcompound.removeTag("Equipment");
        if (nbttagcompound.hasKey("DropChances", 9)) {
            nbttaglist1 = nbttagcompound.getTagList("DropChances", 5);
            NBTTagList nbttaglist2;

            if (!nbttagcompound.hasKey("HandDropChances", 10)) {
                nbttaglist2 = new NBTTagList();
                nbttaglist2.appendTag(new NBTTagFloat(nbttaglist1.getFloatAt(0)));
                nbttaglist2.appendTag(new NBTTagFloat(0.0F));
                nbttagcompound.setTag("HandDropChances", nbttaglist2);
            }

            if (!nbttagcompound.hasKey("ArmorDropChances", 10)) {
                nbttaglist2 = new NBTTagList();
                nbttaglist2.appendTag(new NBTTagFloat(nbttaglist1.getFloatAt(1)));
                nbttaglist2.appendTag(new NBTTagFloat(nbttaglist1.getFloatAt(2)));
                nbttaglist2.appendTag(new NBTTagFloat(nbttaglist1.getFloatAt(3)));
                nbttaglist2.appendTag(new NBTTagFloat(nbttaglist1.getFloatAt(4)));
                nbttagcompound.setTag("ArmorDropChances", nbttaglist2);
            }

            nbttagcompound.removeTag("DropChances");
        }

        return nbttagcompound;
    }
}

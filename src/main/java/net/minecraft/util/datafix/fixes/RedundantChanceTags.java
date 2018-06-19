package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class RedundantChanceTags implements IFixableData {

    public RedundantChanceTags() {}

    public int func_188216_a() {
        return 113;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist;

        if (nbttagcompound.func_150297_b("HandDropChances", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("HandDropChances", 5);
            if (nbttaglist.func_74745_c() == 2 && nbttaglist.func_150308_e(0) == 0.0F && nbttaglist.func_150308_e(1) == 0.0F) {
                nbttagcompound.func_82580_o("HandDropChances");
            }
        }

        if (nbttagcompound.func_150297_b("ArmorDropChances", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("ArmorDropChances", 5);
            if (nbttaglist.func_74745_c() == 4 && nbttaglist.func_150308_e(0) == 0.0F && nbttaglist.func_150308_e(1) == 0.0F && nbttaglist.func_150308_e(2) == 0.0F && nbttaglist.func_150308_e(3) == 0.0F) {
                nbttagcompound.func_82580_o("ArmorDropChances");
            }
        }

        return nbttagcompound;
    }
}

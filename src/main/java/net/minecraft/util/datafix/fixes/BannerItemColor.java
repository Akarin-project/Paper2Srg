package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class BannerItemColor implements IFixableData {

    public BannerItemColor() {}

    public int func_188216_a() {
        return 804;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("minecraft:banner".equals(nbttagcompound.func_74779_i("id")) && nbttagcompound.func_150297_b("tag", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("tag");

            if (nbttagcompound1.func_150297_b("BlockEntityTag", 10)) {
                NBTTagCompound nbttagcompound2 = nbttagcompound1.func_74775_l("BlockEntityTag");

                if (nbttagcompound2.func_150297_b("Base", 99)) {
                    nbttagcompound.func_74777_a("Damage", (short) (nbttagcompound2.func_74765_d("Base") & 15));
                    if (nbttagcompound1.func_150297_b("display", 10)) {
                        NBTTagCompound nbttagcompound3 = nbttagcompound1.func_74775_l("display");

                        if (nbttagcompound3.func_150297_b("Lore", 9)) {
                            NBTTagList nbttaglist = nbttagcompound3.func_150295_c("Lore", 8);

                            if (nbttaglist.func_74745_c() == 1 && "(+NBT)".equals(nbttaglist.func_150307_f(0))) {
                                return nbttagcompound;
                            }
                        }
                    }

                    nbttagcompound2.func_82580_o("Base");
                    if (nbttagcompound2.func_82582_d()) {
                        nbttagcompound1.func_82580_o("BlockEntityTag");
                    }

                    if (nbttagcompound1.func_82582_d()) {
                        nbttagcompound.func_82580_o("tag");
                    }
                }
            }
        }

        return nbttagcompound;
    }
}

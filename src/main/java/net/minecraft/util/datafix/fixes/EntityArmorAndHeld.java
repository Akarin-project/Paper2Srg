package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class EntityArmorAndHeld implements IFixableData {

    public EntityArmorAndHeld() {}

    public int func_188216_a() {
        return 100;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Equipment", 10);
        NBTTagList nbttaglist1;

        if (!nbttaglist.func_82582_d() && !nbttagcompound.func_150297_b("HandItems", 10)) {
            nbttaglist1 = new NBTTagList();
            nbttaglist1.func_74742_a(nbttaglist.func_179238_g(0));
            nbttaglist1.func_74742_a(new NBTTagCompound());
            nbttagcompound.func_74782_a("HandItems", nbttaglist1);
        }

        if (nbttaglist.func_74745_c() > 1 && !nbttagcompound.func_150297_b("ArmorItem", 10)) {
            nbttaglist1 = new NBTTagList();
            nbttaglist1.func_74742_a(nbttaglist.func_150305_b(1));
            nbttaglist1.func_74742_a(nbttaglist.func_150305_b(2));
            nbttaglist1.func_74742_a(nbttaglist.func_150305_b(3));
            nbttaglist1.func_74742_a(nbttaglist.func_150305_b(4));
            nbttagcompound.func_74782_a("ArmorItems", nbttaglist1);
        }

        nbttagcompound.func_82580_o("Equipment");
        if (nbttagcompound.func_150297_b("DropChances", 9)) {
            nbttaglist1 = nbttagcompound.func_150295_c("DropChances", 5);
            NBTTagList nbttaglist2;

            if (!nbttagcompound.func_150297_b("HandDropChances", 10)) {
                nbttaglist2 = new NBTTagList();
                nbttaglist2.func_74742_a(new NBTTagFloat(nbttaglist1.func_150308_e(0)));
                nbttaglist2.func_74742_a(new NBTTagFloat(0.0F));
                nbttagcompound.func_74782_a("HandDropChances", nbttaglist2);
            }

            if (!nbttagcompound.func_150297_b("ArmorDropChances", 10)) {
                nbttaglist2 = new NBTTagList();
                nbttaglist2.func_74742_a(new NBTTagFloat(nbttaglist1.func_150308_e(1)));
                nbttaglist2.func_74742_a(new NBTTagFloat(nbttaglist1.func_150308_e(2)));
                nbttaglist2.func_74742_a(new NBTTagFloat(nbttaglist1.func_150308_e(3)));
                nbttaglist2.func_74742_a(new NBTTagFloat(nbttaglist1.func_150308_e(4)));
                nbttagcompound.func_74782_a("ArmorDropChances", nbttaglist2);
            }

            nbttagcompound.func_82580_o("DropChances");
        }

        return nbttagcompound;
    }
}

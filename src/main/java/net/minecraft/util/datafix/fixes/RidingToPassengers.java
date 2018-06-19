package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class RidingToPassengers implements IFixableData {

    public RidingToPassengers() {}

    public int func_188216_a() {
        return 135;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        while (nbttagcompound.func_150297_b("Riding", 10)) {
            NBTTagCompound nbttagcompound1 = this.func_188220_b(nbttagcompound);

            this.func_188219_a(nbttagcompound, nbttagcompound1);
            nbttagcompound = nbttagcompound1;
        }

        return nbttagcompound;
    }

    protected void func_188219_a(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        NBTTagList nbttaglist = new NBTTagList();

        nbttaglist.func_74742_a(nbttagcompound);
        nbttagcompound1.func_74782_a("Passengers", nbttaglist);
    }

    protected NBTTagCompound func_188220_b(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Riding");

        nbttagcompound.func_82580_o("Riding");
        return nbttagcompound1;
    }
}

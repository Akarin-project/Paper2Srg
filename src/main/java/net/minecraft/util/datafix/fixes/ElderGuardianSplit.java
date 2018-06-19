package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ElderGuardianSplit implements IFixableData {

    public ElderGuardianSplit() {}

    public int func_188216_a() {
        return 700;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("Guardian".equals(nbttagcompound.func_74779_i("id"))) {
            if (nbttagcompound.func_74767_n("Elder")) {
                nbttagcompound.func_74778_a("id", "ElderGuardian");
            }

            nbttagcompound.func_82580_o("Elder");
        }

        return nbttagcompound;
    }
}

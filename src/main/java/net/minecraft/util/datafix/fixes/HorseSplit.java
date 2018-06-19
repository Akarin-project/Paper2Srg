package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class HorseSplit implements IFixableData {

    public HorseSplit() {}

    public int func_188216_a() {
        return 703;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("EntityHorse".equals(nbttagcompound.func_74779_i("id"))) {
            int i = nbttagcompound.func_74762_e("Type");

            switch (i) {
            case 0:
            default:
                nbttagcompound.func_74778_a("id", "Horse");
                break;

            case 1:
                nbttagcompound.func_74778_a("id", "Donkey");
                break;

            case 2:
                nbttagcompound.func_74778_a("id", "Mule");
                break;

            case 3:
                nbttagcompound.func_74778_a("id", "ZombieHorse");
                break;

            case 4:
                nbttagcompound.func_74778_a("id", "SkeletonHorse");
            }

            nbttagcompound.func_82580_o("Type");
        }

        return nbttagcompound;
    }
}

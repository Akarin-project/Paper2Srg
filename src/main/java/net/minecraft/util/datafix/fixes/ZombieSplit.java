package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ZombieSplit implements IFixableData {

    public ZombieSplit() {}

    public int func_188216_a() {
        return 702;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("Zombie".equals(nbttagcompound.func_74779_i("id"))) {
            int i = nbttagcompound.func_74762_e("ZombieType");

            switch (i) {
            case 0:
            default:
                break;

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                nbttagcompound.func_74778_a("id", "ZombieVillager");
                nbttagcompound.func_74768_a("Profession", i - 1);
                break;

            case 6:
                nbttagcompound.func_74778_a("id", "Husk");
            }

            nbttagcompound.func_82580_o("ZombieType");
        }

        return nbttagcompound;
    }
}

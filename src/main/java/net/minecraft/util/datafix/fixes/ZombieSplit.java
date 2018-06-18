package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ZombieSplit implements IFixableData {

    public ZombieSplit() {}

    public int getFixVersion() {
        return 702;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("Zombie".equals(nbttagcompound.getString("id"))) {
            int i = nbttagcompound.getInteger("ZombieType");

            switch (i) {
            case 0:
            default:
                break;

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                nbttagcompound.setString("id", "ZombieVillager");
                nbttagcompound.setInteger("Profession", i - 1);
                break;

            case 6:
                nbttagcompound.setString("id", "Husk");
            }

            nbttagcompound.removeTag("ZombieType");
        }

        return nbttagcompound;
    }
}

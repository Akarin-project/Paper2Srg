package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class HorseSplit implements IFixableData {

    public HorseSplit() {}

    public int getFixVersion() {
        return 703;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("EntityHorse".equals(nbttagcompound.getString("id"))) {
            int i = nbttagcompound.getInteger("Type");

            switch (i) {
            case 0:
            default:
                nbttagcompound.setString("id", "Horse");
                break;

            case 1:
                nbttagcompound.setString("id", "Donkey");
                break;

            case 2:
                nbttagcompound.setString("id", "Mule");
                break;

            case 3:
                nbttagcompound.setString("id", "ZombieHorse");
                break;

            case 4:
                nbttagcompound.setString("id", "SkeletonHorse");
            }

            nbttagcompound.removeTag("Type");
        }

        return nbttagcompound;
    }
}
